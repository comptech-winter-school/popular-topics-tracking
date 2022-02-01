package com.comptechschool.populartopicstracking.function;

import com.comptechschool.populartopicstracking.entity.InputEntity;
import org.apache.flink.api.java.functions.KeySelector;

public class InputEntityKeyBy implements KeySelector<InputEntity, Long> {
    @Override
    public Long getKey(InputEntity inputEntity) {
        return inputEntity.getId();
    }
}
