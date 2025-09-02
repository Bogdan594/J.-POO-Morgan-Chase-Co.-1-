package org.poo.transactions;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter

public class PayOnlineTransaction extends Transaction {
    private int timestamp;
    private String description;
    private double amount;
    private String commerciant;


    public PayOnlineTransaction(final int timestamp, final double amount, final String commerciant,
                                final String account) {
        this.timestamp = timestamp;
        this.description = "Card payment";
        this.amount = amount;
        this.commerciant = commerciant;
        this.setAccount(account);
    }
}
