package com.denfop.container;

import com.denfop.tiles.base.TileEntityTesseract;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerTesseract extends ContainerFullInv<TileEntityTesseract> {

    public ContainerTesseract(TileEntityTesseract tileEntityTesseract, EntityPlayer var1) {
        super(var1, tileEntityTesseract, 207);

    }

}
