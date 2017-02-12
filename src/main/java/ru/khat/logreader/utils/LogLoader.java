package ru.khat.logreader.utils;

import ru.khat.logreader.types.Block;
import ru.khat.logreader.types.PairOfDatesMs;
import ru.khat.logreader.types.PairOfDatesXMLGrCal;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LogLoader { // ----------  Старая версия поиска  -----------

    private final static Logger logger = Logger.getLogger(LogLoaderFindStr.class.getName());

    public List<Block> getResultList(String searchIn, String input, List<PairOfDatesXMLGrCal> pairOfDatesPeriods) throws Exception {

        DateConverter dateConverter = new DateConverter();
        List<PairOfDatesMs> listOfDatesLong = dateConverter.getListOfDatesInMillis(pairOfDatesPeriods);
        List<Block> resultList = new ArrayList<>();
        List<String> listOfLogPath = new LogSearcher().getLogsPath(searchIn, listOfDatesLong);

        for (String logPath : listOfLogPath) {

            try (BufferedReader in = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(File.separator.concat(logPath.substring(3))))))) {

                Pattern patternInput = Pattern.compile(input.toLowerCase());
                StringBuilder sbBlock = new StringBuilder();
                String line = in.readLine();

                do {
                    sbBlock.append(line);
                    sbBlock.append("\n");

                    if (line.startsWith("####")) {
                        Matcher matcherInput = patternInput.matcher(sbBlock.toString().toLowerCase());
                        String[] splitter = sbBlock.toString().split("> <");
                        long msInLine = Long.parseLong(splitter[9]);

                        if (matcherInput.find() && isDateFits(msInLine, listOfDatesLong)) {
                            resultList.add(new Block(dateConverter.convertDateToXMLGrCal(msInLine), sbBlock.toString(), splitter[5]));
                        }
                        sbBlock.setLength(0);
                    }
                } while ((line = in.readLine()) != null);

            } catch (Exception ex) {
                logger.log(Level.FINEST, ex.getMessage());
            }
        }
        return resultList;
    }

    private boolean isDateFits(long msInline, List<PairOfDatesMs> listOfDatesLong) {

        for (PairOfDatesMs p : listOfDatesLong) {
            if (p.getFrom() <= msInline && msInline <= p.getTo()) {
                return true;
            }
        }
        return false;
    }

}
