package com.denfop.tiles.base;

import cofh.redstoneflux.api.IEnergyContainerItem;
import com.denfop.Config;
import com.denfop.ElectricItem;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.item.IEnergyItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine2;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerFisher;
import com.denfop.gui.GuiFisher;
import com.denfop.invslot.InventoryFisher;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.utils.ModUtils;
import com.google.common.collect.Lists;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class TileFisher extends TileElectricMachine
        implements IUpgradableBlock, IManufacturerBlock {

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
    public final InventoryFisher inputslot;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    public int progress;
    protected Random _rand = null;
    protected float _next = (float) (Double.NaN);
    private boolean checkwater;
    private int level = 1;
    private FakePlayerSpawner player;
    private EntityFishHook energyFishHook;
    private LootContext.Builder lootcontext$builder;
    private LootTable table;
    private LootPool listPool;

    public TileFisher() {
        super(1E4, 14, 9);
        this.progress = 0;
        this.energyconsume = 100;
        this.checkwater = false;
        this.inputslot = new InventoryFisher(this);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.05));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.1));
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            progress = (int) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, progress);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine2.fisher;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine1;
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public void setLevel(final int level) {
        this.level = level;
    }

    @Override
    public void removeLevel(final int level) {
        this.level -= level;
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.machines_work_energy") + this.energyconsume + Localization.translate("iu" +
                    ".machines_work_energy_type_eu"));
        }
        super.addInformation(stack, tooltip);

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

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (this.getWorld().isRemote) {
            return;
        }
        checkwater = checkwater();
        this.player = new FakePlayerSpawner(getWorld());
        this.energyFishHook = new EntityFishHook(getWorld(), player);
        this.lootcontext$builder = new LootContext.Builder((WorldServer) this.world);
        this.table = this.world.getLootTableManager().getLootTableFromLocation(LootTableList.GAMEPLAY_FISHING);
        this.listPool = this.table.getPool("main");
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
            if ((this.inputslot.get().getItem() instanceof IEnergyItem)) {
                boolean need = ElectricItem.manager.canUse(this.inputslot.get(), 100);
                if (!need) {
                    return;
                }
            } else if ((this.inputslot.get().getItem() instanceof IEnergyContainerItem)) {
                IEnergyContainerItem item = (IEnergyContainerItem) this.inputslot.get().getItem();
                int energy = item.getEnergyStored(this.inputslot.get());
                if (energy < 100 * Config.coefficientrf) {
                    return;
                }
            }
            if (progress < 100) {
                if (this.energy.getEnergy() >= this.energyconsume) {
                    if (!this.getActive()) {
                        this.setActive(true);
                    }
                    if (this.getActive() && this.progress == 0) {
                        initiate(0);
                    }
                    progress += this.level;


                } else {
                    if (this.getActive()) {
                        initiate(2);
                        this.setActive(false);
                    }
                }
            }
        } else {
            if (this.getActive()) {
                initiate(2);
                this.setActive(false);
            }
        }

        if (checkwater && progress >= 100) {
            if (this.getActive()) {
                initiate(2);
            }
            if (!this.inputslot.isEmpty()) {
                ItemStack stack = this.inputslot.get();
                int j = EnchantmentHelper.getFishingSpeedBonus(stack);

                if (j > 0) {
                    this.energyFishHook.setLureSpeed(j);
                }

                int k = EnchantmentHelper.getFishingLuckBonus(stack);

                if (k > 0) {
                    this.energyFishHook.setLuck(k);
                }
                lootcontext$builder
                        .withLuck((float) k + this.energyFishHook.angler.getLuck())
                        .withPlayer(this.energyFishHook.angler)
                        .withLootedEntity(this.energyFishHook);
                List<ItemStack> list = Lists.newArrayList();
                this.listPool.generateLoot(list, this._rand, lootcontext$builder.build());

                for (ItemStack var1 : list) {
                    if (this.outputSlot.add(var1)) {
                        this.energy.useEnergy(this.energyconsume);
                    }
                    this._next = this._rand.nextFloat();
                    progress = 0;
                }
                int damage = stack.getMaxDamage() - stack.getItemDamage();
                int m = EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, stack);
                if (!((stack.getItem() instanceof IEnergyItem) || (stack.getItem() instanceof IEnergyContainerItem))) {
                    if (this.inputslot.get().getItemDamage() > -1) {

                        Random rand = this.getWorld().rand;
                        if (rand.nextInt(1 + m) == 0 && damage > -1) {
                            this.inputslot.get().setItemDamage(this.inputslot.get().getItemDamage() + 1);
                        }


                    }

                    if (this.inputslot.get().getItemDamage() >= this.inputslot.get().getMaxDamage() && damage > -1) {
                        this.inputslot.consume(1);
                    }
                } else {
                    Random rand = this.getWorld().rand;
                    if ((stack.getItem() instanceof IEnergyItem)) {
                        if (rand.nextInt(1 + m) == 0) {
                            ElectricItem.manager.use(stack, 100, null);
                        }
                    } else {
                        IEnergyContainerItem item = (IEnergyContainerItem) stack.getItem();
                        if (rand.nextInt(1 + m) == 0) {
                            item.extractEnergy(stack, 100 * Config.coefficientrf, false);
                        }
                    }
                }
            }
        }
        if (getActive()) {
            if (this.world.getWorldTime() % 20 == 0 && !this.outputSlot.isEmpty()) {
                ModUtils.tick(this.outputSlot, this);
            }
        }
    }

    public List<ItemStack> getWrenchDrops(EntityPlayer player, int fortune) {
        List<ItemStack> ret = super.getWrenchDrops(player, fortune);
        if (this.level != 1) {
            ret.add(new ItemStack(IUItem.upgrade_speed_creation, this.level));
            this.level = 1;
        }
        return ret;
    }

    @Override
    public boolean onActivated(
            final EntityPlayer player,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        if (level < 10) {
            ItemStack stack = player.getHeldItem(hand);
            if (!stack.getItem().equals(IUItem.upgrade_speed_creation)) {
                return super.onActivated(player, hand, side, hitX, hitY, hitZ);
            } else {
                stack.shrink(1);
                this.level++;
                return true;
            }
        } else {
            return super.onActivated(player, hand, side, hitX, hitY, hitZ);
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
        this.level = nbttagcompound.getInteger("level");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setFloat("next", this._next);
        nbttagcompound.setInteger("level", this.level);
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
    public GuiFisher getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiFisher(new ContainerFisher(entityPlayer, this));
    }

    public ContainerFisher getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerFisher(entityPlayer, this);
    }


    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.ItemInput
        );
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.fisher.getSoundEvent();
    }

}
