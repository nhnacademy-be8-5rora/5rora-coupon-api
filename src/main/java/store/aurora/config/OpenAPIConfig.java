package store.aurora.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("My API Documentation")
                        .version("1.0")
                        .description("This is the API documentation for my application.")
                        .contact(new Contact()
                                .name("Your Name")
                                .email("your-email@example.com")
                                .url("https://your-website.com")));
    }
}
