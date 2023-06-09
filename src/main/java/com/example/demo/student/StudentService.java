package com.example.demo.student;

import com.example.demo.student.exception.BadRequestException;
import com.example.demo.student.exception.StudentNotFoundException;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student addStudent(@NotNull Student student) {
        Boolean existsEmail = studentRepository
                .selectExistsEmail(student.getEmail());
        if (existsEmail) {
            throw new BadRequestException(
                    "Email " + student.getEmail() + " taken");
        }

        studentRepository.save(student);
        return student;
    }

    public Student saveEditStudent(@NotNull Student student, Long studentId) {
        Boolean existsEmailConflict = studentRepository
                .selectConflictEmail(student.getEmail(), studentId);
        if (existsEmailConflict) {
            throw new BadRequestException(
                    "Email " + student.getEmail() + " conflict");
        }

        studentRepository.save(student);
        return student;
    }

    public void deleteStudent(Long studentId) {
        if(!studentRepository.existsById(studentId)) {
            throw new StudentNotFoundException(
                    "Student with id " + studentId + " does not exists");
        }
        studentRepository.deleteById(studentId);
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }
}
