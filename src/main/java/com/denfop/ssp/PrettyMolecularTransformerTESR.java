package com.denfop.ssp;

import com.denfop.ssp.tiles.TileEntityMolecularAssembler;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@SideOnly(Side.CLIENT)
public class PrettyMolecularTransformerTESR extends TileEntitySpecialRenderer<TileEntityMolecularAssembler> {
	public static final PrettyMolecularTransformerModel model = new PrettyMolecularTransformerModel();
	public static final boolean drawActiveCore = false;
	private static final ResourceLocation transfTextloc = new ResourceLocation("super_solar_panels", "textures/models/textureMolecularTransformer.png");
	private static final ResourceLocation plazmaTextloc = new ResourceLocation("super_solar_panels", "textures/models/plazma.png");
	private static final ResourceLocation particlesTextloc = new ResourceLocation("super_solar_panels", "textures/models/particles.png");
	private static final TObjectIntMap<List<Serializable>> textureSizeCache = (TObjectIntMap<List<Serializable>>) new TObjectIntHashMap();
	private static final IResourceManager resources = Minecraft.getMinecraft().getResourceManager();
	public int ticker;

	protected int getTileLighting(TileEntity tile, int lightValue) {
		if (tile == null || !tile.hasWorld()) {
			int blockLight = EnumSkyBlock.BLOCK.defaultLightValue;
			if (blockLight < lightValue)
				blockLight = lightValue;
			return EnumSkyBlock.SKY.defaultLightValue << 20 | blockLight << 4;
		}
		return tile.getWorld().getCombinedLight(tile.getPos(), lightValue);
	}

