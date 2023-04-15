package io.openhaul.openhaul.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.openhaul.openhaul.entities.User;
import io.openhaul.openhaul.models.JwtPayload;
import io.openhaul.openhaul.services.UserService;
import io.openhaul.openhaul.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtUserDetailsFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    public static Cookie getCookieByName(Cookie[] cookies, String name) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie;
                }
            }
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            final Cookie sessionCookie = getCookieByName(request.getCookies(), "session_token");
            final Cookie refreshCookie = getCookieByName(request.getCookies(), "refresh_token");
            final String requestPath = request.getRequestURI();
            if(sessionCookie != null && refreshCookie != null && !requestPath.contains("/login")) {
                final String token = sessionCookie.getValue();
                final JwtPayload payload = jwtTokenUtil.getPayloadObjectFromToken(token);
                if(!userService.validateUserHasRefreshToken(refreshCookie.getValue(), payload.getSub())) {
                    throw new JWTVerificationException("Refresh token does not exist for user");
                }
                if(SecurityContextHolder.getContext().getAuthentication() == null) {
                    final User thisUser = userService.getUserByUsername(payload.getSub());
                    final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(thisUser.getUsername(), thisUser.getPassword(), thisUser.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (JWTVerificationException e) {
            logger.error("Token was not able to be verified", e);
        } catch (Exception e) {
            logger.error("Error", e);
        }
        filterChain.doFilter(request, response);
    }
}
