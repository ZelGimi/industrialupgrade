package com.denfop.tiles.cyclotron;

import com.denfop.IUItem;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockCyclotron;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerCyclotronElectrostaticDeflector;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiCyclotronElectrostaticDeflector;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TileEntityCyclotronElectrostaticDeflector extends TileEntityMultiBlockElement implements IElectrostaticDeflector {


    private final InvSlotOutput outputSlot;

    public TileEntityCyclotronElectrostaticDeflector(BlockPos pos, BlockState state) {
        super(BlockCyclotron.cyclotron_electrostatic_deflector,pos,state);
        this.outputSlot = new InvSlotOutput(this, 1);
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    public ContainerCyclotronElectrostaticDeflector getGuiContainer(final Player var1) {
        return new ContainerCyclotronElectrostaticDeflector(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiCyclotronElectrostaticDeflector((ContainerCyclotronElectrostaticDeflector) menu);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockCyclotron.cyclotron_electrostatic_deflector;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.cyclotron.getBlock(getTeBlock());
    }


    @Override
    public InvSlotOutput getOutputSlot() {
        return outputSlot;
    }

}
