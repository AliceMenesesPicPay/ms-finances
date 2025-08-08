// ===== CONFIGURAÇÃO E ESTADO GLOBAL =====
const API_BASE_URL = 'http://localhost:8081';
let currentCustomer = null;
let customerAccounts = [];
let currentAccountType = 'CHECKING'; // Conta corrente como padrão
let checkingAccount = null;
let savingsAccount = null;

// ===== ELEMENTOS DOM =====
const loginScreen = document.getElementById('loginScreen');
const createAccountScreen = document.getElementById('createAccountScreen');
const bankApp = document.getElementById('bankApp');
const currentCustomerIdEl = document.getElementById('currentCustomerId');
const loadingOverlay = document.getElementById('loadingOverlay');
const notification = document.getElementById('notification');
const notificationMessage = document.getElementById('notificationMessage');
const successScreen = document.getElementById('successScreen');
const errorScreen = document.getElementById('errorScreen');

// ===== NAVEGAÇÃO ENTRE TELAS =====
function showLoginScreen() {
    loginScreen.style.display = 'flex';
    createAccountScreen.style.display = 'none';
    bankApp.style.display = 'none';
    successScreen.style.display = 'none';
    errorScreen.style.display = 'none';
    
    // Limpar formulários
    document.getElementById('loginForm').reset();
    document.getElementById('createAccountForm').reset();
}

function showCreateAccountScreen() {
    loginScreen.style.display = 'none';
    createAccountScreen.style.display = 'flex';
    bankApp.style.display = 'none';
    successScreen.style.display = 'none';
    errorScreen.style.display = 'none';
    
    // Focar no campo de input
    setTimeout(() => {
        document.getElementById('newCustomerId').focus();
    }, 100);
}

function showBankApp() {
    loginScreen.style.display = 'none';
    createAccountScreen.style.display = 'none';
    bankApp.style.display = 'flex';
    successScreen.style.display = 'none';
    errorScreen.style.display = 'none';
}

