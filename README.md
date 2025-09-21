# Gerenciador de Projetos

**Status do Projeto:** Concluído

## Descrição

Este é um projeto de aplicação desktop desenvolvido em Java para demonstrar os conceitos fundamentais de Programação Orientada a Objetos (POO), arquitetura MVC, desenvolvimento de interface gráfica com Swing e persistência de dados com banco de dados relacional (SQLite) via JDBC.

A aplicação permite o gerenciamento completo de Usuários, Projetos e Equipes, com operações de CRUD (Criar, Ler, Atualizar, Excluir), validação de dados e busca em tempo real.

## Funcionalidades

-   [x] **CRUD Completo:** Cadastro, Edição, Exclusão e Listagem para Usuários, Projetos e Equipes.
-   **Validação de Dados:** Impede a entrada de dados inválidos (campos em branco, CPFs/E-mails duplicados).
-   **Máscaras de Entrada:** Formatação automática para campos de CPF e Data.
-   **Busca em Tempo Real:** Filtro dinâmico das tabelas conforme o usuário digita.
-   **Relacionamentos:**
    -   Associação de um Gerente (Usuário) a um Projeto.
    -   Associação de Membros (Usuários) a uma Equipe, com visualização.
-   **Persistência de Dados:** Todas as informações são salvas em um banco de dados SQLite local.

## Tecnologias Utilizadas

-   **Linguagem:** Java (JDK 11+)
-   **Interface Gráfica (GUI):** Java Swing
-   **Banco de Dados:** SQLite
-   **Conectividade com BD:** JDBC
-   **IDE:** Apache NetBeans
-   **Controle de Versão:** Git & GitHub

##  Como Executar

1.  **Pré-requisitos:** Ter o Java (JDK ou JRE) versão 11 ou superior instalado.
2.  **Executando o `.jar`:**
    -   Vá para a pasta `dist/` deste repositório.
    -   Execute o arquivo `GerenciadorProjetos.jar` (o nome pode variar). Geralmente, um duplo clique é o suficiente.
    -   Se não funcionar, execute via linha de comando: `java -jar GerenciadorProjetos.jar`
3.  **Executando o Código-Fonte:**
    -   Clone o repositório.
    -   Abra o projeto no NetBeans IDE.
    -   Adicione o driver JDBC do SQLite às bibliotecas do projeto.
    -   Delete o arquivo `.db` existente (se houver) para que um novo seja criado.
    -   Execute a classe principal.

##  Autores

-   **[Luis Souza]**
-   **[Vitória Mariana]**
-   **[Tiago Moraes]**