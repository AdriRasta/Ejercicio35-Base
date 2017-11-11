import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class VentanaPrincipal {

	VentanaPrincipal ventanaInterna = this;

	// La ventana principal, en este caso, guarda todos los componentes:
	JFrame ventana;
	JPanel panelImagen;
	JPanel panelEmpezar;
	JPanel panelPuntuacion;
	JPanel panelJuego;

	// Todos los botones se meten en un panel independiente.
	// Hacemos esto para que podamos cambiar despu√©s los componentes por otros
	JPanel[][] panelesJuego;
	JButton[][] botonesJuego;

	// Label que contendr· la imagen del juego
	JLabel labelImagen;

	// Correspondencia de colores para las minas:
	Color correspondenciaColores[] = { Color.BLACK, Color.CYAN, Color.GREEN, Color.ORANGE, Color.RED, Color.RED,
			Color.RED, Color.RED, Color.RED, Color.RED };

	JButton botonEmpezar;
	JTextField pantallaPuntuacion;

	// Stream de lectura de la imagen
	BufferedImage bufferedImage;

	// LA VENTANA GUARDA UN CONTROL DE JUEGO:
	ControlJuego juego;

	// Constructor, marca el tama√±o y el cierre del frame
	public VentanaPrincipal() {
		ventana = new JFrame();
		ventana.setBounds(100, 100, 700, 500);
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		juego = new ControlJuego();
	}

	// Inicializa todos los componentes del frame
	public void inicializarComponentes() {

		// Definimos el layout:
		ventana.setLayout(new GridBagLayout());

		// Inicializamos componentes
		panelImagen = new JPanel();
		panelEmpezar = new JPanel();
		panelEmpezar.setLayout(new GridLayout(1, 1));
		panelPuntuacion = new JPanel();
		panelPuntuacion.setLayout(new GridLayout(1, 1));
		panelJuego = new JPanel();
		panelJuego.setLayout(new GridLayout(10, 10));

		botonEmpezar = new JButton("Go!");
		pantallaPuntuacion = new JTextField("0");
		pantallaPuntuacion.setEditable(false);
		pantallaPuntuacion.setHorizontalAlignment(SwingConstants.CENTER);

		// Bordes y colores:
		panelImagen.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
		panelEmpezar.setBorder(BorderFactory.createTitledBorder("Empezar"));
		panelPuntuacion.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
		panelJuego.setBorder(BorderFactory.createTitledBorder("Juego"));

		// Colocamos los componentes:
		// colocar el la imagen en el panel
		panelImagen.setLayout(new GridLayout(1, 1));
		labelImagen = new JLabel();
		panelImagen.add(labelImagen);

		// AZUL
		GridBagConstraints settings = new GridBagConstraints();
		settings.gridx = 0;
		settings.gridy = 0;
		settings.weightx = 1;
		settings.weighty = 1;
		settings.fill = GridBagConstraints.BOTH;
		ventana.add(panelImagen, settings);

		// VERDE
		settings = new GridBagConstraints();
		settings.gridx = 1;
		settings.gridy = 0;
		settings.weightx = 1;
		settings.weighty = 1;
		settings.fill = GridBagConstraints.BOTH;
		ventana.add(panelEmpezar, settings);

		// AMARILLO
		settings = new GridBagConstraints();
		settings.gridx = 2;
		settings.gridy = 0;
		settings.weightx = 1;
		settings.weighty = 1;
		settings.fill = GridBagConstraints.BOTH;
		ventana.add(panelPuntuacion, settings);

		// ROJO
		settings = new GridBagConstraints();
		settings.gridx = 0;
		settings.gridy = 1;
		settings.weightx = 1;
		settings.weighty = 10;
		settings.gridwidth = 3;
		settings.fill = GridBagConstraints.BOTH;
		ventana.add(panelJuego, settings);

		// Paneles
		panelesJuego = new JPanel[10][10];
		for (int i = 0; i < panelesJuego.length; i++) {
			for (int j = 0; j < panelesJuego[i].length; j++) {
				panelesJuego[i][j] = new JPanel();
				panelesJuego[i][j].setLayout(new GridLayout(1, 1));
				panelJuego.add(panelesJuego[i][j]);
			}
		}

		// Botones
		botonesJuego = new JButton[10][10];
		for (int i = 0; i < botonesJuego.length; i++) {
			for (int j = 0; j < botonesJuego[i].length; j++) {
				botonesJuego[i][j] = new JButton("-");
				panelesJuego[i][j].add(botonesJuego[i][j]);
			}
		}

		// Bot√≥nEmpezar:
		panelEmpezar.add(botonEmpezar);
		panelPuntuacion.add(pantallaPuntuacion);

	}

	/**
	 * M√©todo que inicializa todos los listeners que necesita inicialmente el
	 * programa
	 */
	public void inicializarListeners() {

		// Inicializar la escucha de los botones
		for (int i = 0; i < botonesJuego.length; i++) {
			for (int j = 0; j < botonesJuego[i].length; j++) {
				botonesJuego[i][j].addActionListener(new ActionBoton(this, i, j));
				refrescarPantalla();
			}
		}

		// Comenzar de nuevo la partida
		botonEmpezar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// Inicializar de nuevo el control del juego, a su vez se inicializa de nuevo la
				// partida en el constructor de ControlJuego
				juego = new ControlJuego();

				// Establecer la puntuaciÛn de nuevo a cero
				actualizarPuntuacion();

				// Borrar Los botones
				for (int i = 0; i < botonesJuego.length; i++) {
					for (int j = 0; j < botonesJuego[i].length; j++) {
						panelesJuego[i][j].removeAll();
					}
				}

				// Inicializar de nuevo los botones
				for (int i = 0; i < botonesJuego.length; i++) {
					for (int j = 0; j < botonesJuego[i].length; j++) {
						botonesJuego[i][j] = new JButton("-");
						panelesJuego[i][j].add(botonesJuego[i][j]);
					}
				}

				// Inicializar de nuevo los listeners de los botones
				for (int i = 0; i < botonesJuego.length; i++) {
					for (int j = 0; j < botonesJuego[i].length; j++) {
						botonesJuego[i][j].addActionListener(new ActionBoton(ventanaInterna, i, j));
						refrescarPantalla();
					}
				}

				// Refresco de la pantalla
				refrescarPantalla();
			}
		});

		// Redimensionado de la imagen
		/*
		 * panelImagen.addComponentListener(new ComponentAdapter() {
		 * 
		 * @Override public void componentResized(ComponentEvent e) { ImageIcon icon =
		 * new ImageIcon(bufferedImage.getScaledInstance(labelImagen.getWidth(),
		 * labelImagen.getHeight(), Image.SCALE_SMOOTH)); labelImagen.setIcon(icon); }
		 * });
		 */

	}

	/**
	 * MÈtodo que pinta en la pantalla el n˙mero de minas que hay alrededor de la
	 * celda Saca el botÛn que haya en la celda determinada y aÒade un JLabel
	 * centrado y no editable con el n˙mero de minas alrededor. Se pinta el color
	 * del texto seg˙n la siguiente correspondecia (consultar la variable
	 * correspondeciaColor): - 0 : negro - 1 : cyan - 2 : verde - 3 : naranja - 4 √≥
	 * m·s : rojo
	 * 
	 * @param i:
	 *            posici√≥n vertical de la celda.
	 * @param j:
	 *            posici√≥n horizontal de la celda.
	 */
	public void mostrarNumMinasAlrededor(int i, int j) {
		panelesJuego[i][j].removeAll();

		int numMinas = juego.getMinasAlrededor(i, j);

		JLabel label = new JLabel(Integer.toString(numMinas));
		label.setForeground(correspondenciaColores[numMinas]);
		label.setHorizontalAlignment(SwingConstants.CENTER);

		panelesJuego[i][j].add(label);
	}

	/**
	 * M√©todo que muestra una ventana que muestra el fin del juego
	 * 
	 * @param porExplosion
	 *            : Un booleano que indica si es final del juego porque ha explotado
	 *            una mina (true) o bien porque hemos desactivado todas (false)
	 * @post : Todos los botones se desactivan excepto el de volver a iniciar el
	 *       juego.
	 */
	public void mostrarFinJuego(boolean porExplosion) {
		if (porExplosion) {
			for (int i = 0; i < botonesJuego.length; i++) {
				for (int j = 0; j < botonesJuego.length; j++) {
					botonesJuego[i][j].setEnabled(false);
				}
			}

			JOptionPane.showMessageDialog(null, "Vaya... Ha explotado una una mina.");

		} else {
			JOptionPane.showMessageDialog(null, "°En hora buena, has descubierto donde se encuentran las minas!");
		}
	}

	/**
	 * M√©todo que muestra la puntuaci√≥n por pantalla.
	 */
	public void actualizarPuntuacion() {
		pantallaPuntuacion.setText(Integer.toString(juego.getPuntuacion()));
	}

	/**
	 * M√©todo para refrescar la pantalla
	 */
	public void refrescarPantalla() {
		ventana.revalidate();
		ventana.repaint();
	}

	/**
	 * M√©todo que devuelve el control del juego de una ventana
	 * 
	 * @return un ControlJuego con el control del juego de la ventana
	 */
	public ControlJuego getJuego() {
		return juego;
	}

	/**
	 * Insertar la imagen del juego
	 */
	public void insertarImagen() {

		bufferedImage = null;

		try {
			bufferedImage = ImageIO.read(new File("image/bombday.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		ImageIcon icon = new ImageIcon(
				bufferedImage.getScaledInstance(panelImagen.getWidth(), panelImagen.getHeight(), Image.SCALE_SMOOTH));
		labelImagen.setIcon(icon);
		labelImagen.setHorizontalAlignment(SwingConstants.CENTER);

	}

	/**
	 * M√©todo para inicializar el programa
	 */
	public void inicializar() {
		// IMPORTANTE, PRIMERO HACEMOS LA VENTANA VISIBLE Y LUEGO INICIALIZAMOS LOS
		// COMPONENTES.
		ventana.setVisible(true);
		inicializarComponentes();
		inicializarListeners();
		insertarImagen();
	}

}
