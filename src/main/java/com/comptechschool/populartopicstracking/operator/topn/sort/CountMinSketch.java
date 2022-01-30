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

    //I'm sorry...it looks terrible, but it's the best I could make
    //FIXME Fix data type and change hash function
    public static AdvanceInputEntity[] getFrequencyArray(long maxIncrement, long iterations, List<InputEntity> entityList) {
        CountMinSketch.Hasher<Long> hasher1 = Math::toIntExact;
        CountMinSketch.Hasher<Long> hasher4 = number4 -> String.valueOf(number4).hashCode();
        CountMinSketch.Hasher<Long> hasher2 = number3 -> String.valueOf(number3).hashCode();
        CountMinSketch.Hasher<Long> hasher3 = number3 -> String.valueOf(number3).hashCode();
        CountMinSketch.Hasher<Long> hasher5 = number5 -> String.valueOf(number5).hashCode();
        CountMinSketch.Hasher<Long> hasher6 = number6 -> String.valueOf(number6).hashCode();
        CountMinSketch.Hasher<Long> hasher7 = number7 -> String.valueOf(number7).hashCode();
        CountMinSketch.Hasher<Long> hasher8 = number8 -> String.valueOf(number8).hashCode();
        CountMinSketch.Hasher<Long> hasher9 = number9 -> String.valueOf(number9).hashCode();
        CountMinSketch.Hasher<Long> hasher10 = number10 -> String.valueOf(number10).hashCode();

        //10 independent hash functions are needed here. This is the second implementation option,
        // but I think you can make a separate method for this

/*        CountMinSketch.Hasher<Long> hasher5 = number -> (int) (number * 12);
        CountMinSketch.Hasher<Long> hasher8 = number -> (int) (number * 17);
        CountMinSketch.Hasher<Long> hasher3 = number -> {
            number ^= (number << 13);
            number ^= (number >> 17);
            number ^= (number << 5);
            return (int) Math.abs(number);
        };
        CountMinSketch.Hasher<Long> hasher7 = number -> {
            number ^= (number << 1);
            number ^= (number >> 8);
            number ^= (number << 12);
            return (int) Math.abs(number);
        };
        CountMinSketch.Hasher<Long> hasher10 = number -> {
            number ^= (number << 7);
            number ^= (number >> 4);
            number ^= (number << 11);
            return (int) Math.abs(number);
        };
        CountMinSketch.Hasher<Long> hasher2 = number -> {
            String strForm = String.valueOf(number);
            int hashVal = 0;
            for (int i = 0; i < strForm.length(); i++) {
                hashVal = strForm.charAt(i) + (31 * hashVal);
            }
            return hashVal;
        };
        CountMinSketch.Hasher<Long> hasher6 = number -> {
            String strForm = String.valueOf(number);
            int hashVal = 0;
            for (int i = 0; i < strForm.length(); i++) {
                hashVal = strForm.charAt(i) + (14 * hashVal);
            }
            return hashVal;
        };
        CountMinSketch.Hasher<Long> hasher9 = number -> {
            String test = String.valueOf(number);
            int hashVal = 0;
            for (int i = 0; i < test.length(); i++) {
                hashVal = test.charAt(i) + (3 * hashVal);
            }
            return hashVal;
        };*/

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
