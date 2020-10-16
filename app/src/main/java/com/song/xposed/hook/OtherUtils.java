package com.song.xposed.hook;

import java.lang.reflect.Array;

public class OtherUtils {

    /* renamed from: ʻ  reason: contains not printable characters */
    public static final byte[] f4494 = new byte[0];

    /* renamed from: ʽ  reason: contains not printable characters */
    public static final Character[] f4495 = new Character[0];

    /* renamed from: ʾ  reason: contains not printable characters */
    public static final float[] f4496 = new float[0];

    /* renamed from: ʿ  reason: contains not printable characters */
    public static final Float[] f4497 = new Float[0];

    /* renamed from: ˆ  reason: contains not printable characters */
    public static final Byte[] f4498 = new Byte[0];

    /* renamed from: ˈ  reason: contains not printable characters */
    public static final boolean[] f4499 = new boolean[0];

    /* renamed from: ˉ  reason: contains not printable characters */
    public static final Object[] f4500 = new Object[0];

    /* renamed from: ˋ  reason: contains not printable characters */
    public static final int[] f4501 = new int[0];

    /* renamed from: ˎ  reason: contains not printable characters */
    public static final double[] f4502 = new double[0];

    /* renamed from: ˏ  reason: contains not printable characters */
    public static final Class<?>[] f4503 = new Class[0];

    /* renamed from: ˑ  reason: contains not printable characters */
    public static final String[] f4504 = new String[0];

    /* renamed from: י  reason: contains not printable characters */
    public static final Long[] f4505 = new Long[0];

    /* renamed from: ـ  reason: contains not printable characters */
    public static final char[] f4506 = new char[0];

    /* renamed from: ᐧ  reason: contains not printable characters */
    public static final Integer[] f4507 = new Integer[0];

    /* renamed from: ᵎ  reason: contains not printable characters */
    public static final Short[] f4508 = new Short[0];

    /* renamed from: ᵔ  reason: contains not printable characters */
    public static final Boolean[] f4509 = new Boolean[0];

    /* renamed from: ᵢ  reason: contains not printable characters */
    public static final long[] f4510 = new long[0];

    /* renamed from: ⁱ  reason: contains not printable characters */
    public static final short[] f4511 = new short[0];

    /* renamed from: ﹶ  reason: contains not printable characters */
    public static final Double[] f4512 = new Double[0];

    /* renamed from: ˉ  reason: contains not printable characters */
    public static int m6439(Object obj) {
        if (obj == null) {
            return 0;
        }
        return Array.getLength(obj);
    }

    public static boolean isEmpty(char[] cArr) {
        return m6439(cArr) == 0;
    }

    /* renamed from: ˉ  reason: contains not printable characters */
    public static <T> T[] m6441(T[] tArr) {
        if (tArr == null) {
            return null;
        }
        return (T[]) tArr.clone();
    }
}