package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.myclasses.ExchangeRates;
import org.poo.myclasses.User;
import org.poo.myclasses.Tools;
import org.poo.myclasses.Account;
import org.poo.myclasses.Card;
import org.poo.myclasses.OneTimeCard;

import org.poo.transactions.FrozenCardTransaction;
import org.poo.transactions.InsufficientFundsTransaction;
import org.poo.transactions.PayOnlineTransaction;
import org.poo.transactions.Transaction;
import org.poo.transactions.CardDestroyedTransaction;
import org.poo.transactions.CreateOneTimeCardTransaction;
import org.poo.utils.Utils;

import java.util.ArrayList;

public class PayOnlineCommand implements Command {
    private ArrayList<User> users;
    private String cardNumber;
    private double amount;
    private String currency;
    private int timestamp;
    private String commerciant;
    private String email;
    private ArrayNode output;
    private boolean foundCard = false;
    private ArrayList<ExchangeRates> exchangeRates;

    public PayOnlineCommand(final ArrayList<User> users, final String cardNumber,
                            final double amount, final String currency, final int timestamp,
                            final String email, final ArrayNode output,
                            final ArrayList<ExchangeRates> exchangeRates,
                            final String commerciant) {
        this.users = users;
        this.cardNumber = cardNumber;
        this.amount = amount;
        this.currency = currency;
        this.timestamp = timestamp;
        this.email = email;
        this.output = output;
        this.exchangeRates = exchangeRates;
        this.commerciant = commerciant;
    }

    /**
     * Firstly, the method searches for the user, account and card.
     * If the card is not found, a message will be shown.
     * Otherwise, it will go through certain checks: whether it is
     * enough balance in the account in order to make the payment,
     * if the card is frozen, and if the account's currency is the
     * same as the transaction's. If everything works well, the amount
     * will be reduced from the account's balance. In the end, there is a check
     * to see if the used card was a one time card. If it is, it will be replaced
     * by another one.
     */

    @Override
    public void execute() {
        Tools tools = new Tools();
            User user = null;
            for (User u : users) {
                if (u.getEmail().equals(email)) {
                    user = u;
                    break;
                }
            }

            Account target = null;
            Card foundCardObject = null;
            if (user != null && user.getAccounts() != null) {
                for (Account acc : user.getAccounts()) {
                    if (acc.getCards() != null) {
                        for (Card card : acc.getCards()) {
                            if (card.getCardNumber().equals(cardNumber)) {
                                target = acc;
                                foundCardObject = card;
                                foundCard = true;
                            }
                        }
                    }
                }
            }

            if (!foundCard) {
                ObjectMapper objectMapper = new ObjectMapper();
                ObjectNode objectNode = objectMapper.createObjectNode();
                objectNode.put("command", "payOnline");
                ObjectNode outputNode = objectMapper.createObjectNode();
                outputNode.put("timestamp", timestamp);
                outputNode.put("description", "Card not found");
                objectNode.set("output", outputNode);
                objectNode.put("timestamp", timestamp);
                output.add(objectNode);
            } else {
                double availableBalance = target.getBalance() - target.getMinBalance();

                if (currency.equals(target.getCurrency())) {
                    if (foundCardObject.isFrozen()) {
                        FrozenCardTransaction frozen = new FrozenCardTransaction(timestamp,
                                target.getIBAN());
                            user.getTransactions().add(frozen);
                    } else {
                        if (availableBalance >= amount) {
                            target.setBalance(target.getBalance() - amount);
                            Transaction payOnlineTransaction = new PayOnlineTransaction(timestamp,
                                    amount, commerciant, target.getIBAN());
                            user.getTransactions().add(payOnlineTransaction);
                            if (foundCardObject.getType().equals("oneTime")) {
                                target.getCards().remove(foundCardObject);
                                CardDestroyedTransaction deleted = new CardDestroyedTransaction(
                                        target.getIBAN(), cardNumber, user.getEmail(), timestamp);
                                user.getTransactions().add(deleted);
                                String cardNum = Utils.generateCardNumber();
                                target.getCards().add(new OneTimeCard(cardNum));
                                CreateOneTimeCardTransaction newCard = new
                                        CreateOneTimeCardTransaction(target.getIBAN(), cardNum,
                                        user.getEmail(), timestamp);
                                user.getTransactions().add(newCard);
                            }
                        } else {
                            Transaction insufficientFundsTransaction = new
                                    InsufficientFundsTransaction(timestamp, target.getIBAN());
                        user.getTransactions().add(insufficientFundsTransaction);
                        }
                    }
                } else {
                    double convertedAmount = tools.calculateAmountExchange(target.getCurrency(),
                            currency, amount, exchangeRates);
                    if (foundCardObject.isFrozen()) {
                        FrozenCardTransaction frozen = new FrozenCardTransaction(timestamp,
                                target.getIBAN());
                            user.getTransactions().add(frozen);
                    } else {
                        if (availableBalance >= convertedAmount) {
                            target.setBalance(target.getBalance() - convertedAmount);
                            Transaction payOnlineTransaction = new PayOnlineTransaction(timestamp,
                                    convertedAmount, commerciant, target.getIBAN());
                            user.getTransactions().add(payOnlineTransaction);
                            if (foundCardObject.getType().equals("oneTime")) {
                                target.getCards().remove(foundCardObject);
                                CardDestroyedTransaction deleted = new CardDestroyedTransaction(
                                        target.getIBAN(), cardNumber, user.getEmail(), timestamp);
                                user.getTransactions().add(deleted);
                                String cardNum = Utils.generateCardNumber();
                                target.getCards().add(new OneTimeCard(cardNum));
                                CreateOneTimeCardTransaction newCard = new
                                        CreateOneTimeCardTransaction(target.getIBAN(), cardNum,
                                        user.getEmail(), timestamp);
                                user.getTransactions().add(newCard);
                            }
                        } else {
                            InsufficientFundsTransaction insufficientFundsTransaction = new
                                    InsufficientFundsTransaction(timestamp, target.getIBAN());
                            user.getTransactions().add(insufficientFundsTransaction);
                        }
                    }
                }
            }
    }
}
