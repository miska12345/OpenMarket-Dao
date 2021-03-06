package io.openmarket.transaction.dao.dynamodb;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.TransactionWriteRequest;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import com.amazonaws.services.dynamodbv2.local.shared.access.AmazonDynamoDBLocal;
import com.amazonaws.services.dynamodbv2.model.*;
import com.google.common.collect.ImmutableList;
import io.openmarket.transaction.model.Transaction;
import io.openmarket.transaction.model.TransactionStatus;
import io.openmarket.transaction.model.TransactionType;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;

import static io.openmarket.config.TransactionConfig.*;
import static org.junit.jupiter.api.Assertions.*;

public class TransactionDaoImplTest {
    private static final String PAYER_ID = "123";
    private static AmazonDynamoDBLocal localDBClient;
    private AmazonDynamoDB dbClient;
    private DynamoDBMapper dbMapper;
    private TransactionDaoImpl transactionDao;

    @BeforeAll
    public static void setupLocalDB() {
        localDBClient = DynamoDBEmbedded.create();
    }

    @BeforeEach
    public void setup() {
        dbClient = localDBClient.amazonDynamoDB();
        dbMapper = new DynamoDBMapper(dbClient);
        transactionDao = new TransactionDaoImpl(dbClient, dbMapper);
        createTable();
    }

    @AfterEach
    public void reset() {
        dbClient.deleteTable(TRANSACTION_DDB_TABLE_NAME);
    }

    @Test
    public void when_SaveTransaction_then_Exists_In_DB() {
        Transaction transaction = Transaction.builder().transactionId("123").payerId("123").status(TransactionStatus.PENDING)
                .recipientId("123").amount(10.0).currencyId("123").type(TransactionType.TRANSFER).build();
        transactionDao.save(transaction);

        ScanResult result = dbClient.scan(new ScanRequest().withTableName(TRANSACTION_DDB_TABLE_NAME));
        assertEquals(1, result.getItems().size());
    }

    @Test
    public void can_Load_Transaction_when_Exists() {
        Transaction transaction = Transaction.builder().transactionId("123").payerId("123").status(TransactionStatus.PENDING)
                .recipientId("123").currencyId("123").amount(3.1).type(TransactionType.TRANSFER).build();
        transactionDao.save(transaction);

        Optional<Transaction> opTransaction = transactionDao.load("123");
        assertTrue(opTransaction.isPresent());

        Transaction transaction1 = opTransaction.get();
        assertEquals(transaction.getTransactionId(), transaction1.getTransactionId());
        assertEquals(transaction.getCurrencyId(), transaction1.getCurrencyId());
        assertEquals(transaction.getAmount(), transaction1.getAmount());
        assertEquals(transaction.getPayerId(), transaction1.getPayerId());
        assertEquals(transaction.getStatus(), transaction1.getStatus());
    }

