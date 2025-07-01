package com.api.myFinancePro.controller;

import com.api.myFinancePro.dto.UserCreationDTO;
import com.api.myFinancePro.dto.UserResponseDTO;
import com.api.myFinancePro.exception.EmailAlreadyExistsException;
import com.api.myFinancePro.exception.UsernameAlreadyExistsException;
import com.api.myFinancePro.model.User;
import com.api.myFinancePro.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@Tag (name = "User", description = "API for User management")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody UserCreationDTO user) throws UsernameAlreadyExistsException, EmailAlreadyExistsException {
        UserResponseDTO userResponseDTO = userService.register(user);
        return ResponseEntity.ok(userResponseDTO);
    }
}
