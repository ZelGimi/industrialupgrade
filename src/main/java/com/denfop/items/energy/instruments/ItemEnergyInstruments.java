package com.denfop.items.energy.instruments;

import com.denfop.Constants;
import com.denfop.ElectricItem;
import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.IModelRegister;
import com.denfop.api.Recipes;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.item.IEnergyItem;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.upgrade.ILevelInstruments;
import com.denfop.api.upgrade.IUpgradeWithBlackList;
import com.denfop.api.upgrade.UpgradeItemInform;
import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.api.upgrade.event.EventItemBlackListLoad;
import com.denfop.audio.EnumSound;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.items.IItemStackInventory;
import com.denfop.items.energy.ItemStackUpgradeItem;
import com.denfop.proxy.CommonProxy;
import com.denfop.register.Register;
import com.denfop.utils.ExperienceUtils;
import com.denfop.utils.KeyboardClient;
import com.denfop.utils.ModUtils;
import com.denfop.utils.RetraceDiggingUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ItemEnergyInstruments extends ItemTool implements IEnergyItem, IItemStackInventory, IUpgradeWithBlackList,
        IModelRegister, ILevelInstruments {

    private final String name;
    private final int transferLimit;
    private final int maxCharge;
    private final int tier;
    private final int normalPower;
    private final int bigHolePower;
    private final int energyPerOperation;
    private final int energyBigHolePowerOperation;
    private final int ultraLowPower1;
    private final int ultraLowPower;
    private final Set<IBlockState> mineableBlocks;
    private final Set<Material> materials;
    private final int energyPerbigHolePowerOperation;
    private final int energyPerultraLowPowerOperation;
    private final List<EnumOperations> operations;
    private final List<Item> item_tools;
    private final String name_type;
    private final float fuel_balance = 10.0F;
    private final EnumTypeInstruments type;
    Set<String> toolType;

    public ItemEnergyInstruments(EnumTypeInstruments type, EnumVarietyInstruments variety, String name) {
        super(0.0F, 0.0F + ToolMaterial.DIAMOND.getAttackDamage(), ToolMaterial.DIAMOND, new HashSet<>());
        this.name = name;
        this.name_type = type.getType_name() == null ? type.name().toLowerCase() : type.getType_name();
        this.transferLimit = variety.getEnergy_transfer();
        this.maxCharge = variety.getCapacity();
        this.tier = variety.getTier();
        this.type = type;
        this.efficiency = this.normalPower = variety.getNormal_power();
        this.bigHolePower = variety.getBig_holes();
        this.energyPerOperation = variety.getEnergyPerOperation();
        this.energyBigHolePowerOperation = variety.getEnergyPerBigOperation();
        this.energyPerbigHolePowerOperation = variety.getEnergyPerbigHolePowerOperation();
        this.energyPerultraLowPowerOperation = variety.getEnergyPerultraLowPowerOperation();
        this.ultraLowPower = variety.getMega_holes();
        this.ultraLowPower1 = variety.getUltra_power();
        this.mineableBlocks = type.getMineableBlocks();
        this.materials = type.getMaterials();
        this.toolType = type.getToolType();
        this.operations = type.getListOperations();
        this.item_tools = type.getListItems();
        this.setMaxDamage(0);
        setCreativeTab(IUCore.EnergyTab);
        this.setUnlocalizedName(name);
        Register.registerItem((Item) this, IUCore.getIdentifier(name)).setUnlocalizedName(name);
        IUCore.proxy.addIModelRegister(this);
        UpgradeSystem.system.addRecipe(this, type.getEnumInfoUpgradeModules());

    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation1(String name, String extraName) {
        final String loc = Constants.MOD_ID +
                ':' +
                "energy_tools" + "/" + name + extraName;

        return new ModelResourceLocation(loc, null);
    }

    @Override
    public List<EnumInfoUpgradeModules> getUpgradeModules() {
        return type.getEnumInfoUpgradeModules();
    }

    @Override
    public int getMaxLevel(final ItemStack stack) {
        int maxLevel = ILevelInstruments.super.getMaxLevel(stack);
        if (maxLevel == Integer.MAX_VALUE) {
            return maxLevel;
        }
        maxLevel *= Math.max(0.5, 0.5 * tier);
        return maxLevel;
    }

    public boolean onBlockDestroyed(
            @Nonnull ItemStack stack,
            @Nonnull World world,
            @Nonnull IBlockState state,
            @Nonnull BlockPos pos,
            @Nonnull EntityLivingBase entity
    ) {
        Block block = state.getBlock();
        if (block.equals(Blocks.AIR)) {
            return false;
        } else {


            if (state.getMaterial() instanceof MaterialLiquid || (state.getBlockHardness(
                    world,
                    pos
            ) == -1 && !((EntityPlayer) entity).capabilities.isCreativeMode)) {
                return false;
            }

            if (!world.isRemote) {
                if (ForgeHooks.onBlockBreakEvent(world, world.getWorldInfo().getGameType(), (EntityPlayerMP) entity, pos) == -1) {
                    return false;
                }


                if (block.removedByPlayer(state, world, pos, (EntityPlayerMP) entity, true)) {
                    List<UpgradeItemInform> upgradeItemInforms = UpgradeSystem.system.getInformation(stack);
                    boolean smelter = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.SMELTER, stack, upgradeItemInforms);
                    boolean comb = UpgradeSystem.system.hasModules(
                            EnumInfoUpgradeModules.COMB_MACERATOR,
                            stack,
                            upgradeItemInforms
                    );
                    boolean mac = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.MACERATOR, stack, upgradeItemInforms);
                    boolean generator = UpgradeSystem.system.hasModules(
                            EnumInfoUpgradeModules.GENERATOR,
                            stack,
                            upgradeItemInforms
                    );
                    int random = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.RANDOM, stack, upgradeItemInforms) ?
                            UpgradeSystem.system.getModules(EnumInfoUpgradeModules.RANDOM, stack, upgradeItemInforms).number : 0;
                    NBTTagCompound nbt = ModUtils.nbt(stack);
                    boolean black_list = nbt.getBoolean("black");

                    block.onBlockDestroyedByPlayer(world, pos, state);
                    block.harvestBlock(world, (EntityPlayerMP) entity, pos, state, null, stack);
                    List<EntityItem> items = entity.getEntityWorld().getEntitiesWithinAABB(
                            EntityItem.class,
                            new AxisAlignedBB(pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1, pos.getX() + 1,
                                    pos.getY() + 1,
                                    pos.getZ() + 1
                            )
                    );
                    ((EntityPlayerMP) entity).addExhaustion(-0.025F);
                    if (!black_list || (ModUtils.getore(block, block.getMetaFromState(state)) && check_list(block,
                            block.getMetaFromState(state)
                            , UpgradeSystem.system.getBlackList(stack)
                    ))) {
                        for (EntityItem item : items) {
                            if (!entity.getEntityWorld().isRemote) {
                                ItemStack stack1 = item.getItem();

                                if (comb) {
                                    RecipeOutput rec = Recipes.recipes.getRecipeOutput("comb_macerator", false, stack1).output;
                                    if (rec != null) {
                                        stack1 = rec.items.get(0).copy();
                                        stack1.setCount(3);
                                    }
                                } else if (mac) {
                                    RecipeOutput rec = Recipes.recipes.getRecipeOutput("macerator", false, stack1).output;
                                    if (rec != null) {
                                        stack1 = rec.items.get(0).copy();

                                    }
                                }
                                ItemStack smelt = new ItemStack(Items.AIR);
                                if (smelter) {
                                    smelt = FurnaceRecipes.instance().getSmeltingResult(stack1).copy();
                                    if (!smelt.isEmpty()) {
                                        smelt.setCount(stack1.getCount());
                                    }
                                }
                                if (generator) {
                                    final int fuel = ModUtils.getFuelValue(stack1, false);
                                    final boolean rec = fuel > 0;
                                    if (rec) {
                                        int amount = stack1.getCount();
                                        int value = fuel / 4;
                                        amount *= value;
                                        amount *= fuel_balance;
                                        double sentPacket = ElectricItem.manager.charge(
                                                stack,
                                                amount,
                                                2147483647,
                                                true,
                                                false
                                        );
                                        amount -= sentPacket;
                                        amount /= (value * fuel_balance);
                                        stack1.setCount(amount);
                                    }
                                }
                                if (!smelt.isEmpty()) {
                                    item.setItem(smelt);
                                } else {
                                    item.setItem(stack1);
                                }

                                item.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, 0.0F, 0.0F);
                                ((EntityPlayerMP) entity).connection.sendPacket(new SPacketEntityTeleport(item));
                                item.setPickupDelay(0);

                            }
                        }
                        if (random != 0) {
                            final int rand = world.rand.nextInt(100001);
                            if (rand >= 100000 - random) {
                                EntityItem item = new EntityItem(world);
                                item.setItem(IUCore.get_ingot.get(world.rand.nextInt(IUCore.get_ingot.size())));
                                item.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, 0.0F, 0.0F);
                                ((EntityPlayerMP) entity).connection.sendPacket(new SPacketEntityTeleport(item));
                                item.setPickupDelay(0);
                            }
                        }
                    } else {
                        if (nbt.getBoolean("black")) {
                            for (EntityItem item : items) {
                                if (!entity.getEntityWorld().isRemote) {
                                    item.setDead();

                                }
                            }
                        }
                        if (random != 0) {
                            final int rand = world.rand.nextInt(100001);
                            if (rand >= 100000 - random) {
                                EntityItem item = new EntityItem(world);
                                item.setItem(IUCore.get_ingot.get(world.rand.nextInt(IUCore.get_ingot.size())));
                                item.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, 0.0F, 0.0F);
                                ((EntityPlayerMP) entity).connection.sendPacket(new SPacketEntityTeleport(item));
                                item.setPickupDelay(0);
                            }
                        }
                    }
                }

                EntityPlayerMP mpPlayer = (EntityPlayerMP) entity;
                mpPlayer.connection.sendPacket(new SPacketBlockChange(world, new BlockPos(pos)));
            } else {
                if (block.removedByPlayer(state, world, pos, (EntityPlayer) entity, true)) {
                    block.onBlockDestroyedByPlayer(world, pos, state);
                }

                Objects.requireNonNull(Minecraft.getMinecraft().getConnection()).sendPacket(new CPacketPlayerDigging(
                        CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK,
                        pos,
                        Minecraft.getMinecraft().objectMouseOver.sideHit
                ));
            }
            if (entity.isEntityAlive()) {
                List<UpgradeItemInform> upgradeItemInforms = UpgradeSystem.system.getInformation(stack);
                float energy = energy(stack, upgradeItemInforms);
                if (energy != 0.0F) {
                    ElectricItem.manager.use(stack, energy, entity);
                }
            }

            return true;
        }
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(
            @Nonnull final World worldIn,
            final EntityPlayer player,
            @Nonnull final EnumHand hand
    ) {

        ItemStack itemStack = player.getHeldItem(hand);
        if (IUCore.keyboard.isSaveModeKeyDown(player)) {
            NBTTagCompound nbt = ModUtils.nbt(itemStack);
            boolean save = !nbt.getBoolean("save");
            CommonProxy.sendPlayerMessage(
                    player,
                    TextFormatting.GREEN + Localization.translate("message.savemode") +
                            (save ? Localization.translate("message.allow") : Localization.translate("message.disallow"))
            );
            nbt.setBoolean("save", save);
        }
        if (IUCore.keyboard.isBlackListModeKeyDown(player)) {
            NBTTagCompound nbt = ModUtils.nbt(itemStack);
            boolean black = !nbt.getBoolean("black");
            CommonProxy.sendPlayerMessage(
                    player,
                    TextFormatting.GREEN + Localization.translate("message.blacklist") +
                            (black ? Localization.translate("message.allow") : Localization.translate("message.disallow"))
            );
            nbt.setBoolean("black", black);
        }
        if (IUCore.keyboard.isChangeKeyDown(player)) {
            int toolMode = readToolMode(itemStack) + 1;
            if (!IUCore.proxy.isRendering()) {
                worldIn.playSound(null, player.posX, player.posY, player.posZ, EnumSound.toolchange.getSoundEvent(),
                        SoundCategory.MASTER, 1F, 1
                );
            }
            if (toolMode >= this.operations.size()) {
                toolMode = 0;
            }
            saveToolMode(itemStack, toolMode);
            EnumOperations operation = this.operations.get(toolMode);
            if (IUCore.proxy.isSimulating()) {
                IUCore.proxy.messagePlayer(
                        player,
                        TextFormatting.GREEN + Localization.translate("message.text.mode") + ": "
                                + operation.getTextFormatting() + operation.getName_mode()
                );
            }
            switch (operation) {
                case ORE:
                case TREE:
                case SHEARS:
                case TUNNEL:
                case DEFAULT:
                    this.efficiency = this.normalPower;
                    break;
                case BIGHOLES:
                    this.efficiency = this.bigHolePower;
                    break;
                case MEGAHOLES:
                    this.efficiency = this.ultraLowPower;
                    break;
                case ULTRAHOLES:
                    this.efficiency = this.ultraLowPower1;
                    break;
            }
            return ActionResult.newResult(EnumActionResult.SUCCESS, itemStack);
        }
        return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand));

    }

    public int readToolMode(ItemStack itemstack) {
        NBTTagCompound nbt = ModUtils.nbt(itemstack);
        int toolMode = nbt.getInteger("toolMode");

        if (toolMode < 0 || toolMode >= this.operations.size()) {
            toolMode = 0;
        }
        return toolMode;
    }

    @Override
    public void registerModels() {
        registerModels(this.name);
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(final String name) {
        ModelLoader.setCustomMeshDefinition(this, stack -> {
            final NBTTagCompound nbt = ModUtils.nbt(stack);
            if (nbt.getString("mode").equals("")) {
                return getModelLocation1(name, nbt.getString("mode"));
            }

            return getModelLocation1(name, "_" + nbt.getString("mode"));

        });
        String[] mode = {"", "Zelen", "Demon", "Dark", "Cold", "Ender", "Ukraine", "Fire", "Snow", "Taiga", "Desert", "Emerald"};
        for (final String s : mode) {
            if (!s.isEmpty()) {
                ModelBakery.registerItemVariants(this, getModelLocation1(name, "_" + s));
            } else {
                ModelBakery.registerItemVariants(this, getModelLocation1(name, s));
            }
        }

    }

    @Override
    public void onUpdate(@Nonnull ItemStack itemStack, @Nonnull World world, @Nonnull Entity entity, int slot, boolean par5) {
        NBTTagCompound nbt = ModUtils.nbt(itemStack);

        if (world.getWorldTime() % 20 == 0 && !UpgradeSystem.system.hasInMap(itemStack)) {
            nbt.setBoolean("hasID", false);
            MinecraftForge.EVENT_BUS.post(new EventItemBlackListLoad(world, this, itemStack, itemStack.getTagCompound()));
        }

        if (entity instanceof EntityPlayer) {
            if (IUCore.keyboard.isBlackListModeViewKeyDown((EntityPlayer) entity)) {
                if (IUCore.proxy.isSimulating() && !itemStack.isEmpty() && ((EntityPlayer) entity)
                        .getHeldItem(EnumHand.MAIN_HAND)
                        .isItemEqual(itemStack)) {
                    ((EntityPlayer) entity).openGui(
                            IUCore.instance,
                            1,
                            world,
                            (int) entity.posX,
                            (int) entity.posY,
                            (int) entity.posZ
                    );

                }
            }
        }
    }

    @Override
    public void getSubItems(@Nonnull final CreativeTabs subs, @Nonnull final NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(subs)) {
            ItemStack stack = new ItemStack(this, 1);

            NBTTagCompound nbt = ModUtils.nbt(stack);
            ElectricItem.manager.charge(stack, 2.147483647E9D, 2147483647, true, false);
            nbt.setInteger("ID_Item", Integer.MAX_VALUE);
            items.add(stack);
            ItemStack itemstack = new ItemStack(this, 1);
            nbt = ModUtils.nbt(itemstack);
            nbt.setInteger("ID_Item", Integer.MAX_VALUE);
            items.add(itemstack);
        }
    }


    @Override
    public int getHarvestLevel(
            @Nonnull final ItemStack stack,
            @Nonnull final String toolClass,
            @Nullable final EntityPlayer player,
            @Nullable final IBlockState blockState
    ) {
        return !this.toolType.contains(toolClass) ?
                super.getHarvestLevel(stack, toolClass, player, blockState)
                : (this.type == EnumTypeInstruments.VAJRA ? 20 : this.toolMaterial.getHarvestLevel());
    }

    public boolean hitEntity(@Nonnull ItemStack stack, @Nonnull EntityLivingBase damagee, @Nonnull EntityLivingBase damager) {
        return true;
    }

    public int getItemEnchantability() {
        return 0;
    }

    public boolean isBookEnchantable(@Nonnull ItemStack stack, @Nonnull ItemStack book) {
        return false;
    }

    public boolean canProvideEnergy(ItemStack itemStack) {
        return false;
    }

    public double getMaxEnergy(ItemStack itemStack) {
        return this.maxCharge;
    }

    public boolean showDurabilityBar(final ItemStack stack) {
        return true;
    }

    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return ModUtils.convertRGBcolorToInt(33, 91, 199);
    }

    public double getDurabilityForDisplay(ItemStack stack) {
        return Math.min(
                Math.max(
                        1 - ElectricItem.manager.getCharge(stack) / ElectricItem.manager.getMaxCharge(stack),
                        0.0
                ),
                1.0
        );
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(
            @Nonnull final ItemStack par1ItemStack,
            @Nullable final World worldIn,
            @Nonnull final List<String> par3List,
            @Nonnull final ITooltipFlag flagIn
    ) {
        int toolMode = readToolMode(par1ItemStack);
        EnumOperations operations = this.operations.get(toolMode);
        par3List.add(TextFormatting.GOLD + Localization.translate("message.text.mode") + ": "
                + operations.getTextFormatting() + operations.getName_mode());
        par3List.add(operations.getDescription());
        if (ModUtils.nbt(par1ItemStack).getBoolean("save")) {
            par3List.add(TextFormatting.GREEN + Localization.translate("iu.savemode_allow"));
        }
        if (ModUtils.nbt(par1ItemStack).getBoolean("black")) {
            par3List.add(TextFormatting.DARK_GRAY + Localization.translate("iu.blacklist_allow"));
        }
        List<UpgradeItemInform> upgradeItemInforms = UpgradeSystem.system.getInformation(par1ItemStack);

        if (operations != EnumOperations.DEFAULT && operations != EnumOperations.ORE && operations != EnumOperations.TREE) {
            int aoe = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.AOE_DIG, par1ItemStack,
                    upgradeItemInforms
            )
                    ?
                    UpgradeSystem.system.getModules(EnumInfoUpgradeModules.AOE_DIG, par1ItemStack, upgradeItemInforms).number
                    : 0);
            int dig_depth = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.DIG_DEPTH, par1ItemStack, upgradeItemInforms)
                    ?
                    UpgradeSystem.system.getModules(EnumInfoUpgradeModules.DIG_DEPTH, par1ItemStack, upgradeItemInforms).number
                    : 0);

            par3List.add(Localization.translate("iu.instruments.info") + (operations.getArea_x() + aoe) + "x" + (operations.getArea_y() + aoe) + "x" + (operations.getArea_z() + dig_depth));
        }
        float energy = energy(par1ItemStack, upgradeItemInforms);

        par3List.add(Localization.translate("iu.instruments.info2") + (int) energy + " EF");

        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            par3List.add(Localization.translate("press.lshift"));
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {


            par3List.add(Localization.translate("iu.changemode_key") + Keyboard.getKeyName(Math.abs(KeyboardClient.changemode.getKeyCode())) + Localization.translate(
                    "iu.changemode_rcm"));

            par3List.add(Localization.translate("iu.blacklist_key") + Keyboard.getKeyName(Math.abs(KeyboardClient.blackmode.getKeyCode())) + Localization.translate(
                    "iu.changemode_rcm"));

            par3List.add(Localization.translate("iu.savemode_key") + Keyboard.getKeyName(Math.abs(KeyboardClient.savemode.getKeyCode())) + Localization.translate(
                    "iu.changemode_rcm"));
            par3List.add(Localization.translate("iu.blacklist_gui") + Keyboard.getKeyName(Math.abs(KeyboardClient.blacklistviewmode.getKeyCode())));

        }
        ModUtils.mode(par1ItemStack, par3List);
        final int level = this.getLevel(par1ItemStack);
        final int maxLevel = this.getMaxLevel(par1ItemStack);
        final int experience = this.getExperience(par1ItemStack);
        par3List.add(Localization.translate("iu.tier")+" "+  level);
        par3List.add(Localization.translate("iu.space_colony_experience")+  experience + "/" + maxLevel);
        super.addInformation(par1ItemStack, worldIn, par3List, flagIn);
    }

    public short getTierItem(ItemStack itemStack) {
        return (short) this.tier;
    }

    public double getTransferEnergy(ItemStack itemStack) {
        return this.transferLimit;
    }

    @Nonnull
    public Set<String> getToolClasses(@Nonnull ItemStack stack) {
        return toolType;
    }

    @Override
    public boolean canHarvestBlock(@Nonnull final IBlockState state, @Nonnull final ItemStack stack) {
        if (mineableBlocks.contains(state)) {
            return true;
        }
        if (materials.contains(state.getMaterial())) {
            return true;
        }
        for (Item item : this.item_tools) {
            if (item.canHarvestBlock(state, stack)
                    || item.getDestroySpeed(stack, state) > 1.0F) {
                return true;
            }
        }
        return false;


    }

    public float getDestroySpeed(@Nonnull ItemStack stack, @Nonnull IBlockState state) {
        List<UpgradeItemInform> upgradeItemInforms = UpgradeSystem.system.getInformation(stack);
        int speed = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.EFFICIENCY, stack, upgradeItemInforms) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.EFFICIENCY, stack, upgradeItemInforms).number : 0;
        speed += UpgradeSystem.system.getUpgradeFromList(stack).get(0);
        return !ElectricItem.manager.canUse(stack, this.energy(stack, upgradeItemInforms))
                ? 0.0F
                : (canHarvestBlock(state, stack) ? (this.efficiency + (int) (this.efficiency * 0.2 * speed)) : 1.0F);

    }

    void chopTree(
            BlockPos pos,
            EntityPlayer player,
            World world,
            ItemStack stack,
            final List<BlockPos> list, float energy,
            final boolean smelter,
            final boolean comb,
            final boolean mac,
            final boolean generator,
            final int random,
            final boolean black_list, List<String> blackList
    ) {
        int Y = pos.getY();
        int X = pos.getX();
        int Z = pos.getZ();
        for (int xPos = X - 1; xPos <= X + 1; xPos++) {
            for (int yPos = Y; yPos <= Y + 1; yPos++) {
                for (int zPos = Z - 1; zPos <= Z + 1; zPos++) {
                    BlockPos pos1 = new BlockPos(xPos, yPos, zPos);
                    if (list.contains(pos1)) {
                        continue;
                    }
                    IBlockState state = world.getBlockState(pos1);
                    Block block = state.getBlock();
                    list.add(pos1);
                    if (block.isWood(world, pos1)) {

                        if (!player.capabilities.isCreativeMode) {
                            onBlockDestroyed(stack, world, state, pos1, player, energy, smelter, comb, mac, generator, random,
                                    black_list, blackList
                            );
                        }
                        chopTree(
                                pos1,
                                player,
                                world,
                                stack,
                                list,
                                energy,
                                smelter,
                                comb,
                                mac,
                                generator,
                                random,
                                black_list,
                                blackList
                        );
                    }
                }
            }
        }
    }

    private boolean isTree(World world, BlockPos pos) {
        Block wood = world.getBlockState(pos).getBlock();
        if (wood.equals(Blocks.AIR) || !wood.isWood(world, pos)) {
            return false;
        }
        int top = pos.getY();
        int Y = pos.getY();
        int X = pos.getX();
        int Z = pos.getZ();
        for (int y = pos.getY(); y <= pos.getY() + 50; y++) {
            BlockPos pos1 = new BlockPos(X, y, Z);
            final IBlockState blockstate = world.getBlockState(pos1);
            if (!blockstate.getBlock().isWood(world, pos1)
                    && !blockstate.getBlock().isLeaves(blockstate, world, pos1)) {
                top += y;
                break;
            }
        }
        int leaves = 0;
        for (int xPos = X - 1; xPos <= X + 1; xPos++) {
            for (int yPos = Y; yPos <= top; yPos++) {
                for (int zPos = Z - 1; zPos <= Z + 1; zPos++) {
                    BlockPos pos1 = new BlockPos(xPos, yPos, zPos);
                    final IBlockState blockstate = world.getBlockState(pos1);
                    if (blockstate.getBlock().isLeaves(blockstate, world, pos1)) {
                        leaves++;
                    }
                }
            }
        }
        return leaves >= 3;
    }

    //
    boolean break_block(
            World world, Block block, RayTraceResult mop, byte mode_item, EntityPlayer player, BlockPos pos,
            ItemStack stack,
            final List<UpgradeItemInform> upgradeItemInforms,
            final boolean smelter,
            final boolean comb,
            final boolean mac,
            final boolean generator,
            final int random,
            final boolean black_list, List<String> blackList
    ) {
        byte xRange = mode_item;
        byte yRange = mode_item;
        byte zRange = mode_item;
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        byte dig_depth = (byte) (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.DIG_DEPTH, stack, upgradeItemInforms) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.DIG_DEPTH, stack, upgradeItemInforms).number : 0);
        dig_depth += (byte) ((int) UpgradeSystem.system.getUpgradeFromList(stack).get(4));
        switch (mop.sideHit.ordinal()) {
            case 0:
            case 1:
                yRange = dig_depth;
                break;
            case 2:
            case 3:
                zRange = dig_depth;
                break;
            case 4:
            case 5:
                xRange = dig_depth;
                break;
        }

        boolean lowPower = false;
        boolean silktouch = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0;
        int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack) + UpgradeSystem.system
                .getUpgradeFromList(stack)
                .get(2);
        fortune = Math.min(3, fortune);

        int Yy;
        Yy = yRange > 0 ? yRange - 1 : 0;
        NBTTagCompound nbt = ModUtils.nbt(stack);
        float energy = energy(stack, upgradeItemInforms);


        boolean save = nbt.getBoolean("save");
        if (!player.capabilities.isCreativeMode) {
            for (int xPos = x - xRange; xPos <= x + xRange; xPos++) {
                for (int yPos = y - yRange + Yy; yPos <= y + yRange + Yy; yPos++) {
                    for (int zPos = z - zRange; zPos <= z + zRange; zPos++) {
                        if (ElectricItem.manager.canUse(stack, energy)) {

                            BlockPos pos_block = new BlockPos(xPos, yPos, zPos);
                            if (save) {
                                if (world.getTileEntity(pos_block) != null) {
                                    continue;
                                }
                            }

                            IBlockState state = world.getBlockState(pos_block);
                            Block localBlock = state.getBlock();
                            if (!localBlock.equals(Blocks.AIR) && canHarvestBlock(state, stack)
                                    && state.getBlockHardness(world, pos_block) >= 0.0F
                            ) {
                                if (state.getBlockHardness(world, pos_block) > 0.0F) {
                                    onBlockDestroyed(stack, world, state, pos_block,
                                            player, energy, smelter, comb, mac, generator, random, black_list, blackList
                                    );
                                }
                                if (!silktouch) {
                                    ExperienceUtils.addPlayerXP(player, getExperience(state, world, pos_block, fortune, stack
                                            , localBlock));
                                }


                            }
                        } else {
                            lowPower = true;
                            break;
                        }
                    }
                }
            }
        } else {
            if (ElectricItem.manager.canUse(stack, energy)) {
                IBlockState state = world.getBlockState(pos);
                Block localBlock = state.getBlock();
                if (!localBlock.equals(Blocks.AIR) && canHarvestBlock(state, stack)
                        && state.getBlockHardness(world, pos) >= 0.0F
                        || (
                        block == Blocks.MONSTER_EGG)) {
                    if (state.getBlockHardness(world, pos) > 0.0F) {
                        onBlockDestroyed(stack, world, state, pos,
                                player, energy,
                                smelter, comb, mac, generator, random,
                                black_list, blackList
                        );
                    }
                    if (!silktouch) {
                        ExperienceUtils.addPlayerXP(player, getExperience(state, world, pos, fortune, stack
                                , localBlock));
                    }


                } else {
                    if (state.getBlockHardness(world, pos) > 0.0F) {
                        return onBlockDestroyed(stack, world, state, pos,
                                player, energy,
                                smelter, comb, mac, generator, random,
                                black_list, blackList
                        );
                    }
                }
            }
        }
        if (lowPower) {
            if (ElectricItem.manager.canUse(stack, energy)) {
                IBlockState state = world.getBlockState(pos);
                Block localBlock = state.getBlock();

                if (!localBlock.equals(Blocks.AIR) && canHarvestBlock(state, stack)
                        && state.getBlockHardness(world, pos) >= 0.0F
                        || (block == Blocks.MONSTER_EGG)) {

                    if (state.getBlockHardness(world, pos) > 0.0F) {
                        onBlockDestroyed(stack, world, state, pos,
                                player, energy,
                                smelter, comb, mac, generator, random,
                                black_list, blackList
                        );
                    }
                    if (!silktouch) {
                        ExperienceUtils.addPlayerXP(player, getExperience(state, world, pos, fortune, stack
                                , localBlock));
                    }


                } else {
                    if (state.getBlockHardness(world, pos) > 0.0F) {
                        return onBlockDestroyed(stack, world, state, pos,
                                player, energy,
                                smelter, comb, mac, generator, random,
                                black_list, blackList
                        );
                    }
                }
            }
        }
        return true;
    }

    public EnumTypeInstruments getType() {
        return type;
    }

    public List<EnumOperations> getOperations() {
        return operations;
    }

    public boolean onBlockStartBreak(@Nonnull ItemStack stack, @Nonnull BlockPos pos, @Nonnull EntityPlayer player) {
        EnumOperations operations = this.operations.get(readToolMode(stack));
        World world = player.getEntityWorld();
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        RayTraceResult mop = RetraceDiggingUtils.retrace(player);
        List<UpgradeItemInform> upgradeItemInforms = UpgradeSystem.system.getInformation(stack);
        boolean smelter = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.SMELTER, stack, upgradeItemInforms);
        boolean comb = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.COMB_MACERATOR, stack, upgradeItemInforms);
        boolean mac = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.MACERATOR, stack, upgradeItemInforms);
        boolean generator = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.GENERATOR, stack, upgradeItemInforms);
        int random = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.RANDOM, stack, upgradeItemInforms) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.RANDOM, stack, upgradeItemInforms).number : 0;
        NBTTagCompound nbt = ModUtils.nbt(stack);
        boolean black_list = nbt.getBoolean("black");
        final List<String> list = UpgradeSystem.system.getBlackList(stack);
        switch (operations) {
            case DEFAULT:

                if (block == Blocks.AIR) {
                    return super.onBlockStartBreak(stack, pos, player);
                }

                byte aoe = (byte) (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.AOE_DIG, stack, upgradeItemInforms) ?
                        UpgradeSystem.system.getModules(EnumInfoUpgradeModules.AOE_DIG, stack, upgradeItemInforms).number : 0);
                aoe += (byte) ((int) UpgradeSystem.system.getUpgradeFromList(stack).get(3));
                return break_block(world, block, mop, aoe, player, pos, stack, upgradeItemInforms, smelter, comb, mac, generator,
                        random, black_list, list
                );
            case BIGHOLES:

                if (block.equals(Blocks.AIR)) {
                    return super.onBlockStartBreak(stack, pos, player);
                }


                aoe = (byte) (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.AOE_DIG, stack, upgradeItemInforms) ?
                        UpgradeSystem.system.getModules(EnumInfoUpgradeModules.AOE_DIG, stack, upgradeItemInforms).number : 0);
                aoe += (byte) ((int) UpgradeSystem.system.getUpgradeFromList(stack).get(3));
                if (player.isSneaking()) {
                    if (!mop.typeOfHit.equals(RayTraceResult.Type.MISS)) {
                        return break_block(world, block, mop, aoe, player, pos, stack, upgradeItemInforms,
                                smelter,
                                comb,
                                mac,
                                generator,
                                random,
                                black_list, list
                        );
                    }
                }

                return break_block(world, block, mop, (byte) (1 + aoe), player, pos, stack, upgradeItemInforms,
                        smelter,
                        comb,
                        mac,
                        generator,
                        random,
                        black_list, list
                );
            case MEGAHOLES:
                if (block.equals(Blocks.AIR)) {
                    return super.onBlockStartBreak(stack, pos, player);
                }

                aoe = (byte) (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.AOE_DIG, stack, upgradeItemInforms) ?
                        UpgradeSystem.system.getModules(EnumInfoUpgradeModules.AOE_DIG, stack, upgradeItemInforms).number : 0);
                aoe += (byte) ((int) UpgradeSystem.system.getUpgradeFromList(stack).get(3));
                if (player.isSneaking()) {
                    if (!mop.typeOfHit.equals(RayTraceResult.Type.MISS)) {
                        return break_block(world, block, mop, aoe, player, pos, stack, upgradeItemInforms,
                                smelter,
                                comb,
                                mac,
                                generator,
                                random,
                                black_list, list
                        );
                    }
                }
                return break_block(world, block, mop, (byte) (2 + aoe), player, pos, stack, upgradeItemInforms,
                        smelter,
                        comb,
                        mac,
                        generator,
                        random,
                        black_list, list
                );
            case ULTRAHOLES:

                if (block.equals(Blocks.AIR)) {
                    return super.onBlockStartBreak(stack, pos, player);
                }
                aoe = (byte) (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.AOE_DIG, stack, upgradeItemInforms) ?
                        UpgradeSystem.system.getModules(EnumInfoUpgradeModules.AOE_DIG, stack, upgradeItemInforms).number : 0);
                aoe += (byte) ((int) UpgradeSystem.system.getUpgradeFromList(stack).get(3));
                if (player.isSneaking()) {
                    if (!mop.typeOfHit.equals(RayTraceResult.Type.MISS)) {
                        return break_block(world, block, mop, aoe, player, pos, stack, upgradeItemInforms,
                                smelter,
                                comb,
                                mac,
                                generator,
                                random,
                                black_list, list
                        );
                    }
                }
                return break_block(world, block, mop, (byte) (3 + aoe), player, pos, stack, upgradeItemInforms,
                        smelter,
                        comb,
                        mac,
                        generator,
                        random,
                        black_list, list
                );
            case ORE:
                if (block.equals(Blocks.AIR)) {
                    return super.onBlockStartBreak(stack, pos, player);
                }

                boolean silktouch = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0;
                int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);

                nbt.setInteger("ore", 1);
                float energy = energy(stack, upgradeItemInforms);
                if (!mop.typeOfHit.equals(RayTraceResult.Type.MISS)) {
                    ore_break(world, pos, player, silktouch, fortune, false, stack, block, smelter,
                            comb,
                            mac,
                            generator,
                            random, black_list, energy, list
                    );
                    return break_block(world, block, mop, (byte) (0), player, pos, stack, upgradeItemInforms,
                            smelter,
                            comb,
                            mac,
                            generator,
                            random,
                            black_list, list
                    );
                }
                return break_block(world, block, mop, (byte) (0), player, pos, stack, upgradeItemInforms,
                        smelter,
                        comb,
                        mac,
                        generator,
                        random,
                        black_list, list
                );
            case TREE:
                if (!IUCore.dynamicTrees && isTree(player.getEntityWorld(), pos)) {
                    player.getEntityWorld().playEvent(
                            2001,
                            pos,
                            Block.getIdFromBlock(player.getEntityWorld().getBlockState(pos).getBlock())
                                    + (player
                                    .getEntityWorld()
                                    .getBlockState(pos)
                                    .getBlock()
                                    .getMetaFromState(player.getEntityWorld().getBlockState(pos)) << 12)
                    );

                    List<BlockPos> list1 = new ArrayList<>();
                    energy = energy(stack, upgradeItemInforms);
                    chopTree(pos, player, player.getEntityWorld(), stack, list1, energy, smelter,
                            comb,
                            mac,
                            generator,
                            random, black_list, list
                    );
                    return break_block(world, block, mop, (byte) (0), player, pos, stack, upgradeItemInforms,
                            smelter,
                            comb,
                            mac,
                            generator,
                            random,
                            black_list, list
                    );
                }
                return break_block(world, block, mop, (byte) (0), player, pos, stack, upgradeItemInforms,
                        smelter,
                        comb,
                        mac,
                        generator,
                        random,
                        black_list, list
                );
            case TUNNEL:

                if (block == Blocks.AIR) {
                    return super.onBlockStartBreak(stack, pos, player);
                }


                return break_block_tunel(world, block, mop, player, pos, stack, upgradeItemInforms, smelter, comb, mac,
                        generator,
                        random, black_list, list
                );
            case SHEARS:
                if (block == Blocks.AIR) {
                    return super.onBlockStartBreak(stack, pos, player);
                }
                if (block instanceof IShearable) {
                    return break_shears(world, block, mop, player, pos, stack, upgradeItemInforms, smelter, comb, mac,
                            generator,
                            random, black_list, list
                    );
                } else {
                    aoe = (byte) (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.AOE_DIG, stack, upgradeItemInforms)
                            ?
                            UpgradeSystem.system.getModules(EnumInfoUpgradeModules.AOE_DIG, stack, upgradeItemInforms).number
                            : 0);
                    aoe += (byte) ((int) UpgradeSystem.system.getUpgradeFromList(stack).get(3));
                    return break_block(
                            world,
                            block,
                            mop,
                            aoe,
                            player,
                            pos,
                            stack,
                            upgradeItemInforms,
                            smelter,
                            comb,
                            mac,
                            generator,
                            random,
                            black_list,
                            list
                    );
                }
        }
        return false;
    }

    private boolean break_shears(
            World world,
            Block block,
            RayTraceResult mop,
            EntityPlayer player,
            BlockPos pos,
            ItemStack itemstack,
            List<UpgradeItemInform> upgradeItemInforms,
            boolean smelter,
            boolean comb,
            boolean mac,
            boolean generator,
            int random,
            boolean blackList,
            List<String> list
    ) {
        float energy = energy(itemstack, upgradeItemInforms);
        IBlockState state = world.getBlockState(pos);
        IShearable target = (IShearable) block;
        if (target.isShearable(itemstack, world, pos) && ElectricItem.manager.use(itemstack, energy, player)) {
            if (!block.equals(Blocks.AIR) && canHarvestBlock(state, itemstack)
                    && state.getBlockHardness(world, pos) >= 0.0F
            ) {
                if (state.getBlockHardness(world, pos) > 0.0F) {
                    onBlockDestroyed(itemstack, world, state, pos,
                            player, energy, smelter, comb, mac, generator, random, blackList, list, true
                    );
                    return true;
                }
            }
        }
        return false;
    }

    private boolean break_block_tunel(
            World world, Block block, RayTraceResult mop, EntityPlayer player, BlockPos pos,
            ItemStack stack,
            final List<UpgradeItemInform> upgradeItemInforms,
            final boolean smelter,
            final boolean comb,
            final boolean mac,
            final boolean generator,
            final int random,
            final boolean black_list, List<String> list
    ) {
        byte xRange = 6;
        byte yRange = 6;
        byte zRange = 6;
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        switch (mop.sideHit.ordinal()) {

            case 0:
            case 1:
                xRange = 0;
                zRange = 0;
                break;
            case 2:
            case 3:
                xRange = 0;
                yRange = 0;
                break;
            case 4:
            case 5:
                zRange = 0;
                yRange = 0;
                break;
        }

        boolean lowPower = false;
        boolean silktouch = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0;
        int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);

        NBTTagCompound nbt = ModUtils.nbt(stack);
        float energy = energy(stack, upgradeItemInforms);
        byte dig_depth = (byte) (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.DIG_DEPTH, stack, upgradeItemInforms) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.DIG_DEPTH, stack, upgradeItemInforms).number : 0);
        if (dig_depth > 0) {
            if (zRange > 0) {
                xRange = dig_depth;

            } else if (xRange > 0) {
                zRange = dig_depth;
            } else {
                zRange = dig_depth;
            }
        }
        boolean save = nbt.getBoolean("save");
        if (!player.capabilities.isCreativeMode) {
            for (int xPos = x - xRange; xPos <= x + xRange; xPos++) {
                for (int yPos = y - yRange; yPos <= y + yRange; yPos++) {
                    for (int zPos = z - zRange; zPos <= z + zRange; zPos++) {
                        if (ElectricItem.manager.canUse(stack, energy)) {

                            BlockPos pos_block = new BlockPos(xPos, yPos, zPos);
                            if (save) {
                                if (world.getTileEntity(pos_block) != null) {
                                    continue;
                                }
                            }

                            IBlockState state = world.getBlockState(pos_block);
                            Block localBlock = state.getBlock();

                            if (!localBlock.equals(Blocks.AIR) && canHarvestBlock(state, stack)
                                    && state.getBlockHardness(world, pos_block) >= 0.0F
                            ) {
                                if (state.getBlockHardness(world, pos_block) > 0.0F) {
                                    onBlockDestroyed(stack, world, state, pos_block,
                                            player, energy, smelter, comb, mac, generator, random, black_list
                                            , list
                                    );
                                }
                                if (!silktouch) {
                                    ExperienceUtils.addPlayerXP(player, getExperience(state, world, pos_block, fortune, stack
                                            , localBlock));
                                }


                            }


                        } else {
                            lowPower = true;
                            break;
                        }
                    }
                }
            }
        } else {
            if (ElectricItem.manager.canUse(stack, energy)) {
                IBlockState state = world.getBlockState(pos);
                Block localBlock = state.getBlock();


                if (!localBlock.equals(Blocks.AIR) && canHarvestBlock(state, stack)
                        && state.getBlockHardness(world, pos) >= 0.0F
                        || (
                        block == Blocks.MONSTER_EGG)) {
                    if (state.getBlockHardness(world, pos) > 0.0F) {
                        onBlockDestroyed(stack, world, state, pos,
                                player, energy,
                                smelter, comb, mac, generator, random,
                                black_list, list
                        );
                    }
                    if (!silktouch) {
                        ExperienceUtils.addPlayerXP(player, getExperience(state, world, pos, fortune, stack
                                , localBlock));
                    }


                } else {
                    if (state.getBlockHardness(world, pos) > 0.0F) {
                        return onBlockDestroyed(stack, world, state, pos,
                                player, energy,
                                smelter, comb, mac, generator, random,
                                black_list, list
                        );
                    }
                }
            }
        }
        if (lowPower) {
            if (ElectricItem.manager.canUse(stack, energy)) {
                IBlockState state = world.getBlockState(pos);
                Block localBlock = state.getBlock();

                if (!localBlock.equals(Blocks.AIR) && canHarvestBlock(state, stack)
                        && state.getBlockHardness(world, pos) >= 0.0F
                        || (block == Blocks.MONSTER_EGG)) {

                    if (state.getBlockHardness(world, pos) > 0.0F) {
                        onBlockDestroyed(stack, world, state, pos,
                                player, energy,
                                smelter, comb, mac, generator, random,
                                black_list, list
                        );
                    }
                    if (!silktouch) {
                        ExperienceUtils.addPlayerXP(player, getExperience(state, world, pos, fortune, stack
                                , localBlock));
                    }


                } else {
                    if (state.getBlockHardness(world, pos) > 0.0F) {
                        return onBlockDestroyed(stack, world, state, pos,
                                player, energy,
                                smelter, comb, mac, generator, random,
                                black_list, list
                        );
                    }
                }
            }
        }
        return true;
    }

    //
    private void ore_break(
            World world, BlockPos pos, EntityPlayer player, boolean silktouch, int fortune, boolean lowPower,
            ItemStack stack, Block block1,
            final boolean smelter,
            final boolean comb,
            final boolean mac,
            final boolean generator,
            final int random,
            final boolean black_list, float energy, List<String> list
    ) {

        NBTTagCompound NBTTagCompound = ModUtils.nbt(stack);
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        for (int Xx = x - 1; Xx <= x + 1; Xx++) {
            for (int Yy = y - 1; Yy <= y + 1; Yy++) {
                for (int Zz = z - 1; Zz <= z + 1; Zz++) {

                    int ore = NBTTagCompound.getInteger("ore");
                    if (ore < 40) {
                        BlockPos pos_block = new BlockPos(Xx, Yy, Zz);
                        IBlockState state = world.getBlockState(pos_block);
                        Block localBlock = state.getBlock();
                        if (ModUtils.getore(localBlock, block1)) {
                            if (ElectricItem.manager.canUse(
                                    stack, energy

                            )) {


                                if (!player.capabilities.isCreativeMode) {

                                    if (state.getBlockHardness(world, pos_block) > 0.0F) {
                                        onBlockDestroyed(stack, world, state, pos_block,
                                                player, energy,
                                                smelter, comb, mac, generator, random,
                                                black_list, list
                                        );

                                    }
                                    if (!silktouch) {
                                        ExperienceUtils.addPlayerXP(player, getExperience(state, world, pos_block, fortune, stack
                                                , localBlock));
                                    }


                                    ore = ore + 1;
                                    NBTTagCompound.setInteger("ore", ore);
                                    ore_break(world, pos_block, player, silktouch, fortune, lowPower, stack, block1,
                                            smelter, comb, mac, generator, random,
                                            black_list, energy, list
                                    );
                                } else {
                                    break;
                                }


                            } else {
                                lowPower = true;
                                break;
                            }
                        }
                    }
                }
            }
        }

    }

    private int getExperience(
            IBlockState state,
            World world,
            BlockPos pos_block,
            int fortune,
            ItemStack stack,
            final Block localBlock
    ) {
        int col = localBlock.getExpDrop(state, world, pos_block, fortune);
        col *= (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.EXPERIENCE, stack) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.EXPERIENCE, stack).number * 0.5 + 1 : 1);
        return col;
    }

    public boolean onBlockDestroyed(
            @Nonnull ItemStack stack,
            @Nonnull World world,
            IBlockState state,
            @Nonnull BlockPos pos,
            @Nonnull EntityLivingBase entity, float energy,
            final boolean smelter,
            final boolean comb,
            final boolean mac,
            final boolean generator,
            final int random,
            final boolean black_list,
            List<String> blackList
    ) {
        return onBlockDestroyed(
                stack,
                world,
                state,
                pos,
                entity,
                energy,
                smelter,
                comb,
                mac,
                generator,
                random,
                black_list,
                blackList,
                false
        );
    }

    public boolean onBlockDestroyed(
            @Nonnull ItemStack stack,
            @Nonnull World world,
            IBlockState state,
            @Nonnull BlockPos pos,
            @Nonnull EntityLivingBase entity, float energy,
            final boolean smelter,
            final boolean comb,
            final boolean mac,
            final boolean generator,
            final int random,
            final boolean black_list,
            List<String> blackList, boolean shears
    ) {

        Block block = state.getBlock();
        if (block.equals(Blocks.AIR)) {
            return false;
        } else {


            if (state.getMaterial() instanceof MaterialLiquid || (state.getBlockHardness(
                    world,
                    pos
            ) == -1 && !((EntityPlayer) entity).capabilities.isCreativeMode)) {
                return false;
            }

            if (!world.isRemote) {

                if (ForgeHooks.onBlockBreakEvent(world, world.getWorldInfo().getGameType(), (EntityPlayerMP) entity, pos) == -1) {
                    return false;
                }
                List<ItemStack> drops1 = null;
                if (shears && block instanceof IShearable) {
                    drops1 = ((IShearable) block).onSheared(stack, world, pos,
                            EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack)
                    );

                }

                if (block.removedByPlayer(state, world, pos, (EntityPlayerMP) entity, true)) {
                    block.onBlockDestroyedByPlayer(world, pos, state);
                    this.addExperience(stack, 1);
                    if (!shears) {
                        block.harvestBlock(world, (EntityPlayerMP) entity, pos, state, null, stack);
                        NBTTagCompound nbt = ModUtils.nbt(stack);
                        List<EntityItem> items = entity.getEntityWorld().getEntitiesWithinAABB(
                                EntityItem.class,
                                new AxisAlignedBB(pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1, pos.getX() + 1,
                                        pos.getY() + 1,
                                        pos.getZ() + 1
                                )
                        );
                        ((EntityPlayerMP) entity).addExhaustion(-0.025F);
                        if (!black_list || (ModUtils.getore(block, block.getMetaFromState(state)) && check_list(block,
                                block.getMetaFromState(state)
                                , blackList
                        ))) {
                            for (EntityItem item : items) {
                                if (!entity.getEntityWorld().isRemote) {
                                    ItemStack stack1 = item.getItem();

                                    if (comb) {
                                        final BaseMachineRecipe rec = Recipes.recipes.getRecipeOutput(
                                                "comb_macerator",
                                                false,
                                                stack1
                                        );
                                        if (rec != null) {
                                            stack1 = rec.output.items.get(0).copy();
                                            item.setItem(stack1);
                                        }
                                    } else if (mac) {
                                        final BaseMachineRecipe rec = Recipes.recipes.getRecipeOutput("macerator", false, stack1);
                                        if (rec != null) {
                                            stack1 = rec.output.items.get(0).copy();
                                            item.setItem(stack1);
                                        }
                                    }

                                    ItemStack smelt;
                                    if (smelter) {
                                        smelt = FurnaceRecipes.instance().getSmeltingResult(stack1).copy();
                                        if (!smelt.isEmpty()) {
                                            smelt.setCount(stack1.getCount());
                                            item.setItem(smelt);
                                        } else {
                                            item.setItem(item.getItem());
                                        }
                                    }
                                    if (generator) {
                                        final int fuel = ModUtils.getFuelValue(stack1, false);
                                        final boolean rec = fuel > 0;
                                        if (rec) {
                                            int amount = stack1.getCount();
                                            int value = fuel / 4;
                                            amount *= value;
                                            amount *= fuel_balance;
                                            double sentPacket = ElectricItem.manager.charge(
                                                    stack,
                                                    amount,
                                                    2147483647,
                                                    true,
                                                    false
                                            );
                                            amount -= sentPacket;
                                            amount /= (value * fuel_balance);
                                            stack1.setCount(amount);
                                        }
                                    }
                                    item.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, 0.0F, 0.0F);
                                    item.setPickupDelay(0);
                                    ((EntityPlayerMP) entity).connection.sendPacket(new SPacketEntityTeleport(item));


                                }
                            }
                        } else {
                            if (nbt.getBoolean("black")) {
                                for (EntityItem item : items) {
                                    if (!entity.getEntityWorld().isRemote) {
                                        item.setDead();

                                    }
                                }
                            }
                        }
                    } else {

                        for (final ItemStack stack1 : drops1) {
                            ModUtils.dropAsEntity(world, pos, stack1);
                        }
                        final NBTTagCompound nbt = ModUtils.nbt(stack);
                        List<EntityItem> items = entity.getEntityWorld().getEntitiesWithinAABB(
                                EntityItem.class,
                                new AxisAlignedBB(pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1, pos.getX() + 1,
                                        pos.getY() + 1,
                                        pos.getZ() + 1
                                )
                        );
                        ((EntityPlayerMP) entity).addExhaustion(-0.025F);
                        if (!black_list || (ModUtils.getore(block, block.getMetaFromState(state)) && check_list(block,
                                block.getMetaFromState(state)
                                , blackList
                        ))) {
                            for (EntityItem item : items) {
                                if (!entity.getEntityWorld().isRemote) {
                                    ItemStack stack1 = item.getItem();

                                    if (comb) {
                                        final BaseMachineRecipe rec = Recipes.recipes.getRecipeOutput(
                                                "comb_macerator",
                                                false,
                                                stack1
                                        );
                                        if (rec != null) {
                                            stack1 = rec.output.items.get(0).copy();
                                            item.setItem(stack1);
                                        }
                                    } else if (mac) {
                                        final BaseMachineRecipe rec = Recipes.recipes.getRecipeOutput("macerator", false, stack1);
                                        if (rec != null) {
                                            stack1 = rec.output.items.get(0).copy();
                                            item.setItem(stack1);
                                        }
                                    }

                                    ItemStack smelt;
                                    if (smelter) {
                                        smelt = FurnaceRecipes.instance().getSmeltingResult(stack1).copy();
                                        if (!smelt.isEmpty()) {
                                            smelt.setCount(stack1.getCount());
                                            item.setItem(smelt);
                                        } else {
                                            item.setItem(item.getItem());
                                        }
                                    }
                                    if (generator) {
                                        final int fuel = ModUtils.getFuelValue(stack1, false);
                                        final boolean rec = fuel > 0;
                                        if (rec) {
                                            int amount = stack1.getCount();
                                            int value = fuel / 4;
                                            amount *= value;
                                            amount *= fuel_balance;
                                            double sentPacket = ElectricItem.manager.charge(
                                                    stack,
                                                    amount,
                                                    2147483647,
                                                    true,
                                                    false
                                            );
                                            amount -= sentPacket;
                                            amount /= (value * fuel_balance);
                                            stack1.setCount(amount);
                                        }
                                    }
                                    item.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, 0.0F, 0.0F);
                                    item.setPickupDelay(0);
                                    ((EntityPlayerMP) entity).connection.sendPacket(new SPacketEntityTeleport(item));


                                }
                            }
                        } else {
                            if (nbt.getBoolean("black")) {
                                for (EntityItem item : items) {
                                    if (!entity.getEntityWorld().isRemote) {
                                        item.setDead();

                                    }
                                }
                            }
                        }
                        return true;
                    }
                }
                if (random != 0) {
                    final int rand = world.rand.nextInt(100001);
                    if (rand >= 100000 - random) {
                        EntityItem item = new EntityItem(world);
                        item.setItem(IUCore.get_ingot.get(world.rand.nextInt(IUCore.get_ingot.size())));
                        item.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, 0.0F, 0.0F);
                        ((EntityPlayerMP) entity).connection.sendPacket(new SPacketEntityTeleport(item));
                    }
                }
                EntityPlayerMP mpPlayer = (EntityPlayerMP) entity;
                mpPlayer.connection.sendPacket(new SPacketBlockChange(world, pos));
            } else {
                if (block.removedByPlayer(state, world, pos, (EntityPlayer) entity, true)) {
                    block.onBlockDestroyedByPlayer(world, pos, state);
                }

                Objects.requireNonNull(Minecraft.getMinecraft().getConnection()).sendPacket(new CPacketPlayerDigging(
                        CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK,
                        pos,
                        Minecraft.getMinecraft().objectMouseOver.sideHit
                ));
            }
            if (entity.isEntityAlive()) {

                if (energy != 0.0F) {
                    ElectricItem.manager.use(stack, energy, entity);
                }
            }

            return true;
        }
    }

    @Override
    public IAdvInventory getInventory(final EntityPlayer player, final ItemStack stack) {
        return new ItemStackUpgradeItem(player, stack);
    }


    public boolean check_list(Block block, int metaFromState, final List<String> blackList) {

        if (blackList.isEmpty()) {
            return true;
        }

        ItemStack stack1 = new ItemStack(block, 1, metaFromState);
        if (stack1.isEmpty()) {
            return true;
        }
        final int[] ids = OreDictionary.getOreIDs(stack1);
        if (ids.length < 1) {
            return true;
        }

        String name = OreDictionary.getOreName(ids[0]);


        return !blackList.contains(name);
    }

    public void saveToolMode(ItemStack itemstack, int toolMode) {
        NBTTagCompound nbt = ModUtils.nbt(itemstack);
        nbt.setInteger("toolMode", toolMode);
        itemstack.setTagCompound(nbt);
    }


    public float energy(ItemStack stack, final List<UpgradeItemInform> upgradeItemInforms) {
        double energy1 = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.ENERGY, stack, upgradeItemInforms) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.ENERGY, stack, upgradeItemInforms).number : 0;
        final List<Integer> list = UpgradeSystem.system.getUpgradeFromList(stack);
        if (!list.isEmpty()) {
            energy1 += (list).get(1) / 6D;
        }
        int toolMode = readToolMode(stack);
        float energy;
        EnumOperations operations = this.operations.get(toolMode);
        switch (operations) {
            case BIGHOLES:
                energy = (float) (this.energyBigHolePowerOperation - this.energyBigHolePowerOperation * 0.25 * energy1);
                break;
            case MEGAHOLES:
                energy = (float) (this.energyPerbigHolePowerOperation - this.energyPerbigHolePowerOperation * 0.25 * energy1);
                break;
            case ULTRAHOLES:
                energy = (float) (this.energyPerultraLowPowerOperation - this.energyPerultraLowPowerOperation * 0.25 * energy1);
                break;
            default:
                energy = (float) (this.energyPerOperation - this.energyPerOperation * 0.25 * energy1);
                break;
        }
        return energy;


    }

    @Nonnull
    @Override
    public EnumActionResult onItemUse(
            @Nonnull final EntityPlayer player,
            @Nonnull final World world,
            @Nonnull final BlockPos pos,
            @Nonnull final EnumHand hand,
            @Nonnull final EnumFacing facing,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        if (this.toolType.contains("pickaxe")) {
            for (int i = 0; i < player.inventory.mainInventory.size(); i++) {
                ItemStack torchStack = player.inventory.mainInventory.get(i);
                if (!torchStack.isEmpty() && torchStack.getUnlocalizedName().toLowerCase().contains("torch")) {
                    Item item = torchStack.getItem();
                    if (item instanceof net.minecraft.item.ItemBlock) {
                        int oldMeta = torchStack.getItemDamage();
                        int oldSize = torchStack.getCount();
                        ItemStack stack = player.getHeldItem(hand).copy();
                        boolean result = torchStack.onItemUse(player, world, pos, hand, facing, hitX,
                                hitY, hitZ
                        ) == EnumActionResult.SUCCESS;
                        if (player.capabilities.isCreativeMode) {
                            torchStack.setItemDamage(oldMeta);
                            torchStack.setCount(oldSize);
                        }
                        if (result) {
                            ForgeEventFactory.onPlayerDestroyItem(player, torchStack, null);
                            torchStack = player.inventory.mainInventory.get(i);
                            player.setHeldItem(hand, stack);
                            torchStack.setCount(torchStack.getCount() - 1);
                            return EnumActionResult.SUCCESS;
                        }
                    }
                }
            }
        }
        return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
    }

}
