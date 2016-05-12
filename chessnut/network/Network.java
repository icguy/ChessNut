/*************************************************
 *  \file     Network.java
 *  \brief    Chessnut network kezel�s absztrakci�ja
 *  \note     NetworkServer �s NetWorkClient sz�rmaztatja le.
 *  \date     2016. m�j. 11.
 *************************************************/
package chessnut.network;

public abstract class Network
{
	// Kapcsolat
	abstract void connect(String ipAddr);   //!< Kapcsol�d�s
	abstract void disconnect();             //!< Kapcsolat bont�s
}
