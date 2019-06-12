package org.csuc.typesafe.quality;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class QualityConfig {

    private static Logger logger = LogManager.getLogger(QualityConfig.class);

    private Path filename;

    public QualityConfig(Path filename) {
        this.filename = filename;
    }

    public Config getQualityConfig(){
        Config defaultConfig = ConfigFactory.parseResources("quality.defaults.conf");
        if(Objects.nonNull(filename) && Files.exists(filename)){
            logger.debug("load config {}", filename);
            Config fallbackConfig = ConfigFactory.parseFile(filename.toFile())
                    .withFallback(defaultConfig)
                    .resolve();
            return fallbackConfig;
        }else{
            logger.debug("load config defaults.conf");
            return defaultConfig;
        }
    }
}
