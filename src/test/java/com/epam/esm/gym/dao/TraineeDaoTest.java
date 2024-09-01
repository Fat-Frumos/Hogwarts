package com.epam.esm.gym.dao;

import com.epam.esm.gym.dao.jdbc.JDBCUserDao;
import com.epam.esm.gym.dao.jdbc.tool.DBUtil;
import com.epam.esm.gym.domain.Trainee;
import com.epam.esm.gym.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class TraineeDaoTest {

    @Autowired
    private JDBCUserDao userDao;

    User bilbo = User.builder()
            .id(1)
            .firstName("Bilbo")
            .lastName("Begging")
            .username("Bilbo.Begging")
            .password("password123")
            .active(true)
            .build();

    Trainee trainee = Trainee.builder()
            .id(1L)
            .dateOfBirth(LocalDate.parse("1980-07-31"))
            .address("Hogwarts")
            .user(bilbo)
            .trainings(new HashSet<>())
            .build();

//    @Test
    @Transactional
    void testSaveNewUser() throws SQLException {
        User saved = userDao.update(bilbo);
        try (Connection connection = DBUtil.getConnection()) {
            String sql = "SELECT * FROM users WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, bilbo.getUsername());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        assertEquals(bilbo.getFirstName(), resultSet.getString("first_name"));
                        assertEquals(bilbo.getLastName(), resultSet.getString("last_name"));
                        assertEquals(bilbo.getUsername(), resultSet.getString("username"));
                        assertEquals(bilbo.getPassword(), resultSet.getString("password"));
                        assertTrue(resultSet.getBoolean("is_active"));
                    } else {
                        throw new AssertionError("User not found in database.");
                    }
                }
            }
        }
    }
}
