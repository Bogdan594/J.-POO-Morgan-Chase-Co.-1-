package org.poo.transactions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class AccountCreatedTransaction extends Transaction {
    public AccountCreatedTransaction(final int timestamp, final String account) {
        this.setTimestamp(timestamp);
        this.setDescription("New account created");
        this.setType("AccountCreated");
        this.setAccount(account);
    }
}
