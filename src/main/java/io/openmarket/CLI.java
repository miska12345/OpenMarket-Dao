package io.openmarket;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CLI {
    public static void main(String[] args) {
//        AmazonDynamoDB db = AmazonDynamoDBClientBuilder.standard().build();
//        TransactionDao dao = new TransactionDaoImpl(db, new DynamoDBMapper(db));
//        StampEventDao eventDao = new StampEventDaoImpl(db, new DynamoDBMapper(db));

//        Transaction t = Transaction.builder().type(TransactionType.TRANSFER).status(TransactionStatus.COMPLETED).recipientId("123").payerId("321").transactionId("123456").amount(100.0).currencyId("123").build();
//        dao.save(t);
//        List<Transaction> lst = new ArrayList<>();
//        dao.getTransactionForRecipient("123", lst, null);
//        System.out.println(lst);


//        StampEventDao dao = new StampEventDaoImpl(db, new DynamoDBMapper(db));
//        System.out.println(dao.load("3596a7ea-ecaf-43c2-9514-a0e6b3236d6f"));
//        dao.delete("1e2bbee9-965c-4c07-9c7d-6b3df9bae705");
        //dao.save(StampEvent.builder().ownerId("321").currencyId("222").totalAmount(100.0).rewardAmount(1.0).remainingAmount(100.0).expireAt(new Date()).build());

        ComboPooledDataSource cpds = new ComboPooledDataSource();
        cpds.setJdbcUrl("jdbc:mysql://marketplace.cbr1diopi1hk.us-west-2.rds.amazonaws.com/openmarket");
        cpds.setUser("admin");
        cpds.setPassword("wassup123");
        cpds.setInitialPoolSize(5);
        cpds.setMinPoolSize(5);
        cpds.setAcquireIncrement(5);
        cpds.setMaxPoolSize(20);
        cpds.setMaxStatements(100);

        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        try
        {
            ComboPooledDataSource dataSource = cpds;
            connection = dataSource.getConnection();
            pstmt = connection.prepareStatement("SELECT * FROM ITEMS");

            System.out.println("The Connection Object is of Class: " + connection.getClass());
            resultSet = pstmt.executeQuery();
            while (resultSet.next())
            {
                System.out.println(resultSet.getString(3));
                //System.out.println(resultSet.getString(1) + "," + resultSet.getString(2) + "," + resultSet.getString(3));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
