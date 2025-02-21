package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.CustomButton;
import com.denfop.api.gui.FluidItem;
import com.denfop.container.ContainerSmelteryController;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.tiles.smeltery.ITank;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class GuiSmelteryController extends GuiIU<ContainerSmelteryController> {

    List<FluidTank> fluidTanks = new ArrayList<>();
    List<FluidTank> fluidTanks1 = new ArrayList<>();
    List<Integer> integerList = new ArrayList<>();

    public GuiSmelteryController(ContainerSmelteryController guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.ySize = 181;
        int i = 0;
        for (ITank tank : guiContainer.base.listTank) {
            fluidTanks.add(tank.getTank());
            integerList.add(i);
            i++;
        }
        this.addElement(new CustomButton(this, 8, 58, 162, 20, container.base, -1, "") {
            @Override
            public String getText() {
                return Localization.translate("iu.mix");
            }

            @Override
            public boolean visible() {
                return container.base.list.size() > 1;
            }
        });
        this.addElement(new CustomButton(this, 8, 58, 162, 20, container.base, -3, "") {
            @Override
            public String getText() {
                return Localization.translate("iu.separate");
            }

            @Override
            public boolean visible() {
                return container.base.list.size() == 1;
            }
        });
        this.addElement(new CustomButton(this, 8, 78, 162, 20, container.base, -2, "") {
            @Override
            public String getText() {
                return Localization.translate("iu.mix_max");
            }

            @Override
            public boolean visible() {
                return container.base.list.size() > 1;
            }
        });
    }
    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 3 && mouseX <= 13 && mouseY >= 3 && mouseY <= 13) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("smeltery_info"));
            List<String> compatibleUpgrades = new ArrayList<>();
            for (int i = 1; i < 10; i++) {
                compatibleUpgrades.add(Localization.translate("smeltery_info" + i));
            }
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX-60, mouseY, text);
        }
    }
    @Override
    protected void mouseClicked(int i, final int j, final int k) throws IOException {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = i - xMin;
        int y = j - yMin;
        for (i = 0; i < fluidTanks1.size(); i++) {
            if (x >= 8 + (i % 9) * 18 && x < 8 + ((i % 9) + 1) * 18 && y >= 20 + (i / 9) * 18 && y < 20 + ((i / 9) + 1) * 18) {
                new PacketUpdateServerTile(this.container.base, i);
            }
        }
    }

    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
        tanksRefresh();
        handleUpgradeTooltip(par1, par2);
        for (int i = 0; i < fluidTanks1.size(); i++) {
            new FluidItem(this, 8 + (i % 9) * 18, 20 + (i / 9) * 18, fluidTanks1.get(i).getFluid()).drawForeground(par1, par2);
        }
    }

    private void tanksRefresh() {
        fluidTanks1 = fluidTanks.stream()
                .sorted((tank1, tank2) -> {
                    FluidStack fluid1 = tank1.getFluid();
                    FluidStack fluid2 = tank2.getFluid();

                    boolean hasFluid1 = fluid1 != null && fluid1.amount > 0;
                    boolean hasFluid2 = fluid2 != null && fluid2.amount > 0;
                    if (hasFluid1 && !hasFluid2) {
                        return -1;
                    } else if (!hasFluid1 && hasFluid2) {
                        return 1;
                    }


                    return 0;
                })
                .collect(Collectors.toList());

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        this.bindTexture();
        GlStateManager.color(1, 1, 1, 1);

        for (int i = 0; i < fluidTanks1.size(); i++) {
            this.bindTexture();
            drawTexturedModalRect(this.guiLeft + 8 + (i % 9) * 18, guiTop + 20 + (i / 9) * 18, 178, 83, 18, 18);
            new FluidItem(this, 8 + (i % 9) * 18, 20 + (i / 9) * 18, fluidTanks1.get(i).getFluid()).drawBackground(
                    this.guiLeft,
                    guiTop
            );
        }
        this.mc.getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/guiantiupgrade.png"));
        for (int i = 0; i < container.base.list.size(); i++) {
            int index = container.base.list.get(i);
            drawTexturedModalRect(this.guiLeft + 8 + (index % 9) * 18, guiTop + 20 + (index / 9) * 18, 200,
                    11, 18, 18
            );
        }
        this.mc.getTextureManager().bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
        this.drawTexturedRect(3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);

    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guismeltery.png");
    }

}
