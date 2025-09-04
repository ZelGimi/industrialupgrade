package com.denfop.blockentity.mechanism.multimechanism.simple;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.blockentity.base.BlockEntityMultiMachine;
import com.denfop.blockentity.base.EnumMultiMachine;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine3Entity;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.recipe.IInputHandler;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityAssamplerScrap extends BlockEntityMultiMachine {

    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public BlockEntityAssamplerScrap(BlockPos pos, BlockState state) {
        super(
                EnumMultiMachine.AssamplerScrap.usagePerTick,
                EnumMultiMachine.AssamplerScrap.lenghtOperation, BlockMoreMachine3Entity.assamplerscrap, pos, state
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

    public MultiBlockEntity getTeBlock() {
        return BlockMoreMachine3Entity.assamplerscrap;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base3.getBlock(getTeBlock().getId());
    }

    public void init() {
        addrecipe(
                ModUtils.setSize(IUItem.scrap, 9),
                IUItem.scrapBox
        );
        addrecipe(
                ModUtils.setSize(IUItem.scrapBox, 9),
                new ItemStack(IUItem.doublescrapBox.getItem(), 1)
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
