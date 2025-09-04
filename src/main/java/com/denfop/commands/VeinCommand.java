package com.denfop.commands;


import com.denfop.Constants;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.denfop.world.vein.AlgorithmVein.shellClusterChuncks;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class VeinCommand {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(net.minecraft.commands.Commands.literal("refresh_vein")
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    if (!player.isCreative())
                        return 0;
                    shellClusterChuncks.clear();
                    return 1;
                }));
    }
}