// ===== ALTERNADOR DE CONTAS =====
function switchAccount(accountType) {
    currentAccountType = accountType;
    
    // Atualizar botões do seletor
    document.querySelectorAll('.account-selector-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    document.querySelector(`.account-selector-btn[data-account-type="${accountType}"]`).classList.add('active');
    
    // Atualizar título da conta
    const accountTitle = accountType === 'CHECKING' ? 'Saldo - Conta Corrente' : 'Saldo - Conta Poupança';
    document.getElementById('currentAccountTitle').textContent = accountTitle;
    
    // Atualizar saldo exibido
    updateCurrentAccountBalance();
    
    // Atualizar overview se necessário
    if (customerAccounts.length > 0) {
        loadAccountsOverview();
        loadRecentTransactions();
    }
}

function loadAccountsOverview() {
    const accountsOverview = document.getElementById('accountsOverview');
    accountsOverview.innerHTML = '';
    
    if (customerAccounts.length > 0) {
        // Mostrar apenas a conta do tipo atual
        const currentAccount = currentAccountType === 'CHECKING' ? checkingAccount : savingsAccount;
        
        if (currentAccount) {
            const accountCard = createAccountCard(currentAccount);
            accountsOverview.appendChild(accountCard);
        } else {
            accountsOverview.innerHTML = `<p class="no-accounts">Conta ${currentAccountType === 'CHECKING' ? 'Corrente' : 'Poupança'} não encontrada.</p>`;
        }
    } else {
        accountsOverview.innerHTML = '<p class="no-accounts">Nenhuma conta encontrada.</p>';
    }
}

async function loadRecentTransactions() {
    if (!currentCustomer) {
        console.log('Nenhum cliente logado, não é possível carregar transações');
        return;
    }
    
    try {
        // Buscar transações do cliente
        const transactions = await makeAPIRequest(`/transactions?customerId=${currentCustomer}`);
        
        const recentTransactionsContainer = document.getElementById('recentTransactions');
        
        if (transactions && transactions.length > 0) {
            // Ordenar por data (mais recentes primeiro) e pegar apenas as 5 mais recentes
            const sortedTransactions = transactions
                .sort((a, b) => new Date(b.createdAt || b.timestamp || 0) - new Date(a.createdAt || a.timestamp || 0))
                .slice(0, 5);
            
            recentTransactionsContainer.innerHTML = sortedTransactions.map(transaction => `
                <div class="transaction-item">
                    <div class="transaction-icon">
                        ${getTransactionIcon(transaction.type)}
                    </div>
                    <div class="transaction-details">
                        <div class="transaction-description">${getTransactionDescription(transaction)}</div>
                        <div class="transaction-date">${formatDate(transaction.createdAt || transaction.timestamp)}</div>
                    </div>
                    <div class="transaction-amount ${transaction.type === 'CREDIT' || transaction.type === 'DEPOSIT' ? 'positive' : 'negative'}">
                        ${transaction.type === 'CREDIT' || transaction.type === 'DEPOSIT' ? '+' : '-'}R$ ${Math.abs(transaction.amount || 0).toLocaleString('pt-BR', {
                            minimumFractionDigits: 2,
                            maximumFractionDigits: 2
                        })}
                    </div>
                </div>
            `).join('');
        } else {
            recentTransactionsContainer.innerHTML = `
                <div class="no-transactions">
                    <span class="no-transactions-icon">📋</span>
                    <p>Nenhuma transação encontrada</p>
                    <small>Suas transações aparecerão aqui</small>
                </div>
            `;
        }
        
    } catch (error) {
        console.error('Erro ao carregar transações:', error);
        document.getElementById('recentTransactions').innerHTML = `
            <div class="no-transactions">
                <span class="no-transactions-icon">⚠️</span>
                <p>Erro ao carregar transações</p>
                <small>${error.message}</small>
            </div>
        `;
    }
}

function createTransactionElement(transaction) {
    const div = document.createElement('div');
    div.className = 'transaction-item';
    
    // Determinar tipo e ícone da transação
    let transactionType, icon, amountClass, amountPrefix;
    
    if (transaction.transactionType === 'DEPOSIT') {
        transactionType = 'deposit';
        icon = '+';
        amountClass = 'positive';
        amountPrefix = '+';
    } else if (transaction.transactionType === 'TRANSFER') {
        transactionType = 'transfer';
        icon = '→';
        amountClass = 'negative';
        amountPrefix = '-';
    } else {
        transactionType = 'withdrawal';
        icon = '-';
        amountClass = 'negative';
        amountPrefix = '-';
    }
    
    // Formatar data
    const transactionDate = new Date(transaction.createdAt || transaction.date);
    const formattedDate = transactionDate.toLocaleDateString('pt-BR', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric'
    });
    
    // Determinar título baseado no tipo
    let title;
    switch (transaction.transactionType) {
        case 'DEPOSIT':
            title = 'Depósito';
            break;
        case 'TRANSFER':
            title = 'Transferência';
            break;
        default:
            title = 'Movimentação';
    }
    
    div.innerHTML = `
        <div class="transaction-info">
            <div class="transaction-icon ${transactionType}">
                ${icon}
            </div>
            <div class="transaction-details">
                <div class="transaction-title">${title}</div>
                <div class="transaction-subtitle">${formattedDate}</div>
            </div>
        </div>
        <div class="transaction-amount ${amountClass}">
            ${amountPrefix}${formatCurrency(Math.abs(transaction.amount))}
        </div>
    `;
    
    return div;
}

function updateCurrentAccountBalance() {
    const currentAccount = currentAccountType === 'CHECKING' ? checkingAccount : savingsAccount;
    
    if (currentAccount) {
        const balance = currentAccount.balance || 0;
        document.getElementById('totalBalance').textContent = `R$ ${balance.toLocaleString('pt-BR', {
            minimumFractionDigits: 2,
            maximumFractionDigits: 2
        })}`;
    } else {
        // Se a conta não existir, mostrar opção de criar
        document.getElementById('totalBalance').textContent = 'Conta não encontrada';
    }
}

// ===== SISTEMA DE LOGIN =====
document.getElementById('loginForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const customerId = document.getElementById('customerId').value;
    await login(customerId);
});

// ===== SISTEMA DE CRIAÇÃO DE CONTA =====
document.getElementById('createAccountForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const accountName = document.getElementById('accountName').value;
    const customerId = document.getElementById('customerId').value;
    const accountType = document.querySelector('input[name="accountType"]:checked').value;
    
    await createAccount(accountName, customerId, accountType);
});

