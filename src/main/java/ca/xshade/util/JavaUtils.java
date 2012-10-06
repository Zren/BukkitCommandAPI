/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.xshade.util;

import org.apache.commons.lang.ArrayUtils;

/**
 *
 * @author Admin
 */
public class JavaUtils {

    public static <T> T[] subArray(T[] arr, int startIndexInclusive, int endIndexExclusive) {
        return (T[]) ArrayUtils.subarray(arr, startIndexInclusive, endIndexExclusive);
    }

    public static <T> T[] subArray(T[] arr, int startIndexInclusive) {
        return (T[]) ArrayUtils.subarray(arr, startIndexInclusive, arr.length);
    }
}
