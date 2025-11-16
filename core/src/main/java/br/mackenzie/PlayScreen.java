package br.mackenzie;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Color;

public class PlayScreen implements Screen {
    private final GameMain game;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;

    private Player player;
    private Texture background;
    private Terrain terrain;

    // ✅ VARIÁVEL PARA O PERSONAGEM SELECIONADO
    private String personagemSelecionado;

    // VARIÁVEIS PARA BOTÕES
    private Stage gameStage;
    private Skin gameSkin;
    private boolean isPaused = false;
    private Texture whitePixel;

    // VARIÁVEIS PARA O TEXTO DE PAUSA
    private TextureRegion pauseBackground;

    private float gameTime = 0f;
    private float totalDistance = 0f;
    private float physicsSpeed = 0f;

    // SISTEMA DE ÁUDIO
    private AudioManager audioManager;
    private boolean bikeDescendo = false;
    private float ultimaAltura = 0f;

    // ✅ SISTEMA DE FIM DE JOGO (AGORA DINÂMICO)
    private boolean jogoTerminado = false;

    // ✅ CONSTRUTOR ÚNICO - USA APENAS GameMain
    public PlayScreen(GameMain game) {
        this.game = game;

        // ✅ PEGA O PERSONAGEM DO GameMain
        this.personagemSelecionado = game.getPersonagemSelecionado();

        System.out.println("🎮 PlayScreen criada - Personagem: " + personagemSelecionado);
    }

    // ✅ MÉTODO PARA OBTER META DINÂMICA DO PERSONAGEM
    private float getDistanciaMaxima() {
        return game.getMetaPersonagemAtual();
    }

