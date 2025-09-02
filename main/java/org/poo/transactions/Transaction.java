package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public abstract class Transaction {
    private int timestamp;
    private String description;
    @JsonIgnore
    private String type;
    @JsonIgnore
    private String account;

}
