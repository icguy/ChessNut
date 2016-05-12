/*************************************************
 *  \file     Network.java
 *  \brief    Chessnut network kezelés absztrakciója
 *  \note     NetworkServer és NetWorkClient származtatja le.
 *  \date     2016. máj. 11.
 *************************************************/
package chessnut.network;

public abstract class Network
{
	// Kapcsolat
	abstract void connect(String ipAddr);   //!< Kapcsolódás
	abstract void disconnect();             //!< Kapcsolat bontás
}
