package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.CustomButton;
import com.denfop.container.ContainerTunerRecipe;
import net.minecraft.resources.ResourceLocation;

public class GuiRecipeTuner<T extends ContainerTunerRecipe> extends GuiIU<ContainerTunerRecipe> {

    public GuiRecipeTuner(ContainerTunerRecipe guiContainer) {
        super(guiContainer);
        this.addElement(new CustomButton(
                this,
                60,
                20,
                105,
                16,
                container.base,
                0,
                Localization.translate("recipe_tuner.change_mode")
        ));
        this.addElement(new CustomButton(this, 60, 40, 105, 16, container.base, 1, Localization.translate("recipe_tuner.write")));

    }


    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
