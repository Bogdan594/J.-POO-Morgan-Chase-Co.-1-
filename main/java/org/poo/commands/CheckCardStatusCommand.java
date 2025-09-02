package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.myclasses.Account;
import org.poo.myclasses.Bank;
import org.poo.myclasses.Card;
import org.poo.myclasses.User;
import org.poo.transactions.CheckCardStatusTransaction;
import org.poo.transactions.Transaction;

@Getter
@Setter

public class CheckCardStatusCommand implements Command {
    private String cardNumber;
    private int timestamp;
    private Bank bank;
    private ArrayNode output;

    public CheckCardStatusCommand(final Bank bank, final String cardNumber, final int timestamp,
                                  final ArrayNode output) {
        this.bank = bank;
        this.cardNumber = cardNumber;
        this.timestamp = timestamp;
        this.output = output;
    }

    /**
     * The method searches for the targeted card through the user list and then
     * through each user's account list. If the card is not found, a message will
     * be shown. If found, the method will check if the account's balance is greater
     * that the minimum balance set. If not, the card will be frozen
     */
    @Override
    public void execute() {
        User user = null;
        Account acc = null;
        Card target = null;
        for (User u : bank.getUsers()) {
            for (Account a : u.getAccounts()) {
                for (Card c : a.getCards()) {
                    if (c.getCardNumber().equals(cardNumber)) {
                        target = c;
                        user = u;
                        acc = a;
                    }
                }
            }
        }
        if (target == null) {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("command", "checkCardStatus");
            ObjectNode outputNode = objectMapper.createObjectNode();
            outputNode.put("timestamp", timestamp);
            outputNode.put("description", "Card not found");
            objectNode.set("output", outputNode);
            objectNode.put("timestamp", timestamp);
            output.add(objectNode);
        }

        if (acc != null) {
            if (acc.getBalance() <= acc.getMinBalance()) {
                target.setFrozen(true);
                target.setStatus("frozen");
                Transaction frozen = new CheckCardStatusTransaction(timestamp, acc.getIBAN());
                user.getTransactions().add(frozen);
            }
        }
    }
}
