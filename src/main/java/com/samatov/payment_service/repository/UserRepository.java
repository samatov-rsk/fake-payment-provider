package com.samatov.payment_service.repository;

import com.samatov.payment_service.model.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface UserRepository extends R2dbcRepository<User, Long> {
}