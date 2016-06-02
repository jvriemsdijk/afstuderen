package nl.h2.ejb;

import nl.h2.ejb.schema.ConditionJPA;
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
     * @param averageNumberOfApplications - The average number of applications an applying person has
     * @param averageHousholdSize         - The average size of the household of an applicant
     */
    public void generateTrainingData(int numberOfRecords, double averageNumberOfApplications, double sigmaNumberOfApplications, double averageHousholdSize, double sigmaHouseholdSize) {

        // Clear previous data
        clearData();

        // Start initial data load
        initialDataLoad();

        // Determine number of applicants

        // For each applicant determine number of applications



    }


    private void clearData() {
        // TODO : Create function to clear all data
        // TODO : named queries for the deletion of all data
    }


    private void initialDataLoad() {
        // TODO : Standard set of inital conditions and adjustment definitions creation
    }


    private void createApplicant(int numberOfApplications, double averageHouseholdSize) {

        // Create a new person and housing situation

        // Generate the given number of applications

        //


        // For extra applications add conditions to the original condition list
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
