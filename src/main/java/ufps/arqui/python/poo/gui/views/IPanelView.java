package ufps.arqui.python.poo.gui.views;

import ufps.arqui.python.poo.gui.exceptions.Exceptions;

import javax.swing.*;

/**
 * Interfaz todos los paneles del proyecto.
 *
 * @author Omar Ramón Montes
 */
public interface IPanelView {

    /**
     * Obtiene el Jpanel contenedor.
     * @return JPanel contenedor.
     */
    JPanel getPanel();

    /**
     * Debe inicializar los componentes que tendrá en panel.
     */
    void inicializarContenido();

    /**
     * Metodo para mostrar cuadro de error en los paneles.
     * @param e Excepción generada.
     */
    default void mostrarError(Exceptions e){
        JOptionPane.showMessageDialog(null, e.getMessage(),"ERROR", JOptionPane.ERROR_MESSAGE);
    };
}
