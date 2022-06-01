package com.denfop.events;

import com.denfop.Constants;
import com.denfop.api.research.ResearchSystem;
import com.denfop.api.research.main.BaseLevelSystem;
import com.denfop.api.research.main.EnumLeveling;
import com.denfop.utils.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class ClientTickHandler {

    public static final Minecraft mc = FMLClientHandler.instance().getClient();
    protected static final RenderItem itemRender = mc.getRenderItem();


    public static void onTickRender() {
        final EntityPlayerSP entityClientPlayer = mc.player;
        if (mc.world != null && mc.inGameHasFocus && !mc.gameSettings.showDebugInfo) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPushMatrix();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GlStateManager.disableLighting();
            GlStateManager.enableDepth();
            itemRender.zLevel = 100.0F;
            mc.getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/guilevel.png"));
            BaseLevelSystem system = ResearchSystem.instance.getLevel(entityClientPlayer);
            GL11.glScalef(2f, 2f, 2f);
            mc.ingameGUI.drawString(mc.fontRenderer, system.getLevelPoint(EnumLeveling.BASE) + "",
                    8, 6, ModUtils.convertRGBcolorToInt(44, 192, 224)
            );
            mc.ingameGUI.drawString(mc.fontRenderer, system.getLevelPoint(EnumLeveling.PRACTICE) + "",
                    8, 26, ModUtils.convertRGBcolorToInt(229, 201, 15)
            );
            mc.ingameGUI.drawString(mc.fontRenderer, system.getLevelPoint(EnumLeveling.THEORY) + "",
                    8, 46, ModUtils.convertRGBcolorToInt(6, 254, 197)
            );
            mc.ingameGUI.drawString(mc.fontRenderer, system.getLevelPoint(EnumLeveling.PVP) + "",
                    8, 66, ModUtils.convertRGBcolorToInt(255, 8, 4)
            );
            mc.getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/guilevel.png"));
            GL11.glScalef(0.5f, 0.5f, 0.5f);
            GL11.glScalef(0.5f, 0.5f, 0.5f);

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            mc.ingameGUI.drawTexturedModalRect(0, 4, 0, 51, 240, 76);
            double bar = system.getBar(EnumLeveling.BASE, 132);
            mc.ingameGUI.drawTexturedModalRect(97, 29, 97, 155, (int) bar, 26);
            mc.getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/guilevel1.png"));
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            mc.ingameGUI.drawTexturedModalRect(0, 84, 0, 51, 240, 76);
            bar = system.getBar(EnumLeveling.PRACTICE, 132);
            mc.ingameGUI.drawTexturedModalRect(97, 109, 97, 155, (int) bar, 26);

            mc.getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/guilevel3.png"));
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            mc.ingameGUI.drawTexturedModalRect(0, 164, 0, 51, 240, 76);
            bar = system.getBar(EnumLeveling.THEORY, 132);
            mc.ingameGUI.drawTexturedModalRect(97, 189, 97, 155, (int) bar, 26);

            mc.getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/guilevel2.png"));
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            mc.ingameGUI.drawTexturedModalRect(0, 244, 0, 51, 240, 76);
            bar = system.getBar(EnumLeveling.PVP, 132);
            mc.ingameGUI.drawTexturedModalRect(97, 269, 97, 155, (int) bar, 26);


            GL11.glEnable(GL11.GL_LIGHTING);
            GlStateManager.enableLighting();

            RenderHelper.enableStandardItemLighting();
            GL11.glColor4f(0.1F, 1, 0.1F, 1);

            GL11.glPopMatrix();
        }


    }


}
