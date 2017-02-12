package ru.khat.logreader.types;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Comparator;

@XmlRootElement
public class Block implements Comparator<Block> {


    private XMLGregorianCalendar xmlGCDate;
    private String textBlock;
    private String thread;


    public Block(XMLGregorianCalendar xmlGCDate, String textBlock, String thread) {
        this.xmlGCDate = xmlGCDate;
        this.textBlock = textBlock;
        this.thread = thread;
    }

    public Block(String textBlock){
        this.textBlock = textBlock;
    }

    public Block(){

    }

    @Override
    public int compare(Block o1, Block o2) {

        return 0;
    }

    public XMLGregorianCalendar getXmlGCDate() {
        return xmlGCDate;
    }

    public void setXmlGCDate(XMLGregorianCalendar xmlGCDate) {
        this.xmlGCDate = xmlGCDate;
    }

    public String getTextBlock() {
        return textBlock;
    }

    public void setTextBlock(String textBlock) {
        this.textBlock = textBlock;
    }

    public String getThread() {
        return thread;
    }

    public void setThread(String thread) {
        this.thread = thread;
    }

    @Override
    public String toString() {
        return xmlGCDate + "\n" + textBlock + thread + "\n";
    }
}
