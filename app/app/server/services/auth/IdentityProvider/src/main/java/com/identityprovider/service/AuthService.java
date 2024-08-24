package com.identityprovider.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import com.identityprovider.dto.*;
//import com.identityprovider.mapper.AuthConverter;
//import com.identityprovider.mapper.RegisterConverter;

import java.util.ArrayList;

@Slf4j
@Service
public class AuthService {
//    @Autowired
//    private AppParams appParams;
//
//    @Autowired
//    private com.identityprovider.repository.AuthRepository repository;
//
//
//    public TokenResponse getToken(AuthRequest request) {
//        TokenRequest tokenRequest = AuthConverter.fromAuthRequestToTokenRequest(request)
//                .setScope(appParams.scope)
//                .setGrantType(appParams.grantType)
//                .setClientId(appParams.clientId)
//                .setClientSecret(appParams.clientSecret);
//
//        return repository.postToken(tokenRequest);
//    }
//
//    public HttpStatusCode register(RegisterRequest request) {
//        RegisterRequestDto requestDto = RegisterConverter.fromRegisterRequestToRegisterRequestDto(request);
//        requestDto.setGroupIds(new ArrayList<>(){{
//            add(appParams.groupId);
//        }});
//
//        return repository.register(requestDto);
//    }
}
