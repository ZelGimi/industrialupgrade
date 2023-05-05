package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.api.inv.IHasGui;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.componets.AdvEnergy;
import com.denfop.container.ContainerCombinerSolidMatter;
import com.denfop.gui.GuiCombinerSolidMatter;
import com.denfop.invslot.InvSlotSolidMatter;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.tiles.solidmatter.EnumSolidMatter;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.IC2;
import ic2.core.init.Localization;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileEntityCombinerSolidMatter extends TileEntityInventory implements IHasGui,
        IUpgradableBlock {

    private static final List<AxisAlignedBB> aabbs = Collections.singletonList(new AxisAlignedBB(
            -0.28125,
            0.0D,
            -0.28125,
            1.28125,
            1.5D,
            1.28125
    ));
    public final InvSlotSolidMatter inputSlot;
    public final InvSlotUpgrade upgradeSlot;
    public final InvSlotOutput outputSlot;
    public final AdvEnergy energy;
    public EnumSolidMatter[] solid;
    public int[] solid_col;

    public TileEntityCombinerSolidMatter() {
        this.inputSlot = new InvSlotSolidMatter(this);

        this.outputSlot = new InvSlotOutput(this, "output", 9);
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, "upgrade", 4);

        this.energy = this.addComponent(AdvEnergy.asBasicSink(this, 0, 14));
        this.solid = new EnumSolidMatter[9];
        this.solid_col = new int[9];
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, List<String> tooltip, ITooltipFlag advanced) {
        if (this.getComp(AdvEnergy.class) != null) {
            AdvEnergy energy = this.getComp(AdvEnergy.class);
            if (!energy.getSourceDirs().isEmpty()) {
                tooltip.add(Localization.translate("ic2.item.tooltip.PowerTier", energy.getSourceTier()));
            } else if (!energy.getSinkDirs().isEmpty()) {
                tooltip.add(Localization.translate("ic2.item.tooltip.PowerTier", energy.getSinkTier()));
            }
        }
    }

    protected List<AxisAlignedBB> getAabbs(boolean forCollision) {
        return aabbs;
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

    @Override
    protected ItemStack getPickBlock(final EntityPlayer player, final RayTraceResult target) {
        return new ItemStack(IUItem.combinersolidmatter);
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);

    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        return nbttagcompound;
    }


    protected void onLoaded() {
        super.onLoaded();
        this.inputSlot.update();

    }


    protected void updateEntityServer() {
        super.updateEntityServer();

        if (this.energy.getCapacity() > 0 && this.energy.getEnergy() == this.energy.getCapacity()) {

            for (int i = 0; i < this.solid_col.length; i++) {
                if (this.solid_col[i] == 0) {
                    continue;
                }
                ItemStack stack = this.solid[i].stack.copy();
                stack.setCount(this.solid_col[i]);
                this.outputSlot.add(stack);
            }
            this.energy.useEnergy(this.energy.getEnergy());
        }
        if (this.upgradeSlot.tickNoMark()) {
            setOverclockRates();
        }


    }

    protected void onUnloaded() {
        super.onUnloaded();


    }

    public void markDirty() {
        super.markDirty();
        if (IC2.platform.isSimulating()) {
            setOverclockRates();
        }
    }

    public void setOverclockRates() {

    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiCombinerSolidMatter(new ContainerCombinerSolidMatter(entityPlayer, this));
    }

    public ContainerCombinerSolidMatter getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerCombinerSolidMatter(entityPlayer, this);
    }


    public String getStartSoundFile() {
        return null;
    }

    public String getInterruptSoundFile() {
        return null;
    }


    public double getEnergy() {
        return this.energy.getEnergy();
    }

    public boolean useEnergy(double amount) {
        return this.energy.useEnergy(amount);
    }

    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.Transformer,
                UpgradableProperty.ItemProducing
        );
    }

    public void onGuiClosed(EntityPlayer player) {
    }


    public String getInventoryName() {
        return null;
    }


}
