package com.reza.hollowknight.model;

import com.reza.hollowknight.model.enums.MenuType;

public class App {
    private static MenuType menuType;
    private static GameSession gameSession;

    public static MenuType getMenuType() {
        return menuType;
    }

    public static void setMenuType(MenuType menuType) {
        App.menuType = menuType;
    }
}
