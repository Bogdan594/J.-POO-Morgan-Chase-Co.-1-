package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.myclasses.User;

import java.util.ArrayList;

@Getter
@Setter

public class PrintUsersCommand implements Command {
    private ArrayList<User> users;
    private ArrayNode output;
    private int timestamp;

    public PrintUsersCommand(final ArrayList<User> users, final ArrayNode output,
                             final int timestamp) {
        this.users = users;
        this.output = output;
        this.timestamp = timestamp;
    }

    /**
     * The method creates an object node and sets the output to the users
     */
    @Override
    public void execute() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", "printUsers");
        objectNode.set("output", objectMapper.valueToTree(users));
        objectNode.put("timestamp", timestamp);
        output.add(objectNode);
    }
}
