package io.openmarket.wallet.dao.dynamodb;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import com.amazonaws.services.dynamodbv2.local.shared.access.AmazonDynamoDBLocal;
import com.amazonaws.services.dynamodbv2.model.*;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.openmarket.wallet.model.Wallet;
import io.openmarket.wallet.model.WalletType;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.openmarket.config.WalletConfig.*;
import static org.junit.jupiter.api.Assertions.*;

public class WalletDaoImplTest {
    private static final String OWNER_ID = "123";
    private static final String CURRENCY_ID = "666";
    private static AmazonDynamoDBLocal localDBClient;
    private AmazonDynamoDB dbClient;
    private DynamoDBMapper dbMapper;
    private WalletDaoImpl walletDao;

    @BeforeAll
    public static void setupLocalDB() {
        localDBClient = DynamoDBEmbedded.create();
    }

    @BeforeEach
    public void setup() {
        dbClient = localDBClient.amazonDynamoDB();
        dbMapper = new DynamoDBMapper(dbClient);
        walletDao = new WalletDaoImpl(dbClient, dbMapper);
        createTable();
    }

    @AfterEach
    public void reset() {
        dbClient.deleteTable(WALLET_DDB_TABLE_NAME);
    }

    @AfterAll
    public static void tearDown() {
        localDBClient.shutdown();
    }

    @Test
    public void test_Save_Wallet() {
        Wallet wallet = Wallet.builder().ownerId(OWNER_ID)
                .coins(ImmutableMap.of("123", 190.0)).type(WalletType.USER).build();
        walletDao.save(wallet);

        ScanRequest request = new ScanRequest().withTableName(WALLET_DDB_TABLE_NAME);
        ScanResult result = dbClient.scan(request);
        assertEquals(1, result.getCount());
    }

    @Test
    public void can_Load_Wallet_If_Exists() {
        Wallet wallet = Wallet.builder().ownerId(OWNER_ID).coins(ImmutableMap.of("123", 190.0))
                .type(WalletType.USER).build();
        walletDao.save(wallet);

        assertTrue(walletDao.load(OWNER_ID).isPresent());
    }

    @Test
    public void cannot_Load_Wallet_If_Not_Exists() {
        assertFalse(walletDao.load(OWNER_ID).isPresent());
    }

    @Test
    public void test_Transaction_Write() {
        List<TransactWriteItem> items = ImmutableList.of(
                new TransactWriteItem().withPut(new Put()
                        .withTableName(WALLET_DDB_TABLE_NAME)
                        .withItem(ImmutableMap.of(WALLET_DDB_ATTRIBUTE_OWNER_ID, new AttributeValue("123")))
                )
        );
        assertDoesNotThrow(() -> walletDao.doTransactionWrite(items));
    }

    @Test
    public void test_Transaction_Write_Failed() {
        Wallet wallet = Wallet.builder().ownerId(OWNER_ID).coins(ImmutableMap.of("123", 190.0))
                .type(WalletType.USER).build();
        walletDao.save(wallet);
        Map<String, AttributeValue> key = ImmutableMap.of("OwnerId", new AttributeValue(OWNER_ID));
        Map<String, String> attributeNameMap = ImmutableMap.of("#cm", "CoinMap", "#cid", "123");
        Map<String, AttributeValue> attributeValueMap = ImmutableMap.of(":val", new AttributeValue().withN("100"));
        List<TransactWriteItem> items = ImmutableList.of(
                new TransactWriteItem().withUpdate(new Update()
                        .withTableName(WALLET_DDB_TABLE_NAME)
                        .withUpdateExpression("SET #cm.#cid = :val")
                        .withConditionExpression("#cm.#cid < :val")
                        .withExpressionAttributeNames(attributeNameMap)
                        .withExpressionAttributeValues(attributeValueMap)
                        .withKey(key)
                )
        );
        assertThrows(TransactionCanceledException.class, () -> walletDao.doTransactionWrite(items));
    }

    @Test
    public void test_Update() {
        Wallet wallet = generateWallet();
        wallet.getCoins().put(CURRENCY_ID, 0.0);
        walletDao.save(wallet);

        walletDao.update(new UpdateItemRequest().withTableName(WALLET_DDB_TABLE_NAME)
                .withUpdateExpression("SET #cm.#coin = if_not_exists(#cm.#coin, :zero)")
                .withExpressionAttributeNames(ImmutableMap.of("#cm", WALLET_DDB_ATTRIBUTE_COIN_MAP, "#coin",
                        CURRENCY_ID))
                .withExpressionAttributeValues(ImmutableMap.of(":zero", new AttributeValue().withN("0")))
                .withKey(ImmutableMap.of(WALLET_DDB_ATTRIBUTE_OWNER_ID, new AttributeValue(OWNER_ID)))
        );
        wallet = walletDao.load(wallet.getOwnerId()).get();
        assertEquals(0.0, wallet.getCoins().get(CURRENCY_ID));
    }

    private Wallet generateWallet() {
        return Wallet.builder().ownerId(OWNER_ID).coins(new HashMap<>())
                .type(WalletType.USER).build();
    }

    private static void createTable() {
        ProvisionedThroughput throughput = new ProvisionedThroughput(5L, 5L);
        localDBClient.amazonDynamoDB().createTable(new CreateTableRequest().withTableName(WALLET_DDB_TABLE_NAME)
                .withKeySchema(ImmutableList.of(new KeySchemaElement(WALLET_DDB_ATTRIBUTE_OWNER_ID, KeyType.HASH)))
                .withAttributeDefinitions(new AttributeDefinition(WALLET_DDB_ATTRIBUTE_OWNER_ID, ScalarAttributeType.S))
                .withProvisionedThroughput(throughput));
    }
}
