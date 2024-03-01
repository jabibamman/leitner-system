package com.esgi.leitnersystem.infrastructure.repository;

import com.esgi.leitnersystem.domain.user.UserRepositoryPort;
import com.esgi.leitnersystem.infrastructure.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserRepositoryAdapter implements UserRepositoryPort {
    private final UserRepository userRepository;

    @Autowired
    public UserRepositoryAdapter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserEntity login(String username, String password) {
        return userRepository.login(username, password);
    }

    @Override
    public UserEntity save(UserEntity user) {
        return userRepository.save(user);
    }
}
