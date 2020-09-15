package io.openmarket.account.dynamodb;


import io.openmarket.account.model.Account;
import io.openmarket.dynamodb.dao.dynamodb.DynamoDBDao;

public interface UserDao extends DynamoDBDao<Account> {
}
