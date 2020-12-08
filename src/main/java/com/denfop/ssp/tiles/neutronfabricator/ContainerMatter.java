package com.denfop.ssp.tiles.neutronfabricator;

import ic2.core.ContainerFullInv;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerMatter extends ContainerFullInv<TileEntityMassFabricator> {
  public ContainerMatter(EntityPlayer player, TileEntityMassFabricator tileEntity) {
    super(player, tileEntity, 166);
    addSlotToContainer(new SlotInvSlot(tileEntity.amplifierSlot, 0, 72, 40));
    addSlotToContainer(new SlotInvSlot(tileEntity.outputSlot, 0, 125, 59));
    addSlotToContainer(new SlotInvSlot(tileEntity.containerslot, 0, 125, 23));
    for (int i = 0; i < 4; i++)
      addSlotToContainer(new SlotInvSlot(tileEntity.upgradeSlot, i, 152, 8 + i * 18));
  }
  
  public List<String> getNetworkedFields() {
    List<String> ret = super.getNetworkedFields();
    ret.add("energy");
    ret.add("scrap");
    ret.add("fluidTank");
    return ret;
  }
}
