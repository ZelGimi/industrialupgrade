package com.denfop.container;

import com.denfop.tiles.mechanism.energy.TileEntityEnergyController;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerController extends ContainerFullInv<TileEntityEnergyController> {

    public ContainerController(TileEntityEnergyController tileEntity1, EntityPlayer entityPlayer) {
        super(entityPlayer, tileEntity1, 179);
    }

    @Override
    public List<String> getNetworkedFields() {
        final List<String> list = super.getNetworkedFields();
        list.add("size");
        return list;
    }

}
