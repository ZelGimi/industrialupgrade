package com.denfop.tiles.mechanism;

import com.denfop.api.Recipes;
import com.denfop.container.ContainerGenStone;
import com.denfop.gui.GUIGenStone;
import com.denfop.invslot.InvSlotProcessableStone;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.init.Localization;
import ic2.core.item.type.CellType;
import ic2.core.ref.ItemName;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;
import java.util.Set;

public class TileEntityGenerationStone extends TileEntityBaseGenStone {


    public TileEntityGenerationStone() {
        super(1, 100, 12);
        this.inputSlotA = new InvSlotProcessableStone(this, "inputA", 2);
    }

    public static void init() {
        final IRecipeInputFactory input = ic2.api.recipe.Recipes.inputFactory;
        addGen(input.forStack(new ItemStack(Items.LAVA_BUCKET), 1), input.forStack(
                new ItemStack(Items.WATER_BUCKET),
                1
        ), new ItemStack(Blocks.COBBLESTONE, 8));
        addGen(
                input.forStack(ItemName.cell.getItemStack(CellType.lava)),
                input.forStack(ItemName.cell.getItemStack(CellType.water)),
                new ItemStack(Blocks.COBBLESTONE, 8)
        );

    }

    public static void addGen(IRecipeInput container, IRecipeInput fill, ItemStack output) {
        Recipes.GenStone.addRecipe(container, fill, output);
        Recipes.GenStone.addRecipe(fill, container, output);
    }

    public String getInventoryName() {
        return Localization.translate("iu.genstone");
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GUIGenStone(new ContainerGenStone(entityPlayer, this));
    }

    public String getStartSoundFile() {
        return "Machines/gen_cobblectone.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


    @Override
    public double getEnergy() {
        return 0;
    }

    @Override
    public boolean useEnergy(final double v) {
        return false;
    }

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.Processing,
                UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage,
                UpgradableProperty.ItemConsuming,
                UpgradableProperty.ItemProducing
        );
    }

    @Override
    public void onNetworkEvent(final int i) {

    }

}
