/*
 *****************************************************
 *                                                   *
 * ! IMPORTANT ! DO NOT DELETE COMMENT ! IMPORTANT ! *
 *                                                   *
 *****************************************************
 *                                                   *
 *            THIS CODE IS RELEASE READY.            *
 *                                                   *
 *       THIS CODE HAS BEEN TESTED HEAVILY AND       *
 *       CONSIDERED STABLE. THIS MODULE HAS NO       *
 *       KNOWN ISSUES. CONSIDERED RELEASE READY      *
 *                                                   *
 *****************************************************
 */

package API.Tools;

import java.io.*;
import API.*;

/**
 * Program to read files written by the user
 *
 * <br>
 * @author Deepak Anil Kumar (DAK404)
 * @version 1.0.0
 * @since 09-May-2020
 * <p>
 * *** Technical Details ***<br>
 * - Module Name       : Mosaic: API_S04<BR>
 * - Module Version    : 1.7.0<BR>
 * - Module Author     : Deepak Anil Kumar (DAK404), Bebhin Mathew<BR></p>
 */
public final class textEdit {	
    private String message = "";
    private String User    = "";
	private File file = null;
	
    Console console = System.console();
    API.Information info = new API.Information();

	BufferedWriter obj = null;
	PrintWriter pr = null;
	
    public textEdit()
	{
		
	}
	
	
    public void editScript(String user, String dir) throws Exception
	{
		try
		{
			User=user;
			file = new File(dir+console.readLine("Enter the name of file to be saved: "));
			writeContent();
			return;
		}
		catch(Exception E)
		{
			//E.printStackTrace();
		}
	}
	
    private void writeContent() throws Exception 
	{
        try 
		{
			boolean writeMode=true;
            if(file.exists()==true)
			{
				System.out.println("Do you want to overwrite the file or append to the file?\n[ Overwrite | Append ]");
				if(console.readLine().toLowerCase().equals("overwrite"))
					writeMode=false;
			}
			
            obj = new BufferedWriter(new FileWriter(file, writeMode));
            pr = new PrintWriter(obj);
            
			info.AboutProgram();
			
            System.out.println("Mosaic Text Editor 1.7");
			System.out.println("______________________\n\n");
			
			while(true)
			{
				message=console.readLine();
				
				//Keep receiving inputs until the user types <exit>
				if(message.equalsIgnoreCase("<exit>"))
					break;
				
				pr.println(message);
			}
        } catch (Exception E)
		{
            System.out.println("Error.");
            E.printStackTrace();
        } finally 
		{
            pr.close();
            obj.close();
			System.gc();
            return;
        }
    }
}