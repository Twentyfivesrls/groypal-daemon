package com.example.groypaldaemon.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreOrder {
    private String orderId;
    private List<SimpleItem> items;
}
