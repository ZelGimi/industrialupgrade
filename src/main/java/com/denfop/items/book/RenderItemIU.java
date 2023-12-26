package com.denfop.items.book;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;

public class RenderItemIU extends RenderItem {

    public RenderItemIU() {
        super(
                Minecraft.getMinecraft().getTextureManager(),
                Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager(),
                Minecraft.getMinecraft().getItemColors()
        );
    }

    public void setupGuiTransform(int xPosition, int yPosition, boolean isGui3d) {
        GlStateManager.translate((float) xPosition, (float) yPosition, 100.0F + this.zLevel);
        GlStateManager.translate(8.0F, 8.0F, 0.0F);


        GlStateManager.scale(1.0F, -1.0F, 1.0F);
        GlStateManager.scale(32, 32, 32);

        if (isGui3d) {
            GlStateManager.enableLighting();
        } else {
            GlStateManager.disableLighting();
        }
    }


}
