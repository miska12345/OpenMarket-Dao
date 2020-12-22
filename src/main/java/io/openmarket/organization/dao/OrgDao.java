package io.openmarket.organization.dao;

import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.amazonaws.services.dynamodbv2.model.UpdateItemResult;
import io.openmarket.dynamodb.dao.dynamodb.DynamoDBDao;
import io.openmarket.organization.model.Organization;

import java.util.List;
import java.util.Optional;

public interface OrgDao extends DynamoDBDao<Organization> {
    Optional<Organization> getOrganization(String orgName, String projection);
    UpdateItemResult updateOrg(UpdateItemRequest request);
    List<String> getPosterIds(String orgId);
}
