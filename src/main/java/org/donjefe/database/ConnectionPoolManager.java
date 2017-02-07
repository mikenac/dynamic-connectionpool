/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.donjefe.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 *
 * @author mnacey
 */
public class ConnectionPoolManager {
    public static final Map<Integer, BasicDataSource> connectionPools = new ConcurrentHashMap<>();
    
    public static Connection getConnection (int connectionStringHash, Function<ConnectionConfig, BasicDataSource> defaultDataSourceProvider, 
            ConnectionConfig config) throws SQLException {
        if (!connectionPools.containsKey(connectionStringHash)) {
            connectionPools.putIfAbsent(connectionStringHash, defaultDataSourceProvider.apply(config));
        }
        return connectionPools.get(connectionStringHash).getConnection();
    }
}
