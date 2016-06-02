package nl.h2.controllers;

import nl.h2.ejb.TrainingDataGenerator;
import nl.h2.utils.exception.InterneException;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ManagedBean(name = "applicatieManager")
@ApplicationScoped
public class ApplicatieManager implements Serializable {

    private static final long serialVersionUID = 441462296373244095L;
    private static final Logger LOGGER = Logger.getLogger(ApplicatieManager.class);
    private String applicatieNaam = null;
    private String versienummer = null;

    @EJB
    private TrainingDataGenerator trainingDataGenerator;

    @PostConstruct
    public void construct() {
        init();
    }

    public void init() {
        LOGGER.debug("Initialisatie applicatiemanager");
        setApplicatieNaam("H2 webapp");
        setVersienummer("1.0-SNAPSHOT");

    }

    public String getHelloWorld() throws InterneException {
        return trainingDataGenerator.sayHello("Johan Cruijf");
    }

    public String getApplicatieNaam() {
        return applicatieNaam;
    }

    private void setApplicatieNaam(String naam) {
        this.applicatieNaam = naam;
    }

    public String getVersienummer() {
        return this.versienummer;
    }

    private void setVersienummer(String versienummer) {
        this.versienummer = versienummer;
    }

}
