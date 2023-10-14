package org.example;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.FileChooser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.opencsv.exceptions.CsvValidationException;


public class copyCsvFiles {
  public static void main(String[] args) throws IOException, InterruptedException, CsvValidationException  {
   
    String _credsCsv="C:\\Users\\zareena.banu\\OneDrive - Ceridian HCM Inc\\MyFolder\\Zar-Per\\TJ\\Auto\\creds.csv";
    String _inputFileCsv="C:\\Users\\zareena.banu\\OneDrive - Ceridian HCM Inc\\MyFolder\\Zar-Per\\TJ\\Auto\\InputFile.csv";
    String _logFileName="OutputLogFile-"+((String) (java.time.LocalDate.now()+"-"+java.time.LocalTime.now()).subSequence(0, 19)).replace(":","-")+".txt";
    String _outputLogFile="C:\\Users\\zareena.banu\\OneDrive - Ceridian HCM Inc\\MyFolder\\Zar-Per\\TJ\\Auto\\"+_logFileName;
     String _destinationCSVFolder="";
    String _smsUrl="";
    String _smsUsername="";
    String _smsReloadUrl="";
    String _smsPassword="";  
    
    
    try {

      FileWriter myWriter = new FileWriter(_outputLogFile);
      myWriter.write((java.time.LocalDate.now()+" "+java.time.LocalTime.now()).subSequence(0, 19)+"\n-------------------------------------------------------------------\n");      
      myWriter.write((java.time.LocalDate.now()+" "+java.time.LocalTime.now()).subSequence(0, 19)+"  Starting the Log File\n");      
       
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(_credsCsv), "UTF8"))) {
      String[] lineInArray; 
      String tempStr="";
      while ((tempStr=reader.readLine()) != null) {
             lineInArray = tempStr.split(",");	  
	     switch (lineInArray[0]) {	 
              
          case "_destinationCSVFolder":
          _destinationCSVFolder=lineInArray[1];
          break;

          case "_smsUrl":
          _smsUrl=lineInArray[1];
          break;

          case "_smsUsername":
          _smsUsername=lineInArray[1];
          break;
    
         case "_smsPassword":
          _smsPassword=lineInArray[1];
          break;
        

         case "_smsReloadUrl":
         _smsReloadUrl=lineInArray[1];
          break;


                   
        }
      }  
  } 
 
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(_inputFileCsv), "UTF8"))) {
     
      try (Playwright playwright = Playwright.create()) { 

       BrowserType firefox = playwright.firefox();
        BrowserContext context1 = firefox.launchPersistentContext(Paths.get(""),
          new BrowserType.LaunchPersistentContextOptions()
            .setTimeout(60000).setHeadless(false)
            .setIgnoreHTTPSErrors(true)  );

         Page pagesms = context1.newPage();
         pagesms.navigate(_smsUrl); //Navigate to SMS pagesms
         Thread.sleep(3000);
         myWriter.write((java.time.LocalDate.now()+" "+java.time.LocalTime.now()).subSequence(0, 19)+"  ******************Starting the SMS part**************\n");    
    
      // Interact with SMS login form to provide Username & Password      
      pagesms.fill("input[name='username']", _smsUsername);
      pagesms.fill("input[name='password']", _smsPassword);
      pagesms.click("button[name='login']");

      pagesms.waitForLoadState();      
      myWriter.write((java.time.LocalDate.now()+" "+java.time.LocalTime.now()).subSequence(0, 19)+"  SMS pagesms Launched\n"); 
      
      while ((reader.readLine()) != null) {  
     

       String csvFileName = _destinationCSVFolder+".csv";  
    myWriter.write((java.time.LocalDate.now()+" "+java.time.LocalTime.now()).subSequence(0, 19)+"  Fetching Details from:"+_inputFileCsv+"\n");          
      
              
     
       
       FileWriter pw = new FileWriter(csvFileName);
       StringBuilder builder = new StringBuilder();      
       String headerList = "FirstName,Address,Number,Email,Home,Work,emailSubscription";                
       builder.append(headerList +"\n");
       String records = "Tariq Jameel,,0421250566,,,,\nZareena Banu,,0480120604,,,,\n";;    //                         
      
      
        builder.append(records);
         pw.write(builder.toString());
         pw.close();
         myWriter.write((java.time.LocalDate.now()+" "+java.time.LocalTime.now()).subSequence(0, 19)+"  Copied data to : " +csvFileName+"\n");
         
      //SMS STart
         ///////////////////////////////     
         pagesms.navigate(_smsReloadUrl); 
        
      pagesms.click("li[data-test='menu-contacts']");  //Go to Contacts            
      pagesms.click("a[data-test='contact-groups']");  //Go to Contact Groups
      
     /* pagesms.click("button[name='new-group']");          
      
      pagesms.fill("input[id='name']", csvFileName);
      pagesms.click("button[name='modal-addEditGroup']");
      myWriter.write((java.time.LocalDate.now()+" "+java.time.LocalTime.now()).subSequence(0, 19)+"  Created Contact Group :"+csvFileName+"\n");
      Thread.sleep(5000); 

      pagesms.click("input[name='contactGroupsFilter']");
      pagesms.fill("input[name='contactGroupsFilter']", csvFileName);     
      Thread.sleep(3000);   
      pagesms.keyboard().press("Enter");  
      Thread.sleep(3000); */
      
      com.microsoft.playwright.Locator tdsmsCGSearch=pagesms.locator("td");      
      System.out.println(pagesms.locator("tr").count()); 
      System.out.println(tdsmsCGSearch.nth(0).innerText()); 
      System.out.println(tdsmsCGSearch.nth(pagesms.locator("tr").count()).innerText()); 

            
      for (int i=0;i<pagesms.locator("tr").count();i++){ 

       if (tdsmsCGSearch.nth(i).innerText().equals(csvFileName)){  
          myWriter.write((java.time.LocalDate.now()+" "+java.time.LocalTime.now()).subSequence(0, 19)+"  Trying to click :" + tdsmsCGSearch.nth(i).innerText()+"\n");
          tdsmsCGSearch.nth(i).click();
          Thread.sleep(3000);        
          pagesms.click("button[name='import-contacts']"); 

         FileChooser fileChooser = pagesms.waitForFileChooser(() -> {
          pagesms.click("span[class='k2tz3']"); //Click on Browse          
         });

        
         fileChooser.setFiles(Paths.get(csvFileName));
         Thread.sleep(3000);
 
 
      //Number
     pagesms.click("div[name='columnMapping.mobilenumber']");
     pagesms.keyboard().press("ArrowDown");  
     pagesms.click("[data-test='select-columnMapping[mobilenumber]-number']");   
    

         myWriter.write((java.time.LocalDate.now()+" "+java.time.LocalTime.now()).subSequence(0, 19)+"  Uploaded the CSV file: "+csvFileName+"\n"); 
         pagesms.click("button[name='import']"); //Click on Import  
         Thread.sleep(5000);
         myWriter.write((java.time.LocalDate.now()+" "+java.time.LocalTime.now()).subSequence(0, 19)+"  Contacts imported via the CSV file.\n");    
         pagesms.click("button[name='modal-importContactsResult']");    
         myWriter.write((java.time.LocalDate.now()+" "+java.time.LocalTime.now()).subSequence(0, 19)+"  All done for : "+csvFileName+"\n");
         myWriter.write((java.time.LocalDate.now()+" "+java.time.LocalTime.now()).subSequence(0, 19)+"  -------------------------------------------------------------------\n");
         
       }
       
      }
      //////////////////////////////////// 
   // SMS End*/ 
      
    }  
    //Playwright Try Ends here 
    }  
   } 
   myWriter.close();
  }
  catch (IOException e) {
    System.out.println("An error occurred.");
    e.printStackTrace();
  }
  } 
}