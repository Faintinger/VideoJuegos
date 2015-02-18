/**
 * Juego Examen
 *
 *
 * @author Omar García
 * @version 1
 * @date 11/Feb/2015
 */
 
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.LinkedList;

/**
 *
 * @author Omar García
 */
public class Juego extends Applet implements Runnable, KeyListener {

    private final int iMAXANCHO = 10; // maximo numero de personajes por ancho
    private final int iMAXALTO = 8;  // maxuimo numero de personajes por alto
    private Base basPrincipal;         // Objeto principal
    //private Base basMalo;         // Objeto malo
    private Base basFantasmita;     // Objeto fantasmita
    private Base basOver;           //Objeto Game Over
    private LinkedList<Base> lklFantasma; //Guarda a todos los fantasmas
    private LinkedList<Base> lklMalos; //Guarda a todos los Juanitos
    
    private int iPuntos;            //Variable que contiene el puntaje
    private int iVidas;             //Variable que contiene la cantidad de vidas
    private int iDireccion;         //direccion del movimiento del jugador
    private int iVelfantasma;       //velocidad con la que avanza el fantasma
    private int iVeljuanito;        //velocidad con la que avanza el fantasma
    private int iVelPlayer;         //velocidad con la que avanza el jugador
    private int iColisiones;        //Cantidad de colisiones con juanito
    private boolean bPausa;         //Guarda si esta en pausa o no
    
    /* objetos para manejar el buffer del Applet y este no parpadee */
    private Image    imaImagenApplet;   // Imagen a proyectar en Applet	
    private Graphics graGraficaApplet;  // Objeto grafico de la Imagen
    private AudioClip adcSonidoChimpy;   // Objeto sonido de Chimpy
    private AudioClip adcSonidoChimpJuan; //Objeto Sonido de colision Chimp-juan
    
    
	
    /** 
     * init
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>.<P>
     * En este metodo se inizializan las variables o se crean los objetos
     * a usarse en el <code>Applet</code> y se definen funcionalidades.
     * 
     */
    public void init() {
        // hago el applet de un tamaño 500,500
        setSize(800,500);
        
        //Se inicia la cantidad de vidas en 3 a 5
        iVidas = (int)(Math.random() * 2) + 3;
        
        //Se inicializa la direccion sin movimiento
        iDireccion = 0;
        
        //Inicio mi lista de fantasmas
        lklFantasma = new LinkedList <Base>();
        
        //Inicio la lista de Malos
        lklMalos = new  LinkedList <Base>();
        
        //Inicio la velocidad del fantasma
        iVelfantasma = 1;
        
        //Inicio la velocidad del Malo
        iVeljuanito = 1;
                
        //Inicio la velocidad del jugador en 2;
        iVelPlayer = 4;
        
        //Inicio cantidad de colisiones en 0
        iColisiones = 0;
        
        //Inicio pausa en falso
        bPausa = false;
        
        // Selecciono imagen del Pricipal    
	URL urlImagenPrincipal = this.getClass().getResource("chimpy.gif");
                
        // se crea el objeto para principal 
	basPrincipal = new Base(0, 0, getWidth() / iMAXANCHO,
                getHeight() / iMAXALTO,
                Toolkit.getDefaultToolkit().getImage(urlImagenPrincipal));

        // se posiciona a principal  en la esquina superior izquierda del Applet 
        basPrincipal.setX(getWidth() / 2);
        basPrincipal.setY(getHeight() / 2);
        
        // defino la imagen del malo
	URL urlImagenMalo = this.getClass().getResource("juanito.gif");
        
        // se crea el objeto para malo 
        int iPosX = (iMAXANCHO - 1) * getWidth() / iMAXANCHO;
        int iPosY = (iMAXALTO - 1) * getHeight() / iMAXALTO;        
	/*basMalo = new Base(iPosX,iPosY, getWidth() / iMAXANCHO,
                getHeight() / iMAXALTO,
                Toolkit.getDefaultToolkit().getImage(urlImagenMalo));*/
        
        // Selecciono imagen del fantasmita  
	URL urlImagenFantasmita = this.getClass().getResource("fantasmita.gif");
        
        // se crea el objeto para fantasmita 
        int iRandom = (int)(Math.random() * 2) + 8;
        //genero varios malos
        for(int i = 0; i < iRandom; i++){
            iPosX = (int)(Math.random() * -(getWidth()));    
            iPosY = (int) (Math.random() *(getHeight()));
            basFantasmita = new Base(iPosX,iPosY,getWidth() / iMAXANCHO,
                getHeight() / iMAXALTO,
                Toolkit.getDefaultToolkit().getImage(urlImagenFantasmita));
            lklFantasma.add(basFantasmita);
        }
        
        // se crea el objeto para Juanito
        iRandom = (int)(Math.random() * 5) + 10;
        Base basMalo;
        //genero varios malos
        for(int i = 0; i < iRandom; i++){
            iPosX = (int)(Math.random() * (getWidth()));    
            iPosY = (int) (Math.random() * -(getHeight()));
            basMalo = new Base(iPosX,iPosY,getWidth() / iMAXANCHO,
                getHeight() / iMAXALTO,
                Toolkit.getDefaultToolkit().getImage(urlImagenMalo));
            lklMalos.add(basMalo);
        }
        
        //Genero la imagen de Game Over
        URL urlImagenOver = this.getClass().getResource("gameover.jpg");
        basOver = new Base ((getWidth()/2 - 400),(getHeight()/2 - 250),800,500,
        Toolkit.getDefaultToolkit().getImage(urlImagenOver));
        
        URL urlSonidoChimpJuan = this.getClass().getResource("monkey1.wav");
        URL urlSonidoChimpy = this.getClass().getResource("monkey2.wav");
        adcSonidoChimpJuan = getAudioClip(urlSonidoChimpJuan);
        adcSonidoChimpy = getAudioClip (urlSonidoChimpy);
        //adcSonidoChimpy.play();
        //Color de fondo negro
        setBackground (Color.black);
        //Agrego el teclaso
        addKeyListener(this); //Agrego que escuche al teclado
    }
	
