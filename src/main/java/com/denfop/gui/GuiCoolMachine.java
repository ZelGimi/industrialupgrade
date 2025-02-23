package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.componets.ComponentButton;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.container.ContainerCoolMachine;
import com.denfop.tiles.mechanism.cooling.TileCooling;
import com.denfop.utils.ListInformationUtils;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuiCoolMachine extends GuiIU<ContainerCoolMachine> {

    public ContainerCoolMachine container;
    public String name;

    public GuiCoolMachine(ContainerCoolMachine guiContainer) {
        super(guiContainer);
        this.container = guiContainer;
        this.name = Localization.translate(guiContainer.base.getName());
        this.addComponent(new GuiComponent(this, 3, 14, EnumTypeComponent.SOUND_BUTTON,
                new Component<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.componentList.add(new GuiComponent(this, 113, 21, EnumTypeComponent.ENERGY_HEIGHT,
                new Component<>(this.container.base.energy)
        ));

        this.componentList.add(new GuiComponent(this, 51, 41, EnumTypeComponent.COOL_ENERGY_WEIGHT,
                new Component<>(this.container.base.cold)
        ));
        this.componentList.add(new GuiComponent(this, 20, 37, EnumTypeComponent.WORK_BUTTON,
                new Component<>(new ComponentButton(this.container.base, 2, "") {
                    @Override
                    public String getText() {
                        return ((TileCooling) this.getEntityBlock()).work ? Localization.translate("turn_off") :
                                Localization.translate("turn_on");
                    }

                    @Override
                    public boolean active() {
                        return !((TileCooling) this.getEntityBlock()).work;
                    }
                })
        ));
        this.componentList.add(new GuiComponent(this, 53, 60, EnumTypeComponent.PLUS_BUTTON,
                new Component<>(new ComponentButton(this.container.base, 0, "") {
                    @Override
                    public String getText() {
                        return "+4";
                    }

                })
        ));
        this.componentList.add(new GuiComponent(this, 78, 60, EnumTypeComponent.MINUS_BUTTON,
                new Component<>(new ComponentButton(this.container.base, 1, "") {
                    @Override
                    public String getText() {
                        return "-4";
                    }

                })
        ));
    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 3 && mouseX <= 15 && mouseY >= 3 && mouseY <= 15) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.cool_storage.info_main"));
            List<String> compatibleUpgrades = ListInformationUtils.cooling;
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX, mouseY, text);
        }
    }

    protected void mouseClicked(int i, int j, int k) throws IOException {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = i - xMin;
        int y = j - yMin;

    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);
        handleUpgradeTooltip(par1, par2);
        this.fontRenderer.drawString(this.name, (this.xSize - this.fontRenderer.getStringWidth(this.name)) / 2, 6, 4210752);


    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(getTexture());
        int xOffset = (this.width - this.xSize) / 2;
        int yOffset = (this.height - this.ySize) / 2;
        this.mc.getTextureManager()
                .bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/infobutton.png"));
        drawTexturedModalRect(xOffset + 3, yOffset + 3, 0, 0, 10, 10);
        this.mc.getTextureManager().bindTexture(getTexture());


    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");


    }


}
