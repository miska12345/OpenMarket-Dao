package io.openmarket.order.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class ItemInfoConverter implements DynamoDBTypeConverter<String, List<ItemInfo>> {
    private static final Gson GSON = new Gson();
    @Override
    public String convert(List<ItemInfo> items) {
        return GSON.toJson(items);
    }

    @Override
    public List<ItemInfo> unconvert(String objectsString) {
        Type listType = new TypeToken<ArrayList<ItemInfo>>(){}.getType();
        return GSON.fromJson(objectsString, listType);
    }
}