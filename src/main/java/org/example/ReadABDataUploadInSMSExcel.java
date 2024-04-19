package org.example;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
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
import org.apache.poi.xssf.usermodel.*;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;


public class ReadABDataUploadInSMSExcel {
  public static void main(String[] args) throws IOException, InterruptedException, CsvValidationException  {
   
    String _credsCsv="C:\\Users\\P128DEF\\OneDrive - Ceridian HCM Inc\\MyFolder\\Zar-Per\\TJ\\Auto\\creds.csv";
    String _inputFileCsv="C:\\Users\\P128DEF\\OneDrive - Ceridian HCM Inc\\MyFolder\\Zar-Per\\TJ\\Auto\\InputFile.csv";
    String _propUsernameWNWH="";
    String _propPasswordWNWH="";
    String _propReloadUrlWNWH="";
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
    String csvFilePropName="";

  try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(_credsCsv), "UTF8"))) {
      String[] lineInArray; 
      String tempStr="";
      while ((tempStr=reader.readLine()) != null) {
             lineInArray = tempStr.split(",");	  
	     switch (lineInArray[0]) {	 
          
        case "_propUsernameWNWH":
          _propUsernameWNWH=lineInArray[1];
          break;

          case "_propPasswordWNWH":
          _propPasswordWNWH=lineInArray[1];
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
        Browser browser =  chromium.launch(new BrowserType.LaunchOptions().setHeadless(false).setChannel("chrome").setTimeout(1200000).setExecutablePath(Paths.get("")));//"C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe")
        BrowserContext context = browser.newContext(); 
        Page  page  = browser.newPage();  
        page.route("**/*.{png,jpg,jpeg,svg,fli,flc}", route -> route.abort());
    
        Browser browserWNWH =  chromium.launch(new BrowserType.LaunchOptions().setHeadless(false).setChannel("chrome").setTimeout(1200000).setExecutablePath(Paths.get("")));//C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe
        BrowserContext contextWNWH = browserWNWH.newContext();          
            
        Page pageWNWHPage = contextWNWH.newPage(); 
        pageWNWHPage.route("**/*.{png,jpg,jpeg,svg,fli,flc}", route -> route.abort());

       //For Tariq

        pageWNWHPage.navigate(_propReloadUrlWNWH);  
        Thread.sleep(3000);
        pageWNWHPage.click("text=Login"); // Interact with login form ptovide Username & Password
        pageWNWHPage.fill("input[name='_username']", _propUsernameWNWH);
        pageWNWHPage.fill("input[name='_password']", _propPasswordWNWH);
        pageWNWHPage.click("input[value='Login']");
        pageWNWHPage.waitForLoadState();

   
        BrowserType firefox = playwright.firefox();
        BrowserContext context1 = firefox.launchPersistentContext(Paths.get(""),
          new BrowserType.LaunchPersistentContextOptions()
            .setTimeout(60000).setHeadless(false)
            .setIgnoreHTTPSErrors(true) 
            .setBaseURL(_smsUrl) );
          

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

                  
        else 
         System.out.println("Do Nothing");
    
  

    if (_suburbGroup=="")
     csvFilePropName= (_propertyName.replace("/","-")+" - "+_typeOfAddress+" - "+_typeofBuyers+" - "+((String) (java.time.LocalDate.now()+"-"+java.time.LocalTime.now()).subSequence(0, 19)).replace(":","-"));   
    else 
    csvFilePropName  = _suburbGroup+" - "+(_propertyName.replace("/","-")+" - "+_typeOfAddress+" - "+_typeofBuyers+" - "+((String) (java.time.LocalDate.now()+"-"+java.time.LocalTime.now()).subSequence(0, 19)).replace(":","-"));
    
    if (_ABOfficeUrl.equalsIgnoreCase("JOHN"))
        csvFilePropName="JOHN "+csvFilePropName; 
    
    String csvFileName = _destinationCSVFolder+csvFilePropName+".csv";
    String xlsxFileName = _destinationCSVFolder+csvFilePropName+".xlsx";  
      page.navigate(_propReload);    
                 //alert banner notification
 page.locator("xpath=/html/body/div[4]/div[1]/div/div[2]/p/a/i").click(); 
   //page.click("a[class='banner-close']");
      Thread.sleep(3000);
      // Add suburb to Poperty Address
      _propertyNameSuburbGroup = _propertyName + ", " + _suburbGroup;
  
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
     
         if (_ABOfficeUrl.equalsIgnoreCase("WNWH")){
          basicContactListPage = contextWNWH.waitForPage(() -> 
          {
            pageWNWHPage.click("text=Basic Contact/Call List");
            } );
            
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
     
       try{  // Convert CSV files to XLS/XLSX format in Java
       
           XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("sheet1");
            XSSFFont xssfFont = workbook.createFont();
            xssfFont.setCharSet(XSSFFont.ANSI_CHARSET);
            XSSFCellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setFont(xssfFont);
            String currentLine;
            int RowNum = -1;
            BufferedReader br = new BufferedReader(new FileReader(csvFileName));//testcsvFilePropName  csvFileName
            while((currentLine = br.readLine()) != null){
                String[] str = currentLine.split(",");
                RowNum++;
                XSSFRow currentRow = sheet.createRow(RowNum);
                for(int i=0; i< str.length; i++){
                    str[i] = str[i].replaceAll("\"","");
                    str[i] = str[i].replaceAll("=","");
                    XSSFCell cell = currentRow.createCell(i);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(str[i].trim());
                }
            }
            FileOutputStream fileOutputStream = new FileOutputStream(xlsxFileName);
            workbook.write(fileOutputStream);
            fileOutputStream.close();
            br.close();            
            workbook.close();    
        }catch (Exception e){
            e.printStackTrace();
        }
      
        
   
      //SMS STart
         ///////////////////////////////     
         pagesms.navigate(_smsReloadUrl); 
        
       pagesms.click("li[data-test='menu-contacts']");  //Go to Contacts            
         pagesms.click("a[data-test='contact-groups-v2']");  //Go to Lists
         pagesms.click("button:has-text(\"New list\")"); //Click on New List
         Thread.sleep(2000);  
         pagesms.fill("input[class='sc-gplwa-d Knjej']", csvFilePropName ); //csvFilePropName   
         Thread.sleep(2000); 
         pagesms.click("button:has-text(\"Create list\")"); //Click on create list button
         Thread.sleep(2000); 
         
        pagesms.click("input[class='sc-gplwa-d kYLkcC']"); // Click on Search input 
        pagesms.fill("input[class='sc-gplwa-d kYLkcC']",csvFilePropName ); //csvFilePropName  
        Thread.sleep(2000);  
        pagesms.keyboard().press("Enter"); 
        Thread.sleep(3000);  
      
          
         
       com.microsoft.playwright.Locator tdsmsCGSearch=pagesms.locator("td");       
               
         for (int i=0;i<pagesms.locator("tr").count();i++){ 
   
          if (tdsmsCGSearch.nth(i).innerText().equals(csvFilePropName)){  //csvFilePropName
              tdsmsCGSearch.nth(i).click();
             Thread.sleep(3000);  
           
             
             pagesms.click("button[class='sc-dtBdUo dVHTXp']"); //Click on Import
                  
            FileChooser fileChooser = pagesms.waitForFileChooser(() -> {
             pagesms.click("button:has-text(\"Browse\")"); //Click on Browse
            });
           
           fileChooser.setFiles(Paths.get(xlsxFileName));
               
           Thread.sleep(2000);
          
           
           pagesms.click("button[class='sc-dtBdUo dVHTXp']"); //Click on Import  
          
           Thread.sleep(2000);
           pagesms.click("button:has-text(\"Import contacts\")");
        
           Thread.sleep(2000);
          }
        }     
         }
         
      // SMS End*/ 
      
    }  
    //Playwright Try Ends here 
    }  
  
   
  } 
}