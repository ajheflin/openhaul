package io.openhaul.openhaul.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.openhaul.openhaul.entities.User;
import io.openhaul.openhaul.models.JwtPayload;
import io.openhaul.openhaul.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Date;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;
    private final Algorithm algorithm;
    @Value("${cookie.domain}")
    private String cookieDomain;


    @GetMapping("/login")
    public void login(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletRequest request, HttpServletResponse response) {
        try {
            final User user = userService.getUserByUsernameAndPassword(username, password);
            final ObjectMapper om = new ObjectMapper();
            final JwtPayload payload = new JwtPayload(user.getUsername());
            final String token = JWT.create()
                    .withSubject(username)
                    .withPayload(om.writeValueAsString(payload))
                    .withExpiresAt(new Date(System.currentTimeMillis() + 86400000))
                    .sign(algorithm);
            final Cookie sessionCookie = new Cookie("session_token", token);
            sessionCookie.setHttpOnly(true);
            sessionCookie.setDomain(cookieDomain);
            sessionCookie.setPath("/");
            sessionCookie.setMaxAge(900);
            response.addCookie(sessionCookie);
            final Cookie refreshCookie = new Cookie("refresh_token", userService.createSessionAndGetRefreshToken(user));
            refreshCookie.setHttpOnly(true);
            refreshCookie.setDomain(cookieDomain);
            refreshCookie.setPath("/");
            refreshCookie.setMaxAge(86400);
            response.addCookie(refreshCookie);
        } catch (IllegalStateException e) {
            //user does not exist
            log.info("User does not exist");
            response.setStatus(401);
        } catch (Exception e) {
            //do nothing
            e.printStackTrace();
            log.info("Something went wrong");
            response.setStatus(500);
        }
    }
}
