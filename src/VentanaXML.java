import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.File;

public class VentanaXML extends JFrame {

    // Panel que cambia de "pantalla"
    private CardLayout cardLayout;
    private JPanel panelContenido;
    // ====== LÓGICA DEL PROYECTO (sin librerías XML externas) ======
    private ArbolXML arbol = new ArbolXML();
    private boolean xmlValidoCargado = false;

    // Para mostrar estado en pantalla
    private JLabel lblEstadoXML;
    private JTextArea areaLogXML;

    public VentanaXML() {

        // ====== CONFIGURACIÓN BÁSICA DE LA VENTANA ======
        setTitle("🌳 XML Parser - Árbol Multihijos");
        setSize(1100, 650);
        setLocationRelativeTo(null); // Centrar
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Layout principal (izquierda menú / centro contenido)
        setLayout(new BorderLayout());

        // ====== MENÚ LATERAL ======
        JPanel menuLateral = crearMenuLateral();
        add(menuLateral, BorderLayout.WEST);

        // ====== CONTENIDO CENTRAL (CardLayout) ======
        panelContenido = new JPanel();
        cardLayout = new CardLayout();
        panelContenido.setLayout(cardLayout);

        // Agregar "pantallas"
        panelContenido.add(crearPanelInicio(), "INICIO");
        panelContenido.add(crearPanelCargarXML(), "CARGAR");
        panelContenido.add(crearPanelRecorridos(), "RECORRIDOS");
        panelContenido.add(crearPanelBusqueda(), "BUSQUEDA");
        panelContenido.add(crearPanelOrdenamiento(), "ORDENAMIENTO");
        panelContenido.add(crearPanelAgregarNodo(), "AGREGAR");


        add(panelContenido, BorderLayout.CENTER);

        // Mostrar inicio por defecto
        cardLayout.show(panelContenido, "INICIO");
    }

    // ==========================================================
    // MENÚ LATERAL
    // ==========================================================
    private JPanel crearMenuLateral() {
        JPanel menu = new JPanel();
        menu.setPreferredSize(new Dimension(240, 0));
        menu.setBackground(new Color(52, 73, 94)); // parecido al tuyo
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBorder(new EmptyBorder(20, 15, 20, 15));

        JLabel titulo = new JLabel("Menú");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        menu.add(titulo);

        menu.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton btnInicio = crearBotonMenu("🏠 Inicio");
        JButton btnCargar = crearBotonMenu("📄 Cargar XML");
        JButton btnRecorridos = crearBotonMenu("🔁 Recorridos");
        JButton btnBusqueda = crearBotonMenu("🔎 Búsqueda");
        JButton btnOrden = crearBotonMenu("📚 Ordenamiento");
        JButton btnAgregar = crearBotonMenu("➕ Agregar Nodo");

        // Solo cambian de vista (sin lógica)
        btnInicio.addActionListener(e -> cardLayout.show(panelContenido, "INICIO"));
        btnCargar.addActionListener(e -> cardLayout.show(panelContenido, "CARGAR"));
        btnRecorridos.addActionListener(e -> cardLayout.show(panelContenido, "RECORRIDOS"));
        btnBusqueda.addActionListener(e -> cardLayout.show(panelContenido, "BUSQUEDA"));
        btnOrden.addActionListener(e -> cardLayout.show(panelContenido, "ORDENAMIENTO"));
        btnAgregar.addActionListener(e -> cardLayout.show(panelContenido, "AGREGAR"));


        menu.add(btnInicio);
        menu.add(Box.createRigidArea(new Dimension(0, 10)));
        menu.add(btnCargar);
        menu.add(Box.createRigidArea(new Dimension(0, 10)));
        menu.add(btnRecorridos);
        menu.add(Box.createRigidArea(new Dimension(0, 10)));
        menu.add(btnBusqueda);
        menu.add(Box.createRigidArea(new Dimension(0, 10)));
        menu.add(btnOrden);
        menu.add(Box.createRigidArea(new Dimension(0, 10)));
        menu.add(btnAgregar);


        return menu;
    }

