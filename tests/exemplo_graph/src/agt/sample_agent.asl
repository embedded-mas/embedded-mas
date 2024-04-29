/*edge(a,1).
edge(b,2).
edge(c,3).
edge(d,4).
edge(f,6).
edge(1,2).
edge(1,6).
edge(2,3).
edge(3,4).
edge(4,5).
edge(5,6).
edge(6,1).*/

edge(a,1,front).
edge(b,2,front).
edge(c,3,front).
edge(d,4,front).
edge(f,6,front).
edge(1,2,left).
edge(1,6,right).
edge(2,3,right).
edge(3,4,right).
edge(4,5,right).
edge(5,6,right).

//uma aresta de X para Y indica um caminho de Y para X.
counteredge(X,Y,Direction) :- edge(Y,X,direction) 
                              & X\==Y & opposite_direction(direction,Direction).

opposite_direction(front,back).
opposite_direction(back,front).
opposite_direction(left,right).
opposite_direction(right,left).


/* O agente conclui que pode chegar de Origem a Destino por um Caminho... */
caminho(Origem, Destino, Caminho) :-
    caminho_aux(Origem, Destino, [Origem], Caminho).

// Regra auxiliar para buscar um caminho usando recursão - caso base
caminho_aux(Destino, Destino, Caminho, Caminho).

caminho_aux(Origem, Destino, Visitados, Caminho) :-    
    .findall(Proximos,edge(Origem,Proximos,_),LProximos) & .member(Destino,LProximos) &
    .concat(Visitados,[Destino],NVisitados) &
    caminho_aux(Destino,Destino,NVisitados,Caminho).

caminho_aux(Origem, Destino, Visitados, Caminho) :-
    // Verifica se existe uma aresta de Origem para um próximo nó
    (edge(Origem, Proximo,_) | counteredge(Origem,Proximo,_))

    //obter uma lista de todos os Proximo, ver se o destino está nesta lista, pegar o "proximo"<>destino e parar de procurar

    // Verifica se o próximo nó não foi visitado ainda para evitar ciclos
    &  not(.member(Proximo, Visitados))
    // Adiciona o próximo nó à lista de visitados
   // & caminho_aux(Proximo, Destino, [Proximo | Visitados], Caminho).
    & .concat(Visitados,[Proximo],NVisitados) &  caminho_aux(Proximo, Destino, NVisitados, Caminho).
                             



!go_to(a,6). //Objetivo go_to(X,Y): o agente deseja ir do ponto X ao Y.


+!go_to(Origem,Destino) :  caminho(Origem,Destino,Caminho) 
   <- .print("Vou percorrer o caminho ", Caminho);
      !percorre_caminho(Caminho);      
      .print("Cheguei ao destino").




//Planos para atingir o objetivo "percorre_caminho". Estes planos fazem o agente percorrer uma lista de pontos passados como parâmetro.

//Caso 1: A lista de pontos percorrer está vazia. Logo, não há caminho a percorrer.
+!percorre_caminho([]) 
   <- .print("Percorri todo o caminho").

//Caso 2: O agente não tem crença a respeito da sua posição atual. Logo, ele ainda não começou o trajeto.
+!percorre_caminho([H|T]) : not posicao_atual(_)  //se o agente não tem a crença "posicao_atual", ele está no início do percurso
   <- .print("Iniciando o caminho em ", H, ".");
      +posicao_atual(H); //atualiza a posição atual
      !percorre_caminho(T).

//Caso 3: O agente está no meio do trajeto.
+!percorre_caminho([H|T]) : posicao_atual(P)  //verificar a posição atual
                            & edge(P,H,D) //verificar a direção a seguir de P para H
   <- .print("Indo de ", P, " para ", H, " - direção: ", D);
      -+posicao_atual(H); //atualiza a posição atual
      !percorre_caminho(T).