package io.openmarket.marketplace.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nonnull;

import java.util.Date;

import static io.openmarket.config.MerchandiseConfig.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = ITEM_DDB_TABLE_NAME)
public class Item {
    @DynamoDBAutoGeneratedKey
    @DynamoDBHashKey(attributeName = ITEM_DDB_ATTRIBUTE_ID)
    private String itemID;

    @DynamoDBAttribute(attributeName = ITEM_DDB_ATTRIBUTE_NAME)
    @Nonnull private String itemName;

    @DynamoDBAttribute(attributeName = ITEM_DDB_ATTRIBUTE_BELONG_TO)
    @Nonnull private String belongTo;

    @DynamoDBAttribute(attributeName = ITEM_DDB_ATTRIBUTE_STOCK)
    private int stock;

    @DynamoDBAttribute(attributeName = ITEM_DDB_ATTRIBUTE_PURCHASE_COUNT)
    private int purchasedCount;

    @DynamoDBAttribute(attributeName = ITEM_DDB_ATTRIBUTE_PRICE)
    private Double itemPrice;

    @DynamoDBAttribute(attributeName = ITEM_DDB_ATTRIBUTE_DESCRIPTION)
    private String itemDescription;

    @DynamoDBAttribute(attributeName = ITEM_DDB_ATTRIBUTE_IMAGE_LINK)
    private String itemImageLink;

    @DynamoDBAutoGeneratedTimestamp(strategy = DynamoDBAutoGenerateStrategy.CREATE)
    @DynamoDBTypeConvertedTimestamp
    @DynamoDBAttribute(attributeName = ITEM_DDB_ATTRIBUTE_CREATE_AT)
    private Date createAt;



}
