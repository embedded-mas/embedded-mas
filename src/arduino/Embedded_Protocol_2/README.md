# Biblioteca Embedded_Protocol_2

Para utilizar a biblioteca é necessário conhecer basicamente 4 funções, são elas: 
- startBelief (String beliefName) :  cria (inicia) uma crença com o nome que lhe é passado.

- beliefAdd (String msg | int Int | float Float | double Double) : adiciona um parâmetro a crença criada anteriormente. Pode ser chamada quantas vezes for necessário, ou seja a crença pode ter diversos parâmetros. Pode receber dados do tipo String, int, float e double.

- endBelief() : fecha a crença em que estamos adicionando parâmetros e adiciona-a ao conjunto de crenças (armazena-a em um buffer), deixando-a pronta para ser enviada.

- sendMessage(): envia o conjunto de crenças que criamos através das funções acima. Depois de invocada essa função limpa todas as crenças que estão armazenadas no buffer. 

