package com.denfop.ssp.items.tools;

import com.denfop.ssp.items.itembase.ItemElectricTool;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import ic2.api.item.ElectricItem;
import ic2.core.IC2;
import ic2.core.audio.PositionSpec;
import ic2.core.item.armor.ItemArmorNanoSuit;
import ic2.core.item.armor.ItemArmorQuantumSuit;
import ic2.core.item.tool.HarvestLevel;
import ic2.core.item.tool.ToolClass;
import ic2.core.slot.ArmorSlot;
import ic2.core.util.StackUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class ItemNanoSaber extends ItemElectricTool {

    public static final int ticker = 4;
    private final int damage1;

    private final int damage2;
    protected String name;
    private int soundTicker;

    public ItemNanoSaber(
            String name,
            int damage,
            HarvestLevel harvestLevel,
            ToolClass toolClasses,
            int maxCharge,
            int transferLimit,
            int tier,
            int damage2,
            int damage1
    ) {
        super(name, damage, harvestLevel, toolClasses, maxCharge, transferLimit, tier, damage2, damage1);
        this.soundTicker = 0;
        ItemElectricTool.maxCharge = maxCharge;
        ItemElectricTool.transferLimit = transferLimit;
        ItemElectricTool.tier = tier;
        this.damage1 = damage1;
        this.damage2 = damage2;
    }

    public static void drainSaber(ItemStack stack, double amount, EntityLivingBase entity) {
        if (!ElectricItem.manager.use(stack, amount, entity)) {
            NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
            setActive(nbt, false);
        }
    }

    private static void setActive(NBTTagCompound nbt, boolean active) {
        nbt.setBoolean("active", active);
    }

    private static boolean isActive(ItemStack stack) {
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
        return isActive(nbt);
    }

    private static boolean isActive(NBTTagCompound nbt) {
        return nbt.getBoolean("active");
    }

    @Nonnull
    public Multimap<String, AttributeModifier> getAttributeModifiers(
            @Nonnull EntityEquipmentSlot slot,
            @Nonnull ItemStack stack
    ) {
        if (slot != EntityEquipmentSlot.MAINHAND) {
            return super.getAttributeModifiers(slot, stack);
        }
        int dmg = damage2;
        if (ElectricItem.manager.canUse(stack, 400.0D) && isActive(stack)) {
            dmg = damage1;
        }
        HashMultimap<String, AttributeModifier> hashMultimap = HashMultimap.create();
        hashMultimap.put(
                SharedMonsterAttributes.ATTACK_SPEED.getName(),
                new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", this.attackSpeed, 0)
        );
        hashMultimap.put(
                SharedMonsterAttributes.ATTACK_DAMAGE.getName(),
                new AttributeModifier(Item.ATTACK_DAMAGE_MODIFIER, "Tool modifier", dmg, 0)
        );
        return hashMultimap;
    }

    public boolean onBlockStartBreak(@Nonnull ItemStack stack, @Nonnull BlockPos pos, @Nonnull EntityPlayer player) {
        if (isActive(stack)) {
            drainSaber(stack, 80.0D, player);
        }
        return false;
    }

    public boolean onEntitySwing(@Nonnull EntityLivingBase entity, @Nonnull ItemStack stack) {
        if (IC2.platform.isRendering() && isActive(stack)) {
            IC2.audioManager.playOnce(
                    entity,
                    PositionSpec.Hand,
                    getRandomSwingSound(),
                    true,
                    IC2.audioManager.getDefaultVolume()
            );
        }
        return false;
    }

    public boolean canDestroyBlockInCreative(
            @Nonnull World world,
            @Nonnull BlockPos pos,
            @Nonnull ItemStack stack,
            @Nonnull EntityPlayer player
    ) {
        return false;
    }

    public String getRandomSwingSound() {
        switch (IC2.random.nextInt(3)) {
            case 1:
                return "Tools/Nanosabre/NanosabreSwing2.ogg";
            case 2:
                return "Tools/Nanosabre/NanosabreSwing3.ogg";
            default:
                return "Tools/Nanosabre/NanosabreSwing1.ogg";
        }
    }

    public boolean isFull3D() {
        return true;
    }

    public float getDestroySpeed(ItemStack stack, IBlockState state) {
        if (isActive(stack)) {
            this.soundTicker++;
            if (IC2.platform.isRendering() && this.soundTicker % 4 == 0) {
                IC2.platform.playSoundSp(getRandomSwingSound(), 1.0F, 1.0F);
            }
            return (state.getBlock() == Blocks.WEB) ? 50.0F : 4.0F;
        }
        return 1.0F;
    }

    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, @Nonnull EntityPlayer player, @Nonnull EnumHand hand) {
        ItemStack stack = StackUtil.get(player, hand);
        if (world.isRemote) {
            return new ActionResult<>(EnumActionResult.PASS, stack);
        }
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
        if (isActive(nbt)) {
            setActive(nbt, false);
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }
        if (ElectricItem.manager.canUse(stack, 32.0D)) {
            setActive(nbt, true);
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }
        return super.onItemRightClick(world, player, hand);
    }

    public void onUpdate(@Nonnull ItemStack stack, @Nonnull World world, @Nonnull Entity entity, int slot, boolean par5) {
        super.onUpdate(stack, world, entity, slot, (par5 && isActive(stack)));
        if (!isActive(stack)) {
            return;
        }
        if (entity instanceof EntityPlayerMP) {
            if (slot < 9) {
                drainSaber(stack, 64.0D, (EntityLivingBase) entity);
            } /*else if (ticker % 64 == 0) {
				drainSaber(stack, 32.0D, (EntityLivingBase) entity);
			}*/
        }
    }

    protected String getIdleSound(EntityLivingBase player, ItemStack stack) {
        return "Tools/Nanosabre/NanosabreIdle.ogg";
    }

    protected String getStartSound(EntityLivingBase player, ItemStack stack) {
        return "Tools/Nanosabre/NanosabrePowerup.ogg";
    }

    public boolean hitEntity(@Nonnull ItemStack stack, @Nonnull EntityLivingBase target, @Nonnull EntityLivingBase source) {
        if (!isActive(stack)) {
            return true;
        }
        if (IC2.platform.isSimulating()) {
            drainSaber(stack, 400.0D, source);
            if (!(source instanceof EntityPlayerMP) || !(target instanceof EntityPlayer) || ((EntityPlayerMP) source).canAttackPlayer(
                    (EntityPlayer) target)) {
                for (EntityEquipmentSlot slot : ArmorSlot.getAll()) {
                    if (!ElectricItem.manager.canUse(stack, 2000.0D)) {
                        break;
                    }
                    ItemStack armor = target.getItemStackFromSlot(slot);
                    if (armor.isEmpty()) {
                        continue;
                    }
                    double amount = 0.0D;
                    if (armor.getItem() instanceof ItemArmorNanoSuit) {
                        amount = 48000.0D;
                    } else if (armor.getItem() instanceof ItemArmorQuantumSuit) {
                        amount = 300000.0D;
                    }
                    if (amount > 0.0D) {
                        ElectricItem.manager.discharge(armor, amount, tier, true, false, false);
                        if (!ElectricItem.manager.canUse(armor, 1.0D)) {
                            target.setItemStackToSlot(slot, null);
                        }
                        drainSaber(stack, 2000.0D, source);
                    }
                }
            }
        }
        if (IC2.platform.isRendering()) {
            IC2.platform.playSoundSp(getRandomSwingSound(), 1.0F, 1.0F);
        }
        return true;
    }

    @Nonnull
    @SideOnly(Side.CLIENT)
    public EnumRarity getForgeRarity(@Nonnull ItemStack stack) {
        return EnumRarity.UNCOMMON;
    }

}
