package com.denfop.container;

import com.denfop.tiles.mechanism.TileEntityMagnetGenerator;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerMagnetGenerator extends ContainerFullInv<TileEntityMagnetGenerator> {

    public ContainerMagnetGenerator(EntityPlayer entityPlayer, TileEntityMagnetGenerator tileEntity1) {
        this(entityPlayer, tileEntity1, 166);

    }

    public ContainerMagnetGenerator(EntityPlayer entityPlayer, TileEntityMagnetGenerator tileEntity1, int height) {
        super(entityPlayer, tileEntity1, height);
    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("timer");
        ret.add("sound");
        return ret;
    }


}
