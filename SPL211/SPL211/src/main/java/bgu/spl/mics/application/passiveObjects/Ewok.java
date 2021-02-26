package bgu.spl.mics.application.passiveObjects;

/**
 * Passive data-object representing a forest creature summoned when HanSolo and C3PO receive AttackEvents.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add fields and methods to this class as you see fit (including public methods).
 */
public class Ewok {
    //@inv: getSerialNumber() >=0

	int serialNumber;
	boolean available;

	//constructor
    public Ewok(int serialNumber){
        this.serialNumber =  serialNumber;
        this.available = true;
    }

    /** Getter function - return serialNumber
     * @PRE: None
     * @POST: @POST(getSerialNumber()) == @PRE(getSerialNumber())
     */
    public int getSerialNumber(){
        return serialNumber;
    }

    /** return available status
     * @PRE: None@PRE: None
     * @POST: @POST(getSerialNumber()) == @PRE(getSerialNumber())
     */
    public boolean getAvailable(){
        return available;
    }

    /**Acquires an Ewok
     * @PRE: getAvailable() == true
     * @POST: getAvailable() == false
     */
    public void acquire() {
        if(!getAvailable())
            throw new IllegalStateException("The Ewok is not available");
        available = false;
    }

    /**
     * release an Ewok
     * @PRE: getAvailable() == false
     * @POST: getAvailable() == true
     */
    public void release() {
        if(getAvailable())
            throw new IllegalStateException("The Ewok is not acquired");
        available = true;
    }
}
