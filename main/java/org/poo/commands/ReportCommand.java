package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.myclasses.Account;
import org.poo.myclasses.Bank;
import org.poo.myclasses.User;
import org.poo.transactions.Transaction;

import java.util.ArrayList;

@Getter
@Setter

public class ReportCommand implements Command {
    private int startTimestamp;
    private int endTimestamp;
    private String account;
    private int timestamp;
    private Bank bank;
    private ArrayNode output;

    public ReportCommand(final int startTimestamp, final int endTimestamp, final String account,
                         final int timestamp, final Bank bank, final ArrayNode output) {
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.account = account;
        this.timestamp = timestamp;
        this.bank = bank;
        this.output = output;
    }

    /**
     * Firstly, the method searches for the user and the targeted account.
     * If the account is not found, a message will be shown.
     * Otherwise, a new arraylist will be created and for each transaction
     * from the user's list there will be 2 things checked: first, if it belongs
     * to the targeted account, second if the timestamp is between the two margins
     * given in input.
     * After that, an output message will be created, and the transactions will be shown.
     */
    @Override
    public void execute() {
        User user = null;
        Account acc = null;
        for (User u : bank.getUsers()) {
            for (Account a : u.getAccounts()) {
                if (a.getIBAN().equals(account)) {
                    user = u;
                    acc = a;
                    break;
                }
            }
        }

        ArrayList<Transaction> transactions = new ArrayList<>();
        if (acc == null) {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("command", "report");
            ObjectNode outputNode = objectMapper.createObjectNode();
            outputNode.put("description", "Account not found");
            outputNode.put("timestamp", timestamp);
            objectNode.set("output", outputNode);
            objectNode.put("timestamp", timestamp);
            output.add(objectNode);
        }

        if (user != null) {
            for (Transaction t : user.getTransactions()) {
                if (t.getTimestamp() >= startTimestamp && t.getTimestamp() <= endTimestamp
                        && t.getAccount() != null) {
                    if (t.getAccount().equals(account)) {
                        transactions.add(t);
                    }
                }

            }
                ObjectMapper objectMapper = new ObjectMapper();
                ObjectNode objectNode = objectMapper.createObjectNode();
                objectNode.put("command", "report");
                ObjectNode outputNode = objectMapper.createObjectNode();
                outputNode.put("balance", acc.getBalance());
                outputNode.put("currency", acc.getCurrency());
                outputNode.put("IBAN", acc.getIBAN());
                outputNode.set("transactions", objectMapper.valueToTree(transactions));
                objectNode.set("output", outputNode);
                objectNode.put("timestamp", timestamp);
                output.add(objectNode);
        }

    }
}
