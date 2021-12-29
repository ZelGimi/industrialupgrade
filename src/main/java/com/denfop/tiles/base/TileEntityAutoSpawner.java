package com.denfop.tiles.base;


import cofh.redstoneflux.api.IEnergyHandler;
import cofh.redstoneflux.api.IEnergyReceiver;
import com.denfop.Config;
import com.denfop.Ic2Items;
import com.denfop.container.ContainerAutoSpawner;
import com.denfop.gui.GUIAutoSpawner;
import com.denfop.invslot.InvSlotBook;
import com.denfop.invslot.InvSlotModules;
import com.denfop.invslot.InvSlotUpgradeModule;
import com.denfop.items.modules.EnumSpawnerModules;
import com.denfop.items.modules.EnumSpawnerType;
import com.denfop.tiles.mechanism.TileEntityMagnet;
import com.denfop.utils.CapturedMob;
import com.denfop.utils.Enchant;
import com.denfop.utils.FakePlayerSpawner;
import ic2.api.energy.EnergyNet;
import ic2.api.network.INetworkTileEntityEventListener;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.api.upgrade.IUpgradeItem;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.ContainerBase;
import ic2.core.IHasGui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class TileEntityAutoSpawner extends TileEntityElectricMachine
        implements IHasGui, INetworkTileEntityEventListener, IEnergyReceiver, IEnergyHandler, IUpgradableBlock {

    public final InvSlotModules module_slot;
    public final InvSlotBook book_slot;
    public final int expmaxstorage;
    public final int maxprogress;
    public final InvSlotUpgradeModule module_upgrade;
    public final int tempcostenergy;
    public int costenergy;
    public int tempprogress;
    public int expstorage;
    public final int[] progress;
    public FakePlayerSpawner player;
    public final double maxEnergy2;
    public double energy2;

    public TileEntityAutoSpawner() {
        super("", 150000, 14, 27);
        this.module_slot = new InvSlotModules(this);
        this.book_slot = new InvSlotBook(this);
        this.module_upgrade = new InvSlotUpgradeModule(this);
        progress = new int[module_slot.size()];
        this.maxEnergy2 = 50000 * Config.coefficientrf;
        this.expmaxstorage = 15000;
        this.maxprogress = 100;
        this.tempprogress = 100;
        this.tempcostenergy = 900;
        this.costenergy = 900;

    }

    public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {

        return receiveEnergy(maxReceive, simulate);

    }

    public int receiveEnergy(int paramInt, boolean paramBoolean) {
        int i = (int) Math.min(
                this.maxEnergy2 - this.energy2,
                Math.min(EnergyNet.instance.getPowerFromTier(this.energy.getSinkTier()) * 4, paramInt)
        );
        if (!paramBoolean) {
            this.energy2 += i;
        }
        return i;
    }

    public boolean canConnectEnergy(EnumFacing arg0) {
        return true;
    }

    public int getEnergyStored(EnumFacing from) {
        return (int) this.energy2;
    }

    public int getMaxEnergyStored(EnumFacing from) {
        return (int) this.maxEnergy2;
    }


    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GUIAutoSpawner(new ContainerAutoSpawner(entityPlayer, this));
    }

    public ContainerBase<? extends TileEntityAutoSpawner> getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerAutoSpawner(entityPlayer, this);
    }




    public void updateEntityServer() {

        super.updateEntityServer();


        int speed = 0;
        int chance = 0;
        int spawn = 1;
        int experience = 0;
        ItemStack stack3 = Ic2Items.ejectorUpgrade;
       ((IUpgradeItem)stack3.getItem()).onTick(stack3, this);


        this.costenergy = this.tempcostenergy;
        for (int i = 0; i < module_upgrade.size(); i++) {
            if (module_upgrade.get(i) != null) {
                EnumSpawnerModules module =EnumSpawnerModules.getFromID(module_upgrade.get(i).getItemDamage());
                EnumSpawnerType type = module.type;
                switch (type) {
                    case SPAWN:
                        spawn += module.percent;
                        if (spawn <= 4) {
                            this.costenergy *= module.percent;
                        }
                        break;
                    case LUCKY:
                        chance += module.percent;
                        if (chance <= 3) {
                            this.costenergy += module.percent * this.costenergy * 0.2;
                        }
                        break;
                    case SPEED:
                        speed += module.percent;
                        if (speed <= 80) {
                            this.costenergy += module.percent * this.costenergy / 100;
                        }
                        break;
                    case EXPERIENCE:
                        experience += module.percent;
                        if (experience <= 100) {
                            this.costenergy += (module.percent * this.costenergy) / 100;
                        }
                        break;
                }
            }
        }
        chance = Math.min(3, chance);
        spawn = Math.min(4, spawn);
        speed = Math.min(80, speed);
        experience = Math.min(100, experience);
        for (int i = 0; i < module_slot.size(); i++) {
            if (!module_slot.get(i).isEmpty()) {
                if (this.energy.getEnergy() >= costenergy || this.energy2 >= costenergy * Config.coefficientrf) {
                    progress[i]++;
                    if (this.energy.getEnergy() >= costenergy) {
                        this.energy.useEnergy(costenergy);
                    } else {
                        this.energy2 -= costenergy * Config.coefficientrf;
                    }
                }
                tempprogress = maxprogress - speed;

                if (progress[i] >= tempprogress) {
                    progress[i] = 0;
                    if (this.player == null) {
                        this.player = new FakePlayerSpawner(getWorld());
                    }
                    String name = module_slot.get(i).serializeNBT().getString("id");

                    if (Config.EntityList.contains(name)) {
                        return;
                    }

                    CapturedMob capturedMob = CapturedMob.create(module_slot.get(i));
                    Entity entity = capturedMob.getEntity(this.world, true);


                    if (module_slot.get(i).serializeNBT().getInteger("type") != 0) {
                        if (entity instanceof EntitySheep) {
                            ((EntitySheep) entity).setFleeceColor(EnumDyeColor.byDyeDamage(module_slot
                                    .get(i)
                                    .serializeNBT()
                                    .getInteger(
                                            "type")));
                        }
                    }
                    EntityLiving entityliving = entity instanceof EntityLiving ? (EntityLiving) entity : null;
                    if (entityliving == null) {
                        continue;
                    }


                    for (int j = 0; j < spawn; j++) {
                        entity.setWorld(this.getWorld());

                        entity.setLocationAndAngles(this.pos.getX(), this.pos.getY(), this.pos.getZ(),
                                (getWorld()).rand.nextFloat() * 360.0F,
                                0.0F
                        );
                        int fireAspect = getEnchant(20);
                        int loot = getEnchant(21);
                        int reaper = getEnchant(11);
                        ItemStack stack = new ItemStack(Items.ENCHANTED_BOOK);
                        if (Config.DraconicLoaded) {
                            Enchant.addEnchant(stack, reaper);
                        }
                        this.player.fireAspect = fireAspect;
                        this.player.loot = loot;
                        this.player.loot += chance;
                        stack.addEnchantment(Enchantment.getEnchantmentByID(21), this.player.loot);
                        stack.addEnchantment(Enchantment.getEnchantmentByID(20), this.player.fireAspect);

                        this.player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, stack);
                        if (entity instanceof EntityBlaze) {
                            Random rand = new Random();
                            int m = rand.nextInt(2 + this.player.loot);

                            for (int k = 0; k < m; ++k) {
                                entity.dropItem(Items.BLAZE_ROD, 1);
                            }

                        }
                        int exp = ((EntityLiving) entity).experienceValue;
                        ((EntityLivingBase) entity).onDeath(DamageSource.causePlayerDamage(this.player));


                        if (expstorage + exp >= expmaxstorage) {
                            expstorage = expmaxstorage;
                        } else {
                            expstorage += (exp + experience * exp / 100);
                        }
                        if (world.provider.getWorldTime() % 10 == 0) {
                            for (int x = this.getPos().getX() - 10; x <= this.getPos().getX() + 10; x++) {
                                for (int y = this.getPos().getY() - 10; y <= this.getPos().getY() + 10; y++) {
                                    for (int z = this.getPos().getZ() - 10; z <= this.getPos().getZ() + 10; z++) {
                                        if (world.getTileEntity(getPos()) != null) {
                                            if (world.getTileEntity(getPos()) instanceof TileEntityMagnet) {
                                                if (world.getTileEntity(getPos()) != this) {
                                                    return;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }
                    int radius = 2;
                    AxisAlignedBB axisalignedbb = new AxisAlignedBB(this.getPos().getX() - radius, this.getPos().getY() - radius,
                            this.getPos().getZ() - radius, this.getPos().getX() + radius, this.getPos().getY() + radius,
                            this.getPos().getZ() + radius
                    );
                    List<EntityItem> list = this.world.getEntitiesWithinAABB(EntityItem.class, axisalignedbb);
                    for (EntityItem item : list) {

                        ItemStack drop = item.getItem();
                        ItemStack smelt = null;
                        if (player.fireAspect > 0) {

                            smelt = FurnaceRecipes.instance().getSmeltingResult(drop);
                            if (!smelt.isEmpty()) {
                                smelt.setCount(drop.getCount());
                            }
                        }
                        if (smelt == null) {
                            if (this.outputSlot.canAdd(drop)) {
                                this.outputSlot.add(drop);
                            }
                        } else {
                            if (this.outputSlot.canAdd(smelt)) {
                                this.outputSlot.add(smelt);
                            }
                        }
                        item.setDead();
                    }

                }
            }
        }
    }


    @Override
    public void onNetworkEvent(int i) {

    }

    public int getEnchant(int enchantID) {
        for (int i = 0; i < this.book_slot.size(); i++) {
            if (this.book_slot.get(i) == null) {
                continue;
            }
            ItemStack stack = this.book_slot.get(i);
            if (stack.getItem() instanceof ItemEnchantedBook) {
                NBTTagList bookNBT = ((ItemEnchantedBook) this.book_slot.get(i).getItem()).getEnchantments(this.book_slot.get(i));
                if (bookNBT.tagCount() == 1) {
                    short id = bookNBT.getCompoundTagAt(0).getShort("id");
                    if (id == enchantID) {
                        return bookNBT.getCompoundTagAt(0).getShort("lvl");
                    }
                }
            }
        }
        return 0;
    }


    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {

    }

    @Override
    public String getInventoryName() {
        return null;
    }


    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setDouble("energy2", energy2);
        nbttagcompound.setInteger("expstorage", expstorage);
        for (int i = 0; i < 4; i++) {
            nbttagcompound.setInteger("progress" + i, progress[i]);
        }
        return nbttagcompound;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        for (int i = 0; i < 4; i++) {
            progress[i] = nbttagcompound.getInteger("progress" + i);
        }
        expstorage = nbttagcompound.getInteger("expstorage");
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
