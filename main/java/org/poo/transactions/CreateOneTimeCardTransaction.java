package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class CreateOneTimeCardTransaction extends Transaction {
    @JsonProperty("account")
    private String acc;
    private String card;
    private String cardHolder;
    private String description;
    private int timestamp;

    public CreateOneTimeCardTransaction(final String account, final String card,
                                        final String cardHolder, final int timestamp) {
        this.setAccount(account);
        this.acc = account;
        this.card = card;
        this.cardHolder = cardHolder;
        this.timestamp = timestamp;
        this.description = "New card created";
    }
}
