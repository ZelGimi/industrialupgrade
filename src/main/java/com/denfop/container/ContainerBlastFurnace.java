package com.denfop.container;

import com.denfop.componets.TileEntityAdvComponent;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.tiles.mechanism.blastfurnace.block.TileEntityBlastFurnaceMain;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IContainerListener;

import javax.annotation.Nonnull;
import java.util.List;

public class ContainerBlastFurnace extends ContainerFullInv<TileEntityBlastFurnaceMain> {

    public ContainerBlastFurnace(EntityPlayer entityPlayer, TileEntityBlastFurnaceMain tileEntityBlastFurnaceMain) {
        super(entityPlayer, tileEntityBlastFurnaceMain, 166);


        addSlotToContainer(new SlotInvSlot(tileEntityBlastFurnaceMain.output, 0,
                116, 35
        ));


        addSlotToContainer(new SlotInvSlot(tileEntityBlastFurnaceMain.invSlotBlastFurnace, 0,
                56, 34
        ));


        addSlotToContainer(new SlotInvSlot(tileEntityBlastFurnaceMain.output1,
                0, 29, 62
        ));
        addSlotToContainer(new SlotInvSlot(tileEntityBlastFurnaceMain.fluidSlot,
                0, 8, 62
        ));
    }

    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (this.base.getHeat() instanceof TileEntityInventory) {
            for (final TileEntityAdvComponent component : ((TileEntityInventory) this.base.getHeat()).getComponentList()) {
                for (IContainerListener var5 : this.listeners) {
                    if (var5 instanceof EntityPlayerMP) {
                        component.onContainerUpdate((EntityPlayerMP) var5);
                    }
                }
            }
        }
    }

    @Override
    public void onContainerClosed(@Nonnull final EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        this.base.entityPlayerList.remove(playerIn);
    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("tank");
        ret.add("full");
        ret.add("tank1");
        ret.add("blastHeat");
        ret.add("progress");
        ret.add("bar");
        ret.add("sound");
        return ret;

    }

}
