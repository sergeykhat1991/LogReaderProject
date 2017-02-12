package ru.khat.logreader.utils;

import javafx.util.Pair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.khat.logreader.types.PairOfDatesMs;

class LogSearcher {

    private final static Logger logger = Logger.getLogger(LogLoaderFindStr.class.getName());

    List<String> getLogsPath(String searchIn, List<PairOfDatesMs> listOfDatesLong) {

        List<String> listOfServerPaths = getServersPaths(searchIn);
        List<String> listOfFiles = new ArrayList<>();

        try {
            for (String serverPath : listOfServerPaths) {
                Runtime runtime = Runtime.getRuntime();
                Process process = runtime.exec("cmd /c findstr /R /M \"" + getTimestampPattern(listOfDatesLong) + "\" " + serverPath + "*.log");
                BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    listOfFiles.add(line);
                }
            }
        } catch (IOException ex) {
            logger.log(Level.FINEST, ex.getMessage());
        }
        return listOfFiles;
    }

    private String getTimestampPattern(List<PairOfDatesMs> listOfDatesLong) {

        Pair<Long, Long> minAndMaxLong = getMinAndMaxLong(listOfDatesLong);
        long minLong = minAndMaxLong.getKey();
        long maxLong = minAndMaxLong.getValue();
        String firstNumbers = "";
        String textMinLong = minLong + "";
        String textMaxLong = maxLong + "";
        int amountOfNumbers = 0;

        for (; amountOfNumbers < 13; amountOfNumbers++) {
            if (textMinLong.startsWith(textMaxLong.substring(0, amountOfNumbers + 1))) {
                firstNumbers = textMaxLong.substring(0, amountOfNumbers + 1);
            } else {
                break;
            }
        }
        amountOfNumbers = 13 - amountOfNumbers;
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < amountOfNumbers; i++) {
            sb.append(".");
        }
        return "<" + firstNumbers + sb.toString();
    }

    private List<String> getServersPaths(String searchIn) {

        List<String> listOfServersPaths = new ArrayList<>();
        List<String> temporaryServersPaths = new ArrayList<>();
        String domainPath = System.getProperty("user.dir");

        try {

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new File(domainPath + "\\config\\config.xml"));
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName("server");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node nNode = nodeList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) nNode;
                    String serverName = element.getElementsByTagName("name").item(0).getTextContent();
                    if (element.getElementsByTagName("cluster").item(0) != null && element.getElementsByTagName("cluster").item(0).getTextContent().equals(searchIn)) {
                        listOfServersPaths.add((domainPath + "\\servers\\" + serverName + "\\logs\\"));

                    } else {
                        if (serverName.equals(searchIn)) {
                            listOfServersPaths.add((domainPath + "\\servers\\" + serverName + "\\logs\\"));
                        }
                        temporaryServersPaths.add((domainPath + "\\servers\\" + serverName + "\\logs\\"));
                    }
                }
            }
            if ((searchIn.equals("") || searchIn.equals("domain")) && listOfServersPaths.size() == 0) {
                listOfServersPaths = temporaryServersPaths;
            }
        } catch (ParserConfigurationException | IOException | SAXException ex) {
            logger.log(Level.FINEST, ex.getMessage());
        }
        return listOfServersPaths;
    }

    private Pair<Long, Long> getMinAndMaxLong(List<PairOfDatesMs> listOfDatesLong) {

        long minLong = listOfDatesLong.get(0).getFrom();
        long maxLong = listOfDatesLong.get(0).getTo();

        for (PairOfDatesMs pair : listOfDatesLong) {
            if (pair.getFrom() < minLong) {
                minLong = pair.getFrom();
            }
            if (pair.getTo() > maxLong) {
                maxLong = pair.getTo();
            }
        }
        return new Pair<>(minLong, maxLong);
    }

}
