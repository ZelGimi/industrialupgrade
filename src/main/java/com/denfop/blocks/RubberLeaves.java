package com.denfop.blocks;

import com.denfop.IUItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.Tags;

import java.util.List;

import static com.denfop.utils.ParticleUtils.spawnFallingLeavesParticles;

public class RubberLeaves extends LeavesBlock {
    public RubberLeaves() {
        super(Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn((S, W, P, E) -> E == EntityType.OCELOT || E == EntityType.PARROT).isSuffocating((K, V, E) -> false).isViewBlocking((K, V, E) -> false));
    }

    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 30;
    }

    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 60;
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        super.tick(pState, pLevel, pPos, pRandom);
        spawnFallingLeavesParticles(pLevel, pPos, this);
        pLevel.scheduleTick(pPos, this, pRandom.nextInt(200) + 100);
    }

    @OnlyIn(Dist.CLIENT)
    public RenderType getType() {
        return Minecraft.useFancyGraphics() ? RenderType.cutoutMipped() : RenderType.solid();
    }

    public List<ItemStack> getDrops(BlockState p_60537_, LootContext.Builder p_60538_) {
        if (!p_60538_.getLevel().isClientSide) {
            ItemStack stack = p_60538_.getParameter(LootContextParams.TOOL);
            if (stack.is(Tags.Items.SHEARS))
                return List.of(new ItemStack(IUItem.leaves.getItem()));
            if (p_60538_.getLevel().random.nextInt(20) == 0) {
                return List.of(new ItemStack(IUItem.rubberSapling.getItem()));
            }
        }
        return super.getDrops(p_60537_, p_60538_);
    }
}
