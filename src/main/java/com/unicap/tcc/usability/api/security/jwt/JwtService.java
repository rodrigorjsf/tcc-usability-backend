package com.unicap.tcc.usability.api.security.jwt;

import com.unicap.tcc.usability.api.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;

@Service
public class JwtService {


    @Value("${security.jwt.expiration}")
    private String expiration;
    @Value("${security.jwt.sign-key}")
    private String signKey;

    public String generateToken(User user) {
        Long expirationString = Long.valueOf(this.expiration);
        LocalDateTime dataHoraExpiration = LocalDateTime.now().plusMinutes(expirationString);
        Date date = Date.from(dataHoraExpiration.atZone(ZoneId.systemDefault()).toInstant());

        HashMap<String, Object> claims = new HashMap<>();
        claims.put("roles", user.isAdmin() ? new String[]{"USER", "ADMIN"} : new String[]{"USER"});
        //claims.put("expiration", date);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getLogin())
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, signKey)
                .compact();
    }

    private Claims obterClaims(String token) throws ExpiredJwtException {
        return Jwts.parser()
                .setSigningKey(signKey)
                .parseClaimsJws(token)
                .getBody();
    }

    public Boolean tokenValido(String token) {
        try {
            Claims claims = obterClaims(token);
            Date expirationDate = claims.getExpiration();
            LocalDateTime date = expirationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            return !LocalDateTime.now().isAfter(date);
        } catch (Exception e) {
            return false;
        }
    }

    public String obterLoginUsuario(String token) throws ExpiredJwtException {
        return (String) obterClaims(token).getSubject();
    }
}
