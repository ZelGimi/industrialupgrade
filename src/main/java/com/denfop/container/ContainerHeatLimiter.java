package com.denfop.container;

import com.denfop.tiles.reactors.TileEntityHeatSensor;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerHeatLimiter extends ContainerFullInv<TileEntityHeatSensor> {

    public ContainerHeatLimiter(TileEntityHeatSensor tileEntityLimiter, EntityPlayer entityPlayer) {
        super(entityPlayer, tileEntityLimiter, 166);


    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("limit");
        return ret;
    }

}
