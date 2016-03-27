package me.arianvp.time.resources;

import io.dropwizard.auth.Auth;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import me.arianvp.time.core.Login;
import me.arianvp.time.core.User;
import me.arianvp.time.db.UserDao;
import org.mindrot.jbcrypt.BCrypt;

import javax.crypto.SecretKey;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;


/**
 * Created by arian on 3/27/16.
 */

@Path("/token")
public class TokenResource {


    private final SecretKey key;
    private final JwtBuilder builder;
    private final UserDao dao;
    private final JwtParser parser;

    public TokenResource(UserDao dao, SecretKey key) {
        this.dao = dao;
        this.key = key;
        this.builder = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256,key);
        this.parser = Jwts.parser().setSigningKey(key);
    }

    /**
     * Creates a new JWT Token
     * @param login a username and password
     * @return a new jwt token
     */

    @Path("/new")
    @POST
    public String newToken(Login login) {

        User user = dao.getUserByName(login.getUsername());

        if (BCrypt.checkpw(login.getPassword(), user.getPassword())) {
            return builder
                    .setSubject(login.getUsername())
                    .compact();
        } else {
            throw new WebApplicationException("Invalid username or password", Response.Status.UNAUTHORIZED);
        }
    }

    /**
     * Renews a token.
     * @return a new unexpired token
     */
    @Path("/renew")
    @POST
    public String renewToken(@Auth User user) {
        return builder.setSubject(user.getName()).compact();
    }
}
