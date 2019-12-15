// ContaServer.java
// Copyright and License
import java.sql.*;
import ContaApp.*;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;

import java.util.Properties;

class ContaImpl extends ContaPOA {
	private ORB orb;
	
	public void setORB(ORB orb_val) {
		orb = orb_val; 
	}
	
	// nome JDBC driver name e URL do banco de dados
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://localhost:3306/banco?verifyServerCertificate=false";
	
	//  credenciais do banco de dados 
	static final String USER = "corba";
	static final String PASS = "corba";
	Connection conn = null;
	Statement stmt = null;
	public float saldo(){
		try{
			
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			System.out.println("Creating statement...");
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT saldo FROM conta where idconta = 1;";
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()){
				return rs.getFloat("saldo");
			}
			rs.close();
			stmt.close();
			conn.close();
		}catch(SQLException se){
			se.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(stmt!=null)
					stmt.close();
			}catch(SQLException se2){
			}
			try{
				if(conn!=null)
					conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}
		}
		return 1.99f;
	}
	public void deposito(float valor){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			
			System.out.println("Creating statement...");
			stmt = conn.createStatement();
			String sql;
			sql ="UPDATE conta SET saldo=saldo + "+valor+" WHERE idconta=1;";
			stmt.executeUpdate(sql);
			stmt.close();
			conn.close();
		}catch(SQLException se){
			se.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(stmt!=null)
					stmt.close();
			}catch(SQLException se2){
			}
			try{
				if(conn!=null)
					conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}
		}
	}
	public void saque(float valor){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			
			System.out.println("Creating statement...");
			stmt = conn.createStatement();
			String sql;
			
			sql ="UPDATE conta SET saldo=saldo - "+valor+" WHERE idconta=1;";
			stmt.executeUpdate(sql);
			stmt.close();
			conn.close();
		}catch(SQLException se){
			se.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(stmt!=null)
					stmt.close();
			}catch(SQLException se2){
			}
			try{
				if(conn!=null)
					conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}
		}
	}
	
	public void shutdown() {
		orb.shutdown(false);
	}
}


public class ContaServer {

  public static void main(String args[]) {
    try{
      // cria e inicializa o ORB
      ORB orb = ORB.init(args, null);

      // pega a referencia para  rootpoa & etiava o POAManager
      POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
      rootpoa.the_POAManager().activate();

      // cria o  servant e  registra  com o ORB
      ContaImpl contaImpl = new ContaImpl();
      contaImpl.setORB(orb); 

      // get object reference from the servant
      org.omg.CORBA.Object ref = rootpoa.servant_to_reference(contaImpl);
      Conta href = ContaHelper.narrow(ref);
          
      // pega o  root naming context
      // NameService invoca o serviço de nome
      org.omg.CORBA.Object objRef =
          orb.resolve_initial_references("NameService");
      // Use NamingContextExt which is part of the Interoperable
      // Naming Service (INS) specification.
      NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

      // vincula o Objecto e referencia no serviço de nome
      String name = "Conta";
      NameComponent path[] = ncRef.to_name( name );
      ncRef.rebind(path, href);

      System.out.println("ContaServer pronto e esperando ...");

      // espera por invocaçoes dos clientes 
      orb.run();
    } 
        
      catch (Exception e) {
        System.err.println("ERRO: " + e);
        e.printStackTrace(System.out);
      }
          
      System.out.println("ContaServer  ...");
        
  }
}