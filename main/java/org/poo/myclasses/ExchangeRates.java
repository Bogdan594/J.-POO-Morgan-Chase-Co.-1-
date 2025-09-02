package org.poo.myclasses;

import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.ExchangeInput;

@Getter
@Setter

public class ExchangeRates {
    private String from;
    private String to;
    private double rate;
    private int timestamp;

    public ExchangeRates(final ExchangeInput input) {
        this.from = input.getFrom();
        this.to = input.getTo();
        this.rate = input.getRate();
        this.timestamp = input.getTimestamp();
    }
}
