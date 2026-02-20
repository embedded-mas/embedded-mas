# Biblioteca Embedded_Protocol_2

A biblioteca disponibiliza funções para percepção e atuação, descritas a seguir.

## Funções para percepção:
- startBelief (String beliefName) :  cria (inicia) uma percepção com o nome que lhe é passado.

- beliefAdd (String msg | int Int | float Float | double Double) : adiciona um parâmetro à percepção criada anteriormente. Pode ser chamada quantas vezes for necessário, ou seja a percepção pode ter diversos parâmetros. Pode receber dados do tipo String, int, float e double.

- endBelief() : fecha a percepção em que estamos adicionando parâmetros e adiciona-a ao conjunto de percepções (armazena-a em um buffer), deixando-a pronta para ser enviada.

- sendMessage(): envia o conjunto de percepções que criamos através das funções acima. Depois de invocada essa função limpa todas as percepções que estão armazenadas no buffer. 

Há um exemplo disponível [aqui](https://github.com/embedded-mas/embedded-mas/tree/master/src/arduino/Embedded_Protocol_2/examples/Embedded_Protocol_2)

## Funções para atuação
- actuationName(String input): retorna o nome da atuação correspondente a um valor receibido via seria. Ex.: retorna *test* caso o valor recebido seja *test(3,"hello", 3.14)*
- paramStr(String s, int i): retorna um parâmetro do tipo String na i-ésima posição. Ex.: `paramStr(test(3,"hello", 3.14),1)` retorna `hello`
- paramInt(String s, int i): retorna um parâmetro do tipo int na i-ésima posição. Ex.: `paramStr(test(3,"hello", 3.14),0)` retorna `3`
- paramFloat(String s, int i): retorna um parâmetro do tipo float na i-ésima posição. Ex.: `paramStr(test(3,"hello", 3.14),2)` retorna `3.14`

Há um exemplo disponível [aqui](https://github.com/embedded-mas/embedded-mas/blob/master/src/arduino/Embedded_Protocol_2/examples/Embedded_Protocol_2/actuation.ino/actuation.ino.ino)
          
