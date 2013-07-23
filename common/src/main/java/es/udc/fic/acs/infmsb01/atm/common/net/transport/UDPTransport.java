package es.udc.fic.acs.infmsb01.atm.common.net.transport;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public final class UDPTransport {

	public final void send(byte[] buffer, String destinationIP,
			int destinationPort, DatagramSocket socket) throws IOException {

		InetAddress destIPv4 = InetAddress.getByName(destinationIP);

		DatagramPacket udpPacket = new DatagramPacket(buffer, buffer.length,
				destIPv4, destinationPort);

		socket.send(udpPacket);

	}

	public final DatagramPacket receive(DatagramSocket socket)
			throws IOException {
		byte[] receivedData = new byte[1024];

		DatagramPacket receivePacket = new DatagramPacket(receivedData,
				receivedData.length);

		socket.receive(receivePacket);

		return receivePacket;
	}

}
