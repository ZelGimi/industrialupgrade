package com.denfop.effects;

import com.denfop.api.windsystem.WindSystem;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class SteamAshParticle extends AshParticle {
    private final float smokeParticleScale;

    protected SteamAshParticle(ClientLevel level, double x, double y, double z, double dx, double dy, double dz, SpriteSet sprites) {
        super(level, x + 0.5, y, z + 0.5, dx, dy, dz,1.5f, sprites);
        this.lifetime = 20 + this.random.nextInt(10);
        this.hasPhysics = false;
        this.setSpriteFromAge(sprites);
        this.smokeParticleScale = this.quadSize;
    }

    @Override
    public void render(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {
        Vec3 wind = WindSystem.windSystem.getWindSide().getDirectionVector();
        float speed = (float) (WindSystem.windSystem.getSpeed() / 32f);
        this.xd += wind.x * speed * 0.0625;
        this.zd += wind.z * speed * 0.0625;
        this.setColor(1,1,1);
        float lifeFrac = (float) this.age / (float) this.lifetime;
        float f = ((float) this.age + pPartialTicks) / (float) this.lifetime * 32.0F;
        f = Mth.clamp(f, 0.0F, 1.0F);
        this.quadSize = f*smokeParticleScale;
        RenderSystem.setShaderColor(1,1,1,1);
        super.render(pBuffer, pRenderInfo, pPartialTicks);
    }


    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;
        public Factory(SpriteSet sprites) {
            this.sprites = sprites;
        }
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel world, double x, double y, double z, double dx, double dy, double dz) {
            return new SteamAshParticle(world, x, y, z, dx, dy, dz, sprites);
        }
    }

}
