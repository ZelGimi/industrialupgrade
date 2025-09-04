package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.menu.VirtualSlot;
import com.denfop.api.water.upgrade.RotorUpgradeSystem;
import com.denfop.api.windsystem.IWindUpgradeBlock;
import com.denfop.api.windsystem.WindRotor;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.AbstractComponent;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuWaterRotorUpgrade;
import com.denfop.inventory.Inventory;
import com.denfop.inventory.InventoryRotorWater;
import com.denfop.inventory.InventoryWaterUpgrade;
import com.denfop.items.modules.ItemWaterRotorsUpgrade;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenWaterRotorUpgrade;
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

public class BlockEntityWaterRotorModifier extends BlockEntityInventory implements IWindUpgradeBlock, IUpdatableTileEvent {

    public final InventoryWaterUpgrade slot;
    public final InventoryRotorWater rotor_slot;

    public BlockEntityWaterRotorModifier(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3Entity.water_modifier, pos, state);
        slot = new InventoryWaterUpgrade(this);
        rotor_slot = new InventoryRotorWater(slot);
    }

    @Override
    public List<ItemStack> getAuxDrops(int fortune) {
        List<ItemStack> ret = new ArrayList<>();
        for (final Inventory slot : this.inventories) {
            if (!(slot instanceof VirtualSlot) && !(slot instanceof InventoryWaterUpgrade && !this.rotor_slot.isEmpty())) {
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

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.water_modifier;
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
    public WindRotor getRotor() {
        if (!this.rotor_slot.get(0).isEmpty()) {
            return (WindRotor) this.rotor_slot.get(0).getItem();
        }
        return null;
    }

    @Override
    public ItemStack getItemStack() {
        return this.rotor_slot.get(0);
    }

    @Override
    public ContainerMenuWaterRotorUpgrade getGuiContainer(final Player entityPlayer) {
        return new ContainerMenuWaterRotorUpgrade(this, entityPlayer);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {

        return new ScreenWaterRotorUpgrade((ContainerMenuWaterRotorUpgrade) menu);
    }


    @Override
    public void updateTileServer(final Player var1, final double var2) {
        if (var2 == 0) {
            if (!this.rotor_slot.get(0).isEmpty()) {
                RotorUpgradeSystem.instance.removeUpdate(this.getItemStack(), this.getWorld(), 0);
                List<ItemStack> stacks = new ArrayList<>(this.slot);
                this.slot.update();
                for (int i = 0; i < stacks.size(); i++) {
                    if (!stacks.get(i).isEmpty()) {
                        RotorUpgradeSystem.instance.addUpdate(this.getItemStack(), this.getWorld(), (com.denfop.api.water.upgrade.EnumInfoRotorUpgradeModules.getFromID(((ItemWaterRotorsUpgrade<?>) stacks.get(i).getItem()).getElement().getId())));

                        NeoForge.EVENT_BUS.post(new com.denfop.api.water.upgrade.event.EventRotorItemLoad(this.getWorld(), (com.denfop.api.water.upgrade.IRotorUpgradeItem) this
                                .getItemStack().getItem(), this
                                .getItemStack()));
                    }
                }
                this.slot.update(this.rotor_slot.get(0));
            }
        } else {
            if (!this.rotor_slot.get(0).isEmpty()) {
                for (int i = 0; i < this.slot.size(); i++) {
                    RotorUpgradeSystem.instance.removeUpdate(this.getItemStack(), this.getWorld(), i);

                }
            }
        }
    }

}
