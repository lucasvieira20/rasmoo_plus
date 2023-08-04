package com.client.ws.rasmooplus.service.implementations;

import com.client.ws.rasmooplus.dto.UserDto;
import com.client.ws.rasmooplus.exception.BadRequestException;
import com.client.ws.rasmooplus.exception.NotFoundException;
import com.client.ws.rasmooplus.mapper.UserMapper;
import com.client.ws.rasmooplus.model.jpa.User;
import com.client.ws.rasmooplus.model.jpa.UserType;
import com.client.ws.rasmooplus.repository.jpa.UserRepository;
import com.client.ws.rasmooplus.repository.jpa.UserTypeRepository;
import com.client.ws.rasmooplus.repository.redis.UserRecoveryCodeRepository;
import com.client.ws.rasmooplus.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImplementation implements UserService {

    private static final String PNG = ".png";
    private static final String JPG = ".jpg";
    private static final String JPEG = ".jpeg";

    private final UserRepository userRepository;

    private final UserTypeRepository userTypeRepository;

    UserServiceImplementation(
        UserRepository userRepository,
        UserTypeRepository userTypeRepository,
        UserRecoveryCodeRepository userRecoveryCodeRepository
    ) {
        this.userRepository = userRepository;
        this.userTypeRepository = userTypeRepository;
    }

    @Override
    public User create(UserDto dto) {
        if (Objects.nonNull(dto.getId())) {
            throw new BadRequestException("Id deve ser nulo");
        }

        var userTypeOpt = userTypeRepository.findById(dto.getUserTypeId());

        if (userTypeOpt.isEmpty()) {
            throw new NotFoundException("UserTypeId não encontrado");
        }

        UserType userType = userTypeOpt.get();

        User user = UserMapper.fromDtoToEntity(dto, userType, null);

        return userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User uploadPhoto(Long id, MultipartFile file) throws IOException {
        String imgName = file.getOriginalFilename();
        String formatPNG = imgName.substring(imgName.length() - 4);
        String formatJPEG = imgName.substring(imgName.length() - 5);

        if (!(PNG.equalsIgnoreCase(formatPNG) ||
                JPG.equalsIgnoreCase(formatJPEG) ||
                JPEG.equalsIgnoreCase(formatJPEG))) {
            throw new BadRequestException("Formato de imagem inválido - Deve possuir JPG ou PNG");
        }

        User user = findById(id);
        user.setPhotoName(file.getOriginalFilename());
        user.setPhoto(file.getBytes());
        return userRepository.save(user);
    }

    @Override
    public byte[] downloadPhoto(Long id) {
        User user = findById(id);
        if (Objects.isNull(user.getPhoto())) {
            throw new BadRequestException("Usuário sem foto não cadastrada");
        }
        return user.getPhoto();
    }

    private User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
    }
}
