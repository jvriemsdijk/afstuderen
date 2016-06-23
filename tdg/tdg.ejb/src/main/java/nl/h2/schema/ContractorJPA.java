package nl.h2.schema;

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

        if (getContractorId() != null ? !getContractorId().equals(that.getContractorId()) : that.getContractorId() != null) return false;
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
        if (getApproved() != null ? !getApproved().equals(that.getApproved()) : that.getApproved() != null) return false;
        if (getCostModifier() != null ? !getCostModifier().equals(that.getCostModifier()) : that.getCostModifier() != null) return false;
        if (getDateAdded() != null ? !getDateAdded().equals(that.getDateAdded()) : that.getDateAdded() != null) return false;
        if (getDateRemoved() != null ? !getDateRemoved().equals(that.getDateRemoved()) : that.getDateRemoved() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getContractorId() != null ? getContractorId().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getApproved() != null ? getApproved().hashCode() : 0);
        result = 31 * result + (getCostModifier() != null ? getCostModifier().hashCode() : 0);
        result = 31 * result + (getDateAdded() != null ? getDateAdded().hashCode() : 0);
        result = 31 * result + (getDateRemoved() != null ? getDateRemoved().hashCode() : 0);
        return result;
    }
}
