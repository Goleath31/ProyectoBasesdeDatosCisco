/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Presentacion;

import Negocio.ComputadoraNegocio;
import Negocio.IComputadoraNegocio;
import Negocio.NegocioException;
import Persistencia.ComputadoraDAO;
import Persistencia.ConexionBD;
import Persistencia.IComputadoraDAO;
import Persistencia.IConexionBD;
import dtos.ComputadoraTablaDTO;
import java.net.InetAddress;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author golea
 */
public class Administrador_Equipos extends javax.swing.JFrame {

    private final IComputadoraNegocio computadoraNegocio;
    private int idLaboratorioActual = 0;
    private int paginaActual = 1;
    private final int REGISTROS_POR_PAGINA = 5;
    private int totalPaginas = 1;

    /**
     * Creates new form Administrador_Equipos
     */
    public Administrador_Equipos() {
        initComponents();
        if (tabladeequipos != null) {
            javax.swing.table.DefaultTableModel modeloCuatroColumnas = new javax.swing.table.DefaultTableModel(
                    new Object[][]{},
                    new String[]{"ID Equipo", "Dirección IP", "Número Máquina", "Estado"}
            ) {
                @Override
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return false;
                }
            };
            tabladeequipos.setModel(modeloCuatroColumnas);
        }
        IConexionBD conexionBD = new ConexionBD();
        IComputadoraDAO computadoraDAO = new ComputadoraDAO(conexionBD);
        this.computadoraNegocio = new ComputadoraNegocio(computadoraDAO);
        this.inicializarPanelAdministrador();

    }

    private void inicializarPanelAdministrador() {
        try {
            String ipLocal = InetAddress.getLocalHost().getHostAddress();
            System.out.println("IP detectada de la máquina: " + ipLocal);
            /*midificar si hacen pruebas*/
            ipLocal = "192.168.2.3";

            ComputadoraTablaDTO labInfo = computadoraNegocio.obtenerIdLaboratorioPorIP(ipLocal);

            if (labInfo != null) {
                this.idLaboratorioActual = labInfo.getIdComputadora(); // Recuperamos el ID
                String nombreLaboratorio = labInfo.getEstatus();       // Recuperamos el Nombre

                lbllaboratorionombre.setText(nombreLaboratorio);
                txtcontraseña.setText(labInfo.getDireccionIp());

            } else {
                // SALVAVIDAS DESARROLLO: Si tu IP de Wi-Fi actual no está en el SQL, forzamos datos de prueba
                System.out.println("La IP no está en la BD. Usando Laboratorio de Pruebas.");
                this.idLaboratorioActual = 1;
                lbllaboratorionombre.setText("Laboratorio de Cómputo 1");
            }

            // 3. Poblar elementos visuales de forma segura
            this.cargarTablaComputadoras();
            this.actualizarMetricasUso();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error al establecer comunicación con el centro de cómputo: " + e.getMessage(),
                    "Error de Carga",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarTablaComputadoras() {
        try {
            if (tabladeequipos == null) {
                return;
            }

            DefaultTableModel modeloTabla = (DefaultTableModel) tabladeequipos.getModel();
            modeloTabla.setRowCount(0);
            List<ComputadoraTablaDTO> listaEquipos = computadoraNegocio.obtenerComputadorasPorLaboratorio(idLaboratorioActual);

            if (listaEquipos != null) {
                for (ComputadoraTablaDTO dto : listaEquipos) {
                    Object[] fila = new Object[]{
                        dto.getIdComputadora(),
                        dto.getDireccionIp(),
                        dto.getNumeroMaquina(),
                        dto.getEstatus()
                    };
                    modeloTabla.addRow(fila);
                }
            }
        } catch (NegocioException e) {
            JOptionPane.showMessageDialog(this, "Error de Datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarMetricasUso() {
        try {
            if (lblnumeroenuso != null) {
                int enUso = computadoraNegocio.obtenerContadorEquiposEnUso(idLaboratorioActual);
                lblnumeroenuso.setText(String.valueOf(enUso));
            }

            if (jLabel8lblnumeroequipostotales != null) {
                int totales = computadoraNegocio.obtenerTotalEquiposLaboratorio(idLaboratorioActual);
                jLabel8lblnumeroequipostotales.setText(String.valueOf(totales));
            }

            if (lblocupacionporcentage != null) {
                String porcentajeTexto = computadoraNegocio.calcularPorcentajeOcupacion(idLaboratorioActual);
                lblocupacionporcentage.setText(porcentajeTexto);
            }

        } catch (NegocioException e) {
            System.err.println("No se pudieron refrescar los contadores y porcentajes en la interfaz: " + e.getMessage());
        }
    }

    public int getIdLaboratorioActual() {
        return this.idLaboratorioActual;
    }

    public Negocio.IComputadoraNegocio getComputadoraNegocio() {
        return this.computadoraNegocio;
    }

    private void cargarTablaComputadorasPaginada() {
        try {
            if (tabladeequipos == null) {
                return;
            }

            DefaultTableModel modeloTabla = (DefaultTableModel) tabladeequipos.getModel();
            modeloTabla.setRowCount(0);

            String criterio = tctbuscarequipo.getText();
            String estatus = (cbxestados.getSelectedItem() != null) ? cbxestados.getSelectedItem().toString() : "Todos";

            this.totalPaginas = computadoraNegocio.calcularTotalPaginas(idLaboratorioActual, criterio, estatus, REGISTROS_POR_PAGINA);

            if (this.paginaActual > this.totalPaginas) {
                this.paginaActual = this.totalPaginas;
            }
            if (this.paginaActual < 1) {
                this.paginaActual = 1;
            }

            lblcontadordepaginas.setText("Mostrando " + this.paginaActual + " de " + this.totalPaginas + " Paginas Equipos");

            List<ComputadoraTablaDTO> listaEquipos = computadoraNegocio.filtrarComputadorasPorLaboratorio(
                    idLaboratorioActual, criterio, estatus, this.paginaActual, REGISTROS_POR_PAGINA);

            if (listaEquipos != null) {
                for (ComputadoraTablaDTO dto : listaEquipos) {
                    Object[] fila = new Object[]{
                        dto.getIdComputadora(),
                        dto.getDireccionIp(),
                        dto.getNumeroMaquina(),
                        dto.getEstatus()
                    };
                    modeloTabla.addRow(fila);
                }
            }
        } catch (NegocioException e) {
            JOptionPane.showMessageDialog(this, "Error de actualización visual: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelfondo = new javax.swing.JPanel();
        panelbarraequipos_bloqueos = new javax.swing.JPanel();
        contenedordeopcioneslaterales = new javax.swing.JPanel();
        btnBloqueos = new javax.swing.JButton();
        BtnDesbloqueos = new javax.swing.JButton();
        lblEquipos = new javax.swing.JLabel();
        contenedorsuperior = new javax.swing.JPanel();
        lbltitulo = new javax.swing.JLabel();
        btnRegresar = new javax.swing.JButton();
        lbllaboratorionombre = new javax.swing.JLabel();
        lblusuario = new javax.swing.JLabel();
        lbladmin = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        contenedorequipostotales = new javax.swing.JPanel();
        lblequipostotales = new javax.swing.JLabel();
        jLabel8lblnumeroequipostotales = new javax.swing.JLabel();
        contenedorenuso = new javax.swing.JPanel();
        lblenuso = new javax.swing.JLabel();
        lblnumeroenuso = new javax.swing.JLabel();
        contenedorcocupacion = new javax.swing.JPanel();
        lblocupacionactual = new javax.swing.JLabel();
        lblocupacionporcentage = new javax.swing.JLabel();
        panelcontenido = new javax.swing.JPanel();
        lblestado = new javax.swing.JLabel();
        cbxestados = new javax.swing.JComboBox<>();
        lblbuscarequipo = new javax.swing.JLabel();
        tctbuscarequipo = new javax.swing.JTextField();
        btnfltrar = new javax.swing.JButton();
        btnrestaurartabla = new javax.swing.JButton();
        btnbloquearequipo = new javax.swing.JButton();
        btndesbloquearequipo = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabladeequipos = new javax.swing.JTable();
        panelcontraseñamaestra = new javax.swing.JPanel();
        lblContraseñatitulo = new javax.swing.JLabel();
        lbltxtclabedeacceso = new javax.swing.JLabel();
        txtcontraseña = new javax.swing.JTextField();
        lblcontadordepaginas = new javax.swing.JLabel();
        btnizquierda = new javax.swing.JButton();
        btnderecha = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelfondo.setBackground(new java.awt.Color(255, 51, 51));
        panelfondo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelbarraequipos_bloqueos.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(226, 226, 226), 2, true));

        btnBloqueos.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btnBloqueos.setForeground(new java.awt.Color(102, 102, 102));
        btnBloqueos.setText("Bloqueos");
        btnBloqueos.setBorder(null);
        btnBloqueos.setContentAreaFilled(false);
        btnBloqueos.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnBloqueos.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnBloqueos.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnBloqueos.setInheritsPopupMenu(true);
        btnBloqueos.setMaximumSize(new java.awt.Dimension(46, 14));
        btnBloqueos.setMinimumSize(new java.awt.Dimension(46, 14));
        btnBloqueos.setPreferredSize(new java.awt.Dimension(46, 14));
        btnBloqueos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBloqueosActionPerformed(evt);
            }
        });

        BtnDesbloqueos.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        BtnDesbloqueos.setForeground(new java.awt.Color(102, 102, 102));
        BtnDesbloqueos.setText("Desbloqueos");
        BtnDesbloqueos.setBorder(null);
        BtnDesbloqueos.setContentAreaFilled(false);
        BtnDesbloqueos.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        BtnDesbloqueos.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        BtnDesbloqueos.setMaximumSize(new java.awt.Dimension(46, 14));
        BtnDesbloqueos.setMinimumSize(new java.awt.Dimension(46, 14));
        BtnDesbloqueos.setPreferredSize(new java.awt.Dimension(46, 14));

        lblEquipos.setBackground(new java.awt.Color(226, 226, 226));
        lblEquipos.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        lblEquipos.setForeground(new java.awt.Color(0, 86, 150));
        lblEquipos.setText("Equipos");
        lblEquipos.setOpaque(true);

        javax.swing.GroupLayout contenedordeopcioneslateralesLayout = new javax.swing.GroupLayout(contenedordeopcioneslaterales);
        contenedordeopcioneslaterales.setLayout(contenedordeopcioneslateralesLayout);
        contenedordeopcioneslateralesLayout.setHorizontalGroup(
            contenedordeopcioneslateralesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contenedordeopcioneslateralesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(contenedordeopcioneslateralesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(BtnDesbloqueos, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
                    .addComponent(btnBloqueos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(contenedordeopcioneslateralesLayout.createSequentialGroup()
                        .addComponent(lblEquipos, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        contenedordeopcioneslateralesLayout.setVerticalGroup(
            contenedordeopcioneslateralesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contenedordeopcioneslateralesLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(lblEquipos, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBloqueos, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BtnDesbloqueos, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelbarraequipos_bloqueosLayout = new javax.swing.GroupLayout(panelbarraequipos_bloqueos);
        panelbarraequipos_bloqueos.setLayout(panelbarraequipos_bloqueosLayout);
        panelbarraequipos_bloqueosLayout.setHorizontalGroup(
            panelbarraequipos_bloqueosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelbarraequipos_bloqueosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(contenedordeopcioneslaterales, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelbarraequipos_bloqueosLayout.setVerticalGroup(
            panelbarraequipos_bloqueosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelbarraequipos_bloqueosLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(contenedordeopcioneslaterales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(436, Short.MAX_VALUE))
        );

        panelfondo.add(panelbarraequipos_bloqueos, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 160, 620));

        lbltitulo.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lbltitulo.setForeground(new java.awt.Color(0, 86, 150));
        lbltitulo.setText("ITSON labs");
        lbltitulo.setPreferredSize(new java.awt.Dimension(40, 16));

        btnRegresar.setText("Regresar");
        btnRegresar.setBorder(null);
        btnRegresar.setContentAreaFilled(false);
        btnRegresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegresarActionPerformed(evt);
            }
        });

        lbllaboratorionombre.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        lbllaboratorionombre.setForeground(new java.awt.Color(0, 86, 150));
        lbllaboratorionombre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbllaboratorionombre.setText("LAboratorio Nombre");
        lbllaboratorionombre.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lbllaboratorionombre.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        lblusuario.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        lblusuario.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblusuario.setText("Usuario");
        lblusuario.setToolTipText("");
        lblusuario.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lblusuario.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        lbladmin.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        lbladmin.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbladmin.setText("Admin");

        javax.swing.GroupLayout contenedorsuperiorLayout = new javax.swing.GroupLayout(contenedorsuperior);
        contenedorsuperior.setLayout(contenedorsuperiorLayout);
        contenedorsuperiorLayout.setHorizontalGroup(
            contenedorsuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contenedorsuperiorLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(lbltitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 351, Short.MAX_VALUE)
                .addComponent(btnRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(lbllaboratorionombre, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(contenedorsuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblusuario, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbladmin, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        contenedorsuperiorLayout.setVerticalGroup(
            contenedorsuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contenedorsuperiorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(contenedorsuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(contenedorsuperiorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbltitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnRegresar)
                        .addComponent(lbllaboratorionombre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(contenedorsuperiorLayout.createSequentialGroup()
                        .addComponent(lblusuario)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbladmin)))
                .addContainerGap())
        );

        panelfondo.add(contenedorsuperior, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 840, 40));

        jPanel3.setBackground(new java.awt.Color(195, 199, 202));

        contenedorequipostotales.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(226, 226, 226), 2, true));

        lblequipostotales.setBackground(new java.awt.Color(226, 226, 226));
        lblequipostotales.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        lblequipostotales.setForeground(new java.awt.Color(93, 95, 95));
        lblequipostotales.setText("EQUIPOS TOTALES");

        jLabel8lblnumeroequipostotales.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel8lblnumeroequipostotales.setForeground(new java.awt.Color(0, 86, 150));
        jLabel8lblnumeroequipostotales.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel8lblnumeroequipostotales.setText("300");

        javax.swing.GroupLayout contenedorequipostotalesLayout = new javax.swing.GroupLayout(contenedorequipostotales);
        contenedorequipostotales.setLayout(contenedorequipostotalesLayout);
        contenedorequipostotalesLayout.setHorizontalGroup(
            contenedorequipostotalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contenedorequipostotalesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(contenedorequipostotalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8lblnumeroequipostotales, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblequipostotales))
                .addContainerGap(84, Short.MAX_VALUE))
        );
        contenedorequipostotalesLayout.setVerticalGroup(
            contenedorequipostotalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contenedorequipostotalesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblequipostotales, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8lblnumeroequipostotales)
                .addGap(37, 37, 37))
        );

        contenedorenuso.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(226, 226, 226), 2, true));

        lblenuso.setBackground(new java.awt.Color(226, 226, 226));
        lblenuso.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        lblenuso.setForeground(new java.awt.Color(93, 95, 95));
        lblenuso.setText("EN USO");

        lblnumeroenuso.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        lblnumeroenuso.setForeground(new java.awt.Color(0, 86, 150));
        lblnumeroenuso.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblnumeroenuso.setText("300");

        javax.swing.GroupLayout contenedorenusoLayout = new javax.swing.GroupLayout(contenedorenuso);
        contenedorenuso.setLayout(contenedorenusoLayout);
        contenedorenusoLayout.setHorizontalGroup(
            contenedorenusoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contenedorenusoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(contenedorenusoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblenuso, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblnumeroenuso, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(115, Short.MAX_VALUE))
        );
        contenedorenusoLayout.setVerticalGroup(
            contenedorenusoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contenedorenusoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblenuso, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblnumeroenuso)
                .addGap(37, 37, 37))
        );

        contenedorcocupacion.setBackground(new java.awt.Color(0, 86, 150));
        contenedorcocupacion.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 86, 150), 2, true));

        lblocupacionactual.setBackground(new java.awt.Color(226, 226, 226));
        lblocupacionactual.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        lblocupacionactual.setForeground(new java.awt.Color(255, 255, 255));
        lblocupacionactual.setText("OCUPACION ACTUAL");

        lblocupacionporcentage.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        lblocupacionporcentage.setForeground(new java.awt.Color(255, 255, 255));
        lblocupacionporcentage.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblocupacionporcentage.setText("30%");

        javax.swing.GroupLayout contenedorcocupacionLayout = new javax.swing.GroupLayout(contenedorcocupacion);
        contenedorcocupacion.setLayout(contenedorcocupacionLayout);
        contenedorcocupacionLayout.setHorizontalGroup(
            contenedorcocupacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contenedorcocupacionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(contenedorcocupacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblocupacionporcentage, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblocupacionactual, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        contenedorcocupacionLayout.setVerticalGroup(
            contenedorcocupacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contenedorcocupacionLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblocupacionactual, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblocupacionporcentage)
                .addGap(37, 37, 37))
        );

        panelcontenido.setBackground(new java.awt.Color(255, 255, 255));

        lblestado.setBackground(new java.awt.Color(226, 226, 226));
        lblestado.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        lblestado.setForeground(new java.awt.Color(93, 95, 95));
        lblestado.setText("Estados");

        cbxestados.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        cbxestados.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Disponible", "En uso", "Bloqueado" }));
        cbxestados.setPreferredSize(new java.awt.Dimension(40, 15));
        cbxestados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxestadosActionPerformed(evt);
            }
        });

        lblbuscarequipo.setBackground(new java.awt.Color(226, 226, 226));
        lblbuscarequipo.setFont(new java.awt.Font("Arial", 1, 10)); // NOI18N
        lblbuscarequipo.setForeground(new java.awt.Color(93, 95, 95));
        lblbuscarequipo.setText("Buscar Equipo:");

        tctbuscarequipo.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        tctbuscarequipo.setPreferredSize(new java.awt.Dimension(73, 15));

        btnfltrar.setText("Fltrar");
        btnfltrar.setPreferredSize(new java.awt.Dimension(73, 15));
        btnfltrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnfltrarActionPerformed(evt);
            }
        });

        btnrestaurartabla.setBackground(new java.awt.Color(0, 86, 150));
        btnrestaurartabla.setFont(new java.awt.Font("Arial", 0, 8)); // NOI18N
        btnrestaurartabla.setForeground(new java.awt.Color(255, 255, 255));
        btnrestaurartabla.setText("Restaurar Tabla");
        btnrestaurartabla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnrestaurartablaActionPerformed(evt);
            }
        });

        btnbloquearequipo.setBackground(new java.awt.Color(255, 0, 0));
        btnbloquearequipo.setFont(new java.awt.Font("Arial", 0, 8)); // NOI18N
        btnbloquearequipo.setForeground(new java.awt.Color(255, 255, 255));
        btnbloquearequipo.setText("Bloquear Equipo");
        btnbloquearequipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbloquearequipoActionPerformed(evt);
            }
        });

        btndesbloquearequipo.setBackground(new java.awt.Color(0, 150, 86));
        btndesbloquearequipo.setFont(new java.awt.Font("Arial", 0, 8)); // NOI18N
        btndesbloquearequipo.setForeground(new java.awt.Color(255, 255, 255));
        btndesbloquearequipo.setText("Desbloquear Equipo");
        btndesbloquearequipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btndesbloquearequipoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 352, Short.MAX_VALUE)
        );

        tabladeequipos.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jScrollPane1.setViewportView(tabladeequipos);
        if (tabladeequipos.getColumnModel().getColumnCount() > 0) {
            tabladeequipos.getColumnModel().getColumn(0).setResizable(false);
            tabladeequipos.getColumnModel().getColumn(1).setResizable(false);
            tabladeequipos.getColumnModel().getColumn(2).setResizable(false);
            tabladeequipos.getColumnModel().getColumn(3).setResizable(false);
            tabladeequipos.getColumnModel().getColumn(4).setResizable(false);
        }

        panelcontraseñamaestra.setBackground(new java.awt.Color(226, 226, 226));
        panelcontraseñamaestra.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(226, 226, 226), 2, true));
        panelcontraseñamaestra.setOpaque(false);

        lblContraseñatitulo.setBackground(new java.awt.Color(226, 226, 226));
        lblContraseñatitulo.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        lblContraseñatitulo.setForeground(new java.awt.Color(0, 86, 150));
        lblContraseñatitulo.setText("Contaseña Maestra:");

        lbltxtclabedeacceso.setBackground(new java.awt.Color(226, 226, 226));
        lbltxtclabedeacceso.setFont(new java.awt.Font("Arial", 1, 8)); // NOI18N
        lbltxtclabedeacceso.setForeground(new java.awt.Color(93, 95, 95));
        lbltxtclabedeacceso.setText("CLAVE DE ACCESO GLOBAL");

        txtcontraseña.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        txtcontraseña.setForeground(new java.awt.Color(0, 86, 150));
        txtcontraseña.setText("iTSON");
        txtcontraseña.setEnabled(false);
        txtcontraseña.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtcontraseñaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelcontraseñamaestraLayout = new javax.swing.GroupLayout(panelcontraseñamaestra);
        panelcontraseñamaestra.setLayout(panelcontraseñamaestraLayout);
        panelcontraseñamaestraLayout.setHorizontalGroup(
            panelcontraseñamaestraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelcontraseñamaestraLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelcontraseñamaestraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblContraseñatitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtcontraseña, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbltxtclabedeacceso))
                .addContainerGap(47, Short.MAX_VALUE))
        );
        panelcontraseñamaestraLayout.setVerticalGroup(
            panelcontraseñamaestraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelcontraseñamaestraLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblContraseñatitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbltxtclabedeacceso, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtcontraseña, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lblcontadordepaginas.setBackground(new java.awt.Color(226, 226, 226));
        lblcontadordepaginas.setFont(new java.awt.Font("Arial", 1, 8)); // NOI18N
        lblcontadordepaginas.setForeground(new java.awt.Color(93, 95, 95));
        lblcontadordepaginas.setText("Mostrando 1 de 45 Paginas Equipos");

        btnizquierda.setText("<");
        btnizquierda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnizquierdaActionPerformed(evt);
            }
        });

        btnderecha.setText(">");
        btnderecha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnderechaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelcontenidoLayout = new javax.swing.GroupLayout(panelcontenido);
        panelcontenido.setLayout(panelcontenidoLayout);
        panelcontenidoLayout.setHorizontalGroup(
            panelcontenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelcontenidoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelcontenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelcontenidoLayout.createSequentialGroup()
                        .addGroup(panelcontenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelcontenidoLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(lblcontadordepaginas, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnizquierda, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnderecha, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(43, 43, 43))
                            .addGroup(panelcontenidoLayout.createSequentialGroup()
                                .addGroup(panelcontenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 656, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(panelcontraseñamaestra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(panelcontenidoLayout.createSequentialGroup()
                        .addGroup(panelcontenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblestado, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxestados, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelcontenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblbuscarequipo, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelcontenidoLayout.createSequentialGroup()
                                .addComponent(tctbuscarequipo, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnfltrar, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28)
                                .addComponent(btndesbloquearequipo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnbloquearequipo, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnrestaurartabla)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        panelcontenidoLayout.setVerticalGroup(
            panelcontenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelcontenidoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelcontenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblestado, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblbuscarequipo, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelcontenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxestados, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tctbuscarequipo, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnfltrar, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btndesbloquearequipo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnbloquearequipo, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnrestaurartabla, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(panelcontenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelcontenidoLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelcontenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblcontadordepaginas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(panelcontenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnizquierda, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addComponent(btnderecha, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                        .addGap(18, 18, 18)
                        .addComponent(panelcontraseñamaestra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(47, 47, 47))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelcontenido, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(contenedorequipostotales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(contenedorenuso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(contenedorcocupacion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(contenedorcocupacion, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(contenedorenuso, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(contenedorequipostotales, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelcontenido, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelfondo.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 40, 680, 620));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelfondo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelfondo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbxestadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxestadosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxestadosActionPerformed

    private void btnrestaurartablaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnrestaurartablaActionPerformed
        // TODO add your handling code here:
        // 1. Limpiar el cuadro de texto de búsqueda
        tctbuscarequipo.setText("");
        cbxestados.setSelectedIndex(0);
        this.paginaActual = 1;
        this.cargarTablaComputadorasPaginada();
        this.actualizarMetricasUso();


    }//GEN-LAST:event_btnrestaurartablaActionPerformed

    private void btnbloquearequipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbloquearequipoActionPerformed
        // TODO add your handling code here:
        javax.swing.JDialog dialogo = new javax.swing.JDialog(this, "Bloquear Equipo", true);

        Confirmacion_Bloqueo_Equipos panelBloqueo = new Confirmacion_Bloqueo_Equipos(this, dialogo);

        dialogo.getContentPane().add(panelBloqueo);
        dialogo.pack();
        dialogo.setLocationRelativeTo(this);
        dialogo.setVisible(true);

    }//GEN-LAST:event_btnbloquearequipoActionPerformed

    private void btnRegresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegresarActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnRegresarActionPerformed

    private void txtcontraseñaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtcontraseñaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtcontraseñaActionPerformed

    private void btnfltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnfltrarActionPerformed

        this.paginaActual = 1;
        this.cargarTablaComputadorasPaginada();
    }//GEN-LAST:event_btnfltrarActionPerformed

    private void btnderechaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnderechaActionPerformed
        // TODO add your handling code here:
        if (this.paginaActual < this.totalPaginas) {
            this.paginaActual++;
            this.cargarTablaComputadorasPaginada();
        }
    }//GEN-LAST:event_btnderechaActionPerformed

    private void btnizquierdaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnizquierdaActionPerformed
        if (this.paginaActual > 1) {
            this.paginaActual--;
            this.cargarTablaComputadorasPaginada();
        }
    }//GEN-LAST:event_btnizquierdaActionPerformed

    private void btndesbloquearequipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btndesbloquearequipoActionPerformed
        javax.swing.JDialog dialogo = new javax.swing.JDialog(this, "Desbloquear Equipo", true);
        Confirmacion_Desbloqueo_Equipo panelDesbloqueo = new Confirmacion_Desbloqueo_Equipo(this, dialogo);
        dialogo.getContentPane().add(panelDesbloqueo);
        dialogo.pack();
        dialogo.setLocationRelativeTo(this);
        dialogo.setVisible(true);
    }//GEN-LAST:event_btndesbloquearequipoActionPerformed

    private void btnBloqueosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBloqueosActionPerformed

        String nombreLab = this.lbllaboratorionombre.getText();
        Administrador_Bloqueos panelBloqueos = new Administrador_Bloqueos(nombreLab);

        javax.swing.JFrame frame = new javax.swing.JFrame("Administración de Bloqueos");

        frame.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
        frame.add(panelBloqueos);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        frame.setVisible(true);
        this.dispose();


    }//GEN-LAST:event_btnBloqueosActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Administrador_Equipos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Administrador_Equipos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Administrador_Equipos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Administrador_Equipos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Administrador_Equipos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnDesbloqueos;
    private javax.swing.JButton btnBloqueos;
    private javax.swing.JButton btnRegresar;
    private javax.swing.JButton btnbloquearequipo;
    private javax.swing.JButton btnderecha;
    private javax.swing.JButton btndesbloquearequipo;
    private javax.swing.JButton btnfltrar;
    private javax.swing.JButton btnizquierda;
    private javax.swing.JButton btnrestaurartabla;
    private javax.swing.JComboBox<String> cbxestados;
    private javax.swing.JPanel contenedorcocupacion;
    private javax.swing.JPanel contenedordeopcioneslaterales;
    private javax.swing.JPanel contenedorenuso;
    private javax.swing.JPanel contenedorequipostotales;
    private javax.swing.JPanel contenedorsuperior;
    private javax.swing.JLabel jLabel8lblnumeroequipostotales;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblContraseñatitulo;
    private javax.swing.JLabel lblEquipos;
    private javax.swing.JLabel lbladmin;
    private javax.swing.JLabel lblbuscarequipo;
    private javax.swing.JLabel lblcontadordepaginas;
    private javax.swing.JLabel lblenuso;
    private javax.swing.JLabel lblequipostotales;
    private javax.swing.JLabel lblestado;
    private javax.swing.JLabel lbllaboratorionombre;
    private javax.swing.JLabel lblnumeroenuso;
    private javax.swing.JLabel lblocupacionactual;
    private javax.swing.JLabel lblocupacionporcentage;
    private javax.swing.JLabel lbltitulo;
    private javax.swing.JLabel lbltxtclabedeacceso;
    private javax.swing.JLabel lblusuario;
    private javax.swing.JPanel panelbarraequipos_bloqueos;
    private javax.swing.JPanel panelcontenido;
    private javax.swing.JPanel panelcontraseñamaestra;
    private javax.swing.JPanel panelfondo;
    private javax.swing.JTable tabladeequipos;
    private javax.swing.JTextField tctbuscarequipo;
    private javax.swing.JTextField txtcontraseña;
    // End of variables declaration//GEN-END:variables
}
