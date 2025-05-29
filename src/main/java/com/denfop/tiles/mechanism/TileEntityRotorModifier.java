package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.inv.VirtualSlot;
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
import com.denfop.componets.AbstractComponent;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerRotorUpgrade;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiRotorUpgrade;
import com.denfop.invslot.InvSlot;
import com.denfop.items.modules.ItemRotorsUpgrade;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TileEntityRotorModifier extends TileEntityInventory implements IWindUpgradeBlock, IUpdatableTileEvent {

    public final InvSlotUpgrade slot;
    public final InvSlotRotor rotor_slot;

    public TileEntityRotorModifier(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3.rotor_modifier,pos,state);
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
        return IUItem.basemachine2.getBlock(getTeBlock());
    }



    @Override
    public IWindRotor getRotor() {
        if (!this.rotor_slot.get(0).isEmpty()) {
            return (IWindRotor) this.rotor_slot.get(0).getItem();
        }
        return null;
    }

    @Override
    public ItemStack getItemStack() {
        return this.rotor_slot.get(0);
    }

    @Override
    public ContainerRotorUpgrade getGuiContainer(final Player entityPlayer) {
        return new ContainerRotorUpgrade(this, entityPlayer);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {

        return new GuiRotorUpgrade((ContainerRotorUpgrade) menu);
    }

    @Override
    public List<ItemStack> getAuxDrops(int fortune) {
        List<ItemStack> ret = new ArrayList<>();
        for (final InvSlot slot : this.invSlots) {
            if (!(slot instanceof VirtualSlot)  && !(slot instanceof InvSlotUpgrade && !this.rotor_slot.isEmpty())) {
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

    @Override
    public void updateTileServer(final Player var1, final double var2) {
        if (var2 == 0) {
            if (!this.rotor_slot.get(0).isEmpty()) {
                for (int i = 0; i < this.slot.size(); i++) {
                    RotorUpgradeSystem.instance.removeUpdate(this.getItemStack(), this.getParent().getWorld(), i);
                    if (!this.slot.get(i).isEmpty()) {
                        CompoundTag nbt = ModUtils.nbt(this.getItemStack());
                        nbt.putString(
                                "mode_module" + i,
                                (EnumInfoRotorUpgradeModules.getFromID(IUItem.rotors_upgrade.getMeta((ItemRotorsUpgrade) this.slot.get(i).getItem()))).name
                        );
                        MinecraftForge.EVENT_BUS.post(new EventRotorItemLoad(this
                                .getParent().getWorld(), (IRotorUpgradeItem) this
                                .getItemStack().getItem(), this
                                .getItemStack()));
                    }
                }
            }
        } else {
            if (!this.rotor_slot.get(0).isEmpty()) {
                for (int i = 0; i < this.slot.size(); i++) {
                    RotorUpgradeSystem.instance.removeUpdate(this.getItemStack(), this.getParent().getWorld(), i);
                    MinecraftForge.EVENT_BUS.post(new EventRotorItemLoad(this
                            .getParent().getWorld(), (IRotorUpgradeItem) this
                            .getItemStack().getItem(), this
                            .getItemStack()));
                }

            }
        }
    }

}
