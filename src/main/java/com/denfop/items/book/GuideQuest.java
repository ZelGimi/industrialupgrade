package com.denfop.items.book;

import com.denfop.Localization;
import com.denfop.api.gui.FluidItem;
import com.denfop.api.gui.ItemImage;
import com.denfop.api.gui.ItemStackImage;
import com.denfop.api.gui.ScrollDirection;
import com.denfop.api.guidebook.GuideBookCore;
import com.denfop.api.guidebook.Quest;
import com.denfop.network.packet.PacketUpdateCompleteQuest;
import com.denfop.utils.ModUtils;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.denfop.items.book.GUIBook.background1;

public class GuideQuest {

    private final Quest quest;
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final EntityPlayer player;
    private boolean isUnlocked = false;
    private int offsetX1 = 0, offsetY1 = 0;
    private int maxPage = 1;
    private int page = 1;
    private int maxItemPage = 1;
    private int itemPage = 1;
    private int lastMouseX1, lastMouseY1;
    private boolean hover = false;
    private boolean hoverButton = false;
    List<ItemStack> stacks = new ArrayList<>();

    public GuideQuest(Quest quest) {
        this.quest = quest;
        this.x = 25;
        this.y = 25;
        this.width = 208;
        this.height = 176;

        this.player = null;
    }

    public boolean isUnlocked() {
        return isUnlocked;
    }

