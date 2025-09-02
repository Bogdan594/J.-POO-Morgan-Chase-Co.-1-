package org.poo.commands;

import lombok.Getter;
import lombok.Setter;
import org.poo.myclasses.Account;
import org.poo.myclasses.Bank;
import org.poo.myclasses.User;

@Getter
@Setter

public class SetAliasCommand implements Command {
    private String email;
    private String account;
    private String alias;
    private int timestamp;
    private Bank bank;

    public SetAliasCommand(final String email, final String account,
                           final String alias, final int timestamp, final Bank bank) {
        this.email = email;
        this.account = account;
        this.alias = alias;
        this.timestamp = timestamp;
        this.bank = bank;
    }

    /**
     * The method searches for the user and targeted account and if found sets the alias
     * of the account to the one given in input
     */
    @Override
    public void execute() {
        User user = null;
        for (User u : bank.getUsers()) {
            if (u.getEmail().equals(email)) {
                user = u;
                break;
            }
        }

        if (user != null) {
            for (Account acc : user.getAccounts()) {
                if (acc.getIBAN().equals(account)) {
                    acc.setAlias(alias);
                    break;
                }
            }
        }
    }
}
