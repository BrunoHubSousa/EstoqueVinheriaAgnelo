package br.com.fiap.agneloestoque.estoque

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import br.com.fiap.agneloestoque.database.repository.ProdutoRepository
import br.com.fiap.agneloestoque.model.Produto

@Composable
fun ProdutoScreen() {
    val context = LocalContext.current
    val produtoRepository = ProdutoRepository(context)

    val nomeState = remember { mutableStateOf("") }
    val tipoState = remember { mutableStateOf("") }
    val safraState = remember { mutableIntStateOf(0) }
    val precoState = remember { mutableDoubleStateOf(0.0) }
    val quantidadeState = remember { mutableStateOf(0) }

    val listaProdutosState = remember { mutableStateOf(produtoRepository.listarProduto()) }
    val isUpdateMode = remember { mutableStateOf(false) }
    val produtoAtualState = remember { mutableStateOf<Produto?>(null) }

    Column {
        ProdutoForm(
            nome = nomeState.value,
            tipo = tipoState.value,
            safra = safraState.value,
            preco = precoState.value,
            quantidade = quantidadeState.value,
            onNomeChange = { nomeState.value = it },
            onTipoChange = { tipoState.value = it },
            onSafraChange = { safraState.value = it },
            onPrecoChange = { precoState.value = it },
            onQuantidadeChange = { quantidadeState.value = it },
            atualizar = { listaProdutosState.value = produtoRepository.listarProduto() },
            isUpdateMode = isUpdateMode,
            produtoAtual = produtoAtualState.value
        )
        ProdutoList(
            produtos = listaProdutosState.value,
            atualizar = { listaProdutosState.value = produtoRepository.listarProduto() },
            preencherFormulario = { produto ->
                nomeState.value = produto.nome
                tipoState.value = produto.tipo
                safraState.value = produto.safra
                precoState.value = produto.preco
                quantidadeState.value = produto.quantidade
                isUpdateMode.value = true
                produtoAtualState.value = produto
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProdutoForm(
    nome: String,
    tipo: String,
    safra: Int,
    preco: Double,
    quantidade: Int,
    onNomeChange: (String) -> Unit,
    onTipoChange: (String) -> Unit,
    onSafraChange: (Int) -> Unit,
    onPrecoChange: (Double) -> Unit,
    onQuantidadeChange: (Int) -> Unit,
    atualizar: () -> Unit,
    isUpdateMode: MutableState<Boolean>,
    produtoAtual: Produto?
) {
    val context = LocalContext.current
    val produtoRepository = ProdutoRepository(context)


    // Estados para mensagens de erro e sucesso
    val errorMessage = remember { mutableStateOf("") }
    val successMessage = remember { mutableStateOf("") }

    // Estado do menu suspenso
    val expanded = remember { mutableStateOf(false) }
    val tipos = listOf("Tinto", "Rose", "Espumante", "Seco")

    Column(
        modifier = Modifier
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text(
            text = if (isUpdateMode.value) "Atualizar Vinho" else "Cadastrar Vinho",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = nome,
            onValueChange = onNomeChange,
            label = { Text("Nome do Vinho") },
            modifier = Modifier.fillMaxWidth()
                .onFocusChanged {
                    if (it.isFocused) {
                        errorMessage.value = ""
                        successMessage.value = ""
                    }
                },
        )
        // Menu suspenso para o campo tipo
        ExposedDropdownMenuBox(
            expanded = expanded.value,
            onExpandedChange = { expanded.value = !expanded.value }
        ) {
            OutlinedTextField(
                value = tipo,
                onValueChange = {},
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .onFocusChanged {
                        if (it.isFocused) {
                            errorMessage.value = ""
                            successMessage.value = ""
                        }
                    },
                label = { Text(text = "Tipo do Vinho") },
                trailingIcon = {
                    IconButton(onClick = { expanded.value = !expanded.value }) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null
                        )
                    }
                },
                readOnly = true // Impede edição manual
            )
            ExposedDropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {
                tipos.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item) },
                        onClick = {
                            onTipoChange(item) // Atualiza o valor selecionado
                            expanded.value = false
                        }
                    )
                }
            }
        }


        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = safra.toString(),
            onValueChange = { onSafraChange(it.toIntOrNull() ?: 0) },
            label = { Text("Ano de Safra") },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    if (it.isFocused) {
                        errorMessage.value = ""
                        successMessage.value = ""
                    }
                },
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = preco.toString(),
            onValueChange = { onPrecoChange(it.toDoubleOrNull() ?: 0.0) },
            label = { Text("Preço R$") },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    if (it.isFocused) {
                        errorMessage.value = ""
                        successMessage.value = ""
                    }
                },
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = quantidade.toString(),
            onValueChange = { onQuantidadeChange(it.toIntOrNull() ?: 0) },
            label = { Text("Quantidade") },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    if (it.isFocused) {
                        errorMessage.value = ""
                        successMessage.value = ""
                    }
                },

            )
        Spacer(modifier = Modifier.height(16.dp))

        if (isUpdateMode.value) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(onClick = {
                    produtoAtual?.let { produto ->
                        val produtoAtualizado = produto.copy(
                            nome = nome,
                            tipo = tipo,
                            safra = safra,
                            preco = preco,
                            quantidade = quantidade
                        )
                        produtoRepository.atualizar(produtoAtualizado)
                        atualizar()
                        isUpdateMode.value = false
                    }
                }) {
                    Text("Atualizar")
                }
                Button(onClick = {
                    isUpdateMode.value = false
                    onNomeChange("")
                    onTipoChange("")
                    onSafraChange(0)
                    onPrecoChange(0.0)
                    onQuantidadeChange(0)
                }) {
                    Text("Cancelar")
                }
            }
        } else {

            // Exibir mensagem de erro
            if (errorMessage.value.isNotEmpty()) {
                Text(
                    text = errorMessage.value,
                    color = Color.Red,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // Exibir mensagem de sucesso
            if (successMessage.value.isNotEmpty()) {
                Text(
                    text = successMessage.value,
                    color = Color.Green,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Button(
                onClick = {
                    if (nome.isNotBlank() && tipo.isNotBlank() && preco > 0.0 && quantidade > 0) {
                        val produto = Produto(
                            nome = nome,
                            tipo = tipo,
                            safra = safra,
                            preco = preco,
                            quantidade = quantidade
                        )
                        produtoRepository.salvar(produto)
                        atualizar()
                        onNomeChange("")
                        onTipoChange("")
                        onSafraChange(0)
                        onPrecoChange(0.0)
                        onQuantidadeChange(0)


                        // Exibir mensagem de sucesso
                        successMessage.value = "Produto cadastrado com sucesso!"
                    } else {
                        errorMessage.value = "Por favor, preencha todos os campos corretamente."
                        successMessage.value = ""
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cadastrar")
            }
        }
    }
}


@Composable
fun ProdutoList(
    produtos: List<Produto>,
    atualizar: () -> Unit,
    preencherFormulario: (Produto) -> Unit
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .verticalScroll(rememberScrollState())
    ) {
        for (produto in produtos){
            ProdutoCard(produto, context = LocalContext.current, atualizar,preencherFormulario)
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}


@Composable
fun ProdutoCard(
    produto: Produto,
    context: Context,
    atualizar: () -> Unit,
    preencherFormulario: (Produto) -> Unit
) {
    val hexadecimalColor = "#D9D9D9" // Cor de fundo do card
    val color = Color(hexadecimalColor.toColorInt())

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(2f)
            ) {
                Text(text = produto.nome, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(text = produto.tipo, fontSize = 16.sp)
                Text(text = "Safra: ${produto.safra}", fontSize = 16.sp)
                Text(text = "Preço: R$ ${produto.preco}", fontSize = 16.sp)
                Text(text = "Quantidade: ${produto.quantidade}", fontSize = 16.sp)
            }
            IconButton(onClick = {
                preencherFormulario(produto) // Preenche o formulário para edição
            }) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar Produto")
            }
            IconButton(onClick = {
                val produtoRepository = ProdutoRepository(context)
                produtoRepository.excluir(produto)
                atualizar()
            }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Excluir Produto")
            }
        }
    }
}
