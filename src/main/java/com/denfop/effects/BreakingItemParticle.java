package com.denfop.effects;

import com.denfop.blockentity.base.BlockEntityAnvil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BreakingItemParticle extends TextureSheetParticle {
    private final float uo;
    private final float vo;

    BreakingItemParticle(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, ItemStack pStack) {
        this(pLevel, pX, pY, pZ, pStack);
        this.xd *= (double) 0.1F;
        this.yd *= (double) 0.1F;
        this.zd *= (double) 0.1F;
        this.xd += pXSpeed;
        this.yd += pYSpeed;
        this.zd += pZSpeed;
    }

    protected BreakingItemParticle(ClientLevel pLevel, double pX, double pY, double pZ, ItemStack pStack) {
        super(pLevel, pX, pY, pZ, 0.0D, 0.0D, 0.0D);
        var model = Minecraft.getInstance().getItemRenderer().getModel(pStack, pLevel, (LivingEntity) null, 0);
        this.setSprite(model.getOverrides().resolve(model, pStack, pLevel, null, 0).getParticleIcon(net.minecraftforge.client.model.data.ModelData.EMPTY));
        this.gravity = 1.0F;
        this.quadSize /= 1.0F;
        this.uo = this.random.nextFloat() * 3.0F;
        this.vo = this.random.nextFloat() * 3.0F;
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.TERRAIN_SHEET;
    }

    protected float getU0() {
        return this.sprite.getU((double) ((this.uo + 1.0F) / 4.0F * 16.0F));
    }

    protected float getU1() {
        return this.sprite.getU((double) (this.uo / 4.0F * 16.0F));
    }

    protected float getV0() {
        return this.sprite.getV((double) (this.vo / 4.0F * 16.0F));
    }

    protected float getV1() {
        return this.sprite.getV((double) ((this.vo + 1.0F) / 4.0F * 16.0F));
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<ItemParticleOption> {
        public Particle createParticle(ItemParticleOption pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new BreakingItemParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, pType.getItem());
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class AnvilProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public AnvilProvider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            BlockEntity block = pLevel.getBlockEntity(new BlockPos((int) pX, (int) (pY - 1), (int) pZ));
            ItemStack stack = ItemStack.EMPTY;
            if (block instanceof BlockEntityAnvil)
                stack = ((BlockEntityAnvil) block).inputSlotA.get(0);
            return new BreakingItemParticle(pLevel, pX, pY, pZ, stack);
        }
    }


}