    private JButton crearBotonMenu(String texto) {
        JButton btn = new JButton(texto);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));

        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);

        btn.setBackground(new Color(52, 73, 94));
        btn.setForeground(Color.WHITE);

        // Hover simple
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(44, 62, 80));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(52, 73, 94));
            }
        });

        return btn;
    }

    // ==========================================================
    // PANTALLAS (SOLO VISUAL)
    // ==========================================================

    private JPanel crearPanelInicio() {
        JPanel p = crearPanelBase();

        JLabel titulo = new JLabel("Bienvenido al XML Parser");
        titulo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 22));
        titulo.setForeground(new Color(44, 62, 80));

        JLabel subtitulo = new JLabel("Carga un archivo XML para construir el Árbol multihijos y usar las funciones.");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitulo.setForeground(new Color(120, 120, 120));

        p.add(Box.createVerticalGlue());
        p.add(titulo);
        p.add(Box.createRigidArea(new Dimension(0, 10)));
        p.add(subtitulo);
        p.add(Box.createVerticalGlue());

        return p;
    }

    private JPanel crearPanelCargarXML() {

        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(30, 30, 30, 30));

        // -------- PARTE SUPERIOR --------
        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Cargar archivo XML");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setForeground(new Color(44, 62, 80));

        JButton btnSeleccionar = new JButton("Seleccionar XML");
        btnSeleccionar.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        lblEstadoXML = new JLabel("Estado: No hay XML cargado");
        lblEstadoXML.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblEstadoXML.setForeground(Color.RED);

        btnSeleccionar.addActionListener(e -> seleccionarYCargarXML());

        top.add(titulo);
        top.add(Box.createRigidArea(new Dimension(0, 10)));
        top.add(btnSeleccionar);
        top.add(Box.createRigidArea(new Dimension(0, 10)));
        top.add(lblEstadoXML);

        // -------- RESULTADO --------
        areaLogXML = new JTextArea();
        areaLogXML.setFont(new Font("Consolas", Font.PLAIN, 14));
        areaLogXML.setEditable(false);

        JScrollPane scroll = new JScrollPane(areaLogXML);
        scroll.setPreferredSize(new Dimension(800, 400));

        // -------- ARMAR PANEL --------
        p.add(top, BorderLayout.NORTH);
        p.add(scroll, BorderLayout.CENTER);

        return p;
    }



    private JPanel crearPanelRecorridos() {

        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(30, 30, 30, 30));

        // -------- PARTE SUPERIOR --------
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setOpaque(false);

        JLabel titulo = new JLabel("Recorridos del Árbol");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setForeground(new Color(44, 62, 80));

        JButton btnPre = new JButton("Preorden");
        JButton btnPost = new JButton("Postorden");

        top.add(titulo);
        top.add(btnPre);
        top.add(btnPost);

        // -------- SALIDA --------
        JTextPane salida = new JTextPane();
        salida.setEditable(false);
        salida.setFont(new Font("Consolas", Font.PLAIN, 14));

        JScrollPane scroll = new JScrollPane(salida);
        scroll.setPreferredSize(new Dimension(800, 420));

        btnPre.addActionListener(e -> {
            if (!xmlValidoCargado) {
                salida.setText("⚠️ No hay un XML válido cargado.");
                return;
            }
            salida.setText("");
            arbol.imprimirPreordenVisual(arbol.getRaiz(), "", true, salida);
        });

        btnPost.addActionListener(e -> {
            if (!xmlValidoCargado) {
                salida.setText("⚠️ No hay un XML válido cargado.");
                return;
            }
            salida.setText("");
            arbol.imprimirPostordenVisual(arbol.getRaiz(), "", salida);
        });

        p.add(top, BorderLayout.NORTH);
        p.add(scroll, BorderLayout.CENTER);

        return p;
    }

    private JPanel crearPanelBusqueda() {

        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(30, 30, 30, 30));

        // ===== PARTE SUPERIOR =====
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setOpaque(false);

        JLabel titulo = new JLabel("Búsqueda por etiqueta");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setForeground(new Color(44, 62, 80));

        JTextField txtEtiqueta = new JTextField(20);
        txtEtiqueta.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        top.add(titulo);
        top.add(Box.createRigidArea(new Dimension(15, 0)));
        top.add(new JLabel("Etiqueta:"));
        top.add(txtEtiqueta);
        top.add(btnBuscar);

        // ===== ÁREA DE RESULTADOS =====
        JTextArea salida = new JTextArea();
        salida.setFont(new Font("Consolas", Font.PLAIN, 14));
        salida.setEditable(false);
        salida.setLineWrap(true);
        salida.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(salida);
        scroll.setPreferredSize(new Dimension(800, 420));

        // ===== LÓGICA EXISTENTE (NO TOCADA) =====
        btnBuscar.addActionListener(e -> {

            if (!xmlValidoCargado) {
                salida.setText("⚠️ No hay un XML válido cargado.");
                return;
            }

            String etiqueta = txtEtiqueta.getText().trim();

            if (etiqueta.isEmpty()) {
                salida.setText("⚠️ Ingresa una etiqueta para buscar.");
                return;
            }

            java.util.List<String> valores = arbol.buscarValoresPorEtiqueta(etiqueta);

            if (valores.isEmpty()) {
                salida.setText("No se encontraron valores para la etiqueta <" + etiqueta + ">.");
                return;
            }

            StringBuilder sb = new StringBuilder();
            sb.append("Valores encontrados (orden natural del XML):\n\n");

            for (String v : valores) {
                sb.append("- ").append(v).append("\n");
            }

            salida.setText(sb.toString());
        });

        // ===== ARMAR PANEL =====
        p.add(top, BorderLayout.NORTH);
        p.add(scroll, BorderLayout.CENTER);

        return p;
    }
    private JPanel crearPanelOrdenamiento() {

        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(30, 30, 30, 30));

        // ===== PARTE SUPERIOR =====
        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));

        // ---- TÍTULO ----
        JLabel titulo = new JLabel("Ordenamiento (Heap)");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setForeground(new Color(44, 62, 80));
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ---- FILA DE CONTROLES ----
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fila.setOpaque(false);

        JLabel lblEtiqueta = new JLabel("Etiqueta:");
        JTextField txtEtiqueta = new JTextField(20);
        txtEtiqueta.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JButton btnOrdenar = new JButton("Ordenar valores");
        btnOrdenar.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        fila.add(lblEtiqueta);
        fila.add(txtEtiqueta);
        fila.add(btnOrdenar);

        top.add(titulo);
        top.add(Box.createRigidArea(new Dimension(0, 10)));
        top.add(fila);

        // ===== ÁREA DE RESULTADOS =====
        JTextArea salida = new JTextArea();
        salida.setFont(new Font("Consolas", Font.PLAIN, 14));
        salida.setEditable(false);
        salida.setLineWrap(true);
        salida.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(salida);
        scroll.setPreferredSize(new Dimension(800, 420));

        // ===== LÓGICA (NO TOCADA) =====
        btnOrdenar.addActionListener(e -> {

            if (!xmlValidoCargado) {
                salida.setText("⚠️ No hay un XML válido cargado.");
                return;
            }

            String etiqueta = txtEtiqueta.getText().trim();

            if (etiqueta.isEmpty()) {
                salida.setText("⚠️ Ingresa una etiqueta para ordenar.");
                return;
            }

            java.util.List<String> ordenados =
                    arbol.buscarValoresOrdenadosPorEtiqueta(etiqueta);

            if (ordenados.isEmpty()) {
                salida.setText("No se encontraron valores para la etiqueta <" + etiqueta + ">.");
                return;
            }

            StringBuilder sb = new StringBuilder();
            sb.append("Valores ordenados alfabéticamente (Heap):\n\n");

            for (String v : ordenados) {
                sb.append("- ").append(v).append("\n");
            }

            salida.setText(sb.toString());
        });

        // ===== ARMAR PANEL =====
        p.add(top, BorderLayout.NORTH);
        p.add(scroll, BorderLayout.CENTER);

        return p;
    }
    private JPanel crearPanelAgregarNodo() {

        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(30, 40, 30, 30));

        // ===== TÍTULO =====
        JLabel titulo = new JLabel("Agregar nuevo nodo al XML");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setForeground(new Color(44, 62, 80));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setOpaque(false);
        top.add(titulo);

        // ===== FORMULARIO (alineado a la izquierda) =====
        JPanel formWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        formWrapper.setOpaque(false);

        JPanel form = new JPanel(new GridLayout(3, 2, 15, 18));
        form.setOpaque(false);

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 16);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 15);

        JTextField txtPadre = new JTextField();
        JTextField txtEtiqueta = new JTextField();
        JTextField txtTexto = new JTextField();

        Dimension fieldSize = new Dimension(320, 32);
        txtPadre.setPreferredSize(fieldSize);
        txtEtiqueta.setPreferredSize(fieldSize);
        txtTexto.setPreferredSize(fieldSize);

        txtPadre.setFont(fieldFont);
        txtEtiqueta.setFont(fieldFont);
        txtTexto.setFont(fieldFont);

        JLabel lblPadre = new JLabel("Etiqueta del padre:");
        JLabel lblEtiqueta = new JLabel("Nueva etiqueta:");
        JLabel lblTexto = new JLabel("Texto del nodo:");

        lblPadre.setFont(labelFont);
        lblEtiqueta.setFont(labelFont);
        lblTexto.setFont(labelFont);

        form.add(lblPadre);
        form.add(txtPadre);
        form.add(lblEtiqueta);
        form.add(txtEtiqueta);
        form.add(lblTexto);
        form.add(txtTexto);

        formWrapper.add(form);

        // ===== BOTONES =====
        JButton btnAgregar = new JButton("Agregar nodo");
        JButton btnGuardar = new JButton("Guardar XML");

        btnAgregar.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btnGuardar.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        botones.setOpaque(false);
        botones.add(btnAgregar);
        botones.add(btnGuardar);

        // ===== SALIDA =====
        JTextArea salida = new JTextArea(6, 60);
        salida.setEditable(false);
        salida.setFont(new Font("Consolas", Font.PLAIN, 14));

        JScrollPane scroll = new JScrollPane(salida);

        JPanel bottom = new JPanel(new BorderLayout(10, 10));
        bottom.setOpaque(false);
        bottom.add(botones, BorderLayout.NORTH);
        bottom.add(scroll, BorderLayout.CENTER);

        // ===== LÓGICA (NO SE TOCA) =====
        btnAgregar.addActionListener(e -> {

            if (!xmlValidoCargado) {
                salida.setText("⚠️ Primero carga un XML válido.");
                return;
            }

            String padre = txtPadre.getText().trim();
            String etiqueta = txtEtiqueta.getText().trim();
            String texto = txtTexto.getText().trim();

            if (padre.isEmpty() || etiqueta.isEmpty()) {
                salida.setText("⚠️ Completa al menos el padre y la etiqueta.");
                return;
            }

            boolean ok = arbol.agregarNodo(padre, etiqueta, texto);

            if (ok) {
                salida.setText(
                        "✅ Nodo agregado correctamente\n\n" +
                                "Padre: " + padre +
                                "\nEtiqueta: " + etiqueta +
                                "\nTexto: " + texto
                );
            } else {
                salida.setText(
                        "❌ No se encontró un nodo padre con etiqueta <" + padre + ">"
                );
            }
        });

        btnGuardar.addActionListener(e -> {

            if (!xmlValidoCargado) {
                JOptionPane.showMessageDialog(
                        this,
                        "No hay un XML cargado para guardar.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Guardar XML");

            int result = chooser.showSaveDialog(this);
            if (result != JFileChooser.APPROVE_OPTION) return;

            File archivo = chooser.getSelectedFile();

            if (!archivo.getName().toLowerCase().endsWith(".xml")) {
                archivo = new File(archivo.getAbsolutePath() + ".xml");
            }

            boolean ok = arbol.guardarXML(archivo.getAbsolutePath());

            JOptionPane.showMessageDialog(
                    this,
                    ok ? "XML guardado correctamente" : "Error al guardar el XML",
                    ok ? "Éxito" : "Error",
                    ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE
            );
        });

        // ===== ARMAR PANEL =====
        p.add(top, BorderLayout.NORTH);
        p.add(formWrapper, BorderLayout.CENTER);
        p.add(bottom, BorderLayout.SOUTH);

        return p;
    }



    // Panel base con fondo y padding
    private JPanel crearPanelBase() {
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(30, 30, 30, 30));
        return p;
    }


    private void seleccionarYCargarXML() {

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Selecciona un archivo XML");

        // Abrir diálogo
        int result = chooser.showOpenDialog(this);

        if (result != JFileChooser.APPROVE_OPTION) {
            return; // usuario canceló
        }

        File archivo = chooser.getSelectedFile();

        // Validación simple: extensión .xml (no es XML parser, solo filtro)
        String nombre = archivo.getName().toLowerCase();
        if (!nombre.endsWith(".xml")) {
            xmlValidoCargado = false;
            lblEstadoXML.setText("Estado: Archivo NO es .xml");
            lblEstadoXML.setForeground(new Color(200, 0, 0));
            areaLogXML.setText("ERROR: Selecciona un archivo con extensión .xml\n");
            return;
        }

        // Limpiar estado anterior
        xmlValidoCargado = false;
        areaLogXML.setText("Archivo seleccionado: " + archivo.getAbsolutePath() + "\n\n");

        // Cargar usando TU código (sin librerías XML externas)
        try {
            arbol = new ArbolXML(); // árbol limpio
            boolean ok = arbol.cargarXML(archivo.getAbsolutePath());

            xmlValidoCargado = ok;

            if (ok) {
                lblEstadoXML.setText("Estado: XML cargado ✅");
                lblEstadoXML.setForeground(new Color(0, 140, 0));
                areaLogXML.append("[OK] Árbol construido correctamente.\n");
            } else {
                lblEstadoXML.setText("Estado: XML inválido ❌");
                lblEstadoXML.setForeground(new Color(200, 0, 0));
                areaLogXML.append("ERROR: XML mal formado. Revisa etiquetas.\n");
            }


        } catch (Exception ex) {
            xmlValidoCargado = false;
            lblEstadoXML.setText("Estado: Error cargando XML ❌");
            lblEstadoXML.setForeground(new Color(200, 0, 0));
            areaLogXML.append("ERROR: " + ex.getMessage() + "\n");
        }
    }




}
