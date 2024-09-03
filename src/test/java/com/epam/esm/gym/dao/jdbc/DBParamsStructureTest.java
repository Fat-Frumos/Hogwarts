package com.epam.esm.gym.dao.jdbc;

import com.epam.esm.gym.dao.jdbc.tool.DBUtil;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static com.epam.esm.gym.dao.jdbc.tool.DBUtil.getColumnType;
import static com.epam.esm.gym.dao.jdbc.tool.DBUtil.getNotNull;
import static com.epam.esm.gym.dao.jdbc.tool.DBUtil.getPrimaryKeys;
import static com.epam.esm.gym.dao.jdbc.tool.DBUtil.getUnique;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for database schema verification using the {@link DBUtil} class.
 *
 * <p>This test class is designed to validate the existence and structure of various tables and columns
 * in the database, as well as check the constraints and foreign key relationships. It utilizes methods
 * from the {@link DBUtil} class to perform these checks.</p>
 *
 * <p>Each test method is responsible for validating a specific aspect of the database schema, including
 * table existence, column existence, data types, NOT NULL constraints, UNIQUE constraints, primary keys,
 * and foreign keys. The tests are executed against a PostgreSQL database, and assertions are used to
 * verify that the database schema matches the expected structure.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
public class DBParamsStructureTest {

    /**
     * Tests if the 'users' table exists in the database.
     *
     * <p>Validates that the 'users' table is present in the database schema.</p>
     *
     * @throws SQLException if there is an error accessing the database.
     */
    @Test
    void testUserTableExists() throws SQLException {
        assertTrue(DBUtil.isTableExist("users"), "Table 'user' should exist");
    }

    /**
     * Tests if the 'trainee' table exists in the database.
     *
     * <p>Validates that the 'trainee' table is present in the database schema.</p>
     *
     * @throws SQLException if there is an error accessing the database.
     */
    @Test
    void testTraineeTableExists() throws SQLException {
        assertTrue(DBUtil.isTableExist("trainee"), "Table 'trainee' should exist");
    }

    /**
     * Tests if the 'training_type' table exists in the database.
     *
     * <p>Validates that the 'training_type' table is present in the database schema.</p>
     *
     * @throws SQLException if there is an error accessing the database.
     */
    @Test
    void testTrainingTypeTableExists() throws SQLException {
        assertTrue(DBUtil.isTableExist("training_type"), "Table 'training_type' should exist");
    }

    /**
     * Tests if the 'trainer' table exists in the database.
     *
     * <p>Validates that the 'trainer' table is present in the database schema.</p>
     *
     * @throws SQLException if there is an error accessing the database.
     */
    @Test
    void testTrainerTableExists() throws SQLException {
        assertTrue(DBUtil.isTableExist("trainer"), "Table 'trainer' should exist");
    }

    /**
     * Tests if the 'training' table exists in the database.
     *
     * <p>Validates that the 'training' table is present in the database schema.</p>
     *
     * @throws SQLException if there is an error accessing the database.
     */
    @Test
    void testTrainingTableExists() throws SQLException {
        assertTrue(DBUtil.isTableExist("training"), "Table 'training' should exist");
    }

    /**
     * Tests if the 'trainee_trainer' table exists in the database.
     *
     * <p>Validates that the 'trainee_trainer' table is present in the database schema.</p>
     *
     * @throws SQLException if there is an error accessing the database.
     */
    @Test
    void testTraineeTrainerTableExists() throws SQLException {
        assertTrue(DBUtil.isTableExist("trainee_trainer"), "Table 'trainee_trainer' should exist");
    }

    /**
     * Tests constraints and column properties of the 'users' table.
     *
     * <p>Validates the column types, NOT NULL constraints, UNIQUE constraints, and primary key
     * for the 'users' table.</p>
     *
     * @throws SQLException if there is an error accessing the database.
     */
    @Test
    void testUserTableConstraints() throws SQLException {
        assertEquals("integer", getColumnType("users", "id"), "Column 'id' should be of type serial");
        assertTrue(getNotNull("users").contains("first_name"), "Column 'first_name' should be NOT NULL");
        assertTrue(getNotNull("users").contains("last_name"), "Column 'last_name' should be NOT NULL");
        assertTrue(getNotNull("users").contains("username"), "Column 'username' should be NOT NULL");
        assertTrue(getNotNull("users").contains("password"), "Column 'password' should be NOT NULL");
        assertTrue(getNotNull("users").contains("is_active"), "Column 'is_active' should be NOT NULL");
        assertTrue(getUnique("users").contains("username"), "Column 'username' should be UNIQUE");
        assertEquals("id", getPrimaryKeys("users"), "Primary key of 'user' table should be 'id'");
    }

