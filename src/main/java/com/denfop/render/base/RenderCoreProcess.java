package com.denfop.render.base;


import com.denfop.Constants;
import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.blockentity.base.IIsMolecular;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.*;

import static net.minecraft.world.item.ItemDisplayContext.FIXED;

public class RenderCoreProcess<T extends BlockEntityBase & IIsMolecular> implements BlockEntityRenderer<T> {
    private static final Map<List<Serializable>, Integer> textureSizeCache = new HashMap<>();
    private static final ResourceLocation plazmaTextloc = new ResourceLocation(Constants.MOD_ID, "textures/block/plazma.png");
    private static final ResourceLocation particlesTextloc = new ResourceLocation(
            Constants.MOD_ID,
            "textures/block/particles.png"
    );
    public static int size1 = -1;
    public static int size2 = -1;
    private final BlockEntityRendererProvider.Context contex;
    private final Random random = new Random();
    public int ticker;
    float rotation;
    float prevRotation;

    public RenderCoreProcess(BlockEntityRendererProvider.Context p_173636_) {
        this.contex = p_173636_;
    }

    public static int getTextureSize(String s, int dv) {
        Integer textureSize = textureSizeCache.get(Arrays.asList(s, dv));
        if (textureSize != null) {
            return textureSize;
        }
        try {
            InputStream inputstream = Minecraft
                    .getInstance()
                    .getResourceManager()
                    .getResource(new ResourceLocation(Constants.MOD_ID, s))
                    .get().open();
            BufferedImage bi = ImageIO.read(inputstream);
            int size = bi.getWidth() / dv;
            textureSizeCache.put(Arrays.asList(new Serializable[]{s, dv}), size);
            return size;
        } catch (Exception var5) {
            var5.printStackTrace();
            return 16;
        }
    }

    @Override
    public void render(T te, float partialTicks, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int combinedOverlay) {
        if (te.getActive()) {
            renderCore(te, poseStack, bufferSource, packedLight, combinedOverlay);
            renderItem(te, poseStack, bufferSource, packedLight, combinedOverlay);
        }
    }

    public void renderItem(IIsMolecular te, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        ItemStack itemStack = te.getItemStack();
        if (itemStack.isEmpty()) {
            return;
        }

        BakedModel bakedModel = te.getBakedModel();
        if (bakedModel != null) {
            boolean isGui3d = bakedModel.isGui3d();

            poseStack.pushPose();


            int count = transformModelCount(te, poseStack, buffer, Minecraft.getInstance().getPartialTick());

            for (int i = 0; i < count; ++i) {
                poseStack.pushPose();

                if (isGui3d) {
                    if (i > 0) {
                        float xOffset = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                        float yOffset = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                        float zOffset = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                        poseStack.translate(xOffset, yOffset, zOffset);
                    }
                } else {
                    if (i > 0) {
                        float xOffset = (random.nextFloat() * 2.0F - 1.0F) * 0.075F;
                        float yOffset = (random.nextFloat() * 2.0F - 1.0F) * 0.075F;
                        poseStack.translate(xOffset, yOffset, 0.0F);
                    }
                }

                BakedModel transformedModel = te.getTransformedModel();
                contex.getItemRenderer().render(itemStack, FIXED, false, poseStack, buffer, light, overlay, transformedModel);
                poseStack.popPose();
            }

            poseStack.popPose();
        }
    }

