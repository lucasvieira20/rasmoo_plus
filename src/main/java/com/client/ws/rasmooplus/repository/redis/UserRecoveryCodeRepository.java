package com.client.ws.rasmooplus.repository.redis;

import com.client.ws.rasmooplus.model.redis.UserRecoveryCode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRecoveryCodeRepository extends CrudRepository<UserRecoveryCode, String> {}