    /**
     * Tests constraints and column properties of the 'training_type' table.
     *
     * <p>Validates the column types, NOT NULL constraints, and primary key for the 'training_type' table.</p>
     *
     * @throws SQLException if there is an error accessing the database.
     */
    @Test
    void testTrainingTypeTableConstraints() throws SQLException {
        assertEquals("integer", getColumnType("training_type", "id"),
                "Column 'id' should be of type serial");
        assertTrue(getNotNull("training_type").contains("training_type_name"),
                "Column 'training_type_name' should be NOT NULL");
        assertEquals("id", getPrimaryKeys("training_type"),
                "Primary key of 'training_type' table should be 'id'");
    }

    /**
     * Tests if required columns exist in the 'role_authorities' table.
     *
     * <p>Validates the existence of 'role_id' and 'authority' columns in the 'role_authorities' table.</p>
     *
     * @throws SQLException if there is an error accessing the database.
     */
    @Test
    void testRoleAuthoritiesTableColumnsExist() throws SQLException {
        assertTrue(isColumnInTableExist("role_authorities", "role_id"),
                "Column role_id is missing in role_authorities");
        assertTrue(isColumnInTableExist("role_authorities", "authority"),
                "Column authority is missing in role_authorities");
    }

    /**
     * Tests if required columns exist in the 'roles' table.
     *
     * <p>Validates the existence of 'id' and 'permission' columns in the 'roles' table.</p>
     *
     * @throws SQLException if there is an error accessing the database.
     */
    @Test
    void testRolesTableColumnsExist() throws SQLException {
        assertTrue(isColumnInTableExist("roles", "id"), "Column id is missing in roles");
        assertTrue(isColumnInTableExist("roles", "permission"), "Column permission is missing in roles");
    }

    /**
     * Tests if required columns exist in the 'users' table.
     *
     * <p>Validates the existence of 'id', 'first_name', 'last_name', 'username', 'password', 'is_active',
     * and 'permission' columns in the 'users' table.</p>
     *
     * @throws SQLException if there is an error accessing the database.
     */
    @Test
    void testUsersTableColumnsExist() throws SQLException {
        assertTrue(isColumnInTableExist("users", "id"), "Column id is missing in users");
        assertTrue(isColumnInTableExist("users", "first_name"), "Column first_name is missing in users");
        assertTrue(isColumnInTableExist("users", "last_name"), "Column last_name is missing in users");
        assertTrue(isColumnInTableExist("users", "username"), "Column username is missing in users");
        assertTrue(isColumnInTableExist("users", "password"), "Column password is missing in users");
        assertTrue(isColumnInTableExist("users", "is_active"), "Column is_active is missing in users");
        assertTrue(isColumnInTableExist("users", "permission"), "Column permission is missing in users");
    }

    /**
     * Tests if required columns exist in the 'trainee' table.
     *
     * <p>Validates the existence of 'id', 'date_of_birth', 'address', and 'user_id' columns in the 'trainee' table.</p>
     *
     * @throws SQLException if there is an error accessing the database.
     */
    @Test
    void testTraineeTableColumnsExist() throws SQLException {
        assertTrue(isColumnInTableExist("trainee", "id"), "Column id is missing in trainee");
        assertTrue(isColumnInTableExist("trainee", "date_of_birth"), "Column date_of_birth is missing in trainee");
        assertTrue(isColumnInTableExist("trainee", "address"), "Column address is missing in trainee");
        assertTrue(isColumnInTableExist("trainee", "user_id"), "Column user_id is missing in trainee");
    }

    /**
     * Tests if required columns exist in the 'trainer' table.
     *
     * <p>Validates the existence of 'id', 'specialization_id', and 'user_id' columns in the 'trainer' table.</p>
     *
     * @throws SQLException if there is an error accessing the database.
     */
    @Test
    void testTrainerTableColumnsExist() throws SQLException {
        assertTrue(isColumnInTableExist("trainer", "id"), "Column id is missing in trainer");
        assertTrue(isColumnInTableExist("trainer", "specialization_id"), "Column specialization_id is missing");
        assertTrue(isColumnInTableExist("trainer", "user_id"), "Column user_id is missing in trainer");
    }

