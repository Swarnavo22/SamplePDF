package com.certificate.PDFGenerator;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;


@Configuration
class ThymeleafConfiguration {
    @Bean
    public ClassLoaderTemplateResolver emailTemplateResolver(){
        ClassLoaderTemplateResolver emailTemplateResolver=new ClassLoaderTemplateResolver();
        emailTemplateResolver.setPrefix("templates/");
        emailTemplateResolver.setTemplateMode("HTML5");
        emailTemplateResolver.setSuffix(".html");
        emailTemplateResolver.setTemplateMode("XHTML");
        emailTemplateResolver.setCharacterEncoding("UTF-8");
        emailTemplateResolver.setOrder(1);
        return emailTemplateResolver;
    }
}

//@CrossOrigin("http://localhost:8080")
@Controller
@RequestMapping("/api/csv")
public class CSVController {

  @Autowired
  CSVService fileService;
  @Autowired
  PdfGeneratorUtil pdfGeneratorUtil;

  @PostMapping("/upload")
  public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
    String message = "";

    if (CSVHelper.hasCSVFormat(file)) {
      try {
        fileService.save(file);
        
        message = "Uploaded the file successfully: " + file.getOriginalFilename();
        
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message,""));
      } 
      catch (Exception e) {
        message = "Could not upload the file: " + file.getOriginalFilename() + "!";
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message,""));
      }
    }

    message = "Please upload a csv file!";
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message,""));
  }
  
  
  @RequestMapping(value = "/student" , method = RequestMethod.GET)
  public String template(Model m,@RequestParam("id") long studentId) throws Exception {	

	  Student obj = this.fileService.getStudent(studentId);
	  System.out.println(studentId);
	  System.out.println(obj);
	  m.addAttribute("student",obj);
	  m.addAttribute("standardDate", new Date());
	  System.out.println(m.getAttribute("student"));
      pdfGeneratorUtil.createPdf("template2",  obj.getData());
      return "template";

  }
 
}