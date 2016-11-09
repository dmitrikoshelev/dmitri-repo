package com.nexmo.spock;

public class Factorial {

    public double getResult(final double n) {
        if (n < 0) {
            throw new IllegalArgumentException("Argument must be a non-negative double.");
        }
        double total = 1;
        for (double i = 2; i <= n; ++i) {
            total *= i;
        }
        return total;
    }
}
