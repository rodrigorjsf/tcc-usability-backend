# Backend Whiskies

**Backend destinado ao projeto da eletiva de React da Universidade Católica de Pernambuco.**

```
:: GRUPO ::

Rodrigo França
Lucas Matheus
```

Projeto de desenvolvimento do módulo _backend_ da aplicação **Whiskies**, implementado em linguagem de programação Java, com o objetivo de prover as funcionalidades necessárias para o módulo _frontend_ implementado em linguagem de programação TypeScript utilizando a biblioteca React.

```
                    :: PROJETO WHISKIES ::

┌────────────┐       ┌───┬──────────┐       ┌──────────────┐
│  WHISKIES  │       │ R │ WHISKIES │       │              │
│  frontend  │ <===> │ E │ Backend  │ <===> │      BD      │
│  (React)   │       │ S │ (Java)   │       │ (PostgreSQL) │
│            │       │ T │          │       │              │
└────────────┘       └───┴──────────┘       └──────────────┘
```

Este projeto usa as seguintes tecnologias:
- [Java 11](https://www.oracle.com/technetwork/java/javase/downloads/index.html)
- [Sprint Boot](https://spring.io/projects/spring-boot)
- [Maven](https://maven.apache.org/)
- [Spring Data](https://spring.io/projects/spring-data)
     ->  [Java Persistence is the API(JPA)](https://docs.oracle.com/javaee/7/api/javax/persistence/package-summary.html)
     ->  [Hibernate](https://hibernate.org/)
     ->  [PostgreSQL](https://www.postgresql.org/)
- [Lombok](https://projectlombok.org/)
- [Auth0](https://auth0.com/)


## 1- Ferramentas e Configurações

### 1.1- Eclipse

**Instalação**

Baixar e descompactar um dos pacotes (Win ou Linux 64-bit) da versão `Eclipse IDE for Java Developers` que pode ser encontrado [aqui](https://www.eclipse.org/downloads/packages/).

**Importação do Projeto (Git)**

Após abrir o Eclipse, importar projeto do Git:

1. _File_ -> _Import..._ -> _Projects from Git_ -> _Clone URI_
2. URI: `git@github.com:rodrigorjsf/whiskies-backend-project.git` -> _Next_
3. Escolher _branch_ `master`
4. Local destination: _pasta-do-workspace_
5. _Import as general project_ -> _Next_ -> _Finish_

**Configuração do Projeto**

1. Clicar com botão direito no projeto -> _Configure_ -> _Convert to Maven project_
2. Clicar com botão direito no projeto -> _Maven_ -> _Update Project..._
3. OK (incluir opção `Force Update os Snapshots/Releases`)

Incluir `Run Configurations`:

1. _**Maven Build**_ -> New launch configuration -> Name: `whiskies-backend-project [clean package]`, Base directory: `whiskies-backend-project`, Goals: `clean package`
2. _**Java Application**_ -> New launch configuration -> Spring Boot, Project: `whiskies-backend-project`, Main class: `com.unicap.react.project.ProjectApplication`

### 1.2- pgAdmin

Para baixar a ferramenta pgAdmin, clique [aqui](https://www.pgadmin.org/).

**Banco de Dados:**
host: ``
port: `5432`
database: ``

### 1.3- Putty (Windows)

Para baixar a ferramenta Putty, clique [aqui](https://www.chiark.greenend.org.uk/~sgtatham/putty/).

## 2- Arquitetura do Sistema

Este projeto segue um padrão arquitetural em camadas [[1](https://www.oreilly.com/library/view/software-architecture-patterns/9781491971437/ch01.html),[2](https://en.wikipedia.org/wiki/Multitier_architecture)] para fornecer uma API REST [[3](https://dzone.com/articles/intro-rest),[4](https://www.quora.com/What-are-RESTful-APIs-and-how-do-they-work),[5](https://blog.caelum.com.br/rest-principios-e-boas-praticas/)] ao módulo _frontend_ da aplicação. A camada mais externa do sistema (_Service_) implementa os serviços REST (JAX-RS), tendo esta camada a responsabilidade de validar os dados de entrada, assim como realizar as restrições de segurança necessárias (autenticação/autorização) no acesso aos serviços disponibilizados. A camada imediatamente abaixo (_Business_) é responsável pela execução da lógica de negócio de cada serviço, que incluem validações de negócio e transformação dos dados de entrada antes de atualizá-los. A última camada (_Persistence_) do modelo é responsável pelas funcionalidades diretas de acesso/atualização dos dados do sistema no banco de dados.

```
┌───────────────────┐
│   Service (REST)  │
├───────────────────┤
│   Business (BC)   │ 
├───────────────────┤
│ Persistence (DAO) │ SPRING DATA, JPA, HIBERANTE
└───────────────────┘
         ||
     ┌────────┐
     │   BD   │
     └────────┘
```

O projeto foi desenvolvido usando a ferramenta [Spring Boot](https://spring.io/projects/spring-boot).

A organização e significado de cada um dos pacotes do projeto segue abaixo:

```
src
├── main
│   ├── java/com/flexpag/pos/backend/app
│   │   ├── bc                                       -> classes da camada de negócio (xxxxBC.java)
│   │   ├── entity                                   -> classes de entidades/pojos/dtos
│   │   ├── exception                                -> classes de exceções e mappers
│   │   ├── integration                              -> classes que implementam a integração com outros sistemas
│   │   ├── jdbi                                     -> classes de infraestrutura para conexão com banco de dados com jdbi
│   │   ├── persistence                              -> classes da camada de persistência (xxxxDAO.java)
│   │   ├── rest                                     -> classes da camada de serviço (xxxxREST.java)
│   │   ├── security                                 -> classes relacionadas à autenticação/autorização dos serviços
│   │   ├── thorntail                                -> classes relacionadas à infraestrutura do thorntail
│   │   └── util                                     -> classes utilitárias
│   ├── resources
│   │   ├── com/flexpag/pos/backend/app/persistence  -> arquivos *.sql e *.stg (string template)
│   │   ├── modules/org/postgresql/main
│   │       └── module.xml                           -> arquivos de configuracao do driver do postgresql
│   │   ├── MANIFEST.MF                              -> arquivo de manifesto do projeto
│   │   ├── project-defaults.yml                     -> arquivo de propriedades 'default' do projeto
│   │   └── project-hom.yml                          -> arquivo de propriedades para ambiente de homologacao do projeto
|   └── web-inf/WEB-INF
|       └── beans.xml                                -> arquivo de configuração do cdi
└── test
    └── java/com/flexpag/pos/backend/app/bc          -> classes de testes unitários
```

## 3- Geração da Build e Deploy no Heroku

### 3.1- Conectar com o servidor remoto

**Pré-requisitos:**
- Gerar um par de chaves SSH (pública e privada); e,
- Configurar a chave pública no servidor remoto.

Obs.: Para gerar as chaves, usar o `ssh-keygen` no Linux, ou `puttygen` no Windows.

**Servidor de Homologação:**
host: `107.20.27.77`
port: `22` (ssh)
username: `nome-de-usuario`
password: `senha-da-chave-privada`

### 3.2- Remover o servidor do sistema do ar (opcional - caso esteja online)

```sh
$ sudo bash  # senha da chave privada

$ cd /flexpag/pos-backend
$ ps aux | grep pos-backend
$ kill -9 pid-do-processo-do-backend 
```

### 3.3- Recuperar as últimas atualizações do repositório Git

```sh
$ git checkout path/servicos-solicitados-klaus  # usuario e senha do GitHub
$ git pull
```

### 3.4- Gerar a build do sistema

```sh
$ mvn clean package
```

### 3.5- Executar aplicação

```sh
$ nohup java -jar target/pos-backend-thorntail.jar -Shom &
```

Para visualizar o log da aplicação em tempo real:

```sh
$ tail -f nohup.out -n 500
```

Para verificar se a aplicação está online, usar o endereço `http://107.20.27.77:8080/api/pos-device/ping` no navegador. Deve ser apresentada a mensagem `You cannot access this resource`, uma vez que os serviços são protegidos por Basic Authentication.

[![Quality gate](https://sonar.flexpag.com/api/project_badges/quality_gate?project=pos-backend)](https://sonar.flexpag.com/dashboard?id=pos-backend)
