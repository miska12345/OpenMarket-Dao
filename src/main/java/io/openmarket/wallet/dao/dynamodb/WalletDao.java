package io.openmarket.wallet.dao.dynamodb;

import com.amazonaws.services.dynamodbv2.model.TransactWriteItem;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import io.openmarket.dynamodb.dao.dynamodb.DynamoDBDao;
import io.openmarket.wallet.model.Wallet;

import java.util.Collection;

/**
 * WalletDao provides an interface to manipulate the Wallet table.
 */
public interface WalletDao extends DynamoDBDao<Wallet> {
    /**
     * doTransactionWrite takes a Collection of TransactWriteItem and write all to DB.
     * @param items the items to use with transaction write.
     */
    void doTransactionWrite(Collection<TransactWriteItem> items);

    /**
     * update a Wallet entry with the given request.
     * @param request the update request.
     */
    void update(UpdateItemRequest request);
}
