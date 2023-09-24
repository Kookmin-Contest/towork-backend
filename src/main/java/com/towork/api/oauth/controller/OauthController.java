package com.towork.api.oauth.controller;

import com.towork.api.auth.domain.dto.response.TokenResponseDto;
import com.towork.api.oauth.service.SocialLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/oauth2")
public class OauthController {

    private final SocialLoginService socialLoginService;

    @GetMapping("/social-login/{registrationId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "로그인 페이지 리다이렉트",
            description = "Oauth 서버에서 제공하는 로그인 페이지를 리다이렉트 합니다.",
            parameters = {
                    @Parameter(name = "registrationId", description = "Provider의 ID")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "리다이렉트 성공", content = {
                            @Content(schema = @Schema(type = "string"))
                    }),
                    @ApiResponse(responseCode = "400", description = "Provider의 ID가 유효하지 않음", content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))
                    })
            },
            security = {}
    )
    public void redirectLoginPage(HttpServletResponse response, @PathVariable String registrationId) throws IOException {
        String authorizationUriRequest = socialLoginService.getAuthorizationUriRequest(registrationId);
        response.sendRedirect(authorizationUriRequest);
    }

    @GetMapping("/callback/{registrationId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "callback 받기",
            description = "Oauth 서버가 보낸 코드로 토큰을 얻고, 토큰으로 사용자 정보를 리소스 서버로부터 가져와 회원가입 또는 로그인 진행",
            parameters = {
                    @Parameter(name = "registrationId", description = "Provider의 ID"),
                    @Parameter(name = "code", description = "Provider가 제공한 인증 코드")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "response가 엑세스 토큰과 리프레쉬 토큰을 담아 리다이렉트 시킴", content = {
                            @Content(schema = @Schema(type = "string"))
                    }),
                    @ApiResponse(responseCode = "400", description = "동일한 이메일이 존재함", content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class))
                    })
            }
    )
    public void socialLogin(@PathVariable String registrationId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        TokenResponseDto tokenResponseDto = socialLoginService.socialLogin(registrationId, request.getParameter("code"));
        String callbackUrlScheme = UriComponentsBuilder.fromUriString("com.example.gotowork://oauth-callback")
                .queryParam("accessToken", tokenResponseDto.getAccessToken())
                .queryParam("refreshToken", tokenResponseDto.getRefreshToken())
                .build().toUriString();
        log.info(callbackUrlScheme);
        response.sendRedirect(callbackUrlScheme);
    }

}
