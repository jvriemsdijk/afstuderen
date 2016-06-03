package nl.h2.ejb;

import nl.h2.ejb.schema.AdjustmentDefinitionJPA;
import nl.h2.ejb.schema.ConditionJPA;
import nl.h2.utils.exception.ApplicatieException;
import org.apache.log4j.Logger;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Map;
import java.util.Random;

@Stateless
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class TrainingDataGenerator {

    private static final Logger LOGGER = Logger.getLogger(TrainingDataGenerator.class.getName());

    public TrainingDataGenerator() {
    }
    @PersistenceContext(unitName = "tdg.database")
    private EntityManager entityManager;


    private Random random = null;
    private Map<ConditionJPA, Double> conditionProbabilityMap;



    /**
     * Method to generate a fresh instance of the database training data. Will generate content for the database with
     * the given parameters.
     *
     * @param numberOfRecords             - The total number of records to be generated
     * @param meanNumberOfApplications - The average number of applications an applying person has
     * @param meanHousholdSize         - The average size of the household of an applicant
     */
    public void generateTrainingData(int numberOfRecords, double meanNumberOfApplications, double sigmaNumberOfApplications, double meanHousholdSize, double sigmaHouseholdSize) throws Exception {

        // Input validation
        if (numberOfRecords < 1) {
            throw new ApplicatieException(Constanten.ERR_NUMBER_OF_RECORDS_TOO_LOW);
        }
        if (meanNumberOfApplications < 1) {
            throw new ApplicatieException(Constanten.ERR_MEAN_NUMBER_OF_APPLICATIONS_TOO_LOW);
        }
        if (sigmaNumberOfApplications <= 0) {
            throw new ApplicatieException(Constanten.ERR_SIGMA_NUMBER_OF_APPLICATIONS_TOO_LOW);
        }
        if (meanHousholdSize < 1) {
            throw new ApplicatieException(Constanten.ERR_MEAN_HOUSEHOLD_SIZE_TOO_LOW);
        }
        if (sigmaHouseholdSize <= 0) {
            throw new ApplicatieException(Constanten.ERR_SIGMA_HOUSEHOLD_SIZE_TOO_LOW);
        }


        // Clear previous data
        clearData();

        // Start initial data load
        initialDataLoad();

        // Determine number of applicants
        int numberOfApplicants = (int) Math.ceil(numberOfRecords / meanNumberOfApplications);

        for (int applicantNumber = 0; applicantNumber < numberOfApplicants; applicantNumber++) {

            // For each applicant, determine the number of applications and the household size
            long numberOfApplications = Math.round(calculateAbsoluteGaussian(meanNumberOfApplications, sigmaNumberOfApplications));
            long householdSize = Math.round(calculateAbsoluteGaussian(meanHousholdSize, sigmaHouseholdSize));

            // Create the applicant
            createApplicant(numberOfApplications, householdSize);
        }


    }


    public void clearData() throws Exception {

        // Clear all conditions
        Query q = entityManager.createNamedQuery("Condition.deleteAll");
        q.executeUpdate();


    }


    public void initialDataLoad() throws Exception {

//        try {

            /*
            Conditions
             */

            // Minor mobility impairment
            ConditionJPA minorMovementImpairment = new ConditionJPA();
            minorMovementImpairment.setName("Minor mobility impairment");
            minorMovementImpairment.setChronic(false);
            entityManager.flush();
            entityManager.persist(minorMovementImpairment);

            // Medium mobility impairment
            ConditionJPA mediumMovementImpairment = new ConditionJPA();
            mediumMovementImpairment.setName("Medium mobility impairment");
            mediumMovementImpairment.setChronic(false);
            entityManager.flush();
            entityManager.persist(mediumMovementImpairment);

            // Severe mobility impairment
            ConditionJPA severeMovementImpairment = new ConditionJPA();
            severeMovementImpairment.setName("Severe mobility impairment");
            severeMovementImpairment.setChronic(false);
            entityManager.flush();
            entityManager.persist(severeMovementImpairment);

            // Wheelchair bound
            ConditionJPA wheelchairBound = new ConditionJPA();
            wheelchairBound.setName("Wheelchair bound");
            wheelchairBound.setChronic(false);
            entityManager.flush();
            entityManager.persist(wheelchairBound);

            // Minor mobility impairment - Chronic
            ConditionJPA minorMovementImpairmentChronic = new ConditionJPA();
            minorMovementImpairmentChronic.setName("Minor mobility impairment - Chronic");
            minorMovementImpairmentChronic.setChronic(true);
            entityManager.flush();
            entityManager.persist(minorMovementImpairmentChronic);

            // Medium mobility impairment - Chronic
            ConditionJPA mediumMovementImpairmentChronic = new ConditionJPA();
            mediumMovementImpairmentChronic.setName("Medium mobility impairment - Chronic");
            mediumMovementImpairmentChronic.setChronic(true);
            entityManager.flush();
            entityManager.persist(mediumMovementImpairmentChronic);

            // Severe mobility impairment - Chronic
            ConditionJPA severeMovementImpairmentChronic = new ConditionJPA();
            severeMovementImpairmentChronic.setName("Severe mobility impairment - Chronic");
            severeMovementImpairment.setChronic(true);
            entityManager.flush();
            entityManager.persist(severeMovementImpairmentChronic);

            // Wheelchair bound - Chronic
            ConditionJPA wheelchairBoundChronic = new ConditionJPA();
            wheelchairBoundChronic.setName("Wheelchair bound - Chronic");
            wheelchairBoundChronic.setChronic(true);
            entityManager.flush();
            entityManager.persist(wheelchairBoundChronic);



            /*
            Adjustments
             */













            AdjustmentDefinitionJPA adjustment = new AdjustmentDefinitionJPA();



//
//        } catch (Exception e) {
//            throw new ApplicatieException(Constanten.FOUT_ONBEKENDE_FOUT);
//        }





    }


    private void createApplicant(long numberOfApplications, long householdSize) {

        // Create a new person and housing situation

        // Generate the given number of applications

        //


        // For extra applications add conditions to the original condition list
    }


    private double calculateAbsoluteGaussian(double mean, double sigma) {
        return Math.abs(getRandom().nextGaussian() * sigma + mean);
    }




    /*
    Getters & Setters
     */


    public Random getRandom() {
        if (random == null) {
            setRandom(new Random());
        }
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }
}
