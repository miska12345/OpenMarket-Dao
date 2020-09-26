package io.openmarket.order.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class ItemInfoConverter implements DynamoDBTypeConverter<String, List<ItemInfo>> {

    @Override
    public String convert(List<ItemInfo> objects) {
        //Jackson object mapper
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String objectsString = objectMapper.writeValueAsString(objects);
            return objectsString;
        } catch (JsonProcessingException e) {
            //do something
        }
        return null;
    }

    @Override
    public List<ItemInfo> unconvert(String objectsString) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<ItemInfo> objects = objectMapper.readValue(objectsString, new TypeReference<List<ItemInfo>>(){});
            return objects;
        } catch (JsonParseException e) {
            //do something
            System.out.println(e);
        } catch (JsonMappingException e) {
            //do something
            System.out.println(e);
        } catch (IOException e) {
            //do something
            System.out.println(e);
        }
        return null;
    }
}