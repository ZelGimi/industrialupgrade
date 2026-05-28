package com.denfop.world.vein;

public enum TypeVein {
    SMALL(5, 10, 2, 7, 7),
    MEDIUM(10, 14, 3, 5, 10),
    BIG(15, 21, 4, 3, 13);
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
