package com.denfop.blockentity.base;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.item.upgrade.UpgradeSystem;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.containermenu.ContainerMenuAntiUpgrade;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.inventory.InventoryAntiUpgradeBlock;
import com.denfop.items.modules.ItemUpgradeModule;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenAntiUpgradeBlock;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.utils.Keyboard;
import com.denfop.utils.Localization;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.io.IOException;
import java.util.List;

public class BlockEntityAntiUpgradeBlock extends BlockEntityElectricMachine implements IUpdatableTileEvent {

    public final InventoryAntiUpgradeBlock input;
    public int index;
    public int progress;
    public boolean need;

    public BlockEntityAntiUpgradeBlock(BlockPos pos, BlockState state) {
        super(1000, 14, 4, BlockBaseMachine3Entity.antiupgradeblock, pos, state);
        this.need = false;
        this.progress = 0;
        this.input = new InventoryAntiUpgradeBlock(this);
        this.index = 0;
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.antiupgradeblock;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
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


    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.need) {
            if (!this.input.isEmpty()) {
                if (this.energy.canUseEnergy(5)) {
                    this.progress++;
                    this.energy.useEnergy(5);
                    if (this.progress >= 100) {
                        final List<ItemStack> list = UpgradeSystem.system.getListStack(this.input.get(0));
                        if (this.outputSlot.canAdd(list.get(this.index))) {
                            this.outputSlot.add(list.get(this.index));
                        }
                        UpgradeSystem.system.removeUpdate(this.input.get(0), this.getWorld(), ((ItemUpgradeModule<?>) list.get(index).getItem()).getElement().getId());
                        this.need = false;
                        this.progress = 0;

                    }

                }
            }
        }
    }

    @Override
    public ContainerMenuAntiUpgrade getGuiContainer(final Player entityPlayer) {
        return new ContainerMenuAntiUpgrade(entityPlayer, this);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {

        return new ScreenAntiUpgradeBlock((ContainerMenuAntiUpgrade) menu);
    }

    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.progress = nbttagcompound.getInt("progress");
        this.need = nbttagcompound.getBoolean("need");
        this.index = nbttagcompound.getInt("index");
    }

    public CompoundTag writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.putInt("progress", this.progress);
        nbttagcompound.putInt("index", this.index);
        nbttagcompound.putBoolean("need", this.need);
        return nbttagcompound;
    }

    @Override
    public void updateTileServer(final Player entityPlayer, final double i) {
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
            final List<ItemStack> list = UpgradeSystem.system.getListStack(this.input.get(0));

            if (!list.get((int) (i - 1)).isEmpty()) {
                this.index = (int) (i - 1);
                return;
            }
        }
        if (i == 0) {
            final List<ItemStack> list = UpgradeSystem.system.getListStack(this.input.get(0));
            boolean need = false;
            if (list.size() < this.index) {
                this.need = false;
            } else {
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

    @Override
    public SoundEvent getSound() {
        return null;
    }

}
