package com.comptechschool.populartopicstracking.operator.topn.processimpl;

import com.comptechschool.populartopicstracking.entity.InputEntity;
import com.comptechschool.populartopicstracking.operator.topn.processimpl.advancedsketch.AdvancedCountMinSketchAlg;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;

import java.util.ArrayList;
import java.util.List;

public class AdvancedEntityProcessFunction extends AbstractProcess {

    MapStateDescriptor<Long, InputEntity> descriptorOfAllMap =
            new MapStateDescriptor<Long, InputEntity>("id_freq_all_map", Long.class, InputEntity.class);

    MapState<Long, InputEntity> allMap = null;
    private final int topN;

    public AdvancedEntityProcessFunction(int topN) {
        super();
        this.topN = topN;
    }

    @Override
    public void open(Configuration parameters) {
        allMap = getRuntimeContext().getMapState(descriptorOfAllMap);
    }

    @Override
    public void process(Context context, Iterable<InputEntity> iterable, Collector<List<Tuple4<Long, Long, String, Long>>> collector) throws Exception {

        long start = System.currentTimeMillis();
        List<Tuple4<Long, Long, String, Long>> tuples = new ArrayList<>();
        AdvancedCountMinSketchAlg sketch = new AdvancedCountMinSketchAlg(0.0001, 0.999, 1);
        String type = "like";

        for (InputEntity entity : iterable) {
            Long id = entity.getId();
            sketch.add(id, 1);
            if (!allMap.contains(entity.getId())) {
                allMap.put(id, new InputEntity(id, 1L, type));
            }
        }

        for (InputEntity inputEntity : allMap.values()) {
            long id = inputEntity.getId();
            long freq = sketch.estimateCount(id);
            tuples.add(new Tuple4<>(id, freq, type, inputEntity.getTimestamp()));
        }

        long finish = System.currentTimeMillis();
        long elapsed = finish - start;
        System.out.println("Top-N with advanced Sketch, ms: " + elapsed);
        collector.collect(tuples);
    }

    @Override
    public void clear(Context context) {
        allMap.clear();
    }
}