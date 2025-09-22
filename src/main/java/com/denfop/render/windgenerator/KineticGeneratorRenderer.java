package com.denfop.render.windgenerator;

import com.denfop.api.windsystem.IWindMechanism;
import com.denfop.tiles.mechanism.wind.TileWindGenerator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class KineticGeneratorRenderer extends TileEntitySpecialRenderer<TileWindGenerator> {

    private static final Map<Integer, ModelBase> rotorModels = new HashMap<>();

    public KineticGeneratorRenderer() {
    }

    protected void renderBlockRotor(IWindMechanism windGen, World world, BlockPos pos) {
        int diameter = windGen.getRotorDiameter();

        if (diameter != 0) {
            float angle = windGen.getAngle();
            ResourceLocation rotorRL = windGen.getRotorRenderTexture();
            ModelBase model = rotorModels.get(diameter);
            if (model == null) {
                model = new KineticGeneratorRotor(diameter);
                rotorModels.put(diameter, model);
            }

            EnumFacing facing = windGen.getFacing();
            pos = pos.offset(facing);
            int light = world.getCombinedLight(pos, 0);
            int blockLight = light % 65536;
            int skyLight = light / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) blockLight, (float) skyLight);
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.5F, 0.5F, 0.5F);
            switch (facing) {
                case NORTH:
                    GlStateManager.translate(0F, 0F, -0.25F);
                    break;
                case EAST:
                    GlStateManager.translate(0.25F, 0F, 0);
                    break;
                case SOUTH:
                    GlStateManager.translate(0F, 0F, 0.25F);
                    break;
                case WEST:
                    GlStateManager.translate(-0.25F, 0F, 0);
                    break;
            }
            switch (facing) {
                case NORTH:
                    GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
                    break;
                case EAST:
                    GL11.glRotatef(-180.0F, 0.0F, 1.0F, 0.0F);
                    break;
                case SOUTH:
                    GL11.glRotatef(-270.0F, 0.0F, 1.0F, 0.0F);
                    break;
                case UP:
                    GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
            }
            if (windGen.getSpace()) {
                if (!Minecraft.getMinecraft().isGamePaused()) {
                    GlStateManager.rotate(angle, 1.0F, 0.0F, 0.0F);
                }
            }
            GlStateManager.translate(-0.2F, 0.0F, 0.0F);
            this.bindTexture(rotorRL);
            model.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
            GlStateManager.popMatrix();
        }
    }

    public void render(
            @Nonnull TileWindGenerator te,
            double x,
            double y,
            double z,
            float partialTicks,
            int destroyStage,
            float alpha
    ) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x, (float) y, (float) z);
        this.renderBlockRotor(te, te.getWorld(), te.getBlockPos());

        GL11.glPopMatrix();
    }

}
