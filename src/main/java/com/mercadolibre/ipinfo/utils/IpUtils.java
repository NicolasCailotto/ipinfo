package com.mercadolibre.ipinfo.utils;

/**
 * Clase util donde se deberan poner metodos de ayuda para logica especifica de la aplicacion en general
 */
public class IpUtils {

    /**
     * Este metodo se encarga de validar si la ip recibida tiene un formato valido
     *
     * @param ip
     * @return
     */
    public static boolean isValidIp(String ip) {
        try {
            if (ip == null || ip.isEmpty()) {
                return false;
            }

            String[] parts = ip.split("\\.");
            if (parts.length != 4 || ip.endsWith(".")) {
                return false;
            }

            for (String s : parts) {
                int i = Integer.parseInt(s);
                if ((i < 0) || (i > 255)) {
                    return false;
                }
            }

            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
}
