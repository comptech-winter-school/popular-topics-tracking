package com.comptechschool.populartopicstracking.operator.topn.processimpl;


import com.comptechschool.populartopicstracking.entity.InputEntity;
import com.comptechschool.populartopicstracking.operator.topn.AdvanceInputEntity;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
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
    public void process(Context context, Iterable<InputEntity> iterable, Collector<List<InputEntity>> collector) {
        Iterator<InputEntity> it = iterable.iterator();
        Map<Long , AdvanceInputEntity> idToEntityMap = new HashMap();
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

        List<AdvanceInputEntity> mapValues = new ArrayList<>(idToEntityMap.values());
        Collections.sort(mapValues , Collections.reverseOrder());

        //Temporarily
        System.out.println("TOP N:");
        for (int i = 0; i < topN; i++) {
            System.out.println(mapValues.get(i));
        }

        collector.collect(inputEntities);
    }



    @Override
    public void clear(Context context) {
        allMap.clear();
    }
}