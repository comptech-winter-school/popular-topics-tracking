package com.comptechschool.populartopicstracking.operator.topn;


import com.comptechschool.populartopicstracking.entity.InputEntity;
import com.comptechschool.populartopicstracking.operator.sort.CountMinSketch;
import com.comptechschool.populartopicstracking.operator.sort.EntityHeapSortUtils;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class EntityProcessFunction extends ProcessAllWindowFunction<InputEntity, List<InputEntity>, TimeWindow> {
    MapStateDescriptor<Long, AdvanceInputEntity> descriptorOfAllMap = new MapStateDescriptor<Long, AdvanceInputEntity>("id_freq_all_map", Long.class, AdvanceInputEntity.class);
    MapState<Long, AdvanceInputEntity> allMap = null;
    private final int topN;

    public EntityProcessFunction(int topN) {
        super();
        this.topN = topN;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        allMap = getRuntimeContext().getMapState(descriptorOfAllMap);
    }

    @Override
    public void process(Context context, Iterable<InputEntity> iterable, Collector<List<InputEntity>> collector) throws Exception {
        Iterator<InputEntity> it = iterable.iterator();
        long temp = 0;
        ArrayList<InputEntity> list = new ArrayList<>();
        while (it.hasNext()) {
            temp++;
            InputEntity inputEntity = it.next();
            list.add(inputEntity);

            Long id = inputEntity.getId();
            if (allMap.contains(id)) {
                AdvanceInputEntity advanceInput = allMap.get(id);
                advanceInput.setEventFrequency(advanceInput.getEventFrequency() + 1);
                allMap.put(id, advanceInput);
            } else {
                allMap.put(id, new AdvanceInputEntity(1L, inputEntity));
            }
        }

        System.out.println("==Each process:" + temp);

        AdvanceInputEntity[] inputEntities = EntityHeapSortUtils.
                formTopN(CountMinSketch.getFrequencyArray(1, list.size(), list), topN, Comparator
                        .comparing(AdvanceInputEntity::getEventFrequency));

        List<InputEntity> res = new ArrayList<>();
        for (int i = 0; i < inputEntities.length; i++) {
            res.add(inputEntities[i].getInputEntity());
        }

        //FIXME change res
        collector.collect(res);
    }

    @Override
    public void clear(Context context) throws Exception {
        allMap.clear();
    }
}
