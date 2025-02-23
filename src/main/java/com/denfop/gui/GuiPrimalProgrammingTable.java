package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.container.ContainerPrimalProgrammingTable;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.world.WorldBaseGen;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiPrimalProgrammingTable extends GuiIU<ContainerPrimalProgrammingTable> {

    public final ContainerPrimalProgrammingTable container;
    int pointer;
    int prevPointer;

    boolean hover;
    public GuiPrimalProgrammingTable(
            ContainerPrimalProgrammingTable container1
    ) {
        super(container1);
        this.container = container1;
        this.componentList.clear();

    }

    @Override
    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);
        hover = par1 >= 7 && par2 >= 62 && par1 <= 18 && par2 <= 73;
        handleUpgradeTooltip(par1, par2);
    }
    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 3 && mouseX <= 13 && mouseY >= 3 && mouseY <= 13) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("programming_table.info"));
            List<String> compatibleUpgrades = new ArrayList<>();
            for (int i = 1; i < 9; i++) {
                compatibleUpgrades.add(Localization.translate("programming_table.info" + i));
            }
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX-60, mouseY, text);
        }
    }
    @Override
    protected void mouseClicked(final int i, final int j, final int k) throws IOException {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = i - xMin;
        int y = j - yMin;
        if (container.base.start) {
            if (x >= 7 && y >= 62 && x <= 18 && y <= 73) {
               final int pointer1 = pointer - 20;
                if (pointer1 > 0) {
                    int value = container.base.data[pointer1% container.base.data.length];
                    if (value == 2) {
                        value = 0;
                    } else if (value == 0) {
                        value = 1;
                    } else if (value == 3) {
                        value = 2;
                    } else {
                        value = -1;
                    }
                    if (value != -1) {
                        this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(
                                SoundEvents.UI_BUTTON_CLICK,
                                1.0F
                        ));
                        new PacketUpdateServerTile(this.container.base, value);
                        pointer = WorldBaseGen.random.nextInt(146) + 20;
                    }
                }
            }
        }
    }

    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }
    public void updateTickInterface(){
        if (pointer < 20) {
            pointer = 20;
            prevPointer = 0;
        } else if (pointer > 165) {
            pointer = 165;
            prevPointer = 1;
        }

            if (pointer < 20) {
                pointer = 20;
                prevPointer = 0;
            } else if (pointer >= 166) {
                pointer = 165;
                prevPointer = 1;
            }
            if (prevPointer == 0) {
                pointer++;
            } else {
                pointer--;
            }

    };
    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        int xoffset = (this.width - this.xSize) / 2;
        int yoffset = (this.height - this.ySize) / 2;
        this.mc.getTextureManager().bindTexture(getTexture());
        int progress = (int) (34.0F * this.container.base.componentProgress.getBar());
        this.drawTexturedModalRect(this.guiLeft + 85, this.guiTop + 24, 177, 1, progress, 20);

        if (hover){
            this.drawTexturedModalRect(this.guiLeft + 7, this.guiTop + 62, 177, 32, 12, 12);
        }
        if (container.base.start) {
            int k = 0;
            for (int i : container.base.data) {
                if (i == 1) {
                    k++;
                    continue;
                }
                int y1 = 0;
                if (i == 0) {
                    y1 = 167;
                }
                if (i == 2) {
                    y1 = 177;
                }
                if (i == 3) {
                    y1 = 172;
                }
                this.drawTexturedModalRect(this.guiLeft + 20 + k, this.guiTop + 66, 1 + k, y1, 1, 4);
                k++;
            }
            this.drawTexturedModalRect(this.guiLeft + pointer - 1, this.guiTop + 68, 177, 25, 4, 4);

        }
        this.mc.getTextureManager().bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
        this.drawTexturedRect(3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);

    }

    public String getName() {
        return "";
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/guiprogrammingtable.png");
    }

}
