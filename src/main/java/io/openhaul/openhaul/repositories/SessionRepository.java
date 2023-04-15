package io.openhaul.openhaul.repositories;

import io.openhaul.openhaul.entities.Session;
import io.openhaul.openhaul.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SessionRepository extends JpaRepository<Session, UUID> {
    Optional<Session> findSessionByRefreshToken(String refreshToken);
    void deleteAllByUserEquals(User user);
    void deleteAllByUserUuidEquals(UUID userId);
}
