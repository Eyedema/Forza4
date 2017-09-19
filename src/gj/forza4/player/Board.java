package gj.forza4.player;

// TODO: Auto-generated Javadoc
/**
 * La classe Board è la classe che tiene in memoria la board per l'AI.
 * 
 * @author Ubaldo Puocci
 */
public class Board {

	/** Segnalino che rappresenta la cella vuota. */
	private final byte NOBODY = 0;

	/** Segnalino che rappresenta la cella occupata dal giocatore. */
	private final byte PLAYER = 1;

	/** Segnalino che rappresenta la cella occupata dall'AI. */
	private final byte AI = 2;

	/** Numero di segnalini in fila che occorrono per vincere. */
	private final byte WIN_LENGTH = 4;

	/** Altezza della board. */
	private int height;

	/** Larghezza della board. */
	private int width;

	/** Array multidimensionale che rappresenta la board. */
	private byte[][] board;

	/**
	 * Array di interi che mantiene lo stato di ogni colonna. Usato per
	 * controllare se un move è legale o meno.
	 */
	private int[] columnCounts;

	/**
	 * Istanzia una nuova Board creando l'array multidimensionale di altezza e
	 * larghezza passati come parametri.
	 *
	 * @param height
	 *            l'altezza
	 * 
	 * @param width
	 *            la larghezza
	 * 
	 */
	public Board(int height, int width) {
		this.height = height;
		this.width = width;
		this.board = new byte[width][height];
		this.columnCounts = new int[width];
	}

	/**
	 * Metodo che controlla se un move è valido o meno.
	 *
	 * @param column
	 *            colonna in cui è il move da controllare
	 * 
	 * @return true, se il move è valido. False altrimenti.
	 */
	public boolean isValidMove(int column) {
		return columnCounts[column] < height;
	}

	/**
	 * Aggiunge il move del player alla board tramite il metodo
	 * {@link #makeMove(int, boolean)}.
	 *
	 * @param column
	 *            colonna in cui inserire il segnalino
	 * @return true, se andato a buon fine. False altrimenti.
	 */
	public boolean makeMovePlayer(int column) {
		return makeMove(column, true);
	}

	/**
	 * Aggiunge il move dell'AI alla board tramite il metodo
	 * {@link #makeMove(int, boolean)}.
	 *
	 * @param column
	 *            colonna in cui inserire il segnalino
	 * @return true, se andato a buon fine. False altrimenti.
	 */
	public boolean makeMoveAI(int column) {
		return makeMove(column, false);
	}

	/**
	 * Rimuove il move del player alla board tramite il metodo
	 * {@link #undoMove(int, boolean)}.
	 *
	 * @param column
	 *            colonna in cui eliminare il segnalino
	 * @return true, se andato a buon fine. False altrimenti.
	 */
	public boolean undoMovePlayer(int column) {
		return undoMove(column, true);
	}

	/**
	 * Rimuove il move del player alla board tramite il metodo
	 * {@link #undoMove(int, boolean)}.
	 *
	 * @param column
	 *            colonna in cui eliminare il segnalino
	 * @return true, se andato a buon fine. False altrimenti.
	 */
	public boolean undoMoveAI(int column) {
		return undoMove(column, false);
	}

	/**
	 * Metodo che aggiunge alla board il segnalino nella colonna passata come
	 * parametro.
	 *
	 * @param column
	 *            colonna in cui inserire il segnalino
	 * @param player
	 *            booleano che descrive il player che ha effettuato la mossa
	 * @return true, se andato a buon fine. False altrimenti.
	 */
	boolean makeMove(int column, boolean player) {
		if (columnCounts[column] < height) {
			byte sign = player ? PLAYER : AI;
			board[column][columnCounts[column]++] = sign;
			return true;
		}
		return false;
	}

	/**
	 * Metodo che rimuove dalla board il segnalino nella colonna passata come
	 * parametro.
	 *
	 * @param column
	 *            colonna in cui eliminare il segnalino
	 * @param player
	 *            booleano che descrive il player che ha effettuato la mossa
	 * @return true, se andato a buon fine. False altrimenti.
	 */
	boolean undoMove(int column, boolean player) {
		if (columnCounts[column] > 0) {
			// if player then sign = PLAYER, if !player then sign = AI
			byte sign = player ? PLAYER : AI;
			if (board[column][columnCounts[column] - 1] == sign) {
				board[column][columnCounts[column] - 1] = NOBODY;
				columnCounts[column]--;
				return true;
			}
		}
		return false;
	}

