package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerSafe;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiSafe;
import com.denfop.invslot.InvSlot;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class TileEntitySafe extends TileEntityInventory {

    public final InvSlot slot;
    public int timer = 10;

    public TileEntitySafe(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3.safe, pos,state);
        this.getComponentPrivate().setActivate(true);
        this.slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT_OUTPUT, 63);
    }



    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction facing) {
        if (cap == ForgeCapabilities.ITEM_HANDLER)
            return LazyOptional.empty();
        return super.getCapability(cap, facing);
    }


    public List<InvSlot> getInputSlots() {
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
    public ContainerSafe getGuiContainer(final Player var1) {
        return new ContainerSafe(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {

        return new GuiSafe((ContainerSafe) menu);

    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.safe;
    }

}
