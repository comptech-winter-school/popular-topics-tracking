package com.comptechschool.populartopicstracking.operator.topn.processimpl.advancedsketch;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;


public abstract class Filter {

    int hashCount;

    public int getHashCount() {
        return hashCount;
    }

    public int[] getHashBuckets(String key) {
        return Filter.getHashBuckets(key, hashCount, buckets());
    }

    public int[] getHashBuckets(byte[] key) {
        return Filter.getHashBuckets(key, hashCount, buckets());
    }


    abstract int buckets();

    public abstract void add(String key);

    public abstract boolean isPresent(String key);

    // for testing
    abstract int emptyBuckets();

    @SuppressWarnings("unchecked")
    ICompactSerializer<Filter> getSerializer() {
        Method method = null;
        try {
            method = getClass().getMethod("serializer");
            return (ICompactSerializer<Filter>) method.invoke(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static int[] getHashBuckets(String key, int hashCount, int max) {
        byte[] b;
        b = key.getBytes(StandardCharsets.UTF_16);
        return getHashBuckets(b, hashCount, max);
    }

    static int[] getHashBuckets(byte[] b, int hashCount, int max) {
        int[] result = new int[hashCount];
        int hash1 = AdvancedHash.hash(b, b.length, 0);
        int hash2 = AdvancedHash.hash(b, b.length, hash1);
        for (int i = 0; i < hashCount; i++) {
            result[i] = Math.abs((hash1 + i * hash2) % max);
        }
        return result;
    }
}