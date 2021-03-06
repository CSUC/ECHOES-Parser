package org.csuc.format;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.dao.entity.QualityDetails;
import org.csuc.step.StepInterface;
import org.csuc.util.FormatType;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class Json implements FormatInterface {

    private Logger logger = LogManager.getLogger(getClass().getSimpleName());

    private StepInterface<QualityDetails> stepInterface;
    private Path out;

    public Json(StepInterface stepInterface) {
        logger.info("{}", getClass().getSimpleName());

        Objects.requireNonNull(stepInterface, "Schema must not be null");
        this.stepInterface = stepInterface;
    }

    public Json(StepInterface stepInterface, Path out) throws IOException {
        logger.info("{}", getClass().getSimpleName());

        Objects.requireNonNull(stepInterface, "Schema must not be null");
        this.stepInterface = stepInterface;
        this.out = Objects.isNull(out) ? null
                : (Files.notExists(out) ? Files.createDirectories(out) : out);
    }

    @Override
    public void execute(Path path) throws IOException {
        Files.walk(path)
                .parallel()
                .filter(Files::isRegularFile)
                .filter(f -> FormatType.RDFXML.lang().getFileExtensions().stream().anyMatch(m -> f.toString().endsWith(String.format(".%s", m))))
                .forEach(p -> {
                    try {
                        QualityDetails qualityDetails = stepInterface.quality(p);

                        if (Objects.isNull(out)) logger.info(qualityDetails);
                        else
                            Files.write(Paths.get(String.format("%s/%s.json", out, FilenameUtils.removeExtension(p.getFileName().toString()))), qualityDetails.toString().getBytes());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    @Override
    public void execute(URL url) throws Exception {
        QualityDetails qualityDetails = stepInterface.quality(url);

        if (Objects.isNull(out)) logger.info(qualityDetails);
        else
            Files.write(Paths.get(String.format("%s/%s.json", out, qualityDetails.get_id())), qualityDetails.toString().getBytes());
    }
}
