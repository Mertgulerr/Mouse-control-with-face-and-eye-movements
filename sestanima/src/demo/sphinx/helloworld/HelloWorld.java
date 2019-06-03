package demo.sphinx.helloworld;

import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;
import edu.cmu.sphinx.util.props.PropertyException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.speech.recognition.ResultToken;

public class HelloWorld {
    /**
     * Main method for running the HelloWorld demo.
     */
	static int i=1;
	static String resultText;
    public static void main(String[] args) {
        try {
            URL url;
            url = HelloWorld.class.getResource("helloworld.config.xml");
          
            System.out.println("Loading...");

            ConfigurationManager cm = new ConfigurationManager(url);

	    Recognizer recognizer = (Recognizer) cm.lookup("recognizer");
	    Microphone microphone = (Microphone) cm.lookup("microphone");


            /* allocate the resource necessary for the recognizer */
            recognizer.allocate();

            /* the microphone will keep recording until the program exits */
	    
            if (microphone.startRecording())
            {	
            	System.out.println("Say: (Command | Program | Chrome |  Device Manager | Calculater | Control | Task manager )");
            	System.out.println("Say: ( open word |open Access| start Excel )");
            while (true) 
            {
		   // System.out.println("Start speaking. Press Ctrl-C to quit.\n");
		    Result result = recognizer.recognize();
		    if (result != null)
		    {
		    	System.out.println("Enter your choise"+ "\n");
			 resultText = result.getBestFinalResultNoFiller();
			System.out.println("You said: " + resultText + "\n");
			
			if(resultText.equalsIgnoreCase("command"))
 			{
 				try{
 				Process p;
 				p = Runtime.getRuntime().exec("cmd /c start cmd");
 				}catch(Exception er)
 				{}
 			}
                                                  if (resultText.equalsIgnoreCase("close command"))
 		    {
 		        try{
 		        Process p;	//resultText="";
 		        p = Runtime.getRuntime().exec("cmd /c start taskkill /im cmd.exe /f");
 		     
 		        }catch(Exception ae){}
 		    } 			
 			if (resultText.equalsIgnoreCase("open calculator"))
 		    {
 		        try{
 		        Process p;	//resultText="";
 		        p = Runtime.getRuntime().exec("cmd /c calc.exe");
 		     
 		        }catch(Exception ae){}
 		    }	                       
 			else  if (resultText.equalsIgnoreCase("Program"))
 		    {
 		        try{
 		        Process p;
 		    	//resultText="";
 		        p = Runtime.getRuntime().exec("cmd /c start appwiz.cpl");
 		        }catch(Exception ae){}
 		    }
                                                     else if (resultText.equalsIgnoreCase("Control"))
 			    {
 			        try{
 			        Process p;
 			    //	resultText="";
 			        p = Runtime.getRuntime().exec("cmd /c control");
 			        }catch(Exception ae){}
 			    }
		        
 			
 			else if(resultText.equalsIgnoreCase("close calculator"))
 			{	 try{
                                                                        Process p;
 		    	//resultText="";
                                                                 p = Runtime.getRuntime().exec("cmd /c start taskkill /im calc.exe /f");
                                                                // System.out.println("inside");
 		        }catch(Exception ae){}
                                                    }
 	          //open chrome close chorome sıkıntı yok.		
                        else if (resultText.equalsIgnoreCase("Open Chrome"))
 		    {
 		        try{
 		        Process p;//	resultText="";
 		        p = Runtime.getRuntime().exec("cmd /c start chrome.exe");
 		       // System.out.println("inside");
 		        }catch(Exception ae){}
 		     }else if (resultText.equalsIgnoreCase("close Chrome"))
 			    {
 			        try{
 			        Process p;//	resultText="";
 			        p = Runtime.getRuntime().exec("cmd /c start taskkill /im chrome.exe /f");
 			       // System.out.println("inside");
 			        }catch(Exception ae){}
 			     }
                     
                     else if (resultText.equalsIgnoreCase("open Keyboard"))
 		    {
 		        try{
 		        Process p;//	resultText="";
 		        p = Runtime.getRuntime().exec("cmd /c start osk.exe");
 		       // System.out.println("inside");
 		        }catch(Exception ae){}
 		     }else if (resultText.equalsIgnoreCase("close Keyboard"))
 			    {
 			        try{
 			        Process p;//	resultText="";
 			        p = Runtime.getRuntime().exec("cmd /c start taskkill /im osk.exe /f");
 			       // System.out.println("inside");
 			        }catch(Exception ae){}
 			     }
 			else if(resultText.equalsIgnoreCase("Task manager"))
 				{	 try{
 			        Process p;
 			    	//resultText="";
 			        p = Runtime.getRuntime().exec("cmd /c start taskmgr.exe");
 			       // System.out.println("inside");
 			        }catch(Exception ae){}
 				}
 			
 			else if(resultText.equalsIgnoreCase("face book"))
 			{	 try{
 		        Process p;
 		    	//resultText="";
 		        p = Runtime.getRuntime().exec("cmd /c start chrome www.facebook.com");
 		       // System.out.println("inside");
 		        }catch(Exception ae){}
 			}
                        
                        else if(resultText.equalsIgnoreCase("school")){
                            try {
                                Process p;
                                p=Runtime.getRuntime().exec("cmd /c start chrome https://bilmuh.trakya.edu.tr/");
                            } catch (Exception ae) {}
                        }
 			 else if(resultText.equalsIgnoreCase("close task manager"))
 				{	 try{
 			        Process p;
 			    	//resultText="";
 			        p = Runtime.getRuntime().exec("cmd /c start taskkill /im taskmgr.exe /f");
 			       // System.out.println("inside");
 			        }catch(Exception ae){}
 		}
 			else if (resultText.equalsIgnoreCase("start text"))
 		    {
 		        try{
 		        Process p;	//resultText="";
 		        p = Runtime.getRuntime().exec("cmd /c start winword");
 		       // System.out.println("inside");
 		        }catch(Exception ae){}
 		     }
 			else if (resultText.equalsIgnoreCase("stop text"))
 		    {
 		        try{
 		        Process p;	//resultText="";
 		        p = Runtime.getRuntime().exec("cmd /c start taskkill /im winword.exe /f");
 		       // System.out.println("inside");
 		        }catch(Exception ae){}
 		     }
 			 						
 			else if (resultText.equalsIgnoreCase("start Excel"))
 		    {
 		        try{
 		        Process p;	//resultText="";
 		        p = Runtime.getRuntime().exec("cmd /c start excel");
 		       // System.out.println("inside");
 		        }catch(Exception ae){}
 		     }else if (resultText.equalsIgnoreCase("stop Excel"))
 			    {
 			        try{
 			        Process p;	//resultText="";
 			        p = Runtime.getRuntime().exec("cmd /c start taskkill /im excel.exe /f");
 			       // System.out.println("inside");
 			        }catch(Exception ae){}
 			     }
 			else if (resultText.equalsIgnoreCase("open access"))
 		    {
 		        try{
 		        Process p;	//resultText="";
 		        p = Runtime.getRuntime().exec("cmd /c start msaccess");
 		       // System.out.println("inside");
 		        }catch(Exception ae){}
 		     }
 			else if (resultText.equalsIgnoreCase("close access"))
 		    {
 		        try{
 		        Process p;	//resultText="";
 		        p = Runtime.getRuntime().exec("cmd /c start taskkill /im msaccess.exe /f");
 		       // System.out.println("inside");
 		        }catch(Exception ae){}
 		     }
 			else if(resultText.equalsIgnoreCase("See You Later"))
 		    {
 				try{
 		// şimdi is burada diğer işlemleri gerçekleştiriyoruz
                                                    // teameye projesini açması gerekecektir.
 			//recognizer.wait();
 					Process p;
                                                            String yol="C:\\Users\\Mert\\Documents\\NetBeansProjects\\teameye\\dist\\teameye.jar";
                                                            System.out.println("TeamEYE projesine geçiş yapılıyor.!!!");
                                                            p=Runtime.getRuntime().exec("cmd /c java -jar "+yol);
                                                             Runtime.getRuntime().exit(0);
 					}                              
 				catch(Exception estop ){}
 		    }
                        
 	     else if (resultText.equalsIgnoreCase("Device Manager"))
 			    {
 			        try{
 			        Process p;	//resultText="";
 			        p = Runtime.getRuntime().exec("cmd /c start devmgmt.msc");
 			       // System.out.println("inside");
 			        }catch(Exception ae){}
 			     }
             
                        else if (resultText.equalsIgnoreCase("what sapp"))
 		    {
 		        try{
 		        Process p;	//resultText="";
 		        p = Runtime.getRuntime().exec("cmd /c start chrome https://web.whatsapp.com/");
 		       // System.out.println("inside");
 		        }catch(Exception ae){}
 		     }
 		    }
		    
	    	else{
		System.out.println("I can't hear what you said.\n");
		    }
       }// while döngüüsünü kapatıyor.
	   }//baştaki ifi kapatıyor. 
            
        }// try i kapatıyor.
        catch(NullPointerException e){
             System.err.println("Problem when loading HelloWorld: " + e);
            e.printStackTrace();
        }
        catch (IOException e) {
            System.err.println("Problem when loading HelloWorld: " + e);
            e.printStackTrace();
        } catch (PropertyException e) {
            System.err.println("Problem configuring HelloWorld: " + e);
            e.printStackTrace();
        } catch (InstantiationException e) {
            System.err.println("Problem creating HelloWorld: " + e);
            e.printStackTrace();
        }
        
    }
}