package me.arianvp.time;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;


/**
 * Created by arian on 3/26/16.
 */

@RegisterMapper(UserMapper.class)
public interface UserDao {
    @SqlQuery("select id, name from users where id = :id")
    User getUserById(@Bind("id") int id);
}
