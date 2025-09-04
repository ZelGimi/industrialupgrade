package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.api.vein.common.Type;
import com.denfop.api.vein.common.VeinSystem;
import com.denfop.api.widget.*;
import com.denfop.blocks.FluidName;
import com.denfop.containermenu.ContainerMenuQuarryVein;
import com.denfop.utils.ListInformationUtils;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.common.Tags;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static net.minecraft.tags.BiomeTags.IS_HILL;
import static net.minecraft.tags.BiomeTags.IS_TAIGA;


public class ScreenQuarryVein<T extends ContainerMenuQuarryVein> extends ScreenMain<ContainerMenuQuarryVein> {

    public final ContainerMenuQuarryVein container;
    public int[][] colors = new int[39][66];

    public ScreenQuarryVein(ContainerMenuQuarryVein container1) {
        super(container1, container1.base.getStyle());
        this.container = container1;
        for (int x1 = 74; x1 <= 112; x1++) {
            for (int y1 = 14; y1 <= 79; y1++) {
                int y2 = getCoord(y1, this.container.base.getBlockPos().getY());
                int x2 = getCoordX(x1, this.container.base.getBlockPos().getX(), 88);
                final BlockPos pos = new BlockPos(x2, y2, this.container.base.getBlockPos().getZ());
                BlockState state = this.container.base.getWorld().getBlockState(pos);
                colors[x1 - 74][y1 - 14] = this.getColor(state, this.container.base.getWorld(), pos);
            }
        }
        this.imageHeight += 30;
        this.inventory.setY(this.inventory.getY() + 30);
        this.elements.add(new ImageInterfaceWidget(this, 0, 0, this.imageHeight, this.imageHeight));
        this.elements.add(new ImageScreenWidget(this, 7, 30, 60, 15));
        this.addComponent(new ScreenWidget(this, 69, 28, EnumTypeComponent.BIGGEST_FRAME,
                new WidgetDefault<>(new EmptyWidget())
        ));
        this.addComponent(new ScreenWidget(this, 129, 58, EnumTypeComponent.ENERGY_HEIGHT,
                new WidgetDefault<>(this.container.base.energy)
        ));
    }

    int getChance(final Holder<Biome> biome) {
        if (biome.is(Tags.Biomes.IS_DESERT)) {
            return 65;
        } else if (biome.is(BiomeTags.IS_OCEAN)) {
            return 60;
        } else if (biome.is(BiomeTags.IS_DEEP_OCEAN)) {
            return 65;
        } else if (biome.is(BiomeTags.IS_RIVER)) {
            return 50;
        } else if (biome.is(BiomeTags.IS_SAVANNA)) {
            return 50;
        } else {
            return 10;
        }
    }

