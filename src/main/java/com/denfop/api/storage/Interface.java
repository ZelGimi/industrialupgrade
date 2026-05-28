package com.denfop.api.storage;

import com.denfop.api.storage.autocrafting.PatternStack;

import java.util.List;

public interface Interface extends TypeMechanismStorage {

    List<PatternStack> getPatterns();
}
