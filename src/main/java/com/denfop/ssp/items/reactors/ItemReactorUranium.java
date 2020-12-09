package com.denfop.ssp.items.reactors;

import com.denfop.ssp.items.SSPItems;
import com.denfop.ssp.items.resource.CraftingThings;
import com.google.common.base.CaseFormat;
import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorComponent;
import ic2.core.IC2Potion;
import ic2.core.init.BlocksItems;
import ic2.core.item.armor.ItemArmorHazmat;
import ic2.core.item.reactor.AbstractDamageableReactorComponent;
import ic2.core.ref.ItemName;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Queue;

public class ItemReactorUranium extends AbstractDamageableReactorComponent {
	public final int numberOfCells;
	protected final String name;

	public ItemReactorUranium(String name, int cells) {
		this(name, cells, 20000);

	}

	protected ItemReactorUranium(String name, int cells, int duration) {
		super(null, duration);
		setMaxStackSize(64);
		this.numberOfCells = cells;
		BlocksItems.registerItem(this, new ResourceLocation("super_solar_panels", this.name = name)).setUnlocalizedName(name);
	}

	@SideOnly(Side.CLIENT)
	public void registerModels(ItemName name) {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("super_solar_panels:" + CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, this.name), null));
	}

	public String getUnlocalizedName() {
		return "super_solar_panels." + super.getUnlocalizedName().substring(4);
	}

	public String setUnlocalizedName() {
		return "super_solar_panels." + super.getUnlocalizedName().substring(4);
	}

	public void processChamber(ItemStack stack, IReactor reactor, int x, int y, boolean heatRun) {
		if (!reactor.produceEnergy())
			return;
		int basePulses = 1 + this.numberOfCells / 2;
		for (int iteration = 0; iteration < this.numberOfCells; iteration++) {
			int pulses = basePulses;
			if (!heatRun) {
				for (int i = 0; i < pulses; i++)
					acceptUraniumPulse(stack, reactor, stack, x, y, x, y, heatRun);
				pulses += checkPulseable(reactor, x - 1, y, stack, x, y, heatRun) +
						checkPulseable(reactor, x + 1, y, stack, x, y, heatRun) +
						checkPulseable(reactor, x, y - 1, stack, x, y, heatRun) +
						checkPulseable(reactor, x, y + 1, stack, x, y, heatRun);
			} else {
				pulses += checkPulseable(reactor, x - 1, y, stack, x, y, heatRun) +
						checkPulseable(reactor, x + 1, y, stack, x, y, heatRun) +
						checkPulseable(reactor, x, y - 1, stack, x, y, heatRun) +
						checkPulseable(reactor, x, y + 1, stack, x, y, heatRun);
				int heat = triangularNumber(pulses) * 4;
				heat = getFinalHeat(stack, reactor, x, y, heat);
				Queue<ItemStackCoord> heatAcceptors = new ArrayDeque<>();
				checkHeatAcceptor(reactor, x - 1, y, heatAcceptors);
				checkHeatAcceptor(reactor, x + 1, y, heatAcceptors);
				checkHeatAcceptor(reactor, x, y - 1, heatAcceptors);
				checkHeatAcceptor(reactor, x, y + 1, heatAcceptors);
				while (!heatAcceptors.isEmpty() && heat > 0) {
					int dheat = heat / heatAcceptors.size();
					heat -= dheat;
					ItemStackCoord acceptor = heatAcceptors.remove();
					IReactorComponent acceptorComp = (IReactorComponent) acceptor.stack.getItem();
					dheat = acceptorComp.alterHeat(acceptor.stack, reactor, acceptor.x, acceptor.y, dheat);
					heat += dheat;
				}
				if (heat > 0)
					reactor.addHeat(heat);
			}
		}
		if (!heatRun && getCustomDamage(stack) >= getMaxCustomDamage(stack) - 1) {
			reactor.setItemAt(x, y, getDepletedStack(stack, reactor));
		} else if (!heatRun) {
			applyCustomDamage(stack, 1, null);
		}
	}

	protected static int checkPulseable(IReactor reactor, int x, int y, ItemStack stack, int mex, int mey, boolean heatrun) {
		ItemStack other = reactor.getItemAt(x, y);
		if (other != null && other.getItem() instanceof IReactorComponent && (
				(IReactorComponent) other.getItem()).acceptUraniumPulse(other, reactor, stack, x, y, mex, mey, heatrun))
			return 1;
		return 0;
	}

	protected static int triangularNumber(int x) {
		return (x * x + x) / 2;
	}

	protected int getFinalHeat(ItemStack stack, IReactor reactor, int x, int y, int heat) {
		return heat;
	}

	protected void checkHeatAcceptor(IReactor reactor, int x, int y, Collection<ItemStackCoord> heatAcceptors) {
		ItemStack stack = reactor.getItemAt(x, y);
		if (stack != null && stack.getItem() instanceof IReactorComponent && (
				(IReactorComponent) stack.getItem()).canStoreHeat(stack, reactor, x, y))
			heatAcceptors.add(new ItemStackCoord(stack, x, y));
	}

	protected ItemStack getDepletedStack(ItemStack stack, IReactor reactor) {
		ItemStack ret;
		switch (this.numberOfCells) {
			case 1:
				ret = SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.depleted_proton_fuel_rod);
				return ret.copy();
			case 2:
				ret = SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.depleted_dual_proton_fuel_rod);
				return ret.copy();
			case 4:
				ret = SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.depleted_quad_proton_fuel_rod);
				return ret.copy();
			case 8:
				ret = SSPItems.CRAFTING.getItemStack(CraftingThings.CraftingTypes.depleted_eit_proton_fuel_rod);
				return ret.copy();
		}
		throw new RuntimeException("invalid cell count: " + this.numberOfCells);
	}

	public boolean acceptUraniumPulse(ItemStack stack, IReactor reactor, ItemStack pulsingStack, int youX, int youY, int pulseX, int pulseY, boolean heatrun) {
		if (!heatrun)
			reactor.addOutput(2.0F);
		return true;
	}

	public float influenceExplosion(ItemStack stack, IReactor reactor) {
		return (2 * this.numberOfCells);
	}

	public void onUpdate(ItemStack stack, World world, Entity entity, int slotIndex, boolean isCurrentItem) {
		if (entity instanceof EntityLivingBase) {
			EntityLivingBase entityLiving = (EntityLivingBase) entity;
			if (!ItemArmorHazmat.hasCompleteHazmat(entityLiving))
				IC2Potion.radiation.applyTo(entityLiving, 200, 100);
		}
	}

	public int getMetadata(ItemStack stack) {
		return (getCustomDamage(stack) > 0) ? 1 : 0;
	}

	private static class ItemStackCoord {
		public final ItemStack stack;

		public final int x;

		public final int y;

		public ItemStackCoord(ItemStack stack, int x, int y) {
			this.stack = stack;
			this.x = x;
			this.y = y;
		}
	}
}
