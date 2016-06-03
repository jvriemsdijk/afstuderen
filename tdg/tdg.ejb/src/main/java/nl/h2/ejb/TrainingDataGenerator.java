package nl.h2.ejb;

import nl.h2.ejb.schema.AdjustmentDefinitionJPA;
import nl.h2.ejb.schema.ConditionJPA;
import nl.h2.utils.exception.ApplicatieException;
import org.apache.log4j.Logger;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
    public void generateTrainingData(int numberOfRecords, double meanNumberOfApplications, double sigmaNumberOfApplications, double meanHousholdSize, double sigmaHouseholdSize) throws ApplicatieException {

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


    private void clearData() {
        // TODO : Create function to clear all data
        // TODO : named queries for the deletion of all data
    }


    private void initialDataLoad() {
        // TODO : Standard set of inital conditions and adjustment definitions creation

        ConditionJPA condition;
        AdjustmentDefinitionJPA adjustment;









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
