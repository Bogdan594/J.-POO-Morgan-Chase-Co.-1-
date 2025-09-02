package org.poo.commands;

import org.poo.myclasses.Account;
import org.poo.myclasses.User;

import java.util.ArrayList;

public class SetMinimumBalanceCommand implements Command {
    private ArrayList<User> users;
    private String account;
    private double minBalance;

    public SetMinimumBalanceCommand(final ArrayList<User> users,
                                    final String account, final double minBalance) {
        this.users = users;
        this.account = account;
        this.minBalance = minBalance;
    }

    /**
     * THe method searches for the account and if found,
     * it sets the minimum balance to the one given in input
     */
    @Override
    public void execute() {
        for (User u : users) {
            for (Account a : u.getAccounts()) {
                if (a.getIBAN().equals(account)) {
                    a.setMinBalance(minBalance);
                    break;
                }
            }
        }
    }
}
