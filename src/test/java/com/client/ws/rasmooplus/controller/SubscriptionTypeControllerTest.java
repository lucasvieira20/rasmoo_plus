package com.client.ws.rasmooplus.controller;

import com.client.ws.rasmooplus.dto.SubscriptionTypeDto;
import com.client.ws.rasmooplus.model.jpa.SubscriptionType;
import com.client.ws.rasmooplus.service.SubscriptionTypeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
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
    private ObjectMapper objectMapper;

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

    @Test
    void given_create_whenDtoIsOk_then_returnOnSuscriptionTypeCreated() throws Exception {
        SubscriptionType subscriptionType = new SubscriptionType(
                2L,
                "VITALICIO",
                null,
                BigDecimal.valueOf(997),
                "FOREVER22"
        );

        SubscriptionTypeDto subscriptionTypeDto = new SubscriptionTypeDto(
                null,
                "VITALICIO",
                null,
                BigDecimal.valueOf(997),
                "FOREVER22"
        );


        Mockito.when(subscriptionTypeService.create(subscriptionTypeDto)).thenReturn(subscriptionType);

        mockMvc.perform(MockMvcRequestBuilders.post("/subscription-type")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(subscriptionTypeDto)))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", org.hamcrest.Matchers.is(2)));
    }

    @Test
    void given_create_whenDtoIsMissinValues_then_returnBadRequest() throws Exception {

        SubscriptionTypeDto subscriptionTypeDto = new SubscriptionTypeDto(
                null,
                "",
                13L,
                null,
                "22"
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/subscription-type")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(subscriptionTypeDto)))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", org.hamcrest.Matchers.is(
                "[price=Campo price não pode ser nulo, accessMonths=Campo accessMonth não pode ser maior que 12, name=Campo productKey deve ter entre 5 e 40 caracteres, productKey=Campo productKey deve ter entre 5 e 15 caracteres]"
                )))
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus", org.hamcrest.Matchers.is("BAD_REQUEST")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode", org.hamcrest.Matchers.is(400)));

        Mockito.verify(subscriptionTypeService, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    void given_update_whenDtoIsOk_then_returnOnSuscriptionTypeUpdated() throws Exception {
        SubscriptionType subscriptionType = new SubscriptionType(
                2L,
                "VITALICIO",
                null,
                BigDecimal.valueOf(997),
                "FOREVER22"
        );

        SubscriptionTypeDto subscriptionTypeDto = new SubscriptionTypeDto(
                2L,
                "VITALICIO",
                null,
                BigDecimal.valueOf(997),
                "FOREVER22"
        );


        Mockito.when(subscriptionTypeService.update(2L, subscriptionTypeDto)).thenReturn(subscriptionType);

        mockMvc.perform(MockMvcRequestBuilders.put("/subscription-type/2")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(subscriptionTypeDto)))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", org.hamcrest.Matchers.is(2)));
    }

    @Test
    void given_update_whenDtoIsMissinValues_then_returnBadRequest() throws Exception {

        SubscriptionTypeDto subscriptionTypeDto = new SubscriptionTypeDto(
                null,
                "",
                13L,
                null,
                "22"
        );

        mockMvc.perform(MockMvcRequestBuilders.put("/subscription-type/2")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(subscriptionTypeDto)))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.message", org.hamcrest.Matchers.is("[price=Campo price não pode ser nulo, accessMonths=Campo accessMonth não pode ser maior que 12, name=Campo productKey deve ter entre 5 e 40 caracteres, productKey=Campo productKey deve ter entre 5 e 15 caracteres]")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus", org.hamcrest.Matchers.is("BAD_REQUEST")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode", org.hamcrest.Matchers.is(400)));

        Mockito.verify(subscriptionTypeService, Mockito.times(0)).update(Mockito.any(), Mockito.any());
    }

    @Test
    void given_update_whenIdIsNull_then_returnBadRequest() throws Exception {

        SubscriptionTypeDto subscriptionTypeDto = new SubscriptionTypeDto(
                null,
                "",
                13L,
                null,
                "22"
        );

        mockMvc.perform(MockMvcRequestBuilders.put("/subscription-type/")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(subscriptionTypeDto)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        Mockito.verify(subscriptionTypeService, Mockito.times(0)).update(Mockito.any(), Mockito.any());
    }

}
