package com.denfop.blockentity.chemicalplant;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockChemicalPlantEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityChemicalPlantTransport extends BlockEntityMultiBlockElement implements ITransport {

    public BlockEntityChemicalPlantTransport(BlockPos pos, BlockState state) {
        super(BlockChemicalPlantEntity.chemical_plant_transport, pos, state);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockChemicalPlantEntity.chemical_plant_transport;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.chemicalPlant.getBlock(getTeBlock().getId());
    }

}
