package com.usatiuk.tjv.y.server.spasupport;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@Profile("prod")
// A bit of a mess, but even then it seems to be the simplest way to serve a single page application...
public class WebConfig implements WebMvcConfigurer {
    private final AppResourceResolver appResourceResolver;

    public WebConfig(AppResourceResolver appResourceResolver) {
        this.appResourceResolver = appResourceResolver;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/app");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/app/**")
                .resourceChain(true)
                .addResolver(appResourceResolver);
    }
}