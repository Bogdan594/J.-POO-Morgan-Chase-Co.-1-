package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.myclasses.Account;
import org.poo.myclasses.User;
import org.poo.transactions.DeleteAccountFailTransaction;

import java.util.ArrayList;

public class DeleteAccountCommand implements Command {
    private ArrayList<User> users;
    private String email;
    private String account;
    private int timestamp;
    private ArrayNode output;

    public DeleteAccountCommand(final ArrayList<User> users, final String email,
                                final String account, final ArrayNode output, final int timestamp) {
        this.users = users;
        this.email = email;
        this.account = account;
        this.output = output;
        this.timestamp = timestamp;
    }

    /**
     * Firstly, the method searches for the targeted account through the list of users
     * If the account is not found, a message will be shown
     * If found, but the account's owner is not the same as the email given in input,
     * an error message will be shown
     * Then, if the account is found and the owner checks, there will be one more verification to
     * do, whether there are founds left in the account. If yes, an error message will be shown,
     * else the cards array will be cleared and the account will be deleted and a message will be
     * shown
     */
    @Override
    public void execute() {
        User user = null;
        Account acc = null;
        int exists = 0;
        int error = 0;
        for (User u : users) {
            for (Account a : u.getAccounts()) {
                if (a.getIBAN().equals(account)) {
                    acc = a;
                    user = u;
                    break;
                }
            }
        }

        if (user == null || acc == null) {
            exists = 1;
            error = 1;
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("command", "deleteAccount");
            ObjectNode outputNode = objectMapper.createObjectNode();
            outputNode.put("error",
                    "Account couldn't be deleted - see org.poo.transactions for details");
            outputNode.put("timestamp", timestamp);
            objectNode.set("output", outputNode);
            objectNode.put("timestamp", timestamp);
            output.add(objectNode);
        }

        if (exists == 0) {
            if (!user.getEmail().equals(email)) {
                error = 1;
                ObjectMapper objectMapper = new ObjectMapper();
                ObjectNode objectNode = objectMapper.createObjectNode();
                objectNode.put("command", "deleteAccount");
                ObjectNode outputNode = objectMapper.createObjectNode();
                outputNode.put("error",
                        "Account couldn't be deleted - see org.poo.transactions for details");
                outputNode.put("timestamp", timestamp);
                objectNode.set("output", outputNode);
                objectNode.put("timestamp", timestamp);
                output.add(objectNode);
            }
        }

        if (error == 0 && exists == 0) {
            if (acc.getBalance() != 0) {
                ObjectMapper objectMapper2 = new ObjectMapper();
                    ObjectNode objectNode = objectMapper2.createObjectNode();
                    objectNode.put("command", "deleteAccount");
                    ObjectNode outputNode = objectMapper2.createObjectNode();
                    outputNode.put("error",
                            "Account couldn't be deleted - see org.poo.transactions for details");
                    outputNode.put("timestamp", timestamp);
                    objectNode.set("output", outputNode);
                    objectNode.put("timestamp", timestamp);
                    output.add(objectNode);
                DeleteAccountFailTransaction fail = new DeleteAccountFailTransaction(timestamp,
                        acc.getIBAN());
                user.getTransactions().add(fail);
            } else {
                acc.getCards().clear();
                    user.getAccounts().remove(acc);
                    ObjectMapper objectMapper3 = new ObjectMapper();
                    ObjectNode objectNode = objectMapper3.createObjectNode();
                    objectNode.put("command", "deleteAccount");
                    ObjectNode outputNode = objectMapper3.createObjectNode();
                    outputNode.put("success", "Account deleted");
                    outputNode.put("timestamp", timestamp);
                    objectNode.set("output", outputNode);
                    objectNode.put("timestamp", timestamp);
                    output.add(objectNode);
            }
        }
    }
}
