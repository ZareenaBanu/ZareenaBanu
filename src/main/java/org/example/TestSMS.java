package org.example;

import java.nio.file.Paths;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class TestSMS {
  public static void main(String[] args) throws InterruptedException {

    String _smsUrl="https://app.smsbroadcast.com.au/login";
    String _smsUsername="zareena.tester@gmail.com";
    String _smsPassword="Testing@123";  

    try (Playwright playwright = Playwright.create()) { 

      BrowserType firefox = playwright.firefox();
      BrowserContext context = firefox.launchPersistentContext(Paths.get(""),
       new BrowserType.LaunchPersistentContextOptions()
       .setHeadless(false)
       .setIgnoreHTTPSErrors(true)  );
          
      
         Page pagesms = context.newPage();
         pagesms.navigate(_smsUrl); //Navigate to SMS pagesms
        // pagesms.navigate(_smsUrl); //Navigate to SMS pagesms
         
         Thread.sleep(3000);        
    
         pagesms.fill("input[name='username']", _smsUsername);
         pagesms.fill("input[name='password']", _smsPassword);
         pagesms.click("button[name='login']");
         Thread.sleep(3000);     
         pagesms.close();

    }

  }
}