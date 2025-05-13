/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package comun;

public class AppState {
    private static AppState instancia;
    private Ticket.Tipo tipoSeleccionado;

    private AppState() {}

    public static AppState getInstancia() {
        if (instancia == null) {
            instancia = new AppState();
        }
        return instancia;
    }

    public Ticket.Tipo getTipoSeleccionado() {
        return tipoSeleccionado;
    }

    public void setTipoSeleccionado(Ticket.Tipo tipo) {
        this.tipoSeleccionado = tipo;
    }
}