    /**
     * Tests if required columns exist in the 'training' table.
     *
     * <p>Validates the existence of 'id', 'trainee_id', 'trainer_id', 'training_name', 'training_type_id',
     * 'training_date', and 'training_duration' columns in the 'training' table.</p>
     *
     * @throws SQLException if there is an error accessing the database.
     */
    @Test
    void testTrainingTableColumnsExist() throws SQLException {
        assertTrue(isColumnInTableExist("training", "id"),
                "Column id is missing in training");
        assertTrue(isColumnInTableExist("training", "trainee_id"),
                "Column trainee_id is missing in training");
        assertTrue(isColumnInTableExist("training", "trainer_id"),
                "Column trainer_id is missing in training");
        assertTrue(isColumnInTableExist("training", "training_name"),
                "Column training_name is missing in training");
        assertTrue(isColumnInTableExist("training", "training_type_id"),
                "Column training_type_id is missing in training");
        assertTrue(isColumnInTableExist("training", "training_date"),
                "Column training_date is missing in training");
        assertTrue(isColumnInTableExist("training", "training_duration"),
                "Column training_duration is missing in training");
    }

    /**
     * Tests if required columns exist in the 'trainee_trainer' table.
     *
     * <p>Validates the existence of 'trainee_id' and 'trainer_id' columns in the 'trainee_trainer' table.</p>
     *
     * @throws SQLException if there is an error accessing the database.
     */
    @Test
    void testTraineeTrainerTableColumnsExist() throws SQLException {
        assertTrue(isColumnInTableExist("trainee_trainer", "trainee_id"),
                "Column trainee_id is missing in trainee_trainer");
        assertTrue(isColumnInTableExist("trainee_trainer", "trainer_id"),
                "Column trainer_id is missing in trainee_trainer");
    }

    /**
     * Tests if required columns exist in the 'tokens' table.
     *
     * <p>Validates the existence of 'id', 'token_type', 'access_token', 'access_token_ttl', 'revoked',
     * 'expired', and 'user_id' columns in the 'tokens' table.</p>
     *
     * @throws SQLException if there is an error accessing the database.
     */
    @Test
    void testTokensTableColumnsExist() throws SQLException {
        assertTrue(isColumnInTableExist("tokens", "id"),
                "Column id is missing in tokens");
        assertTrue(isColumnInTableExist("tokens", "token_type"),
                "Column token_type is missing in tokens");
        assertTrue(isColumnInTableExist("tokens", "access_token"),
                "Column access_token is missing in tokens");
        assertTrue(isColumnInTableExist("tokens", "access_token_ttl"),
                "Column access_token_ttl is missing in tokens");
        assertTrue(isColumnInTableExist("tokens", "revoked"), "Column revoked is missing in tokens");
        assertTrue(isColumnInTableExist("tokens", "expired"), "Column expired is missing in tokens");
        assertTrue(isColumnInTableExist("tokens", "user_id"), "Column user_id is missing in tokens");
    }

    /**
     * Tests if foreign keys are present in the 'trainee_trainer' table.
     *
     * <p>Validates the presence of foreign keys 'trainee_trainer_trainee_id_fkey' and
     * 'trainee_trainer_trainer_id_fkey' in the 'trainee_trainer' table.</p>
     *
     * @throws SQLException if there is an error accessing the database.
     */
    @Test
    void testTraineeTrainerTableColumnsGetForeignKeys() throws SQLException {
        List<String> foreignKeys = DBUtil.getForeignKeys("trainee_trainer");
        assertTrue(foreignKeys.contains("trainee_trainer_trainee_id_fkey"), "Foreign key for trainee_id is missing");
        assertTrue(foreignKeys.contains("trainee_trainer_trainer_id_fkey"), "Foreign key for trainer_id is missing");
    }

