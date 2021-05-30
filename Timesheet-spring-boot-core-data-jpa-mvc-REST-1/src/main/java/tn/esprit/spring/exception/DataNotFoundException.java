package tn.esprit.spring.exception;

public class DataNotFoundException extends Exception {
	    
    private final int status;

    public DataNotFoundException(String message, int status) {
      super(message);
      this.status = status;
    }

    public int getStatus() {
      return status;
    }


}
