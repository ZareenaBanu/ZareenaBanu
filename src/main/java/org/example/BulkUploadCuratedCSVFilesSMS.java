package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.FileChooser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class BulkUploadCuratedCSVFilesSMS {

public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException, IOException, InterruptedException {
        // Specify the path of the folder you want to scan
        String curatedFilesPath = "C:\\Users\\P128DEF\\OneDrive - Ceridian HCM Inc\\MyFolder\\Zar-Per\\TJ\\SMS\\curatedCSVFilesToUploadInSMS";
        String _credsCsv="C:\\Users\\P128DEF\\OneDrive - Ceridian HCM Inc\\MyFolder\\Zar-Per\\TJ\\Auto\\creds.csv";
        String _smsUrl="";
        String _smsUsername="";
        String _smsReloadUrl="";
        String _smsPassword=""; 

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(_credsCsv), "UTF8"))) {
        String[] lineInArray; 
        String tempStr="";
            while ((tempStr=reader.readLine()) != null) {
                    lineInArray = tempStr.split(",");	  
                switch (lineInArray[0]) {	 
                        
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

        try (Playwright playwright = Playwright.create()) { 

                
                    BrowserType firefox = playwright.firefox();
                        BrowserContext context1 = firefox.launchPersistentContext(Paths.get(""),
                        new BrowserType.LaunchPersistentContextOptions()
                            .setTimeout(99000).setHeadless(false)
                            .setIgnoreHTTPSErrors(true)  );

                        Page pagesms = context1.newPage();
                        pagesms.route("**/*.{png,jpg,jpeg,svg,fli,flc}", route -> route.abort());
                        pagesms.navigate(_smsUrl); //Navigate to SMS pagesms
                        Thread.sleep(4000);
                        
                        
                    // Interact with SMS login form to provide Username & Password      
                    pagesms.fill("input[name='username']", _smsUsername);
                    pagesms.fill("input[name='password']", _smsPassword);
                    pagesms.click("button[name='login']");  

                    pagesms.waitForLoadState();  
                    Thread.sleep(9000);
                    //SMS STart
                 File folder = new File(curatedFilesPath);     
                 if (folder.isDirectory()) {
                 // Get list of files and subdirectories in the folder
                 File[] files = folder.listFiles();

                  if (files != null) {
                    // Iterate through files and directories
                    for (File file : files) {
                    // Check if it's a CSV file

                    if (file.isFile() && file.getName().toLowerCase().endsWith(".csv")) {
                        // Print the CSV file name
                    System.out.println(file.getName());
                    String CsvFileFullPath= curatedFilesPath + "\\" + file.getName();
                    String CsvFileNameinSMS= file.getName();
                    CsvFileNameinSMS=CsvFileNameinSMS.substring(0, CsvFileNameinSMS.length()-4);
                    System.out.println(CsvFileFullPath);
                    System.out.println(CsvFileNameinSMS);
                    pagesms.navigate(_smsReloadUrl);      
                    Thread.sleep(3000);                   
                    pagesms.click("li[data-test='menu-contacts']");  //Go to Contacts            
                    pagesms.click("a[data-test='contact-groups']");  //Go to Contact Groups                    
                    pagesms.click("button[name='new-group']");   
                    
                    pagesms.fill("input[id='name']", CsvFileNameinSMS);
                    pagesms.click("button[name='modal-addEditGroup']");
                    
                    Thread.sleep(3000); 

                    pagesms.click("input[name='contactGroupsFilter']");
                    pagesms.fill("input[name='contactGroupsFilter']", CsvFileNameinSMS);     
                    Thread.sleep(3000);   
                    pagesms.keyboard().press("Enter");  
                    Thread.sleep(3000);
                    
                    com.microsoft.playwright.Locator tdsmsCGSearch=pagesms.locator("td");       
                            
                    for (int i=0;i<pagesms.locator("tr").count();i++){ 

                    if (tdsmsCGSearch.nth(i).innerText().equals(CsvFileNameinSMS)){  
                        tdsmsCGSearch.nth(i).click();
                        Thread.sleep(3000);        
                        pagesms.click("button[name='import-contacts']"); 

                        FileChooser fileChooser = pagesms.waitForFileChooser(() -> {
                        pagesms.click("span[class='f09uz']"); //Click on Browse  
                        //pagesms.click("p[data-test='paragraph-undefined']");        
                        });

                        
                        fileChooser.setFiles(Paths.get(CsvFileFullPath));
                        Thread.sleep(9000);
                
                
                    //Number
                   // pagesms.click("div[name='columnMapping.mobilenumber']");
                    Thread.sleep(5000);
                    //pagesms.keyboard().press("ArrowDown");  
                    //pagesms.click("[data-test='select-columnMapping[Phone number]-number']");   
                

                    
                        
                        pagesms.click("button[name='import']"); //Click on Import  
                        Thread.sleep(5000);
                        pagesms.click("button[name='modal-importContactsResult']");    
                        
                    }
                    
                    }
                    
                // SMS End*/     
                    }
                }
            }
        } else {
            System.out.println("Invalid folder path.");
        }

                         
            }

         
    printCSVFileNames(curatedFilesPath);

}

public static void printCSVFileNames(String folderPath) {
        

        // Check if the provided path is a directory
        
    }
}