async function createAccount(accountName, customerId, accountType) {
    try {
        // Mostrar loading
        const submitBtn = document.querySelector('#createAccountForm button');
        const loadingSpinner = document.querySelector('.create-loading');
        const buttonText = submitBtn.querySelector('span');
        
        buttonText.style.display = 'none';
        loadingSpinner.style.display = 'flex';
        submitBtn.disabled = true;
        
        // Verificar se o cliente já existe
        try {
            const existingAccounts = await makeAPIRequest(`/accounts?customerId=${customerId}`);
            if (existingAccounts.length > 0) {
                throw new Error('Este número de cliente já possui contas no sistema. Tente fazer login ou use um número diferente.');
            }
        } catch (error) {
            // Se der erro 404 ou similar, é porque o cliente não existe (que é o que queremos)
            if (!error.message.includes('já possui contas')) {
                // Continue com a criação da conta
            } else {
                throw error;
            }
        }
        
        // Criar a conta com o tipo especificado
        const newAccount = await makeAPIRequest('/accounts', {
            method: 'POST',
            body: JSON.stringify({ 
                customerId: parseInt(customerId),
                accountName: accountName,
                accountType: accountType
            })
        });
        
        // Mostrar tela de sucesso
        showSuccessScreen(customerId, [newAccount], accountType);
        
    } catch (error) {
        showErrorScreen(error.message);
    } finally {
        // Restaurar botão
        const submitBtn = document.querySelector('#createAccountForm button');
        const loadingSpinner = document.querySelector('.create-loading');
        const buttonText = submitBtn.querySelector('span');
        
        buttonText.style.display = 'block';
        loadingSpinner.style.display = 'none';
        submitBtn.disabled = false;
    }
}

function showSuccessScreen(customerId, accounts, accountType) {
    const accountTypeName = accountType === 'CHECKING' ? 'Conta Corrente' : 'Conta Poupança';
    const accountTypeIcon = accountType === 'CHECKING' ? '💳' : '🏛️';
    
    document.getElementById('successMessage').innerHTML = `
        <strong>Bem-vindo ao Alice Bank!</strong><br><br>
        
        ✅ Seu número de cliente: <strong>#${customerId}</strong><br>
        ✅ Conta criada: <strong>${accountTypeIcon} ${accountTypeName}</strong><br>
        ✅ Status: <strong>Ativa e pronta para uso</strong><br><br>
        
        Agora você pode fazer login e começar a usar seu banco digital!
    `;
    
    successScreen.style.display = 'flex';
    
    // Salvar o customerId temporariamente para login automático
    sessionStorage.setItem('newCustomerId', customerId);
}

function showErrorScreen(errorMessage) {
    let friendlyMessage = '';
    
    if (errorMessage.includes('já possui contas')) {
        friendlyMessage = 'Este número de cliente já está em uso. Que tal tentar fazer login ou escolher um número diferente?';
    } else if (errorMessage.includes('Bad Request') || errorMessage.includes('400')) {
        friendlyMessage = 'Por favor, verifique se o número do cliente está correto. Use apenas números.';
    } else if (errorMessage.includes('500') || errorMessage.includes('Internal Server Error')) {
        friendlyMessage = 'Nosso sistema está passando por instabilidades. Tente novamente em alguns instantes.';
    } else if (errorMessage.includes('Network Error') || errorMessage.includes('fetch')) {
        friendlyMessage = 'Verifique sua conexão com a internet e tente novamente.';
    } else {
        friendlyMessage = errorMessage;
    }
    
    document.getElementById('errorMessage').innerHTML = friendlyMessage;
    errorScreen.style.display = 'flex';
}

function proceedToLogin() {
    const newCustomerId = sessionStorage.getItem('newCustomerId');
    if (newCustomerId) {
        document.getElementById('customerId').value = newCustomerId;
        sessionStorage.removeItem('newCustomerId');
    }
    showLoginScreen();
    
    // Auto-login após um pequeno delay
    if (newCustomerId) {
        setTimeout(() => {
            login(newCustomerId);
        }, 500);
    }
}

function closeErrorModal() {
    errorScreen.style.display = 'none';
}

// ===== MELHORIAS NO SISTEMA DE LOGIN =====

