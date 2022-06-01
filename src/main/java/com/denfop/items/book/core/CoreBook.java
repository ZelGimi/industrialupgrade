package com.denfop.items.book.core;

import com.denfop.IUItem;
import net.minecraft.item.ItemStack;

public class CoreBook {

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
        new Pages("info", "basic", 1, "description.basic.info", new ItemStack(IUItem.book));
        new AddPages("desinfo", "info", 0, "description.basic.info.desc", "iu.text1");

    }

}
