package jason.stdlib;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

import java.nio.file.*;
import java.nio.charset.*;


/*
  Parametros:
  1. Nome do arquivo
  2. Texto a ser escrito
*/
public class save extends DefaultInternalAction {

   
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
       
       String regex = "\"(.*)\"";
       Path file = Paths.get(args[0].toString());
       String text =  args[1].toString().replaceAll(regex, "$1") + "\n";
       Charset charset = StandardCharsets.UTF_8;

       if (Files.exists(file)) 
          Files.write(file, text.getBytes(charset), StandardOpenOption.APPEND);
       else
          Files.write(file, text.getBytes(charset));
       
       return true;
    }
}
