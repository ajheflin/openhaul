package io.openhaul.openhaul.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.openhaul.openhaul.converters.PasswordConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.HashSet;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "uuid-char")
    private UUID uuid;
    private String username;
    @Convert(converter = PasswordConverter.class)
    @JsonIgnore
    private String password;
    private String email;
    private HashSet<String> roles;
}