async function login(customerId) {
    try {
        showLoading(true);
        
        // Buscar contas do cliente para validar se existe
        const accounts = await makeAPIRequest(`/accounts?customerId=${customerId}`);
        
        if (accounts.length === 0) {
            // Cliente não tem contas, sugerir criação
            const createAccounts = confirm(
                '🏦 Cliente não encontrado no sistema.\n\n' +
                'Deseja criar novas contas para este cliente?\n\n' +
                '✅ Conta Corrente\n' +
                '✅ Conta Poupança\n' +
                '✅ Acesso imediato'
            );
            
            if (createAccounts) {
                await createAccount(customerId);
                return; // A função createAccount vai gerenciar o fluxo
            } else {
                showNotification('Login cancelado. Você pode criar uma conta clicando em "Criar conta".', 'error');
                return;
            }
        } else {
            customerAccounts = accounts;
            
            // Separar contas por tipo
            checkingAccount = accounts.find(acc => acc.accountType === 'CHECKING');
            savingsAccount = accounts.find(acc => acc.accountType === 'SAVINGS');
        }
        
        currentCustomer = customerId;
        currentCustomerIdEl.textContent = `#${customerId}`;
        
        // Definir conta corrente como ativa por padrão
        currentAccountType = 'CHECKING';
        
        // Mostrar app
        showBankApp();
        
        // Carregar dashboard
        await loadDashboard();
        
        // Inicializar o seletor de conta
        switchAccount('CHECKING');
        
        showNotification(`Bem-vindo de volta ao Alice Bank! 🏦`, 'success');
        
    } catch (error) {
        showNotification(`Erro no login: ${error.message}`, 'error');
    } finally {
        showLoading(false);
    }
}

function logout() {
    currentCustomer = null;
    customerAccounts = [];
    checkingAccount = null;
    savingsAccount = null;
    currentAccountType = 'CHECKING';
    
    // Voltar para a tela de login
    showLoginScreen();
    
    sessionStorage.removeItem('currentCustomer');
    showNotification('Logout realizado com sucesso! 👋', 'success');
}

// ===== CONFIGURAÇÃO DOS SELETORES DE TIPO DE CONTA =====
function setupAccountTypeSelectors() {
    const accountTypeOptions = document.querySelectorAll('.account-type-option');
    const radioInputs = document.querySelectorAll('input[name="accountType"]');
    
    // Adicionar classe selected ao primeiro item por padrão
    if (accountTypeOptions.length > 0) {
        accountTypeOptions[0].classList.add('selected');
    }
    
    accountTypeOptions.forEach(option => {
        option.addEventListener('click', function() {
            // Remover selected de todos
            accountTypeOptions.forEach(opt => opt.classList.remove('selected'));
            
            // Adicionar selected ao clicado
            this.classList.add('selected');
            
            // Marcar o radio button correspondente
            const accountType = this.dataset.type;
            const radioInput = document.querySelector(`input[value="${accountType}"]`);
            if (radioInput) {
                radioInput.checked = true;
            }
        });
    });
    
    // Também permitir que o clique no radio button mude a seleção
    radioInputs.forEach(radio => {
        radio.addEventListener('change', function() {
            if (this.checked) {
                accountTypeOptions.forEach(opt => opt.classList.remove('selected'));
                const correspondingOption = document.querySelector(`.account-type-option[data-type="${this.value}"]`);
                if (correspondingOption) {
                    correspondingOption.classList.add('selected');
                }
            }
        });
    });
}

// ===== INICIALIZAÇÃO =====
document.addEventListener('DOMContentLoaded', () => {
    // Verificar se há um cliente logado (para desenvolvimento)
    const savedCustomer = sessionStorage.getItem('currentCustomer');
    if (savedCustomer) {
        login(savedCustomer);
    } else {
        showLoginScreen();
    }
    
    // Configurar seletores de tipo de conta
    setupAccountTypeSelectors();
    
    // Adicionar eventos de teclado para melhor UX
    document.getElementById('customerId').addEventListener('keypress', (e) => {
        if (e.key === 'Enter') {
            document.getElementById('loginForm').dispatchEvent(new Event('submit'));
        }
    });
    
    document.getElementById('newCustomerId').addEventListener('keypress', (e) => {
        if (e.key === 'Enter') {
            document.getElementById('createAccountForm').dispatchEvent(new Event('submit'));
        }
    });
});

