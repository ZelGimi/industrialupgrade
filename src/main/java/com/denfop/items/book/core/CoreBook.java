package com.denfop.items.book.core;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.gui.DefaultRecipeElement;
import com.denfop.api.gui.GuiElementFluidToFluids;
import com.denfop.api.gui.GuiElementItemFluid;
import com.denfop.api.gui.GuiElementMultiBlock;
import com.denfop.api.gui.GuiFluidRecipe;
import com.denfop.api.gui.TextBook;
import com.denfop.api.recipe.generators.TypeGenerator;
import com.denfop.blocks.FluidName;
import com.denfop.register.InitMultiBlockSystem;
import com.denfop.utils.ModUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CoreBook {

    @SideOnly(Side.CLIENT)
    public static void init() {
    /*    new MainPage("basic", 1, "description.basic", new ItemStack(IUItem.blockpanel));
        new MainPage("basic1", 2, "description.basic1", new ItemStack(IUItem.basemachine2, 1, 4));
        new MainPage("basic2", 3, "description.basic2", IUItem.reactorDepletedeinsteiniumQuad);
        new MainPage("basic3", 4, "description.basic3", new ItemStack(IUItem.ore, 1, 2));
        new MainPage("basic4", 5, "description.basic4", new ItemStack(IUItem.spectralpickaxe, 1, 27));
        new MainPage("basic5", 6, "description.basic5", new ItemStack(IUItem.machines_base, 1, 5));
        new MainPage("basic6", 7, "description.basic6", new ItemStack(IUItem.electricblock, 1, 8));
        new MainPage("basic7", 8, "description.basic7", new ItemStack(IUItem.oilgetter, 1));
        new MainPage("basic8", 9, "description.basic8", new ItemStack(IUItem.basemodules));
        new MainPage("basic9", 10, "description.basic9", new ItemStack(IUItem.sunnarium, 1, 3));
        new MainPage("basic10", 11, "description.basic10", new ItemStack(IUItem.alloysingot, 1, 7));
        new MainPage("basic11", 12, "description.basic11", new ItemStack(IUItem.solidmatter, 1, 5));
        for (int i = 0; i < 14; i++) {
            new Pages("info" + i, "basic", i + 1, "description.basic.info" + i, new ItemStack(IUItem.blockpanel, 1, i));
        }
        for (int i = 0; i < 2; i++) {
            new AddPages("desinfo" + i, "info0", i, "description.basic.info.desc" + i, "iu.text1");
        }
        new AddPages("desinfo" + 2, "info0", 2, "description.basic.info.desc" + 2, "sss", new ResourceLocation(
                Constants.TEXTURES,
                "textures/gui/book.png"
        ), 0, 0, 60, 32, "dfdfdfdf", "", true, "fdfd");
*/
        new MainPage("basic", 1, "description.basic", new ItemStack(IUItem.book));
        final Pages page = new Pages("info", "basic", 1, "description.basic.info", new ItemStack(IUItem.book));
        page.addPages(TextBook.create(null, 12, 26, Localization.translate("iu.text1"), ModUtils.convertRGBcolorToInt(
                10,
                158,
                27
        ), false));
        page.addPages(GuiFluidRecipe.createFluidSlot(null, 12, 26, TypeGenerator.GAS), false);
        page.addPages(GuiFluidRecipe.createFluidSlot(null, 12, 26, TypeGenerator.MATTER), true);
        page.addPages(
                new DefaultRecipeElement(null, 12, 26, () -> new ItemStack(IUItem.radiationresources, 1, 4), "enrichment"),
                true
        );
        page.addPages(
                new DefaultRecipeElement(null, 12, 26, () -> new ItemStack(IUItem.alloysingot, 1, 0), "advalloysmelter"),
                true
        );
        page.addPages(new DefaultRecipeElement(null, 12, 26, () -> new ItemStack(IUItem.sunnarium, 1, 3), "sunnurium"), true);
        page.addPages(new DefaultRecipeElement(null, 12, 26, () -> new ItemStack(IUItem.heavyore), "handlerho", true), true);
        page.addPages(GuiElementFluidToFluids.GuiElementFluidToFluids(
                null,
                12,
                26,
                GuiElementFluidToFluids.TypeFluids.OIL_REFINERY
        ), true);
        page.addPages(GuiElementItemFluid.GuiElementItemFluid(
                null,
                12,
                26,
                "cannerenrich",
                null,
                FluidName.fluidcoolant.getInstance()
        ), true);


        page.addPages(new GuiElementMultiBlock(null, 12, 26, InitMultiBlockSystem.blastFurnaceMultiBlock), false);

        new Pages("info_ore", "basic", 2, "description.info_ore.info", new ItemStack(IUItem.ore));
        new Pages("info_mineral_ore", "basic", 3, "description.info_mineral_ore.info", new ItemStack(IUItem.heavyore));

    }

}
