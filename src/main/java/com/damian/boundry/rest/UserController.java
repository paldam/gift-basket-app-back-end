package com.damian.boundry.rest;

import com.damian.domain.prize.LoyaltyNumbersDao;
import com.damian.dto.ProductionUserDto;
import com.damian.dto.UserDto;
import com.damian.domain.user.Authority;
import com.damian.domain.user.User;
import com.damian.domain.user.UserPasswordChange;
import com.damian.domain.user.AuthorityRepository;
import com.damian.domain.user.UserRepository;
import com.damian.boundry.rest.util.HeaderUtil;
import com.damian.domain.user.UserService;
import com.damian.security.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;


@CrossOrigin
@RestController
@RequestMapping(value = {"/"}, produces = "application/json;charset=UTF-8")
public class UserController {

    private static final String ENTITY_NAME = "userManagement";
    private final UserRepository userRepository;
    private final UserService userService;
    private final AuthorityRepository authorityRepository;
    private final LoyaltyNumbersDao loyaltyNumbersDao;

    public UserController(LoyaltyNumbersDao loyaltyNumbersDao, UserRepository userRepository, UserService userService
        , AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.authorityRepository = authorityRepository;
        this.loyaltyNumbersDao = loyaltyNumbersDao;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> userList = userRepository.findAll();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }
    @GetMapping("/users_without_del")
    public ResponseEntity<List<User>> getUserWithoutDel() {
        List<User> userList = userRepository.getUsersWithoutDel();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }


    @GetMapping("/program_users")
    public ResponseEntity<List<User>> getAllProgramUsers() {
        List<User> userList = userRepository.getAllProgramUser();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping("/program_users/resetpassword/{email:.+}")
    public ResponseEntity resetPasswordForProgramUser(@PathVariable String email) {
        userService.resetProgramUserPassword(email);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/production_users")
    public ResponseEntity<List<ProductionUserDto>> getAllProductionUsers() {
        List<ProductionUserDto> userList = userRepository.getAllProductionUser();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping("/current_user_points")
    public ResponseEntity<Integer> getCurrentUserPoints() {
        Integer userPoints = userRepository.getPoints(SecurityUtils.getCurrentUserLogin());
        return new ResponseEntity<>(userPoints, HttpStatus.OK);
    }

    @GetMapping("/current_user_name")
    public ResponseEntity<String> getCurrentUserName() {
        String userName = userRepository.getName(SecurityUtils.getCurrentUserLogin());
        return new ResponseEntity<>(userName, HttpStatus.OK);
    }

    @GetMapping("/is_first_time")
    public ResponseEntity<Boolean> isFirstTimeLog() {
        Boolean isFirstTimeLog = userRepository.isFirstLog(SecurityUtils.getCurrentUserLogin());
        return new ResponseEntity<>(isFirstTimeLog, HttpStatus.OK);
    }

    @PostMapping(value = "/users")
    public ResponseEntity createUser(@RequestBody UserDto userDto) throws URISyntaxException {
        if (userDto.getId() != null) {
            return ResponseEntity.badRequest()
                .body("Nowy użytkownik nie może mieć numeru ID");
        } else if (userRepository.findOneByLogin(userDto.getLogin().toLowerCase()).isPresent()) {
            return ResponseEntity.badRequest()
                .body("Podany login jest już zajęty");
        } else {
            User newUser = userService.createUser(userDto);
            return ResponseEntity.created(new URI("/api/users/" + newUser.getLogin()))
                .body("Dodano użytownika " + userDto.getLogin());
        }
    }

    @PostMapping(value = "/program_users")
    public ResponseEntity createProgramUser(@RequestBody User user) {
        user.setLogin(user.getLogin().toUpperCase());
        if (user.getId() != null) {
            return ResponseEntity.badRequest()
                .body("Nowy użytkownik nie może mieć numeru ID");
        } else if (userRepository.findOneByLogin(user.getLogin().toLowerCase()).isPresent()) {
            return ResponseEntity.badRequest()
                .body("Podany login jest już zajęty");
        } else if (userRepository.findOneByEmail(user.getEmail().toLowerCase()).isPresent()) {
            return ResponseEntity.badRequest()
                .body("Podany email jest już zajęty");
        } else if (!loyaltyNumbersDao.findAllByNumber(user.getLogin()).isPresent()) {
            return ResponseEntity.badRequest().body("Błędny numer klienta");
        } else {
            User newUser = userService.createUserForLoyaltyProgram(user);
            return ResponseEntity.ok(newUser);
        }
    }

    @PostMapping(value = "/useredit")
    public ResponseEntity simpleEditProgramUser(@RequestBody User user) {
        userRepository.editSimpleUser(user.getLogin(), user.getPoints(), user.getName());
        return ResponseEntity.ok(user);
    }

    @PutMapping("/users")
    public ResponseEntity updateUser(@RequestBody UserDto userDto) {
        Optional<User> existingUser = userRepository.findOneByLogin(userDto.getLogin().toLowerCase());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDto.getId()))) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME,
                "Login already in " + "use")).body(null);
        }
        User updatedUser = userService.updateUser(userDto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(
                "A user is updated with identifier " + updatedUser.getLogin(), updatedUser.getLogin()))
            .body(updatedUser);
    }

    @PutMapping("/users/reset/{login}")
    public ResponseEntity resetPassword(@PathVariable String login) {
        userRepository.findOneByLogin(login).ifPresent(user -> userRepository.resetPassword(login));
        return ResponseEntity.ok().body("Zresetowno hasło dla użytkownika " + login);
    }

    @PutMapping("/users/reset/")
    public ResponseEntity changePassword(@RequestBody UserPasswordChange userPasswordChange) {
        int status = userService.resetPassword(userPasswordChange);
        if (status == 1) {
            return ResponseEntity.ok().body("Zmieniono hasło użytkownikowi " + userPasswordChange.getLogin());
        } else {
            return ResponseEntity.badRequest().body("Stare hasło jest nie poprawne");
        }
    }

    @GetMapping("/users/authorities")
    public List<Authority> getAuthorities() {
        return authorityRepository.findAll();
    }

    @DeleteMapping("/users/{login}")
    public ResponseEntity<Void> deleteUser(@PathVariable String login) {
        userService.deleteUser(login);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("userManagement.deleted", login)).build();
    }

    @DeleteMapping("/programusers/{login}")
    public ResponseEntity<?> deleteProgramUser(@PathVariable String login) {
        userService.deleteProgramUser(login);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("userManagement.deleted", login)).build();
    }
}
