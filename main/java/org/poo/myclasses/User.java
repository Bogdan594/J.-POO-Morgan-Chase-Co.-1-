package org.poo.myclasses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.poo.fileio.UserInput;
import org.poo.transactions.Transaction;

import java.util.ArrayList;

@Data
@Setter
@Getter
@NoArgsConstructor
public class User {
    private String firstName;
    private String lastName;
    private String email;
    private ArrayList<Account> accounts = new ArrayList<>();
    @JsonIgnore
    private ArrayList<Transaction> transactions = new ArrayList<>();

    /**
     * @param account the account that will be added to user's list of accounts
     */
    public void addAccount(final Account account) {
        this.accounts.add(account);
    }

    /**
     * @param input the user given in input that will have its atributes copied to the new one
     */
    public void copy(final UserInput input) {
        this.firstName = input.getFirstName();
        this.lastName = input.getLastName();
        this.email = input.getEmail();
    }

}
