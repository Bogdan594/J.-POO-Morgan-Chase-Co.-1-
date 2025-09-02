package org.poo.transactions;

public class InsufficientFundsTransaction extends Transaction {
    public InsufficientFundsTransaction(final int timestamp, final String account) {
        this.setAccount(account);
        this.setDescription("Insufficient funds");
        this.setTimestamp(timestamp);
    }
}
