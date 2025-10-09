/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package Vista;

import Modelo.Materia;
import Persistencia.MateriaData;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author maria
 */
public class VistaMateria extends javax.swing.JInternalFrame {

    //CANDE
    private MateriaData materiaData = new MateriaData();
    private DefaultTableModel modeloTabla;
    private Materia materiaSeleccionada;

    /**
     * Creates new form VistaMateria
     */
    public VistaMateria() {
        initComponents();
        configurarComponentes();
        cargarDatosIniciales();
    }

    private void configurarComponentes() {
        configurarTabla();

        //combo box de estados
        comboEstadosMateria.removeAllItems();
        comboEstadosMateria.addItem("Activa");
        comboEstadosMateria.addItem("Inactiva");

        // deshabilitar botones
        btnBorrarMateria.setEnabled(false);
        btnActualizarMateria.setEnabled(false);
        btnAltaBajaLogica.setEnabled(false);
        comboEstadosMateria.setEnabled(false);
    }

    private void configurarTabla() {
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                //editar solo nombre y año
                return column == 1 || column == 2;
            }
        };

        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Año");
        modeloTabla.addColumn("Estado");

        tablaMaterias.setModel(modeloTabla);

    }

    private void cargarDatosIniciales() {
        try {
            modeloTabla.setRowCount(0);
            List<Materia> materias = materiaData.listarMaterias();

            for (Materia materia : materias) {
                modeloTabla.addRow(new Object[]{
                    materia.getIdMateria(),
                    materia.getNombre(),
                    materia.getAnio(),
                    materia.isEstado() ? "Activa" : "Inactiva"
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar las materias: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarMateriaPorNombre() {
        String nombre = textBuscarNombreMateria.getText().trim(); 

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un nombre para buscar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            modeloTabla.setRowCount(0);

            Materia materia = materiaData.buscarMateria(nombre);

            if (materia != null) {
                modeloTabla.addRow(new Object[]{
                    materia.getIdMateria(),
                    materia.getNombre(),
                    materia.getAnio(),
                    materia.isEstado() ? "Activa" : "Inactiva"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al buscar materia: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarMateria() {
        String nombre = textNombreMateria.getText().trim();
        String anioStr = textAñoMateria.getText().trim();

        //controles
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El nombre es obligatorio",
                    "Error", JOptionPane.ERROR_MESSAGE);
            textNombreMateria.requestFocus();
            return;
        }

        if (anioStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El año es obligatorio",
                    "Error", JOptionPane.ERROR_MESSAGE);
            textAñoMateria.requestFocus();
            return;
        }

        try {
            int anio=Integer.parseInt(anioStr);

            if (anio < 2023 || anio > 2027) {
                JOptionPane.showMessageDialog(this,
                        "El año debe estar entre 2023 y 2027",
                        "Error", JOptionPane.ERROR_MESSAGE);
                textAñoMateria.requestFocus();
                return;
            }

            Materia nuevaMateria = new Materia(nombre, anio, true);
            materiaData.guardarMateria(nuevaMateria);

            JOptionPane.showMessageDialog(this,
                    "Materia agregada correctamente con ID: " + nuevaMateria.getIdMateria(),
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

            textNombreMateria.setText("");
            textAñoMateria.setText("");
            textNombreMateria.requestFocus();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "El año debe ser un número válido",
                    "Error", JOptionPane.ERROR_MESSAGE);
            textAñoMateria.requestFocus();
        }   catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al agregar materia: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void borrarMateria() {

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de ELIMINAR PERMANENTEMENTE la materia:\n"
                + materiaSeleccionada.getNombre() + "?\n\n"
                + "Esta acción no se puede deshacer.",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                materiaData.borrarMateria(materiaSeleccionada.getIdMateria());

                JOptionPane.showMessageDialog(this, "Materia eliminada correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                materiaSeleccionada = null;
                deshabilitarBotones();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error al eliminar materia: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void guardarCambiosDesdeTabla() {
        int filaSeleccionada = tablaMaterias.getSelectedRow();

        try {
            // obtener datos de la fila seleccionada
            int id = Integer.parseInt(modeloTabla.getValueAt(filaSeleccionada, 0).toString());
            String nombre = modeloTabla.getValueAt(filaSeleccionada, 1).toString().trim();
            int anio = Integer.parseInt(modeloTabla.getValueAt(filaSeleccionada, 2).toString().trim());
            String estadoStr = modeloTabla.getValueAt(filaSeleccionada, 3).toString();
            boolean estado = estadoStr.equals("Activa");

            // controles
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "El nombre no puede estar vacío",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Crear y actualizar materia
            Materia materiaActualizada = new Materia(nombre, anio, estado);
            materiaActualizada.setIdMateria(id);

            materiaData.actualizarMateria(materiaActualizada);

            JOptionPane.showMessageDialog(this,
                    "Materia actualizada correctamente",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
            cargarDatosIniciales();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al actualizar materia: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cambiarEstadoMateria() {

        String nuevoEstado = (String) comboEstadosMateria.getSelectedItem();
        boolean estadoBoolean = nuevoEstado.equals("Activa");

        try {
            if (estadoBoolean) {
                materiaData.habilitarMateria(materiaSeleccionada);
            } else {
                materiaData.deshabilitarMateria(materiaSeleccionada);
            }

            JOptionPane.showMessageDialog(this,
                    "Estado de la materia cambiado a: " + nuevoEstado,
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cambiar estado: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void seleccionarMateriaDeTabla() {
        int filaSeleccionada = tablaMaterias.getSelectedRow();

        if (filaSeleccionada != -1) {
            try {
                int id = Integer.parseInt(modeloTabla.getValueAt(filaSeleccionada, 0).toString());
                String nombre = modeloTabla.getValueAt(filaSeleccionada, 1).toString();
                int anio = Integer.parseInt(modeloTabla.getValueAt(filaSeleccionada, 2).toString());
                String estadoStr = modeloTabla.getValueAt(filaSeleccionada, 3).toString();
                boolean estado = estadoStr.equals("Activa");

                materiaSeleccionada = new Materia(nombre, anio, estado);
                materiaSeleccionada.setIdMateria(id);

                comboEstadosMateria.setSelectedItem(estadoStr);

                habilitarBotones();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Error al seleccionar materia: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void habilitarBotones() {
        btnBorrarMateria.setEnabled(true);
        btnActualizarMateria.setEnabled(true);
        btnAltaBajaLogica.setEnabled(true);
        comboEstadosMateria.setEnabled(true);
    }

    private void deshabilitarBotones() {
        btnBorrarMateria.setEnabled(false);
        btnActualizarMateria.setEnabled(false);
        btnAltaBajaLogica.setEnabled(false);
        comboEstadosMateria.setEnabled(false);
    }

    private void restaurarEstadoNormal() {
        textNombreMateria.setText("");
        textAñoMateria.setText("");
        btnAgregarMateria.setText("Agregar");

        // Restaurar ActionListener original
        for (java.awt.event.ActionListener al : btnAgregarMateria.getActionListeners()) {
            btnAgregarMateria.removeActionListener(al);
        }
        btnAgregarMateria.addActionListener(e -> agregarMateria());

        materiaSeleccionada = null;
        deshabilitarBotones();
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tablaMaterias = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        textBuscarNombreMateria = new javax.swing.JTextField();
        btnBuscarNombre = new javax.swing.JButton();
        btnBorrarMateria = new javax.swing.JButton();
        btnActualizarMateria = new javax.swing.JButton();
        btnAltaBajaLogica = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        comboEstadosMateria = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        btnAgregarMateria = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        textNombreMateria = new javax.swing.JTextField();
        textAñoMateria = new javax.swing.JTextField();
        btnRefrescar = new javax.swing.JButton();

        setTitle("Ver Materias");

        jScrollPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane1MouseClicked(evt);
            }
        });

        tablaMaterias.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tablaMaterias.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaMateriasMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaMaterias);

        jLabel1.setText("Nombre de la Materia:");

        btnBuscarNombre.setText("Buscar");
        btnBuscarNombre.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBuscarNombreMouseClicked(evt);
            }
        });

        btnBorrarMateria.setText("Borrar");
        btnBorrarMateria.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBorrarMateriaMouseClicked(evt);
            }
        });

        btnActualizarMateria.setText("Actualizar");
        btnActualizarMateria.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnActualizarMateriaMouseClicked(evt);
            }
        });

        btnAltaBajaLogica.setText("Editar Estado");
        btnAltaBajaLogica.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAltaBajaLogicaMouseClicked(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Estado:");

        comboEstadosMateria.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activa", "Inactiva" }));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Agregar Materia"));

        btnAgregarMateria.setText("Agregar");
        btnAgregarMateria.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAgregarMateriaMouseClicked(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Nombre:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Año:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(textNombreMateria, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(textAñoMateria, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAgregarMateria)))
                .addContainerGap(65, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(textNombreMateria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(textAñoMateria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAgregarMateria))
                .addGap(15, 15, 15))
        );

        btnRefrescar.setText("Refrescar Tabla");
        btnRefrescar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRefrescarMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboEstadosMateria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnAltaBajaLogica)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(textBuscarNombreMateria, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBuscarNombre))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(14, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnBorrarMateria)
                        .addGap(28, 28, 28)
                        .addComponent(btnActualizarMateria)
                        .addGap(18, 18, 18)
                        .addComponent(btnRefrescar)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(textBuscarNombreMateria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarNombre))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBorrarMateria)
                    .addComponent(btnActualizarMateria)
                    .addComponent(btnRefrescar))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(comboEstadosMateria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAltaBajaLogica))
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregarMateriaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAgregarMateriaMouseClicked
        // TODO add your handling code here:
        agregarMateria();
    }//GEN-LAST:event_btnAgregarMateriaMouseClicked

    private void btnBuscarNombreMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscarNombreMouseClicked
        // TODO add your handling code here:
        buscarMateriaPorNombre();
    }//GEN-LAST:event_btnBuscarNombreMouseClicked

    private void btnBorrarMateriaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBorrarMateriaMouseClicked
        // TODO add your handling code here:
        borrarMateria();
    }//GEN-LAST:event_btnBorrarMateriaMouseClicked

    private void btnActualizarMateriaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnActualizarMateriaMouseClicked
        // TODO add your handling code here:
        guardarCambiosDesdeTabla();
    }//GEN-LAST:event_btnActualizarMateriaMouseClicked

    private void btnAltaBajaLogicaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAltaBajaLogicaMouseClicked
        // TODO add your handling code here:
        cambiarEstadoMateria();
    }//GEN-LAST:event_btnAltaBajaLogicaMouseClicked

    private void btnRefrescarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRefrescarMouseClicked
        // TODO add your handling code here:
        cargarDatosIniciales();
        restaurarEstadoNormal();
    }//GEN-LAST:event_btnRefrescarMouseClicked

    private void jScrollPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane1MouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_jScrollPane1MouseClicked

    private void tablaMateriasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaMateriasMouseClicked
        // TODO add your handling code here:
        seleccionarMateriaDeTabla();
    }//GEN-LAST:event_tablaMateriasMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizarMateria;
    private javax.swing.JButton btnAgregarMateria;
    private javax.swing.JButton btnAltaBajaLogica;
    private javax.swing.JButton btnBorrarMateria;
    private javax.swing.JButton btnBuscarNombre;
    private javax.swing.JButton btnRefrescar;
    private javax.swing.JComboBox<String> comboEstadosMateria;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaMaterias;
    private javax.swing.JTextField textAñoMateria;
    private javax.swing.JTextField textBuscarNombreMateria;
    private javax.swing.JTextField textNombreMateria;
    // End of variables declaration//GEN-END:variables
}