// Salvar cliente atual na sessão (para desenvolvimento)
window.addEventListener('beforeunload', () => {
    if (currentCustomer) {
        sessionStorage.setItem('currentCustomer', currentCustomer);
    }
});
function showSection(sectionName) {
    // Remover active de todas as seções
    document.querySelectorAll('.section').forEach(section => {
        section.classList.remove('active');
    });
    
    // Remover active de todos os nav items
    document.querySelectorAll('.nav-item').forEach(item => {
        item.classList.remove('active');
    });
    
    // Ativar seção e nav item
    document.getElementById(sectionName).classList.add('active');
    document.querySelector(`[onclick="showSection('${sectionName}')"]`).classList.add('active');
    
    // Carregar dados específicos da seção
    switch(sectionName) {
        case 'dashboard':
            if (currentCustomer) {
                loadDashboard();
            }
            break;
        case 'transfer':
            if (currentCustomer) {
                loadTransferForm();
            }
            break;
        case 'deposit':
            if (currentCustomer) {
                loadDepositForm();
            }
            break;
        case 'statement':
            if (currentCustomer) {
                loadStatement();
            }
            break;
        case 'accounts':
            if (currentCustomer) {
                loadAccountsDetails();
            }
            break;
    }
}

// ===== DASHBOARD =====
async function loadDashboard() {
    // Verificar se há um cliente logado
    if (!currentCustomer) {
        console.log('Nenhum cliente logado, não é possível carregar dashboard');
        return;
    }
    
    try {
        // Atualizar contas do cliente
        customerAccounts = await makeAPIRequest(`/accounts?customerId=${currentCustomer}`);
        
        // Separar contas por tipo
        checkingAccount = customerAccounts.find(acc => acc.accountType === 'CHECKING');
        savingsAccount = customerAccounts.find(acc => acc.accountType === 'SAVINGS');
        
        // Calcular saldo total
        const totalBalance = customerAccounts.reduce((sum, account) => {
            return sum + parseFloat(account.balance || 0);
        }, 0);
        
        document.getElementById('totalBalance').textContent = formatCurrency(totalBalance);
        
        // Mostrar overview das contas
        loadAccountsOverview();
        
        // Carregar transações recentes
        loadRecentTransactions();
        
    } catch (error) {
        showNotification(`Erro ao carregar dashboard: ${error.message}`, 'error');
    }
}

function createAccountCard(account) {
    const card = document.createElement('div');
    card.className = 'account-card';
    
    const statusClass = account.status === 'ACTIVATED' ? 'active' : 'canceled';
    const statusText = account.status === 'ACTIVATED' ? 'Ativa' : 'Cancelada';
    const accountTypeText = account.accountType === 'CHECKING' ? 'Conta Corrente' : 'Poupança';
    
    card.innerHTML = `
        <div class="account-header">
            <span class="account-type">${accountTypeText}</span>
            <span class="account-status ${statusClass}">${statusText}</span>
        </div>
        <div class="account-number">
            Agência: ${account.agency} | Conta: ${account.number}-${account.digit}
        </div>
        <div class="account-balance">${formatCurrency(account.balance || 0)}</div>
    `;
    
    return card;
}

async function refreshBalance() {
    if (!currentCustomer) {
        showNotification('Faça login para atualizar o saldo', 'error');
        return;
    }
    
    await loadDashboard();
    showNotification('Saldo atualizado!', 'success');
}

// ===== TRANSFERÊNCIA =====
async function loadTransferForm() {
    if (!currentCustomer) {
        console.log('Nenhum cliente logado, não é possível carregar formulário de transferência');
        return;
    }
    
    const fromAccountSelect = document.getElementById('fromAccountSelect');
    fromAccountSelect.innerHTML = '<option value="">Selecione a conta de origem</option>';
    
    // Carregar apenas contas ativas
    const activeAccounts = customerAccounts.filter(account => account.status === 'ACTIVATED');
    activeAccounts.forEach(account => {
        const option = document.createElement('option');
        option.value = account.id;
        const accountType = account.accountType === 'CHECKING' ? 'Corrente' : 'Poupança';
        option.textContent = `${accountType} - ${account.number}-${account.digit} (${formatCurrency(account.balance)})`;
        fromAccountSelect.appendChild(option);
    });
}

