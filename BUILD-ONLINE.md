# Build Online - Texturas APK

## Opção 1: Replit (Mais Fácil)

1. Acesse: https://replit.com
2. Crie uma conta gratuita
3. Clique em **Create** → **Import from GitHub**
4. Cole o URL: `https://github.com/phmello98-create/texturas`
5. Escolha template **Java** ou **Kotlin**
6. No terminal, execute:
   ```bash
   chmod +x gradlew
   ./gradlew assembleDebug
   ```
7. O APK estará em: `app/build/outputs/apk/debug/app-debug.apk`
8. Clique no arquivo com botão direito → **Download**

## Opção 2: Gitpod (Recomendado)

1. Acesse: https://gitpod.io/#https://github.com/phmello98-create/texturas
2. Faça login com GitHub
3. Aguarde o ambiente iniciar (~2 minutos)
4. No terminal, execute:
   ```bash
   chmod +x gradlew
   ./gradlew assembleDebug
   ```
5. O APK estará em: `app/build/outputs/apk/debug/app-debug.apk`
6. Clique no arquivo no explorer → **Download**

## Opção 3: CodeSandbox

1. Acesse: https://codesandbox.io
2. Crie uma conta
3. Importe do GitHub: `phmello98-create/texturas`
4. Abra o terminal (Ctrl + `)
5. Execute:
   ```bash
   chmod +x gradlew
   ./gradlew assembleDebug
   ```
6. Baixe o APK gerado

## Opção 4: GitHub Codespaces

1. Acesse: https://github.com/codespaces
2. Clique em **New codespace**
3. Selecione o repositório `phmello98-create/texturas`
4. Aguarde o ambiente iniciar
5. No terminal, execute:
   ```bash
   chmod +x gradlew
   ./gradlew assembleDebug
   ```
6. O APK estará em: `app/build/outputs/apk/debug/app-debug.apk`
7. Clique com botão direito → **Download**

---

## Instruções Passo a Passo (Gitpod)

**Passo 1:** Clique neste link direto:
https://gitpod.io/#https://github.com/phmello98-create/texturas

**Passo 2:** Faça login com sua conta GitHub

**Passo 3:** Aguarde o ambiente iniciar (pode levar 2-3 minutos)

**Passo 4:** No terminal (abaixo do editor), digite:
```bash
chmod +x gradlew && ./gradlew assembleDebug
```

**Passo 5:** Aguarde o build completar (~5 minutos)

**Passo 6:** No explorer lateral (esquerda), navegue até:
`app/build/outputs/apk/debug/`

**Passo 7:** Clique com botão direito em `app-debug.apk` → **Download**

**Passo 8:** Transfira o APK para seu Android e instale!

---

## Dicas

- **Gitpod** é a opção mais rápida e já vem com JDK configurado
- O build pode demorar 3-5 minutos na primeira vez
- Se der erro, tente executar o comando novamente
- Certifique-se de ter espaço suficiente no Gitpod (pelo menos 5GB)
