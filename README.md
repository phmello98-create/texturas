# Texturas - App Android

Aplicativo Android (Kotlin + Jetpack Compose) para apoio em dependência e recaída.

## Como Buildar o APK

### Opção 1: GitHub Actions (Recomendado)

1. Acesse o repositório: https://github.com/phmello98-create/texturas
2. Clique em **Actions** (Ações)
3. Clique em **New workflow** (Novo workflow)
4. Copie e cole o código abaixo:

```yaml
name: Build Android APK

on:
  push:
    branches:
      - main
  workflow_dispatch:

jobs:
  build:
    runs-on: ubuntu-latest
    
    steps:
      - name: Checkout code
        uses: actions/checkout@v4
        
      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
          
      - name: Grant execute permission for gradlew
        run: chmod +x gradlew
        
      - name: Build with Gradle
        run: ./gradlew assembleDebug
        
      - name: Upload APK artifact
        uses: actions/upload-artifact@v4
        with:
          name: app-debug
          path: app/build/outputs/apk/debug/*.apk
          retention-days: 30
```

5. Salve o arquivo como `.github/workflows/build.yml`
6. O build começará automaticamente
7. Após a conclusão, baixe o APK em **Artifacts** → **app-debug**

### Opção 2: CircleCI

1. Crie uma conta em: https://circleci.com/signup/
2. Conecte sua conta do GitHub
3. Importe o repositório `phmello98-create/texturas`
4. Crie um arquivo `.circleci/config.yml`:

```yaml
version: 2.1
jobs:
  build:
    docker:
      - image: cimg/android:2024.01
    steps:
      - checkout
      - run:
          name: Grant execute permission for gradlew
          command: chmod +x gradlew
      - run:
          name: Build APK
          command: ./gradlew assembleDebug
      - store_artifacts:
          path: app/build/outputs/apk/debug/
          destination: apk
  test:
    docker:
      - image: cimg/android:2024.01
    steps:
      - checkout
      - run:
          name: Grant execute permission for gradlew
          command: chmod +x gradlew
      - run:
          name: Run tests
          command: ./gradlew test
      - store_test_results:
          path: app/build/test-results
workflows:
  build_and_test:
    jobs:
      - build
      - test
```

5. Commit e push o arquivo
6. O build começará automaticamente
7. Baixe o APK da aba **Artifacts**

### Opção 3: Build Local (Android Studio)

1. Instale o Android Studio: https://developer.android.com/studio
2. Clone o repositório: `git clone https://github.com/phmello98-create/texturas.git`
3. Abra o projeto no Android Studio
4. Aguarde o Gradle sync
5. Clique em **Build** → **Build Bundle(s) / APK(s)** → **Build APK(s)**
6. O APK estará em: `app/build/outputs/apk/debug/`

### Opção 4: Build Local (Linha de Comando)

Requer Android SDK instalado:

```bash
# Clone o repositório
git clone https://github.com/phmello98-create/texturas.git
cd texturas

# Execute o build
./gradlew assembleDebug

# O APK estará em:
# app/build/outputs/apk/debug/app-debug.apk
```

## Instalação no Dispositivo

### Via USB
```bash
# Ative o modo de desenvolvedor no dispositivo
# Conecte via USB e execute:
adb install app/build/outputs/apk/debug/app-debug.apk
```

### Via Transferência de Arquivo
1. Copie o arquivo `app-debug.apk` para o dispositivo
2. Abra o arquivo no gerenciador de arquivos
3. Permita instalações de fontes desconhecidas
4. Instale o aplicativo

## Configuração do Projeto

- **Linguagem**: Kotlin
- **UI Framework**: Jetpack Compose
- **Minimum SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)
- **Compile SDK**: 34
- **AGP Version**: 8.0.2
- **Kotlin Version**: 1.9.22

## Funcionalidades

- Onboarding inicial
- Botão de crise
- Repertório de atividades
- Diário de humor (Mood Journal)
- Persistência de dados com DataStore

## Dependências Principais

- Jetpack Compose (UI)
- Hilt (Injeção de dependência)
- Room (Banco de dados)
- Navigation Compose (Navegação)
- DataStore (Persistência)
- Coroutines (Programação assíncrona)