document.getElementById('transferForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const fromAccountId = document.getElementById('fromAccountSelect').value;
    const toAccountId = document.getElementById('toAccountId').value;
    const amount = document.getElementById('transferAmount').value;
    
    if (!fromAccountId || !toAccountId || !amount) {
        showNotification('Preencha todos os campos', 'error');
        return;
    }
    
    try {
        showLoading(true);
        
        await makeAPIRequest('/transactions', {
            method: 'POST',
            body: JSON.stringify({
                fromAccountId: parseInt(fromAccountId),
                toAccountId: parseInt(toAccountId),
                amount: parseFloat(amount)
            })
        });
        
        showNotification('Transferência realizada com sucesso!', 'success');
        document.getElementById('transferForm').reset();
        
        // Atualizar dashboard
        await loadDashboard();
        
    } catch (error) {
        showNotification(`Erro na transferência: ${error.message}`, 'error');
    } finally {
        showLoading(false);
    }
});

// ===== DEPÓSITO =====
async function loadDepositForm() {
    if (!currentCustomer) {
        console.log('Nenhum cliente logado, não é possível carregar formulário de depósito');
        return;
    }
    
    // Configurar seletores de tipo de conta para depósito
    setupDepositAccountSelectors();
    
    // Marcar conta corrente como selecionada por padrão
    const checkingOption = document.querySelector('.deposit-account-selector .account-type-option[data-account-type="CHECKING"]');
    if (checkingOption) {
        checkingOption.classList.add('selected');
    }
}

function setupDepositAccountSelectors() {
    const depositAccountOptions = document.querySelectorAll('.deposit-account-selector .account-type-option');
    const depositRadioInputs = document.querySelectorAll('input[name="depositAccountType"]');
    
    depositAccountOptions.forEach(option => {
        option.addEventListener('click', function() {
            // Remover selected de todos
            depositAccountOptions.forEach(opt => opt.classList.remove('selected'));
            
            // Adicionar selected ao clicado
            this.classList.add('selected');
            
            // Marcar o radio button correspondente
            const accountType = this.dataset.accountType;
            const radioInput = document.querySelector(`input[name="depositAccountType"][value="${accountType}"]`);
            if (radioInput) {
                radioInput.checked = true;
            }
        });
    });
    
    // Também permitir que o clique no radio button mude a seleção
    depositRadioInputs.forEach(radio => {
        radio.addEventListener('change', function() {
            if (this.checked) {
                depositAccountOptions.forEach(opt => opt.classList.remove('selected'));
                const correspondingOption = document.querySelector(`.deposit-account-selector .account-type-option[data-account-type="${this.value}"]`);
                if (correspondingOption) {
                    correspondingOption.classList.add('selected');
                }
            }
        });
    });
}

