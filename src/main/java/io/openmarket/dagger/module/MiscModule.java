package io.openmarket.dagger.module;

import com.google.gson.Gson;
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
}
