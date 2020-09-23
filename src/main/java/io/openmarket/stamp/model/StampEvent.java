package io.openmarket.stamp.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGenerateStrategy;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedTimestamp;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import static io.openmarket.config.StampEventConfig.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = EVENT_DDB_TABLE_NAME)
public class StampEvent {
    @Builder.Default
    @DynamoDBHashKey
    @DynamoDBAttribute(attributeName = EVENT_DDB_ATTRIBUTE_ID)
    @NonNull private String eventId = UUID.randomUUID().toString();

    @DynamoDBAttribute(attributeName = EVENT_DDB_ATTRIBUTE_OWNER_ID)
    @NonNull private String ownerId;

    @Builder.Default
    @DynamoDBTypeConvertedEnum
    @DynamoDBAttribute(attributeName = EVENT_DDB_ATTRIBUTE_OWNER_TYPE)
    @NonNull private EventOwnerType type = EventOwnerType.ORGANIZATION;

    @DynamoDBAttribute(attributeName = EVENT_DDB_ATTRIBUTE_PARTICIPANTS)
    private Set<String> participants;

    @DynamoDBAttribute(attributeName = EVENT_DDB_ATTRIBUTE_EXPIRE_AT)
    @NonNull private Date expireAt;

    @DynamoDBAttribute(attributeName = EVENT_DDB_ATTRIBUTE_CREATED_AT)
    @DynamoDBAutoGeneratedTimestamp(strategy = DynamoDBAutoGenerateStrategy.CREATE)
    private Date createdAt;

    @DynamoDBAttribute(attributeName = EVENT_DDB_ATTRIBUTE_CURRENCY_ID)
    @NonNull private String currencyId;

    @DynamoDBAttribute(attributeName = EVENT_DDB_ATTRIBUTE_REWARD_AMOUNT)
    @NonNull private Double rewardAmount;

    @DynamoDBAttribute(attributeName = EVENT_DDB_ATTRIBUTE_TOTAL_AMOUNT)
    @NonNull private Double totalAmount;

    @DynamoDBAttribute(attributeName = EVENT_DDB_ATTRIBUTE_REMAINING_AMOUNT)
    @NonNull private Double remainingAmount;

    @Builder.Default
    @DynamoDBAttribute(attributeName = EVENT_DDB_ATTRIBUTE_SUCCESS_MESSAGE)
    @NonNull private String messageOnSuccess = EVENT_DEFAULT_SUCCESS_MESSAGE;

    @Builder.Default
    @DynamoDBAttribute(attributeName = EVENT_DDB_ATTRIBUTE_ERROR_MESSAGE)
    @NonNull private String messageOnError = EVENT_DEFAULT_ERROR_MESSAGE;
}