document.getElementById('depositForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const amount = document.getElementById('depositAmount').value;
    const accountType = document.querySelector('input[name="depositAccountType"]:checked')?.value;
    
    if (!amount || !accountType) {
        showNotification('Preencha todos os campos', 'error');
        return;
    }
    
    if (parseFloat(amount) <= 0) {
        showNotification('O valor deve ser maior que zero', 'error');
        return;
    }
    
    try {
        // Mostrar loading no botão
        const submitBtn = document.querySelector('#depositForm button[type="submit"]');
        const buttonText = submitBtn.querySelector('span');
        const loadingSpinner = submitBtn.querySelector('.loading-spinner');
        
        buttonText.style.display = 'none';
        loadingSpinner.style.display = 'flex';
        submitBtn.disabled = true;
        
        // Encontrar a conta do tipo selecionado
        const targetAccount = accountType === 'CHECKING' ? checkingAccount : savingsAccount;
        
        if (!targetAccount) {
            throw new Error(`Você não possui uma conta ${accountType === 'CHECKING' ? 'corrente' : 'poupança'}`);
        }
        
        // Fazer o depósito
        await makeAPIRequest('/accounts/deposit', {
            method: 'PUT',
            body: JSON.stringify({
                amount: parseFloat(amount),
                account: {
                    number: targetAccount.number,
                    digit: targetAccount.digit,
                    agency: targetAccount.agency
                }
            })
        });
        
        const accountTypeName = accountType === 'CHECKING' ? 'Conta Corrente' : 'Conta Poupança';
        showNotification(`Depósito de R$ ${parseFloat(amount).toLocaleString('pt-BR', {minimumFractionDigits: 2})} realizado na ${accountTypeName}!`, 'success');
        
        // Limpar formulário
        document.getElementById('depositForm').reset();
        
        // Reselecionar conta corrente como padrão
        document.querySelector('input[name="depositAccountType"][value="CHECKING"]').checked = true;
        document.querySelector('.deposit-account-selector .account-type-option[data-account-type="CHECKING"]').classList.add('selected');
        document.querySelector('.deposit-account-selector .account-type-option[data-account-type="SAVINGS"]').classList.remove('selected');
        
        // Atualizar dashboard
        await loadDashboard();
        
    } catch (error) {
        showNotification(`Erro no depósito: ${error.message}`, 'error');
    } finally {
        // Restaurar botão
        const submitBtn = document.querySelector('#depositForm button[type="submit"]');
        const buttonText = submitBtn.querySelector('span');
        const loadingSpinner = submitBtn.querySelector('.loading-spinner');
        
        buttonText.style.display = 'block';
        loadingSpinner.style.display = 'none';
        submitBtn.disabled = false;
    }
});

// ===== EXTRATO =====
async function loadStatement() {
    if (!currentCustomer) {
        console.log('Nenhum cliente logado, não é possível carregar extrato');
        showNotification('Faça login para ver o extrato', 'error');
        return;
    }
    
    try {
        showLoading(true);
        
        const transactions = await makeAPIRequest(`/transactions?customerId=${currentCustomer}`);
        const statementContent = document.getElementById('statementContent');
        
        if (transactions && transactions.length > 0) {
            statementContent.innerHTML = formatStatement(transactions);
        } else {
            statementContent.innerHTML = `
                <div class="statement-content">
                    <div class="no-transactions">
                        Nenhuma transação encontrada para este cliente.
                    </div>
                </div>
            `;
        }
        
    } catch (error) {
        showNotification(`Erro ao carregar extrato: ${error.message}`, 'error');
        document.getElementById('statementContent').innerHTML = `
            <div class="statement-content">
                <div class="no-transactions">
                    Erro ao carregar extrato.
                </div>
            </div>
        `;
    } finally {
        showLoading(false);
    }
}

function formatStatement(transactions) {
    let html = `
        <div class="statement-header">
            <h4>📄 Extrato Bancário - Alice Bank</h4>
            <p>Gerado em: ${new Date().toLocaleString('pt-BR')}</p>
            <p>Total de transações: ${transactions.length}</p>
        </div>
    `;
    
    html += '<div class="transactions-list">';
    transactions.forEach((transaction, index) => {
        const isCredit = transaction.type === 'CREDIT' || transaction.type === 'DEPOSIT';
        const amountClass = isCredit ? 'amount-credit' : 'amount-debit';
        const icon = getTransactionIcon(transaction.type);
        
        html += `
            <div class="transaction-item">
                <div class="transaction-header">
                    <span class="transaction-icon">${icon}</span>
                    <span class="transaction-type">${transaction.type || transaction.financialTransactionType}</span>
                    <span class="transaction-date">${formatDate(transaction.createdAt || transaction.timestamp)}</span>
                </div>
                <div class="transaction-description">${getTransactionDescription(transaction)}</div>
                <div class="transaction-amounts">
                    <span class="transaction-amount ${amountClass}">
                        ${isCredit ? '+' : ''}${formatCurrency(Math.abs(transaction.amount))}
                    </span>
                    <span class="transaction-balance">Saldo: ${formatCurrency(transaction.balance || 0)}</span>
                </div>
            </div>
        `;
    });
    html += '</div>';
    
    return html;
}

// ===== CONTAS =====
async function loadAccountsDetails() {
    if (!currentCustomer) {
        console.log('Nenhum cliente logado, não é possível carregar detalhes das contas');
        showNotification('Faça login para ver as contas', 'error');
        return;
    }
    
    const accountsList = document.getElementById('accountsList');
    accountsList.innerHTML = '';
    
    if (customerAccounts.length > 0) {
        customerAccounts.forEach(account => {
            const accountCard = createDetailedAccountCard(account);
            accountsList.appendChild(accountCard);
        });
    } else {
        accountsList.innerHTML = '<div class="no-accounts">Nenhuma conta encontrada.</div>';
    }
}

