package pl.edu.agh.gma.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Slf4j
public class SwaggerConfiguration {

  @Value("${app.url.backendBase}")
  public String baseUrl;

  @Value("${app.url.swagger}")
  public String swaggerUrl;

  @Bean
  public Docket SInGApi() {
    log.info("Rest Api documentation at: " + baseUrl + swaggerUrl);
    return new Docket(DocumentationType.SWAGGER_2).select()
        .apis(RequestHandlerSelectors.any())
        .paths(PathSelectors.any())
        .build()
        .apiInfo(getApiInfo());
  }

  private ApiInfo getApiInfo() {
    return new ApiInfoBuilder().title("Social interactions games")
        .version("1.0.0")
        .description("API Social interactions games system")
        .contact(new Contact("Mateusz Najdek", "", "najdek@agh.edu.pl"))
        .license("@copyright")
        .build();
  }
}
