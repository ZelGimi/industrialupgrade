package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.windsystem.IWindRotor;
import com.denfop.api.windsystem.IWindUpgradeBlock;
import com.denfop.api.windsystem.InvSlotUpgrade;
import com.denfop.api.windsystem.upgrade.EnumInfoRotorUpgradeModules;
import com.denfop.api.windsystem.upgrade.IRotorUpgradeItem;
import com.denfop.api.windsystem.upgrade.InvSlotRotor;
import com.denfop.api.windsystem.upgrade.RotorUpgradeSystem;
import com.denfop.api.windsystem.upgrade.event.EventRotorItemLoad;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.container.ContainerRotorUpgrade;
import com.denfop.gui.GuiRotorUpgrade;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

public class TileEntityRotorModifier extends TileEntityInventory implements IWindUpgradeBlock, IUpdatableTileEvent {

    public final InvSlotUpgrade slot;
    public final InvSlotRotor rotor_slot;

    public TileEntityRotorModifier() {
        slot = new InvSlotUpgrade(this);
        rotor_slot = new InvSlotRotor(slot);
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

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.rotor_modifier;
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
    public ContainerRotorUpgrade getGuiContainer(final EntityPlayer entityPlayer) {
        return new ContainerRotorUpgrade(this, entityPlayer);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer entityPlayer, final boolean b) {
        return new GuiRotorUpgrade(getGuiContainer(entityPlayer));
    }


    @Override
    public void updateTileServer(final EntityPlayer var1, final double var2) {
        if(!this.rotor_slot.get().isEmpty())
        for(int i =0; i < this.slot.size();i++){
            RotorUpgradeSystem.instance.removeUpdate(this.getItemStack(), this.getParent().getWorld(), i);
            if(!this.slot.get(i).isEmpty()){
                NBTTagCompound nbt = ModUtils.nbt(this.getItemStack());
                nbt.setString("mode_module" + i, (EnumInfoRotorUpgradeModules.getFromID(this.slot.get(i).getItemDamage())).name);
                MinecraftForge.EVENT_BUS.post(new EventRotorItemLoad(this
                        .getParent().getWorld(), (IRotorUpgradeItem) this
                        .getItemStack().getItem(), this
                        .getItemStack()));
            }
        }
    }

}
