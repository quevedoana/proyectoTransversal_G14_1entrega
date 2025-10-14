package Vista;

import Modelo.Alumno;
import Modelo.Inscripcion;
import Modelo.Materia;
import Persistencia.AlumnoData;
import Persistencia.InscripcionData;
import Persistencia.MateriaData;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author maria
 */
public class VistaCargaNotas extends javax.swing.JInternalFrame {

    private AlumnoData alumnoData = new AlumnoData();
    private MateriaData materiaData = new MateriaData();
    private InscripcionData inscripcionData = new InscripcionData();
    private List<Alumno> listaAlumnos;
    private DefaultTableModel tabla;

    /**
     * Creates new form VistaCargaNotas
     */
    public VistaCargaNotas() {
        initComponents();
        cargarComboAlumnos();
        armarCabeceraTabla();
        agregarListeners();
    }

    private void cargarComboAlumnos() {
        listaAlumnos = alumnoData.listarAlumnos();
        comboAlumnos.removeAllItems();

        // filtrar solo alumnos activos
        for (Alumno alumno : listaAlumnos) {
            if (alumno.isEstado()) {
                comboAlumnos.addItem(alumno);
            }
        }

        if (comboAlumnos.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "No hay alumnos activos disponibles.");
        }
    }

    private void armarCabeceraTabla() {
        tabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Solo permite editar la columna de notas
                return column == 2;
            }
        };

        String[] columnas = {"ID Materia", "Nombre Materia", "Nota"};
        for (String columna : columnas) {
            tabla.addColumn(columna);
        }

        tablaAlumnoNotas.setModel(tabla);
    }

    private void cargarMateriasYNotas(Alumno alumno) {
        while (tabla.getRowCount() > 0) {
            tabla.removeRow(0);
        }

        if (alumno != null) {
            // verifica que el alumno este activo
            if (!alumno.isEstado()) {
                JOptionPane.showMessageDialog(this, "El alumno seleccionado está inactivo.");
                return;
            }

            List<Inscripcion> inscripciones = inscripcionData.buscarInscripcionesPorAlumno(alumno.getIdAlumno());

            for (Inscripcion inscripcion : inscripciones) {
                Materia materia = materiaData.buscarMateriaPorId(inscripcion.getMateria().getIdMateria());

                // solo mostrar materias activas
                if (materia != null && materia.isEstado()) {
                    Object[] fila = {
                        materia.getIdMateria(),
                        materia.getNombre(),
                        inscripcion.getNota()
                    };
                    tabla.addRow(fila);
                }
            }

            // si no hay materias activas
            if (tabla.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "El alumno no tiene materias activas asignadas.");
            }
        }
    }

    private void agregarListeners() {
        // Listener para el combo de alumnos
        comboAlumnos.addActionListener(e -> {
            Alumno alumnoSeleccionado = (Alumno) comboAlumnos.getSelectedItem();
            if (alumnoSeleccionado != null) {
                cargarMateriasYNotas(alumnoSeleccionado);
            }
        });

        // Listener para el botón Guardar
        btnGuardar.addActionListener(e -> {
            guardarNotas();
        });

        // Listener para el botón Salir
        JBsalir.addActionListener(e -> {
            this.dispose();
        });
    }

    private void guardarNotas() {
        Alumno alumnoSeleccionado = (Alumno) comboAlumnos.getSelectedItem();
        if (alumnoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un alumno");
            return;
        }

        // verifica que el alumno este activo
        if (!alumnoSeleccionado.isEstado()) {
            JOptionPane.showMessageDialog(this, "No se pueden cargar notas a un alumno inactivo.");
            return;
        }

        int filas = tabla.getRowCount();
        

        for (int i = 0; i < filas; i++) {
            try {
                int idMateria = Integer.parseInt(tabla.getValueAt(i, 0).toString());
                Object valorNota = tabla.getValueAt(i, 2);

                // verifica que la materia este activa
                Materia materia = materiaData.buscarMateriaPorId(idMateria);
                

                if (valorNota != null && !valorNota.toString().trim().isEmpty()) {
                    double nota = Double.parseDouble(valorNota.toString().trim());

                    // controlar nota entre 0 y 10
                    if (nota >= 0 && nota <= 10) {
                        inscripcionData.actualizarNota(alumnoSeleccionado.getIdAlumno(), idMateria, nota);
                        
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "La nota debe estar entre 0 y 10. Materia en fila " + (i + 1));
                        return;
                    }
                    JOptionPane.showMessageDialog(this,"Nota cargada correctamente");
                    cargarMateriasYNotas(alumnoSeleccionado);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Formato de nota invalido en la fila " + (i + 1) + ". Use numeros.");
                return;
            }
        }
    }

        /**
         * This method is called from within the constructor to initialize the
         * form. WARNING: Do NOT modify this code. The content of this method is
         * always regenerated by the Form Editor.
         */
        @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        tablaAlumnoNotas = new javax.swing.JTable();
        JBsalir = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        comboAlumnos = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jInternalFrame1 = new javax.swing.JInternalFrame();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaAlumnoNotas1 = new javax.swing.JTable();
        JBsalir1 = new javax.swing.JButton();
        btnGuardar1 = new javax.swing.JButton();
        comboAlumnos1 = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(204, 204, 204));
        setTitle("Carga de Notas");

        tablaAlumnoNotas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(tablaAlumnoNotas);

        JBsalir.setBackground(new java.awt.Color(204, 204, 204));
        JBsalir.setText("Salir");

        btnGuardar.setBackground(new java.awt.Color(204, 204, 204));
        btnGuardar.setText("Guardar");
        btnGuardar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnGuardarMouseClicked(evt);
            }
        });

        jLabel2.setText("Seleccione un Alumno:");

        jInternalFrame1.setBackground(new java.awt.Color(204, 204, 204));
        jInternalFrame1.setTitle("Carga de Notas");

        tablaAlumnoNotas1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane3.setViewportView(tablaAlumnoNotas1);

        JBsalir1.setBackground(new java.awt.Color(204, 204, 204));
        JBsalir1.setText("Salir");

        btnGuardar1.setBackground(new java.awt.Color(204, 204, 204));
        btnGuardar1.setText("Guardar");
        btnGuardar1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnGuardar1MouseClicked(evt);
            }
        });

        jLabel3.setText("Seleccione un Alumno:");

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jInternalFrame1Layout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addComponent(btnGuardar1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(JBsalir1)
                        .addGap(85, 85, 85))
                    .addGroup(jInternalFrame1Layout.createSequentialGroup()
                        .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jInternalFrame1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(comboAlumnos1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(comboAlumnos1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JBsalir1)
                    .addComponent(btnGuardar1))
                .addGap(20, 20, 20))
        );

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Carga de notas");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addComponent(btnGuardar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(JBsalir)
                        .addGap(85, 85, 85))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(comboAlumnos, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addContainerGap(19, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(121, 121, 121))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jInternalFrame1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(comboAlumnos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JBsalir)
                    .addComponent(btnGuardar))
                .addGap(20, 20, 20))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jInternalFrame1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGuardarMouseClicked
        // TODO add your handling code here:
        guardarNotas();
    }//GEN-LAST:event_btnGuardarMouseClicked

    private void btnGuardar1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGuardar1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnGuardar1MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JBsalir;
    private javax.swing.JButton JBsalir1;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnGuardar1;
    private javax.swing.JComboBox<Alumno> comboAlumnos;
    private javax.swing.JComboBox<Alumno> comboAlumnos1;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable tablaAlumnoNotas;
    private javax.swing.JTable tablaAlumnoNotas1;
    // End of variables declaration//GEN-END:variables

}
