package com.comptechschool.populartopicstracking.topnoperator.sort;


import com.comptechschool.populartopicstracking.entity.InputEntity;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Sort algorithm
 */
public class EntityHeapSortUtils {

    public static InputEntity[] topN(InputEntity[] arr, int topSize, Comparator<InputEntity> comparator) {
        if (arr == null || arr.length == 0) {
            return null;
        }
        if (topSize >= arr.length) {
            Arrays.sort(arr, comparator);
            return arr;
        }
        int lastIndex = arr.length - 1;
        for (int i = lastIndex / 2 - 1; i >= 0; i--) {
            constructHeap(arr, i, lastIndex, comparator);
        }
        //Change the top elements of the heap to the end in turn
        while (lastIndex >= 1) {
            swap(arr, lastIndex, 0);
            lastIndex--;
            constructHeap(arr, 0, lastIndex, comparator);
            if (lastIndex == arr.length - topSize - 1) {
                break;
            }
        }
        //return arr;
        InputEntity[] res = new InputEntity[topSize];
        //System.arraycopy(arr, 1, res, 0, topSize);
        int idx = arr.length - 1;
        for (int i = 0; i < topSize; i++) {
            res[i] = arr[idx - i];
        }
        return res;
    }

    public static InputEntity[] sort(InputEntity[] arr, Comparator<InputEntity> comparator) {
        if (arr == null || arr.length == 0) {
            return null;
        }

        int lastIndex = arr.length - 1;
        for (int i = lastIndex / 2 - 1; i >= 0; i--) {
            constructHeap(arr, i, lastIndex, comparator);
        }

        //Change the top elements of the heap to the end in turn
        while (lastIndex >= 1) {
            swap(arr, lastIndex, 0);
            lastIndex--;
            constructHeap(arr, 0, lastIndex, comparator);
        }
        return arr;
    }

    private static void constructHeap(InputEntity[] arr, int i, int lastIndex, Comparator<InputEntity> comparator) {
        if (lastIndex <= 0) {
            return;
        }
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        if (right > lastIndex) {
            //It shows that there is only the left child node
            //Compare the size of the left child node and the parent node
            if (comparator.compare(arr[left], arr[i]) > 0) {
                swap(arr, left, i);
                //Compare the size of the left child node and the parent node
                if (left <= lastIndex / 2 - 1) {
                    constructHeap(arr, left, lastIndex, comparator);
                }
            }
        } else {
            //Both left and right child nodes have
            int sonMax = comparator.compare(arr[left], arr[right]) > 0 ? left : right;
            if (comparator.compare(arr[sonMax], arr[i]) > 0) {
                swap(arr, sonMax, i);
                //If the child node exchanged with the top node has a child node, recursively
                if (sonMax <= lastIndex / 2 - 1) {
                    constructHeap(arr, sonMax, lastIndex, comparator);
                }
            }
        }
    }

    private static void swap(InputEntity[] arr, int left, int top) {
        InputEntity temp = arr[top];
        arr[top] = arr[left];
        arr[left] = temp;
    }
}
