package com.certificate.PDFGenerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;


public class CSVHelper {
  public static String TYPE = "text/csv";
  static String[] HEADERs = { "Id", "Name", "College", "Course", "EventName", "Project", "StreamName", "StartDate", "EndDate" };

  public static boolean hasCSVFormat(MultipartFile file) {
    if (TYPE.equals(file.getContentType())
    		|| file.getContentType().equals("application/vnd.ms-excel")) {
      return true;
    }

    return false;
  }

  public static List<Student> csvToTutorials(InputStream is) {
    try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        CSVParser csvParser = new CSVParser(fileReader,
            CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

      List<Student> studentList = new ArrayList<>();

      Iterable<CSVRecord> csvRecords = csvParser.getRecords();

      for (CSVRecord csvRecord : csvRecords) {
    	  Student student = new Student(
              Long.parseLong(csvRecord.get("Id")),
              csvRecord.get("Name"),
              csvRecord.get("College"),
              csvRecord.get("Course"),
              csvRecord.get("EventName"),
              csvRecord.get("Project"),
              csvRecord.get("StreamName"),
              csvRecord.get("StartDate"),
              csvRecord.get("EndDate")
            );

    	  studentList.add(student);
      }

      return studentList;
    } catch (IOException e) {
      throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
    }
  }

}
