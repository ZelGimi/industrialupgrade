package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.inv.VirtualSlot;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.water.upgrade.RotorUpgradeSystem;
import com.denfop.api.windsystem.IWindRotor;
import com.denfop.api.windsystem.IWindUpgradeBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AbstractComponent;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerWaterRotorUpgrade;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiWaterRotorUpgrade;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotRotorWater;
import com.denfop.invslot.InvSlotWaterUpgrade;
import com.denfop.items.modules.ItemWaterRotorsUpgrade;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.NeoForge;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TileEntityWaterRotorModifier extends TileEntityInventory implements IWindUpgradeBlock, IUpdatableTileEvent {

    public final InvSlotWaterUpgrade slot;
    public final InvSlotRotorWater rotor_slot;

    public TileEntityWaterRotorModifier(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3.water_modifier, pos, state);
        slot = new InvSlotWaterUpgrade(this);
        rotor_slot = new InvSlotRotorWater(slot);
    }

    @Override
    public List<ItemStack> getAuxDrops(int fortune) {
        List<ItemStack> ret = new ArrayList<>();
        for (final InvSlot slot : this.invSlots) {
            if (!(slot instanceof VirtualSlot) && !(slot instanceof InvSlotWaterUpgrade && !this.rotor_slot.isEmpty())) {
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
        return IUItem.basemachine2.getBlock(getTeBlock());
    }


    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        rotor_slot.readFromNbt(customPacketBuffer.registryAccess(), getNBTFromSlot(customPacketBuffer));
        slot.readFromNbt(customPacketBuffer.registryAccess(), getNBTFromSlot(customPacketBuffer));

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
    public ContainerWaterRotorUpgrade getGuiContainer(final Player entityPlayer) {
        return new ContainerWaterRotorUpgrade(this, entityPlayer);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {

        return new GuiWaterRotorUpgrade((ContainerWaterRotorUpgrade) menu);
    }


    @Override
    public void updateTileServer(final Player var1, final double var2) {
        if (var2 == 0) {
            if (!this.rotor_slot.get(0).isEmpty()) {
                RotorUpgradeSystem.instance.removeUpdate(this.getItemStack(), this.getParent().getWorld(), 0);
                List<ItemStack> stacks = new ArrayList<>(this.slot);
                this.slot.update();
                for (int i = 0; i < stacks.size(); i++) {
                    if (!stacks.get(i).isEmpty()) {
                        RotorUpgradeSystem.instance.addUpdate(this.getItemStack(), this.getParent().getWorld(), (com.denfop.api.water.upgrade.EnumInfoRotorUpgradeModules.getFromID(((ItemWaterRotorsUpgrade<?>) stacks.get(i).getItem()).getElement().getId())));

                        NeoForge.EVENT_BUS.post(new com.denfop.api.water.upgrade.event.EventRotorItemLoad(this
                                .getParent().getWorld(), (com.denfop.api.water.upgrade.IRotorUpgradeItem) this
                                .getItemStack().getItem(), this
                                .getItemStack()));
                    }
                }
                this.slot.update(this.rotor_slot.get(0));
            }
        } else {
            if (!this.rotor_slot.get(0).isEmpty()) {
                for (int i = 0; i < this.slot.size(); i++) {
                    RotorUpgradeSystem.instance.removeUpdate(this.getItemStack(), this.getParent().getWorld(), i);

                }
            }
        }
    }

}
