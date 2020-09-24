package io.openmarket.order.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemInfo {

    private String item_id;

    private String item_name;

    private double price;

    private int quantity;

}
