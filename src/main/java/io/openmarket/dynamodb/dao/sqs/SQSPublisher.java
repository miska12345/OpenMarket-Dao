package io.openmarket.dynamodb.dao.sqs;

public interface SQSPublisher<T> {
    String publish(String queueURL, T msg);
}
