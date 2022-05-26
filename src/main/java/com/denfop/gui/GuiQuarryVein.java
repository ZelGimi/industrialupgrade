package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.container.ContainerQuarryVein;
import com.denfop.tiles.base.TileEntityQuarryVein;
import com.denfop.tiles.base.TileEntityVein;
import com.denfop.tiles.base.TileOilBlock;
import com.denfop.utils.ModUtils;
import ic2.core.GuiIC2;
import ic2.core.init.Localization;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.biome.Biome;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public class GuiQuarryVein extends GuiIC2<ContainerQuarryVein> {

    public final ContainerQuarryVein container;

    public GuiQuarryVein(ContainerQuarryVein container1) {
        super(container1);
        this.container = container1;

    }

    int getChance(Biome biome) {
        if (Biome.getIdForBiome(biome) == 2) {
            return 60;
        } else if (Biome.getIdForBiome(biome) == 0) {
            return 35;
        } else if (Biome.getIdForBiome(biome) == 24) {
            return 60;
        } else if (Biome.getIdForBiome(biome) == 10) {
            return 35;
        } else if (Biome.getIdForBiome(biome) == 17) {
            return 60;
        } else if (Biome.getIdForBiome(biome) == 7) {
            return 45;
        } else if (Biome.getIdForBiome(biome) == 35) {
            return 45;
        } else {
            return 25;
        }
    }

    List<String> getList() {
        List<String> lst = new ArrayList<>();
        final Biome biome = this.container.base.getWorld().getBiomeForCoordsBody(this.container.base.getPos());
        String n = this.container.base.level > 1 ?
                (TextFormatting.GREEN + "+" + ((this.container.base.level - 1) * 3) + "%") : "";
        lst.add(Localization.translate("iu.biome") + biome.getBiomeName());
        lst.add(Localization.translate("iu.gettingvein") + getChance(biome) + "%" + n);
        n = this.container.base.level > 1 ?
                (TextFormatting.GREEN + "+" + ((this.container.base.level - 1) * 2) + "%") : "";
        lst.add(Localization.translate("iu.gettingvein1") + 20 + "%" + n);

        return lst;
    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 73 && mouseX <= 113 && mouseY >= 13 && mouseY < 80) {
            int y = getCoord(mouseY, this.container.base.getPos().getY());
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
        }
    }

    private int getCoord(final int mouseY, int y) {
        double m = y * 1D / 59D;
        double m1 = mouseY - 21;
        m *= m1;
        return (int) Math.min(Math.max(y - m, 0D), 256);
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
        if (this.container.base.analysis) {
            this.fontRenderer.drawString(
                    (this.container.base.progress * 100 / 1200) + "%",
                    29,
                    32,
                    ModUtils.convertRGBcolorToInt(13, 229, 34)
            );


        }
        handleUpgradeTooltip(par1, par2);
        if (!this.container.base.analysis) {
            if (this.container.base.empty) {
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
                TileEntityQuarryVein quarry = this.container.base;
                BlockPos pos = new BlockPos(quarry.x, quarry.y, quarry.z);
                int col = 0;
                int colmax = 0;
                boolean isOil = false;
                String name_vein = "";
                if (quarry.getWorld().getTileEntity(pos) instanceof TileEntityVein) {
                    TileEntityVein vein = (TileEntityVein) quarry.getWorld().getTileEntity(pos);
                    col = container.base.number;
                    colmax = container.base.max;
                    name_vein = new ItemStack(IUItem.heavyore, 1, vein.getBlockMetadata()).getDisplayName();
                } else if (quarry.getWorld().getTileEntity(pos) instanceof TileOilBlock) {
                    col = container.base.number;
                    isOil = true;
                    colmax = container.base.max;
                    name_vein = Localization.translate("iu.fluidneft");
                }
                new AdvArea(this, 20, 54, 68, 72).withTooltip(name_vein + " " + col + (isOil ? "mb" : "") + "/" + colmax + (isOil
                        ?
                        "mb"
                        : "")).drawForeground(par1, par2);
            }
        }
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        int h = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;
        this.mc.getTextureManager().bindTexture(getTexture());
        drawTexturedModalRect(h, k, 0, 0, this.xSize, this.ySize);
        int m = this.container.base.progress * 34 / 1200;
        drawTexturedModalRect(h + 88, k + 23, 183, 50, 1, m);
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
        int chargeLevel = (int) (48.0F * this.container.base.energy.getEnergy()
                / this.container.base.energy.getCapacity());

        if (chargeLevel > 0) {
            drawTexturedModalRect(h + 6, k + 32 + 48 - chargeLevel, 196,
                    85 - chargeLevel, 48, chargeLevel
            );
        }
        if (!this.container.base.analysis) {
            if (!this.container.base.empty) {
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
                TileEntityQuarryVein quarry = this.container.base;
                BlockPos pos = new BlockPos(quarry.x, quarry.y, quarry.z);
                if (quarry.getWorld().getTileEntity(pos) instanceof TileEntityVein) {
                    TileEntityVein vein = (TileEntityVein) quarry.getWorld().getTileEntity(pos);
                    stack = new ItemStack(IUItem.heavyore, 1, vein.getBlockMetadata());

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


    public String getName() {
        return container.base.getInventoryName();
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiquarryvein.png");
    }

}