	/**
	 * Ritorna la larghezza della board.
	 *
	 * @return la larghezza della board.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Controlla se c'è un possibile vincitore nello stato corrente della board.
	 *
	 * @return true, se andato a buon fine. Falso altrimenti.
	 */
	public boolean hasWinner() {
		return getWinner() != NOBODY;
	}

	/**
	 * Metodo che controlla lo stato corrente della board e ritorna il
	 * vincitore. Il metodo esegue delle scansioni della board dall'alto verso
	 * il basso, controllando se c'è una sequenza che può far vincere uno dei
	 * due player. Il metodo prima controlla se la sequenza esiste in verticale,
	 * poi in orizzontale, poi in obliquo da sinistra a destra ed infine da
	 * destra verso sinistra. Viene selezionata una cella e viene controllata la
	 * cella confinante, se è dello stesso player della cella precedente,
	 * continua a controllare le celle vicine finchè non arriva ad avere una
	 * sequenza di segnalini uguale a {@link #WIN_LENGTH}.
	 *
	 * @return il segnalino che rappresenta il vincitore.
	 */
	public byte getWinner() {
		// VERTICAL CHECK
		for (int x = 0; x < width; x++) {
			for (int y = 0; y <= height - WIN_LENGTH; y++) {
				boolean playerWin = true;
				boolean aiWin = true;
				for (int w = 0; w < WIN_LENGTH; w++) {
					if (playerWin && board[x][y + w] != PLAYER) {
						playerWin = false;
					}
					if (aiWin && board[x][y + w] != AI) {
						aiWin = false;
					}
				}
				if (playerWin) {
					return PLAYER;
				} else if (aiWin) {
					return AI;
				}
			}
		}
		// LINEAR CHECK
		for (int y = 0; y < height; y++) {
			for (int x = 0; x <= width - WIN_LENGTH; x++) {
				boolean playerWin = true;
				boolean aiWin = true;
				for (int w = 0; w < WIN_LENGTH; w++) {
					if (playerWin && board[x + w][y] != PLAYER) {
						playerWin = false;
					}
					if (aiWin && board[x + w][y] != AI) {
						aiWin = false;
					}
				}
				if (playerWin) {
					return PLAYER;
				} else if (aiWin) {
					return AI;
				}
			}
		}
		// OBLIQUI DA SINISTRA A DESTRA PARTENDO DA [0,0]
		for (int x = 0; x <= width - WIN_LENGTH; x++) {
			for (int y = 0; y <= height - WIN_LENGTH; y++) {
				boolean playerWin = true;
				boolean aiWin = true;
				for (int w = 0; w < WIN_LENGTH; w++) {
					if (playerWin && board[x + w][y + w] != PLAYER) {
						playerWin = false;
					}
					if (aiWin && board[x + w][y + w] != AI) {
						aiWin = false;
					}
				}
				if (playerWin) {
					return PLAYER;
				} else if (aiWin) {
					return AI;
				}
			}
		}
		// OBLIQUI DA DESTRA A SINISTRA PARTENDO DA [0,6]
		for (int x = width - 1; x >= WIN_LENGTH - 1; x--) {
			for (int y = 0; y <= height - WIN_LENGTH; y++) {
				boolean playerWin = true;
				boolean aiWin = true;
				for (int w = 0; w < WIN_LENGTH; w++) {
					if (playerWin && board[x - w][y + w] != PLAYER) {
						playerWin = false;
					}
					if (aiWin && board[x - w][y + w] != AI) {
						aiWin = false;
					}
				}
				if (playerWin) {
					return PLAYER;
				} else if (aiWin) {
					return AI;
				}
			}
		}

		return NOBODY;
	}

	/**
	 * Controlla se il Player è il vincitore del corrente stato della board.
	 *
	 * @return true, se andato a buon fine. Falso altrimenti.
	 */
	public boolean playerIsWinner() {
		return getWinner() == PLAYER;
	}

	/**
	 * Controlla se il corrente stato della board determina un pareggio.
	 *
	 * @return true, se andato a buon fine. Falso altrimenti.
	 */
	public boolean isTie() {
		return isBoardFull() && getWinner() == NOBODY;
	}

	/**
	 * Controlla se la board allo stato corrente è piena.
	 *
	 * @return true, se andato a buon fine. Falso altrimenti.
	 */
	boolean isBoardFull() {
		boolean emptyColumnFound = false;
		for (int x = 0; x < width; x++) {
			if (columnCounts[x] < height) {
				emptyColumnFound = true;
				break;
			}
		}
		return !emptyColumnFound;
	}
}
