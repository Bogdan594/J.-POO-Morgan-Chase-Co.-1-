package org.poo.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class SendMoneyTransaction extends Transaction {
    private String senderIBAN;
    private String receiverIBAN;
    @JsonIgnore
    private double sum;
    private String transferType;
    private String description;
    private int timestamp;
    @JsonIgnore
    private String currency;
    private String amount;

    public SendMoneyTransaction(final String sender, final String receiver, final double sum,
                                final int timestamp, final String description,
                                final String currency, final int descriptor,
                                final String account) {
        this.setSenderIBAN(sender);
        this.setReceiverIBAN(receiver);
        this.setSum(sum);
        this.timestamp = timestamp;
        this.description = description;
        this.currency = currency;
        this.setType("SendMoney");
        this.setAmount(sum + " " + currency);
        this.setAccount(account);
        if (descriptor == 0) {
            this.setTransferType("received");
        } else if (descriptor == 1) {
            this.setTransferType("sent");
        }
    }
}
