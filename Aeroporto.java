import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Collections;
import java.util.LinkedList;
import java.security.SecureRandom;

public class Aeroporto {

	 public static void main(String[] args) throws InterruptedException {
		 
	        LinkedList<Aviao> c = new LinkedList<>();
	        LinkedList<Aviao> c_2 = new LinkedList<>();
	        
	        // Adiciona avioes randomicamente a suas pistas
	        for(int i = 0; i<6 ; i++) {
	        	Aviao a = new Aviao(i);
	        	if(a.idPista == 0) {
	        		c.add(a);
	        	} else if(a.idPista == 1) {
	        		c_2.add(a);
	        	}
	        }
	       
	        //Embaralha Avioes
	        Collections.shuffle(c);
	        Collections.shuffle(c_2); 
	        
	        ExecutorService executorService = Executors.newCachedThreadPool();

	        executorService.execute(new Pista(1, c));
	        executorService.execute(new Pista(2, c_2));

	        executorService.shutdown();
	        executorService.awaitTermination(2, TimeUnit.MINUTES);
	 }
}

class Pista implements Runnable {
	private int idPista;
	
    // Lista de avioes designados para decolar na pista
    private LinkedList<Aviao> avioes = new LinkedList<Aviao>();

    public Pista(int id, LinkedList<Aviao> l) {
    	this.idPista = id;
        this.avioes = l;
    }

    @Override
    public void run() {
    	while(avioes.isEmpty() != true) {
    		Aviao nextOnFiFo = avioes.remove();
    		// remove aviao decolado da lista
    		nextOnFiFo.decolar(idPista);
    	}
    }
    
	public Aviao remove() {
		return this.avioes.remove(0);
	}

	public boolean isEmpty() {
		return this.avioes.size() == 0;
	}

}

class Aviao {
    private int id;
    
    public Aviao(int id) {
    	this.id = id;
    }
    
    private static final SecureRandom gerador = new SecureRandom();
    
    int idPista = gerador.nextInt(2);
    
    private final Semaphore semaphore1 = new Semaphore(1);
    private final Semaphore semaphore2 = new Semaphore(2);
    
    public void decolar(int idPista) {
    	//Aviao decola em pista 1
        if(idPista == 0){
        	try {
                semaphore1.acquire();
        	} catch (InterruptedException ex) {
                Logger.getLogger(Aviao.class.getName()).log(Level.SEVERE, null, ex);
        	}
        }
        //Aviao decola em pista 2
        else if(idPista == 1){
        	try {
                semaphore2.acquire();
            } catch (InterruptedException ex) {
                Logger.getLogger(Aviao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }	
        
        try {
            semaphore1.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(Aviao.class.getName()).log(Level.SEVERE, null, ex);
        }
	
        System.out.println("AV" +id +" Iniciando processo de decolagem. Na Pista " + idPista);		
        System.out.println("AV" +id +" Manobrando... Na Pista " + idPista);
        
        try {
            Thread.sleep(3000 + gerador.nextInt(5000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("AV" +id +" Terminou de manobrar. Na Pista " + idPista);
        
        System.out.println("AV" +id +" Taxiando... Na Pista " + idPista);
        try {
            Thread.sleep(2000 + gerador.nextInt(4000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("AV" +id +" Terminou de taxiar. Na Pista " + idPista);
        
        System.out.println("AV" +id +" Posicionando...");
        try {
            Thread.sleep(1000 + gerador.nextInt(4000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("AV" +id +" Terminou de posicionar. Na Pista " + idPista);
        
        System.out.println("AV" +id +" Acelerando... Na Pista " + idPista);
        try {
            Thread.sleep(3000 + gerador.nextInt(3000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("AV" +id +" Terminou de acelerar. Na Pista " + idPista);
        
        System.out.println("AV" +id +" Decolando... Na Pista " + idPista);
        try {
            Thread.sleep(40 + gerador.nextInt(4000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("AV" +id +" Terminou de decolar. Na Pista " + idPista);
        
        System.out.println("AV" +id +" Afastando... Na Pista " + idPista);
        try {
            Thread.sleep(20 + gerador.nextInt(5000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("AV" +id +" Terminou de se afastar. Na Pista " + idPista);
        
        semaphore1.release();
    }

	public int getId() {
		return id;
	}
}
  