	public void render(TileEntityMolecularAssembler tileTransformer, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
		if (destroyStage >= 0) {
			bindTexture(DESTROY_STAGES[destroyStage]);
			GlStateManager.matrixMode(5890);
			GlStateManager.pushMatrix();
			GlStateManager.scale(4.0F, 4.0F, 1.0F);
			GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
			GlStateManager.matrixMode(5888);
		}
		GlStateManager.pushMatrix();
		GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
		bindTexture(transfTextloc);
		model.render(null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		GlStateManager.popMatrix();
		if (destroyStage >= 0) {
			GlStateManager.matrixMode(5890);
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(5888);
		}
		GlStateManager.popMatrix();
		if (tileTransformer != null && drawActiveCore && tileTransformer.getActive()) {
			GL11.glPushMatrix();
			GlStateManager.pushAttrib();
			renderCore(tileTransformer, x, y, z);
			GlStateManager.popAttrib();
			GL11.glPopMatrix();
		}
	}

	public void renderCore(TileEntity te, double x, double y, double z) {
		this.ticker++;
		if (this.ticker > 160)
			this.ticker = 0;
		int plazmaSize = getTextureSize(plazmaTextloc.getResourcePath(), 64);
		int particleSize = getTextureSize(particlesTextloc.getResourcePath(), 32);
		float rotationX = ActiveRenderInfo.getRotationX();
		float rotationXZ = ActiveRenderInfo.getRotationXZ();
		float rotationZ = ActiveRenderInfo.getRotationZ();
		float rotationYZ = ActiveRenderInfo.getRotationYZ();
		float rotationXY = ActiveRenderInfo.getRotationXY();
		float scaleCore = 0.35F;
		float posX = (float) x + 0.5F;
		float posY = (float) y + 0.5F;
		float posZ = (float) z + 0.5F;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		Color colour = new Color(12648447);
		GL11.glPushMatrix();
		GL11.glDepthMask(false);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 1);
		bindTexture(plazmaTextloc);
		int phase = this.ticker % 16;
		float quadPlazmaSize = (plazmaSize * 4);
		float plasmaEdge = plazmaSize - 0.01F;
		float xBottom = ((phase % 4 * plazmaSize) + 0.0F) / quadPlazmaSize;
		float xTop = ((phase % 4 * plazmaSize) + plasmaEdge) / quadPlazmaSize;
		float yBottom = ((phase / 4 * plazmaSize) + 0.0F) / quadPlazmaSize;
		float yTop = ((phase / 4 * plazmaSize) + plasmaEdge) / quadPlazmaSize;
		buffer.begin(7, DefaultVertexFormats.BLOCK);
		GL11.glColor4f(colour.getRed() / 255.0F, colour.getGreen() / 255.0F, colour.getBlue() / 255.0F, 1.0F);
		buffer.pos((posX - rotationX * scaleCore - rotationYZ * scaleCore), (posY - rotationXZ * scaleCore), (posZ - rotationZ * scaleCore - rotationXY * scaleCore)).tex(xTop, yTop)
				.endVertex();
		buffer.pos((posX - rotationX * scaleCore + rotationYZ * scaleCore), (posY + rotationXZ * scaleCore), (posZ - rotationZ * scaleCore + rotationXY * scaleCore)).tex(xTop, yBottom)
				.endVertex();
		buffer.pos((posX + rotationX * scaleCore + rotationYZ * scaleCore), (posY + rotationXZ * scaleCore), (posZ + rotationZ * scaleCore + rotationXY * scaleCore)).tex(xBottom, yBottom)
				.endVertex();
		buffer.pos((posX + rotationX * scaleCore - rotationYZ * scaleCore), (posY - rotationXZ * scaleCore), (posZ + rotationZ * scaleCore - rotationXY * scaleCore)).tex(xBottom, yTop)
				.endVertex();
		tessellator.draw();
		GL11.glDisable(3042);
		GL11.glDepthMask(true);
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		GL11.glDepthMask(false);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 1);
		bindTexture(particlesTextloc);
		phase += 24;
		float octParticleSize = (particleSize * 8);
		plasmaEdge = particleSize - 0.01F;
		xBottom = ((phase % 8 * particleSize) + 0.0F) / octParticleSize;
		xTop = ((phase % 8 * particleSize) + plasmaEdge) / octParticleSize;
		yBottom = ((phase / 8 * particleSize) + 0.0F) / octParticleSize;
		yTop = ((phase / 8 * particleSize) + plasmaEdge) / octParticleSize;
		scaleCore = 0.4F + MathHelper.sin(this.ticker / 10.0F) * 0.1F;
		buffer.begin(7, DefaultVertexFormats.BLOCK);
		GlStateManager.disableLighting();
		buffer.pos((posX - rotationX * scaleCore - rotationYZ * scaleCore), (posY - rotationXZ * scaleCore), (posZ - rotationZ * scaleCore - rotationXY * scaleCore)).tex(xTop, yTop)
				.color(255, 255, 255, 255).endVertex();
		buffer.pos((posX - rotationX * scaleCore + rotationYZ * scaleCore), (posY + rotationXZ * scaleCore), (posZ - rotationZ * scaleCore + rotationXY * scaleCore)).tex(xTop, yBottom)
				.color(255, 255, 255, 255).endVertex();
		buffer.pos((posX + rotationX * scaleCore + rotationYZ * scaleCore), (posY + rotationXZ * scaleCore), (posZ + rotationZ * scaleCore + rotationXY * scaleCore)).tex(xBottom, yBottom)
				.color(255, 255, 255, 255).endVertex();
		buffer.pos((posX + rotationX * scaleCore - rotationYZ * scaleCore), (posY - rotationXZ * scaleCore), (posZ + rotationZ * scaleCore - rotationXY * scaleCore)).tex(xBottom, yTop)
				.color(255, 255, 255, 255).endVertex();
		GlStateManager.enableLighting();
		tessellator.draw();
		GL11.glDisable(3042);
		GL11.glDepthMask(true);
		GL11.glPopMatrix();
	}

	public static int getTextureSize(String s, int dv) {
		if (textureSizeCache.containsKey(Arrays.asList(s, dv)))
			return textureSizeCache.get(Arrays.asList(s, dv));
		try {
			InputStream inputstream = resources.getResource(new ResourceLocation("advanced_solar_panels", s)).getInputStream();
			if (inputstream == null)
				throw new FileNotFoundException("Image not found: " + s);
			int size = ImageIO.read(inputstream).getWidth() / dv;
			textureSizeCache.put(Arrays.asList(new Serializable[]{s, dv}), size);
			return size;
		} catch (Exception e) {
			SuperSolarPanels.log.error("Error getting size of texture " + s + " (" + dv + ')', e);
			return 16;
		}
	}
}
