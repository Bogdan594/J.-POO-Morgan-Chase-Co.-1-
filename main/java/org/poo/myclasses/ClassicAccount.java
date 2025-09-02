package org.poo.myclasses;

public class ClassicAccount extends Account {
    public ClassicAccount(final String iban, final String currency) {
        this.setBalance(0);
        this.setIBAN(iban);
        this.setCurrency(currency);
        this.setType("classic");
        this.setMinBalance(0);
    }
}
