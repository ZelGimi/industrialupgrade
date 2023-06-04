package com.denfop.invslot;


import com.denfop.IUCore;
import com.denfop.items.modules.ItemEntityModule;
import com.denfop.tiles.base.TileEntityAutoSpawner;
import com.denfop.utils.CapturedMobUtils;
import ic2.core.init.Localization;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;

public class InvSlotModules extends InvSlot {

    private final TileEntityAutoSpawner tile;
    private int stackSizeLimit;

    public InvSlotModules(TileEntityAutoSpawner base1) {
        super(base1, "modules", InvSlot.Access.I, 4, InvSlot.InvSide.ANY);
        this.tile = base1;
        this.stackSizeLimit = 1;
    }

    public boolean accepts(ItemStack itemStack, final int index) {
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
                EntityLiving entityLiving = (EntityLiving) captured.getEntity(tile.getWorld(), true);
                this.tile.mobUtils[i] = entityLiving;
                this.tile.loot_Tables[i] = IUCore.lootTables.get(captured.getResource());
                this.tile.maxprogress[i] = 100 * captured.getCoefficient();
                this.tile.description_mobs[i] =
                        entityLiving.getName() + "\n" + Localization.translate("iu.show.health") + (int) entityLiving.getHealth() + "/" + (int) entityLiving.getMaxHealth()
                                + "\n" + Localization.translate("iu.show.speed") + (int) this.tile.maxprogress[i];
            } else {
                this.tile.mobUtils[i] = null;
                this.tile.loot_Tables[i] = null;
                this.tile.lootContext[i] = null;
                this.tile.maxprogress[i] = 100;
                this.tile.description_mobs[i] = "";
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
                EntityLiving entityLiving = (EntityLiving) captured.getEntity(tile.getWorld(), true);

                this.tile.mobUtils[i] = entityLiving;
                this.tile.maxprogress[i] = 100 * captured.getCoefficient();
                this.tile.loot_Tables[i] = IUCore.lootTables.get(captured.getResource());
                System.out.println(this.tile.loot_Tables[i] + " " + captured.getResource());

                this.tile.description_mobs[i] =
                        entityLiving.getName() + "\n" + Localization.translate("iu.show.health") + (int) entityLiving.getHealth() + "/" + (int) entityLiving.getMaxHealth()
                                + "\n" + Localization.translate("iu.show.speed") + (int) this.tile.maxprogress[i];

            } else {
                this.tile.mobUtils[i] = null;
                this.tile.loot_Tables[i] = null;
                this.tile.lootContext[i] = null;
                this.tile.maxprogress[i] = 100;
                this.tile.description_mobs[i] = "";

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
