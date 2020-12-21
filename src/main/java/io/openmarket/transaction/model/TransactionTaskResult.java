package io.openmarket.transaction.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * TransactionTaskResult is the result of transaction processing.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionTaskResult {
    @SerializedName("transactionId")
    @NonNull
    private String transactionId;

    @SerializedName("type")
    private TransactionType type;

    @SerializedName("error")
    private TransactionErrorType error;

    @SerializedName("status")
    private TransactionStatus status;
}
