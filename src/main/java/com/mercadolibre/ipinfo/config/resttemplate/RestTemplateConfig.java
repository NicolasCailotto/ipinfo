package com.mercadolibre.ipinfo.config.resttemplate;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.concurrent.TimeUnit;

/**
 * Esta clase se encarga de crear un rest template generico e insertalo en el contexto de spring
 * Adicionalmente se podria configurar el RestTemplate para que funcione con un pool de conexiones, de esa forma no
 * crearia conexiones cada vez que se desee usar.
 */
@Configuration
public class RestTemplateConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateConfig.class);


    @Value("${ipapi.service.endpoint}")
    private String ipApiEndpoint;

    @Value("${restcountry.service.endpoint}")
    private String restCountryApiEndpoint;

    @Value("${fixer.service.endpoint}")
    private String fixerApiEnpoint;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder, HttpComponentsClientHttpRequestFactory clientHttpRequestFactory) {
        return builder.requestFactory(clientHttpRequestFactory.getClass()).build();
    }

    /**
     * Creacion del connection manager mediante un pool
     *
     * @return PoolingHttpClientConnectionManager
     */
    @Bean
    public PoolingHttpClientConnectionManager poolingConnectionManager() {

        PoolingHttpClientConnectionManager poolingConnectionManager = new PoolingHttpClientConnectionManager();

        // set a total amount of connections across all HTTP routes
        poolingConnectionManager.setMaxTotal(150);

        // set a maximum amount of connections for each HTTP route in pool
        poolingConnectionManager.setDefaultMaxPerRoute(20);

        setMaxConnectionPerRoute(poolingConnectionManager, ipApiEndpoint, 100);
        setMaxConnectionPerRoute(poolingConnectionManager, restCountryApiEndpoint, 20);
        setMaxConnectionPerRoute(poolingConnectionManager, fixerApiEnpoint, 20);

        return poolingConnectionManager;
    }

    /**
     * Este metodo se encarga de configurar las conecciones maximas para un host especifico
     *
     * @param poolingConnectionManager connection manager
     * @param host                     host
     * @param maxConnection            maximas conecciones para el host
     */
    private void setMaxConnectionPerRoute(PoolingHttpClientConnectionManager poolingConnectionManager, String host, int maxConnection) {
        URI esbURI = URI.create(host);

        HttpHost esb = new HttpHost(esbURI.getHost(), esbURI.getPort());

        poolingConnectionManager.setMaxPerRoute(new HttpRoute(esb), maxConnection);
    }

    /**
     * Definicion de la estrategia keep alive
     * Esta estrategia define el tiempo que tiene que pasar para que una conexion deje de poder ser usada
     *
     * @return ConnectionKeepAliveStrategy
     */
    @Bean
    public ConnectionKeepAliveStrategy connectionKeepAliveStrategy() {
        return (httpResponse, httpContext) -> {
            HeaderIterator headerIterator = httpResponse.headerIterator(HTTP.CONN_KEEP_ALIVE);
            HeaderElementIterator elementIterator = new BasicHeaderElementIterator(headerIterator);

            while (elementIterator.hasNext()) {
                HeaderElement element = elementIterator.nextElement();
                String param = element.getName();
                String value = element.getValue();
                if (value != null && param.equalsIgnoreCase("timeout")) {
                    return Long.parseLong(value) * 1000; // convert to ms
                }
            }

            return HttpClientConfigConstants.DEFAULT_KEEP_ALIVE_TIME;
        };
    }

    /**
     * Creacion del httpclient con configuracion de timeout, asignacion de keepalive strategy y connectionmanager
     *
     * @return
     */
    @Bean
    public CloseableHttpClient httpClient(PoolingHttpClientConnectionManager poolingHttpClientConnectionManager,
                                          ConnectionKeepAliveStrategy connectionKeepAliveStrategy) {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(HttpClientConfigConstants.CONNECTION_TIMEOUT)
                .setConnectionRequestTimeout(HttpClientConfigConstants.REQUEST_TIMEOUT)
                .setSocketTimeout(HttpClientConfigConstants.SOCKET_TIMEOUT)
                .build();
        return HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(poolingHttpClientConnectionManager)
                .setKeepAliveStrategy(connectionKeepAliveStrategy)
                .build();
    }

    @Bean
    public HttpComponentsClientHttpRequestFactory clientHttpRequestFactory(CloseableHttpClient closeableHttpClient) {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setHttpClient(closeableHttpClient);
        return clientHttpRequestFactory;
    }

    /**
     * Job que se ejecuta cada cierta cantidad de tiempo limpiando las conexiones que ya no son validas
     *
     * @param pool
     */
    @Bean
    @Scheduled(fixedDelay = 20000)
    public void idleConnectionManagerJob(PoolingHttpClientConnectionManager pool) {
        if (pool != null) {
            pool.closeExpiredConnections();
            pool.closeIdleConnections(HttpClientConfigConstants.IDLE_CONNECTION_WAIT_TIME, TimeUnit.MILLISECONDS);

            LOGGER.info("Cerrando conexiones http vencidas e inactivas");
        }
    }
}
