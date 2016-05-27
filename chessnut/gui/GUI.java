/*************************************************
 *  \file     GUI.java
 *  \brief    Chessnut grafikus felhaszn�l�i fel�lete
 *  \note     
 *  \date     2016. m�j. 13.
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


//! \brief  GUI f� oszt�ly
public class GUI extends JFrame implements IPlayer
{
	private static final long serialVersionUID = 1111111111111111L;  //!< Nem kell foglalkozni vele, ez a serializable oszt�lyoknak kell, hogy azonos�tani tudj�k magukat
	
	private ILogic logic;                 //!< Ezen a referenci�n tudom a kapcsolatot tartani a j�t�klogik�val
	private PlayerColor myPlayerColor;    //!< Ebben megjegyzem, hogy milyen oldal vagyok, hogy n�h�ny dologr�l el tudjam d�nteni, hogy vonatkozik-e r�m
	private boolean gameStarted = false;  //!< Kezdt�nk-e m�r j�t�kot. Innnent�l nem lehet a men�ben �jat kezdeni. Innent�l lehet kattintani
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
		// Alapvet� be�ll�t�sok
		super("Chessnut");                                    // L�trej�n az ablak
		setSize(600, 600);                                    // Ablakm�ret be�ll�t�sa
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
	//		}
	//	});
	//	add(inputPanel);
		

		try {
			BBishop = ImageIO
					.read(new FileInputStream("D:\\BME\\MSC\\1 f�l�v\\Beagyazott szoftech\\H�zi\\chessnut\\piece\\BBishop.png"));
			BKing = ImageIO
					.read(new FileInputStream("D:\\BME\\MSC\\1 f�l�v\\Beagyazott szoftech\\H�zi\\chessnut\\piece\\BKing.png"));
			BKnight = ImageIO
					.read(new FileInputStream("D:\\BME\\MSC\\1 f�l�v\\Beagyazott szoftech\\H�zi\\chessnut\\piece\\BKnight.png"));
			BPawn = ImageIO
					.read(new FileInputStream("D:\\BME\\MSC\\1 f�l�v\\Beagyazott szoftech\\H�zi\\chessnut\\piece\\BPawn.png"));
			BQueen = ImageIO
					.read(new FileInputStream("D:\\BME\\MSC\\1 f�l�v\\Beagyazott szoftech\\H�zi\\chessnut\\piece\\BQueen.png"));
			BRook = ImageIO
					.read(new FileInputStream("D:\\BME\\MSC\\1 f�l�v\\Beagyazott szoftech\\H�zi\\chessnut\\piece\\BRook.png"));
			WBishop = ImageIO
					.read(new FileInputStream("D:\\BME\\MSC\\1 f�l�v\\Beagyazott szoftech\\H�zi\\chessnut\\piece\\WBishop.png"));
			WKing = ImageIO
					.read(new FileInputStream("D:\\BME\\MSC\\1 f�l�v\\Beagyazott szoftech\\H�zi\\chessnut\\piece\\WKing.png"));
			WKnight = ImageIO
					.read(new FileInputStream("D:\\BME\\MSC\\1 f�l�v\\Beagyazott szoftech\\H�zi\\chessnut\\piece\\WKnight.png"));
			WPawn = ImageIO
					.read(new FileInputStream("D:\\BME\\MSC\\1 f�l�v\\Beagyazott szoftech\\H�zi\\chessnut\\piece\\WPawn.png"));
			WQueen = ImageIO
					.read(new FileInputStream("D:\\BME\\MSC\\1 f�l�v\\Beagyazott szoftech\\H�zi\\chessnut\\piece\\WQueen.png"));
			WRook = ImageIO
					.read(new FileInputStream("D:\\BME\\MSC\\1 f�l�v\\Beagyazott szoftech\\H�zi\\chessnut\\piece\\WRook.png"));
		} catch (Exception ex) {
		}

		
		
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
		this.chessBoard = chessboard;
		/*if (myPlayerColor==)
		{
			
		}*/
		repaint();
		
		
		setVisible(true);
		
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
