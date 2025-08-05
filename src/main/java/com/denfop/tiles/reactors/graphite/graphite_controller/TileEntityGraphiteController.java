package com.denfop.tiles.reactors.graphite.graphite_controller;

import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.reactors.IGraphiteReactor;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerGraphiteController;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiGraphiteGraphiteController;
import com.denfop.invslot.InvSlot;
import com.denfop.items.ItemCraftingElements;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.graphite.IGraphiteController;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class TileEntityGraphiteController extends TileEntityMultiBlockElement implements IGraphiteController,
        IUpdatableTileEvent {

    public final InvSlot slot;
    private final int levelBlock;
    public double fuel;
    public int levelGraphite = 1;
    private int index;

    public TileEntityGraphiteController(int levelBlock, IMultiTileBlock block, BlockPos pos, BlockState state) {
        super(block, pos, state);
        this.levelBlock = levelBlock;
        this.slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                if (!(stack.getItem() instanceof ItemCraftingElements)) {
                    return false;
                }
                if (!level.isClientSide) {
                    final int itemDamage = ((ItemCraftingElements<?>) stack.getItem()).getElement().getId();
                    switch (itemDamage) {
                        case 357:
                            return ((TileEntityGraphiteController) this.base).getBlockLevel() >= 0;
                        case 410:
                            return ((TileEntityGraphiteController) this.base).getBlockLevel() >= 1;
                        case 310:
                            return ((TileEntityGraphiteController) this.base).getBlockLevel() >= 2;
                        case 368:
                            return ((TileEntityGraphiteController) this.base).getBlockLevel() >= 3;
                    }
                }
                return false;
            }

            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                if (!level.isClientSide) {
                    if (content.isEmpty()) {
                        ((TileEntityGraphiteController) this.base).fuel = 0;
                    }
                }
                return content;
            }
        };
    }

    public int getIndex() {
        return index;
    }

    @Override
    public void setIndex(final int i) {
        this.index = i;
    }

    public double getFuel() {
        return fuel;
    }

    public InvSlot getSlot() {
        return slot;
    }

    @Override
    public ContainerGraphiteController getGuiContainer(final Player var1) {
        return new ContainerGraphiteController(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiGraphiteGraphiteController((ContainerGraphiteController) menu);
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        customPacketBuffer.writeInt(this.index);
        customPacketBuffer.writeDouble(this.fuel);
        customPacketBuffer.writeInt(this.levelGraphite);
        return customPacketBuffer;
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        this.index = customPacketBuffer.readInt();
        this.fuel = customPacketBuffer.readDouble();
        this.levelGraphite = customPacketBuffer.readInt();
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    public void readFromNBT(final CompoundTag nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        fuel = nbtTagCompound.getDouble("fuel");
        levelGraphite = nbtTagCompound.getInt("levelGraphite");
    }

    @Override
    public CompoundTag writeToNBT(final CompoundTag nbt) {
        CompoundTag nbtTagCompound = super.writeToNBT(nbt);
        nbtTagCompound.putDouble("fuel", fuel);
        nbtTagCompound.putInt("levelGraphite", levelGraphite);
        return nbtTagCompound;
    }

    @Override
    public int getBlockLevel() {
        return levelBlock;
    }

    @Override
    public ItemStack getGraphite() {
        return this.slot.get(0);
    }

    @Override
    public int getLevelGraphite() {
        return this.levelGraphite;
    }

    @Override
    public double getFuelGraphite() {
        return this.fuel;
    }

    @Override
    public void consumeFuelGraphite(double col) {
        this.fuel -= col;
    }

    @Override
    public void consumeGraphite() {
        if (!this.slot.get(0).isEmpty()) {
            final int itemDamage = ((ItemCraftingElements<?>) this.slot.get(0).getItem()).getElement().getId();
            this.slot.get(0).shrink(1);
            switch (itemDamage) {
                case 357:
                    this.fuel = 100;
                    break;
                case 410:
                    this.fuel = 500;
                    break;
                case 310:
                    this.fuel = 2500;
                    break;
                case 368:
                    this.fuel = 5000;
                    break;
            }
        } else {
            this.fuel = 0;
        }
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        this.levelGraphite = Math.max(1, levelGraphite);
    }

    @Override
    public void updateTileServer(final Player var1, final double var2) {
        if (var2 == 0) {
            this.levelGraphite = Math.min(this.levelGraphite + 1, 5);
        } else {
            this.levelGraphite = Math.max(1, levelGraphite - 1);
        }
        if (this.getMain() != null) {
            IGraphiteReactor graphiteReactor = (IGraphiteReactor) this.getMain();
            graphiteReactor.updateDataReactor();
        }
    }

}
