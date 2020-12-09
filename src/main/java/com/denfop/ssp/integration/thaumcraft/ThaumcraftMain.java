package com.denfop.ssp.integration.thaumcraft;

import com.denfop.ssp.SuperSolarPanels;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchCategory;

public class ThaumcraftMain {
	public static final Aspect Sun = new Aspect("Sun", 16758272, new Aspect[]{Aspect.LIGHT, Aspect.AIR}, new ResourceLocation(SuperSolarPanels.MOD_ID, "textures/aspects/sun.png"), 1);
	public static final Aspect Night = new Aspect("Night", 16758273, new Aspect[]{ThaumcraftMain.Sun, Aspect.ENTROPY}, new ResourceLocation(SuperSolarPanels.MOD_ID, "textures/aspects/night.png"), 1);
	public static final Aspect Energy = new Aspect("Energy", 16758274, new Aspect[]{Aspect.ENERGY, Aspect.MECHANISM}, new ResourceLocation(SuperSolarPanels.MOD_ID, "textures/aspects/energy.png"), 1);
	public static ResearchCategory SSPT;

	public static void init() {
		SSPTab();
		Scan();
		Theory();
	}

	private static void SSPTab() {
		SSPT = ResearchCategories.registerCategory("SSPT", "SSPTKEY", (new AspectList()).add(Aspect.ALCHEMY, 10), new ResourceLocation(SuperSolarPanels.MOD_ID, "textures/thaumcraft/ssptab.png"), new ResourceLocation(SuperSolarPanels.MOD_ID, "textures/thaumcraft/ssptabbackground.png"), new ResourceLocation(SuperSolarPanels.MOD_ID, "textures/thaumcraft/ssptabbackground1.png"));

	}

	@SuppressWarnings("EmptyMethod")
	private static void Scan() {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("EmptyMethod")
	private static void Theory() {
		// TODO Auto-generated method stub

	}
}
