package ru.khat.logreader.utils;

import javafx.util.Pair;
import ru.khat.logreader.types.Block;
import ru.khat.logreader.types.PairOfDatesMs;
import ru.khat.logreader.types.PairOfDatesXMLGrCal;

import java.io.File;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class LogLoaderFindStr {

    private final static Logger logger = Logger.getLogger(LogLoaderFindStr.class.getName());

    public List<Block> getResultList(String searchIn, String input, List<PairOfDatesXMLGrCal> pairOfDatesPeriods) {

        DateConverter dateConverter = new DateConverter();
        List<PairOfDatesMs> listOfDatesMs = dateConverter.getListOfDatesInMillis(pairOfDatesPeriods);
        List<String> listOfLogPath = new LogSearcher().getLogsPath(searchIn, listOfDatesMs);
        List<Block> resultList = new ArrayList<>();

        for (String logPath : listOfLogPath) {
            List<Pair<Integer, Integer>> blocksBounds = new BlockSearcher().getBlocksBounds(logPath, input);
            logPath = File.separator.concat(logPath.substring(logPath.indexOf(File.separator) + 1));

            try (LineNumberReader lineReader = new LineNumberReader(new InputStreamReader(Files.newInputStream(Paths.get(logPath))))) {
                String line;
                StringBuilder sbBlock = new StringBuilder();
                int count = 0;

                while ((line = lineReader.readLine()) != null && count < blocksBounds.size()) {
                    Pair<Integer, Integer> pair = blocksBounds.get(count);
                    if (pair.getKey() <= lineReader.getLineNumber() && lineReader.getLineNumber() <= pair.getValue()) {
                        sbBlock.append(line).append("\n");
                        if (pair.getValue() == lineReader.getLineNumber()) {
                            count++;
                            String[] splitter = sbBlock.toString().split("> <");
                            long msInLine = Long.parseLong(splitter[9]);
                            if (isDateFits(msInLine, listOfDatesMs)) {
                                resultList.add(new Block(dateConverter.convertDateToXMLGrCal(msInLine), sbBlock.toString(), splitter[5]));
                            }
                            sbBlock.setLength(0);
                        }
                    }
                }
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
