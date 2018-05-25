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
	
	final static Semaphore semaphore1 = new Semaphore(1);
    final static Semaphore semaphore2 = new Semaphore(1);
    
    // Lista de avioes designados para decolar na pista
    private LinkedList<Aviao> avioes = new LinkedList<Aviao>();

    public Pista(int id, LinkedList<Aviao> l) {
    	this.idPista = id;
        this.avioes = l;
    }

    @Override
    public void run() {
    	ExecutorService executorService = Executors.newCachedThreadPool();

    	while(avioes.isEmpty() != true) {
    		Aviao nextOnFiFo = avioes.remove();
    		// remove aviao decolado da lista
    		executorService.execute(nextOnFiFo);
    	}
    	executorService.shutdown();
        try {
			executorService.awaitTermination(2, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
	public Aviao remove() {
		return this.avioes.remove(0);
	}

	public boolean isEmpty() {
		return this.avioes.size() == 0;
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
                Pista.semaphore1.acquire();
        	} catch (InterruptedException ex) {
                Logger.getLogger(Aviao.class.getName()).log(Level.SEVERE, null, ex);
        	}
        }
        else if(idPista == 1){
        	try {
        		Pista.semaphore2.acquire();
            } catch (InterruptedException ex) {
                Logger.getLogger(Aviao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
	}
	private void realeaseSemaforo(int idPista) {
		if(idPista == 0){
			Pista.semaphore1.release();
        }
        //Aviao decola em pista 2
        else if(idPista == 1){
        	Pista.semaphore2.release();
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