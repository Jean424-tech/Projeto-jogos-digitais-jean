package br.mackenzie;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class PreferencesScreen implements Screen {

    private final GameMain game;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Stage stage;
    private Skin skin;

    private Texture background;
    private Terrain terrain;
    private Texture cyclistWaving;

    private float backgroundOffset = 0f;
    private float backgroundSpeed = 15f;

    private float fixedPlayerX = 400f;
    private float fixedPlayerY = 0f;

    private AudioManager audioManager;

    public PreferencesScreen(GameMain game) {
        this.game = game;
        this.audioManager = AudioManager.getInstance();
    }

    @Override
    public void show() {
        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        camera.position.set(400f, 240, 0);
        camera.update();

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        background = game.getAssetManager().getTexture("background");

        terrain = new Terrain();

        cyclistWaving = game.getAssetManager().getTexture("cyclist_waving");
        if (cyclistWaving == null) {
            cyclistWaving = createPlaceholderCyclist();
        }

        findGoodPlayerPosition();

        createPreferenceButtons();

        System.out.println("✅ Preferências carregadas!");
    }

    private void findGoodPlayerPosition() {
        for (float x = 300f; x < 500f; x += 10f) {
            float height = terrain.getHeightAt(x);
            if (height > 100f && height < 180f) {
                fixedPlayerX = x - 250f;
                fixedPlayerY = height - 28f;
                return;
            }
        }
        fixedPlayerX = 1100f;
        fixedPlayerY = terrain.getHeightAt(1100f) - 1100f;
    }

    private void createPreferenceButtons() {
        skin = new Skin();
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        Texture whitePixel = new Texture(pixmap);
        pixmap.dispose();

        skin.add("white", whitePixel);
        BitmapFont font = game.getFont();
        skin.add("default-font", font);

        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.font = skin.getFont("default-font");
        textButtonStyle.fontColor = Color.WHITE;

        textButtonStyle.up = skin.newDrawable("white", new Color(0.1f, 0.3f, 0.6f, 0.7f));
        textButtonStyle.down = skin.newDrawable("white", new Color(0.05f, 0.2f, 0.5f, 0.9f));
        textButtonStyle.over = skin.newDrawable("white", new Color(0.2f, 0.4f, 0.8f, 0.8f));

        skin.add("default", textButtonStyle);

        // BOTÕES DE ÁUDIO - SEM EMOJIS
        TextButton btnMusica = new TextButton(
            audioManager.isMusicaLigada() ? "MUSICA: LIGADA" : "MUSICA: DESLIGADA",
            skin
        );
        TextButton btnEfeitos = new TextButton(
            audioManager.isEfeitosLigados() ? "EFEITOS: LIGADOS" : "EFEITOS: DESLIGADOS",
            skin
        );
        TextButton btnVoltar = new TextButton("VOLTAR AO MENU", skin);

        float centerX = Gdx.graphics.getWidth() / 2f;
        float centerY = Gdx.graphics.getHeight() / 2f;

        btnMusica.setSize(280, 65);
        btnEfeitos.setSize(280, 65);
        btnVoltar.setSize(280, 65);

        // MESMA POSIÇÃO DO MENU PRINCIPAL
        float buttonX = centerX + 200;

        btnMusica.setPosition(buttonX - 140, centerY + 80);
        btnEfeitos.setPosition(buttonX - 140, centerY - 0);
        btnVoltar.setPosition(buttonX - 140, centerY - 80);

        // LISTENERS COM CONTROLE DE ÁUDIO
        btnMusica.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audioManager.playBotao();
                audioManager.toggleMusica();
                btnMusica.setText(audioManager.isMusicaLigada() ? "MUSICA: LIGADA" : "MUSICA: DESLIGADA");
            }
        });

        btnEfeitos.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audioManager.playBotao();
                audioManager.toggleEfeitos();
                btnEfeitos.setText(audioManager.isEfeitosLigados() ? "EFEITOS: LIGADOS" : "EFEITOS: DESLIGADOS");
            }
        });

        btnVoltar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                audioManager.playBotao();
                game.setScreen(new MenuScreen(game));
            }
        });

        stage.addActor(btnMusica);
        stage.addActor(btnEfeitos);
        stage.addActor(btnVoltar);
    }

    @Override
    public void render(float delta) {
        backgroundOffset += backgroundSpeed * delta;
        if (backgroundOffset > 800f) {
            backgroundOffset = 0f;
        }

        renderGameScene();
        stage.act(delta);
        stage.draw();
        renderPreferenceUI();
    }

    private void renderGameScene() {
        Gdx.gl.glClearColor(0.2f, 0.6f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        if (background != null) {
            for (int i = -1; i <= 1; i++) {
                float x = backgroundOffset + (i * 800f);
                batch.draw(background, x, 0, 800, 480);
            }
        }

        terrain.render(batch);

        if (cyclistWaving != null) {
            batch.draw(cyclistWaving, fixedPlayerX, fixedPlayerY, 400, 300);
        }

        batch.end();
    }

    private void renderPreferenceUI() {
        batch.begin();
        batch.setProjectionMatrix(camera.projection);

        // TÍTULO SEM EMOJI
        game.getFont().getData().setScale(1.5f);
        game.getFont().setColor(Color.YELLOW);
        game.getFont().draw(batch, "CONFIGURACOES DE AUDIO", 180, 420);

        game.getFont().getData().setScale(1.0f);
        game.getFont().setColor(Color.WHITE);
        game.getFont().draw(batch, "Ajuste as preferencias de som", 230, 390);

        batch.end();
    }

    private Texture createPlaceholderCyclist() {
        Pixmap pixmap = new Pixmap(64, 64, Pixmap.Format.RGBA8888);
        pixmap.setColor(0.8f, 0.1f, 0.1f, 1);
        pixmap.fillRectangle(10, 10, 44, 44);
        pixmap.setColor(1, 0.8f, 0.6f, 1);
        pixmap.fillCircle(32, 45, 8);
        pixmap.setColor(0.8f, 0.1f, 0.1f, 1);
        pixmap.fillRectangle(40, 30, 15, 25);
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        camera.setToOrtho(false, 800, 480);
    }

    @Override public void pause() {}
    @Override public void resume() {}

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        if (stage != null) stage.dispose();
        if (skin != null) skin.dispose();
        if (terrain != null) terrain.dispose();
    }
}
