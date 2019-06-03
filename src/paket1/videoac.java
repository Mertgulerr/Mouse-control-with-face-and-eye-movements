
package paket1;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import static org.opencv.highgui.HighGui.imshow;
import static org.opencv.highgui.HighGui.waitKey;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
import static org.opencv.objdetect.Objdetect.CASCADE_SCALE_IMAGE;

public class videoac {
static ArrayList<Point>merkez_noktalarının_dizisi=new ArrayList<>();
static  ArrayList<Point>tıklanma_noktalari=new ArrayList<>();
static int aradegisken=0;

//sag tıklama icin tıklanıp tıklanmadığını kontrol eden pointer kullanmamız lazım
 public static  int sag_tiklandimi=0;
 static  ArrayList<Point> sag_merkez_noktalarının_dizisi=new ArrayList<>();
 static  ArrayList<Point> sag_tıklanma_noktalari=new ArrayList<>();
 static int sag_aradegisken=0;  

 //çift tıklama için gerekli global tanımlamaların bulunduğu bölümü.
 public static int cift_tiklandimi=0;
 static ArrayList<Point> cift_merkez_noktalarının_dizisi=new ArrayList<>();
 static ArrayList<Point> cift_tıklanma_noktalari=new ArrayList<>();
 static int cift_aradegisken=0;


    public videoac() {
    }
 
     public static void main(String[] args) throws AWTException,NullPointerException {
      System.load("C:\\Users\\Mert\\Desktop\\opencv\\opencv\\build\\java\\x64\\opencv_java400.dll");
       CascadeClassifier facecascade = new CascadeClassifier("C:\\Users\\Mert\\Desktop\\opencv\\opencv\\build\\etc\\haarcascades\\haarcascade_frontalface_alt2.xml");
       CascadeClassifier eyecascade=new CascadeClassifier("C:\\Users\\Mert\\Desktop\\opencv\\opencv\\build\\etc\\haarcascades\\haarcascade_eye.xml");
        formolustur yeni_form=new formolustur();
         yeni_form.setVisible(true);
          VideoCapture videodevice=new VideoCapture();
          videodevice.open(0);
          if(videodevice.isOpened()){
               videodevice.set(Videoio.CAP_PROP_FRAME_WIDTH, 320.0);
                  videodevice.set(Videoio.CAP_PROP_FRAME_HEIGHT, 240.0);
                        videodevice.set(Videoio.CAP_PROP_BRIGHTNESS,50);
                                 videodevice.set(Videoio.CAP_PROP_CONTRAST, 50);
                                       videodevice.set(Videoio.CAP_PROP_SATURATION, 50);
                                              videodevice.set(Videoio.CAP_PROP_HUE,50);
                                                       videodevice.set(Videoio.CAP_PROP_FPS, 30);
          }
          else{
              System.out.println("Kamerayı açamadım bir hata olmalı");
              Runtime.getRuntime().exit(0);
          }
          Mat kameraverisi=new Mat(); 
          //sag tıklama için yeni bir kamera verisi yakalamam lazım
          Mat sagtıklamaverisi=new Mat();
          //çift tıklama yapmak için yeni bir frame alalım
          Mat  cifttıklamaverisi=new Mat();
          while (true) {     
              //şimdi sag tıklama için gerekli olan kodu yazmam lazım
              // ilk olarak aktifliğine ve pasifliğine bakıyorum
              if(formolustur.sagtik_pointer==1&&formolustur.cifttik_pointer==0){
                  sag_tiklandimi=0;
                  Date now=new Date();
                  long eklenmemis=now.getTime();
                  long eklenmis=now.getTime()+20000L;
                  while(eklenmemis!=eklenmis){
                      videodevice.read(sagtıklamaverisi);
                      Core.flip(sagtıklamaverisi,sagtıklamaverisi,1);
                      sagtıklama_islemleri_yap(sagtıklamaverisi,facecascade,eyecascade);
                      yeni_form.iconata(donustur(sagtıklamaverisi));
                      eklenmis-=1000;
                      // burada da tıklanıp tıklanmadığına bakıyorum 
                      // eğer tıklama yapıldıysa doğrudan dönğünün kırılması lazım yoksa devam eder 
                      // amacım tıklam yapıldıktan sonra döngüyü kırmak
                      if(sag_tiklandimi==1){
                         yeni_form.sagdeğistir();
                          break;
                      }
                  }
              }
              if(formolustur.cifttik_pointer==1 && formolustur.sagtik_pointer==0){
                 cift_tiklandimi=0;
                  Date now=new Date();
                  long eklenmemis=now.getTime();
                  long eklenmis=now.getTime()+20000L;
                  while(eklenmis!=eklenmemis){
                      videodevice.read(cifttıklamaverisi);
                      Core.flip(cifttıklamaverisi,cifttıklamaverisi,1);
                      cifttıklama_islemlerini_yap(cifttıklamaverisi,facecascade,eyecascade);
                      yeni_form.iconata(donustur(cifttıklamaverisi));
                      eklenmis-=1000;
                      if(cift_tiklandimi==1){
                          yeni_form.ciftdeğistir();
                          break;
                      }
                  }
              }
              
            videodevice.read(kameraverisi);
            Core.flip(kameraverisi, kameraverisi, 1);
             yuzgozbul(kameraverisi,facecascade,eyecascade);
             yeni_form.iconata(donustur(kameraverisi));
     }
}
     //alınan görüntünün form içerisine yollayabilmek için ramden geçiren fonksiyon.
    private static  BufferedImage donustur(Mat veri){
        MatOfByte bytematversi=new MatOfByte();
        Imgcodecs.imencode(".jpg", veri, bytematversi);
        byte[] bytearray=bytematversi.toArray();
        BufferedImage goruntu=null;
        try {
            InputStream inputStream=new ByteArrayInputStream(bytearray);
            goruntu=ImageIO.read(inputStream);
        } catch (IOException e) {
            return null;
        }
        return  goruntu;
    }  
    
