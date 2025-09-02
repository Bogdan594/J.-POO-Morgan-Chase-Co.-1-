package org.poo.myclasses;

public class OneTimeCard extends Card {
    public OneTimeCard(final String cardNum) {
        this.setCardNumber(cardNum);
        this.setType("oneTime");
    }
}
