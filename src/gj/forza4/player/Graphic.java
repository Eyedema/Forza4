package gj.forza4.player;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * La classe Graphic è la classe principale che raggruppa tutte le classi per il
 * player grafico.
 *
 * @author Ubaldo Puocci
 */
public class Graphic extends AbstractHumanPlayer {

	private static final long serialVersionUID = 2643435289120530198L;

	/** Istanza della classe Window. */
	Window window;

	/**
	 * Metodo che viene invocato all'inizio di ogni partita. Viene creata una
	 * nuova finestra.
	 * 
	 * @see gj.forza4.player.Player#start(int, int)
	 */
	public void start(int nRighe, int nColonne) {
		Window forza4 = new Window("Forza 4");
		this.window = forza4;
	}

	/**
	 * Metodo che viene invocato ogni volta che il giocatore avversario effettua
	 * una mossa, e che invoca il metodo {@link Window#aiMove(int)} per far
	 * disegnare la mossa.
	 * 
	 * @see gj.forza4.player.Player#tellMove(int)
	 */
	public void tellMove(int a) {
		window.aiMove(a);
	}

	/**
	 * Metodo che viene invocato ogni volta che il giocatore effettua una mossa,
	 * e che invoca il metodo {@link Window#playerMove(int)} per far disegnare
	 * la mossa.
	 * 
	 * @see gj.forza4.player.Player#tellMove(int)
	 */
	@Override
	public void humanPlayerMove(int x) {
		window.playerMove(x);
	}

	/**
	 * La classe Drawer è la classe che si occupa di disegnare la board ed i
	 * segnalini dei giocatori dopo ogni turno.
	 */
	public class Drawer extends JPanel {

		private static final long serialVersionUID = 7973532446329731829L;

		/**
		 * Booleano che viene usato per decidere di che colore disegnare il
		 * segnalino nelle coordinate appena ricevute
		 */
		private boolean enemy;

		/**
		 * Booleano che viene usato per controllare se la finestra è pronta a
		 * ridisegnarsi.
		 */
		private boolean ready = false;

		/** Intero che rappresenta la colonna giocata nel turno corrente. */
		private int col = -1;

		/**
		 * Array multidimensionale di booleani che mantiene lo stato della board
		 * con solo i segnalini dell'AI.
		 */
		private boolean[][] mosseAI = new boolean[7][6];

		/**
		 * Array multidimensionale di booleani che mantiene lo stato della board
		 * con solo i segnalini del player grafico.
		 */
		private boolean[][] mossePlayer = new boolean[7][6];

		/**
		 * Array di interi che mantiene lo stato di ogni colonna. Usato per
		 * controllare se un move è legale o meno.
		 */
		private int[] columnCounts = new int[7];

		/**
		 * Istanzia un nuovo Drawer settando le due board con
		 * {@link #setArrays()}.
		 * 
		 */
		public Drawer() {
			setArrays();
		}

		/**
		 * Override del metodo {@code paintComponent(Graphics g)}. Disegna la
		 * board chiamando il metodo {@link #draw(boolean, int)} e, se la
		 * variabile booleana ready è vera, disegna i segnalini tramite
		 * {@link #updateBoard(Graphics)}.
		 * 
		 * 
		 */
		@Override
		protected void paintComponent(Graphics g) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			super.paintComponent(g2d);
			drawBoard(g2d);
			if (ready) {
				updateBoard(g2d);
			}
		}

		/**
		 * Disegna la board. Prima di disegnare gli assi con
		 * {@link #drawVerticalAxis(Graphics)} e
		 * {@link #drawHorizzontalAxis(Graphics)} riempie di bianco la finestra.
		 * 
		 * @param g
		 *            Istannza della classe Graphics.
		 */
		private void drawBoard(Graphics g) {
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(Color.BLACK);
			drawVerticalAxis(g);
			drawHorizzontalAxis(g);
		}

