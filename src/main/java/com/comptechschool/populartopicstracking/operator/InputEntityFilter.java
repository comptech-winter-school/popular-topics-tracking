package com.comptechschool.populartopicstracking.operator;

import com.comptechschool.populartopicstracking.entity.InputEntity;
import org.apache.flink.api.common.functions.FilterFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class InputEntityFilter implements FilterFunction<InputEntity> {
    private final static String FILE_PATH = "src/main/resources/config.properties";
    private final String actionType;

    private final static Logger logger = LoggerFactory.getLogger(InputEntityFilter.class);

    public InputEntityFilter() throws IOException {
        Properties properties = new Properties();
        FileInputStream fileInputStream = new FileInputStream(FILE_PATH);
        properties.load(fileInputStream);
        actionType = properties.getProperty("ACTION_TYPE");
        logger.debug("Filter action type: {}", properties.getProperty("ACTION_TYPE"));
    }

    @Override
    public boolean filter(InputEntity inputEntity) throws Exception {
        return inputEntity.getActionType().equalsIgnoreCase(actionType);
    }
}
