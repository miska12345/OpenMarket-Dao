package io.openmarket.dagger.module;

import dagger.Module;
import dagger.Provides;
import lombok.NonNull;

import java.util.Map;
import java.util.TreeMap;

@Module
public class EnvMap extends TreeMap<String, String> {
    @Provides
    public EnvMap provideMap() {
        return this;
    }

    /**
     * Use the given map to resolve environmental variables.
     * @param map a Map containing environmental variable name to value pairs.
     * @return this.
     */
    public EnvMap withMap(@NonNull final Map<String, String> map) {
        this.putAll(map);
        return this;
    }
}
