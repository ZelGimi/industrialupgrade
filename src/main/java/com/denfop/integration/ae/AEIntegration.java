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
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AEIntegration {

    public static Map<Integer, Map<ChunkPos, List<DataAE>>> worldAE = new HashMap<>();
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
            Map<ChunkPos, List<DataAE>> listAE = worldAE.computeIfAbsent(
                    event.getWorld().provider.getDimension(),
                    k -> new HashMap<>()
            );
            boolean remove =
                    listAE.computeIfAbsent(new ChunkPos(event.tileentity.getPos()), k -> new ArrayList<>()).remove(new DataAE ((AEBasePoweredTile) event.tileentity,true));
            if (remove)
                MinecraftForge.EVENT_BUS.post(new EnergyTileUnLoadEvent(
                        event.tileentity.getWorld(),
                        EnergyNetGlobal.instance.getTile(event.tileentity.getWorld(), event.tileentity.getPos())
                ));
            listAE.computeIfAbsent(new ChunkPos(event.tileentity.getPos()), k -> new ArrayList<>()).add(new DataAE ((AEBasePoweredTile) event.tileentity,true));

        }
    }


    @SubscribeEvent(priority = EventPriority.LOW)
    public void update(TilesUpdateEvent event) {

            for (TileEntity entity : event.tiles) {
                if (entity instanceof AEBasePoweredTile) {
                   Map<ChunkPos, List<DataAE>> listAE = worldAE.computeIfAbsent(
                            event.getWorld().provider.getDimension(),
                            k -> new HashMap<>()
                    );
                    listAE.computeIfAbsent(new ChunkPos(entity.getPos()), k -> new ArrayList<>()).add(new DataAE ((AEBasePoweredTile) entity,true));
                }

        }
    }
    @SubscribeEvent(priority = EventPriority.LOW)
    public void unLoad(TileUnLoadEvent event) {
        if (event.tileentity instanceof AEBasePoweredTile) {
            Map<ChunkPos, List<DataAE>> listAE = worldAE.computeIfAbsent(
                    event.getWorld().provider.getDimension(),
                    k -> new HashMap<>()
            );
          boolean remove =
                  listAE.computeIfAbsent(new ChunkPos(event.tileentity.getPos()), k -> new ArrayList<>()).remove(new DataAE ((AEBasePoweredTile) event.tileentity,true));
            if (remove)
          MinecraftForge.EVENT_BUS.post(new EnergyTileUnLoadEvent(
                    event.tileentity.getWorld(),
                    EnergyNetGlobal.instance.getTile(event.tileentity.getWorld(), event.tileentity.getPos())
            ));
        }
    }
    @SubscribeEvent(priority = EventPriority.LOW)
    public void breakBlock(BlockEvent.BreakEvent event) {
        final Map<ChunkPos, List<DataAE>> map = worldAE.computeIfAbsent(
                event.getWorld().provider.getDimension(),
                k -> new HashMap<>()
        );
        if (!map.isEmpty()){
            List<DataAE> list = map.computeIfAbsent(
                    new ChunkPos(event.getPos()),
                    k -> new ArrayList<>()
            );
            TileEntity tile = event.getWorld().getTileEntity(event.getPos());
            if (tile instanceof AEBasePoweredTile) {
                if (!list.isEmpty()) {
                  boolean remove =  list.remove(new DataAE((AEBasePoweredTile) tile));
                  if (remove){
                      MinecraftForge.EVENT_BUS.post(new EnergyTileUnLoadEvent(
                              tile.getWorld(),
                              EnergyNetGlobal.instance.getTile(tile.getWorld(),tile.getPos())
                      ));
                  }
                }
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
        for (Map.Entry<Integer, Map<ChunkPos, List<DataAE>>> entry : worldAE.entrySet()) {
            Map<ChunkPos,List<DataAE>> map = entry.getValue();
            if (map != null) {
                for (Map.Entry<ChunkPos,List<DataAE>> entry1 : map.entrySet()) {
                    final List<DataAE> list = entry1.getValue();
                    if (list != null) {
                        for (DataAE poweredTile : list) {
                            if (!poweredTile.isLoaded()) {
                                poweredTile.setRemove(false);
                                poweredTile.setLoaded(true);
                                MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(poweredTile.getTile().getWorld(),
                                        new AESink(poweredTile.getTile())));

                            }
                        }
                    }
                }
            }
        }

    }

    @SubscribeEvent
    public void worldLoad(WorldEvent.Unload event) {
        World world = event.getWorld();
        if (!world.isRemote) {
            worldAE.computeIfAbsent(world.provider.getDimension(), k -> new HashMap<>()).clear();

        }
    }

}
