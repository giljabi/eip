package kr.giljabi.eip.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

/**
 * @Author : eahn.park@gmail.com
 */
public class CommonUtils {
    public static String DEFAULT_TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    public static String UUID_COOKIE_NAME = "EIPclientUUID";

    public static String getCurrentTime(String format) {
        if (format == null || format.isEmpty()) {
            format = DEFAULT_TIMESTAMP_FORMAT;
        }
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        String formattedDateTime = currentDateTime.format(formatter);
        return formattedDateTime;
    }

    public static String getLocalIp() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            return inetAddress.getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    // 쿠키에서 특정 이름의 쿠키 값을 가져오는 메서드
    public static String getCookieValue(HttpServletRequest request, String name) {
        if (request.getCookies() != null) {
            return Arrays.stream(request.getCookies())
                    .filter(cookie -> name.equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
}