package ru.khat.logreader.types;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "outputData")
public class OutputData {

    private List<Block> list = new ArrayList<>();
    String urlFile;

    public List<Block> getList() {
        return list;
    }

    public void setList(List<Block> list) {
        this.list = list;
    }

    public String getUrlFile() {
        return urlFile;
    }

    public void setUrlFile(String urlFile) {
        this.urlFile = urlFile;
    }
}
