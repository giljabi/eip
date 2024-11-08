package kr.giljabi.eip.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Swagger 설정
 * 어떤 controller들이 있는지 헷갈릴때 사용
 * /swagger-ui.html
 * /swagger-ui/index.html
 */
@Configuration
@Profile("local")  // local 프로필에서만 활성화
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI(@Value("${giljabi.doc.version}") String springdocVersion) {
		Info info = new Info()
				.title("My Quizzz API")
				.version(springdocVersion)
				.description("API 소개");

		OpenAPI openApi = new OpenAPI()
				.info(info);

		return openApi;
	}
}