    /**
     * Tests if foreign keys are present in the 'role_authorities' table.
     *
     * <p>Validates the presence of the foreign key 'role_authorities_role_id_fkey' in the 'role_authorities' table.</p>
     *
     * @throws SQLException if there is an error accessing the database.
     */
    @Test
    void testRolesTableForeignKeys() throws SQLException {
        List<String> foreignKeys = DBUtil.getForeignKeys("role_authorities");
        assertTrue(foreignKeys.contains("role_authorities_role_id_fkey"), "Foreign key for role_id is missing");
    }

    /**
     * Tests if foreign keys are present in the 'trainee' table.
     *
     * <p>Validates the presence of the foreign key 'trainee_user_id_fkey' in the 'trainee' table.</p>
     *
     * @throws SQLException if there is an error accessing the database.
     */
    @Test
    void testTraineeTableForeignKeys() throws SQLException {
        List<String> foreignKeys = DBUtil.getForeignKeys("trainee");
        assertTrue(foreignKeys.contains("trainee_user_id_fkey"), "Foreign key for user_id is missing");
    }

    /**
     * Tests if foreign keys are present in the 'trainer' table.
     *
     * <p>Validates the presence of foreign keys 'trainer_specialization_id_fkey' and
     * 'trainer_user_id_fkey' in the 'trainer' table.</p>
     *
     * @throws SQLException if there is an error accessing the database.
     */
    @Test
    void testTrainerTableForeignKeys() throws SQLException {
        List<String> foreignKeys = DBUtil.getForeignKeys("trainer");
        assertTrue(foreignKeys.contains("trainer_specialization_id_fkey"),
                "Foreign key for specialization_id is missing");
        assertTrue(foreignKeys.contains("trainer_user_id_fkey"), "Foreign key for user_id is missing");
    }

    /**
     * Tests if foreign keys are present in the 'training' table.
     *
     * <p>Validates the presence of foreign keys 'training_trainee_id_fkey', 'training_trainer_id_fkey',
     * and 'training_training_type_id_fkey' in the 'training' table.</p>
     *
     * @throws SQLException if there is an error accessing the database.
     */
    @Test
    void testTrainingTableForeignKeys() throws SQLException {
        List<String> foreignKeys = DBUtil.getForeignKeys("training");
        assertTrue(foreignKeys.contains("training_trainee_id_fkey"), "Foreign key for trainee_id is missing");
        assertTrue(foreignKeys.contains("training_trainer_id_fkey"), "Foreign key for trainer_id is missing");
        assertTrue(foreignKeys.contains("training_training_type_id_fkey"), "Foreign key for training_type_id");
    }

    /**
     * Tests if foreign keys are present in the 'trainee_trainer' table.
     *
     * <p>Validates the presence of foreign keys 'trainee_trainer_trainee_id_fkey' and
     * 'trainee_trainer_trainer_id_fkey' in the 'trainee_trainer' table.</p>
     *
     * @throws SQLException if there is an error accessing the database.
     */
    @Test
    void testTraineeTrainerTableForeignKeys() throws SQLException {
        List<String> foreignKeys = DBUtil.getForeignKeys("trainee_trainer");
        assertTrue(foreignKeys.contains("trainee_trainer_trainee_id_fkey"), "Foreign key for trainee_id is missing");
        assertTrue(foreignKeys.contains("trainee_trainer_trainer_id_fkey"), "Foreign key for trainer_id is missing");
    }

    /**
     * Tests if foreign keys are present in the 'tokens' table.
     *
     * <p>Validates the presence of the foreign key 'tokens_user_id_fkey' in the 'tokens' table.</p>
     *
     * @throws SQLException if there is an error accessing the database.
     */
    @Test
    void testTokensTableForeignKeys() throws SQLException {
        List<String> foreignKeys = DBUtil.getForeignKeys("tokens");
        assertTrue(foreignKeys.contains("tokens_user_id_fkey"), "Foreign key for user_id is missing");
    }

    /**
     * Checks if a specific column exists in a given table.
     *
     * <p>This method uses the {@link DBUtil#isColumnInTableExist(String, String)} utility method to query the
     * database and determine whether the specified column is present in the specified table.</p>
     *
     * @param tableName  The name of the table to check.
     * @param columnName The name of the column to check for.
     * @return {@code true} if the column exists in the table; {@code false} otherwise.
     * @throws SQLException if there is an error accessing the database.
     */
    private boolean isColumnInTableExist(String tableName, String columnName) throws SQLException {
        return DBUtil.isColumnInTableExist(tableName, columnName);
    }
}
