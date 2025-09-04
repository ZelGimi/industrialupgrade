package com.denfop.blockentity.quarry_earth;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockEarthQuarryEntity;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuEarthChest;
import com.denfop.inventory.Inventory;
import com.denfop.screen.ScreenEarthChest;
import com.denfop.screen.ScreenIndustrialUpgrade;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class BlockEntityChest extends BlockEntityMultiBlockElement implements IEarthChest {

    private final Inventory slot;

    public BlockEntityChest(BlockPos pos, BlockState state) {
        super(BlockEarthQuarryEntity.earth_chest, pos, state);
        this.slot = new Inventory(this, Inventory.TypeItemSlot.OUTPUT, 9);
    }

    @Override
    public Inventory getSlot() {
        return slot;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.earthQuarry.getBlock(getTeBlock());
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockEarthQuarryEntity.earth_chest;
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    public ContainerMenuEarthChest getGuiContainer(final Player var1) {
        return new ContainerMenuEarthChest(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenEarthChest((ContainerMenuEarthChest) menu);
    }

}
