package org.example;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.FileChooser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.opencsv.exceptions.CsvValidationException;


public class TestOpenHomes {
  public static void main(String[] args) throws IOException, InterruptedException, CsvValidationException  {
   
    String _credsCsv="C:\\Users\\zareena.banu\\OneDrive - Ceridian HCM Inc\\MyFolder\\Zar-Per\\TJ\\Auto\\creds.csv";
    String _inputFileCsv="C:\\Users\\zareena.banu\\OneDrive - Ceridian HCM Inc\\MyFolder\\Zar-Per\\TJ\\Auto\\InputFile.csv";
    String _logFileName="OutputLogFile-"+((String) (java.time.LocalDate.now()+"-"+java.time.LocalTime.now()).subSequence(0, 19)).replace(":","-")+".txt";
    String _outputLogFile="C:\\Users\\zareena.banu\\OneDrive - Ceridian HCM Inc\\MyFolder\\Zar-Per\\TJ\\Auto\\"+_logFileName;
    String _propUrl="";//Read from _credsCsv File
    String _propUsername="";
    String _propPassword="";
    String _propReloadUrl="";
    String _destinationCSVFolder="";
    String _smsUrl="";
    String _smsUsername="";
    String _smsReloadUrl="";
    String _smsPassword="";  
    String _typeOfAddress="";//Read from _inputFileCsv File
    String _typeofBuyers="";
    String _propertyName="";
    String _suburbGroup="";
    
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
          case "_propUrl":
          _propUrl=lineInArray[1];
          break;

          case "_propUsername":
          _propUsername=lineInArray[1];
          break;

          case "_propPassword":
          _propPassword=lineInArray[1];
          break;
    
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

          case "_propReloadUrl":
          _propReloadUrl=lineInArray[1];
          break;
    
         case "_smsReloadUrl":
         _smsReloadUrl=lineInArray[1];
          break;
        }
      }  
  } 
 
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(_inputFileCsv), "UTF8"))) {
      String[] lineInArray; 
      String tempStr="";

      try (Playwright playwright = Playwright.create()) { 

        BrowserType chromium = playwright.chromium();
        Browser browser =  chromium.launch(new BrowserType.LaunchOptions().setHeadless(false).setChannel("chrome").setExecutablePath(Paths.get("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe")));
        BrowserContext context = browser.newContext();  
        Page page = context.newPage();

        page.navigate(_propUrl); 
    
        myWriter.write((java.time.LocalDate.now()+" "+java.time.LocalTime.now()).subSequence(0, 19)+"  Starting the extraction.\n");       
        
          page.click("text=Login"); // Interact with login form ptovide Username & Password
          page.fill("input[name='_username']", _propUsername);
          page.fill("input[name='_password']", _propPassword);
          page.click("input[value='Login']");
          page.waitForLoadState();
        // //div[text()='Today 17, February, 2022']
       // page.click("\'Today 17, February, 2022\'");
          System.out.println(page.innerText("\'Today 17, February, 2022\'"));
        //  page.click("div:has-text('Today')"); 
          //System.out.print(page.innerText("div:has-text('Today')"));

          page.click("a:has-text('Inspection (Open)')"); 
          System.out.print(page.innerText("a:has-text('Inspection (Open)')"));
          
        BrowserType firefox = playwright.firefox();
        BrowserContext context1 = firefox.launchPersistentContext(Paths.get(""),
          new BrowserType.LaunchPersistentContextOptions()
            .setHeadless(false)
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
      
      while ((tempStr=reader.readLine()) != null) {

        _typeOfAddress="";
        _typeofBuyers="";
        _propertyName="";
        _suburbGroup="";

        lineInArray = tempStr.split(",");  
        if (lineInArray[0].compareTo("_propertyName")==0)
          continue;   
      
      _propertyName=lineInArray[0];
      if (lineInArray.length>=2)       
        _typeOfAddress=lineInArray[1];
      if (lineInArray.length>=3)       
        _typeofBuyers=lineInArray[2]; 
      if (lineInArray.length>=4)       
        _suburbGroup=lineInArray[3];
     
      myWriter.write((java.time.LocalDate.now()+" "+java.time.LocalTime.now()).subSequence(0, 19)+"  Extracting Data for:\n");
      myWriter.write((java.time.LocalDate.now()+" "+java.time.LocalTime.now()).subSequence(0, 19)+"        "+_propertyName+"\n");
      myWriter.write((java.time.LocalDate.now()+" "+java.time.LocalTime.now()).subSequence(0, 19)+"        "+_typeOfAddress+"\n");
      myWriter.write((java.time.LocalDate.now()+" "+java.time.LocalTime.now()).subSequence(0, 19)+"        "+_typeofBuyers+"\n");
      myWriter.write((java.time.LocalDate.now()+" "+java.time.LocalTime.now()).subSequence(0, 19)+"        "+_suburbGroup+"\n");
       
  
    
     String csvFilePropName;

    if (_suburbGroup=="")
     csvFilePropName= (_propertyName.replace("/","-")+" - "+_typeOfAddress+" - "+_typeofBuyers+" - "+((String) (java.time.LocalDate.now()+"-"+java.time.LocalTime.now()).subSequence(0, 19)).replace(":","-"));   
    else 
    csvFilePropName  = _suburbGroup+" - "+(_propertyName.replace("/","-")+" - "+_typeOfAddress+" - "+_typeofBuyers+" - "+((String) (java.time.LocalDate.now()+"-"+java.time.LocalTime.now()).subSequence(0, 19)).replace(":","-"));
    
    
    String csvFileName = _destinationCSVFolder+csvFilePropName+".csv";  
    myWriter.write((java.time.LocalDate.now()+" "+java.time.LocalTime.now()).subSequence(0, 19)+"  Fetching Details from:"+_inputFileCsv+"\n"); 
   
    page.navigate(_propReloadUrl); 
      
      myWriter.write((java.time.LocalDate.now()+" "+java.time.LocalTime.now()).subSequence(0, 19)+"  Page Launched.\n");    
      Thread.sleep(3000);
      
      switch (_typeOfAddress) {

        case "Listings":
        page.click("a[id='listingtab']");
        page.click("input[id='universalsearchp']");   
        myWriter.write((java.time.LocalDate.now()+" "+java.time.LocalTime.now()).subSequence(0, 19)+"  Clicked : "+_typeOfAddress+"\n");
        page.fill("input[id='universalsearchp']", _propertyName);
        break;

        case "Properties":
        page.click("a[id='proptab']");
        page.click("input[id='universalsearchprop']");    
        myWriter.write((java.time.LocalDate.now()+" "+java.time.LocalTime.now()).subSequence(0, 19)+"  Clicked : "+_typeOfAddress+"\n");
        page.fill("input[id='universalsearchprop']", _propertyName);
        break;

        case "Projects":
        page.click("a[id='projecttab']");
        myWriter.write((java.time.LocalDate.now()+" "+java.time.LocalTime.now()).subSequence(0, 19)+"  Clicked : "+_typeOfAddress+"\n");
        page.fill("input[id='universalsearchj']", _propertyName);
        page.fill("input[id='universalsearchj']", _propertyName);
        break;
      
        default:
        page.click("a[id='listingtab']");
        page.click("input[id='universalsearchp']");
        myWriter.write((java.time.LocalDate.now()+" "+java.time.LocalTime.now()).subSequence(0, 19)+" Clicked : "+_typeOfAddress+"\n");
        page.fill("input[id='universalsearchp']", _propertyName);
        break;

      }
      
      myWriter.write((java.time.LocalDate.now()+" "+java.time.LocalTime.now()).subSequence(0, 19)+"  Typed : "+_propertyName+"\n");
      page.keyboard().press("ArrowDown");             
      String concatString= "text=" + _propertyName;
      page.click(concatString); //Click on Property Name
      myWriter.write((java.time.LocalDate.now()+" "+java.time.LocalTime.now()).subSequence(0, 19)+"  Clicked : "+_propertyName+"\n");

      switch (_typeofBuyers) {

        case "PB":
        page.click("h3:has-text(\"Prospective Buyers\")"); //Click on Prospective Buyers Link  
        page.click("input[value='Contacts / Follow Up']");      
        break;

        case "MB":   
          if (_typeOfAddress.compareTo("Listings")==0)        
           page.click("div[title='Match Buyers to this Property']"); //Click on Match Buyers to this Property 
          if (_typeOfAddress.compareTo("Properties")==0)        
           page.click("div[title='Match Buyers']"); //Click on Match Buyers
        break;
                
        default:         
        page.click("h3:has-text(\"Prospective Buyers\")"); //Click on Prospective Buyers Link        
        break;

      }    
      
      page.click("div[title='Print Contacts']"); //Print Contacts
      myWriter.write((java.time.LocalDate.now()+" "+java.time.LocalTime.now()).subSequence(0, 19)+" Clicked :  Print Contacts of "+_typeofBuyers+"\n");
      
      Page basicContactListPage = context.waitForPage(() -> {
      page.click("text=Basic Contact/Call List");  }); // Get page after a specific action (e.g. clicking a link)
      basicContactListPage.waitForLoadState(); //Waiting for Contact lists to be loaded
      myWriter.write((java.time.LocalDate.now()+" "+java.time.LocalTime.now()).subSequence(0, 19)+"  Launched : Basic Contact/Call List\n");
      Thread.sleep(5000); 
       
       FileWriter pw = new FileWriter(csvFileName);
       StringBuilder builder = new StringBuilder();      
       String headerList = "FirstName,Address,Number,Email,Home,Work,emailSubscription";                
       builder.append(headerList +"\n");
       String records = "Tariq Jameel,,0421250566,,,,\nZareena Banu,,0480120604,,,,\n";;    //                         
       com.microsoft.playwright.Locator locatortd=basicContactListPage.locator("td");         
       myWriter.write((java.time.LocalDate.now()+" "+java.time.LocalTime.now()).subSequence(0, 19)+"  Number of Contacts :" + (locatortd.count())/3+"\n");

       for(int i=3;i<locatortd.count();i++) {               
        if ((i+1)%3 != 0) {                  
           records+=locatortd.nth(i).innerText().replace("\n"," ").replace("\t"," ").replace(","," ").trim()+",";  }                             
        else {         
            String[] str = locatortd.nth(i).innerText().split("\\n");
            
            if (locatortd.nth(i).innerText().contains("[M]:")) {
              for(int j=0; j<str.length;j++) {
               if(str[j].contains("[M]:")) {
                String[] strmobile = str[j].split(":");
                records+=strmobile[1].replace("+61", "0").replace(" ","").trim()+",";           
                break;}
               }
            }
               else records+=",";

               if (locatortd.nth(i).innerText().contains("[E]:")) {
                for(int j=0; j<str.length;j++) {
                 if(str[j].contains("[E]:")) {
                  String[] stremail = str[j].split(":");
                  records+=stremail[1].replace(" ","").trim()+",";           
                  break;  }
                 }
               }
               else records+=",";

               if (locatortd.nth(i).innerText().contains("[H]:")) {  
              for(int j=0; j<str.length;j++) {              
                if(str[j].contains("[H]:")) {
                  String[] strmobile = str[j].split(":");
                  records+=strmobile[1].replace("+61", "0").replace(" ","").trim()+",";           
                  break; }
                }
               }
                else records+=",";

              if (locatortd.nth(i).innerText().contains("[W]:")) {   
                for(int j=0; j<str.length;j++) {             
                if(str[j].contains("[W]:")) {
                    String[] strmobile = str[j].split(":");
                    records+=strmobile[1].replace("+61", "0").replace(" ","").trim()+",";           
                    break;  }
                  }
              }
              else records+=",";

              if(locatortd.nth(i).innerText().contains("Do Not Email")){ 
                for(int j=0; j<str.length;j++) {             
                  if(str[j].contains("Do Not Email")) {
                    records+="Do Not Email";                                  
                  break;  
                 }
               }
              }
                else records+=",";
           records+="\n";                                                         
          }  
        }           
      
        builder.append(records);
         pw.write(builder.toString());
         pw.close();
         myWriter.write((java.time.LocalDate.now()+" "+java.time.LocalTime.now()).subSequence(0, 19)+"  Copied data to : " +csvFileName+"\n");
         basicContactListPage.close();
      //SMS STart
         ///////////////////////////////     
         pagesms.navigate(_smsReloadUrl); 
        
      pagesms.click("li[data-test='menu-contacts']");  //Go to Contacts            
      pagesms.click("a[data-test='contact-groups']");  //Go to Contact Groups
      
      pagesms.click("button[name='new-group']");          
      
      pagesms.fill("input[id='name']", csvFilePropName);
      pagesms.click("button[name='modal-addEditGroup']");
      myWriter.write((java.time.LocalDate.now()+" "+java.time.LocalTime.now()).subSequence(0, 19)+"  Created Contact Group :"+csvFilePropName+"\n");
      Thread.sleep(5000); 

      pagesms.click("input[name='contactGroupsFilter']");
      pagesms.fill("input[name='contactGroupsFilter']", csvFilePropName);     
      Thread.sleep(3000);   
      pagesms.keyboard().press("Enter");  
      Thread.sleep(3000);
      
      com.microsoft.playwright.Locator tdsmsCGSearch=pagesms.locator("td");       
            
      for (int i=0;i<pagesms.locator("tr").count();i++){ 

       if (tdsmsCGSearch.nth(i).innerText().equals(csvFilePropName)){  
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
     /*
     try {       
 
     //First Name
     pagesms.click("div[name='columnMapping.firstname']");
     pagesms.keyboard().press("ArrowDown");  
     pagesms.click("[data-test='select-columnMapping[firstname]-firstname']");           
    
     
     //email
     pagesms.click("div[name='columnMapping.emailaddress']");
     pagesms.keyboard().press("ArrowDown");  
     pagesms.click("[data-test='select-columnMapping[emailaddress]-email']");
 
     //location
     pagesms.click("div[name='columnMapping.location']");
     pagesms.keyboard().press("ArrowDown");  
     pagesms.click("[data-test='select-columnMapping[location]-address']");
      
     //Custom Field 1
     pagesms.click("div[name='columnMapping.customField1']");
     pagesms.keyboard().press("ArrowDown");  
     pagesms.click("[data-test='select-columnMapping[customField1]-emailsubscription']");
 
     //Custom Field 2
     pagesms.click("div[name='columnMapping.customField2']");
     pagesms.keyboard().press("ArrowDown");  
     pagesms.click("[data-test='select-columnMapping[customField2]-home']");
     
     //Custom Field 3
     pagesms.click("div[name='columnMapping.customField3']");
     pagesms.keyboard().press("ArrowDown");  
     pagesms.click("[data-test='select-columnMapping[customField3]-work']");
 
          }

         
     catch (Exception e) {       
      System.out.println("SMS Broadcast other sttributes error:\n"+e);
      myWriter.write((java.time.LocalDate.now()+" "+java.time.LocalTime.now()).subSequence(0, 19)+"  Error in mapping CSV attributes"); 
     } */

         myWriter.write((java.time.LocalDate.now()+" "+java.time.LocalTime.now()).subSequence(0, 19)+"  Uploaded the CSV file: "+csvFileName+"\n"); 
         pagesms.click("button[name='import']"); //Click on Import  
         Thread.sleep(5000);
         myWriter.write((java.time.LocalDate.now()+" "+java.time.LocalTime.now()).subSequence(0, 19)+"  Contacts imported via the CSV file.\n");    
         pagesms.click("button[name='modal-importContactsResult']");    
         myWriter.write((java.time.LocalDate.now()+" "+java.time.LocalTime.now()).subSequence(0, 19)+"  All done for : "+csvFilePropName+"\n");
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