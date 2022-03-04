package _03_entities_university;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "_03_students")
public class Student extends Person{

    @Column(name = "avg_grade")
    private double avgGrade;

    private int attendance;

    @ManyToMany
    @JoinTable(
            name = "_03_students_courses",
            joinColumns =
            @JoinColumn(name = "student_id", referencedColumnName = "id"),
            inverseJoinColumns =
            @JoinColumn(name = "course_id", referencedColumnName = "id")
    )
    private Set<Course> courses;

    public Student() {
        this.courses = new HashSet<>();
    }

    public Student(String firstName, String lastName, String phoneNumber, double avgGrade, int attendance) {
        super(firstName, lastName, phoneNumber);
        this.avgGrade = avgGrade;
        this.attendance = attendance;
        this.courses = new HashSet<>();
    }

    public double getAvgGrade() {
        return avgGrade;
    }

    public void setAvgGrade(double avgGrade) {
        this.avgGrade = avgGrade;
    }

    public int getAttendance() {
        return attendance;
    }

    public void setAttendance(int attendance) {
        this.attendance = attendance;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> course) {
        this.courses = course;
    }
}
