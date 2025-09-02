package org.poo.myclasses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
@Getter
@Setter

public abstract class Account {
    @JsonProperty("IBAN")
    private String iBAN;
    private double balance;
    private String currency;
    private String type;
    @JsonIgnore
    private String alias;
    private ArrayList<Card> cards = new ArrayList<>();
    @JsonIgnore
    private double minBalance;
}
