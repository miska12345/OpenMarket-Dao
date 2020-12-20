package io.openmarket.mysql.dao;

import lombok.AccessLevel;
import lombok.Getter;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.sql.Connection;
import java.util.Properties;

public class AbstractMySQLDao {
    @Getter(AccessLevel.PROTECTED)
    private final Connection conn;

    @Getter(AccessLevel.PROTECTED)
    private Properties querystatements;

    @Inject
    public AbstractMySQLDao(@Nonnull Connection dbconn){
        this.conn = dbconn;
    }

}
