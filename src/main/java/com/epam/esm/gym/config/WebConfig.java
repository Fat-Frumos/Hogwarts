package com.epam.esm.gym.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.format.DateTimeFormatter;

/**
 * Configuration class for web-related settings and MVC configuration.
 *
 * <p>This class configures settings related to web MVC, such as view resolvers,
 * resource handling, and other web-specific configurations. It implements the
 * {@code WebMvcConfigurer} interface to customize Spring MVC settings.</p>
 *
 * <p>Web configuration is essential for defining how the web layer of the
 * application interacts with clients and handles web requests.</p>
 *
 * @author Pavlo Poliak
 * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurer
 * @see org.springframework.web.servlet.config.annotation.EnableWebMvc
 * @since 1.0
 */
@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configures resource handlers for serving Swagger UI and webjars.
     *
     * <p>This method sets up the necessary handlers to serve the Swagger UI HTML page and static resources
     * such as JavaScript libraries and CSS files from the classpath. It adds a resource handler for the
     * Swagger UI at "swagger-ui.html" and maps it to the location "classpath:/META-INF/resources/".
     * Additionally, it configures another handler for "/webjars/**" to serve webjar resources from the
     * "classpath:/META-INF/resources/webjars/" location.</p>
     *
     * @param registry the {@link ResourceHandlerRegistry} to configure
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * Configures a {@link MappingJackson2HttpMessageConverter} bean with custom settings.
     *
     * <p>This method creates a {@link MappingJackson2HttpMessageConverter} with a {@link Jackson2ObjectMapperBuilder}
     * that disables the serialization of dates as timestamps
     * and sets a custom serializer for {@link java.time.LocalDateTime}.
     * The converter also excludes null values from JSON serialization to reduce payload size.
     * This bean is used to customize the JSON serialization and deserialization process in the application.</p>
     *
     * @return a {@link MappingJackson2HttpMessageConverter} configured with custom settings
     */
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        Jackson2ObjectMapperBuilder builder =
                new Jackson2ObjectMapperBuilder()
                        .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                        .serializers(
                                new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                        .serializationInclusion(JsonInclude.Include.NON_NULL);
        return new MappingJackson2HttpMessageConverter(builder.build());
    }

    /**
     * Creates a {@link RestTemplate} bean for making HTTP requests.
     *
     * <p>This method provides a simple {@link RestTemplate} instance configured with default settings.
     * It is used to perform RESTful web service calls from the application. The {@link RestTemplate} bean
     * is available for injection into other components that need to make HTTP requests. Customizations and
     * further configurations can be applied to this instance if needed.</p>
     *
     * @return a {@link RestTemplate} instance for making HTTP requests
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
