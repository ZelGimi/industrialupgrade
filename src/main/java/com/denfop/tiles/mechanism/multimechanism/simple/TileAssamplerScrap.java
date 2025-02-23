package com.denfop.tiles.mechanism.multimechanism.simple;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;
import com.denfop.utils.ModUtils;
import net.minecraft.item.ItemStack;

public class TileAssamplerScrap extends TileMultiMachine {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public TileAssamplerScrap() {
        super(
                EnumMultiMachine.AssamplerScrap.usagePerTick,
                EnumMultiMachine.AssamplerScrap.lenghtOperation
        );
        Recipes.recipes.addInitRecipes(this);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.15));
    }

    public static void addrecipe(ItemStack input, ItemStack output) {
        final IInputHandler input1 = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "scrap",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input)
                        ),
                        new RecipeOutput(null, output)
                )
        );
    }

    public IMultiTileBlock getTeBlock() {
        return BlockMoreMachine3.assamplerscrap;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base3;
    }

    public void init() {
        addrecipe(
                ModUtils.setSize(IUItem.scrap, 9),
                IUItem.scrapBox
        );
        addrecipe(
                ModUtils.setSize(IUItem.scrapBox, 9),
                new ItemStack(IUItem.doublescrapBox, 1)
        );
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.AssamplerScrap;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockAssamplerScrap.name");
    }

    public String getStartSoundFile() {
        return "Machines/AssamplerScrap.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
