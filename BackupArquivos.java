import javax.swing.*;  
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.StyledDocument;
import java.util.Arrays;
import java.util.ArrayList;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.awt.*; 
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import java.awt.Container;
import java.util.*;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;


/*
  Classe BackupArquivos responsavel por gerar o backup
  dos arquivos selecionados via File Browser e também
  selecionar o destino do backup gerado em zip.
*/

public class BackupArquivos implements ActionListener
{  
  private List<String> listaArquivos = new ArrayList<>(); /* Arquivos selecionados, para serem salvos */
  private List<String> arquivosSelecionados = new ArrayList<>(); /* Caminho completo dos arquivos a serem salvos */
  private JButton selecionarBtn = new JButton("Selecionar"); /* Botão para selecionar itens */
  private JButton gerarBackBtn = new JButton("Gerar Backup"); /* Botão para gerar backup */
  private JButton destinoBtn = new JButton("Destino"); /* Botão para selecionar destino */
  private JButton limparBtn = new JButton("Limpar"); /* Botão para limpar os itens selecionados anteriormente */
  private JButton removeBtn = new JButton("Remover >>");  /* Botão para remover algum item na lista de selecionados */
  private DefaultListModel<String> listaSelecionadosModel = new DefaultListModel<>();  
  private DefaultListModel<String> listaCompactadosModel = new DefaultListModel<>();  
  private JList<String> listaSelecionados = new JList<>(listaSelecionadosModel); /* Lista de itens selecionados */  
  private JList<String> listaCompactados = new JList<>(listaCompactadosModel); /* Lista de itens compactados */    
  private JLabel tituloPrincipal;  /* Label titulo principal */  
  private JLabel tituloSecundario;   /* Label titulo secundario */  
  private ImageIcon sigaIcone = new ImageIcon("siga.png");   /* Logo Siga */  
  private ImageIcon bbIcone = new ImageIcon("bb.png");   /* Logo BB */  
  private JLabel labelSiga;
  private JLabel labelBB;
  private JLabel labelDestino; /* Label do destino */  
  private JTextField destinoSelecionado; /* TextField Destino */  
  private JTextField nomeBackupTextField; /* TexField para nome de um backup marcado em outros */  
  private JPanel selecionadosPanel; /* Painel de itens selecionados */  
  private JPanel compactadosPanel; /* Painel de itens compactados */  
  private JPanel backupMenuPanel; /* Painel de finalidade */  
  private JRadioButton radioBackSiga; /* Radio button para finalidade siga para backup*/  
  private JRadioButton radioBackOutroArquivo; /* Radio button para finalidade diferente do siga */  
  private ButtonGroup radioBackGroup; /* Grupo radio de finalidade */  
  private Log log = new Log(); /* Classe log instanciada */  
  public static String destino; /* Destino do backup */  
  public String nomeArquivoBackup; /* Nome do backup */  

  /* Início método principal para o backup de arquivos */                
  BackupArquivos(){  
    ArrayList<String> arquivosSelecionados = new ArrayList(); /* Arquivos selecionados para o backup */  
    JFrame f= new JFrame(); /* Frame principal */  
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); /* Fechar o frame */  
    selecionadosPanel = new JPanel(); /* Instanciar painel de itens selecionados */                
    compactadosPanel = new JPanel(); /* Instanciar paineç de itens compactados */                              
    backupMenuPanel = new JPanel(); /* Instanciar painel de finalidade */                              
    /* Ativar eventos dos botões */                
    selecionarBtn.addActionListener(this);
    gerarBackBtn.addActionListener(this);
    destinoBtn.addActionListener(this);
    limparBtn.addActionListener(this);
    removeBtn.addActionListener(this);

    destinoSelecionado = new JTextField();  /* Instanciar campo para o destino */                
    nomeBackupTextField = new JTextField();  /* Instanciar campo para preencher o nome do arquivo no caso de outros */                

    /* Instanciar labels principais */                
    tituloPrincipal = new JLabel();  
    tituloSecundario = new JLabel(); 
    labelDestino = new JLabel(); 
    JLabel labelBB = new JLabel(bbIcone);
    JLabel labelSiga = new JLabel(sigaIcone);

     /* Configuração de nome e posicionamento do nome backup em caso de outra finalidade */                
		nomeBackupTextField.setText("Nome Backup");
		nomeBackupTextField.setBounds(20, 550, 120, 20);
		
