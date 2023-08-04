package com.client.ws.rasmooplus.service;

import com.client.ws.rasmooplus.dto.UserDto;
import com.client.ws.rasmooplus.exception.BadRequestException;
import com.client.ws.rasmooplus.exception.NotFoundException;
import com.client.ws.rasmooplus.model.jpa.User;
import com.client.ws.rasmooplus.model.jpa.UserType;
import com.client.ws.rasmooplus.repository.jpa.UserRepository;
import com.client.ws.rasmooplus.repository.jpa.UserTypeRepository;
import com.client.ws.rasmooplus.service.implementations.UserServiceImplementation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private  UserRepository userRepository;

    @Mock
    private  UserTypeRepository userTypeRepository;

    @InjectMocks
    private UserServiceImplementation userService;

    private UserDto dto;

    @BeforeEach
    public void loadUser() {
        dto = new UserDto();
        dto.setId(1L);
        dto.setEmail("felipe@email.com");
        dto.setCpf("11111111111");
        dto.setUserTypeId(1L);
    }

    @Test
    void given_create_when_idIsNotNullAndUserIsFound_then_returnUserCreated(){

        UserType userType = getUserType();

        when(userTypeRepository.findById(1L))
                .thenReturn(Optional.of(userType));

        dto.setId(null);

        User user = getUser(userType);
        doReturn(user).when(userRepository).save(Mockito.any(User.class));

        Assertions.assertEquals(user,userService.create(dto));

        Mockito.verify(userTypeRepository, times(1)).findById(1L);
        Mockito.verify(userRepository, times(1)).save(Mockito.any(User.class));
    }



    @Test
    void given_create_when_idIsNotNull_then_throwBadRequestException() {


        Assertions.assertThrows(BadRequestException.class, () -> userService.create(dto));

        verify(userTypeRepository, times(0)).findById(any());
        verify(userRepository, times(0)).save(any());
    }

    @Test
    void given_create_when_idIsNullAndUserTypeIsNotFound_then_throwNotFoudException(){

        dto.setId(null);
        when(userTypeRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> userService.create(dto));

        verify(userTypeRepository, times(1)).findById(1L);
        verify(userRepository, times(0)).save(any());
    }

    @Test
    void given_uploadPhoto_when_thereIsUserAndFileAndItIsPNGorJPEG_then_updatePhotoAndReturnUser() throws Exception {

        FileInputStream fis = new FileInputStream("src/test/resources/static/jonh-wick.jpeg");
        MockMultipartFile file = new MockMultipartFile("file", "jonh-wick.jpeg", MediaType.MULTIPART_FORM_DATA_VALUE, fis);
        UserType userType = getUserType();
        User user = getUser(userType);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User userReturned = userService.uploadPhoto(1L, file);

        assertNotNull(userReturned);
        assertNotNull(userReturned.getPhoto());
        assertEquals("jonh-wick.jpeg", userReturned.getPhotoName());

        verify(userRepository, times(1)).findById(1L);

    }

    @Test
    void given_downloadPhoto_when_thereIsUserPhoto_then_returnByteArray() throws Exception {

        UserType userType = getUserType();
        User user = getUser(userType);
        user.setPhoto(new byte[0]);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        assertNotNull(userService.downloadPhoto(1L));
        verify(userRepository, times(1)).findById(1L);

    }

    @Test
    void given_downloadPhoto_when_thereIsNoPhoto_then_returBadRequestException() throws Exception {

        UserType userType = getUserType();
        User user = getUser(userType);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        assertThrows(BadRequestException.class, () -> userService.downloadPhoto(1L));
        verify(userRepository, times(1)).findById(1L);

    }

    @Test
    void given_uploadPhoto_when_thereIsUserAndFileAndItIsNotPNGorJPG_then_throwBadRequest() throws Exception {

        FileInputStream fis = new FileInputStream("src/test/resources/static/jonh-wick.jpeg");
        MockMultipartFile file = new MockMultipartFile("file", "jonh-wick.txt", MediaType.MULTIPART_FORM_DATA_VALUE, fis);

        assertThrows(BadRequestException.class, () ->userService.uploadPhoto(1L, file));

        verify(userRepository, times(0)).findById(any());

    }


    // Methods
    private static UserType getUserType() {
        return new UserType(1L, "Aluno", "Aluno da plataforma");
    }

    private User getUser(UserType userType) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setCpf(dto.getCpf());
        user.setDtSubscription(dto.getDtSubscription());
        user.setDtExpiration(dto.getDtExpiration());
        user.setUserType(userType);
        return user;
    }
}
