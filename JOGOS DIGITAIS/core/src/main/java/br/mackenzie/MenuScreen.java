package br.mackenzie;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MenuScreen implements Screen {

    private final GameMain game;
    private SpriteBatch batch;
    private Texture background;
    private Stage stage;
    private Skin skin;

    public MenuScreen(GameMain game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();

        // Fundo
        background = new Texture(Gdx.files.internal("menu_background.png"));

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Criar skin manualmente (sem arquivos externos)
        skin = new Skin();

        BitmapFont font = new BitmapFont();
        skin.add("default-font", font);

        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.font = skin.getFont("default-font");
        textButtonStyle.fontColor = Color.WHITE;
        textButtonStyle.up = new com.badlogic.gdx.scenes.scene2d.utils.Drawable() {
            @Override
            public void draw(com.badlogic.gdx.graphics.g2d.Batch batch, float x, float y, float width, float height) {
                batch.setColor(0.2f, 0.4f, 0.8f, 1); // azul
                batch.draw(background, x, y, width, height);
                batch.setColor(Color.WHITE);
            }
            @Override public float getLeftWidth() { return 0; }
            @Override public void setLeftWidth(float leftWidth) {}
            @Override public float getRightWidth() { return 0; }
            @Override public void setRightWidth(float rightWidth) {}
            @Override public float getTopHeight() { return 0; }
            @Override public void setTopHeight(float topHeight) {}
            @Override public float getBottomHeight() { return 0; }
            @Override public void setBottomHeight(float bottomHeight) {}
            @Override public float getMinWidth() { return 0; }
            @Override public void setMinWidth(float minWidth) {}
            @Override public float getMinHeight() { return 0; }
            @Override public void setMinHeight(float minHeight) {}
        };

        skin.add("default", textButtonStyle);

        // Botões
        TextButton btnIniciar = new TextButton("Iniciar", skin);
        TextButton btnHistorico = new TextButton("Histórico", skin);
        TextButton btnConfig = new TextButton("Configurações", skin);

        float centerX = Gdx.graphics.getWidth() / 2f;
        float centerY = Gdx.graphics.getHeight() / 2f;

        btnIniciar.setSize(200, 50);
        btnHistorico.setSize(200, 50);
        btnConfig.setSize(200, 50);

        btnIniciar.setPosition(centerX - 110, centerY + 60);
        btnHistorico.setPosition(centerX - 110, centerY);
        btnConfig.setPosition(centerX - 110, centerY - 60);

        // Listeners
        btnIniciar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("🎮 Iniciar clicado!");
                game.setScreen(new PlayScreen(game));
            }
        });

        btnHistorico.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("📜 Histórico clicado!");
            }
        });

        btnConfig.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("⚙️ Configurações clicadas!");
            }
        });

        stage.addActor(btnIniciar);
        stage.addActor(btnHistorico);
        stage.addActor(btnConfig);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        stage.dispose();
        skin.dispose();
    }
}
