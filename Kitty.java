package com.company;

public class Kitty {

    private int total;

    public void setTotal(int m) {
        total = m;
    }

    public void update (int m) {
        total += m;
    }

    public int payout() {
        int temp = total;
        total = 0;
        return temp;
    }

    public int getTotal() {
        return total;
    }

}

