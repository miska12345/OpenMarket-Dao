package io.openmarket.transaction.dao.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import io.openmarket.transaction.model.TransactionTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SQSTransactionTaskPublisherTest {
    private static final String QUEUE_URL = "queue_url";
    private AmazonSQS sqsClient;
    private SQSTransactionTaskPublisher publisher;

    @BeforeEach
    public void setup() {
        sqsClient = mock(AmazonSQS.class);
        publisher = new SQSTransactionTaskPublisher(sqsClient);
    }

    @Test
    public void test_Publish_Message() {
        String messageID = "123";
        when(sqsClient.sendMessage(any(SendMessageRequest.class))).thenReturn(new SendMessageResult().withMessageId(messageID));
        String retId = publisher.publish(QUEUE_URL, TransactionTask.builder().transactionId("123").build());
        assertEquals(messageID, retId);
    }

    @Test
    public void when_Publish_Empty_URL_Then_Throw_IllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> publisher.publish("", new TransactionTask()));
    }

    @Test
    public void when_Publish_Invalid_Msg_Then_Throw_IllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> publisher.publish(QUEUE_URL, new TransactionTask("")));
    }

    @Test
    public void test_Validate() {
        assertFalse(publisher.validate(TransactionTask.builder().transactionId("").build()));
    }
}
