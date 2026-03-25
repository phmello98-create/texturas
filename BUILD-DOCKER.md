# Texturas - Script de Build Docker

Este script cria um container Docker para buildar o APK do Texturas em qualquer máquina Linux x86_64.

## Como usar

### 1. Salve este arquivo como `build-texturas.sh`

### 2. Torne executável e execute:
```bash
chmod +x build-texturas.sh
./build-texturas.sh
```

### Ou execute diretamente com bash:
```bash
bash build-texturas.sh
```

### 3. O APK será gerado em:
```
./Texturas/app/build/outputs/apk/debug/app-debug.apk
```

---

## Script build-texturas.sh

```bash
#!/bin/bash

# Criar Dockerfile temporário
cat > Dockerfile.texturas << 'DOCKERFILE'
FROM openjdk:17-jdk-slim

# Instalar dependências
RUN apt-get update && apt-get install -y \
    wget \
    unzip \
    git \
    && rm -rf /var/lib/apt/lists/*

# Baixar Android SDK
ENV ANDROID_SDK_ROOT=/opt/android-sdk
ENV PATH=${PATH}:${ANDROID_SDK_ROOT}/cmdline-tools/latest/bin:${ANDROID_SDK_ROOT}/platform-tools

RUN mkdir -p ${ANDROID_SDK_ROOT} && \
    cd ${ANDROID_SDK_ROOT} && \
    wget -q https://dl.google.com/android/repository/commandlinetools-linux-9477386_latest.zip && \
    unzip -q commandlinetools-linux-9477386_latest.zip && \
    rm commandlinetools-linux-9477386_latest.zip && \
    mkdir -p cmdline-tools/latest && \
    mv cmdline-tools/* cmdline-tools/latest/ 2>/dev/null || true

# Aceitar licenças e instalar componentes
RUN yes | sdkmanager --licenses && \
    sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0"

# Clonar repositório
WORKDIR /app
RUN git clone https://github.com/phmello98-create/texturas.git .

# Buildar APK
RUN chmod +x gradlew && ./gradlew assembleDebug

# Copiar APK para diretório de saída
RUN mkdir -p /output && \
    cp app/build/outputs/apk/debug/*.apk /output/
DOCKERFILE

# Buildar imagem Docker
echo "Criando imagem Docker para build..."
docker build -t texturas-builder -f Dockerfile.texturas .

# Criar diretório de saída
mkdir -p Texturas

# Extrair APK do container
echo "Extraindo APK..."
docker create --name texturas-temp texturas-builder
docker cp texturas-temp:/output/. Texturas/
docker rm texturas-temp

# Limpar
rm Dockerfile.texturas

echo ""
echo "✅ Build concluído!"
echo "📱 APK disponível em: ./Texturas/app-debug.apk"
echo ""
echo "Para instalar no seu dispositivo Android:"
echo "  adb install Texturas/app-debug.apk"
```

---

## Requisitos

- Docker instalado
- Conexão com internet
- ~5GB de espaço em disco

## Instalação do Docker

### Ubuntu/Debian:
```bash
sudo apt update
sudo apt install docker.io
sudo usermod -aG docker $USER
# Reinicie a sessão ou execute:
newgrp docker
```

### Fedora:
```bash
sudo dnf install docker
sudo systemctl start docker
sudo usermod -aG docker $USER
```

### Arch Linux:
```bash
sudo pacman -S docker
sudo systemctl start docker
sudo usermod -aG docker $USER
```

---

## Instalação do APK

Após o build, instale no seu dispositivo:

```bash
# Via ADB
adb install Texturas/app-debug.apk

# Ou copie para o dispositivo e instale manualmente
```

## Solução de Problemas

Se o Docker não estiver disponível, você pode:

1. **Usar GitHub Codespaces**: https://github.com/codespaces
   - Crie um codespace no repositório
   - Execute: `./gradlew assembleDebug`

2. **Usar Gitpod**: https://gitpod.io
   - Abra o repositório no Gitpod
   - Execute o build

3. **Instalar Android Studio** em um PC/Mac e buildar localmente