    private static void yuzgozbul(Mat kameraverisi, CascadeClassifier facecascade, CascadeClassifier eyecascade) {
        try {
            Mat griMat=new Mat();
            Imgproc.cvtColor(kameraverisi, griMat, Imgproc.COLOR_RGB2GRAY);
            Imgproc.equalizeHist(griMat, griMat);
          
           /* gerçek_video_tasaması:
            
              HighGui.namedWindow("gerçek video", HighGui.WINDOW_NORMAL);
             imshow("gerçek video", griMat);
             if (waitKey(30)>=0) {
               Runtime.getRuntime().exit(0); 
            }*/
             
            MatOfRect bulacagim_yuzler=new MatOfRect();
            MatOfRect bulacagim_gozler=new MatOfRect();
            Rect[] bulanan_yuzlerin_dizisi;
            Rect[] bulunan_gozlerin_dizisi;
            
            facecascade.detectMultiScale(griMat,bulacagim_yuzler,1.1,2,0|CASCADE_SCALE_IMAGE,new Size(50,50),new Size(120,120)); 
            bulanan_yuzlerin_dizisi=bulacagim_yuzler.toArray();
            if(bulanan_yuzlerin_dizisi.length==0){
               // JOptionPane.showMessageDialog(null, "Şuan yüzünüzü bulamıyorum eğer yüz bölgenizde bir nesne varsa çıkartmanız gerekmektedir","Yüz bulamama Uyarısı",JOptionPane.ERROR_MESSAGE);
             return;
            }
            //tam bir yüz benim bulduğum ilk yüz olmaktadır.
            Mat tambiryuz=new Mat();
            tambiryuz=griMat.submat(bulanan_yuzlerin_dizisi[0]);
            /*bulduğum_ilk_yuz:
            
              HighGui.namedWindow("bulduğum_ilk_yuz", HighGui.WINDOW_NORMAL);
             imshow("bulduğum_ilk_yuz", tambiryuz);
             if (waitKey(30)>=0) {
               Runtime.getRuntime().exit(0); 
            }*/
            
            //Şimdi gözleri buluyorum
            //Ama artık kameraverisinden göz bulmama gerek kalmadı tambiryüzden bulabilirim
            //artık herşey tambir yüz içerisinde
            eyecascade.detectMultiScale(tambiryuz, bulacagim_gozler,1.1,2,0|CASCADE_SCALE_IMAGE,new Size(15,15),new Size(30,30));
            //Şimdi yüzümün etrafını çizelim
            Imgproc.rectangle(kameraverisi, bulanan_yuzlerin_dizisi[0].tl(),bulanan_yuzlerin_dizisi[0].br(),new Scalar(0, 0, 179),3);
            bulunan_gozlerin_dizisi=bulacagim_gozler.toArray();
            if(bulunan_gozlerin_dizisi.length!=2){
                //JOptionPane.showMessageDialog(null, "Nedense iki tane göz bulamadım göz bölgenizde bir nesne varsa çıkartınız","Göz bulamama hatası",JOptionPane.ERROR_MESSAGE);
                return;
            }
             for (Rect rect : bulunan_gozlerin_dizisi) {
                Imgproc.rectangle(kameraverisi,new Point(bulanan_yuzlerin_dizisi[0].tl().x+rect.tl().x,bulanan_yuzlerin_dizisi[0].tl().y+rect.tl().y),new Point(bulanan_yuzlerin_dizisi[0].tl().x+rect.br().x,bulanan_yuzlerin_dizisi[0].tl().y+rect.br().y),new Scalar(0,255,0),2);
            }
             //Şimdi sol gözü bulmam gerekiyo
             Rect solgozuncercevesi = null;
              Rect saggozuncercevesi = null;
              solgozuncercevesi=solgozgetir(bulunan_gozlerin_dizisi);
                    for(int i=0;i<bulunan_gozlerin_dizisi.length;i++){
                        if(solgozuncercevesi.x!=bulunan_gozlerin_dizisi[i].x){
                            saggozuncercevesi=bulunan_gozlerin_dizisi[i];
                        }
                    }
               //sol gozun kendisi
              Mat solgozum=tambiryuz.submat(solgozuncercevesi);
               //sag gozun kendisi
              Mat saggozum=tambiryuz.submat(saggozuncercevesi);
              
              //ŞİMDİ İLK OLARAK SOL GÖZ İÇİN BÜYÜN OLAYLARI YAPALIM
              //DAHA SONRA AYNISINI SAG GOZ İÇİN YAPMAM GEREKECEK
              //1.SOL
              Mat sol_buyutulmus_goz=new Mat();
              Imgproc.resize(solgozum,sol_buyutulmus_goz,new Size(320,240));
               int sol_satır,sol_sutun;
               sol_satır=sol_buyutulmus_goz.rows();
               sol_sutun=sol_buyutulmus_goz.cols();
               Imgproc.equalizeHist(sol_buyutulmus_goz,sol_buyutulmus_goz );
               Imgproc.GaussianBlur(sol_buyutulmus_goz,sol_buyutulmus_goz,new Size(7,7),0);
               Mat sol_treshold=new Mat();
               Imgproc.threshold(sol_buyutulmus_goz,sol_treshold,1,255,Imgproc.THRESH_BINARY_INV);
               Mat sol_canny=new Mat();
               Imgproc.Canny(sol_treshold,sol_canny,10,20);
               
               ArrayList<MatOfPoint>sol_contours=new ArrayList<>();
               ArrayList<MatOfPoint>yeni_sol_contours=new ArrayList<>();
               Mat sol_hierarsi=new Mat();
               Random sol_rng=new Random(12345);
               
               Imgproc.findContours(sol_canny,sol_contours,sol_hierarsi,Imgproc.RETR_TREE,Imgproc.CHAIN_APPROX_SIMPLE);
               
               double sol_sonuc[]=new double[sol_contours.size()];
               for(int i=0;i<sol_contours.size();i++){
                   sol_sonuc[i]=Imgproc.contourArea(sol_contours.get(i));
               }
               //SOL İÇİN BUBBLE SORT
               double sol_temp;
               for(int i=0;i<sol_sonuc.length;i++){
                   for(int j=sol_sonuc.length-1;j>i;j--){
                       if(sol_sonuc[j-1]>sol_sonuc[j]){
                           sol_temp=sol_sonuc[j-1];
                           sol_sonuc[j-1]=sol_sonuc[j];
                           sol_sonuc[j]=sol_temp;
                       }
                   }
               }
               for(int i=0;i<sol_contours.size();i++){
                   for(int j=0;j<sol_contours.size();j++){
                       if(Imgproc.contourArea(sol_contours.get(i))==sol_sonuc[j]){
                           yeni_sol_contours.add(sol_contours.get(i));
                       }
                   }
               }
              
               Mat sol_drawing=Mat.zeros(sol_canny.size(), CvType.CV_8UC1);
               Rect sol_cerceveRect=new Rect();
               String sol_metin[]=new String[yeni_sol_contours.size()];
               for(int i=0;i<sol_contours.size();i++){
                   sol_cerceveRect=Imgproc.boundingRect(yeni_sol_contours.get(i));
                   sol_metin[i]=String.valueOf((sol_cerceveRect.width)*(sol_cerceveRect.height));
               }
               
               double sol_alan[]=new double[sol_metin.length];
               ArrayList<MatOfPoint>sol_result_contours=new ArrayList<>();
               for(int i=0;i<sol_alan.length;i++){
                   sol_alan[i]=Double.valueOf(sol_metin[i]);
               }
              
               double sol_maxalan=sol_alan[0];
               for(int i=1;i<sol_alan.length;i++){
                   if(sol_alan[i]>sol_maxalan){
                       sol_maxalan=sol_alan[i];
                   }
               }
               
               for(int t=0;t<yeni_sol_contours.size();t++){
                   Rect shaRect=new Rect();
                   shaRect=Imgproc.boundingRect(yeni_sol_contours.get(t));
                   if((shaRect.width*shaRect.height)==sol_maxalan){
                       sol_result_contours.add(yeni_sol_contours.get(t));
                       break;
                   }
               }
               Rect solnewRect=new Rect();
               Rect solusingRect=new Rect();
               String solmetinalani;
               for(int i=0;i<sol_result_contours.size();i++){
                   
                   Scalar color=new Scalar(sol_rng.nextInt(256),sol_rng.nextInt(256),sol_rng.nextInt(256));
                   solnewRect=Imgproc.boundingRect(sol_result_contours.get(i));
                   solusingRect=solnewRect;
                   solmetinalani=String.valueOf((solnewRect.width)*(solnewRect.height));
                   Imgproc.rectangle(sol_buyutulmus_goz,new Point(solnewRect.x,solnewRect.y),new Point(solnewRect.x+solnewRect.width,solnewRect.y+solnewRect.height),color,3);
                   Imgproc.putText(sol_buyutulmus_goz,solmetinalani,new Point(solnewRect.x,solnewRect.y-5),1,2,color);
                   Imgproc.line(sol_buyutulmus_goz,new Point(solnewRect.x+(int)(solnewRect.width/2),0),new Point(solnewRect.x+(int)(solnewRect.width/2),sol_satır),color,2);
                   Imgproc.line(sol_buyutulmus_goz,new Point(0,solnewRect.y+(int)(solnewRect.height/2)),new Point(sol_sutun,solnewRect.y+(int)(solnewRect.height/2)),color,2);
               }
               //şimdi denemek istediğim sol_buyutulmuş göz için houghcircle uygulmak
               //dogrudan hough yazacam
               
                 Mat sol_daireler=new Mat();
                 ArrayList<Mat>sol_dairelerin_dizisi=new ArrayList<>();
                 if(sol_buyutulmus_goz.rows()!=0){
                     Mat gecici_çözüm=new Mat(sol_buyutulmus_goz.rows(),sol_buyutulmus_goz.cols(),sol_buyutulmus_goz.type());
                     sol_buyutulmus_goz.convertTo(gecici_çözüm,-1,1,0);
                     Imgproc.HoughCircles(gecici_çözüm,sol_daireler,Imgproc.CV_HOUGH_GRADIENT,1,255,100,10,15,30);//burası değişebilir
                    if(sol_daireler.cols()>0){
                      //  System.out.println(sol_daireler.cols()+"bulunan dairelerin sutunu------->"+sol_daireler.rows()+"bulunan dairelerin satırı");
                        sol_dairelerin_dizisi.add(sol_daireler);
                    } 
                    if(sol_dairelerin_dizisi.size()>0&&!sol_dairelerin_dizisi.isEmpty()){
                        Mat sol_gozbebeği=gözbebegibul(gecici_çözüm,sol_dairelerin_dizisi,sol_dairelerin_dizisi.size());
                        Point sol_merkez=new Point();
                        if(sol_gozbebeği.cols()>0){
                            for(int i=0;i<sol_gozbebeği.cols();i++){
                                double sol_data[]=sol_gozbebeği.get(0, i);
                                if(sol_data==null)break;
                                sol_merkez.x=Math.round(sol_data[0]);
                                sol_merkez.y=Math.round(sol_data[1]);
                                int sol_yaricap=(int)Math.round(sol_data[2]);
                                Imgproc.circle(sol_buyutulmus_goz,sol_merkez,sol_yaricap,new Scalar(255,15,50),2);
                                Imgproc.circle(sol_buyutulmus_goz, sol_merkez,1,new Scalar(100,100,250));
                            }
                        }
                    }
                 }
               //şimdi contour algoritması ile hough transformunu birleştirmem lkazım
               //hough transformunun merkezleri ile  contoursun merkezlerini karşılaştıracağım
               //olay şu circle un içinde olması gerekir contoursun dışına çıkamaz
                //sol göz için merkezlerin heabı
                Point sol_icin_orta_nokta=new Point();
                sol_icin_orta_nokta.x=(solgozuncercevesi.tl().x+solgozuncercevesi.br().x)/2;
                sol_icin_orta_nokta.y=(solgozuncercevesi.tl().y+solgozuncercevesi.br().y)/2;
                //ekrana bas bakalım
               // System.out.println("Sol gozun orta noktası x ekseni:"+sol_icin_orta_nokta.x+"y ekseni:"+sol_icin_orta_nokta.y);
                Imgproc.circle(kameraverisi, new Point(bulanan_yuzlerin_dizisi[0].tl().x+sol_icin_orta_nokta.x,bulanan_yuzlerin_dizisi[0].tl().y+sol_icin_orta_nokta.y),3,new Scalar(0,0,255),3);
               
                //sag goz icin merkez hesabı
                Point sag_icin_orta_nokta=new Point();
                sag_icin_orta_nokta.x=(saggozuncercevesi.tl().x+saggozuncercevesi.br().x)/2;
                sag_icin_orta_nokta.y=(saggozuncercevesi.tl().y+saggozuncercevesi.br().y)/2;
                //System.out.println("Sag gozun orta noktası x ekseni:"+sag_icin_orta_nokta.x+"y ekseni"+sag_icin_orta_nokta.y);
                Imgproc.circle(kameraverisi, new Point(bulanan_yuzlerin_dizisi[0].tl().x+sag_icin_orta_nokta.x,bulanan_yuzlerin_dizisi[0].tl().y+sag_icin_orta_nokta.y),3,new Scalar(255,0,0),3);
                
                //noktalar arası uzaklık
              Point gozler_arasi_orta_nokta=new Point();
              gozler_arasi_orta_nokta.x=(sol_icin_orta_nokta.x+sag_icin_orta_nokta.x)/2;
              gozler_arasi_orta_nokta.y=(sol_icin_orta_nokta.y+sag_icin_orta_nokta.y)/2;
             // System.out.println("gozler arasi orta nokta x ekseni:"+gozler_arasi_orta_nokta.x+"y ekseni:"+gozler_arasi_orta_nokta.y);
              Imgproc.circle(kameraverisi, new Point(bulanan_yuzlerin_dizisi[0].tl().x+ gozler_arasi_orta_nokta.x,bulanan_yuzlerin_dizisi[0].tl().y+ gozler_arasi_orta_nokta.y),3,new Scalar(0,255,255),3);
                
              //yuzun ortası
              Point yuzun_ortası=new Point();
              yuzun_ortası.x=(bulanan_yuzlerin_dizisi[0].tl().x+bulanan_yuzlerin_dizisi[0].br().x)/2;
              yuzun_ortası.y=(bulanan_yuzlerin_dizisi[0].tl().y+bulanan_yuzlerin_dizisi[0].br().y)/2;
              //System.out.println("bulunan yuzun orta noktası x ekseni:"+yuzun_ortası.x+"y ekseni:"+yuzun_ortası.y);
              Imgproc.circle(kameraverisi, new Point(yuzun_ortası.x,yuzun_ortası.y),3,new Scalar(255,0,255),3);
                
              //sonuc hesaplama
           double[] sonuc=new double[4];
             merkez_noktalarının_dizisi.add(yuzun_ortası);
             sonuc=maxminbul(merkez_noktalarının_dizisi);
             Point sonucPoint=new Point();
             sonucPoint=merkez_noktalarını_stabilize_et(merkez_noktalarının_dizisi);
             Point monitor_konumu=new Point();
             System.out.println("ŞU AN Kİ MECVUT NOKTA X EKSENİ:"+sonucPoint.x+"Y EKSENİ:"+sonucPoint.y);
             if(sonucPoint.x>=sonuc[0]&& sonucPoint.x<=sonuc[2]){
                 if(sonucPoint.y>=sonuc[1]&& sonucPoint.y<=sonuc[3]){
                 if(sonucPoint.y>=60 && sonucPoint.y<=100){
                   
                     sonucPoint.y-=15;
                 }
                 if(sonucPoint.y>160){
                   
                     sonucPoint.y+=20;
                 }
                if(sonucPoint.x>250){   //bu x eksenini sağ tarafa gitsin diye
                   
                    sonucPoint.x+=30;
                }
                if(sonucPoint.x<70){
                   
                    sonucPoint.x-=30;
                }
                     if(sonucPoint.x>=0&& sonucPoint.x<=80){
             if(sonucPoint.y>=0&& sonucPoint.y<=60){
                 monitor_konumu.x=sonucPoint.x*5;
                 monitor_konumu.y=sonucPoint.y*0.70;

             }
             else if(sonucPoint.y>60 &&sonucPoint.y<=120){
                  monitor_konumu.x=sonucPoint.x*5;
                 monitor_konumu.y=sonucPoint.y*3.75;

             }
             else if(sonucPoint.y>120 && sonucPoint.y<=180){
                   monitor_konumu.x=sonucPoint.x*5;
                 monitor_konumu.y=sonucPoint.y*3.75;
             }
             else if(sonucPoint.y>180 &&sonucPoint.y<=240){
                 monitor_konumu.x=sonucPoint.x*5;
                 monitor_konumu.y=sonucPoint.y*5.75;
             }
         }
         else if(sonucPoint.x>80 &&sonucPoint.x<=160){
              if(sonucPoint.y>=0&& sonucPoint.y<=60){
                    monitor_konumu.x=sonucPoint.x*5;
                 monitor_konumu.y=sonucPoint.y*0.70;
             }
             else if(sonucPoint.y>60 &&sonucPoint.y<=120){
                   monitor_konumu.x=sonucPoint.x*5;
                 monitor_konumu.y=sonucPoint.y*3.75;
             }
             else if(sonucPoint.y>120 && sonucPoint.y<=180){
                   monitor_konumu.x=sonucPoint.x*5;
                 monitor_konumu.y=sonucPoint.y*3.75;
             }
             else if(sonucPoint.y>180 &&sonucPoint.y<=240){
                  monitor_konumu.x=sonucPoint.x*5;
                 monitor_konumu.y=sonucPoint.y*5.75;
             }
         }
         else if(sonucPoint.x>160 && sonucPoint.x<=240){
              if(sonucPoint.y>=0&& sonucPoint.y<=60){
                    monitor_konumu.x=sonucPoint.x*5;
                 monitor_konumu.y=sonucPoint.y*0.70;
             }
             else if(sonucPoint.y>60 &&sonucPoint.y<=120){
                   monitor_konumu.x=sonucPoint.x*5;
                 monitor_konumu.y=sonucPoint.y*3.75;
             }
             else if(sonucPoint.y>120 && sonucPoint.y<=180){
                  monitor_konumu.x=sonucPoint.x*5;
                 monitor_konumu.y=sonucPoint.y*3.75;
             }
             else if(sonucPoint.y>180 &&sonucPoint.y<=240){
                 monitor_konumu.x=sonucPoint.x*5;
                 monitor_konumu.y=sonucPoint.y*5.75;
             }
         }
         //burada daha fazla x ekseninde gidebilmesi için 8 ile çarptım
         else if(sonucPoint.x>240 &&sonucPoint.x<=320){
              if(sonucPoint.y>=0&& sonucPoint.y<=60){
                   monitor_konumu.x=sonucPoint.x*5;
                 monitor_konumu.y=sonucPoint.y*0.70;
             }
             else if(sonucPoint.y>60 &&sonucPoint.y<=120){
                   monitor_konumu.x=sonucPoint.x*5;
                 monitor_konumu.y=sonucPoint.y*3.75;
             }
             else if(sonucPoint.y>120 && sonucPoint.y<=180){
                  monitor_konumu.x=sonucPoint.x*5;
                 monitor_konumu.y=sonucPoint.y*3.75;
             }
             else if(sonucPoint.y>180 &&sonucPoint.y<=240){
                  monitor_konumu.x=sonucPoint.x*5;
                 monitor_konumu.y=sonucPoint.y*5.75;
             }
         }
                 }
             }            
            mousekonumdegistir(monitor_konumu);
            HighGui.namedWindow("solgöz", HighGui.WINDOW_NORMAL);
             imshow("solgöz", sol_buyutulmus_goz);
             if (waitKey(30)>=0) {
             Runtime.getRuntime().exit(0); 
           }
            System.gc();
        } catch (Exception e) {
            System.err.println(e.getMessage()+e.getLocalizedMessage());
        }
        }
//Sol gözü bulmak için yazdığım kod gayet iyi çalışıyo
    private static Rect solgozgetir(Rect[] bulunan_gozlerin_dizisi) {
         int leftmost=99999999;
         int leftmostindex=-1;
         for (int i = 0; i < bulunan_gozlerin_dizisi.length; i++) 
         {
            if(bulunan_gozlerin_dizisi[i].tl().x<leftmost)
            {
                leftmost=(int) bulunan_gozlerin_dizisi[i].tl().x;
                leftmostindex=i;
            }
        }
         return bulunan_gozlerin_dizisi[leftmostindex];
    }
//dogru gözbebeğini bulmak için yazdığım metot tamam doğru çalışıyo
    private static Mat gözbebegibul(Mat grimat2, ArrayList<Mat> dairelerin_dizisi, int size) {
        try {
            grimat2.convertTo(grimat2, CvType.CV_32S);
            int[] toplam=new int[size];
            if(toplam.length==0){
                System.err.println("Toplam dizisi bomboş ve işlemler devam edemez.");
            }
            int [] values=new int[grimat2.channels()];
            Point center=new Point();
            double data[];
            Mat aradegisken=new Mat();
            for(int y=0;y<grimat2.rows();y++){
                for(int x=0;x<grimat2.cols();x++){
                    grimat2.get(y, x,values);
                    int value=values[0];
                    for(int i=0;i<dairelerin_dizisi.size();i++){
                        aradegisken=(Mat)dairelerin_dizisi.get(i);
                        data=aradegisken.get(0, i);
                        if(data==null){
                            break;
                        }
                        center.x=Math.round(data[0]);
                        center.y=Math.round(data[1]);
                        int radius=(int)Math.round(data[2]);
                        if(Math.pow(x-center.x,2)+Math.pow(y-center.y,2)<Math.pow(radius,2)){
                            toplam[i]+=value;
                            break;
                        }
                    }
                }
            }
            int smallestsum=9999999;
            int smallsumindex=-1;
            for(int i=0;i<dairelerin_dizisi.size();i++){
                if(toplam[i]<smallestsum){
                    smallestsum=toplam[i];
                    smallsumindex=i;
                }
            }
            return (Mat) dairelerin_dizisi.get(smallsumindex);
        } catch (Exception e) {
            System.err.println(e.getMessage()+e.getLocalizedMessage());
        return  null;
        }
    }
//gözbebeğinin merkezlerini stabile edebilmek adına yazdığım metot
    private static Point stabilizasyon(ArrayList<Point> centers, int windowsize) {
        try {
            float sumx=0;
            float sumy=0;
            int count=0;
            for(int i=Math.max(0, (int)(centers.size()-windowsize));i<centers.size();i++){
             sumx+=centers.get(i).x;
             sumy+=centers.get(i).y;
             ++count;
            }
            if(count>0){
                sumx/=count;
                sumy/=count;
            }
            return  new Point(sumx,sumy);
        } catch (Exception e) {
            System.err.println(e.getMessage()+e.getLocalizedMessage());
            return null;
        }
    }

