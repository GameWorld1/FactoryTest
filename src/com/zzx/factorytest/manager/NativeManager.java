package com.zzx.factorytest.manager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class NativeManager {

	static {
		System.loadLibrary("factorytest");
	}
	
	/**
	 *
	 * @return
	 */
	 
    native static private int getCorrectResult();
	
	public static  boolean checkCorrect(){
		execMethod();
		return getCorrectResult()!=0;
		
	}
	
	public static  void execMethod(){
        String line ="";
        String args[] = new String[3];

        args[0] = "chmod";

        args[1] = "555";
        
        args[2] ="/data/nvram/md/NVRAM/CALIBRAT/MPA2_000";

        try
        {
         Process process = Runtime.getRuntime().exec(args);
 
         //get the err line

         InputStream stderr = process.getErrorStream();
         InputStreamReader isrerr = new InputStreamReader(stderr);
         BufferedReader brerr = new BufferedReader(isrerr);

         //get the output line  
         InputStream outs = process.getInputStream();
         InputStreamReader isrout = new InputStreamReader(outs);
         BufferedReader brout = new BufferedReader(isrout);

         String errline = null;

         String result = "";

         

         // get the whole error message string  while ( (line = brerr.readLine()) != null)
         {
          result += line;
          result += "\n";


         } 

         if( result != "" )

         {

          // put the result string on the screen

         }

         // get the whole standard output string

         while ( (line = brout.readLine()) != null)
         {
          result += line;
          result += "\n";
         }
         if( result != "" )
         {

          System.out.println(result);

         }

        }catch(Throwable t)
        {
         t.printStackTrace();
        }
}
}
