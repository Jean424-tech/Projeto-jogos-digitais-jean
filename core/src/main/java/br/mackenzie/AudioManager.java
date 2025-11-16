// AudioManager.java
package br.mackenzie;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.Preferences;
import java.util.HashMap;
import java.util.Map;

public class AudioManager {
    private static AudioManager instance;
    private Music musicaFundo;
    private Map<String, Sound> sons;
    private boolean musicaLigada = true;
    private boolean efeitosLigados = true;
    private float volumeMusica = 0.7f;
    private float volumeEfeitos = 0.8f;
    private Preferences prefs;

    // 🔥 CONTROLE DE SONS CONTÍNUOS
    private long idPedalada = -1;
    private long idDescendo = -1;

    private AudioManager() {
        prefs = Gdx.app.getPreferences("ConfiguracoesJogo");
        sons = new HashMap<>();
        carregarConfiguracoes();
    }

    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    public void carregarMusica() {
        try {
            musicaFundo = Gdx.audio.newMusic(Gdx.files.internal("musica2.mp3"));
            musicaFundo.setLooping(true);
            musicaFundo.setVolume(volumeMusica);
            if (musicaLigada) {
                musicaFundo.play();
            }
            System.out.println("🎵 Música carregada com sucesso!");
        } catch (Exception e) {
            System.out.println("❌ Música não encontrada: " + e.getMessage());
        }
    }

    public void carregarEfeitos() {
        try {
            // 🔥 CARREGA APENAS OS EFEITOS QUE VOCÊ PRECISA
            carregarSom("pedalada", "pedalada.wav");
            carregarSom("descendo", "descendo.wav");
            carregarSom("vitoria", "vitoria.wav");
            carregarSom("botao", "botao.wav");

            System.out.println("🎯 Efeitos sonoros carregados!");
        } catch (Exception e) {
            System.out.println("❌ Alguns efeitos não foram carregados: " + e.getMessage());
        }
    }

    private void carregarSom(String nome, String caminho) {
        try {
            Sound som = Gdx.audio.newSound(Gdx.files.internal(caminho));
            sons.put(nome, som);
            System.out.println("✅ Som '" + nome + "' carregado");
        } catch (Exception e) {
            System.out.println("❌ Não foi possível carregar: " + caminho);
        }
    }

    // 🔥 MÉTODOS PARA SONS NORMAIS (ÚNICOS)
    public void playSom(String nome) {
        if (efeitosLigados && sons.containsKey(nome)) {
            sons.get(nome).play(volumeEfeitos);
        }
    }

    public void playSom(String nome, float volume) {
        if (efeitosLigados && sons.containsKey(nome)) {
            sons.get(nome).play(volume * volumeEfeitos);
        }
    }

    // 🔥 MÉTODOS PARA SONS CONTÍNUOS (PEDALADA E DESCIDA)
    public void iniciarPedalada() {
        if (efeitosLigados && sons.containsKey("pedalada")) {
            if (idPedalada == -1) { // Só inicia se não estiver tocando
                idPedalada = sons.get("pedalada").loop(volumeEfeitos * 0.6f);
            }
        }
    }

    public void pararPedalada() {
        if (idPedalada != -1) {
            sons.get("pedalada").stop(idPedalada);
            idPedalada = -1;
        }
    }

    public void iniciarDescida() {
        if (efeitosLigados && sons.containsKey("descendo")) {
            if (idDescendo == -1) {
                idDescendo = sons.get("descendo").loop(volumeEfeitos * 0.4f);
            }
        }
    }

    public void pararDescida() {
        if (idDescendo != -1) {
            sons.get("descendo").stop(idDescendo);
            idDescendo = -1;
        }
    }

    // 🔥 MÉTODO PARA VITÓRIA (SOM ÚNICO)
    public void playVitoria() {
        playSom("vitoria");
    }

    // 🔥 MÉTODO PARA BOTÕES (SOM ÚNICO)
    public void playBotao() {
        playSom("botao", 0.5f); // Volume um pouco mais baixo para botões
    }

    // 🔥 CONTROLES DE CONFIGURAÇÃO
    public void toggleMusica() {
        musicaLigada = !musicaLigada;
        if (musicaFundo != null) {
            if (musicaLigada) {
                musicaFundo.play();
            } else {
                musicaFundo.pause();
            }
        }
        salvarConfiguracoes();
    }

    public void toggleEfeitos() {
        efeitosLigados = !efeitosLigados;

        // Se desligou os efeitos, para os sons contínuos
        if (!efeitosLigados) {
            pararPedalada();
            pararDescida();
        }

        salvarConfiguracoes();
    }

    public void setVolumeMusica(float novoVolume) {
        this.volumeMusica = Math.max(0, Math.min(1, novoVolume));
        if (musicaFundo != null) {
            musicaFundo.setVolume(volumeMusica);
        }
        salvarConfiguracoes();
    }

    public void setVolumeEfeitos(float novoVolume) {
        this.volumeEfeitos = Math.max(0, Math.min(1, novoVolume));

        // Atualiza volume dos sons contínuos se estiverem tocando
        if (idPedalada != -1) {
            sons.get("pedalada").setVolume(idPedalada, volumeEfeitos * 0.6f);
        }
        if (idDescendo != -1) {
            sons.get("descendo").setVolume(idDescendo, volumeEfeitos * 0.4f);
        }

        salvarConfiguracoes();
    }

    public boolean isMusicaLigada() {
        return musicaLigada;
    }

    public boolean isEfeitosLigados() {
        return efeitosLigados;
    }

    public float getVolumeMusica() {
        return volumeMusica;
    }

    public float getVolumeEfeitos() {
        return volumeEfeitos;
    }

    private void carregarConfiguracoes() {
        musicaLigada = prefs.getBoolean("musicaLigada", true);
        efeitosLigados = prefs.getBoolean("efeitosLigados", true);
        volumeMusica = prefs.getFloat("volumeMusica", 0.7f);
        volumeEfeitos = prefs.getFloat("volumeEfeitos", 0.8f);
    }

    private void salvarConfiguracoes() {
        prefs.putBoolean("musicaLigada", musicaLigada);
        prefs.putBoolean("efeitosLigados", efeitosLigados);
        prefs.putFloat("volumeMusica", volumeMusica);
        prefs.putFloat("volumeEfeitos", volumeEfeitos);
        prefs.flush();
    }

    public void dispose() {
        // Para todos os sons antes de liberar
        pararPedalada();
        pararDescida();

        if (musicaFundo != null) {
            musicaFundo.dispose();
        }
        for (Sound som : sons.values()) {
            som.dispose();
        }
        sons.clear();
    }
}
