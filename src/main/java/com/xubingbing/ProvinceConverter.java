package com.xubingbing;

import javafx.util.StringConverter;

public class ProvinceConverter extends StringConverter<Province> {

    @Override
    public String toString(Province object) {
        return object.getPROVINCE_NAME();
    }

    @Override
    public Province fromString(String string) {
        return Controller.provinceMap.get(string);
    }
}
