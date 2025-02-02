package ufps.arqui.python.poo.gui.views.impl;

import ufps.arqui.python.poo.gui.controllers.IProyectoController;
import ufps.arqui.python.poo.gui.exceptions.Exceptions;
import ufps.arqui.python.poo.gui.utils.ConfGrid;
import ufps.arqui.python.poo.gui.utils.ViewTool;
import ufps.arqui.python.poo.gui.views.IPanelView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * PanelClass componente que se dibujara en el area de proyecto como Clase
 *
 * @author Sachikia
 */
public class PanelClass implements IPanelView{

    /**
     * JPanel en el cual sera dibujado y arratrado este PanelClass
     */
    private JPanel parent;

    /**
     * JPanel representación visual de una clase de python
     */
    private JPanel panel;

    /**
     * JLabel representación visual del nombre de una clase de python
     */
    private JLabel lblName;
    
    /**
     * Ruta del archivo que contiene la clase actual
     */
    private String relativePathFile;
    
    /**
     * Listado de paneles de herencia, se usa para posteriormente saber hacia
     * que panel dibujar la flecha
     */
    private List<PanelClass> herencia = new ArrayList<>();

    /**
     * Indica si este panel ya ha sido dibujado
     */
    private Boolean estaDibujado;

    /**
     * Ubicación actual de este PanelClass en el JPanel parent donde esta siendo
     * dibujado
     */
    private Point location;

    /**
     * Ultima ubicación registrada del PanelClass luego de haber sido arrastrado
     */
    private Point lastLocation;

    private IProyectoController controller;

    public PanelClass(String relativePathFile, String name, String pathModule, JPanel parent, IProyectoController controller) {
        this.relativePathFile = relativePathFile;
        this.controller = controller;
        this.panel = new JPanel(new GridBagLayout());
        this.estaDibujado = false;
        this.parent = parent;
        this.lblName = new JLabel(name);
        this.lblName.setToolTipText("Módulo: "+pathModule);
        this.location = new Point();
        this.lastLocation = new Point();

        this.inicializarContenido();
        this.draggableMode();
    }

    @Override
    public void inicializarContenido() {
        this.panel.setBackground(new Color(243, 215, 174));
        this.panel.setBorder(BorderFactory.createLineBorder(Color.black));
        this.panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        this.lblName.setHorizontalAlignment(SwingConstants.CENTER);
        this.lblName.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));

        ConfGrid config = new ConfGrid(panel, lblName);
        config.setWeightx(1);
        config.setWeighty(1);
        config.setFill(GridBagConstraints.HORIZONTAL);
        config.setAnchor(GridBagConstraints.PAGE_START);

        ViewTool.insert(config);
    }

    /**
     * Añade un panel que representa una clase de la cual this heredo Recordar
     * que esta clase(PanelClass) es la forma visual de una clase de python
     *
     * @param panelClass PanelClass relacionado a this por herencia en clases de
     * python
     */
    public void añadirHerencia(PanelClass panelClass) {
        this.herencia.add(panelClass);
    }

    /**
     * Añade los eventos la clase(PanelClass)
     */
    private void draggableMode() {
        this.panel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent ev) {
                location.move(ev.getX(), ev.getY());
            }
            
            public void mouseClicked(MouseEvent event){
                if (event.getClickCount() == 2 && event.getButton() == MouseEvent.BUTTON1) {
                    try{
                        controller.abrirArchivo(relativePathFile);
                    }catch(Exceptions e){
                        mostrarError(panel, e);
                    }
                }
            }
        });

        this.panel.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent evt) {
                //Calculo para pasar del puntero en la screen al panel
                int calX = evt.getXOnScreen() - parent.getLocationOnScreen().x;
                int calY = evt.getYOnScreen() - parent.getLocationOnScreen().y;

                int xt = calX - location.x;
                int yt = calY - location.y;

                lastLocation.move(xt, yt);
                panel.setLocation(lastLocation);
                parent.repaint();
            }
        });
    }
    
    @Override
    public JPanel getPanel() {
        return this.panel;
    }

    public List<PanelClass> getHerencia() {
        return herencia;
    }

    /**
     * Obtiene la ubicación de este panel relativa al panel padre que lo
     * contiene
     *
     * @return
     */
    public Point getLocation() {
        int calX = panel.getLocationOnScreen().x - parent.getLocationOnScreen().x;
        int calY = panel.getLocationOnScreen().y - parent.getLocationOnScreen().y;

        return new Point(calX, calY);
    }

    /**
     * Cambia la posición de este panel a la ultima ubicación en la que estuvo
     */
    public void cambiaAUltimaUbicacion() {
        if (this.lastLocation.x != 0) {
            this.panel.setLocation(this.lastLocation);
        }
    }

    public Point getLastLocation() {
        return this.lastLocation;
    }

    public Boolean estaDibujado() {
        return estaDibujado;
    }

    public void setEstaDibujado(Boolean isDraw) {
        this.estaDibujado = isDraw;
    }
}
