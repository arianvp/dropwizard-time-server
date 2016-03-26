package me.arianvp.time;

import io.dropwizard.auth.Authorizer;

/**
 * Created by arian on 3/26/16.
 */
public class TimeAuthorizer implements Authorizer<User> {
    @Override
    public boolean authorize(User user, String s) {
        return false;
    }
}
