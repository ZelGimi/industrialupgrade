package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.api.vein.common.Type;
import com.denfop.api.widget.*;
import com.denfop.containermenu.ContainerMenuOilPump;
import com.denfop.utils.Localization;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenOilPump<T extends ContainerMenuOilPump> extends ScreenMain<ContainerMenuOilPump> {

    public final ContainerMenuOilPump container;


    public ScreenOilPump(ContainerMenuOilPump container1) {
        super(container1);
        this.container = container1;
        addWidget(TankWidget.createNormal(this, 96, 22, container.base.fluidTank));
        this.componentList.add(new ScreenWidget(this, 117, 41, EnumTypeComponent.FLUID_PART,
                new WidgetDefault<>(new EmptyWidget())
        ));
        this.componentList.add(new ScreenWidget(this, 43, 39, EnumTypeComponent.OIL,
                new WidgetDefault(new EmptyWidget()) {
                    @Override
                    public String getText(final ScreenWidget screenWidget) {
                        if (container.base.find && container.base.count > 0 && container.base.maxcount > 0 && container.base.type == Type.OIL.ordinal()) {
                            int col = container.base.vein.getCol();
                            int colmax = container.base.vein.getMaxCol();
                            boolean isOil = container.base.vein.getType() == Type.OIL;
                            int variety = container.base.vein.getMeta() / 3;
                            int type = container.base.vein.getMeta() % 3;
                            String varietyString = variety == 0 ? "iu.sweet_oil" : "iu.sour_oil";
                            String typeString = type == 0 ? "iu.light_oil" : type == 1 ? "iu.medium_oil" : "iu.heavy_oil";
                            String name_vein = Localization.translate(varietyString) + " " + Localization.translate(
                                    typeString) + " " + Localization.translate(new ItemStack(IUItem.oilblock.getItem()).getDescriptionId());
                            return
                                    name_vein + " " + col + (isOil ? "mB" : "") + "/" + colmax + (
                                            isOil
                                                    ?
                                                    "mB"
                                                    : "");

                        } else {
                            return Localization.translate("iu.notfindoil");

                        }
                    }
                }
        ));
    }


    public ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
