package net.engineeringdigest.journalApp.controller;

import java.util.*;

import net.engineeringdigest.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.entity.User;


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

    @GetMapping("/findUser")
    public ResponseEntity<User> findById(@RequestParam String userName ) {
        Optional<User> user = userService.findByUserName(userName);
        if (user.isPresent()) {
            return new ResponseEntity<User>(user.get(), HttpStatus.OK);
        }
        return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping
    public ResponseEntity<?> remove() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        userService.remove(userName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Optional<User> userInDb = userService.findByUserName(userName);
        if(userInDb.isPresent()){
            userInDb.get().setUserName(user.getUserName());
            userInDb.get().setPassword(user.getPassword());
            return new ResponseEntity<User>(userService.createNewUser(userInDb.get()), HttpStatus.OK);
        }
        return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
    }
}