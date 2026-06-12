package com.reza.hollowknight.model;

import com.reza.hollowknight.model.enums.MenuType;

public class App {
    private static MenuType menuType = MenuType.MAIN;
    private static GameSession gameSession;

    public static MenuType getMenuType() {
        return menuType;
    }

    public static void setMenuType(MenuType menuType) {
        App.menuType = menuType;
    }

    public static GameSession getGameSession() {
        return gameSession;
    }

    public static void setGameSession(GameSession session) {
        App.gameSession = session;
    }
}
