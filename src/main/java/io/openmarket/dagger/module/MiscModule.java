package io.openmarket.dagger.module;

import com.google.gson.Gson;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import dagger.Module;
import dagger.Provides;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

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
    SessionFactory provideSessionFactory() {
        Configuration cfg = new Configuration();
        cfg.configure();
        cfg.setProperty("hibernate.connection.url", System.getenv("DB_URL"));
        cfg.setProperty("hibernate.connection.username", System.getenv("DB_USER"));
        cfg.setProperty("hibernate.connection.password", System.getenv("DB_PASS"));
        cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        return cfg.buildSessionFactory();
    }
}
