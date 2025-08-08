# Frontend do MS Finances

Esta é uma interface web simples para interagir com a API do MS Finances. Ela permite que clientes gerenciem contas e transações financeiras através de uma interface amigável.

## 🚀 Como usar

### 1. Iniciar a API
Primeiro, certifique-se de que a API está rodando:

```bash
# Na pasta raiz do projeto
./gradlew bootRun
```

A API ficará disponível em `http://localhost:8081`

### 2. Abrir a interface
Abra o arquivo `index.html` no seu navegador web favorito. Você pode:

- **Duplo clique** no arquivo `index.html`
- **Ou** abrir pelo navegador: File → Open → Selecionar `index.html`
- **Ou** usar um servidor local (recomendado):

```bash
# Se você tem Python instalado:
cd frontend
python3 -m http.server 3000

# Depois acesse: http://localhost:3000
```

## 📋 Funcionalidades Disponíveis

### 🏦 Gerenciamento de Contas
- **Buscar conta por ID**: Digite o ID da conta para ver seus detalhes
- **Buscar contas por cliente**: Digite o ID do cliente para ver todas suas contas
- **Criar contas**: Digite o ID do cliente para criar novas contas para ele
- **Cancelar contas**: Digite o ID do cliente para cancelar todas suas contas

### 💸 Gerenciamento de Transações
- **Buscar transação por ID**: Digite o ID da transação para ver seus detalhes
- **Buscar transações por cliente**: Digite o ID do cliente para ver todas suas transações
- **Criar transferência**: Informe conta origem, destino e valor para fazer uma transferência
- **Criar estorno**: Digite o ID da transação para estorná-la

## 🎯 Fluxo de uso recomendado

1. **Criar um cliente**: Comece criando contas para um cliente (ex: cliente ID 1)
2. **Verificar contas**: Use "Buscar contas por cliente" para ver as contas criadas
3. **Fazer transferência**: Use os IDs das contas para fazer uma transferência
4. **Verificar transações**: Use "Buscar transações por cliente" para ver o histórico
5. **Estornar se necessário**: Use o ID da transação para fazer estorno

## 💡 Dicas importantes

- **Valores monetários**: Use ponto como separador decimal (ex: 100.50, não 100,50)
- **IDs**: Todos os IDs são números inteiros
- **Conexão**: A interface precisa que a API esteja rodando na porta 8081
- **Erro de CORS**: Se encontrar erros de CORS, use um servidor local conforme mostrado acima

## 🔧 Estrutura dos arquivos

```
frontend/
├── index.html    # Página principal com formulários
├── styles.css    # Estilos e design da interface
├── script.js     # Lógica JavaScript para comunicação com a API
└── README.md     # Este arquivo de documentação
```

## 🌐 Endpoints da API

A interface consome os seguintes endpoints:

**Contas:**
- `GET /accounts/{id}` - Buscar conta por ID
- `GET /accounts?customerId={customerId}` - Buscar contas por customer ID  
- `POST /accounts` - Criar contas
- `PATCH /accounts/cancel` - Cancelar contas

**Transações:**
- `GET /transactions/{id}` - Buscar transação por ID
- `GET /transactions?customerId={customerId}` - Buscar transações por customer ID
- `POST /transactions` - Criar transferência
- `POST /transactions/{id}/refund` - Criar estorno

## 🎨 Recursos da Interface

- **Design responsivo**: Funciona em desktop e mobile
- **Feedback visual**: Animações e cores para melhor experiência
- **Tratamento de erros**: Mensagens claras quando algo dá errado
- **Formatação JSON**: Resultados bem formatados e coloridos
- **Abas organizadas**: Separação clara entre contas e transações
- **Loading**: Indicador visual durante as requisições

## 🐛 Solução de problemas

**API não conecta:**
- Verifique se a API está rodando em `http://localhost:8081`
- Verifique se não há firewall bloqueando a conexão

**Erro de CORS:**
- Use um servidor local para servir os arquivos HTML
- Ou configure CORS na API Spring Boot

**Formulário não funciona:**
- Verifique se todos os campos obrigatórios estão preenchidos
- Verifique se os valores estão no formato correto

---

🎉 **Pronto!** Agora você tem uma interface completa para interagir com sua API financeira!
