package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.ImageInterface;
import com.denfop.componets.ComponentButton;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.container.ContainerMagnet;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiMagnet extends GuiIU<ContainerMagnet> {

    public final ContainerMagnet container;

    public GuiMagnet(ContainerMagnet container1) {
        super(container1);
        this.container = container1;
        this.inventory.setY(this.inventory.getY() + 20);
        this.addComponent(new GuiComponent(this, 7, 14, EnumTypeComponent.SOUND_BUTTON,
                new Component<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addComponent(new GuiComponent(
                this,
                147,
                27,
                EnumTypeComponent.ENERGY_HEIGHT,
                new Component<>(this.container.base.energy)
        ));
        this.xSize = 230;
        this.ySize = 190;
        this.addElement(new ImageInterface(this, 0, 0, this.xSize, this.ySize));
        this.componentList.add(new GuiComponent(this, 10, 85, EnumTypeComponent.PLUS_BUTTON,
                new Component<>(new ComponentButton(this.container.base, 1, "") {
                    @Override
                    public String getText() {
                        return "x: +1";
                    }

                })
        ));
        this.componentList.add(new GuiComponent(this, 35, 85, EnumTypeComponent.MINUS_BUTTON,
                new Component<>(new ComponentButton(this.container.base, 0, "") {
                    @Override
                    public String getText() {
                        return "x: -1";
                    }

                })
        ));

        this.componentList.add(new GuiComponent(this, 60, 85, EnumTypeComponent.PLUS_BUTTON,
                new Component<>(new ComponentButton(this.container.base, 3, "") {
                    @Override
                    public String getText() {
                        return "y: +1";
                    }

                })
        ));
        this.componentList.add(new GuiComponent(this, 85, 85, EnumTypeComponent.MINUS_BUTTON,
                new Component<>(new ComponentButton(this.container.base, 2, "") {
                    @Override
                    public String getText() {
                        return "y: -1";
                    }

                })
        ));
        this.componentList.add(new GuiComponent(this, 110, 85, EnumTypeComponent.PLUS_BUTTON,
                new Component<>(new ComponentButton(this.container.base, 5, "") {
                    @Override
                    public String getText() {
                        return "z: +1";
                    }

                })
        ));
        this.componentList.add(new GuiComponent(this, 135, 85, EnumTypeComponent.MINUS_BUTTON,
                new Component<>(new ComponentButton(this.container.base, 4, "") {
                    @Override
                    public String getText() {
                        return "z: -1";
                    }

                })
        ));

        this.addElement(new AdvArea(
                this,
                175,
                18,
                175 + 18,
                18 + 9 * 18
        ).withTooltip(Localization.translate("iu.blacklist_tube")));
        this.addElement(new AdvArea(
                this,
                202,
                18,
                202 + 18,
                18 + 9 * 18
        ).withTooltip(Localization.translate("iu.whitelist_tube")));

    }

    @Override
    protected void drawForegroundLayer(final int mouseX, final int mouseY) {
        super.drawForegroundLayer(mouseX, mouseY);
        this.fontRenderer.drawString(String.valueOf(this.container.base.x), 26, 87,
                4210752
        );
        this.fontRenderer.drawString(String.valueOf(this.container.base.y), 76, 87,
                4210752
        );
        this.fontRenderer.drawString(String.valueOf(this.container.base.z), 126, 87,
                4210752
        );
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);

    }


    public String getName() {
        return null;
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/guimachine.png");
    }

}
