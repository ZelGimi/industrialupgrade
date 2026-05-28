package com.denfop.blocks;

import com.denfop.IUItem;
import com.denfop.dataregistry.DataBlock;
import com.denfop.potion.IUPotion;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SootBlock<T extends Enum<T> & ISubEnum> extends AbstractLayerDustBlock<T> {

    public SootBlock(
            T[] elements,
            T element,
            DataBlock<T, ? extends BlockCore<T>, ? extends ItemBlockCore<T>> dataBlock
    ) {
        super(
                Properties.of(Material.PLANT).color(MaterialColor.COLOR_BLACK),
                elements,
                element,
                dataBlock
        );
    }

    @Override
    protected void applyDustEffects(net.minecraft.world.level.block.state.BlockState state, Level level, net.minecraft.core.BlockPos pos, LivingEntity living, int layers) {
        if (living instanceof Player player && (player.isCreative() || player.isSpectator())) {
            return;
        }

        int interval = Math.max(8, 28 - layers * 2);
        if (living.tickCount % interval != 0) {
            return;
        }

        living.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 0));

        if (layers >= 4) {
            living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 0));
        }

        if (layers >= 5) {
            living.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 80, 0));
        }

        if (layers >= 7) {
            living.addEffect(new MobEffectInstance(IUPotion.poison_gas, 80, 0));
        }

        if (layers >= 8 && living.tickCount % 20 == 0) {
            living.hurt(DamageSource.IN_WALL, 1.0F);
        }
    }

    @Override
    public List<ItemStack> getDrops(@Nonnull final Level world,
                                    @Nonnull final BlockPos pos,
                                    @Nonnull final BlockState state,
                                    final int fortune) {
        List<ItemStack> drops = new ArrayList<>();
        drops.add(new ItemStack(IUItem.crafting_elements.getStack(789)));
        return drops;
    }

    @Override
    protected net.minecraft.core.particles.ParticleOptions getAmbientParticle() {
        return ParticleTypes.ASH;
    }

    @Override
    public int getTopTint() {
        return 0x4A4A4A;
    }

    @Override
    public int getSideTint() {
        return 0x323232;
    }

    @Override
    public <E extends Enum<E> & ISubEnum> void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items, E element) {
        items.add(new ItemStack(this.stateDefinition.any().getBlock()));
    }


    public enum Type implements ISubEnum {
        soot(0);

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
            return "soot";
        }

        public int getLight() {
            return 0;
        }
    }
}