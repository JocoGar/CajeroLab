package comun;

import java.io.Serializable;

public class Ticket implements Serializable {
    private static final long serialVersionUID = 1L;
    public enum Tipo { CITA_MEDICA, FARMACIA, LABORATORIO }

    private final int numero;
    private final Tipo tipo;
    private final String nombre;
    private final String dpi;
    private final String email;
    private final String telefono;

    public Ticket(int numero, Tipo tipo, String nombre, String dpi, String email, String telefono) {
        this.numero = numero;
        this.tipo   = tipo;
        this.nombre = nombre;
        this.dpi    = dpi;
        this.email  = email;
        this.telefono = telefono; 
    }

    public int getNumero()       { return numero; }
    public Tipo getTipo()        { return tipo; }
    public String getNombre()    { return nombre; }
    public String getDpi()       { return dpi; }
    public String getEmail()     { return email; }
    public String getTelefono()    { return telefono; }

    @Override
    public String toString() {
        return String.format("%03d – %s – %s",
          numero,
          nombre,
          tipo==Tipo.CITA_MEDICA ? "Cita Médica" :
          tipo==Tipo.FARMACIA    ? "Farmacia" :
                                   "Laboratorio"
        );
    }
}
