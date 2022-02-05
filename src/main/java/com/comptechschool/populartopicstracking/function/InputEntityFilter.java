package com.comptechschool.populartopicstracking.function;

import com.comptechschool.populartopicstracking.entity.InputEntity;
import com.comptechschool.populartopicstracking.utils.ConfigUtils;
import org.apache.flink.api.common.functions.FilterFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class InputEntityFilter implements FilterFunction<InputEntity> {
    private final String actionType;

    private final static Logger logger = LoggerFactory.getLogger(InputEntityFilter.class);

    public InputEntityFilter() throws IOException {
        actionType = ConfigUtils.getProperty("filter.action-type");
        logger.debug("Filter action type: {}", actionType);
    }

    @Override
    public boolean filter(InputEntity inputEntity) throws Exception {
        return inputEntity.getActionType().equalsIgnoreCase(actionType);
    }
}
