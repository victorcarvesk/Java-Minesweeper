import javax.swing.*;       // Elementos de interface
import java.awt.*;          // Cores dos elementos
import java.util.Random;    // Números aleatórios
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CampoMinadoGUI
{
    // Objetos de interface
    static JFrame gameFrame;
    static JPanel menuPanel, playPanel;
    static JLabel[][] botoes;
    static JLabel restartLabel, modeLabel, plusLabel, minusLabel, nivelLabel;
    static int[][] campo, flags;
    static int conversions, quantFlags, nivel=0;
    static String[] niveis = {"Fácil", "Intermediário", "Difícil"};

    static Timer cronometro;
    static boolean cronoUp = false;
    static int delay = 1000, seconds = 0;
    static JLabel cronoIcon, cronoView, mineIcon, minesView;

    // Variáveis de apoio
    static int leftClick = 1, rightClick = 3;
    static boolean mode = false;

    static Color panelColor = Color.darkGray;
    static Color buttonsColor = Color.gray;
    static Color clickedColor = Color.lightGray;
    static Color flagColor = new Color(243, 216, 175);
    static Color restartColor = Color.gray;
    static Color modeColor = Color.gray;

    static Color oneColor = new Color(70, 160, 70);
    static Color twoColor = new Color(255, 255, 0);
    static Color threeColor = new Color(255, 140, 0);
    static Color fourColor = new Color(223, 76, 42);
    static Color fiveColor = new Color(138, 74, 54);

    // bases
    static int[] bases = {8, 12, 16};
    static int[] btnSize = {78, 50, 38};
    static int[] quant_bombas = {10, 25, 40};
    static int iconSize = 80, space;
    
    // Cria icone do cronômetro
    static ImageIcon crono_raw = new ImageIcon("./img/crono.png");
    static Image crono_1 = crono_raw.getImage();
    static Image crono_2;
    static ImageIcon crono;

    // Cria icone da mina naval
    static ImageIcon mine_raw = new ImageIcon("./img/naval_mine.png");
    static Image mine_1 = mine_raw.getImage();
    static Image mine_2;
    static ImageIcon mine;

    // Cria icone da explosão
    static ImageIcon exp_raw = new ImageIcon("./img/explosion.png");
    static Image exp_1 = exp_raw.getImage();
    static Image exp_2;
    static ImageIcon exp;
    
    // Cria icone da bandeira
    static ImageIcon flag_raw = new ImageIcon("./img/flag.png");
    static Image flag_1 = flag_raw.getImage();
    static Image flag_2;
    static Image flagIcon_2;
    static ImageIcon flag;
    static ImageIcon flagIcon;

    // Cria icone do coronga
    static ImageIcon coronga_raw = new ImageIcon("./img/coronga.png");
    static Image coronga_1 = coronga_raw.getImage();
    static Image coronga_2;
    static ImageIcon coronga;
    

    // Cria icone do bio hazard
    static ImageIcon hazard_raw = new ImageIcon("./img/bio_hazard.png");
    static Image hazard_1 = hazard_raw.getImage();
    static Image hazard_2;
    static Image hazardIcon_2;
    static ImageIcon hazard;
    static ImageIcon hazardIcon;

    // Posição do primeiro label de botões 
    static int x_i=30, y_i=30;

    


    public static void main(String[] args) {

        createFrame();
    }

    public static void createFrame()
    {
        

        // Cria frame (janela) principal do jogo
        gameFrame = new JFrame();
        gameFrame.setSize(1120,740);
        gameFrame.setIconImage(mine_1);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Insere painéis
        createPanel();

        // Insere um nome na janela
        gameFrame.setTitle("Campo Minado - CIMATEC Edition");
        
        // Centraliza a janela no monitor
        gameFrame.setLocationRelativeTo(null);

        // Desabilita redimensionamento
        gameFrame.setResizable(false);
        
        // Exibe o frame na janela, deve vir após configuração dos objetos internos (buttons, labels)
        gameFrame.setVisible(true);
    }

    public static void createPanel()
    {
        int imageSize = btnSize[nivel] - 5;
        crono_2 = crono_1.getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
        crono = new ImageIcon(crono_2);

        mine_2 = mine_1.getScaledInstance(imageSize, imageSize, Image.SCALE_SMOOTH);
        mine = new ImageIcon(mine_2);

        exp_2 = exp_1.getScaledInstance(imageSize, imageSize, Image.SCALE_SMOOTH);
        exp = new ImageIcon(exp_2);

        flag_2 = flag_1.getScaledInstance(imageSize, imageSize, Image.SCALE_SMOOTH);
        flagIcon_2 = flag_1.getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
        flag = new ImageIcon(flag_2);
        flagIcon = new ImageIcon(flagIcon_2);

        coronga_2 = coronga_1.getScaledInstance(imageSize, imageSize, Image.SCALE_SMOOTH);
        coronga = new ImageIcon(coronga_2);

        hazard_2 = hazard_1.getScaledInstance(imageSize, imageSize, Image.SCALE_SMOOTH);
        hazardIcon_2 = hazard_1.getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
        hazard = new ImageIcon(hazard_2);
        hazardIcon = new ImageIcon(hazardIcon_2);

        // Cria panel (painel) principal do jogo
        playPanel = new JPanel();
        
        // Define posicionamento absoluto dos componentes
        playPanel.setLayout(null);
        
        // Define background do painel
        playPanel.setBackground(panelColor);

        // Criar e inserir componentes no painel
        createField();   // -> "botões" do campo
        createOptions(); // -> "botão" reiniciar
        
        // Insere painel playPanel no frame gameFrame
        gameFrame.add(playPanel);
    }


    public static void createField()
    {
        // Instanciação da matriz de botões e da matriz de dados
        space = btnSize[nivel] + 2;

        campo = new int[bases[nivel]][bases[nivel]];
        bombInsert();
        bombCounter();

        flags = new int[bases[nivel]][bases[nivel]];
        quantFlags=0;

        botoes = new JLabel[bases[nivel]][bases[nivel]];
        int x_pos=x_i, y_pos=y_i;

        // Altera a linha do label (dentro da matriz)
        for(int i=0; i<botoes.length; i++)
        {
            // Altera a coluna do label (dentro de cada linha)
            for(int j=0; j<botoes[i].length; j++)
            {
                // O método addMouseListener só aceita variáveis "finais"
                final int h = i, k = j;
                
                // Cria novo label na matriz
                botoes[i][j] = new JLabel(" ", JLabel.CENTER);
                
                // Posiciona o label na janela
                botoes[i][j].setBounds(x_pos, y_pos, btnSize[nivel], btnSize[nivel]);
                
                // Deixa label opaco para inserção de cor de fundo
                botoes[i][j].setOpaque(true);
                
                // Define cor de fundo do label (só funciona para labels opacos)
                botoes[i][j].setBackground(buttonsColor);
                
                // Adiciona os labels ao painel principal (home)
                playPanel.add(botoes[i][j]);
                
                // Indicador de posição para o próximo label
                x_pos+=space;
                
                // Configura ação quando houver um clique no label
                
                botoes[i][j].addMouseListener(new java.awt.event.MouseAdapter()
                {
                    public void mouseClicked(java.awt.event.MouseEvent evt)
                    {
                        if (cronoUp==false) {
                            cronoUp=true;
                            timerTask();                            
                        }
                        botoesClick(playPanel, botoes[h][k], evt, campo);  
                        
                    }
                });
            }
            // Retorna ao começo da linha
            x_pos=x_i;
            
            // Vai para a próxima linha (debaixo)
            y_pos+=space;
        }
    }

    public static  void createOptions()
    {
        // Label do cronômetro
        cronoIcon = new JLabel("", JLabel.CENTER);
        cronoIcon.setIcon(crono);
        cronoIcon.setBounds(700, y_i, 100, 100);
        cronoIcon.setOpaque(true);
        cronoIcon.setBackground(restartColor);  
        playPanel.add(cronoIcon);
        
        // Label do cronômetro
        cronoView = new JLabel("00:00", JLabel.CENTER);
        cronoView.setFont(new Font("Monospaced", Font.BOLD, 40));
        cronoView.setBounds(800, y_i, 200, 100);
        cronoView.setOpaque(true);
        cronoView.setBackground(restartColor);  
        playPanel.add(cronoView);

        // Label do icone do cronômetro
        mineIcon = new JLabel("", JLabel.CENTER);
        if (mode==false) {
            mineIcon.setIcon(flagIcon);
        } else {
            mineIcon.setIcon(hazardIcon);
        }
        mineIcon.setFont(new Font("Monospaced", Font.BOLD, 40));
        mineIcon.setBounds(700, y_i+110, 100, 100);
        mineIcon.setOpaque(true);
        mineIcon.setBackground(restartColor);
        playPanel.add(mineIcon);
        
        // Label das minas
        minesView = new JLabel("0/"+String.valueOf(quant_bombas[nivel]), JLabel.CENTER);
        minesView.setFont(new Font("Monospaced", Font.BOLD, 40));
        minesView.setBounds(800, y_i+110, 200, 100);
        minesView.setOpaque(true);
        minesView.setBackground(restartColor);
        playPanel.add(minesView);

        // Label de reinicio
        restartLabel = new JLabel("Reiniciar", JLabel.CENTER);
        restartLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        restartLabel.setBounds(700, y_i+220, 300, 100);
        restartLabel.setOpaque(true);
        restartLabel.setBackground(restartColor);   
        restartLabel.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                optionsClick(playPanel, evt, campo);
            }
        });

        playPanel.add(restartLabel);

        // Label de reinicio
        if (mode==false) {
            modeLabel = new JLabel("Go to Pandemic Edition", JLabel.CENTER);
        } else {
            modeLabel = new JLabel("Go to Classic Edition", JLabel.CENTER);
            
        }
        modeLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        modeLabel.setBounds(700, y_i+330, 300, 100);
        modeLabel.setOpaque(true);
        modeLabel.setBackground(modeColor);   
        modeLabel.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                changeMode(modeLabel);
            }
        });
        playPanel.add(modeLabel);

        // Label da dificuldade
        minusLabel = new JLabel(" < ", JLabel.CENTER);
        minusLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        minusLabel.setBounds(700, y_i+440, 50, 100);
        minusLabel.setOpaque(true);
        minusLabel.setBackground(restartColor);
        minusLabel.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                minusClick();
            }
        });
        playPanel.add(minusLabel);

        // Label da dificuldade
        nivelLabel = new JLabel(niveis[nivel], JLabel.CENTER);
        nivelLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        nivelLabel.setBounds(750, y_i+440, 200, 100);
        nivelLabel.setOpaque(true);
        nivelLabel.setBackground(restartColor);
        playPanel.add(nivelLabel);

        // Label da dificuldade
        plusLabel = new JLabel(" > ", JLabel.CENTER);
        plusLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        plusLabel.setBounds(950, y_i+440, 50, 100);
        plusLabel.setOpaque(true);
        plusLabel.setBackground(restartColor);
        plusLabel.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                plusClick();
            }
        });
        playPanel.add(plusLabel);
    }

    public static void minusClick()
    {
        // code
        if (nivel>0) {
            nivel--;
        } else {
            nivel = 2;
        }
        nivelLabel.setText(niveis[nivel]);
        newGame();
    }

    public static void plusClick()
    {
        // code
        if (nivel<2) {
            nivel++;
        } else {
            nivel = 0;
        }
        nivelLabel.setText(niveis[nivel]);
        newGame();
    }


    public static void timerTask()
    {
        System.out.println("entrei");
        ActionListener action = new ActionListener()
        {
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                cronoView.setText(String.format("%02d", seconds/60) + ":" + String.format("%02d", seconds%60));
                
                System.out.println(String.valueOf(seconds));
                seconds++;
                cronoView.revalidate();
            }
        }
        ;
        cronometro = new Timer(delay, action);
        cronometro.setInitialDelay(0);
        cronometro.start();
    }

    public static void bombInsert()
    {
        // Criando gerador de números aleatórios
        Random random = new Random();

        for(int i=0; i<quant_bombas[nivel]; i++)
        {
            // Gera i e j aleatórios
            int r1 = random.nextInt(bases[nivel]);
            int r2 = random.nextInt(bases[nivel]);
            
            // Insere bomba na coordenada gerada pelo random
            if(campo[r1][r2] == 9)
            {
                r1 = random.nextInt(bases[nivel]);
                r2 = random.nextInt(bases[nivel]);
                campo[r1][r2] = 9;
            } else {
                campo[r1][r2] = 9;
            }
        }
    }

    public static void bombCounter()
    {
        for(int i=0; i<campo.length; i++)
        {
            for(int j=0; j<campo[i].length; j++)
            {
                // 
                if(campo[i][j]==0)
                {
                    int conta_bombas = 0;
                    
                    for(int m=-1; m<2; m++)
                    {
                        for(int n=-1; n<2; n++)
                        {
                            int h = i+m;
                            int k = j+n;
                            
                            if(h>=0 && k>=0)
                            {
                                try
                                {
                                    if(campo[h][k] == 9)
                                    {
                                        conta_bombas++;
                                    }
                                }
                                catch(ArrayIndexOutOfBoundsException e){}
                            }
                        }
                    }
                    campo[i][j] = conta_bombas;
                }
            }
        }
    }

    // Varre a matriz campo e chama a submatriz 
    public static void fieldExtend()
    {
        conversions = 0;
        
        // Imprime a matriz no terminal
        for(int i=0; i<campo.length; i++)
        {
            for(int j=0; j<campo[i].length; j++)
            {
                // Matriz das bombas com valor 0
                localExtend(i, j);
            }
            //System.out.println(" ");
        }
    }

    // Analisa submatriz de cada elemento
    public static void localExtend(int i, int j)
    {
        if(botoes[i][j].getText().contentEquals("0"))
        {
            for(int m=-1; m<2; m++)
            {
                for(int n=-1; n<2; n++)
                {
                    int h = i+m;
                    int k = j+n;
                    
                    if(h>=0 && k>=0)
                    {
                        try
                        {
                            if(botoes[h][k].getText().contentEquals(" ") || botoes[h][k].getBackground()==Color.yellow)
                            {
                                if (botoes[h][k].getBackground()==Color.yellow) {
                                    quantFlags--;
                                    minesView.setText(String.valueOf(quantFlags)+"/"+String.valueOf(quant_bombas));
                                }
                                botoes[h][k].setIcon(null);
                                botoes[h][k].setText(String.valueOf(campo[h][k]));
                                botoes[h][k].setBackground(clickedColor);
                                conversions++;
                            }
                        }
                        catch(ArrayIndexOutOfBoundsException e){}
                    }
                }
            }
        }
    }

    // Esta função é chamada quando é realizado um clique sobre um label
    private static void botoesClick(JPanel panel, JLabel label, java.awt.event.MouseEvent evt, int[][] campo)
    {
        int x = label.getX();
        int y = label.getY();

        int i = (y-y_i)/space;
        int j = (x-x_i)/space;
                
        if(evt.getButton()==leftClick && flags[i][j]==0)
        {
            // Ações quando clicar com botão esquerdo
            label.setBackground(clickedColor);
            label.setIcon(null);

            if(campo[i][j]!=9)
            {
                label.setText(String.valueOf(campo[i][j]));
                if(campo[i][j]==0)
                {
                    conversions++;
                    while(conversions!=0)
                    {
                        fieldExtend();
                    }
                }
            } else {
                label.setBackground(Color.red);
                label.setText("");
                if (mode==false) {
                    label.setIcon(exp);
                } else {
                    label.setIcon(coronga);
                }
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setVerticalAlignment(JLabel.CENTER);
                gameOver();
            }
            alterarCores();

        } else if(evt.getButton()==rightClick)
        {
            if (label.getBackground()==buttonsColor) {
                flags[i][j]=1;
                label.setText("");
                if (mode==false) {
                    label.setIcon(flag);
                } else {
                    label.setIcon(hazard);
                }
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setVerticalAlignment(JLabel.CENTER);
                label.setBackground(Color.yellow);
                quantFlags++;
            } else if(label.getBackground()==Color.yellow){
                flags[i][j]=0;
                label.setBackground(buttonsColor);
                label.setIcon(null);
                label.setText(" ");
                quantFlags--;
            }
            minesView.setText(String.valueOf(quantFlags)+"/"+String.valueOf(quant_bombas[nivel]));
        }
        gameWin();
    }

    

    private static void optionsClick(JPanel panel, java.awt.event.MouseEvent evt, int[][] campo)
    {
        newGame();
    }

    public static void newGame()
    {
        gameFrame.getContentPane().remove(playPanel);
        if (cronoUp==true) {
            cronometro.stop();
            seconds = 0;
            cronoUp = false;
        }
        createPanel();
        gameFrame.setContentPane(playPanel);
        gameFrame.revalidate();
    }


    // Colorir texto 
    public static void alterarCores()
    {
        for(int i=0; i<botoes.length; i++)
        {
            for(int j=0; j<botoes[i].length; j++)
            {
                // Matriz das bombas com valor 0
                switch(botoes[i][j].getText())
                {
                    case "0":
                        botoes[i][j].setText(" ");
                        botoes[i][j].setForeground(clickedColor);
                        break;
                    case "1":
                        botoes[i][j].setForeground(oneColor);
                        botoes[i][j].setFont(new Font("Monospaced", Font.BOLD, 25));
                        break;
                    case "2":
                        botoes[i][j].setForeground(twoColor);
                        botoes[i][j].setFont(new Font("Monospaced", Font.BOLD, 25));
                        break;
                    case "3":
                        botoes[i][j].setForeground(threeColor);
                        botoes[i][j].setFont(new Font("Monospaced", Font.BOLD, 25));
                        break;
                    case "4":
                        botoes[i][j].setForeground(fourColor);
                        botoes[i][j].setFont(new Font("Monospaced", Font.BOLD, 25));
                        break;
                    case "5":
                        botoes[i][j].setForeground(fiveColor);
                        botoes[i][j].setFont(new Font("Monospaced", Font.BOLD, 25));
                        break;
                    case "9":
                        botoes[i][j].setText("");;
                        if (mode==false && botoes[i][j].getBackground()!=Color.red) {
                            botoes[i][j].setIcon(mine);
                        } else {
                            botoes[i][j].setIcon(coronga);
                        }
                        break;
                }
            }
        }
    }


    // imprime matriz campo (Usada para debug)
    public static void mostraMatriz()
    {
        // Imprime a matriz no terminal
        for(int i=0; i<campo.length; i++)
        {
            for(int j=0; j<campo[i].length; j++)
            {
                // Matriz das bombas com valor 0
                System.out.print(campo[i][j]+" ");
            }
            System.out.println(" ");
        }
    }

    public static void changeMode(JLabel label)
    {
        mode = !mode;
        newGame();
    }

    public static void gameWin()
    {
        int hideLabels = 0;
        // Imprime a matriz no terminal
        for(int i=0; i<campo.length; i++)
        {
            for(int j=0; j<campo[i].length; j++)
            {
                if(botoes[i][j].getBackground()!=clickedColor)
                {
                    hideLabels++;
                }
            }
        }
        if(hideLabels==quant_bombas[nivel])
        {
            cronometro.stop();
            endGame("Você venceu!");
        }
    }

    public static void gameOver()
    {
        // code here
        for(int i=0; i<campo.length; i++)
        {
            for(int j=0; j<campo[i].length; j++)
            {
                // Matriz das bombas com valor 0
                if(campo[i][j]==9)
                {
                    botoes[i][j].setText("");
                    if(mode==false) {
                        botoes[i][j].setIcon(mine);
                    } else {
                        botoes[i][j].setIcon(coronga);
                    }
                    botoes[i][j].setHorizontalAlignment(JLabel.CENTER);
                    botoes[i][j].setVerticalAlignment(JLabel.CENTER);
                }
                try {
                    botoes[i][j].removeMouseListener(botoes[i][j].getMouseListeners()[0]);
                } catch (ArrayIndexOutOfBoundsException e) {}
            }
        }
        cronometro.stop();
        endGame("Game Over");        
    }

    public static void endGame(String message)
    {
        Object[] options = {"Reinicar", "Sair", "Cancelar"};
        int result = JOptionPane.showOptionDialog(null, "Escolha uma opção", message, JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        
        if (result == 0)
            newGame();
        else if (result == 1)            
            Runtime.getRuntime().exit(0);
    }
} 