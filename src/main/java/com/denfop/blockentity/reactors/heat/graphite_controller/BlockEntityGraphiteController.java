package com.denfop.blockentity.reactors.heat.graphite_controller;

import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.reactors.IHeatReactor;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blockentity.reactors.heat.IGraphiteController;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuHeatGraphiteController;
import com.denfop.inventory.Inventory;
import com.denfop.items.ItemCraftingElements;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenHeatGraphiteGraphiteController;
import com.denfop.screen.ScreenIndustrialUpgrade;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class BlockEntityGraphiteController extends BlockEntityMultiBlockElement implements IGraphiteController,
        IUpdatableTileEvent {

    public final Inventory slot;
    private final int levelBlock;
    public double fuel;
    public int levelGraphite = 1;
    private int index;

    public BlockEntityGraphiteController(int levelBlock, MultiBlockEntity block, BlockPos pos, BlockState state) {
        super(block, pos, state);
        this.levelBlock = levelBlock;
        this.slot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                if (!(stack.getItem() instanceof ItemCraftingElements)) {
                    return false;
                }
                final int itemDamage = ((ItemCraftingElements<?>) stack.getItem()).getElement().getId();
                switch (itemDamage) {
                    case 357:
                        return ((BlockEntityGraphiteController) this.base).getBlockLevel() >= 0;
                    case 410:
                        return ((BlockEntityGraphiteController) this.base).getBlockLevel() >= 1;
                    case 310:
                        return ((BlockEntityGraphiteController) this.base).getBlockLevel() >= 2;
                    case 368:
                        return ((BlockEntityGraphiteController) this.base).getBlockLevel() >= 3;
                }
                return false;
            }

            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                if (content.isEmpty()) {
                    ((BlockEntityGraphiteController) this.base).fuel = 0;
                }
                return content;
            }
        };
    }


    public double getFuel() {
        return fuel;
    }

    public Inventory getSlot() {
        return slot;
    }

    @Override
    public ContainerMenuHeatGraphiteController getGuiContainer(final Player var1) {
        return new ContainerMenuHeatGraphiteController(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {

        return new ScreenHeatGraphiteGraphiteController((ContainerMenuHeatGraphiteController) menu);
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
        index = nbtTagCompound.getInt("index");
    }

    @Override
    public CompoundTag writeToNBT(final CompoundTag nbt) {
        CompoundTag nbtTagCompound = super.writeToNBT(nbt);
        nbtTagCompound.putDouble("fuel", fuel);
        nbtTagCompound.putInt("index", index);
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
            final int itemDamage = ((ItemCraftingElements<?>) this.getSlot().get(0).getItem()).getElement().getId();
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
    public int getIndex() {
        return this.index;
    }

    @Override
    public void updateTileServer(final Player var1, final double var2) {
        if (this.getMain() != null) {
            IHeatReactor heatReactor = (IHeatReactor) this.getMain();
            if (var2 == 0) {
                this.levelGraphite = Math.min(this.levelGraphite + 1, 5);
            } else if (var2 == 1) {
                this.levelGraphite = Math.max(1, levelGraphite - 1);
            } else if (var2 == 2) {
                this.index = Math.min(this.index + 1, heatReactor.getWidth() - 1);
            } else if (var2 == 3) {
                this.index = Math.max(this.index - 1, 0);
            }


            heatReactor.updateDataReactor();
        }
    }

}
