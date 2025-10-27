package com.github.klambo94.mycena_rosea.domain;

public enum Type {
    POTION("potion"),
    ELIXIR("elixir"),
    INGREDIENT("ingredient");
    private String type;


    Type(String type) {
        type = type.toLowerCase();
    }

    String getType() {
        return type;
    }
}
