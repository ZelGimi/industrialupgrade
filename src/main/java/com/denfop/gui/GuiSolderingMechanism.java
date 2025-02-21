package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.container.ContainerSolderingMechanism;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.world.WorldBaseGen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiSolderingMechanism extends GuiIU<ContainerSolderingMechanism> {

    public final ContainerSolderingMechanism container;
    int pointer = 93;
    int prevPointer;
    int prevPointer1;
    int data[] = new int[156];
    int tick = 0;
    private int startPoint;

    public GuiSolderingMechanism(
            ContainerSolderingMechanism container1
    ) {
        super(container1);
        this.container = container1;
        this.componentList.clear();

    }
    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 3 && mouseX <= 13 && mouseY >= 3 && mouseY <= 13) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("soldering.info"));
            List<String> compatibleUpgrades = new ArrayList<>();
            for (int i = 1; i < 11; i++) {
                compatibleUpgrades.add(Localization.translate("soldering.info" + i));
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
    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);
        handleUpgradeTooltip(par1, par2);
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

            }
        }
    }

    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }
    public void updateTickInterface() {
        final boolean left = Keyboard.isKeyDown(Keyboard.KEY_LEFT);
        final boolean right = Keyboard.isKeyDown(Keyboard.KEY_RIGHT);
        if (left || right) {
            if (left) {
                pointer--;
                if (pointer < 10) {
                    pointer = 165;
                }

            }
            if (right) {
                pointer++;
                if (pointer >= 166) {
                    pointer = 10;
                }
            }


        }
        int k;
        if (tick % 10 == 0) {
            if (prevPointer == 0) {
                if (WorldBaseGen.random.nextInt(2) == 0) {
                    prevPointer = WorldBaseGen.random.nextInt(5) * -1;
                } else {
                    prevPointer = WorldBaseGen.random.nextInt(5);
                }
            }
            if (prevPointer < 0) {
                prevPointer1--;
                prevPointer++;
            } else {
                prevPointer--;
                prevPointer1++;
            }
        }
        if (tick % 20 == 0) {
            k = (pointer - 10);
            int value = data[k];
            if (value == 2) {
                value = 0;
            } else if (value == 0) {
                value = 1;
            } else {
                value = 2;
            }
            new PacketUpdateServerTile(this.container.base, value);
        }
        tick++;
    }
    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        int xoffset = (this.width - this.xSize) / 2;
        int yoffset = (this.height - this.ySize) / 2;
        this.mc.getTextureManager().bindTexture(getTexture());
        int progress = (int) (34.0F * this.container.base.componentProgress.getBar());
        this.drawTexturedModalRect(this.guiLeft + 85, this.guiTop + 24, 177, 1, progress, 20);
        this.drawTexturedModalRect(this.guiLeft + 93, this.guiTop + 45, 56, 16, 18, 18);


        if (container.base.start) {
            int k = 0;
            for (int i : container.base.data) {
                if (i == 1) {
                    k++;
                    int pos;
                    if (k + prevPointer1 < 0) {
                        pos = 156 + k + prevPointer1;
                    } else if (k + prevPointer1 >= 156) {
                        pos = (k + prevPointer1) % 156;
                    } else {
                        pos = k + prevPointer1;
                    }
                    data[Math.abs(pos)] = i;
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
                int pos;
                if (k + prevPointer1 < 0) {
                    pos = 156 + k + prevPointer1;
                } else if (k + prevPointer1 >= 156) {
                    pos = (k + prevPointer1) % 156;
                } else {
                    pos = k + prevPointer1;
                }
                this.drawTexturedModalRect(this.guiLeft + 10 + pos, this.guiTop + 66, 1 + k, y1, 1, 4);
                data[pos] = i;
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
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/guisolderingmechanism.png");
    }

}
