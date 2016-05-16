/*************************************************
 *  \file     GUI.java
 *  \brief    Chessnut grafikus felhasználói felülete
 *  \note     
 *  \date     2016. máj. 13.
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


//! \brief  GUI fõ osztály
public class GUI extends JFrame implements IPlayer
{
	private static final long serialVersionUID = 1111111111111111L;  //!< Nem kell foglalkozni vele, ez a serializable osztályoknak kell, hogy azonosítani tudják magukat
	
	private ILogic logic;                 //!< Ezen a referencián tudom a kapcsolatot tartani a játéklogikával
	private PlayerColor myPlayerColor;    //!< Ebben megjegyzem, hogy milyen oldal vagyok, hogy néhány dologról el tudjam dönteni, hogy vonatkozik-e rám
	private boolean gameStarted = false;  //!< Kezdtünk-e már játékot. Innnentõl nem lehet a menüben újat kezdeni. Innentõl lehet kattintani
	
	//! \brief  Konstruktor
	public GUI()
	{
		// Alapvetõ beállítások
		super("Chessnut");                                    // Létrejön az ablak
		setSize(800, 600);                                    // Ablakméret beállítása
		setDefaultCloseOperation(EXIT_ON_CLOSE);              // Alapértelmezett kilépési beállítás
		setLayout(null);                                      // Layout
		
		// Menüsor
		JMenuBar menuBar = new JMenuBar();                    // Menüsor létrejön
		JMenu menu = new JMenu("Start game");                 // Start game menüpont

		JMenuItem menuItem = new JMenuItem("Start server");   // Start szerver almenüpont
		menuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Szerver indítása, ha még nem fut játék
				if( !gameStarted )
				{
					Main.setupServer();  // Szerver setup
					myPlayerColor = PlayerColor.White;
					gameStarted = true;
				}
			}
		});
		menu.add(menuItem);

		menuItem = new JMenuItem("Connect to server");        // Connect to server játékmód almenüpontja
		menuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Kliens indítása, ha még nem fut játék
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


		menuItem = new JMenuItem("Single player game");       // AI elleni játék almenüpontja
		menuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// AI elleni játék indítása
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

		menuItem = new JMenuItem("Exit");                      // Kilépõ gomb
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

		
		
    // TODO {GUI} Ilyennel kell majd a kattintást vizsgálni
		/*
		 * Csinálni kell ilyenbõl egy akkorát, ami a sakktábla képét lefedi.
		 * Az ezen beérkezett kattintást megkapjuk itt ennek a mouseListenerjében:
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
	//			 * Itt ha megvannak a kattintott koordináták,
	//			 * Ezeket át kell számolni Position-re,
	//			 * és oda kell adni a logic-nak a click-et.
	//			 * 
	//			 */
	//	
	//	
	//
	//		}
	//	});
	//	add(inputPanel);
		
		

		setVisible(true);               // Láthatóvá teszem az ablakot
	}
	
	
	//! \brief  Beállítható a referenciám a Logic-ra
	@Override
	public void setGameLogic(ILogic logic)
	{
		this.logic = logic;
	}
	
	
	//! \brief  Beérkezõ sakktábla lekezelése
	@Override
	public void setChessboard(ChessBoard chessboard)
	{
		System.out.println("GUI handles setChessboard.");
		
		// TODO {GUI} Beérkezõ sakktábla lekezelése
		
		/*
		 * Ami lényegében abból áll, hogy kirajzolom újra,
		 * Meg megnézem, hogy highlight-ok vannak-e rajta és azokat is kirajzolom
		 * 
		 * Utóbbi a myPlayerColor alapján szelektálható, hogy vonatkozik-e rám
		 */
		
	}
	
	
	//! \brief  Gyalogváltás kérelem lekezelése
	@Override
	public void notifyPromotion(Position position)
	{
		System.out.println("GUI handles notifyPromotion.");
		
		// TODO {GUI} Gyalogváltás kérelem lekezelése.
		
		/*
		 * Ehhez kell valamilyen másik felület, amit ilyenkor feldobunk, és nézzük rajta, hogy mire kattint az ember,
		 * utána azt visszaküldjük
		 */
		
	}
	
	
}
