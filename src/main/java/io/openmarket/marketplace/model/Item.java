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
@DynamoDBTable(tableName = MER_DDB_TABLE_NAME)
public class Item {
    @DynamoDBAutoGeneratedKey
    @DynamoDBHashKey(attributeName = MER_DDB_ATTRIBUTE_ID)
    private String itemID;

    @DynamoDBAttribute(attributeName = MER_DDB_ATTRIBUTE_NAME)
    @Nonnull private String itemName;

    @DynamoDBAttribute(attributeName = MER_DDB_ATTRIBUTE_STOCK)
    @Nonnull private int stock;

    @DynamoDBAttribute(attributeName = MER_DDB_ATTRIBUTE_PURCHASE_COUNT)
    private int purchasedCount;

    @DynamoDBAutoGeneratedTimestamp(strategy = DynamoDBAutoGenerateStrategy.CREATE)
    @DynamoDBTypeConvertedTimestamp
    @DynamoDBAttribute(attributeName = MER_DDB_ATTRIBUTE_CREATE_AT)
    private Date createAt;



}
