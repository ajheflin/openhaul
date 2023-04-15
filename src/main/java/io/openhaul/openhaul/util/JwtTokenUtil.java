package io.openhaul.openhaul.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.openhaul.openhaul.entities.User;
import io.openhaul.openhaul.models.JwtPayload;
import io.openhaul.openhaul.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class JwtTokenUtil {

    private final Algorithm algorithm;
    private final UserService userService;

    public User getUserFromToken(String token) throws  IllegalStateException {
        final DecodedJWT jwt = JWT.require(algorithm).build().verify(token);
        return userService.getUserByUsername(jwt.getSubject());
    }

    public JwtPayload getPayloadObjectFromToken(String token) throws JsonProcessingException {
        final DecodedJWT jwt = JWT.require(algorithm).build().verify(token);
        return new ObjectMapper().readValue(new String(Base64.getDecoder().decode(jwt.getPayload())), JwtPayload.class);
    }
}
