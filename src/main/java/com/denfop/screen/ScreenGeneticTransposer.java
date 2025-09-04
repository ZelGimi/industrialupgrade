package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.upgrades.BlockEntityUpgrade;
import com.denfop.api.upgrades.BlockEntityUpgradeManager;
import com.denfop.api.upgrades.EnumBlockEntityUpgrade;
import com.denfop.api.upgrades.UpgradeItem;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.TankWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.containermenu.ContainerMenuGeneticTransposer;
import com.denfop.utils.Localization;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class ScreenGeneticTransposer<T extends ContainerMenuGeneticTransposer> extends ScreenMain<ContainerMenuGeneticTransposer> {

    public final ContainerMenuGeneticTransposer container;

    public ScreenGeneticTransposer(ContainerMenuGeneticTransposer container1) {
        super(container1);
        this.container = container1;
        componentList.clear();
        this.inventoryList.add(container.base.outputSlot);
        inventory = new ScreenWidget(this, 7, 83, getComponent(),
                new WidgetDefault<>(new ComponentRenderInventory(EnumTypeComponentSlot.ALL))
        );
        this.slots = new ScreenWidget(this, 0, 0, getComponent(),
                new WidgetDefault<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS_UPGRADE, this.inventoryList))
        );

        componentList.add(inventory);
        componentList.add(slots);
        this.addComponent(new ScreenWidget(this, 80, 35, EnumTypeComponent.PROCESS,
                new WidgetDefault<>(this.container.base.componentProgress)
        ));
        this.addComponent(new ScreenWidget(this, 117, 60, EnumTypeComponent.SOUND_BUTTON,
                new WidgetDefault<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));

        this.addComponent(new ScreenWidget(this, 58, 62, EnumTypeComponent.ENERGY, new WidgetDefault<>(this.container.base.energy)));

        this.addWidget(TankWidget.createNormal(this, 6, 5, container.base.fluidTank));
    }

    private static List<ItemStack> getCompatibleUpgrades(BlockEntityUpgrade block) {
        List<ItemStack> ret = new ArrayList<>();
        Set<EnumBlockEntityUpgrade> properties = block.getUpgradableProperties();

        for (final ItemStack stack : BlockEntityUpgradeManager.getUpgrades()) {
            UpgradeItem item = (UpgradeItem) stack.getItem();
            if (item.isSuitableFor(stack, properties)) {
                ret.add(stack);
            }
        }

        return ret;
    }

    protected void drawForegroundLayer(PoseStack poseStack, int par1, int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);

        this.handleUpgradeTooltip(par1, par2);
    }

    public void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 0 && mouseX <= 12 && mouseY >= 0 && mouseY <= 12) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate(Constants.ABBREVIATION + ".generic.text.upgrade"));

            for (final ItemStack stack : getCompatibleUpgrades(this.container.base)) {
                text.add(stack.getDisplayName().getString());
            }

            this.drawTooltip(mouseX, mouseY, text);
        }
    }

    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);
        bindTexture(getTexture());

        bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
        this.drawTexturedRect(poseStack, 3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);

    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/guimachine.png");
    }

}
