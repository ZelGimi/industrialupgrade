package com.denfop.items.energy;


import com.denfop.Config;
import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.upgrade.EnumUpgrades;
import com.denfop.api.upgrade.IUpgradeWithBlackList;
import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.api.upgrade.event.EventItemBlackListLoad;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.utils.ExperienceUtils;
import com.denfop.utils.KeyboardClient;
import com.denfop.utils.ModUtils;
import com.denfop.utils.RetraceDiggingUtils;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import ic2.api.info.Info;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.core.IC2;
import ic2.core.init.BlocksItems;
import ic2.core.init.Localization;
import ic2.core.init.MainConfig;
import ic2.core.util.ConfigUtil;
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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.ForgeHooks;
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

public class ItemEnergyPickaxe extends ItemTool implements IElectricItem, IUpgradeWithBlackList, IModelRegister {

    public static final Set<IBlockState> mineableBlocks = Sets.newHashSet(
            Blocks.COBBLESTONE.getDefaultState(),
            Blocks.DOUBLE_STONE_SLAB.getDefaultState(),
            Blocks.STONE_SLAB.getDefaultState(),
            Blocks.STONE.getDefaultState(),
            Blocks.SANDSTONE.getDefaultState(),
            Blocks.MOSSY_COBBLESTONE.getDefaultState(),
            Blocks.IRON_ORE.getDefaultState(),
            Blocks.IRON_BLOCK.getDefaultState(),
            Blocks.COAL_ORE.getDefaultState(),
            Blocks.GOLD_BLOCK.getDefaultState(),
            Blocks.GOLD_ORE.getDefaultState(),
            Blocks.DIAMOND_ORE.getDefaultState(),
            Blocks.DIAMOND_BLOCK.getDefaultState(),
            Blocks.ICE.getDefaultState(),
            Blocks.NETHERRACK.getDefaultState(),
            Blocks.LAPIS_ORE.getDefaultState(),
            Blocks.LAPIS_BLOCK.getDefaultState(),
            Blocks.REDSTONE_ORE.getDefaultState(),
            Blocks.LIT_REDSTONE_ORE.getDefaultState(),
            Blocks.RAIL.getDefaultState(),
            Blocks.DETECTOR_RAIL.getDefaultState(),
            Blocks.GOLDEN_RAIL.getDefaultState(),
            Blocks.ACTIVATOR_RAIL.getDefaultState()
    );
    private static final Set<Material> materials = Sets.newHashSet(Material.IRON, Material.ANVIL,
            Material.ROCK, Material.GRASS, Material.ICE, Material.PACKED_ICE
    );

    private static final Set<String> toolType = ImmutableSet.of("pickaxe");
    public final String name;
    public final int efficienty;
    public final int lucky;
    private final float bigHolePower;
    private final float normalPower;
    private final int maxCharge;
    private final int tier;
    private final int energyPerOperation;
    private final int energyPerbigHolePowerOperation;
    private final int transferLimit;
    private final List<String> blacklist = new ArrayList<>();
    private boolean hasBlackList = false;

    public ItemEnergyPickaxe(
            Item.ToolMaterial toolMaterial, String name, int efficienty, int lucky, int transferlimit,
            int maxCharge, int tier, int normalPower, int bigHolesPower, int energyPerOperation,
            int energyPerbigHolePowerOperation
    ) {
        super(0.0F, 0.0F + toolMaterial.getAttackDamage(), toolMaterial, new HashSet<>());
        setMaxDamage(27);

        setCreativeTab(IUCore.EnergyTab);
        this.efficienty = efficienty;
        this.lucky = lucky;
        this.name = name;
        this.transferLimit = transferlimit;
        this.maxCharge = maxCharge;
        this.tier = tier;
        this.efficiency = this.normalPower = normalPower;
        this.bigHolePower = bigHolesPower;
        this.energyPerOperation = energyPerOperation;
        this.energyPerbigHolePowerOperation = energyPerbigHolePowerOperation;
        this.setUnlocalizedName(name);
        BlocksItems.registerItem((Item) this, IUCore.getIdentifier(name)).setUnlocalizedName(name);
        IUCore.proxy.addIModelRegister(this);
        UpgradeSystem.system.addRecipe(this, EnumUpgrades.INSTRUMENTS.list);
    }

