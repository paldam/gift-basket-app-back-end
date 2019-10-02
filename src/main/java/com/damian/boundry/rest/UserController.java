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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.damian.config.Constants.ANSI_RESET;
import static com.damian.config.Constants.ANSI_YELLOW;

@CrossOrigin
@RestController
@RequestMapping(value={"/"}, produces="application/json;charset=UTF-8")
public class UserController {

    private static final String ENTITY_NAME = "userManagement";
    private final UserRepository userRepository;
    private final UserService userService;
    private  AuthorityRepository authorityRepository;
    private LoyaltyNumbersDao loyaltyNumbersDao;

    public UserController( LoyaltyNumbersDao loyaltyNumbersDao,UserRepository userRepository, UserService userService, AuthorityRepository authorityRepository ) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.authorityRepository = authorityRepository;
        this.loyaltyNumbersDao = loyaltyNumbersDao;
    }



    @CrossOrigin
    @GetMapping("/users")
    ResponseEntity<List<User>> getAllUsers(){
        List<User> userList = userRepository.findAll();
        return new ResponseEntity<List<User>>(userList, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/program_users")
    ResponseEntity<List<User>> getAllProgramUsers(){
        //
        List<User> userList = userRepository.getAllProgramUser();
        return new ResponseEntity<List<User>>(userList, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/program_users/resetpassword/{email:.+}")
    ResponseEntity resetPasswordForProgramUser(@PathVariable String email){

        userService.resetProgramUserPassword(email);
        return new ResponseEntity( HttpStatus.OK);
    }


    @CrossOrigin
    @GetMapping("/production_users")
    ResponseEntity<List<ProductionUserDto>> getAllProductionUsers(){
        //
        List<ProductionUserDto> userList = userRepository.getAllProductionUser();
        return new ResponseEntity<List<ProductionUserDto>>(userList, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/current_user_points")
    ResponseEntity<Integer> getCurrentUserPoints() {
        //
        Integer userPoints = userRepository.getPoints(SecurityUtils.getCurrentUserLogin());
        return new ResponseEntity<Integer>(userPoints, HttpStatus.OK);
    }


    @CrossOrigin
    @GetMapping("/current_user_name")
    ResponseEntity<String> getCurrentUserName() {
        //
        String userName = userRepository.getName(SecurityUtils.getCurrentUserLogin());
        return new ResponseEntity<>(userName, HttpStatus.OK);
    }


    @CrossOrigin
    @GetMapping("/is_first_time")
    ResponseEntity<Boolean> isFirstTimeLog(){
        //
        Boolean isFirstTimeLog = userRepository.isFirstLog(SecurityUtils.getCurrentUserLogin());
        return new ResponseEntity<Boolean>(isFirstTimeLog, HttpStatus.OK);
    }


    @CrossOrigin
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


    @CrossOrigin
    @PostMapping(value = "/program_users")
    public ResponseEntity createProgramUser(@RequestBody User user) {

        user.setLogin(user.getLogin().toUpperCase());


        if (user.getId() != null) {
            return ResponseEntity.badRequest()
                //.headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new user cannot already have an ID"))
                .body("Nowy użytkownik nie może mieć numeru ID");
            // Lowercase the user login before comparing with database
        } else if (userRepository.findOneByLogin(user.getLogin().toLowerCase()).isPresent()) {
            return ResponseEntity.badRequest()
                //.headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "userexists", "Login already in use"))
                .body("Podany login jest już zajęty");
        } else if (userRepository.findOneByEmail(user.getEmail().toLowerCase()).isPresent()) {
            return ResponseEntity.badRequest()
                //.headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "userexists", "Login already in use"))
                .body("Podany email jest już zajęty");


        } else if (!loyaltyNumbersDao.findAllByNumber(user.getLogin()).isPresent()) {
        return ResponseEntity.badRequest()
            .body("Błędny numer klienta");

       }
        else {
            User newUser = userService.createUserForLoyaltyProgram(user);
            return ResponseEntity.ok(newUser);

        }
    }


    @CrossOrigin
    @PostMapping(value = "/useredit")
    public ResponseEntity simpleEditProgramUser(@RequestBody User user) {



            userRepository.editSimpleUser(user.getLogin(),user.getPoints(),user.getName());
            return ResponseEntity.ok(user);


    }


    @CrossOrigin
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
    @CrossOrigin
    @PutMapping("/users/reset/{login}")
    public ResponseEntity resetPassword(@PathVariable String login) {

        userRepository.findOneByLogin(login).ifPresent(user -> {
            userRepository.resetPassword(login);
        });
        return ResponseEntity.ok().body("Zresetowno hasło dla użytkownika "+ login);


    }
    @CrossOrigin
    @PutMapping("/users/reset/")
    public ResponseEntity changePassword(@RequestBody UserPasswordChange userPasswordChange ) {

        int status = userService.resetPassword(userPasswordChange);



        if (status ==1){
            return ResponseEntity.ok().body("Zmieniono hasło użytkownikowi "+ userPasswordChange.getLogin());
        }else{
            return ResponseEntity.badRequest().body("Stare hasło jest nie poprawne");
        }




    }
    @CrossOrigin
    @GetMapping("/users/authorities")

    public List<Authority> getAuthorities() {
        return authorityRepository.findAll();
    }


    @CrossOrigin
    @DeleteMapping("/users/{login}")

    public ResponseEntity<Void> deleteUser(@PathVariable String login) {

        userService.deleteUser(login);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert( "userManagement.deleted", login)).build();
    }



    @CrossOrigin
    @DeleteMapping("/programusers/{login}")

    public ResponseEntity<Void> deleteProgramUser(@PathVariable String login) {

        userService.deleteProgramUser(login);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert( "userManagement.deleted", login)).build();
    }
}
