package com.denfop.items.upgradekit;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.IModelRegister;
import com.denfop.blocks.ISubEnum;
import com.denfop.items.resource.ItemSubTypes;
import com.denfop.register.Register;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Locale;

public class ItemUpgradeMachinesKit extends ItemSubTypes<ItemUpgradeMachinesKit.Types> implements IModelRegister {

    protected static final String NAME = "upgradekitmachine";
    public static int tick = 0;
    public static int[] inform = new int[4];

    public ItemUpgradeMachinesKit() {
        super(Types.class);
        this.setCreativeTab(IUCore.UpgradeTab);
        Register.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
    }


    @Override
    public void addInformation(
            @Nonnull final ItemStack p_77624_1_,
            @Nullable final World p_77624_2_,
            final List<String> p_77624_3_,
            @Nonnull final ITooltipFlag p_77624_4_
    ) {
        p_77624_3_.add(Localization.translate("waring_kit"));
        p_77624_3_.add(Localization.translate("using_kit"));
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            final List<ItemStack> list = IUItem.map_upgrades.get(p_77624_1_.getItemDamage());
            p_77624_3_.add(Localization.translate(list
                    .get(inform[p_77624_1_.getItemDamage()] % list.size())
                    .getUnlocalizedName()));
        } else {
            for (ItemStack name : IUItem.map_upgrades.get(p_77624_1_.getItemDamage())) {
                p_77624_3_.add(Localization.translate(name
                        .getUnlocalizedName()));
            }
        }
        super.addInformation(p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_);

    }

    @Nonnull
    public EnumActionResult onItemUseFirst(
            @Nonnull EntityPlayer player,
            @Nonnull World world,
            @Nonnull BlockPos pos,
            @Nonnull EnumFacing side,
            float hitX,
            float hitY,
            float hitZ,
            @Nonnull EnumHand hand
    ) {
        if (!IUCore.proxy.isSimulating()) {
            return EnumActionResult.PASS;
        } else {
            final boolean hooks = ForgeHooks.onRightClickBlock(player, hand, pos, side, new Vec3d(hitX, hitY, hitZ)).isCanceled();
            if (hooks) {
                return EnumActionResult.PASS;
            }
        }
        return EnumActionResult.PASS;
    }


    public String getUnlocalizedName() {
        return "iu." + super.getUnlocalizedName().substring(3);
    }

    @SideOnly(Side.CLIENT)
    public void registerModel(Item item, int meta, String extraName) {
        ModelLoader.setCustomModelResourceLocation(
                this,
                meta,
                new ModelResourceLocation(Constants.MOD_ID + ":" + NAME + "/" + Types.getFromID(meta).getName(), null)
        );
    }

    public enum Types implements ISubEnum {
        upgradepanelkitmachine(0),
        upgradepanelkitmachine1(1),
        upgradepanelkitmachine2(2),
        upgradepanelkitmachine3(3),
        ;

        private final String name;
        private final int ID;

        Types(final int ID) {
            this.name = this.name().toLowerCase(Locale.US);
            this.ID = ID;
        }

        public static Types getFromID(final int ID) {
            return values()[ID % values().length];
        }

        public String getName() {
            return this.name;
        }

        public int getId() {
            return this.ID;
        }
    }


}
