package com.damian.rest;

import com.damian.dto.UserDto;
import com.damian.model.Authority;
import com.damian.model.User;
import com.damian.model.UserPasswordChange;
import com.damian.repository.AuthorityRepository;
import com.damian.repository.UserRepository;
import com.damian.rest.util.HeaderUtil;
import com.damian.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value={"/"}, produces="application/json;charset=UTF-8")
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

    @PostMapping(value = "/users")
    public ResponseEntity createUser(@RequestBody UserDto userDto) throws URISyntaxException {

        if (userDto.getId() != null) {
            return ResponseEntity.badRequest()
                    //.headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new user cannot already have an ID"))
                    .body("Nowy użytkownik nie może mieć numeru ID");
            // Lowercase the user login before comparing with database
        } else if (userRepository.findOneByLogin(userDto.getLogin().toLowerCase()).isPresent()) {
            return ResponseEntity.badRequest()
                    //.headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "userexists", "Login already in use"))
                    .body("Podany login jest już zajęty");
        } else {
            User newUser = userService.createUser(userDto);
            return ResponseEntity.created(new URI("/api/users/" + newUser.getLogin()))
                    //.headers(HeaderUtil.createAlert( "A user is created with identifier " + newUser.getLogin(), newUser.getLogin()))
                    .body("Dodano użytownika " +userDto.getLogin());
        }
    }

    @PutMapping("/users")
    public ResponseEntity updateUser(@RequestBody UserDto userDto) {

//        Optional<User> existingUser = userRepository.findOneByEmail(userDto.getEmail());
//        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDto.getId()))) {
//            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "emailexists", "Email already in use")).body(null);
//        }
        Optional<User> existingUser = userRepository.findOneByLogin(userDto.getLogin().toLowerCase());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDto.getId()))) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "userexists", "Login already in use")).body(null);
        }
        User updatedUser = userService.updateUser(userDto);

        return ResponseEntity.ok().headers(HeaderUtil.createAlert("A user is updated with identifier " + updatedUser.getLogin(), updatedUser.getLogin())).body(updatedUser);


    }
    @PutMapping("/users/reset/{login}")
    public ResponseEntity resetPassword(@PathVariable String login) {

        userRepository.findOneByLogin(login).ifPresent(user -> {
            userRepository.resetPassword(login);
        });
        return ResponseEntity.ok().body("Zresetowno hasło dla użytkownika "+ login);


    }

    @PutMapping("/users/reset/")
    public ResponseEntity changePassword(@RequestBody UserPasswordChange userPasswordChange ) {

        int status = userService.resetPassword(userPasswordChange);

        if (status ==1){
            return ResponseEntity.ok().body("Zmieniono hasło użytkownikowi "+ userPasswordChange.getLogin());
        }else{
            return ResponseEntity.badRequest().body("Stare hasło jest nie poprawne");
        }




    }

    @GetMapping("/users/authorities")

    public List<Authority> getAuthorities() {
        return authorityRepository.findAll();
    }

    @DeleteMapping("/users/{login}")

    public ResponseEntity<Void> deleteUser(@PathVariable String login) {

        userRepository.findOneByLogin(login).ifPresent(user -> {
            userRepository.delete(user);
        });
        return ResponseEntity.ok().headers(HeaderUtil.createAlert( "userManagement.deleted", login)).build();
    }
}
