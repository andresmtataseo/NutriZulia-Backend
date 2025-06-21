package com.nutrizulia.auth.service;

import com.nutrizulia.auth.dto.AuthResponseDto;
import com.nutrizulia.auth.dto.SignInRequestDto;
import com.nutrizulia.auth.dto.SignUpRequestDto;

public interface IAuthService {

    AuthResponseDto signIn(SignInRequestDto signInRequestDto);

    AuthResponseDto signUp(SignUpRequestDto signUpRequestDto);

}
