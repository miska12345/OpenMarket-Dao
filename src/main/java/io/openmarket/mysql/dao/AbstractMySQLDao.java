package io.openmarket.mysql.dao;

import lombok.AccessLevel;
import lombok.Getter;

import javax.annotation.Nonnull;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

public class AbstractMySQLDao {
    @Getter(AccessLevel.PROTECTED)
    private final Connection conn;
    @Getter(AccessLevel.PROTECTED)
    private Properties querystatements;

    public AbstractMySQLDao(Connection dbconn, @Nonnull String queryFilePath){
        this.conn = dbconn;
        this.querystatements = new Properties();
        try {
            this.querystatements.load(new FileInputStream(queryFilePath));
        } catch(IOException e) {
            throw new RuntimeException("Query File Not Found");
        }
    }

    public String getQuery(String queryName) {
        return this.querystatements.getProperty(queryName);
    }
}
