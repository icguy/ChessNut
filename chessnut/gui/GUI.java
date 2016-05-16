/*************************************************
 *  \file     GUI.java
 *  \brief    Chessnut grafikus felhaszn�l�i fel�lete
 *  \note     
 *  \date     2016. m�j. 13.
 *************************************************/
package chessnut.gui;

import java.awt.event.*;


import javax.swing.*;

import chessnut.ILogic;
import chessnut.IPlayer;
import chessnut.Main;
import chessnut.logic.ChessBoard;
import chessnut.logic.PlayerColor;
import chessnut.logic.Position;


//! \brief  GUI f� oszt�ly
public class GUI extends JFrame implements IPlayer
{
	private static final long serialVersionUID = 1111111111111111L;  //!< Nem kell foglalkozni vele, ez a serializable oszt�lyoknak kell, hogy azonos�tani tudj�k magukat
	
	private ILogic logic;                 //!< Ezen a referenci�n tudom a kapcsolatot tartani a j�t�klogik�val
	private PlayerColor myPlayerColor;    //!< Ebben megjegyzem, hogy milyen oldal vagyok, hogy n�h�ny dologr�l el tudjam d�nteni, hogy vonatkozik-e r�m
	private boolean gameStarted = false;  //!< Kezdt�nk-e m�r j�t�kot. Innnent�l nem lehet a men�ben �jat kezdeni. Innent�l lehet kattintani
	
	//! \brief  Konstruktor
	public GUI()
	{
		// Alapvet� be�ll�t�sok
		super("Chessnut");                                    // L�trej�n az ablak
		setSize(800, 600);                                    // Ablakm�ret be�ll�t�sa
		setDefaultCloseOperation(EXIT_ON_CLOSE);              // Alap�rtelmezett kil�p�si be�ll�t�s
		setLayout(null);                                      // Layout
		
		// Men�sor
		JMenuBar menuBar = new JMenuBar();                    // Men�sor l�trej�n
		JMenu menu = new JMenu("Start game");                 // Start game men�pont

		JMenuItem menuItem = new JMenuItem("Start server");   // Start szerver almen�pont
		menuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Szerver ind�t�sa, ha m�g nem fut j�t�k
				if( !gameStarted )
				{
					Main.setupServer();  // Szerver setup
					myPlayerColor = PlayerColor.White;
					gameStarted = true;
				}
			}
		});
		menu.add(menuItem);

		menuItem = new JMenuItem("Connect to server");        // Connect to server j�t�km�d almen�pontja
		menuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Kliens ind�t�sa, ha m�g nem fut j�t�k
				if( !gameStarted )
				{
					InputIPAddrDialog ipDialog = new InputIPAddrDialog();
					String IP = null;
					while ( IP == null )
					{
						IP = ipDialog.getIp();
					}
					
					Main.setupClient(IP);  // Kliens szetup
					myPlayerColor = PlayerColor.Black;
					gameStarted = true;
				}
			}
		});
		menu.add(menuItem);


		menuItem = new JMenuItem("Single player game");       // AI elleni j�t�k almen�pontja
		menuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// AI elleni j�t�k ind�t�sa
				if( !gameStarted )
				{
					Main.setupSinglePlayer(); // Singleplayer setup
					myPlayerColor = PlayerColor.White;
					gameStarted = true;
				}
			}
		});
		menu.add(menuItem);
		
		menuBar.add(menu);

		menuItem = new JMenuItem("Exit");                      // Kil�p� gomb
		menuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});
		menuBar.add(menuItem);

		setJMenuBar(menuBar);

		
		
    // TODO {GUI} Ilyennel kell majd a kattint�st vizsg�lni
		/*
		 * Csin�lni kell ilyenb�l egy akkor�t, ami a sakkt�bla k�p�t lefedi.
		 * Az ezen be�rkezett kattint�st megkapjuk itt ennek a mouseListenerj�ben:
		 */
	//	JPanel inputPanel = new JPanel();
	//	inputPanel.setBounds(30, 30, 200, 200);
	//	inputPanel.setBorder(BorderFactory.createTitledBorder("Input"));
	//	inputPanel.addMouseListener(new MouseAdapter()
	//	{
	//
	//		@Override
	//		public void mousePressed(MouseEvent e)
	//		{
	//			System.out.println("X:" + e.getX() + " Y:" + e.getY());
	//			/*
	//			 * Itt ha megvannak a kattintott koordin�t�k,
	//			 * Ezeket �t kell sz�molni Position-re,
	//			 * �s oda kell adni a logic-nak a click-et.
	//			 * 
	//			 */
	//	
	//	
	//
	//		}
	//	});
	//	add(inputPanel);
		
		

		setVisible(true);               // L�that�v� teszem az ablakot
	}
	
	
	//! \brief  Be�ll�that� a referenci�m a Logic-ra
	@Override
	public void setGameLogic(ILogic logic)
	{
		this.logic = logic;
	}
	
	
	//! \brief  Be�rkez� sakkt�bla lekezel�se
	@Override
	public void setChessboard(ChessBoard chessboard)
	{
		System.out.println("GUI handles setChessboard.");
		
		// TODO {GUI} Be�rkez� sakkt�bla lekezel�se
		
		/*
		 * Ami l�nyeg�ben abb�l �ll, hogy kirajzolom �jra,
		 * Meg megn�zem, hogy highlight-ok vannak-e rajta �s azokat is kirajzolom
		 * 
		 * Ut�bbi a myPlayerColor alapj�n szelekt�lhat�, hogy vonatkozik-e r�m
		 */
		
	}
	
	
	//! \brief  Gyalogv�lt�s k�relem lekezel�se
	@Override
	public void notifyPromotion(Position position)
	{
		System.out.println("GUI handles notifyPromotion.");
		
		// TODO {GUI} Gyalogv�lt�s k�relem lekezel�se.
		
		/*
		 * Ehhez kell valamilyen m�sik fel�let, amit ilyenkor feldobunk, �s n�zz�k rajta, hogy mire kattint az ember,
		 * ut�na azt visszak�ldj�k
		 */
		
	}
	
	
}
