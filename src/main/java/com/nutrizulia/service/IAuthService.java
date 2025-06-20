package com.nutrizulia.service;

import com.nutrizulia.dto.auth.AuthResponseDto;
import com.nutrizulia.dto.auth.SignInRequestDto;
import com.nutrizulia.dto.auth.SignUpRequestDto;

public interface IAuthService {

    AuthResponseDto signIn(SignInRequestDto signInRequestDto);

    AuthResponseDto signUp(SignUpRequestDto signUpRequestDto);

}
