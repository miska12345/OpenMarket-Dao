package io.openmarket.transaction.dao.dynamodb;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTransactionWriteExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.KeyPair;
import com.amazonaws.services.dynamodbv2.datamodeling.TransactionWriteRequest;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.google.common.collect.ImmutableMap;
import io.openmarket.dynamodb.dao.dynamodb.AbstractDynamoDBDao;
import io.openmarket.transaction.model.Transaction;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

import static io.openmarket.config.TransactionConfig.*;

@Log4j2
public class TransactionDaoImpl extends AbstractDynamoDBDao<Transaction> implements TransactionDao {
    private static final String DDB_QUERY_VALUE = ":val";
    @Inject
    public TransactionDaoImpl(@NonNull final AmazonDynamoDB dbClient, @NonNull final DynamoDBMapper dbMapper) {
        super(dbClient, dbMapper);
    }

    public Optional<Transaction> load(@NonNull final String key) {
        return super.load(Transaction.class, key);
    }

    @Override
    protected boolean validate(final Transaction transaction) {
        return !transaction.getTransactionId().isEmpty()
                && !transaction.getPayerId().isEmpty()
                && !transaction.getRecipientId().isEmpty()
                && !transaction.getCurrencyId().isEmpty()
                && transaction.getAmount() > 0;

    }

    @Override
    public Map<String, AttributeValue> getTransactionForPayer(@NonNull final String payerId,
                                                    @NonNull final Collection<Transaction> output,
                                                    final Map<String, AttributeValue> exclusiveStartKey) {
        final QueryRequest request = new QueryRequest()
                .withTableName(TRANSACTION_DDB_TABLE_NAME)
                .withIndexName(TRANSACTION_DDB_INDEX_NAME)
                .withExpressionAttributeValues(ImmutableMap.of(DDB_QUERY_VALUE, new AttributeValue(payerId)))
                .withKeyConditionExpression(String.format("%s = %s", TRANSACTION_DDB_ATTRIBUTE_PAYER_ID,
                        DDB_QUERY_VALUE))
                .withExclusiveStartKey(exclusiveStartKey);
        final QueryResult queryResult = getDbClient().query(request);
        queryResult.getItems().forEach(a -> output.add(load(a.get(TRANSACTION_DDB_ATTRIBUTE_ID).getS()).get()));

        log.info("Loaded {} transactions for payer with Id '{}'", queryResult.getCount(), payerId);
        return queryResult.getLastEvaluatedKey();
    }

    @Override
    public List<Transaction> batchLoad(@NonNull final Collection<String> transactionIds) {
        final List<KeyPair> keyPairs = transactionIds.stream()
                .map(a -> new KeyPair().withHashKey(a)).collect(Collectors.toList());
        final Map<String, List<Object>> loadResult = getDbMapper()
                .batchLoad(ImmutableMap.of(Transaction.class, keyPairs));
        final List<Object> transactionList = loadResult.get(TRANSACTION_DDB_TABLE_NAME);
        if (transactionList == null) {
            return Collections.emptyList();
        }
        return transactionList.stream().map(a -> (Transaction) a).collect(Collectors.toList());
    }

    public void transactionWrite(@NonNull final TransactionWriteRequest request) {
        getDbMapper().transactionWrite(request);
    }
}
