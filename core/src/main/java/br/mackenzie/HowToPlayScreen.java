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

public class HowToPlayScreen implements Screen {
    private final GameMain game;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Stage stage;
    private Skin skin;

    // ✅ VARIÁVEIS DO FUNDO (MESMO LAYOUT DAS OUTRAS TELAS)
    private Texture background;
    private Terrain terrain;
    private Texture cyclistBackground;

    private float backgroundOffset = 0f;
    private float backgroundSpeed = 15f;
    private float fixedPlayerX = 400f;
    private float fixedPlayerY = 0f;

    public HowToPlayScreen(GameMain game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();

        // ✅ CÂMERA E VIEWPORT (IGUAL ÀS OUTRAS TELAS)
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        camera.position.set(400f, 240, 0);
        camera.update();

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // ✅ CARREGA OS ASSETS (IGUAL ÀS OUTRAS TELAS)
        background = game.getAssetManager().getTexture("background");
        terrain = new Terrain();

        // ✅ POSICIONA O CICLISTA (IGUAL ÀS OUTRAS TELAS)
        findGoodPlayerPosition();

        // ✅ CARREGA O PERSONAGEM PADRÃO
        carregarPersonagemFundo();

        // ✅ CRIA SKIN COM MESMO ESTILO
        criarSkin();

        // ✅ CRIA UI
        criarUI();

        System.out.println("✅ Tela de Como Jogar carregada!");
    }

    // ✅ MÉTODO PARA CARREGAR PERSONAGEM DO FUNDO
    private void carregarPersonagemFundo() {
        cyclistBackground = game.getAssetManager().getPersonagemMenuTexture("padrao");

        if (cyclistBackground == null) {
            System.out.println("❌ Personagem não encontrado para fundo");
        } else {
            System.out.println("✅ Personagem carregado para fundo");
        }
    }

    // ✅ MÉTODO PARA ENCONTRAR BOA POSIÇÃO DO CICLISTA
    private void findGoodPlayerPosition() {
        for (float x = 300f; x < 500f; x += 10f) {
            float height = terrain.getHeightAt(x);
            if (height > 100f && height < 180f) {
                fixedPlayerX = x - 250f;
                fixedPlayerY = height - 28f;
                System.out.println("🎯 Ciclista posicionado em: " + fixedPlayerX + ", " + fixedPlayerY);
                return;
            }
        }
        fixedPlayerX = 1100f;
        fixedPlayerY = terrain.getHeightAt(1100f) - 1100f;
    }

    // ✅ MÉTODO CRIAR SKIN (MESMO ESTILO DAS OUTRAS TELAS)
    private void criarSkin() {
        skin = new Skin();

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        Texture whitePixel = new Texture(pixmap);
        pixmap.dispose();

        skin.add("white", whitePixel);

        BitmapFont font = game.getFont();
        skin.add("default-font", font);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = skin.getFont("default-font");
        textButtonStyle.fontColor = Color.WHITE;

        textButtonStyle.up = skin.newDrawable("white", new Color(0.1f, 0.3f, 0.6f, 0.7f));
        textButtonStyle.down = skin.newDrawable("white", new Color(0.05f, 0.2f, 0.5f, 0.9f));
        textButtonStyle.over = skin.newDrawable("white", new Color(0.2f, 0.4f, 0.8f, 0.8f));

        skin.add("default", textButtonStyle);
    }

    // ✅ MÉTODO CRIAR UI
    private void criarUI() {
        float centerX = Gdx.graphics.getWidth() / 2f;
        float centerY = Gdx.graphics.getHeight() / 2f;

        // ✅ BOTÃO VOLTAR (MESMA POSIÇÃO DAS OUTRAS TELAS)
        TextButton btnVoltar = criarBotao("VOLTAR AO MENU", new Color(0.1f, 0.3f, 0.6f, 0.7f));

        btnVoltar.setSize(280, 65);
        btnVoltar.setPosition(centerX + 200 - 140, centerY - 200); // MESMA POSIÇÃO DO VOLTAR

        stage.addActor(btnVoltar);

        btnVoltar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("🔙 Voltando ao menu principal...");
                game.setScreen(new MenuScreen(game));
            }
        });
    }

    // ✅ MÉTODO CRIAR BOTÃO SIMPLES
    private TextButton criarBotao(String texto, Color cor) {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = game.getFont();
        style.fontColor = Color.WHITE;
        style.up = skin.newDrawable("white", cor);
        style.down = skin.newDrawable("white", new Color(cor.r * 0.7f, cor.g * 0.7f, cor.b * 0.7f, 0.9f));
        style.over = skin.newDrawable("white", new Color(cor.r * 1.2f, cor.g * 1.2f, cor.b * 1.2f, 0.8f));

        return new TextButton(texto, style);
    }

    // ✅ RENDER (MESMO DAS OUTRAS TELAS)
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

    // ✅ RENDER DO CENÁRIO (MESMO DAS OUTRAS TELAS)
    private void renderGameScene() {
        Gdx.gl.glClearColor(0.2f, 0.6f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // ✅ BACKGROUND ANIMADO
        if (background != null) {
            for (int i = -1; i <= 1; i++) {
                float x = backgroundOffset + (i * 800f);
                batch.draw(background, x, 0, 800, 480);
            }
        }

        terrain.render(batch);

        // ✅ PERSONAGEM NO FUNDO
        if (cyclistBackground != null) {
            batch.draw(cyclistBackground, fixedPlayerX, fixedPlayerY, 400, 300);
        }

        batch.end();
    }

    // ✅ RENDER DA UI COM TEXTO EXPLICATIVO
    private void renderMenuUI() {
        batch.begin();
        batch.setProjectionMatrix(camera.projection);

        // ✅ TÍTULO
        game.getFont().getData().setScale(1.8f);
        game.getFont().setColor(Color.YELLOW);
        game.getFont().draw(batch, "🎮 COMO JOGAR", 180, 450);

        game.getFont().getData().setScale(1.0f);
        game.getFont().setColor(Color.WHITE);

        // ✅ TEXTO EXPLICATIVO - CONTROLES
        float startY = 200;
        float lineHeight = 25;

        game.getFont().draw(batch, "CONTROLES:", 50, startY);
        game.getFont().draw(batch, "ESPAÇO = ACELERAR", 70, startY - (lineHeight * 1));

        // ✅ OBJETIVO DO JOGO
        game.getFont().draw(batch, "OBJETIVO:", 50, startY - (lineHeight * 3));
        game.getFont().draw(batch, "Percorra a maior distância possível!", 70, startY - (lineHeight * 4));
        game.getFont().draw(batch, "Complete a meta de cada personagem!", 70, startY - (lineHeight * 5));
        // ✅ PERSONAGENS
        game.getFont().draw(batch, "PERSONAGENS:", 50, startY - (lineHeight * 7));
        game.getFont().draw(batch, "CICLISTINHA = Meta: 1000m", 70, startY - (lineHeight * 8));
        game.getFont().draw(batch, "AMARELINHO = Meta: 2000m", 70, startY - (lineHeight * 9));
        game.getFont().draw(batch, "AZULZINHO = Meta: 3000m", 70, startY - (lineHeight * 10));

        batch.end();
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
        cyclistBackground = null;
        System.out.println("✅ Tela de Como Jogar liberada");
    }
}
