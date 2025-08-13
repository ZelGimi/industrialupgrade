package com.denfop.items.book;

import com.denfop.Localization;
import com.denfop.api.gui.FluidItem;
import com.denfop.api.gui.ItemImage;
import com.denfop.api.gui.ItemStackImage;
import com.denfop.api.gui.ScrollDirection;
import com.denfop.api.guidebook.GuideBookCore;
import com.denfop.api.guidebook.Quest;
import com.denfop.gui.GuiIU;
import com.denfop.gui.GuiResearchTableSpace;
import com.denfop.network.packet.PacketUpdateCompleteQuest;
import com.denfop.network.packet.PacketUpdateSkipQuest;
import com.denfop.toast.GuideToast;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.AdvancementToast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.denfop.gui.GuiCore.bindTexture;
import static com.denfop.items.book.GUIBook.background1;

public class GuideQuest {

    private final Quest quest;
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final Player player;
    private final int tab;
    private final int id;
    private boolean isUnlocked = false;
    private int offsetX1 = 0, offsetY1 = 0;
    private int maxPage = 1;
    private int page = 1;
    private int maxItemPage = 1;
    private int itemPage = 1;
    private int lastMouseX1, lastMouseY1;
    private boolean hover = false;
    private boolean hoverBookMark = false;
    private boolean hoverButton = false;
    private boolean hoverSkip = false;
    List<ItemStack> stacks = new ArrayList<>();

    public GuideQuest(Quest quest, int tab,int id) {
        this.quest = quest;
        this.x = 25;
        this.y = 25;
        this.width = 208;
        this.height = 176;
        this.tab = tab;
        this.id=id;
        this.player = null;
    }

    public boolean isUnlocked() {
        return isUnlocked;
    }

    public boolean hasAllItems(Player player, List<ItemStack> requiredItems) {
        for (FluidStack fluidStack : getQuest().fluidStacks) {
           int amount  = fluidStack.getAmount();
            for (int i = 0; i < player.getInventory().items.size(); i++) {
                ItemStack inInventory = player.getInventory().items.get(i);
                if (FluidUtil.getFluidHandler(inInventory).orElse(null) != null) {
                    final IFluidHandlerItem handler = FluidUtil.getFluidHandler(inInventory).orElse(null);
                    if (!handler.getFluidInTank(0).isEmpty() &&handler.getFluidInTank(0)
                            .isFluidEqual(fluidStack)) {
                        if (handler.getFluidInTank(0).getAmount() < fluidStack.getAmount()) {
                            amount-=handler.getFluidInTank(0).getAmount();
                        }else{
                            amount = 0;
                            break;
                        }
                    }
                }
            }
            if (amount != 0){
                return  false;
            }
        }
        for (ItemStack required : requiredItems) {
            int neededCount = required.getCount();
            int foundCount = 0;

            for (int i = 0; i < player.getInventory().items.size(); i++) {
                ItemStack inInventory = player.getInventory().items.get(i);


                if (!ItemStack.isSameItem(inInventory, required)) {
                    continue;
                }
                foundCount += inInventory.getCount();
                if (foundCount >= neededCount) {
                    break;
                }
            }

            if (foundCount < neededCount) {
                return false;
            }
        }

        return true;
    }

    public GuideQuest(Quest quest, Player player, final boolean isUnlocked, int tab,int id) {
        this.quest = quest;
        this.x = 25;
        this.y = 25;
        this.tab=tab;
        this.id=id;
        this.width = 208;
        this.height = 176;
        this.isUnlocked=isUnlocked;
        for (ItemStack stack : quest.itemStacks) {
            boolean merged = false;

            for (ItemStack existing : stacks) {
                if (ItemStack.isSameItem(existing, stack)) {


                    existing.grow(stack.getCount());
                    merged = true;
                    break;
                }
            }
            if (!merged) {
                stacks.add(stack.copy());
            }
        }
        this.player = player;
    }

