package nl.h2.ejb.schema;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by: J. van Riemsdijk | H2
 */
@Entity
@Table(name = "contractor", schema = "public", catalog = "postgres")
public class ContractorJPA {

    private Long contractorId;
    private String name;
    private Boolean approved;
    private Double costModifier;
    private Date dateAdded;
    private Date dateRemoved;
    private List<AdjustmentJPA> placedAdjustments;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contractor_id", nullable = false)
    public Long getContractorId() {
        return contractorId;
    }

    public void setContractorId(Long contractorId) {
        this.contractorId = contractorId;
    }


    @Basic
    @Column(name = "name", length = 254)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Basic
    @Column(name = "approved", nullable = false)
    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }


    @Basic
    @Column(name = "cost_modifier", nullable = false)
    public Double getCostModifier() {
        return costModifier;
    }

    public void setCostModifier(Double costModifier) {
        this.costModifier = costModifier;
    }


    @Basic
    @Column(name = "date_added", nullable = false)
    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }


    @Basic
    @Column(name = "date_removed")
    public Date getDateRemoved() {
        return dateRemoved;
    }

    public void setDateRemoved(Date dateRemoved) {
        this.dateRemoved = dateRemoved;
    }


    @OneToMany(mappedBy = "contractor")
    public List<AdjustmentJPA> getPlacedAdjustments() {
        return placedAdjustments;
    }

    public void setPlacedAdjustments(List<AdjustmentJPA> placedAdjustments) {
        this.placedAdjustments = placedAdjustments;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContractorJPA that = (ContractorJPA) o;

        if (contractorId != null ? !contractorId.equals(that.contractorId) : that.contractorId != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (approved != null ? !approved.equals(that.approved) : that.approved != null) return false;
        if (costModifier != null ? !costModifier.equals(that.costModifier) : that.costModifier != null) return false;
        if (dateAdded != null ? !dateAdded.equals(that.dateAdded) : that.dateAdded != null) return false;
        if (dateRemoved != null ? !dateRemoved.equals(that.dateRemoved) : that.dateRemoved != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = contractorId != null ? contractorId.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (approved != null ? approved.hashCode() : 0);
        result = 31 * result + (costModifier != null ? costModifier.hashCode() : 0);
        result = 31 * result + (dateAdded != null ? dateAdded.hashCode() : 0);
        result = 31 * result + (dateRemoved != null ? dateRemoved.hashCode() : 0);
        return result;
    }
}
