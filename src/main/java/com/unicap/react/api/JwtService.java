package com.unicap.react.api;

import com.unicap.react.api.models.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JwtService {


    @Value("${security.jwt.expiration}")
    private String expiration;
    @Value("${security.jwt.sign-key}")
    private String signKey;

    public String gerarToken(Usuario usuario) {
        Long expirationString = Long.valueOf(this.expiration);
        LocalDateTime dataHoraExpiration = LocalDateTime.now().plusMinutes(expirationString);
        Date date = Date.from(dataHoraExpiration.atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setSubject(usuario.getLogin())
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, signKey)
                .compact();
    }
}
