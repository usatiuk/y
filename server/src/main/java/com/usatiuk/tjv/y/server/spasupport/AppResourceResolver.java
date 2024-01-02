package com.usatiuk.tjv.y.server.spasupport;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.resource.ResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

@Component
@Profile("prod")
public class AppResourceResolver implements ResourceResolver {
    private final String rootPath;

    AppResourceResolver(@Value("${webdatadir}") String rootPath) {
        this.rootPath = rootPath;

        var indexFile = new File(rootPath, "index.html");
        if (!indexFile.exists() || indexFile.isDirectory())
            throw new IllegalArgumentException("index.html doesn't exist!");

    }

    private Resource resourceResourceImpl(String requestPath) {
        FileSystemResource res = new FileSystemResource(Paths.get(rootPath, requestPath));
        if (res.exists()) return res;
        FileSystemResource indexRes = new FileSystemResource(Paths.get(rootPath, "index.html"));
        if (indexRes.exists()) return indexRes;
        return null;
    }

    @Override
    public Resource resolveResource(HttpServletRequest request, String requestPath, List<? extends Resource> locations, ResourceResolverChain chain) {
        return resourceResourceImpl(requestPath);
    }

    @Override
    public String resolveUrlPath(String resourcePath, List<? extends Resource> locations, ResourceResolverChain chain) {
        if (resourceResourceImpl(resourcePath) != null)
            return resourcePath;
        return null;
    }
}
