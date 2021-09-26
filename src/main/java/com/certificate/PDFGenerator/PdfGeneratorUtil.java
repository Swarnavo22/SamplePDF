package com.certificate.PDFGenerator;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

@Component
public class PdfGeneratorUtil {
    @Autowired
    private TemplateEngine templateEngine;


    private String convertToBase64(Path path) {
        byte[] imageAsBytes = new byte[0];
        try {
            Resource resource = new UrlResource(path.toUri());
            InputStream inputStream = resource.getInputStream();
            imageAsBytes = inputStream.readAllBytes();
            System.out.println(imageAsBytes.toString());
        } catch (IOException e) {
            System.out.println("\n File read Exception");
        }

        return Base64.getEncoder().encodeToString(imageAsBytes);
    }

    public void createPdf(String templateName, Map map) throws Exception {

        Assert.notNull(templateName, "The templateName can not be null");
        Context ctx = new Context();
        if (map != null) {
            Iterator itMap = map.entrySet().iterator();
            while (itMap.hasNext()) {
                Map.Entry pair = (Map.Entry) itMap.next();
                ctx.setVariable(pair.getKey().toString(), pair.getValue());
            }
        }

        String processedHtml = templateEngine.process(templateName, ctx);
        FileOutputStream os = null;
        String fileName = UUID.randomUUID().toString();
        try {
            File outputFile = new File("dd.pdf");
            outputFile.createNewFile();// File(fileName, ".pdf");
            os = new FileOutputStream(outputFile);
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(processedHtml);
            renderer.layout();
            renderer.createPDF(os,false);
            renderer.finishPDF();
            System.out.println("PDF created successfully");
        }
        finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) { /*ignore*/ }
            }
        }
    }
}
