package org.example;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.FileChooser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.poi.xssf.usermodel.*;

public class ReadABContactSuburbUploadinSMSExcelJohn {
  public static void main(String[] args) throws IOException, InterruptedException, CsvValidationException {

    String _credsCsv = "C:\\Users\\P128DEF\\OneDrive - Ceridian HCM Inc\\MyFolder\\Zar-Per\\TJ\\Auto\\creds.csv";
    String _inputFileABSuburbs = "C:\\Users\\P128DEF\\OneDrive - Ceridian HCM Inc\\MyFolder\\Zar-Per\\TJ\\Auto\\InputFile-ABSuburbsJ.csv";
  //  String _propUsernameWNWH = "";
 //   String _propPasswordWNWH = "";
 //   String _propReloadUrlWNWH = "";
    //String _propReloadUrlCompany = "";
    String _destinationCSVFolder = "";
    String _propReloadUrlJohn="";
    String _propUsernameJohn="";
    String _propPasswordJohn="";
    String _smsUrl = "";
    String _smsUsername = "";
    String _smsReloadUrl = "";
    String _smsPassword = "";
    // String _suburbGroup="";
    String csvFilePropName = "";
    boolean _headlessmodeAB = true;
    boolean _headlessmodeSMS = true;

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(_credsCsv), "UTF8"))) {
      String[] lineInArray;
      String tempStr = "";
      while ((tempStr = reader.readLine()) != null) {
        lineInArray = tempStr.split(",");
        switch (lineInArray[0]) {

         /*  case "_propUsernameWNWH":
            _propUsernameWNWH = lineInArray[1];
            break;

          case "_propPasswordWNWH":
            _propPasswordWNWH = lineInArray[1];
            break;

            case "_propReloadUrlWNWH":
            _propReloadUrlWNWH = lineInArray[1];
            break;

          case "_propReloadUrlCompany":
            _propReloadUrlCompany = lineInArray[1];
            break; */

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
            _destinationCSVFolder = lineInArray[1];
            break;

          case "_smsUrl":
            _smsUrl = lineInArray[1];
            break;

          case "_smsUsername":
            _smsUsername = lineInArray[1];
            break;

          case "_smsPassword":
            _smsPassword = lineInArray[1];
            break;

          case "_smsReloadUrl":
            _smsReloadUrl = lineInArray[1];
            break;
        }
      }
    }

    try (Playwright playwright = Playwright.create()) {

      BrowserType chromium = playwright.chromium();
      Browser browser = chromium.launch(new BrowserType.LaunchOptions().setHeadless(_headlessmodeAB).setChannel("chrome")
          .setTimeout(1200000).setExecutablePath(Paths.get("")));// "C:\\Program Files
                                                                 // (x86)\\Google\\Chrome\\Application\\chrome.exe")
      BrowserContext context = browser.newContext();
      Page page = browser.newPage();
      page.route("**/*.{png,jpg,jpeg,svg,fli,flc}", route -> route.abort());

      Browser browserWNWH = chromium.launch(new BrowserType.LaunchOptions().setHeadless(_headlessmodeAB).setChannel("chrome")
          .setTimeout(1200000).setExecutablePath(Paths.get("")));// C:\\Program Files
                                                                 // (x86)\\Google\\Chrome\\Application\\chrome.exe
      BrowserContext contextWNWH = browserWNWH.newContext();

      Page pageWNWHPage = contextWNWH.newPage();
      pageWNWHPage.route("**/*.{png,jpg,jpeg,svg,fli,flc}", route -> route.abort());

      // For Tariq

      pageWNWHPage.navigate(_propReloadUrlJohn);
      Thread.sleep(3000);
      pageWNWHPage.click("text=Login"); // Interact with login form ptovide Username & Password
      pageWNWHPage.fill("input[name='_username']", _propUsernameJohn);
      pageWNWHPage.fill("input[name='_password']", _propPasswordJohn);
      pageWNWHPage.click("input[value='Login']");
      pageWNWHPage.waitForLoadState();

      BrowserType firefox = playwright.firefox();
      BrowserContext context1 = firefox.launchPersistentContext(Paths.get(""),
          new BrowserType.LaunchPersistentContextOptions()
              .setTimeout(60000).setHeadless(_headlessmodeSMS)
              .setIgnoreHTTPSErrors(true)
              .setBaseURL(_smsUrl));

      Page pagesms = context1.newPage();
      pagesms.route("**/*.{png,jpg,jpeg,svg,fli,flc}", route -> route.abort());
      pagesms.navigate(_smsUrl); // Navigate to SMS pagesms
      Thread.sleep(3000);

      // Interact with SMS login form to provide Username & Password
      pagesms.fill("input[name='username']", _smsUsername);
      pagesms.fill("input[name='password']", _smsPassword);
      pagesms.click("button[name='login']");

      pagesms.waitForLoadState();

      try (BufferedReader reader = new BufferedReader(
          new InputStreamReader(new FileInputStream(_inputFileABSuburbs), "UTF8"))) {
        String[] lineInArray;
        String tempStr = "";

        while ((tempStr = reader.readLine()) != null) {

          String _suburbGroup = "";
          String _propReload = "";
          lineInArray = tempStr.split(",");
          if (lineInArray[0].compareTo("_suburbGroup") == 0)
            continue;
          _suburbGroup = lineInArray[0].trim();
          System.out.println(_suburbGroup);
          csvFilePropName = "John Contacts Suburb - " + _suburbGroup + " - "
              + (((String) (java.time.LocalDate.now() + "-" + java.time.LocalTime.now()).subSequence(0, 19))
                  .replace(":", "-"));

          String csvFileName = _destinationCSVFolder + csvFilePropName + ".csv";
          String xlsxFileName = _destinationCSVFolder + csvFilePropName + ".xlsx";

          _propReload = _propReloadUrlJohn;
          page = pageWNWHPage;
          page.navigate(_propReload);
          // alert banner notification
          // page.locator("xpath=/html/body/div[4]/div[1]/div/div[2]/p/a/i").click();
          // page.click("a[class='banner-close']");
          Thread.sleep(9000);

          page.click("li[id='nav4']"); // Click on Contacts on footer
          page.click("span[class='advanced-search-btn']"); // Click on Advanced Search button
          page.click("input[id='element_4_1_0_0_7']"); // Click on Suburb Text box
          String[] strSplit = _suburbGroup.split("");

          // Now convert string into ArrayList
          ArrayList<String> strList = new ArrayList<String>(
          Arrays.asList(strSplit));
          int k = 0;
          int lengthOfChrArray = _suburbGroup.length();
          System.out.println(_suburbGroup);
          System.out.println("-------------------------------");
          List<ElementHandle>  arrayOfElements;
          // Now print the ArrayList
          for (String s : strList) {
            page.keyboard().press("End");            
            page.press("input[id='element_4_1_0_0_7']", s);
            Thread.sleep(1000);
            arrayOfElements = page.locator("li:has(span.acpr)").elementHandles();             
           if (k == lengthOfChrArray-2) {
              System.out.println( "Number of Elements Matched : "+arrayOfElements.size()); 

                for (int q=0;q<arrayOfElements.size();){
                 
                    String entireAddress = arrayOfElements.get(q).innerText().replace("\n","");
                    String ConcSuburbState []=entireAddress.split("2");               
                                  
                    if ( ConcSuburbState[0].trim().equals((_suburbGroup+"NSW")))                        
                        arrayOfElements.get(q).click();                                  
                    System.out.println("Entire Locator Text : " + entireAddress);
                    System.out.println("Concatenated Address String : " + ConcSuburbState[0].trim());
                    System.out.println("Print SubGroup plus NSW : "+_suburbGroup+"NSW");
                    System.out.println( ConcSuburbState[0].trim().equals((_suburbGroup+"NSW")));   
                    q++;
                    break;                      
                    }                    
                 System.out.println("***********************************************");
                Thread.sleep(3000);
                break;
                }
            k++;
          }

          page.click("input[id='element_4_1_0_2_8']"); // Click on Search
          Thread.sleep(12000);
          page.click("div[title='Print Contacts']"); // Print Contacts

          Page basicContactListPage;

          basicContactListPage = contextWNWH.waitForPage(() -> {
            pageWNWHPage.click("text=Basic Contact/Call List");
          });

          basicContactListPage.waitForLoadState(); // Waiting for Contact lists to be loaded
          Thread.sleep(12000);

          FileWriter pw = new FileWriter(csvFileName);
          StringBuilder builder = new StringBuilder();
          String headerList = "FirstName,Location,Number,Email,Home,Work,emailSubscription";
          builder.append(headerList + "\n");
          String records;

          records = "Tariq Jameel,,0421250566,,,,\nZareena Banu,,0480120604,,,,\n";
          ;

          com.microsoft.playwright.Locator locatortd = basicContactListPage.locator("td");

          for (int i = 3; i < locatortd.count(); i++) {
            if ((i + 1) % 3 != 0) {
              records += locatortd.nth(i).innerText().replace("\n", " ").replace("\t", " ").replace(",", " ").trim()
                  + ",";
            } else {
              String[] str = locatortd.nth(i).innerText().split("\\n");

              if (locatortd.nth(i).innerText().contains("[M]:")) {
                for (int j = 0; j < str.length; j++) {
                  if (str[j].contains("[M]:")) {
                    String[] strmobile = str[j].split(":");
                    records += strmobile[1].replace("+61", "0").replace(" ", "").trim() + ",";
                    break;
                  }
                }
              } else
                records += ",";

              if (locatortd.nth(i).innerText().contains("[E]:")) {
                for (int j = 0; j < str.length; j++) {
                  if (str[j].contains("[E]:")) {
                    String[] stremail = str[j].split(":");
                    records += stremail[1].replace(" ", "").trim() + ",";
                    break;
                  }
                }
              } else
                records += ",";

              if (locatortd.nth(i).innerText().contains("[H]:")) {
                for (int j = 0; j < str.length; j++) {
                  if (str[j].contains("[H]:")) {
                    String[] strmobile = str[j].split(":");
                    records += strmobile[1].replace("+61", "0").replace(" ", "").trim() + ",";
                    break;
                  }
                }
              } else
                records += ",";

              if (locatortd.nth(i).innerText().contains("[W]:")) {
                for (int j = 0; j < str.length; j++) {
                  if (str[j].contains("[W]:")) {
                    String[] strmobile = str[j].split(":");
                    records += strmobile[1].replace("+61", "0").replace(" ", "").trim() + ",";
                    break;
                  }
                }
              } else
                records += ",";

              if (locatortd.nth(i).innerText().contains("Do Not Email")) {
                for (int j = 0; j < str.length; j++) {
                  if (str[j].contains("Do Not Email")) {
                    records += "Do Not Email";
                    break;
                  }
                }
              } else
                records += ",";
              records += "\n";

            }
          }

          builder.append(records);
          pw.write(builder.toString());
          pw.close();
          basicContactListPage.close();

          try { // Convert CSV files to XLS/XLSX format in Java

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("sheet1");
            XSSFFont xssfFont = workbook.createFont();
            xssfFont.setCharSet(XSSFFont.ANSI_CHARSET);
            XSSFCellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setFont(xssfFont);
            String currentLine;
            int RowNum = -1;
            BufferedReader br = new BufferedReader(new FileReader(csvFileName));// testcsvFilePropName csvFileName
            while ((currentLine = br.readLine()) != null) {
              String[] str = currentLine.split(",");
              RowNum++;
              XSSFRow currentRow = sheet.createRow(RowNum);
              for (int i = 0; i < str.length; i++) {
                str[i] = str[i].replaceAll("\"", "");
                str[i] = str[i].replaceAll("=", "");
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
          } catch (Exception e) {
            e.printStackTrace();
          }
          System.out.println(_suburbGroup + "   :DONE Creating Excel File");
          // SMS STart
          ///////////////////////////////
          pagesms.navigate(_smsReloadUrl);

          pagesms.click("li[data-test='menu-contacts']"); // Go to Contacts
          pagesms.click("a[data-test='contact-groups-v2']"); // Go to Lists
          pagesms.click("button:has-text(\"New list\")"); // Click on New List
          Thread.sleep(2000);
          pagesms.fill("input[class='sc-gplwa-d Knjej']", csvFilePropName); // csvFilePropName
          Thread.sleep(2000);
          pagesms.click("button:has-text(\"Create list\")"); // Click on create list button
          Thread.sleep(2000);

          pagesms.click("input[class='sc-gplwa-d kYLkcC']"); // Click on Search input
          pagesms.fill("input[class='sc-gplwa-d kYLkcC']", csvFilePropName); // csvFilePropName
          Thread.sleep(2000);
          pagesms.keyboard().press("Enter");
          Thread.sleep(3000);

          com.microsoft.playwright.Locator tdsmsCGSearch = pagesms.locator("td");

          for (int i = 0; i < pagesms.locator("tr").count(); i++) {

            if (tdsmsCGSearch.nth(i).innerText().equals(csvFilePropName)) { // csvFilePropName
              tdsmsCGSearch.nth(i).click();
              Thread.sleep(8000);

              pagesms.click("button[class='sc-iHGNWf hOfDeg']"); // Click on Import

              FileChooser fileChooser = pagesms.waitForFileChooser(() -> {
                pagesms.click("button:has-text(\"Browse\")");
              });

              fileChooser.setFiles(Paths.get(xlsxFileName));

              Thread.sleep(5000);

              pagesms.click("button[class='sc-iHGNWf hOfDeg']"); // Click on Import
              Thread.sleep(2000);
              pagesms.click("button:has-text(\"Import contacts\")"); // Click on Import Contacts

              Thread.sleep(2000);
              System.out.println(_suburbGroup + "   :DONE Uploading in SMS\n________________________________________________________");
            }
          }
        }
      }
    }
   
    // SMS End*/
    // Playwright Try Ends here
  }
}
