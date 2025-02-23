package com.denfop.integration.ae;

import appeng.core.Api;
import appeng.tile.powersink.AEBasePoweredTile;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.energy.event.EnergyTileLoadEvent;
import com.denfop.api.energy.event.EnergyTileUnLoadEvent;
import com.denfop.api.energy.event.TileLoadEvent;
import com.denfop.api.energy.event.TileUnLoadEvent;
import com.denfop.api.energy.event.TilesUpdateEvent;
import com.denfop.recipes.MaceratorRecipe;
import com.denfop.tiles.base.TileSunnariumMaker;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AEIntegration {

    public static Map<Integer, List<AEBasePoweredTile>> worldAE = new HashMap<>();
    public static List<AEBasePoweredTile> basePoweredTileAdderList = new LinkedList<>();
    public static List<AEBasePoweredTile> basePoweredTileRemoverList = new LinkedList<>();

    public static void init() {
        MinecraftForge.EVENT_BUS.register(new AEIntegration());
        MaceratorRecipe.addmacerator(
                new ItemStack(Items.QUARTZ),
                Api.INSTANCE.definitions().materials().netherQuartzDust().maybeStack(1).get()
        );
        if (Api.INSTANCE.definitions().materials().purifiedNetherQuartzCrystal().maybeStack(1).isPresent()) {
            TileSunnariumMaker.addSunnariumMaker(
                    new ItemStack(IUItem.sunnarium, 4, 4),
                    new ItemStack(Items.GLOWSTONE_DUST),
                    Api.INSTANCE.definitions().materials().purifiedNetherQuartzCrystal().maybeStack(1).get(),
                    new ItemStack(IUItem.iuingot, 1, 3),
                    new ItemStack(IUItem.sunnarium, 1, 3)
            );
        }
        MaceratorRecipe.addmacerator(
                Api.INSTANCE.definitions().materials().certusQuartzCrystal().maybeStack(1).get(),
                Api.INSTANCE.definitions().materials().certusQuartzDust().maybeStack(1).get()
        );
        MaceratorRecipe.addmacerator(
                Api.INSTANCE.definitions().materials().certusQuartzCrystalCharged().maybeStack(1).get(),
                Api.INSTANCE.definitions().materials().certusQuartzDust().maybeStack(1).get()
        );
        MaceratorRecipe.addmacerator(
                Api.INSTANCE.definitions().materials().fluixCrystal().maybeStack(1).get(),
                Api.INSTANCE.definitions().materials().fluixDust().maybeStack(1).get()
        );
        IUCore.list.add(Api.INSTANCE.definitions().materials().certusQuartzCrystal().maybeStack(1).get());
        IUCore.list.add(Api.INSTANCE.definitions().materials().certusQuartzCrystalCharged().maybeStack(1).get());

    }

    public static boolean isTile(TileEntity tileentity) {
        return tileentity instanceof AEBasePoweredTile;
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void load(TileLoadEvent event) {
        if (event.tileentity instanceof AEBasePoweredTile) {
            basePoweredTileAdderList.add((AEBasePoweredTile) event.tileentity);
        }
    }
    @SubscribeEvent(priority = EventPriority.LOW)
    public void unLoad(TileUnLoadEvent event) {
        if (event.tileentity instanceof AEBasePoweredTile) {
            basePoweredTileRemoverList.add((AEBasePoweredTile) event.tileentity);
        }
    }
    @SubscribeEvent(priority = EventPriority.LOW)
    public void update(TilesUpdateEvent event) {

            for (TileEntity entity : event.tiles) {
                if (entity instanceof AEBasePoweredTile) {
                    List<AEBasePoweredTile> listAE = worldAE.computeIfAbsent(
                            event.getWorld().provider.getDimension(),
                            k -> new LinkedList<>()
                    );
                    listAE.add((AEBasePoweredTile) entity);
                }

        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void tick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            return;
        }
        if (event.side == Side.CLIENT) {
            return;
        }
        for (Map.Entry<Integer, List<AEBasePoweredTile>> entry : worldAE.entrySet()) {
            List<AEBasePoweredTile> list = entry.getValue();
            if (list != null) {
                final Iterator<AEBasePoweredTile> iter = list.iterator();
                while (iter.hasNext()) {
                    AEBasePoweredTile poweredTile = iter.next();
                    if (poweredTile.isInvalid()) {
                        MinecraftForge.EVENT_BUS.post(new EnergyTileUnLoadEvent(
                                poweredTile.getWorld(),
                                EnergyNetGlobal.instance.getTile(poweredTile.getWorld(), poweredTile.getPos())
                        ));
                        iter.remove();
                    }
                }

            }
        }
        for (AEBasePoweredTile poweredTile : basePoweredTileAdderList) {
            List<AEBasePoweredTile> listAE = worldAE.computeIfAbsent(
                    poweredTile.getWorld().provider.getDimension(),
                    k -> new LinkedList<>()
            );
            listAE.add(poweredTile);
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(poweredTile.getWorld(), new AESink(poweredTile)));

        }
        basePoweredTileAdderList.clear();
        for (AEBasePoweredTile poweredTile : basePoweredTileRemoverList) {
            List<AEBasePoweredTile> listAE = worldAE.computeIfAbsent(
                    poweredTile.getWorld().provider.getDimension(),
                    k -> new LinkedList<>()
            );
            listAE.remove(poweredTile);
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnLoadEvent(
                    poweredTile.getWorld(),
                    EnergyNetGlobal.instance.getTile(poweredTile.getWorld(), poweredTile.getPos())
            ));

        }
        basePoweredTileRemoverList.clear();
    }

    @SubscribeEvent
    public void worldLoad(WorldEvent.Unload event) {
        World world = event.getWorld();
        if (!world.isRemote) {
            List<AEBasePoweredTile> listAE = worldAE.computeIfAbsent(world.provider.getDimension(), k -> new LinkedList<>());
            listAE.clear();
        }
    }

}
