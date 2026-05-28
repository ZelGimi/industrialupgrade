package com.denfop.blocks;

import com.denfop.IUItem;
import com.denfop.api.item.armor.HazmatLike;
import com.denfop.dataregistry.DataBlock;
import com.denfop.potion.IUPotion;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RadiationDustBlock<T extends Enum<T> & ISubEnum> extends AbstractLayerDustBlock<T> {

    public RadiationDustBlock(
            T[] elements,
            T element,
            DataBlock<T, ? extends BlockCore<T>, ? extends ItemBlockCore<T>> dataBlock
    ) {
        super(
                Properties.of().mapColor(MapColor.COLOR_LIGHT_GREEN),
                elements,
                element,
                dataBlock
        );
    }

    @Override
    public List<ItemStack> getDrops(@Nonnull final Level world,
                                    @Nonnull final BlockPos pos,
                                    @Nonnull final BlockState state,
                                    final int fortune) {
        List<ItemStack> drops = new ArrayList<>();
        drops.add(new ItemStack(IUItem.crafting_elements.getStack(788)));
        return drops;
    }

    @Override
    protected void applyDustEffects(net.minecraft.world.level.block.state.BlockState state, Level level, net.minecraft.core.BlockPos pos, LivingEntity living, int layers) {
        if (living instanceof Player player) {
            if (player.isCreative() || player.isSpectator()) {
                return;
            }

            if (HazmatLike.hasCompleteHazmat(player)) {
                return;
            }

            double currentRadiation = player.getPersistentData().getDouble("radiation");
            double addRadiation = 0.05D + (layers * 0.08D);

            if (living.tickCount % Math.max(6, 24 - layers * 2) == 0) {
                player.getPersistentData().putDouble("radiation", currentRadiation + addRadiation);
            }
        }

        int interval = Math.max(6, 24 - layers * 2);
        if (living.tickCount % interval != 0) {
            return;
        }

        living.addEffect(new MobEffectInstance(IUPotion.rad, 120, 0));

        if (layers >= 4) {
            living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 0));
        }

        if (layers >= 6) {
            living.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 80, 0));
        }

        if (layers >= 7) {
            living.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 80, 0));
        }

        if (layers >= 8 && living.tickCount % 20 == 0) {
            living.hurt(level.damageSources().magic(), 1.0F);
        }
    }

    @Override
    protected net.minecraft.core.particles.ParticleOptions getAmbientParticle() {
        return ParticleTypes.SPORE_BLOSSOM_AIR;
    }

    @Override
    public int getTopTint() {
        return 0x6F8E43;
    }

    @Override
    public int getSideTint() {
        return 0x587132;
    }

    @Override
    public <E extends Enum<E> & ISubEnum> void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items, E element) {
        items.add(new ItemStack(this));
    }

    public enum Type implements ISubEnum {
        radiation_dust(0);

        private final int metadata;
        private final String name;

        Type(int metadata) {
            this.metadata = metadata;
            this.name = this.name().toLowerCase(Locale.US);
        }

        public static Type getFromID(final int id) {
            return values()[id % values().length];
        }

        @Override
        public int getId() {
            return this.metadata;
        }

        @Override
        public String getOtherPart() {
            return "type=";
        }

        @Nonnull
        public String getName() {
            return this.name;
        }

        @Override
        public String getMainPath() {
            return "radiation_dust";
        }

        public int getLight() {
            return 0;
        }
    }
}