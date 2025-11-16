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

public class GameOverScreen implements Screen {

    private final GameMain game;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Stage stage;
    private Skin skin;

    private Texture background;
    private Terrain terrain;
    private Texture victoryCharacter;

    private float backgroundOffset = 0f;
    private float backgroundSpeed = 15f;

    private float fixedPlayerX = 400f;
    private float fixedPlayerY = 0f;

    // Estatísticas do jogo
    private float distanciaPercorrida;
    private float tempoJogo;
    private String personagemUsado;

    public GameOverScreen(GameMain game, float distancia, float tempo, String personagem) {
        this.game = game;
        this.distanciaPercorrida = distancia;
        this.tempoJogo = tempo;
        this.personagemUsado = personagem;
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

        // ✅ CARREGA PERSONAGEM ESPECÍFICO PARA VITÓRIA
        carregarPersonagemVitoria();

        // Posição do personagem
        findGoodPlayerPosition();

        createMenuButtons();

        System.out.println("🏁 Game Over Screen carregada!");
    }

    // ✅ MÉTODO PARA CARREGAR PERSONAGEM DE VITÓRIA
    private void carregarPersonagemVitoria() {
        victoryCharacter = game.getAssetManager().getPersonagemVitoriaTexture(personagemUsado);

        if (victoryCharacter == null) {
            System.out.println("❌ Textura de vitória não encontrada, criando placeholder");
        } else {
            System.out.println("🎉 Personagem de vitória carregado: usando vitoria.png para " + personagemUsado);
        }
    }

    private void findGoodPlayerPosition() {
        // Procura por uma posição com altura razoável no terreno
        for (float x = 300f; x < 500f; x += 10f) {
            float height = terrain.getHeightAt(x);
            if (height > 100f && height < 180f) {
                fixedPlayerX = x - 250f;
                fixedPlayerY = height - 28f;
                System.out.println("🎯 Personagem posicionado em: " + fixedPlayerX + ", " + fixedPlayerY);
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

        // ✅ ESTILO ESPECIAL PARA O BOTÃO DO TEMPO (VERMELHO)
        TextButtonStyle tempoButtonStyle = new TextButtonStyle();
        tempoButtonStyle.font = skin.getFont("default-font");
        tempoButtonStyle.fontColor = Color.RED; // 🔴 COR VERMELHA

        tempoButtonStyle.up = skin.newDrawable("white", new Color(0.1f, 0.3f, 0.6f, 0.7f));
        tempoButtonStyle.down = skin.newDrawable("white", new Color(0.05f, 0.2f, 0.5f, 0.9f));
        tempoButtonStyle.over = skin.newDrawable("white", new Color(0.2f, 0.4f, 0.8f, 0.8f));

        // ✅ BOTÃO DO TEMPO (ÚNICO COM INFORMAÇÃO) - AGORA EM VERMELHO
        String tempoFormatado = formatarTempo(tempoJogo);
        TextButton btnTempo = new TextButton("TEMPO: " + tempoFormatado, tempoButtonStyle);

        // ✅ BOTÕES DE AÇÃO PRINCIPAIS (estilo normal)
        TextButton btnReiniciar = new TextButton("REINICIAR JOGO", skin);
        TextButton btnTrocarPersonagem = new TextButton("TROCAR PERSONAGEM", skin);
        TextButton btnMenuPrincipal = new TextButton("MENU PRINCIPAL", skin);

        float centerX = Gdx.graphics.getWidth() / 2f;
        float centerY = Gdx.graphics.getHeight() / 2f;

        // ✅ TAMANHO DOS BOTÕES (MESMO DA CHARACTER SELECTION)
        btnTempo.setSize(280, 65);
        btnReiniciar.setSize(280, 65);
        btnTrocarPersonagem.setSize(280, 65);
        btnMenuPrincipal.setSize(280, 65);

        // ✅ POSIÇÃO DOS BOTÕES - INVERTIDAS COMO SOLICITADO
        float buttonX = centerX + 200;

        // ✅ AGORA: MENU PRINCIPAL NO TOPO DA DIREITA (onde estava o Tempo)
        btnMenuPrincipal.setPosition(buttonX - 140, centerY + 80);      // POSIÇÃO DO CICLISTINHA
        btnReiniciar.setPosition(buttonX - 140, centerY + 0);           // POSIÇÃO DO AMARELINHO
        btnTrocarPersonagem.setPosition(buttonX - 140, centerY - 80);   // POSIÇÃO DO AZULZINHO

        // ✅ AGORA: TEMPO NA ESQUERDA/BAIXO (onde estava o Menu Principal)
        btnTempo.setPosition(buttonX - 500, centerY - 200);             // MUITO MAIS ESQUERDA

        // ✅ BOTÃO TEMPO (APENAS INFORMATIVO - SEM AÇÃO)
        btnTempo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Apenas informativo, não faz nada
                System.out.println("⏰ Tempo da partida: " + tempoFormatado);
            }
        });

        // ✅ BOTÃO REINICIAR
        btnReiniciar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("🔄 Reiniciando jogo com o mesmo personagem...");
                game.setScreen(new PlayScreen(game));
            }
        });

        // ✅ BOTÃO TROCAR PERSONAGEM
        btnTrocarPersonagem.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("👤 Indo para seleção de personagens...");
                game.setScreen(new CharacterSelectionScreen(game));
            }
        });

        // ✅ BOTÃO MENU PRINCIPAL
        btnMenuPrincipal.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("🏠 Voltando ao menu principal...");
                game.setScreen(new MenuScreen(game));
            }
        });

        // ✅ ADICIONA TODOS OS BOTÕES AO STAGE
        stage.addActor(btnTempo);
        stage.addActor(btnReiniciar);
        stage.addActor(btnTrocarPersonagem);
        stage.addActor(btnMenuPrincipal);
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

        // ✅ USA O PERSONAGEM ESPECÍFICO DE VITÓRIA
        if (victoryCharacter != null) {
            batch.draw(victoryCharacter, fixedPlayerX, fixedPlayerY, 400, 300);
        }

        batch.end();
    }

    private void renderMenuUI() {
        batch.begin();
        batch.setProjectionMatrix(camera.projection);

        // ✅ TÍTULO NA MESMA POSIÇÃO DO CHARACTER SELECTION
        game.getFont().getData().setScale(1.5f);
        game.getFont().setColor(Color.YELLOW);
        game.getFont().draw(batch, "🏆 VITÓRIA!", 180, 420); // MESMA POSIÇÃO DO "SELECIONE SEU PERSONAGEM"

        game.getFont().getData().setScale(1.0f);
        game.getFont().setColor(Color.WHITE);
        game.getFont().draw(batch, "Parabéns! Você completou o percurso!", 180, 390);

        batch.end();
    }

    // ✅ MÉTODO PARA FORMATAR TEMPO (MINUTOS:SEGUNDOS)
    private String formatarTempo(float segundos) {
        int minutos = (int) segundos / 60;
        int segs = (int) segundos % 60;
        return String.format("%d:%02d", minutos, segs);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        camera.setToOrtho(false, 800, 480);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

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
        System.out.println("🗑️ GameOverScreen recursos liberados");
    }
}
