package com.denfop.tiles.base;

import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.container.ContainerAntiUpgrade;
import com.denfop.gui.GuiAntiUpgradeBlock;
import com.denfop.invslot.InvSlotAntiUpgradeBlock;
import ic2.api.network.INetworkClientTileEntityEventListener;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class TileEntityAntiUpgradeBlock extends TileEntityElectricMachine implements INetworkClientTileEntityEventListener {

    public final InvSlotAntiUpgradeBlock input;
    public int index;
    public int progress;
    public boolean need;

    public TileEntityAntiUpgradeBlock() {
        super(1000, 14, 4);
        this.need = false;
        this.progress = 0;
        this.input = new InvSlotAntiUpgradeBlock(this);
        this.index = 0;
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

    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.need) {
            if (!this.input.isEmpty()) {
                if (this.energy.canUseEnergy(5)) {
                    this.progress++;
                    this.energy.useEnergy(5);
                    if (this.progress >= 100) {
                        final List<ItemStack> list = UpgradeSystem.system.getListStack(this.input.get());
                        if (this.outputSlot.canAdd(list.get(this.index))) {
                            this.outputSlot.add(list.get(this.index));
                        }
                        UpgradeSystem.system.removeUpdate(this.input.get(), this.getWorld(), index);
                        this.need = false;
                        this.progress = 0;

                    }

                }
            }
        }
    }

    @Override
    public ContainerAntiUpgrade getGuiContainer(final EntityPlayer entityPlayer) {
        return new ContainerAntiUpgrade(entityPlayer, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer entityPlayer, final boolean b) {
        return new GuiAntiUpgradeBlock(this.getGuiContainer(entityPlayer));
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.progress = nbttagcompound.getInteger("progress");
        this.need = nbttagcompound.getBoolean("need");
        this.index = nbttagcompound.getInteger("index");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("progress", this.progress);
        nbttagcompound.setInteger("index", this.index);
        nbttagcompound.setBoolean("need", this.need);
        return nbttagcompound;
    }

    @Override
    public void onNetworkEvent(final EntityPlayer entityPlayer, final int i) {

        if (this.input.isEmpty()) {
            return;
        }
        if (this.need && i == 0) {
            return;
        }

        if (i >= 1) {
            final List<ItemStack> list = UpgradeSystem.system.getListStack(this.input.get());
            if (!list.get(i - 1).isEmpty()) {
                this.index = i - 1;
                return;
            }
        }
        if (i == 0) {
            final List<ItemStack> list = UpgradeSystem.system.getListStack(this.input.get());
            boolean need = false;
            final ItemStack stack = list.get(this.index);
            if (!stack.isEmpty()) {
                if (this.outputSlot.canAdd(stack)) {
                    need = true;
                }
            }
            this.need = need;
        }


    }

}
