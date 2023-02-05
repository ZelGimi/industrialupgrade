package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.api.vein.Type;
import com.denfop.container.ContainerQuarryVein;
import com.denfop.utils.ListInformationUtils;
import com.denfop.utils.ModUtils;
import ic2.core.IC2;
import ic2.core.init.Localization;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public class GuiQuarryVein extends GuiIU<ContainerQuarryVein> {

    public final ContainerQuarryVein container;
    public int[][] colors = new int[39][66];

    public GuiQuarryVein(ContainerQuarryVein container1) {
        super(container1, container1.base.getStyle());
        this.container = container1;
        for (int x1 = 74; x1 <= 112; x1++) {
            for (int y1 = 14; y1 <= 79; y1++) {
                int y2 = getCoord(y1, this.container.base.getBlockPos().getY());
                int x2 = getCoordX(x1, this.container.base.getBlockPos().getX(), 88);
                final BlockPos pos = new BlockPos(x2, y2, this.container.base.getBlockPos().getZ());
                IBlockState state = this.container.base.getWorld().getBlockState(pos);
                colors[x1 - 74][y1 - 14] = this.getColor(state, this.container.base.getWorld(), pos);
            }
        }
    }

    int getChance(Biome biome) {
        if (Biome.getIdForBiome(biome) == 2) {
            return 65;
        } else if (Biome.getIdForBiome(biome) == 0) {
            return 40;
        } else if (Biome.getIdForBiome(biome) == 24) {
            return 65;
        } else if (Biome.getIdForBiome(biome) == 10) {
            return 40;
        } else if (Biome.getIdForBiome(biome) == 17) {
            return 65;
        } else if (Biome.getIdForBiome(biome) == 7) {
            return 50;
        } else if (Biome.getIdForBiome(biome) == 35) {
            return 50;
        } else {
            return 30;
        }
    }

    List<String> getList() {
        List<String> lst = new ArrayList<>();
        final Biome biome = this.container.base.getWorld().getBiomeForCoordsBody(this.container.base.getBlockPos());
        lst.add(Localization.translate("iu.biome") + biome.getBiomeName());
        lst.add(Localization.translate("iu.gettingvein") + getChance(biome) + "%");
        lst.add(Localization.translate("iu.gettingvein1") + 15 + "%");

        return lst;
    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 73 && mouseX <= 113 && mouseY >= 13 && mouseY < 80) {
            int y = getCoord(mouseY, this.container.base.getBlockPos().getY());
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.quarryveininformation"));
            List<String> compatibleUpgrades = getList();
            compatibleUpgrades.add("Y: " + y);
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX, mouseY, text);
        } else if (mouseX >= 3 && mouseX <= 15 && mouseY >= 3 && mouseY <= 15) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.quarryvein_info"));
            List<String> compatibleUpgrades = ListInformationUtils.quarryvein;
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX, mouseY, text);
        }
    }

    private int getCoord(final int mouseY, int y) {
        double m = y * 1D / 59D;
        double m1 = mouseY - 21;
        m *= m1;
        return (int) Math.min(Math.max(y - m, 0D), 256);
    }

    private int getCoordX(final int mouseX, int x, int center) {
        // center = x
        int x1 = x;
        if (mouseX < center) {
            x1 -= (center - mouseX) * 2;
        } else {
            x1 += (center - mouseX) * 2;
        }
        return x1;
    }

    private void handleUpgradeTooltip1(int mouseX, int mouseY) {
        if (mouseX >= 122 && mouseX <= 166 && mouseY >= 23 && mouseY <= 39) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.canupdate"));
            String m = "";
            switch (this.container.base.level) {
                case 2:
                    m = Localization.translate("iu.upgradekitmachine" +
                            ".upgradepanelkitmachine1");
                    break;
                case 3:
                    m = Localization.translate("iu.upgradekitmachine" +
                            ".upgradepanelkitmachine2");
                    break;
                case 4:
                    break;
                default:
                    m = Localization.translate("iu.upgradekitmachine.upgradepanelkitmachine");
                    break;
            }
            List<String> compatibleUpgrades = Collections.singletonList(m);
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }
            if (!(m.equals(""))) {
                this.drawTooltip(mouseX, mouseY, text);
            }
        }
    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);
        String name = Localization.translate("iu.quarry_vein_item.quarry_vein_item");
        switch (this.container.base.level) {
            case 2:
                name = TextFormatting.GOLD + Localization.translate("iu.advanced_name") + " " + name.toLowerCase();
                break;
            case 3:
                name = TextFormatting.BLUE + Localization.translate("iu.improved_name") + " " + name.toLowerCase();
                break;
            case 4:
                name = TextFormatting.DARK_PURPLE + Localization.translate("iu.perfect_name") + " " + name.toLowerCase();
                break;
            default:
                break;
        }
        int nmPos = (this.xSize - this.fontRenderer.getStringWidth(name)) / 2;
        this.fontRenderer.drawString(name, nmPos, 4, 4210752);
        this.fontRenderer.drawString(Localization.translate("iu.level"), 122, 23, 4210752);

        switch (this.container.base.level) {
            case 2:
                this.fontRenderer.drawString(TextFormatting.GOLD + Localization.translate("iu.advanced"), 122, 31, 4210752);
                break;
            case 3:
                this.fontRenderer.drawString(TextFormatting.BLUE + Localization.translate("iu.improved"), 122, 31, 4210752);
                break;
            case 4:
                this.fontRenderer.drawString(TextFormatting.DARK_PURPLE + Localization.translate("iu.perfect"), 122, 31, 4210752);
                break;
            default:
                this.fontRenderer.drawString(Localization.translate("iu.simply"), 122, 31, 4210752);
                break;
        }
        handleUpgradeTooltip1(par1, par2);

        if (this.container.base.vein == null || this.container.base.progress < 1200) {
            this.fontRenderer.drawString(
                    (this.container.base.progress * 100 / 1200) + "%",
                    29,
                    32,
                    ModUtils.convertRGBcolorToInt(13, 229, 34)
            );


        }
        handleUpgradeTooltip(par1, par2);

        if (this.container.base.vein != null && this.container.base.vein.get()) {
            if (this.container.base.vein.getType() == Type.EMPTY || this.container.base.vein.getMaxCol() == 0) {
                this.fontRenderer.drawString(
                        Localization.translate("iu.empty"),
                        29,
                        32,
                        ModUtils.convertRGBcolorToInt(13, 229, 34)
                );
            } else {
                this.fontRenderer.drawString(
                        Localization.translate("iu.find"),
                        26,
                        32,
                        ModUtils.convertRGBcolorToInt(13, 229, 34)
                );
                int col = this.container.base.vein.getCol();
                int colmax = this.container.base.vein.getMaxCol();
                boolean isOil = this.container.base.vein.getType() == Type.OIL;
                String name_vein;
                if (!isOil) {
                    name_vein = new ItemStack(IUItem.heavyore, 1, this.container.base.vein.getMeta()).getDisplayName();
                } else {
                    name_vein = Localization.translate("iu.fluidneft");
                }
                new AdvArea(this, 20, 54, 68, 72).withTooltip(name_vein + " " + col + (isOil ? "mb" : "") + "/" + colmax + (
                        isOil
                                ?
                                "mb"
                                : "")).drawForeground(par1, par2);
            }
        }

        String tooltip2 =
                ModUtils.getString(Math.min(
                        this.container.base.energy.getEnergy(),
                        this.container.base.energy.getCapacity()
                )) + "/" + ModUtils.getString(this.container.base.energy.getCapacity()) + " " +
                        "EU";
        new AdvArea(this, 6, 31, 17, 80)
                .withTooltip(tooltip2)
                .drawForeground(par1, par2);
    }

    private int getColor(IBlockState state, World world, BlockPos pos) {
        if (state.getMaterial() == Material.AIR) {
            return (int) this.container.base.getWorld().provider.getCloudColor(0).x;
        }
        MapColor color = state.getMapColor(world, pos);
        return color.colorValue | -16777216;
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        int h = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;
        this.mc.getTextureManager().bindTexture(getTexture());
        drawTexturedModalRect(h, k, 0, 0, this.xSize, this.ySize);
        this.drawBackground();
        this.mc.getTextureManager().bindTexture(getTexture());
        int m = this.container.base.progress * 34 / 1200;


        for (int x1 = 74; x1 <= 112; x1++) {
            for (int y1 = 14; y1 <= 79; y1++) {

                this.drawColoredRect(x1, y1, 1, 1, colors[x1 - 74][y1 - 14]);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            }
        }
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        drawTexturedModalRect(h + 88, k + 22, 183, 50, 1, m);
        switch (this.container.base.level) {
            case 2:
                drawTexturedModalRect(h + 88, k + 21, 184, 48, 1, 1);
                break;
            case 3:
                drawTexturedModalRect(h + 88, k + 21, 185, 48, 1, 1);
                break;
            case 4:
                drawTexturedModalRect(h + 88, k + 21, 186, 48, 1, 1);
                break;
            default:
                break;
        }
        this.mc.getTextureManager()
                .bindTexture(new ResourceLocation(IC2.RESOURCE_DOMAIN, "textures/gui/infobutton.png"));
        drawTexturedModalRect(h + 3, k + 3, 0, 0, 10, 10);
        this.mc.getTextureManager().bindTexture(getTexture());
        int chargeLevel = (int) (48.0F * this.container.base.energy.getEnergy()
                / this.container.base.energy.getCapacity());

        if (chargeLevel > 0) {
            drawTexturedModalRect(h + 6, k + 32 + 48 - chargeLevel, 196,
                    85 - chargeLevel, 48, chargeLevel
            );
        }

        if (this.container.base.vein != null && this.container.base.vein.get()) {
            if (this.container.base.vein.getType() != Type.EMPTY) {
                RenderHelper.enableGUIStandardItemLighting();
                GL11.glPushMatrix();
                GL11.glColor4f(0.1F, 1, 0.1F, 1);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glEnable(GL12.GL_RESCALE_NORMAL);
                GlStateManager.disableLighting();
                GlStateManager.enableDepth();
                this.zLevel = 100.0F;
                mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                ItemStack stack;
                if (this.container.base.vein.getType() == Type.VEIN) {
                    stack = new ItemStack(IUItem.heavyore, 1, this.container.base.vein.getMeta());

                } else {
                    stack = new ItemStack(IUItem.oilblock);
                }
                itemRender.renderItemAndEffectIntoGUI(
                        stack,
                        h + 32,
                        k + 54
                );
                GL11.glEnable(GL11.GL_LIGHTING);
                GlStateManager.enableLighting();

                RenderHelper.enableStandardItemLighting();
                GL11.glColor4f(0.1F, 1, 0.1F, 1);
                GL11.glPopMatrix();
            }
        }

    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiquarryvein.png");
    }

}
