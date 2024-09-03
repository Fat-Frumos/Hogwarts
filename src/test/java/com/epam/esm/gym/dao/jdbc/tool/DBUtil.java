package com.epam.esm.gym.dao.jdbc.tool;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/**
 * Utility class for database operations.
 *
 * <p>This class provides static methods for interacting with a PostgreSQL database, including
 * checking table and column existence, retrieving column types, and querying primary and foreign
 * keys. It uses a utility class {@link Util} for executing SQL queries and managing database connections.</p>
 *
 * <p>The class initializes a static {@link Util} instance in a static block by establishing a
 * database connection using the {@link #getConnection()} method. It relies on properties loaded
 * from the "db.properties" file for configuring the connection.</p>
 *
 * <p>Each method throws {@link SQLException} in case of database access errors. Runtime exceptions
 * are thrown if there are issues with loading properties or establishing the connection.</p>
 *
 * @author Pavlo Poliak
 * @version 1.0.0
 * @since 1.0
 */
public class DBUtil {

    private static final Util util;

    static {
        try {
            util = new Util(getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Establishes and returns a connection to the PostgreSQL database.
     *
     * <p>This method loads database connection properties from the "db.properties" file, which should
     * be located in the classpath. It uses these properties to create a connection to the PostgreSQL database.</p>
     *
     * @return a {@link Connection} object to the PostgreSQL database.
     * @throws SQLException if there is an error establishing the connection.
     */
    public static Connection getConnection() throws SQLException {
        try (InputStream input = DBUtil.class.getClassLoader().getResourceAsStream("db.properties")) {

            Properties prop = new Properties();
            prop.load(input);
            return DriverManager
                    .getConnection(
                            String.format("jdbc:postgresql://%s:%s/%s",
                                    prop.getProperty("hostname"),
                                    prop.getProperty("port"),
                                    prop.getProperty("db.name")),
                            prop.getProperty("db.user"),
                            prop.getProperty("db.password"));
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    /**
     * Checks if a table exists in the database.
     *
     * <p>This method queries the PostgreSQL catalog to determine if a table with the specified name
     * exists in the database.</p>
     *
     * @param tableName the name of the table to check.
     * @return {@code true} if the table exists, {@code false} otherwise.
     * @throws SQLException if there is an error executing the query.
     */
    public static boolean isTableExist(String tableName) throws SQLException {
        return util.executeQueryOneBoolean(String.format("SELECT EXISTS ( SELECT 1 FROM pg_tables\n" +
                "            WHERE tablename = '%s');", tableName));
    }

    /**
     * Retrieves the data type of a specific column in a table.
     *
     * <p>This method queries the information schema to get the data type of the specified column
     * in the given table.</p>
     *
     * @param table the name of the table.
     * @param name  the name of the column.
     * @return the data type of the column.
     * @throws SQLException if there is an error executing the query.
     */
    public static String getColumnType(String table, String name) throws SQLException {
        return util.executeQueryOneString(String.format("SELECT data_type\n" +
                "FROM information_schema.columns WHERE table_name = '%s' and column_name = '%s';", table, name));
    }

    /**
     * Retrieves the primary key columns of a table.
     *
     * <p>This method queries the PostgreSQL catalog to get the primary key columns of the specified
     * table.</p>
     *
     * @param tableName the name of the table.
     * @return a comma-separated string of primary key column names.
     * @throws SQLException if there is an error executing the query.
     */
    public static String getPrimaryKeys(String tableName) throws SQLException {
        return util.executeQueryOneString(String.format("""
                SELECT pg_attribute.attname
                    FROM pg_class, pg_attribute, pg_index
                    WHERE pg_class.oid = pg_attribute.attrelid AND
                    pg_class.oid = pg_index.indrelid AND
                    pg_index.indkey[0] = pg_attribute.attnum AND
                    pg_index.indisprimary = 't' and pg_class.relname = '%s';""", tableName));
    }

    /**
     * Retrieves the foreign key constraints for a table.
     *
     * <p>This method queries the PostgreSQL catalog to get the foreign key constraints for the specified
     * table. It returns a list of constraint names and definitions.</p>
     *
     * @param tableName the name of the table.
     * @return a list of strings representing the foreign key constraints.
     * @throws SQLException if there is an error executing the query.
     */
    public static List<String> getForeignKeys(String tableName) throws SQLException {
        String query = String.format("""
                SELECT conname AS constraint_name,
                       pg_catalog.pg_get_constraintdef(c.oid, true) AS constraint_definition
                FROM pg_catalog.pg_constraint c
                WHERE c.conrelid = '%s'::regclass
                  AND c.contype = 'f'
                """, tableName);

        return util.executeQueryListString(query);
    }

    /**
     * Retrieves the columns that are defined as NOT NULL in a table.
     *
     * <p>This method queries the information schema to get the columns that have a NOT NULL constraint
     * in the specified table.</p>
     *
     * @param tableName the name of the table.
     * @return a list of column names that are defined as NOT NULL.
     * @throws SQLException if there is an error executing the query.
     */
    public static List<String> getNotNull(String tableName) throws SQLException {
        return util.executeQueryListString(String.format("""
                SELECT column_name FROM information_schema.columns
                WHERE is_nullable = 'NO' and table_name = '%s';
                """, tableName));
    }

    /**
     * Retrieves the unique columns in a table.
     *
     * <p>This method queries the information schema to get the columns that have a UNIQUE constraint
     * in the specified table.</p>
     *
     * @param tableName the name of the table.
     * @return a list of column names that have a UNIQUE constraint.
     * @throws SQLException if there is an error executing the query.
     */
    public static List<String> getUnique(String tableName) throws SQLException {
        return util.executeQueryListString(String.format("""
                SELECT column_name
                    FROM information_schema.constraint_column_usage
                    WHERE table_name = '%s'
                    AND constraint_name IN (
                            SELECT constraint_name
                    FROM information_schema.table_constraints
                            WHERE constraint_type = 'UNIQUE'
                    );""", tableName));
    }

    /**
     * Checks if a column exists in a table.
     *
     * <p>This method queries the information schema to determine if the specified column exists in the
     * given table.</p>
     *
     * @param tableName  the name of the table.
     * @param columnName the name of the column.
     * @return {@code true} if the column exists, {@code false} otherwise.
     * @throws SQLException if there is an error executing the query.
     */
    public static boolean isColumnInTableExist(String tableName, String columnName) throws SQLException {
        return util.executeQueryOneBoolean(String.format("""
                        SELECT EXISTS (SELECT 1 FROM information_schema.columns
                                       WHERE table_schema='public' AND table_name='%s' AND column_name='%s');""",
                tableName, columnName));
    }
}