    /* Instanciar radio buttons e o grupo de radio button*/                
    radioBackSiga = new JRadioButton(); 
    radioBackOutroArquivo = new JRadioButton(); 
    radioBackGroup = new ButtonGroup(); 

    /* Configuração dos textos e posicionamento dos radio buttons */                
    radioBackSiga.setText("Projeto Siga"); 
    radioBackOutroArquivo.setText("Outros"); 
    radioBackSiga.setBounds(20, 475, 120, 20); 
    radioBackOutroArquivo.setBounds(20, 510, 120, 20); 

    /* Adicionamento dos radio buttons no grupo */
    radioBackGroup.add(radioBackSiga);
    radioBackGroup.add(radioBackOutroArquivo);

    /* Configuração de posicionamento e titulo dos paineis */
    selecionadosPanel.setBounds(160,120,280,345);    
    selecionadosPanel.setBorder(BorderFactory.createTitledBorder("Itens Selecionados"));
    compactadosPanel.setBounds(450,120,340,345);    
    compactadosPanel.setBorder(BorderFactory.createTitledBorder("Itens Compactados"));
    backupMenuPanel.setBounds(15,450,135,100);    
    backupMenuPanel.setBorder(BorderFactory.createTitledBorder("Finalidade"));
    
    destinoSelecionado.setBounds(310,530, 290,30); /* Posicionamento do campo de destino */

    /* Posicionamento dos botões */
    selecionarBtn.setBounds(15, 290, 135, 23);
    destinoBtn.setBounds(15, 240, 135, 23);
    gerarBackBtn.setBounds(250, 480, 135, 23);
    limparBtn.setBounds(500, 480, 135, 23);
    removeBtn.setBounds(15, 360, 135, 23);

    /* Posicionamento dos titulos */
    tituloPrincipal.setBounds(215, 10, 500, 64);
    tituloSecundario.setBounds(260, 60, 500, 48);
    
    /* Posicionamento dos labels dos icones e do label destino */
    labelSiga.setBounds(20, 9, 130, 130);
    labelBB.setBounds(675, 40, 70, 70);
    labelDestino.setBounds(250, 510, 70, 70);
    labelDestino.setText("Destino"); /* Texto para o label destino */

    destinoSelecionado.setEditable(false); /* Destino não é editável */
    
    /* Posicionamento das listas */
    listaSelecionados.setBounds(180,140, 240, 300);  
    listaCompactados.setBounds(460,140, 300, 300);

    /* Configuração dos titulos */
		tituloPrincipal.setFont (tituloPrincipal.getFont ().deriveFont (36.0f)); /* Fonte do titulo principal */
    tituloPrincipal.setText("CESUP PATRIMÔNIO"); /* Texto do titulo principal */
    tituloPrincipal.setForeground(Color.gray); /* Cor do titulo principal */
    tituloSecundario.setFont (tituloSecundario.getFont ().deriveFont (28.0f)); /* Fonte do titulo secundario */
    tituloSecundario.setText("Backup de Arquivos"); /* Fonte do titulo secundario */
    tituloSecundario.setForeground(Color.gray); /* Fonte do titulo secundario */

