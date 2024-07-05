package org.pahappa.systems.registrationapp;

import org.pahappa.systems.registrationapp.views.UserBean;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;

public class RegistrationApp {

    public static void main(String[] args) {
//        UserView userView = new UserView();
//        userView.displayMenu();
        try {
            SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket socket = (SSLSocket) factory.createSocket();
            socket.setEnabledProtocols(new String[]{"TLSv1.2"});
            socket.startHandshake();
            System.out.println("TLS 1.2 is supported");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("TLS 1.2 is not supported");
        }



    }
}
