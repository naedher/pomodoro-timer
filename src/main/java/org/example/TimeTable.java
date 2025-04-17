package org.example;

public class TimeTable {
    private String name;
    private int[] t;
    private String[] n;

    public TimeTable(String name, int[] t, String[] n) {
        this.name = name;
        this.t = t;
        this.n = n;
    }

    public String getName() {
        return name;
    }

    public int[] getT() {
        return t;
    }

    public String[] getN() {
        return n;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setT(int[] t) {
        this.t = t;
    }

    public void setN(String[] n) {
        this.n = n;
    }
}
