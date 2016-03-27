package me.arianvp.time.resources;

import io.dropwizard.auth.Auth;
import me.arianvp.time.core.User;
import me.arianvp.time.db.UserDao;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;


/**
 * Created by arian on 3/26/16.
 */
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    private final UserDao userDao;

    public UserResource(UserDao userDao) {
        this.userDao = userDao;
    }

    @GET
    @Path("/{id}")
    public User getUser(@Auth User user, @PathParam("id")int id) {
        return userDao.get(id);
    }
}
