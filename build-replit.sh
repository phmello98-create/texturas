#!/bin/bash
# Script de build para Replit - Texturas APK
# Execute este script no terminal do Replit

set -e

echo "=========================================="
echo "🚀 Build do Texturas - Replit"
echo "=========================================="
echo ""

# Configurar Android SDK
echo "📱 Configurando Android SDK..."
mkdir -p ~/android-sdk
cd ~/android-sdk

if [ ! -d "cmdline-tools/latest" ]; then
    echo "⬇️  Baixando Android SDK..."
    wget -q https://dl.google.com/android/repository/commandlinetools-linux-9477386_latest.zip
    unzip -q commandlinetools-linux-9477386_latest.zip
    rm commandlinetools-linux-9477386_latest.zip
    mkdir -p cmdline-tools/latest
    mv cmdline-tools/* cmdline-tools/latest/ 2>/dev/null || true
fi

# Configurar variáveis de ambiente
export ANDROID_SDK_ROOT=$HOME/android-sdk
export PATH=$PATH:$ANDROID_SDK_ROOT/cmdline-tools/latest/bin:$ANDROID_SDK_ROOT/platform-tools

echo "✅ Android SDK configurado!"

# Instalar componentes se necessário
echo "📦 Verificando componentes do SDK..."
sdkmanager --version 2>&1 > /dev/null || {
    echo "⬇️  Aceitando licenças..."
    yes | sdkmanager --licenses
    echo "⬇️  Instalando componentes..."
    sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0"
}

# Voltar ao projeto
cd /home/runner/texturas

# Verificar Java
echo "☕ Verificando Java..."
java -version 2>&1 | head -1

# Dar permissão ao gradlew
chmod +x gradlew

echo ""
echo "=========================================="
echo "🔨 Iniciando build do APK..."
echo "=========================================="
echo ""

# Executar build
./gradlew assembleDebug --no-daemon

# Verificar resultado
if [ -f "app/build/outputs/apk/debug/app-debug.apk" ]; then
    echo ""
    echo "=========================================="
    echo "✅ BUILD CONCLUÍDO COM SUCESSO!"
    echo "=========================================="
    echo ""
    echo "📱 APK gerado:"
    ls -lh app/build/outputs/apk/debug/app-debug.apk
    echo ""
    echo "📍 Local: app/build/outputs/apk/debug/app-debug.apk"
    echo ""
    echo "💡 Para baixar:"
    echo "   1. No File Explorer (esquerda), navegue até:"
    echo "      app/build/outputs/apk/debug/"
    echo "   2. Clique com botão direito em app-debug.apk"
    echo "   3. Selecione Download"
    echo ""
    echo "📲 Para instalar via ADB:"
    echo "   adb install app/build/outputs/apk/debug/app-debug.apk"
    echo ""
    echo "🎉 Pronto para usar!"
    echo "=========================================="
else
    echo ""
    echo "❌ ERRO: APK não foi gerado"
    echo "   Verifique os logs acima para detalhes"
    exit 1
fi
