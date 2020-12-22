package io.openmarket.sns.dao;

import com.amazonaws.services.dynamodbv2.model.InternalServerErrorException;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.InvalidParameterException;
import com.amazonaws.services.sns.model.InvalidParameterValueException;
import com.amazonaws.services.sns.model.NotFoundException;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.google.gson.Gson;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

import javax.inject.Inject;

@Log4j2
public class SNSDaoImpl implements  SNSDao {
    private final AmazonSNS snsClient;
    private final Gson gson;
    @Inject
    public SNSDaoImpl(@NonNull final AmazonSNS snsClient, @NonNull final Gson gson) {
        this.snsClient = snsClient;
        this.gson = gson;

    }

    @Override
    public void publishToTopic(@NonNull final String topicArn, @NonNull final String content) {
        try {
            PublishResult result = snsClient.publish(new PublishRequest()
                    .withTopicArn(topicArn)
                    .withMessage(content));
            log.info("Published to SNS topicArn: {}, messageId: {}", topicArn, result.getMessageId());
        } catch (InvalidParameterException | InvalidParameterValueException | InternalServerErrorException e) {
            log.error("Illegal argument for publish with topicArn:{} content:{}", topicArn, content, e);
            throw new IllegalArgumentException(e);
        } catch (NotFoundException e) {
            log.error("Invalid topicArn:{}", topicArn);
            throw new IllegalArgumentException(String.format("Invalid topicArn:%s", topicArn), e);
        }
    }
}
