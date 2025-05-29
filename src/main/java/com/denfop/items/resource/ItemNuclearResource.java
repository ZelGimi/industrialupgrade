package com.denfop.items.resource;

import com.denfop.IUCore;
import com.denfop.IUPotion;
import com.denfop.api.item.IHazmatLike;
import com.denfop.blocks.ISubEnum;
import com.denfop.items.ItemMain;
import com.denfop.items.reactors.IRadioactiveItemType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemNuclearResource<T extends Enum<T> & ISubEnum> extends ItemMain<T> {
    public ItemNuclearResource(T element) {
        super(new Item.Properties(), element);
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.ReactorsTab;
    }
    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotIndex, boolean isCurrentItem) {
        if (entity instanceof LivingEntity) {
            LivingEntity entityLiving = (LivingEntity) entity;
            if (!IHazmatLike.hasCompleteHazmat(entityLiving)) {
                IUPotion.radiation.applyEffect(entityLiving, ((Types) getElement()).getRadiationDuration());
            }
        }
    }

    public enum Types implements ISubEnum, IRadioactiveItemType {

        uranium(0, 60, 100),
        uranium_235(1, 150, 100),
        uranium_238(2, 10, 90),
        plutonium(3, 150, 100),
        mox(4, 300, 100),
        small_uranium_235(5, 150, 100),
        small_uranium_238(6, 10, 90),
        small_plutonium(7, 150, 100),
        uranium_pellet(8, 60, 100),
        mox_pellet(9, 300, 100),
        americium_dust(10, 60, 100),
        neptunium_dust(11, 60, 100),
        curium_dust(12, 60, 100),
        americium_smalldust(13, 60, 100),
        neptunium_smalldust(14, 60, 100),
        curium_smalldust(15, 60, 100),
        thorium_shard(16, 60, 100),
        unprocessed_radioactive_americium(17, 60, 100),
        unprocessed_radioactive_neptunium(18, 60, 100),
        unprocessed_radioactive_thorium(19, 60, 100),
        unprocessed_radioactive_curium(20, 60, 100),
        unprocessed_radioactive_uranium(21, 60, 100),

        ;
        private final int id;
        private final int radLen;
        private final int radAmplifier;

        Types(int id, int radLen, int radAmplifier) {
            this.id = id;
            this.radLen = radLen;
            this.radAmplifier = radAmplifier;
        }

        public static Types getFromID(final int ID) {
            return values()[ID % values().length];
        }

        public String getName() {
            return this.name();
        }

        @Override
        public String getMainPath() {
            return "nuclearresource";
        }

        public int getId() {
            return this.id;
        }

        public int getRadiationDuration() {
            return this.radLen;
        }

        public int getRadiationAmplifier() {
            return this.radAmplifier;
        }

    }
}