    public static void updateGhostBlocks(EntityPlayer player, World world) {
        if (world.isRemote) {
            return;
        }
        int xPos = (int) player.posX;
        int yPos = (int) player.posY;
        int zPos = (int) player.posZ;
        for (int x = xPos - 6; x < xPos + 6; x++) {
            for (int y = yPos - 6; y < yPos + 6; y++) {
                for (int z = zPos - 6; z < zPos + 6; z++) {
                    ((EntityPlayerMP) player).connection.sendPacket(new SPacketBlockChange(world, new BlockPos(x, y, z)));
                }
            }
        }
    }

    public static int readToolMode(ItemStack itemstack) {
        NBTTagCompound nbt = ModUtils.nbt(itemstack);
        int toolMode = nbt.getInteger("toolMode");

        if (toolMode < 0 || toolMode > 2) {
            toolMode = 0;
        }
        return toolMode;
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation1(String name, String extraName) {
        final String loc = Constants.MOD_ID +
                ':' +
                "energy_tools" + "/" + name + extraName;

        return new ModelResourceLocation(loc, null);
    }

    private int getExpierence(
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

    @Override
    public void onUpdate(@Nonnull ItemStack itemStack, @Nonnull World world, @Nonnull Entity entity, int slot, boolean par5) {
        NBTTagCompound nbt = ModUtils.nbt(itemStack);

        if (!UpgradeSystem.system.hasInMap(itemStack)) {
            nbt.setBoolean("hasID", false);
            MinecraftForge.EVENT_BUS.post(new EventItemBlackListLoad(world, this, itemStack, itemStack.getTagCompound()));
        }
    }

    boolean break_block(
            World world, Block block, RayTraceResult mop, byte mode_item, EntityPlayer player, BlockPos pos,
            ItemStack stack
    ) {
        byte xRange = mode_item;
        byte yRange = mode_item;
        byte zRange = mode_item;
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        switch (mop.sideHit.ordinal()) {
            case 0:
            case 1:
                yRange = 0;
                break;
            case 2:
            case 3:
                zRange = 0;
                break;
            case 4:
            case 5:
                xRange = 0;
                break;
        }

        boolean lowPower = false;
        boolean silktouch = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0;
        int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);

        int Yy;
        Yy = yRange > 0 ? yRange - 1 : 0;
        NBTTagCompound nbt = ModUtils.nbt(stack);
        float energy = energy(stack);
        byte dig_depth = (byte) (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.DIG_DEPTH, stack) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.DIG_DEPTH, stack).number : 0);


