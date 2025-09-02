package org.poo.transactions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class CheckCardStatusTransaction extends Transaction {
    private int timestamp;
    private String description;

    public CheckCardStatusTransaction(final int timestamp, final String account) {
        this.setAccount(account);
        this.timestamp = timestamp;
        this.description = "You have reached the minimum amount of funds, the card will be frozen";
    }
}
