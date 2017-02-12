package ru.khat.logreader.utils;

import ru.khat.logreader.types.PairOfDatesMs;
import ru.khat.logreader.types.PairOfDatesXMLGrCal;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

class DateConverter {

    XMLGregorianCalendar convertDateToXMLGrCal(long millis) throws Exception {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTimeInMillis(millis);
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
    }

    List<PairOfDatesMs> getListOfDatesInMillis(List<PairOfDatesXMLGrCal> listOfDatesXMLGrCal) {
        List<PairOfDatesMs> listOfDatesLong = new ArrayList<>();
        for (PairOfDatesXMLGrCal period : listOfDatesXMLGrCal) {
            GregorianCalendar grCalFrom = validate(period.getFrom(), false);
            GregorianCalendar grCalTo = validate(period.getTo(), true);
            listOfDatesLong.add(new PairOfDatesMs(grCalFrom.getTimeInMillis(), grCalTo.getTimeInMillis()));
        }
        return listOfDatesLong;
    }

    private GregorianCalendar validate(XMLGregorianCalendar date, boolean isMax) {
        GregorianCalendar grCal;
        if (date == null) {
            grCal = new GregorianCalendar();
            grCal.setTime(new Date(isMax ? Long.MAX_VALUE : Long.MIN_VALUE));
        } else {
            grCal = date.toGregorianCalendar();
        }
        return grCal;
    }


}
