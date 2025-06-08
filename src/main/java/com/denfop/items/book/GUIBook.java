package com.denfop.items.book;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Area;
import com.denfop.api.gui.ItemImage;
import com.denfop.api.gui.ScrollDirection;
import com.denfop.api.guidebook.GuideBookCore;
import com.denfop.api.guidebook.GuideTab;
import com.denfop.api.guidebook.Lines;
import com.denfop.api.guidebook.Quest;
import com.denfop.api.guidebook.Shape;
import com.denfop.gui.GuiIU;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.denfop.gui.GuiResearchTableSpace.enableScissor;
import static com.mojang.blaze3d.systems.RenderSystem.disableScissor;

@OnlyIn(Dist.CLIENT)
public class GUIBook<T extends ContainerBook> extends GuiIU<ContainerBook> {

    public static final ResourceLocation background = new ResourceLocation(Constants.TEXTURES, "textures/gui/guidebook.png");
    public static final ResourceLocation sprites = new ResourceLocation(Constants.TEXTURES, "textures/gui/sprites.png");
    public static final ResourceLocation sprites_lines = new ResourceLocation(
            Constants.TEXTURES,
            "textures/gui/slider_guide.png"
    );
    public static final ResourceLocation background1 = new ResourceLocation(
            Constants.TEXTURES,
            "textures/gui/guidebook1.png"
    );
    private final Player player;

    int tab = 0;
    List<Quest> questList = new ArrayList<>();
    LinkedList<GuideQuest> guideQuests = new LinkedList<>();
    private Map<String, List<String>> map;
    private boolean hoverDiscord = false;
    private boolean hoverGithub = false;
    private boolean hoverPU = false;
    private boolean hoverQG = false;
    private boolean hoverSQ = false;

    public GUIBook(Player player, final ItemStack itemStack1, final ContainerBook containerBook) {
        super(containerBook);
        this.player = player;
        this.imageWidth = 255 + 90;
        this.imageHeight = 195;
        this.elements.clear();
        this.componentList.clear();
        questList = GuideBookCore.instance.getQuests(0);
    }

    @Override
    public void render(GuiGraphics pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        this.map = GuideBookCore.uuidGuideMap.get(player.getUUID());
        if (reset) {
            questList = GuideBookCore.instance.getQuests(tab);
            offsetX = 0;
            offsetY = 0;
            lastMouseX = 0;
            lastMouseY = 0;
            reset = false;
            guideQuests.clear();
        }
    }

    boolean reset = false;

