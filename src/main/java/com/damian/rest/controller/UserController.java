package com.damian.rest.controller;

import com.damian.dto.UserDto;
import com.damian.model.Authority;
import com.damian.model.Customer;
import com.damian.model.User;
import com.damian.repository.AuthorityRepository;
import com.damian.repository.UserRepository;
import com.damian.rest.controller.util.HeaderUtil;
import com.damian.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    private static final String ENTITY_NAME = "userManagement";
    private final UserRepository userRepository;
    private final UserService userService;
    private  AuthorityRepository authorityRepository;

    public UserController(UserRepository userRepository, UserService userService,AuthorityRepository authorityRepository ) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.authorityRepository = authorityRepository;
    }



    @CrossOrigin
    @GetMapping("/users")
    ResponseEntity<List<User>> getAllUsers(){
        List<User> userList = userRepository.findAll();
        return new ResponseEntity<List<User>>(userList, HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity createUser(@RequestBody UserDto userDto) throws URISyntaxException {

        if (userDto.getId() != null) {
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new user cannot already have an ID"))
                    .body(null);
            // Lowercase the user login before comparing with database
        } else if (userRepository.findOneByLogin(userDto.getLogin().toLowerCase()).isPresent()) {
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "userexists", "Login already in use"))
                    .body(null);
        } else {
            User newUser = userService.createUser(userDto);
            return ResponseEntity.created(new URI("/api/users/" + newUser.getLogin()))
                    .headers(HeaderUtil.createAlert( "A user is created with identifier " + newUser.getLogin(), newUser.getLogin()))
                    .body(newUser);
        }
    }

    @PutMapping("/users")
    public ResponseEntity updateUser(@RequestBody UserDto userDto) {

        Optional<User> existingUser = userRepository.findOneByEmail(userDto.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDto.getId()))) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "emailexists", "Email already in use")).body(null);
        }
        existingUser = userRepository.findOneByLogin(userDto.getLogin().toLowerCase());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDto.getId()))) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "userexists", "Login already in use")).body(null);
        }
        User updatedUser = userService.updateUser(userDto);

        return ResponseEntity.ok().headers(HeaderUtil.createAlert("A user is updated with identifier " + updatedUser.getLogin(), updatedUser.getLogin())).body(updatedUser);


    }

    @GetMapping("/users/authorities")

    public List<Authority> getAuthorities() {
        return authorityRepository.findAll();
    }
}
