import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import io.openmarket.marketplace.dao.ItemDao;
import io.openmarket.marketplace.dao.ItemDaoImpl;
import io.openmarket.marketplace.model.Item;
import io.openmarket.organization.dao.OrgDao;
import io.openmarket.organization.dao.OrgDaoImpl;
import io.openmarket.organization.model.Organization;

import static io.openmarket.config.MerchandiseConfig.*;
import static io.openmarket.config.OrgConfig.*;

public class CLI {
    public static void main(String[] args) {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
        //ItemDao dao = new ItemDaoImpl(client, new DynamoDBMapper(client));
        OrgDao orgDao = new OrgDaoImpl(client, new DynamoDBMapper(client));
        Organization org = Organization.builder()
                .orgCurrency("HELLO")
                .orgDescription("test")
                .orgName("testOrg")
                .orgOwnerId("owner1")
                .orgPortraitS3Key("ldksjfasdo")
                .build();
        orgDao.save(org);

        UpdateItemRequest request = new UpdateItemRequest()
                .withTableName(ORG_DDB_TABLE_NAME)
                .withKey(ImmutableMap.of(ORG_DDB_KEY_ORGNAME, new AttributeValue("testOrg")))
                .withUpdateExpression("ADD #posterCol :newIds")
                .withConditionExpression("not(contains(#posterCol, :newIds))")
                .withExpressionAttributeNames(ImmutableMap.of("#posterCol", ORG_DDB_ATTRIBUTE_POSTERS))
                .withExpressionAttributeValues(ImmutableMap.of(":newIds", new AttributeValue().withSS("organization of bubble tea")));
        orgDao.updateOrg(request);

//        Item item = Item.builder().itemName("Mac3").stock(1).belongTo("ChaCha")
//                .itemCategory("Electronic")
//                .itemDescription("it is very hot")
//                .itemImageLink("https//watever")
//                .itemPrice(100.0).build();
//        dao.save(item);
//        System.out.println(dao.getItemIdByCategory("Electronic", 3));
//        dao.getItemIdsByOrg("ChaCha");
    }
}
