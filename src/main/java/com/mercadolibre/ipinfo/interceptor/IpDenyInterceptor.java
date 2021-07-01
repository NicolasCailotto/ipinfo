package com.mercadolibre.ipinfo.interceptor;

import com.mercadolibre.ipinfo.exception.IpAccessDeniedException;
import com.mercadolibre.ipinfo.repository.BannedIpRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Interceptor que va a tener la logica para permitir o no el paso de distintas ip a un endpoint
 */
@Component
public class IpDenyInterceptor implements HandlerInterceptor {

    private BannedIpRepository bannedIpRepository;

    public IpDenyInterceptor(BannedIpRepository bannedIpRepository) {
        this.bannedIpRepository = bannedIpRepository;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<String, String> pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        String ip = pathVariables.get("ip");

        if (ip != null && bannedIpRepository.findByIpAddress(ip).isPresent()) {
            throw new IpAccessDeniedException("La ip no cuenta con permisos para acceder al endpoint");
        }
        return true;
    }
}