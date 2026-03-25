#!/bin/bash
# Script de build automatizado para Gitpod
# Executa automaticamente o build do APK

set -e

echo "=========================================="
echo "🚀 Build do Texturas - Gitpod"
echo "=========================================="
echo ""

# Verificar se está no Gitpod
if [ -z "$GITPOD_WORKSPACE_ID" ]; then
    echo "⚠️  AVISO: Este script é otimizado para Gitpod"
    echo "   Mas pode funcionar em outros ambientes Linux"
    echo ""
fi

# Configurar Android SDK
if [ -d "/workspace/android-sdk" ]; then
    export ANDROID_SDK_ROOT=/workspace/android-sdk
    export PATH=$PATH:$ANDROID_SDK_ROOT/cmdline-tools/latest/bin:$ANDROID_SDK_ROOT/platform-tools
    echo "✅ Android SDK encontrado em /workspace/android-sdk"
else
    echo "❌ Android SDK não encontrado."
    echo "   Execute: export ANDROID_SDK_ROOT=/caminho/do/sdk"
    exit 1
fi

# Verificar Java
echo "☕ Verificando Java..."
java -version 2>&1 | head -1

# Verificar Android SDK
echo "📱 Verificando Android SDK..."
sdkmanager --version 2>&1 | head -1

echo ""
echo "=========================================="
echo "🔨 Iniciando build do APK..."
echo "=========================================="
echo ""

# Limpar build anterior (opcional)
# ./gradlew clean

# Executar build
./gradlew assembleDebug --no-daemon --stacktrace

# Verificar se build foi bem sucedido
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
    echo "   1. Clique no arquivo no painel lateral (Explorer)"
    echo "   2. Botão direito → Download"
    echo "   3. Salve no seu dispositivo Android"
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
