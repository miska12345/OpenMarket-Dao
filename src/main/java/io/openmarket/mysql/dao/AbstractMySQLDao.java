package io.openmarket.mysql.dao;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import lombok.AccessLevel;
import lombok.Getter;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Properties;

public class AbstractMySQLDao<T> {
    @Getter(AccessLevel.PROTECTED)
    private final ComboPooledDataSource source;

    @Inject
    public AbstractMySQLDao(@Nonnull ComboPooledDataSource dbsource){
        this.source = dbsource;
    }

}
