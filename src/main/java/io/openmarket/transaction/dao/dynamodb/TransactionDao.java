package io.openmarket.transaction.dao.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.TransactionWriteRequest;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import io.openmarket.dynamodb.dao.dynamodb.DynamoDBDao;
import io.openmarket.transaction.model.Transaction;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * TransactionDao handle transaction related DDB operations.
 */
public interface TransactionDao extends DynamoDBDao<Transaction> {
    /**
     * getTransactionForPayer get transactions with the given payerId.
     * @param payerId the payerId to get transaction for.
     * @param output the output collection.
     * @param exclusiveStartKey the exclusiveStartKey of DDB query.
     * @return the lastEvaluatedKey of the query, can be used for exclusiveStartKey to retrieve more results.
     */
    Map<String, AttributeValue> getTransactionForPayer(String payerId, Collection<Transaction> output,
                                                       Map<String, AttributeValue> exclusiveStartKey);

    /**
     * getTransactionForRecipient get transactions with the given recipientId.
     * @param recipientId the recipientId to get transaction for.
     * @param output the output collection.
     * @param exclusiveStartKey the exclusiveStartKey of DDB query.
     * @return the lastEvaluatedKey of the query, can be used for exclusiveStartKey to retrieve more results.
     */
    Map<String, AttributeValue> getTransactionForRecipient(String recipientId,
                                                           Collection<Transaction> output,
                                                           Map<String, AttributeValue> exclusiveStartKey);

    /**
     * Use batch to load a collection of {@link Transaction}.
     * @param transactionIds a Collection containing transactions to load.
     * @return a List of Transactions.
     */
    List<Transaction> batchLoad(Collection<String> transactionIds);

    /**
     * Use transaction to write multiple items.
     * @param request the request to do transaction write.
     */
    void transactionWrite(TransactionWriteRequest request);
}
