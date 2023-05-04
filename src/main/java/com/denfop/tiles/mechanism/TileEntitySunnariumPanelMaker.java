package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.*;
import com.denfop.api.sytem.EnergyType;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.container.ContainerDoubleElectricMachine;
import com.denfop.gui.GuiSunnariumPanelMaker;
import com.denfop.tiles.base.EnumDoubleElectricMachine;
import com.denfop.tiles.base.TileEntityDoubleElectricMachine;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.api.upgrade.UpgradableProperty;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntitySunnariumPanelMaker extends TileEntityDoubleElectricMachine implements IHasRecipe {

    public final ComponentBaseEnergy sunenergy;

    public TileEntitySunnariumPanelMaker() {
        super(1, 300, 1, EnumDoubleElectricMachine.SUNNARIUM_PANEL);
        this.sunenergy = this.addComponent(ComponentBaseEnergy
                .asBasicSink(EnergyType.SOLARIUM, this, 10000, 1));
        Recipes.recipes.addInitRecipes(this);
    }

    public static void addsunnuriumpanel(ItemStack container, ItemStack fill, ItemStack output) {
        int id = OreDictionary.getOreIDs(fill)[0];
        String name = OreDictionary.getOreName(id);
        final IRecipeInputFactory input = ic2.api.recipe.Recipes.inputFactory;
        if (name == null && fill.getItem() != IUItem.neutroniumingot) {
            Recipes.recipes.addRecipe(
                    "sunnuriumpanel",
                    new BaseMachineRecipe(
                            new Input(
                                    input.forStack(container),
                                    input.forStack(fill)
                            ),
                            new RecipeOutput(null, output)
                    )
            );
        } else {
            Recipes.recipes.addRecipe(
                    "sunnuriumpanel",
                    new BaseMachineRecipe(
                            new Input(
                                    input.forStack(container),
                                    input.forOreDict(name)
                            ),
                            new RecipeOutput(null, output)
                    )
            );
        }
    }

    public void init() {

        addsunnuriumpanel(
                new ItemStack(IUItem.sunnarium, 1, 2),
                new ItemStack(IUItem.plate, 1, 9),
                new ItemStack(IUItem.sunnariumpanel, 1, 0)
        );
        addsunnuriumpanel(
                new ItemStack(IUItem.sunnariumpanel, 1, 0),
                new ItemStack(IUItem.plate, 1, 0),
                new ItemStack(IUItem.sunnariumpanel, 1, 1)
        );
        addsunnuriumpanel(
                new ItemStack(IUItem.sunnariumpanel, 1, 1),
                new ItemStack(IUItem.plate, 1, 11),
                new ItemStack(IUItem.sunnariumpanel, 1, 2)
        );
        addsunnuriumpanel(
                new ItemStack(IUItem.sunnariumpanel, 1, 2),
                new ItemStack(IUItem.plate, 1, 13),
                new ItemStack(IUItem.sunnariumpanel, 1, 3)
        );
        addsunnuriumpanel(
                new ItemStack(IUItem.sunnariumpanel, 1, 3),
                new ItemStack(IUItem.plate, 1, 7),
                new ItemStack(IUItem.sunnariumpanel, 1, 4)
        );
        addsunnuriumpanel(
                new ItemStack(IUItem.sunnariumpanel, 1, 4),
                new ItemStack(IUItem.plate, 1, 15),
                new ItemStack(IUItem.sunnariumpanel, 1, 5)
        );
        addsunnuriumpanel(
                new ItemStack(IUItem.sunnariumpanel, 1, 5),
                new ItemStack(IUItem.plate, 1, 16),
                new ItemStack(IUItem.sunnariumpanel, 1, 6)
        );
        addsunnuriumpanel(
                new ItemStack(IUItem.sunnariumpanel, 1, 6),
                new ItemStack(IUItem.plate, 1, 6),
                new ItemStack(IUItem.sunnariumpanel, 1, 7)
        );
        addsunnuriumpanel(
                new ItemStack(IUItem.sunnariumpanel, 1, 7),
                new ItemStack(IUItem.plate, 1, 8),
                new ItemStack(IUItem.sunnariumpanel, 1, 8)
        );
        addsunnuriumpanel(
                new ItemStack(IUItem.sunnariumpanel, 1, 8),
                new ItemStack(IUItem.plate, 1, 14),
                new ItemStack(IUItem.sunnariumpanel, 1, 9)
        );
        addsunnuriumpanel(
                new ItemStack(IUItem.sunnariumpanel, 1, 9),
                new ItemStack(IUItem.plate, 1, 2),
                new ItemStack(IUItem.sunnariumpanel, 1, 10)
        );
        addsunnuriumpanel(
                new ItemStack(IUItem.sunnarium, 1, 0),
                new ItemStack(IUItem.plate, 1, 1),
                new ItemStack(IUItem.sunnarium, 1, 1)
        );
        addsunnuriumpanel(
                new ItemStack(IUItem.sunnariumpanel, 1, 10),
                new ItemStack(IUItem.alloysplate, 1, 7),
                new ItemStack(IUItem.sunnariumpanel, 1, 11)
        );
        addsunnuriumpanel(
                new ItemStack(IUItem.sunnariumpanel, 1, 11),
                new ItemStack(IUItem.plate, 1, 5),
                new ItemStack(IUItem.sunnariumpanel, 1, 12)
        );

    }

    protected void updateEntityServer() {

        MachineRecipe output = this.output;
        if (output != null && this.energy.getEnergy() >= this.energyConsume && this.sunenergy.getEnergy() >= 5) {
            if (!this.getActive()) {
                setActive(true);
            }
            if (this.progress == 0) {
                initiate(0);
            }
            this.progress = (short) (this.progress + 1);
            this.energy.useEnergy(this.energyConsume);
            this.sunenergy.useEnergy(5);
            double k = this.progress;

            this.guiProgress = (k / this.operationLength);
            if (this.progress >= this.operationLength) {
                this.guiProgress = 0;
                operate(output);
                this.progress = 0;
                initiate(2);
            }
        } else {
            if (this.progress != 0 && getActive()) {
                initiate(1);
            }
            if (output == null) {
                this.progress = 0;
            }
            if (this.getActive()) {
                setActive(false);
            }
        }
        if (this.upgradeSlot.tickNoMark()) {
            setOverclockRates();
        }

    }

    @Override
    protected ItemStack getPickBlock(final EntityPlayer player, final RayTraceResult target) {
        return new ItemStack(IUItem.sunnariummaker);
    }

    @SideOnly(Side.CLIENT)
    protected boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    protected boolean isNormalCube() {
        return false;
    }

    protected boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    protected boolean isSideSolid(EnumFacing side) {
        return false;
    }

    protected boolean clientNeedsExtraModelInfo() {
        return true;
    }

    public boolean shouldRenderInPass(int pass) {
        return true;
    }


    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiSunnariumPanelMaker(new ContainerDoubleElectricMachine(entityPlayer, this, type));

    }

    @Override
    public void operateOnce(MachineRecipe output, List<ItemStack> processResult) {
        this.inputSlotA.consume();
        this.outputSlot.add(processResult);
    }

    public String getStartSoundFile() {
        return null;
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    public float getWrenchDropRate() {
        return 0.85F;
    }

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.Processing, UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage, UpgradableProperty.ItemConsuming, UpgradableProperty.ItemProducing
        );
    }

}
