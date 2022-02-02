package com.comptechschool.populartopicstracking.operator.topn.advancedsketch;


public class ConservativeAddSketchAdvanced extends AdvancedCountMinSketchAlg {

    ConservativeAddSketchAdvanced() {
        super();
    }

    public ConservativeAddSketchAdvanced(int depth, int width, int seed) {
        super(depth, width, seed);
    }

    public ConservativeAddSketchAdvanced(double epsOfTotalCount, double confidence, int seed) {
        super(epsOfTotalCount, confidence, seed);
    }

    ConservativeAddSketchAdvanced(int depth, int width, long size, long[] hashA, long[][] table) {
        super(depth, width, size, hashA, table);
    }

    @Override
    public void add(long item, long count) {
        if (count < 0) {
            throw new IllegalArgumentException("Negative increments not implemented");
        }
        int[] buckets = new int[depth];
        for (int i = 0; i < depth; ++i) {
            buckets[i] = hash(item, i);
        }
        long min = table[0][buckets[0]];
        for (int i = 1; i < depth; ++i) {
            min = Math.min(min, table[i][buckets[i]]);
        }
        for (int i = 0; i < depth; ++i) {
            long newVal = Math.max(table[i][buckets[i]], min + count);
            table[i][buckets[i]] = newVal;
        }
        size += count;
    }

    @Override
    public void add(String item, long count) {
        if (count < 0) {

            throw new IllegalArgumentException("Negative increments not implemented");
        }
        int[] buckets = Filter.getHashBuckets(item, depth, width);
        long min = table[0][buckets[0]];
        for (int i = 1; i < depth; ++i) {
            min = Math.min(min, table[i][buckets[i]]);
        }
        for (int i = 0; i < depth; ++i) {
            long newVal = Math.max(table[i][buckets[i]], min + count);
            table[i][buckets[i]] = newVal;
        }
        size += count;
    }
}