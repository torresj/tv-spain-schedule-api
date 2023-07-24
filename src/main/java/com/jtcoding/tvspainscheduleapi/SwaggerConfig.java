package com.jtcoding.tvspainscheduleapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.parameters.Parameter;
import lombok.AllArgsConstructor;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class SwaggerConfig {

  @Value("${info.app.version}")
  private final String version;

  @Bean
  public OpenAPI springOpenAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title("TV Spain schedule API")
                .description(
                    "TV Spain schedule API to get movies and series from spain channels from TVGuia and rates from TMDB")
                .version(version)
                .license(
                    new License()
                        .name("GNU General Public License V3.0")
                        .url("https://www.gnu.org/licenses/gpl-3.0.html")));
  }
}
