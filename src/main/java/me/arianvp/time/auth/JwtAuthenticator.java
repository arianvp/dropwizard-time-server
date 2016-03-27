package me.arianvp.time.auth;

import io.dropwizard.auth.AuthenticationException;
import me.arianvp.time.core.User;


import java.util.Optional;


/**
 * Created by arian on 3/26/16.
 */
public class JwtAuthenticator implements io.dropwizard.java8.auth.Authenticator<String, User> {

    @Override
    public Optional<User> authenticate(String s) throws AuthenticationException {
        return null;
    }
}
