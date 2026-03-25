# Build Replit - Texturas APK

## ✅ Replit é Gratuito (sem cartão de crédito!)

## Passo a Passo

### 1. Acesse o Replit
https://replit.com

### 2. Crie conta gratuita
- Clique em **Sign up**
- Use email, Google ou GitHub
- **Não pede cartão de crédito!**

### 3. Importe o projeto
- Clique em **Create** (ou **+ Create Repl**)
- Selecione **Import from GitHub**
- Cole: `https://github.com/phmello98-create/texturas`
- Escolha template: **Java** (ou deixe em branco)
- Clique em **Import from GitHub**

### 4. Configure o Android SDK
No terminal do Replit (abaixo do editor), execute:

```bash
# Baixar Android SDK
mkdir -p ~/android-sdk
cd ~/android-sdk

wget -q https://dl.google.com/android/repository/commandlinetools-linux-9477386_latest.zip
unzip -q commandlinetools-linux-9477386_latest.zip
rm commandlinetools-linux-9477386_latest.zip
mkdir -p cmdline-tools/latest
mv cmdline-tools/* cmdline-tools/latest/ 2>/dev/null || true

# Configurar variáveis de ambiente
export ANDROID_SDK_ROOT=$HOME/android-sdk
export PATH=$PATH:$ANDROID_SDK_ROOT/cmdline-tools/latest/bin:$ANDROID_SDK_ROOT/platform-tools

# Aceitar licenças
yes | sdkmanager --licenses

# Instalar componentes necessários
sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0"
```

### 5. Buildar o APK
```bash
# Voltar ao diretório do projeto
cd /home/runner/texturas

# Dar permissão ao gradlew
chmod +x gradlew

# Buildar APK
./gradlew assembleDebug
```

### 6. Baixar o APK
Quando o build terminar:

1. No painel lateral esquerdo (File Explorer)
2. Navegue até: `app/build/outputs/apk/debug/`
3. Você verá o arquivo `app-debug.apk`
4. Clique com botão direito nele
5. Selecione **Download**
6. O arquivo será baixado para seu computador

### 7. Instalar no Android
- Transfira o APK para seu dispositivo
- Abra o arquivo
- Permita instalação de fontes desconhecidas
- Instale o aplicativo

---

## Dicas

- **Tempo de build**: 5-10 minutos na primeira vez
- **Espaço necessário**: ~3GB para SDK + dependências
- **Se der erro**: Execute o comando de build novamente
- **Espaço Replit**: O plano gratuito tem espaço suficiente

## Solução de Problemas

### Erro: "command not found"
Execute novamente o comando de configuração do Android SDK (Passo 4).

### Erro: "Out of memory"
- Feche outros projetos no Replit
- Tente novamente

### Erro: "Build failed"
- Verifique os logs de erro
- Execute `./gradlew clean` e tente novamente

---

## Pronto! 🎉

Após baixar o APK, instale no seu Android e comece a usar o Texturas!
