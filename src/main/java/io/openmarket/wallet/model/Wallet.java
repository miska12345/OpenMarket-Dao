package io.openmarket.wallet.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.*;

import java.util.Map;

import static io.openmarket.config.WalletConfig.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = WALLET_DDB_TABLE_NAME)
public class Wallet {
    @DynamoDBHashKey(attributeName = WALLET_DDB_ATTRIBUTE_OWNER_ID)
    @NonNull private String ownerId;

    @DynamoDBAttribute(attributeName = WALLET_DDB_ATTRIBUTE_COIN_MAP)
    @NonNull private Map<String, Double> coins;

    @DynamoDBTypeConvertedEnum
    @DynamoDBAttribute(attributeName = WALLET_DDB_ATTRIBUTE_TYPE)
    private WalletType type;
}
