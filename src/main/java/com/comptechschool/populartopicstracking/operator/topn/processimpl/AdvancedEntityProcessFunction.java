package com.comptechschool.populartopicstracking.operator.topn.processimpl;

import com.comptechschool.populartopicstracking.entity.AdvanceInputEntity;
import com.comptechschool.populartopicstracking.entity.InputEntity;
import com.comptechschool.populartopicstracking.operator.topn.processimpl.advancedsketch.AdvancedCountMinSketchAlg;
import com.comptechschool.populartopicstracking.operator.topn.sort.EntityHeapSortUtils;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;

import java.util.*;

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
    public void process(Context context, Iterable<InputEntity> iterable, Collector<List<Tuple3<Long, Long, String>>> collector) throws Exception {

        long start = System.currentTimeMillis();
        List<Tuple3<Long, Long, String>> tuples = new ArrayList<>();
        //Map<Long , InputEntity> entityMap = new HashMap<>();

        AdvancedCountMinSketchAlg sketch = new AdvancedCountMinSketchAlg(0.0001, 0.999, 1);
        //String type = iterable.iterator().next().getActionType();
        String type = "like";

        int size=0;
        for (InputEntity entity : iterable) {
            Long id = entity.getId();
            sketch.add(id, 1);
            //allMap.put(id, new InputEntity(id, 1L, type));
            if (!allMap.contains(entity.getId())) {
                allMap.put(id, new InputEntity(id, 1L, type));
                size++;
            }
        }

        AdvanceInputEntity[] inputEntities = new AdvanceInputEntity[size];

        int count = 0;
        for (InputEntity inputEntity:allMap.values()) {
            long id = inputEntity.getId();
            long freq = sketch.estimateCount(id);
            tuples.add(new Tuple3<>(id, freq, type));
            inputEntities[count] = new AdvanceInputEntity(freq, new InputEntity(id, 0L, type));
            count++;
        }

        EntityHeapSortUtils.getSortedArray(inputEntities, topN, //получение топ н
                Comparator.comparing(AdvanceInputEntity::getEventFrequency));

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