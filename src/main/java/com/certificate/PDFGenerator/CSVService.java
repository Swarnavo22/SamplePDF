package com.certificate.PDFGenerator;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service 
@Transactional
public class CSVService {
  @Autowired
  StudentRepository repository;

  public void save(MultipartFile file) {
    try {
      List<Student> students = CSVHelper.csvToTutorials(file.getInputStream());
      repository.saveAll(students);
    } catch (IOException e) {
      throw new RuntimeException("fail to store csv data: " + e.getMessage());
    }
  }  
  public Student getStudent(long studentId){
		return repository.getOne(studentId);
	}
}