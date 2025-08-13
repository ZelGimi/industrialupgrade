package com.denfop.world.vein;

public enum TypeVein {
    SMALL(5, 15, 2, 8, 5),
    MEDIUM(10, 20, 3, 5, 7),
    BIG(15, 35, 4, 3, 9);
    private final int max;
    private final int max_length;
    private final int level;
    private final int need;
    private final int minNeed;

    TypeVein(int max, int max_length, int level, int need, int minNeed) {
        this.max = max;
        this.max_length = max_length;
        this.level = level;
        this.need = need;
        this.minNeed = minNeed;
    }

    public int getMinNeed() {
        return minNeed;
    }

    public int getNeed() {
        return need;
    }

    public int getLevel() {
        return level;
    }

    public int getMax_length() {
        return max_length;
    }

    public int getMax() {
        return max;
    }
}
