package io.openhaul.openhaul.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashSet;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtPayload {
    private String sub;
    private Date exp;

    public JwtPayload(String sub) {
        this.sub = sub;
    }
}
