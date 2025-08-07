package com.nutrizulia.features.auth.service;

import com.nutrizulia.common.dto.ApiResponseDto;
import com.nutrizulia.features.auth.dto.*;

public interface IAuthService {

    ApiResponseDto<AuthResponseDto> signIn(SignInRequestDto signInRequestDto);

    ApiResponseDto<AuthAdminResponseDto> signInAdmin(SignInRequestDto signInRequestDto);

    ApiResponseDto<Object> signUp(SignUpRequestDto signUpRequestDto);

    ApiResponseDto<Object> forgotPassword(ForgotPasswordRequestDto request);

    ApiResponseDto<Object> changePassword(ChangePasswordRequestDto request, String cedula);

    ApiResponseDto<Object> checkAuthStatus(String cedula);

    ApiResponseDto<Object> logout(String token, String cedula);

}
