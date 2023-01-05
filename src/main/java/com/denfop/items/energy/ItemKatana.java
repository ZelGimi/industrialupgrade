package com.denfop.items.energy;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.api.upgrade.EnumUpgrades;
import com.denfop.api.upgrade.IUpgradeItem;
import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.api.upgrade.event.EventItemLoad;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.utils.ModUtils;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import ic2.api.item.ElectricItem;
import ic2.api.item.IBoxable;
import ic2.api.item.IElectricItem;
import ic2.api.item.IItemHudInfo;
import ic2.core.IC2;
import ic2.core.audio.AudioSource;
import ic2.core.init.BlocksItems;
import ic2.core.init.Localization;
import ic2.core.item.armor.ItemArmorNanoSuit;
import ic2.core.item.armor.ItemArmorQuantumSuit;
import ic2.core.item.tool.HarvestLevel;
import ic2.core.item.tool.IToolClass;
import ic2.core.item.tool.ToolClass;
import ic2.core.slot.ArmorSlot;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ItemKatana extends ItemTool implements IElectricItem, IUpgradeItem, IBoxable, IItemHudInfo, IModelRegister {

    public final int maxCharge;
    public final int transferLimit;
    public final int tier;
    private final EnumSet<ToolClass> toolClasses;
    private final String name;
    public int damage1;
    protected AudioSource audioSource;
    private int soundTicker;


    public ItemKatana(
            String name
    ) {
        super(0, 2, HarvestLevel.Diamond.toolMaterial, Collections.emptySet());
        this.soundTicker = 0;
        this.toolClasses = EnumSet.of(ToolClass.Sword);
        this.maxCharge = 500000;
        this.transferLimit = 5000;
        this.tier = 4;
        this.name = name;
        this.damage1 = 13;
        setMaxDamage(27);
        setMaxStackSize(1);
        setNoRepair();
        setUnlocalizedName(name);
        setCreativeTab(IUCore.EnergyTab);

        for (final ToolClass aClass : toolClasses) {
            if (((IToolClass) aClass).getName() != null) {
                this.setHarvestLevel(((IToolClass) aClass).getName(), HarvestLevel.Diamond.level);
            }
        }
        BlocksItems.registerItem((Item) this, IUCore.getIdentifier(name)).setUnlocalizedName(name);
        IUCore.proxy.addIModelRegister(this);
        UpgradeSystem.system.addRecipe(this, EnumUpgrades.SABERS.list);

    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation1(String name, String extraName) {
        final String loc = Constants.MOD_ID +
                ':' +
                "energy_tools" + "/" + name + extraName;

        return new ModelResourceLocation(loc, null);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(
            final ItemStack stack,
            @Nullable final World worldIn,
            final List<String> tooltip,
            final ITooltipFlag flagIn
    ) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.changemode_key") + Localization.translate(
                    "iu.changemode_rcm1"));
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    public int getItemEnchantability() {
        return 0;
    }

    public boolean isBookEnchantable(@Nonnull ItemStack stack, @Nonnull ItemStack book) {
        return false;
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(
            @Nonnull final World worldIn,
            final EntityPlayer playerIn,
            @Nonnull final EnumHand handIn
    ) {
        final NBTTagCompound nbt = ModUtils.nbt(playerIn.getHeldItem(handIn));
        switch (nbt.getString("type")) {
            case "":
                nbt.setString("type", "yellow");
                break;
            case "yellow":
                nbt.setString("type", "green");
                break;
            case "green":
                nbt.setString("type", "pink");
                break;
            case "pink":
                nbt.setString("type", "");
                break;
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

    @Override
    public void getSubItems(@Nonnull final CreativeTabs subs, @Nonnull final NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(subs)) {
            ItemStack stack = new ItemStack(this, 1);


            ElectricItem.manager.charge(stack, 2.147483647E9D, 2147483647, true, false);
            items.add(stack);
            ItemStack itemstack = new ItemStack(this, 1, 27);
            items.add(itemstack);
        }
    }

    public boolean canHarvestBlock(IBlockState state, @Nonnull ItemStack itemStack) {
        Material material = state.getMaterial();
        Iterator var4 = this.toolClasses.iterator();

        IToolClass toolClass;
        do {
            if (!var4.hasNext()) {
                return super.canHarvestBlock(state, itemStack);
            }

            toolClass = (IToolClass) var4.next();
            if (toolClass.getBlacklist().contains(state.getBlock())) {
                return false;
            }

            if (toolClass.getBlacklist().contains(material)) {
                return false;
            }

            if (toolClass.getWhitelist().contains(state.getBlock())) {
                return true;
            }
        } while (!toolClass.getWhitelist().contains(material));

        return true;
    }


    public boolean drainSaber(ItemStack itemStack, double amount) {
        int saberenergy = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.SABERENERGY, itemStack) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.SABERENERGY, itemStack).number : 0);


        return ElectricItem.manager.use(itemStack, amount - amount * 0.15 * saberenergy, null);
    }

    @Override
    public double getMaxCharge(ItemStack itemStack) {
        return this.maxCharge;
    }


    @Override
    public boolean canProvideEnergy(ItemStack itemStack) {
        return true;
    }

    public boolean onDroppedByPlayer(@Nonnull ItemStack item, @Nonnull EntityPlayer player) {
        return true;
    }

    public float getDestroySpeed(@Nonnull ItemStack itemStack, @Nonnull IBlockState state) {

        this.soundTicker++;
        if (this.soundTicker % 4 == 0) {
            IC2.platform.playSoundSp(getRandomSwingSound(), 1.0F, 1.0F);
        }
        return 4.0F;


    }

    @Nonnull
    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(
            @Nonnull final EntityEquipmentSlot p_getAttributeModifiers_1_,
            @Nonnull final ItemStack stack
    ) {
        if (p_getAttributeModifiers_1_ != EntityEquipmentSlot.MAINHAND) {
            return super.getAttributeModifiers(p_getAttributeModifiers_1_, stack);
        } else {
            int saberdamage = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.SABER_DAMAGE, stack) ?
                    UpgradeSystem.system.getModules(EnumInfoUpgradeModules.SABER_DAMAGE, stack).number : 0);
            int dmg = (int) (this.damage1 + this.damage1 * 0.15 * saberdamage);
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
    }

    @Override
    public boolean hitEntity(@Nonnull ItemStack stack, @Nonnull EntityLivingBase target, @Nonnull EntityLivingBase source) {
        if (IC2.platform.isSimulating()) {
            if (!drainSaber(stack, 400.0D)) {
                return false;
            }
            int vampires = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.VAMPIRES, stack) ?
                    UpgradeSystem.system.getModules(EnumInfoUpgradeModules.VAMPIRES, stack).number : 0);
            boolean wither = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.WITHER, stack);
            boolean poison = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.POISON, stack);


            if (vampires != 0) {
                target.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 40, vampires));
            }
            if (wither) {
                target.addPotionEffect(new PotionEffect(MobEffects.WITHER, 60));
            }
            if (poison) {
                target.addPotionEffect(new PotionEffect(MobEffects.POISON, 60));
            }
            if (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.HUNGRY, stack)) {
                target.addPotionEffect(new PotionEffect(MobEffects.HUNGER, 60));
            }

            Iterator<EntityEquipmentSlot> var4;
            if (!(source instanceof EntityPlayerMP) || !(target instanceof EntityPlayer) || ((EntityPlayerMP) source).canAttackPlayer(
                    (EntityPlayer) target)) {
                var4 = ArmorSlot.getAll().iterator();

                while (var4.hasNext()) {
                    EntityEquipmentSlot slot = var4.next();
                    if (!ElectricItem.manager.canUse(stack, 2000.0D)) {
                        break;
                    }

                    ItemStack armor = target.getItemStackFromSlot(slot);
                    if (armor.isEmpty()) {
                        double amount = 0.0D;
                        if (armor.getItem() instanceof ItemArmorNanoSuit) {
                            amount = 48000.0D;
                        } else if (armor.getItem() instanceof ItemArmorQuantumSuit) {
                            amount = 300000.0D;
                        }

                        if (amount > 0.0D) {
                            ElectricItem.manager.discharge(armor, amount, this.tier, true, false, false);
                            if (!ElectricItem.manager.canUse(armor, 1.0D)) {
                                target.setItemStackToSlot(slot, ItemStack.EMPTY);
                            }

                            drainSaber(stack, 2000.0D);
                        }
                    }
                }

            }
        }
        if (IC2.platform.isRendering()) {
            IC2.platform.playSoundSp(getRandomSwingSound(), 1.0F, 1.0F);
        }
        return true;
    }

    public String getRandomSwingSound() {
        switch (IC2.random.nextInt(3)) {
            default:
                return "Tools/Nanosabre/NanosabreSwing1.ogg";
            case 1:
                return "Tools/Nanosabre/NanosabreSwing2.ogg";
            case 2:
                break;
        }
        return "Tools/Nanosabre/NanosabreSwing3.ogg";
    }

    @Override
    public boolean onBlockStartBreak(@Nonnull ItemStack stack, @Nonnull BlockPos pos, @Nonnull EntityPlayer player) {
        return drainSaber(stack, 80.0D);
    }


    @Override
    public void onUpdate(@Nonnull ItemStack itemStack, @Nonnull World world, @Nonnull Entity entity, int slot, boolean par5) {
        NBTTagCompound nbt = ModUtils.nbt(itemStack);

        if (!UpgradeSystem.system.hasInMap(itemStack)) {
            nbt.setBoolean("hasID", false);
            MinecraftForge.EVENT_BUS.post(new EventItemLoad(world, this, itemStack));
        }
    }

    @Nonnull
    @Override
    public String getUnlocalizedName() {
        return "iu" + super.getUnlocalizedName().substring(4);
    }

    @Nonnull
    @Override
    public String getUnlocalizedName(@Nonnull ItemStack itemStack) {
        return getUnlocalizedName();
    }

    @Nonnull
    @Override
    public String getItemStackDisplayName(@Nonnull ItemStack itemStack) {
        return Localization.translate(getUnlocalizedName(itemStack));
    }

    @Override
    public int getTier(ItemStack itemStack) {

        return this.tier;
    }

    @Override
    public double getTransferLimit(ItemStack itemStack) {

        return this.transferLimit;
    }

    public boolean canBeStoredInToolbox(ItemStack itemstack) {
        return true;
    }

    public boolean isRepairable() {
        return false;
    }

    @Override
    public List<String> getHudInfo(final ItemStack itemStack, final boolean b) {
        List<String> info = new LinkedList<>();
        info.add(ElectricItem.manager.getToolTip(itemStack));
        info.add(Localization.translate("ic2.item.tooltip.PowerTier") + " " + this.tier);
        return info;
    }

    @Override
    public void registerModels() {
        registerModels(this.name);
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(final String name) {
        ModelLoader.setCustomMeshDefinition(this, stack -> {
            final NBTTagCompound nbt = ModUtils.nbt(stack);

            return getModelLocation1(name, nbt.getString("type").equals("") ? "" : "_" + nbt.getString("type"));
        });
        String[] mode = {"", "_yellow", "_green", "_pink"};
        for (final String s : mode) {
            ModelBakery.registerItemVariants(this, getModelLocation1(name, s));
        }

    }


}
