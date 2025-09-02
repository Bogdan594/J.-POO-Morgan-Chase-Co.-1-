package org.poo.commands;

import lombok.Getter;
import lombok.Setter;
import org.poo.myclasses.Bank;
import org.poo.myclasses.ExchangeRates;
import org.poo.myclasses.Tools;
import org.poo.myclasses.User;
import org.poo.myclasses.Account;

import org.poo.transactions.SplitPaymentFailTransactions;
import org.poo.transactions.SplitPaymentTransaction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter


public class SplitPaymentCommand implements Command {
    private List<String> accounts;
    private double amount;
    private String currency;
    private int timestamp;
    private Bank bank;
    private ArrayList<ExchangeRates> exchangeRates;

    public SplitPaymentCommand(final List<String> accounts, final double amount,
                               final String currency, final int timestamp, final Bank bank) {
        this.accounts = accounts;
        this.amount = amount;
        this.currency = currency;
        this.timestamp = timestamp;
        this.bank = bank;
        this.exchangeRates = bank.getExchangeRates();
    }

    /**
     * Firstly, and individual amount will be calculated as the amount / number of participating
     * accounts. Then, for each account, the method will search its owner. After that, there will
     * be a check whether each account has enough money for the payment. If even one account fails,
     * a "fail" transaction will be generated for each account in users' transactions list.
     * If not, each account will be charged the amount / exchanged amount and a "done" transactions
     * will be generated
     */
    @Override
    public void execute() {
        Tools tools = new Tools();
        double individual = amount / accounts.size();
        String failer = "";
        int error = 0;
        int notEnoughFunds = 0;
        Set<User> participatingUsers = new HashSet<>();

        for (String acc : accounts) {
            User u = tools.findUserUsingAccount(acc, bank);
            if (u != null) {
                participatingUsers.add(u);
            }
        }

        for (String acc : accounts) {
            Account a = tools.findAccountUsingIBAN(acc, bank);
            if (a == null) {
                error = 1;
                break;
            }
            if (a.getCurrency().equals(currency)) {
                if (a.getBalance() < individual) {
                    failer = a.getIBAN();
                    notEnoughFunds = 1;
                }
            } else {
                double converted = tools.calculateAmountExchange(a.getCurrency(), currency,
                        individual, exchangeRates);
                if (a.getBalance() < converted) {
                    failer = a.getIBAN();
                    notEnoughFunds = 1;
                }
            }
        }

        if (error == 0 && notEnoughFunds == 0) {
            for (String acc : accounts) {
                Account a = tools.findAccountUsingIBAN(acc, bank);
                if (a.getCurrency().equals(currency)) {
                    a.setBalance(a.getBalance() - individual);
                } else {
                    double converted = tools.calculateAmountExchange(a.getCurrency(), currency,
                            individual, exchangeRates);
                    a.setBalance(a.getBalance() - converted);
                }
            }

            for (User u : participatingUsers) {
                for (Account a : u.getAccounts()) {
                    for (String acc : accounts) {
                        if (a.getIBAN().equals(acc)) {
                            SplitPaymentTransaction split = new SplitPaymentTransaction(timestamp,
                                    currency, amount, individual, accounts, acc);
                            u.getTransactions().add(split);
                        }
                    }
                }

            }
        }

        if (notEnoughFunds == 1 && error == 0) {
            for (User u : participatingUsers) {
                for (Account a : u.getAccounts()) {
                    for (String acc : accounts) {
                        if (a.getIBAN().equals(acc)) {
                            SplitPaymentFailTransactions fail = new SplitPaymentFailTransactions(
                                    timestamp, currency, amount, individual,
                                    accounts, failer, acc);
                            u.getTransactions().add(fail);
                        }
                    }
                }

            }
        }
    }

}
