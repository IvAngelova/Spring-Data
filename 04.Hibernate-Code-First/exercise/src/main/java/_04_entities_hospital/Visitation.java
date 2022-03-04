package _04_entities_hospital;

import org.hibernate.annotations.AnyMetaDefs;

import javax.persistence.*;
import java.time.LocalDate;
@Entity
@Table(name = "_04_visitations")
public class Visitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String comments;

    @ManyToOne
    private Patient patient;

    @ManyToOne
    private Diagnose diagnose;

    @ManyToOne
    private Medicament medicament;

    public Visitation() {
    }

    public Visitation(LocalDate date, String comments, Patient patient) {
        this.date = date;
        this.comments = comments;
        this.patient = patient;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Diagnose getDiagnose() {
        return diagnose;
    }

    public void setDiagnose(Diagnose diagnose) {
        this.diagnose = diagnose;
    }

    public Medicament getMedicament() {
        return medicament;
    }

    public void setMedicament(Medicament medicament) {
        this.medicament = medicament;
    }
}
