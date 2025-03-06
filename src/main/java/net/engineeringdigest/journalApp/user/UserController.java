package net.engineeringdigest.journalApp.user;

import java.util.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.user.entity.User;


@RestController
@RequestMapping("user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUser() {
        List<User> users = userService.getAll();
        return users.isEmpty() ? new ResponseEntity<List<User>>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        log.info("testing create user");
        try {
            User createdUser = userService.createUser(user);
            return new ResponseEntity<User>(createdUser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/findUser")
    public ResponseEntity<User> findById(@RequestParam String userName ) {
        Optional<User> user = userService.findByUserName(userName);
        if (user.isPresent()) {
            return new ResponseEntity<User>(user.get(), HttpStatus.OK);
        }
        return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> remove(@PathVariable ObjectId id) {
        userService.remove(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("{userName}")
    public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable String userName) {
        Optional<User> userInDb = userService.findByUserName(userName);
        if(userInDb.isPresent()){
            userInDb.get().setUserName(user.getUserName());
            userInDb.get().setPassword(user.getPassword());
            return new ResponseEntity<User>(userService.createUser(userInDb.get()), HttpStatus.OK);
        }
        return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
    }
}