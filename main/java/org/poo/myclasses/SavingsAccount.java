package org.poo.myclasses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SavingsAccount extends Account {
    @JsonIgnore
    private double interestRate;
    public SavingsAccount(final String iban, final String currency, final double interestRate) {
        this.setBalance(0);
        this.setIBAN(iban);
        this.setCurrency(currency);
        this.setType("savings");
        this.setMinBalance(0);
        this.interestRate = interestRate;
    }
}
