package com.denfop.integration.jei.rubbertree;

import com.denfop.IUItem;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RubberTreeWrapper implements IRecipeWrapper {


    private final Map<BlockPos, IBlockState> map;
    int rotare = 0;


    public RubberTreeWrapper(RubberTreeHandler container) {


        this.map = container.map;

    }

    public List<ItemStack> getInputs() {
        List<ItemStack> list = new ArrayList<>();
        List<IBlockState> blockStates = new ArrayList<>();
        map.forEach((pos, state) -> {
            if (!blockStates.contains(state)) {
                list.add(new ItemStack(state.getBlock()));
                blockStates.add(state);
            }
        });
        return list;
    }

    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputs(VanillaTypes.ITEM, this.getInputs());
        ingredients.setOutput(VanillaTypes.ITEM, new ItemStack(IUItem.rawLatex));
    }

    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        minecraft.fontRenderer.drawSplitString("->", 165, 60,
                recipeWidth - 5, 4210752
        );
        minecraft.fontRenderer.drawSplitString("+", 30, 60,
                recipeWidth - 5, 4210752
        );

        if (minecraft.player.world != null) {
            if (minecraft.player.world.provider.getWorldTime() % 10 == 0) {
                rotare++;
            }
            if (rotare >= 180) {
                rotare = -180;
            }
        }
        int length = 5;
        int width = 4;
        int height = 10;

        GlStateManager.pushMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableRescaleNormal();


        float maxDimension = Math.max(Math.max(width, height), length);
        float guiMaxDimension = 300;
        float scale = (guiMaxDimension / maxDimension) / 2f;


        GL11.glTranslatef(130, 40, 100);
        GlStateManager.rotate(45, 0f, 1.0F, 0.0F);
        GlStateManager.scale(scale, -scale, scale);

        BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        Map<BlockPos, IBlockState> blocks = map;
        for (Map.Entry<BlockPos, IBlockState> entry : blocks.entrySet()) {
            BlockPos pos = entry.getKey();

            IBlockState state = entry.getValue();
            GlStateManager.pushMatrix();
            GlStateManager.translate((pos.getX() - width / 2), (pos.getY() - height / 2), (pos.getZ() - length / 2));
            IBakedModel model = blockRenderer.getModelForState(state);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();
            buffer.begin(7, DefaultVertexFormats.BLOCK);
            blockRenderer.getBlockModelRenderer().renderModel(minecraft.player.world, model, state, BlockPos.ORIGIN, buffer,
                    false
            );
            buffer.setTranslation(0, 0, 0);
            tessellator.draw();
            GlStateManager.popMatrix();
        }

        GlStateManager.popMatrix();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
    }

}