    @Test
    public void cannot_Load_If_Not_Exists() {
        Optional<Transaction> opTransaction = transactionDao.load("123");
        assertFalse(opTransaction.isPresent());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 5})
    public void can_Load_All_Transactions_For_Payer(int count) {
        Map<String, Transaction> transactionMap = new HashMap<>();
        for (int i = 0; i < count; i++) {
            Transaction transaction = generateRandomTransaction();
            transactionMap.put(transaction.getTransactionId(), transaction);
            transactionDao.save(transaction);
        }
        Set<Transaction> result = new HashSet<>();
        transactionDao.getTransactionForPayer(PAYER_ID, result, null);
        assertEquals(transactionMap.size(), result.size());

        for (Transaction tran : result) {
            assertTrue(transactionMap.containsKey(tran.getTransactionId()));
        }
    }

    @Test
    public void cannot_Load_Transaction_For_Invalid_Payer() {
        Set<Transaction> result = new HashSet<>();
        transactionDao.getTransactionForPayer("1", result, null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void do_Throw_IllegalArgumentException_If_Negative_Amount() {
        Transaction transaction = Transaction.builder().transactionId("123").payerId("123").status(TransactionStatus.PENDING)
                .recipientId("123").amount(-10.0).currencyId("123").type(TransactionType.TRANSFER).build();
        assertThrows(IllegalArgumentException.class, () -> transactionDao.save(transaction));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 10})
    public void test_BatchLoad(int count) {
        Set<String> ids = new HashSet<>();
        for (int i = 0; i < count; i++) {
            Transaction transac = generateRandomTransaction();
            ids.add(transac.getTransactionId());
            transactionDao.save(transac);
        }
        List<Transaction> transactionList = transactionDao.batchLoad(ids);
        assertEquals(ids.size(), transactionList.size());
        transactionList.forEach(a -> {
            assertTrue(ids.contains(a.getTransactionId()));
        });
    }

    @Test
    public void test_transactionWrite() {
        Transaction a = generateRandomTransaction();
        Transaction b = generateRandomTransaction();
        a.setStatus(TransactionStatus.COMPLETED);
        b.setStatus(TransactionStatus.ERROR);

        transactionDao.transactionWrite(new TransactionWriteRequest().addPut(a).addPut(b));

        a = transactionDao.load(a.getTransactionId()).get();
        b = transactionDao.load(b.getTransactionId()).get();

        assertEquals(TransactionStatus.COMPLETED, a.getStatus());
        assertEquals(TransactionStatus.ERROR, b.getStatus());
    }

    @Test
    public void test_Outdated_Save_Throws_Exception() {
        Transaction a = generateRandomTransaction();
        transactionDao.save(a);

        Transaction b = transactionDao.load(a.getTransactionId()).get();
        b.setStatus(TransactionStatus.REFUNDED);
        transactionDao.save(b);

        a.setStatus(TransactionStatus.COMPLETED);
        assertThrows(ConditionalCheckFailedException.class, () -> transactionDao.save(a));
    }

    @AfterAll
    public static void tearDown() {
        localDBClient.shutdown();
    }

    private Transaction generateRandomTransaction() {
        return Transaction.builder().transactionId(UUID.randomUUID().toString())
                .payerId(PAYER_ID)
                .status(TransactionStatus.PENDING)
                .recipientId("123")
                .currencyId("123")
                .amount(3.14)
                .type(TransactionType.TRANSFER)
                .build();
    }

    private static void createTable() {
        ProvisionedThroughput throughput = new ProvisionedThroughput(5L, 5L);
        localDBClient.amazonDynamoDB().createTable(new CreateTableRequest().withTableName(TRANSACTION_DDB_TABLE_NAME)
                .withKeySchema(ImmutableList.of(new KeySchemaElement(TRANSACTION_DDB_ATTRIBUTE_ID, KeyType.HASH)))
                .withAttributeDefinitions(new AttributeDefinition(TRANSACTION_DDB_ATTRIBUTE_ID, ScalarAttributeType.S),
                        new AttributeDefinition(TRANSACTION_DDB_ATTRIBUTE_PAYER_ID, ScalarAttributeType.S),
                        new AttributeDefinition(TRANSACTION_DDB_ATTRIBUTE_CREATED_AT, ScalarAttributeType.S))
                .withGlobalSecondaryIndexes(new GlobalSecondaryIndex()
                        .withIndexName(TRANSACTION_DDB_INDEX_NAME)
                        .withKeySchema(new KeySchemaElement()
                                        .withAttributeName(TRANSACTION_DDB_ATTRIBUTE_PAYER_ID)
                                        .withKeyType(KeyType.HASH),
                                new KeySchemaElement()
                                        .withKeyType(KeyType.RANGE)
                                        .withAttributeName(TRANSACTION_DDB_ATTRIBUTE_CREATED_AT))
                        .withProjection(new Projection().withProjectionType(ProjectionType.INCLUDE)
                                .withNonKeyAttributes(TRANSACTION_DDB_ATTRIBUTE_ID))
                        .withProvisionedThroughput(throughput))
                .withProvisionedThroughput(throughput));
    }
}
