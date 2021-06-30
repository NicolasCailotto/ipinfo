package com.mercadolibre.ipinfo.config.cache;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.ipinfo.controller.response.IpInformationDTO;
import com.mercadolibre.ipinfo.dto.CountryDataDTO;
import com.mercadolibre.ipinfo.dto.CurrencyDataDTO;
import com.mercadolibre.ipinfo.dto.IpDataDTO;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;
import java.util.List;

/**
 * Clase que contiene la configuracion de la cache
 * Esta cache es manejada por Spring, y persistida en un server de Redis.
 * Se eligio persistir la cache en un server Redis para que la data cacheada no dependa de la instancia de la aplicacion
 * Dado el caso de que se caiga la api al volver a levantarse seguira teniendo la info de la cache disponible
 * Esta tambien posibilita que si tenemos varias instancias de la api balanceadas, todas ellas van a acceder a la misma
 * informacion de cache
 *
 * <p>
 * Para el caso de IP_DATA y COUNTRY_DATA el dato proviene de 2 apis que no especifican cada cuanto se actualizan
 * pero al ser datos que no deberian cambiar mucho se elige que se limpie la cache cada 3 horas
 * Para el caso de CURRENCY_DATA el dato proviene de una api que SI especifica que las actualizaciones son cada 1 hora, por este motivo
 * es que se eligio que se limpie cada 1 hora.
 * Para IP_INFO (cache que guarda la respuesta propia de la api) se setea una duracion de 1 hora. Esto se eligio en base
 * a la duracion de las demas cache
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Creacion del bean RedisCacheManager con una configuracion especifica (explicada en otro metodo),
     * ademas se le pasas los parametros de conexion que internamente Spring obtiene de la properties
     *
     * @param connectionFactory
     * @param redisCacheManagerBuilderCustomizers
     * @return
     */
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory, RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizers) {
        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.builder(connectionFactory);
        redisCacheManagerBuilderCustomizers.customize(builder);
        return builder.build();
    }

    /**
     * Creacion del bean RedisCacheManagerBuilderCustomizer con las configuraciones de las distintas
     * secciones de cache que se van a usar. Para cada seccion se crea un Serializer para que se use Jackson y de esa
     * forma no haya errores a la hora de buscar los datos en la cache.
     * Ademas se configura un TTL (time to live) a cada entrada de la cache
     *
     * @return
     */
    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return (builder) -> builder
                .withCacheConfiguration(CacheNames.IP_DATA_CACHE,
                        RedisCacheConfiguration.defaultCacheConfig()
                                .serializeValuesWith(buildIpDataDTOSerializer())
                                .entryTtl(Duration.ofMinutes(180)))
                .withCacheConfiguration(CacheNames.COUNTRY_DATA_CACHE,
                        RedisCacheConfiguration.defaultCacheConfig()
                                .serializeValuesWith(buildCountryDataDTOSerializer())
                                .entryTtl(Duration.ofMinutes(180)))
                .withCacheConfiguration(CacheNames.CURRENCY_DATA_CACHE,
                        RedisCacheConfiguration.defaultCacheConfig()
                                .serializeValuesWith(buildCurrencyDataDTOSerializer())
                                .entryTtl(Duration.ofMinutes(60)))
                .withCacheConfiguration(CacheNames.IP_INFO_CACHE,
                        RedisCacheConfiguration.defaultCacheConfig()
                                .serializeValuesWith(buildIpInformationDTOSerializer())
                                .entryTtl(Duration.ofMinutes(60)));
    }

    private RedisSerializationContext.SerializationPair<IpDataDTO> buildIpDataDTOSerializer() {
        RedisSerializer<IpDataDTO> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(IpDataDTO.class);
        return RedisSerializationContext.SerializationPair
                .fromSerializer(jackson2JsonRedisSerializer);
    }

    private RedisSerializationContext.SerializationPair<CountryDataDTO> buildCountryDataDTOSerializer() {
        RedisSerializer<CountryDataDTO> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(CountryDataDTO.class);
        return RedisSerializationContext.SerializationPair
                .fromSerializer(jackson2JsonRedisSerializer);
    }

    private RedisSerializationContext.SerializationPair<List> buildCurrencyDataDTOSerializer() {
        Jackson2JsonRedisSerializer<List> jackson2JsonRedisSerializerList = new Jackson2JsonRedisSerializer<List>(List.class) {
            @Override
            protected JavaType getJavaType(Class<?> clazz) {
                if (List.class.isAssignableFrom(clazz)) {
                    ObjectMapper mapper = new ObjectMapper();
                    setObjectMapper(mapper);
                    return mapper.getTypeFactory().constructCollectionType(List.class, CurrencyDataDTO.class);
                } else {
                    return super.getJavaType(clazz);
                }
            }
        };
        return RedisSerializationContext.SerializationPair
                .fromSerializer(jackson2JsonRedisSerializerList);
    }

    private RedisSerializationContext.SerializationPair<IpInformationDTO> buildIpInformationDTOSerializer() {
        RedisSerializer<IpInformationDTO> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(IpInformationDTO.class);
        return RedisSerializationContext.SerializationPair
                .fromSerializer(jackson2JsonRedisSerializer);
    }

}