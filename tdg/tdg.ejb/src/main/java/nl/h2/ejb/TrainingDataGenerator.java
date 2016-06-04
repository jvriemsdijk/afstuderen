package nl.h2.ejb;

import nl.h2.ejb.schema.AdjustmentDefinitionJPA;
import nl.h2.ejb.schema.ConditionJPA;
import nl.h2.utils.exception.ApplicatieException;
import org.apache.log4j.Logger;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
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
     * @param numberOfRecords          - The total number of records to be generated
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


//        // Clear previous data
//        clearData();
//
//        // Start initial data load
//        initialDataLoad();

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
        q = entityManager.createNamedQuery("AdjustmentDefinition.deleteAll");
        q.executeUpdate();
        System.out.println("Called clear data");

    }


    public void initialDataLoad() throws Exception {

//        try {

            /*
            Conditions
             */

        // Minor mobility impairment
        ConditionJPA minorMovementImpairment = new ConditionJPA();
        minorMovementImpairment.setId(1);
        minorMovementImpairment.setName("Minor mobility impairment");
        minorMovementImpairment.setChronic(false);
        entityManager.persist(minorMovementImpairment);
        entityManager.flush();

        // Medium mobility impairment
        ConditionJPA mediumMovementImpairment = new ConditionJPA();
        mediumMovementImpairment.setId(2);
        mediumMovementImpairment.setName("Medium mobility impairment");
        mediumMovementImpairment.setChronic(false);
        entityManager.persist(mediumMovementImpairment);
        entityManager.flush();

        // Severe mobility impairment
        ConditionJPA severeMovementImpairment = new ConditionJPA();
        severeMovementImpairment.setId(3);
        severeMovementImpairment.setName("Severe mobility impairment");
        severeMovementImpairment.setChronic(false);
        entityManager.persist(severeMovementImpairment);
        entityManager.flush();

        // Wheelchair bound
        ConditionJPA wheelchairBound = new ConditionJPA();
        wheelchairBound.setId(4);
        wheelchairBound.setName("Wheelchair bound");
        wheelchairBound.setChronic(false);
        entityManager.persist(wheelchairBound);
        entityManager.flush();

        // Minor mobility impairment - Chronic
        ConditionJPA minorMovementImpairmentChronic = new ConditionJPA();
        minorMovementImpairmentChronic.setId(5);
        minorMovementImpairmentChronic.setName("Minor mobility impairment - Chronic");
        minorMovementImpairmentChronic.setChronic(true);
        entityManager.persist(minorMovementImpairmentChronic);
        entityManager.flush();

        // Medium mobility impairment - Chronic
        ConditionJPA mediumMovementImpairmentChronic = new ConditionJPA();
        mediumMovementImpairmentChronic.setId(6);
        mediumMovementImpairmentChronic.setName("Medium mobility impairment - Chronic");
        mediumMovementImpairmentChronic.setChronic(true);
        entityManager.persist(mediumMovementImpairmentChronic);
        entityManager.flush();

        // Severe mobility impairment - Chronic
        ConditionJPA severeMovementImpairmentChronic = new ConditionJPA();
        severeMovementImpairmentChronic.setId(7);
        severeMovementImpairmentChronic.setName("Severe mobility impairment - Chronic");
        severeMovementImpairmentChronic.setChronic(true);
        entityManager.persist(severeMovementImpairmentChronic);
        entityManager.flush();

        // Wheelchair bound - Chronic
        ConditionJPA wheelchairBoundChronic = new ConditionJPA();
        wheelchairBoundChronic.setId(8);
        wheelchairBoundChronic.setName("Wheelchair bound - Chronic");
        wheelchairBoundChronic.setChronic(true);
        entityManager.persist(wheelchairBoundChronic);
        entityManager.flush();



        /*
        Adjustments
         */

        // Shower seat
        AdjustmentDefinitionJPA showerSeat = new AdjustmentDefinitionJPA();
        showerSeat.setId(1);
        showerSeat.setName("Shower seat");
        showerSeat.setAverageCost(400.0);
        showerSeat.setCostMargin(150.0);
        entityManager.persist(showerSeat);
        entityManager.flush();

        // Plateau elevator
        AdjustmentDefinitionJPA plateauElevator = new AdjustmentDefinitionJPA();
        plateauElevator.setId(2);
        plateauElevator.setName("Plateau elevator");
        plateauElevator.setAverageCost(4500.0);
        plateauElevator.setCostMargin(1000.0);
        entityManager.persist(plateauElevator);
        entityManager.flush();

        // Between steps
        AdjustmentDefinitionJPA betweenSteps = new AdjustmentDefinitionJPA();
        betweenSteps.setId(3);
        betweenSteps.setName("Between steps");
        betweenSteps.setAverageCost(850.0);
        betweenSteps.setCostMargin(25.0);
        entityManager.persist(betweenSteps);
        entityManager.flush();

        // Standing elevator
        AdjustmentDefinitionJPA standingElevator = new AdjustmentDefinitionJPA();
        standingElevator.setId(4);
        standingElevator.setName("Standing elevator");
        standingElevator.setAverageCost(5000.0);
        standingElevator.setCostMargin(500.0);
        entityManager.persist(standingElevator);
        entityManager.flush();

        // Doorstep helper
        AdjustmentDefinitionJPA doorstepHelper = new AdjustmentDefinitionJPA();
        doorstepHelper.setId(5);
        doorstepHelper.setName("Doorstep helper");
        doorstepHelper.setAverageCost(210.0);
        doorstepHelper.setCostMargin(25.0);
        entityManager.persist(doorstepHelper);
        entityManager.flush();

        // Armrests
        AdjustmentDefinitionJPA armrests = new AdjustmentDefinitionJPA();
        armrests.setId(6);
        armrests.setName("Armrests");
        armrests.setAverageCost(110.0);
        armrests.setCostMargin(25.0);
        entityManager.persist(armrests);
        entityManager.flush();

        // Height adjustable kitchen
        AdjustmentDefinitionJPA heightAdjustableKitchen = new AdjustmentDefinitionJPA();
        heightAdjustableKitchen.setId(7);
        heightAdjustableKitchen.setName("Height adjustable kitchen");
        heightAdjustableKitchen.setAverageCost(10000.0);
        heightAdjustableKitchen.setCostMargin(3000.0);
        entityManager.persist(heightAdjustableKitchen);
        entityManager.flush();

        // Bidet toilet seat
        AdjustmentDefinitionJPA bidetToiletSeat = new AdjustmentDefinitionJPA();
        bidetToiletSeat.setId(8);
        bidetToiletSeat.setName("Bidet toilet seat");
        bidetToiletSeat.setAverageCost(1750.0);
        bidetToiletSeat.setCostMargin(250.0);
        entityManager.persist(bidetToiletSeat);
        entityManager.flush();

        // Bidet toilet
        AdjustmentDefinitionJPA bidetToilet = new AdjustmentDefinitionJPA();
        bidetToilet.setId(9);
        bidetToilet.setName("Bidet toilet");
        bidetToilet.setAverageCost(2700.0);
        bidetToilet.setCostMargin(750.0);
        entityManager.persist(bidetToilet);
        entityManager.flush();

        // Toilet back support
        AdjustmentDefinitionJPA toiletBackSupport = new AdjustmentDefinitionJPA();
        toiletBackSupport.setId(10);
        toiletBackSupport.setName("Toilet back support");
        toiletBackSupport.setAverageCost(275.0);
        toiletBackSupport.setCostMargin(7.5);
        entityManager.persist(toiletBackSupport);
        entityManager.flush();

        // Toilet seat with armrests
        AdjustmentDefinitionJPA toiletSeatWithArmrests = new AdjustmentDefinitionJPA();
        toiletSeatWithArmrests.setId(11);
        toiletSeatWithArmrests.setName("Toilet seat with armrests");
        toiletSeatWithArmrests.setAverageCost(240.0);
        toiletSeatWithArmrests.setCostMargin(40.0);
        entityManager.persist(toiletSeatWithArmrests);
        entityManager.flush();

        // Heightend toilet seat
        AdjustmentDefinitionJPA heightendToiletSeat = new AdjustmentDefinitionJPA();
        heightendToiletSeat.setId(12);
        heightendToiletSeat.setName("Heightend toilet seat");
        heightendToiletSeat.setAverageCost(100.0);
        heightendToiletSeat.setCostMargin(12.5);
        entityManager.persist(heightendToiletSeat);
        entityManager.flush();

        // Wide toilet seat
        AdjustmentDefinitionJPA wideToiletSeat = new AdjustmentDefinitionJPA();
        wideToiletSeat.setId(13);
        wideToiletSeat.setName("Wide toilet seat");
        wideToiletSeat.setAverageCost(35.0);
        wideToiletSeat.setCostMargin(3.5);
        entityManager.persist(wideToiletSeat);
        entityManager.flush();

        // Hand grips
        AdjustmentDefinitionJPA handGrips = new AdjustmentDefinitionJPA();
        handGrips.setId(14);
        handGrips.setName("Hand grips");
        handGrips.setAverageCost(225.0);
        handGrips.setCostMargin(33.3);
        entityManager.persist(handGrips);
        entityManager.flush();

        // Heightend toilet
        AdjustmentDefinitionJPA heightendToilet = new AdjustmentDefinitionJPA();
        heightendToilet.setId(15);
        heightendToilet.setName("Heightend toilet");
        heightendToilet.setAverageCost(200.0);
        heightendToilet.setCostMargin(33.3);
        entityManager.persist(heightendToilet);
        entityManager.flush();

        // Electrical door
        AdjustmentDefinitionJPA electricalDoor = new AdjustmentDefinitionJPA();
        electricalDoor.setId(16);
        electricalDoor.setName("Electrical door");
        electricalDoor.setAverageCost(2200.0);
        electricalDoor.setCostMargin(666.7);
        entityManager.persist(electricalDoor);
        entityManager.flush();

        // Anti slip mat
        AdjustmentDefinitionJPA antiSlipMat = new AdjustmentDefinitionJPA();
        antiSlipMat.setId(17);
        antiSlipMat.setName("Anti slip mat");
        antiSlipMat.setAverageCost(30.0);
        antiSlipMat.setCostMargin(10.0);
        entityManager.persist(antiSlipMat);
        entityManager.flush();

        // Handrail
        AdjustmentDefinitionJPA handrail = new AdjustmentDefinitionJPA();
        handrail.setId(18);
        handrail.setName("Handrail");
        handrail.setAverageCost(340.0);
        handrail.setCostMargin(100.0);
        entityManager.persist(handrail);
        entityManager.flush();

        // Ceiling mounted patient elevator
        AdjustmentDefinitionJPA ceilingMountedPatientElevator = new AdjustmentDefinitionJPA();
        ceilingMountedPatientElevator.setId(19);
        ceilingMountedPatientElevator.setName("Ceiling mounted patient elevator");
        ceilingMountedPatientElevator.setAverageCost(6500.0);
        ceilingMountedPatientElevator.setCostMargin(1166.6);
        entityManager.persist(ceilingMountedPatientElevator);
        entityManager.flush();

        // Passive patient elevator
        AdjustmentDefinitionJPA passivePatientElevator = new AdjustmentDefinitionJPA();
        passivePatientElevator.setId(20);
        passivePatientElevator.setName("Passive patient elevator");
        passivePatientElevator.setAverageCost(5000.0);
        passivePatientElevator.setCostMargin(666.7);
        entityManager.persist(passivePatientElevator);
        entityManager.flush();

        // Wall mounted patient elevator
        AdjustmentDefinitionJPA wallMountedPatientElevator = new AdjustmentDefinitionJPA();
        wallMountedPatientElevator.setId(21);
        wallMountedPatientElevator.setName("Wall mounted patient elevator");
        wallMountedPatientElevator.setAverageCost(5000.0);
        wallMountedPatientElevator.setCostMargin(1000.0);
        entityManager.persist(wallMountedPatientElevator);
        entityManager.flush();

        // Lifting sling
        AdjustmentDefinitionJPA liftingSling = new AdjustmentDefinitionJPA();
        liftingSling.setId(22);
        liftingSling.setName("Lifting sling");
        liftingSling.setAverageCost(825.0);
        liftingSling.setCostMargin(225.0);
        entityManager.persist(liftingSling);
        entityManager.flush();

        // Support pole
        AdjustmentDefinitionJPA supportPole = new AdjustmentDefinitionJPA();
        supportPole.setId(23);
        supportPole.setName("Support pole");
        supportPole.setAverageCost(375.0);
        supportPole.setCostMargin(41.7);
        entityManager.persist(supportPole);
        entityManager.flush();





        /*
        Connect conditions and adjustments
         */



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
