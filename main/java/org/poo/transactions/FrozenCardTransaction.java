package org.poo.transactions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class FrozenCardTransaction extends Transaction {
    private int timestamp;
    private String description;

    public FrozenCardTransaction(final int timestamp, final String account) {
        this.timestamp = timestamp;
        this.setAccount(account);
        this.description = "The card is frozen";
    }
}
