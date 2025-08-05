package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.bee.Product;
import com.denfop.api.bee.genetics.EnumGenetic;
import com.denfop.api.bee.genetics.GeneticTraits;
import com.denfop.api.gui.Area;
import com.denfop.api.gui.ScrollDirection;
import com.denfop.container.ContainerApiary;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class GuiApiary<T extends ContainerApiary> extends GuiIU<ContainerApiary> {


    public ContainerApiary container;
    int indexGenome = 0;
    int maxIndexGenome = 3;
    int indexAdditionProducts = 0;
    int maxIndexAdditionProducts = 3;

    public GuiApiary(ContainerApiary container1) {
        super(container1);
        this.container = container1;
        componentList.clear();
        this.imageWidth = 199;
        this.imageHeight = 207;
        this.addElement(new AdvArea(this, 10, 21, 21, 69).withTooltip(() -> ModUtils.getString(container1.base.food)));
        this.addElement(new AdvArea(this, 154, 21, 165, 69).withTooltip(() -> ModUtils.getString(container1.base.royalJelly)));
        this.addElement(new AdvArea(this, 37, 22, 50, 32).withTooltip(() -> ModUtils.getString(container1.base.workers)));
        this.addElement(new AdvArea(this, 37, 35, 50, 49).withTooltip(() -> ModUtils.getString(container1.base.doctors)));
        this.addElement(new AdvArea(this, 37, 51, 50, 64).withTooltip(() -> ModUtils.getString(container1.base.builders)));
        this.addElement(new AdvArea(this, 37, 67, 50, 80).withTooltip(() -> ModUtils.getString(container1.base.attacks)));
        this.addElement(new AdvArea(this, 37, 83, 50, 94).withTooltip(() -> ModUtils.getString(container1.base.ill)));
        this.addElement(new AdvArea(this,
                37,
                97,
                50,
                109).withTooltip(() -> ModUtils.getString(container1.base.birth) + "/" + ModUtils.getString(container1.base.death)));

    }

    @Override
    public boolean mouseScrolled(double d, double d2, double d4, double d3) {
        ScrollDirection scrollDirection = d3 != 0.0 ? (d3 < 0.0 ? ScrollDirection.down : ScrollDirection.up) : ScrollDirection.stopped;
        int mouseX = (int) (d - this.guiLeft);
        int mouseY = (int) (d2 - this.guiTop);
        if (mouseX >= 174 && mouseX <= 176 + 16 + 2 && mouseY >= 4 && mouseY <= 4 + 16 * (3.4)) {
            if (scrollDirection == ScrollDirection.up) {
                indexGenome--;
                if (indexGenome < 0) {
                    indexGenome = 0;
                }
            }
            if (scrollDirection == ScrollDirection.down) {
                indexGenome++;
                if (indexGenome > maxIndexGenome - 3) {
                    indexGenome = maxIndexGenome - 3;
                    indexGenome = Math.max(0, indexGenome);
                }
            }
        }
        if (mouseX >= 174 && mouseX <= 176 + 16 + 2 && mouseY >= 4 + 16 * (0 + 3.5) && mouseY <= 8 + 16 * (4) + 16 * (0 + 3.5)) {
            if (scrollDirection == ScrollDirection.up) {
                indexAdditionProducts--;
                if (indexAdditionProducts < 0) {
                    indexAdditionProducts = 0;
                }
            }
            if (scrollDirection == ScrollDirection.down) {
                indexAdditionProducts++;
                if (indexAdditionProducts > maxIndexAdditionProducts - 3) {
                    indexAdditionProducts = maxIndexAdditionProducts - 3;

                    indexAdditionProducts = Math.max(0, indexAdditionProducts);
                }
            }
        }

        return super.mouseScrolled(d, d2, d4,d3);
    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 3 && mouseX <= 13 && mouseY >= 3 && mouseY <= 13) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.bee.info"));
            List<String> compatibleUpgrades = new ArrayList<>();
            for (int i = 1; i < 21; i++) {
                compatibleUpgrades.add(Localization.translate("iu.bee.info" + i));
            }
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX - 160, mouseY, text);
        }
    }

    protected void drawForegroundLayer(GuiGraphics poseStack, int par1, int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        List<GeneticTraits> values = this.container.base.getGenome().getGeneticTraitsMap().values().stream().toList();
        maxIndexGenome = values.size();
        List<Product> products = this.container.base.getQueen().getProduct();
        maxIndexAdditionProducts = products.size();
        if (maxIndexGenome == 0) {
            this.addElement(new Area(this, 174, 4, 20, 4 + 8 + 16 * (4)).withTooltip(() -> Localization.translate("iu.apiary_genome_descriptions")));

        } else {
            int j = 0;
            for (int i = indexGenome; i < Math.min((indexGenome) + 3, maxIndexGenome); i++, j++) {
                int finalI = i;
                this.addElement(new Area(this, 176, 8 + 16 * (j), 16, 16).withTooltip(() ->Localization.translate("iu.info.bee_genome_"+ values.get(finalI).name().toLowerCase())));

            }

        }

        if (maxIndexAdditionProducts == 0) {
            this.addElement(new Area(this, 174, (int) (4 + 16 * (0 + 3.5)), 20, 4 + 8 + 16 * (4)).withTooltip(() -> Localization.translate("iu.apiary_additional_product_descriptions")));

        } else {
            int j = 0;
            for (int i = indexAdditionProducts; i < Math.min((indexAdditionProducts + 1) * 3, maxIndexAdditionProducts); i++, j++) {
                Product product = products.get(i);

                this.addElement(new Area(this, 176, (int) (8 + 16 * (j + 3.5)), 16, 16).withTooltip(() -> product.getCrop().getDrop().get(0).getDisplayName().getString()+"\n"+Localization.translate("iu.space_chance")+" " +ModUtils.getString( product.getChance()/3)+"%"));


            }
        }
        handleUpgradeTooltip(par1, par2);
    }

    @Override
    protected ResourceLocation getTexture() {
        return  ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guiapiary.png");
    }

    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        List<GeneticTraits> values = this.container.base.getGenome().getGeneticTraitsMap().values().stream().toList();
        maxIndexGenome = values.size();
        int j = 0;
        for (int i = indexGenome; i < Math.min((indexGenome) + 3, maxIndexGenome); i++, j++) {
            GeneticTraits geneticTraits = values.get(i);
            RenderSystem.enableBlend();
            poseStack.renderItem(new ItemStack(IUItem.genome_bee.getStack(geneticTraits.ordinal()), 1), 176 + this.guiLeft(), 8 + 16 * (j) + this.guiTop());
            RenderSystem.disableBlend();

        }
        j = 0;
        List<Product> products = this.container.base.getQueen().getProduct();
        maxIndexAdditionProducts = products.size();
        for (int i = indexAdditionProducts; i < Math.min((indexAdditionProducts + 1) * 3, maxIndexAdditionProducts); i++, j++) {
            Product product = products.get(i);
            RenderSystem.enableBlend();
            poseStack.renderItem(product.getCrop().getDrop().get(0), 176 + this.guiLeft(), (int) (8 + 16 * (j + 3.5) + this.guiTop()));
            RenderSystem.disableBlend();

        }
        bindTexture();
        switch (this.container.base.task) {
            case 0:
                this.drawTexturedModalRect(poseStack, this.guiLeft + 60, this.guiTop + 24, 249, 1, 6, 6);
                break;
            case 1:
                this.drawTexturedModalRect(poseStack, this.guiLeft + 60, this.guiTop + 71, 249, 1, 6, 6);
                break;
            case 2:
                this.drawTexturedModalRect(poseStack, this.guiLeft + 60, this.guiTop + 39, 249, 1, 6, 6);
                break;
            case 3:
                this.drawTexturedModalRect(poseStack, this.guiLeft + 60, this.guiTop + 55, 249, 1, 6, 6);
                break;
        }
        if (this.container.base.deathTask == 1) {
            this.drawTexturedModalRect(poseStack, this.guiLeft + 60, this.guiTop + 101, 249, 1, 6, 6);
        }

        if (this.container.base.illTask == 1) {
            this.drawTexturedModalRect(poseStack, this.guiLeft + 60, this.guiTop + 86, 249, 1, 6, 6);
        }
        int renderHeight = (int) (49 * ModUtils.limit(
                this.container.base.food / (this.container.base.maxFood * 1D),
                0.0D,
                1.0D
        ));
        this.drawTexturedModalRect(
                poseStack, this.guiLeft + 10,
                this.guiTop + 20 + 50 - renderHeight,
                236,
                50 - renderHeight,
                12,
                renderHeight
        );
        renderHeight = (int) (49 * ModUtils.limit(
                this.container.base.royalJelly / (this.container.base.maxJelly * 1D),
                0.0D,
                1.0D
        ));
        this.drawTexturedModalRect(
                poseStack, this.guiLeft + 154,
                this.guiTop + 20 + 50 - renderHeight,
                223,
                50 - renderHeight,
                12,
                renderHeight
        );
        bindTexture( ResourceLocation.tryBuild("industrialupgrade", "textures/gui/infobutton.png"));
        this.drawTexturedRect(poseStack, 3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
    }

}
