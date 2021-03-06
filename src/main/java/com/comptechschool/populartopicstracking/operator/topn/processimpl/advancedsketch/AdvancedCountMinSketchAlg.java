package com.comptechschool.populartopicstracking.operator.topn.processimpl.advancedsketch;

import org.apache.flink.calcite.shaded.com.google.common.base.Preconditions;

import java.io.*;
import java.util.Arrays;
import java.util.Random;


public class AdvancedCountMinSketchAlg implements IFrequency, Serializable {

    public static final long PRIME_MODULUS = (1L << 31) - 1;
    private static final long serialVersionUID = -5084982213094657923L;

    int depth;
    int width;
    long[][] table;
    long[] hashA;
    long size;
    double eps;
    double confidence;

    AdvancedCountMinSketchAlg() {
    }

    public AdvancedCountMinSketchAlg(int depth, int width, int seed) {
        this.depth = depth;
        this.width = width;
        this.eps = 2.0 / width;
        this.confidence = 1 - 1 / Math.pow(2, depth);
        initTablesWith(depth, width, seed);
    }

    public AdvancedCountMinSketchAlg(double epsOfTotalCount, double confidence, int seed) {
        // 2/w = eps ; w = 2/eps
        // 1/2^depth <= 1-confidence ; depth >= -log2 (1-confidence)
        this.eps = epsOfTotalCount;
        this.confidence = confidence;
        this.width = (int) Math.ceil(2 / epsOfTotalCount);
        this.depth = (int) Math.ceil(-Math.log(1 - confidence) / Math.log(2));
        initTablesWith(depth, width, seed);
    }

    AdvancedCountMinSketchAlg(int depth, int width, long size, long[] hashA, long[][] table) {
        this.depth = depth;
        this.width = width;
        this.eps = 2.0 / width;
        this.confidence = 1 - 1 / Math.pow(2, depth);
        this.hashA = hashA;
        this.table = table;

        Preconditions.checkState(size >= 0, "The size cannot be smaller than ZER0: " + size);
        this.size = size;
    }

