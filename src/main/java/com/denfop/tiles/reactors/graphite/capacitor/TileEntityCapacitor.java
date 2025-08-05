package com.denfop.tiles.reactors.graphite.capacitor;

import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.reactors.IGraphiteReactor;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerCapacitor;
import com.denfop.gui.GuiCapacitor;
import com.denfop.gui.GuiCore;
import com.denfop.invslot.InvSlot;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.graphite.ICapacitor;
import com.denfop.tiles.reactors.graphite.ICapacitorItem;
import com.denfop.tiles.reactors.graphite.controller.TileEntityMainController;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class TileEntityCapacitor extends TileEntityMultiBlockElement implements ICapacitor, IUpdatableTileEvent {

    private final int levelBlock;
    private final InvSlot slot;
    public double percent = 1;
    private int x = 0;
    private ICapacitorItem item;

    public TileEntityCapacitor(int levelBlock, IMultiTileBlock block, BlockPos pos, BlockState state) {
        super(block, pos, state);
        this.levelBlock = levelBlock;
        this.slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {

            @Override
            public EnumTypeSlot getTypeSlot() {
                return EnumTypeSlot.CAPACITOR;
            }

            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() instanceof ICapacitorItem && ((ICapacitorItem) stack.getItem()).getLevelCapacitor() <= ((TileEntityCapacitor) this.base).getBlockLevel();
            }

            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                if (!level.isClientSide) {
                    if (content.isEmpty()) {
                        ((TileEntityCapacitor) this.base).percent = 1;
                    } else {
                        ((TileEntityCapacitor) this.base).percent = 1 - ((ICapacitorItem) content.getItem()).getPercent();
                        item = (ICapacitorItem) content.getItem();
                    }
                }
                return content;
            }
        };
        slot.setStackSizeLimit(1);
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
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isClientSide) {
            if (this.getSlot().get(0).isEmpty()) {
                this.percent = 1;
            } else {
                this.percent = 1 - ((ICapacitorItem) this.getSlot().get(0).getItem()).getPercent();
            }
        }
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.getMain() != null) {
            TileEntityMainController controller = (TileEntityMainController) this.getMain();
            if (controller.work && !this.slot.isEmpty() && level.getGameTime() % 20 == 0) {
                if (item == null) {
                    this.item = (ICapacitorItem) this.slot.get(0).getItem();
                }
                this.item.damageItem(this.slot.get(0), -1);
            }
        }
    }

    @Override
    public void readFromNBT(final CompoundTag nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        x = nbtTagCompound.getInt("capacitor_x");
    }

    @Override
    public CompoundTag writeToNBT(final CompoundTag nbt) {
        CompoundTag nbtTagCompound = super.writeToNBT(nbt);
        nbtTagCompound.putInt("capacitor_x", x);
        return nbtTagCompound;
    }

    @Override
    public int getBlockLevel() {
        return levelBlock;
    }


    public InvSlot getSlot() {
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
    public ContainerCapacitor getGuiContainer(final Player var1) {
        return new ContainerCapacitor(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiCapacitor((ContainerCapacitor) menu);
    }


    public int getX() {
        return x;
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

}
