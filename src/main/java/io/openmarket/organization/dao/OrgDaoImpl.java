package io.openmarket.organization.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.google.common.collect.ImmutableMap;
import io.openmarket.dynamodb.dao.dynamodb.AbstractDynamoDBDao;
import io.openmarket.organization.model.Organization;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;
import java.util.Set;

import static io.openmarket.config.OrgConfig.*;

public class OrgDaoImpl extends AbstractDynamoDBDao<Organization> implements OrgDao {

    @Inject
    public OrgDaoImpl(AmazonDynamoDB dbClient, DynamoDBMapper dbMapper) {
        super(dbClient, dbMapper);
    }

    @Override
    public Optional<Organization> getOrganization(String orgName, String projection) {
        return Optional.empty();
    }

    @Override
    public Optional<Organization> load(String key) {
        return super.load(Organization.class, key);
    }


    @Override
    protected boolean validate(Organization obj) {
        if (obj.getOrgName() == null) return false;

        return true;
    }



}
