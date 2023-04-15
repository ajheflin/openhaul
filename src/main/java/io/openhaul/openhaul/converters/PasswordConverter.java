package io.openhaul.openhaul.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.AttributeConverter;

@RequiredArgsConstructor
public class PasswordConverter implements AttributeConverter<String, String> {
    private final PasswordEncoder passwordEncoder;

    @Override
    public String convertToDatabaseColumn(String attribute) {
        String password = passwordEncoder.encode(attribute);
        System.out.println(password);
        return password;
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return dbData;
    }
}
