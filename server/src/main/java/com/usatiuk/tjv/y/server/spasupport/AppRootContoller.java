package com.usatiuk.tjv.y.server.spasupport;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping(value = "/app", produces = MediaType.TEXT_HTML_VALUE)
@Profile("prod")
class AppRootContoller {
    private final File indexFile;

    AppRootContoller(@Value("${webdatadir}") String rootPath) {
        this.indexFile = new File(rootPath, "index.html");
        if (!this.indexFile.exists() || this.indexFile.isDirectory())
            throw new IllegalArgumentException("index.html doesn't exist!");
    }

    @GetMapping
    public String get() throws IOException {
        return Files.readString(indexFile.toPath());
    }

    @GetMapping("/")
    public String getSlash() throws IOException {
        return Files.readString(indexFile.toPath());
    }
}
