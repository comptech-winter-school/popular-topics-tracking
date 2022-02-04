package com.comptechschool.populartopicstracking.operator.topn.processimpl;

import com.comptechschool.populartopicstracking.entity.AdvanceInputEntity;
import com.comptechschool.populartopicstracking.entity.InputEntity;
import com.comptechschool.populartopicstracking.operator.topn.sort.CountMinSketchOptimization;
import com.comptechschool.populartopicstracking.operator.topn.sort.EntityHeapSortUtils;
import com.comptechschool.populartopicstracking.source.DataSource;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EntityProcessFunction extends AbstractProcess {

    MapStateDescriptor<Long, AdvanceInputEntity> descriptorOfAllMap =
            new MapStateDescriptor<Long, AdvanceInputEntity>("id_freq_all_map", Long.class, AdvanceInputEntity.class);

    DataSource dataSource;
    MapState<Long, AdvanceInputEntity> allMap = null;
    private final int topN;

    public EntityProcessFunction(int topN) {
        super();
        this.topN = topN;
    }

    public EntityProcessFunction(int topN, DataSource dataSource) {
        super();
        this.topN = topN;
        this.dataSource = dataSource;
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
    public void process(Context context, Iterable<InputEntity> iterable, Collector<List<Tuple4<Long, Long, String, Long>>> collector) {
        long start = System.currentTimeMillis();

        List<Tuple4<Long, Long, String, Long>> tuples = new ArrayList<>();
        AdvanceInputEntity[] advanceInputEntities = new CountMinSketchOptimization<InputEntity>().getFrequencyArray(iterable);
        for (int i = 0; i < advanceInputEntities.length; i++) {
            AdvanceInputEntity advanceInputEntity = advanceInputEntities[i];
            tuples.add(new Tuple4<>(
                    advanceInputEntity.getInputEntity().getId(),
                    advanceInputEntity.getEventFrequency(),
                    advanceInputEntity.getInputEntity().getActionType()
                    , advanceInputEntity.getInputEntity().getTimestamp()));
        }
        System.out.println("\n" + "=====Result separator=====" + "\n");

        AdvanceInputEntity[] sortedArray = EntityHeapSortUtils.
                getSortedArray(advanceInputEntities, topN,
                        Comparator.comparing(AdvanceInputEntity::getEventFrequency));

/*        System.out.println("Top " + topN + " items in the Stream:");
        for (int i = 0; i < topN; i++) {
            System.out.println("[" + (i + 1) + "]" + sortedArray[i]);
        }*/

        long finish = System.currentTimeMillis();
        long elapsed = finish - start;
        System.out.println("Top-N with sketch, ms: " + elapsed);
        collector.collect(tuples);
    }

    @Override
    public void clear(Context context) {
        allMap.clear();
    }
}
