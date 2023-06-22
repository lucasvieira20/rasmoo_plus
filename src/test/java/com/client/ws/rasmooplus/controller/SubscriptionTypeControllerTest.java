package com.client.ws.rasmooplus.controller;

import com.client.ws.rasmooplus.model.jpa.SubscriptionType;
import com.client.ws.rasmooplus.service.SubscriptionTypeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@AutoConfigureDataJpa
@AutoConfigureTestDatabase
@WebMvcTest(SubscriptionTypeController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class SubscriptionTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubscriptionTypeService subscriptionTypeService;

    @Test
    void given_findAll_then_returnAllSubscriptionType() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/subscription-type"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void given_findById_whenGetId2_then_returnOnSuscriptionType() throws Exception {
        SubscriptionType subscriptionType = new SubscriptionType(
                2L,
                "VITALICIO",
                null,
                BigDecimal.valueOf(997),
                "FOREVER22"
        );

        Mockito.when(subscriptionTypeService.findById(2L)).thenReturn(subscriptionType);

        mockMvc.perform(MockMvcRequestBuilders.get("/subscription-type/2"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", org.hamcrest.Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", org.hamcrest.Matchers.is("VITALICIO")))
        ;
    }

    @Test
    void given_delete_whenGetId2_then_noReturnAndNoContent() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/subscription-type/{id}", 2L))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(subscriptionTypeService, Mockito.times(1)).delete(2L);
    }
}
