package io.openmarket.order.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemInfo {
    @SerializedName("itemId")
    @Builder.Default
    private int itemId = -1;

    @SerializedName("itemName")
    @Builder.Default
    private String itemName = "";

    @SerializedName("price")
    @Builder.Default
    private double price = 0.0;

    @SerializedName("quantity")
    @Builder.Default
    private int quantity = 0;
}
