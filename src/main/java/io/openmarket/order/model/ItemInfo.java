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
    private String itemId;

    @SerializedName("itemName")
    private String itemName;

    @SerializedName("price")
    private double price;

    @SerializedName("quantity")
    private int quantity;
}
