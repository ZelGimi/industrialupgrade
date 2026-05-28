package com.denfop.api.storage.cell;

public record CellInfo(int capacity, TypeCell typeCell) {
    public static final CellInfo ITEM_1000 = new CellInfo(1_000, TypeCell.ITEM);
    public static final CellInfo ITEM_4000 = new CellInfo(4_000, TypeCell.ITEM);
    public static final CellInfo ITEM_16000 = new CellInfo(16_000, TypeCell.ITEM);
    public static final CellInfo ITEM_64000 = new CellInfo(64_000, TypeCell.ITEM);
    public static final CellInfo ITEM_256000 = new CellInfo(256_000, TypeCell.ITEM);
    public static final CellInfo ITEM_1024000 = new CellInfo(1_024_000, TypeCell.ITEM);
    public static final CellInfo ITEM_4096000 = new CellInfo(4096000, TypeCell.ITEM);
    public static final CellInfo ITEM_16384000 = new CellInfo(16384000, TypeCell.ITEM);
    public static final CellInfo ITEM_65536000 = new CellInfo(65536000, TypeCell.ITEM);
    public static final CellInfo ITEM_262144000 = new CellInfo(262144000, TypeCell.ITEM);
    public static final CellInfo ITEM_1048576000 = new CellInfo(1048576000, TypeCell.ITEM);
    public static final CellInfo ITEM_2097152000 = new CellInfo(2097152000, TypeCell.ITEM);


    public static final CellInfo FLUID_1000 = new CellInfo(1_000_000, TypeCell.FLUID);
    public static final CellInfo FLUID_4000 = new CellInfo(4_000_000, TypeCell.FLUID);
    public static final CellInfo FLUID_16000 = new CellInfo(16_000_000, TypeCell.FLUID);
    public static final CellInfo FLUID_64000 = new CellInfo(64_000_000, TypeCell.FLUID);
    public static final CellInfo FLUID_256000 = new CellInfo(256_000_000, TypeCell.FLUID);
    public static final CellInfo FLUID_1024000 = new CellInfo(1_024_000_000, TypeCell.FLUID);
}
