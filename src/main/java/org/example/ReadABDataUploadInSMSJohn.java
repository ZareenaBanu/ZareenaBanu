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


public class ReadABDataUploadInSMSJohn {
  public static void main(String[] args) throws IOException, InterruptedException, CsvValidationException  {
   
    String _credsCsv="C:\\Users\\P128DEF\\OneDrive - Ceridian HCM Inc\\MyFolder\\Zar-Per\\TJ\\Auto\\creds.csv";
    String _inputFileCsv="C:\\Users\\P128DEF\\OneDrive - Ceridian HCM Inc\\MyFolder\\Zar-Per\\TJ\\Auto\\InputFile.csv";

    String _propUrlWNWH="";//Read from _credsCsv File
    String _propUsernameWNWH="";
    String _propPasswordWNWH="";
    String _propReloadUrlWNWH="";
    String _propReloadUrlJohn="";
    String _propUsernameJohn="";
    String _propPasswordJohn="";
    String _propUrlCompany ="";//Read from _credsCsv File
    String _propUsernameCompany ="";
    String _propPasswordCompany ="";
    String _propReloadUrlCompany="";    
    String _destinationCSVFolder="";
    String _smsUrl="";
    String _smsUsername="";
    String _smsReloadUrl="";
    String _smsPassword="";  
    String _typeOfAddress="";//Read from _inputFileCsv File
    String _typeofBuyers="";
    String _propertyName="";
    String _propertyNameSuburbGroup="";
    String _suburbGroup="";
    String _ABOfficeUrl="";
    
   // try {

     // FileWriter myWriter = new FileWriter(_outputLogFile);
      
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(_credsCsv), "UTF8"))) {
      String[] lineInArray; 
      String tempStr="";
      while ((tempStr=reader.readLine()) != null) {
             lineInArray = tempStr.split(",");	  
	     switch (lineInArray[0]) {	 
          case "_propUrlWNWH":
          _propUrlWNWH=lineInArray[1];
          break;

          case "_propUsernameWNWH":
          _propUsernameWNWH=lineInArray[1];
          break;

          case "_propPasswordWNWH":
          _propPasswordWNWH=lineInArray[1];
          break;     
                         
          case "_propReloadUrlJohn":
          _propReloadUrlJohn=lineInArray[1];
          break;

          case "_propUsernameJohn":
          _propUsernameJohn=lineInArray[1];
          break;

          case "_propPasswordJohn":
          _propPasswordJohn=lineInArray[1];
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
        

         case "_smsReloadUrl":
         _smsReloadUrl=lineInArray[1];
          break;


          case "_propReloadUrlWNWH":
          _propReloadUrlWNWH=lineInArray[1];
          break;
    
         case "_propUrlCompany":
         _propUrlCompany=lineInArray[1];
          break;

          case "_propUsernameCompany":
          _propUsernameCompany=lineInArray[1];
          break;

          case "_propPasswordCompany":
          _propPasswordCompany=lineInArray[1];
          break;
    
         case "_propReloadUrlCompany":
         _propReloadUrlCompany=lineInArray[1];
          break;         
          
        }
      }  
  } 
 
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(_inputFileCsv), "UTF8"))) {
      String[] lineInArray; 
      String tempStr="";
      try (Playwright playwright = Playwright.create()) { 

        BrowserType chromium = playwright.chromium();
        Browser browser =  chromium.launch(new BrowserType.LaunchOptions().setHeadless(false).setChannel("chrome").setTimeout(1200000).setExecutablePath(Paths.get("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe")));
        BrowserContext context = browser.newContext(); 
        Page  page  = browser.newPage();  
        page.route("**/*.{png,jpg,jpeg,svg,fli,flc}", route -> route.abort());
    
        Browser browserWNWH =  chromium.launch(new BrowserType.LaunchOptions().setHeadless(false).setChannel("chrome").setTimeout(1200000).setExecutablePath(Paths.get("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe")));
        BrowserContext contextWNWH = browserWNWH.newContext();  
        
        chromium.launch(new BrowserType.LaunchOptions().setHeadless(false).setChannel("chrome").setTimeout(1200000).setExecutablePath(Paths.get("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe")));
        BrowserContext contextJohn = browserWNWH.newContext();  

       
        Page pageWNWHPage = contextWNWH.newPage(); 
        pageWNWHPage.route("**/*.{png,jpg,jpeg,svg,fli,flc}", route -> route.abort());

        Page pageJohnPage = contextJohn.newPage(); 
        pageJohnPage.route("**/*.{png,jpg,jpeg,svg,fli,flc}", route -> route.abort());

        //For Tariq

        pageWNWHPage.navigate(_propReloadUrlWNWH);  
        pageWNWHPage.click("text=Login"); // Interact with login form ptovide Username & Password
        pageWNWHPage.fill("input[name='_username']", _propUsernameWNWH);
        pageWNWHPage.fill("input[name='_password']", _propPasswordWNWH);
        pageWNWHPage.click("input[value='Login']");
        pageWNWHPage.waitForLoadState();

         
//for John
        pageJohnPage.navigate(_propReloadUrlJohn);  
        pageJohnPage.click("text=Login"); // Interact with login form ptovide Username & Password
        pageJohnPage.fill("input[name='_username']", _propUsernameJohn);
        pageJohnPage.fill("input[name='_password']", _propPasswordJohn);
        pageJohnPage.click("input[value='Login']");
        pageJohnPage.waitForLoadState();

        BrowserType firefox = playwright.firefox();
        BrowserContext context1 = firefox.launchPersistentContext(Paths.get(""),
          new BrowserType.LaunchPersistentContextOptions()
            .setTimeout(60000).setHeadless(false)
            .setIgnoreHTTPSErrors(true)  );

      /*      BrowserContext contextCompany = firefox.launchPersistentContext(Paths.get(""),
            new BrowserType.LaunchPersistentContextOptions()
              .setTimeout(60000).setHeadless(false)
              .setIgnoreHTTPSErrors(true)  ); */

         Page pagesms = context1.newPage();
         pagesms.route("**/*.{png,jpg,jpeg,svg,fli,flc}", route -> route.abort());
          pagesms.navigate(_smsUrl); //Navigate to SMS pagesms
         Thread.sleep(3000);
         
      // Interact with SMS login form to provide Username & Password      
    pagesms.fill("input[name='username']", _smsUsername);
      pagesms.fill("input[name='password']", _smsPassword);
      pagesms.click("button[name='login']");  

      pagesms.waitForLoadState();    
      
      while ((tempStr=reader.readLine()) != null) {

        _typeOfAddress="";
        _typeofBuyers="";
        _propertyName="";
        _suburbGroup="";
        _propertyNameSuburbGroup="";
        _ABOfficeUrl="";
        String _propReload="";
        
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
      if (lineInArray.length>=5)       
      _ABOfficeUrl=lineInArray[4];

      if (_ABOfficeUrl.equalsIgnoreCase("Company")){
        _propReload=_propReloadUrlCompany; 
        
      }
        else if (_ABOfficeUrl.equalsIgnoreCase("WNWH")){
        _propReload=_propReloadUrlWNWH; 
         page=pageWNWHPage;     } 

         else if (_ABOfficeUrl.equalsIgnoreCase("JOHN")){
        _propReload=_propReloadUrlJohn; 
         page=pageJohnPage;     } 
            
        else 
         System.out.println("Do Nothing");
    
  
    
     String csvFilePropName;

    if (_suburbGroup=="")
     csvFilePropName= (_propertyName.replace("/","-")+" - "+_typeOfAddress+" - "+_typeofBuyers+" - "+((String) (java.time.LocalDate.now()+"-"+java.time.LocalTime.now()).subSequence(0, 19)).replace(":","-"));   
    else 
    csvFilePropName  = _suburbGroup+" - "+(_propertyName.replace("/","-")+" - "+_typeOfAddress+" - "+_typeofBuyers+" - "+((String) (java.time.LocalDate.now()+"-"+java.time.LocalTime.now()).subSequence(0, 19)).replace(":","-"));
    
    if (_ABOfficeUrl.equalsIgnoreCase("JOHN"))
        csvFilePropName="JOHN "+csvFilePropName; 
    
    String csvFileName = _destinationCSVFolder+csvFilePropName+".csv";  
   
      page.navigate(_propReload); 
     //alert banner notification
   //page.locator("xpath=/html/body/div[4]/div[1]/div/div[2]/p/a/i").click(); -older
     // page.click("a[class='banner-close']");
      Thread.sleep(3000);
      // Add suburb to Poperty Address
      _propertyNameSuburbGroup = _propertyName + ", " + _suburbGroup;
      //newUI
      page.click("span[class='unisearch-pro-label']"); //Click on Search Icon
      page.click("input[id='unisearch-pro-search']");
      page.fill("input[id='unisearch-pro-search']", _propertyNameSuburbGroup);
      page.keyboard().press("Enter")  ; 
      Thread.sleep(3000);
     
      switch (_typeOfAddress) {

        case "Listings":
        page.click("li[data-type='listings']");
        page.locator("xpath=/html/body/div[27]/div[1]/div[2]/div/div/form/div[2]/div[2]/div[3]/div[2]/div[1]/img").click();
        break;

        case "Properties":
        page.click("li[data-type='properties']");
        page.locator("xpath=/html/body/div[27]/div[1]/div[2]/div/div/form/div[2]/div[2]/div[5]/div[2]").click();   
        break;

        case "Projects":
        page.click("a[id='projects']");
        page.click("li[data-type='projects']");
        page.locator("xpath=/html/body/div[27]/div[1]/div[2]/div/div/form/div[2]/div[2]/div[3]/div[2]/div[1]/img").click();
        break;
      
        default:
        page.click("li[data-type='listings']");
        page.locator("xpath=/html/body/div[27]/div[1]/div[2]/div/div/form/div[2]/div[2]/div[3]/div[2]/div[1]/img").click();
        break;

      }
      
      
         
      String concatString= "text=" + _propertyNameSuburbGroup;
     // page.click(concatString); //Click on Property Name

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
      
      Page basicContactListPage ;
      /*if (_ABOfficeUrl.equalsIgnoreCase("Company")){
      basicContactListPage = contextCompany.waitForPage(() -> {
         
          }); /
          
         } 
         
         else */
         if (_ABOfficeUrl.equalsIgnoreCase("WNWH")){
          basicContactListPage = contextWNWH.waitForPage(() -> {
            pageWNWHPage.click("text=Basic Contact/Call List");  });
            
           } 

      else if (_ABOfficeUrl.equalsIgnoreCase("JOHN")){
          basicContactListPage = contextJohn.waitForPage(() -> {
            pageJohnPage.click("text=Basic Contact/Call List");  });
            
           } 

      else 
      basicContactListPage = context.newPage();

        
          
      basicContactListPage.waitForLoadState(); //Waiting for Contact lists to be loaded
      Thread.sleep(5000); 
       
       FileWriter pw = new FileWriter(csvFileName);
       StringBuilder builder = new StringBuilder();      
       String headerList = "FirstName,Address,Number,Email,Home,Work,emailSubscription";                
       builder.append(headerList +"\n");
       String   records;

       records = "Tariq Jameel,,0421250566,,,,\nZareena Banu,,0480120604,,,,\n";;
        
       //if (_ABOfficeUrl.equalsIgnoreCase("JOHN"))
       // records = "John Irudayaraj,,0433969731,,,,\\n";       
       //else
       // records = "Tariq Jameel,,0421250566,,,,\nZareena Banu,,0480120604,,,,\n";;    //  

       com.microsoft.playwright.Locator locatortd=basicContactListPage.locator("td");         
       
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
          basicContactListPage.close();
      //SMS STart
         ///////////////////////////////     
         pagesms.navigate(_smsReloadUrl); 
        
      pagesms.click("li[data-test='menu-contacts']");  //Go to Contacts            
      pagesms.click("a[data-test='contact-groups']");  //Go to Contact Groups
      
      pagesms.click("button[name='new-group']");          
      
      pagesms.fill("input[id='name']", csvFilePropName);
      pagesms.click("button[name='modal-addEditGroup']");
     
      Thread.sleep(5000); 

      pagesms.click("input[name='contactGroupsFilter']");
      pagesms.fill("input[name='contactGroupsFilter']", csvFilePropName);     
      Thread.sleep(3000);   
      pagesms.keyboard().press("Enter");  
      Thread.sleep(3000);
      
      com.microsoft.playwright.Locator tdsmsCGSearch=pagesms.locator("td");       
            
      for (int i=0;i<pagesms.locator("tr").count();i++){ 

       if (tdsmsCGSearch.nth(i).innerText().equals(csvFilePropName)){  
           tdsmsCGSearch.nth(i).click();
          Thread.sleep(3000);        
          pagesms.click("button[name='import-contacts']"); 

         FileChooser fileChooser = pagesms.waitForFileChooser(() -> {
         pagesms.click("span[class='f09uz']"); //Click on Browse  
         //pagesms.click("p[data-test='paragraph-undefined']");        
         });

        
         fileChooser.setFiles(Paths.get(csvFileName));
         Thread.sleep(3000);
 
 
      //Number
     pagesms.click("div[name='columnMapping.mobilenumber']");
     pagesms.keyboard().press("ArrowDown");  
     pagesms.click("[data-test='select-columnMapping[mobilenumber]-number']");   
   

    
        
         pagesms.click("button[name='import']"); //Click on Import  
         Thread.sleep(5000);
         pagesms.click("button[name='modal-importContactsResult']");    
         
       }
       
      }
      //////////////////////////////////// 
   // SMS End*/ 
      
    }  
    //Playwright Try Ends here 
    }  
   } 
   
  //}
  
  } 
}