/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ControladoresVentanas;

import AdministradoresDAO.AdministradorInventario;
import Modelo.Articulo;
import Modelo.DetalleArticulo;
import Modelo.Precio;
import Ventanas.Inventario.VentanaInventario;
import java.awt.HeadlessException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 *
 * @author rodrigopeniche
 */
public class ControladorVentanaInventario {

    private static ControladorVentanaInventario controladorVentanaInventario = null;

    private ControladorVentanaInventario() {

    }

    private static void generarControladorVentanaInventario() {
        controladorVentanaInventario = new ControladorVentanaInventario();
    }

    public static ControladorVentanaInventario obtenerControladorVentanaInventario() {
        if (controladorVentanaInventario == null) {
            generarControladorVentanaInventario();
        }
        VentanaInventario.obtenerVentanaVerInventario().setVisible(true);
        return controladorVentanaInventario;
    }

    protected void mostrarPanelVerInventario() throws SQLException {
        VentanaInventario.obtenerVentanaVerInventario().mostrarPanelVerInventario();
        try {
            generarTablaArticulos();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ControladorVentanaInventario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected void mostrarPanelAgregarArticulo() {
        VentanaInventario.obtenerVentanaVerInventario().mostrarPanelAgregarArticulo();
        eventoBotonAgregarArticulo();
        eventoBotonCancelarAgregacion();

    }

    private void generarTablaArticulos() throws SQLException, ClassNotFoundException {
        AdministradorInventario admin = new AdministradorInventario();
        ArrayList<Articulo> articulos = admin.obtenerDatos();
        iniciarContadorFilasTablaArticulos();
        
        for (int numArticulo = 0; numArticulo < articulos.size(); numArticulo++) {
            VentanaInventario.obtenerVentanaVerInventario().getPanelVerInventario().
                    getContenidoTablaArticulos().addRow(new Object[]{articulos.get(numArticulo).getClaveArticulo(),
                articulos.get(numArticulo).getClaveProveedor(),
                articulos.get(numArticulo).getDescripcion(),
                articulos.get(numArticulo).getDetalleArticulo().getCantidad(),
                articulos.get(numArticulo).getDetalleArticulo().getPrecioCompra().getPrecio(),
                articulos.get(numArticulo).getDetalleArticulo().getPrecioVenta().getPrecio()
            });
        }
    }

    private void actualizarTablaArticulos() throws SQLException, ClassNotFoundException {
        iniciarContadorFilasTablaArticulos();
        generarTablaArticulos();
    }
    
    private void iniciarContadorFilasTablaArticulos(){
        VentanaInventario.obtenerVentanaVerInventario().getPanelVerInventario().getContenidoTablaArticulos().setRowCount(0);
    }

    protected void eventoBotonBuscarArticulo() {
        JButton botonBuscar = VentanaInventario.obtenerVentanaVerInventario().getPanelVerInventario().getBotonBuscarArticulo();

        botonBuscar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                VentanaInventario.obtenerVentanaVerInventario().mostrarPanelBuscarArticulo();
                eventoBotonBuscar();
                eventoBotonCancelarBusqueda();
            }
        });
        VentanaInventario.obtenerVentanaVerInventario().getPanelVerInventario().setBotonBuscarArticulo(botonBuscar);
    }

    protected void eventoBotonEliminarArticulo() {
        JButton botonEliminar = VentanaInventario.obtenerVentanaVerInventario().getPanelVerInventario().getBotonEliminarArticulo();

        botonEliminar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                VentanaInventario.obtenerVentanaVerInventario().mostrarPanelEliminarArticulo();
                eventoBotonEliminar();
                eventoBotonCancelarEliminacion();
            }
        });
        VentanaInventario.obtenerVentanaVerInventario().getPanelVerInventario().setBotonEliminarArticulo(botonEliminar);
    }

    protected void eventoBotonActualizarArticulo() {
        JButton botonActualizar = VentanaInventario.obtenerVentanaVerInventario().getPanelVerInventario().getBotonActualizarArticulo();

        VentanaInventario.obtenerVentanaVerInventario().getPanelVerInventario().getBotonActualizarArticulo().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                VentanaInventario.obtenerVentanaVerInventario().mostrarPanelActualizarArticulo();
                eventoBotonActualizar();
                eventoBotonCancelarActualizacion();
            }
        });
        VentanaInventario.obtenerVentanaVerInventario().getPanelVerInventario().setBotonActualizarArticulo(botonActualizar);
    }

    private void eventoBotonEliminar() {
        JButton botonEliminar = VentanaInventario.obtenerVentanaVerInventario().getPanelEliminarArticulo().getBotonEliminar();

        botonEliminar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                eliminarArticulo();
            }
        });
       VentanaInventario.obtenerVentanaVerInventario().getPanelEliminarArticulo().setBotonEliminar(botonEliminar);
    }

    private void eliminarArticulo() {
        AdministradorInventario adminInventario = new AdministradorInventario();
        String claveArticulo = VentanaInventario.obtenerVentanaVerInventario().getPanelEliminarArticulo().getCampoTextoClave().getText();

        try {
            adminInventario.eliminar(claveArticulo);
            actualizarTablaArticulos();
            JOptionPane.showMessageDialog(null, "Artículo eliminado exitosamente");
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Hubo un error al actualizar al artículo");
            Logger.getLogger(ControladorVentanaInventario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void eventoBotonCancelarEliminacion() {
        JButton botonCancelarEliminacion = VentanaInventario.obtenerVentanaVerInventario().getPanelEliminarArticulo().getBotonCancelar();

        botonCancelarEliminacion.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                VentanaInventario.obtenerVentanaVerInventario().getPanelEliminarArticulo().setVisible(false);
            }
        });
        VentanaInventario.obtenerVentanaVerInventario().getPanelEliminarArticulo().setBotonCancelar(botonCancelarEliminacion);
    }

    private void eventoBotonAgregarArticulo() {
        JButton botonAgregar = VentanaInventario.obtenerVentanaVerInventario().getPanelAgregarArticulo().getBotonAgregarArticulo();
        botonAgregar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                agregarArticulo();
            }
        });
    }

    private void agregarArticulo() {

        String claveArticulo = VentanaInventario.obtenerVentanaVerInventario().getPanelAgregarArticulo().getCampoTextoClave().getText();
        String claveProveedor = VentanaInventario.obtenerVentanaVerInventario().getPanelAgregarArticulo().getCampoTextoClaveProveedor().getText();
        String descripcion = VentanaInventario.obtenerVentanaVerInventario().getPanelAgregarArticulo().getCampoTextoDescripcion().getText();
        String cantidad = VentanaInventario.obtenerVentanaVerInventario().getPanelAgregarArticulo().getCampoTextoCantidad().getText();
        String precioCompra = VentanaInventario.obtenerVentanaVerInventario().getPanelAgregarArticulo().getCampoTextoPrecioCompra().getText();
        String precioVenta = VentanaInventario.obtenerVentanaVerInventario().getPanelAgregarArticulo().getCampoTextoPrecioVenta().getText();

        AdministradorInventario admin = new AdministradorInventario();

        Articulo articulo = new Articulo(claveArticulo, claveProveedor, descripcion,
                new DetalleArticulo(Integer.parseInt(cantidad),
                        new Precio(Double.parseDouble(precioCompra)),
                        new Precio(Double.parseDouble(precioVenta))));

        try {
            admin.agregar(articulo);
            JOptionPane.showMessageDialog(null, "El artículo se ha agregado exitosamente");
            vaciarCamposAgregarArticulo();
        } catch (HeadlessException createException) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error al agregar el artículo");
        }
    }

    private void eventoBotonCancelarAgregacion() {
        JButton botonCancelar = VentanaInventario.obtenerVentanaVerInventario().getPanelAgregarArticulo().getBotonCancelar();

        botonCancelar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                VentanaInventario.obtenerVentanaVerInventario().getPanelAgregarArticulo().setVisible(false);
                VentanaInventario.obtenerVentanaVerInventario().setVisible(false);
            }
        });
        VentanaInventario.obtenerVentanaVerInventario().getPanelAgregarArticulo().setBotonCancelar(botonCancelar);
    }

    private void eventoBotonBuscar() {
        JButton botonBuscar = VentanaInventario.obtenerVentanaVerInventario().getPanelBuscarArticulo().getBotonBuscar();

        botonBuscar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buscarArticulo();
            }
        });
        VentanaInventario.obtenerVentanaVerInventario().getPanelBuscarArticulo().setBotonBuscar(botonBuscar);
    }

    private void buscarArticulo() {
        VentanaInventario.obtenerVentanaVerInventario().getPanelBuscarArticulo().mostrarCampoTextoCantidad();
        VentanaInventario.obtenerVentanaVerInventario().getPanelBuscarArticulo().mostrarEtiquetaDescripcion();
        VentanaInventario.obtenerVentanaVerInventario().getPanelBuscarArticulo().mostrarCampoTextoCantidad();
        VentanaInventario.obtenerVentanaVerInventario().getPanelBuscarArticulo().mostrarEtiquetaCantidad();
        VentanaInventario.obtenerVentanaVerInventario().getPanelBuscarArticulo().mostrarCampoTextoPrecioCompra();
        VentanaInventario.obtenerVentanaVerInventario().getPanelBuscarArticulo().mostrarEtiquetaPrecioCompra();
        VentanaInventario.obtenerVentanaVerInventario().getPanelBuscarArticulo().mostrarCampoTextoPrecioVenta();
        VentanaInventario.obtenerVentanaVerInventario().getPanelBuscarArticulo().mostrarEtiquetaPrecioVenta();

        String claveArticulo = VentanaInventario.obtenerVentanaVerInventario().getPanelBuscarArticulo().getCampoTextoClave().getText();
        AdministradorInventario admin = new AdministradorInventario();
        Articulo articulo = admin.buscar(claveArticulo);

        VentanaInventario.obtenerVentanaVerInventario().getPanelBuscarArticulo().setCampoTextoDescripcion(articulo.getDescripcion());
        VentanaInventario.obtenerVentanaVerInventario().getPanelBuscarArticulo().setCampoTextoCantidad(String.valueOf(articulo.getDetalleArticulo().getCantidad()));
        VentanaInventario.obtenerVentanaVerInventario().getPanelBuscarArticulo().setCampoTextoPrecioCompra(String.valueOf(articulo.getDetalleArticulo().getPrecioCompra().getPrecio()));
        VentanaInventario.obtenerVentanaVerInventario().getPanelBuscarArticulo().setCampoTextoPrecioVenta(String.valueOf(articulo.getDetalleArticulo().getPrecioVenta().getPrecio()));
    }

    protected void eventoBotonCancelarBusqueda() {
        JButton botonCancelar = VentanaInventario.obtenerVentanaVerInventario().getPanelBuscarArticulo().getBotonCancelar();

        botonCancelar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                VentanaInventario.obtenerVentanaVerInventario().getPanelBuscarArticulo().setVisible(false);
            }
        });
        VentanaInventario.obtenerVentanaVerInventario().getPanelBuscarArticulo().setBotonCancelar(botonCancelar);
    }

    protected void eventoBotonActualizar() {
        JButton botonActualizar = VentanaInventario.obtenerVentanaVerInventario().getPanelActualizarArticulo().getBotonGuardarCambios();
        botonActualizar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                actualizarArticulo();
            }
        });
    }

    private void actualizarArticulo() {
        String claveArticulo = VentanaInventario.obtenerVentanaVerInventario().getPanelActualizarArticulo().getCampoTextoClave().getText();
        String nuevaClaveProveedor = VentanaInventario.obtenerVentanaVerInventario().getPanelActualizarArticulo().getCampoTextoClaveProveedor().getText();
        String nuevaDescripcion = VentanaInventario.obtenerVentanaVerInventario().getPanelActualizarArticulo().getCampoTextoDescripcion().getText();
        String nuevaCantidad = VentanaInventario.obtenerVentanaVerInventario().getPanelActualizarArticulo().getCampoTextoCantidad().getText();
        String nuevoPrecioCompra = VentanaInventario.obtenerVentanaVerInventario().getPanelActualizarArticulo().getCampoTextoPrecioCompra().getText();
        String nuevoPrecioVenta = VentanaInventario.obtenerVentanaVerInventario().getPanelActualizarArticulo().getCampoTextoPrecioVenta().getText();

        Articulo articulo;
        articulo = new Articulo(claveArticulo, nuevaClaveProveedor, nuevaDescripcion,
                new DetalleArticulo(Integer.parseInt(nuevaCantidad),
                        new Precio(Double.parseDouble(nuevoPrecioCompra)),
                        new Precio(Double.parseDouble(nuevoPrecioVenta))));

        AdministradorInventario admin = new AdministradorInventario();
        try {
            admin.actualizar(articulo);
            JOptionPane.showMessageDialog(null, "El artículo ha sido actualizado exitosamente");
        } catch (HeadlessException updateException) {
            JOptionPane.showMessageDialog(null, "Error al actualizar el artículo");
        }

        try {
            actualizarTablaArticulos();
        } catch (SQLException | ClassNotFoundException ex) {

        }
    }

    protected void eventoBotonCancelarActualizacion() {
        JButton botonCancelar = VentanaInventario.obtenerVentanaVerInventario().getPanelActualizarArticulo().getBotonCancelar();
        VentanaInventario.obtenerVentanaVerInventario().getPanelActualizarArticulo().getBotonCancelar().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                VentanaInventario.obtenerVentanaVerInventario().getPanelActualizarArticulo().setVisible(false);
            }
        });
        VentanaInventario.obtenerVentanaVerInventario().getPanelActualizarArticulo().setBotonCancelar(botonCancelar);
    }

    private void vaciarCamposAgregarArticulo() {
        VentanaInventario.obtenerVentanaVerInventario().getPanelAgregarArticulo().getCampoTextoClave().setText(null);
        VentanaInventario.obtenerVentanaVerInventario().getPanelAgregarArticulo().getCampoTextoClaveProveedor().setText(null);
        VentanaInventario.obtenerVentanaVerInventario().getPanelAgregarArticulo().getCampoTextoDescripcion().setText(null);
        VentanaInventario.obtenerVentanaVerInventario().getPanelAgregarArticulo().getCampoTextoCantidad().setText(null);
        VentanaInventario.obtenerVentanaVerInventario().getPanelAgregarArticulo().getCampoTextoPrecioCompra().setText(null);
        VentanaInventario.obtenerVentanaVerInventario().getPanelAgregarArticulo().getCampoTextoPrecioVenta().setText(null);
    }

//    private boolean validarCamposActualizarArticulo(){
//        String claveProveedor = ventanaInventario.getPanelActualizarArticulo().getCampoTextoClaveProveedor().getText();
//        String descripcion = ventanaInventario.getPanelActualizarArticulo().getCampoTextoDescripcion().getText();
//        String cantidad = ventanaInventario.getPanelActualizarArticulo().getCampoTextoCantidad().getText();
//        String precioCompra = ventanaInventario.getPanelActualizarArticulo().getCampoTextoPrecioCompra().getText();
//        String precioVenta = ventanaInventario.getPanelActualizarArticulo().getCampoTextoPrecioVenta().getText();
//        
//        
//    }
}