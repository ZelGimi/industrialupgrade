package com.denfop.events;

import com.denfop.Constants;
import com.denfop.api.item.IHazmatLike;
import com.denfop.api.radiationsystem.EnumLevelRadiation;
import com.denfop.api.radiationsystem.Radiation;
import com.denfop.api.radiationsystem.RadiationSystem;
import com.denfop.audio.EnumSound;
import com.denfop.gui.GuiCore;
import com.denfop.items.ItemCraftingElements;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ClientTickHandler {
    public static void onTickRender1(GuiGraphics pose) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        if (player == null || mc.level == null) return;
        PoseStack guiGraphics = pose.pose();
        if (shouldDisplayRadiationInfo(player)) {
            guiGraphics.pushPose();
            double radiationLevel = player.getPersistentData().getDouble("radiation");
            String radiationText = String.format("%.2f Sv", radiationLevel);
            pose.drawString(Minecraft.getInstance().font, radiationText, 40, 90, 0xFFFFFF);

            GuiCore.bindTexture(0, ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/gui_dosimeter.png"));
            guiGraphics.scale(0.4f, 0.4f, 0.4f);
            pose.blit(GuiCore.currentTexture, 0, 10, 0, 0, 256, 256, 256, 256);


            ResourceLocation radiationTexture = getRadiationTexture(player);
            GuiCore.bindTexture(radiationTexture);
            pose.blit(GuiCore.currentTexture, 0, 10, 0, 0, 256, 256, 256, 256);
            guiGraphics.popPose();
        }
    }

    private static boolean shouldDisplayRadiationInfo(LocalPlayer player) {
        ItemStack stack = player.getMainHandItem();
        boolean isHoldingGeigerCounter = stack.getItem() instanceof ItemCraftingElements<?> && ((ItemCraftingElements<?>) stack.getItem()).getElement().getId() == 40;
        boolean isWearingHazmat = IHazmatLike.hasCompleteHazmat(player);
        return isHoldingGeigerCounter || isWearingHazmat;
    }

    private static ResourceLocation getRadiationTexture(LocalPlayer player) {
        boolean dimension = player.level().dimension() == Level.OVERWORLD;
        long worldTime = player.level().getGameTime();

        if (!dimension) {
            int frame = (worldTime % 2 == 0) ? 0 : 1;
            return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/radiation/" + frame + ".png");
        } else {
            Radiation radiation = RadiationSystem.rad_system.getMap().get(player.chunkPosition());
            if (radiation == null) {
                int frame = (worldTime % 4 == 0) ? 0 : 1;
                return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/radiation/" + frame + ".png");
            } else {
                return getRadiationLevelTexture(radiation, worldTime);
            }
        }
    }

    private static ResourceLocation getRadiationLevelTexture(Radiation radiation, long worldTime) {
        if (radiation == null) {
            if (Minecraft.getInstance().player.level().getGameTime() % 20 == 0) {
                Minecraft.getInstance().player.playSound(EnumSound.low_radiation.getSoundEvent(), 10, 1);
            }
            if (worldTime % 4 == 0) {
                return ResourceLocation.tryBuild(
                        Constants.MOD_ID,
                        "textures/gui/radiation/0.png"
                );
            } else {
                return ResourceLocation.tryBuild(
                        Constants.MOD_ID,
                        "textures/gui/radiation/1.png"
                );
            }

        } else {
            if (Minecraft.getInstance().player.level().getGameTime() % 20 == 0) {
                Minecraft.getInstance().player.playSound(EnumSound.low_radiation.getSoundEvent(), 10, 1);
            }
            if (radiation.getLevel() == EnumLevelRadiation.LOW) {
                int col = (int) ((radiation.getRadiation() / 800) * 2);

                if (worldTime % 2 == 0) {
                    return ResourceLocation.tryBuild(
                            Constants.MOD_ID,
                            "textures/gui/radiation/" + col + ".png"
                    );
                } else if (worldTime % 9 == 0) {
                    return ResourceLocation.tryBuild(
                            Constants.MOD_ID,
                            "textures/gui/radiation/" + (int) Math.max(0, col - 1) + ".png"
                    );
                } else if (worldTime % 5 == 0) {
                    return ResourceLocation.tryBuild(
                            Constants.MOD_ID,
                            "textures/gui/radiation/" + (col + 1) + ".png"
                    );
                } else {
                    return ResourceLocation.tryBuild(
                            Constants.MOD_ID,
                            "textures/gui/radiation/" + (col) + ".png"
                    );
                }

            } else if (radiation.getLevel() == EnumLevelRadiation.DEFAULT) {
                int col = (int) ((radiation.getRadiation() / 800) * 4);
                if (Minecraft.getInstance().player.level().getGameTime() % 20 == 0) {
                    Minecraft.getInstance().player.playSound(EnumSound.default_radiation.getSoundEvent(), 10, 1);
                }
                if (worldTime % 2 == 0) {
                    return ResourceLocation.tryBuild(
                            Constants.MOD_ID,
                            "textures/gui/radiation/" + (3 + col) + ".png"
                    );
                } else if (worldTime % 9 == 0) {
                    return ResourceLocation.tryBuild(
                            Constants.MOD_ID,
                            "textures/gui/radiation/" + (2 + col) + ".png"
                    );
                } else if (worldTime % 5 == 0) {
                    return ResourceLocation.tryBuild(
                            Constants.MOD_ID,
                            "textures/gui/radiation/" + (4 + col) + ".png"
                    );
                } else {
                    return ResourceLocation.tryBuild(
                            Constants.MOD_ID,
                            "textures/gui/radiation/" + (3 + col) + ".png"
                    );
                }

            } else if (radiation.getLevel() == EnumLevelRadiation.MEDIUM) {
                int col = (int) ((radiation.getRadiation() / 800) * 3);
                if (Minecraft.getInstance().player.level().getGameTime() % 20 == 0) {
                    Minecraft.getInstance().player.playSound(EnumSound.medium_radiation.getSoundEvent(), 10, 1);
                }
                if (worldTime % 2 == 0) {
                    return ResourceLocation.tryBuild(
                            Constants.MOD_ID,
                            "textures/gui/radiation/" + (7 + col) + ".png"
                    );
                } else if (worldTime % 9 == 0) {
                    return ResourceLocation.tryBuild(
                            Constants.MOD_ID,
                            "textures/gui/radiation/" + (6 + col) + ".png"
                    );
                } else if (worldTime % 5 == 0) {
                    return ResourceLocation.tryBuild(
                            Constants.MOD_ID,
                            "textures/gui/radiation/" + (8 + col) + ".png"
                    );
                } else {
                    return ResourceLocation.tryBuild(
                            Constants.MOD_ID,
                            "textures/gui/radiation/" + (7 + col) + ".png"
                    );
                }

            } else if (radiation.getLevel() == EnumLevelRadiation.HIGH) {
                int col = (int) ((radiation.getRadiation() / 800) * 2);
                if (Minecraft.getInstance().player.level().getGameTime() % 30 == 0) {
                    Minecraft.getInstance().player.playSound(EnumSound.high_radiation.getSoundEvent(), 10, 1);
                }
                if (worldTime % 2 == 0) {
                    return ResourceLocation.tryBuild(
                            Constants.MOD_ID,
                            "textures/gui/radiation/" + (11 + col) + ".png"
                    );
                } else if (worldTime % 9 == 0) {
                    return ResourceLocation.tryBuild(
                            Constants.MOD_ID,
                            "textures/gui/radiation/" + (10 + col) + ".png"
                    );
                } else if (worldTime % 5 == 0) {
                    return ResourceLocation.tryBuild(
                            Constants.MOD_ID,
                            "textures/gui/radiation/" + (12 + col) + ".png"
                    );
                } else {
                    return ResourceLocation.tryBuild(
                            Constants.MOD_ID,
                            "textures/gui/radiation/" + (11 + col) + ".png"
                    );
                }

            } else if (radiation.getLevel() == EnumLevelRadiation.VERY_HIGH) {
                int col = (int) ((radiation.getRadiation() / 800) * 2);
                if (Minecraft.getInstance().player.level().getGameTime() % 30 == 0) {
                    Minecraft.getInstance().player.playSound(EnumSound.very_high_radiation.getSoundEvent(), 10, 1);
                }
                if (worldTime % 2 == 0) {
                    return ResourceLocation.tryBuild(
                            Constants.MOD_ID,
                            "textures/gui/radiation/" + Math.min(14 + col, 16) + ".png"
                    );
                } else if (worldTime % 9 == 0) {
                    return ResourceLocation.tryBuild(
                            Constants.MOD_ID,
                            "textures/gui/radiation/" + Math.min(13 + col, 16) + ".png"
                    );
                } else if (worldTime % 5 == 0) {
                    return ResourceLocation.tryBuild(
                            Constants.MOD_ID,
                            "textures/gui/radiation/" + Math.min(15 + col, 16) + ".png"
                    );
                } else {
                    return ResourceLocation.tryBuild(
                            Constants.MOD_ID,
                            "textures/gui/radiation/" + Math.min(14 + col, 16) + ".png"
                    );
                }

            }
        }
        return ResourceLocation.tryBuild(
                Constants.MOD_ID,
                "textures/gui/radiation/0.png"
        );
    }
}
