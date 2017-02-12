package ru.khat.logreader.ws;

import ru.khat.logreader.Singleton.ThreadsPool;
import ru.khat.logreader.types.Block;
import ru.khat.logreader.types.PairOfDatesXMLGrCal;
import ru.khat.logreader.types.InputData;
import ru.khat.logreader.types.OutputData;
import ru.khat.logreader.utils.LogLoaderFindStr;
import ru.khat.logreader.utils.FileWriter;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;


@Path("TestREST")
public class TestREST {

    @POST
    @Path("mainPOST")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public OutputData writeLogs(InputData rest) {

        String searchIn = rest.getSearchIn();
        String input = rest.getInput();
        String outputFormat = rest.getOutputFormat();
        List<PairOfDatesXMLGrCal> pairOfDatesPeriods = rest.getList();

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
            outputData.setUrlFile("http://192.168.0.171:7001/out/log_" + name + "." + outputFormat);
        }
        return outputData;
    }
}