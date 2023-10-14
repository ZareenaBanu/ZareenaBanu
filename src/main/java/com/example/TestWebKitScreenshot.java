package com.example;
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


public class TestWebKitScreenshot {
  public static void main(String[] args) throws IOException, InterruptedException, CsvValidationException  {
   
    String _credsCsv="C:\\Users\\zareena.banu\\OneDrive - Ceridian HCM Inc\\MyFolder\\Zar-Per\\TJ\\Auto\\creds.csv";
    String _inputFileCsv="C:\\Users\\zareena.banu\\OneDrive - Ceridian HCM Inc\\MyFolder\\Zar-Per\\TJ\\Auto\\InputFile.csv";
    String _logFileName="OutputLogFile-"+((String) (java.time.LocalDate.now()+"-"+java.time.LocalTime.now()).subSequence(0, 19)).replace(":","-")+".txt";
    String _outputLogFile="C:\\Users\\zareena.banu\\OneDrive - Ceridian HCM Inc\\MyFolder\\Zar-Per\\TJ\\Auto\\"+_logFileName;
    //Read from _credsCsv File
    String _propUrl="";
    String _propUsername="";
    String _propPassword="";
    String _destinationCSVFolder="";
    String _smsUrl="";
    String _smsUsername="";
    String _smsPassword="";    
    //Read from _inputFileCsv File
    String _typeOfAddress="";
    String _typeofBuyers="";
    String _propertyName="";

    try {

      FileWriter myWriter = new FileWriter(_outputLogFile);
      myWriter.write((java.time.LocalDate.now()+" "+java.time.LocalTime.now()).subSequence(0, 19)+"\n-------------------------------------------------------------------\n");      
      myWriter.write((java.time.LocalDate.now()+" "+java.time.LocalTime.now()).subSequence(0, 19)+"  Starting the Log File\n");      
       
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(_credsCsv), "UTF8"))) {
      String[] lineInArray; 
      String tempStr="";
      while ((tempStr=reader.readLine()) != null) {
      // System.out.println(tempStr);
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
        }
      }  
  } 
    
   try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(_inputFileCsv), "UTF8"))) {
      String[] lineInArray; 
      String tempStr="";
      while ((tempStr=reader.readLine()) != null) {
        lineInArray = tempStr.split(",");  
        if (lineInArray[0].compareTo("_propertyName")==0)
          continue;   
      
      _propertyName=lineInArray[0];
      _typeOfAddress=lineInArray[1];
      _typeofBuyers=lineInArray[2];           
     
      myWriter.write((java.time.LocalDate.now()+" "+java.time.LocalTime.now()).subSequence(0, 19)+"  Extracting Data for:\n");
      myWriter.write((java.time.LocalDate.now()+" "+java.time.LocalTime.now()).subSequence(0, 19)+"        "+_propertyName+"\n");
      myWriter.write((java.time.LocalDate.now()+" "+java.time.LocalTime.now()).subSequence(0, 19)+"        "+_typeOfAddress+"\n");
      myWriter.write((java.time.LocalDate.now()+" "+java.time.LocalTime.now()).subSequence(0, 19)+"        "+_typeofBuyers+"\n");
       
    try (Playwright playwright = Playwright.create()) { 

    Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));//.setSlowMo(50));

    String csvFilePropName= (_propertyName.replace("/","-")+" - "+_typeOfAddress+" - "+_typeofBuyers+" - "+java.time.LocalDate.now()).replace("  "," ");
    String csvFileName = _destinationCSVFolder+csvFilePropName+".csv";  
    myWriter.write((java.time.LocalDate.now()+" "+java.time.LocalTime.now()).subSequence(0, 19)+"  Fetching Details from:"+_inputFileCsv+"\n");
  
    BrowserContext context = browser.newContext();
    Page page = context.newPage(); 
  	/*  page.navigate(_propUrl);
    
    myWriter.write((java.time.LocalDate.now()+" "+java.time.LocalTime.now()).subSequence(0, 19)+"  Starting the extraction.\n");    

    
    // Interact with login form ptovide Username & Password
      page.click("text=Login");
      page.fill("input[name='_username']", _propUsername);
      page.fill("input[name='_password']", _propPassword);
      page.click("input[value='Login']");
      page.waitForLoadState();
      
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
      // Get page after a specific action (e.g. clicking a link)
      Page basicContactListPage = context.waitForPage(() -> {
      page.click("text=Basic Contact/Call List");  });  //Basic Contact/Call List     
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
        
       */
      myWriter.write((java.time.LocalDate.now()+" "+java.time.LocalTime.now()).subSequence(0, 19)+"  ******************Starting the SMS part**************\n");    
      page.navigate(_smsUrl); //Navigate to SMS page
      Thread.sleep(3000);
    
      // Interact with SMS login form to provide Username & Password      
      page.fill("input[name='username']", _smsUsername);
      page.fill("input[name='password']", _smsPassword);
      page.click("button[name='login']");

      page.waitForLoadState();      
      myWriter.write((java.time.LocalDate.now()+" "+java.time.LocalTime.now()).subSequence(0, 19)+"  SMS Page Launched\n"); 
      page.click("li[data-test='menu-contacts']");  //Go to Contacts            
      page.click("a[data-test='contact-groups']");  //Go to Contact Groups
      
      page.click("button[name='new-group']");          
      
      page.fill("input[id='name']", csvFilePropName);
      page.click("button[name='modal-addEditGroup']");
      myWriter.write((java.time.LocalDate.now()+" "+java.time.LocalTime.now()).subSequence(0, 19)+"  Created Contact Group :"+csvFilePropName+"\n");
      Thread.sleep(5000); 

      page.click("input[name='contactGroupsFilter']");
      page.fill("input[name='contactGroupsFilter']", csvFilePropName);     
      Thread.sleep(3000);   
      page.keyboard().press("Enter");  
      Thread.sleep(3000);
      
      com.microsoft.playwright.Locator tdsmsCGSearch=page.locator("td");         
            
      for (int i=0;i<page.locator("tr").count();i++){ 

       if (tdsmsCGSearch.nth(i).innerText().equals(csvFilePropName)){  
          myWriter.write((java.time.LocalDate.now()+" "+java.time.LocalTime.now()).subSequence(0, 19)+"  Trying to click :" + tdsmsCGSearch.nth(i).innerText()+"\n");
          tdsmsCGSearch.nth(i).click();
          Thread.sleep(3000);        
          page.click("button[name='import-contacts']"); 

         FileChooser fileChooser = page.waitForFileChooser(() -> {
          page.click("span[class='k2tz3']"); //Click on Browse          
         });

        fileChooser.setFiles(Paths.get(csvFileName));
        Thread.sleep(3000);


     //Number
		page.click("div[name='columnMapping.mobilenumber']");
    page.keyboard().press("ArrowDown");  
		page.click("div[data-test='select-columnMapping[mobilenumber]-number']");
             

    //First Name
    page.click("div[name='columnMapping.firstname']");
    page.keyboard().press("ArrowDown");  
    page.click("div[data-test='select-columnMapping[firstname]-firstname']");
             //div[@data-test='select-columnMapping[firstname]-firstname']//div[1]  (//div[text()='FirstName'])[2]
    
   
    
    //email
    page.click("div[name='columnMapping.emailaddress']");
    page.click("div[data-test='select-columnMapping[emailaddress]-email']");

    //location
    page.click("div[name='columnMapping.location']");
    page.click("div[data-test='select-columnMapping[location]-address']");
    //div[@data-test='select-columnMapping[location]-address']
    //page.click("//span[title='Address']");

    //Custom Field 1
    page.click("div[name='columnMapping.customField1']");
    //page.click("//span[title='emailSubscription'");
    page.click("div[data-test='select-columnMapping[customField1]-emailsubscription']");

    //Custom Field 2
    page.click("div[name='columnMapping.customField2']");
    //page.click("//span[title='Home'");
    page.click("div[data-test='select-columnMapping[customField2]-home']");
    
    //Custom Field 3
    page.click("div[name='columnMapping.customField3']");
    //page.click("//span[title='Work'");
    page.click("div[data-test='select-columnMapping[customField3]-work']");

		       
        myWriter.write((java.time.LocalDate.now()+" "+java.time.LocalTime.now()).subSequence(0, 19)+"  Uploaded the CSV file: "+csvFileName+"\n"); 
        page.click("button[name='import']"); //Click on Import  
        Thread.sleep(5000);
        myWriter.write((java.time.LocalDate.now()+" "+java.time.LocalTime.now()).subSequence(0, 19)+"  Contacts imported via the CSV file.\n");    
        page.click("button[name='modal-importContactsResult']");    
        myWriter.write((java.time.LocalDate.now()+" "+java.time.LocalTime.now()).subSequence(0, 19)+"  All done for : "+csvFilePropName+"\n");
       }
      }
    }   
   
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
