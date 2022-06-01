package com.denfop.invslot;


import com.denfop.items.modules.ItemEntityModule;
import com.denfop.tiles.base.TileEntityAutoSpawner;
import com.denfop.utils.CapturedMobUtils;
import ic2.core.block.invslot.InvSlot;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;

public class InvSlotModules extends InvSlot {

    private final TileEntityAutoSpawner tile;
    private int stackSizeLimit;

    public InvSlotModules(TileEntityAutoSpawner base1) {
        super(base1, "modules", InvSlot.Access.I, 4, InvSlot.InvSide.TOP);
        this.tile = base1;
        this.stackSizeLimit = 1;
    }

    public boolean accepts(ItemStack itemStack) {
        if (!(itemStack.getItem() instanceof ItemEntityModule)) {
            return false;
        }
        if (itemStack.getItemDamage() == 0) {
            return false;
        }


        return CapturedMobUtils.containsSoul(itemStack);
    }

    public void update() {
        for (int i = 0; i < this.size(); i++) {
            if (!this.get(i).isEmpty()) {
                final CapturedMobUtils captured = CapturedMobUtils.create(this.get(i));
                assert captured != null;
                this.tile.mobUtils[i] = (EntityLiving) captured.getEntity(tile.getWorld(), true);
            } else {
                this.tile.mobUtils[i] = null;
            }
        }
    }

    @Override
    public void put(final int index, final ItemStack content) {
        super.put(index, content);
        for (int i = 0; i < this.size(); i++) {
            if (!this.get(i).isEmpty()) {
                final CapturedMobUtils captured = CapturedMobUtils.create(this.get(i));
                assert captured != null;
                this.tile.mobUtils[i] = (EntityLiving) captured.getEntity(tile.getWorld(), true);
            } else {
                this.tile.mobUtils[i] = null;
            }
        }
    }

    public int getStackSizeLimit() {
        return this.stackSizeLimit;
    }

    public void setStackSizeLimit(int stackSizeLimit) {
        this.stackSizeLimit = stackSizeLimit;
    }

}
