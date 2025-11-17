# 🚴 Ciclistinha - Jogo de Reabilitação Física Gamificado

**Aluno:** Jean Alex da Silva
**RA:** 10426728

<div align="center">

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![libGDX](https://img.shields.io/badge/libGDX-FF0000?style=for-the-badge)
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)

</div>

---

## 📋 Descrição do Projeto

Projeto de desenvolvimento de um jogo sério voltado para o processo de reabilitação física, utilizando a biblioteca **libGDX** para a implementação prática.

Através da gamificação do exercício físico, o jogo transforma a atividade de pedalar em uma experiência interativa e motivadora, usando a velocidade e a consistência das pedaladas como métricas para avaliação do desempenho do paciente/jogador.

---

## 🎯 Objetivos

* 🏥 Incentivar a melhoria contínua do paciente por meio de feedback visual e desafios baseados no desempenho
* 📊 Utilizar dados coletados do dispositivo IoT para ajustar a dificuldade e os objetivos do jogo
* 💪 Promover engajamento durante o processo de reabilitação física
* 🎮 Transformar exercícios repetitivos em uma experiência divertida e motivadora

---

## ⚙️ Funcionalidades Principais

### 🎮 Sistema de Jogo

* **Seleção de Personagens:** Ciclistinha (1000m), Amarelinho (2000m), Azulzinho (3000m)
* **Controles Simples:** Barra de espaço para acelerar
* **Interface Intuitiva:** Velocidade, distância, tempo e barra de progresso
* **Sistema de Metas:** Objetivos progressivos para cada personagem

### 🔄 Mecânicas de Reabilitação

* Mecânicas de jogo que respondem à velocidade e constância do exercício
* Feedback visual claro e motivacional do progresso
* Sistema de pontuação e desafios progressivos

### 🎵 Recursos de Acessibilidade

* Controle de áudio: Ligar/desligar música e efeitos sonoros
* Interface adaptável: Design claro e fácil de entender
* Múltiplas telas: Menu, seleção, jogo e resultados

---

## 🛠️ Tecnologias Utilizadas

* **libGDX** – Framework Java para jogos multiplataforma
* **Java** – Linguagem principal
* **Gradle** – Gerenciamento de dependências
* **IntelliJ IDEA** – IDE do projeto

---

## 🚀 Como Executar o Projeto

### Pré-requisitos

* Java JDK 8 ou superior
* Gradle
* Dispositivo IoT configurado (opcional para testes)

### 📥 Clone e Execução

```bash
# Clone o repositório
git clone https://github.com/seu-usuario/ciclistinha.git

# Navegue até o diretório
cd ciclistinha

# Execute o projeto
./gradlew desktop:run
```

### 🖥️ Execução no IntelliJ IDEA

1. Abra o projeto no IntelliJ IDEA
2. Configure o SDK do Java
3. Execute a classe **DesktopLauncher** no módulo *desktop*

---

## 🎮 Como Jogar

### Controles

* **Espaço:** Acelerar

### Personagens e Metas

| Personagem     | Meta        | Dificuldade |
| -------------- | ----------- | ----------- |
| 🟥 Ciclistinha | 1000 metros | Fácil       |
| 🟨 Amarelinho  | 2000 metros | Médio       |
| 🟦 Azulzinho   | 3000 metros | Difícil     |

### Fluxo do Jogo

1. Menu Principal → Iniciar Jogo
2. Seleção de Personagem → Escolha seu ciclista
3. Tela de Jogo → Pressione ESPAÇO para pedalar
4. Resultados → Veja seu tempo e progresso

---

## 🗂️ Estrutura do Projeto

```
src/
└── main/
    └── java/
        └── br.mackenzie/
            ├── AnimationManager.java
            ├── AssetManager.java
            ├── AudioManager.java
            ├── CharacterSelectionScreen.java
            ├── GameMain.java
            ├── GameOverScreen.java
            ├── HowToPlayScreen.java
            ├── LevelManager.java
            ├── Main.java
            ├── MenuScreen.java
            ├── PhysicsEngine.java
            ├── Player.java
            ├── PlayScreen.java
            ├── PreferencesScreen.java
            └── Terrain.java

```
## 📞 Contato

**Desenvolvedor:** Jean Alex da Silva
**RA:** 10426728
**Instituição de Ensino:** Universidade Presbiteriana Mackenzie 

---

## 📄 Licença

Este projeto é desenvolvido para fins acadêmicos e de reabilitação física.
