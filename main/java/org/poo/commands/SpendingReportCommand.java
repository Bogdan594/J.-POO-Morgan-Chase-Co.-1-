package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.myclasses.Account;
import org.poo.myclasses.Bank;
import org.poo.myclasses.CommerciantAndSum;
import org.poo.myclasses.User;
import org.poo.myclasses.CommerciantComparator;
import org.poo.transactions.PayOnlineTransaction;
import org.poo.transactions.Transaction;

import java.util.ArrayList;
import java.util.Collections;

@Getter
@Setter

public class SpendingReportCommand implements Command {
    private int startTimestamp;
    private int endTimestamp;
    private String account;
    private int timestamp;
    private Bank bank;
    private ArrayNode output;

    public SpendingReportCommand(final int startTimestamp, final int endTimestamp,
                                 final String account, final int timestamp, final Bank bank,
                                 final ArrayNode output) {
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.account = account;
        this.timestamp = timestamp;
        this.bank = bank;
        this.output = output;
    }

    /**
     * The method searches for the user and account. If not found, a message will be shown.
     * Also, if the targeted account is of type "savings", an error message will be shown.
     * After that, an array list of commerciants and sum(I will refer to it as "CAS" in the future)
     * will be created. This class has afield for the commerciant's name and another one for the
     * sum spend to that commerciant. Then, the method will iterate through all the user's
     * transactions between two given timestamps, will check if it is assigned to the account and
     * if the transaction comes from an online payment. Then, it will check if there is a node
     * created in the CAS list for the current commerciant. If yes, it will add the sum to the
     * already existing one in the node, otherwise, a new node will be created. In the end,
     * there will be an objectnode created and the transactions and CAS will be added.
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
        ArrayList<CommerciantAndSum> pairs = new ArrayList<>();

        if (acc == null) {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("command", "spendingsReport");
            ObjectNode outputNode = objectMapper.createObjectNode();
            outputNode.put("description", "Account not found");
            outputNode.put("timestamp", timestamp);
            objectNode.set("output", outputNode);
            objectNode.put("timestamp", timestamp);
            output.add(objectNode);
        }

        if (user != null) {
            if (acc.getType().equals("savings")) {
                ObjectMapper objectMapper = new ObjectMapper();
                ObjectNode objectNode = objectMapper.createObjectNode();
                objectNode.put("command", "spendingsReport");
                ObjectNode outputNode = objectMapper.createObjectNode();
                outputNode.put("error",
                        "This kind of report is not supported for a saving account");
                objectNode.set("output", outputNode);
                objectNode.put("timestamp", timestamp);
                output.add(objectNode);
            } else {
                for (Transaction t : user.getTransactions()) {
                    if (t.getTimestamp() >= startTimestamp && t.getTimestamp() <= endTimestamp) {
                        if (t.getDescription().equals("Card payment")) {
                            boolean found = false;
                            PayOnlineTransaction tr  = (PayOnlineTransaction) t;
                            if (tr.getAccount().equals(account)) {
                                transactions.add(tr);
                                for (CommerciantAndSum com : pairs) {
                                    if (tr.getCommerciant().equals(com.getCommerciant())) {
                                        com.setTotal(com.getTotal() + tr.getAmount());
                                        found = true;
                                        break;
                                    }
                                }
                                if (!found) {
                                    pairs.add(new CommerciantAndSum(tr.getCommerciant(),
                                            tr.getAmount()));
                                }
                            }
                        }
                    }
                }
                Collections.sort(pairs, new CommerciantComparator());

                ObjectMapper objectMapper = new ObjectMapper();
                ObjectNode objectNode = objectMapper.createObjectNode();
                objectNode.put("command", "spendingsReport");
                ObjectNode outputNode = objectMapper.createObjectNode();
                outputNode.put("IBAN", account);
                outputNode.put("balance", acc.getBalance());
                outputNode.put("currency", acc.getCurrency());
                outputNode.set("transactions", objectMapper.valueToTree(transactions));
                outputNode.set("commerciants", objectMapper.valueToTree(pairs));
                objectNode.set("output", outputNode);
                objectNode.put("timestamp", timestamp);
                output.add(objectNode);
            }
        }

    }
}
