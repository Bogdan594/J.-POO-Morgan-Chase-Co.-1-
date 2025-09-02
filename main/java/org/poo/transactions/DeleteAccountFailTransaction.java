package org.poo.transactions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteAccountFailTransaction extends Transaction {
    public DeleteAccountFailTransaction(final int timestamp, final String account) {
        this.setDescription("Account couldn't be deleted - there are funds remaining");
        this.setTimestamp(timestamp);
        this.setAccount(account);
    }
}
