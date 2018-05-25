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
    
    public Aviao(int id) {
    	this.id = id;
    }
    
    private static final SecureRandom gerador = new SecureRandom();
    
    int idPista = gerador.nextInt(2);

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
        
        decolar_avi(idPista);
        
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
		System.out.println("AV" +id +" Manobrando... Na Pista " + idPista);
        
        try {
            Thread.sleep(3000 + gerador.nextInt(5000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("AV" +id +" Terminou de manobrar. Na Pista " + idPista);
	}

	private void taxiar(int idPista) {
		System.out.println("AV" +id +" Taxiando... Na Pista " + idPista);
        try {
            Thread.sleep(2000 + gerador.nextInt(4000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("AV" +id +" Terminou de taxiar. Na Pista " + idPista);
	}

	private void posicionar(int idPista) {
		System.out.println("AV" +id +" Posicionando...");
        try {
            Thread.sleep(1000 + gerador.nextInt(4000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("AV" +id +" Terminou de posicionar. Na Pista " + idPista);
	}

	private void acelerar(int idPista) {
		System.out.println("AV" +id +" Acelerando... Na Pista " + idPista);
        try {
            Thread.sleep(3000 + gerador.nextInt(3000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("AV" +id +" Terminou de acelerar. Na Pista " + idPista);
	}

	private void decolar_avi(int idPista) {
		System.out.println("AV" +id +" Decolando... Na Pista " + idPista);
        try {
            Thread.sleep(40 + gerador.nextInt(4000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("AV" +id +" Terminou de decolar. Na Pista " + idPista);
	}

	private void afastar(int idPista) {
		System.out.println("AV" +id +" Afastando... Na Pista " + idPista);
        try {
            Thread.sleep(20 + gerador.nextInt(5000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("AV" +id +" Terminou de se afastar. Na Pista " + idPista);
	}
}