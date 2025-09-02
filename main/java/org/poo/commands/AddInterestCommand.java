package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.myclasses.Account;
import org.poo.myclasses.Bank;
import org.poo.myclasses.SavingsAccount;
import org.poo.myclasses.User;

@Getter
@Setter
public class AddInterestCommand implements Command {
    private int timestamp;
    private String account;
    private Bank bank;
    private ArrayNode output;

    public AddInterestCommand(final int timestamp, final String account, final Bank bank,
                              final ArrayNode output) {
        this.timestamp = timestamp;
        this.account = account;
        this.bank = bank;
        this.output = output;
    }

    /**
     * Firstly, the method searches for the targeted account by iterating through
     * every user's accounts list. Once found, it verifies if the account is of
     * savings type. If it is, the interest rate will be added to the old one,
     * otherwise, a message will be shown
     */
    @Override
    public void execute() {
        User user = null;
        SavingsAccount acc = null;

        for (User u : bank.getUsers()) {
            for (Account a : u.getAccounts()) {
                if (a.getIBAN().equals(account) && a.getType().equals("savings")) {
                    acc = (SavingsAccount) a;
                    user = u;
                    break;
                } else if (a.getIBAN().equals(account) && !a.getType().equals("savings")) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    ObjectNode objectNode = objectMapper.createObjectNode();
                    objectNode.put("command", "addInterest");
                    ObjectNode outputNode = objectMapper.createObjectNode();
                    outputNode.put("description", "This is not a savings account");
                    outputNode.put("timestamp", timestamp);
                    objectNode.set("output", outputNode);
                    objectNode.put("timestamp", timestamp);
                    output.add(objectNode);
                }
            }
        }

        if (acc != null) {
            if (acc.getType().equals("savings")) {
                double rate = acc.getInterestRate();
                double sum = acc.getBalance();
                double incoming = sum * rate;
                acc.setBalance(acc.getBalance() + incoming);
            }
        }
    }
}
