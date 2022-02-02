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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AdvancedEntityProcessFunction extends AbstractProcess {

    MapStateDescriptor<Long, AdvanceInputEntity> descriptorOfAllMap =
            new MapStateDescriptor<Long, AdvanceInputEntity>("id_freq_all_map", Long.class, AdvanceInputEntity.class);

    MapState<Long, AdvanceInputEntity> allMap = null;
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
    public void process(Context context, Iterable<InputEntity> iterable, Collector<List<Tuple3<Long, Long, String>>> collector) {
        List<Tuple3<Long, Long, String>> tuples = new ArrayList<>();

        int size = 0;
        AdvancedCountMinSketchAlg sketch = new AdvancedCountMinSketchAlg(0.0001, 0.99999, 1);
        for (InputEntity entity : iterable) {
            sketch.add(entity.getId(), 1);
            size++;
        }

        AdvanceInputEntity[] advanceInputEntities = new AdvanceInputEntity[size];
        for (InputEntity entity : iterable) {
            InputEntity inputEntity = new InputEntity(entity.getId(), entity.getTimestamp(), entity.getActionType());
            long frequency = sketch.estimateCount(entity.getId());
            tuples.add(new Tuple3<>(inputEntity.getId(), frequency, inputEntity.getActionType()));
        }

        AdvanceInputEntity[] sortedArray = EntityHeapSortUtils.
                getSortedArray(advanceInputEntities, topN,
                        Comparator.comparing(AdvanceInputEntity::getEventFrequency));

        System.out.println("Top " + topN + " items in the Stream:");
        for (int i = 0; i < topN; i++) {
            System.out.println("[" + (i + 1) + "]" + sortedArray[i]);
        }

        collector.collect(tuples);
    }

    @Override
    public void clear(Context context) {
        allMap.clear();
    }
}