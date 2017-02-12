package ru.khat.logreader.ws;


import ru.khat.logreader.Singleton.ThreadsPool;
import ru.khat.logreader.types.Block;
import ru.khat.logreader.types.OutputData;
import ru.khat.logreader.types.PairOfDatesXMLGrCal;
import ru.khat.logreader.utils.FileWriter;
import ru.khat.logreader.utils.LogLoaderFindStr;

import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;

@WebService(name = "TestSoap")
@Stateless

public class TestWS {

    @WebMethod(operationName = "writeLogs")
    public OutputData writeLogs(@WebParam(name = "searchIn") String searchIn,
                                @WebParam(name = "input") String input,
                                @WebParam(name = "outputFormat") String outputFormat,
                                @WebParam(name = "dateFromXMLGrCal") List<PairOfDatesXMLGrCal> pairOfDatesPeriods) throws Exception {

        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy_HH-mm-ss-SSS");
        String name = dateFormat.format(new Date());
        OutputData outputData = new OutputData();

        if (outputFormat.equals("text")) {
            List<Block> resultList = new LogLoaderFindStr().getResultList(searchIn, input, pairOfDatesPeriods);
            Collections.sort(resultList, (b1, b2) -> b1.getXmlGCDate().compare(b2.getXmlGCDate()));
            outputData.setList(resultList);
        } else {
            ExecutorService service = ThreadsPool.getInstance().getService();
            service.execute(new FileWriter(searchIn, input, pairOfDatesPeriods, name, outputFormat));
            outputData.setUrlFile("log_" + name + "." + outputFormat);
        }
        return outputData;
    }
}