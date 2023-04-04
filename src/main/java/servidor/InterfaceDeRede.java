/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;

public class InterfaceDeRede {
    /**
     *busca por endereços ip local das interfaces de rede do host
     */
    public static Collection<String> buscarEnderecosLocais() throws SocketException {
        Collection<String> enderecosIp = new ArrayList<>();
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

        while (interfaces.hasMoreElements()) {
            NetworkInterface interfaceDeRede = interfaces.nextElement();
            Enumeration<InetAddress> enderecosDeRede = interfaceDeRede.getInetAddresses();
            while (enderecosDeRede.hasMoreElements()) {
                InetAddress endereco = enderecosDeRede.nextElement();
                if (endereco.isSiteLocalAddress()) {
                    enderecosIp.add( endereco.getHostAddress() );
                }
            }
        }
        return enderecosIp;
    }
}
