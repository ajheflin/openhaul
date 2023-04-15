package io.openhaul.openhaul.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "sessions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "uuid-char")
    private UUID id;
    @OneToOne
    private User user;
    private String refreshToken;
    private Date expiration;

    public Session(User user, String refreshToken, Date expiration) {
        this.user = user;
        this.refreshToken = refreshToken;
        this.expiration = expiration;
    }
}
