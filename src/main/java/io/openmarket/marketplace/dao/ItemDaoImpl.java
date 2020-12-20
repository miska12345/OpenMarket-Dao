package io.openmarket.marketplace.dao;


import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.KeyPair;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.google.common.collect.ImmutableMap;
import io.openmarket.dynamodb.dao.dynamodb.AbstractDynamoDBDao;
import io.openmarket.marketplace.model.Item;
import io.openmarket.mysql.dao.AbstractMySQLDao;
import io.openmarket.transaction.model.Transaction;
import lombok.extern.log4j.Log4j2;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static io.openmarket.config.MerchandiseConfig.*;

@Log4j2
public class ItemDaoImpl extends AbstractMySQLDao implements ItemDao {



    private PreparedStatement getItemIDByOrgID;
    private PreparedStatement getItemByID;
    @Inject
    public ItemDaoImpl(@Nonnull Connection conn) {
        super(conn, "./src/main/java/io/openmarket/marketplace/sql/QueryStatements.properties");
        try {
            this.getItemIDByOrgID = this.getConn().prepareStatement(this.getQuery(QUERY_BY_ORGID));
            this.getItemByID      = this.getConn().prepareStatement(this.getQuery(QUERY_BY_ITEM_ID));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected boolean validate(Item obj) {
        if (obj.getItemName().isEmpty()) {
            return false;
        } else if (obj.getStock() == 0) {
            return false;
        }
        return true;
    }

    //Todo add exclusive start key and return that to the user after each result

    public List<Integer> getItemIdsByOrg(@Nonnull final String orgId) throws SQLException {
        this.getItemIDByOrgID.clearParameters();
        this.getItemIDByOrgID.setString(1,orgId);
        ResultSet rs = this.getItemIDByOrgID.executeQuery();
        List<Integer> ids = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("itemID");
            ids.add(id);
        }
        return ids;
    }

    //Todo same as above, paginate result using exclusiveStartkey
    public List<String> getItemIdByCategory(@Nonnull final String category, int limit) {
//        List<String> itemIds = new ArrayList<>();
//        QueryRequest request = new QueryRequest().withTableName(ITEM_DDB_TABLE_NAME)
//                .withIndexName(ITEM_DDB_INDEX_ITEMCATEGORY_ITEMPURCHASED)
//                .withScanIndexForward(false)
//                .withLimit(limit)
//                .withKeyConditionExpression("#id = :v")
//                .withExpressionAttributeNames(
//                        ImmutableMap.of("#id", ITEM_DDB_ATTRIBUTE_CATEGORY)
//                )
//                .withExpressionAttributeValues(
//                        ImmutableMap.of(":v", new AttributeValue().withS(category))
//                );
//        QueryResult result = this.getDbClient().query(request);
//
//        for(Map<String, AttributeValue> item : result.getItems()) {
//            itemIds.add(item.get(ITEM_DDB_ATTRIBUTE_ID).getS());
//        }
//        return itemIds;
        return null;
    }

    public List<Item> batchLoad(@Nonnull final Collection<Integer> itemIds) throws SQLException{
        List<Item> result = new ArrayList<>();
        for (int id : itemIds) {
            try {
                this.getItemByID.clearParameters();
                this.getItemByID.setInt(1, id);
                ResultSet rs = this.getItemByID.executeQuery();
                if (!rs.next()) continue;
                Item item = Item.builder().itemID(rs.getString("itemID"))
                        .itemName(rs.getString("itemName"))
                        .itemPrice(rs.getDouble("itemPrice"))
                        .itemCategory(rs.getString("itemCategory"))
                        .itemImageLink(rs.getString("itemImageLink"))
                        .itemDescription(rs.getString("itemDescription"))
                        .purchasedCount(rs.getInt("purchasedCount"))
                        .stock(rs.getInt("stock"))
                        .belongTo(rs.getString("belongTo")).build();
                result.add(item);
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

//    @Override
//    public Optional<Item> load(String key) {
//        return super.load(Item.class, key);
//    }
}
