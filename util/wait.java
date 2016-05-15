/*************************************************
 *  \file     wait.java
 *  \brief    Ezzel lehet v�rni
 *  \note     Mert ha �res ciklust hagysz a v�rakoz�sba, azt a Java nem szereti
 *  \date     2016. m�j. 15.
 *************************************************/
package util;

public class wait
{
	public static final int SEC = 1000;  //!< M�sodperc defin�ci�ja (ms-ben)
	
	//! \brief  M�sodperc v�rakoz�s
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
