package io.openhaul.openhaul.services;

import io.openhaul.openhaul.entities.Session;
import io.openhaul.openhaul.entities.User;
import io.openhaul.openhaul.repositories.SessionRepository;
import io.openhaul.openhaul.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Calendar;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final PasswordEncoder passwordEncoder;

    public User getUserByUsernameAndPassword(String username, String password) throws IllegalStateException {
        User mUser = userRepository.findByUsername(username).orElseThrow(IllegalStateException::new);
        if (passwordEncoder.matches(password, mUser.getPassword()))
            return mUser;
        else
            throw new IllegalStateException("Password does not match");
    }

    public User getUserByUsername(String username) throws IllegalStateException {
        return userRepository.findByUsername(username).orElseThrow(IllegalStateException::new);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public boolean validateUserHasRefreshToken(String refreshToken, String username) throws IllegalStateException {
        log.info("Refresh token: " + refreshToken);
        return sessionRepository.findSessionByRefreshToken(refreshToken).orElseThrow(IllegalStateException::new).getUser().getUsername().equals(username);
    }

    @Transactional
    public String createSessionAndGetRefreshToken(User user) {
        final byte[] b = new byte[16];
        new SecureRandom().nextBytes(b);
        final String refreshToken = Base64.getEncoder().encodeToString(b);
        sessionRepository.deleteAllByUserEquals(user);
        final Calendar oneDayFromNow = Calendar.getInstance();
        oneDayFromNow.add(Calendar.DAY_OF_YEAR, 1);
        return sessionRepository.save(new Session(user, refreshToken, oneDayFromNow.getTime())).getRefreshToken();
    }
}
