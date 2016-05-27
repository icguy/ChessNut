/*************************************************
 *  \file     GUI.java
 *  \brief    Chessnut grafikus felhasználói felülete
 *  \note     
 *  \date     2016. máj. 13.
 *************************************************/
package chessnut.gui;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;

import javax.imageio.ImageIO;
import javax.swing.*;

import chessnut.ILogic;
import chessnut.IPlayer;
import chessnut.Main;
import chessnut.logic.ChessBoard;
import chessnut.logic.PlayerColor;
import chessnut.logic.Position;
import chessnut.logic.pieces.Piece;


//! \brief  GUI fõ osztály
public class GUI extends JFrame implements IPlayer
{
	private static final long serialVersionUID = 1111111111111111L;  //!< Nem kell foglalkozni vele, ez a serializable osztályoknak kell, hogy azonosítani tudják magukat
	
	private ILogic logic;                 //!< Ezen a referencián tudom a kapcsolatot tartani a játéklogikával
	private PlayerColor myPlayerColor;    //!< Ebben megjegyzem, hogy milyen oldal vagyok, hogy néhány dologról el tudjam dönteni, hogy vonatkozik-e rám
	private boolean gameStarted = false;  //!< Kezdtünk-e már játékot. Innnentõl nem lehet a menüben újat kezdeni. Innentõl lehet kattintani
	private ChessBoard chessBoard;
	
	BufferedImage BBishop = null;
	BufferedImage BKing = null;
	BufferedImage BKnight = null;
	BufferedImage BPawn = null;
	BufferedImage BQueen = null;
	BufferedImage BRook = null;
	BufferedImage WBishop = null;
	BufferedImage WKing = null;
	BufferedImage WKnight = null;
	BufferedImage WPawn = null;
	BufferedImage WQueen = null;
	BufferedImage WRook = null;
	
	//! \brief  Konstruktor
	public GUI()
	{
		// Alapvetõ beállítások
		super("Chessnut");                                    // Létrejön az ablak
		setSize(600, 600);                                    // Ablakméret beállítása
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
	//		}
	//	});
	//	add(inputPanel);
		

		try {
			BBishop = ImageIO
					.read(new FileInputStream("D:\\BME\\MSC\\1 félév\\Beagyazott szoftech\\Házi\\chessnut\\piece\\BBishop.png"));
			BKing = ImageIO
					.read(new FileInputStream("D:\\BME\\MSC\\1 félév\\Beagyazott szoftech\\Házi\\chessnut\\piece\\BKing.png"));
			BKnight = ImageIO
					.read(new FileInputStream("D:\\BME\\MSC\\1 félév\\Beagyazott szoftech\\Házi\\chessnut\\piece\\BKnight.png"));
			BPawn = ImageIO
					.read(new FileInputStream("D:\\BME\\MSC\\1 félév\\Beagyazott szoftech\\Házi\\chessnut\\piece\\BPawn.png"));
			BQueen = ImageIO
					.read(new FileInputStream("D:\\BME\\MSC\\1 félév\\Beagyazott szoftech\\Házi\\chessnut\\piece\\BQueen.png"));
			BRook = ImageIO
					.read(new FileInputStream("D:\\BME\\MSC\\1 félév\\Beagyazott szoftech\\Házi\\chessnut\\piece\\BRook.png"));
			WBishop = ImageIO
					.read(new FileInputStream("D:\\BME\\MSC\\1 félév\\Beagyazott szoftech\\Házi\\chessnut\\piece\\WBishop.png"));
			WKing = ImageIO
					.read(new FileInputStream("D:\\BME\\MSC\\1 félév\\Beagyazott szoftech\\Házi\\chessnut\\piece\\WKing.png"));
			WKnight = ImageIO
					.read(new FileInputStream("D:\\BME\\MSC\\1 félév\\Beagyazott szoftech\\Házi\\chessnut\\piece\\WKnight.png"));
			WPawn = ImageIO
					.read(new FileInputStream("D:\\BME\\MSC\\1 félév\\Beagyazott szoftech\\Házi\\chessnut\\piece\\WPawn.png"));
			WQueen = ImageIO
					.read(new FileInputStream("D:\\BME\\MSC\\1 félév\\Beagyazott szoftech\\Házi\\chessnut\\piece\\WQueen.png"));
			WRook = ImageIO
					.read(new FileInputStream("D:\\BME\\MSC\\1 félév\\Beagyazott szoftech\\Házi\\chessnut\\piece\\WRook.png"));
		} catch (Exception ex) {
		}

		
		
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
		this.chessBoard = chessboard;
		/*if (myPlayerColor==)
		{
			
		}*/
		repaint();
		
		
		setVisible(true);
		
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
	
	public void paint(Graphics g) {
        super.paint(g);  // fixes the immediate problem.
        Graphics2D g2 = (Graphics2D) g;
        
        Color darkBrown = new Color(139, 69, 19);
		Color lightBrown = new Color(232, 194, 145);
		
		//g.setColor(lightBrown);
		//g.fillRect(100, 100, 100, 200);
		int width=getContentPane().getWidth();
		int height=getContentPane().getHeight();
		int yOffset = getHeight() - getContentPane().getHeight();

		int size;

		if ( width <=height)
		{
			size=width/8;
		}
		else
			size=height/8;
		
		for (int j = 0; j < 8; j++)
		{
			for (int i = 0; i < 8; i++) 
			
			{
				if ((i + j) % 2 == 0)
				{					
					g.setColor(darkBrown);
				}
				else
				{
					g.setColor(lightBrown);
				}
				int y = (7-i) * size + yOffset;
				int x = j * size;
				g.fillRect( x, y, size, size);
				
				if (this.chessBoard != null)
				{
					Piece p = chessBoard.getPiece( new Position(i, j) );
					if (p != null)
					{
						System.out.println("no para");
						
						if (p.toString() == "B")
						{
							g.drawImage(WBishop, x, y, size, size, null);
						}
						else if (p.toString() == "b")
						{
							g.drawImage(BBishop, x, y, size, size, null);
						}
						else if (p.toString() == "K")
						{
							g.drawImage(WKing, x, y, size, size, null);
						}
						else if (p.toString() == "k")
						{
							g.drawImage(BKing, x, y, size, size, null);
						}	
						else if (p.toString() == "R")
						{
							g.drawImage(WRook, x, y, size, size, null);
						}	
						else if (p.toString() == "r")
						{
							g.drawImage(BRook, x, y, size, size, null);
						}	
						else if (p.toString() == "N")
						{
							g.drawImage(WKnight, x, y, size, size, null);
						}	
						else if (p.toString() == "n")
						{
							g.drawImage(BKnight, x, y, size, size, null);
						}	
						else if (p.toString() == "Q")
						{
							g.drawImage(WQueen, x, y, size, size, null);
						}	
						else if (p.toString() == "q")
						{
							g.drawImage(BQueen, x, y, size, size, null);
						}	
						else if (p.toString() == "P")
						{
							g.drawImage(WPawn, x, y, size, size, null);
						}	
						else if (p.toString() == "p")
						{
							g.drawImage(BPawn, x, y, size, size, null);
						}

					}
				}
				
				
			}
			
		}
		

	}
	
}
