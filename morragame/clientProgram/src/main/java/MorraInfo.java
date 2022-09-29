import java.io.Serializable;

public class MorraInfo implements Serializable {

	public int p1Score = 0, p2Score = 0;
	public int p1Plays = 0, p2Plays = 0;
	public int p1Guess = 0, p2Guess = 0;
	
	boolean p1Moved = false, p2Moved = false;
	
	public int playerCount = 0;
	public boolean have2players = false;
	
	public boolean bothPlayed = false;
	
	public boolean win = false;
	
	public boolean playing = false;
	
	public int cmd = 0;
	
	public boolean p1Again = false, p2Again = false;
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
