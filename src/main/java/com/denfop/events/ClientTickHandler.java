package com.denfop.events;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.api.item.IHazmatLike;
import com.denfop.api.radiationsystem.EnumLevelRadiation;
import com.denfop.api.radiationsystem.Radiation;
import com.denfop.api.radiationsystem.RadiationSystem;
import com.denfop.api.research.ResearchSystem;
import com.denfop.api.research.main.BaseLevelSystem;
import com.denfop.api.research.main.EnumLeveling;
import com.denfop.audio.EnumSound;
import com.denfop.utils.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class ClientTickHandler {

    public static final Minecraft mc = FMLClientHandler.instance().getClient();
    protected static final RenderItem itemRender = mc.getRenderItem();

    @SideOnly(Side.CLIENT)
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
            GL11.glScalef(1.7f, 1.7f, 1.7f);
           /* if (system == null) {
                GL11.glEnable(GL11.GL_LIGHTING);
                GlStateManager.enableLighting();

                RenderHelper.enableStandardItemLighting();
                GL11.glColor4f(0.1F, 1, 0.1F, 1);

                GL11.glPopMatrix();
                return;
            }*/
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

    @SideOnly(Side.CLIENT)
    public static void onTickRender1() {
        final EntityPlayerSP entityClientPlayer = mc.player;
        if (entityClientPlayer == null) {
            return;
        }
        ItemStack stack = entityClientPlayer.getHeldItem(EnumHand.MAIN_HAND);


        if (mc.world != null && mc.inGameHasFocus &&
                !mc.gameSettings.showDebugInfo && ((stack.getItem() == IUItem.crafting_elements && stack.getItemDamage() == 40) || IHazmatLike.hasCompleteHazmat(
                mc.player))) {
            String radiation_amount_player = ModUtils.getString(entityClientPlayer
                    .getEntityData()
                    .getDouble("radiation")) + " Sv";
            mc.fontRenderer.drawString(radiation_amount_player, 40, 90, ModUtils.convertRGBcolorToInt(255, 255, 255));
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPushMatrix();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GlStateManager.disableLighting();
            GlStateManager.enableDepth();
            itemRender.zLevel = 100.0F;
            mc.getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_dosimeter.png"));
            GL11.glScalef(0.4f, 0.4f, 0.4f);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            mc.ingameGUI.drawTexturedModalRect(0, 10, 0, 0, 256, 256);

            if (entityClientPlayer.getEntityWorld().provider.getDimension() != 0) {
                if (entityClientPlayer.getEntityWorld().getWorldTime() % 2 == 0) {
                    mc.getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/radiation/0.png"));
                } else {
                    mc.getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/radiation/1.png"));
                }
                mc.ingameGUI.drawTexturedModalRect(0, 10, 0, 0, 256, 256);
            } else {
                Radiation radiation =
                        RadiationSystem.rad_system.getMap().get(entityClientPlayer
                                .getEntityWorld()
                                .getChunkFromBlockCoords(entityClientPlayer.getPosition())
                                .getPos());
                if (radiation == null) {
                    if (entityClientPlayer.getEntityWorld().getWorldTime() % 4 == 0) {
                        mc.getTextureManager().bindTexture(new ResourceLocation(
                                Constants.MOD_ID,
                                "textures/gui/radiation/0.png"
                        ));
                    } else {
                        mc.getTextureManager().bindTexture(new ResourceLocation(
                                Constants.MOD_ID,
                                "textures/gui/radiation/1.png"
                        ));
                    }
                    if (entityClientPlayer.getEntityWorld().getWorldTime() % 20 == 0) {
                        entityClientPlayer.playSound(EnumSound.low_radiation.getSoundEvent(), 1, 1);
                    }
                    mc.ingameGUI.drawTexturedModalRect(0, 10, 0, 0, 256, 256);
                } else {
                    if (radiation.getLevel() == EnumLevelRadiation.LOW) {
                        int col = (int) ((radiation.getRadiation() / 800) * 2);

                        if (entityClientPlayer.getEntityWorld().getWorldTime() % 2 == 0) {
                            mc.getTextureManager().bindTexture(new ResourceLocation(
                                    Constants.MOD_ID,
                                    "textures/gui/radiation/" + col + ".png"
                            ));
                        } else if (entityClientPlayer.getEntityWorld().getWorldTime() % 9 == 0) {
                            mc.getTextureManager().bindTexture(new ResourceLocation(
                                    Constants.MOD_ID,
                                    "textures/gui/radiation/" + (int) Math.max(0, col - 1) + ".png"
                            ));
                        } else if (entityClientPlayer.getEntityWorld().getWorldTime() % 5 == 0) {
                            mc.getTextureManager().bindTexture(new ResourceLocation(
                                    Constants.MOD_ID,
                                    "textures/gui/radiation/" + (col + 1) + ".png"
                            ));
                        } else {
                            mc.getTextureManager().bindTexture(new ResourceLocation(
                                    Constants.MOD_ID,
                                    "textures/gui/radiation/" + (col) + ".png"
                            ));
                        }
                        if (entityClientPlayer.getEntityWorld().getWorldTime() % 20 == 0) {
                            entityClientPlayer.playSound(EnumSound.low_radiation.getSoundEvent(), 1, 1);
                        }
                        mc.ingameGUI.drawTexturedModalRect(0, 10, 0, 0, 256, 256);
                    } else if (radiation.getLevel() == EnumLevelRadiation.DEFAULT) {
                        int col = (int) ((radiation.getRadiation() / 800) * 4);
                        if (entityClientPlayer.getEntityWorld().getWorldTime() % 2 == 0) {
                            mc.getTextureManager().bindTexture(new ResourceLocation(
                                    Constants.MOD_ID,
                                    "textures/gui/radiation/" + (3 + col) + ".png"
                            ));
                        } else if (entityClientPlayer.getEntityWorld().getWorldTime() % 9 == 0) {
                            mc.getTextureManager().bindTexture(new ResourceLocation(
                                    Constants.MOD_ID,
                                    "textures/gui/radiation/" + (2 + col) + ".png"
                            ));
                        } else if (entityClientPlayer.getEntityWorld().getWorldTime() % 5 == 0) {
                            mc.getTextureManager().bindTexture(new ResourceLocation(
                                    Constants.MOD_ID,
                                    "textures/gui/radiation/" + (4 + col) + ".png"
                            ));
                        } else {
                            mc.getTextureManager().bindTexture(new ResourceLocation(
                                    Constants.MOD_ID,
                                    "textures/gui/radiation/" + (3 + col) + ".png"
                            ));
                        }
                        if (entityClientPlayer.getEntityWorld().getWorldTime() % 20 == 0) {
                            entityClientPlayer.playSound(EnumSound.default_radiation.getSoundEvent(), 1, 1);
                        }
                        mc.ingameGUI.drawTexturedModalRect(0, 10, 0, 0, 256, 256);
                    } else if (radiation.getLevel() == EnumLevelRadiation.MEDIUM) {
                        int col = (int) ((radiation.getRadiation() / 800) * 3);

                        if (entityClientPlayer.getEntityWorld().getWorldTime() % 2 == 0) {
                            mc.getTextureManager().bindTexture(new ResourceLocation(
                                    Constants.MOD_ID,
                                    "textures/gui/radiation/" + (7 + col) + ".png"
                            ));
                        } else if (entityClientPlayer.getEntityWorld().getWorldTime() % 9 == 0) {
                            mc.getTextureManager().bindTexture(new ResourceLocation(
                                    Constants.MOD_ID,
                                    "textures/gui/radiation/" + (6 + col) + ".png"
                            ));
                        } else if (entityClientPlayer.getEntityWorld().getWorldTime() % 5 == 0) {
                            mc.getTextureManager().bindTexture(new ResourceLocation(
                                    Constants.MOD_ID,
                                    "textures/gui/radiation/" + (8 + col) + ".png"
                            ));
                        } else {
                            mc.getTextureManager().bindTexture(new ResourceLocation(
                                    Constants.MOD_ID,
                                    "textures/gui/radiation/" + (7 + col) + ".png"
                            ));
                        }
                        if (entityClientPlayer.getEntityWorld().getWorldTime() % 20 == 0) {
                            entityClientPlayer.playSound(EnumSound.medium_radiation.getSoundEvent(), 1, 1);
                        }
                        mc.ingameGUI.drawTexturedModalRect(0, 10, 0, 0, 256, 256);
                    } else if (radiation.getLevel() == EnumLevelRadiation.HIGH) {
                        int col = (int) ((radiation.getRadiation() / 800) * 2);
                        if (entityClientPlayer.getEntityWorld().getWorldTime() % 2 == 0) {
                            mc.getTextureManager().bindTexture(new ResourceLocation(
                                    Constants.MOD_ID,
                                    "textures/gui/radiation/" + (11 + col) + ".png"
                            ));
                        } else if (entityClientPlayer.getEntityWorld().getWorldTime() % 9 == 0) {
                            mc.getTextureManager().bindTexture(new ResourceLocation(
                                    Constants.MOD_ID,
                                    "textures/gui/radiation/" + (10 + col) + ".png"
                            ));
                        } else if (entityClientPlayer.getEntityWorld().getWorldTime() % 5 == 0) {
                            mc.getTextureManager().bindTexture(new ResourceLocation(
                                    Constants.MOD_ID,
                                    "textures/gui/radiation/" + (12 + col) + ".png"
                            ));
                        } else {
                            mc.getTextureManager().bindTexture(new ResourceLocation(
                                    Constants.MOD_ID,
                                    "textures/gui/radiation/" + (11 + col) + ".png"
                            ));
                        }
                        if (entityClientPlayer.getEntityWorld().getWorldTime() % 30 == 0) {
                            entityClientPlayer.playSound(EnumSound.high_radiation.getSoundEvent(), 1, 1);
                        }
                        mc.ingameGUI.drawTexturedModalRect(0, 10, 0, 0, 256, 256);
                    } else if (radiation.getLevel() == EnumLevelRadiation.VERY_HIGH) {
                        int col = (int) ((radiation.getRadiation() / 800) * 2);

                        if (entityClientPlayer.getEntityWorld().getWorldTime() % 5 == 0) {
                            mc.getTextureManager().bindTexture(new ResourceLocation(
                                    Constants.MOD_ID,
                                    "textures/gui/radiation/" + Math.min(14 + col, 16) + ".png"
                            ));
                        } else if (entityClientPlayer.getEntityWorld().getWorldTime() % 9 == 0) {
                            mc.getTextureManager().bindTexture(new ResourceLocation(
                                    Constants.MOD_ID,
                                    "textures/gui/radiation/" + Math.min(13 + col, 16) + ".png"
                            ));
                        } else if (entityClientPlayer.getEntityWorld().getWorldTime() % 5 == 0) {
                            mc.getTextureManager().bindTexture(new ResourceLocation(
                                    Constants.MOD_ID,
                                    "textures/gui/radiation/" + Math.min(15 + col, 16) + ".png"
                            ));
                        } else {
                            mc.getTextureManager().bindTexture(new ResourceLocation(
                                    Constants.MOD_ID,
                                    "textures/gui/radiation/" + Math.min(14 + col, 16) + ".png"
                            ));
                        }
                        if (entityClientPlayer.getEntityWorld().getWorldTime() % 30 == 0) {
                            entityClientPlayer.playSound(EnumSound.very_high_radiation.getSoundEvent(), 1, 1);
                        }
                        mc.ingameGUI.drawTexturedModalRect(0, 10, 0, 0, 256, 256);
                    }
                }
            }
            GL11.glEnable(GL11.GL_LIGHTING);
            GlStateManager.enableLighting();

            RenderHelper.enableStandardItemLighting();
            GL11.glColor4f(0.1F, 1, 0.1F, 1);

            GL11.glPopMatrix();
        }


    }

}
