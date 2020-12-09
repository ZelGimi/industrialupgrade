package com.denfop.ssp.integration.botania;

import ic2.core.block.ITeBlock;
import ic2.core.block.TileEntityBlock;
import ic2.core.ref.TeBlock;
import ic2.core.util.Util;
import net.minecraft.item.EnumRarity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import java.util.Set;


public enum BotaniaMain implements ITeBlock
//implements ITeBlock
{

	manasteel_solar_panel(),
	elementium_solar_panel(TileEntityElementiumSolar.class, 72, EnumRarity.RARE),
	terrasteel_solar_panel(TileEntityTerrasteelSolar.class, 73, EnumRarity.EPIC);
	public static final ResourceLocation IDENTITY1;
	private static final BotaniaMain[] VALUES1;

	static {
		VALUES1 = values();
		IDENTITY1 = new ResourceLocation("super_solar_panels", "machines1");
	}

	private final Class<? extends TileEntityBlock> teClass1;
	private final int itemMeta;
	private final EnumRarity rarity;
	private TileEntityBlock dummyTe1;


	BotaniaMain() {
		this(TileEntityManasteelSolar.class, 71, EnumRarity.UNCOMMON);

	}

	BotaniaMain(final Class<? extends TileEntityBlock> teClass, final int itemMeta, final EnumRarity rarity) {
		this.teClass1 = teClass;
		this.itemMeta = itemMeta;
		this.rarity = rarity;
		GameRegistry.registerTileEntity(teClass, "super_solar_panels:" + this.getName());


	}

	@Override
	public String getName() {
		return this.name();
	}

	@Override
	public int getId() {
		return this.itemMeta;
	}

	public static void buildDummies() {
		final ModContainer mc = Loader.instance().activeModContainer();
		if (mc == null || !"super_solar_panels".equals(mc.getModId())) {
			throw new IllegalAccessError("Don't mess with this please.");
		}
		for (final BotaniaMain block : BotaniaMain.VALUES1) {
			if (block.teClass1 != null) {
				try {
					block.dummyTe1 = block.teClass1.newInstance();
				} catch (Exception e) {
					if (Util.inDev()) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	@Nonnull
	public ResourceLocation getIdentifier() {
		return BotaniaMain.IDENTITY1;
	}

	@Override
	public boolean hasItem() {
		return true;
	}

	@Override
	public Class<? extends TileEntityBlock> getTeClass() {
		return this.teClass1;
	}

	@Override
	public boolean hasActive() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@Nonnull
	public Set<EnumFacing> getSupportedFacings() {
		return Util.horizontalFacings;
	}

	@Override
	public float getHardness() {
		return 3.0f;
	}

	@Override
	public float getExplosionResistance() {
		return 0.0f;
	}

	@Override
	@Nonnull
	public TeBlock.HarvestTool getHarvestTool() {
		return TeBlock.HarvestTool.Pickaxe;
	}

	@Override
	@Nonnull
	public TeBlock.DefaultDrop getDefaultDrop() {
		return TeBlock.DefaultDrop.Self;
	}

	@Override
	@Nonnull
	public EnumRarity getRarity() {
		return this.rarity;
	}

	@Override
	public boolean allowWrenchRotating() {
		return false;
	}

	@Override
	public TileEntityBlock getDummyTe() {
		return this.dummyTe1;
	}
}

