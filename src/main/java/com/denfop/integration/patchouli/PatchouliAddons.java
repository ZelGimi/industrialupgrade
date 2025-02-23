package com.denfop.integration.patchouli;

import com.denfop.IUItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class PatchouliAddons {

    public static void init() {
        MinecraftForge.EVENT_BUS.register(new PatchouliAddons());
        IUItem.book_p = new ItemBook();
    }

    @SubscribeEvent
    public void loginPlayer(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player.getEntityWorld().isRemote) {
            return;
        }
        if (!event.player.getEntityData().getBoolean("iu_book")) {
            event.player.getEntityData().setBoolean("iu_book", true);
            event.player.addItemStackToInventory(new ItemStack(IUItem.book_p));
        }
    }

}
