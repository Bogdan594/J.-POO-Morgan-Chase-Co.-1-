package org.poo.myclasses;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter

public class CommerciantAndSum {
    private String commerciant;
    private double total;

    public CommerciantAndSum(final String commerciant, final double total) {
        this.commerciant = commerciant;
        this.total = total;
    }
}