    /* Adicionando os itens ao frame */
    f.add(nomeBackupTextField);
		f.add(labelDestino);
    f.add(labelSiga);
    f.add(labelBB);
    f.add(selecionarBtn);
    f.add(gerarBackBtn);
    f.add(destinoBtn);
    f.add(limparBtn);
    f.add(removeBtn);
    f.add(radioBackSiga);
    f.add(radioBackOutroArquivo);
    f.add(listaSelecionados);  
    f.add(listaCompactados);  
    f.add(destinoSelecionado);  
    f.add(tituloSecundario);  
    f.add(tituloPrincipal);  
    f.add(selecionadosPanel);  
    f.add(compactadosPanel);  
    f.add(backupMenuPanel);  
    f.setSize(800,650);  
    f.setLayout(null);  
    f.setVisible(true);  
				
    }

    /* Método para deletar os itens selecionados */
    public void deletaItensArray(){
    	arquivosSelecionados.clear(); /* Deleta todos os arquivos selecionados */
    	listaArquivos.clear(); /* Deleta todos os caminhos dos arquivos selecionados */
    }

    /* Método para os Eventos dos botões */
    public void actionPerformed(ActionEvent evt) {
        Log log = new Log();
        String cmd = evt.getActionCommand();
        if (evt.getSource() == selecionarBtn) {
          JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
          jfc.setDialogTitle("Multiple file and directory selection:");
          jfc.setMultiSelectionEnabled(true);
          jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
          int returnValue = jfc.showOpenDialog(null);
          if (returnValue == JFileChooser.APPROVE_OPTION) {
            File[] files = jfc.getSelectedFiles();
            Arrays.asList(files).forEach(x -> {
            if (x.isDirectory()) {
              System.out.println(x.getName());
              System.out.println("Path : " + x.getAbsolutePath());
              arquivosSelecionados.add( x.getAbsolutePath());
              listaSelecionadosModel.addElement(x.getName());  
            }
          });
          System.out.println("\n- - - - - - - - - - -\n");
          System.out.println("Files Found\n");
          Arrays.asList(files).forEach(x -> {
          if (x.isFile()) {
            System.out.println(x.getName());
            System.out.println("Path : " + x.getAbsolutePath());
            arquivosSelecionados.add( x.getAbsolutePath());
            listaSelecionadosModel.addElement(x.getName());  
          }
        });      
      } 
    } 

    if (evt.getSource() == gerarBackBtn) {
      if(verificaFinalidade()=='N'){
        JOptionPane.showMessageDialog(null, "Você esqueceu de definir a finalidade");
        return;
      }
      if(!verificaRequisitos()){
        JOptionPane.showMessageDialog(null, "Você esqueceu de escolher destino ou selecionar os arquivos");
        return;
      }
      listaCompactadosModel.clear();
      JOptionPane.showMessageDialog(null, "Backup Iniciado");
      DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
      Date date = new Date();
      String data_corrente = dateFormat.format(date);
      DateFormat timeFormat = new SimpleDateFormat("HH.mm.ss");
      date = new Date();
      String hora_corrente = timeFormat.format(date);
      String dir;
      nomeArquivoBackup.toLowerCase();
      String zipFile = destino+"\\"+"backup_"+nomeArquivoBackup+"_"+data_corrente+"_"+hora_corrente+".zip";
      log.logUsuario(destino,zipFile);
      System.out.println(zipFile);
      for(int i=0;i<arquivosSelecionados.size();++i){
        System.out.println(arquivosSelecionados.get(i));
        dir = arquivosSelecionados.get(i);
        File directory = new File(dir);
        getFileList(directory);
        for (String filePath : listaArquivos) {
          System.out.println("Compressing: " + filePath);
          listaCompactadosModel.addElement(filePath);  
          log.gerarLog(filePath);
        }    
    }
    String strArquivosSelecionados = arquivosSelecionados.toString();
  
    try{
  System.out.println(strArquivosSelecionados.replace('[',' ').replace(']',' ').replace(',',' ').trim());
  strArquivosSelecionados = strArquivosSelecionados.replace('[',' ').replace(']',' ').replace(',',' ').trim();

      StringBuilder str = new StringBuilder(strArquivosSelecionados);
      StringBuilder str1 = new StringBuilder(destino);
      System.out.println("string = " + strArquivosSelecionados);
      System.out.println("string = " + destino);
    
      // appends the char argument as string to the StringBuilder.
      str.insert(0,'"');
      str.append('"');
      str1.insert(0,'"');
      str1.append('"');
      
      // print the StringBuilder after appending
      System.out.println("After append = " + str);
            System.out.println("After append = " + str1);


  Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", "start script\\copiarArquivo.bat",strArquivosSelecionados,destino}); 
  } 
    catch(IOException e) {
      System.out.println("exception");
      }

    JOptionPane.showMessageDialog(null, "Backup Finalizado");
  
    log.fimLog();
  
    deletaItensArray();
  
  }

  if (evt.getSource() == destinoBtn) {
    JFileChooser chooser = new JFileChooser();
    chooser.setCurrentDirectory(new java.io.File("."));
    chooser.setDialogTitle("choosertitle");
    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    chooser.setAcceptAllFileFilterUsed(false);
    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
      destino = String.valueOf(chooser.getSelectedFile());
      destinoSelecionado.setText(destino);
    } else {
    System.out.println("No Selection ");
    }
  }
  if (evt.getSource() == removeBtn) {
    int index = listaSelecionados.getSelectedIndex();
    String s = (String) listaSelecionados.getSelectedValue();
    listaSelecionadosModel.remove(index);
    arquivosSelecionados.remove(index);
    JOptionPane.showMessageDialog(null, "Item "+s+" removido");      	      
  }
  if (evt.getSource() == limparBtn) {
    deletaItensArray();
    listaCompactadosModel.clear();
    listaSelecionadosModel.clear();
    JOptionPane.showMessageDialog(null, "Itens Removidos");
  }
}

