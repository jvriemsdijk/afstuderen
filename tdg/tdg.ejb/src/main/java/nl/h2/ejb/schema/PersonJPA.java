package nl.h2.ejb.schema;

import javax.persistence.*;
import java.util.List;

/**
 * Created by: J. van Riemsdijk | H2
 */
@Entity
@Table(name = "person", schema = "public", catalog = "postgres")
@NamedQueries({
        @NamedQuery(name = "Person.findAll", query = "SELECT a FROM PersonJPA a")
})
public class PersonJPA {

    private long bsn;
    private List<ApplicationJPA> applications;
    private List<PersonToHousingSituationJPA> housingHistory;


    @Id
    @Column(name = "bsn", nullable = false)
    public long getBsn() {
        return bsn;
    }

    public void setBsn(long bsn) {
        this.bsn = bsn;
    }


    @OneToMany(mappedBy = "applicant", cascade = CascadeType.ALL)
    public List<ApplicationJPA> getApplications() {
        return applications;
    }

    public void setApplications(List<ApplicationJPA> applications) {
        this.applications = applications;
    }


    @OneToMany(mappedBy = "resident", cascade = CascadeType.ALL)
    public List<PersonToHousingSituationJPA> getHousingHistory() {
        return housingHistory;
    }

    public void setHousingHistory(List<PersonToHousingSituationJPA> housingHistory) {
        this.housingHistory = housingHistory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersonJPA personJPA = (PersonJPA) o;

        if (getBsn() != personJPA.getBsn()) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (getBsn() ^ (getBsn() >>> 32));
    }

}
