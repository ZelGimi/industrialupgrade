package com.denfop.blockentity.base;

public enum EnumDoubleElectricMachine {
    ALLOY_SMELTER("alloysmelter", 38, 17, 74, 17, 56, 53, true, 116, 35, true),
    SUNNARIUM_PANEL("sunnuriumpanel", 29, 36, 51, 36, 0, 0, false, 106, 36),
    ENRICH("enrichment", 16, 36, 38, 36, 0, 0, false, 102, 36),
    SYNTHESIS("synthesis", 23, 34, 59, 34, 0, 0, false, 111, 34),
    PAINTING("painter", 29, 36, 51, 36, 0, 0, false, 106, 36),
    UPGRADE("upgradeblock", 19, 38, 64, 38, 0, 0, false, 113, 38),
    WELDING("welding", 38, 17, 74, 17, 56, 53, true, 116, 35, true),

    CANNING("cannerbottle", 67, 36, 37, 36, 8, 62, true, 116, 36),
    UPGRADE_ROVER("roverupgradeblock", 19, 38, 64, 38, 0, 0, false, 113, 38),

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
    public final boolean heat;

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
        this.heat = false;
    }

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
            int outputy,
            boolean heat
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
        this.heat = heat;
    }
}
