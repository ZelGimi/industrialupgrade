package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.Recipes;
import com.denfop.container.ContainerDoubleElectricMachine;
import com.denfop.items.modules.UpgradeModule;
import com.denfop.utils.EnumInfoUpgradeModules;
import com.denfop.utils.ModUtils;
import ic2.api.recipe.RecipeOutput;
import ic2.core.GuiIC2;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.denfop.events.IUEventHandler.getUpgradeItem;

@SideOnly(Side.CLIENT)
public class GUIUpgradeBlock extends GuiIC2<ContainerDoubleElectricMachine> {

    public final ContainerDoubleElectricMachine container;

    public GUIUpgradeBlock(ContainerDoubleElectricMachine container1) {
        super(container1);
        this.container = container1;
    }


    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(getTexture());
        int chargeLevel = (int) (14.0F * this.container.base.getChargeLevel());
        int progress = (int) (31 * this.container.base.getProgress());
        int progress1 = (int) (27 * this.container.base.getProgress());
        final int xoffset = (this.width - this.xSize) / 2;
        final int yoffset = (this.height - this.ySize) / 2;
        if (chargeLevel > 0) {
            drawTexturedModalRect(xoffset + 24, yoffset + 56 + 14 - chargeLevel, 176, 14 - chargeLevel,
                    14, chargeLevel
            );
        }
        ItemStack module = getUpgradeItem(this.container.base.inputSlotA.get(0))
                ? this.container.base.inputSlotA.get(0)
                : this.container.base.inputSlotA.get(1);
        ItemStack stack1 = getUpgradeItem(this.container.base.inputSlotA.get(0))
                ? this.container.base.inputSlotA.get(1)
                : this.container.base.inputSlotA.get(0);

        boolean allow = true;
        NBTTagCompound nbt1 = ModUtils.nbt(stack1);
        RecipeOutput output = Recipes.upgrade.getOutputFor(
                this.container.base.inputSlotA.get(0),
                this.container.base.inputSlotA.get(1),
                false,
                false
        );

        if (output != null) {
            if (!nbt1.getString("mode_module" + 3).isEmpty()) {
                allow = false;
            }
            if (module.getItem() instanceof UpgradeModule) {
                EnumInfoUpgradeModules type = UpgradeModule.getType(module.getItemDamage());
                int min = 0;
                for (int i = 0; i < 4; i++) {
                    if (nbt1.getString("mode_module" + i).equals(type.name)) {
                        min++;
                    }
                }
                if (min >= type.max) {
                    allow = false;
                }
                if (allow) {
                    if (progress > 0) {
                        drawTexturedModalRect(xoffset + 31, yoffset + 36, 176, 17, progress + 1, 11);
                    }
                    if (progress1 > 0) {
                        drawTexturedModalRect(xoffset + 81, yoffset + 34, 176, 29, progress1 + 1, 15);
                    }
                } else {
                    drawTexturedModalRect(xoffset + 31, yoffset + 34, 176, 48, 31, 15);
                    drawTexturedModalRect(xoffset + 81, yoffset + 34, 177, 69, 27, 16);


                }
            } else {
                if (progress > 0) {
                    drawTexturedModalRect(xoffset + 31, yoffset + 36, 176, 17, progress + 1, 11);
                }
                if (progress1 > 0) {
                    drawTexturedModalRect(xoffset + 81, yoffset + 34, 176, 29, progress1 + 1, 15);
                }
            }
        }
    }

    public String getName() {
        return this.container.base.getInventoryName();
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/GUIUpgradeBlock.png");
    }

}
