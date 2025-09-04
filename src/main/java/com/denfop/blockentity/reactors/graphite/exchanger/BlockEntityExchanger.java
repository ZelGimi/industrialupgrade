package com.denfop.blockentity.reactors.graphite.exchanger;

import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.reactors.IGraphiteReactor;
import com.denfop.api.widget.EnumTypeSlot;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blockentity.reactors.graphite.IExchanger;
import com.denfop.blockentity.reactors.graphite.IExchangerItem;
import com.denfop.blockentity.reactors.graphite.controller.BlockEntityMainController;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuExchanger;
import com.denfop.inventory.Inventory;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenExchanger;
import com.denfop.screen.ScreenIndustrialUpgrade;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockEntityExchanger extends BlockEntityMultiBlockElement implements IExchanger, IUpdatableTileEvent {

    public final int levelBlock;
    private final Inventory slot;
    public double percent = 1;
    private int x = 0;
    private IExchangerItem item;

    public BlockEntityExchanger(int levelBlock, MultiBlockEntity block, BlockPos pos, BlockState state) {
        super(block, pos, state);
        this.levelBlock = levelBlock;
        this.slot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public EnumTypeSlot getTypeSlot() {
                return EnumTypeSlot.EXCHANGE;
            }

            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                return stack.getItem() instanceof IExchangerItem && ((IExchangerItem) stack.getItem()).getLevelExchanger() <= ((BlockEntityExchanger) this.base).getBlockLevel();
            }

            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                if (!level.isClientSide) {
                    if (content.isEmpty()) {
                        ((BlockEntityExchanger) this.base).percent = 1;
                    } else {
                        ((BlockEntityExchanger) this.base).percent = 1 - ((IExchangerItem) content.getItem()).getPercent();
                        item = (IExchangerItem) content.getItem();
                    }
                }
                return content;
            }
        };
        slot.setStackSizeLimit(1);
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getMain() != null) {
            BlockEntityMainController controller = (BlockEntityMainController) this.getMain();
            if (controller.work && !this.slot.isEmpty() && level.getGameTime() % 20 == 0) {
                if (item == null) {
                    this.item = (IExchangerItem) this.slot.get(0).getItem();
                }
                this.item.damageItem(this.slot.get(0), 1);
            }
        }
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isClientSide) {
            if (this.getSlot().get(0).isEmpty()) {
                this.percent = 1;
            } else {
                this.percent = 1 - ((IExchangerItem) this.getSlot().get(0).getItem()).getPercent();
            }
        }
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        customPacketBuffer.writeInt(this.x);
        customPacketBuffer.writeDouble(this.percent);
        return customPacketBuffer;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        this.x = customPacketBuffer.readInt();
        this.percent = customPacketBuffer.readDouble();
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    public void readFromNBT(final CompoundTag nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        x = nbtTagCompound.getInt("exchanger_x");
    }

    @Override
    public CompoundTag writeToNBT(final CompoundTag nbt) {
        CompoundTag nbtTagCompound = super.writeToNBT(nbt);
        nbtTagCompound.putInt("exchanger_x", x);
        return nbtTagCompound;
    }

    @Override
    public int getBlockLevel() {
        return levelBlock;
    }


    public Inventory getSlot() {
        return slot;
    }

    @Override
    public double getPercent(final int x) {
        if (this.getMain() == null || x != this.x || this.getSlot().isEmpty()) {
            return 1;
        }
        return this.percent;

    }

    @Override
    public ContainerMenuExchanger getGuiContainer(final Player var1) {
        return new ContainerMenuExchanger(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {

        return new ScreenExchanger((ContainerMenuExchanger) menu);
    }

    @Override
    public void updateTileServer(final Player var1, final double var2) {
        if (this.getMain() == null) {
            return;
        }
        IGraphiteReactor reactor = (IGraphiteReactor) this.getMain();
        if (var2 == 0) {
            this.x = Math.min(this.x + 1, reactor.getWidth() - 1);
            reactor.updateDataReactor();
        } else {
            this.x = Math.max(0, this.x - 1);
            reactor.updateDataReactor();
        }
    }

    public int getX() {
        return x;
    }

}
