package com.denfop.blockentity.cyclotron;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockCyclotronEntity;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuCyclotronElectrostaticDeflector;
import com.denfop.screen.ScreenCyclotronElectrostaticDeflector;
import com.denfop.screen.ScreenIndustrialUpgrade;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class BlockEntityCyclotronElectrostaticDeflector extends BlockEntityMultiBlockElement implements IElectrostaticDeflector {


    private final InventoryOutput outputSlot;

    public BlockEntityCyclotronElectrostaticDeflector(BlockPos pos, BlockState state) {
        super(BlockCyclotronEntity.cyclotron_electrostatic_deflector, pos, state);
        this.outputSlot = new InventoryOutput(this, 1);
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    public ContainerMenuCyclotronElectrostaticDeflector getGuiContainer(final Player var1) {
        return new ContainerMenuCyclotronElectrostaticDeflector(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenCyclotronElectrostaticDeflector((ContainerMenuCyclotronElectrostaticDeflector) menu);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockCyclotronEntity.cyclotron_electrostatic_deflector;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.cyclotron.getBlock(getTeBlock());
    }


    @Override
    public InventoryOutput getOutputSlot() {
        return outputSlot;
    }

}
