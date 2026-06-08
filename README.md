# Sistema de Controle de Biblioteca

Software corporativo desktop para o controle de uma biblioteca: cadastro e gestão de
livros, autores, leitores e empréstimos, com login, perfis de acesso e persistência em
banco de dados MySQL.

> Projeto acadêmico da disciplina de **Programação Orientada a Objetos (POO)**.

---

## Visão rápida

| Item | Definição |
|------|-----------|
| Linguagem | Java |
| Interface | Desktop (JavaFX ou Swing) |
| Banco de dados | MySQL (`biblioteca_db`) |
| Conexão | JDBC |
| Arquitetura | Camadas: Fronteira, Controle, Entidade, DAO e Segurança |
| Perfis de acesso | Administrador e Atendente |
| Build | Gradle (`gradlew`) |

## Funcionalidades (CRUDs)

1. **Livro** — código, título, editora, ano, ISBN, quantidade, autor.
2. **Autor** — código, nome, nacionalidade, data de nascimento, e-mail, telefone.
3. **Leitor** — código, nome, CPF, telefone, e-mail, endereço, data de cadastro.
4. **Empréstimo** — código, leitor, livro, datas (empréstimo/prevista/devolução), status.

Cada CRUD oferece: **Cadastrar, Consultar, Alterar, Remover** e exibir em **TableView**.

## Mapa das camadas (pacotes)

```
edu.curso
├── boundary    → Fronteira (telas)
├── controller  → Controle (regras e validações)
├── entity      → Entidade (modelos de domínio)
├── DAO         → Persistência via JDBC
└── security    → Segurança (login e perfis)
```

## Documentação

A documentação completa está em [`docs/`](docs/):

| Documento | Conteúdo |
|-----------|----------|
| [01 - Visão Geral](docs/01-visao-geral.md) | Objetivos, escopo e glossário |
| [02 - Arquitetura](docs/02-arquitetura.md) | Camadas, responsabilidades e fluxo |
| [03 - Requisitos](docs/03-requisitos.md) | Requisitos funcionais e não funcionais |
| [04 - Modelo de Dados](docs/04-modelo-de-dados.md) | Entidades, dicionário de dados e relacionamentos |
| [05 - Banco de Dados](docs/05-banco-de-dados.md) | Tabelas e script de criação |
| [06 - Segurança e Perfis](docs/06-seguranca-e-perfis.md) | Login e matriz de permissões |
| [07 - Casos de Uso](docs/07-casos-de-uso.md) | Atores e fluxos das operações |
| [08 - Plano de Implementação](docs/08-plano-de-implementacao.md) | Etapas sugeridas e classes |

## Como executar (após implementação)

```bash
# Compilar
./gradlew build

# Executar
./gradlew run
```

> A configuração de conexão com o MySQL deve ser ajustada na classe `Conexao` da
> camada DAO antes da execução. Detalhes em [docs/05-banco-de-dados.md](docs/05-banco-de-dados.md).
