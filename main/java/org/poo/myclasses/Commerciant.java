package org.poo.myclasses;

import org.poo.fileio.CommerciantInput;

import java.util.List;

public class Commerciant {
    private int id;
    private String description;
    private List<String> commerciants;

    /**
     *
     * @param input the commerciant given in input that
     * will have its atributes copied to the new one
     */
    public void copy(final CommerciantInput input) {
        this.id = input.getId();
        this.description = input.getDescription();
        this.commerciants = input.getCommerciants();
    }
}
