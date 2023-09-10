package com.denfop.container;

import com.denfop.tiles.base.TileQuarryVein;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerQuarryVein extends ContainerFullInv<TileQuarryVein> {

    public ContainerQuarryVein(EntityPlayer entityPlayer, TileQuarryVein tileEntity1) {
        this(entityPlayer, tileEntity1, 166);

    }

    public ContainerQuarryVein(EntityPlayer entityPlayer, TileQuarryVein tileEntity1, int height) {
        super(entityPlayer, tileEntity1, height);
    }


}
