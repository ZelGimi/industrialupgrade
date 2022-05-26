package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.container.ContainerSunnariumMaker;
import com.denfop.gui.GUISunnariumMaker;
import com.denfop.invslot.InvSlotProcessableSunnarium;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.init.Localization;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;
import java.util.Set;

public class TileSunnariumMaker extends TileEntityBaseSunnariumMaker {

    public TileSunnariumMaker() {
        super(1, 300, 1);
        this.inputSlotA = new InvSlotProcessableSunnarium(this, "inputA", 0, 4);
    }

    public static void init() {
        addSunnariumMaker(new ItemStack(IUItem.sunnarium, 1, 4), new ItemStack(Items.GLOWSTONE_DUST), new ItemStack(Items.QUARTZ),
                new ItemStack(IUItem.iuingot, 1, 3), new ItemStack(IUItem.sunnarium, 1, 3)
        );
    }

    public static void addSunnariumMaker(
            ItemStack container,
            ItemStack container1,
            ItemStack container2,
            ItemStack container3,
            ItemStack output
    ) {
        final IRecipeInputFactory input = ic2.api.recipe.Recipes.inputFactory;
        Recipes.sunnurium.addRecipe(input.forStack(container, 4), input.forStack(container1),
                input.forStack(container2), input.forStack(container3), output
        );

    }

    @Override
    protected ItemStack getPickBlock(final EntityPlayer player, final RayTraceResult target) {
        return new ItemStack(IUItem.sunnariumpanelmaker);
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

    public String getInventoryName() {

        return Localization.translate("blockSunnariumMaker.name");
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GUISunnariumMaker(new ContainerSunnariumMaker(entityPlayer, this));
    }

    public String getStartSoundFile() {
        return "Machines/MaceratorOp.ogg";
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
