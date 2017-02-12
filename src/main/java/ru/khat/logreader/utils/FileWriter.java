package ru.khat.logreader.utils;


import org.apache.fop.apps.*;
import ru.khat.logreader.types.Block;
import ru.khat.logreader.types.PairOfDatesXMLGrCal;
import ru.khat.logreader.types.OutputData;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.Collections;
import java.util.List;

public class FileWriter implements Runnable {

    private String searchIn;
    private String input;
    private List<PairOfDatesXMLGrCal> pairOfDatesPeriods;
    private String name;
    private String outputFormat;

    public FileWriter(String searchIn, String input, List<PairOfDatesXMLGrCal> pairOfDatesPeriods, String name, String outputFormat) {
        this.searchIn = searchIn;
        this.input = input;
        this.pairOfDatesPeriods = pairOfDatesPeriods;
        this.name = name;
        this.outputFormat = outputFormat;
    }

    @Override
    public void run() {

        List<Block> resultList = new LogLoaderFindStr().getResultList(searchIn, input, pairOfDatesPeriods);
        Collections.sort(resultList, (b1, b2) -> b1.getXmlGCDate().compare(b2.getXmlGCDate()));
        String filePath = (System.getProperty("user.dir") + "\\downloads\\log_" + name);
        determineFormat(filePath, resultList);
        Thread.currentThread().interrupt();

    }

    private void determineFormat(String filePath, List<Block> resultList) {

        switch (outputFormat) {
            case "xml":
                convertToXML(filePath, resultList);
                break;
            case "log":
                convertToLOG(filePath, resultList);
                break;
            case "html":
            case "doc":
                convertToHTMLorDOC(filePath, resultList, outputFormat);
                break;
            case "pdf":
            case "rtf":
                convertToPDForRTF(filePath, resultList, outputFormat);
                break;
        }
    }

    private void convertToXML(String filePath, List<Block> resultList) {

        OutputData outputData = new OutputData();
        outputData.setList(resultList);

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(OutputData.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(outputData, new File(filePath + ".xml"));
        } catch (JAXBException ex) {
            ex.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    private void convertToLOG(String filepath, List<Block> resultList) {

        try {
            try (java.io.FileWriter fileWriter = new java.io.FileWriter(new File(filepath + ".log"))) {
                for (Block block : resultList) {
                    fileWriter.write(block.getXmlGCDate().toString() + "\n" + block.getThread() + "\n" + block.getTextBlock() + "\n");
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    private void convertToHTMLorDOC(String filePath, List<Block> resultList, String outputFormat) {

        convertToXML(filePath, resultList);

        try {
            TransformerFactory tFactory = TransformerFactory.newInstance();

            Source xsl = new StreamSource(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("/html.xsl")));
            Source doc = new StreamSource(filePath + ".xml");
            String outputFileName = filePath + "." + outputFormat;
            try (OutputStream htmlFile = new FileOutputStream(outputFileName)) {
                Transformer transform = tFactory.newTransformer(xsl);
                transform.transform(doc, new StreamResult(htmlFile));
            }
        } catch (IOException | TransformerException ex) {
            ex.printStackTrace();
            Thread.currentThread().interrupt();
        }
        new File(filePath + ".xml").delete();
    }

    private void convertToPDForRTF(String filePath, List<Block> resultList, String outputFormat) {

        convertToXML(filePath, resultList);

        FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
        // File xslFile = new File(System.getProperty("user.dir") + "\\xsl\\pdf.xsl");
        StreamSource xmlSource = new StreamSource(new File(filePath + ".xml"));

        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(filePath + "." + outputFormat))) {

            Fop fop;
            if (outputFormat.equals("rtf")) {
                fop = fopFactory.newFop(MimeConstants.MIME_RTF, out);
            } else {
                fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);
            }

            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("/pdf.xsl"))));
            Result res = new SAXResult(fop.getDefaultHandler());
            transformer.transform(xmlSource, res);

        } catch (IOException | FOPException | TransformerException ex) {
            ex.printStackTrace();
            Thread.currentThread().interrupt();
        }
        new File(filePath + ".xml").delete();
    }

}
