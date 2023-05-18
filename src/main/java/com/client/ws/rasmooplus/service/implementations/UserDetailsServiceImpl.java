package com.client.ws.rasmooplus.service.implementations;

import com.client.ws.rasmooplus.exception.BadRequestException;
import com.client.ws.rasmooplus.exception.NotFoundException;
import com.client.ws.rasmooplus.model.jpa.UserCredentials;
import com.client.ws.rasmooplus.model.redis.UserRecoveryCode;
import com.client.ws.rasmooplus.repository.jpa.UserDetailsRepository;
import com.client.ws.rasmooplus.repository.redis.UserRecoveryCodeRepository;
import com.client.ws.rasmooplus.service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private UserRecoveryCodeRepository userRecoveryCodeRepository;

    @Override
    public UserCredentials loadUserByUsernameAndPass(String username, String pass) {

        var userCredentialsOpt = userDetailsRepository.findByUsername(username);

        if (userCredentialsOpt.isEmpty()) {
            throw new NotFoundException("Usuário não encontrado");
        }

        UserCredentials userCredentials = userCredentialsOpt.get();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (encoder.matches(pass, userCredentials.getPassword())) {
            return userCredentials;
        }

        throw new BadRequestException("Usuário ou senha inválido");
    }

    @Override
    public Object sendRecoveryCode(String email) {
        // Steps
        // Buscar primeiro no Redis
        // Buscar na base de dados
        // Gerar código

        String code = String.format("%04d", new Random().nextInt(10000));

        userRecoveryCodeRepository.save(UserRecoveryCode.builder().code(code).build());

        return null;
    }
}
