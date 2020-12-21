package io.openmarket.mysql.dao;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import lombok.AccessLevel;
import lombok.Getter;

import javax.annotation.Nonnull;
import javax.inject.Inject;

public class AbstractMySQLDao {
    @Getter(AccessLevel.PROTECTED)
    private final ComboPooledDataSource source;

    @Inject
    public AbstractMySQLDao(@Nonnull ComboPooledDataSource dbsource){
        this.source = dbsource;
    }
}
