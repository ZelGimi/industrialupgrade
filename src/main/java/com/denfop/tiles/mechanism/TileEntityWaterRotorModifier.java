package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.inv.VirtualSlot;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.water.upgrade.RotorUpgradeSystem;
import com.denfop.api.windsystem.IWindRotor;
import com.denfop.api.windsystem.IWindUpgradeBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AbstractComponent;
import com.denfop.container.ContainerWaterRotorUpgrade;
import com.denfop.gui.GuiWaterRotorUpgrade;
import com.denfop.invslot.Inventory;
import com.denfop.invslot.InventoryRotorWater;
import com.denfop.invslot.InventoryWaterUpgrade;
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
import java.util.ArrayList;
import java.util.List;

public class TileEntityWaterRotorModifier extends TileEntityInventory implements IWindUpgradeBlock, IUpdatableTileEvent {

    public final InventoryWaterUpgrade slot;
    public final InventoryRotorWater rotor_slot;

    public TileEntityWaterRotorModifier() {
        slot = new InventoryWaterUpgrade(this);
        rotor_slot = new InventoryRotorWater(slot);
    }
    @Override
    public List<ItemStack> getAuxDrops(int fortune) {
        List<ItemStack> ret = new ArrayList<>();
        for (final Inventory slot : this.inventories) {
            if (!(slot instanceof VirtualSlot)  && !(slot instanceof InventoryWaterUpgrade && !this.rotor_slot.isEmpty())) {
                for (final ItemStack stack : slot) {
                    if (!ModUtils.isEmpty(stack)) {
                        ret.add(stack);
                    }
                }
            }
        }
        for (AbstractComponent component : this.getComponentList()) {
            if (!component.getDrops().isEmpty()) {
                ret.addAll(component.getDrops());
            }
        }
        return ret;
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


    @Override
    public void updateTileServer(final EntityPlayer var1, final double var2) {
        if (var2 == 0) {
            if (!this.rotor_slot.get().isEmpty()) {
                for (int i = 0; i < this.slot.size(); i++) {
                    RotorUpgradeSystem.instance.removeUpdate(this.getItemStack(), this.getWorld(), i);
                    if (!this.slot.get(i).isEmpty()) {
                        NBTTagCompound nbt = ModUtils.nbt(this.getItemStack());
                        nbt.setString(
                                "mode_module" + i,
                                (com.denfop.api.water.upgrade.EnumInfoRotorUpgradeModules.getFromID(this.slot
                                        .get(i)
                                        .getItemDamage())).name
                        );
                        MinecraftForge.EVENT_BUS.post(new com.denfop.api.water.upgrade.event.EventRotorItemLoad(this
                               .getWorld(), (com.denfop.api.water.upgrade.IRotorUpgradeItem) this
                                .getItemStack().getItem(), this
                                .getItemStack()));
                    }
                }
            }
        } else {
            if (!this.rotor_slot.get().isEmpty()) {
                for (int i = 0; i < this.slot.size(); i++) {
                    RotorUpgradeSystem.instance.removeUpdate(this.getItemStack(), this.getWorld(), i);
                    MinecraftForge.EVENT_BUS.post(new com.denfop.api.water.upgrade.event.EventRotorItemLoad(this
                           .getWorld(), (com.denfop.api.water.upgrade.IRotorUpgradeItem) this
                            .getItemStack().getItem(), this
                            .getItemStack()));
                }
            }
        }
    }

}
