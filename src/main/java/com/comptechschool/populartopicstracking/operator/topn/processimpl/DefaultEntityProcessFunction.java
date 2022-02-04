package com.comptechschool.populartopicstracking.operator.topn.processimpl;

import com.comptechschool.populartopicstracking.entity.AdvanceInputEntity;
import com.comptechschool.populartopicstracking.entity.InputEntity;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;

import java.util.*;

public class DefaultEntityProcessFunction extends AbstractProcess {

    MapStateDescriptor<Long, AdvanceInputEntity> descriptorOfAllMap = new MapStateDescriptor<Long, AdvanceInputEntity>("id_freq_all_map", Long.class, AdvanceInputEntity.class);
    MapState<Long, AdvanceInputEntity> allMap = null;

    private final int topN;

    public DefaultEntityProcessFunction(int topN) {
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

        Iterator<InputEntity> it = iterable.iterator();
        Map<Long, AdvanceInputEntity> idToEntityMap = new HashMap();
        List<InputEntity> inputEntities = new ArrayList<>();
        while (it.hasNext()) {
            InputEntity inputEntity = it.next();
            Long id = inputEntity.getId();
            inputEntities.add(inputEntity);
            if (idToEntityMap.containsKey(id)) {
                AdvanceInputEntity advanceInput = idToEntityMap.get(id);
                advanceInput.setEventFrequency(advanceInput.getEventFrequency() + 1);
                idToEntityMap.put(id, advanceInput);
            } else {
                idToEntityMap.put(id, new AdvanceInputEntity(1L, inputEntity));
            }
        }
        allMap.putAll(idToEntityMap);

        List<Tuple4<Long, Long, String, Long>> tuples = new ArrayList<>();
        for (int i = 0; i < inputEntities.size(); i++) {
            long id = inputEntities.get(i).getId();
            tuples.add(new Tuple4<>(id,
                    idToEntityMap.get(id).getEventFrequency(),
                    inputEntities.get(i).getActionType(),
                    inputEntities.get(i).getTimestamp()));
        }

        long finish = System.currentTimeMillis();
        long elapsed = finish - start;
        System.out.println("Top-N with default, ms: " + elapsed);
        collector.collect(tuples);
    }


    @Override
    public void clear(Context context) {
        allMap.clear();
    }
}