function createDetailedAccountCard(account) {
    const card = document.createElement('div');
    card.className = 'account-card';
    
    const statusClass = account.status === 'ACTIVATED' ? 'active' : 'canceled';
    const statusText = account.status === 'ACTIVATED' ? 'Ativa' : 'Cancelada';
    const accountTypeText = account.accountType === 'CHECKING' ? 'Conta Corrente' : 'Conta Poupança';
    
    card.innerHTML = `
        <div class="account-header">
            <span class="account-type">${accountTypeText}</span>
            <span class="account-status ${statusClass}">${statusText}</span>
        </div>
        <div class="account-details">
            <p><strong>ID:</strong> ${account.id}</p>
            <p><strong>Agência:</strong> ${account.agency}</p>
            <p><strong>Conta:</strong> ${account.number}-${account.digit}</p>
            <p><strong>Cliente:</strong> ${account.customerId}</p>
        </div>
        <div class="account-balance">${formatCurrency(account.balance || 0)}</div>
        ${account.status === 'ACTIVATED' ? `
            <div class="account-actions">
                <button class="secondary-btn" onclick="cancelAccount(${account.customerId})">
                    Cancelar Contas
                </button>
            </div>
        ` : ''}
    `;
    
    return card;
}

async function createNewAccounts() {
    try {
        showLoading(true);
        
        await makeAPIRequest('/accounts', {
            method: 'POST',
            body: JSON.stringify({ customerId: parseInt(currentCustomer) })
        });
        
        showNotification('Novas contas criadas com sucesso!', 'success');
        await loadDashboard();
        await loadAccountsDetails();
        
    } catch (error) {
        showNotification(`Erro ao criar contas: ${error.message}`, 'error');
    } finally {
        showLoading(false);
    }
}

async function cancelAccount(customerId) {
    const confirm = window.confirm('Tem certeza que deseja cancelar todas as contas deste cliente?');
    if (!confirm) return;
    
    try {
        showLoading(true);
        
        await makeAPIRequest('/accounts/cancel', {
            method: 'PATCH',
            body: JSON.stringify({ customerId: parseInt(customerId) })
        });
        
        showNotification('Contas canceladas com sucesso!', 'success');
        await loadDashboard();
        await loadAccountsDetails();
        
    } catch (error) {
        showNotification(`Erro ao cancelar contas: ${error.message}`, 'error');
    } finally {
        showLoading(false);
    }
}

// ===== UTILITÁRIOS =====
async function makeAPIRequest(url, options = {}) {
    try {
        const response = await fetch(`${API_BASE_URL}${url}`, {
            headers: {
                'Content-Type': 'application/json',
                ...options.headers
            },
            ...options
        });

        if (!response.ok) {
            const errorText = await response.text();
            let errorData;
            try {
                errorData = JSON.parse(errorText);
            } catch (e) {
                errorData = { message: errorText };
            }
            
            throw new Error(errorData?.message || `Erro ${response.status}: ${response.statusText}`);
        }
        
        // Se a resposta está vazia (204 No Content), retorna null
        if (response.status === 204) {
            return null;
        }
        
        return await response.json();
        
    } catch (error) {
        // Melhorar mensagens de erro específicas
        if (error.name === 'TypeError' && error.message.includes('fetch')) {
            throw new Error('Não foi possível conectar com o servidor. Verifique se a API está rodando em http://localhost:8081');
        }
        
        throw error;
    }
}

function formatCurrency(amount) {
    return new Intl.NumberFormat('pt-BR', {
        style: 'currency',
        currency: 'BRL'
    }).format(amount);
}

function showLoading(show) {
    loadingOverlay.style.display = show ? 'flex' : 'none';
}

function showNotification(message, type = 'success') {
    notificationMessage.textContent = message;
    notification.className = `notification ${type === 'error' ? 'error' : ''}`;
    notification.style.display = 'block';
    
    setTimeout(() => {
        notification.style.display = 'none';
    }, 4000);
}
