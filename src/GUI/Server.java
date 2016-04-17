/**
 * Componente Curricular: Módulo Integrador de Concorrência e Conectividade
 * Autor: Daniel Andrade e Solenir Figuerêdo
 * Data:  17/04/2016
 *
 * Declaramos que este código foi elaborado por nós em dupla e
 * não contém nenhum trecho de código de outro colega ou de outro autor, 
 * tais como provindos de livros e apostilas, e páginas ou documentos 
 * eletrônicos da Internet. Qualquer trecho de código de outra autoria que
 * uma citação para o não a nossa está destacado com autor e a fonte do
 * código, e estamos cientes que estes trechos não serão considerados para fins
 * de avaliação. Alguns trechos do código podem coincidir com de outros
 * colegas pois estes foram discutidos em sessões tutorias.
 */
package GUI;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import model.SpeedControlImpl;

/**
 *
 * @author Daniel Andrade e Solenir Figuerêdo
 */
public class Server {
    
    public Server() {
    
        System.out.println("Server is Online!");
        
        try{
           System.setProperty("java.rmi.server.hostname", "127.0.0.1");
           LocateRegistry.createRegistry(3333);
           Naming.rebind("Train1", new SpeedControlImpl());
       
       }
       catch(Exception ex){
           
       }
    }
    
    public static void main (String args[]){
        new Server();
         
       
    }
    
   
}
