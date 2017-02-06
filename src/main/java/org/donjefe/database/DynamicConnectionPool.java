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
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 *
 * @author mnacey
 */
public class DynamicConnectionPool {
    private volatile Map<Integer,BasicDataSource> connectionPools;
    
    public DynamicConnectionPool () {
        connectionPools = new HashMap<>();
        
    }
    
    public BasicDataSource getConnectionPool (String connectionUrl, String user, 
            String password) {
        int connectionHash = getConnectionHash (connectionUrl, user, password);
        
        if (connectionPools.containsKey(connectionHash)) {
            return connectionPools.get(connectionHash);
        } else {
            BasicDataSource ds = new BasicDataSource();
            ds.setUrl(connectionUrl);
            ds.setUsername(user);
            ds.setPassword(password);
            
            ds.setMinIdle(5);
            ds.setMaxIdle(10);
            ds.setMaxOpenPreparedStatements(100);
            
            connectionPools.put(connectionHash, ds);
            return ds;
        }
    }
    
    public int getConnectionHash (String connectionUrl, String user, 
            String password) {
        String combo = connectionUrl.concat(user).concat(password);
        return combo.hashCode();
    }
    
    private static void doQuery (Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM account");
        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next()) {
                    System.out.println(resultSet.getString(1) + 
                            "," + resultSet.getString(2) + "," + resultSet.getString(3));
        }
    }
    
    public static void main (String [] args) throws SQLException {
        DynamicConnectionPool dcp = new DynamicConnectionPool();
        String dbUrl = "jdbc:mysql://localhost/test";
        String dbUser = "root";
        String dbPass = "bohica";
 
        try {
            try (Connection conn1 = dcp.getConnectionPool(dbUrl, dbUser, dbPass).getConnection()) {
                doQuery(conn1);
            }
            try (Connection conn2 = dcp.getConnectionPool(dbUrl, dbUser, dbPass).getConnection()) {
                doQuery(conn2);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
                    
    }
}
