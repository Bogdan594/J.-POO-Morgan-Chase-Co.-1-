package org.poo.myclasses;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
@Getter
@Setter

public class Tools {
    /**
     *
     * @param target currency we need
     * @param source currency we start from
     * @param sum the sum
     * @param exchangeRates all the exchange rates
     * @return exchanged amount
     */
    public double calculateAmountExchange(final String target, final String source,
                                          final double sum, final ArrayList<ExchangeRates>
                                                    exchangeRates) {
        Set<String> visited = new HashSet<>();
        return search(source, target, sum, visited, exchangeRates);
    }

    /**
     *
     * @param current the starting currency
     * @param target the one we need
     * @param sum the amount
     * @param visited all the other visited currencies/exchange rates
     * @param exchangeRates all the exchange rates
     * @return the exchanged amount
     */
    public double search(final String current, final String target, final double sum,
                      final Set<String> visited, final ArrayList<ExchangeRates> exchangeRates) {
        if (current.equals(target)) {
            return sum;
        }

        visited.add(current);

        for (ExchangeRates rate : exchangeRates) {
            String nextCurrency = null;
            double nextRate = 1;

            if (rate.getFrom().equals(current) && !visited.contains(rate.getTo())) {
                nextCurrency = rate.getTo();
                nextRate = rate.getRate();
            } else if (rate.getTo().equals(current) && !visited.contains(rate.getFrom())) {
                nextCurrency = rate.getFrom();
                nextRate = 1 / rate.getRate();
            }

            if (nextCurrency != null) {
                double result = search(nextCurrency, target, sum * nextRate, visited,
                        exchangeRates);
                if (result != -1) {
                    return result;
                }
            }
        }

        return -1;
    }

    /**
     * @param iban of the account
     * @return account that matches the iban
     */
    public Account findAccountUsingIBAN(final String iban, final Bank bank) {
        for (User user : bank.getUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getIBAN().equals(iban)) {
                    return account;
                }
            }
        }
        return null;
    }

    /**
     * @param iban the iban of the account
     * @return user that owns the account
     */
    public User findUserUsingAccount(final String iban, final Bank bank) {
        for (User user : bank.getUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getIBAN().equals(iban)) {
                    return user;
                }
            }
        }
        return null;
    }
}
