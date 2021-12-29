package com.denfop.tiles.base;

import com.denfop.container.ContainerCombinerSolidMatter;
import com.denfop.gui.GUICombinerSolidMatter;
import com.denfop.invslot.InvSlotSolidMatter;
import com.denfop.items.ItemSolidMatter;
import com.denfop.tiles.solidmatter.EnumSolidMatter;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.ContainerBase;
import ic2.core.IHasGui;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.comp.Energy;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.block.invslot.InvSlotUpgrade;
import ic2.core.util.StackUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;
import java.util.Set;

public class TileEntityCombinerSolidMatter extends TileEntityInventory implements IHasGui,
        IUpgradableBlock {

    private final Energy energy;
    public final InvSlotSolidMatter inputSlot;
    public final InvSlotUpgrade upgradeSlot;
    public final InvSlotOutput outputSlot;

    protected boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    protected boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    protected boolean isNormalCube() {
        return false;
    }

    public TileEntityCombinerSolidMatter() {
        this.inputSlot = new InvSlotSolidMatter(this);

        this.outputSlot = new InvSlotOutput(this, "output", 9);
        this.upgradeSlot = new InvSlotUpgrade(this, "upgrade", 4);

        this.energy = this.addComponent(Energy.asBasicSink(this, this.inputSlot.getMaxEnergy(), 10));

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


    }

    protected void onUnloaded() {


    }


    public boolean onUpdateUpgrade() {
        for (int i = 0; i < this.upgradeSlot.size(); i++) {
            ItemStack stack = this.upgradeSlot.get(i);
            if (stack != null) {
                return true;
            }
        }
        for (int i = 0; i < this.inputSlot.size(); i++) {
            ItemStack stack = this.inputSlot.get(i);
            if (stack != null) {
                return true;
            }
        }
        return false;
    }

    protected void updateEntityServer() {
        super.updateEntityServer();
        boolean update = onUpdateUpgrade();

        EnumSolidMatter[] solid = new EnumSolidMatter[0];
        for (int i = 0; i < this.inputSlot.size(); i++) {

            if (!StackUtil.isEmpty(this.inputSlot.get(i))) {
                EnumSolidMatter[] solid1 = solid;
                solid = new EnumSolidMatter[solid.length + 1];
                solid[solid.length - 1] = ItemSolidMatter.getsolidmatter(this.inputSlot.get(i).getItemDamage());
                System.arraycopy(solid1, 0, solid, 0, solid1.length);
            }
        }
        if (this.energy.getEnergy() == this.energy.getCapacity()) {
            if (solid.length > 0) {
                for (EnumSolidMatter enumSolidMatter : solid) {

                    if (this.outputSlot.canAdd(enumSolidMatter.stack)) {
                        this.outputSlot.add(enumSolidMatter.stack);

                        this.energy.useEnergy(this.energy.getCapacity());
                    }
                }
            }
        }
        if (update) {
            markDirty();
        }


    }


    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GUICombinerSolidMatter(new ContainerCombinerSolidMatter(entityPlayer, this));
    }

    public ContainerBase<? extends TileEntityCombinerSolidMatter> getGuiContainer(EntityPlayer entityPlayer) {
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


    public int getMode() {
        return 0;
    }

    public String getInventoryName() {
        return null;
    }


}
