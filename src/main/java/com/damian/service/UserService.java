package com.damian.service;

import com.damian.dto.UserDto;
import com.damian.model.Authority;
import com.damian.model.User;
import com.damian.repository.UserRepository;
import com.damian.security.AuthoritiesConstants;
import com.damian.repository.AuthorityRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {


    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
    }


    public User createUser(UserDto userDto) {


        User user = new User();
        user.setLogin(userDto.getLogin());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setAuthorities(userDto.getAuthorities());
        String encryptedPassword = passwordEncoder.encode(userDto.getPassword());
        user.setPassword(encryptedPassword);
        user.setActivated(true);
        userRepository.save(user);
        return user;
    }

    public User updateUser(UserDto userDto) {
        User user = userRepository.findOne(userDto.getId());

        user.setLogin(userDto.getLogin());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setActivated(userDto.isActivated());

        if (userDto.getPassword() != null) {
            String encryptedPassword = passwordEncoder.encode(userDto.getPassword());
            user.setPassword(encryptedPassword);
        }
        Set<Authority> managedAuthorities = userDto.getAuthorities();

        user.setAuthorities(managedAuthorities);

        userRepository.save(user);

        return user;

    }
}