package org.csuc.typesafe.server;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigBeanFactory;
import com.typesafe.config.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.typesafe.consumer.ProducerAndConsumerConfig;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * @author amartinez
 */
public class ServerConfig extends ConfigBeanFactory {

    private static Logger logger = LogManager.getLogger(ProducerAndConsumerConfig.class);

    private Path filename;

    public ServerConfig(Path filename) {
        this.filename = filename;
    }

    public Application getConfig(){
        Config defaultConfig = ConfigFactory.parseResources("echoes-gui-server.default.conf");
        if(Objects.nonNull(filename) && Files.exists(filename)){
            logger.debug("load config {}", filename);
            Config fallbackConfig = ConfigFactory.parseFile(filename.toFile())
                    .withFallback(defaultConfig)
                    .resolve();

            Application config = new Application(fallbackConfig.getConfig("echoes-gui-server"));
            return config;
        }else{
            logger.debug("load config defaults.conf");
            Application config = new Application(defaultConfig.getConfig("echoes-gui-server"));
            return config;
        }
    }

}
