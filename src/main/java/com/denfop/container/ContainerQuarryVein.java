package com.denfop.container;

import com.denfop.tiles.base.TileQuarryVein;
import net.minecraft.world.entity.player.Player;

public class ContainerQuarryVein extends ContainerFullInv<TileQuarryVein> {

    public ContainerQuarryVein(Player entityPlayer, TileQuarryVein tileEntity1) {
        this(entityPlayer, tileEntity1, 196);

    }

    public ContainerQuarryVein(Player entityPlayer, TileQuarryVein tileEntity1, int height) {
        super(entityPlayer, tileEntity1, height);
    }


}
