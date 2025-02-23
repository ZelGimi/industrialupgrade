package com.quantumgenerators;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.GuiElement;
import com.denfop.api.gui.GuiSlider;
import com.denfop.api.gui.ImageScreen;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.gui.AdvArea;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiIU;
import com.denfop.network.packet.PacketColorPicker;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.render.streak.PlayerStreakInfo;
import com.denfop.render.streak.RGB;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiCheckBox;

import javax.annotation.Nonnull;
import java.io.IOException;


public class GuiQG extends GuiIU<ContainerQG> implements GuiPageButtonList.GuiResponder, GuiSlider.FormatHelper{

    public final ContainerQG container;

    public GuiQG(ContainerQG container1) {
        super(container1);
        this.container = container1;
        this.ySize = 200;
        this.addElement(new ImageScreen(this,21,23,133,21));
        componentList.clear();
        inventory = new GuiComponent(this, 7, 113, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.ALL))
        );
        this.slots = new GuiComponent(this, 0, 0, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS_UPGRADE))
        );

        componentList.add(inventory);
        componentList.add(slots);
    }
    @Override
    protected void actionPerformed(@Nonnull GuiButton guibutton) throws IOException {
        super.actionPerformed(guibutton);
        if (guibutton instanceof GuiSlider) {
            GuiSlider slider = (GuiSlider) guibutton;
            this.setEntryValue(guibutton.id,slider.getSliderValue());
        }

    }
    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.add(new GuiSlider(this, 0, (this.width - this.xSize) / 2 + 21, (this.height - this.ySize) / 2 + 72,
               "",
                (float) 0, (float) this.container.base.genmax, (float) this.container.base.gen, this,152-18
        ));



    }
    @Override
    protected void drawForegroundLayer(final int mouseX, final int mouseY) {
        super.drawForegroundLayer(mouseX, mouseY);

        String generatingString = Localization.translate("gui.SuperSolarPanel.generating") + ": ";
        this.fontRenderer.drawString(TextFormatting.GREEN + generatingString + ModUtils.getString(this.container.base.gen) + " " +
                        "QE/t", 36
                , 30, ModUtils.convertRGBcolorToInt(217, 217, 217));
     }

    protected void mouseClicked(int i, int j, int k) throws IOException {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = i - xMin;
        int y = j - yMin;

    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
      super.drawGuiContainerBackgroundLayer(f,x,y);

    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine_main1.png");
    }

    @Override
    public String getText(final int var1, final String var2, final float var3) {
        return "";
    }

    @Override
    public void setEntryValue(final int i, final boolean b) {

    }

    @Override
    public void setEntryValue(final int i, final float v) {
        new PacketUpdateServerTile(this.container.base, v);

    }

    @Override
    public void setEntryValue(final int i, final String s) {

    }

}
