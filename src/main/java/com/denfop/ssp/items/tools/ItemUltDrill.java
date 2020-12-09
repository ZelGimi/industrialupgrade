package com.denfop.ssp.items.tools;

import com.denfop.ssp.common.Configs;
import com.denfop.ssp.common.Constants;
import com.google.common.base.CaseFormat;
import ic2.core.IC2;
import ic2.core.init.BlocksItems;
import ic2.core.init.Localization;
import ic2.core.item.tool.HarvestLevel;
import ic2.core.item.tool.ItemDrill;
import ic2.core.ref.ItemName;
import ic2.core.util.StackUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ItemUltDrill extends ItemDrill {
	protected static final Material[] MATERIALS = new Material[]{Material.ROCK, Material.GRASS, Material.GROUND, Material.SAND, Material.CLAY};
	protected static final String NAME = "ItemUltDrill";

	public ItemUltDrill() {
		super(null, Configs.operationEnergyCost, HarvestLevel.Iridium, Configs.maxChargedrill, Configs.transferLimitdrill, Configs.tierdrill, DrillMode.NORMAL.drillSpeed);
		BlocksItems.registerItem((Item) this, new ResourceLocation(Constants.MOD_ID, "ItemUltDrill")).setUnlocalizedName("ItemUltDrill");
	}

	public static Collection<BlockPos> getBrokenBlocks(EntityPlayer player, RayTraceResult ray) {
		return getBrokenBlocks(player, ray.getBlockPos(), ray.sideHit);
	}

	protected static Collection<BlockPos> getBrokenBlocks(EntityPlayer player, BlockPos pos, EnumFacing side) {
		assert side != null;
		int xMove = 2, yMove = xMove, zMove = yMove;
		switch (side.getAxis()) {
			case X:
				xMove = 0;
				break;
			case Y:
				yMove = 0;
				break;
			case Z:
				zMove = 0;
				break;
		}
		World world = player.world;
		Collection<BlockPos> list = new ArrayList<>(25);
		for (int x = pos.getX() - xMove; x <= pos.getX() + xMove; x++) {
			for (int y = pos.getY() - yMove; y <= pos.getY() + yMove; y++) {
				for (int z = pos.getZ() - zMove; z <= pos.getZ() + zMove; z++) {
					BlockPos potential = new BlockPos(x, y, z);
					if (canBlockBeMined(world, potential, player, false))
						list.add(potential);
				}
			}
		}
		return list;
	}

	protected static boolean canBlockBeMined(World world, BlockPos pos, EntityPlayer player, boolean skipEffectivity) {
		IBlockState state = world.getBlockState(pos);
		return (state.getBlock().canHarvestBlock(world, pos, player) && (skipEffectivity || isEffective(state.getMaterial())) && state.getPlayerRelativeBlockHardness(player, world, pos) != 0.0F);
	}

	protected static boolean isEffective(Material material) {
		for (Material option : MATERIALS) {
			if (material == option)
				return true;
		}
		return false;
	}

	public static Collection<BlockPos> getBrokenBlocks1(EntityPlayer player, RayTraceResult ray) {
		return getBrokenBlocks1(player, ray.getBlockPos(), ray.sideHit);
	}

	protected static Collection<BlockPos> getBrokenBlocks1(EntityPlayer player, BlockPos pos, EnumFacing side) {
		assert side != null;
		int xMove = 1, yMove = xMove, zMove = yMove;
		switch (side.getAxis()) {
			case X:
				xMove = 0;
				break;
			case Y:
				yMove = 0;
				break;
			case Z:
				zMove = 0;
				break;
		}
		World world = player.world;
		Collection<BlockPos> list = new ArrayList<>(9);
		for (int x = pos.getX() - xMove; x <= pos.getX() + xMove; x++) {
			for (int y = pos.getY() - yMove; y <= pos.getY() + yMove; y++) {
				for (int z = pos.getZ() - zMove; z <= pos.getZ() + zMove; z++) {
					BlockPos potential = new BlockPos(x, y, z);
					if (canBlockBeMined(world, potential, player, false))
						list.add(potential);
				}
			}
		}
		return list;
	}

	public String getUnlocalizedName() {
		return "super_solar_panels." + super.getUnlocalizedName().substring(4);
	}

	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.UNCOMMON;
	}

	@SideOnly(Side.CLIENT)
	public void registerModels(ItemName name) {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("super_solar_panels:" + CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, "ItemUltDrill"), null));
	}

	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		if (IC2.keyboard.isModeSwitchKeyDown(player)) {
			ItemStack stack = StackUtil.get(player, hand);
			if (!world.isRemote) {
				DrillMode mode = readNextDrillMode(stack);
				saveDrillMode(stack, mode);
				IC2.platform.messagePlayer(player, "super_solar_panels.ItemUltDrill.mode", mode.colour, new Object[]{mode.translationName});
				this.efficiency = mode.drillSpeed;
				this.operationEnergyCost = mode.energyCost;
			}
			return new ActionResult<>(EnumActionResult.SUCCESS, stack);
		}
		return super.onItemRightClick(world, player, hand);
	}

	public static DrillMode readNextDrillMode(ItemStack stack) {
		return DrillMode.getFromID(StackUtil.getOrCreateNbtData(stack).getInteger("toolMode") + 1);
	}

	public static void saveDrillMode(ItemStack stack, DrillMode mode) {
		StackUtil.getOrCreateNbtData(stack).setInteger("toolMode", mode.ordinal());
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(@Nonnull ItemStack stack, World world, List<String> tooltip, @Nonnull ITooltipFlag flag) {
		tooltip.add(TextFormatting.GOLD + Localization.translate("super_solar_panels.ItemUltDrill.mode", new Object[]{TextFormatting.WHITE + Localization.translate((readDrillMode(stack)).translationName)}));
	}

	public static DrillMode readDrillMode(ItemStack stack) {
		return DrillMode.getFromID(StackUtil.getOrCreateNbtData(stack).getInteger("toolMode"));
	}

	public boolean onBlockStartBreak(@Nonnull ItemStack stack, @Nonnull BlockPos pos, @Nonnull EntityPlayer player) {
		World world;
		if (readDrillMode(stack) == DrillMode.BIG_HOLES && !(world = player.world).isRemote) {
			Collection<BlockPos> blocks = getBrokenBlocks(player, rayTrace(world, player, true));
			if (!blocks.contains(pos) && canBlockBeMined(world, pos, player, true))
				blocks.add(pos);
			boolean powerRanOut = false;
			for (BlockPos blockPos : blocks) {

				if (!world.isBlockLoaded(blockPos))
					continue;
				IBlockState state = world.getBlockState(blockPos);
				Block block = state.getBlock();
				if (!block.isAir(state, world, blockPos)) {
					int experience;
					if (player instanceof EntityPlayerMP) {
						experience = ForgeHooks.onBlockBreakEvent(world, ((EntityPlayerMP) player).interactionManager.getGameType(), (EntityPlayerMP) player, blockPos);
						if (experience < 0)
							return false;
					} else {
						experience = 0;
					}
					block.onBlockHarvested(world, blockPos, state, player);
					if (player.isCreative()) {
						if (block.removedByPlayer(state, world, blockPos, player, false))
							block.onBlockDestroyedByPlayer(world, blockPos, state);
					} else {
						if (block.removedByPlayer(state, world, blockPos, player, true)) {
							block.onBlockDestroyedByPlayer(world, blockPos, state);
							block.harvestBlock(world, player, blockPos, state, world.getTileEntity(blockPos), stack);
							if (experience > 0)
								block.dropXpOnBlockBreak(world, blockPos, experience);
						}
						stack.onBlockDestroyed(world, state, blockPos, player);
					}
					world.playEvent(2001, blockPos, Block.getStateId(state));
					((EntityPlayerMP) player).connection.sendPacket(new SPacketBlockChange(world, blockPos));
				}
			}
			if (powerRanOut)
				IC2.platform.messagePlayer(player, "super_solar_panels.advancedDrill.ranOut");
			return true;
		}
		if (readDrillMode(stack) == DrillMode.BIG_HOLES1 && !(world = player.world).isRemote) {
			Collection<BlockPos> blocks = getBrokenBlocks1(player, rayTrace(world, player, true));
			if (!blocks.contains(pos) && canBlockBeMined(world, pos, player, true))
				blocks.add(pos);
			boolean powerRanOut = false;
			for (BlockPos blockPos : blocks) {

				if (!world.isBlockLoaded(blockPos))
					continue;
				IBlockState state = world.getBlockState(blockPos);
				Block block = state.getBlock();
				if (!block.isAir(state, world, blockPos)) {
					int experience;
					if (player instanceof EntityPlayerMP) {
						experience = ForgeHooks.onBlockBreakEvent(world, ((EntityPlayerMP) player).interactionManager.getGameType(), (EntityPlayerMP) player, blockPos);
						if (experience < 0)
							return false;
					} else {
						experience = 0;
					}
					block.onBlockHarvested(world, blockPos, state, player);
					if (player.isCreative()) {
						if (block.removedByPlayer(state, world, blockPos, player, false))
							block.onBlockDestroyedByPlayer(world, blockPos, state);
					} else {
						if (block.removedByPlayer(state, world, blockPos, player, true)) {
							block.onBlockDestroyedByPlayer(world, blockPos, state);
							block.harvestBlock(world, player, blockPos, state, world.getTileEntity(blockPos), stack);
							if (experience > 0)
								block.dropXpOnBlockBreak(world, blockPos, experience);
						}
						stack.onBlockDestroyed(world, state, blockPos, player);
					}
					world.playEvent(2001, blockPos, Block.getStateId(state));
					((EntityPlayerMP) player).connection.sendPacket(new SPacketBlockChange(world, blockPos));
				}
			}
			if (powerRanOut)
				IC2.platform.messagePlayer(player, "super_solar_panels.ItemUltDrill.ranOut");
			return true;
		}
		return super.onBlockStartBreak(stack, pos, player);
	}

	public enum DrillMode {
		NORMAL(TextFormatting.DARK_GREEN, Configs.drillSpeed, Configs.energyCost),
		BIG_HOLES(TextFormatting.AQUA, Configs.drillSpeed1, Configs.energyCost1),
		BIG_HOLES1(TextFormatting.LIGHT_PURPLE, Configs.drillSpeed2, Configs.energyCost2);

		private static final DrillMode[] VALUES = values();

		public final float drillSpeed;

		public final double energyCost;

		public final TextFormatting colour;

		public final String translationName;

		DrillMode(TextFormatting colour, float speed, double energyCost) {
			this.translationName = "super_solar_panels.ItemUltDrill." + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name());
			this.energyCost = energyCost;
			this.drillSpeed = speed;
			this.colour = colour;
		}

		public static DrillMode getFromID(int ID) {
			return VALUES[ID % VALUES.length];
		}

	}
}
