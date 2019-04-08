package com.damian.domain.user;

import com.damian.dto.UserDto;
import com.damian.domain.user.Authority;
import com.damian.domain.user.User;
import com.damian.domain.user.UserPasswordChange;
import com.damian.domain.user.UserRepository;
import com.damian.domain.user.AuthorityRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    public int resetPassword(UserPasswordChange userPasswordChange) {
        Boolean isPasswordOk = checkPassword(userPasswordChange.getPassword(),userPasswordChange.getLogin());


        if (isPasswordOk){
            String encryptedNewPassword = passwordEncoder.encode(userPasswordChange.getNewPassword());
            userRepository.changePassword(encryptedNewPassword,userPasswordChange.getLogin());
            return 1;
        }else{
            return 0;
        }

    }

    private boolean checkPassword(String password , String login){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String dbPassword = userRepository.getPassword(login);


        if (passwordEncoder.matches(password, dbPassword)) {
            return true;
        } else {
           return false;
        }
    }
}
