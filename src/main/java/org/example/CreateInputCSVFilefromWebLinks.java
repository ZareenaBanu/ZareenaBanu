
package org.example;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.WaitUntilState;

public class CreateInputCSVFilefromWebLinks {

	public static void main( String[] args )
    {
		
		String OPfileName = "C:\\Users\\P128DEF\\OneDrive - Ceridian HCM Inc\\MyFolder\\Zar-Per\\TJ\\Auto\\PropLinksConversion\\NowProp-InputFile.csv";
		File file = new File(OPfileName);
		final String ipFilename ="C:\\Users\\P128DEF\\OneDrive - Ceridian HCM Inc\\MyFolder\\Zar-Per\\TJ\\Auto\\PropLinksConversion\\Input-PropLinks.csv";
		String records = "_propertyName,_typeOfAddress,_typeofBuyers,_suburbGroup,_ABOfficeUrl\n";
		PrintWriter pw = null;
		List<String> content;
        
		
		try(Playwright playwright = Playwright.create()) {
			 pw = new PrintWriter(file);
    		 BrowserType WebKit = playwright.webkit();
			 BrowserContext context2 = WebKit.launchPersistentContext(Paths.get(""), new BrowserType.LaunchPersistentContextOptions()
			 .setTimeout(0).setHeadless(false) .setIgnoreHTTPSErrors(true)  );
			 Page page = context2.newPage();
			 StringBuilder builder = new StringBuilder();         	
 			 content = Files.readAllLines(Paths.get(ipFilename));
            

			try {
			for(int i = 1; i<content.size()-1; i++) {
				
				String currentRow[] = content.get(i).split(",");		
				page.route("**/*.{png,jpg,jpeg,svg,fli,flc}", route -> route.abort());
	    	    page.navigate(currentRow[0],new Page.NavigateOptions().setWaitUntil(WaitUntilState.NETWORKIDLE).setTimeout(0));
				
				
				//String[] rec = page.title().split("-")[0].trim().split(",");
				String[] rec = page.title().trim().split(",");

				System.out.println("\n\n"+currentRow[0]);
				System.out.println(page.title());
				System.out.println("Prpoerty: " +rec[0]+","+rec[1]);
					
	    	    if(currentRow[1].equals("PB")) {
	    	    	records += rec[0] + "," + "Listings,PB," + rec[1].trim() + ",WNWH\n";
	    	    }
					
	    	    if(currentRow[1].equals("MB")) {
					
	    	    	records += rec[0] + "," + "Listings,MB," + rec[1].trim() + ",WNWH\n";
	    	    	records += rec[0] + "," + "Properties,MB," + rec[1].trim() + ",WNWH\n";
					
	    	    }
					
	    	    if(currentRow[1].equals("PBMB")) {
	    	    	records += rec[0] + "," + "Listings,PB," + rec[1].trim() + ",WNWH\n";
	    	    	records += rec[0] + "," + "Listings,MB," + rec[1].trim() + ",WNWH\n";
	    	    	records += rec[0] + "," + "Properties,MB," + rec[1].trim() + ",WNWH\n";
	    	    }
											
			
		}
			
		}
		finally{
			builder.append(records +"\n");
			pw.write(builder.toString());
			pw.close();
			System.out.println("data has been written!"); }
			
	}	
		 
	  catch (IOException e) {
		  // TODO Auto-generated catch block
		  e.printStackTrace();
	  }   
	 

  }
			
}
