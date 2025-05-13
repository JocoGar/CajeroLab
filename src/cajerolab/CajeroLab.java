package cajerolab;

import com.formdev.flatlaf.intellijthemes.FlatGrayIJTheme;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import comun.ConexionUtil;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.Timer;

public class CajeroLab extends javax.swing.JFrame {
    
    private JLabel lblEnEspera;
private Timer refrescarTimer;
    
    
    private JLabel lblHistorial;
    private JLabel lblInfoTickets;
    private JLabel lblCajero;
    private JScrollPane scrollPane;
    private JTextArea txtAreaHistorial;
    private JButton btnRegistrarTicket;
    private java.util.List<comun.Ticket> completedTickets = new java.util.ArrayList<>();


    public CajeroLab() {
    initComponents();
   /* conectarMonitor(); */
    cargarDatosEjemplo();
    
}
/*
      private void conectarMonitor() {
    new Thread(() -> {
        try (Socket s = new Socket(ConexionUtil.HOST, ConexionUtil.PUERTO);
             ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
             ObjectInputStream  in  = new ObjectInputStream(s.getInputStream()))
        {
            out.writeUTF("CONNECT");
            out.writeObject(prueba.Ticket.Tipo.LABORATORIO);
            out.flush();
            // Leer el ACK antes de cerrar
            String ack = in.readUTF();
            System.out.println("Monitor respondió: " + ack);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }).start();
}

private void desconectarMonitor() {
    // Llamar desde windowClosing()
    new Thread(() -> {
        try (Socket s = new Socket(ConexionUtil.HOST, ConexionUtil.PUERTO);
             ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
             ObjectInputStream  in  = new ObjectInputStream(s.getInputStream()))
        {
            out.writeUTF("DISCONNECT");
            out.writeObject(prueba.Ticket.Tipo.LABORATORIO);
            out.flush();
            String ack = in.readUTF();
            System.out.println("Monitor respondió: " + ack);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }).start();
}*/

private void initComponents() {
 /*   addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
            // 1) Avisar al monitor
            desconectarMonitor();
            // 2) Detener el timer si existe
            if (refrescarTimer != null) {
                refrescarTimer.stop();
            }
            // 3) Cerrar la ventana
            dispose();
        }
    });
*/ addWindowListener(new java.awt.event.WindowAdapter() {
    @Override
    public void windowClosing(java.awt.event.WindowEvent e) {
        if (refrescarTimer != null) {
            refrescarTimer.stop();
        }
    }
});
    setTitle("Cajero - Laboratorio");
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setPreferredSize(new Dimension(850, 600));

    JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
    mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    JPanel topPanel = new JPanel(new BorderLayout());
    
    lblHistorial = new JLabel("Historial");
    lblHistorial.setFont(new Font("Segoe UI Semibold", Font.BOLD, 24));
    topPanel.add(lblHistorial, BorderLayout.WEST);
    
    lblCajero = new JLabel("Cajero 2 Laboratorio");
    lblCajero.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
    topPanel.add(lblCajero, BorderLayout.EAST);

    JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
    
    JPanel infoPanel = new JPanel(new BorderLayout());
    lblInfoTickets = new JLabel("Información de los Tickets");
    lblInfoTickets.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
    infoPanel.add(lblInfoTickets, BorderLayout.WEST);
    
    txtAreaHistorial = new JTextArea();
    txtAreaHistorial.setFont(new Font("Monospaced", Font.PLAIN, 12));
    txtAreaHistorial.setEditable(false);
    scrollPane = new JScrollPane(txtAreaHistorial);
    scrollPane.setPreferredSize(new Dimension(720, 230));
    
    centerPanel.add(infoPanel, BorderLayout.NORTH);
    centerPanel.add(scrollPane, BorderLayout.CENTER);

    btnRegistrarTicket = new JButton("Siguiente");
    btnRegistrarTicket.setFont(new Font("Segoe UI Semibold", Font.BOLD, 14));
    btnRegistrarTicket.setPreferredSize(new Dimension(200, 70));
    btnRegistrarTicket.addActionListener(this::btnRegistrarTicketActionPerformed);
    
    JPanel buttonPanel = new JPanel();
    buttonPanel.add(btnRegistrarTicket);

mainPanel.add(topPanel, BorderLayout.NORTH);
    mainPanel.add(centerPanel, BorderLayout.CENTER);
   
JPanel south = new JPanel(new BorderLayout());
south.add(buttonPanel, BorderLayout.CENTER);


lblEnEspera = new JLabel("Tickets en espera: …");
south.add(lblEnEspera, BorderLayout.WEST);

mainPanel.add(south, BorderLayout.SOUTH);

    getContentPane().add(mainPanel);
    pack();
    setLocationRelativeTo(null);
}

private void cargarDatosEjemplo() {
    StringBuilder sb = new StringBuilder();
    sb.append("N° Ticket   | Nombre del Cliente      | Tipo Examen   -   Fecha    \n");
    sb.append("-----------------------------------------------------------------------------------\n");
    txtAreaHistorial.setText(sb.toString());
    updateEnEspera();
refrescarTimer = new Timer(3000, e -> updateEnEspera());
refrescarTimer.setRepeats(true);
refrescarTimer.start();
}
/**
 * Añade un ticket al historial y vuelve a pintar todo el área.
 */
public void addCompletedTicket(comun.Ticket ticket, String servicioInfo) {
    completedTickets.add(ticket);

    StringBuilder sb = new StringBuilder();
    sb.append("N° Ticket   | Nombre del Cliente      | Tipo Examen   -   Fecha    \n");
    sb.append("-----------------------------------------------------------------------------------\n");

    for (comun.Ticket t : completedTickets) {
        String servicio = "Laboratorio";
         sb.append(String.format("%-12s| %-23s| %s%n",
        String.format("%03d", t.getNumero()),   
        t.getNombre(),
        servicioInfo
         ));
    }

       sb.append("-----------------------------------------------------------------------------------\n");
    sb.append("Total tickets registrados hoy: ")
      .append(completedTickets.size())
      .append("\n");

    txtAreaHistorial.setText(sb.toString());
}


private void btnRegistrarTicketActionPerformed(ActionEvent evt) {
    try {
        comun.Ticket ticket = ConexionUtil.pedirSiguiente(comun.Ticket.Tipo.LABORATORIO);
        if (ticket != null) {
            String email = ticket.getEmail();
            if (email != null && !email.trim().isEmpty()) {
                String asunto = "Es su turno: Ticket #" + String.format("%03d", ticket.getNumero());
                String cuerpo = String.format(
     "Hola %s,%nPor favor diríjase a Laboratorio (Cajero 2) (ticket nº %s).",
     ticket.getNombre(), String.format("%03d", ticket.getNumero())  
 );

                comun.MailUtil.enviarCorreo(email, asunto, cuerpo);
            }
            this.setVisible(false);
            formLab labForm = new formLab(this);
            labForm.setTicketData(ticket);
            labForm.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this,
                "No hay tickets para Laboratorio",
                "Información", JOptionPane.INFORMATION_MESSAGE);
        }
    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this,
            "Error al conectar con el servidor:\n" + ex.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void updateEnEspera() {
    try {
        int n = ConexionUtil.contarEnEspera(comun.Ticket.Tipo.LABORATORIO);
        lblEnEspera.setText("Tickets en espera: " + n);
    } catch (Exception ex) {
        lblEnEspera.setText("Tickets en espera: –");
        ex.printStackTrace();
    }
}


public static void main(String args[]) {
    try {
        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                javax.swing.UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
        java.util.logging.Logger.getLogger(CajeroLab.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }

    try {
        UIManager.setLookAndFeel(new FlatMacLightLaf());
    } catch (Exception ex) {
        System.err.println("No se pudo aplicar el tema FlatLaf");
    }

    java.awt.EventQueue.invokeLater(() -> {
        new CajeroLab().setVisible(true);
    });
}
}