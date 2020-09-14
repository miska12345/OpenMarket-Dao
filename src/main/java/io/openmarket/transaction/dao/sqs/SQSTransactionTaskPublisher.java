package io.openmarket.transaction.dao.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.google.common.annotations.VisibleForTesting;
import com.google.gson.Gson;
import io.openmarket.dynamodb.dao.sqs.SQSPublisher;
import io.openmarket.transaction.model.TransactionTask;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

import javax.inject.Inject;

@Log4j2
public class SQSTransactionTaskPublisher implements SQSPublisher<TransactionTask> {
    private final Gson gson;
    private final AmazonSQS sqsClient;

    @Inject
    public SQSTransactionTaskPublisher(@NonNull final AmazonSQS sqsClient) {
        this.gson = new Gson();
        this.sqsClient = sqsClient;
    }

    @Override
    public String publish(@NonNull final String queueURL, @NonNull final TransactionTask msg) {
        if (queueURL.isEmpty()) {
            throw new IllegalArgumentException("QueueURL cannot be empty");
        }
        if (!validate(msg)) {
            log.error("The given msg is not valid: {}", msg);
            throw new IllegalArgumentException(String.format("msg %s failed to validate", msg));
        }
        final String jsonObj = gson.toJson(msg);
        final SendMessageResult result = sqsClient.sendMessage(new SendMessageRequest().withQueueUrl(queueURL)
                .withMessageBody(jsonObj));
        log.info("Successfully published message to queue '{}': msg {}", queueURL, msg);
        return result.getMessageId();
    }

    @VisibleForTesting
    protected boolean validate(final TransactionTask task) {
        return !task.getTransactionId().isEmpty() && task.getRemainingAttempts() >= 0;
    }
}
