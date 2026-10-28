!go_to_position.

+!go_to_position
   <- .print("Enviando goal de navegação...");
      .moveNav2(
          [["map"], [[10, 0, 0], [0, 0, 0, 1]]],
          "",
          GoalId
      );
      +active_navigation(GoalId);
      .print("Goal enviado: ", GoalId).

+navigation_status(GoalId, succeeded)
   : active_navigation(GoalId)
   <- -active_navigation(GoalId);
      .print("Navegação concluida.").

+navigation_status(GoalId, aborted)
   : active_navigation(GoalId)
   <- -active_navigation(GoalId);
      .print("Navegação abortada: ", GoalId).

+navigation_status(GoalId, canceled)
   : active_navigation(GoalId)
   <- -active_navigation(GoalId);
      .print("Navegação cancelada: ", GoalId).

{ include("$jacamo/templates/common-cartago.asl") }
{ include("$jacamo/templates/common-moise.asl") }
