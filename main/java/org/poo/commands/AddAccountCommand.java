package org.poo.commands;

import lombok.Getter;
import lombok.Setter;
import org.poo.myclasses.Account;
import org.poo.myclasses.ClassicAccount;
import org.poo.myclasses.SavingsAccount;
import org.poo.myclasses.User;
import org.poo.transactions.AccountCreatedTransaction;
import org.poo.transactions.Transaction;
import org.poo.utils.Utils;

import java.util.ArrayList;

@Getter
@Setter

public class AddAccountCommand implements Command {
    private ArrayList<User> users;
    private String email;
    private String accountType;
    private String currency;
    private int timestamp;
    private double interestRate;

    public AddAccountCommand(final ArrayList<User> users, final String email,
                             final String accountType, final String currency, final int timestamp,
                             final double interestRate) {
        this.users = users;
        this.email = email;
        this.accountType = accountType;
        this.currency = currency;
        this.timestamp = timestamp;
        this.interestRate = interestRate;
    }

    /**
     * Firstly, the method searches for the user and if found
     * it generates a new account depending on its type
     * and assigns it to the user
     * It also adds a new transaction for the user
     */
    @Override
    public void execute() {
        User user = null;
        for (User u : users) {
            if (u.getEmail().equals(email)) {
                user = u;
                break;
            }
        }

        String iban = Utils.generateIBAN();
        if (user != null) {
            if (accountType.equals("classic")) {
                Account account = new ClassicAccount(iban, currency);
                user.addAccount(account);
                Transaction addAcc = new AccountCreatedTransaction(timestamp, account.getIBAN());
                user.getTransactions().add(addAcc);
            } else if (accountType.equals("savings")) {
                Account account = new SavingsAccount(iban, currency, interestRate);
                user.addAccount(account);
                Transaction addAcc = new AccountCreatedTransaction(timestamp, account.getIBAN());
                user.getTransactions().add(addAcc);
            }
        }
    }
}
