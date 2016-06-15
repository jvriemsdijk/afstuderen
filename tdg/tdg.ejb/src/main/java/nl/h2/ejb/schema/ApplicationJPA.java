package nl.h2.ejb.schema;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by: J. van Riemsdijk | H2
 */
@Entity
@Table(name = "application", schema = "public", catalog = "postgres")
public class ApplicationJPA {

    private Long applicationId;
    private Date dateRecieved;
    private PersonJPA applicant;
    private List<AdjustmentJPA> proposedAdjustments;
    private AdviceJPA advice;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id", nullable = false)
    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }


    @Basic
    @Column(name = "date_recieved")
    public Date getDateRecieved() {
        return dateRecieved;
    }

    public void setDateRecieved(Date dateRecieved) {
        this.dateRecieved = dateRecieved;
    }


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "applicant", referencedColumnName = "bsn", nullable = false)
    public PersonJPA getApplicant() {
        return applicant;
    }

    public void setApplicant(PersonJPA applicant) {
        this.applicant = applicant;
    }


    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL)
    public List<AdjustmentJPA> getProposedAdjustments() {
        return proposedAdjustments;
    }

    public void setProposedAdjustments(List<AdjustmentJPA> proposedAdjustments) {
        this.proposedAdjustments = proposedAdjustments;
    }


    @OneToOne(mappedBy = "application", cascade = CascadeType.ALL)
    public AdviceJPA getAdvice() {
        return advice;
    }

    public void setAdvice(AdviceJPA advice) {
        this.advice = advice;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ApplicationJPA that = (ApplicationJPA) o;

        if (applicationId != null ? !applicationId.equals(that.applicationId) : that.applicationId != null)
            return false;
        if (dateRecieved != null ? !dateRecieved.equals(that.dateRecieved) : that.dateRecieved != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = applicationId != null ? applicationId.hashCode() : 0;
        result = 31 * result + (dateRecieved != null ? dateRecieved.hashCode() : 0);
        return result;
    }
}