    public void drawForegroundLayer(GUIBook guiIU, GuiGraphics poseStack, int x, int y) {
        int startX = x;
        int startY = y;
        if (startX >= this.x + offsetX1 + 191 && startX <= this.x + offsetX1 + 191 + 13 && startY >= this.y + offsetY1 + 4 && startY <= this.y + offsetY1 + 15) {
            hover = true;
        } else {
            hover = false;
        }
        if (startX >= this.x + offsetX1 + 15 && startX <= this.x + offsetX1 + 34 && startY >= this.y + offsetY1 + 37 && startY <= this.y + offsetY1 + 55) {
            hoverButton = true;
        } else {
            hoverButton = false;
        }
        if (startX >= this.x + offsetX1 + 50 && startX <= this.x + offsetX1 +50+206- 68  && startY >= this.y + offsetY1 + 42 && startY <= this.y + offsetY1 + 42+196-182) {
            hoverSkip = true;
        } else {
            hoverSkip = false;
        }
        if (startX >= this.x + offsetX1 + 195 && startX <= this.x + offsetX1 +195+11 && startY >= this.y + offsetY1 + 17 && startY <= this.y + offsetY1 + 17+11) {
            hoverBookMark = true;
        } else {
            hoverBookMark = false;
        }
        int j = -1;
        int size = quest.itemStacks.size() + quest.fluidStacks.size();
        poseStack.pose().pushPose();
        poseStack.pose().translate(0,0,200);
        for (int i = (itemPage - 1) * 11; i < Math.min((itemPage) * 11, size); i++) {
            j++;
            if (i < quest.itemStacks.size()) {
                final int finalI = i;
                new ItemStackImage(
                        guiIU,
                        this.x + offsetX1 + 6 + j * 18,
                        this.y + offsetY1 + 64,
                        () -> quest.itemStacks.get(finalI)
                ).drawForeground(poseStack,
                        x,
                        y
                );
            } else {
                final int finalI = i - quest.itemStacks.size();
                new FluidItem(
                        guiIU,
                        this.x + offsetX1 + 6 + j * 18,
                        this.y + offsetY1 + 64,
                        quest.fluidStacks.get(finalI)
                ).drawForeground(poseStack,
                        x,
                        y
                );

            }
        }
        poseStack.pose().popPose();
    }

    public boolean isRemove(int x, int y) {
        int startX = x;
        int startY = y;
        return startX >= this.x + offsetX1 + 191 && startX <= this.x + offsetX1 + 191 + 13 && startY >= this.y + offsetY1 + 4 && startY <= this.y + offsetY1 + 15;
    }

    public void setLastMouseX1(final int lastMouseX1) {
        this.lastMouseX1 = lastMouseX1;
    }

    public void setLastMouseY1(final int lastMouseY1) {
        this.lastMouseY1 = lastMouseY1;
    }

    public void setOffsetX1(final int offsetX1) {
        this.offsetX1 = offsetX1;
    }

    public void setOffsetY1(final int offsetY1) {
        this.offsetY1 = offsetY1;
    }

    public int getLastMouseX1() {
        return lastMouseX1;
    }

    public int getLastMouseY1() {
        return lastMouseY1;
    }

    public int getOffsetX1() {
        return offsetX1;
    }

    public int getOffsetY1() {
        return offsetY1;
    }

    public boolean is(int x, int y) {
        int tempX = this.x + offsetX1;
        int tempy = this.y + offsetY1;
        return tempX <= x && tempy <= y && y <= tempy + height && x <= tempX + width;
    }

