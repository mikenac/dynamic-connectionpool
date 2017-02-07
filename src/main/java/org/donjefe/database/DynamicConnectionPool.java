/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.donjefe.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 * <p>Dynamic Connection Pooling, similar to ADO.NET</p>
 * @author mnacey
 */
public class DynamicConnectionPool {
    
    private static void doQuery (Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM account");
        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next()) {
                    System.out.println(resultSet.getString(1) + 
                            "," + resultSet.getString(2) + "," + resultSet.getString(3));
        }
    }
    
    public static BasicDataSource createDataSource (ConnectionConfig config) {
        
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl(config.dbUrl);
        ds.setUsername(config.dbUser);
        ds.setPassword(config.dbPassword);
        
        ds.setMinIdle(5);
        ds.setMaxIdle(10);
        ds.setMaxOpenPreparedStatements(100);
        
        return ds;
    }
    
    public static void main (String [] args) throws SQLException {
        String dbUrl = "jdbc:mysql://localhost/test";
        String dbUser = "root";
        String dbPass = "bohica";
        
        ConnectionConfig config = new ConnectionConfig();
        config.dbUrl = dbUrl;
        config.dbUser = dbUser;
        config.dbPassword = dbPass;
        
        try {
            try (Connection conn1 = ConnectionPoolManager.getConnection(config.getHash(), DynamicConnectionPool::createDataSource, config)) {
                doQuery(conn1);
            }
            try (Connection conn2 = ConnectionPoolManager.getConnection(config.getHash(), DynamicConnectionPool::createDataSource, config)) {
                doQuery(conn2);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
                    
    }
}
