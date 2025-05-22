package com.denfop.effects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ParticleItemCustom extends Particle {

    private final ItemStack stack;
    private final TextureManager textureManager;
    private final TextureAtlasSprite sprite;

    public ParticleItemCustom(
            World world,
            double x,
            double y,
            double z,
            double motionX,
            double motionY,
            double motionZ,
            ItemStack stack
    ) {
        super(world, x, y, z, motionX, motionY, motionZ);
        this.stack = stack;
        this.textureManager = Minecraft.getMinecraft().getTextureManager();
        RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();

        this.sprite = itemRenderer.getItemModelMesher().getParticleIcon(stack.getItem(), stack.getItemDamage());

        this.setParticleTexture(sprite);

        this.particleGravity = 1F;
        this.particleMaxAge = 20 + world.rand.nextInt(10);
    }

    public int getFXLayer() {
        return 1;
    }

    public void renderParticle(
            BufferBuilder buffer,
            Entity entityIn,
            float partialTicks,
            float rotationX,
            float rotationZ,
            float rotationYZ,
            float rotationXY,
            float rotationXZ
    ) {

        float f = ((float) this.particleTextureIndexX + this.particleTextureJitterX / 4.0F) / 16.0F;
        float f1 = f + 0.015609375F;
        float f2 = ((float) this.particleTextureIndexY + this.particleTextureJitterY / 4.0F) / 16.0F;
        float f3 = f2 + 0.015609375F;
        float f4 = 0.1F * this.particleScale;

        if (this.particleTexture != null) {
            f = this.particleTexture.getInterpolatedU((double) (this.particleTextureJitterX / 4.0F * 16.0F));
            f1 = this.particleTexture.getInterpolatedU((double) ((this.particleTextureJitterX + 1.0F) / 4.0F * 16.0F));
            f2 = this.particleTexture.getInterpolatedV((double) (this.particleTextureJitterY / 4.0F * 16.0F));
            f3 = this.particleTexture.getInterpolatedV((double) ((this.particleTextureJitterY + 1.0F) / 4.0F * 16.0F));
        }

        float f5 = (float) (this.prevPosX + (this.posX - this.prevPosX) * (double) partialTicks - interpPosX);
        float f6 = (float) (this.prevPosY + (this.posY - this.prevPosY) * (double) partialTicks - interpPosY);
        float f7 = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * (double) partialTicks - interpPosZ);
        int i = this.getBrightnessForRender(partialTicks);
        int j = i >> 16 & 65535;
        int k = i & 65535;
        buffer
                .pos(
                        (double) (f5 - rotationX * f4 - rotationXY * f4),
                        (double) (f6 - rotationZ * f4),
                        (double) (f7 - rotationYZ * f4 - rotationXZ * f4)
                )
                .tex((double) f, (double) f3)
                .color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F)
                .lightmap(j, k)
                .endVertex();
        buffer
                .pos(
                        (double) (f5 - rotationX * f4 + rotationXY * f4),
                        (double) (f6 + rotationZ * f4),
                        (double) (f7 - rotationYZ * f4 + rotationXZ * f4)
                )
                .tex((double) f, (double) f2)
                .color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F)
                .lightmap(j, k)
                .endVertex();
        buffer
                .pos(
                        (double) (f5 + rotationX * f4 + rotationXY * f4),
                        (double) (f6 + rotationZ * f4),
                        (double) (f7 + rotationYZ * f4 + rotationXZ * f4)
                )
                .tex((double) f1, (double) f2)
                .color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F)
                .lightmap(j, k)
                .endVertex();
        buffer
                .pos(
                        (double) (f5 + rotationX * f4 - rotationXY * f4),
                        (double) (f6 - rotationZ * f4),
                        (double) (f7 + rotationYZ * f4 - rotationXZ * f4)
                )
                .tex((double) f1, (double) f3)
                .color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F)
                .lightmap(j, k)
                .endVertex();
    }

}
