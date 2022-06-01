package aroma1997.uncomplication.enet;


import com.denfop.Config;
import com.denfop.api.IAdvEnergyNet;
import ic2.api.energy.IEnergyNetEventReceiver;
import ic2.api.energy.NodeStats;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.info.ILocatable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import java.io.PrintStream;
import java.util.Map;
import java.util.WeakHashMap;

public class EnergyNetGlobal implements IAdvEnergyNet {


    private static Map<World, EnergyNetLocal> worldToEnergyNetMap;

    static {
        EnergyNetGlobal.worldToEnergyNetMap = new WeakHashMap<>();
    }

    public static EnergyNetLocal getForWorld(final World world) {
        if (world == null) {
            return null;
        }
        if (!EnergyNetGlobal.worldToEnergyNetMap.containsKey(world)) {
            EnergyNetGlobal.worldToEnergyNetMap.put(world, new EnergyNetLocal(world));
        }
        return EnergyNetGlobal.worldToEnergyNetMap.get(world);
    }


    public static void onTickEnd(final World world) {
        final EnergyNetLocal energyNet = getForWorld(world);
        if (energyNet != null) {
            energyNet.onTickEnd();
        }
    }

    public static EnergyNetGlobal initialize() {
        new EventHandler();
        MinecraftForge.EVENT_BUS.unregister(ic2.core.energy.grid.EventHandler.class);
        return new EnergyNetGlobal();
    }

    public static void onWorldUnload(final World world) {
        final EnergyNetLocal local = EnergyNetGlobal.worldToEnergyNetMap.remove(world);
        if (local != null) {
            local.onUnload();
        }
    }

    public IEnergyTile getTileEntity(final World world, final int x, final int y, final int z) {
        final EnergyNetLocal local = getForWorld(world);
        if (local != null) {
            return local.getTileEntity(new BlockPos(x, y, z));
        }
        return null;
    }

    public IEnergyTile getTileEntity(final World world, BlockPos pos) {
        final EnergyNetLocal local = getForWorld(world);
        if (local != null) {
            return local.getTileEntity(pos);
        }
        return null;
    }


    @Override
    public IEnergyTile getTile(final World world, final BlockPos blockPos) {
        final EnergyNetLocal local = getForWorld(world);
        if (local != null) {
            return local.getTileEntity(blockPos);
        }
        return null;
    }

    @Override
    public IEnergyTile getSubTile(final World world, final BlockPos blockPos) {


        return this.getTileEntity(world, blockPos);
    }

    @Override
    public <T extends TileEntity & IEnergyTile> void addTile(final T t) {
        final EnergyNetLocal local = getForWorld(this.getWorld(t));
        local.addTile(t);
    }

    @Override
    public <T extends ILocatable & IEnergyTile> void addTile(final T t) {
        final EnergyNetLocal local = getForWorld(this.getWorld(t));
        local.addTile(t);
    }

    @Override
    public void removeTile(final IEnergyTile iEnergyTile) {
        final EnergyNetLocal local = getForWorld(this.getWorld(iEnergyTile));
        local.removeTile(iEnergyTile);
    }

    @Override
    public World getWorld(final IEnergyTile tile) {
        if (tile == null) {
            return null;
        } else if (tile instanceof ILocatable) {
            return ((ILocatable) tile).getWorldObj();
        } else if (tile instanceof TileEntity) {
            return ((TileEntity) tile).getWorld();
        } else {
            throw new UnsupportedOperationException("unlocatable tile type: " + tile.getClass().getName());
        }
    }

    @Override
    public BlockPos getPos(final IEnergyTile iEnergyTile) {
        return ((TileEntity) iEnergyTile).getPos();
    }

    @Override
    public NodeStats getNodeStats(final IEnergyTile te) {
        final EnergyNetLocal local = getForWorld(getWorld(te));
        if (local == null) {
            return new NodeStats(0.0, 0.0, 0.0);
        }
        return local.getNodeStats(te);
    }

    @Override
    public boolean dumpDebugInfo(
            final World world,
            final BlockPos blockPos,
            final PrintStream printStream,
            final PrintStream printStream1
    ) {
        return false;
    }

    public double getPowerFromTier(final int tier) {


        return tier < 22 ? 8.0D * Math.pow(4.0D, tier) : 9.223372036854776E18D;

    }

    public int getTierFromPower(final double power) {
        if (power <= 0.0) {
            return 0;
        }
        return (int) Math.ceil(Math.log(power / 8.0) / Math.log(4.0));
    }

    @Override
    public double getRFFromEU(final int amount) {
        return amount * Config.coefficientrf;
    }

    public synchronized void registerEventReceiver(IEnergyNetEventReceiver receiver) {

    }

    public synchronized void unregisterEventReceiver(IEnergyNetEventReceiver receiver) {

    }

}
