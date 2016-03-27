package me.arianvp.time.auth;

import io.dropwizard.auth.AuthenticationException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import me.arianvp.time.core.User;
import me.arianvp.time.db.UserDao;


import javax.crypto.SecretKey;
import java.util.Optional;


/**
 * Created by arian on 3/26/16.
 */
public class JwtAuthenticator implements io.dropwizard.java8.auth.Authenticator<String, User> {

    private final UserDao dao;
    private final JwtParser parser;

    public JwtAuthenticator(SecretKey key, UserDao dao) {
        this.dao = dao;
        this.parser = Jwts.parser().setSigningKey(key);
    }
    @Override
    public Optional<User> authenticate(String s) throws AuthenticationException {
        try {
            String name = parser.parseClaimsJws(s).getBody().getSubject();
            return Optional.of(dao.getUserByName(name));
        } catch( SignatureException | MalformedJwtException e) {
            return Optional.empty();
        }


    }
}
