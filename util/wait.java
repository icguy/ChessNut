/*************************************************
 *  \file     wait.java
 *  \brief    Ezzel lehet várni
 *  \note     Mert ha üres ciklust hagysz a várakozásba, azt a Java nem szereti
 *  \date     2016. máj. 15.
 *************************************************/
package util;

public class wait
{
	public static final int SEC = 1000;  //!< Másodperc definíciója (ms-ben)
	
	//! \brief  Másodperc várakozás
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
