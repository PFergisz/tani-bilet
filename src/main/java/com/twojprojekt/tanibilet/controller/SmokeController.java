package com.twojprojekt.tanibilet.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SmokeController {

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @GetMapping("/smoke")
    public String smoke() {return "OK"; }
}
