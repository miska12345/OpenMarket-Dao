package io.openmarket.organization.dao;

import io.openmarket.dynamodb.dao.dynamodb.DynamoDBDao;
import io.openmarket.organization.model.Organization;

import java.util.Optional;

public interface OrgDao extends DynamoDBDao<Organization> {
    Optional<Organization> getOrganization(String orgName, String projection);
}
