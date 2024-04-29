edge(a,1).
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
edge(6,1).

counteredge(X,Y) :- edge(Y,X) & X\==Y.

direction(a,1,f).
direction(b,2,f).
direction(c,3,f).
direction(d,4,f).
direction(f,6,f).
direction(1,2,l).
direction(1,6,d).
direction(2,3,d).
direction(3,4,d).
direction(4,5,d).
direction(5,6,d).
direction(6,1,d).


/* O agente conclui que pode chegar de Origem a Destino por um Caminho... */
caminho(Origem, Destino, Caminho) :-
    caminho_aux(Origem, Destino, [Origem], Caminho).

// Regra auxiliar para buscar um caminho usando recursão - caso base
caminho_aux(Destino, Destino, Caminho, Caminho).

caminho_aux(Origem, Destino, Visitados, Caminho) :-    
    .findall(Proximos,edge(Origem,Proximos),LProximos) & .member(Destino,LProximos) &
    .concat(Visitados,[Destino],NVisitados) &
    caminho_aux(Destino,Destino,NVisitados,Caminho).

caminho_aux(Origem, Destino, Visitados, Caminho) :-
    // Verifica se existe uma aresta de Origem para um próximo nó
    (edge(Origem, Proximo) | counteredge(Origem,Proximo))

    //obter uma lista de todos os Proximo, ver se o destino está nesta lista, pegar o "proximo"<>destino e parar de procurar

    // Verifica se o próximo nó não foi visitado ainda para evitar ciclos
    &  not(.member(Proximo, Visitados))
    // Adiciona o próximo nó à lista de visitados
   // & caminho_aux(Proximo, Destino, [Proximo | Visitados], Caminho).
    & .concat(Visitados,[Proximo],NVisitados) &  caminho_aux(Proximo, Destino, NVisitados, Caminho).
                             




//!check_path(a,6).

!go_to(a,6).


      
/*+!check_path(O,T) : caminho(O,T,Path)
   <- .print("Caminho ", O, " - ", T,": ", Path).


+!check_path(O,T) : not caminho(O,T,Path)
   <- .print("Não há caminho ", O, " - ", T,": ", Path).
   */
   

+!go_to(Origem,Destino) :  caminho(Origem,Destino,Caminho) 
   <- !percorre_caminho(Caminho);
      .print("Cheguei ao destino").


+!percorre_caminho([]) 
   <- .print("Percorri todo o caminho").


+!percorre_caminho([H|T])
   <- .print("Indo para ", H, " - direção: ", D);
      !percorre_caminho(T).