package org.poo.myclasses;

import java.util.Comparator;

public class CommerciantComparator implements Comparator<CommerciantAndSum> {
    /**
     *
     * @param com1 the first object to be compared.
     * @param com2 the second object to be compared.
     * @return result
     */
    @Override
    public int compare(final CommerciantAndSum com1, final CommerciantAndSum com2) {
        return com1.getCommerciant().compareTo(com2.getCommerciant());
    }
}