    /** 
     * start
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>.<P>
     * En este metodo se crea e inicializa el hilo
     * para la animacion este metodo es llamado despues del init o 
     * cuando el usuario visita otra pagina y luego regresa a la pagina
     * en donde esta este <code>Applet</code>
     * 
     */
    public void start () {
        // Declaras un hilo
        Thread th = new Thread (this);
        // Empieza el hilo
        th.start ();
    }
	
    /** 
     * run
     * 
     * Metodo sobrescrito de la clase <code>Thread</code>.<P>
     * En este metodo se ejecuta el hilo, que contendrá las instrucciones
     * de nuestro juego.
     * 
     */
    public void run () {
        /* mientras dure el juego, se actualizan posiciones de jugadores
           se checa si hubo colisiones para desaparecer jugadores o corregir
           movimientos y se vuelve a pintar todo
        */ 
        while (iVidas > 0) {
            if(!bPausa){
                actualiza();
                checaColision();
            }
            repaint();
            try	{
                // El thread se duerme.
                Thread.sleep (20);
            }
            catch (InterruptedException iexError) {
                System.out.println("Hubo un error en el juego " + 
                        iexError.toString());
            }
	}
    }
	
    /** 
     * actualiza
     * 
     * Metodo que actualiza la posicion de los objetos 
     * 
     */
    public void actualiza(){
        switch(iDireccion) {
            case 1: { //se mueve hacia arriba
                basPrincipal.setY(basPrincipal.getY() - iVelPlayer);
                break;    
            }
            case 2: { //se mueve hacia abajo
                basPrincipal.setY(basPrincipal.getY() + iVelPlayer);
                break;    
            }
            case 3: { //se mueve hacia izquierda
                basPrincipal.setX(basPrincipal.getX() - iVelPlayer);
                break;    
            }
            case 4: { //se mueve hacia derecha
                basPrincipal.setX(basPrincipal.getX() + iVelPlayer);
                break;    	
            }
        }
        if(iColisiones > 5){
            iVidas--;
            iColisiones = 0;
            iVeljuanito++;
        }
        for(Base basFantasmita : lklFantasma){
            iVelfantasma = (int)(Math.random() * 2) + 3;
            basFantasmita.setX(basFantasmita.getX() + iVelfantasma);
        }
        for(Base basMalo : lklMalos){
            basMalo.setY(basMalo.getY() + iVeljuanito);
        }
    }
	
