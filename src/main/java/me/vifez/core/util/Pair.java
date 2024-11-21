package me.vifez.core.util;

public class Pair<E1, E2> {

    private E1 elementOne;
    private E2 elementTwo;

    public Pair(E1 elementOne, E2 elementTwo) {
        this.elementOne = elementOne;
        this.elementTwo = elementTwo;
    }

    public E1 getElementOne() {
        return elementOne;
    }

    public E2 getElementTwo() {
        return elementTwo;
    }

}
