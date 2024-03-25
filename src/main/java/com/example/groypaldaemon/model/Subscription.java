package com.example.groypaldaemon.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {

    private String id;
    private String customId;
    private List<String> appIds;
    private String roleName; // Keycloak rolename to add
    private Long duration; // day duration of the subscription
    private boolean active;
}
