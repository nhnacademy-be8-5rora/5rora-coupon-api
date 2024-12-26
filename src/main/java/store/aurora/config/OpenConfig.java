package store.aurora.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class OpenConfig {

    private static final String API_NAME = "Coupon-Api";
    private static final String API_VERSION = "1.0.0";
    private static final String API_DESCRIPTION = "쿠폰 관리 생성 shop api";

    @Bean
    @Primary
    public OpenAPI openConfig() {
        return new OpenAPI()
                .info(new Info().title(API_NAME).description(API_DESCRIPTION).version(API_VERSION));
    }
}