private void getFileList(File directory) {
  File[] files = directory.listFiles();
  if (files != null && files.length > 0) {
    for (File file : files) {
      if (file.isFile()) {
        listaArquivos.add(file.getAbsolutePath());
      } else {
        getFileList(file);
      }
    }
  }
}

public boolean verificaRequisitos(){
	if(arquivosSelecionados.size()==0){
		System.out.println("Nenhum item foi selecionado");
    		return false;
  	}
	if(destino.isEmpty()){
		System.out.println("Nenhum destino foi selecionado");
		return false;
	}
  return true;
}

public  static void executarScriptBackup(String strArquivosSelecionados){
  try{
  System.out.println(strArquivosSelecionados.replace('[',' ').replace(']',' ').replace(',',' ').trim());
  strArquivosSelecionados = strArquivosSelecionados.replace('[',' ').replace(']',' ').replace(',',' ').trim();
  
  Runtime.getRuntime().exec(new String[]{"copiar.bat",destino,strArquivosSelecionados,"-m","2"});
  } 
    catch(IOException e) {
      System.out.println("exception");
      }
}

public char verificaFinalidade(){
	char finalidade = ' '; 
  if (radioBackSiga.isSelected()) {   
    finalidade = 'S'; 
    nomeArquivoBackup = "siga";
  }   
  else if (radioBackOutroArquivo.isSelected()) { 
   	finalidade = 'O'; 
   	nomeArquivoBackup = nomeBackupTextField.getText();    
   } 
  else{
   	finalidade = 'N';
   }
  return finalidade;
}

  public static void main(String args[])    throws IOException{  
     new BackupArquivos();  
  } 
}


class Log{
    public static final int FILE_SIZE = 4096;
    private static DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    private static String dirFolder = "C:\\log";
    public static void gerarLog(String str){
       Logger logger = Logger.getLogger(RollingLogFile.class.getName());
        try {
            // Creating an instance of FileHandler with 5 logging files
            // sequences.
            Date date = new Date();
            String data_corrente = dateFormat.format(date);
            FileHandler handler = new FileHandler(dirFolder+"\\backup_"+data_corrente+".log", FILE_SIZE,1, true);
            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);
            logger.setUseParentHandlers(false);
        } catch (IOException e) {
            logger.warning("Failed to initialize logger handler.");
        }
        logger.info("compacta-> "+str);
    }
    public static void logUsuario(String destino,String nomeCompleto){
       Logger logger = Logger.getLogger(RollingLogFile.class.getName());
        try {
            // Creating an instance of FileHandler with 5 logging files
            // sequences.
            criaDiretorioLog();
            Date date = new Date();
            String data_corrente = dateFormat.format(date);
            FileHandler handler = new FileHandler(dirFolder+"\\backup_"+data_corrente+".log", FILE_SIZE,5, true);
            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);
            logger.setUseParentHandlers(false);
        } catch (IOException e) {
            logger.warning("Failed to initialize logger handler.");
        }
        String username = System.getProperty("user.name");
        logger.info("Usuário: "+username);
        logger.info("Destino: "+destino);
        logger.info("Arquivo de Backup: "+nomeCompleto);
        logger.info("-------INICIO BACKUP-------");
        
    }
    public static void fimLog(){
       Logger logger = Logger.getLogger(RollingLogFile.class.getName());
        try {
            // Creating an instance of FileHandler with 5 logging files
            // sequences.
            Date date = new Date();
            String data_corrente = dateFormat.format(date);
            FileHandler handler = new FileHandler(dirFolder+"\\backup_"+data_corrente+".log", FILE_SIZE,5, true);
            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);
            logger.setUseParentHandlers(false);
        } catch (IOException e) {
            logger.warning("Failed to initialize logger handler.");
        }
    logger.info("-------FIM BACKUP-------");
    
    }
    public static void criaDiretorioLog()  throws IOException {
        Path path = Paths.get(dirFolder);
        if (!Files.exists(path)) {
            Files.createDirectory(path);
            System.out.println("Directory created");
        } else {
            
            System.out.println("Directory already exists");
        }
    }
}
class RollingLogFile {

    public static void main(String[] args) {
        Log log = new Log();
        log.gerarLog("teste");
    }
}
