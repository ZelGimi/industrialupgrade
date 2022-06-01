package com.denfop.tiles.base;

@SuppressWarnings("ALL")
public enum EnumTripleElectricMachine {
    ADV_ALLOY_SMELTER("advalloysmelter", 74, 17, 56, 17, 38, 17, 56, 53, true, 116, 35);

    public final int inputx;
    public final int inputy;
    public final int inputx1;
    public final int inputy1;
    public final int dischangeX;
    public final int dischangeY;
    public final int outputx;
    public final int outputy;
    public final boolean register;
    public final int inputx2;
    public final int inputy2;
    public final String recipe_name;

    EnumTripleElectricMachine(
            String recipe_name,
            int inputx,
            int inputy,
            int inputx1,
            int inputy1,
            int inputx2,
            int inputy2,
            int dischangeX,
            int dischangeY,
            boolean register,
            int outputx,
            int outputy
    ) {
        this.recipe_name = recipe_name;
        this.inputx = inputx;
        this.inputy = inputy;
        this.inputx1 = inputx1;
        this.inputy1 = inputy1;
        this.inputx2 = inputx2;
        this.inputy2 = inputy2;
        this.dischangeX = dischangeX;
        this.dischangeY = dischangeY;
        this.register = register;
        this.outputx = outputx;
        this.outputy = outputy;
    }
}
