package com.comptechschool.populartopicstracking.function;

import com.comptechschool.populartopicstracking.entity.InputEntity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.flink.api.common.functions.MapFunction;

public class JsonToInputEntityMapper implements MapFunction<String, InputEntity> {

    private static final Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().create();

    @Override
    public InputEntity map(String json) throws Exception {
        return gson.fromJson(json, InputEntity.class);
    }
}
