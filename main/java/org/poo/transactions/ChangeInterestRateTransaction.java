package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ChangeInterestRateTransaction extends Transaction {
    @JsonIgnore
    private double newRate;

    public ChangeInterestRateTransaction(final double newRate, final int timestamp,
                                         final String account) {
        this.setAccount(account);
        this.newRate = newRate;
        this.setDescription("Interest rate of the account changed to " + newRate);
        this.setTimestamp(timestamp);
    }
}
