package com.denfop.api.reactors;

import com.denfop.tiles.reactors.ReactorsItem;
import ic2.api.reactor.IReactor;

import java.util.List;

public interface IAdvReactor extends IReactor {

    List<ReactorsItem> getReactorsItems();

}