		/**
		 * Disegna gli assi verticali con un'istruzione do-while.
		 * 
		 * @param g
		 *            Istannza della classe Graphics.
		 * 
		 */
		private void drawVerticalAxis(Graphics g) {
			int i = 0;
			do {
				g.drawLine(i, 0, i, 599);
				i += 100;
			} while (i <= 707);
		}

		/**
		 * Disegna gli assi orizzontali con un'istruzione do-while.
		 *
		 * @param g
		 *            Istannza della classe Graphics.
		 */
		private void drawHorizzontalAxis(Graphics g) {
			int i = 0;
			do {
				g.drawLine(0, i, 699, i);
				i += 100;
			} while (i <= 630);
		}

		/**
		 * Aggiorna la board. Metodo che viene invocato da paintComponent e dal
		 * {@link javax.swing.JComponent#repaint()} ogni volta che la finestra
		 * deve ridisegnarsi. Il metodo scorre gli array multidimensionali con
		 * le mosse dei due giocatori e se trovano una coordinata che non sia
		 * false, disegnano un cerchio del rispettivo colore del giocatore sulla
		 * board.
		 *
		 * @param g
		 *            istanza della classe Graphic, usata per disegnare.
		 */
		public void updateBoard(Graphics g) {
			for (int colonne = 0; colonne < 7; colonne++) {
				for (int righe = 0; righe < 6; righe++) {
					if (this.mossePlayer[colonne][righe] == true) {
						g.setColor(Color.RED);
						g.fillOval(colonne * 100 + 2, righe * 100 + 2, 98, 98);
					} else if (this.mosseAI[colonne][righe] == true) {
						g.setColor(Color.GREEN);
						g.fillOval(colonne * 100 + 2, righe * 100 + 2, 98, 98);
					}
				}
			}
		}

		/**
		 * Aggiunge la mossa appena ricevuta nell'array corrispondente al
		 * giocatore che l'ha compiuta, aggiorna poi il numero di righe libere
		 * per quella colonna ed infine invoca il metodo {@code repaint()} per
		 * far ridisegnare la finestra.
		 * 
		 * @param col
		 *            la colonna in cui inserire il move.
		 */
		private void addMoveToArray(int col) {
			if (enemy) {
				this.mossePlayer[col][5 - columnCounts[col]] = true;
				updateColLibere(this.col);
			} else if (!enemy) {
				this.mosseAI[col][5 - columnCounts[col]] = true;
				updateColLibere(this.col);
			}
			repaint();
		}

		/**
		 * Metodo che viene usato dalla classe del giocatore per comunicare alla
		 * classe Drawer la mossa appena compiuta che va disegnata.
		 *
		 * @param enemy
		 *            descrive chi ha appena effettuato la mossa.
		 * @param col
		 *            descrive la colonna in cui è stata effettuata la mossa
		 * 
		 */
		public void draw(boolean enemy, int col) {
			this.col = col;
			this.enemy = enemy;
			this.ready = true;
			addMoveToArray(col);
		}

		/**
		 * Metodo che viene invocato quando viene istanziata la classe Drawer.
		 * Prepara gli array multidimensionali che mantengono le mosse dei
		 * giocatori.
		 */
		private void setArrays() {
			for (int righe = 0; righe < 7; righe++) {
				for (int colonne = 0; colonne < 6; colonne++) {
					this.mosseAI[righe][colonne] = false;
					this.mossePlayer[righe][colonne] = false;
				}
			}
		}

		/**
		 * Metodo che aggiorna il numero di righe libere per la colonna in cui è
		 * stata appena effettuata la mossa.
		 *
		 * @param move
		 *            colonna di cui vengono aggiornate le colonne libere.
		 * 
		 */
		private void updateColLibere(int move) {
			columnCounts[move]++;
		}

