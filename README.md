# Sistema de Controle de Biblioteca

## Nome do projeto

**Sistema de Controle de Biblioteca**

## Integrantes

- **Rafael Melo da Silva**
- **Luiz Henrique Alvarenga**

## Tema escolhido

Sistema corporativo desktop para **gestão de uma biblioteca**, desenvolvido em **Java 21**
com interface gráfica em **JavaFX 21** e persistência em banco de dados **MariaDB/MySQL**
via **JDBC**. A aplicação é dividida nas camadas **Fronteira (boundary)**, **Controle
(controller)**, **Entidade (entity)** e **DAO**.

## Descrição do problema resolvido

Uma biblioteca precisa controlar seu acervo e a circulação dos livros entre os leitores.
Feito manualmente, esse controle gera retrabalho, dados inconsistentes e dificuldade para
saber quais exemplares estão disponíveis ou emprestados.

O sistema resolve isso oferecendo o cadastro completo de **autores**, **livros** e
**leitores**, e o registro de **empréstimos** com vários livros por empréstimo. Cada
operação valida os dados antes de gravar (com mensagens específicas por campo) e tudo é
persistido em banco de dados. O controle de **estoque** é automático: ao registrar um
empréstimo os exemplares são baixados e, ao removê-lo, retornam ao acervo — sempre dentro
de uma **transação**, para manter o banco consistente.

## Entidades implementadas

| Entidade | Campos | Observação |
|----------|--------|------------|
| **Autor** | código, nome, nacionalidade, data de nascimento, e-mail, telefone | — |
| **Livro** | código, título, editora, ano, ISBN, quantidade, autor | Referencia um `Autor` |
| **Leitor** | código, CPF, nome, telefone, e-mail, CEP, data de cadastro | — |
| **Empréstimo** | código, leitor, lista de livros, data do empréstimo, data prevista de devolução, status | Mestre-detalhe |
| **ItemEmprestimo** | empréstimo, livro, data de devolução, status | Item de cada empréstimo |
| **StatusItem** (enum) | PENDENTE, DEVOLVIDO, ATRASADO | Status do item emprestado |

Cada CRUD (**Autor, Livro, Leitor, Empréstimo**) permite **Cadastrar, Consultar, Alterar e
Remover**, exibindo os registros em uma **`TableView`**.

## Instruções para execução

### Pré-requisitos
- **JDK 21** instalado.
- **MariaDB** ou **MySQL** em execução em `localhost:3306`.

### Passos
1. **Criar as tabelas** (uma única vez) — o script está em [`database.sql`](database.sql):
   ```bash
   mysql -u root -p < database.sql
   ```
   > O banco `biblioteca_db` é criado automaticamente pela aplicação
   > (`createDatabaseIfNotExist=true`), mas as **tabelas** vêm do `database.sql`.

2. **Configurar as credenciais** do banco na classe
   [`Conexao`](src/main/java/edu/curso/DAO/Conexao.java) — ajuste `USUARIO`, `SENHA` e, se
   necessário, `PORTA`.

3. **Executar** (o plugin JavaFX do Gradle baixa as dependências gráficas):
   ```bash
   ./gradlew run
   ```
   Para apenas compilar: `./gradlew build`.

A aplicação abre no menu **Cadastro**, de onde se acessa as telas de Livros, Leitores,
Empréstimos e Autores.

## Divisão de responsabilidades por integrante

| Integrante                  | CRUDs sob responsabilidade |
|-----------------------------|----------------------------|
| **Rafael Melo da Silva**    | **Empréstimo** e **Autor** |
| **Luiz Henrique Alvarenga** | **Livro** e **Leitor** |

Cada integrante implementou, para os seus CRUDs, as quatro camadas correspondentes:
entidade (`entity`), tela (`boundary`), controlador (`controller`) e persistência (`DAO`).

## Link do vídeo

▶️ **YouTube:** _https://www.youtube.com/watch?v=7TjrU9PwRos_

---

### Tecnologias e arquitetura (resumo)

| Item | Definição |
|------|-----------|
| Linguagem | Java 21 |
| Interface | JavaFX 21 (`javafx.controls`, `javafx.fxml`) |
| Banco de dados | MariaDB / MySQL (`biblioteca_db`) |
| Conexão | JDBC — driver `mariadb-java-client` |
| Build | Gradle (`gradlew` / `build.gradle.kts`) |
| Classe principal | `edu.curso.App` → `TelaMenuPrincipal` |

```
edu.curso
├── boundary    → telas JavaFX (TableView + formulários)
├── controller  → properties, validações e orquestração do DAO
├── entity      → modelos de domínio (Autor, Livro, Leitor, Emprestimo, ItemEmprestimo)
└── DAO         → persistência via JDBC (interfaces + implementações + Conexao)
```
