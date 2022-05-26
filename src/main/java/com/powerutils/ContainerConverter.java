package com.powerutils;

import com.denfop.tiles.base.TileEntityElectricBlock;
import ic2.core.ContainerFullInv;
import ic2.core.slot.ArmorSlot;
import ic2.core.slot.SlotArmor;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerConverter extends ContainerFullInv<TileEntityConverter> {

    public ContainerConverter(EntityPlayer entityPlayer, TileEntityConverter tileEntity) {
        super(entityPlayer, tileEntity, 176);
        for (int k = 0; k < 4; k++)
            addSlotToContainer(new SlotInvSlot(tileEntity.upgradeSlot, k, 152, 17 + k * 18));
    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("energy2");
        ret.add("energy");
        ret.add("capacity");
        ret.add("maxStorage2");
        ret.add("rf");
        ret.add("tier");
        ret.add("differenceenergy1");
        ret.add("differenceenergy");
        return ret;
    }

}
