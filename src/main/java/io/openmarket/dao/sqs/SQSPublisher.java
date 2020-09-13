package io.openmarket.dao.sqs;

public interface SQSPublisher<T> {
    String publish(T msg);
}
