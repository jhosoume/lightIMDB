# Light IMDB
 
Uma implementação de um sistema web para 
avaliar filmes (ou outros trabalhos artísticos). 

### Requisitos

   * instalar o MySQL e setar a senha do usuário root como *root* 
   * instalar o play-framwork (na essência, instalar o sbt... consulte o site) 

### Executar o projeto
   * clonar esse repositório via o git 
   * criar o banco de dados imdb no mysql 
   * definir o esquema do banco de dados (verificar o script em conf/evolutions/1.sql)
   * executar o sbt (isso no diretório raiz do projeto) 
   * no shell do sbt, executar "run" 
   * acessar o projeto em http://localhost:9000  
 

### Objetivo
    * Mecanismo de persistência (Sistema de Arquivos, SGBD (sistema gerenciador de banco de dados))
    * Arquitetura MVC e Desenvolvimento de Aplicações Web

    -> POC (Proof of Concept)
        ** Features implementadas
            - cadastrar novo filme
            - listar filmes cadastrados

### TODO
    * Features
        -> Login (change structure of the db)
        -> Lista de Filmes (@ if session.isLogged shows more information (evaluate)) USER SHOULD BE IN SESSION (saves in a cookie)
        -> Computar a média das notas do filme -> SELECT Titulo AVG(Nota) FROM Filme INNER JOIN Avaliacao WHERE Filme.id = Avaliacao.FK_Filme GROUP BY Titulo
