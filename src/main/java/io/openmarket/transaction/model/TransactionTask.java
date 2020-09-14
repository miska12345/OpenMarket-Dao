package io.openmarket.transaction.model;

import com.google.gson.annotations.SerializedName;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionTask {
    @SerializedName("transactionId")
    @NonNull
    private String transactionId;

    @SerializedName("attempts")
    private int remainingAttempts;
}
