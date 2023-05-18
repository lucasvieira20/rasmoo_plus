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

import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;

    private final UserTypeRepository userTypeRepository;

    private final UserRecoveryCodeRepository userRecoveryCodeRepository;

    UserServiceImplementation(
        UserRepository userRepository,
        UserTypeRepository userTypeRepository,
        UserRecoveryCodeRepository userRecoveryCodeRepository
    ) {
        this.userRepository = userRepository;
        this.userTypeRepository = userTypeRepository;
        this.userRecoveryCodeRepository = userRecoveryCodeRepository;
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

}
