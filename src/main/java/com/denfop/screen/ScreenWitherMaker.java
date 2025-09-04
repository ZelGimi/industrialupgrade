package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.containermenu.ContainerMenuBaseWitherMaker;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenWitherMaker<T extends ContainerMenuBaseWitherMaker> extends ScreenMain<ContainerMenuBaseWitherMaker> {

    public final ContainerMenuBaseWitherMaker container;

    public ScreenWitherMaker(
            ContainerMenuBaseWitherMaker container1
    ) {
        super(container1);
        this.container = container1;
        this.componentList.clear();
        this.addComponent(new ScreenWidget(this, 6, 65, EnumTypeComponent.SOUND_BUTTON,
                new WidgetDefault<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addComponent(new ScreenWidget(this, 122, 63, EnumTypeComponent.ENERGY,
                new WidgetDefault<>(this.container.base.energy)
        ));
    }

    @Override
    protected void drawForegroundLayer(PoseStack poseStack, final int mouseX, final int mouseY) {
        super.drawForegroundLayer(poseStack, mouseX, mouseY);

    }

    protected void renderBg(PoseStack poseStack, float f, int x, int y) {
        super.renderBg(poseStack, f, x, y);

        int progress = (int) (22 * this.container.base.componentProgress.getBar());
        bindTexture(getTexture());


        if (progress > 0) {
            drawTexturedModalRect(poseStack, guiLeft() + 77, guiTop() + 35, 177, 0, progress, 18);
        }

    }

    public String getName() {
        return "";
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/GuiWitherMaker.png".toLowerCase());
    }

}
