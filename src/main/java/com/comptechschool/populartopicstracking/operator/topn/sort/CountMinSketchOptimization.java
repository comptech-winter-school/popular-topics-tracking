package com.comptechschool.populartopicstracking.operator.topn.sort;

import com.comptechschool.populartopicstracking.entity.AdvanceInputEntity;
import com.comptechschool.populartopicstracking.entity.InputEntity;

import java.util.Random;

public class CountMinSketchOptimization<T> {

    public interface Hasher<T> {
        int hash(T obj);
    }

    private final int width = 20000;
    private final int depth = 10;

    private final CountMinSketch.Hasher<T>[] hashers;
    private final int[][] frequencyMatrix;


    @SafeVarargs
    public CountMinSketchOptimization(CountMinSketch.Hasher<T>... hashers) {
        //this.width = width;
        this.hashers = hashers;
        //depth = hashers.length;*/
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
    public AdvanceInputEntity[] getFrequencyArray(Iterable<InputEntity> iterable) {

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

        //TODO: Tests with another hash functions

        CountMinSketch<Long> cms = new CountMinSketch<>(20000, hasher1, hasher2, hasher3,
                hasher4, hasher5, hasher6, hasher7,
                hasher8, hasher9, hasher10);
        Random rand = new Random();

        //HashMap<Long, Long> freqCount = new HashMap<>();
        int i = 0;
        for (InputEntity entity : iterable) {
/*            int increment = rand.nextInt(1) + 1;
            freqCount.compute(entity.getId(), (k, v) -> v == null ? increment : v + increment); /*/
            cms.update(entity.getId(), 1);
            i++;
        }

        int count = 0;
        AdvanceInputEntity[] entitiesArray = new AdvanceInputEntity[i];
        for (InputEntity entity : iterable) {
            Long key = entity.getId();
/*            System.out.println(count + ") " + "For id: " + key + "\t real count: " + freqCount.get(key) + "\t estimated count: "
                    + cms.estimate(key));*/
            entitiesArray[count] = new AdvanceInputEntity(cms.estimate(key), new InputEntity(key, 0L, entity.getActionType()));
            count++;
        }
        return entitiesArray;
    }
}
