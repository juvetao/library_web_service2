package com.example.demo.controllers;

import com.example.demo.entities.User;
import com.example.demo.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/library/users")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @Secured("ROLE_ADMIN")
    @GetMapping
    public ResponseEntity<List<User>> findAllUsers(@RequestParam(required = false) String name, @RequestParam(required = false) boolean sort){
        return ResponseEntity.ok(userService.findAll(name, sort));
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/{id}")
    public ResponseEntity<User> findUserById(@PathVariable String id){
        return ResponseEntity.ok(userService.findById(id));
    }

    @Secured("ROLE_ADMIN")
    @PostMapping
    public ResponseEntity<User> saveUser(@Validated @RequestBody User user) {
        return ResponseEntity.ok(userService.save(user));
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUser(@PathVariable String id, @Validated @RequestBody User user) {
        userService.update(id, user);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable String id) {
        userService.delete(id);
    }

}
