package com.mercadolibre.ipinfo.interceptor;

import com.mercadolibre.ipinfo.exception.IpAccessDeniedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Interceptor que va a tener la logica para permitir o no el paso de distintas ip a un endpoint
 * Actualmente las ip denegadas estan configuradas en el archivo .properties
 * Esto no es lo idea porque si queremos agregar ips tenemos que reiniciar la aplicacion, se podrian cargar
 * en una base de datos y validar si la ip consulta existe en la tabla.
 */
@Component
public class IpDenyInterceptor implements HandlerInterceptor {

    /**
     * La lista de IPs podria ser obtenida desde una base de datos
     */
    @Value("#{'${ip.denies.list}'.split(',')}")
    private List<String> ipDenyList;

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<String, String> pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        String ip = pathVariables.get("ip");

        if (ip != null && ipDenyList != null && ipDenyList.contains(ip)) {
            throw new IpAccessDeniedException("La ip no cuenta con permisos para acceder al endpoint");
        }
        return true;
    }
}