    List<String> getList() {
        List<String> lst = new ArrayList<>();
        Holder<Biome> biome = this.container.base.getWorld().getBiome(this.container.base.getBlockPos());
        ResourceLocation biomeKey = this.container.base.getWorld().registryAccess()
                .registryOrThrow(Registries.BIOME)
                .getKey(biome.value());
        lst.add(Localization.translate("iu.biome") + Localization.translate("biome." + biomeKey.getNamespace() + "." + biomeKey.getPath()));
        int col = biome.is(IS_HILL) ? 25 : 0;
        int col1 = (biome.is(IS_TAIGA) || biome.is(Tags.Biomes.IS_SNOWY)) ? 15 : 0;
        lst.add(Localization.translate("iu.gettingvein") + ((int) (Math.max(0, getChance(biome) - col - col1) * 0.85) + "%"));
        lst.add(Localization.translate("iu.gettingvein1") + String.valueOf(15 + col) + "%");
        lst.add(Localization.translate("iu.gettingvein2") + String.valueOf(15 + col + col1) + "%");

        return lst;
    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 73 && mouseX <= 113 && mouseY >= 33 && mouseY < 100) {
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
            switch (this.container.base.levelMech) {
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

    protected void drawForegroundLayer(GuiGraphics poseStack, int par1, int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        String name = Localization.translate(this.container.base.getName());
        switch (this.container.base.levelMech) {
            case 2:
                name = ChatFormatting.GOLD + Localization.translate("iu.advanced_name") + " " + name.toLowerCase();
                break;
            case 3:
                name = ChatFormatting.BLUE + Localization.translate("iu.improved_name") + " " + name.toLowerCase();
                break;
            case 4:
                name = ChatFormatting.DARK_PURPLE + Localization.translate("iu.perfect_name") + " " + name.toLowerCase();
                break;
            default:
                break;
        }
        int nmPos = (this.imageWidth - this.getStringWidth(name)) / 2;
        draw(poseStack, name, nmPos, 4, 4210752);
        draw(poseStack, Localization.translate("iu.level"), 122, 23, 4210752);

        switch (this.container.base.levelMech) {
            case 2:
                draw(poseStack, ChatFormatting.GOLD + Localization.translate("iu.advanced"), 122, 31, 4210752);
                break;
            case 3:
                draw(poseStack, ChatFormatting.BLUE + Localization.translate("iu.improved"), 122, 31, 4210752);
                break;
            case 4:
                draw(poseStack, ChatFormatting.DARK_PURPLE + Localization.translate("iu.perfect"), 122, 31, 4210752);
                break;
            default:
                draw(poseStack, Localization.translate("iu.simply"), 122, 31, 4210752);
                break;
        }
        handleUpgradeTooltip1(par1, par2);

        if (this.container.base.vein == null || this.container.base.progress < 1200) {
            draw(poseStack,
                    (this.container.base.progress * 100 / 1200) + "%",
                    29,
                    34,
                    ModUtils.convertRGBcolorToInt(13, 229, 34)
            );


        }
        handleUpgradeTooltip(par1, par2);

        if (this.container.base.vein != VeinSystem.system.getEMPTY() && this.container.base.vein.get()) {
            if (this.container.base.vein.getType() == Type.EMPTY || this.container.base.vein.getMaxCol() == 0) {
                draw(poseStack,
                        Localization.translate("iu.empty"),
                        20,
                        34,
                        ModUtils.convertRGBcolorToInt(13, 229, 34)
                );
            } else {
                draw(poseStack,
                        Localization.translate("iu.find"),
                        11,
                        34,
                        ModUtils.convertRGBcolorToInt(13, 229, 34)
                );
                int col = this.container.base.vein.getCol();
                int colmax = this.container.base.vein.getMaxCol();
                boolean isOil = this.container.base.vein.getType() == Type.OIL;
                String name_vein;
                if (!isOil) {
                    if (this.container.base.vein.getType() != Type.GAS) {
                        if (container.base.vein.isOldMineral()) {
                            name_vein = new ItemStack(IUItem.heavyore.getItem(this.container.base.vein.getMeta()), 1).getDisplayName().getString();
                        } else {
                            name_vein = new ItemStack(IUItem.mineral.getItem(this.container.base.vein.getMeta()), 1).getDisplayName().getString();
                        }

                    } else {
                        name_vein = Localization.translate(FluidName.fluidgas.getInstance().get().getFluidType().getDescriptionId());
                        isOil = true;
                    }
                } else {
                    int variety = this.container.base.vein.getMeta() / 3;
                    int type = this.container.base.vein.getMeta() % 3;
                    String varietyString = variety == 0 ? "iu.sweet_oil" : "iu.sour_oil";
                    String typeString = type == 0 ? "iu.light_oil" : type == 1 ? "iu.medium_oil" : "iu.heavy_oil";
                    name_vein = Localization.translate(varietyString) + " " + Localization.translate(
                            typeString) + " " + Localization.translate(new ItemStack(IUItem.oilblock.getItem()).getDescriptionId());

                }
                new AdvancedTooltipWidget(this, 20, 54, 68, 72).withTooltip(name_vein + " " + col + (isOil ? "mb" : "") + "/" + colmax + (
                        isOil
                                ?
                                "mb"
                                : "")).drawForeground(poseStack, par1, par2);
            }
        }


    }

    private int getColor(BlockState state, Level world, BlockPos pos) {
        MapColor color = state.getMapColor(world, pos);
        return color.col | -16777216;
    }

    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);
        int h = guiLeft;
        int k = guiTop;
        bindTexture(getTexture());
        int m = this.container.base.progress * 34 / 1200;
        ShaderInstance shader = RenderSystem.getShader();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        BufferBuilder bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        for (int x1 = 74; x1 <= 112; x1++) {
            for (int y1 = 14; y1 <= 79; y1++) {

                this.drawColoredRect(poseStack, x1, y1 + 20, 1, 1, colors[x1 - 74][y1 - 14], bufferBuilder);

            }
        }
        BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
        RenderSystem.setShader(() -> shader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        drawTexturedModalRect(poseStack, h + 88, k + 42, 183, 50, 1, m);
        switch (this.container.base.levelMech) {
            case 2:
                drawTexturedModalRect(poseStack, h + 88, k + 41, 184, 48, 1, 1);
                break;
            case 3:
                drawTexturedModalRect(poseStack, h + 88, k + 41, 185, 48, 1, 1);
                break;
            case 4:
                drawTexturedModalRect(poseStack, h + 88, k + 41, 186, 48, 1, 1);
                break;
            default:
                break;
        }
        bindTexture(ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/infobutton.png"));
        drawTexturedModalRect(poseStack, h + 3, k + 3, 0, 0, 10, 10);
        bindTexture(getTexture());


        if (this.container.base.vein != null && this.container.base.vein.get()) {
            if (this.container.base.vein.getType() != Type.EMPTY) {
                ItemStack stack;
                if (this.container.base.vein.getType() == Type.VEIN) {
                    if (this.container.base.vein.isOldMineral()) {
                        stack = new ItemStack(IUItem.heavyore.getItem(this.container.base.vein.getMeta()), 1);
                    } else {
                        stack = new ItemStack(IUItem.mineral.getItem(this.container.base.vein.getMeta()), 1);
                    }

                } else {
                    if (this.container.base.vein.getType() == Type.OIL) {
                        stack = new ItemStack(IUItem.oilblock.getItem());
                    } else {
                        stack = new ItemStack(IUItem.gasBlock.getItem());
                    }
                }
                new ItemWidget(this, 32, 54, () -> stack).drawBackground(poseStack, guiLeft, guiTop);
            }
        }

    }


    public ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guiquarryvein.png");
    }

}
