package com.comptechschool.populartopicstracking.operator.topn.sort;

import com.comptechschool.populartopicstracking.entity.InputEntity;
import com.comptechschool.populartopicstracking.operator.topn.AdvanceInputEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class CountMinSketch<T> {

    public static interface Hasher<T> {
        public int hash(T obj);
    }

    private final int depth;
    private final int width;
    private final Hasher<T>[] hashers;
    private final int[][] frequencyMatrix;

    @SafeVarargs
    public CountMinSketch(int width, Hasher<T>... hashers) {
        this.width = width;
        this.hashers = hashers;
        depth = hashers.length;
        frequencyMatrix = new int[depth][width];
    }

    public void update(T item, int count) {
        for (int row = 0; row < depth; row++) {
            frequencyMatrix[row][boundHashCode(hashers[row].hash(item))] += count;
        }
    }

    public int estimate(T item) {
        int count = Integer.MAX_VALUE;
        for (int row = 0; row < depth; row++) {
            count = Math.min(count, frequencyMatrix[row][boundHashCode(hashers[row].hash(item))]);
        }
        return count;
    }

    private int boundHashCode(int hashCode) {
        return hashCode % width;
    }

    public static int firstHashFunc(long number) {
        int hash = (String.valueOf(number).hashCode());
        return hash;
    }

    public static int secondHashFunc(long number, int i) {
        int hash = (String.valueOf(number).hashCode() * i);
        return hash;
    }

    public static int getRandomNumber() {
        return (int) (Math.random() * 32) + 19;
    }

    //FIXME Fix data type and change hash function
    public static AdvanceInputEntity[] getFrequencyArray(long maxIncrement, long iterations, List<InputEntity> entityList) {

        /*
        CountMinSketch.Hasher<Long> hasher1 = number1 -> firstHashFunc(number1) + secondHashFunc(number1, 1);
        CountMinSketch.Hasher<Long> hasher2 = number2 -> firstHashFunc(number2) + secondHashFunc(number2, 2);
        CountMinSketch.Hasher<Long> hasher3 = number3 -> firstHashFunc(number3) + secondHashFunc(number3, 3);
        CountMinSketch.Hasher<Long> hasher4 = number4 -> firstHashFunc(number4) + secondHashFunc(number4, 4);
        CountMinSketch.Hasher<Long> hasher5 = number5 -> firstHashFunc(number5) + secondHashFunc(number5, 5);
        CountMinSketch.Hasher<Long> hasher6 = number6 -> firstHashFunc(number6) + secondHashFunc(number6, 6);
        CountMinSketch.Hasher<Long> hasher7 = number7 -> firstHashFunc(number7) + secondHashFunc(number7, 7);
        CountMinSketch.Hasher<Long> hasher8 = number8 -> firstHashFunc(number8) + secondHashFunc(number8, 8);
        CountMinSketch.Hasher<Long> hasher9 = number9 -> firstHashFunc(number9) + secondHashFunc(number9, 9);
        CountMinSketch.Hasher<Long> hasher10 = number10 -> firstHashFunc(number10) + secondHashFunc(number10, 10);
        */

        CountMinSketch.Hasher<Long> hasher1 = Math::toIntExact;
        CountMinSketch.Hasher<Long> hasher4 = number4 -> String.valueOf(number4).hashCode();
        CountMinSketch.Hasher<Long> hasher2 = number2 -> String.valueOf(number2).hashCode();
        CountMinSketch.Hasher<Long> hasher3 = number3 -> String.valueOf(number3).hashCode();
        CountMinSketch.Hasher<Long> hasher5 = number5 -> String.valueOf(number5).hashCode();
        CountMinSketch.Hasher<Long> hasher6 = number6 -> String.valueOf(number6).hashCode();
        CountMinSketch.Hasher<Long> hasher7 = number7 -> String.valueOf(number7).hashCode();
        CountMinSketch.Hasher<Long> hasher8 = number8 -> String.valueOf(number8).hashCode();
        CountMinSketch.Hasher<Long> hasher9 = number9 -> String.valueOf(number9).hashCode();
        CountMinSketch.Hasher<Long> hasher10 = number10 -> String.valueOf(number10).hashCode();

        int numberOfBuckets = 20000;
        CountMinSketch<Long> cms = new CountMinSketch<>(numberOfBuckets, hasher1, hasher2, hasher3,
                hasher4, hasher5, hasher6, hasher7,
                hasher8, hasher9, hasher10);
        Random rand = new Random();
        HashMap<Long, Long> freqCount = new HashMap<>();

        for (int i = 0; i < iterations; i++) {
            int increment = rand.nextInt((int) maxIncrement) + 1;
            freqCount.compute(entityList.get(i).getId(), (k, v) -> v == null ? increment : v + increment);
            cms.update(entityList.get(i).getId(), increment);
        }

        int count = 0;
        AdvanceInputEntity[] entitiesArray = new AdvanceInputEntity[freqCount.size()];
        for (Long key : freqCount.keySet()) {
/*            System.out.println(count + ") " + "For id: " + key + "\t real count: " + freqCount.get(key) + "\t estimated count: "
                    + cms.estimate(key));*/
            entitiesArray[count] = new AdvanceInputEntity(cms.estimate(key), new InputEntity(key, 0L, ""));
            count++;
        }
        return entitiesArray;
    }
}
