package com.koushik.expansetracker.controller.security;

import com.koushik.expansetracker.dto.CreateUserRequest;
import com.koushik.expansetracker.entity.security.User;
import com.koushik.expansetracker.service.security.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest request) {
        User user = userService.createUser(request);
        return ResponseEntity.ok(user);
    }
}
