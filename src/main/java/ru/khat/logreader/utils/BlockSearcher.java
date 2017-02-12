package ru.khat.logreader.utils;

import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

class BlockSearcher {

    private final static Logger logger = Logger.getLogger(LogLoaderFindStr.class.getName());

    List<Pair<Integer, Integer>> getBlocksBounds(String logPath, String input) {
        List<Pair<Integer, Integer>> blockBoundsList = new ArrayList<>();

        try {
            List<Integer> inputLines = getNumberOfLines(logPath, input);
            List<Integer> sharpLinesList = getNumberOfLines(logPath, "^####");

            if (inputLines.size() > 0) {
                BlockChecker blockChecker = new BlockChecker();

                for (int i = 0; i < sharpLinesList.size() - 1; i++) {
                    int currentNumber = sharpLinesList.get(i);
                    int length = sharpLinesList.get(i + 1) - currentNumber - 1;
                    Pair pair = new Pair<>(currentNumber, currentNumber + length);
                    if (blockChecker.isBlockFits(pair, inputLines)) {
                        blockBoundsList.add(pair);
                    }
                }
            }
        } catch (Exception ex) {
            logger.log(Level.FINEST, ex.getMessage());
        }
        return blockBoundsList;
    }

    private List<Integer> getNumberOfLines(String logPath, String input) {
        List<Integer> inputList = new ArrayList<>();

        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec("cmd /c findstr /i  /n \"" + input + "\" " + logPath);
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            while ((line = in.readLine()) != null && !line.equals("")) {
                String[] splitter = line.split(":");
                inputList.add(Integer.parseInt(splitter[0]));
            }
        } catch (IOException ex) {
            logger.log(Level.FINEST, ex.getMessage());
        }
        return inputList;
    }

    private class BlockChecker {
        int count = 0;

        boolean isBlockFits(Pair<Integer, Integer> pair, List<Integer> inputLines) {
            int inputNumber = inputLines.get(count);
            if (inputNumber >= pair.getKey() && inputNumber <= pair.getValue()) {
                for (; count < inputLines.size(); count++) {
                    if ((inputLines.size() == count + 1) || (inputLines.get(count + 1) > pair.getValue())) {
                        count++;
                        return true;
                    }
                }
            }
            return false;
        }
    }

}
