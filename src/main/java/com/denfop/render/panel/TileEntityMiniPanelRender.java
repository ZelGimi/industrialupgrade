package com.denfop.render.panel;

import com.denfop.Constants;
import com.denfop.api.solar.EnumTypeParts;
import com.denfop.api.solar.ISolarItem;
import com.denfop.tiles.panels.entity.TileEntityMiniPanels;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class TileEntityMiniPanelRender<T extends TileEntityMiniPanels> extends TileEntitySpecialRenderer<T> {

    private static final Map<Integer, ModelBase> panelModels = new HashMap<>();
    private static final Map<Integer, ModelBase> bottomModels = new HashMap<>();
    private static final ResourceLocation bottomTextures = new ResourceLocation(
            Constants.TEXTURES,
            "textures/blocks/admsp_bottom.png"
    );
    private static final ModelBase bonusPanel = new ModelMiniPanelGlass(10);
    private static final ModelBase bonusBottom = new BottomModel(10);

    public void render(
            @Nonnull TileEntityMiniPanels te,
            double x,
            double y,
            double z,
            float partialTicks,
            int destroyStage,
            float alpha
    ) {

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        if (te.getBonus(EnumTypeParts.GENERATION) == 0) {
            for (int i = 0; i < 9; i++) {
                if (te.invSlotGlass.get(i).isEmpty()) {
                    continue;
                }

                ModelBase model = panelModels.get(i);
                if (model == null) {
                    model = new ModelMiniPanelGlass(i);
                    panelModels.put(i, model);
                }
                ModelBase model1 = bottomModels.get(i);
                if (model1 == null) {
                    model1 = new BottomModel(i);
                    bottomModels.put(i, model1);
                }
                this.bindTexture(((ISolarItem) te.invSlotGlass.get(i).getItem()).getResourceLocation(te.invSlotGlass
                        .get(i)
                        .getItemDamage()));
                model.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1F);
                this.bindTexture(bottomTextures);
                model1.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1F);

            }
        } else {
            this.bindTexture(((ISolarItem) te.invSlotGlass.get(0).getItem()).getResourceLocation(te.invSlotGlass
                    .get(0)
                    .getItemDamage()));
            bonusPanel.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1F);
            this.bindTexture(bottomTextures);
            bonusBottom.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1F);

        }
        GlStateManager.popMatrix();

    }

}
