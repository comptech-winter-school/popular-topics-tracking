package com.comptechschool.populartopicstracking.operator.topn.processimpl;

import com.comptechschool.populartopicstracking.entity.AdvanceInputEntity;
import com.comptechschool.populartopicstracking.entity.InputEntity;
import com.comptechschool.populartopicstracking.operator.topn.sort.CountMinSketchOptimization;
import com.comptechschool.populartopicstracking.operator.topn.sort.EntityHeapSortUtils;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.util.Collector;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EntityProcessFunction extends AbstractProcess {

    MapStateDescriptor<Long, AdvanceInputEntity> descriptorOfAllMap =
            new MapStateDescriptor<Long, AdvanceInputEntity>("id_freq_all_map", Long.class, AdvanceInputEntity.class);

    MapState<Long, AdvanceInputEntity> allMap = null;
    private final int topN;

    public EntityProcessFunction(int topN) {
        super();
        this.topN = topN;
    }

    @Override
    public void open(Configuration parameters) {
        allMap = getRuntimeContext().getMapState(descriptorOfAllMap);
    }

    /**
     * Computational complexity of mathematical operations and algorithms (Heap Sort and Count min sketch):
     * Algorithm |    Memory    |    Time     |
     * ----------------------------------------
     * HeapSort  |     O(1)     |  O(nLog(n)) |
     * Sketch    |     O(1)     |  O(nLog(n)) |
     */

    @Override
    public void process(Context context, Iterable<InputEntity> iterable, Collector<List<Tuple3<Long, Long, String>>> collector) {
        List<Tuple3<Long, Long, String>> tuples = new ArrayList<>();

        AdvanceInputEntity[] advanceInputEntities = new CountMinSketchOptimization<InputEntity>().getFrequencyArray(iterable);
        for (int i = 0; i < advanceInputEntities.length; i++) {
            AdvanceInputEntity advanceInputEntity = advanceInputEntities[i];
            tuples.add(new Tuple3<>(
                    advanceInputEntity.getInputEntity().getId(),
                    advanceInputEntity.getEventFrequency(),
                    advanceInputEntity.getInputEntity().getActionType()));
        }
        System.out.println("\n" + "=====Result separator=====" + "\n");

        AdvanceInputEntity[] sortedArray = EntityHeapSortUtils.
                getSortedArray(advanceInputEntities, topN,
                        Comparator.comparing(AdvanceInputEntity::getEventFrequency));

        System.out.println("Top " +topN+ " items in the Stream:"  );
        for (int i = 0; i < topN; i++) {
            System.out.println("["+(i+1)+"]" + sortedArray[i]);
        }

        collector.collect(tuples);
    }

    @Override
    public void clear(ProcessAllWindowFunction.Context context) {
        allMap.clear();
    }
}
