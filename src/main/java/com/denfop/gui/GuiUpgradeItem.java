package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.ImageInterface;
import com.denfop.api.gui.ItemStackImage;
import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.container.ContainerHeldUpgradeItem;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiUpgradeItem extends GuiIU<ContainerHeldUpgradeItem> {

    private static final ResourceLocation background = new ResourceLocation(Constants.TEXTURES, "textures/gui/guiblacklist.png");
    final List<ItemStack> list;
    private final String name;


    public GuiUpgradeItem(ContainerHeldUpgradeItem container, final ItemStack itemStack1) {
        super(container);

        this.name = itemStack1.getDisplayName();
        this.ySize = 125;
        this.list = ModUtils.get_blacklist_block();
        final List<String> list2 = UpgradeSystem.system.getBlackList(itemStack1);
        for (String name : list2) {
            list.add(OreDictionary.getOres(name).get(0));
        }
        this.componentList.clear();
        this.addElement(new ImageInterface(this, 0, 0, xSize, ySize));
        for (int i = 0; i < this.list.size(); i++) {
            int y = i / 9;
            int x = i % 9;
            ItemStack stack = this.list.get(i);
            this.addElement(new ItemStackImage(this, 8 + x * 18, 40 + y * 18, () -> stack));
        }
    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);
        GlStateManager.color(1, 1, 1, 1);
        this.fontRenderer.drawString(this.name, (this.xSize - this.fontRenderer.getStringWidth(this.name)) / 2, 11, 0);
        this.fontRenderer.drawString(
                Localization.translate("iu.blacklist_description"),
                (this.xSize - this.fontRenderer.getStringWidth(Localization.translate("iu.blacklist_description"))) / 2,
                21,
                0
        );

    }

    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);


    }

    protected ResourceLocation getTexture() {
        return background;
    }

}
