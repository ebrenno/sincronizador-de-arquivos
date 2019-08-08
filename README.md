# sincronizador-de-arquivos
Um projeto acadêmico em java que utiliza sockets como objeto de estudo para comunicação entre aplicações.
## Descrição do projeto
O projeto compõe duas aplicações que colaboram entre si para a transferência de arquivos pela rede utilizando sockets. O conceito é análogo a qualquer sincronizador conhecido, vizando fazer as aplicações transferirem os arquivos que foram alterados.
## Avisos importantes
  - Os arquivos são carregados na memória ram antes de serem transferidos e não há limite máximo para o tamanho dos arquivos. Portanto, atente-se para a disponibilidade de memória.
  - O ambiente de testes foi uma distribuição Linux com sistema de arquivos ext4. O comportamento dos apps podem não ser o esperado em diferentes ambientes.
## Construindo o projeto
O projeto contém dois pacotes independentes com suas respectivas classes executáveis `Servidor.java` e `Cliente.java`. Para construir os produtos individualmente, basta separar esses pacotes em duas cópias do projeto.
## Executando
### Servidor
1. selecione o endereço ip que será atribuído ao app.
    - Se nenhum endereço for identificado, o app irá utilizar o endereço de loopback(127.0.0.1) por padrão.
2. selecione o diretório em que o app irá monitorar os arquivos.
3. clique no botão "aplicar" e depois "ligar".
### Cliente
1. especifique o endereço ip que foi escolhido para o app server.
2. especifique o tempo(em segundos) de cooldown em que o app irá operar.
3. selecione o diretório em que o app irá monitorar os arquivos.
4. clique no botão "aplicar" e depois "ligar".
    - o botão "ligar" se torna "desligar", onde o app deixará de fazer requisições ao servidor e podendo ter suas configurações refeitas.
5. eventualmente uma caixa de confirmação aparecerá para aceitar ou recusar a transferência de cada arquivo.
## Comportamentos de destaque
  - busca por conexões de rede local nas interfaces de rede identificadas.
  - apenas novos arquivos e os que foram alterados por último são candidatos à transferência.
  - os apps não farão a leitura de arquivos que por ventura estiverem sendo movidos para o diretório naquele instante, evitando inconsistência nos dados.
    - a necessidade dessa característica aumenta quanto maior for o tamanho do arquivo.
## Limitações
  - o projeto foi pensado em servir apenas a um app cliente.
  - as aplicações funcionam apenas em rede local ou na mesma máquina.
  - socket não é a melhor forma de transferir arquivos pela rede.
