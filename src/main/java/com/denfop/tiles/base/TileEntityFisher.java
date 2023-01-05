package com.denfop.tiles.base;

import cofh.redstoneflux.api.IEnergyContainerItem;
import com.denfop.Config;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Ic2Items;
import com.denfop.container.ContainerFisher;
import com.denfop.gui.GuiFisher;
import com.denfop.invslot.InvSlotFisher;
import com.denfop.utils.ModUtils;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.ContainerBase;
import ic2.core.init.Localization;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.Field;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class TileEntityFisher extends TileEntityElectricMachine
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
    public final InvSlotFisher inputslot;
    public int progress;
    protected Random _rand = null;
    protected float _next = (float) (Double.NaN);
    private boolean checkwater;
    private int level = 1;

    public TileEntityFisher() {
        super(1E4, 14, 9);
        this.progress = 0;
        this.energyconsume = 100;
        this.checkwater = false;
        this.inputslot = new InvSlotFisher(this);
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
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack stack, final List<String> tooltip, final ITooltipFlag advanced) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.machines_work_energy") + this.energyconsume + Localization.translate("iu" +
                    ".machines_work_energy_type_eu"));
        }
        super.addInformation(stack, tooltip, advanced);

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
    protected void onLoaded() {
        super.onLoaded();
        checkwater = checkwater();
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
            if ((this.inputslot.get().getItem() instanceof IElectricItem)) {
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
                    progress += this.level;
                    if (!this.getActive()) {
                        initiate(0);
                        this.setActive(true);
                    }
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
                    if (this.outputSlot.add(var1)) {
                        this.energy.useEnergy(this.energyconsume);
                    }
                    this._next = this._rand.nextFloat();
                    progress = 0;
                }
                int damage = stack.getMaxDamage() - stack.getItemDamage();
                int m = EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, stack);
                if (!((stack.getItem() instanceof IElectricItem) || (stack.getItem() instanceof IEnergyContainerItem))) {
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
                    if ((stack.getItem() instanceof IElectricItem)) {
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
                ItemStack stack3 = Ic2Items.ejectorUpgrade;
                ModUtils.tick(this.outputSlot, this);
            }
        }
    }

    protected List<ItemStack> getWrenchDrops(EntityPlayer player, int fortune) {
        List<ItemStack> ret = super.getWrenchDrops(player, fortune);
        if (this.level != 1) {
            ret.add(new ItemStack(IUItem.upgrade_speed_creation, this.level));
            this.level = 1;
        }
        return ret;
    }

    @Override
    protected boolean onActivated(
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
                return false;
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
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiFisher(new ContainerFisher(entityPlayer, this));
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

    @Override
    public double getEnergy() {
        return 0;
    }

    @Override
    public boolean useEnergy(final double v) {
        return false;
    }

    @Override
    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.ItemProducing
        );
    }

}
