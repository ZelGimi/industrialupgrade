package com.denfop.ssp.common;

import com.denfop.ssp.SuperSolarPanels;
import ic2.core.gui.dynamic.GuiParser;
import net.minecraft.util.ResourceLocation;

public class Utils {
	public static GuiParser.GuiNode parse(String teBlock) {
		ResourceLocation loc = SuperSolarPanels.getIdentifier("guidef/" + teBlock + ".xml");
		try {
			return GuiParser.parse(loc, SuperSolarPanels.class);
		} catch (Exception var3) {
			throw new RuntimeException(var3);
		}
	}
}
