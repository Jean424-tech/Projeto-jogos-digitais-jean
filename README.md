# 🚴 Ciclistinha — Jogo de Reabilitação Física Gamificado

**Aluno:** Jean Alex da Silva  
**RA:** 10426728  

<div align="center">

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![libGDX](https://img.shields.io/badge/libGDX-FF0000?style=for-the-badge)
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)

</div>

---

## 📋 Descrição do Projeto

**Ciclistinha** é um jogo desenvolvido em **Java** utilizando a biblioteca **libGDX**, criado para auxiliar no processo de **reabilitação física** através da gamificação.

O jogo transforma o exercício físico de pedalar em uma experiência divertida, interativa e motivacional.  
A velocidade e constância das pedaladas são usadas como métricas para avaliar o desempenho do jogador/paciente.

---

## 🎯 Objetivos

- 🏥 Incentivar a melhora contínua durante a reabilitação  
- 🎮 Tornar a atividade de pedalar mais divertida e engajadora  
- 📊 Utilizar dados de um dispositivo IoT (opcional) para adaptar desafios  
- 💪 Estimular esforço constante e progressivo  
- 🔄 Auxiliar terapeutas com dados de desempenho  

---

## ⚙️ Funcionalidades Principais

### 🎮 Sistema de Jogo

- Seleção entre três personagens com metas diferentes  
- Controles simples (apenas **ESPAÇO** para acelerar)  
- Interface completa exibindo:
  - Velocidade  
  - Distância percorrida  
  - Tempo  
  - Barra de progresso  
- Sistema de metas e finalização de fase  
- Animações e efeitos visuais  

---

## 🔄 Mecânicas de Reabilitação

- Velocidade do ciclista baseada na **frequência da pedalada**  
- Feedback visual para motivação  
- Progresso gradual e recompensas simples  
- Possível integração com pedal físico via IoT  

---

## 🎵 Acessibilidade

- Ativar/desativar música  
- Interface clara e intuitiva  
- Telas separadas:
  - Menu Principal  
  - Seleção de Personagem  
  - Jogo  
  - Tela de Resultado  
  - Tela de Preferências  

---

## 🛠️ Tecnologias Utilizadas

- **Java (JDK 8+)**  
- **libGDX**  
- **Gradle**  
- **IntelliJ IDEA**  


---

## 🚀 Como Executar o Projeto

### ✔️ Pré-requisitos

- Java JDK **8 ou superior**
- Gradle instalado  
- IntelliJ IDEA ou Eclipse  
- (Opcional) dispositivo IoT configurado

---
### 🖥️ 2. Executar no IntelliJ IDEA

Abra o projeto na IDE
Configure o SDK do Java (8+)
Aguarde o Gradle sincronizar
Vá até o módulo desktop

Execute a classe: lwjgl3Launcher.java

---

### 🎮 Como Jogar
- ⌨️ Controles
- Tecla	Função
- ESPAÇO Acelerar o ciclista

---

### 🧍‍♂️ Personagens
Personagem	Meta	Dificuldade
- 🟥 Ciclistinha	1000 metros	Fácil
- 🟨 Amarelinho	2000 metros	Médio
- 🟦 Azulzinho	    3000 metros	Difícil

---
### 🔁 Fluxo do Jogo

- Menu Principal
- Seleção de Personagem
- Tela de Jogo
- Pressione ESPAÇO para pedalar
- Atenção à barra de progresso e velocidade
  
---
### 🗂️ Estrutura
```bash
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

### 📥 1. Clonar o Repositório

```bash
git clone https://github.com/Jean424-tech/Projeto-jogos-digitais-jean.git
```

### 📞 Contato

- Desenvolvedor: Jean Alex da Silva
- RA: 10426728
- Instituição: Universidade Presbiteriana Mackenzie

---

### 📄 Licença

Este projeto foi desenvolvido para fins acadêmicos e de apoio à reabilitação física.
Distribuição permitida mediante citação do autor. era assim que eu queria
