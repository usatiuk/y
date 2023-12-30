package com.usatiuk.tjv.y.server;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.ResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    static class AppResourceResolver implements ResourceResolver {
        @Override
        public Resource resolveResource(HttpServletRequest request, String requestPath, List<? extends Resource> locations, ResourceResolverChain chain) {
            FileSystemResource res = new FileSystemResource("/usr/src/app/client/dist/" + requestPath);
            if (res.exists()) return res;
            FileSystemResource indexRes = new FileSystemResource("/usr/src/app/client/dist/index.html");
            if (indexRes.exists()) return indexRes;
            return null;
        }

        @Override
        public String resolveUrlPath(String resourcePath, List<? extends Resource> locations, ResourceResolverChain chain) {
            return null;
        }
    }


    @RestController
    @RequestMapping(value = "/app", produces = MediaType.TEXT_HTML_VALUE)
    static class AppRootContoller {
        @GetMapping
        public String get() throws IOException {
            return Files.readString(Path.of("/usr/src/app/client/dist/index.html"));
        }

        @GetMapping("/")
        public String getSlash() throws IOException {
            return Files.readString(Path.of("/usr/src/app/client/dist/index.html"));
        }
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/app");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/app/**")
                .setCacheControl(CacheControl.maxAge(Duration.ofDays(365)))
                .resourceChain(true)
                .addResolver(new AppResourceResolver());

    }
}