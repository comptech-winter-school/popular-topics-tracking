package com.comptechschool.populartopicstracking.operator.topn.processimpl;

import com.comptechschool.populartopicstracking.entity.AdvanceInputEntity;
import com.comptechschool.populartopicstracking.entity.InputEntity;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple3;
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
    public void process(Context context, Iterable<InputEntity> iterable, Collector<List<Tuple3<Long, Long, String>>> collector) throws Exception {
        long start = System.currentTimeMillis();

        Iterator<InputEntity> it = iterable.iterator();
        Map<Long, AdvanceInputEntity> idToEntityMap = new HashMap();
        List<InputEntity> inputEntities = new ArrayList<>();
        long temp = 0;
        while (it.hasNext()) {
            temp++;
            InputEntity inputEntity = it.next();
            Long id = inputEntity.getId();
            inputEntities.add(inputEntity);
            try {
                if (idToEntityMap.containsKey(id)) {
                    AdvanceInputEntity advanceInput = idToEntityMap.get(id);
                    advanceInput.setEventFrequency(advanceInput.getEventFrequency() + 1);
                    idToEntityMap.put(id, advanceInput);
                } else {
                    idToEntityMap.put(id, new AdvanceInputEntity(1L, inputEntity));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        allMap.putAll(idToEntityMap);

        List<AdvanceInputEntity> mapValues = new ArrayList<>(idToEntityMap.values());
        Collections.sort(mapValues, Collections.reverseOrder());

/*        System.out.println("TOP N:");
        for (int i = 0; i < topN; i++) {
            System.out.println(mapValues.get(i));
        }*/

        List<Tuple3<Long, Long, String>> tuples = new ArrayList<>();
        for (int i = 0; i < inputEntities.size(); i++) {
            long id = inputEntities.get(i).getId();
            tuples.add(new Tuple3<>(id, idToEntityMap.get(id).getEventFrequency(), inputEntities.get(i).getActionType()));
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