    /**
     * checaColision
     * 
     * Metodo usado para checar la colision entre objetos
     * 
     */
    public void checaColision(){
        //Checa si se el jugador sale del Applet
        if(basPrincipal.getY() < 0) { // y esta pasando el limite
            basPrincipal.setY(0);
        }
        if(basPrincipal.getY() + basPrincipal.getAlto() > getHeight()) {
            basPrincipal.setY(getHeight()- basPrincipal.getAlto());
        }
        if(basPrincipal.getX() < 0) { // y se sale del applet
            basPrincipal.setX(0);
        }
        if(basPrincipal.getX() + basPrincipal.getAncho() > getWidth()) { 
            basPrincipal.setX(getWidth()- basPrincipal.getAncho());
        }
        for(Base basFantasmita : lklFantasma){
            //Checa si se el fantasma sale del Applet
            if(basFantasmita.getY() < 0) { // y esta pasando el limite
                basFantasmita.setX((int)(Math.random() * -(getWidth())));;
                basFantasmita.setY((int) (Math.random() *(getHeight())));
            }
            if(basFantasmita.getY() + basFantasmita.getAlto() > getHeight()) {
                basFantasmita.setX((int)(Math.random() * -(getWidth())));;
                basFantasmita.setY((int) (Math.random() *(getHeight())));
            }
            if(basFantasmita.getX() + basFantasmita.getAncho() > getWidth()) { 
                basFantasmita.setX((int)(Math.random() * -(getWidth())));
                basFantasmita.setY((int) (Math.random() *(getHeight())));
            } 
            if (basPrincipal.intersecta(basFantasmita)) {
                //if(basPrincipal.getX() - basFantasmita.getX() > 70){
                    basFantasmita.setX((int)(Math.random() * -(getWidth())));;
                    basFantasmita.setY((int) (Math.random() *(getHeight())));
                    iPuntos++; 
                    adcSonidoChimpy.play();
                //}
            }
        }
        //Checo la colision entre chimpy y Juanito
        for(Base basMalo : lklMalos){
            //Checa si se el fantasma sale del Applet
            if(basMalo.getX() < 0) { // y esta pasando el limite
                basMalo.setX((int)(Math.random() * (getWidth())));
                basMalo.setY((int) (Math.random() * -(getHeight())));
            }
            if(basMalo.getY() + basMalo.getAlto() > getHeight()) {
                basMalo.setX((int)(Math.random() * (getWidth())));
                basMalo.setY((int) (Math.random() * -(getHeight())));
            }
            if(basMalo.getX() + basMalo.getAncho() > getWidth()) { 
                basMalo.setX((int)(Math.random() * (getWidth())));
                basMalo.setY((int) (Math.random() * -(getHeight())));
            } 
            if (basPrincipal.intersecta(basMalo)) {
                //if(basPrincipal.getY() - basMalo.getY() > 30){
                    basMalo.setX((int)(Math.random() * (getWidth())));
                    basMalo.setY((int) (Math.random() * -(getHeight())));
                    iColisiones++; 
                    adcSonidoChimpJuan.play();
                //}
            }  
        }
    }
	
