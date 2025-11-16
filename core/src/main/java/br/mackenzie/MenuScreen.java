package br.mackenzie;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.math.Vector2;

public class MenuScreen implements Screen {

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

    public MenuScreen(GameMain game) {
        this.game = game;
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

        // ✅ POSIÇÃO DO CICLISTA AJUSTADA
        findGoodPlayerPosition();

        createMenuButtons();

        System.out.println("✅ Menu carregado!");
    }

    // ✅ MÉTODO PARA ENCONTRAR BOA POSIÇÃO DO CICLISTA
    private void findGoodPlayerPosition() {
        // Procura por uma posição com altura razoável no terreno
        for (float x = 300f; x < 500f; x += 10f) {
            float height = terrain.getHeightAt(x);
            if (height > 100f && height < 180f) {
                fixedPlayerX = x - 250f;
                fixedPlayerY = height - 28f;
                System.out.println("🎯 Ciclista posicionado em: " + fixedPlayerX + ", " + fixedPlayerY);
                return;
            }
        }

        // Se não encontrou, usa uma posição padrão
        fixedPlayerX = 1100f;
        fixedPlayerY = terrain.getHeightAt(1100f) - 1100f;
    }

    private void createMenuButtons() {
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

        // ✅ CORREÇÃO: CRIAR OS 3 BOTÕES UMA ÚNICA VEZ
        TextButton btnIniciar = new TextButton("INICIAR JOGO", skin);
        TextButton btnOpcoes = new TextButton("PREFERÊNCIAS", skin);
        TextButton btnComoJogar = new TextButton("COMO JOGAR", skin);

        float centerX = Gdx.graphics.getWidth() / 2f;
        float centerY = Gdx.graphics.getHeight() / 2f;

        btnIniciar.setSize(280, 65);
        btnOpcoes.setSize(280, 65);
        btnComoJogar.setSize(280, 65);

        // ✅ MENU MAIS PRA DIREITA
        float buttonX = centerX + 200;

        // ✅ POSIÇÕES CORRETAS DOS 3 BOTÕES
        btnIniciar.setPosition(buttonX - 140, centerY + 80);    // TOPO
        btnOpcoes.setPosition(buttonX - 140, centerY + 0);      // MEIO
        btnComoJogar.setPosition(buttonX - 140, centerY - 80);  // BASE

        // ✅ LISTENER DO BOTÃO INICIAR
        btnIniciar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Abrindo seleção de personagens...");
                game.setScreen(new CharacterSelectionScreen(game));
            }
        });

        // ✅ LISTENER DO BOTÃO PREFERÊNCIAS
        btnOpcoes.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Abrindo Preferências...");
                game.setScreen(new PreferencesScreen(game));
            }
        });

        // ✅ LISTENER DO BOTÃO COMO JOGAR
        btnComoJogar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("📖 Abrindo Como Jogar...");
                game.setScreen(new HowToPlayScreen(game));
            }
        });

        // ✅ ADICIONAR TODOS OS BOTÕES AO STAGE
        stage.addActor(btnIniciar);
        stage.addActor(btnOpcoes);
        stage.addActor(btnComoJogar);
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
        renderMenuUI();
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

    private void renderMenuUI() {
        batch.begin();
        batch.setProjectionMatrix(camera.projection);

        game.getFont().getData().setScale(1.5f);
        game.getFont().setColor(Color.YELLOW);
        game.getFont().draw(batch, "🚴 JOGO DE REABILITAÇÃO", 200, 420);

        game.getFont().getData().setScale(1.0f);
        game.getFont().setColor(Color.WHITE);
        game.getFont().draw(batch, "Pedale para a recuperação!", 250, 390);

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
        if (batch != null) {
            batch.dispose();
            batch = null;
        }
        if (stage != null) {
            stage.dispose();
            stage = null;
        }
        if (skin != null) {
            skin.dispose();
            skin = null;
        }
        if (terrain != null) {
            terrain.dispose();
            terrain = null;
        }
    }}
