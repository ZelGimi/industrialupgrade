package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.windsystem.IWindRotor;
import com.denfop.api.windsystem.IWindUpgradeBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.container.ContainerWaterRotorUpgrade;
import com.denfop.gui.GuiWaterRotorUpgrade;
import com.denfop.invslot.InvSlotRotorWater;
import com.denfop.invslot.InvSlotWaterUpgrade;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

public class TileEntityWaterRotorModifier extends TileEntityInventory implements IWindUpgradeBlock {

    public final InvSlotWaterUpgrade slot;
    public final InvSlotRotorWater rotor_slot;

    public TileEntityWaterRotorModifier() {
        slot = new InvSlotWaterUpgrade(this);
        rotor_slot = new InvSlotRotorWater(slot);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.water_modifier;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
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
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        rotor_slot.readFromNbt(getNBTFromSlot(customPacketBuffer));
        slot.readFromNbt(getNBTFromSlot(customPacketBuffer));

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, rotor_slot);
            EncoderHandler.encode(packet, slot);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    @Override
    public IWindRotor getRotor() {
        if (!this.rotor_slot.get().isEmpty()) {
            return (IWindRotor) this.rotor_slot.get().getItem();
        }
        return null;
    }

    @Override
    public ItemStack getItemStack() {
        return this.rotor_slot.get();
    }

    @Override
    public ContainerWaterRotorUpgrade getGuiContainer(final EntityPlayer entityPlayer) {
        return new ContainerWaterRotorUpgrade(this, entityPlayer);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer entityPlayer, final boolean b) {
        return new GuiWaterRotorUpgrade(getGuiContainer(entityPlayer));
    }


}