    protected void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.imageWidth) / 2;
        int yMin = (this.height - this.imageHeight) / 2;
        int x = i - xMin + 90 / 2;
        int y = j - yMin;
        int y1 = 15;
        for (GuideQuest guideQuest : new ArrayList<>(guideQuests)) {
            if (guideQuest.is(x, y)) {
                if (guideQuest.isRemove(x, y)) {
                    guideQuests.remove(guideQuest);
                } else if (guideQuest.isComplete(player, tab)) {
                    guideQuest.complete(player, tab);
                }
                return;
            }
        }
        List<GuideTab> guideTabs = GuideBookCore.instance.getGuideTabs();
        for (int index = 0; index < guideTabs.size(); index++) {
            if (index != tab) {
                GuideTab guideTab = guideTabs.get(index);
                int dx = true ? -28 : -33;
                int u = true ? 5 : 0;
                int v = true ? 28 : 56;
                int w = true ? 28 : 33;
                if (x >= dx && y >= y1 && x <= w + dx && y <= y1 + 27) {
                    tab = index;
                    reset = true;
                    return;
                }
            }
            y1 += 30;
        }

            if (hoverDiscord) {
                try {
                    Util.getPlatform().openUri(new URI("https://discord.com/invite/fqQPH6HKJV"));
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
            if (hoverGithub) {
                try {
                    Util.getPlatform().openUri(new URI("https://github.com/ZelGimi/industrialupgrade"));
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
            if (hoverPU) {
                try {
                    Util.getPlatform().openUri(new URI("https://www.curseforge.com/minecraft/mc-mods/power-utilities-iu"));
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
            if (hoverSQ) {
                try {
                    Util.getPlatform().openUri(new URI("https://www.curseforge.com/minecraft/mc-mods/simply-quarries"));
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
            if (hoverQG) {
                try {
                    Util.getPlatform().openUri(new URI("https://www.curseforge.com/minecraft/mc-mods//quantum-generators"));
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
        
        GuideTab guideTab = guideTabs.get(tab);
        List<String> quests = map.get(guideTab.unLocalized);
        for (Quest quest : GuideBookCore.instance.getQuests(tab)) {

            int centerX = 116 + offsetX;
            int centerY = 73 + offsetY;
            int xOffset = centerX + quest.x + (quest.shape == Shape.EPIC ? 2 : 3);
            int yOffset = centerY + quest.y + (quest.shape == Shape.EPIC ? 2 : 3);
            int texW = (quest.shape == Shape.EPIC) ? 26 : 24;
            int texH = texW;
            boolean hasPrev = quest.hasPrev;

            boolean isUnlocked = hasPrev ? quests.contains(quest.prevName) : false;

                if (x >= xOffset && x <= xOffset + texW && y >= yOffset && y <= yOffset + texH) {
                    if (this.guideQuests.contains(new GuideQuest(quest))) {
                        this.guideQuests.removeIf(guideQuest -> guideQuest.getQuest().equals(quest));
                    } else if (guideQuests.size() <= 1) {
                        this.guideQuests.add(new GuideQuest(quest, container.base.player,isUnlocked));
                    }
                }


        }
    }

    public void drawForegroundLayer(GuiGraphics poseStack, int par1, int par2) {
        super.drawForegroundLayer( poseStack, par1, par2);
        for (GuideQuest guideQuest : guideQuests) {
            if (guideQuest.is(par1, par2)) {
                guideQuest.drawForegroundLayer(this, poseStack,  par1, par2);
                return;
            }
        }
        List<GuideTab> guideTabs = GuideBookCore.instance.getGuideTabs();
        int y = 15;
        for (int index = 0; index < guideTabs.size(); index++) {

            GuideTab guideTab = guideTabs.get(index);
            int dx = -28;
            int w = 28;
            boolean isSelectedTab = (index == tab);
            if (isSelectedTab) {
                new Area(this, -33, y, 33, 27).withTooltip(guideTab.name)
                        .drawForeground( poseStack, par1, par2);
            } else {
                new Area(this, dx, y, w, 27).withTooltip(guideTab.name)
                        .drawForeground( poseStack, par1, par2);
            }

            y += 30;
        }
        hoverDiscord = false;
        hoverGithub = false;
        hoverPU = false;
        hoverQG = false;
        hoverSQ = false;
        if (par1 >= 10 && par1 <= 10 + 27 && par2 >= -15 && par2 <= -15 + 17) {
            hoverDiscord = true;
        }
        if (par1 >= 50 && par1 <= 50 + 27 && par2 >= -15 && par2 <= -15 + 17) {
            hoverGithub = true;
        }
        if (par1 >= 150 && par1 <= 150 + 27 && par2 >= -15 && par2 <= -15 + 17) {
            hoverPU = true;
        }
        if (par1 >= 180 && par1 <= 180 + 27 && par2 >= -15 && par2 <= -15 + 17) {
            hoverQG = true;
        }
        if (par1 >= 210 && par1 <= 210 + 27 && par2 >= -15 && par2 <= -15 + 17) {
            hoverSQ = true;
        }
        int centerX = 116 + offsetX;
        int centerY = 73 + offsetY;


        GuideTab guideTab = guideTabs.get(tab);
        if (map != null) {
            List<String> quests = map.get(guideTab.unLocalized);
            if (par1 >= 8 && par2 >= 9 && par1 <= 238 + 9 && par2 <= 176 + 9) {
                for (Quest quest : GuideBookCore.instance.getQuests(tab)) {
                    bindTexture(sprites);
                    int texW = (quest.shape == Shape.EPIC) ? 26 : 24;
                    RenderSystem.setShaderColor(1, 1, 1, 1);
                    new Area(this, centerX + quest.x, centerY + quest.y, texW, texW).withTooltip(quest.localizedName)
                            .drawForeground( poseStack, par1, par2);
                }
            }
        }
    }

    private void draw(int mouseX, int mouseY, int x, int x1, String text) {
        if (mouseX >= x && mouseX < x1 && mouseY >= 180 && mouseY < 190) {
            this.drawTooltip(mouseX, mouseY, Collections.singletonList(Localization.translate(text)));
        }
    }

    public void renderLines(GuiGraphics poseStack, Quest current, Lines lines) {
        int centerX = 116;
        int centerY = 73;
        int x = current.x + 3;
        int y = current.y + 3;
        int prevX = current.prevX + 3;
        int prevY = current.prevY + 3;
        Shape shape = current.shape;
        Shape prevShape = current.prevShape;
        int startPosRender = 24;
        int endPosRender = 24;
        switch (prevShape) {
            case DEFAULT:
            case UNIQUE:
                startPosRender = 24;
                break;
            case EPIC:
                startPosRender = 26;
                break;
        }
        switch (shape) {
            case DEFAULT:
            case UNIQUE:
                endPosRender = 24;
                break;
            case EPIC:
                endPosRender = 26;
                break;
        }

        int dx = Math.abs(prevX - x);
        int dy = Math.abs(prevY - y);
        if (prevY < 0 && y < 0){
            dy = Math.abs(-prevY + y);
        }
        boolean firstHorizontal;

        if (dx < dy) {
            firstHorizontal = true;
        } else {
            firstHorizontal = false;
        }
        if (firstHorizontal) {
            drawHorizontalLine(poseStack,prevX + startPosRender, prevY + startPosRender / 2 - 1, x + startPosRender / 2 + 1, prevY, lines);
            if (y - 1 > 0) {
                drawVerticalLine(poseStack,x + startPosRender / 2 - 1, prevY + startPosRender / 2, x, y, lines);
            }else {

                drawVerticalLine(poseStack,x + startPosRender / 2 - 1, y + startPosRender, x, prevY + startPosRender / 2 ,
                        lines);

            }
        } else {
            if (y - 1 > 0) {
                if (y > prevY){
                    drawVerticalLine(poseStack,prevX + startPosRender / 2 - 1 , prevY  + startPosRender, prevX + startPosRender  ,
                            y + startPosRender / 2 + 1, lines);
                    drawHorizontalLine(poseStack,prevX + startPosRender / 2, y + startPosRender / 2 - 1, x, y, lines);
                }else {
                    drawVerticalLine(poseStack,prevX + startPosRender / 2 - 1, prevY + startPosRender, prevX + startPosRender, y, lines);
                    drawHorizontalLine(poseStack,prevX + startPosRender, y + startPosRender / 2 - 1, x, y, lines);
                }
            }else {
                if ((prevY > 0 && y < 0) || (prevY < 0 && y > 0)){
                    drawVerticalLine(poseStack,prevX + startPosRender / 2 - 1, y+ startPosRender / 2 - 1 , prevX + startPosRender,
                            prevY, lines
                    );
                    drawHorizontalLine(poseStack,prevX + startPosRender / 2, y + startPosRender / 2 - 1, x, y, lines);
                }else {
                    drawVerticalLine(poseStack,prevX + startPosRender / 2 - 1, prevY + startPosRender, prevX + startPosRender,
                            y + startPosRender, lines
                    );
                    drawHorizontalLine(poseStack,prevX + startPosRender, y + startPosRender / 2 - 1, x, y, lines);
                }

            }

        }

    }

    public void drawHorizontalLine(GuiGraphics poseStack, int startX, int startY, int endX, int endY, Lines line) {
        int centerX = 116 + offsetX;
        int centerY = 73 + offsetY;
        bindTexture(sprites_lines);
        this.drawTexturedModalRect(poseStack,this.guiLeft + centerX + startX, this.guiTop + centerY + startY, line.getHX(), line.getHY(),
                endX - startX,
                2
        );
    }

    public void drawVerticalLine(GuiGraphics poseStack, int startX, int startY, int endX, int endY, Lines line) {
        int centerX = 116 + offsetX;
        int centerY = 73 + offsetY;
       bindTexture(sprites_lines);
        this.drawTexturedModalRect( poseStack,this.guiLeft + centerX + startX, this.guiTop + centerY + startY, line.getVX(), line.getVY(), 2,
                endY - startY
        );
    }

    private int offsetX = 0, offsetY = 0;

    private boolean isDragging = false;
    private boolean isDragging1 = false;
    private int lastMouseX, lastMouseY;

    @Override
    public boolean mouseScrolled(double d, double d2, double d3) {
        super.mouseScrolled(d, d2, d3);
        int mouseX = (int) (d - this.guiLeft);
        int mouseY = (int) (d2 - this.guiTop);
        ScrollDirection direction = d3 != 0.0 ? (d3 < 0.0 ? ScrollDirection.down : ScrollDirection.up) : ScrollDirection.stopped;

        if (direction != ScrollDirection.stopped) {
            for (GuideQuest guideQuest : guideQuests) {
                if (guideQuest.is(mouseX, mouseY)) {
                    if (guideQuest.isTextFields(mouseX, mouseY) && guideQuest.canScroll(direction)) {
                        guideQuest.scroll(direction);
                        return true;
                    } else if (guideQuest.isItems(mouseX, mouseY) && guideQuest.canScrollItem(direction)) {
                        guideQuest.scrollItem(direction);
                        return true;
                    }
                }
            }
        }
        return super.mouseScrolled(d, d2, d3);
    }
    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        mouseX -= (double) this.leftPos;
        mouseY -= (double) this.topPos;

        if (button == 0) {
            for (GuideQuest guideQuest : guideQuests) {
                if (guideQuest.is((int) mouseX, (int) mouseY)) {

                    if (!isDragging1) {
                        isDragging1 = true;
                    } else {
                        int dx = (int) dragX;
                        int dy = (int) dragY;
                        guideQuest.setOffsetX1(guideQuest.getOffsetX1() + dx);
                        guideQuest.setOffsetY1(guideQuest.getOffsetY1() + dy);

                    }

                    return true;
                }
            }
        } else {
            isDragging1 = false;
        }
        if (mouseX >= 8 && mouseY >= 9 && mouseX <= 246 && mouseY <= 185) {
            if (button == 0) {

                if (!isDragging) {
                    isDragging = true;
                } else {
                    int dx = (int) dragX;
                    int dy = (int) dragY;
                    offsetX += dx ;
                    offsetY += dy;

                }
                return true;
            } else {
                isDragging = false;
            }

        }
        return false;
    }

    public void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, float partialTicks, int mouseX, int mouseY) {
       super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);
        this.imageWidth = 255;
        this.imageHeight = 195;
        if (map == null) {
            return;
        }
       RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        this.bindTexture();
        this.drawTexturedModalRect(poseStack,this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
       bindTexture(sprites);
        if (!hoverDiscord) {
            drawTexturedModalRect(poseStack,this.guiLeft + 10, this.guiTop - 15, 202, 7, 27, 17);
        } else {
            drawTexturedModalRect(poseStack,this.guiLeft + 10, this.guiTop - 15 - 6, 229, 1, 27, 22);
        }
        if (!hoverGithub) {
            drawTexturedModalRect(poseStack,this.guiLeft + 50, this.guiTop - 15, 202, 29, 27, 17);
        } else {
            drawTexturedModalRect(poseStack,this.guiLeft + 50, this.guiTop - 15 - 6, 229, 23, 27, 22);

        }
        if (!hoverPU) {
            drawTexturedModalRect(poseStack,this.guiLeft + 150, this.guiTop - 15, 202, 51, 27, 17);
        } else {
            drawTexturedModalRect(poseStack,this.guiLeft + 150, this.guiTop - 15 - 6, 229, 45, 27, 22);

        }
        if (!hoverQG) {
            drawTexturedModalRect(poseStack,this.guiLeft + 180, this.guiTop - 15, 202, 73, 27, 17);
        } else {
            drawTexturedModalRect(poseStack,this.guiLeft + 180, this.guiTop - 15 - 6, 229, 67, 27, 22);

        }
        if (!hoverSQ) {
            drawTexturedModalRect(poseStack,this.guiLeft + 210, this.guiTop - 15, 202, 95, 27, 17);
        } else {
            drawTexturedModalRect(poseStack,this.guiLeft + 210, this.guiTop - 15 - 6, 229, 89, 27, 22);

        }

        int x1 = mouseX - this.guiLeft;
        int y1 = mouseY - this.guiTop;
        int y = 15;
        int centerX = 116 + offsetX;
        int centerY = 73 + offsetY;

        List<GuideTab> guideTabs = GuideBookCore.instance.getGuideTabs();

        for (int i = 0; i < guideTabs.size(); i++) {
            GuideTab guideTab = guideTabs.get(i);
            String tabKey = guideTab.getUnLocalized();
            List<String> quests = map.get(tabKey);
            boolean isSelectedTab = (i == tab);
            boolean hasQuests = !quests.isEmpty();

           bindTexture(sprites);

            if (isSelectedTab) {
                this.drawTexturedModalRect(poseStack,guiLeft - 33, guiTop + y, 0, hasQuests ? 0 : 56, 33, 27);
                new ItemImage(this, -33 + 9, y + 6, () -> guideTab.icon)
                        .drawBackground(poseStack,guiLeft, guiTop);
                enableScissor(guiLeft + 8, guiTop + 9, guiLeft + 8+238, guiTop + 9+176);

                for (Quest quest : GuideBookCore.instance.getQuests(i)) {
                   bindTexture(sprites);

                    boolean hasPrev = quest.hasPrev;
                    boolean isUnlocked = hasPrev ? quests.contains(quest.prevName) : quests.contains(quest.unLocalizedName);

                    int xOffset = guiLeft + centerX + quest.x + (quest.shape == Shape.EPIC ? 2 : 3);
                    int yOffset = guiTop + centerY + quest.y + (quest.shape == Shape.EPIC ? 2 : 3);

                    int texX = 0;
                    int texY = 0;
                    int texW = (quest.shape == Shape.EPIC) ? 26 : 24;
                    int texH = texW;

                    if (hasPrev) {
                        texY = isUnlocked ? 28 : 1;
                        if (!quests.contains(quest.unLocalizedName)) {
                            texY = 57;
                        }
                    } else {
                        texY = isUnlocked ? 1 : 57;
                    }

                    switch (quest.shape) {
                        case DEFAULT:
                            texX = 36;
                            break;
                        case UNIQUE:
                            texX = 61;
                            break;
                        case EPIC:
                            texX = 86;
                            break;
                    }

                    drawTexturedModalRect(poseStack,xOffset, yOffset, texX, texY, texW, texH);

                    if (hasPrev) {
                       bindTexture(sprites_lines);
                        renderLines(poseStack,quest, getLine(quest, quests, guideTab));
                    }

                    new ItemImage(this, centerX + quest.x + 7, centerY + quest.y + 7, () -> quest.icon)
                            .drawBackground(poseStack,guiLeft, guiTop);
                }

                disableScissor();
            } else {

                int dx = hasQuests ? -28 : -33;
                int u = hasQuests ? 5 : 0;
                int v = hasQuests ? 28 : 56;
                int w = hasQuests ? 28 : 33;
                drawTexturedModalRect(poseStack,guiLeft + dx, guiTop + y, u, v, w, 27);
                new ItemImage(this, dx + 8, y + 6, () -> guideTab.icon)
                        .drawBackground(poseStack,guiLeft, guiTop);
            }

            y += 30;
        }



        PoseStack posestack = RenderSystem.getModelViewStack();
        if (map != null) {
            GuideTab guideTab = guideTabs.get(tab);
            String tabKey = guideTab.getUnLocalized();
            List<String> quests = map.get(tabKey);
            for (GuideQuest guideQuest : guideQuests) {
                poseStack.pose().pushPose();
                poseStack.pose().translate(0,0,300);

                posestack.translate(0,0,300);
                guideQuest.drawBackgroundLayer(this,poseStack, guiLeft, guiTop,
                        !quests.contains(guideQuest.getQuest().unLocalizedName)
                );
                poseStack.pose().popPose();
            }
        }
    }

    private Lines getLine(Quest quest, List<String> quests, GuideTab guideTab) {
        final String PrevName = quest.prevName;
        if (!quests.contains(PrevName)) {
            return Lines.GOLD;
        } else {
            Quest quest1 = GuideBookCore.instance.getPrev(quest.prevName, guideTab);
            if (quest1.hasPrev) {
                if (quests.contains(quest1.prevName)) {
                    return Lines.DARK;
                } else {
                    return Lines.GRAY;
                }
            } else {
                return Lines.GRAY;
            }
        }
    }



    protected ResourceLocation getTexture() {
        return background;
    }

}
