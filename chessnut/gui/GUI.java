/*************************************************
 *  \file     GUI.java
 *  \brief    Chessnut grafikus felhasználói felülete
 *  \note     
 *  \date     2016. máj. 13.
 *************************************************/
package chessnut.gui;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;

import chessnut.ILogic;
import chessnut.IPlayer;
import chessnut.Main;
import chessnut.logic.ChessBoard;
import chessnut.logic.ChessBoard.SelectionType;
import chessnut.logic.PlayerColor;
import chessnut.logic.Position;
import chessnut.logic.pieces.*;


/**
 * GUI fõ osztály
 */
public class GUI extends JFrame implements IPlayer
{
	/**  Egyedi magicnumber a sorosításhoz   */
	private static final long serialVersionUID = 1111111111111111L;  //!< Nem kell foglalkozni vele, ez a serializable osztályoknak kell, hogy azonosítani tudják magukat
	
	/** Ezen a referencián tudom a kapcsolatot tartani a játéklogikával */
	private ILogic logic;
	
	/** Ebben megjegyzem, hogy milyen oldal vagyok, hogy néhány dologról el tudjam dönteni, hogy vonatkozik-e rám*/
	private PlayerColor myPlayerColor;
	
	/** Játék kezdése. Innentõl lehet kattintani */
	private boolean gameStarted = false;
	
	/**  */
	private ChessBoard chessBoard;
	
	/** Sakkfigurák képei */
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
	/** 
	 * GUI konstruktor
	 */
	public GUI()
	{
		/** Alapvetõ beállítások */
		super("Chessnut");                                    // Létrejön az ablak
		setSize(600, 600);                                    // Ablakméret beállítása
		setDefaultCloseOperation(EXIT_ON_CLOSE);              // Alapértelmezett kilépési beállítás
		setLayout(null);                                      // Layout
		
		/** Menüsor, amiben létrehozunk egy "Start game" menüpontot,
		 * melybõl legördülõ listából kiválaszthatjuk a játékmódot:
		 * Szerver indítás
		 * Csatlakozás szerverhez
		 * Gép elleni játék */
		JMenuBar menuBar = new JMenuBar();                    // Menüsor létrejön
		JMenu menu = new JMenu("Start game");                 // Start game menüpont
		
		/** Szerverindítás almenüpont */
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
		
		/** Szerverhez csatlakozás almenüpont */
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

		/** Gép elleni játék almenüpontja */
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

		/** Kilépés menüpont */
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

		/** Sakktáblán való kattintás helyének kiszámítása */
		addMouseListener(new MouseAdapter()
		{
		     @Override
		     public void mousePressed(MouseEvent e) {
		        System.out.println("X" + e.getX() + " Y" + e.getY() );
		        
		        int width=getContentPane().getWidth();
				int height=getContentPane().getHeight();
				int clickX=e.getX();
				int clickY=e.getY();
				int yOffset = getHeight() - getContentPane().getHeight();
				clickY=clickY - yOffset;


				int size;

				if ( width <=height)
				{
					size=width/8;
				}
				else
					size=height/8;
				
		        int posX=clickX/size;
		        int posY=clickY/size;
		        
		        
		        logic.click(new Position(7-posY, posX), myPlayerColor);
		     }
		 });
		
		/** Sakkfigurák képeinek beolvasása  */
		try {
			BBishop = ImageIO.read(getClass().getResource(("pictures\\BBishop.png")));
			BKing = ImageIO.read(getClass().getResource(("pictures\\BKing.png")));
			BKnight = ImageIO.read(getClass().getResource(("pictures\\BKnight.png")));
			BPawn = ImageIO.read(getClass().getResource(("pictures\\BPawn.png")));
			BQueen = ImageIO.read(getClass().getResource(("pictures\\BQueen.png")));
			BRook = ImageIO.read(getClass().getResource(("pictures\\BRook.png")));
			WBishop = ImageIO.read(getClass().getResource(("pictures\\WBishop.png")));
			WKing = ImageIO.read(getClass().getResource(("pictures\\WKing.png")));
			WKnight = ImageIO.read(getClass().getResource(("pictures\\WKnight.png")));
			WPawn = ImageIO.read(getClass().getResource(("pictures\\WPawn.png")));
			WQueen = ImageIO.read(getClass().getResource(("pictures\\WQueen.png")));
			WRook = ImageIO.read(getClass().getResource(("pictures\\WRook.png")));
		} catch (Exception ex)
		{
			System.out.println("File not found: " + ex.getMessage());
		}
		
		/** Ablak láthatóvá tétele */
		setVisible(true);               // Láthatóvá teszem az ablakot
	}
	
	
	//! \brief  Beállítható a referenciám a Logic-ra
	/**
	 * ILogic referencia beállítása
	 * @param logic: ahova a referencia mutat
	 */
	@Override
	public void setGameLogic(ILogic logic)
	{
		this.logic = logic;
	}
	
	
	//! \brief  Beérkezõ sakktábla lekezelése
	/**
	 * ChessBoard referencia beállítása
	 * @param chessboard: akire a referencia mutat
	 */
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

		/** Felület újbóli kirajzolása */
		repaint();
		
		setVisible(true);
		
	}
	
	
	//! \brief  Gyalogváltás kérelem lekezelése
	/**
	 * Gyalogváltás kérelem lekezelése
	 * @param position: ide érkezett be a játékos gyalogja
	 */
	@Override
	public void notifyPromotion(Position position)
	{
		System.out.println("GUI handles notifyPromotion.");
		
		/** Az választott figura változója */
		Piece piece=null;

		/** Választó ablak megnyitása */
		PromotionDialog promDialog = new PromotionDialog();
		
		 /** Választott elem */
		String Chosen = null;
		
		while ( Chosen == null )
		{
			Chosen = promDialog.getChosenOne();
		}
		System.out.println("Valasz: " + Chosen);
		promDialog.dispose();
		promDialog = null;	
		
		
		if (Chosen=="Bishop")
		{
			piece=new Bishop(myPlayerColor);
		}
		else if (Chosen=="Knight")
		{
			piece= new Knight(myPlayerColor);
		}
		else if (Chosen=="Queen")
		{
			piece= new Queen(myPlayerColor);
		}
		else if (Chosen=="Rook")
		{
			piece= new Rook(myPlayerColor);
		}
		
		logic.promote(piece);

	}
	
	/** 
	 * A látható felület megjelenítése.
	 * Itt történik a sakktábla kirajzolása és a figurák elhelyezése a táblán.
	 * 
	 */
	public void paint(Graphics g) {
        super.paint(g);  // fixes the immediate problem.
        
        /** Tábla mezõinek színe */
        Color darkBrown = new Color(139, 69, 19);
		Color lightBrown = new Color(232, 194, 145);
		
		/** Mezõk színe a lehetséges lépések helyein */
		Color selectColor1 = new Color((int)(139*1.5), (int)(69*1.5), (int)(19*1.5));
		Color selectColor2 = new Color((int)(255), (int)(194*1.3), (int)(145*1.5));

		/** Ablak méreteinek meghatározása*/
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
				
				if (this.chessBoard != null) {
					Piece p = chessBoard.getPiece( new Position(i, j) );
					if (chessBoard.getSelections() != null) {
						SelectionType sel = chessBoard.getSelections()[i][j];
						if (sel != null) {
							if ((i + j) % 2 == 0) {					
								g.setColor(selectColor1);
							} else {
								g.setColor(selectColor2);
							}
							g.fillRect( x, y, size, size);
						}
					}


					if (p != null)
					{
						
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
