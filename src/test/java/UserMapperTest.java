import me.arianvp.time.core.User;
import me.arianvp.time.db.UserMapper;
import org.junit.Test;
import org.skife.jdbi.v2.StatementContext;


import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

import static org.assertj.core.api.Assertions.*;
/**
 * Created by arian on 3/27/16.
 */
public class UserMapperTest {
    @Test
    public void itMapsAUser() throws SQLException {
        UserMapper mapper = new UserMapper();

        ResultSet resultSet = mock(ResultSet.class);

        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("name")).thenReturn("username");
        when(resultSet.getString("password")).thenReturn("password");
        StatementContext statementContext = mock(StatementContext.class);
        int i =0;

        assertThat(mapper.map(i,resultSet,statementContext))
                .isEqualToComparingFieldByField(new User(1,"username","password"));
    }
}
