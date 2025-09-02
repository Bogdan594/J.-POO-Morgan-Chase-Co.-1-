package org.poo.myclasses;

import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.CommerciantInput;
import org.poo.fileio.ExchangeInput;
import org.poo.fileio.UserInput;

import java.util.ArrayList;

@Getter
@Setter

public class Bank {
    private ArrayList<User> users;
    private ArrayList<ExchangeRates> exchangeRates;
    private ArrayList<Commerciant> commerciants;

    public Bank(final UserInput[] usersInput, final ExchangeInput[] exchangeRates,
                final CommerciantInput[] commerciantInput) {

        ArrayList<User> usersCopy = new ArrayList<>();
        for (UserInput user : usersInput) {
            User userAux = new User();
            userAux.copy(user);
            usersCopy.add(userAux);
        }
        this.setUsers(usersCopy);

        ArrayList<ExchangeRates> exchangeRatess = new ArrayList<>();
        if (exchangeRates != null) {
            for (ExchangeInput input : exchangeRates) {
                ExchangeRates ex = new ExchangeRates(input);
                exchangeRatess.add(ex);
            }
        }
        this.setExchangeRates(exchangeRatess);

        ArrayList<Commerciant> commerciantsCopy = new ArrayList<>();
        if (commerciantInput != null) {
            for (CommerciantInput com : commerciantInput) {
                Commerciant aux = new Commerciant();
                aux.copy(com);
                commerciantsCopy.add(aux);
            }
        }
        this.setCommerciants(commerciantsCopy);
    }
}
