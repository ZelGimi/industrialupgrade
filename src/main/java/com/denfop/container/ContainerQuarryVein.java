package com.denfop.container;

import com.denfop.IUCore;
import com.denfop.tiles.base.TileEntityQuarryVein;
import ic2.core.ContainerFullInv;
import ic2.core.block.comp.TileEntityComponent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.item.ItemStack;

import java.util.Iterator;
import java.util.List;

public class ContainerQuarryVein extends ContainerFullInv<TileEntityQuarryVein> {

    public ContainerQuarryVein(EntityPlayer entityPlayer, TileEntityQuarryVein tileEntity1) {
        this(entityPlayer, tileEntity1, 166);

    }

    public ContainerQuarryVein(EntityPlayer entityPlayer, TileEntityQuarryVein tileEntity1, int height) {
        super(entityPlayer, tileEntity1, height);
    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("progress");
        ret.add("level");
        ret.add("vein");
        return ret;
    }

    public void detectAndSendChanges() {
        for (int i = 0; i < this.inventorySlots.size(); ++i) {
            ItemStack itemstack = this.inventorySlots.get(i).getStack();
            ItemStack itemstack1 = this.inventoryItemStacks.get(i);

            if (!ItemStack.areItemStacksEqual(itemstack1, itemstack)) {
                boolean clientStackChanged = !ItemStack.areItemStacksEqualUsingNBTShareTag(itemstack1, itemstack);
                itemstack1 = itemstack.isEmpty() ? ItemStack.EMPTY : itemstack.copy();
                this.inventoryItemStacks.set(i, itemstack1);

                if (clientStackChanged) {
                    for (int j = 0; j < this.listeners.size(); ++j) {
                        this.listeners.get(j).sendSlotContents(this, i, itemstack1);
                    }
                }
            }
        }
        if (this.base != null) {
            Iterator var1 = this.getNetworkedFields().iterator();

            Iterator var3;
            IContainerListener crafter;
            while (var1.hasNext()) {
                String name = (String) var1.next();
                var3 = this.listeners.iterator();

                while (var3.hasNext()) {
                    crafter = (IContainerListener) var3.next();
                    if (crafter instanceof EntityPlayerMP) {
                        IUCore.network.get(true).updateTileEntityFieldTo(this.base, name, (EntityPlayerMP) crafter);
                    }
                }
            }

            var1 = this.base.getComponents().iterator();

            while (var1.hasNext()) {
                TileEntityComponent component = (TileEntityComponent) var1.next();
                var3 = this.listeners.iterator();

                while (var3.hasNext()) {
                    crafter = (IContainerListener) var3.next();
                    if (crafter instanceof EntityPlayerMP) {
                        component.onContainerUpdate((EntityPlayerMP) crafter);
                    }
                }
            }
        }

    }

}
