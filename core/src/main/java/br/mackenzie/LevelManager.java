package br.mackenzie;

public class LevelManager {
    private int currentLevel = 1;
    private float levelProgress = 0f;
    private float[] levelDistances = {500f, 1000f, 1500f}; // Metros para cada nível

    public LevelManager() {
        System.out.println("🎮 LevelManager iniciado - 3 fases carregadas");
    }

    public void update(float distance) {
        levelProgress = distance;

        // Verifica se avançou de nível
        if (currentLevel < levelDistances.length && distance >= levelDistances[currentLevel - 1]) {
            currentLevel++;
            System.out.println("🎉 NÍVEL " + currentLevel + " ALCANÇADO!");
        }
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public float getLevelProgress() {
        return levelProgress;
    }

    public float getCurrentLevelTarget() {
        if (currentLevel <= levelDistances.length) {
            return levelDistances[currentLevel - 1];
        }
        return levelDistances[levelDistances.length - 1];
    }

    public float getLevelProgressPercentage() {
        float target = getCurrentLevelTarget();
        return (levelProgress / target) * 100f;
    }

    // ✅ DIFICULDADE PROGRESSIVA POR NÍVEL
    public float getLevelDifficultyMultiplier() {
        switch (currentLevel) {
            case 1: return 1.0f; // Fácil
            case 2: return 1.5f; // Médio
            case 3: return 2.0f; // Difícil
            default: return 2.5f; // Extra difícil
        }
    }

    // ✅ TERRAIN MAIS ACIDENTADO POR NÍVEL
    public float getTerrainAmplitude() {
        switch (currentLevel) {
            case 1: return 60f;  // Colinas suaves
            case 2: return 90f;  // Colinas médias
            case 3: return 120f; // Montanhas íngremes
            default: return 150f;
        }
    }

    public float getTerrainFrequency() {
        switch (currentLevel) {
            case 1: return 0.003f; // Colinas largas
            case 2: return 0.005f; // Colinas mais frequentes
            case 3: return 0.008f; // Muitas subidas/descidas
            default: return 0.01f;
        }
    }

    public void reset() {
        currentLevel = 1;
        levelProgress = 0f;
    }
}
