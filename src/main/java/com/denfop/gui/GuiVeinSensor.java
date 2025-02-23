package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Area;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.ImageInterface;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.container.ContainerVeinSensor;
import com.denfop.items.DataOres;
import com.denfop.utils.Vector2;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.denfop.items.ItemVeinSensor.getOreColor;

@SideOnly(Side.CLIENT)
public class GuiVeinSensor extends GuiIU<ContainerVeinSensor> {

    private static final ResourceLocation background = new ResourceLocation(Constants.TEXTURES, "textures/gui/GUIBags.png");
    private final String name;
    int[][] colors;

    public GuiVeinSensor(ContainerVeinSensor container, final ItemStack itemStack1) {
        super(container);

        this.name = Localization.translate(itemStack1.getUnlocalizedName());
        this.ySize = 242;
        this.componentList.clear();
        this.slots = new GuiComponent(this, 0, 0, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.DEFAULT))
        );
        colors = new int[9 * 16][9 * 16];
        final List<Map<Vector2, DataOres>> list = new ArrayList<>(container.base.getMap().values());
        for (Map<Vector2, DataOres> map : list) {
            for (Map.Entry<Vector2, DataOres> entry : map.entrySet()) {
                colors[entry.getKey().getX() - container.base.getVector().getX()][entry.getKey().getZ() - container.base
                        .getVector()
                        .getZ()] = entry.getValue().getColor();
                int meta = entry.getValue().getBlockState().getBlock().getMetaFromState(entry.getValue().getBlockState());
                ItemStack stack = new ItemStack(entry.getValue().getBlockState().getBlock(), 1, meta);
                int color = getOreColor(entry.getValue().getBlockState());
                if (color != 0xFFFFFFFF) {
                    this.addElement(new Area(
                            this,
                            20 + entry.getKey().getX() - container.base.getVector().getX(),
                            10 + entry.getKey().getZ() - container.base
                                    .getVector()
                                    .getZ(),
                            1,
                            1
                    ).withTooltip(() -> stack.getDisplayName() + "\n" + "X: " + entry.getKey().getX() + "\n" + "Z: " + entry
                            .getKey()
                            .getZ()));
                }
            }
        }
        componentList.add(slots);
        this.addElement(new ImageInterface(this, 0, 0, xSize, ySize));
    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        for (int i = 0; i < 9 * 16; i++) {
            for (int j = 0; j < 9 * 16; j++) {
                this.drawColoredRect(20 + i,
                        10 + j, 1, 1,
                        colors[i][j]
                );
            }
        }
    }

    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

    }

    protected ResourceLocation getTexture() {
        return background;
    }

}
