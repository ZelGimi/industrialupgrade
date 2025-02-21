package com.denfop.tiles.chemicalplant;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockChemicalPlant;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;

public class TileEntityChemicalPlantTransport extends TileEntityMultiBlockElement implements ITransport {

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockChemicalPlant.chemical_plant_transport;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.chemicalPlant;
    }

}