    private int transformModelCount(
            IIsMolecular itemIn, PoseStack poseStack, MultiBufferSource bufferSource, float partialTicks
    ) {
        ItemStack itemStack = itemIn.getItemStack();
        Item item = itemStack.getItem();

        if (item == Items.AIR) {
            return 0;
        }

        int modelCount = 1;
        float offsetY = 1.0F;


        poseStack.translate(0.5, 0.25F * offsetY + 0.25, 0.5);
        /* if (!(itemIn instanceof TileCrystallize)) {
            poseStack.translate( 0.5, 0.25F * offsetY + 0.2, 0.5);
        } else {
            poseStack.translate( 0.5,  0.25F * offsetY + 0.37, 0.5);
        }*/

        poseStack.scale(0.4F, 0.4F, 0.4F);

        rotation = (prevRotation + (rotation - prevRotation) * (partialTicks)) % 360;
         /* if (itemIn instanceof TileCrystallize) {
            rotation = (prevRotation + (rotation - prevRotation) * partialTicks) % 360;
        }*/
        prevRotation = rotation;
        rotation += 0.15F;

        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));


        return modelCount;
    }

    private void renderCore(T te, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int combinedOverlay) {
        this.ticker++;
        if (ticker % 2 != 0) {
            return;
        }

        if (this.ticker > 161) {
            this.ticker = 1;
        }
        if (size1 == -1) {
            size1 = getTextureSize("textures/models/plazma.png", 64);
        }
        if (size1 == -2) {
            size2 = getTextureSize("textures/models/particles.png", 32);
        }
        Camera camera = contex.getEntityRenderer().camera;


        Quaternionf rotation = camera.rotation();


        float scaleCore = 0.35F;
        Matrix4f matrix = poseStack.last().pose();
        float posX = (float) 0.5F;
        float posY = (float) +0.5F;
        float posZ = (float) +0.5F;
        Color color = new Color(12648447);
        poseStack.pushPose();
        poseStack.scale(scaleCore, scaleCore, scaleCore);


        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        RenderSystem.enableDepthTest();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        RenderSystem.setShaderTexture(0, plazmaTextloc);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        int i = this.ticker % 16;
        float size4 = (size1 * 4);
        float float_sizeMinus0_01 = size1 - 0.01F;
        float x0 = ((i % 4 * size1) + 0.0F) / size4;
        float x1 = ((i % 4 * size1) + float_sizeMinus0_01) / size4;
        float x2 = ((i / 4F * size1) + 0.0F) / size4;
        float x3 = ((i / 4F * size1) + float_sizeMinus0_01) / size4;

        Vec3 lookVec = new Vec3(camera.getLookVector());
        Vec3 upVec = new Vec3(camera.getUpVector());
        Vec3 rightVec = new Vec3(camera.getLeftVector());
        float f1 = (float) rightVec.x;
        float f2 = (float) rightVec.y;
        float f3 = (float) rightVec.z;
        float f4 = (float) upVec.x;
        float f5 = (float) upVec.z;
        buffer
                .vertex(matrix,
                        (float) (posX + rightVec.x * -scaleCore + upVec.x * -scaleCore),
                        (float) (posY + rightVec.y * -scaleCore + upVec.y * -scaleCore),
                        (float) (posZ + rightVec.z * -scaleCore + upVec.z * -scaleCore))
                .uv(x1, x3)
                .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                .endVertex();

        buffer
                .vertex(matrix,
                        (float) (posX + rightVec.x * -scaleCore + upVec.x * scaleCore),
                        (float) (posY + rightVec.y * -scaleCore + upVec.y * scaleCore),
                        (float) (posZ + rightVec.z * -scaleCore + upVec.z * scaleCore))
                .uv(x1, x2)
                .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                .endVertex();

        buffer
                .vertex(matrix,
                        (float) (posX + rightVec.x * scaleCore + upVec.x * scaleCore),
                        (float) (posY + rightVec.y * scaleCore + upVec.y * scaleCore),
                        (float) (posZ + rightVec.z * scaleCore + upVec.z * scaleCore))
                .uv(x0, x2)
                .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                .endVertex();

        buffer
                .vertex(matrix,
                        (float) (posX + rightVec.x * scaleCore + upVec.x * -scaleCore),
                        (float) (posY + rightVec.y * scaleCore + upVec.y * -scaleCore),
                        (float) (posZ + rightVec.z * scaleCore + upVec.z * -scaleCore))
                .uv(x0, x3)
                .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                .endVertex();

        BufferUploader.drawWithShader(buffer.end());
        RenderSystem.disableDepthTest();
        RenderSystem.disableBlend();
        poseStack.popPose();


        poseStack.pushPose();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);


        int qq = this.ticker % 16;
        i = 24 + qq;
        float size8 = (size2 * 8);
        float_sizeMinus0_01 = size2 - 0.01F;
        x0 = ((i % 8 * size2) + 0.0F) / size8;
        x1 = ((i % 8 * size2) + float_sizeMinus0_01) / size8;
        x2 = ((i / 8F * size2) + 0.0F) / size8;
        x3 = ((i / 8F * size2) + float_sizeMinus0_01) / size8;
        float var11 = Mth.sin(this.ticker / 10.0F) * 0.1F;
        scaleCore = 0.4F + var11;
        buffer = tesselator.getBuilder();

        RenderSystem.enableDepthTest();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        RenderSystem.setShaderTexture(0, plazmaTextloc);
        if (te.getMode() != 0) {
            RenderSystem.setShaderTexture(0, new ResourceLocation(
                    Constants.MOD_ID,
                    "textures/models/particles" + te.getMode() + ".png"
            ));
        } else {
            RenderSystem.setShaderTexture(0, particlesTextloc);
        }

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        buffer
                .vertex(matrix,
                        (float) (posX + rightVec.x * -scaleCore + upVec.x * -scaleCore),
                        (float) (posY + rightVec.y * -scaleCore + upVec.y * -scaleCore),
                        (float) (posZ + rightVec.z * -scaleCore + upVec.z * -scaleCore))
                .uv(x1, x3)
                .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                .endVertex();

        buffer
                .vertex(matrix,
                        (float) (posX + rightVec.x * -scaleCore + upVec.x * scaleCore),
                        (float) (posY + rightVec.y * -scaleCore + upVec.y * scaleCore),
                        (float) (posZ + rightVec.z * -scaleCore + upVec.z * scaleCore))
                .uv(x1, x2)
                .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                .endVertex();

        buffer
                .vertex(matrix,
                        (float) (posX + rightVec.x * scaleCore + upVec.x * scaleCore),
                        (float) (posY + rightVec.y * scaleCore + upVec.y * scaleCore),
                        (float) (posZ + rightVec.z * scaleCore + upVec.z * scaleCore))
                .uv(x0, x2)
                .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                .endVertex();

        buffer
                .vertex(matrix,
                        (float) (posX + rightVec.x * scaleCore + upVec.x * -scaleCore),
                        (float) (posY + rightVec.y * scaleCore + upVec.y * -scaleCore),
                        (float) (posZ + rightVec.z * scaleCore + upVec.z * -scaleCore))
                .uv(x0, x3)
                .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                .endVertex();
        BufferUploader.drawWithShader(buffer.end());
        RenderSystem.disableDepthTest();
        RenderSystem.disableBlend();
        poseStack.popPose();

    }
}
