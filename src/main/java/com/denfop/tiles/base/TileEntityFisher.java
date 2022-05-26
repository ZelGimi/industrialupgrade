package com.denfop.tiles.base;

import com.denfop.IUCore;
import com.denfop.container.ContainerFisher;
import com.denfop.gui.GUIFisher;
import com.denfop.invslot.InvSlotFisher;
import com.denfop.utils.FakePlayerSpawner;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.core.ContainerBase;
import ic2.core.IHasGui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class TileEntityFisher extends TileEntityElectricMachine
        implements IHasGui, INetworkTileEntityEventListener {

    private static Field _Random_seed = null;

    static {
        try {
            Field var0 = Random.class.getDeclaredField("seed");
            var0.setAccessible(true);
            _Random_seed = var0;
        } catch (Throwable ignored) {
        }

    }

    public final int energyconsume;
    public final InvSlotFisher inputslot;
    public int progress;
    protected Random _rand = null;
    protected float _next = (float) (Double.NaN);
    private boolean checkwater;

    public TileEntityFisher() {
        super("", 1E4, 14, 9);
        this.progress = 0;
        this.energyconsume = 100;
        this.checkwater = false;
        this.inputslot = new InvSlotFisher(this);
    }

    private boolean checkwater() {
        int x1 = this.pos.getX();
        int y1 = this.pos.getY() - 2;
        int z1 = this.pos.getZ();
        for (int i = x1 - 1; i <= x1 + 1; i++) {
            for (int j = z1 - 1; j <= z1 + 1; j++) {
                for (int k = y1 - 1; k <= y1 + 1; k++) {
                    if (this.getWorld().getBlockState(new BlockPos(i, k, j)).getBlock() != Blocks.WATER) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public void updateEntityServer() {

        super.updateEntityServer();
        if (this._rand == null) {
            this._rand = new Random(super.getWorld().getSeed() ^ super.getWorld().rand.nextLong());
            this._next = this._rand.nextFloat();
        }


        if (this.getWorld().provider.getWorldTime() % 100 == 0) {
            checkwater = checkwater();
        }
        if (checkwater && !this.inputslot.isEmpty()) {
            if (progress < 100) {
                if (this.energy.getEnergy() >= this.energyconsume) {
                    progress++;

                    initiate(0);
                    this.setActive(true);
                } else {
                    initiate(2);
                    this.setActive(false);
                }
            }
        } else {
            initiate(2);
            this.setActive(false);
        }
        if (getWorld().provider.getWorldTime() % 60 == 0) {
            initiate(2);
        }
        if (checkwater && progress >= 100) {
            if (!this.inputslot.isEmpty()) {
                ItemStack stack = this.inputslot.get();
                final FakePlayerSpawner player = new FakePlayerSpawner(getWorld());
                EntityFishHook var2 = new EntityFishHook(getWorld(), player);
                int j = EnchantmentHelper.getFishingSpeedBonus(stack);

                if (j > 0) {
                    var2.setLureSpeed(j);
                }

                int k = EnchantmentHelper.getFishingLuckBonus(stack);

                if (k > 0) {
                    var2.setLuck(k);
                }
                LootContext.Builder lootcontext$builder = new LootContext.Builder((WorldServer) this.world);
                lootcontext$builder.withLuck((float) k + var2.angler.getLuck()).withPlayer(var2.angler).withLootedEntity(var2);

                List<ItemStack> var3 =
                        this.world
                                .getLootTableManager()
                                .getLootTableFromLocation(LootTableList.GAMEPLAY_FISHING)
                                .generateLootForPools(this._rand, lootcontext$builder.build());

                for (ItemStack var1 : var3) {
                    if (this.outputSlot.canAdd(var1)) {
                        this.energy.useEnergy(this.energyconsume * 10);
                        outputSlot.add(var1);
                    }
                    this._next = this._rand.nextFloat();
                    progress = 0;
                    this.inputslot.get().setItemDamage(this.inputslot.get().getItemDamage() + 1);
                    if (this.inputslot.get().getItemDamage() >= this.inputslot.get().getMaxDamage()) {
                        this.inputslot.consume(1);
                    }
                }
            }
        }

    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.progress = nbttagcompound.getInteger("progress");
        if (nbttagcompound.hasKey("seed")) {
            this._rand = new Random(nbttagcompound.getLong("seed"));
        }

        if (nbttagcompound.hasKey("next")) {
            this._next = nbttagcompound.getFloat("next");
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setFloat("next", this._next);

        nbttagcompound.setInteger("progress", this.progress);
        if (_Random_seed != null) {
            try {
                nbttagcompound.setLong("seed", ((AtomicLong) _Random_seed.get(this._rand)).get());
            } catch (Throwable ignored) {
            }
        }
        return nbttagcompound;
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GUIFisher(new ContainerFisher(entityPlayer, this));
    }

    public ContainerBase<? extends TileEntityFisher> getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerFisher(entityPlayer, this);
    }

    public String getStartSoundFile() {
        return "Machines/fisher.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    public void onNetworkEvent(int event) {
        if (this.audioSource == null && getStartSoundFile() != null) {
            this.audioSource = IUCore.audioManager.createSource(this, getStartSoundFile());
        }
        switch (event) {
            case 0:
                if (this.audioSource != null) {
                    this.audioSource.play();
                }
                break;
            case 1:
                if (this.audioSource != null) {
                    this.audioSource.stop();
                    if (getInterruptSoundFile() != null) {
                        IUCore.audioManager.playOnce(this, getInterruptSoundFile());
                    }
                }
                break;
            case 2:
                if (this.audioSource != null) {
                    this.audioSource.stop();
                }
                break;


        }
    }
}
