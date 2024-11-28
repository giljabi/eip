package kr.giljabi.eip.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${giljabi.eip.filePath}")
    private String filePath;

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + filePath);

        // favicon.ico 무시
        registry.addResourceHandler("/favicon.ico")
		.addResourceLocations("classpath:/static/");
    }

}