		/**
		 * Metodo che descrive il numero di righe libere per la colonna
		 * selezionata.
		 *
		 * @param move
		 *            colonna di cui ritorna il numero di righe libere.
		 * @return il numero di righe libere per la colonna.
		 */
		private int getColLibere(int move) {
			return columnCounts[move];
		}
	}

	/**
	 * La classe Window è la classe che si occupa di creare la finestra per le
	 * partite e che controlla la mossa giocata con la posizione del mouse.
	 */
	public class Window extends JFrame implements MouseListener, WindowListener {

		private static final long serialVersionUID = 7418537609473404599L;

		/** Istanza della classe Drawer. */
		private Drawer drawer = new Drawer();

		/**
		 * Istanzia una nuova finestra con i parametri indicati.
		 *
		 * @param name
		 *            Nome della finestra da creare.
		 * 
		 */
		public Window(String name) {
			super(name);
			setSize(707, 630);
			setResizable(false);
			setVisible(true);
			setLocationRelativeTo(null);
			setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			init();
		}

		/**
		 * Metodo che aggiunge al JFrame il pannello creato dalla classe Drawer
		 * e che aggiunge il MouseListener e il WindowListener alla finestra.
		 */
		public void init() {
			add(drawer, BorderLayout.CENTER);
			addMouseListener(this);
			addWindowListener(this);
		}

		/**
		 * Override del metodo
		 * {@link java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)}
		 * . Riceve la posizione del mouse e tramite il metodo
		 * {@link #selectMove(int)}.
		 * 
		 */
		@Override
		public void mouseClicked(MouseEvent me) {
			int mousePos = (int) me.getPoint().getX();
			selectMove(mousePos);
		}

		/**
		 * Metodo che riceve la posizione del mouse ed invoca il metodo
		 * {@link Graphic#setMove(int)} in base alla colonna cliccata.
		 *
		 * @param mousePos
		 *            il punto in cui è stato cliccato il mouse.
		 * 
		 */
		private void selectMove(int mousePos) {
			if (mousePos < 101) {
				setMove(0);
			} else if (mousePos < 201 && mousePos > 101) {
				setMove(1);
			} else if (mousePos < 301 && mousePos > 201) {
				setMove(2);
			} else if (mousePos < 401 && mousePos > 301) {
				setMove(3);
			} else if (mousePos < 501 && mousePos > 401) {
				setMove(4);
			} else if (mousePos < 601 && mousePos > 501) {
				setMove(5);
			} else if (mousePos < 707 && mousePos > 601) {
				setMove(6);
			}
		}

		/**
		 * Metodo che comunica alla classe Drawer che l'AI ha effettuato una
		 * mossa nella colonna ricevuta come parametro.
		 *
		 * @param col
		 *            la colonna in cui è stata effettuata la mossa
		 */
		public void aiMove(int col) {
			drawer.draw(true, col);
		}

		/**
		 * Metodo che comunica alla classe Drawer che il giocatore ha effettuato
		 * una mossa nella colonna ricevuta come parametro.
		 *
		 * @param col
		 *            la colonna in cui è stata effettuata la mossa
		 */
		public void playerMove(int col) {
			if (drawer.getColLibere(col) < 6) {
				drawer.draw(false, col);
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
		}

		@Override
		public void windowActivated(WindowEvent arg0) {
		}

		@Override
		public void windowClosed(WindowEvent arg0) {
		}

		/**
		 * Override di {@code WindowListeners.windowClosing(WindowEvent we)}.
		 * Chiede se si vuole davvero chiuedere la finestra.
		 * 
		 * 
		 */
		@Override
		public void windowClosing(WindowEvent arg0) {
			int confirm = JOptionPane.showOptionDialog(null, "Are you sure you want to exit the game?", "Exit",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
			if (confirm == 0) {
				System.exit(0);
			}
		}

		@Override
		public void windowDeactivated(WindowEvent arg0) {
		}

		@Override
		public void windowDeiconified(WindowEvent arg0) {
		}

		@Override
		public void windowIconified(WindowEvent arg0) {
		}

		@Override
		public void windowOpened(WindowEvent arg0) {

		}

	}
}