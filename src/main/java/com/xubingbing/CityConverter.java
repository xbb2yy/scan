package com.xubingbing;

import javafx.util.StringConverter;

public class CityConverter extends StringConverter<City> {
    @Override
    public String toString(City object) {
        return object.getCITY_NAME();
    }

    @Override
    public City fromString(String string) {
        return Controller.cityMap.get(string);
    }
}
