package org.poo.myclasses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Card {
    private String cardNumber;
    private String status = "active";
    @JsonIgnore
    private boolean isFrozen;
    @JsonIgnore
    private double minBalance;
    @JsonIgnore
    private String type;
}
