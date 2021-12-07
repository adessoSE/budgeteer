package de.adesso.budgeteer.rest.security.jwt;

import de.adesso.budgeteer.core.user.domain.User;
import de.adesso.budgeteer.rest.security.jwt.configuration.JwtProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtProperties jwtProperties;

    public String createToken(String username) {
        var issueDate = new Date();
        var expirationDate = new Date(issueDate.getTime() + (jwtProperties.getMaxAge() * 1_000));
        return Jwts.builder()
                .setSubject(username)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(issueDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecret())
                .compact();
    }
}
