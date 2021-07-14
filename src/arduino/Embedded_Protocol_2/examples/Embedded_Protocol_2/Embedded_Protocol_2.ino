 /*   Embedded_Protocol_2 example

   Biblioteca que cria e envia crenças no formato JSON.
     
      Para criar uma crença com essa biblioteca utilizamos tres funcoes:
         - startBelief(String beliefName) :  cria(inicia) uma creça com o nome que lhe e passado.
         - beliefAdd(String msg | int Int | float Float | double Double) : adiciona parametros a crença criada anteriormente. Pode ser chamada quantas vezes for necessario e pode receber dados do tipo String, int, float e double.
         - endBelief() : fecha a crença em que estavamos adicionando paramentros e adiciona-a (amazena-a) ao conjunto de crenças, deixando-a pronta para ser enviada.
     
     Para enviarmos o conjunto de crenças que criamos atraves dos passos acima usamos a funçao sendMessage(). 
     Depois de invocada essa funçao limpa todas as crenças que estao armazenadas.    
 */
 
 #include<Embedded_Protocol_2.h>
  
  Communication com;
  
  int parametro1 = 10;
  float parametro2 = 13.1416;
  String parametro3 = "teste";
  
  void setup() {
    Serial.begin(9600);
  }
  
  void loop() {
    
    com.startBelief("belief1");
    com.beliefAdd(parametro1);
    com.beliefAdd(parametro2);
    com.beliefAdd(parametro3);
    com.endBelief();

    com.startBelief("belief2");
    com.beliefAdd(11);
    com.beliefAdd(12.5879);
    com.beliefAdd("teste2");
    com.beliefAdd("$");
    com.endBelief();

    com.startBelief("belief3");
    com.beliefAdd(1.96);
    com.endBelief();

    com.startBelief("belief4");
    com.beliefAdd("true");
    com.endBelief();
    
    com.sendMessage();
    delay(500);
  }
