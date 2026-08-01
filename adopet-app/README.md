<h1>🐾 Adopet - Plataforma de Adoção de Animais</h1>

<h2>📋 Descrição do Projeto</h2>
<p>
  O Adopet é uma plataforma web desenvolvida para facilitar a adoção responsável de animais.
  O sistema conecta pessoas interessadas em adotar, doadores e administradores, oferecendo
  recursos para cadastro de animais, solicitações de adoção, acompanhamento pós-adoção,
  denúncias e transporte.
</p>

<p>
  Este projeto foi desenvolvido para a disciplina <b>Projeto Integrador</b>, com o objetivo
  de aplicar conhecimentos de desenvolvimento web, persistência de dados, APIs REST,
  autenticação e desenvolvimento de interfaces.
</p>

<h2>🎯 Objetivo</h2>
<p>
  Criar uma solução digital para organizar o processo de adoção de animais, tornando a
  divulgação, a solicitação e o acompanhamento das adoções mais simples, centralizados e
  acessíveis.
</p>

<h2>🛠️ Tecnologias Utilizadas</h2>

<h3>Backend</h3>
<ul>
  <li><b>Java 17</b> - Linguagem de programação principal</li>
  <li><b>Spring Boot 4</b> - Framework para desenvolvimento da aplicação web</li>
  <li><b>Spring Web MVC</b> - Criação da API REST</li>
  <li><b>Spring Data JPA</b> - Persistência e acesso aos dados</li>
  <li><b>Hibernate</b> - ORM para mapeamento das entidades</li>
  <li><b>Spring Security</b> - Autenticação e autorização</li>
  <li><b>Bean Validation</b> - Validação dos dados recebidos pela API</li>
  <li><b>Lombok</b> - Redução de código repetitivo nas classes Java</li>
  <li><b>Maven</b> - Gerenciamento de dependências e execução do projeto</li>
</ul>

<h3>Banco de Dados</h3>
<ul>
  <li><b>SQLite</b> - Banco de dados relacional utilizado no ambiente de desenvolvimento</li>
  <li><b>Hibernate Community Dialects</b> - Dialeto SQLite para o Hibernate</li>
  <li><b>JPA</b> - Mapeamento entre as classes Java e as tabelas do banco</li>
</ul>

<h3>Frontend</h3>
<ul>
  <li><b>HTML5</b> - Estrutura das páginas</li>
  <li><b>CSS3</b> - Estilização e layout responsivo</li>
  <li><b>JavaScript</b> - Integração com a API e interatividade da aplicação</li>
</ul>

<h2>📊 Funcionalidades Implementadas</h2>

<h3>👤 Usuários</h3>
<ul>
  <li>Cadastro de novos usuários</li>
  <li>Login e autenticação</li>
  <li>Visualização e atualização do perfil</li>
  <li>Alteração de senha</li>
  <li>Controle de papéis de usuário e administrador</li>
</ul>

<h3>🐶 Cadastro e Gerenciamento de Animais</h3>
<ul>
  <li>Cadastro de animais disponíveis para adoção</li>
  <li>Informações de nome, espécie, raça e idade</li>
  <li>Informações sobre vacinação e castração</li>
  <li>Inclusão de fotos do animal</li>
  <li>Consulta de animais disponíveis</li>
  <li>Busca e atualização de animais cadastrados</li>
  <li>Exclusão de animais</li>
</ul>

<h3>🏠 Processo de Adoção</h3>
<ul>
  <li>Visualização de animais disponíveis</li>
  <li>Envio de solicitações de adoção</li>
  <li>Consulta das solicitações feitas pelo usuário</li>
  <li>Consulta das solicitações recebidas pelo doador</li>
  <li>Aprovação ou rejeição de solicitações</li>
  <li>Registro da adoção realizada</li>
</ul>

<h3>📦 Transporte</h3>
<ul>
  <li>Agendamento de transporte relacionado a uma adoção</li>
  <li>Consulta dos transportes cadastrados</li>
  <li>Acompanhamento do transporte</li>
  <li>Registro de mensagens e status do transporte</li>
</ul>

<h3>📝 Acompanhamento Pós-Adoção</h3>
<ul>
  <li>Registro de atualizações sobre o animal após a adoção</li>
  <li>Consulta do histórico de acompanhamento</li>
  <li>Inclusão de fotos e descrições</li>
</ul>

<h3>🚨 Denúncias</h3>
<ul>
  <li>Registro de denúncias relacionadas a animais ou usuários</li>
  <li>Classificação por categoria</li>
  <li>Consulta de denúncias</li>
  <li>Filtragem por status</li>
</ul>

<h3>⚙️ Administração</h3>
<ul>
  <li>Registro de ações administrativas</li>
  <li>Gerenciamento de usuários e animais</li>
  <li>Consulta de informações para acompanhamento da plataforma</li>
