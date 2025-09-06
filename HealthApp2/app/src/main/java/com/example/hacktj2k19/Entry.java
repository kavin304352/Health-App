package com.example.hacktj2k19;

class Entry implements Comparable<Entry> {
    private String injury;
    private double prob;

    public Entry(String inj, double prob) {
        injury = inj;
        this.prob = prob;
    }

    public double getProb() {
        return prob;
    }

    public String getInjury() {
        return injury;
    }

    public int compareTo(Entry e) {
        if (prob > e.prob) return 1;
        else if (prob == e.prob) return 0;
        else return -1;
    }

    public String toString() {
        return injury + " " + prob;
    }
}