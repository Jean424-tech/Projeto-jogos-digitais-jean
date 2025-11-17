🚴 Ciclistinha - Jogo de Reabilitação Física Gamificado
Aluno: Jean Alex da Silva
RA: 10426728

<div align="center">
https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white
https://img.shields.io/badge/libGDX-FF0000?style=for-the-badge
https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white
https://img.shields.io/badge/IoT-00B0F0?style=for-the-badge&logo=arduino&logoColor=white

</div>
📋 Descrição do Projeto
Projeto de desenvolvimento de um jogo sério voltado para o processo de reabilitação física, utilizando a biblioteca libGDX para a implementação prática. O jogo é integrado a um dispositivo IoT conectado a uma bicicleta ergométrica estática que coleta dados de pedaladas em tempo real.

Através da gamificação do exercício físico, o jogo transforma a atividade de pedalar em uma experiência interativa e motivadora, usando a velocidade e a consistência das pedaladas como métricas para avaliação do desempenho do paciente/jogador.

🎯 Objetivos
🏥 Incentivar a melhoria contínua do paciente por meio de feedback visual e desafios baseados no desempenho

📊 Utilizar dados coletados do dispositivo IoT para ajustar a dificuldade e os objetivos do jogo

💪 Promover engajamento durante o processo de reabilitação física

🎮 Transformar exercícios repetitivos em uma experiência divertida e motivadora

⚙️ Funcionalidades Principais
🎮 Sistema de Jogo
Seleção de Personagens: Ciclistinha (1000m), Amarelinho (2000m), Azulzinho (3000m)

Controles Simples: Barra de espaço para acelerar

Interface Intuitiva: Velocidade, distância, tempo e barra de progresso

Sistema de Metas: Objetivos progressivos para cada personagem

🔄 Mecânicas de Reabilitação
Recebimento e processamento dos dados de pedaladas em tempo real via dispositivo IoT

Mecânicas de jogo que respondem à velocidade e constância do exercício

Feedback visual claro e motivacional do progresso

Sistema de pontuação e desafios progressivos

🎵 Recursos de Acessibilidade
Controle de áudio: Ligar/desligar música e efeitos sonoros

Interface adaptável: Design claro e fácil de entender

Múltiplas telas: Menu, seleção, jogo e resultados

🛠️ Tecnologias Utilizadas
libGDX – Framework Java para desenvolvimento de jogos multiplataforma

Java – Linguagem principal do projeto

Dispositivo IoT – Sensor de pedaladas conectado à bicicleta ergométrica

Comunicação IoT – Protocolo de comunicação para integrar hardware e software

Gradle – Gerenciamento de dependências e build

IntelliJ IDEA – Ambiente de desenvolvimento integrado

🚀 Como Executar o Projeto
Pré-requisitos
Java JDK 8 ou superior

Gradle

Dispositivo IoT configurado (opcional para teste)

📥 Clone e Execução
bash
# Clone o repositório
git clone https://github.com/seu-usuario/ciclistinha.git

# Navegue até o diretório
cd ciclistinha

# Execute o projeto
./gradlew desktop:run
🖥️ Execução no IntelliJ IDEA
Abra o projeto no IntelliJ IDEA

Configure o SDK do Java

Execute a classe DesktopLauncher no módulo desktop

Para testes sem hardware IoT, use a tecla ESPAÇO para simular pedaladas

🎮 Como Jogar
Controles
Espaço = Acelerar/Pedalar

Personagens e Metas
Personagem	Meta	Dificuldade
🟥 Ciclistinha	1000 metros	Fácil
🟨 Amarelinho	2000 metros	Médio
🟦 Azulzinho	3000 metros	Difícil
Fluxo do Jogo
Menu Principal → Iniciar Jogo

Seleção de Personagem → Escolha seu ciclista

Tela de Jogo → Pressione ESPAÇO para pedalar

Resultados → Veja seu tempo e progresso

🗂️ Estrutura do Projeto
text
ciclistinha/
├── core/
│   ├── src/
│   │   └── com/ciclistinha/
│   │       ├── screens/
│   │       │   ├── MenuScreen.java
│   │       │   ├── CharacterSelectScreen.java
│   │       │   ├── GameScreen.java
│   │       │   └── GameOverScreen.java
│   │       ├── entities/
│   │       │   ├── Player.java
│   │       │   └── Cyclist.java
│   │       ├── utils/
│   │       │   ├── IoTConnection.java
│   │       │   ├── AudioManager.java
│   │       │   └── GamePreferences.java
│   │       └── CiclistinhaGame.java
├── desktop/
│   └── src/
│       └── DesktopLauncher.java
├── android/
├── assets/
│   ├── images/
│   ├── sounds/
│   └── ui/
└── build.gradle
🔌 Integração IoT
O jogo se conecta com dispositivos IoT através da classe IoTConnection que:

Estabelece comunicação com sensores de pedalada

Converte dados físicos em inputs do jogo

Ajusta a dificuldade baseada no desempenho real

Fornece feedback visual do esforço físico

📞 Contato
Desenvolvedor: Jean Alex da Silva
RA: 10426728
Instituição de Ensino: [Nome da Instituição]

📄 Licença
Este projeto é desenvolvido para fins acadêmicos e de reabilitação física.
