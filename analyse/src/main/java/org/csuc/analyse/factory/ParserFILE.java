package org.csuc.analyse.factory;

import org.apache.hadoop.fs.FileSystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.analyse.strategy.ParserMethod;
import org.csuc.analyse.util.Garbage;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author amartinez
 */
public class ParserFILE implements Parser {

    private static Logger logger = LogManager.getLogger(ParserFILE.class);

    private ParserMethod method;

    private AtomicInteger iter = new AtomicInteger(0);
    private int buffer = 15000;

    public ParserFILE(ParserMethod method) {
        logger.debug(String.format("analyse: %s", getClass().getSimpleName()));
        this.method = method;
    }

    @Override
    public void execute(String fileOrPath) throws Exception {
        Path path = Paths.get(fileOrPath);
        if (Objects.nonNull(path) && Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
            Files.walk(path)
                    .filter(Files::isRegularFile)
//                    .filter(f -> f.toString().endsWith(".xml"))
                    .forEach(f -> {
                        if ((iter.incrementAndGet() % buffer) == 0) Garbage.gc();
                        logger.info(String.format("%s file: %s", iter.get(), f.getFileName()));
                        try {
                            method.parser(f.toString());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
    }

    @Override
    public void execute(URL url) throws Exception {
        throw new IllegalArgumentException("execute URL is not valid for ParserFile!");
    }

    @Override
    public void XML(OutputStream outs) {
        method.createXML(outs);
    }

    @Override
    public void HDFS_XML(FileSystem fileSystem, org.apache.hadoop.fs.Path dest) throws IOException {
        method.createHDFS_XML(fileSystem, dest);
    }

    @Override
    public void JSON(OutputStream outs) {
        method.createJSON(outs);
    }

    @Override
    public void HDFS_JSON(FileSystem fileSystem, org.apache.hadoop.fs.Path dest) throws IOException {
        method.createHDFS_JSON(fileSystem, dest);
    }

}
