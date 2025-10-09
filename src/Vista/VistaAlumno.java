/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package Vista;

import Modelo.Alumno;
import Modelo.Materia;
import Persistencia.AlumnoData;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author maria
 */
public class VistaAlumno extends javax.swing.JInternalFrame {

    //PAULA
    private AlumnoData alumnoData = new AlumnoData();
    private Alumno alumnoAct = null;
    private DefaultTableModel modelo = new DefaultTableModel() {

        public boolean isCellEditable(int fila, int column) {
            return column == 1 || column == 2 || column == 3 || column == 4;
        }
    };

    private void armarCabecera() {
        modelo.addColumn("Id");
        modelo.addColumn("DNI");
        modelo.addColumn("Apellido");
        modelo.addColumn("Nombre");
        modelo.addColumn("Fecha de Nacimiento");
        modelo.addColumn("Estado");
        jtAlumnos.setModel(modelo);
    }

    private void cargarDatos() {
        String activo;
        try {
            modelo.setRowCount(0);

            for (Alumno a : alumnoData.listarAlumnos()) {
                if (a.isEstado()) {
                    activo = "Activo";
                } else {
                    activo = "Inactivo";
                }
                modelo.addRow(new Object[]{
                    a.getIdAlumno(),
                    a.getDni(),
                    a.getApellido(),
                    a.getNombre(),
                    a.getFechaNacimiento(),
                    activo

                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los alumnos " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarAlumnoPorDni() {
        try {
            String dni = textBuscarDniAlumno.getText().trim();
            modelo.setRowCount(0);
            if (dni.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese un nombre para buscar", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            modelo.setRowCount(0);

            alumnoAct = alumnoData.buscarAlumno(Integer.parseInt(dni));
            String activo;
            if (alumnoAct != null) {
                if (alumnoAct.isEstado()) {
                    activo = "Activo";
                } else {
                    activo = "Inactivo";
                }
                modelo.addRow(new Object[]{
                    alumnoAct.getIdAlumno(),
                    alumnoAct.getDni(),
                    alumnoAct.getApellido(),
                    alumnoAct.getNombre(),
                    alumnoAct.getFechaNacimiento(),
                    activo

                });
            }
            textBuscarDniAlumno.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El dni es un numero: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarAlumnoNuevo() {
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        int dni = Integer.parseInt(txtDni.getText().trim());
        LocalDate fecha = JDateFecha.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        Alumno a = new Alumno(dni, apellido, nombre, fecha, true);
        alumnoData.guardarAlumno(a);

    }

    private void borrarAlumno() {
        int fila = jtAlumnos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "seleccione un alumno a eliminar", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }

        if (fila != -1) {
            int conf = JOptionPane.showConfirmDialog(
                    this,
                    "¿Seguro que desea eliminar el alumno seleccionado?", "Advertencia", JOptionPane.YES_NO_OPTION);

            if (conf == JOptionPane.YES_OPTION) {
                try {
                    int idAlumno = (int) jtAlumnos.getValueAt(fila, 0);

                    alumnoData.BorrarAlumno(idAlumno);

                    modelo.removeRow(fila);

                    JOptionPane.showMessageDialog(this, "Alumno eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this,
                            "Error al eliminar el alumno: " + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void guardarCambiosDesdeTabla() {
        int filaSeleccionada = jtAlumnos.getSelectedRow();

        try {
            // obtener datos de la fila seleccionada
            int id = Integer.parseInt(modelo.getValueAt(filaSeleccionada, 0).toString());
            int dni = Integer.parseInt(modelo.getValueAt(filaSeleccionada, 1).toString().trim());
            String apellido = modelo.getValueAt(filaSeleccionada, 2).toString().trim();
            String nombre = modelo.getValueAt(filaSeleccionada, 3).toString().trim();
            LocalDate anio = (LocalDate) modelo.getValueAt(filaSeleccionada, 4);
            String estadoStr = modelo.getValueAt(filaSeleccionada, 5).toString();
            boolean estado = estadoStr.equals("Activo");

            Alumno AlumnoActualizado = new Alumno(dni, apellido, nombre, anio, estado);
            AlumnoActualizado.setIdAlumno(id);

            alumnoData.actualizarAlumno(AlumnoActualizado);

            JOptionPane.showMessageDialog(this, "Alumno actualizado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            cargarDatos();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar alumno: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cambiarEstadoAlumno() {
        int fila = jtAlumnos.getSelectedRow();
        Alumno aux = new Alumno();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un alumno", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        aux.setIdAlumno((int) modelo.getValueAt(fila, 0));
        aux.setDni((int) modelo.getValueAt(fila, 1));
        aux.setApellido((String) modelo.getValueAt(fila, 2));
        aux.setNombre((String) modelo.getValueAt(fila, 3));
        String nuevoEstado = (String) comboEstadosMateria.getSelectedItem();
        boolean estadoBoolean = nuevoEstado.equals("Activo");

        try {
            if (estadoBoolean) {
                
                alumnoData.HabilitarAlumno(aux);
            } else {
                
                alumnoData.DeshabilitarAlumno(aux);
            }
            cargarDatos();

            JOptionPane.showMessageDialog(this, "Estado de la materia cambiado a: " + nuevoEstado, "Exito", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cambiar estado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    

    private void limpiarCampos() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtDni.setText("");
        JDateFecha.setDate(new Date());
    }

    /**
     * Creates new form VistaAlumno
     */
    public VistaAlumno() {
        initComponents();
        armarCabecera();
        cargarDatos();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        comboEstadosMateria = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        textBuscarDniAlumno = new javax.swing.JTextField();
        btnBuscarNombre = new javax.swing.JButton();
        btnBorrarAlumno = new javax.swing.JButton();
        btnRefrescar = new javax.swing.JButton();
        btnActualizarAlumno = new javax.swing.JButton();
        btnAltaBajaLogica = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        txtApellido = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtDni = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        JDateFecha = new com.toedter.calendar.JDateChooser();
        BtnAgregarAlumno = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtAlumnos = new javax.swing.JTable();
        jBSalir = new javax.swing.JButton();

        jLabel3.setText("jLabel3");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Estado:");

        comboEstadosMateria.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activo", "Inactivo" }));

        jLabel1.setText("Dni del Alumno:");

        btnBuscarNombre.setText("Buscar");
        btnBuscarNombre.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBuscarNombreMouseClicked(evt);
            }
        });
        btnBuscarNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarNombreActionPerformed(evt);
            }
        });

        btnBorrarAlumno.setText("Borrar");
        btnBorrarAlumno.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBorrarAlumnoMouseClicked(evt);
            }
        });

        btnRefrescar.setText("Refrescar Tabla");
        btnRefrescar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRefrescarMouseClicked(evt);
            }
        });

        btnActualizarAlumno.setText("Actualizar");
        btnActualizarAlumno.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnActualizarAlumnoMouseClicked(evt);
            }
        });

        btnAltaBajaLogica.setText("Editar Estado");
        btnAltaBajaLogica.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAltaBajaLogicaMouseClicked(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Agregar Alumno:"));

        jLabel4.setText("Nombre: ");

        jLabel5.setText("Apellido:");

        jLabel6.setText("DNI: ");

        jLabel7.setText("Fecha de Nacimiento: ");

        BtnAgregarAlumno.setText("Agregar");
        BtnAgregarAlumno.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnAgregarAlumnoMouseClicked(evt);
            }
        });
        BtnAgregarAlumno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAgregarAlumnoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(70, 70, 70)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(JDateFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addContainerGap(168, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(txtDni, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(BtnAgregarAlumno, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel6))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(BtnAgregarAlumno)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JDateFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30))
        );

        jtAlumnos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jtAlumnos);

        jBSalir.setText("Salir");
        jBSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBSalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(35, 35, 35)
                .addComponent(comboEstadosMateria, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(btnAltaBajaLogica)
                .addGap(111, 111, 111))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addComponent(btnBorrarAlumno, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnActualizarAlumno, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnRefrescar)
                .addGap(58, 58, 58))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(textBuscarDniAlumno, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnBuscarNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(27, 27, 27))
            .addGroup(layout.createSequentialGroup()
                .addGap(263, 263, 263)
                .addComponent(jBSalir)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(textBuscarDniAlumno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarNombre))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBorrarAlumno)
                    .addComponent(btnActualizarAlumno)
                    .addComponent(btnRefrescar))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(comboEstadosMateria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAltaBajaLogica))
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jBSalir))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarNombreMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBuscarNombreMouseClicked
        // TODO add your handling code here:
        buscarAlumnoPorDni();

    }//GEN-LAST:event_btnBuscarNombreMouseClicked

    private void btnBorrarAlumnoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBorrarAlumnoMouseClicked
        // TODO add your handling code here:
        borrarAlumno();

    }//GEN-LAST:event_btnBorrarAlumnoMouseClicked

    private void btnRefrescarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRefrescarMouseClicked
        // TODO add your handling code here:
        cargarDatos();

    }//GEN-LAST:event_btnRefrescarMouseClicked

    private void btnActualizarAlumnoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnActualizarAlumnoMouseClicked
        // TODO add your handling code here:
        guardarCambiosDesdeTabla();

    }//GEN-LAST:event_btnActualizarAlumnoMouseClicked

    private void btnAltaBajaLogicaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAltaBajaLogicaMouseClicked
        // TODO add your handling code here:
        cambiarEstadoAlumno();

    }//GEN-LAST:event_btnAltaBajaLogicaMouseClicked

    private void BtnAgregarAlumnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAgregarAlumnoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnAgregarAlumnoActionPerformed

    private void BtnAgregarAlumnoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnAgregarAlumnoMouseClicked
        // TODO add your handling code here:
        agregarAlumnoNuevo();
        limpiarCampos();
    }//GEN-LAST:event_BtnAgregarAlumnoMouseClicked

    private void btnBuscarNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBuscarNombreActionPerformed

    private void jBSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBSalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jBSalirActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnAgregarAlumno;
    private com.toedter.calendar.JDateChooser JDateFecha;
    private javax.swing.JButton btnActualizarAlumno;
    private javax.swing.JButton btnAltaBajaLogica;
    private javax.swing.JButton btnBorrarAlumno;
    private javax.swing.JButton btnBuscarNombre;
    private javax.swing.JButton btnRefrescar;
    private javax.swing.JComboBox<String> comboEstadosMateria;
    private javax.swing.JButton jBSalir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jtAlumnos;
    private javax.swing.JTextField textBuscarDniAlumno;
    private javax.swing.JTextField txtApellido;
    private javax.swing.JTextField txtDni;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}
