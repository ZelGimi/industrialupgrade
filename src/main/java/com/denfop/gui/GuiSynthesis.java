package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.container.ContainerDoubleElectricMachine;
import com.denfop.tiles.mechanism.dual.TileSynthesis;
import com.denfop.utils.ModUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Collections;

@OnlyIn(Dist.CLIENT)
public class GuiSynthesis<T extends ContainerDoubleElectricMachine> extends GuiIU<ContainerDoubleElectricMachine> {

    public final ContainerDoubleElectricMachine container;

    public GuiSynthesis(ContainerDoubleElectricMachine container1) {
        super(container1);
        this.container = container1;
        this.componentList.clear();
        this.addComponent(new GuiComponent(this, 3, 14, EnumTypeComponent.SOUND_BUTTON,
                new Component<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addComponent(new GuiComponent(this, 9, 63, EnumTypeComponent.RAD,
                new Component<>(((TileSynthesis) this.container.base).rad_energy)
        ));
        this.addComponent(new GuiComponent(this, 6, 34, EnumTypeComponent.ENERGY,
                new Component<>(((TileSynthesis) this.container.base).energy)
        ));
        componentList.add(new GuiComponent(this, 0, 0, getComponent(),
                new Component<>(new ComponentRenderInventory(
                        EnumTypeComponentSlot.SLOT,
                        Collections.singletonList(this.container.base.inputSlotA)
                ))
        ));
        componentList.add(new GuiComponent(this, 0, 0, getComponent(),
                new Component<>(new ComponentRenderInventory(
                        EnumTypeComponentSlot.SLOT,
                        Collections.singletonList(((TileSynthesis) this.container.base).input_slot)
                ))
        ));
    }

    @Override
    protected void drawForegroundLayer(GuiGraphics stack, final int mouseX, final int mouseY) {
        super.drawForegroundLayer(stack, mouseX, mouseY);

    }

    protected void renderBg(GuiGraphics stack, float f, int x, int y) {
        super.renderBg(stack, f, x, y);
        bindTexture(getTexture());
        int progress = (int) (13 * this.container.base.getProgress());
        int progress1 = (int) (24 * this.container.base.getProgress());
        int xoffset = this.guiLeft();
        int yoffset = guiTop();


        if (progress > 0) {
            drawTexturedModalRect(stack, xoffset + 42, yoffset + 35, 177, 33, progress + 1, 14);
        }
        if (progress1 > 0) {
            drawTexturedModalRect(stack, xoffset + 78, yoffset + 33, 177, 52, progress1 + 1, 23);
        }

        final MachineRecipe output = this.container.base.output;
        if (output != null) {
           draw(stack,
                    ChatFormatting.GREEN + Localization.translate("chance") + output.getRecipe().output.metadata.getInt(
                            "percent") + "%", xoffset + 66,
                    yoffset + 66, ModUtils.convertRGBcolorToInt(217, 217, 217)
            );
        }
    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/GuiSynthesis.png".toLowerCase());
    }

}
