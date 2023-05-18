package com.client.ws.rasmooplus.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest

public class UserServiceTest {

    @Autowired
    private UserDetailsService userDetailsService;

    @Test
    void contextLoads() {

        userDetailsService.sendRecoveryCode("email@teste.com");

    }

}
