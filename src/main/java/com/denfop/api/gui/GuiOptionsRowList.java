package com.denfop.api.gui;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiOptionSlider;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.GameSettings.Options;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiOptionsRowList extends GuiListExtended {

    private final List<Row> options = Lists.newArrayList();

    public GuiOptionsRowList(
            Minecraft p_i45015_1_,
            int p_i45015_2_,
            int p_i45015_3_,
            int p_i45015_4_,
            int p_i45015_5_,
            int p_i45015_6_,
            GameSettings.Options... p_i45015_7_
    ) {
        super(p_i45015_1_, p_i45015_2_, p_i45015_3_, p_i45015_4_, p_i45015_5_, p_i45015_6_);
        this.centerListVertically = false;

        for (int lvt_8_1_ = 0; lvt_8_1_ < p_i45015_7_.length; lvt_8_1_ += 2) {
            GameSettings.Options lvt_9_1_ = p_i45015_7_[lvt_8_1_];
            GameSettings.Options lvt_10_1_ = lvt_8_1_ < p_i45015_7_.length - 1 ? p_i45015_7_[lvt_8_1_ + 1] : null;
            GuiButton lvt_11_1_ = this.createButton(p_i45015_1_, p_i45015_2_ / 2 - 155, 0, lvt_9_1_);
            GuiButton lvt_12_1_ = this.createButton(p_i45015_1_, p_i45015_2_ / 2 - 155 + 160, 0, lvt_10_1_);
            this.options.add(new Row(lvt_11_1_, lvt_12_1_));
        }

    }

    private GuiButton createButton(Minecraft p_148182_1_, int p_148182_2_, int p_148182_3_, GameSettings.Options p_148182_4_) {
        if (p_148182_4_ == null) {
            return null;
        } else {
            int lvt_5_1_ = p_148182_4_.getOrdinal();
            return (GuiButton) (p_148182_4_.isFloat()
                    ? new GuiOptionSlider(lvt_5_1_, p_148182_2_, p_148182_3_, p_148182_4_)
                    : new GuiOptionButton(
                            lvt_5_1_,
                            p_148182_2_,
                            p_148182_3_,
                            p_148182_4_,
                            p_148182_1_.gameSettings.getKeyBinding(p_148182_4_)
                    ));
        }
    }

    public Row getListEntry(int p_148180_1_) {
        return (Row) this.options.get(p_148180_1_);
    }

    protected int getSize() {
        return this.options.size();
    }

    public int getListWidth() {
        return 400;
    }

    protected int getScrollBarX() {
        return super.getScrollBarX() + 32;
    }

    @SideOnly(Side.CLIENT)
    public static class Row implements GuiListExtended.IGuiListEntry {

        private final Minecraft client = Minecraft.getMinecraft();
        private final GuiButton buttonA;
        private final GuiButton buttonB;

        public Row(GuiButton p_i45014_1_, GuiButton p_i45014_2_) {
            this.buttonA = p_i45014_1_;
            this.buttonB = p_i45014_2_;
        }

        public void drawEntry(
                int p_192634_1_,
                int p_192634_2_,
                int p_192634_3_,
                int p_192634_4_,
                int p_192634_5_,
                int p_192634_6_,
                int p_192634_7_,
                boolean p_192634_8_,
                float p_192634_9_
        ) {
            if (this.buttonA != null) {
                this.buttonA.y = p_192634_3_;
                this.buttonA.drawButton(this.client, p_192634_6_, p_192634_7_, p_192634_9_);
            }

            if (this.buttonB != null) {
                this.buttonB.y = p_192634_3_;
                this.buttonB.drawButton(this.client, p_192634_6_, p_192634_7_, p_192634_9_);
            }

        }

        public boolean mousePressed(
                int p_148278_1_,
                int p_148278_2_,
                int p_148278_3_,
                int p_148278_4_,
                int p_148278_5_,
                int p_148278_6_
        ) {
            if (this.buttonA.mousePressed(this.client, p_148278_2_, p_148278_3_)) {
                if (this.buttonA instanceof GuiOptionButton) {
                    this.client.gameSettings.setOptionValue(((GuiOptionButton) this.buttonA).getOption(), 1);
                    this.buttonA.displayString = this.client.gameSettings.getKeyBinding(Options.byOrdinal(this.buttonA.id));
                }

                return true;
            } else if (this.buttonB != null && this.buttonB.mousePressed(this.client, p_148278_2_, p_148278_3_)) {
                if (this.buttonB instanceof GuiOptionButton) {
                    this.client.gameSettings.setOptionValue(((GuiOptionButton) this.buttonB).getOption(), 1);
                    this.buttonB.displayString = this.client.gameSettings.getKeyBinding(Options.byOrdinal(this.buttonB.id));
                }

                return true;
            } else {
                return false;
            }
        }

        public void mouseReleased(
                int p_148277_1_,
                int p_148277_2_,
                int p_148277_3_,
                int p_148277_4_,
                int p_148277_5_,
                int p_148277_6_
        ) {
            if (this.buttonA != null) {
                this.buttonA.mouseReleased(p_148277_2_, p_148277_3_);
            }

            if (this.buttonB != null) {
                this.buttonB.mouseReleased(p_148277_2_, p_148277_3_);
            }

        }

        public void updatePosition(int p_192633_1_, int p_192633_2_, int p_192633_3_, float p_192633_4_) {
        }

    }

}
