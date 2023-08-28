package com.denfop.tiles.mechanism.dual;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine1;
import com.denfop.componets.ComponentProcess;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentUpgradeSlots;
import com.denfop.container.ContainerDoubleElectricMachine;
import com.denfop.gui.GuiSynthesis;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.EnumDoubleElectricMachine;
import com.denfop.tiles.base.TileDoubleElectricMachine;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class TileSynthesis extends TileDoubleElectricMachine implements IHasRecipe {

    public TileSynthesis() {
        super(1, 300, 1, EnumDoubleElectricMachine.SYNTHESIS, false);
        Recipes.recipes.addInitRecipes(this);
        this.componentUpgrade = this.addComponent(new ComponentUpgradeSlots(this, upgradeSlot) {
            @Override
            public void onLoaded() {
                super.onLoaded();
                this.componentProcess = ((TileSynthesis) this.getParent()).componentProcess;
            }
        });
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) 300
        ));
        this.componentProcess = this.addComponent(new ComponentProcess(this, 300, 1) {
            public void operateOnce(List<ItemStack> processResult) {
                this.invSlotRecipes.consume();
                NBTTagCompound nbt = this.updateTick.getRecipeOutput().getRecipe().output.metadata;
                int procent = nbt.getInteger("percent");
                Random rand = this.getParent().getWorld().rand;
                if ((rand.nextInt(100) + 1) > (100 - procent)) {
                    this.outputSlot.add(this.updateTick.getRecipeOutput().getRecipe().output.items.get(0));

                }

            }
        });
        this.componentProcess.setHasAudio(true);
        this.componentProcess.setSlotOutput(outputSlot);
        this.componentProcess.setInvSlotRecipes(this.inputSlotA);

    }

    public static void addsynthesis(ItemStack container, ItemStack fill, int number, ItemStack output) {
        NBTTagCompound nbt = ModUtils.nbt();
        nbt.setInteger("percent", number);
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe("synthesis", new BaseMachineRecipe(
                new Input(
                        input.getInput(container),
                        input.getInput(fill)
                ),
                new RecipeOutput(nbt, output)
        ));
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine1.synthesis;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine;
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.synthesys.getSoundEvent();
    }

    public void init() {
        addsynthesis(
                new ItemStack(IUItem.radiationresources, 1, 2),
                new ItemStack(IUItem.cell_all, 1, 2),
                32,
                new ItemStack(IUItem.radiationresources, 1, 3)
        );
        addsynthesis(
                new ItemStack(IUItem.radiationresources, 1, 3),
                new ItemStack(IUItem.cell_all, 1, 2),
                27,
                new ItemStack(IUItem.radiationresources, 1, 6)
        );
        addsynthesis(
                new ItemStack(IUItem.radiationresources, 1, 6),
                new ItemStack(IUItem.cell_all, 1, 2),
                22,
                new ItemStack(IUItem.radiationresources, 1, 7)
        );
        addsynthesis(
                new ItemStack(IUItem.radiationresources, 1, 7),
                new ItemStack(IUItem.cell_all, 1, 2),
                19,
                new ItemStack(IUItem.radiationresources, 1, 5)
        );

        addsynthesis(IUItem.uraniumBlock, new ItemStack(IUItem.toriy), 22, new ItemStack(IUItem.radiationresources, 1, 8));
        addsynthesis(new ItemStack(IUItem.radiationresources, 1, 1), new ItemStack(IUItem.toriy), 20, IUItem.Plutonium);

    }


    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiSynthesis(new ContainerDoubleElectricMachine(entityPlayer, this, type));
    }

    public String getStartSoundFile() {
        return "Machines/synthesys.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    public float getWrenchDropRate() {
        return 0.85F;
    }


}
