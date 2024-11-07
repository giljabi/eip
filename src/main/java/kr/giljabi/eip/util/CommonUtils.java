package kr.giljabi.eip.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

/**
 * @Author : eahn.park@gmail.com
 */
@Slf4j
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

    public static String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

    public static String getFileExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null && originalFilename.contains(".")) {
            return originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        }
        return ""; // 확장자가 없을 때 빈 문자열 반환
    }

    public static void saveFile(String physicalFilePath, String fileName, MultipartFile file) {
        try {
            if (file != null && !file.isEmpty()) {
                String originalFileName = file.getOriginalFilename();
                if (originalFileName != null && (originalFileName.endsWith(".png") || originalFileName.endsWith(".jpg") || originalFileName.endsWith(".jpeg"))) {
                    Path uploadPath = Paths.get(physicalFilePath);
                    if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                    }
                    Path path = uploadPath.resolve(fileName);
                    file.transferTo(path.toFile());
                    log.info("file saved: {}", path.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
