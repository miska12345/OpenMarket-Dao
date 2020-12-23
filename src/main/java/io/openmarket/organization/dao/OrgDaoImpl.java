package io.openmarket.organization.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.*;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import io.openmarket.dynamodb.dao.dynamodb.AbstractDynamoDBDao;
import io.openmarket.organization.model.Organization;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.openmarket.config.OrgConfig.*;

@Log4j2
public class OrgDaoImpl extends AbstractDynamoDBDao<Organization> implements OrgDao {



    @Inject
    public OrgDaoImpl(AmazonDynamoDB dbClient, DynamoDBMapper dbMapper) {
        super(dbClient, dbMapper);
    }

    //TODO look into this method later
    @Override
    public Optional<Organization> getOrganization(String orgName, String projection) {
        return Optional.empty();
    }

    @Override
    public Optional<Organization> load(String key) {
        return super.load(Organization.class, key);
    }

    public UpdateItemResult updateOrg(UpdateItemRequest request) {
        return this.getDbClient().updateItem(request);
    }



    public List<String> getFollowerIds(String orgId) {
        final Map<String,String> KEY = ImmutableMap.of("#col", ORG_DDB_KEY_ORGNAME);
        final Map<String, AttributeValue> VAL = ImmutableMap.of(":id", new AttributeValue(orgId));
        QueryRequest getPosters = new QueryRequest().withTableName(ORG_DDB_TABLE_NAME)
                .withExpressionAttributeNames(KEY)
                .withExpressionAttributeValues(VAL)
                .withKeyConditionExpression("#col = :id")
                .withProjectionExpression(ORG_DDB_ATTRIBUTE_FOLLOWERS);
        QueryResult queryRS = this.getDbClient().query(getPosters);
        return queryRS.getItems().get(0).get(ORG_DDB_ATTRIBUTE_FOLLOWERS).getSS();
    }

    @Override
    protected boolean validate(Organization obj) {
        if (obj.getOrgName() == null) return false;


        return true;
    }



}
