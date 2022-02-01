package com.comptechschool.populartopicstracking.function;

import com.comptechschool.populartopicstracking.entity.InputEntity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.flink.api.common.functions.MapFunction;

public class InputEntityToJsonMapper implements MapFunction<InputEntity, String> {

    private static final Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().create();

    @Override
    public String map(InputEntity inputEntity) throws Exception {
        return gson.toJson(inputEntity);
    }
}
