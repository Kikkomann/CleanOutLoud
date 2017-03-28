package com.runehou.cleanoutloud;

public class RecycleInfoObject {
    private String description, funFact, title;
    private int number;

    public RecycleInfoObject(String title, String description, String funFact, int number) {
        this.description = description;
        this.funFact = funFact;
        this.number = number;
        this.title = title;
    }


    public String getDescription() {
        return description;
    }

    public String getFunFact() {
        return funFact;
    }

    public int getNumber() {
        return number;
    }
}
