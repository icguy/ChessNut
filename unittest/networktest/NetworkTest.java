/*************************************************
 *  \file     NetworkTest.java
 *  \brief    Network programr�sz funkcionalit�s�ra vonatkoz� tesztek
 *  \note     A v�gleges kiadott program nem h�vhatja meg!!!
 *  \date     2016. m�j. 12.
 *************************************************/
package unittest.networktest;

//! \brief  Absztrakt oszt�ly, amelyb�l szerver �s kliens oldali teszt lesz
public abstract class NetworkTest
{
	public static final int SEC = 1000;  //!< M�sodperc defin�ci�ja (ms-ben)
	public abstract void start();        //!< Teszt ind�t�sa
	
	public void waitSec(int sec)
	{
		try
		{
			Thread.sleep(sec * SEC);
		} catch (InterruptedException e)
		{
			System.out.println("Waiting exception: " + e.getMessage());
		}
	}
}