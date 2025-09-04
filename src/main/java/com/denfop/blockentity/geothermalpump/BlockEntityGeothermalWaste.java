package com.denfop.blockentity.geothermalpump;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockGeothermalPumpEntity;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuGeothermalWaste;
import com.denfop.inventory.Inventory;
import com.denfop.screen.ScreenGeothermalWaste;
import com.denfop.screen.ScreenIndustrialUpgrade;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockEntityGeothermalWaste extends BlockEntityMultiBlockElement implements IWaste {

    private final Inventory slot;

    public BlockEntityGeothermalWaste(BlockPos pos, BlockState state) {
        super(BlockGeothermalPumpEntity.geothermal_waste, pos, state);
        this.slot = new Inventory(this, Inventory.TypeItemSlot.OUTPUT, 4);
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    public Inventory getSlot() {
        return slot;
    }

    public ContainerMenuGeothermalWaste getGuiContainer(final Player var1) {
        return new ContainerMenuGeothermalWaste(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {

        return new ScreenGeothermalWaste((ContainerMenuGeothermalWaste) menu);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockGeothermalPumpEntity.geothermal_waste;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.geothermalpump.getBlock(getTeBlock());
    }

}
