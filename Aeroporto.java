import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.security.SecureRandom;

public class Aeroporto {

public static Pista p1 = new Pista(1);
public static Pista p2 = new Pista(2);

	public static void main(String[] args) throws InterruptedException {
		 	
	        
		 	ExecutorService executorService = Executors.newCachedThreadPool();

	        for(int i = 0; i<6 ; i++) {
	        	Aviao a = new Aviao(i);
	        	executorService.execute(a);
	        }

	        executorService.shutdown();
	        executorService.awaitTermination(2, TimeUnit.MINUTES);
	 }
}

class Pista {
	private int idPista;
	
	final Semaphore semaphore = new Semaphore(1);

    public Pista(int id) {
    	this.idPista = id;
    }

	public int getIdPista() {
		return idPista;
	}

}

class Aviao implements Runnable {
    private int id;
    private int idPista = gerador.nextInt(2);
    private static final SecureRandom gerador = new SecureRandom();
 
    public Aviao(int id) {
    	this.id = id;
    }
    
	public int getId() {
		return id;
	}

    
	@Override
	public void run() {
		
		acquireSemaforo(idPista);	
		
        System.out.println("AV" +id +" Iniciando processo de decolagem. Na Pista " + idPista);	
        
        manobrar(idPista);
        
        taxiar(idPista);
        
        posicionar(idPista);
        
        acelerar(idPista);
        
        decolar(idPista);
        
        afastar(idPista);
        
        realeaseSemaforo(idPista);
		
	}
	
	private void acquireSemaforo(int idPista) {
		if(idPista == 0){
        	try {
               Aeroporto.p1.semaphore.acquire();
        	} catch (InterruptedException ex) {
                Logger.getLogger(Aviao.class.getName()).log(Level.SEVERE, null, ex);
        	}
        }
        else if(idPista == 1){
        	try {
        		Aeroporto.p2.semaphore.acquire();
            } catch (InterruptedException ex) {
                Logger.getLogger(Aviao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
	}
	private void realeaseSemaforo(int idPista) {
		if(idPista == 0){
			Aeroporto.p1.semaphore.release();
        }
        //Aviao decola em pista 2
        else if(idPista == 1){
        	Aeroporto.p2.semaphore.release();
        }
	}

	private void manobrar(int idPista) {
        try {
        	long tempo =  gerador.nextInt(4000) + 3000;
        	System.out.println("AV" +id +" Manobrando... "  + tempo  + "ms");
            Thread.sleep(tempo);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	}

	private void taxiar(int idPista) {
        try {
        	long tempo = 2000 + gerador.nextInt(3000);
            Thread.sleep((tempo));
            System.out.println("AV" +id +" Taxiando... " + tempo  + "ms");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	}

	private void posicionar(int idPista) {
        try {
        	long tempo =  1000 + gerador.nextInt(3000);
            Thread.sleep(tempo);
            System.out.println("AV" +id +" Posicionando..." + tempo  + "ms");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	}

	private void acelerar(int idPista) {
		long tempo =  3000 + gerador.nextInt(2000);
		System.out.println("AV" +id +" Acelerando.. "  + tempo  + "ms");
        try {
            Thread.sleep(tempo);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	}

	private void decolar(int idPista) {
		long tempo =  4000 + gerador.nextInt(3000);
		System.out.println("AV" +id +" Decolando..." + tempo  + "ms");
        try {
            Thread.sleep(tempo);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	}

	private void afastar(int idPista) {
		long tempo =  2000 + gerador.nextInt(4000);
		System.out.println("AV" +id +" Afastando... Na Pista " + idPista + " Tempo: " + tempo  + "ms");
        try {
            Thread.sleep(tempo);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	}
}