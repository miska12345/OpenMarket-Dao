package io.openmarket.transaction.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.With;

@With
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoneyAmount {
    @SerializedName("amount")
    private double amount = 0.0;

    @SerializedName("currencyId")
    @NonNull private String currencyId = "";

    public boolean validate() {
        return amount > 0 && !currencyId.isEmpty();
    }
}
