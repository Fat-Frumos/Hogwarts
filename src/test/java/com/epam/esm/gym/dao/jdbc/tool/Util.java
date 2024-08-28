package com.epam.esm.gym.dao.jdbc.tool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public record Util(Connection connection) {

    public boolean executeQueryOneBoolean(String query) throws SQLException {
        try (ResultSet rs = connection.createStatement().executeQuery(query)) {
            if (rs.next()) {
                return rs.getBoolean(1);
            } else {
                throw new SQLException("No result returned for query: " + query);
            }
        }
    }

    public String executeQueryOneString(String query) throws SQLException {
        try (ResultSet rs = connection.createStatement().executeQuery(query)) {
            if (rs.next()) {
                return rs.getString(1);
            } else {
                throw new SQLException("No result returned for query: " + query);
            }
        }
    }

    public List<String> executeQueryListString(String query) throws SQLException {
        List<String> result = new ArrayList<>();
        try (Statement st = connection().createStatement();
             ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                result.add(rs.getString(1));
            }
        }
        return result;
    }
}
