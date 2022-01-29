package com.comptechschool.populartopicstracking.topnoperator.topn;


import com.comptechschool.populartopicstracking.entity.InputEntity;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.util.List;

public class EntityProcessFunction extends ProcessAllWindowFunction<InputEntity, List<InputEntity>, TimeWindow> {
    MapStateDescriptor<Long, InputEntity> descriptorOfAllMap = new MapStateDescriptor<Long, InputEntity>("sku_order_all_map", Long.class, InputEntity.class);
    MapState<Long, InputEntity> allMap = null;
    private int topSize;

    public EntityProcessFunction(int topSize) {
        super();
        this.topSize = topSize;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        allMap = getRuntimeContext().getMapState(descriptorOfAllMap);
    }

    @Override
    public void process(Context context, Iterable<InputEntity> iterable, Collector<List<InputEntity>> collector) throws Exception {
/*        Iterator<InputEntity> it = iterable.iterator();
        int temp = 0;
        while (it.hasNext()) {
            temp++;
            InputEntity inputEntity = it.next();
            //Long skuId = inputEntity.getSkuId();
            Long id = inputEntity.getId(); //FIXME
            if (allMap.contains(id)) {
                InputEntity allMapOrder = allMap.get(id);
                //allMapOrder.setNum(allMapOrder.getNum() + order.getNum()); //FIXME
                //allMap.put(skuId, allMapOrder); //FIXME
            } else {
                allMap.put(id, inputEntity);
            }
        }

        System.out.println("==Each process:" + temp);
        ArrayList<InputEntity> list = new ArrayList<>();
        Iterator<InputEntity> iterator = allMap.values().iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        InputEntity[] arr = new InputEntity[list.size()];
        list.toArray(arr);
        InputEntity[] inputEntities = OrderHeapSortUtils.topN(arr, topSize, Comparator.comparing(InputEntity::getNum).reversed());
        List<InputEntity> res = new ArrayList<>();
        //Guaranteed sort order
        for (int i = 0; i < inputEntities.length; i++) {
            res.add(inputEntities[i]);
        }
        collector.collect(res);*/
    }

    @Override
    public void clear(Context context) throws Exception {
        allMap.clear();
    }


}
