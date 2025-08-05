package com.denfop.events.client;

import com.denfop.api.guidebook.GuideBookCore;
import com.denfop.api.guidebook.GuideTab;
import com.denfop.api.guidebook.Quest;
import com.denfop.network.packet.PacketUpdateCompleteQuest;
import com.denfop.toast.GuideToast;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EventAutoQuests {
    private static int tabIndex = 0;
    private static int questIndex = 0;
    private static final int QUESTS_PER_TICK = 10;
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onWorldTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        List<GuideTab> guideTabs = GuideBookCore.instance.getGuideTabs();
        if (guideTabs.isEmpty()) return;

        Map<String, List<String>> map = GuideBookCore.uuidGuideMap.get(event.player.getUUID());
        if (map == null) return;

        GuideTab guideTab = guideTabs.get(tabIndex);
        List<Quest> quests = GuideBookCore.instance.getQuests(tabIndex);

        if (questIndex >= quests.size()) {
            questIndex = 0;
            tabIndex++;
            if (tabIndex >= guideTabs.size()) {
                tabIndex = 0;
            }
            return;
        }

        int processed = 0;
        while (questIndex < quests.size() && processed < QUESTS_PER_TICK) {
            Quest quest = quests.get(questIndex % quests.size());
            questIndex++;
             List<ItemStack> stacks = mergeStacks(quest.itemStacks);

            boolean hasPrev = quest.hasPrev;
            List<String> completedQuests = map.get(guideTab.unLocalized);
            boolean isUnlocked = hasPrev && completedQuests.contains(quest.prevName);
            if (!stacks.isEmpty())
            if (isComplete(event.player, tabIndex, isUnlocked, stacks, quest)) {
                complete(event.player, tabIndex, quest);
            }

            processed++;
        }
    }

    private List<ItemStack> mergeStacks(List<ItemStack> input) {
        List<ItemStack> stacks = new ArrayList<>();
        for (ItemStack stack : input) {
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
        return stacks;
    }
    public boolean hasAllItems(Player player, List<ItemStack> requiredItems, Quest quest) {
        for (FluidStack fluidStack : quest.fluidStacks) {
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
    public boolean isComplete(Player player, int tab, boolean isUnlocked, List<ItemStack> stacks, Quest quest) {
        if (hasAllItems(player,stacks,quest) && !isUnlocked){
            return GuideBookCore.uuidGuideMap.get(player.getUUID()).get(GuideBookCore.instance.getGuideTabs().get(tab).unLocalized).contains(quest.unLocalizedName);
        }
        return false;
    }

    public void complete(Player player, int tab, Quest quest) {
        Minecraft.getInstance().getToasts().addToast(new GuideToast(quest));
        new PacketUpdateCompleteQuest(player,GuideBookCore.instance.getGuideTabs().get(tab).unLocalized,quest.unLocalizedName);
    }
}
