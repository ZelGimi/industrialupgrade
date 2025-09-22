package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockCombinerSolid;
import com.denfop.componets.Energy;
import com.denfop.container.ContainerCombinerSolidMatter;
import com.denfop.gui.GuiCombinerSolidMatter;
import com.denfop.invslot.InventorySolidMatter;
import com.denfop.invslot.InventoryUpgrade;
import com.denfop.tiles.solidmatter.EnumSolidMatter;
import net.minecraft.client.gui.GuiScreen;
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

public class TileEntityCombinerSolidMatter extends TileEntityInventory implements
        IUpgradableBlock {

    private static final List<AxisAlignedBB> aabbs = Collections.singletonList(new AxisAlignedBB(
            -0.5625,
            0.0D,
            -0.5625,
            1.5625,
            1.5D,
            1.5625
    ));
    public final InventorySolidMatter inputSlot;
    public final InventoryUpgrade upgradeSlot;
    public final InventoryOutput outputSlot;
    public final Energy energy;
    public EnumSolidMatter[] solid;
    public int[] solid_col;

    public TileEntityCombinerSolidMatter() {
        this.inputSlot = new InventorySolidMatter(this);

        this.outputSlot = new InventoryOutput(this, 9);
        this.upgradeSlot = new InventoryUpgrade(this, 4);

        this.energy = this.addComponent(Energy.asBasicSink(this, 0, 14));
        this.solid = new EnumSolidMatter[9];
        this.solid_col = new int[9];
    }

    public IMultiTileBlock getTeBlock() {
        return BlockCombinerSolid.combiner_solid_matter;
    }

    public BlockTileEntity getBlock() {
        return IUItem.combinersolidmatter;
    }


    public void addInformation(ItemStack stack, List<String> tooltip) {
        if (this.getComp(Energy.class) != null) {
            Energy energy = this.getComp(Energy.class);
            if (!energy.getSourceDirs().isEmpty()) {
                tooltip.add(Localization.translate("iu.item.tooltip.PowerTier", energy.getSourceTier()));
            } else if (!energy.getSinkDirs().isEmpty()) {
                tooltip.add(Localization.translate("iu.item.tooltip.PowerTier", energy.getSinkTier()));
            }
        }
    }

    public List<AxisAlignedBB> getAabbs(boolean forCollision) {
        return aabbs;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    public boolean isNormalCube() {
        return false;
    }

    public boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    public boolean isSideSolid(EnumFacing side) {
        return false;
    }

    public boolean clientNeedsExtraModelInfo() {
        return true;
    }

    public boolean shouldRenderInPass(int pass) {
        return true;
    }

    @Override
    public ItemStack getPickBlock(final EntityPlayer player, final RayTraceResult target) {
        return new ItemStack(IUItem.combinersolidmatter);
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);

    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        return nbttagcompound;
    }


    public void onLoaded() {
        super.onLoaded();
        this.inputSlot.update();

    }


    public void updateEntityServer() {
        super.updateEntityServer();

        if (this.energy.getCapacity() > 0 && this.energy.getEnergy() == this.energy.getCapacity()) {
            boolean need = false;
            for (int i = 0; i < this.solid_col.length; i++) {
                if (this.solid_col[i] == 0) {
                    continue;
                }
                ItemStack stack = this.solid[i].stack.copy();
                stack.setCount(this.solid_col[i]);
                if (this.outputSlot.add(stack)) {
                    need = true;
                }
            }
            if (need) {
                this.energy.useEnergy(this.energy.getEnergy());
            }
        }
        this.upgradeSlot.tickNoMark();


    }

    public void onUnloaded() {
        super.onUnloaded();


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


    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.Transformer, UpgradableProperty.ItemExtract,
                UpgradableProperty.ItemInput
        );
    }


}
