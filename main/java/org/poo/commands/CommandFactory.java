package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.fileio.CommandInput;
import org.poo.myclasses.Bank;

public class CommandFactory {
    /**
     *
     * @param com the command from input with all the fields needed
     * @param bank with the users
     * @param output if needed for specific messages
     * @return the new command that will be executed
     */
    public Command createCommand(final CommandInput com, final Bank bank, final ArrayNode output) {
        switch (com.getCommand()) {
            case "addAccount" -> {
                return new AddAccountCommand(bank.getUsers(), com.getEmail(), com.getAccountType(),
                        com.getCurrency(), com.getTimestamp(), com.getInterestRate());
            }
            case "createCard" -> {
                return new CreateCardCommand(bank.getUsers(), com.getEmail(), com.getAccount(),
                        com.getTimestamp());
            }
            case "printUsers" -> {
                return new PrintUsersCommand(bank.getUsers(), output, com.getTimestamp());
            }
            case "addFunds" -> {
                return new AddFundsCommand(bank.getUsers(), com.getAccount(), com.getAmount());
            }
            case "deleteAccount" -> {
                return new DeleteAccountCommand(bank.getUsers(), com.getEmail(), com.getAccount(),
                        output, com.getTimestamp());
            }
            case "createOneTimeCard" -> {
                return new CreateOneTimeCardCommand(bank.getUsers(), com.getEmail(),
                        com.getAccount(), com.getTimestamp());
            }
            case "deleteCard" -> {
                return new DeleteCardCommand(bank.getUsers(), com.getEmail(), com.getCardNumber(),
                        com.getTimestamp());
            }
            case "payOnline" -> {
                return new PayOnlineCommand(bank.getUsers(), com.getCardNumber(), com.getAmount(),
                        com.getCurrency(), com.getTimestamp(), com.getEmail(), output,
                        bank.getExchangeRates(), com.getCommerciant());
            }
            case "sendMoney" -> {
                return new SendMoneyCommand(bank.getUsers(), com.getAccount(), com.getReceiver(),
                        com.getAmount(), bank.getExchangeRates(), output, com.getTimestamp(),
                        com.getDescription());
            }
            case "printTransactions" -> {
                return new PrintTransactionsCommand(com.getTimestamp(), com.getEmail(), bank,
                        output);
            }
            case "setMinimumBalance" -> {
                return new SetMinimumBalanceCommand(bank.getUsers(), com.getAccount(),
                        com.getAmount());
            }
            case "setAlias" -> {
                return new SetAliasCommand(com.getEmail(), com.getAccount(), com.getAlias(),
                        com.getTimestamp(), bank);
            }
            case "checkCardStatus" -> {
                return new CheckCardStatusCommand(bank, com.getCardNumber(), com.getTimestamp(),
                        output);
            }
            case "changeInterestRate" -> {
                return new ChangeInterestRateCommand(com.getAccount(), com.getInterestRate(),
                        com.getTimestamp(), bank, output);
            }
            case "splitPayment" -> {
                return new SplitPaymentCommand(com.getAccounts(), com.getAmount(),
                        com.getCurrency(), com.getTimestamp(), bank);
            }
            case "report" -> {
                return new ReportCommand(com.getStartTimestamp(), com.getEndTimestamp(),
                        com.getAccount(), com.getTimestamp(), bank, output);
            }
            case "spendingsReport" -> {
                return new SpendingReportCommand(com.getStartTimestamp(), com.getEndTimestamp(),
                        com.getAccount(), com.getTimestamp(), bank, output);
            }
            case "addInterest" -> {
                return new AddInterestCommand(com.getTimestamp(), com.getAccount(), bank, output);
            }
            default -> {
                return null;
            }
        }
    }
}
