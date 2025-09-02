package org.poo.commands;

import lombok.Getter;
import lombok.Setter;
import org.poo.myclasses.Account;
import org.poo.myclasses.User;

import java.util.ArrayList;
@Getter
@Setter

public class AddFundsCommand implements Command {
    private ArrayList<User> users;
    private String iban;
    private double amount;

    public AddFundsCommand(final ArrayList<User> users, final String iban, final double amount) {
        this.users = users;
        this.iban = iban;
        this.amount = amount;
    }

    /**
     *  Firstly, the method searches for the selected account
     *  from the user list and then increases the account balance
     *  by the given amount
     */
    @Override
    public void execute() {
        for (User user : users) {
            if (user.getAccounts() != null) {
                for (Account acc : user.getAccounts()) {
                    if (acc.getIBAN().equals(iban)) {
                        acc.setBalance(acc.getBalance() + amount);
                        break;
                    }
                }
            }
        }
    }
}
