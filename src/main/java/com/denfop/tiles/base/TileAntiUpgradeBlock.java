package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.container.ContainerAntiUpgrade;
import com.denfop.gui.GuiAntiUpgradeBlock;
import com.denfop.invslot.InventoryAntiUpgradeBlock;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.List;

public class TileAntiUpgradeBlock extends TileElectricMachine implements IUpdatableTileEvent {

    public final InventoryAntiUpgradeBlock input;
    public int index;
    public int progress;
    public boolean need;

    public TileAntiUpgradeBlock() {
        super(1000, 14, 4);
        this.need = false;
        this.progress = 0;
        this.input = new InventoryAntiUpgradeBlock(this);
        this.index = 0;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.antiupgradeblock;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }


    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.machines_work_energy") + 5 + Localization.translate("iu" +
                    ".machines_work_energy_type_eu"));
        }
        super.addInformation(stack, tooltip);
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            progress = (int) DecoderHandler.decode(customPacketBuffer);
            index = (int) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, progress);
            EncoderHandler.encode(packet, index);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
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
                        UpgradeSystem.system.removeUpdate(this.input.get(), this.getWorld(), list.get(index).getItemDamage());
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
    public void updateTileServer(final EntityPlayer entityPlayer, final double i) {
        if (i == 10) {
            super.updateTileServer(entityPlayer, i);
            return;
        }
        if (this.input.isEmpty()) {
            return;
        }
        if (this.need && i == 0) {
            return;
        }

        if (i >= 1) {
            final List<ItemStack> list = UpgradeSystem.system.getListStack(this.input.get());
            if (!list.get((int) (i - 1)).isEmpty()) {
                this.index = (int) (i - 1);
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

    @Override
    public SoundEvent getSound() {
        return null;
    }

}
