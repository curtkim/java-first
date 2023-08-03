package com.example.protobufspringswagger;

import com.baeldung.protobuf.BaeldungTraining;

import java.util.Arrays;
import java.util.List;

public class CourseMother {

  static List<BaeldungTraining.Student> createTestStudents() {
    BaeldungTraining.Student.PhoneNumber phone1 = createPhone("123456", BaeldungTraining.Student.PhoneType.MOBILE);
    BaeldungTraining.Student student1 = createStudent(1, "John", "Doe", "john.doe@baeldung.com", Arrays.asList(phone1));

    BaeldungTraining.Student.PhoneNumber phone2 = createPhone("234567", BaeldungTraining.Student.PhoneType.LANDLINE);
    BaeldungTraining.Student student2 = createStudent(2, "Richard", "Roe", "richard.roe@baeldung.com", Arrays.asList(phone2));

    BaeldungTraining.Student.PhoneNumber phone3_1 = createPhone("345678", BaeldungTraining.Student.PhoneType.MOBILE);
    BaeldungTraining.Student.PhoneNumber phone3_2 = createPhone("456789", BaeldungTraining.Student.PhoneType.LANDLINE);
    BaeldungTraining.Student student3 = createStudent(3, "Jane", "Doe", "jane.doe@baeldung.com", Arrays.asList(phone3_1, phone3_2));

    return Arrays.asList(student1, student2, student3);
  }

  static BaeldungTraining.Student createStudent(int id, String firstName, String lastName, String email, List<BaeldungTraining.Student.PhoneNumber> phones) {
    return BaeldungTraining.Student.newBuilder().setId(id).setFirstName(firstName).setLastName(lastName).setEmail(email).addAllPhone(phones).build();
  }

  static BaeldungTraining.Student.PhoneNumber createPhone(String number, BaeldungTraining.Student.PhoneType type) {
    return BaeldungTraining.Student.PhoneNumber.newBuilder().setNumber(number).setType(type).build();
  }
}
