package fr.deepanse.soywod.deepanse.model;

import java.util.GregorianCalendar;

/**
 * Created by soywod on 10/02/2015.
 */
public class Report {

    private GregorianCalendar date;
    private double total;

    public Report(){}

    public Report(GregorianCalendar date, double total) {
        this.date = date;
        this.total = total;
    }

    public GregorianCalendar getDate() {
        return date;
    }

    public double getTotal() {
        return total;
    }

    public void setDate(GregorianCalendar date) {
        this.date = date;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
