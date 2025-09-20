\# 🚀 Gerenciador de Projetos Simplificado



\## 📝 Descrição



Este é um projeto acadêmico/de portfólio desenvolvido em Java para demonstrar os conceitos fundamentais de Programação Orientada a Objetos (POO), arquitetura MVC, desenvolvimento de interface gráfica com Swing e persistência de dados com banco de dados relacional (SQLite) via JDBC.



A aplicação é um sistema de desktop simples para gerenciar usuários, projetos e equipes, permitindo operações de CRUD (Create, Read, Update, Delete) para todas as entidades principais.



---



\## ✨ Funcionalidades



\-   \[x] \*\*Gerenciamento de Usuários:\*\* Cadastro, Edição, Exclusão e Listagem.

\-   \[x] \*\*Gerenciamento de Projetos:\*\* Cadastro, Edição e Exclusão, com campos de nome, descrição e datas.

\-   \[x] \*\*Gerenciamento de Equipes:\*\* Cadastro, Edição e Exclusão.

\-   \[x] \*\*Relacionamentos:\*\*

&nbsp;   -   Associação de um Gerente (Usuário) a um Projeto.

&nbsp;   -   Associação de múltiplos Membros (Usuários) a uma Equipe.

\-   \[x] \*\*Persistência de Dados:\*\* Todas as informações são salvas em um banco de dados SQLite local.

\-   \[x] \*\*Interface Gráfica:\*\* Interface intuitiva com abas para cada entidade e botões de ação contextuais.



---



\## 🛠️ Tecnologias Utilizadas



\-   \*\*Linguagem:\*\* Java (JDK 11+)

\-   \*\*Interface Gráfica (GUI):\*\* Java Swing

\-   \*\*Banco de Dados:\*\* SQLite

\-   \*\*Conectividade com BD:\*\* JDBC (SQLite JDBC Driver)

\-   \*\*IDE:\*\* Apache NetBeans

\-   \*\*Controle de Versão:\*\* Git \& GitHub



---



\## 🎓 Principais Conceitos Demonstrados



\-   \*\*Programação Orientada a Objetos (POO):\*\*

&nbsp;   -   \*\*Encapsulamento:\*\* Atributos privados com acesso via getters e setters.

&nbsp;   -   \*\*Herança:\*\* Classe `Usuario` herda de `Pessoa`.

&nbsp;   -   \*\*Polimorfismo:\*\* Sobrescrita do método `toString()` nos modelos para exibição na GUI.

\-   \*\*Arquitetura de Software:\*\*

&nbsp;   -   Padrão \*\*MVC (Model-View-Controller)\*\* para separação clara de responsabilidades (dados, interface e controle).

&nbsp;   -   Padrão \*\*DAO (Data Access Object)\*\* para isolar a lógica de acesso ao banco de dados.

\-   \*\*Persistência de Dados:\*\*

&nbsp;   -   Conexão com banco de dados relacional via \*\*JDBC\*\*.

&nbsp;   -   Execução de operações \*\*CRUD\*\* com SQL (`INSERT`, `SELECT`, `UPDATE`, `DELETE`).

&nbsp;   -   Uso de `PreparedStatement` para segurança contra SQL Injection.

&nbsp;   -   Gerenciamento de \*\*Transações\*\* para operações complexas (ex: exclusão de equipe e seus membros).

\-   \*\*Desenvolvimento de GUI:\*\*

&nbsp;   -   Construção de interface gráfica com componentes \*\*Swing\*\* (`JFrame`, `JTable`, `JButton`, `JTabbedPane`).

&nbsp;   -   Modelo de programação \*\*orientado a eventos\*\* com `ActionListeners` e `ListSelectionListeners`.



---



\## ⚙️ Como Executar o Projeto



1\.  \*\*Pré-requisitos:\*\*

&nbsp;   -   Ter o \[JDK (Java Development Kit)](https://www.oracle.com/java/technologies/downloads/) versão 11 ou superior instalado.

&nbsp;   -   Ter o \[Git](https://git-scm.com/) instalado.



2\.  \*\*Clonar o Repositório:\*\*

&nbsp;   ```bash



&nbsp;   git clone https://github.com/luis-souzaweb/A3-Project.git



3\.  \*\*Configurar no NetBeans:\*\*

&nbsp;   -   Abra o NetBeans e vá em `Arquivo > Abrir Projeto...` e selecione a pasta que você clonou.

&nbsp;   -   O projeto precisa do driver JDBC do SQLite. Se a pasta `lib` não for reconhecida automaticamente, clique com o botão direito em "Bibliotecas" no projeto, vá em "Adicionar JAR/Pasta..." e adicione o arquivo `sqlite-jdbc-x.x.x.jar`.



4\.  \*\*Banco de Dados:\*\*

&nbsp;   -   O banco de dados (`gerenciador\_projetos.db`) é criado automaticamente na pasta raiz do projeto na primeira vez que você executar a aplicação.



5\.  \*\*Executar:\*\*

&nbsp;   -   Apenas clique com o botão direito no projeto e escolha "Executar" (ou pressione F6).



---





