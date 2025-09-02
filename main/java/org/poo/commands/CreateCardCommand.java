package org.poo.commands;

import org.poo.myclasses.Account;
import org.poo.myclasses.ClassicCard;
import org.poo.myclasses.User;
import org.poo.transactions.CreateCardTransaction;
import org.poo.transactions.Transaction;
import org.poo.utils.Utils;

import java.util.ArrayList;

public class CreateCardCommand implements Command {
    private ArrayList<User> users;
    private String email;
    private String account;
    private int timestamp;

    public CreateCardCommand(final ArrayList<User> users, final String email, final String account,
                             final int timestamp) {
        this.users = users;
        this.email = email;
        this.account = account;
        this.timestamp = timestamp;
    }

    /**
     * Firstly, the method searches for the user and finds the right one based on the email
     * Then, it iterates through each account until the targeted one if found and adds a new
     * card to it, and also a new transaction in user's list of transactions
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
        if (user != null && user.getAccounts() != null) {
            for (Account acc : user.getAccounts()) {
                if (acc.getIBAN().equals(this.account)) {
                    String cardNum = Utils.generateCardNumber();
                    ClassicCard newCard = new ClassicCard(cardNum);
                    acc.getCards().add(newCard);
                    Transaction createCardTransaction = new CreateCardTransaction(acc.getIBAN(),
                            cardNum, user.getEmail(), timestamp);
                    user.getTransactions().add(createCardTransaction);
                    break;
                }
            }
        }
    }
}
