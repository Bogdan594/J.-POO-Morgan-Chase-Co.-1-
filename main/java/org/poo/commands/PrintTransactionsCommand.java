package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.myclasses.Bank;
import org.poo.myclasses.User;

@Getter
@Setter
public class PrintTransactionsCommand implements Command {
    private int timestamp;
    private String email;
    private Bank bank;
    private ArrayNode output;

    public PrintTransactionsCommand(final int timestamp, final String email, final Bank bank,
                                    final ArrayNode output) {
        this.timestamp = timestamp;
        this.email = email;
        this.bank = bank;
        this.output = output;
    }

    /**
     * Firstly, the method searches for the targeted user. When found,
     * the user's transactions will be shown.
     */

    @Override
    public void execute() {
        User target = null;
        for (User user : bank.getUsers()) {
            if (user.getEmail().equals(email)) {
                target = user;
                break;
            }
        }
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", "printTransactions");
        objectNode.set("output", objectMapper.valueToTree(target.getTransactions()));
        objectNode.put("timestamp", timestamp);
        output.add(objectNode);
    }
}
