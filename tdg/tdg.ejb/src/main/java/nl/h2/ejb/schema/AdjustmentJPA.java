package nl.h2.ejb.schema;

import javax.persistence.*;

/**
 * Created by: J. van Riemsdijk | H2
 */
@Entity
@Table(name = "adjustment", schema = "public", catalog = "postgres")
@NamedQueries({
        @NamedQuery(name = "Adjustment.findAll", query = "SELECT a FROM AdjustmentJPA a")
})
public class AdjustmentJPA {

    private Long adjustmentId;
    private Double actualCost;
    private AdjustmentDefinitionJPA adjustmentDefinition;
    private Double costSubsidized;
    private ContractorJPA contractor;
    private HousingSituationToAdjustmentJPA installationPeriod;
    private ApplicationJPA application;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adjustment_id", nullable = false)
    public Long getAdjustmentId() {
        return adjustmentId;
    }

    public void setAdjustmentId(Long adjustmentId) {
        this.adjustmentId = adjustmentId;
    }


    @Basic
    @Column(name = "actual_cost", nullable = true, precision = 0)
    public Double getActualCost() {
        return actualCost;
    }


    public void setActualCost(Double actualCost) {
        this.actualCost = actualCost;
    }

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "adjustment_definition", referencedColumnName = "adjustment_definition_id", nullable = false)
    public AdjustmentDefinitionJPA getAdjustmentDefinition() {
        return adjustmentDefinition;
    }


    public void setAdjustmentDefinition(AdjustmentDefinitionJPA adjustmentDefinition) {
        this.adjustmentDefinition = adjustmentDefinition;
    }


    @Basic
    @Column(name = "cost_subsidized", nullable = true, precision = 0)
    public Double getCostSubsidized() {
        return costSubsidized;
    }

    public void setCostSubsidized(Double costSubsidized) {
        this.costSubsidized = costSubsidized;
    }


    @ManyToOne
    @JoinColumn(name = "contractor", referencedColumnName = "contractor_id")
    public ContractorJPA getContractor() {
        return contractor;
    }

    public void setContractor(ContractorJPA contractor) {
        this.contractor = contractor;
    }


    @OneToOne(mappedBy = "adjustment", cascade = CascadeType.ALL)
    public HousingSituationToAdjustmentJPA getInstallationPeriod() {
        return installationPeriod;
    }

    public void setInstallationPeriod(HousingSituationToAdjustmentJPA installationPeriod) {
        this.installationPeriod = installationPeriod;
    }


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "application", referencedColumnName = "application_id")
    public ApplicationJPA getApplication() {
        return application;
    }

    public void setApplication(ApplicationJPA application) {
        this.application = application;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdjustmentJPA that = (AdjustmentJPA) o;

        if (getAdjustmentId().equals(that.getAdjustmentId())) return false;
        if (getActualCost() != null ? !getActualCost().equals(that.getActualCost()) : that.getActualCost() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = 6409;
        result = 31 * result + (getActualCost() != null ? getActualCost().hashCode() : 0);
        return result;
    }
}
