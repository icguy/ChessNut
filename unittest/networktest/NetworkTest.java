/*************************************************
 *  \file     NetworkTest.java
 *  \brief    Network programrész funkcionalitására vonatkozó tesztek
 *  \note     A végleges kiadott program nem hívhatja meg!!!
 *  \date     2016. máj. 12.
 *************************************************/
package unittest.networktest;

//! \brief  Absztrakt osztály, amelybõl szerver és kliens oldali teszt lesz
public abstract class NetworkTest
{
	public static final int SEC = 1000;  //!< Másodperc definíciója (ms-ben)
	public abstract void start();        //!< Teszt indítása
	
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