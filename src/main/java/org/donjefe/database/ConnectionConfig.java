/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.donjefe.database;

/**
 *
 * @author mnacey
 */
public class ConnectionConfig {
    public String dbUrl;
    public String dbUser;
    public String dbPassword;
    
    
    public int getHash () {
        return dbUrl.concat(dbUser).concat(dbPassword).hashCode();
    }
}
