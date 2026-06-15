package utn.programacion3.agencia_de_autos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI configurarOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Agencia de autos")
                        .version("1.0")
                        .description("Documentacion de una API REST de una Agencia de autos con OpenAPI y Spring Boot "));
    }
}