    public void drawBackgroundLayer(GUIBook guiIU,GuiGraphics poseStack, int mouseX, int mouseY, boolean isComplete) {
        RenderSystem.setShaderColor(1, 1, 1, 1);
        guiIU.bindTexture(background1);
        guiIU.drawTexturedModalRect( poseStack,mouseX + x + offsetX1, mouseY + y + offsetY1, 0, 0, width, height);
        if (hover) {
            guiIU.drawTexturedModalRect( poseStack,mouseX + x + offsetX1 + 191, mouseY + y + offsetY1 + 4, 243, 12, 13, 12);
        } else {
            guiIU.drawTexturedModalRect(poseStack,mouseX + x + offsetX1 + 191, mouseY + y + offsetY1 + 4, 243, 0, 13, 12);

        }
        if (isComplete) {
            guiIU.drawTexturedModalRect( poseStack,mouseX + x + offsetX1 + 15, mouseY + y + offsetY1 + 37, 236, 85, 20, 19);
        } else {
            if (hasAllItems(player, stacks)  && !this.isUnlocked()) {
                if (!hoverButton) {
                    guiIU.drawTexturedModalRect( poseStack,mouseX + x + offsetX1 + 15, mouseY + y + offsetY1 + 37, 236, 25, 20, 19);
                } else {
                    guiIU.drawTexturedModalRect( poseStack,mouseX + x + offsetX1 + 15, mouseY + y + offsetY1 + 37, 236, 45, 20, 19);
                }
            } else {
                guiIU.drawTexturedModalRect( poseStack,mouseX + x + offsetX1 + 15, mouseY + y + offsetY1 + 37, 236, 65, 20, 19);

            }
        }

        guiIU.drawString( poseStack, quest.getLocalizedName(),
                mouseX + x + offsetX1 + 5 + width / 2 - guiIU.getStringWidth(quest.getLocalizedName()) / 2,
                mouseY + y + offsetY1 + 5, 0
        );
        guiIU.drawString(poseStack,ChatFormatting.GREEN +
                        Localization.translate("iu.quest.task." + quest.typeQuest.name().toLowerCase()),
                mouseX + x + offsetX1 + 5 + width / 2 -    guiIU.getStringWidth("iu.quest.task." + quest.typeQuest.name().toLowerCase()) / 2,
                mouseY + y + offsetY1 + 25, 0
        );

        new ItemImage(guiIU, x + offsetX1 + 17, y + offsetY1 + 16, () -> quest.icon).drawBackground( poseStack,mouseX, mouseY);
        List<String> lines = guiIU.splitTextToLines(quest.getLocalizedDescription(), width - 15, 1
        );
        maxPage = Math.max(1, lines.size() - 6);
        maxItemPage = Math.max(1, 1 + (quest.fluidStacks.size() + quest.itemStacks.size()) / 11);

        enableScissor(guiIU, mouseX + x + offsetX1 + 6, mouseY + y + offsetY1 + 90, width - 15, 70);
        guiIU.drawTextInCanvasWithScissor(poseStack,
                quest.getLocalizedDescription(),
                mouseX + x + offsetX1 + 6,
                mouseY +y + offsetY1 + 90,
                width - 15,
                70,
                page
        );
        disableScissor(guiIU);
        guiIU.drawString(poseStack,page + "/" + maxPage,
                mouseX + x + offsetX1 + 5 + width / 2 - guiIU.getStringWidth(page + "/" + maxPage) / 2,
                mouseY + y + offsetY1 + 162, 0
        );
        RenderSystem.setShaderColor(1, 1, 1, 1);
        guiIU.bindTexture(background1);
        int scroll = (int) Math.ceil(71D / (maxPage));
        guiIU.drawTexturedModalRect(poseStack,mouseX + x + offsetX1 + 200,
                mouseY + y + offsetY1 + 94 + (page - 1) * 71 / (maxPage),
                232,
                1,
                3,
                scroll);
        scroll = (int) Math.ceil(11D / (maxItemPage));
        guiIU.drawTexturedModalRect(poseStack,
                mouseX + x + offsetX1 + 200,
                mouseY + y + offsetY1 + 67 + (itemPage - 1) * 11 / (maxItemPage),
                232,
                1,
                3,
                scroll
        );
        int j = -1;
        int size = quest.itemStacks.size() + quest.fluidStacks.size();
        for (int i = (itemPage - 1) * 11; i < Math.min((itemPage) * 11, size); i++) {
            j++;
            if (i < quest.itemStacks.size()) {
                final int finalI = i;
                new ItemStackImage(
                        guiIU,
                        x + offsetX1 + 6 + j * 18,
                        y + offsetY1 + 64,
                        () -> quest.itemStacks.get(finalI)
                ).drawBackground(poseStack,mouseX, mouseY);
            } else {
                final int finalI = i - quest.itemStacks.size();
                new FluidItem(guiIU, x + offsetX1 + 6 + j * 18, y + offsetY1 + 64, quest.fluidStacks.get(finalI)).drawBackground(
                        poseStack,  mouseX,
                        mouseY
                );

            }
        }
        guiIU.bindTexture(background1);
        if (!isUnlocked && (!getQuest().itemStacks.isEmpty() ||  !getQuest().fluidStacks.isEmpty())){
            guiIU.drawTexturedModalRect(poseStack,
                    mouseX + x + offsetX1 + 50,
                    mouseY + y + offsetY1 + 42,
                    68,
                    182,
                    206- 68  ,
                    196-182
            );
            if (hoverSkip){
                guiIU.drawTexturedModalRect(poseStack,
                        mouseX + x + offsetX1 + 50,
                        mouseY + y + offsetY1 + 42,
                        68,
                        201,
                        206- 68  ,
                        196-182
                );
            }
            guiIU.drawString(poseStack,ChatFormatting.GREEN +
                            Localization.translate("iu.quest.task.skip"),
                    mouseX + x + offsetX1 + 5 + width / 2 -    guiIU.getStringWidth("iu.quest.task." + quest.typeQuest.name().toLowerCase()) / 2,
                    mouseY + y + offsetY1 + 45, 0
            );
        }
        guiIU.drawTexturedModalRect(poseStack,
                mouseX + x + offsetX1 + 195,
                mouseY + y + offsetY1 + 17,
                245,
                149,
                11  ,
                11
        );


        if (((GUIBook<?>)guiIU).hasBookMark(tab,id)){
            guiIU.drawTexturedModalRect(poseStack,
                    mouseX + x + offsetX1 + 195,
                    mouseY + y + offsetY1 + 17,
                    245,
                    122,
                    11  ,
                    11
            );
            if (hoverBookMark){
                guiIU.drawTexturedModalRect(poseStack,
                        mouseX + x + offsetX1 + 194,
                        mouseY + y + offsetY1 + 16,
                        244,
                        134,
                        11  ,
                        13
                );
            }
        }else{
            guiIU.drawTexturedModalRect(poseStack,
                    mouseX + x + offsetX1 + 195,
                    mouseY + y + offsetY1 + 17,
                    245,
                    149,
                    11  ,
                    11
            );
            if (hoverBookMark){
                guiIU.drawTexturedModalRect(poseStack,
                        mouseX + x + offsetX1 + 194,
                        mouseY + y + offsetY1 + 16,
                        244,
                        108,
                        11  ,
                        13
                );
            }
        }
    }

