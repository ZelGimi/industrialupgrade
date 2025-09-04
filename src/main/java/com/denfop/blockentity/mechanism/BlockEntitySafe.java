package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuSafe;
import com.denfop.inventory.Inventory;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenSafe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class BlockEntitySafe extends BlockEntityInventory {

    public final Inventory slot;
    public int timer = 10;

    public BlockEntitySafe(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3Entity.safe, pos, state);
        this.getComponentPrivate().setActivate(true);
        this.slot = new Inventory(this, Inventory.TypeItemSlot.INPUT_OUTPUT, 63);
    }

    @Override
    public <T> T getCapability(@NotNull BlockCapability<T, Direction> cap, @Nullable Direction side) {
        if (cap == Capabilities.ItemHandler.BLOCK)
            return null;
        return super.getCapability(cap, side);
    }


    public List<Inventory> getInputSlots() {
        return Collections.emptyList();
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (timer > 0) {
            timer--;
            if (timer == 0) {
                this.setActive(false);
            }
        }

    }

    @Override
    public ContainerMenuSafe getGuiContainer(final Player var1) {
        return new ContainerMenuSafe(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {

        return new ScreenSafe((ContainerMenuSafe) menu);

    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.safe;
    }

}
