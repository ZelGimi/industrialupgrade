package com.denfop.tiles.base;

public enum EnumDoubleElectricMachine {
    ALLOY_SMELTER("alloysmelter", 38, 17, 74, 17, 56, 53, true, 116, 35),
    SUNNARIUM_PANEL("sunnuriumpanel", 14, 34, 36, 34, 0, 0, false, 106, 34),
    ENRICH("enrichment", 14, 34, 36, 34, 0, 0, false, 106, 34),
    SYNTHESIS("synthesis", 14, 34, 64, 34, 0, 0, false, 110, 34),
    PAINTING("painter", 14, 34, 36, 34, 0, 0, false, 106, 34),
    UPGRADE("upgradeblock", 14, 34, 64, 34, 0, 0, false, 110, 34),
    ;

    public final int inputx;
    public final int inputy;
    public final int inputx1;
    public final int inputy1;
    public final int dischangeX;
    public final int dischangeY;
    public final int outputx;
    public final int outputy;
    public final boolean register;
    public final String recipe_name;

    EnumDoubleElectricMachine(
            String name,
            int inputx,
            int inputy,
            int inputx1,
            int inputy1,
            int dischangeX,
            int dischangeY,
            boolean register,
            int outputx,
            int outputy
    ) {
        this.recipe_name = name;
        this.inputx = inputx;
        this.inputy = inputy;
        this.inputx1 = inputx1;
        this.inputy1 = inputy1;
        this.dischangeX = dischangeX;
        this.dischangeY = dischangeY;
        this.register = register;
        this.outputx = outputx;
        this.outputy = outputy;
    }
}
