import ContaApp.*;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;

public class ContaClient
{
  static Conta ContaImpl;

  public static void main(String args[])
    {
    float Saldo =0;
      try{
        // cria e inicializa o ORB
        ORB orb = ORB.init(args, null);

	// pega a referencia para  rootpoa & etiava o POAManager (Portable Object Adapter)
        org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
        // Use NamingContextExt instead of NamingContext. This is 
        // part of the Interoperable naming Service.  
        NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
 
        // resolve a referencia do objeto no serviço de nome
        String name = "Conta";
        ContaImpl = ContaHelper.narrow(ncRef.resolve_str(name));

        System.out.println("Obtained a handle on server object: " + ContaImpl);
        
        System.out.println("depositando 700.0 na conta ");
        ContaImpl.deposito(700.0f);
        
        Saldo = ContaImpl.saldo();
        System.out.println("saldo na conta: " + Saldo );
        
        System.out.println("sacando 350.0 na conta ");
        ContaImpl.saque(350.0f);
        
        Saldo = ContaImpl.saldo();
        System.out.println("saldo na conta: " + Saldo );
        
        
        } catch (Exception e) {
          System.out.println("ERRO : " + e) ;
          e.printStackTrace(System.out);
          }
    }

}