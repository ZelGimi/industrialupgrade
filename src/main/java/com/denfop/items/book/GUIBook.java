package com.denfop.items.book;

import com.denfop.Constants;
import com.denfop.items.book.core.AddPages;
import com.denfop.items.book.core.MainPage;
import com.denfop.items.book.core.Pages;
import com.denfop.utils.ModUtils;
import ic2.core.GuiIC2;
import ic2.core.init.Localization;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GUIBook extends GuiIC2<ContainerBook> {

    public static final ResourceLocation background = new ResourceLocation(Constants.TEXTURES, "textures/gui/book.png");
    public final String name;
    public final double maxmain;
    public final int scale;
    public final EntityPlayer player;
    public int colindex;
    public int colindexmax;
    public int mainindexbef;
    public int col;
    public int colmax;
    public int indexpage;
    public int mainindex;
    public int index;
    public boolean back;
    public boolean next;
    public int indexpagebef;
    public int tick = 0;
    public int tick1 = 0;
    public int tick2 = 0;
    public int tick3 = 0;
    RenderItemIU item = new RenderItemIU();
    RenderItemIUM itemmini = new RenderItemIUM();

    public GUIBook(EntityPlayer player, final ItemStack itemStack1, final ContainerBook containerBook) {
        super(containerBook);

        this.name = Localization.translate(itemStack1.getUnlocalizedName() + ".name");
        this.xSize = 145;
        this.ySize = 179;
        this.mainindex = 0;
        this.index = 0;
        this.indexpage = 0;
        this.player = player;
        this.back = false;
        this.col = 0;
        this.colmax = Math.min(12,MainPage.lst.size());
        this.colindex = 0;
        this.colindexmax = 9;
        this.mainindexbef = 0;
        this.indexpagebef = 0;
        this.maxmain = Math.ceil((double) MainPage.lst.size() / 12);
        this.next = MainPage.lst.size() > 12;
        this.scale = Minecraft.getMinecraft().gameSettings.guiScale;
    }

    public static List<String> splitEqually(String text, int size) {

        List<String> ret = new ArrayList<>((text.length() + size - 1) / size);

        for (int start = 0; start < text.length(); start += size) {
            ret.add(text.substring(start, Math.min(text.length(), start + size)));
        }
        return ret;
    }

    protected void mouseClicked(int i, int j, int k) throws IOException {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int xof = i - xMin;
        int yof = j - yMin;
        int x = 0;
        int y = 5;
        if (mainindex == 0) {
            if (xof >= 0 && xof < 16 && yof >= 0 && yof < 16) {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    try {
                        Desktop.getDesktop().browse(new URI("https://discord.gg/SP8DwcA"));
                        return;
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            } else if (xof >= 0 && xof < 16 && yof >= 16 && yof < 32) {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    try {
                        Desktop.getDesktop().browse(new URI("https://minecraft.fandom.com/ru/wiki/Industrial_Upgrade"));
                        return;
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (back) {
            if (xof >= 11 && xof < 29 && yof >= 160 && yof < 170) {
                if (mainindex > 0) {
                    if (index > 0) {
                        if (indexpage > 0) {
                            indexpage--;
                            Pages page1 = Pages.lst.get(index - 1);
                            List<AddPages> lst1 = Pages.pages.get(page1);
                            if (indexpage + 1 < lst1.size()) {
                                next = true;
                            }
                        } else {
                            MainPage page = MainPage.lst.get(mainindex - 1);
                            List<Pages> lst = MainPage.mainpages.get(page);
                            if (colindexmax < lst.size()) {
                                next = true;
                            }
                            index = indexpagebef;
                        }
                    } else if (index < 0) {
                        index++;
                        int u = colindexmax % 9 != 0 ? colindexmax % 9 : 9;
                        colindex -= 9;
                        colindexmax -= (u);
                        MainPage page = MainPage.lst.get(mainindex - 1);
                        List<Pages> lst = MainPage.mainpages.get(page);
                        if (colindexmax < lst.size()) {
                            next = true;
                        }
                        return;

                    } else {
                        mainindex = mainindexbef;
                        if (mainindex == 0) {
                            back = false;
                            next = colmax < MainPage.lst.size();
                        } else {
                            next = Math.abs(mainindex) + 1 != maxmain;
                        }
                    }
                    return;
                } else if (mainindex < 0) {
                    mainindex++;
                    mainindexbef = mainindex;
                    int u = colmax % 12 != 0 ? colmax % 12 : 12;
                    col -= 12;
                    colmax -= (u);
                    if (colmax < MainPage.lst.size()) {
                        next = true;
                        if (mainindex == 0) {
                            back = false;

                        }
                    }
                    return;

                }

            }
        }
        if (next) {
            if (xof >= 111 && xof < 129 && yof >= 160 && yof < 170) {
                if (mainindex > 0) {
                    if (index > 0) {
                        if (indexpage >= 0) {
                            indexpage++;
                            Pages page1 = Pages.lst.get(index - 1);
                            List<AddPages> lst1 = Pages.pages.get(page1);
                            if (indexpage + 1 >= lst1.size()) {
                                next = false;
                            }
                            return;
                        }

                    } else {
                        MainPage page = MainPage.lst.get(mainindex - 1);
                        List<Pages> lst = MainPage.mainpages.get(page);
                        if (colindexmax < lst.size()) {
                            index--;
                            indexpagebef = index;
                            back = true;
                            if (lst.size() - 9 - colindexmax > 0) {
                                colindex += 9;
                                colindexmax += 9;
                                return;
                            } else {
                                colindex += 9;
                                colindexmax = lst.size();
                                next = false;
                                return;
                            }

                        }
                    }

                } else {
                    if (colmax < MainPage.lst.size()) {
                        mainindex--;
                        mainindexbef = mainindex;
                        back = true;
                        if (MainPage.lst.size() - 12 - colmax > 0) {
                            col += 12;
                            colmax += 12;
                            return;
                        } else {
                            col += 12;
                            colmax = MainPage.lst.size();
                            next = false;
                            return;
                        }

                    }

                }
            }

        }
        if (mainindex <= 0) {
            for (int m = col; m < colmax; m++) {
                MainPage page = MainPage.lst.get(m);
                int k1 = m % 12;
                int x1 = x + (45 * ((k1) % 3));
                int y1 = y + 45 * ((k1) / 3);
                if (xof >= x1 + 5 && xof < x1 + 45 && yof >= y1 && yof < (Math.min(y1 + 45, 168))) {
                    this.mainindex = page.index;
                    this.back = true;
                    MainPage page1 = MainPage.lst.get(mainindex - 1);
                    List<Pages> lst = MainPage.mainpages.get(page1);
                    if (colindexmax < lst.size()) {
                        next = true;
                    }
                }
            }

        } else {
            MainPage page = MainPage.lst.get(mainindex - 1);
            List<Pages> lst = MainPage.mainpages.get(page);
            if (index <= 0) {
                int x1 = 12;
                int y1 = 14;
                for (int m = colindex; m < Math.min(colindexmax, lst.size()); m++) {
                    Pages page1 = lst.get(m);
                    int k1 = m % 9;
                    if (xof >= x1 && xof < 125 && yof > y1 + 15 * k1 && yof <= y1 + (15 * (k1 + 1))) {
                        indexpagebef = index;
                        index = page1.index;
                        back = true;
                        Pages page2 = Pages.lst.get(index - 1);
                        List<AddPages> lst1 = Pages.pages.get(page2);
                        next = lst1.size() > 1;
                    }
                }
            }
        }

    }

    public void drawForegroundLayer(int par1, int par2) {
        handleUpgradeTooltip(par1, par2);
        if (this.next) {
            draw(par1, par2, 111, 129, 160, 170, "description.next");
        }
        if (this.back) {
            draw(par1, par2, 11, 29, 160, 170, "description.back");
        }
        if (mainindex > 0) {
            MainPage page = MainPage.lst.get(mainindex - 1);
            List<Pages> lst = MainPage.mainpages.get(page);
            if (index <= 0) {
                int x = 12;
                int y = 14;
                for (int m = colindex; m < Math.min(colindexmax, lst.size()); m++) {
                    Pages page1 = lst.get(m);
                    int k = m % 9;
                    this.fontRenderer.drawString(Localization.translate(page1.text), x + 20, 2 + y + 15 * k,
                            ModUtils.convertRGBcolorToInt(13, 229, 34));
                }
            } else {
                Pages page1 = Pages.lst.get(index - 1);
                List<AddPages> lst1 = Pages.pages.get(page1);
                if (indexpage < lst1.size()) {
                    AddPages addpag = lst1.get(indexpage);
                    if (addpag.resource == null) {
                        List<String> list = splitEqually(Localization.translate(addpag.description), 30);
                        int x0 = 16;
                        int y = 20;
                        if (indexpage > 0) {
                            y -= 6;
                        }
                        if (indexpage == 0) {
                            String name = Localization.translate(page1.text);
                            int nmPos = (this.xSize - this.fontRenderer.getStringWidth(name)) / 2;

                            this.fontRenderer.drawString(name, nmPos, 12, ModUtils.convertRGBcolorToInt(13, 229, 34));
                        }
                        for (String str : list) {
                            this.fontRenderer.drawString(str, x0, y,
                                    ModUtils.convertRGBcolorToInt(13, 229, 34)
                            );
                            y += 8;
                            if (y > 165) {
                                break;
                            }
                        }
                    } else {
                        List<String> list = splitEqually(Localization.translate(addpag.textbefore), 30);
                        int x0 = 16;
                        int y = 20;
                        if (indexpage > 0) {
                            y -= 6;
                        }
                        if (indexpage == 0) {
                            String name = Localization.translate(page1.text);
                            int nmPos = (this.xSize - this.fontRenderer.getStringWidth(name)) / 2;

                            this.fontRenderer.drawString(name, nmPos, 12, ModUtils.convertRGBcolorToInt(13, 229, 34));
                        }
                        for (String str : list) {
                            this.fontRenderer.drawString(str, x0, y,
                                    ModUtils.convertRGBcolorToInt(13, 229, 34)
                            );
                            y += 8;
                            if (y > 165) {
                                break;
                            }
                        }
                        y += 3;
                        int y0 = y;
                        y = Math.min(165 - y, (addpag.y1 - addpag.y));
                        int y1 = y;
                        y = y0 + y1 + 3;
                        if (y + 8 < 165) {
                            if (addpag.centerdescription.isEmpty()) {
                                List<String> list1 = splitEqually(Localization.translate(addpag.textafter), 30);
                                for (String str : list1) {
                                    this.fontRenderer.drawString(str, x0, y,
                                            ModUtils.convertRGBcolorToInt(13, 229, 34)
                                    );
                                    y += 8;
                                    if (y > 165) {
                                        break;
                                    }
                                }
                            } else {
                                String name = Localization.translate(addpag.centerdescription);
                                int nmPos = (this.xSize - this.fontRenderer.getStringWidth(name)) / 2;

                                this.fontRenderer.drawString(name, nmPos, y, ModUtils.convertRGBcolorToInt(13, 229, 34));
                                y += 10;
                                if (y + 8 < 165) {
                                    List<String> list1 = splitEqually(Localization.translate(addpag.textafter), 30);
                                    for (String str : list1) {
                                        this.fontRenderer.drawString(str, x0, y,
                                                ModUtils.convertRGBcolorToInt(13, 229, 34)
                                        );
                                        y += 8;
                                        if (y > 165) {
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void draw(int mouseX, int mouseY, int x, int x1, int y, int y1, String text) {
        if (mouseX >= x && mouseX < x1 && mouseY >= y && mouseY < y1) {
            this.drawTooltip(mouseX, mouseY, Collections.singletonList(Localization.translate(text)));
        }
    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mainindex <= 0) {
            if (mainindex == 0) {
                if (mouseY >= 0 && mouseX >= 0 && mouseX < 16 && mouseY < 16) {
                    List<String> text = new ArrayList<>();
                    text.add(TextFormatting.GREEN + "Discord Server");
                    this.drawTooltip(mouseX, mouseY, text);
                } else if (mouseX >= 0 && mouseX < 16 && mouseY >= 16 && mouseY < 32) {
                    List<String> text = new ArrayList<>();
                    text.add(TextFormatting.GREEN + "Wiki");
                    this.drawTooltip(mouseX, mouseY, text);
                } else {
                    int x = 5;
                    int y = 5;
                    for (int m = col; m < colmax; m++) {
                        MainPage page = MainPage.lst.get(m);
                        int k = m % 12;
                        int x1 = x + (45 * ((k) % 3));
                        int y1 = y + 45 * ((k) / 3);
                        if (mouseX >= x1 && mouseX < x1 + 45 && mouseY >= y1 && mouseY < (Math.min(y1 + 45, 168))) {

                            List<String> text = new ArrayList<>();
                            text.add(TextFormatting.GREEN + Localization.translate(page.text));
                            this.drawTooltip(mouseX, mouseY, text);
                        }
                    }
                }
            } else {
                int x = 5;
                int y = 5;
                for (int m = col; m < colmax; m++) {
                    MainPage page = MainPage.lst.get(m);
                    int k = m % 12;
                    int x1 = x + (45 * ((k) % 3));
                    int y1 = y + 45 * ((k) / 3);
                    if (mouseX >= x1 && mouseX < x1 + 45 && mouseY >= y1 && mouseY < (Math.min(y1 + 45, 168))) {

                        List<String> text = new ArrayList<>();
                        text.add(TextFormatting.GREEN + Localization.translate(page.text));
                        this.drawTooltip(mouseX, mouseY, text);
                    }
                }
            }
        } else {
            MainPage page = MainPage.lst.get(mainindex - 1);
            List<Pages> lst = MainPage.mainpages.get(page);
            if (index <= 0) {
                int x = 12;
                int y = 14;
                for (int m = colindex; m < Math.min(colindexmax, lst.size()); m++) {
                    Pages page1 = lst.get(m);
                    int k = m % 9;
                    if (mouseX >= x && mouseX < 125 && mouseY > y + 15 * k && mouseY <= y + (15 * (k + 1))) {
                        List<String> text = new ArrayList<>();
                        text.add(TextFormatting.GREEN + Localization.translate(page1.text));
                        this.drawTooltip(mouseX, mouseY, text);
                    }
                }
            }
        }
    }

    public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.xSize = 145;
        this.ySize = 179;
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        int x1 = mouseX - this.guiLeft;
        int y1 = mouseY - this.guiTop;
        if (this.next) {
            this.drawTexturedModalRect(this.guiLeft + 111, this.guiTop + 160, 3, 192, 20, 12);
            if (x1 >= 111 && x1 < 129 && y1 >= 160 && y1 < 170) {
                this.drawTexturedModalRect(this.guiLeft + 111, this.guiTop + 160, 26, 192, 20, 12);
            }
        }
        if (this.back) {
            this.drawTexturedModalRect(this.guiLeft + 11, this.guiTop + 160, 3, 206, 20, 12);
            if (x1 >= 11 && x1 < 29 && y1 >= 160 && y1 < 170) {
                this.drawTexturedModalRect(this.guiLeft + 11, this.guiTop + 160, 26, 206, 20, 12);
            }
        }
        if (tick >= 0) {
            int t = tick / 15;
            if (t > 0) {
                this.drawTexturedModalRect(this.guiLeft + 121, this.guiTop + 173, 121, 184 + 4 * t, 11, 4);
            }

            tick++;
            if (tick > 75) {
                tick = 0;
            }
        }
        if (tick1 >= 0) {
            int t = tick1 / 15;
            if (t > 0) {
                this.drawTexturedModalRect(this.guiLeft + 139, this.guiTop + 12, 155 + 4 * t, 12, 4, 23);
            }

            tick1++;
            if (tick1 > 240) {
                tick1 = 0;
            }
        }
        if (tick2 >= 0) {
            int t = tick2 / 80;
            if (t > 0) {
                this.drawTexturedModalRect(this.guiLeft + 105, this.guiTop + 174, 105, 183, 14, 2);
            }

            tick2++;
            if (tick2 > 160) {
                tick2 = 0;
            }
        }
        if (tick3 >= 0) {
            int t = tick3 / 100;
            if (t > 0) {
                this.drawTexturedModalRect(this.guiLeft + 12, this.guiTop + 173, 12, 181, 30, 3);
            }

            tick3++;
            if (tick3 > 200) {
                tick3 = 0;
            }
        }

        if (mainindex == 0) {
            this.drawTexturedModalRect(this.guiLeft, this.guiTop, 240, 240, 16, 16);
            this.drawTexturedModalRect(this.guiLeft, this.guiTop + 16, 240, 224, 16, 16);

        }


        if (mainindex <= 0) {
            int x = 20;
            int y = 20;
            for (int m = col; m < colmax; m++) {
                MainPage page = MainPage.lst.get(m);
                int k = m % 12;
                RenderHelper.enableGUIStandardItemLighting();
                GlStateManager.pushMatrix();
                GlStateManager.color(1, 1, 1, 1);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glEnable(GL12.GL_RESCALE_NORMAL);

                GlStateManager.disableLighting();
                GlStateManager.enableDepth();
                mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                item.zLevel = 100;
                item.renderItemAndEffectIntoGUI(page.stack, (this.guiLeft + x + (45 * ((k) % 3))),
                        (this.guiTop + y + 40 * ((k) / 3))

                );
                GL11.glEnable(GL11.GL_LIGHTING);
                GlStateManager.enableLighting();
                RenderHelper.enableStandardItemLighting();
                GlStateManager.color(1, 1, 1, 1);
                GlStateManager.popMatrix();
                RenderHelper.disableStandardItemLighting();
            }

        } else {
            MainPage page = MainPage.lst.get(mainindex - 1);
            List<Pages> lst = MainPage.mainpages.get(page);
            if (index <= 0) {
                int x = 15;
                int y = 13;
                for (int m = colindex; m < Math.min(colindexmax, lst.size()); m++) {
                    Pages page1 = lst.get(m);
                    int k = m % 9;
                    RenderHelper.enableGUIStandardItemLighting();
                    GL11.glPushMatrix();
                    GL11.glColor4f(1, 1, 1, 1);
                    GL11.glDisable(GL11.GL_LIGHTING);
                    GL11.glEnable(GL12.GL_RESCALE_NORMAL);
                    GlStateManager.disableLighting();
                    GlStateManager.enableDepth();
                    mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                    itemmini.zLevel = 100;
                    itemmini.renderItemAndEffectIntoGUI(page1.stack, this.guiLeft + x,
                            this.guiTop + y + 15 * ((k))
                    );

                    GL11.glEnable(GL11.GL_LIGHTING);
                    GlStateManager.enableLighting();
                    RenderHelper.enableStandardItemLighting();
                    GL11.glColor4f(1, 1, 1, 1);
                    GL11.glPopMatrix();
                }
                RenderHelper.disableStandardItemLighting();
            } else {
                Pages page1 = Pages.lst.get(index - 1);
                List<AddPages> lst1 = Pages.pages.get(page1);
                if (indexpage < lst1.size()) {
                    AddPages addpag = lst1.get(indexpage);
                    if (addpag.resource == null) {

                    } else {
                        List<String> list = splitEqually(Localization.translate(addpag.textbefore), 30);
                        int x0 = 16;
                        int y = 20;
                        if (indexpage > 0) {
                            y -= 6;
                        }
                        for (String ignored : list) {
                            y += 8;
                            if (y > 165) {
                                break;
                            }
                        }
                        y += 3;
                        int y0 = y;
                        y = Math.min(165 - y, (addpag.y1 - addpag.y));
                        int y2 = y;
                        int nmPos = (this.xSize - (addpag.x1 - addpag.x) - x0) / 2;
                        if (nmPos > x0) {
                            x0 = nmPos;
                        }
                        int x2 = Math.min(125 - x0, (addpag.x1 - addpag.x));

                        this.mc.getTextureManager().bindTexture(addpag.resource);
                        if (!addpag.rendercenter) {
                            drawTexturedModalRect(this.guiLeft + x0, this.guiTop + y0, addpag.x, addpag.y,
                                    x2, y2
                            );
                        } else {
                            drawTexturedModalRect(this.guiLeft + nmPos, this.guiTop + y0, addpag.x, addpag.y,
                                    x2, y2
                            );
                        }
                        this.mc.getTextureManager().bindTexture(background);

                    }
                }
            }
        }


    }

    protected ResourceLocation getTexture() {
        return background;
    }

}
