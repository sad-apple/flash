package com.authorization.controller;

import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zsp
 * @date 2023/7/21 13:40
 */

@RestController
@RequestMapping("/client")
public class ClientController {

    @PostMapping("")
    public boolean save(@RequestBody RegisteredClient registeredClient) {
        return false;
    }
}
