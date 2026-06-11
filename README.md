# Sistema de Controle de Biblioteca

Aplicação **desktop** para o controle de uma biblioteca: cadastro e gestão de **autores,
livros, leitores e empréstimos**, com validação dos dados e persistência em banco de
dados relacional via **JDBC**.

> Projeto acadêmico da disciplina de **Programação Orientada a Objetos (POO)**.

---

## Tecnologias

| Item | Definição |
|------|-----------|
| Linguagem | Java 21 |
| Interface gráfica | JavaFX 21 (`javafx.controls`, `javafx.fxml`) |
| Banco de dados | MariaDB / MySQL (`biblioteca_db`) |
| Conexão | JDBC — driver `mariadb-java-client` |
| Arquitetura | Camadas: Fronteira (boundary), Controle (controller), Entidade (entity) e DAO |
| Build | Gradle (`gradlew` / `build.gradle.kts`) |
| Classe principal | `edu.curso.App` → abre `TelaMenuPrincipal` |

## Arquitetura em camadas

```
edu.curso
├── App                 → ponto de entrada (Application.launch)
├── boundary    → Fronteira: telas JavaFX (TableView + formulários)
│   ├── Tela (interface)
│   ├── TelaMenuPrincipal  (menu e troca de telas)
│   ├── TelaAutor / TelaLivro / TelaLeitor / TelaEmprestimo
├── controller  → Controle: properties JavaFX, validações e orquestração do DAO
│   └── AutorController / LivroController / LeitorController / EmprestimoController
├── entity      → Entidade: modelos de domínio
│   ├── Autor / Livro / Leitor / Emprestimo / ItemEmprestimo
│   └── StatusItem (enum: PENDENTE, DEVOLVIDO, ATRASADO)
└── DAO         → Persistência via JDBC
    ├── Conexao  (abertura de conexão)
    ├── *DAO (interfaces) e *DAOImplementation (SQL)
```

**Fluxo:** a tela (boundary) faz *binding* dos campos com as `properties` do controller →
o controller valida e converte para a entidade → o DAO executa o SQL no banco e devolve as
entidades de volta para a `TableView`.

## CRUDs e entidades

Cada CRUD permite **Cadastrar, Consultar, Alterar e Remover**, listando os registros em
uma **`TableView`** e validando cada campo com mensagens específicas.

| Entidade | Campos |
|----------|--------|
| **Autor** | código, nome, nacionalidade, data de nascimento, e-mail, telefone |
| **Livro** | código, título, editora, ano, ISBN, quantidade, autor *(referência)* |
| **Leitor** | código, CPF, nome, telefone, e-mail, CEP, data de cadastro |
| **Empréstimo** | código, leitor, **lista de livros** (itens), data do empréstimo, data prevista de devolução, status |

O **Empréstimo** é mestre-detalhe: um empréstimo possui vários `ItemEmprestimo` (um por
livro), cada um com seu próprio status de devolução. Ao registrar um empréstimo o estoque
do livro é baixado; ao apagar, os exemplares retornam ao estoque — tudo em **transação**.

## Banco de dados

O esquema completo está em [`database.sql`](database.sql), com 5 tabelas:

```
autor ──< livro          (livro.codigo_autor → autor.codigo)
leitor ──< emprestimo     (emprestimo.codigo_leitor → leitor.codigo)
emprestimo ──< item_emprestimo >── livro   (tabela associativa)
```

> A `Conexao` usa `createDatabaseIfNotExist=true`, ou seja, o **banco** é criado
> automaticamente — mas as **tabelas** não. É preciso rodar o `database.sql` uma vez.

## Pré-requisitos

- **JDK 21** instalado (o Gradle também tenta provisionar via toolchain).
- **MariaDB** ou **MySQL** em execução em `localhost:3306`.

## Como executar

1. **Criar as tabelas** (uma única vez):
   ```bash
   mysql -u root -p < database.sql
   ```

2. **Configurar as credenciais** na classe
   [`Conexao`](src/main/java/edu/curso/DAO/Conexao.java) — ajuste `USUARIO`, `SENHA` e,
   se necessário, `PORTA` para os do seu banco local.

3. **Executar** (o plugin JavaFX do Gradle cuida das dependências gráficas):
   ```bash
   ./gradlew run
   ```

   Para apenas compilar:
   ```bash
   ./gradlew build
   ```

## Estrutura do projeto

```
Trabalho_POO/
├── build.gradle.kts          # dependências e plugins (JavaFX, application)
├── database.sql              # script de criação das tabelas
├── docs/                     # documentação do projeto
├── gradlew / gradlew.bat     # wrapper do Gradle
└── src/main/java/edu/curso/  # código-fonte (boundary, controller, entity, DAO)
```

## Documentação

Material complementar em [`docs/`](docs/):

| Documento | Conteúdo |
|-----------|----------|
| [01 - Visão Geral](docs/01-visao-geral.md) | Objetivos, escopo e glossário |
| [02 - Arquitetura](docs/02-arquitetura.md) | Camadas, responsabilidades e fluxo |
| [03 - Requisitos](docs/03-requisitos.md) | Requisitos funcionais e não funcionais |
| [04 - Modelo de Dados](docs/04-modelo-de-dados.md) | Entidades, dicionário de dados e relacionamentos |
| [05 - Banco de Dados](docs/05-banco-de-dados.md) | Tabelas e notas de modelagem |
| [07 - Casos de Uso](docs/07-casos-de-uso.md) | Atores e fluxos das operações |
| [08 - Plano de Implementação](docs/08-plano-de-implementacao.md) | Etapas e classes |

> Observação: os documentos em `docs/` registram o **planejamento**; em pontos de
> nomenclatura de colunas, o esquema **vigente** é o de [`database.sql`](database.sql).