    @Override
    public void show() {
        System.out.println("🎮 Iniciando PlayScreen...");
        System.out.println("👤 Personagem em uso: " + personagemSelecionado);
        System.out.println("🎯 Meta definida: " + getDistanciaMaxima() + " metros");

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 480, camera);
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);

        background = game.getAssetManager().getTexture("background");
        terrain = new Terrain();

        // ✅ CRIA O PLAYER COM O PERSONAGEM DO GameMain
        player = new Player(game, personagemSelecionado);
        System.out.println("🎯 Player criado com: " + personagemSelecionado);

        // INICIALIZA AUDIO MANAGER
        audioManager = AudioManager.getInstance();

        // CRIAR FUNDO PARA TEXTO DE PAUSA
        createPauseBackground();

        float groundY = 50;
        player.setPosition(100, groundY);
        ultimaAltura = groundY;

        setupGameButtons();

        System.out.println("🎮 PlayScreen carregada!");
    }

    private void createPauseBackground() {
        Pixmap pixmap = new Pixmap(200, 80, Pixmap.Format.RGBA8888);
        pixmap.setColor(0.8f, 0.2f, 0.2f, 0.9f);
        pixmap.fill();
        pixmap.setColor(1, 1, 1, 1);
        pixmap.drawRectangle(0, 0, 200, 80);
        Texture texture = new Texture(pixmap);
        pauseBackground = new TextureRegion(texture);
        pixmap.dispose();
    }

    private void setupGameButtons() {
        gameStage = new Stage(new ScreenViewport());

        gameSkin = new Skin();
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        whitePixel = new Texture(pixmap);
        pixmap.dispose();

        gameSkin.add("white", whitePixel);
        gameSkin.add("default-font", game.getFont());

        TextButtonStyle buttonStyle = new TextButtonStyle();
        buttonStyle.font = gameSkin.getFont("default-font");
        buttonStyle.fontColor = Color.WHITE;

        buttonStyle.up = gameSkin.newDrawable("white", new Color(0.1f, 0.3f, 0.6f, 0.7f));
        buttonStyle.down = gameSkin.newDrawable("white", new Color(0.05f, 0.2f, 0.5f, 0.9f));
        buttonStyle.over = gameSkin.newDrawable("white", new Color(0.2f, 0.4f, 0.8f, 0.8f));

        gameSkin.add("default", buttonStyle);

        TextButton btnPause = new TextButton("PAUSAR", gameSkin);
        btnPause.setSize(120, 45);
        btnPause.setPosition(20, 420);

        btnPause.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (jogoTerminado) return; // Não permite pausar se o jogo terminou

                isPaused = !isPaused;
                if (isPaused) {
                    btnPause.setText("CONTINUAR");
                    System.out.println("⏸️ Jogo pausado");
                    audioManager.pararDescida();
                    bikeDescendo = false;
                } else {
                    btnPause.setText("PAUSAR");
                    System.out.println("▶️ Jogo continuado");
                }
            }
        });

        TextButton btnRestart = new TextButton("REINICIAR", gameSkin);
        btnRestart.setSize(120, 45);
        btnRestart.setPosition(150, 420);

        btnRestart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("🔄 Reiniciando jogo...");
                audioManager.pararDescida();
                game.setScreen(new PlayScreen(game));
            }
        });

        TextButton btnMenu = new TextButton("MENU", gameSkin);
        btnMenu.setSize(120, 45);
        btnMenu.setPosition(280, 420);

        btnMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("🏠 Voltando ao menu...");
                audioManager.pararDescida();
                game.setScreen(new MenuScreen(game));
            }
        });

        gameStage.addActor(btnPause);
        gameStage.addActor(btnRestart);
        gameStage.addActor(btnMenu);

        Gdx.input.setInputProcessor(gameStage);
    }

    @Override
    public void render(float delta) {
        handleInput(delta);

        if (!isPaused && !jogoTerminado) {
            update(delta);
        }

        renderGame();
        renderUI();

        gameStage.act(delta);
        gameStage.draw();
    }

    private void handleInput(float delta) {
        if (jogoTerminado) {
            // Desativa controles quando o jogo termina
            return;
        }

        if (!isPaused) {
            boolean isPedaling = Gdx.input.isKeyJustPressed(Input.Keys.SPACE);
            player.update(delta, isPedaling, terrain);
            physicsSpeed = player.getPhysicsSpeed();

            if (isPedaling && audioManager.isEfeitosLigados()) {
                audioManager.playSom("pedalada", 0.2f);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            audioManager.pararDescida();
            game.setScreen(new MenuScreen(game));
        }
    }

    private void update(float delta) {
        gameTime += delta;
        totalDistance += physicsSpeed * delta;

        // ✅ VERIFICA SE ATINGIU A DISTÂNCIA MÁXIMA DO PERSONAGEM
        if (totalDistance >= getDistanciaMaxima() && !jogoTerminado) {
            terminarJogo();
            return;
        }

        terrain.update(player.getX());
        camera.position.x = player.getX() + 200;
        camera.update();

        controlarEfeitosSonoros();
    }

    private void terminarJogo() {
        jogoTerminado = true;
        audioManager.pararDescida();
        bikeDescendo = false;

        System.out.println("🎉 Jogo terminado! Distância alcançada: " + (int)totalDistance + "m");
        System.out.println("🎯 Meta do " + personagemSelecionado + ": " + getDistanciaMaxima() + "m");

        // Troca para a tela de Game Over
        Gdx.app.postRunnable(() -> {
            game.setScreen(new GameOverScreen(game, totalDistance, gameTime, personagemSelecionado));
        });
    }

    private void controlarEfeitosSonoros() {
        if (!audioManager.isEfeitosLigados()) {
            if (bikeDescendo) {
                audioManager.pararDescida();
                bikeDescendo = false;
            }
            return;
        }

        boolean descendoAgora = detectarDescida();

        if (descendoAgora && !bikeDescendo) {
            audioManager.iniciarDescida();
            bikeDescendo = true;
        } else if (!descendoAgora && bikeDescendo) {
            audioManager.pararDescida();
            bikeDescendo = false;
        }
    }

    private boolean detectarDescida() {
        if (physicsSpeed < 3.0f) {
            return false;
        }

        float inclinacao = player.getTerrainAngle();
        if (inclinacao < -3.0f) {
            return true;
        }

        float alturaAtual = player.getY();
        boolean mudancaAltura = alturaAtual < ultimaAltura - 2.0f;
        ultimaAltura = alturaAtual;

        if (mudancaAltura && physicsSpeed > 5.0f) {
            return true;
        }

        return false;
    }

    private void renderGame() {
        Gdx.gl.glClearColor(0.1f, 0.5f, 0.9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        if (background != null) {
            float bgX = camera.position.x - viewport.getWorldWidth() / 2;

            float startX = (float) Math.floor(bgX / 800) * 800;
            for (float x = startX; x < bgX + viewport.getWorldWidth() + 800; x += 800) {
                batch.draw(background, x, 0, 800, 480);
            }
        }

        terrain.render(batch);
        player.render(batch);

        batch.end();
    }

    private void renderUI() {
        batch.begin();

        batch.setProjectionMatrix(gameStage.getCamera().combined);

        game.getFont().draw(batch, "JOGO DE REABILITAÇÃO", 20, 380);
        game.getFont().draw(batch, "Velocidade: " + (int)physicsSpeed + " km/h", 20, 360);
        game.getFont().draw(batch, "Distância: " + (int)totalDistance + " m", 20, 340);
        game.getFont().draw(batch, "Tempo: " + (int)gameTime + "s", 20, 320);

        // ✅ BARRA DE PROGRESSO COM META DINÂMICA - POSIÇÃO AJUSTADA
        float progresso = totalDistance / getDistanciaMaxima();
        if (progresso > 1) progresso = 1;

        // ✅ POSIÇÕES AJUSTADAS (20 pixels mais para baixo)
        float barraY = 290; // Era 300, agora 280 (20px mais baixo)
        float textoMetaY = 285; // Era 295, agora 275 (20px mais baixo)

        // Desenha barra de fundo
        batch.setColor(0.3f, 0.3f, 0.3f, 1);
        batch.draw(whitePixel, 20, barraY, 200, 12);

        // ✅ DESENHA BARRA DE PROGRESSO COM COR DA META DO PERSONAGEM
        batch.setColor(game.getCorMetaAtual());
        batch.draw(whitePixel, 20, barraY, 200 * progresso, 12);
        batch.setColor(Color.WHITE);

        // ✅ META ESPECÍFICA DO PERSONAGEM
        game.getFont().draw(batch, "Meta: " + game.getNomeMetaAtual(), 20, textoMetaY);

        // Tela de pause
        if (isPaused && !jogoTerminado) {
            float screenWidth = 800;
            float screenHeight = 480;
            float bgWidth = 200;
            float bgHeight = 80;

            float bgX = (screenWidth - bgWidth) / 2;
            float bgY = (screenHeight - bgHeight) / 2;

            batch.setColor(1, 1, 1, 1);
            batch.draw(pauseBackground, bgX, bgY, bgWidth, bgHeight);

            game.getFont().setColor(Color.WHITE);
            game.getFont().getData().setScale(1.2f);

            String pauseText = "JOGO PAUSADO";
            float textWidth = pauseText.length() * 12;
            float textX = bgX + (bgWidth - textWidth) / 2;
            float textY = bgY + (bgHeight / 2) + 10;

            game.getFont().draw(batch, pauseText, textX, textY);
            game.getFont().getData().setScale(1.0f);
        }

        batch.end();
    }

    // ✅ MÉTODO AUXILIAR PARA COR DO PERSONAGEM
    private Color getCorPersonagem() {
        switch (personagemSelecionado) {
            case "guerreiro": return Color.YELLOW;
            case "mago": return Color.BLUE;
            case "arqueiro": return Color.GREEN;
            default: return Color.RED; // Padrão = Vermelho
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        gameStage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        audioManager.pararDescida();
        bikeDescendo = false;
    }

    @Override
    public void resume() {}

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
        audioManager.pararDescida();
    }

    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        if (terrain != null) terrain.dispose();
        if (gameStage != null) gameStage.dispose();
        if (gameSkin != null) gameSkin.dispose();
        if (whitePixel != null) whitePixel.dispose();
        if (pauseBackground != null) pauseBackground.getTexture().dispose();
    }
}
