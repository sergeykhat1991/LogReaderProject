package ru.khat.logreader.types;


import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;


@XmlRootElement(name = "inputData")
public class InputData {

    private String searchIn;
    private String input;
    private String outputFormat;
    private List<PairOfDatesXMLGrCal> list = new ArrayList<>();

    public InputData() {

    }

    public String getSearchIn() {
        return searchIn;
    }

    public void setSearchIn(String searchIn) {
        this.searchIn = searchIn;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }

    public List<PairOfDatesXMLGrCal> getList() {
        return list;
    }

    public void setList(List<PairOfDatesXMLGrCal> list) {
        this.list = list;
    }
}