    @Override
    public String toString() {
        return "CountMinSketch{" +
                "eps=" + eps +
                ", confidence=" + confidence +
                ", depth=" + depth +
                ", width=" + width +
                ", size=" + size +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final AdvancedCountMinSketchAlg that = (AdvancedCountMinSketchAlg) o;

        if (depth != that.depth) {
            return false;
        }
        if (width != that.width) {
            return false;
        }

        if (Double.compare(that.eps, eps) != 0) {
            return false;
        }
        if (Double.compare(that.confidence, confidence) != 0) {
            return false;
        }

        if (size != that.size) {
            return false;
        }

        if (!Arrays.deepEquals(table, that.table)) {
            return false;
        }
        return Arrays.equals(hashA, that.hashA);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = depth;
        result = 31 * result + width;
        result = 31 * result + Arrays.deepHashCode(table);
        result = 31 * result + Arrays.hashCode(hashA);
        result = 31 * result + (int) (size ^ (size >>> 32));
        temp = Double.doubleToLongBits(eps);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(confidence);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    private void initTablesWith(int depth, int width, int seed) {
        this.table = new long[depth][width];
        this.hashA = new long[depth];
        Random r = new Random(seed);

        for (int i = 0; i < depth; ++i) {
            hashA[i] = r.nextInt(Integer.MAX_VALUE);
        }
    }

    public double getRelativeError() {
        return eps;
    }

    public double getConfidence() {
        return confidence;
    }

    int hash(long item, int i) {
        long hash = hashA[i] * item;

        hash += hash >> 32;
        hash &= PRIME_MODULUS;
        // Doing "%" after (int) conversion is ~2x faster than %'ing longs.
        return ((int) hash) % width;
    }

    private static void checkSizeAfterOperation(long previousSize, String operation, long newSize) {
        if (newSize < previousSize) {
            throw new IllegalStateException("Overflow error: the size after calling `" + operation +
                    "` is smaller than the previous size. " +
                    "Previous size: " + previousSize +
                    ", New size: " + newSize);
        }
    }

    private void checkSizeAfterAdd(String item, long count) {
        long previousSize = size;
        size += count;
        checkSizeAfterOperation(previousSize, "add(" + item + "," + count + ")", size);
    }

    @Override
    public void add(long item, long count) {
        if (count < 0) {

            throw new IllegalArgumentException("Negative increments not implemented");
        }
        for (int i = 0; i < depth; ++i) {
            table[i][hash(item, i)] += count;
        }

        checkSizeAfterAdd(String.valueOf(item), count);
    }

    @Override
    public void add(String item, long count) {
        if (count < 0) {

            throw new IllegalArgumentException("Negative increments not implemented");
        }
        int[] buckets = Filter.getHashBuckets(item, depth, width);
        for (int i = 0; i < depth; ++i) {
            table[i][buckets[i]] += count;
        }

        checkSizeAfterAdd(item, count);
    }

    @Override
    public long size() {
        return size;
    }


    @Override
    public long estimateCount(long item) {
        long res = Long.MAX_VALUE;
        for (int i = 0; i < depth; ++i) {
            res = Math.min(res, table[i][hash(item, i)]);
        }
        return res;
    }

    @Override
    public long estimateCount(String item) {
        long res = Long.MAX_VALUE;
        int[] buckets = Filter.getHashBuckets(item, depth, width);
        for (int i = 0; i < depth; ++i) {
            res = Math.min(res, table[i][buckets[i]]);
        }
        return res;
    }


    public static AdvancedCountMinSketchAlg merge(AdvancedCountMinSketchAlg... estimators) throws CMSMergeException {
        AdvancedCountMinSketchAlg merged = null;
        if (estimators != null && estimators.length > 0) {
            int depth = estimators[0].depth;
            int width = estimators[0].width;
            long[] hashA = Arrays.copyOf(estimators[0].hashA, estimators[0].hashA.length);

            long[][] table = new long[depth][width];
            long size = 0;

            for (AdvancedCountMinSketchAlg estimator : estimators) {
                if (estimator.depth != depth) {
                    throw new CMSMergeException("Cannot merge estimators of different depth");
                }
                if (estimator.width != width) {
                    throw new CMSMergeException("Cannot merge estimators of different width");
                }
                if (!Arrays.equals(estimator.hashA, hashA)) {
                    throw new CMSMergeException("Cannot merge estimators of different seed");
                }

                for (int i = 0; i < table.length; i++) {
                    for (int j = 0; j < table[i].length; j++) {
                        table[i][j] += estimator.table[i][j];
                    }
                }

                long previousSize = size;
                size += estimator.size;
                checkSizeAfterOperation(previousSize, "merge(" + estimator + ")", size);
            }

            merged = new AdvancedCountMinSketchAlg(depth, width, size, hashA, table);
        }

        return merged;
    }

    public static byte[] serialize(AdvancedCountMinSketchAlg sketch) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream s = new DataOutputStream(bos);
        try {
            s.writeLong(sketch.size);
            s.writeInt(sketch.depth);
            s.writeInt(sketch.width);
            for (int i = 0; i < sketch.depth; ++i) {
                s.writeLong(sketch.hashA[i]);
                for (int j = 0; j < sketch.width; ++j) {
                    s.writeLong(sketch.table[i][j]);
                }
            }
            s.close();
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static AdvancedCountMinSketchAlg deserialize(byte[] data) {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        DataInputStream s = new DataInputStream(bis);
        try {
            AdvancedCountMinSketchAlg sketch = new AdvancedCountMinSketchAlg();
            sketch.size = s.readLong();
            sketch.depth = s.readInt();
            sketch.width = s.readInt();
            sketch.eps = 2.0 / sketch.width;
            sketch.confidence = 1 - 1 / Math.pow(2, sketch.depth);
            sketch.hashA = new long[sketch.depth];
            sketch.table = new long[sketch.depth][sketch.width];
            for (int i = 0; i < sketch.depth; ++i) {
                sketch.hashA[i] = s.readLong();
                for (int j = 0; j < sketch.width; ++j) {
                    sketch.table[i][j] = s.readLong();
                }
            }
            return sketch;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("serial")
    protected static class CMSMergeException extends FrequencyMergeException {

        public CMSMergeException(String message) {
            super(message);
        }
    }
}