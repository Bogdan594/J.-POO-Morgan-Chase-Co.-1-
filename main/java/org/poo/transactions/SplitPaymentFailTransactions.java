package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
@Getter
@Setter

public class SplitPaymentFailTransactions extends Transaction {
    private double amount;
    @JsonIgnore
    private double total;
    private String currency;
    private String error;
    @JsonIgnore
    private String failer;
    private List<String> involvedAccounts;
    private int timestamp;

    public SplitPaymentFailTransactions(final int timestamp, final String currency,
                                         final double total, final double amount,
                                         final List<String> accounts, final String failer,
                                         final String account) {
        this.setAccount(account);
        this.timestamp = timestamp;
        this.currency = currency;
        this.total = total;
        this.amount = amount;
        this.involvedAccounts = accounts;
        this.failer = failer;
        DecimalFormatSymbols simboluri = new DecimalFormatSymbols();
        simboluri.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("#.00", simboluri);
        // e un pic complicat cu DecimalFormat si DecimalFormatSymbols,
        // dar nu am gasit alta solutie eficienta
        this.setDescription("Split payment of " + df.format(total) + " " + currency);
        this.error = "Account " + failer  + " has insufficient funds for a split payment.";
    }




}
