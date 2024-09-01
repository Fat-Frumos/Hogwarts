package com.epam.esm.gym.dao.jdbc;

import com.epam.esm.gym.dao.jdbc.tool.DBUtil;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DBParamsStructureTest {

    @Test
    void testUserTableExists() throws SQLException {
        assertTrue(DBUtil.isTableExist("users"), "Table 'user' should exist");
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
    void testUserTableConstraints() throws SQLException {
        assertEquals("integer", DBUtil.getColumnType("users", "id"), "Column 'id' should be of type serial");
        assertTrue(DBUtil.getNotNull("users").contains("first_name"), "Column 'first_name' should be NOT NULL");
        assertTrue(DBUtil.getNotNull("users").contains("last_name"), "Column 'last_name' should be NOT NULL");
        assertTrue(DBUtil.getNotNull("users").contains("username"), "Column 'username' should be NOT NULL");
        assertTrue(DBUtil.getNotNull("users").contains("password"), "Column 'password' should be NOT NULL");
        assertTrue(DBUtil.getNotNull("users").contains("is_active"), "Column 'is_active' should be NOT NULL");
        assertTrue(DBUtil.getUnique("users").contains("username"), "Column 'username' should be UNIQUE");
        assertEquals("id", DBUtil.getPrimaryKeys("users"), "Primary key of 'user' table should be 'id'");
    }

    @Test
    void testTrainingTypeTableConstraints() throws SQLException {
        assertEquals("integer", DBUtil.getColumnType("training_type", "id"), "Column 'id' should be of type serial");
        assertTrue(DBUtil.getNotNull("training_type").contains("training_type_name"), "Column 'training_type_name' should be NOT NULL");
        assertEquals("id", DBUtil.getPrimaryKeys("training_type"), "Primary key of 'training_type' table should be 'id'");
    }

    @Test
    void testRoleAuthoritiesTableColumnsExist() throws SQLException {
        assertTrue(isColumnInTableExist("role_authorities", "role_id"), "Column role_id is missing in role_authorities");
        assertTrue(isColumnInTableExist("role_authorities", "authority"), "Column authority is missing in role_authorities");
    }

    @Test
    void testRolesTableColumnsExist() throws SQLException {
        assertTrue(isColumnInTableExist("roles", "id"), "Column id is missing in roles");
        assertTrue(isColumnInTableExist("roles", "permission"), "Column permission is missing in roles");
    }

    @Test
    void testUsersTableColumnsExist() throws SQLException {
        assertTrue(isColumnInTableExist("users", "id"), "Column id is missing in users");
        assertTrue(isColumnInTableExist("users", "first_name"), "Column first_name is missing in users");
        assertTrue(isColumnInTableExist("users", "last_name"), "Column last_name is missing in users");
        assertTrue(isColumnInTableExist("users", "username"), "Column username is missing in users");
        assertTrue(isColumnInTableExist("users", "password"), "Column password is missing in users");
        assertTrue(isColumnInTableExist("users", "is_active"), "Column is_active is missing in users");
        assertTrue(isColumnInTableExist("users", "permission"), "Column permission is missing in users");
        assertTrue(isColumnInTableExist("users", "role_id"), "Column role_id is missing in users");
    }

    @Test
    void testTraineeTableColumnsExist() throws SQLException {
        assertTrue(isColumnInTableExist("trainee", "id"), "Column id is missing in trainee");
        assertTrue(isColumnInTableExist("trainee", "date_of_birth"), "Column date_of_birth is missing in trainee");
        assertTrue(isColumnInTableExist("trainee", "address"), "Column address is missing in trainee");
        assertTrue(isColumnInTableExist("trainee", "user_id"), "Column user_id is missing in trainee");
    }

    @Test
    void testTrainerTableColumnsExist() throws SQLException {
        assertTrue(isColumnInTableExist("trainer", "id"), "Column id is missing in trainer");
        assertTrue(isColumnInTableExist("trainer", "specialization_id"), "Column specialization_id is missing in trainer");
        assertTrue(isColumnInTableExist("trainer", "user_id"), "Column user_id is missing in trainer");
    }

    @Test
    void testTrainingTableColumnsExist() throws SQLException {
        assertTrue(isColumnInTableExist("training", "id"), "Column id is missing in training");
        assertTrue(isColumnInTableExist("training", "trainee_id"), "Column trainee_id is missing in training");
        assertTrue(isColumnInTableExist("training", "trainer_id"), "Column trainer_id is missing in training");
        assertTrue(isColumnInTableExist("training", "training_name"), "Column training_name is missing in training");
        assertTrue(isColumnInTableExist("training", "training_type_id"), "Column training_type_id is missing in training");
        assertTrue(isColumnInTableExist("training", "training_date"), "Column training_date is missing in training");
        assertTrue(isColumnInTableExist("training", "training_duration"), "Column training_duration is missing in training");
    }

    @Test
    void testTraineeTrainerTableColumnsExist() throws SQLException {
        assertTrue(isColumnInTableExist("trainee_trainer", "trainee_id"), "Column trainee_id is missing in trainee_trainer");
        assertTrue(isColumnInTableExist("trainee_trainer", "trainer_id"), "Column trainer_id is missing in trainee_trainer");
    }

    @Test
    void testTokensTableColumnsExist() throws SQLException {
        assertTrue(isColumnInTableExist("tokens", "id"), "Column id is missing in tokens");
        assertTrue(isColumnInTableExist("tokens", "token_type"), "Column token_type is missing in tokens");
        assertTrue(isColumnInTableExist("tokens", "access_token"), "Column access_token is missing in tokens");
        assertTrue(isColumnInTableExist("tokens", "access_token_ttl"), "Column access_token_ttl is missing in tokens");
        assertTrue(isColumnInTableExist("tokens", "revoked"), "Column revoked is missing in tokens");
        assertTrue(isColumnInTableExist("tokens", "expired"), "Column expired is missing in tokens");
        assertTrue(isColumnInTableExist("tokens", "user_id"), "Column user_id is missing in tokens");
    }

    @Test
    void testTraineeTrainerTableColumnsGetForeignKeys() throws SQLException {
        List<String> foreignKeys = DBUtil.getForeignKeys("trainee_trainer");
        assertTrue(foreignKeys.contains("trainee_trainer_trainee_id_fkey"), "Foreign key for trainee_id is missing");
        assertTrue(foreignKeys.contains("trainee_trainer_trainer_id_fkey"), "Foreign key for trainer_id is missing");
    }

    @Test
    void testRolesTableForeignKeys() throws SQLException {
        List<String> foreignKeys = DBUtil.getForeignKeys("role_authorities");
        assertTrue(foreignKeys.contains("role_authorities_role_id_fkey"), "Foreign key for role_id is missing");
    }

    @Test
    void testUsersTableForeignKeys() throws SQLException {
        List<String> foreignKeys = DBUtil.getForeignKeys("users");
        assertTrue(foreignKeys.contains("users_role_id_fkey"), "Foreign key for role_id is missing");
    }

    @Test
    void testTraineeTableForeignKeys() throws SQLException {
        List<String> foreignKeys = DBUtil.getForeignKeys("trainee");
        assertTrue(foreignKeys.contains("trainee_user_id_fkey"), "Foreign key for user_id is missing");
    }

    @Test
    void testTrainerTableForeignKeys() throws SQLException {
        List<String> foreignKeys = DBUtil.getForeignKeys("trainer");
        assertTrue(foreignKeys.contains("trainer_specialization_id_fkey"), "Foreign key for specialization_id is missing");
        assertTrue(foreignKeys.contains("trainer_user_id_fkey"), "Foreign key for user_id is missing");
    }

    @Test
    void testTrainingTableForeignKeys() throws SQLException {
        List<String> foreignKeys = DBUtil.getForeignKeys("training");
        assertTrue(foreignKeys.contains("training_trainee_id_fkey"), "Foreign key for trainee_id is missing");
        assertTrue(foreignKeys.contains("training_trainer_id_fkey"), "Foreign key for trainer_id is missing");
        assertTrue(foreignKeys.contains("training_training_type_id_fkey"), "Foreign key for training_type_id is missing");
    }

    @Test
    void testTraineeTrainerTableForeignKeys() throws SQLException {
        List<String> foreignKeys = DBUtil.getForeignKeys("trainee_trainer");
        assertTrue(foreignKeys.contains("trainee_trainer_trainee_id_fkey"), "Foreign key for trainee_id is missing");
        assertTrue(foreignKeys.contains("trainee_trainer_trainer_id_fkey"), "Foreign key for trainer_id is missing");
    }

    @Test
    void testTokensTableForeignKeys() throws SQLException {
        List<String> foreignKeys = DBUtil.getForeignKeys("tokens");
        assertTrue(foreignKeys.contains("tokens_user_id_fkey"), "Foreign key for user_id is missing");
    }

    private boolean isColumnInTableExist(String tableName, String columnName) throws SQLException {
        return DBUtil.isColumnInTableExist(tableName, columnName);
    }
}
