package br.mackenzie;

import com.badlogic.gdx.Game;

public class GameMain extends Game {
    @Override
    public void create() {
        setScreen(new MenuScreen(this)); // Abre o menu ao iniciar
    }
}
