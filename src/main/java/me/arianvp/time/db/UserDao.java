package me.arianvp.time.db;

import me.arianvp.time.core.User;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;


/**
 * Created by arian on 3/26/16.
 */

@RegisterMapper(UserMapper.class)
public interface UserDao {
    @SqlQuery("select id, name from users where id = :id")
    User get(@Bind("id") int id);

    @SqlQuery("select id, name from users where name = :name")
    User getUserByName(@Bind("name") String name);

    @SqlUpdate("insert into users (name, password) values name = :name, password = :password")
    void insert(@BindBean User user);
}
