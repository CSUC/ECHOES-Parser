package org.csuc.Parser.Core.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.Parser.Core.jaxb.JaxbUnmarshal;
import org.csuc.Parser.Core.strategy.ParserMethod;
import org.csuc.Parser.Core.strategy.XPATH;
import org.openarchives.oai._2.OAIPMHtype;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author amartinez
 */
public class ParserOAI implements Parser {

    private static Logger logger = LogManager.getLogger(ParserOAI.class);

    private ParserMethod method;

    private AtomicInteger iter = new AtomicInteger(0);

    public ParserOAI(ParserMethod method){
        logger.info(String.format("Parser: %s", getClass().getSimpleName()));
        this.method = method;
    }

    @Override
    public void execute(String fileOrPath) {
        throw new IllegalArgumentException("execute fileOrPath is not valid for ParserFile!");
    }

    @Override
    public void execute(URL url) {
        try {
            OAIPMHtype OAIPMHtype =
                    (OAIPMHtype) new JaxbUnmarshal(url, OAIPMHtype.class).getObject();

            method.parser(url);
            if (OAIPMHtype.getListRecords().getResumptionToken() != null){
                if(!OAIPMHtype.getListRecords().getResumptionToken().getValue().isEmpty()){
                    logger.info(iter.incrementAndGet() + "\t" + OAIPMHtype.getListRecords().getResumptionToken().getValue());
                    execute(next(url, OAIPMHtype.getListRecords().getResumptionToken().getValue()));
                }
            }
        } catch(IOException e) {
            logger.error(e);
        }
    }

    @Override
    public List<XPATH> getXPATHResult() {
       return method.createXPATHResult();
    }

    @Override
    public Map<String, String> getNamespaceResult() {
        return method.createNamespaceResult();
    }

    private URL next(URL url, String resumptionToken) throws MalformedURLException {
        return new URL(String.format("%s?verb=ListRecords&resumptionToken=%s",
                url.toString().replaceAll("\\?verb=.+", ""), resumptionToken));
    }
}
