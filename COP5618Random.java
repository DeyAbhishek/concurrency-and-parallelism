



import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

/**This class uses the same mechanism to generate random longs as java.util.Random except that it
 * is not thread safe.
 */

public class COP5618Random {
    private static final long multiplier = 0x5DEECE66DL;
    private static final long addend = 0xBL;
    private static final long mask = (1L << 48) - 1;
    private AtomicLong seed;

	public COP5618Random(long seed){
		this.seed =new AtomicLong( (seed ^ multiplier) & mask);
	}
	
	public long nextLong(){
		int bits = 32;
                long seednew,seedold;
                int valold,valnew;
                
                do{
		seedold=seed.get();
		seednew = (seedold * multiplier + addend) & mask;
		valold = (int)(seednew >>> (48 - bits));
		seednew = (seednew * multiplier + addend) & mask;
		valnew = (int)(seednew >>> (48 - bits));
		}while(!seed.compareAndSet(seedold,seednew));	
                return  ((long)valold << 32) + valnew;
	}

	
/* This is the implementation copied out of java.util.Random
 * 
    protected int next(int bits) {
        long oldseed, nextseed;
        AtomicLong seed = this.seed;
        do {
            oldseed = seed.get();
            nextseed = (oldseed * multiplier + addend) & mask;
        } while (!seed.compareAndSet(oldseed, nextseed));
        return (int)(nextseed >>> (48 - bits));
    }

	    public long nextLong() {
	        // it's okay that the bottom word remains signed.
	        return ((long)(next(32)) << 32) + next(32);
	    }
	    
	    
        public int nextInt() {
            return next(32);
        }


*/	
	
}
