package com.github.klambo94.mycena_rosea.domain;

public enum Type {
    POTION("potion"),
    ELIXIR("elixir"),
    DRY_GOODS("dry_goods"),
    FOOD("food"),
    HEALING_POTION("healing_potion"),
    HEALING_ELIXIR("healing_elixir"),
    HEALING_DRY_GOODS("healing_dry_goods"),
    HEALING_FOOD("healing_food"),
    WET_GOODS("wet_goods"),
    INGREDIENT("ingredient");
    private String type;


    Type(String type) {
        type = type.toLowerCase();
    }

    String getType() {
        return type;
    }
}