</ul>

<h2>🚀 Como Executar</h2>

<h3>Pré-requisitos</h3>
<ul>
  <li>JDK 17 ou superior</li>
  <li>Git</li>
  <li>Maven instalado ou Maven Wrapper</li>
  <li>Python 3, Live Server ou outro servidor HTTP para o frontend</li>
</ul>

<h3>Clonar o projeto</h3>
<pre><code>git clone https://github.com/Silva-D-Romario/AdoPet.git
cd adopet</code></pre>

<h3>Executar o backend</h3>
<p>Na pasta que contém <code>adopet-app</code>, execute:</p>
<pre><code>./adopet-app/back/mvnw -f adopet-app/back/pom.xml spring-boot:run</code></pre>
<p>No Linux, caso o wrapper não tenha permissão de execução, use:</p>
<pre><code>bash ./adopet-app/back/mvnw -f adopet-app/back/pom.xml spring-boot:run</code></pre>
<p>
  A API será iniciada por padrão em
  <a href="http://localhost:8080">http://localhost:8080</a>.
  O banco SQLite será criado ou atualizado em <code>back/data/adopet.db</code>.
</p>

<h3>Executar o frontend</h3>
<p>Em outro terminal, na mesma pasta, execute:</p>
<pre><code>python3 -m http.server 5500 --directory adopet-app/front</code></pre>
<p>
  Depois acesse
  <a href="http://localhost:5500">http://localhost:5500</a>.
</p>

<p>
  Também é possível abrir o frontend utilizando a extensão Live Server do VS Code.
  O backend precisa estar em execução para que login, cadastro, adoção e demais operações
  que usam a API funcionem.
</p>

<h2>🗃️ Banco de Dados</h2>
<p>
  A aplicação utiliza SQLite em modo de desenvolvimento. O arquivo é criado em:
</p>
<pre><code>back/data/adopet.db</code></pre>

<p>
  As tabelas são criadas e atualizadas automaticamente pelo Hibernate por meio da
  propriedade <code>spring.jpa.hibernate.ddl-auto=update</code>.
</p>

<p>Para começar novamente com um banco vazio, pare a aplicação e execute:</p>
<pre><code>rm -f back/data/adopet.db
rm -f back/data/adopet.db-shm
rm -f back/data/adopet.db-wal</code></pre>
<p>Ao iniciar o backend novamente, o banco será recriado sem registros.</p>

<h3>Principais páginas do frontend</h3>
<ul>
  <li><b>index.html</b> - Página inicial</li>
  <li><b>login.html</b> - Login de usuários</li>
  <li><b>cadastro-animal.html</b> - Cadastro de animais</li>
  <li><b>animal.html</b> - Visualização de informações do animal</li>
  <li><b>meus-animais.html</b> - Animais cadastrados pelo usuário</li>
  <li><b>minhas-solicitacoes.html</b> - Solicitações de adoção realizadas</li>
  <li><b>solicitacoes-recebidas.html</b> - Solicitações recebidas pelo doador</li>
  <li><b>transporte.html</b> - Consulta de transportes</li>
  <li><b>agendar-transporte.html</b> - Agendamento de transporte</li>
  <li><b>denuncia.html</b> - Registro de denúncias</li>
  <li><b>perfil.html</b> - Perfil do usuário</li>
  <li><b>admin.html</b> - Área administrativa</li>
</ul>

<h2>🔌 Principais Recursos da API</h2>
<ul>
  <li><code>/usuarios</code> - Cadastro, login e gerenciamento de usuários</li>
  <li><code>/animais</code> - Cadastro, consulta, atualização e remoção de animais</li>
  <li><code>/adocoes</code> - Solicitações, aprovações e rejeições de adoção</li>
  <li><code>/transportes</code> - Agendamento e acompanhamento de transportes</li>
  <li><code>/atualizacoes-pos-adocao</code> - Acompanhamento após a adoção</li>
  <li><code>/denuncias</code> - Registro e consulta de denúncias</li>
  <li><code>/acoes-administrativas</code> - Registro de ações administrativas</li>
</ul>

<h2>🔒 Segurança e Validações</h2>
<ul>
  <li>Autenticação utilizando Spring Security</li>
  <li>Senhas armazenadas com hash</li>
  <li>Validação dos dados recebidos nos DTOs</li>
  <li>Separação entre controllers, services, repositories e models</li>
  <li>Controle de acesso para operações administrativas</li>
</ul>

<h2>👥 Desenvolvido por</h2>
<p>Alesson</p>
<p>Romário</p>
<p>Sonayte</p>


<h2>📄 Finalidade</h2>
<p>
  Projeto desenvolvido para fins acadêmicos como parte da disciplina <b>Projeto Integrador</b>.
</p>
