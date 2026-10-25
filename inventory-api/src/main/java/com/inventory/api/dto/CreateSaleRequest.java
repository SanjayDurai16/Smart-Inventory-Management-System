package com.inventory.api.dto;

import java.util.ArrayList;
import java.util.List;

public class CreateSaleRequest {
    private List<CreateSaleItemRequest> items = new ArrayList<CreateSaleItemRequest>();

    public CreateSaleRequest() {
    }

    public List<CreateSaleItemRequest> getItems() {
        return items;
    }

    public void setItems(List<CreateSaleItemRequest> items) {
        this.items = items;
    }
}
