package com.denfop.inventory;


import com.denfop.IUCore;
import com.denfop.blockentity.base.BlockEntityAutoSpawner;
import com.denfop.items.modules.ItemEntityModule;
import com.denfop.utils.CapturedMobUtils;
import com.denfop.utils.Localization;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class InventoryModules extends Inventory {

    private final BlockEntityAutoSpawner tile;
    private int stackSizeLimit;

    public InventoryModules(BlockEntityAutoSpawner base1) {
        super(base1, TypeItemSlot.INPUT, 4);
        this.tile = base1;
        this.stackSizeLimit = 1;
    }

    public boolean canPlaceItem(final int index, ItemStack itemStack) {
        if (!(itemStack.getItem() instanceof ItemEntityModule)) {
            return false;
        }
        if (((ItemEntityModule<?>) itemStack.getItem()).getElement().getId() == 0) {
            return false;
        }


        return CapturedMobUtils.containsSoul(itemStack);
    }

    public void update() {
        for (int i = 0; i < this.size(); i++) {
            if (!this.get(i).isEmpty()) {
                final CapturedMobUtils captured = CapturedMobUtils.create(this.get(i));
                assert captured != null;
                LivingEntity entityLiving = (LivingEntity) captured.getEntity(tile.getWorld(), true);
                this.tile.mobUtils[i] = entityLiving;
                this.tile.loot_Tables[i] = IUCore.lootTables.get(captured.getResource());
                this.tile.maxprogress[i] = 100 * captured.getCoefficient();
                this.tile.description_mobs[i] =
                        entityLiving.getName().getString() + "\n" + Localization.translate("iu.show.health") + (int) entityLiving.getHealth() + "/" + (int) entityLiving.getMaxHealth()
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
    public ItemStack set(final int index, final ItemStack content) {
        super.set(index, content);

        if (!content.isEmpty()) {
            this.tile.mobUtils[index] = null;
            this.tile.loot_Tables[index] = null;
            this.tile.lootContext[index] = null;
            this.tile.maxprogress[index] = 100;
            this.tile.description_mobs[index] = "";
            final CapturedMobUtils captured = CapturedMobUtils.create(content);
            assert captured != null;
            LivingEntity entityLiving = (LivingEntity) captured.getEntity(tile.getWorld(), true);

            this.tile.mobUtils[index] = entityLiving;
            this.tile.maxprogress[index] = 100 * captured.getCoefficient();
            this.tile.loot_Tables[index] = IUCore.lootTables.get(captured.getResource());
            this.tile.description_mobs[index] =
                    entityLiving.getName().getString() + "\n" + Localization.translate("iu.show.health") + (int) entityLiving.getHealth() + "/" + (int) entityLiving.getMaxHealth()
                            + "\n" + Localization.translate("iu.show.speed") + (int) this.tile.maxprogress[index];

        } else {
            this.tile.mobUtils[index] = null;
            this.tile.loot_Tables[index] = null;
            this.tile.lootContext[index] = null;
            this.tile.maxprogress[index] = 100;
            this.tile.description_mobs[index] = "";

        }
        return content;
    }

    public int getStackSizeLimit() {
        return this.stackSizeLimit;
    }

    public void setStackSizeLimit(int stackSizeLimit) {
        this.stackSizeLimit = stackSizeLimit;
    }

}
