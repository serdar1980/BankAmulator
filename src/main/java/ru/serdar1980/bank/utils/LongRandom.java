package ru.serdar1980.bank.utils;

import java.util.Random;

public class LongRandom extends Random {

    public long nextLong(long n) {
        if (n <= 0)
            throw new IllegalArgumentException("n must be positive");    

        if ((n & -n) == n)  // i.e., n is a power of 2
            return nextLong() & (n - 1); // only take the bottom bits

        long bits, val;
        do {
            bits = nextLong() & 0x7FFFFFFFL; // make nextLong non-negative
            val = bits % n;
        } while (bits - val + (n-1) < 0);
        return val;

    }
}