    /**
     * update
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>,
     * heredado de la clase Container.<P>
     * En este metodo lo que hace es actualizar el contenedor y 
     * define cuando usar ahora el paint
     * 
     * @param graGrafico es el <code>objeto grafico</code> usado para dibujar.
     * 
     */
    public void update (Graphics graGrafico){
        // Inicializan el DoubleBuffer
        if (imaImagenApplet == null){
                imaImagenApplet = createImage (this.getSize().width, 
                        this.getSize().height);
                graGraficaApplet = imaImagenApplet.getGraphics ();
        }

        // Actualiza la imagen de fondo.
        URL urlImagenFondo = this.getClass().getResource("Ciudad.png");
        Image imaImagenFondo = Toolkit.getDefaultToolkit().getImage(urlImagenFondo);
         graGraficaApplet.drawImage(imaImagenFondo, 0, 0, getWidth(), getHeight(), this);

        // Actualiza el Foreground.
        graGraficaApplet.setColor (getForeground());
        paint(graGraficaApplet);

        // Dibuja la imagen actualizada
        graGrafico.drawImage (imaImagenApplet, 0, 0, this);
    }
    
    /**
     * paint
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>,
     * heredado de la clase Container.<P>
     * En este metodo se dibuja la imagen con la posicion actualizada,
     * ademas que cuando la imagen es cargada te despliega una advertencia.
     * 
     * @param graDibujo es el objeto de <code>Graphics</code> usado para dibujar.
     * 
     */
    public void paint(Graphics graDibujo) {
        // si la imagen ya se cargo
        if (basPrincipal != null && lklFantasma != null && lklMalos != null && basOver != null) {
            if(iVidas > 0){
                //Dibuja la imagen de susanita en el Applet
                basPrincipal.paint(graDibujo, this);
                //Dibuja la imagen de fantasmita en el Applet
                for(Base basFantasmita : lklFantasma){
                   basFantasmita.paint(graDibujo, this); 
                }
                //Dibujo la imagen de Juanito en el Applet
                for(Base basMalo : lklMalos){
                   basMalo.paint(graDibujo, this); 
                }
                graDibujo.setColor(Color.red);
                graDibujo.drawString("Puntaje: " + iPuntos, 650, 20);
                graDibujo.drawString("Vidas: " + iVidas, 10, 20);
            }
            else{
                //Dibuja la imagen de Game Over en el Applet
                basOver.paint(graDibujo, this);
                graDibujo.setColor(Color.red);
                graDibujo.drawString("Puntaje: " + iPuntos, 650, 20);
                //graDibujo.drawString("Vidas: " + iVidas, 10, 20);
            }
                //graDibujo.drawString(" Susanita = Fantasmita-> " + basSusanita.equals(basFantasmita), 400, 150);
                //graDibujo.drawString(" Susanita ->" + basSusanita.toString(), 400, 250);
                //graDibujo.drawString(" Fantasmita ->" + basFantasmita.toString(), 400, 350);        
        } // sino se ha cargado se dibuja un mensaje 
        else {
                //Da un mensaje mientras se carga el dibujo	
                graDibujo.drawString("No se cargo la imagen..", 20, 20);
        }
    }

    @Override
    public void keyTyped(KeyEvent ke) {
        
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        // si presiono flecha para arriba
            if(keyEvent.getKeyCode() == KeyEvent.VK_W) {    
                iDireccion = 1;  // cambio la dirección arriba
            }
            // si presiono flecha para abajo
            else if(keyEvent.getKeyCode() == KeyEvent.VK_S) {  
                iDireccion = 2;   // cambio la direccion para abajo
            }
            // si presiono flecha a la izquierda
            else if(keyEvent.getKeyCode() == KeyEvent.VK_A) {   
                iDireccion = 3;   // cambio la direccion a la izquierda
            }
            // si presiono flecha a la derecha
            else if(keyEvent.getKeyCode() == KeyEvent.VK_D){  
                iDireccion = 4; //Cambio la direccion a la derecha
            }
            // si preciona la tecla P pausa
            else if(keyEvent.getKeyCode() == KeyEvent.VK_P){
                if(bPausa)
                    bPausa = false;
                else
                    bPausa = true;
            }
            //Si presiona ESC acaba el juego
            else if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE){
                iVidas = 0;
            }
    }
}