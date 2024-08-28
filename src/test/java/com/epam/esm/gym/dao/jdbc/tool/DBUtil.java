package com.epam.esm.gym.dao.jdbc.tool;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class DBUtil {

    private static final Util util;

    static {
        try {
            util = new Util(getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() throws SQLException {
        try (InputStream input = DBUtil.class.getClassLoader().getResourceAsStream("db.properties")) {

            Properties prop = new Properties();
            prop.load(input);
            return DriverManager
                    .getConnection(
                            String.format("jdbc:postgresql://%s:%s/%s", prop.getProperty("hostname"), prop.getProperty("port"),
                                    prop.getProperty("db.name")),
                            prop.getProperty("db.user"), prop.getProperty("db.password"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isTableExist(String tableName) throws SQLException {
        return util.executeQueryOneBoolean(String.format("SELECT EXISTS ( SELECT 1 FROM pg_tables\n" +
                "            WHERE tablename = '%s');", tableName));
    }

    public static String getColumnType(String tableName, String columnName) throws SQLException {
        return util.executeQueryOneString(String.format("SELECT data_type\n" +
                "FROM information_schema.columns WHERE table_name = '%s' and column_name = '%s';", tableName, columnName));
    }

    public static String getPrimaryKeys(String tableName) throws SQLException {
        return util.executeQueryOneString(String.format("""
                SELECT pg_attribute.attname
                    FROM pg_class, pg_attribute, pg_index
                    WHERE pg_class.oid = pg_attribute.attrelid AND
                    pg_class.oid = pg_index.indrelid AND
                    pg_index.indkey[0] = pg_attribute.attnum AND
                    pg_index.indisprimary = 't' and pg_class.relname = '%s';""", tableName));
    }

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

    public static List<String> getNotNull(String tableName) throws SQLException {
        return util.executeQueryListString(String.format("""
                SELECT column_name FROM information_schema.columns
                WHERE is_nullable = 'NO' and table_name = '%s';
                """, tableName));
    }

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

    public static boolean isColumnInTableExist(String tableName, String columnName) throws SQLException {
        return util.executeQueryOneBoolean(String.format("""
                        SELECT EXISTS (SELECT 1 FROM information_schema.columns
                                       WHERE table_schema='public' AND table_name='%s' AND column_name='%s');""",
                tableName, columnName));
    }
}
