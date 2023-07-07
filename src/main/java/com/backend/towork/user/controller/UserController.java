package com.backend.towork.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.towork.global.dto.ResponseDto;
import com.backend.towork.user.dto.UserDto;
import com.backend.towork.user.entity.Role;
import com.backend.towork.user.entity.User;
import com.backend.towork.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<ResponseDto<?>> join(@RequestBody @Valid UserDto userDto, BindingResult result) {
        if (result.hasErrors()) {
            ResponseDto<UserDto> responseDto = ResponseDto.<UserDto>builder()
                    .resultCode(404)
                    .message("unexpected error")
                    .data(userDto)
                    .build();
            return ResponseEntity.badRequest().body(responseDto);
        }
        User user = User.builder()
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .role(Role.ROLE_USER)
                .build();
        log.info(user.getEmail());
        userService.join(user);
        ResponseDto<User> responseDto = ResponseDto.<User>builder()
                .resultCode(200)
                .message("회원가입 성공")
                .data(user)
                .build();

        return ResponseEntity.ok().body(responseDto);
    }

}
