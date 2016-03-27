package me.arianvp.time;

import com.fasterxml.jackson.databind.SerializationFeature;
import io.dropwizard.Application;
import io.dropwizard.java8.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.java8.Java8Bundle;
import io.dropwizard.java8.auth.oauth.OAuthCredentialAuthFilter;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.jsonwebtoken.SignatureAlgorithm;
import me.arianvp.time.auth.JwtAuthenticator;
import me.arianvp.time.core.Login;
import me.arianvp.time.core.User;
import me.arianvp.time.db.UserDao;
import me.arianvp.time.resources.TokenResource;
import org.mindrot.jbcrypt.BCrypt;
import org.skife.jdbi.v2.DBI;
import me.arianvp.time.resources.TimeResource;
import me.arianvp.time.resources.UserResource;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by arian on 3/26/16.
 */
public class TimeApplication extends Application<TimeConfiguration>{

    @Override
    public void initialize(Bootstrap<TimeConfiguration> bootstrap) {
        bootstrap.addBundle(new MigrationsBundle<TimeConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(TimeConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
        bootstrap.addBundle(new Java8Bundle());
        bootstrap.getObjectMapper()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    }

    @Override
    public void run(TimeConfiguration config, Environment environment) throws Exception {
        final DBIFactory factory = new DBIFactory();
        final DBI dbi = factory.build(environment, config.getDataSourceFactory(), "sqlite");
        final UserDao userDao = dbi.onDemand(UserDao.class);
        final JerseyEnvironment jersey = environment.jersey();

        final SecretKey key = new SecretKeySpec(config.getSecretKey().getBytes("UTF-8"), SignatureAlgorithm.HS512.getJcaName());

        final OAuthCredentialAuthFilter<User> authFilter = new OAuthCredentialAuthFilter.Builder<User>()
                .setAuthenticator(new JwtAuthenticator(key, userDao))
                .setPrefix("Bearer")
                .setRealm("NOO")
                .buildAuthFilter();

        jersey.register(new AuthDynamicFeature(authFilter));
        jersey.register(new AuthValueFactoryProvider.Binder<>(User.class));
        jersey.register(new TimeResource(java.time.Clock.systemUTC()));

        jersey.register(new TokenResource(userDao, key));
        jersey.register(new UserResource(userDao));
    }

    @Override
    public String getName() {
        return "time";
    }

    public static void main(String[]args) throws Exception {
        new TimeApplication().run(args);
    }
}
