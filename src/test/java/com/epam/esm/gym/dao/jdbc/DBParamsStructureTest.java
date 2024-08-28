package com.epam.esm.gym.dao.jdbc;

import com.epam.esm.gym.dao.jdbc.tool.DBUtil;
import java.sql.SQLException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class DBParamsStructureTest {

    @Test
    void testUserTableExists() throws SQLException {
        assertTrue(DBUtil.isTableExist("user"), "Table 'user' should exist");
    }

    @Test
    void testTraineeTableExists() throws SQLException {
        assertTrue(DBUtil.isTableExist("trainee"), "Table 'trainee' should exist");
    }

    @Test
    void testTrainingTypeTableExists() throws SQLException {
        assertTrue(DBUtil.isTableExist("training_type"), "Table 'training_type' should exist");
    }

    @Test
    void testTrainerTableExists() throws SQLException {
        assertTrue(DBUtil.isTableExist("trainer"), "Table 'trainer' should exist");
    }

    @Test
    void testTrainingTableExists() throws SQLException {
        assertTrue(DBUtil.isTableExist("training"), "Table 'training' should exist");
    }

    @Test
    void testTraineeTrainerTableExists() throws SQLException {
        assertTrue(DBUtil.isTableExist("trainee_trainer"), "Table 'trainee_trainer' should exist");
    }

    @Test
    void testUserTableColumns() throws SQLException {
        assertTrue(DBUtil.isColumnInTableExist("user", "id"), "Column 'id' should exist in 'user' table");
        assertTrue(DBUtil.isColumnInTableExist("user", "first_name"), "Column 'first_name' should exist in 'user' table");
        assertTrue(DBUtil.isColumnInTableExist("user", "last_name"), "Column 'last_name' should exist in 'user' table");
        assertTrue(DBUtil.isColumnInTableExist("user", "username"), "Column 'username' should exist in 'user' table");
        assertTrue(DBUtil.isColumnInTableExist("user", "password"), "Column 'password' should exist in 'user' table");
        assertTrue(DBUtil.isColumnInTableExist("user", "is_active"), "Column 'is_active' should exist in 'user' table");
    }

    @Test
    void testUserTableConstraints() throws SQLException {
        assertEquals("integer", DBUtil.getColumnType("user", "id"), "Column 'id' should be of type serial");
        assertTrue(DBUtil.getNotNull("user").contains("first_name"), "Column 'first_name' should be NOT NULL");
        assertTrue(DBUtil.getNotNull("user").contains("last_name"), "Column 'last_name' should be NOT NULL");
        assertTrue(DBUtil.getNotNull("user").contains("username"), "Column 'username' should be NOT NULL");
        assertTrue(DBUtil.getNotNull("user").contains("password"), "Column 'password' should be NOT NULL");
        assertTrue(DBUtil.getNotNull("user").contains("is_active"), "Column 'is_active' should be NOT NULL");
        assertTrue(DBUtil.getUnique("user").contains("username"), "Column 'username' should be UNIQUE");
        assertEquals("id", DBUtil.getPrimaryKeys("user"), "Primary key of 'user' table should be 'id'");
    }

    @Test
    void testTraineeTableColumns() throws SQLException {
        assertTrue(DBUtil.isColumnInTableExist("trainee", "id"), "Column 'id' should exist in 'trainee' table");
        assertTrue(DBUtil.isColumnInTableExist("trainee", "date_of_birth"), "Column 'date_of_birth' should exist in 'trainee' table");
        assertTrue(DBUtil.isColumnInTableExist("trainee", "address"), "Column 'address' should exist in 'trainee' table");
        assertTrue(DBUtil.isColumnInTableExist("trainee", "user_id"), "Column 'user_id' should exist in 'trainee' table");
    }

    @Test
    void testTrainingTypeTableColumns() throws SQLException {
        assertTrue(DBUtil.isColumnInTableExist("training_type", "id"), "Column 'id' should exist in 'training_type' table");
        assertTrue(DBUtil.isColumnInTableExist("training_type", "training_type_name"), "Column 'training_type_name' should exist in 'training_type' table");
    }

    @Test
    void testTrainingTypeTableConstraints() throws SQLException {
        assertEquals("bigint", DBUtil.getColumnType("training_type", "id"), "Column 'id' should be of type serial");
        assertTrue(DBUtil.getNotNull("training_type").contains("training_type_name"), "Column 'training_type_name' should be NOT NULL");
        assertEquals("id", DBUtil.getPrimaryKeys("training_type"), "Primary key of 'training_type' table should be 'id'");
    }

    @Test
    void testTrainerTableColumns() throws SQLException {
        assertTrue(DBUtil.isColumnInTableExist("trainer", "id"), "Column 'id' should exist in 'trainer' table");
        assertTrue(DBUtil.isColumnInTableExist("trainer", "specialization_id"), "Column 'specialization_id' should exist in 'trainer' table");
        assertTrue(DBUtil.isColumnInTableExist("trainer", "user_id"), "Column 'user_id' should exist in 'trainer' table");
    }

    @Test
    void testTrainingTableColumns() throws SQLException {
        assertTrue(DBUtil.isColumnInTableExist("training", "id"), "Column 'id' should exist in 'training' table");
        assertTrue(DBUtil.isColumnInTableExist("training", "trainee_id"), "Column 'trainee_id' should exist in 'training' table");
        assertTrue(DBUtil.isColumnInTableExist("training", "trainer_id"), "Column 'trainer_id' should exist in 'training' table");
        assertTrue(DBUtil.isColumnInTableExist("training", "training_name"), "Column 'training_name' should exist in 'training' table");
        assertTrue(DBUtil.isColumnInTableExist("training", "training_type_id"), "Column 'training_type_id' should exist in 'training' table");
        assertTrue(DBUtil.isColumnInTableExist("training", "training_date"), "Column 'training_date' should exist in 'training' table");
        assertTrue(DBUtil.isColumnInTableExist("training", "training_duration"), "Column 'training_duration' should exist in 'training' table");
    }

    @Test
    void testTraineeTrainerTableColumns() throws SQLException {
        assertTrue(DBUtil.isColumnInTableExist("trainee_trainer", "trainee_id"), "Column 'trainee_id' should exist in 'trainee_trainer' table");
        assertTrue(DBUtil.isColumnInTableExist("trainee_trainer", "trainer_id"), "Column 'trainer_id' should exist in 'trainee_trainer' table");
    }

    @Test
    void testTraineeTableConstraints() throws SQLException {
        assertEquals("bigint", DBUtil.getColumnType("trainee", "id"), "Column 'id' should be of type serial");
        assertTrue(DBUtil.getForeignKeys("trainee").stream()
                        .anyMatch(fk -> fk.contains("FOREIGN KEY (user_id) REFERENCES \"user\" (id)")),
                "Foreign key constraint on 'user_id' should exist in 'trainee' table");
        assertEquals("id", DBUtil.getPrimaryKeys("trainee"), "Primary key of 'trainee' table should be 'id'");
    }

    @Test
    void testTrainerTableConstraints() throws SQLException {
        assertEquals("bigint", DBUtil.getColumnType("trainer", "id"), "Column 'id' should be of type serial");
        assertTrue(DBUtil.getForeignKeys("trainer").stream()
                        .anyMatch(fk -> fk.contains("FOREIGN KEY (specialization_id) REFERENCES training_type (id)")),
                "Foreign key constraint on 'specialization_id' should exist in 'trainer' table");
        assertTrue(DBUtil.getForeignKeys("trainer").stream()
                        .anyMatch(fk -> fk.contains("FOREIGN KEY (user_id) REFERENCES \"user\" (id)")),
                "Foreign key constraint on 'user_id' should exist in 'trainer' table");
        assertEquals("id", DBUtil.getPrimaryKeys("trainer"), "Primary key of 'trainer' table should be 'id'");
    }

    @Test
    void testTrainingTableConstraints() throws SQLException {
        assertEquals("bigint", DBUtil.getColumnType("training", "id"), "Column 'id' should be of type serial");
        assertTrue(DBUtil.getForeignKeys("training").stream()
                        .anyMatch(fk -> fk.contains("FOREIGN KEY (trainee_id) REFERENCES \"trainee\" (id)")),
                "Foreign key constraint on 'trainee_id' should exist in 'training' table");
        assertTrue(DBUtil.getForeignKeys("training").stream()
                        .anyMatch(fk -> fk.contains("FOREIGN KEY (trainer_id) REFERENCES \"trainer\" (id)")),
                "Foreign key constraint on 'trainer_id' should exist in 'training' table");
        assertTrue(DBUtil.getForeignKeys("training").stream()
                        .anyMatch(fk -> fk.contains("FOREIGN KEY (training_type_id) REFERENCES training_type (id)")),
                "Foreign key constraint on 'training_type_id' should exist in 'training' table");
        assertEquals("id", DBUtil.getPrimaryKeys("training"), "Primary key of 'training' table should be 'id'");
    }

    @Test
    void testTraineeTrainerTableConstraints() throws SQLException {
        List<String> foreignKeys = DBUtil.getForeignKeys("trainee_trainer");

        boolean hasTraineeIdFK = foreignKeys.stream()
                .anyMatch(fk -> fk.contains("FOREIGN KEY (trainee_id) REFERENCES \"trainee\" (id)"));

        boolean hasTrainerIdFK = foreignKeys.stream()
                .anyMatch(fk -> fk.contains("FOREIGN KEY (trainer_id) REFERENCES \"trainer\" (id)"));

        assertTrue(hasTraineeIdFK, "Foreign key constraint on 'trainee_id' should exist in 'trainee_trainer' table");
        assertTrue(hasTrainerIdFK, "Foreign key constraint on 'trainer_id' should exist in 'trainee_trainer' table");
        assertEquals("trainee_id, trainer_id", DBUtil.getPrimaryKeys("trainee_trainer"), "Primary key of 'trainee_trainer' table should be 'trainee_id, trainer_id'");
    }
}
