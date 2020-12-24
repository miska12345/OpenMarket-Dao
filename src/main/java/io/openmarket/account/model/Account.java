package io.openmarket.account.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGenerateStrategy;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedTimestamp;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedTimestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.annotation.Nonnull;
import java.util.Date;
import java.util.Set;

import static io.openmarket.config.AccountConfig.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = USER_DDB_TABLE_NAME)
public class Account {


    @DynamoDBHashKey(attributeName = USER_DDB_ATTRIBUTE_USERNAME)
    @Nonnull
    @NonNull private String username;

    @DynamoDBAttribute(attributeName = USER_DDB_ATTRIBUTE_PORTRAITKEY)
    @Nonnull
    private String portraitS3Key;

    @DynamoDBAttribute(attributeName = USER_DDB_ATTRIBUTE_PASSWORDHASH)
    @NonNull private String passwordHash;

    @DynamoDBAttribute(attributeName = USER_DDB_ATTRIBUTE_PASSWORDSALT)
    @NonNull private String passwordSalt;

    @DynamoDBAttribute(attributeName = USER_DDB_ATTRIBUTE_DISPLAYNAME)
    @NonNull private String displayName;

    @DynamoDBAutoGeneratedTimestamp(strategy = DynamoDBAutoGenerateStrategy.CREATE)
    @DynamoDBTypeConvertedTimestamp
    @DynamoDBAttribute(attributeName = USER_DDB_ATTRIBUTE_CREATEAT)
    private Date createAt;

    @DynamoDBAttribute(attributeName = USER_DDB_ATTRIBUTE_FOLLOWING)
    private Set<String> allFollowing;

    @DynamoDBAutoGeneratedTimestamp(strategy = DynamoDBAutoGenerateStrategy.ALWAYS)
    @DynamoDBTypeConvertedTimestamp
    @DynamoDBAttribute(attributeName = USER_DDB_ATTRIBUTE_LASTUPDATEDAT)
    private Date lastUpdatedAt;



    //TODO add the list of organization that it belongs to
}
