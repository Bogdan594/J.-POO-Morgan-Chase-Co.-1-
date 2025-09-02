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
import org.poo.transactions.ChangeInterestRateTransaction;

@Getter
@Setter
public class ChangeInterestRateCommand implements Command {
    private String account;
    private double newRate;
    private int timestamp;
    private Bank bank;
    private ArrayNode output;

    public ChangeInterestRateCommand(final String account, final double newRate,
                                     final int timestamp, final Bank bank,
                                     final ArrayNode output) {
        this.account = account;
        this.newRate = newRate;
        this.timestamp = timestamp;
        this.bank = bank;
        this.output = output;
    }

    /**
     * Firstly, the method searches for the targeted account by iterating through
     * every user's accounts list. Once found, it verifies if the account is of
     * savings type. If it is, the interest rate will be replaced by the new one
     * and a new transaction will be generated in user's list, otherwise,
     * a message will be shown
     */
    @Override
    public void execute() {
        User user = null;
        SavingsAccount target = null;
        for (User u : bank.getUsers()) {
            for (Account a : u.getAccounts()) {
                if (a.getIBAN().equals(account) && a.getType().equals("savings")) {
                    user = u;
                    target = (SavingsAccount) a;
                    break;
                } else if (!a.getType().equals("savings") && a.getIBAN().equals(account)) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    ObjectNode objectNode = objectMapper.createObjectNode();
                    objectNode.put("command", "changeInterestRate");
                    ObjectNode outputNode = objectMapper.createObjectNode();
                    outputNode.put("description", "This is not a savings account");
                    outputNode.put("timestamp", timestamp);
                    objectNode.set("output", outputNode);
                    objectNode.put("timestamp", timestamp);
                    output.add(objectNode);
                    break;
                }
            }
        }

        if (target != null) {
            if (target.getType().equals("savings")) {
                target.setInterestRate(newRate);
                ChangeInterestRateTransaction changed = new ChangeInterestRateTransaction(newRate,
                        timestamp, account);
                user.getTransactions().add(changed);
            }
        }

    }
}
