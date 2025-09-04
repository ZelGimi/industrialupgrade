package com.denfop.api.otherenergies.transport;


import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface ITransportNet {


    ITransportTile getSubTile(Level var1, BlockPos var2);

    <T extends BlockEntity & ITransportTile> void addTile(T var1);

    void removeTile(ITransportTile var1);

}