    private static void mousekonumdegistir(Point mousePoint) throws AWTException {
        try{
            System.out.println(mousePoint.x +"------>"+mousePoint.y);
            
            if(mousePoint.x<0){
               mousePoint.x=0;
           }
           if(mousePoint.y<0){
               mousePoint.y=0;
           }
           if(mousePoint.y>900){
               mousePoint.y=900;
           }
           if(mousePoint.x>1600){
               mousePoint.x=1600;
           }
           Robot konum=new Robot();
           konum.mouseMove((int)mousePoint.x,(int)mousePoint.y);
           //Şuan tıklama olayını yazıyorum
           //ama doğrudan tıklayamazsın bir noktaya tıklama yapmak için
           //o noktanın kordinatının belirli bir süre aynı kalması lazım veya çok çok değişmesi lazım
          
           //ilk olarak static arraylist oluştur oluşturdum "tıklanma_noktalari"
          //bu fonksiyona her girmesi bir harekete karşılık geliyorsa her 
          // hareket bir kordinatsa bu kordinatları ekliyeceğim
          tıklanma_noktalari.add(mousePoint);
          tıklamayap(tıklanma_noktalari);
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    private static Point merkez_noktalarını_stabilize_et(ArrayList<Point> merkez_noktalarının_dizisi) {
        double toplam_x=0;
        double toplam_y=0;
        int count=0;
        int pencere_boyutu=5;
        if(merkez_noktalarının_dizisi.size()>0 &&!merkez_noktalarının_dizisi.isEmpty()){
                 for(int i=Math.max(0, (int)(merkez_noktalarının_dizisi.size()-pencere_boyutu));i<merkez_noktalarının_dizisi.size();i++){
                     toplam_x+=merkez_noktalarının_dizisi.get(i).x;
                     toplam_y+=merkez_noktalarının_dizisi.get(i).y;
                     ++count;
                 }
  }
        if(count>0){
            toplam_x/=count;
            toplam_y/=count;
           // System.out.println(toplam_x+"----"+toplam_y);
        }
return new Point(toplam_x,toplam_y);
    }

    private static double[] maxminbul(ArrayList<Point> merkez_noktalarının_dizisi) {
     double max_x,min_x,max_y,min_y;
     double[]sonuclar=new double[4];
     max_x=merkez_noktalarının_dizisi.get(merkez_noktalarının_dizisi.size()-1).x;
             max_y=merkez_noktalarının_dizisi.get(merkez_noktalarının_dizisi.size()-1).y;
                min_x=merkez_noktalarının_dizisi.get(0).x;
                    min_y=merkez_noktalarının_dizisi.get(0).y;
                    //min_x için
                    for(int i=0;i<merkez_noktalarının_dizisi.size();i++){
                        if(merkez_noktalarının_dizisi.get(i).x<min_x){
                            min_x=merkez_noktalarının_dizisi.get(i).x;
                        }
                    }
                    for(int j=0;j<merkez_noktalarının_dizisi.size();j++){
                        if(merkez_noktalarının_dizisi.get(j).y<min_y){
                            min_y=merkez_noktalarının_dizisi.get(j).y;
                        }
                    }
                    for(int k=0;k<merkez_noktalarının_dizisi.size();k++){
                        if(merkez_noktalarının_dizisi.get(k).x>max_x){
                            max_x=merkez_noktalarının_dizisi.get(k).x;
                        }
                    }
                    for(int t=0;t<merkez_noktalarının_dizisi.size();t++){
                        if(merkez_noktalarının_dizisi.get(t).y>max_y){
                            max_y=merkez_noktalarının_dizisi.get(t).y;
                        }
                    }
                    sonuclar[0]=min_x;
                    sonuclar[1]=min_y;
                    sonuclar[2]=max_x;
                    sonuclar[3]=max_y;
        System.out.println("minimum x:"+min_x+"minimum y:"+min_y);
        System.out.println("maxsimum x:"+max_x+"maxsimum y:"+max_y);
        return sonuclar;
    }
        
   private static void tıklamayap(ArrayList<Point> tıklanma_kordinatlari) throws InterruptedException {
        try {
            if(tıklanma_kordinatlari.size()>1)
            { 
                int mask=InputEvent.BUTTON1_DOWN_MASK;
                String soundname="C:\\Users\\Mert\\Desktop\\click.wav";
                Robot yerdegistir=new Robot(); 
                for(int i=aradegisken;i<tıklanma_kordinatlari.size();i++){
                    if((int)tıklanma_kordinatlari.get(i).x==(int)tıklanma_kordinatlari.get(i+1).x &&(int)tıklanma_kordinatlari.get(i).y==(int)tıklanma_kordinatlari.get(i+1).y){
                        yerdegistir.mousePress(mask);
                        AudioInputStream audioInputStream=AudioSystem.getAudioInputStream(new File(soundname).getAbsoluteFile());
                        Clip clip=AudioSystem.getClip();
                        clip.open(audioInputStream);
                        clip.start();
                        yerdegistir.mouseRelease(mask);
                        yerdegistir.delay(550);
                        //clip.stop();
                        break;
                    }
                    else if(Math.abs((int)tıklanma_kordinatlari.get(i).x-(int)tıklanma_kordinatlari.get(i+1).x)<=1 &&Math.abs((int)tıklanma_kordinatlari.get(i).y-(int)tıklanma_kordinatlari.get(i+1).y)<=1){
                         yerdegistir.mousePress(mask);
                         AudioInputStream audioInputStream=AudioSystem.getAudioInputStream(new File(soundname).getAbsoluteFile());
                        Clip clip=AudioSystem.getClip();
                        clip.open(audioInputStream);
                        clip.start();
                        yerdegistir.mouseRelease(mask);
                        yerdegistir.delay(550);
                        //clip.stop();
                        break;
                }
                    break;
                } 
                aradegisken++;
            }
        }
        catch(ArrayIndexOutOfBoundsException ex1){
            System.out.println(ex1.getMessage());
        } 
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
   }
   

   //BU İŞLEMLER SADECE  SAG TIKLAMA AKTİF OLDUĞUNDA GERÇEKLEŞECEKTİR.
   //BAŞLANGICI
    private static void sagtıklama_islemleri_yap(Mat kameraverisi, CascadeClassifier facecascade, CascadeClassifier eyecascade) {
        try {
              Mat griMat=new Mat();
            Imgproc.cvtColor(kameraverisi, griMat, Imgproc.COLOR_RGB2GRAY);
            Imgproc.equalizeHist(griMat, griMat);
          
           /* gerçek_video_tasaması:
            
              HighGui.namedWindow("gerçek video", HighGui.WINDOW_NORMAL);
             imshow("gerçek video", griMat);
             if (waitKey(30)>=0) {
               Runtime.getRuntime().exit(0); 
            }*/
             MatOfRect bulacagim_yuzler=new MatOfRect();
            MatOfRect bulacagim_gozler=new MatOfRect();
            Rect[] bulanan_yuzlerin_dizisi;
            Rect[] bulunan_gozlerin_dizisi;
            
              facecascade.detectMultiScale(griMat,bulacagim_yuzler,1.1,2,0|CASCADE_SCALE_IMAGE,new Size(50,50),new Size(120,120)); 
            bulanan_yuzlerin_dizisi=bulacagim_yuzler.toArray();
            if(bulanan_yuzlerin_dizisi.length==0){
               // JOptionPane.showMessageDialog(null, "Şuan yüzünüzü bulamıyorum eğer yüz bölgenizde bir nesne varsa çıkartmanız gerekmektedir","Yüz bulamama Uyarısı",JOptionPane.ERROR_MESSAGE);
             return;
            }
            //tam bir yüz benim bulduğum ilk yüz olmaktadır.
            Mat tambiryuz=new Mat();
            tambiryuz=griMat.submat(bulanan_yuzlerin_dizisi[0]);
            /*bulduğum_ilk_yuz:
            
              HighGui.namedWindow("bulduğum_ilk_yuz", HighGui.WINDOW_NORMAL);
             imshow("bulduğum_ilk_yuz", tambiryuz);
             if (waitKey(30)>=0) {
               Runtime.getRuntime().exit(0); 
            }*/
            
             //Şimdi gözleri buluyorum
            //Ama artık kameraverisinden göz bulmama gerek kalmadı tambiryüzden bulabilirim
            //artık herşey tambir yüz içerisinde
            eyecascade.detectMultiScale(tambiryuz, bulacagim_gozler,1.1,2,0|CASCADE_SCALE_IMAGE,new Size(15,15),new Size(30,30));
            //Şimdi yüzümün etrafını çizelim
            Imgproc.rectangle(kameraverisi, bulanan_yuzlerin_dizisi[0].tl(),bulanan_yuzlerin_dizisi[0].br(),new Scalar(0, 0, 179),3);
            bulunan_gozlerin_dizisi=bulacagim_gozler.toArray();
            if(bulunan_gozlerin_dizisi.length!=2){
                //JOptionPane.showMessageDialog(null, "Nedense iki tane göz bulamadım göz bölgenizde bir nesne varsa çıkartınız","Göz bulamama hatası",JOptionPane.ERROR_MESSAGE);
                return;
            }
             for (Rect rect : bulunan_gozlerin_dizisi) {
                 //burada da gözlerin etrafını çiziyorum
                Imgproc.rectangle(kameraverisi,new Point(bulanan_yuzlerin_dizisi[0].tl().x+rect.tl().x,bulanan_yuzlerin_dizisi[0].tl().y+rect.tl().y),new Point(bulanan_yuzlerin_dizisi[0].tl().x+rect.br().x,bulanan_yuzlerin_dizisi[0].tl().y+rect.br().y),new Scalar(0,255,0),2);
            }
             //Şimdi sol gözü bulmam gerekiyo
             Rect solgozuncercevesi = null;
              Rect saggozuncercevesi = null;
              solgozuncercevesi=solgozgetir(bulunan_gozlerin_dizisi);
                    for(int i=0;i<bulunan_gozlerin_dizisi.length;i++){
                        if(solgozuncercevesi.x!=bulunan_gozlerin_dizisi[i].x){
                            saggozuncercevesi=bulunan_gozlerin_dizisi[i];
                        }
                    }
                    
                    //şimdi contour algoritması ile hough transformunu birleştirmem lkazım
               //hough transformunun merkezleri ile  contoursun merkezlerini karşılaştıracağım
               //olay şu circle un içinde olması gerekir contoursun dışına çıkamaz
                //sol göz için merkezlerin heabı
                Point sol_icin_orta_nokta=new Point();
                sol_icin_orta_nokta.x=(solgozuncercevesi.tl().x+solgozuncercevesi.br().x)/2;
                sol_icin_orta_nokta.y=(solgozuncercevesi.tl().y+solgozuncercevesi.br().y)/2;
                //ekrana bas bakalım
               // System.out.println("Sol gozun orta noktası x ekseni:"+sol_icin_orta_nokta.x+"y ekseni:"+sol_icin_orta_nokta.y);
                Imgproc.circle(kameraverisi, new Point(bulanan_yuzlerin_dizisi[0].tl().x+sol_icin_orta_nokta.x,bulanan_yuzlerin_dizisi[0].tl().y+sol_icin_orta_nokta.y),3,new Scalar(0,0,255),3);
                    
                //sag goz icin merkez hesabı
                Point sag_icin_orta_nokta=new Point();
                sag_icin_orta_nokta.x=(saggozuncercevesi.tl().x+saggozuncercevesi.br().x)/2;
                sag_icin_orta_nokta.y=(saggozuncercevesi.tl().y+saggozuncercevesi.br().y)/2;
                //System.out.println("Sag gozun orta noktası x ekseni:"+sag_icin_orta_nokta.x+"y ekseni"+sag_icin_orta_nokta.y);
                Imgproc.circle(kameraverisi, new Point(bulanan_yuzlerin_dizisi[0].tl().x+sag_icin_orta_nokta.x,bulanan_yuzlerin_dizisi[0].tl().y+sag_icin_orta_nokta.y),3,new Scalar(255,0,0),3);
                
                //noktalar arası uzaklık
              Point gozler_arasi_orta_nokta=new Point();
              gozler_arasi_orta_nokta.x=(sol_icin_orta_nokta.x+sag_icin_orta_nokta.x)/2;
              gozler_arasi_orta_nokta.y=(sol_icin_orta_nokta.y+sag_icin_orta_nokta.y)/2;
             // System.out.println("gozler arasi orta nokta x ekseni:"+gozler_arasi_orta_nokta.x+"y ekseni:"+gozler_arasi_orta_nokta.y);
              Imgproc.circle(kameraverisi, new Point(bulanan_yuzlerin_dizisi[0].tl().x+ gozler_arasi_orta_nokta.x,bulanan_yuzlerin_dizisi[0].tl().y+ gozler_arasi_orta_nokta.y),3,new Scalar(0,255,255),3);
                
              //yuzun ortası
              Point yuzun_ortası=new Point();
              yuzun_ortası.x=(bulanan_yuzlerin_dizisi[0].tl().x+bulanan_yuzlerin_dizisi[0].br().x)/2;
              yuzun_ortası.y=(bulanan_yuzlerin_dizisi[0].tl().y+bulanan_yuzlerin_dizisi[0].br().y)/2;
              //System.out.println("bulunan yuzun orta noktası x ekseni:"+yuzun_ortası.x+"y ekseni:"+yuzun_ortası.y);
              Imgproc.circle(kameraverisi, new Point(yuzun_ortası.x,yuzun_ortası.y),3,new Scalar(255,0,255),3);
              
              double[] sonuc=new double[4];
              sag_merkez_noktalarının_dizisi.add(yuzun_ortası);
              sonuc=maxminbul(sag_merkez_noktalarının_dizisi);
              Point sonucPoint=new Point();
              sonucPoint=merkez_noktalarını_stabilize_et(sag_merkez_noktalarının_dizisi);
              Point monitor_konumu=new Point();
              System.out.println("SAG TIKLAMA İÇİN ŞUANKİ KONUM -- X EKSENİ:"+sonucPoint.x+"Y EKSENİ:"+sonucPoint.y);
             if(sonucPoint.x>=sonuc[0]&& sonucPoint.x<=sonuc[2]){
                 if(sonucPoint.y>=sonuc[1]&& sonucPoint.y<=sonuc[3]){
                 if(sonucPoint.y>=60 && sonucPoint.y<=100){
                   
                     sonucPoint.y-=15;
                 }
                 if(sonucPoint.y>160){
                     
                     sonucPoint.y+=20;
                 }
                if(sonucPoint.x>250){   //bu x eksenini sağ tarafa gitsin diye
                    
                    sonucPoint.x+=30;
                }
                if(sonucPoint.x<70){
                   
                    sonucPoint.x-=30;
                }
                     if(sonucPoint.x>=0&& sonucPoint.x<=80){
             if(sonucPoint.y>=0&& sonucPoint.y<=60){
                 monitor_konumu.x=sonucPoint.x*5;
                 monitor_konumu.y=sonucPoint.y*0.70;

             }
             else if(sonucPoint.y>60 &&sonucPoint.y<=120){
                  monitor_konumu.x=sonucPoint.x*5;
                 monitor_konumu.y=sonucPoint.y*3.75;

             }
             else if(sonucPoint.y>120 && sonucPoint.y<=180){
                   monitor_konumu.x=sonucPoint.x*5;
                 monitor_konumu.y=sonucPoint.y*3.75;
             }
             else if(sonucPoint.y>180 &&sonucPoint.y<=240){
                 monitor_konumu.x=sonucPoint.x*5;
                 monitor_konumu.y=sonucPoint.y*5.75;
             }
         }
         else if(sonucPoint.x>80 &&sonucPoint.x<=160){
              if(sonucPoint.y>=0&& sonucPoint.y<=60){
                    monitor_konumu.x=sonucPoint.x*5;
                 monitor_konumu.y=sonucPoint.y*0.70;
             }
             else if(sonucPoint.y>60 &&sonucPoint.y<=120){
                   monitor_konumu.x=sonucPoint.x*5;
                 monitor_konumu.y=sonucPoint.y*3.75;
             }
             else if(sonucPoint.y>120 && sonucPoint.y<=180){
                   monitor_konumu.x=sonucPoint.x*5;
                 monitor_konumu.y=sonucPoint.y*3.75;
             }
             else if(sonucPoint.y>180 &&sonucPoint.y<=240){
                  monitor_konumu.x=sonucPoint.x*5;
                 monitor_konumu.y=sonucPoint.y*5.75;
             }
         }
         else if(sonucPoint.x>160 && sonucPoint.x<=240){
              if(sonucPoint.y>=0&& sonucPoint.y<=60){
                    monitor_konumu.x=sonucPoint.x*5;
                 monitor_konumu.y=sonucPoint.y*0.70;
             }
             else if(sonucPoint.y>60 &&sonucPoint.y<=120){
                   monitor_konumu.x=sonucPoint.x*5;
                 monitor_konumu.y=sonucPoint.y*3.75;
             }
             else if(sonucPoint.y>120 && sonucPoint.y<=180){
                  monitor_konumu.x=sonucPoint.x*5;
                 monitor_konumu.y=sonucPoint.y*3.75;
             }
             else if(sonucPoint.y>180 &&sonucPoint.y<=240){
                 monitor_konumu.x=sonucPoint.x*5;
                 monitor_konumu.y=sonucPoint.y*5.75;
             }
         }
         //burada daha fazla x ekseninde gidebilmesi için 8 ile çarptım
         else if(sonucPoint.x>240 &&sonucPoint.x<=320){
              if(sonucPoint.y>=0&& sonucPoint.y<=60){
                   monitor_konumu.x=sonucPoint.x*5;
                 monitor_konumu.y=sonucPoint.y*0.70;
             }
             else if(sonucPoint.y>60 &&sonucPoint.y<=120){
                   monitor_konumu.x=sonucPoint.x*5;
                 monitor_konumu.y=sonucPoint.y*3.75;
             }
             else if(sonucPoint.y>120 && sonucPoint.y<=180){
                  monitor_konumu.x=sonucPoint.x*5;
                 monitor_konumu.y=sonucPoint.y*3.75;
             }
             else if(sonucPoint.y>180 &&sonucPoint.y<=240){
                  monitor_konumu.x=sonucPoint.x*5;
                 monitor_konumu.y=sonucPoint.y*5.75;
             }
         }
                 }
             }
             sag_tıklama_icin_konum_degistir(monitor_konumu);
             System.gc();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void sag_tıklama_icin_konum_degistir(Point mousePoint) {
        try {
            System.out.println("SAG TIKLAMADA HAREKET EDİLMEK İSTENEN NOKTANIN X EKSENİ:"+ mousePoint.x+"Y EKSENİ:"+mousePoint.y);
            if(mousePoint.x<0){
               mousePoint.x=0;
           }
           if(mousePoint.y<0){
               mousePoint.y=0;
           }
           if(mousePoint.y>900){
               mousePoint.y=900;
           }
           if(mousePoint.x>1600){
               mousePoint.x=1600;
           }
             Robot konum=new Robot();
           konum.mouseMove((int)mousePoint.x,(int)mousePoint.y);
            //Şuan tıklama olayını yazıyorum
           //ama doğrudan tıklayamazsın bir noktaya tıklama yapmak için
           //o noktanın kordinatının belirli bir süre aynı kalması lazım veya çok çok değişmesi lazım
          
           //ilk olarak static arraylist oluştur oluşturdum "sag_tıklanma_noktalari"
          //bu fonksiyona her girmesi bir harekete karşılık geliyorsa her 
          // hareket bir kordinatsa bu kordinatları ekliyeceğim
          sag_tıklanma_noktalari.add(mousePoint);
          sag_tıklama_yap(sag_tıklanma_noktalari);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private static void sag_tıklama_yap(ArrayList<Point> sag_tıklanma_kordinatları) {
        try {
            if(sag_tıklanma_kordinatları.size()>1){
                int mask=InputEvent.BUTTON3_DOWN_MASK;
                String soundname="C:\\Users\\Mert\\Desktop\\sag.wav";
                Robot yerdegistir=new Robot(); 
                for(int i=sag_aradegisken;i<sag_tıklanma_kordinatları.size();i++){
                    if((int)sag_tıklanma_kordinatları.get(i).x==(int)sag_tıklanma_kordinatları.get(i+1).x &&(int)sag_tıklanma_kordinatları.get(i).y==(int)sag_tıklanma_kordinatları.get(i+1).y){
                        yerdegistir.mousePress(mask);
                        AudioInputStream audioInputStream=AudioSystem.getAudioInputStream(new File(soundname).getAbsoluteFile());
                        Clip clip=AudioSystem.getClip();
                        clip.open(audioInputStream);
                        clip.start();
                        yerdegistir.mouseRelease(mask);
                        yerdegistir.delay(500);
                        //clip.stop();
                        sag_tiklandimi=1;
                        break;
                    }
                    else if(Math.abs((int)sag_tıklanma_kordinatları.get(i).x-(int)sag_tıklanma_kordinatları.get(i+1).x)<=1 &&Math.abs((int)sag_tıklanma_kordinatları.get(i).y-(int)sag_tıklanma_kordinatları.get(i+1).y)<=1){
                        yerdegistir.mousePress(mask);
                         AudioInputStream audioInputStream=AudioSystem.getAudioInputStream(new File(soundname).getAbsoluteFile());
                        Clip clip=AudioSystem.getClip();
                        clip.open(audioInputStream);
                        clip.start();
                        yerdegistir.mouseRelease(mask);
                        yerdegistir.delay(500);
                        //clip.stop();
                        sag_tiklandimi=1;
                        break;
                    }
                    break;
                }
                sag_aradegisken++;
            }
        } catch(ArrayIndexOutOfBoundsException ex1){
            System.err.println(ex1.getMessage());
        } 
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    //BİTİŞİ
    
    //BU İŞLEMLER SADECE CİFT TIKLAMA İÇİN ÇALIŞACAKTIR.
    //BAŞLANGICI
       private static void cifttıklama_islemlerini_yap(Mat kameraverisi, CascadeClassifier facecascade, CascadeClassifier eyecascade) {
           try {
                Mat griMat=new Mat();
            Imgproc.cvtColor(kameraverisi, griMat, Imgproc.COLOR_RGB2GRAY);
            Imgproc.equalizeHist(griMat, griMat);
          
           /* gerçek_video_tasaması:
            
              HighGui.namedWindow("gerçek video", HighGui.WINDOW_NORMAL);
             imshow("gerçek video", griMat);
             if (waitKey(30)>=0) {
               Runtime.getRuntime().exit(0); 
            }*/
             MatOfRect bulacagim_yuzler=new MatOfRect();
            MatOfRect bulacagim_gozler=new MatOfRect();
            Rect[] bulanan_yuzlerin_dizisi;
            Rect[] bulunan_gozlerin_dizisi;
            
              facecascade.detectMultiScale(griMat,bulacagim_yuzler,1.1,2,0|CASCADE_SCALE_IMAGE,new Size(50,50),new Size(120,120)); 
            bulanan_yuzlerin_dizisi=bulacagim_yuzler.toArray();
            if(bulanan_yuzlerin_dizisi.length==0){
               // JOptionPane.showMessageDialog(null, "Şuan yüzünüzü bulamıyorum eğer yüz bölgenizde bir nesne varsa çıkartmanız gerekmektedir","Yüz bulamama Uyarısı",JOptionPane.ERROR_MESSAGE);
             return;
            }
            //tam bir yüz benim bulduğum ilk yüz olmaktadır.
            Mat tambiryuz=new Mat();
            tambiryuz=griMat.submat(bulanan_yuzlerin_dizisi[0]);
            /*bulduğum_ilk_yuz:
            
              HighGui.namedWindow("bulduğum_ilk_yuz", HighGui.WINDOW_NORMAL);
             imshow("bulduğum_ilk_yuz", tambiryuz);
             if (waitKey(30)>=0) {
               Runtime.getRuntime().exit(0); 
            }*/
            
             //Şimdi gözleri buluyorum
            //Ama artık kameraverisinden göz bulmama gerek kalmadı tambiryüzden bulabilirim
            //artık herşey tambir yüz içerisinde
            eyecascade.detectMultiScale(tambiryuz, bulacagim_gozler,1.1,2,0|CASCADE_SCALE_IMAGE,new Size(15,15),new Size(30,30));
            //Şimdi yüzümün etrafını çizelim
            Imgproc.rectangle(kameraverisi, bulanan_yuzlerin_dizisi[0].tl(),bulanan_yuzlerin_dizisi[0].br(),new Scalar(0, 0, 179),3);
            bulunan_gozlerin_dizisi=bulacagim_gozler.toArray();
            if(bulunan_gozlerin_dizisi.length!=2){
                //JOptionPane.showMessageDialog(null, "Nedense iki tane göz bulamadım göz bölgenizde bir nesne varsa çıkartınız","Göz bulamama hatası",JOptionPane.ERROR_MESSAGE);
                return;
            }
             for (Rect rect : bulunan_gozlerin_dizisi) {
                 //burada da gözlerin etrafını çiziyorum
                Imgproc.rectangle(kameraverisi,new Point(bulanan_yuzlerin_dizisi[0].tl().x+rect.tl().x,bulanan_yuzlerin_dizisi[0].tl().y+rect.tl().y),new Point(bulanan_yuzlerin_dizisi[0].tl().x+rect.br().x,bulanan_yuzlerin_dizisi[0].tl().y+rect.br().y),new Scalar(0,255,0),2);
            }
             //Şimdi sol gözü bulmam gerekiyo
             Rect solgozuncercevesi = null;
              Rect saggozuncercevesi = null;
              solgozuncercevesi=solgozgetir(bulunan_gozlerin_dizisi);
                    for(int i=0;i<bulunan_gozlerin_dizisi.length;i++){
                        if(solgozuncercevesi.x!=bulunan_gozlerin_dizisi[i].x){
                            saggozuncercevesi=bulunan_gozlerin_dizisi[i];
                        }
                    }
               
                    //şimdi contour algoritması ile hough transformunu birleştirmem lkazım
               //hough transformunun merkezleri ile  contoursun merkezlerini karşılaştıracağım
               //olay şu circle un içinde olması gerekir contoursun dışına çıkamaz
                //sol göz için merkezlerin heabı
                Point sol_icin_orta_nokta=new Point();
                sol_icin_orta_nokta.x=(solgozuncercevesi.tl().x+solgozuncercevesi.br().x)/2;
                sol_icin_orta_nokta.y=(solgozuncercevesi.tl().y+solgozuncercevesi.br().y)/2;
                //ekrana bas bakalım
               // System.out.println("Sol gozun orta noktası x ekseni:"+sol_icin_orta_nokta.x+"y ekseni:"+sol_icin_orta_nokta.y);
                Imgproc.circle(kameraverisi, new Point(bulanan_yuzlerin_dizisi[0].tl().x+sol_icin_orta_nokta.x,bulanan_yuzlerin_dizisi[0].tl().y+sol_icin_orta_nokta.y),3,new Scalar(0,0,255),3);
                    
                //sag goz icin merkez hesabı
                Point sag_icin_orta_nokta=new Point();
                sag_icin_orta_nokta.x=(saggozuncercevesi.tl().x+saggozuncercevesi.br().x)/2;
                sag_icin_orta_nokta.y=(saggozuncercevesi.tl().y+saggozuncercevesi.br().y)/2;
                //System.out.println("Sag gozun orta noktası x ekseni:"+sag_icin_orta_nokta.x+"y ekseni"+sag_icin_orta_nokta.y);
                Imgproc.circle(kameraverisi, new Point(bulanan_yuzlerin_dizisi[0].tl().x+sag_icin_orta_nokta.x,bulanan_yuzlerin_dizisi[0].tl().y+sag_icin_orta_nokta.y),3,new Scalar(255,0,0),3);
                
                //noktalar arası uzaklık
              Point gozler_arasi_orta_nokta=new Point();
              gozler_arasi_orta_nokta.x=(sol_icin_orta_nokta.x+sag_icin_orta_nokta.x)/2;
              gozler_arasi_orta_nokta.y=(sol_icin_orta_nokta.y+sag_icin_orta_nokta.y)/2;
             // System.out.println("gozler arasi orta nokta x ekseni:"+gozler_arasi_orta_nokta.x+"y ekseni:"+gozler_arasi_orta_nokta.y);
              Imgproc.circle(kameraverisi, new Point(bulanan_yuzlerin_dizisi[0].tl().x+ gozler_arasi_orta_nokta.x,bulanan_yuzlerin_dizisi[0].tl().y+ gozler_arasi_orta_nokta.y),3,new Scalar(0,255,255),3);
                
              //yuzun ortası
              Point yuzun_ortası=new Point();
              yuzun_ortası.x=(bulanan_yuzlerin_dizisi[0].tl().x+bulanan_yuzlerin_dizisi[0].br().x)/2;
              yuzun_ortası.y=(bulanan_yuzlerin_dizisi[0].tl().y+bulanan_yuzlerin_dizisi[0].br().y)/2;
              //System.out.println("bulunan yuzun orta noktası x ekseni:"+yuzun_ortası.x+"y ekseni:"+yuzun_ortası.y);
              Imgproc.circle(kameraverisi, new Point(yuzun_ortası.x,yuzun_ortası.y),3,new Scalar(255,0,255),3);
              
              double[] sonuc =new double[4];
              cift_merkez_noktalarının_dizisi.add(yuzun_ortası);
              sonuc=maxminbul(cift_merkez_noktalarının_dizisi);
              Point sonucPoint=new Point();
             sonucPoint=merkez_noktalarını_stabilize_et(cift_merkez_noktalarının_dizisi);
             Point monitor_konumu=new Point();
             System.out.println("ÇİFT TIKLAMA İÇİN ŞU ANKİ KONUMUN X EKSENİ:"+sonucPoint.x+"Y EKSENİ:"+sonucPoint.y);
               if(sonucPoint.x>=sonuc[0]&& sonucPoint.x<=sonuc[2]){
                 if(sonucPoint.y>=sonuc[1]&& sonucPoint.y<=sonuc[3]){
                 if(sonucPoint.y>=60 && sonucPoint.y<=100){
                      sonucPoint.y-=15;
                 }
                 if(sonucPoint.y>160){ 
                     sonucPoint.y+=20;
                 }
                if(sonucPoint.x>250){   //bu x eksenini sag tarafa gitsin diye
                    sonucPoint.x+=30;
                }
                if(sonucPoint.x<70){
                    sonucPoint.x-=30;
                }
                     if(sonucPoint.x>=0&& sonucPoint.x<=80){
             if(sonucPoint.y>=0&& sonucPoint.y<=60){
                 monitor_konumu.x=sonucPoint.x*5;
                 monitor_konumu.y=sonucPoint.y*0.70;

             }
             else if(sonucPoint.y>60 &&sonucPoint.y<=120){
                  monitor_konumu.x=sonucPoint.x*5;
                 monitor_konumu.y=sonucPoint.y*3.75;

             }
             else if(sonucPoint.y>120 && sonucPoint.y<=180){
                   monitor_konumu.x=sonucPoint.x*5;
                 monitor_konumu.y=sonucPoint.y*3.75;
             }
             else if(sonucPoint.y>180 &&sonucPoint.y<=240){
                 monitor_konumu.x=sonucPoint.x*5;
                 monitor_konumu.y=sonucPoint.y*5.75;
             }
         }
         else if(sonucPoint.x>80 &&sonucPoint.x<=160){
              if(sonucPoint.y>=0&& sonucPoint.y<=60){
                    monitor_konumu.x=sonucPoint.x*5;
                 monitor_konumu.y=sonucPoint.y*0.70;
             }
             else if(sonucPoint.y>60 &&sonucPoint.y<=120){
                   monitor_konumu.x=sonucPoint.x*5;
                 monitor_konumu.y=sonucPoint.y*3.75;
             }
             else if(sonucPoint.y>120 && sonucPoint.y<=180){
                   monitor_konumu.x=sonucPoint.x*5;
                 monitor_konumu.y=sonucPoint.y*3.75;
             }
             else if(sonucPoint.y>180 &&sonucPoint.y<=240){
                  monitor_konumu.x=sonucPoint.x*5;
                 monitor_konumu.y=sonucPoint.y*5.75;
             }
         }
         else if(sonucPoint.x>160 && sonucPoint.x<=240){
              if(sonucPoint.y>=0&& sonucPoint.y<=60){
                    monitor_konumu.x=sonucPoint.x*5;
                 monitor_konumu.y=sonucPoint.y*0.70;
             }
             else if(sonucPoint.y>60 &&sonucPoint.y<=120){
                   monitor_konumu.x=sonucPoint.x*5;
                 monitor_konumu.y=sonucPoint.y*3.75;
             }
             else if(sonucPoint.y>120 && sonucPoint.y<=180){
                  monitor_konumu.x=sonucPoint.x*5;
                 monitor_konumu.y=sonucPoint.y*3.75;
             }
             else if(sonucPoint.y>180 &&sonucPoint.y<=240){
                 monitor_konumu.x=sonucPoint.x*5;
                 monitor_konumu.y=sonucPoint.y*5.75;
             }
         }
         //burada daha fazla x ekseninde gidebilmesi için 8 ile çarptim
         else if(sonucPoint.x>240 &&sonucPoint.x<=320){
              if(sonucPoint.y>=0&& sonucPoint.y<=60){
                   monitor_konumu.x=sonucPoint.x*5;
                 monitor_konumu.y=sonucPoint.y*0.70;
             }
             else if(sonucPoint.y>60 &&sonucPoint.y<=120){
                   monitor_konumu.x=sonucPoint.x*5;
                 monitor_konumu.y=sonucPoint.y*3.75;
             }
             else if(sonucPoint.y>120 && sonucPoint.y<=180){
                  monitor_konumu.x=sonucPoint.x*5;
                 monitor_konumu.y=sonucPoint.y*3.75;
             }
             else if(sonucPoint.y>180 &&sonucPoint.y<=240){
                  monitor_konumu.x=sonucPoint.x*5;
                 monitor_konumu.y=sonucPoint.y*5.75;
             }
         }
                 }
             }  
             cift_tıklama_icin_konum_degistir(monitor_konumu);
             System.gc();
           } catch (Exception e) {
               System.err.println(e.getMessage());
           }
    }
     
       private static void cift_tıklama_icin_konum_degistir(Point mousePoint) {
          try {
              System.out.println("ÇİFT TIKLAMADA HAREKET EDİLMEK İSTENEN NOKTANIN X EKSENİ:"+ mousePoint.x+"Y EKSENİ:"+mousePoint.y);
            if(mousePoint.x<0){
               mousePoint.x=0;
           }
           if(mousePoint.y<0){
               mousePoint.y=0;
           }
           if(mousePoint.y>900){
               mousePoint.y=900;
           }
           if(mousePoint.x>1600){
               mousePoint.x=1600;
           }
             Robot konum=new Robot();
           konum.mouseMove((int)mousePoint.x,(int)mousePoint.y);
            //Şuan tıklama olayını yazıyorum
           //ama doğrudan tıklayamazsın bir noktaya tıklama yapmak için
           //o noktanın kordinatının belirli bir süre aynı kalması lazım veya çok çok değişmesi lazım
          
           //ilk olarak static arraylist oluştur oluşturdum "sag_tıklanma_noktalari"
          //bu fonksiyona her girmesi bir harekete karşılık geliyorsa her 
          // hareket bir kordinatsa bu kordinatları ekliyeceğim
          cift_tıklanma_noktalari.add(mousePoint);
          cift_tıklama_yap(cift_tıklanma_noktalari);
          } catch (Exception e) {
              System.err.println(e.getMessage());
          }
    }  
      
       private static void cift_tıklama_yap(ArrayList<Point> cift_tıklanma_kordinatları) {
          try {
              if(cift_tıklanma_kordinatları.size()>1)
              {
                  int mask=InputEvent.BUTTON1_DOWN_MASK;
                String soundname="C:\\Users\\Mert\\Desktop\\cift.wav";
                Robot yerdegistir=new Robot(); 
                for(int i=cift_aradegisken;i<cift_tıklanma_kordinatları.size();i++){
                    if((int)cift_tıklanma_kordinatları.get(i).x==(int)cift_tıklanma_kordinatları.get(i+1).x &&(int)cift_tıklanma_kordinatları.get(i).y==(int)cift_tıklanma_kordinatları.get(i+1).y){
                        AudioInputStream audioInputStream=AudioSystem.getAudioInputStream(new File(soundname).getAbsoluteFile());
                        Clip clip=AudioSystem.getClip();
                        clip.open(audioInputStream);
                        clip.start();
                        yerdegistir.mousePress(mask);
                        yerdegistir.mouseRelease(mask);
                        yerdegistir.mousePress(mask);
                        yerdegistir.mouseRelease(mask);
                        yerdegistir.delay(500);
                        //clip.stop();
                        cift_tiklandimi=1;
                        break;
                    }
                    else if(Math.abs((int)cift_tıklanma_kordinatları.get(i).x-(int)cift_tıklanma_kordinatları.get(i+1).x)<=1 &&Math.abs((int)cift_tıklanma_kordinatları.get(i).y-(int)cift_tıklanma_kordinatları.get(i+1).y)<=1){
                       AudioInputStream audioInputStream=AudioSystem.getAudioInputStream(new File(soundname).getAbsoluteFile());
                        Clip clip=AudioSystem.getClip();
                        clip.open(audioInputStream);
                        clip.start(); 
                        yerdegistir.mousePress(mask);
                        yerdegistir.mouseRelease(mask);
                         yerdegistir.mousePress(mask);
                        yerdegistir.mouseRelease(mask);
                        yerdegistir.delay(500);
                        //clip.stop();
                        cift_tiklandimi=1;
                        break;
                    }
                    break;
              }
                cift_aradegisken++;
          }
          } catch(ArrayIndexOutOfBoundsException ex1){
              System.err.println(ex1.getMessage());
          }
          catch (Exception e) {
              System.err.println(e.getMessage());
          }
    }
      
      // PROJE TAMAMEN BİTMİŞTİR.
}