    public boolean hasAllItems(EntityPlayer player, List<ItemStack> requiredItems) {
        for (FluidStack fluidStack : getQuest().fluidStacks) {
           int amount  = fluidStack.amount;
            for (int i = 0; i < player.inventory.mainInventory.size(); i++) {
                ItemStack inInventory = player.inventory.mainInventory.get(i);
                if (FluidUtil.getFluidHandler(inInventory) != null) {
                    final IFluidHandlerItem handler = FluidUtil.getFluidHandler(inInventory);
                    if (handler.getTankProperties()[0].getContents() != null && handler.getTankProperties()[0]
                            .getContents()
                            .isFluidEqual(fluidStack)) {
                        if (handler.getTankProperties()[0].getContents().amount < fluidStack.amount) {
                            amount-=handler.getTankProperties()[0].getContents().amount;
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

            for (int i = 0; i < player.inventory.mainInventory.size(); i++) {
                ItemStack inInventory = player.inventory.mainInventory.get(i);


                if (!ItemStack.areItemsEqual(inInventory, required) && ModUtils.matchesNBT(
                        inInventory.getTagCompound(),
                        required.getTagCompound()
                )) {
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

    public GuideQuest(Quest quest, EntityPlayer player, final boolean isUnlocked) {
        this.quest = quest;
        this.x = 25;
        this.y = 25;
        this.width = 208;
        this.height = 176;
        this.isUnlocked=isUnlocked;
        for (ItemStack stack : quest.itemStacks) {
            boolean merged = false;

            for (ItemStack existing : stacks) {
                if (ItemStack.areItemStacksEqual(existing, stack)) {


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

    public void drawForegroundLayer(GUIBook guiIU, int x, int y) {
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
        int j = -1;
        int size = quest.itemStacks.size() + quest.fluidStacks.size();
        for (int i = (itemPage - 1) * 11; i < Math.min((itemPage) * 11, size); i++) {
            j++;
            if (i < quest.itemStacks.size()) {
                final int finalI = i;
                new ItemStackImage(
                        guiIU,
                        this.x + offsetX1 + 6 + j * 18,
                        this.y + offsetY1 + 64,
                        () -> quest.itemStacks.get(finalI)
                ).drawForeground(
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
                ).drawForeground(
                        x,
                        y
                );

            }
        }
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

    public void drawBackgroundLayer(GUIBook guiIU, int mouseX, int mouseY, boolean isComplete) {
        GlStateManager.color(1, 1, 1, 1);
        guiIU.mc.getTextureManager().bindTexture(background1);
        guiIU.drawTexturedModalRect(mouseX + x + offsetX1, mouseY + y + offsetY1, 0, 0, width, height);
        if (hover) {
            guiIU.drawTexturedModalRect(mouseX + x + offsetX1 + 191, mouseY + y + offsetY1 + 4, 243, 12, 13, 12);
        } else {
            guiIU.drawTexturedModalRect(mouseX + x + offsetX1 + 191, mouseY + y + offsetY1 + 4, 243, 0, 13, 12);

        }
        if (isComplete) {
            guiIU.drawTexturedModalRect(mouseX + x + offsetX1 + 15, mouseY + y + offsetY1 + 37, 236, 85, 20, 19);
        } else {
            if (hasAllItems(player, stacks)  && !this.isUnlocked()) {
                if (!hoverButton) {
                    guiIU.drawTexturedModalRect(mouseX + x + offsetX1 + 15, mouseY + y + offsetY1 + 37, 236, 25, 20, 19);
                } else {
                    guiIU.drawTexturedModalRect(mouseX + x + offsetX1 + 15, mouseY + y + offsetY1 + 37, 236, 45, 20, 19);
                }
            } else {
                guiIU.drawTexturedModalRect(mouseX + x + offsetX1 + 15, mouseY + y + offsetY1 + 37, 236, 65, 20, 19);

            }
        }

        Minecraft.getMinecraft().fontRenderer.drawString(quest.localizedName,
                mouseX + x + offsetX1 + 5 + width / 2 - Minecraft.getMinecraft().fontRenderer.getStringWidth(quest.localizedName) / 2,
                mouseY + y + offsetY1 + 5, 0
        );
        Minecraft.getMinecraft().fontRenderer.drawString(ChatFormatting.GREEN +
                        Localization.translate("iu.quest.task." + quest.typeQuest.name().toLowerCase()),
                mouseX + x + offsetX1 + 5 + width / 2 - Minecraft.getMinecraft().fontRenderer.getStringWidth("iu.quest.task." + quest.typeQuest.name().toLowerCase()) / 2,
                mouseY + y + offsetY1 + 25, 0
        );

        new ItemImage(guiIU, x + offsetX1 + 17, y + offsetY1 + 16, () -> quest.icon).drawBackground(mouseX, mouseY);
        List<String> lines = guiIU.splitTextToLines(quest.localizedDescription, width - 15, 1,
                Minecraft.getMinecraft().fontRenderer
        );
        maxPage = Math.max(1, lines.size() - 6);
        maxItemPage = Math.max(1, 1 + (quest.fluidStacks.size() + quest.itemStacks.size()) / 11);

        enableScissor(mouseX + x + offsetX1 + 6, mouseY + y + offsetY1 + 90, width - 15, 70);
        guiIU.drawTextInCanvasWithScissor(
                quest.localizedDescription,
                x + offsetX1 + 6,
                y + offsetY1 + 90 - ((page - 1) * 10),
                width - 15,
                70,
                1
        );
        disableScissor();
        Minecraft.getMinecraft().fontRenderer.drawString(page + "/" + maxPage,
                mouseX + x + offsetX1 + 5 + width / 2 - Minecraft.getMinecraft().fontRenderer.getStringWidth(page + "/" + maxPage) / 2,
                mouseY + y + offsetY1 + 162, 0
        );
        GlStateManager.color(1, 1, 1, 1);
        guiIU.mc.getTextureManager().bindTexture(background1);
        int scroll = (int) Math.ceil(71D / (maxPage));
        guiIU.drawTexturedModalRect(mouseX + x + offsetX1 + 200,
                mouseY + y + offsetY1 + 94 + (page - 1) * 71 / (maxPage),
                232,
                1,
                3,
                scroll);
        scroll = (int) Math.ceil(11D / (maxItemPage));
        guiIU.drawTexturedModalRect(
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
                ).drawBackground(mouseX, mouseY);
            } else {
                final int finalI = i - quest.itemStacks.size();
                new FluidItem(guiIU, x + offsetX1 + 6 + j * 18, y + offsetY1 + 64, quest.fluidStacks.get(finalI)).drawBackground(
                        mouseX,
                        mouseY
                );

            }
        }
    }

    private void enableScissor(int x, int y, int width, int height) {
        final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        int scaleFactor = scaledResolution.getScaleFactor();
        int scaledHeight = scaledResolution.getScaledHeight();

        GL11.glEnable(GL11.GL_SCISSOR_TEST);


        GL11.glScissor(
                x * scaleFactor,
                (scaledHeight - (y + height)) * scaleFactor,
                width * scaleFactor,
                height * scaleFactor
        );
    }

    private void disableScissor() {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
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

    public boolean isComplete(EntityPlayer player, int tab) {
        if (hasAllItems(player,stacks) && hoverButton && !isUnlocked){
            return GuideBookCore.uuidGuideMap.get(player.getUniqueID()).get(GuideBookCore.instance.getGuideTabs().get(tab).unLocalized).contains(quest.unLocalizedName);
        }
        return false;
    }

    public void complete(EntityPlayer player, int tab) {
        new PacketUpdateCompleteQuest(player,GuideBookCore.instance.getGuideTabs().get(tab).unLocalized,quest.unLocalizedName);
    }

}
