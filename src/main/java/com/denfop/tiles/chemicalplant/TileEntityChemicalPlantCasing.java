package com.denfop.tiles.chemicalplant;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockChemicalPlant;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityChemicalPlantCasing extends TileEntityMultiBlockElement implements ICasing {


    public TileEntityChemicalPlantCasing(BlockPos pos, BlockState state) {
        super(BlockChemicalPlant.chemical_plant_casing, pos, state);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockChemicalPlant.chemical_plant_casing;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.chemicalPlant.getBlock(getTeBlock().getId());
    }

}
