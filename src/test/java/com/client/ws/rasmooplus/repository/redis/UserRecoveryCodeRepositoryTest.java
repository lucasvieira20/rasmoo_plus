package com.client.ws.rasmooplus.repository.redis;

import com.client.ws.rasmooplus.model.redis.UserRecoveryCode;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.AutoConfigureDataRedis;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureDataRedis
@AutoConfigureTestDatabase
@WebMvcTest(UserRecoveryCodeRepository.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRecoveryCodeRepositoryTest {

    @Autowired
    private UserRecoveryCodeRepository recoveryCodeRepository;

    @BeforeAll
    public void loadRecoveryUsers() {
        List<UserRecoveryCode> userRecoveryCodeList = new ArrayList<>();

        UserRecoveryCode userRecoveryCode1 = new UserRecoveryCode();
        userRecoveryCode1.setEmail("usuario1@email.com");
        userRecoveryCode1.setCode("1234");
        userRecoveryCodeList.add(userRecoveryCode1);

        UserRecoveryCode userRecoveryCode2 = new UserRecoveryCode();
        userRecoveryCode2.setEmail("usuario2@email.com");
        userRecoveryCode2.setCode("2345");
        userRecoveryCodeList.add(userRecoveryCode2);

        UserRecoveryCode userRecoveryCode3 = new UserRecoveryCode();
        userRecoveryCode3.setEmail("usuario3@email.com");
        userRecoveryCode3.setCode("3456");
        userRecoveryCodeList.add(userRecoveryCode3);

        recoveryCodeRepository.saveAll(userRecoveryCodeList);
    }

    @AfterAll
    public void DropDataBase() {
        recoveryCodeRepository.deleteAll();
    }

    @Test
    void given_findEmail_when_getByEmail_then_return_CorrectUserRecoveryCode() {
        assertEquals("1234", recoveryCodeRepository.findByEmail("usuario1@email.com").get().getCode());
        assertEquals("2345", recoveryCodeRepository.findByEmail("usuario2@email.com").get().getCode());
        assertEquals("3456", recoveryCodeRepository.findByEmail("usuario3@email.com").get().getCode());
    }
}
