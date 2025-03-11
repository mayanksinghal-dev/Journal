package net.engineeringdigest.journalApp.controller;
import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.service.UserService;
import net.engineeringdigest.journalApp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@Slf4j
@RequestMapping("public")
public class PublicController {

    @Autowired
    private UserService userService;

    @GetMapping("check")
    public String testing(){
        return "oK";
    }

    @PostMapping("createUser")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        log.info("create user");
        try {
            User createdUser = userService.createNewUser(user);
            return new ResponseEntity<User>(createdUser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
