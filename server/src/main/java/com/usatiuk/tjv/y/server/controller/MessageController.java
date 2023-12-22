package com.usatiuk.tjv.y.server.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/message", produces = MediaType.APPLICATION_JSON_VALUE)
public class MessageController {
}
