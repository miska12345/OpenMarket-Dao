package io.openmarket.sns.dao;

/**
 * A Dao for AWS SNS.
 */
public interface SNSDao {
    /**
     * Publish a message to the given topicArn.
     * @param topicArn the AWS ARN for the SNS topic.
     * @param content the message content.
     */
    void publishToTopic(String topicArn, String content);
}
