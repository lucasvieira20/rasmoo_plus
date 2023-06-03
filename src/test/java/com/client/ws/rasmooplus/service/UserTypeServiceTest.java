package com.client.ws.rasmooplus.service;

import com.client.ws.rasmooplus.model.jpa.UserType;
import com.client.ws.rasmooplus.repository.jpa.UserTypeRepository;
import com.client.ws.rasmooplus.service.implementations.UserTypeServiceImplementation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UserTypeServiceTest {

    @Mock
    private UserTypeRepository userTypeRepository;

    @InjectMocks
    private UserTypeServiceImplementation userTypeService;


    /*
    * Given_methodo_when_cenario_then_retorno_esperado
    * */

    @Test
    void given_findAll_when_thereAreDataInDB_then_returnAllData() {

        List<UserType> userTypeList = new ArrayList<>();
        UserType userType1 = new UserType(1l, "Professor", "Professor da Plataforma");
        UserType userType2 = new UserType(2l, "Administrador", "Funcionário da Plataforma");
        UserType userType3 = new UserType(3l, "Aluno", "Aluno da Plataforma");

        userTypeList.add(userType1);
        userTypeList.add(userType2);
        userTypeList.add(userType3);

        Mockito.when(userTypeRepository.findAll()).thenReturn(userTypeList);
        var result = userTypeService.findAll();
        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result).hasSize(3);
    }
}
