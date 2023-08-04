package com.client.ws.rasmooplus.service;

import com.client.ws.rasmooplus.dto.UserDto;
import com.client.ws.rasmooplus.model.jpa.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {

    User create(UserDto dto);

    List<User> findAll();

    User uploadPhoto(Long id, MultipartFile file) throws IOException;

    byte[] downloadPhoto(Long id);
}
