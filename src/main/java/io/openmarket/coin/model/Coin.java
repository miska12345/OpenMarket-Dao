package io.openmarket.coin.model;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import static io.openmarket.config.CoinConfig.*;
import static io.openmarket.config.OrderConfig.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = COIN_DDB_TABLE_NAME)
public class Coin {
    @DynamoDBHashKey
    @DynamoDBAttribute(attributeName = COIN_DDB_ATTRIBUTE_ID)
    private String coinId;

    @DynamoDBAttribute(attributeName = COIN_DDB_ATTRIBUTE_NAME)
    private String name;

    @DynamoDBAttribute(attributeName = COIN_DDB_ATTRIBUTE_ICON)
    private String icon;

    @DynamoDBAttribute(attributeName = COIN_DDB_ATTRIBUTE_NET)
    private String netAmount;
}
