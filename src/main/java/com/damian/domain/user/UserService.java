package com.damian.domain.user;

import com.damian.domain.notification.NotificationDao;
import com.damian.dto.UserDto;
import com.damian.security.SecurityUtils;
import com.damian.util.EmailService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private NotificationDao notificationDao;
    private EmailService emailService;

    public UserService(EmailService emailService, NotificationDao notificationDao, UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.notificationDao = notificationDao;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public User createUser(UserDto userDto) {
        User user = new User();
        user.setLogin(userDto.getLogin());
        user.setAuthorities(userDto.getAuthorities());
        String encryptedPassword = passwordEncoder.encode(userDto.getPassword());
        user.setPassword(encryptedPassword);
        user.setActivated(true);
        user.setArchival(false);
        userRepository.save(user);
        return user;
    }

    public User createUserForLoyaltyProgram(User user) {
        Set<Authority> authoritiesTmp = new HashSet<>();
        authoritiesTmp.add(new Authority("punkty"));
        user.setAuthorities(authoritiesTmp);
        String generatedPlainPass = SecurityUtils.generateRandomSpecialCharacters(14);
        String encryptedPassword = passwordEncoder.encode(generatedPlainPass);
        user.setPassword(encryptedPassword);
        user.setActivated(true);
        user.setIfFirstLogin(true);
        user.setPoints(0);
        userRepository.save(user);
        emailService.sendSimpleMessage(user.getEmail(), "Dane do logowania", "Witamy w programie loyalnościowym " +
            "twoje" + " dane do zalogowania to login: " + user.getLogin() + " hasło: " + generatedPlainPass);
        return user;
    }

    public void resetProgramUserPassword(String email) {
        Optional<User> user = userRepository.findOneByEmail(email);
        if (user.isPresent()) {
            String generatedPlainPass = SecurityUtils.generateRandomSpecialCharacters(14);
            String encryptedPassword = passwordEncoder.encode(generatedPlainPass);
            user.get().setPassword(encryptedPassword);
            userRepository.save(user.get());
            emailService.sendSimpleMessage(user.get().getEmail(), "Dane do logowania", "Twoje hasło zostało " +
                "zresetowane nowe hasło to: " + generatedPlainPass);
        }
    }

    public void deleteUser(String login) {
        userRepository.findOneByLogin(login).ifPresent(user -> {
            notificationDao.deleteByUser(user.getId());
            userRepository.markUserAsArchival(user.getId());
        });
    }

    public void deleteProgramUser(String login) {
        userRepository.findOneByLogin(login).ifPresent(userRepository::delete);
    }

    public User updateUser(UserDto userDto) {
        User user = userRepository.getOne(userDto.getId());
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
        boolean isPasswordOk = checkPassword(userPasswordChange.getPassword(), SecurityUtils.getCurrentUserLogin());
        if (isPasswordOk) {
            String encryptedNewPassword = passwordEncoder.encode(userPasswordChange.getNewPassword());
            userRepository.changePassword(encryptedNewPassword, SecurityUtils.getCurrentUserLogin());
            userRepository.setFirstLoginFalse(SecurityUtils.getCurrentUserLogin());
            return 1;
        } else {
            return 0;
        }
    }

    private boolean checkPassword(String password, String login) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String dbPassword = userRepository.getPassword(login);
        return passwordEncoder.matches(password, dbPassword);
    }
}
