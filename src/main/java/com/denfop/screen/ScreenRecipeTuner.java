package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.ButtonWidget;
import com.denfop.containermenu.ContainerMenuTunerRecipe;
import com.denfop.utils.Localization;
import net.minecraft.resources.ResourceLocation;

public class ScreenRecipeTuner<T extends ContainerMenuTunerRecipe> extends ScreenMain<ContainerMenuTunerRecipe> {

    public ScreenRecipeTuner(ContainerMenuTunerRecipe guiContainer) {
        super(guiContainer);
        this.addWidget(new ButtonWidget(
                this,
                60,
                20,
                105,
                16,
                container.base,
                0,
                Localization.translate("recipe_tuner.change_mode")
        ));
        this.addWidget(new ButtonWidget(this, 60, 40, 105, 16, container.base, 1, Localization.translate("recipe_tuner.write")));

    }


    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
