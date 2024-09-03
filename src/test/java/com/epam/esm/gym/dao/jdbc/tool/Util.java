package com.epam.esm.gym.dao.jdbc.tool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * A utility class for executing SQL queries against a database connection.
 *
 * <p>This class provides methods for executing queries and retrieving results from the database. It uses
 * the provided {@link Connection} to execute queries and process the results.</p>
 *
 * <p>All methods in this class assume that the provided SQL query will return a single result or a list of
 * results in the case of list queries.</p>
 */
public record Util(Connection connection) {

    /**
     * Executes a SQL query that is expected to return a single boolean result.
     *
     * @param query the SQL query to execute
     * @return the boolean result of the query
     * @throws SQLException if there is an error executing the query or if no result is returned
     */
    public boolean executeQueryOneBoolean(String query) throws SQLException {
        try (ResultSet rs = connection.createStatement().executeQuery(query)) {
            if (rs.next()) {
                return rs.getBoolean(1);
            } else {
                throw new SQLException("No result returned for query: " + query);
            }
        }
    }

    /**
     * Executes a SQL query that is expected to return a single string result.
     *
     * @param query the SQL query to execute
     * @return the string result of the query
     * @throws SQLException if there is an error executing the query or if no result is returned
     */
    public String executeQueryOneString(String query) throws SQLException {
        try (ResultSet rs = connection.createStatement().executeQuery(query)) {
            if (rs.next()) {
                return rs.getString(1);
            } else {
                throw new SQLException("No result returned for query: " + query);
            }
        }
    }

    /**
     * Executes a SQL query that returns a list of string results.
     *
     * @param query the SQL query to execute
     * @return a list of string results
     * @throws SQLException if there is an error executing the query
     */
    public List<String> executeQueryListString(String query) throws SQLException {
        List<String> result = new ArrayList<>();
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                result.add(rs.getString(1));
            }
        }
        return result;
    }
}
