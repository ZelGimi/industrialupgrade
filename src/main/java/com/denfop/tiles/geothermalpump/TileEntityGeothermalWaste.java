package com.denfop.tiles.geothermalpump;

import com.denfop.IUItem;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGeothermalPump;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerGeothermalWaste;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiGeothermalWaste;
import com.denfop.invslot.InvSlot;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TileEntityGeothermalWaste extends TileEntityMultiBlockElement implements IWaste {

    private final InvSlot slot;

    public TileEntityGeothermalWaste(BlockPos pos, BlockState state) {
        super(BlockGeothermalPump.geothermal_waste,pos,state);
        this.slot = new InvSlot(this, InvSlot.TypeItemSlot.OUTPUT, 4);
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    public InvSlot getSlot() {
        return slot;
    }

    public ContainerGeothermalWaste getGuiContainer(final Player var1) {
        return new ContainerGeothermalWaste(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {

        return new GuiGeothermalWaste((ContainerGeothermalWaste) menu);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockGeothermalPump.geothermal_waste;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.geothermalpump.getBlock(getTeBlock());
    }

}
