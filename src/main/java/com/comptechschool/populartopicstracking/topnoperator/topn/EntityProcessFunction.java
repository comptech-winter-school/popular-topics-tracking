package com.comptechschool.populartopicstracking.topnoperator.topn;


import com.comptechschool.populartopicstracking.entity.InputEntity;
import com.comptechschool.populartopicstracking.topnoperator.sort.EntityHeapSortUtils;

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
    MapStateDescriptor<Long, AdvanceInputEntity> descriptorOfAllMap = new MapStateDescriptor<Long, AdvanceInputEntity>("id_event_all_map", Long.class, AdvanceInputEntity.class);
    MapState<Long, AdvanceInputEntity> allMap = null; //Key - id , Value - event frequency and InputEntity
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
        int temp = 0;
        while (it.hasNext()) {
            temp++;
            InputEntity inputEntity = it.next();

            Long id = inputEntity.getId();
            if (allMap.contains(id)) {
                AdvanceInputEntity advanceInput = allMap.get(id);
                advanceInput.setEventFrequency(advanceInput.getEventFrequency()+1);
                allMap.put(id, advanceInput);
            } else {
                allMap.put(1L , new AdvanceInputEntity(id , inputEntity));
            }
        }

        System.out.println("==Each process:" + temp);

        ArrayList<AdvanceInputEntity> list = new ArrayList<>();
        for (AdvanceInputEntity advanceInputEntity : allMap.values()) {
            list.add(advanceInputEntity);
        }
        AdvanceInputEntity[] entitiesArray = new AdvanceInputEntity[list.size()];
        list.toArray(entitiesArray);
        AdvanceInputEntity[] inputEntities = EntityHeapSortUtils.formTopN(entitiesArray, topN, Comparator.comparing(AdvanceInputEntity::getEventFrequency).reversed());


        List<InputEntity> res = new ArrayList<>();

        //Guaranteed sort order
        for (int i = 0; i < inputEntities.length; i++) {
            res.add(inputEntities[i].getInputEntity());
        }

        collector.collect(res);
    }

    @Override
    public void clear(Context context) throws Exception {
        allMap.clear();
    }
}
