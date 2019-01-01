package org.bits.wilp.dsad;

import java.util.Objects;

public class StudentRecord {

    private String studentId;
    private float cgpa;

    public StudentRecord(String studentId, float cgpa) {
        this.studentId = studentId;
        this.cgpa = cgpa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentRecord that = (StudentRecord) o;
        return Objects.equals(studentId, that.studentId);
    }

    @Override
    public int hashCode() {
        return getHashCode(studentId);
    }

    public static int getHashCode(String studentId) {
        return Objects.hash(studentId.substring(0, 7));
    }

    public String getStudentId() {
        return studentId;
    }

    public float getCgpa() {
        return cgpa;
    }

    @Override
    public String toString() {
        return "StudentRecord{" +
                "studentId='" + studentId + '\'' +
                ", cgpa=" + cgpa +
                '}';
    }

}
