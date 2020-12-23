package io.openmarket.marketplace.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @Builder.Default
    private int itemID = -1;

    @Builder.Default
    @NonNull
    private String itemName = "Undefined";

    @Builder.Default
    @NonNull
    private String belongTo = "";

    @Builder.Default
    private int stock = 0;

    @Builder.Default
    private int purchasedCount = 0;

    @Builder.Default
    private double itemPrice = 0;

    @Builder.Default
    @NonNull
    private String itemDescription = "The seller is too lazy to leave some description.";

    @Builder.Default
    @NonNull
    private String itemImageLink = "";

    @Builder.Default
    @NonNull
    private String itemCategory = "";
}