        zRange = zRange > 0 ? zRange : (byte) (zRange + dig_depth);
        xRange = xRange > 0 ? xRange : (byte) (xRange + dig_depth);
        yRange = yRange > 0 ? yRange : (byte) (yRange + dig_depth);
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
                            Block localBlock = world.getBlockState(pos_block).getBlock();
                            if (!localBlock.equals(Blocks.AIR) && canHarvestBlock(state, stack)
                                    && state.getBlockHardness(world, pos_block) >= 0.0F
                            ) {
                                if (state.getBlockHardness(world, pos_block) > 0.0F) {
                                    onBlockDestroyed(stack, world, state, pos_block,
                                            player
                                    );
                                }
                                if (!silktouch) {
                                    ExperienceUtils.addPlayerXP(player, getExpierence(state, world, pos_block, fortune, stack
                                            , localBlock));
                                }
                                if (mop.typeOfHit == RayTraceResult.Type.MISS) {
                                    updateGhostBlocks(player, player.getEntityWorld());
                                }

                            } else {
                                if (state.getBlockHardness(world, pos_block) > 0.0F && materials.contains(state.getMaterial())) {
                                    return onBlockDestroyed(stack, world, state, pos_block,
                                            player
                                    );
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
                Block localBlock = world.getBlockState(pos).getBlock();
                IBlockState state = world.getBlockState(pos);
                if (localBlock.equals(Blocks.AIR) && canHarvestBlock(state, stack)
                        && state.getBlockHardness(world, pos) >= 0.0F
                        && (materials.contains(state.getMaterial())
                        || block == Blocks.MONSTER_EGG)) {
                    if (state.getBlockHardness(world, pos) > 0.0F) {
                        onBlockDestroyed(stack, world, state, pos,
                                player
                        );
                    }
                    if (!silktouch) {
                        localBlock.dropXpOnBlockBreak(world, pos,
                                localBlock.getExpDrop(state, world, pos, fortune)
                        );
                    }


                } else {
                    if (state.getBlockHardness(world, pos) > 0.0F) {
                        return onBlockDestroyed(stack, world, state, pos,
                                player
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
                        && (materials.contains(state.getMaterial())
                        || block == Blocks.MONSTER_EGG)) {

                    if (state.getBlockHardness(world, pos) > 0.0F) {
                        onBlockDestroyed(stack, world, state, pos,
                                player
                        );
                    }
                    if (!silktouch) {
                        localBlock.dropXpOnBlockBreak(world, pos,
                                localBlock.getExpDrop(state, world, pos, fortune)
                        );
                    }


                } else {
                    if (state.getBlockHardness(world, pos) > 0.0F) {
                        return onBlockDestroyed(stack, world, state, pos,
                                player
                        );
                    }
                }
            }
        }
        return true;
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

    public double getMaxCharge(ItemStack itemStack) {
        return this.maxCharge;
    }

    public int getTier(ItemStack itemStack) {
        return this.tier;
    }

    public double getTransferLimit(ItemStack itemStack) {
        return this.transferLimit;
    }

    @Nonnull
    public Set<String> getToolClasses(@Nonnull ItemStack stack) {
        return toolType;
    }

    public float getDestroySpeed(@Nonnull ItemStack stack, @Nonnull IBlockState state) {
        int energy = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.ENERGY, stack) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.ENERGY, stack).number : 0;
        int speed = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.EFFICIENCY, stack) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.EFFICIENCY, stack).number : 0;


        return !ElectricItem.manager.canUse(stack, (this.energyPerOperation - (int) (this.energyPerOperation * 0.25 * energy)))
                ? 1.0F
                : (canHarvestBlock(state, stack) ? (this.efficiency + (int) (this.efficiency * 0.2 * speed)) : 1.0F);

    }

    @Override
    public int getHarvestLevel(
            @Nonnull final ItemStack stack,
            final String toolClass,
            @Nullable final EntityPlayer player,
            @Nullable final IBlockState blockState
    ) {
        return (!toolClass.equals("pickaxe")) ? super.getHarvestLevel(stack, toolClass, player, blockState)
                : this.toolMaterial.getHarvestLevel();
    }

    @Override
    public boolean canHarvestBlock(@Nonnull final IBlockState state, @Nonnull final ItemStack stack) {
        return (Items.DIAMOND_PICKAXE.canHarvestBlock(state, stack)
                || Items.DIAMOND_PICKAXE.getDestroySpeed(stack, state) > 1.0F || mineableBlocks.contains(state));

    }

    public boolean onBlockStartBreak(@Nonnull ItemStack stack, @Nonnull BlockPos pos, @Nonnull EntityPlayer player) {
        if (readToolMode(stack) == 0) {
            World world = player.getEntityWorld();
            Block block = world.getBlockState(pos).getBlock();
            if (block == Blocks.AIR) {
                return super.onBlockStartBreak(stack, pos, player);
            }

            RayTraceResult mop = RetraceDiggingUtils.retrace(player);
            byte aoe = (byte) (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.AOE_DIG, stack) ?
                    UpgradeSystem.system.getModules(EnumInfoUpgradeModules.AOE_DIG, stack).number : 0);


            return break_block(world, block, mop, aoe, player, pos, stack);
        }
        if (readToolMode(stack) == 1) {
            World world = player.getEntityWorld();

            IBlockState state = world.getBlockState(pos);
            Block block = state.getBlock();

            if (block.equals(Blocks.AIR)) {
                return super.onBlockStartBreak(stack, pos, player);
            }
            RayTraceResult mop = RetraceDiggingUtils.retrace(player);


            byte aoe = (byte) (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.AOE_DIG, stack) ?
                    UpgradeSystem.system.getModules(EnumInfoUpgradeModules.AOE_DIG, stack).number : 0);
            if (materials.contains(state.getMaterial()) || block == Blocks.MONSTER_EGG) {
                if (player.isSneaking()) {
                    return break_block(world, block, mop, aoe, player, pos, stack);
                }

                return break_block(world, block, mop, (byte) (1 + aoe), player, pos, stack);
            }
        }
        if (readToolMode(stack) == 2) {
            World world = player.getEntityWorld();

            IBlockState state = world.getBlockState(pos);
            Block block = state.getBlock();


            if (block.equals(Blocks.AIR)) {
                return super.onBlockStartBreak(stack, pos, player);
            }
            RayTraceResult mop = RetraceDiggingUtils.retrace(player);
            boolean silktouch = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0;
            int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);


            NBTTagCompound nbt = ModUtils.nbt(stack);
            nbt.setInteger("ore", 1);

            if (!mop.typeOfHit.equals(RayTraceResult.Type.MISS)) {
                ore_break(world, pos, player, silktouch, fortune, false, stack, block);
            }

        }
        return super.onBlockStartBreak(stack, pos, player);
    }

    private void ore_break(
            World world, BlockPos pos, EntityPlayer player, boolean silktouch, int fortune, boolean lowPower,
            ItemStack stack, Block block1
    ) {

        int energy = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.ENERGY, stack) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.ENERGY, stack).number : 0);

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        for (int Xx = x - 1; Xx <= x + 1; Xx++) {
            for (int Yy = y - 1; Yy <= y + 1; Yy++) {
                for (int Zz = z - 1; Zz <= z + 1; Zz++) {
                    NBTTagCompound NBTTagCompound = ModUtils.nbt(stack);
                    int ore = NBTTagCompound.getInteger("ore");
                    if (ore < 16) {
                        if (ElectricItem.manager.canUse(
                                stack,
                                (this.energyPerOperation - this.energyPerOperation * 0.25 * energy)
                        )) {
                            BlockPos pos_block = new BlockPos(Xx, Yy, Zz);
                            Block localBlock = world.getBlockState(pos_block).getBlock();
                            IBlockState state = world.getBlockState(pos_block);
                            if (ModUtils.getore(localBlock, block1)) {


                                if (!player.capabilities.isCreativeMode) {

                                    if (state.getBlockHardness(world, pos_block) > 0.0F) {
                                        onBlockDestroyed(stack, world, state, pos_block,
                                                player
                                        );

                                    }
                                    if (!silktouch) {
                                        localBlock.dropXpOnBlockBreak(world, pos_block,
                                                localBlock.getExpDrop(state, world, pos_block, fortune)
                                        );
                                    }


                                    ore = ore + 1;
                                    NBTTagCompound.setInteger("ore", ore);
                                    ore_break(world, pos_block, player, silktouch, fortune, lowPower, stack, block1);
                                } else {
                                    break;
                                }

                                world.markBlockRangeForRenderUpdate(pos_block, pos_block);

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

    public boolean onBlockDestroyed(
            @Nonnull ItemStack stack,
            @Nonnull World world,
            IBlockState state,
            @Nonnull BlockPos pos,
            @Nonnull EntityLivingBase entity
    ) {

        Block block = state.getBlock();
        if (block.equals(Blocks.AIR)) {
            return false;
        } else {

            if (world.isAirBlock(pos)) {
                return false;
            }
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

                block.onBlockHarvested(world, pos, state, (EntityPlayerMP) entity);

                if (block.removedByPlayer(state, world, pos, (EntityPlayerMP) entity, true)) {
                    block.onBlockDestroyedByPlayer(world, pos, state);
                    block.harvestBlock(world, (EntityPlayerMP) entity, pos, state, null, stack);
                    NBTTagCompound nbt = ModUtils.nbt(stack);

                    List<EntityItem> items = entity.getEntityWorld().getEntitiesWithinAABB(
                            EntityItem.class,
                            new AxisAlignedBB(pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1, pos.getX() + 1,
                                    pos.getY() + 1,
                                    pos.getZ() + 1
                            )
                    );
                    boolean smelter = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.SMELTER, stack);
                    boolean comb = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.COMB_MACERATOR, stack);
                    boolean mac = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.MACERATOR, stack);
                    boolean generator = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.GENERATOR, stack);


                    ((EntityPlayerMP) entity).addExhaustion(-0.025F);
                    if ((ModUtils.getore(block, block.getMetaFromState(state)) && check_list(block, block.getMetaFromState(state)
                            , stack)) || (!Config.blacklist) || !nbt.getBoolean("black")) {
                        for (EntityItem item : items) {
                            if (!entity.getEntityWorld().isRemote) {
                                ItemStack stack1 = item.getItem();

                                if (comb) {
                                    final BaseMachineRecipe rec = Recipes.recipes.getRecipeOutputFromInstruments(
                                            "comb_macerator",
                                            false,
                                            stack1
                                    );
                                    if (rec != null) {
                                        stack1 = rec.output.items.get(0);
                                        stack1.setCount(3);
                                    }
                                } else if (mac) {
                                    BaseMachineRecipe rec = Recipes.recipes.getRecipeOutputFromInstruments(
                                            "macerator",
                                            false,
                                            stack1
                                    );
                                    if (rec != null) {
                                        stack1 = rec.output.items.get(0);
                                    }
                                }
                                ItemStack smelt = new ItemStack(Items.AIR);
                                if (smelter) {
                                    smelt = FurnaceRecipes.instance().getSmeltingResult(stack1);
                                    if (!smelt.isEmpty()) {
                                        smelt.setCount(stack1.getCount());
                                    }
                                }
                                if (generator) {
                                    final boolean rec = Info.itemInfo.getFuelValue(stack1, false) > 0;
                                    if (rec) {
                                        int amount = stack1.getCount();
                                        int value = Info.itemInfo.getFuelValue(stack1, false) / 4;
                                        amount *= value;
                                        amount *= Math.round(10.0F * ConfigUtil.getFloat(
                                                MainConfig.get(),
                                                "balance/energy/generator/generator"
                                        ));
                                        double sentPacket = ElectricItem.manager.charge(
                                                stack,
                                                amount,
                                                2147483647,
                                                true,
                                                false
                                        );
                                        amount -= sentPacket;
                                        amount /= (value * Math.round(10.0F * ConfigUtil.getFloat(MainConfig.get(), "balance" +
                                                "/energy/generator/generator")));
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
                    } else {
                        if (nbt.getBoolean("black")) {
                            for (EntityItem item : items) {
                                if (!entity.getEntityWorld().isRemote) {

                                    item.setDead();

                                }
                            }
                        }
                    }
                }
                int random = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.RANDOM, stack) ?
                        UpgradeSystem.system.getModules(EnumInfoUpgradeModules.RANDOM, stack).number : 0;
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
                float energy = energy(stack);
                if (energy != 0.0F && state.getBlockHardness(world, pos) != 0.0F) {
                    ElectricItem.manager.use(stack, energy, null);
                }
            }

            return true;
        }
    }

    public boolean check_list(Block block, int metaFromState, ItemStack stack) {

        if (!UpgradeSystem.system.hasBlackList(stack)) {
            return true;
        }

        ItemStack stack1 = new ItemStack(block, 1, metaFromState);
        if (stack1.isEmpty()) {
            return true;
        }
        if (OreDictionary.getOreIDs(stack1).length < 1) {
            return true;
        }

        String name = OreDictionary.getOreName(OreDictionary.getOreIDs(stack1)[0]);


        return !UpgradeSystem.system.getBlackList(stack).contains(name);
    }

    public float energy(ItemStack stack) {
        int energy1 = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.ENERGY, stack) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.ENERGY, stack).number : 0;

        int toolMode = readToolMode(stack);
        float energy;
        switch (toolMode) {
            case 1:
            case 2:
                energy = (float) (this.energyPerbigHolePowerOperation - this.energyPerbigHolePowerOperation * 0.25 * energy1);
                break;

            default:
                energy = (float) (this.energyPerOperation - this.energyPerOperation * 0.25 * energy1);
                break;
        }
        return energy;


    }

    public void saveToolMode(ItemStack itemstack, int toolMode) {
        NBTTagCompound nbt = ModUtils.nbt(itemstack);
        nbt.setInteger("toolMode", toolMode);
        itemstack.setTagCompound(nbt);
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUse(
            final EntityPlayer player,
            @Nonnull final World world,
            @Nonnull final BlockPos pos,
            @Nonnull final EnumHand hand,
            @Nonnull final EnumFacing facing,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        for (int i = 0; i < player.inventory.mainInventory.size(); i++) {
            ItemStack torchStack = player.inventory.mainInventory.get(i);
            if (!torchStack.isEmpty() && torchStack.getUnlocalizedName().toLowerCase().contains("torch")) {
                Item item = torchStack.getItem();
                if (item instanceof net.minecraft.item.ItemBlock) {
                    int oldMeta = torchStack.getItemDamage();
                    int oldSize = torchStack.stackSize;
                    ItemStack stack = player.getHeldItem(hand).copy();
                    boolean result = torchStack.onItemUse(player, world, pos, hand, facing, hitX,
                            hitY, hitZ
                    ) == EnumActionResult.SUCCESS;
                    if (player.capabilities.isCreativeMode) {
                        torchStack.setItemDamage(oldMeta);
                        torchStack.stackSize = oldSize;
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
        return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(
            @Nonnull final World worldIn, final EntityPlayer player,
            @Nonnull final EnumHand hand
    ) {

        ItemStack itemStack = player.getHeldItem(hand);
        if (IUCore.keyboard.isSaveModeKeyDown(player)) {
            NBTTagCompound nbt = ModUtils.nbt(itemStack);
            boolean save = !nbt.getBoolean("save");
            if (IC2.platform.isSimulating()) {
                IC2.platform.messagePlayer(
                        player,
                        TextFormatting.GREEN + Localization.translate("message.savemode") +
                                (save ? Localization.translate("message.allow") : Localization.translate("message.disallow"))
                );
            }
            nbt.setBoolean("save", save);
        }
        if (IUCore.keyboard.isBlackListModeKeyDown(player)) {
            NBTTagCompound nbt = ModUtils.nbt(itemStack);
            boolean black = !nbt.getBoolean("black");
            if (IC2.platform.isSimulating()) {
                IC2.platform.messagePlayer(
                        player,
                        TextFormatting.GREEN + Localization.translate("message.blacklist") +
                                (black ? Localization.translate("message.allow") : Localization.translate("message.disallow"))
                );
            }
            nbt.setBoolean("black", black);
        }
        if (IUCore.keyboard.isChangeKeyDown(player)) {
            int toolMode = readToolMode(itemStack) + 1;
            if (!IC2.platform.isRendering()) {
                IUCore.audioManager.playOnce(
                        player,
                        com.denfop.audio.PositionSpec.Hand,
                        "Tools/toolChange.ogg",
                        true,
                        IC2.audioManager.getDefaultVolume()
                );
            }
            if (toolMode > 2) {
                toolMode = 0;
            }
            saveToolMode(itemStack, toolMode);
            switch (toolMode) {
                case 0:
                    if (IC2.platform.isSimulating()) {
                        IC2.platform.messagePlayer(
                                player,
                                TextFormatting.GREEN + Localization.translate("message.text.mode") + ": "
                                        + Localization.translate("message.ultDDrill.mode.normal")
                        );
                    }
                    this.efficiency = this.normalPower;


                    break;

                case 1:


                    if (IC2.platform.isSimulating()) {
                        IC2.platform.messagePlayer(
                                player,
                                TextFormatting.DARK_PURPLE + Localization.translate("message.text.mode") + ": "
                                        + Localization.translate("message.ultDDrill.mode.bigHoles")
                        );
                    }
                    this.efficiency = this.bigHolePower;
                    break;
                case 2:

                    if (IC2.platform.isSimulating()) {
                        IC2.platform.messagePlayer(
                                player,
                                TextFormatting.GOLD + Localization.translate("message.text.mode") + ": "
                                        + Localization.translate("message.ultDDrill.mode.pickaxe")
                        );
                    }
                    this.efficiency = this.normalPower;
                    break;
            }
            return ActionResult.newResult(EnumActionResult.SUCCESS, itemStack);
        }
        return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand));

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
        switch (toolMode) {
            case 0:
                par3List.add(TextFormatting.GOLD + Localization.translate("message.text.mode") + ": "
                        + TextFormatting.WHITE + Localization.translate("message.ultDDrill.mode.normal"));
                par3List.add(Localization.translate("message.description.normal"));
                break;
            case 1:
                par3List.add(TextFormatting.GOLD + Localization.translate("message.text.mode") + ": "
                        + TextFormatting.WHITE + Localization.translate("message.ultDDrill.mode.bigHoles"));
                par3List.add(Localization.translate("message.description.bigHoles"));
                break;
            case 2:

                par3List.add(TextFormatting.GOLD + Localization.translate("message.text.mode") + ": "
                        + TextFormatting.WHITE + Localization.translate("message.ultDDrill.mode.pickaxe"));
                par3List.add(Localization.translate("message.description.pickaxe"));
                break;

        }
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            par3List.add(Localization.translate("press.lshift"));
        }


        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            par3List.add(Localization.translate("iu.changemode_key") + Keyboard.getKeyName(KeyboardClient.changemode.getKeyCode()) + Localization.translate(
                    "iu.changemode_rcm"));
            par3List.add(Localization.translate("iu.blacklist_key") + Keyboard.getKeyName(KeyboardClient.blackmode.getKeyCode()) + Localization.translate(
                    "iu.changemode_rcm"));

        }

        super.addInformation(par1ItemStack, worldIn, par3List, flagIn);
    }

    @Override
    public void getSubItems(@Nonnull final CreativeTabs subs, @Nonnull final NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(subs)) {
            ItemStack stack = new ItemStack(this, 1);

            NBTTagCompound nbt = ModUtils.nbt(stack);
            ElectricItem.manager.charge(stack, 2.147483647E9D, 2147483647, true, false);
            nbt.setInteger("ID_Item", Integer.MAX_VALUE);
            items.add(stack);
            ItemStack itemstack = new ItemStack(this, 1, 27);
            nbt = ModUtils.nbt(itemstack);
            nbt.setInteger("ID_Item", Integer.MAX_VALUE);
            items.add(itemstack);
        }
    }

    @Override
    public void registerModels() {
        registerModels(this.name);
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(final String name) {
        ModelLoader.setCustomMeshDefinition(this, stack -> {
            final NBTTagCompound nbt = ModUtils.nbt(stack);

            return getModelLocation1(name, nbt.getString("mode"));
        });
        String[] mode = {"", "Demon", "Dark", "Cold", "Ender"};
        for (final String s : mode) {
            ModelBakery.registerItemVariants(this, getModelLocation1(name, s));
        }

    }


    @Override
    public void setUpdate(final boolean update) {
    }


    @Override
    public List<String> getBlackList() {
        return this.blacklist;
    }

    @Override
    public void setBlackList(final boolean set) {
        this.hasBlackList = set;
    }

    @Override
    public boolean haveBlackList() {
        return this.hasBlackList;
    }

}
