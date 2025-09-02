package org.poo.commands;

import org.poo.myclasses.Account;
import org.poo.myclasses.Card;
import org.poo.myclasses.User;
import org.poo.transactions.CardDestroyedTransaction;
import org.poo.transactions.Transaction;

import java.util.ArrayList;

public class DeleteCardCommand implements Command {
    private ArrayList<User> users;
    private String email;
    private String cardNumber;
    private int timestamp;

    public DeleteCardCommand(final ArrayList<User> users, final String email,
                             final String cardNumber, final int timestamp) {
        this.users = users;
        this.email = email;
        this.cardNumber = cardNumber;
        this.timestamp = timestamp;
    }

    /**
     * The method finds the target card by iterating through each user's accounts.
     * If found, it will be deleted from the parent account's list of card and a
     * message will be shown
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
                if (acc.getCards() != null) {
                    for (Card card : acc.getCards()) {
                        if (card.getCardNumber().equals(cardNumber)) {
                            acc.getCards().remove(card);
                            Transaction deleteCardTransaction = new CardDestroyedTransaction(
                                    acc.getIBAN(), cardNumber, user.getEmail(), timestamp);
                            user.getTransactions().add(deleteCardTransaction);
                            break;
                        }
                    }
                }
            }
        }
    }
}
