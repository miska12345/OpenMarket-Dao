package io.openmarket.wallet.dao.dynamodb;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.TransactWriteItem;
import com.amazonaws.services.dynamodbv2.model.TransactWriteItemsRequest;
import io.openmarket.dynamodb.dao.dynamodb.AbstractDynamoDBDao;
import io.openmarket.wallet.model.Wallet;
import lombok.NonNull;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Optional;

public class WalletDaoImpl extends AbstractDynamoDBDao<Wallet> implements WalletDao {
    @Inject
    public WalletDaoImpl(@NonNull final AmazonDynamoDB dbClient, @NonNull final DynamoDBMapper dbMapper) {
        super(dbClient, dbMapper);
    }

    @Override
    protected boolean validate(final Wallet obj) {
        return true;
    }

    @Override
    public Optional<Wallet> load(final String key) {
        return super.load(Wallet.class, key);
    }

    @Override
    public void doTransactionWrite(@NonNull final Collection<TransactWriteItem> items) {
        getDbClient().transactWriteItems(new TransactWriteItemsRequest().withTransactItems(items));
    }
}
