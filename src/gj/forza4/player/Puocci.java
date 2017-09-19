package gj.forza4.player;

// TODO: Auto-generated Javadoc
/**
 * La classe Puocci è la classe che implementa l'AI.
 */
public class Puocci implements Player {

	/** La massima profondità dell'albero delle possibili scelte. */
	private final int MAX_DEPTH = 3;

	/** Punteggio assegnato ad un move che porta alla vittoria. */
	private final float WIN_REVENUE = 100;

	/** Punteggio assegnato ad un move che porta alla vittoria. */
	private final float LOSE_REVENUE = -100;

	/** Punteggio assegnato ad un move che porta ad uno stato neutro. */
	private final float UNCERTAIN_REVENUE = 0;

	/** Istanza della classe Board. */
	private Board board;

	/** Booleano usato per controllare se è il primo turno di una partita. */
	private boolean isFirstTurn = false;

	/**
	 * Override del metodo {@code move()}. Se è il primo turno della partita,
	 * ritorna il valore 3. Altrimenti invoca il metodo {@link #makeTurn()} per
	 * decidere dove posizionare il segnalino.
	 * 
	 * 
	 */
	@Override
	public int move() {
		if (isFirstTurn) {
			isFirstTurn = false;
			board.makeMoveAI(3);
			return 3;
		}
		return makeTurn();
	}

	/**
	 * Metodo usato per decidere quale move compiere ad ogni turno della
	 * partita. Viene controllata tutta la board e, se nella colonna appena
	 * controllata è possibile inserire un segnalino, allora viene invocato il
	 * metodo {@link Puocci#moveValue(int)}. Viene scelto il move con valore più
	 * alto, ovvero quello più efficace per lo stato corrente della board.
	 *
	 * @return la colonna in cui inserire il segnalino
	 */
	private int makeTurn() {
		double maxValue = 2.0 * Integer.MIN_VALUE;
		int move = 0;
		for (int colonna = 0; colonna < board.getWidth(); colonna++) {
			if (board.isValidMove(colonna)) {
				double valore = moveValue(colonna);
				if (valore > maxValue) {
					maxValue = valore;
					move = colonna;
					if (valore == WIN_REVENUE) {
						break;
					}
				}
			}
		}
		board.makeMoveAI(move);
		return move;
	}

	/**
	 * Metodo usato per attribuire ad ogni possibile move, un punteggio. Fa uso
	 * del metodo {@link #alphabeta(int, double, double, boolean)}.
	 *
	 * @param colonna
	 *            la colonna della quale si deve calcolare il valore.
	 * @return il valore della mossa.
	 */
	private double moveValue(int colonna) {
		board.makeMoveAI(colonna);
		double valore = alphabeta(MAX_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
		board.undoMoveAI(colonna);
		return valore;
	}

	/**
	 * Il metodo implementa la potatura alfa-beta, un algoritmo di ricerca
	 * ricorsivo. I due valori su cui si basa l'algoritmo sono {@code minValue}
	 * e {@code maxValue}. Essi rappresentano rispettivamente il punteggimo
	 * minimo che l'avversario può raggiungere ed il punteggio massimo che l'AI
	 * può raggiungere a partire da un determinato stato della board. Questi
	 * valori vengono inizializzati rispettivamente a -infinito e +infinito e
	 * vengono aggiornati man mano che si controlla un nuovo nodo dell'albero di
	 * ricerca. Se in un dato nodo {@code minValue} diventa maggiore di
	 * {@code maxValue}, la ricerca si ferma poichè si arriva in uno stato della
	 * board in cui il giocatore non può vincere. La profondità della ricerca (e
	 * quindi l'altezza massima dell'albero di ricerca da generare) viene
	 * passata come parametro.
	 *
	 * @param maxDepth
	 *            la profondità massima della ricerca.
	 * 
	 * @param minValue
	 *            il punteggimo minimo che l'avversario può raggiungere nello
	 *            stato corrente della board.
	 * @param maxValue
	 *            il punteggimo massimo che l'AI può raggiungere nello stato
	 *            corrente della board.
	 * @param maximizingPlayer
	 *            booleano che controlla se il player di cui valutare la mossa è
	 *            il giocatore massimizzante o meno.
	 * 
	 * @return il valore attribuito alla miglior mossa trovata.
	 */
	private double alphabeta(int maxDepth, double minValue, double maxValue, boolean maximizingPlayer) {
		boolean hasWinner = board.hasWinner();
		if (maxDepth == 0 || hasWinner) {
			double punteggio = 0;
			if (hasWinner) {
				punteggio = board.playerIsWinner() ? LOSE_REVENUE : WIN_REVENUE;
			} else {
				punteggio = UNCERTAIN_REVENUE;
			}
			return punteggio / (MAX_DEPTH + 1);

		}
		if (maximizingPlayer) {
			for (int colonna = 0; colonna < board.getWidth(); colonna++) {
				if (board.isValidMove(colonna)) {
					board.makeMoveAI(colonna);
					minValue = Math.max(minValue, alphabeta(maxDepth - 1, minValue, maxValue, true));
					board.undoMoveAI(colonna);
					if (maxValue <= minValue) {
						break;
					}
				}
			}
			return minValue;
		} else {
			for (int colonna = 0; colonna < board.getWidth(); colonna++) {
				if (board.isValidMove(colonna)) {
					board.makeMovePlayer(colonna);
					maxValue = Math.min(maxValue, alphabeta(maxDepth - 1, minValue, maxValue, false));
					board.undoMovePlayer(colonna);
					if (maxValue <= minValue) {
						break;
					}
				}
			}
			return maxValue;
		}

	}

	/**
	 * Metodo che viene invocato all'inizio di ogni partita. Crea una nuova
	 * istanza della classe Board e si assicura che il booleano che controlla se
	 * è il primo turno, sia true.
	 * 
	 */
	@Override
	public void start(int nRighe, int nColonne) {
		Board board = new Board(nRighe, nColonne);
		this.board = board;
		isFirstTurn = true;
	}

	/**
	 * Metodo che viene invocato ogni volta che il giocatore avversario effettua
	 * una mossa. Questa viene aggiunta alla board con l'utilizzo del metodo
	 * {@link Board#makeMovePlayer(int)}.
	 * 
	 */
	@Override
	public void tellMove(int colonna) {
		board.makeMovePlayer(colonna);

	}

}
