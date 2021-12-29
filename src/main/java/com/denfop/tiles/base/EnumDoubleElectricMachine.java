package com.denfop.tiles.base;

import com.denfop.api.IDoubleMachineRecipeManager;
import com.denfop.api.Recipes;

public enum EnumDoubleElectricMachine {
    ALLOY_SMELTER(38, 17, 74, 17, 56, 53, true, Recipes.Alloysmelter, 116, 35),
    SUNNARIUM_PANEL(14, 34, 36, 34, 0, 0, false, Recipes.sunnuriumpanel, 106, 34),
    ENRICH(14, 34, 36, 34, 0, 0, false, Recipes.enrichment, 106, 34),
    SYNTHESIS(14, 34, 64, 34, 0, 0, false, Recipes.synthesis, 110, 34),
    PAINTING(14, 34, 36, 34, 0, 0, false, Recipes.painting, 106, 34),
    UPGRADE(14, 34, 64, 34, 0, 0, false, Recipes.upgrade, 110, 34),
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
    public final IDoubleMachineRecipeManager recipe;

    EnumDoubleElectricMachine(
            int inputx,
            int inputy,
            int inputx1,
            int inputy1,
            int dischangeX,
            int dischangeY,
            boolean register,
            IDoubleMachineRecipeManager recipe,
            int outputx,
            int outputy
    ) {
        this.inputx = inputx;
        this.inputy = inputy;
        this.inputx1 = inputx1;
        this.inputy1 = inputy1;
        this.dischangeX = dischangeX;
        this.dischangeY = dischangeY;
        this.register = register;
        this.recipe = recipe;
        this.outputx = outputx;
        this.outputy = outputy;
    }
}
