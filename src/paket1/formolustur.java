package paket1;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;



public class formolustur extends JFrame{
 
 public static  int sagtik_pointer=0;
 public static  int cifttik_pointer=0;

    private  int width=400;
    private  int height=300;
    JPanel panelim;
    JLabel video_goruntusu;
    JButton kucult_buyult;
    JButton cift_tik;
    JButton sag_tik;
    JCheckBox tek_tikla_sistem;
    JButton ses;
    public formolustur() throws HeadlessException {
                                super("TEAMEYE");
                                getinit();
    }

    private void getinit() {
        try {
            panelim=new JPanel();
            getContentPane().add(panelim);
            panelim.setLayout(null);
            panelim.setAutoscrolls(true);
           panelim.setEnabled(true);
           panelim.setFocusable(Boolean.TRUE);
           panelim.setBounds(0, 0, width, height);
           panelim.setBackground(new Color(217, 242, 242));
         //  panelim.setBorder(BorderFactory.createLineBorder(Color.yellow));
       
            video_goruntusu=new JLabel();
            video_goruntusu.setEnabled(true);
            video_goruntusu.setBounds(33, 10, 320,182);
            video_goruntusu.setFocusable(Boolean.TRUE);
            //video_goruntusu.setToolTipText("Video Acilacaktir");
          //  video_goruntusu.setText("Merhaba");
            video_goruntusu.setVisible(Boolean.TRUE);
           // video_goruntusu.setBorder(BorderFactory.createLineBorder(Color.red));
            panelim.add(video_goruntusu);
 
            
            
          kucult_buyult=new JButton();
           kucult_buyult.setBounds(275, 200, 80, 30);
           kucult_buyult.setForeground(Color.BLACK);
           kucult_buyult.setText("KÜÇÜLT");
           kucult_buyult.setFont(new Font("Arial Black",Font.PLAIN,10));
           kucult_buyult.setToolTipText("EKRANI KUCULTMEYE VE BUYULTMEYE YARAR.");  
           kucult_buyult.setActionCommand("kucult");
           kucult_buyult.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    
               if (e.getActionCommand().equals("kucult")) {
                   try {
                        int genislik,yukseklik;
                      genislik=width-200;
                      yukseklik=height-100;
                      panelim.setBounds(0,0,genislik,yukseklik);
                      video_goruntusu.setBounds(20, 10, 160, 140);
                      kucult_buyult.setBounds(60,155, 80, 30);
                      getContentPane().setPreferredSize(new Dimension(genislik,yukseklik));
                    formolustur.this.pack();
                    String actioncom=e.getActionCommand();
                    actioncom="buyult";
                    kucult_buyult.setActionCommand(actioncom);
                    kucult_buyult.setText("BÜYÜLT");
                   }
                   catch (Exception e1) {
                       JOptionPane.showMessageDialog(null, e1.getMessage(),"Hata var",JOptionPane.WARNING_MESSAGE);
                   }
                    }
               else if (e.getActionCommand().equals("buyult")) 
               {
                   try {
                       panelim.setBounds(0, 0, width, height);
                        video_goruntusu.setBounds(33, 10, 320,182);
                         kucult_buyult.setBounds(275, 200, 80, 30);
                           getContentPane().setPreferredSize(new Dimension(width,height));
                    formolustur.this.pack();
                    String actioncommad1=e.getActionCommand();
                    actioncommad1="kucult";
                    kucult_buyult.setActionCommand(actioncommad1);
                    kucult_buyult.setText("KÜÇÜLT");
                       
                   } catch (Exception e2) {
                        JOptionPane.showMessageDialog(null, e2.getMessage(),"Hata var",JOptionPane.WARNING_MESSAGE);
                   }
         
                    }
                }
               
            });
            kucult_buyult.setEnabled(Boolean.TRUE);
            kucult_buyult.setVisible(Boolean.TRUE);
            panelim.add(kucult_buyult);
            
            
            
            java.net.URL imgUrl=getClass().getResource("cift_tik.jpg");
            ImageIcon icon=new ImageIcon(imgUrl);
            cift_tik=new JButton();
            cift_tik.setIcon(icon);
            //cift_tik.setActionCommand("cift_tiklama butonu");
            cift_tik.setBounds(35, 200, 40, 30);
            cift_tik.setCursor(Cursor.getDefaultCursor());
            cift_tik.setEnabled(Boolean.TRUE);
            cift_tik.setToolTipText("Fare ile Cift Tıklama yapmak icin kullan");
            cift_tik.setFocusable(Boolean.TRUE);
            cift_tik.setActionCommand("tiklandi");
            cift_tik.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(e.getActionCommand().equals("tiklandi")){
                        try {
                            cift_tik.setIcon(null);
                             java.net.URL image=getClass().getResource("cift_tik1.jpg");
                             ImageIcon icon=new ImageIcon(image);
                             cift_tik.setIcon(icon);
                            //pointer degerini set ediyoruz.
                            cifttik_pointer=1;
                            String actioncom=e.getActionCommand();
                            actioncom="tiklanmadi";
                            cift_tik.setActionCommand(actioncom);
                        } catch (Exception exception) {
                            System.err.println(exception.getMessage());
                        }
                    }
                    else if(e.getActionCommand().equals("tiklanmadi")){
                        try {
                              cift_tik.setIcon(null);
                              java.net.URL image=getClass().getResource("cift_tik.jpg");
                              ImageIcon icon=new ImageIcon(image);
                              cift_tik.setIcon(icon);
                              //pointer degerini reset ediyoruz.
                              cifttik_pointer=0;
                               String actioncom=e.getActionCommand();
                         actioncom="tiklandi";
                         cift_tik.setActionCommand(actioncom);
                        } catch (Exception exception) {
                            System.err.println(exception.getMessage());
                        }
                    }
                }
            });
            panelim.add(cift_tik);
            
            
            
            
            java.net.URL imgUrl1=getClass().getResource("sag_tik.jpg");
            ImageIcon icon1=new ImageIcon(imgUrl1);
            sag_tik=new JButton();
            sag_tik.setIcon(icon1);
           // sag_tik.setActionCommand("sag_tiklama butonu");
            sag_tik.setBounds(100, 200, 40, 30);
            sag_tik.setCursor(Cursor.getDefaultCursor());
            sag_tik.setEnabled(Boolean.TRUE);
            sag_tik.setToolTipText("Fare ile sag tiklam yapmak icin kullan");
            sag_tik.setFocusable(Boolean.TRUE);
            sag_tik.setActionCommand("tiklandi");
            sag_tik.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                   if(e.getActionCommand().equals("tiklandi")){
                       try {
                       sag_tik.setIcon(null);
                       java.net.URL image=getClass().getResource("sag2.jpg");
                       ImageIcon icon=new ImageIcon(image);
                       sag_tik.setIcon(icon);
                       //işaretçi degeri set edilidi ve kontrol edilmeye hazır.
                       sagtik_pointer=1;
                       String actioncom=e.getActionCommand();
                       actioncom="tiklanmadi";
                       sag_tik.setActionCommand(actioncom);
                       } catch (Exception ex) {
                           System.err.println(ex.getMessage());
                       }
                   }
                   else if(e.getActionCommand().equals("tiklanmadi")){
                       try {
                       sag_tik.setIcon(null);
                         java.net.URL imgUrl1=getClass().getResource("sag_tik.jpg");
                         ImageIcon icon1=new ImageIcon(imgUrl1);
                          sag_tik.setIcon(icon1);  
                          //işaretçi degerini reset ediyoruz.
                          sagtik_pointer=0;
                          String actioncom=e.getActionCommand();
                         actioncom="tiklandi";
                         sag_tik.setActionCommand(actioncom);
                         
                       } 
                       catch (Exception exception) {
                           System.out.println(exception.getMessage());
                       }
                   }
                }
            });
            panelim.add(sag_tik);
            
            // tamam artık ses işleme yi de buraya
            ses=new JButton();
            ses.setBounds(100,240,175,30);
            ses.setText("SES İLE KONTROL");
            ses.setForeground(Color.RED);
            ses.setCursor(Cursor.getDefaultCursor());
            ses.setEnabled(Boolean.TRUE);
            ses.setToolTipText("Ses ile bilgisayara komut vermek için kullanılmaktadır.");
            ses.setFocusable(Boolean.TRUE);
            ses.setActionCommand("aktif_edildi");
            ses.setFont(new Font("Arial Black", Font.PLAIN,12));
                ses.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        // şimdi yapılması gerekenleri burada şöyle olmuş olacak
                        //1.olarak ses butonuna tıklandığında bu proje kapatılacak ve diğer proje açılacak
                        // bunun önüne şimdi diğer programı açtırmam lazım
                        String metin="C:\\Users\\Mert\\Desktop\\sestanima\\dist\\sestanima.jar";
                        Process p;	//resultText="";
                         p = Runtime.getRuntime().exec("cmd /c java -jar "+metin);
                        Runtime.getRuntime().exit(0);
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            });
            panelim.add(ses);
            
            
             setSize(width, height);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
           // setLocation(new Point(1220, 450));
            setAlwaysOnTop(true);
            setBackground(Color.white);
            setLocationRelativeTo(null);
            setResizable(Boolean.FALSE);
            setEnabled(true);
            
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(),"Hata mesajı",JOptionPane.ERROR_MESSAGE);
        }
    }
    public  void  iconata(Image resiImage){
        ImageIcon icon=new ImageIcon();
        icon.setImage(resiImage);
        video_goruntusu.setIcon(icon);
        panelim.revalidate();
        
    }
    public void sagdeğistir(){
        if(videoac.sag_tiklandimi==1){
            //şimdi burada da tıklandıktan sonraki tam tersi işlemleri yapmam gerekecek.
            try {
                         sag_tik.setIcon(null);
                         java.net.URL imgUrl1=getClass().getResource("sag_tik.jpg");
                         ImageIcon icon1=new ImageIcon(imgUrl1);
                         sag_tik.setIcon(icon1);  
                          //işaretçi degerini reset ediyoruz.
                          sagtik_pointer=0;
                          String actioncom;
                         actioncom="tiklandi";
                         sag_tik.setActionCommand(actioncom);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }
    public  void ciftdeğistir(){
        
        if(videoac.cift_tiklandimi==1){
            try {
                              cift_tik.setIcon(null);
                              java.net.URL image=getClass().getResource("cift_tik.jpg");
                              ImageIcon icon=new ImageIcon(image);
                              cift_tik.setIcon(icon);
                              //pointer degerini reset ediyoruz.
                              cifttik_pointer=0;
                               String actioncom;
                         actioncom="tiklandi";
                         cift_tik.setActionCommand(actioncom);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }
}