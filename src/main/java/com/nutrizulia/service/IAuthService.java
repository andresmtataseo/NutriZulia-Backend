package com.nutrizulia.service;

import com.nutrizulia.dto.auth.AuthResponse;
import com.nutrizulia.dto.auth.LoginRequest;
import com.nutrizulia.dto.auth.RegisterRequest;

public interface IAuthService {

    public AuthResponse login(LoginRequest loginRequest);

    public AuthResponse register(RegisterRequest registerRequest);

}
