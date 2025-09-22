package com.denfop.render.base;

import net.minecraftforge.client.model.IModel;

public interface IReloadableModel extends IModel {

    void onReload();

}
