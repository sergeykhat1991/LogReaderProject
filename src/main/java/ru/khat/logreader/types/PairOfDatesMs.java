package ru.khat.logreader.types;


public class PairOfDatesMs {

    private final long from;
    private final long to;

    public PairOfDatesMs(long from, long to) {
        this.from = from;
        this.to = to;
    }

    public long getFrom() {
        return from;
    }

    public long getTo() {
        return to;
    }
}