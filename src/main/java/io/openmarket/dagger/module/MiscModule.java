package io.openmarket.dagger.module;

import com.google.gson.Gson;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class MiscModule {
    @Provides
    @Singleton
    Gson provideGson() {
        return new Gson();
    }

    @Provides
    @Singleton
    ComboPooledDataSource provideComboPooledDataSource() {
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        cpds.setJdbcUrl(System.getenv("DB_URL"));
        System.out.println(System.getenv("DB_URL"));
        System.out.println(System.getenv("DB_USER"));
        System.out.println(System.getenv("DB_PASS"));
        cpds.setUser(System.getenv("DB_USER"));
        cpds.setPassword(System.getenv("DB_PASS"));
        cpds.setInitialPoolSize(5);
        cpds.setMinPoolSize(5);
        cpds.setAcquireIncrement(5);
        cpds.setMaxPoolSize(20);
        cpds.setMaxStatements(100);
        return cpds;
    }
}
