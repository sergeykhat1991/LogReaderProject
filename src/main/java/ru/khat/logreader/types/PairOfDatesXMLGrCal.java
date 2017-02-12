package ru.khat.logreader.types;

import javax.xml.datatype.XMLGregorianCalendar;


public class PairOfDatesXMLGrCal {

    private XMLGregorianCalendar from;
    private XMLGregorianCalendar to;

    public PairOfDatesXMLGrCal() {
    }


    public XMLGregorianCalendar getFrom() {
        return from;
    }

    public void setFrom(XMLGregorianCalendar from) {
        this.from = from;
    }

    public XMLGregorianCalendar getTo() {
        return to;
    }

    public void setTo(XMLGregorianCalendar to) {
        this.to = to;
    }


}