    private void enableScissor(GUIBook book, int x, int y, int width, int height) {
      GuiResearchTableSpace.enableScissor(x,y,x+width,y+height);
    }

    private void disableScissor(GUIBook book) {
        RenderSystem.disableScissor();
    }

    public boolean isTextFields(int x, int y) {
        int tempX = this.x + offsetX1 + 6;
        int tempy = this.y + offsetY1 + 90;
        return tempX <= x && tempy <= y && y <= tempy + 90 + 70 && x <= tempX + width;
    }

    public void scroll(ScrollDirection direction) {
        if (direction == ScrollDirection.down) {
            page++;
            page = Math.min(page, maxPage);
        } else {
            page--;
            page = Math.max(1, page);

        }
    }

    public void scrollItem(ScrollDirection direction) {
        if (direction == ScrollDirection.down) {
            itemPage++;
            itemPage = Math.min(itemPage, maxItemPage);
        } else {
            itemPage--;
            itemPage = Math.max(1, itemPage);

        }
    }

    public Quest getQuest() {
        return quest;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (o instanceof Quest) {
            Quest that = (Quest) o;
            return Objects.equals(quest, that);
        } else if (o instanceof GuideQuest) {
            GuideQuest that = (GuideQuest) o;
            return Objects.equals(quest, that.quest);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quest);
    }

    public boolean isItems(int x, int y) {
        int tempX = this.x + offsetX1 + 6;
        int tempy = this.y + offsetY1 + 63;
        return tempX <= x && tempy <= y && y <= tempy + 20 && x <= tempX + width;
    }

    public boolean canScroll(ScrollDirection direction) {
        int page = this.page;
        if (direction == ScrollDirection.down) {
            page++;
            page = Math.min(page, maxPage);
        } else {
            page--;
            page = Math.max(1, page);

        }
        return page != this.page;
    }

    public boolean canScrollItem(ScrollDirection direction) {
        int itemPage = this.itemPage;
        if (direction == ScrollDirection.down) {
            itemPage++;
            itemPage = Math.min(itemPage, maxItemPage);
        } else {
            itemPage--;
            itemPage = Math.max(1, itemPage);

        }
        return itemPage != this.itemPage;
    }

    public boolean isComplete(Player player, int tab) {
        if (hasAllItems(player,stacks) && hoverButton && !isUnlocked){
            return GuideBookCore.uuidGuideMap.get(player.getUUID()).get(GuideBookCore.instance.getGuideTabs().get(tab).unLocalized).contains(quest.unLocalizedName);
        }
        return false;
    }

    public void complete(Player player, int tab) {
        Minecraft.getInstance().getToasts().addToast(new GuideToast(this.quest));
        new PacketUpdateCompleteQuest(player,GuideBookCore.instance.getGuideTabs().get(tab).unLocalized,quest.unLocalizedName);
    }
    public boolean isSkip(Player player, int tab) {
        if (hoverSkip && !isUnlocked && (!getQuest().itemStacks.isEmpty() ||  !getQuest().fluidStacks.isEmpty())){
            return GuideBookCore.uuidGuideMap.get(player.getUUID()).get(GuideBookCore.instance.getGuideTabs().get(tab).unLocalized).contains(quest.unLocalizedName);
        }
        return false;
    }

    public void skip(Player player, int tab) {
        Minecraft.getInstance().getToasts().addToast(new GuideToast(this.quest));
        new PacketUpdateSkipQuest(player,GuideBookCore.instance.getGuideTabs().get(tab).unLocalized,quest.unLocalizedName);
    }

    public boolean isBookMark(Player player, int tab) {
        return hoverBookMark;
    }

    public void bookMark(GUIBook book, int tab) {
        if (book.hasBookMark(this.tab,id)){
            book.removeBookMark(this.tab,id);
        }else{
            book.addBookMark(this.tab,id);
        }
    }
}
