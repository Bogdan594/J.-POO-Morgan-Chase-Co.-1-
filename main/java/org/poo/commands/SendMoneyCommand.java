package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.myclasses.Account;
import org.poo.myclasses.ExchangeRates;
import org.poo.myclasses.Tools;
import org.poo.myclasses.User;
import org.poo.transactions.InsufficientFundsTransaction;
import org.poo.transactions.SendMoneyTransaction;
import org.poo.transactions.Transaction;

import java.util.ArrayList;

public class SendMoneyCommand implements Command {
    private ArrayList<User> users;
    private String ibanSender;
    private String ibanReceiver;
    private double amount;
    private ArrayList<ExchangeRates> exchangeRates;
    private ArrayNode output;
    private int timestamp;
    private String description;

    public SendMoneyCommand(final ArrayList<User> users, final String ibanSender,
                            final String ibanReceiver, final double amount,
                            final ArrayList<ExchangeRates> exchangeRates, final ArrayNode output,
                            final int timestamp, final String description) {
        this.users = users;
        this.ibanSender = ibanSender;
        this.ibanReceiver = ibanReceiver;
        this.amount = amount;
        this.exchangeRates = exchangeRates;
        this.output = output;
        this.timestamp = timestamp;
        this.description = description;
    }

    /**
     * Firstly, the two users and two accounts will be searched.
     * Then it will be checked if the two accounts' currencies are
     * the same. If yes, the money will be sent, if not, for the receiver,
     * the amount will be converted. If the sender does not have enough money,
     * a "fail" transactions will be added to the user sender. If the sender has
     * enough money, a "done" transaction will be added to both users.
     */
    @Override
    public void execute() {
        Tools tools = new Tools();
        User userSender = null;
        User userReceiver = null;
        Account accSender = null;
        Account accReceiver = null;
        boolean valid = true;
        int succes = 0;
        double convertedAmount = 0;

        for (User u : users) {
            for (Account a : u.getAccounts()) {
                if (a.getIBAN().equals(ibanSender)) {
                    userSender = u;
                    accSender = a;
                    break;
                }
            }
        }

        if (userSender == null) {
            for (User u : users) {
                for (Account a : u.getAccounts()) {
                    if (a.getAlias() != null) {
                        if (a.getAlias().equals(ibanSender)) {
                            userSender = u;
                            accSender = a;
                            valid = false;
                            break;
                        }
                    }
                }
            }
        }

        for (User u : users) {
            for (Account a : u.getAccounts()) {
                if (a.getIBAN().equals(ibanReceiver)) {
                    userReceiver = u;
                    accReceiver = a;
                    break;
                }
            }
        }

        if (userReceiver == null) {
            for (User u : users) {
                for (Account a : u.getAccounts()) {
                    if (a.getAlias() != null) {
                        if (a.getAlias().equals(ibanReceiver)) {
                            userReceiver = u;
                            accReceiver = a;
                            break;
                        }
                    }
                }
            }
        }

        if (accSender != null && accReceiver != null) {
            if (valid) {
                if (accSender.getCurrency().equals(accReceiver.getCurrency())) {
                    if (accSender.getBalance() >= amount) {
                        accSender.setBalance(accSender.getBalance() - amount);
                        accReceiver.setBalance(accReceiver.getBalance() + amount);
                        succes = 1;
                    } else {
                        Transaction insufficientFundsTransaction = new
                                InsufficientFundsTransaction(timestamp, accSender.getIBAN());
                        userSender.getTransactions().add(insufficientFundsTransaction);
                    }
                } else {
                    convertedAmount = tools.calculateAmountExchange(accReceiver.getCurrency(),
                            accSender.getCurrency(), amount, exchangeRates);
                    if (accSender.getBalance() >= amount) {
                        accSender.setBalance(accSender.getBalance() - amount);
                        accReceiver.setBalance(accReceiver.getBalance() + convertedAmount);
                        succes = 1;
                    } else {
                        Transaction insufficientFundsTransaction = new
                                InsufficientFundsTransaction(timestamp, accSender.getIBAN());
                        userSender.getTransactions().add(insufficientFundsTransaction);
                    }
                }
            }
        }

        if (succes == 1) {
            Transaction sendMoney = new SendMoneyTransaction(ibanSender, ibanReceiver, amount,
                    timestamp, description, accSender.getCurrency(), 1, accSender.getIBAN());
            userSender.getTransactions().add(sendMoney);
            if (accReceiver.getCurrency().equals(accSender.getCurrency())) {
                Transaction receiveMoney = new SendMoneyTransaction(ibanSender, ibanReceiver,
                        amount, timestamp, description, accReceiver.getCurrency(), 0,
                        accReceiver.getIBAN());
                userReceiver.getTransactions().add(receiveMoney);

            } else {
                Transaction receiveMoney = new SendMoneyTransaction(ibanSender, ibanReceiver,
                        convertedAmount, timestamp, description, accReceiver.getCurrency(), 0,
                        accReceiver.getIBAN());
                userReceiver.getTransactions().add(receiveMoney);

            }
        }
    }
}
