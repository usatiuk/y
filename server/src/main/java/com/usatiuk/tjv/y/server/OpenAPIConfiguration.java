package com.usatiuk.tjv.y.server;

import com.usatiuk.tjv.y.server.dto.ErrorTo;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Y API", version = "v1"), security = {@SecurityRequirement(name = "bearerAuth")})
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenAPIConfiguration {
    private ApiResponse createApiResponse(String message, Schema schema) {
        MediaType mediaType = new MediaType();
        mediaType.schema(schema);
        return new ApiResponse().description(message)
                .content(new Content().addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE, mediaType));
    }

    @Bean
    public OpenApiCustomizer customizer() {
        return openApi -> {
            Schema errorResponseSchema = ModelConverters.getInstance()
                    .resolveAsResolvedSchema(new AnnotatedType(ErrorTo.class)).schema;
            openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> {
                var apiResponses = operation.getResponses();
                apiResponses.addApiResponse("500", createApiResponse("Server Error", errorResponseSchema));
            }));
        };


    }
}
