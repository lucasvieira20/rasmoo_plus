package com.client.ws.rasmooplus.controller;

import com.client.ws.rasmooplus.model.jpa.UserType;
import com.client.ws.rasmooplus.service.UserTypeService;
import com.client.ws.rasmooplus.service.implementations.UserTypeServiceImplementation;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureDataJpa
@AutoConfigureTestDatabase
@WebMvcTest(UserTypeController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class UserTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserTypeService userTypeSerivce;

    @Test
    void given_findAll_then_returnAllUserType() throws Exception {
        List<UserType> userTypeList = new ArrayList<>();
        UserType userType1 = new UserType(1l, "Professor", "Professor da Plataforma");
        UserType userType2 = new UserType(2l, "Administrador", "Funcionário da Plataforma");

        userTypeList.add(userType1);
        userTypeList.add(userType2);

        Mockito.when(userTypeSerivce.findAll()).thenReturn(userTypeList);

        mockMvc.perform(MockMvcRequestBuilders.get("/user-type"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}
