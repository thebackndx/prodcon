import java.util.concurrent.Semaphore;
class Que 
{
    int item;		   // Con initialized with 0 permits  to ensure put() executes first
    static Semaphore Con = new Semaphore(0);
    static Semaphore Prod = new Semaphore(1);
    
    void get()		  // to get an item from buffer
        {
            try {                // Before consumer can consume an item,   it must acquire a permit from Con
                	Con.acquire();
         	}
            catch (InterruptedException e) 
           {
              System.out.println("InterruptedException caught");
            }
 
             System.out.println("Consumer consumed item: " + item); // consumer consuming an item
 
             Prod.release();    // After consumer consumes the item,  it releases Prod to notify producer
    }
 
    void put(int item)	   // to put an item in buffer
     {
            try {
                		// Before producer can produce an item, it must acquire a permit from Prod
                	Prod.acquire();
            }
            catch (InterruptedException e) 
            {
		System.out.println("InterruptedException caught");
            }
 
           			 // producer producing an item
            this.item = item;
 
            System.out.println("Producer produced item: " + item);
 
            		// After producer produces the item, it releases Con to notify consumer
            Con.release();
	}
}

class Producer implements Runnable 	// Producer class
{
    	Que q;
    	Producer(Que q)
{
            	this.q = q;
            	new Thread(this, "Producer").start();
	}
    	public void run()
        	{
            	for (int i = 0; i < 5; i++)
                		q.put(i);		// producer produces items
        	}
}

class Consumer implements Runnable 	// Consumer class
{
    	Que q;
    	Consumer(Que q)
        	{
            	this.q = q;
            	new Thread(this, "Consumer").start();
        	}
    	public void run()
        	{
            	for (int i = 0; i < 5; i++)
            		q.get();			// consumer get items
        }
}

class Driver					// Driver class
{
    public static void main(String args[])
        {
            Que q = new Que();			// creating buffer queue
            new Consumer(q);			// starting consumer thread
            new Producer(q);			// starting producer thread
        }
  }
