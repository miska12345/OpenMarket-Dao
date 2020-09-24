package io.openmarket.marketplace.dao;


import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.KeyPair;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.google.common.collect.ImmutableMap;
import io.openmarket.dynamodb.dao.dynamodb.AbstractDynamoDBDao;
import io.openmarket.marketplace.model.Item;
import io.openmarket.transaction.model.Transaction;
import lombok.extern.log4j.Log4j2;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static io.openmarket.config.MerchandiseConfig.*;

@Log4j2
public class ItemDaoImpl extends AbstractDynamoDBDao<Item> implements ItemDao {

    @Inject
    public ItemDaoImpl(AmazonDynamoDB dbClient, DynamoDBMapper mapper) {
        super(dbClient, mapper);
    }

    @Override
    protected boolean validate(Item obj) {
        if (obj.getItemName().isEmpty()) {
            return false;
        } else if (obj.getStock() == 0) {
            return false;
        }
        return true;
    }

    //Todo add exclusive start key and return that to the user after each result

    public List<String> getItemIdsByOrg(@Nonnull final String orgId) {
        List<String> itemIds = new ArrayList<>();
        QueryRequest request = new QueryRequest().withTableName(ITEM_DDB_TABLE_NAME)
                .withIndexName(ITEM_DDB_INDEX_ORGID_2_ITEMID)
                .withKeyConditionExpression("#id = :v")
                .withFilterExpression("#stock > :count")
                .withExpressionAttributeNames(
                        ImmutableMap.of("#id", ITEM_DDB_ATTRIBUTE_BELONG_TO, "#stock", ITEM_DDB_ATTRIBUTE_STOCK)
                )
                .withExpressionAttributeValues(
                        ImmutableMap.of(":v", new AttributeValue(orgId), ":count", new AttributeValue().withN("0"))
                );

        QueryResult items = this.getDbClient().query(request);
        List<Map<String, AttributeValue>> data = items.getItems();
        log.info(data);
        for(Map<String, AttributeValue> item : data) {
            itemIds.add(item.get(ITEM_DDB_ATTRIBUTE_ID).getS());
        }

        return itemIds;
    }

    public void update(UpdateItemRequest request) {
        this.getDbClient().updateItem(request);
    }


    public List<Item> batchLoad(@Nonnull final Collection<String> itemIds) {
        final List<KeyPair> keyPairs = itemIds.stream()
                .map((a) -> new KeyPair().withHashKey(a))
                .collect(Collectors.toList());
        final Map<String, List<Object>> loadResult = this.getDbMapper()
                .batchLoad(ImmutableMap.of(Item.class, keyPairs));
        final List<Object> result = loadResult.get(ITEM_DDB_TABLE_NAME);
        if (result == null)
            return Collections.emptyList();

        return result.stream().map(a -> (Item)a).collect(Collectors.toList());
    }

    @Override
    public Optional<Item> load(String key) {
        return super.load(Item.class, key);
    }
}
