package cajerolab;

import com.formdev.flatlaf.intellijthemes.FlatGrayIJTheme;
import comun.ConexionUtil;
import javax.swing.*;
import java.awt.*;
import com.toedter.calendar.JDateChooser;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.text.SimpleDateFormat;


import comun.Ticket;

public class formLab extends JFrame {

private CajeroLab padre;

    private JTextField txtTelefono;
    private JTextField txtTicket;
    private JTextField txtNombre;
    private JTextField txtDPI;
    private JTextField txtEmail;
    private JComboBox<String> cbTipoServicio;
    private JTextField txtMedicoSolicitante;
    private JDateChooser dateChooser;
    private JTextArea taObservaciones;

    private JButton btnRegistrar;

    private Ticket ticketActual;

    public formLab(CajeroLab padre) {
        super("Servicio de Laboratorio");
        this.padre= padre;
        initComponents();
        System.out.println("[FormLab] UI lista.");
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(620, 600));

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Título
        JPanel titlePanel = new JPanel();
        JLabel lblTitulo = new JLabel("REGISTRO DE SERVICIO DE LABORATORIO");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        titlePanel.add(lblTitulo);

        // Campo Ticket
        JPanel ticketPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        ticketPanel.add(new JLabel("Ticket:"));
        txtTicket = new JTextField(5);
        txtTicket.setEditable(false);
        ticketPanel.add(txtTicket);

        // Contenido
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.add(ticketPanel);
        contentPanel.add(Box.createVerticalStrut(10));

        // Datos del Paciente
        JPanel datosPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        datosPanel.setBorder(BorderFactory.createTitledBorder("Datos del Paciente"));
        datosPanel.add(new JLabel("Nombre completo:"));
        txtNombre = new JTextField(); datosPanel.add(txtNombre);
        txtNombre.addKeyListener(new KeyAdapter() {
    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if (!Character.isLetter(c) && !Character.isWhitespace(c)) {
            e.consume();
        }
    }
});
        datosPanel.add(new JLabel("DPI:"));
        txtDPI = new JTextField(); datosPanel.add(txtDPI);
           txtDPI.addKeyListener(new KeyAdapter() {
    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        String text = txtDPI.getText();
       
        if (!Character.isDigit(c) || text.length() >= 13) {
            e.consume();
        }
    }
});
        datosPanel.add(new JLabel("Email:"));
        txtEmail = new JTextField(); datosPanel.add(txtEmail);

datosPanel.add(new JLabel("Teléfono:"));
txtTelefono = new JTextField(); datosPanel.add(txtTelefono);
txtTelefono.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                String digits = txtTelefono.getText().replaceAll("\\D", "");
               
                if (!Character.isDigit(c) || digits.length() >= 8) {
                    e.consume();
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                
                String digits = txtTelefono.getText().replaceAll("\\D", "");
                if (digits.length() > 4) {
                    digits = digits.substring(0, 4) + "-" + digits.substring(4);
                }
                txtTelefono.setText(digits);
               
                txtTelefono.setCaretPosition(txtTelefono.getText().length());
            }
        });
    
        JPanel servicioPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        servicioPanel.setBorder(BorderFactory.createTitledBorder("Detalles del Servicio"));
        servicioPanel.add(new JLabel("Tipo de servicio:"));
        cbTipoServicio = new JComboBox<>(new String[]{
            "Análisis de Sangre", "Examen de Orina", "Cultivo Bacteriológico"
        });
        servicioPanel.add(cbTipoServicio);
        servicioPanel.add(new JLabel("Médico solicitante:"));
        txtMedicoSolicitante = new JTextField(); servicioPanel.add(txtMedicoSolicitante);
         txtMedicoSolicitante.addKeyListener(new KeyAdapter() {
    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if (!Character.isLetter(c) && !Character.isWhitespace(c)) {
            e.consume();
        }
    }
});
        servicioPanel.add(new JLabel("Fecha requerida:"));
dateChooser = new JDateChooser();
dateChooser.setDateFormatString("dd/MM/yyyy");

dateChooser.setMinSelectableDate(new Date());
servicioPanel.add(dateChooser);


       
        JPanel observacionesPanel = new JPanel(new BorderLayout());
        observacionesPanel.setBorder(BorderFactory.createTitledBorder("Observaciones"));
        taObservaciones = new JTextArea(4, 20);
        taObservaciones.setLineWrap(true);
        observacionesPanel.add(new JScrollPane(taObservaciones), BorderLayout.CENTER);

        // Botones
        JPanel botonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnRegistrar = new JButton("Registrar Servicio");
        botonPanel.add(btnRegistrar);

        // Ensamblado
        contentPanel.add(datosPanel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(servicioPanel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(observacionesPanel);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(botonPanel, BorderLayout.SOUTH);

        getContentPane().add(mainPanel);
        pack(); setLocationRelativeTo(null); setVisible(true);

        btnRegistrar.addActionListener(e -> registrarServicio());
    }

   
public void setTicketData(Ticket ticket) {
    this.ticketActual = ticket;
    txtTicket.setText(String.format("%03d", ticket.getNumero())); 
    txtNombre.setText(ticket.getNombre());
    txtDPI.setText(ticket.getDpi());
    txtTelefono.setText(ticket.getTelefono());
    txtEmail.setText(ticket.getEmail());
}
    private void registrarServicio() {
            if (!txtDPI.getText().matches("\\d{13}")) {
        JOptionPane.showMessageDialog(this,
            "El DPI debe contener exactamente 13 dígitos.",
            "DPI no válido", JOptionPane.ERROR_MESSAGE
        );
        return;
    }
    
    if (!txtTelefono.getText().matches("\\d{4}-\\d{4}")) {
        JOptionPane.showMessageDialog(this,
            "El teléfono debe tener el formato 0000-0000.",
            "Teléfono no válido", JOptionPane.ERROR_MESSAGE
        );
        return;
    }
        String emailTxt = txtEmail.getText().trim();
if (!emailTxt.isEmpty() && !esEmailValido(emailTxt)) {
    JOptionPane.showMessageDialog(this,
        "Por favor ingrese un correo electrónico válido o deje el campo vacío.",
        "Email no válido", JOptionPane.ERROR_MESSAGE
    );
    txtEmail.requestFocus();
    return;
}
        Date fecha = dateChooser.getDate();
if (fecha == null) {
    JOptionPane.showMessageDialog(this,
        "Debe seleccionar una fecha.",
        "Fecha no válida", JOptionPane.ERROR_MESSAGE);
    return;
}
String fechaStr = new SimpleDateFormat("dd/MM/yyyy").format(fecha);
String tipoServ = (String) cbTipoServicio.getSelectedItem();
    String servicioInfo = tipoServ + "   -   " + fechaStr;

        if (ticketActual == null) {
            JOptionPane.showMessageDialog(this,
                "Primero debe solicitar Siguiente Ticket",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        System.out.println("[FormLab] Registrando servicio para ticket " + ticketActual.getNumero());
        try {
        if (ConexionUtil.completarTicket(ticketActual.getNumero())) {
            JOptionPane.showMessageDialog(this,
                "Servicio registrado para ticket " + String.format("%03d", ticketActual.getNumero()),
                "Ok", JOptionPane.INFORMATION_MESSAGE);
            if (padre != null) padre.addCompletedTicket(ticketActual, servicioInfo);
            this.dispose();
            if (padre != null) padre.setVisible(true);
        }
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    }
private boolean esEmailValido(String email) {
   
    return email.matches("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,6}$");
}

}
