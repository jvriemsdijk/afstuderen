package nl.h2.ejb;

import nl.h2.ejb.schema.*;
import nl.h2.utils.exception.ApplicatieException;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.hibernate.mapping.Bag;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

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


    private final double BUDGET = 7200.0;
    private int baseBsn = 923345456;
    private Random random = null;
    private Map<ConditionJPA, Double> conditionProbabilityMap = new HashMap<ConditionJPA, Double>();
    private List<ConditionJPA> allConditions = new ArrayList<ConditionJPA>();
    private List<AdjustmentDefinitionJPA> allAdjustments = new ArrayList<AdjustmentDefinitionJPA>();


    /**
     * Method to generate a fresh instance of the database training data. Will generate content for the database with
     * the given parameters.
     *
     * @param numberOfRecords          - The total number of records to be generated
     * @param meanNumberOfApplications - The average number of applications an applying person has
     * @param meanHousholdSize         - The average size of the household of an applicant
     */
    public void generateTrainingData(int numberOfRecords, double meanNumberOfApplications, double sigmaNumberOfApplications, double meanHousholdSize, double sigmaHouseholdSize, Long seed) throws Exception {

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

        // Seed random function if seed is given
        if (seed != null) {
            getRandom().setSeed(seed);
        }

        // Clear previous data
        clearData();

        // Start initial data load
        initialDataLoad();

        // Determine number of applicants
        int numberOfApplicants = (int) Math.ceil(numberOfRecords / meanNumberOfApplications);
        int totalNumberOfApplications = 0;
        for (int applicantNumber = 0; applicantNumber < numberOfApplicants; applicantNumber++) {

            // For each applicant, determine the number of applications and the household size
            long numberOfApplications = Math.round(calculateAbsoluteGaussian(meanNumberOfApplications, sigmaNumberOfApplications));
            long householdSize = Math.round(calculateAbsoluteGaussian(meanHousholdSize, sigmaHouseholdSize));

            // Determine if the number of applications is valid in order to maintain the total number of records given
            if (numberOfRecords - (totalNumberOfApplications + numberOfApplications) >= 0) {

                // Create the applicant
                createApplicant(numberOfApplications, householdSize);

                // Increase total number of applications
                totalNumberOfApplications += numberOfApplications;

                // If the number of records is equal to the number of applications, break
                if (totalNumberOfApplications == numberOfRecords) {
                    break;
                }

            } else {

                // Create applicant with remaining number of applications and break
                createApplicant(numberOfRecords - totalNumberOfApplications, householdSize);
                break;
            }
        }
    }


    public void clearData() throws Exception {

        LOGGER.info("Start clearData();");
        System.out.println("Start clear data");

        // Clear all class data
        conditionProbabilityMap.clear();
        allConditions.clear();
        allAdjustments.clear();

        Query removeConditionsAdjustments = entityManager.createNativeQuery("DELETE FROM adjustment_condition");
        Query removeAdviceCurrentConditions = entityManager.createNativeQuery("DELETE FROM advice_current_condition");
        Query removeAdviceFutureConditions = entityManager.createNativeQuery("DELETE FROM advice_future_condition");
        Query removeApplicantHistory = entityManager.createNativeQuery("DELETE FROM applicant_history");
        Query removeHousingAdjustments = entityManager.createNativeQuery("DELETE FROM housing_adjustments");
        Query removeResidents = entityManager.createNativeQuery("DELETE FROM residents");
        Query removeConditions = entityManager.createNativeQuery("DELETE FROM conditions");
        Query removeAdjustments = entityManager.createNativeQuery("DELETE FROM adjustment");
        Query removeAdjustmentDefinitions = entityManager.createNativeQuery("DELETE FROM adjustment_definitions");
        Query removeAdvice = entityManager.createNativeQuery("DELETE FROM advice");
        Query removeBag = entityManager.createNativeQuery("DELETE FROM bag");
        Query removeAdresses = entityManager.createNativeQuery("DELETE FROM adresses");
        Query removeHousingSituations = entityManager.createNativeQuery("DELETE FROM housing_situation");
        Query removeWmoDecisions = entityManager.createNativeQuery("DELETE FROM wmo_decisions");
        Query removePersons = entityManager.createNativeQuery("DELETE FROM person");

        removeConditionsAdjustments.executeUpdate();
        removeAdviceCurrentConditions.executeUpdate();
        removeAdviceFutureConditions.executeUpdate();
        removeApplicantHistory.executeUpdate();
        removeHousingAdjustments.executeUpdate();
        removeResidents.executeUpdate();
        removeConditions.executeUpdate();
        removeAdjustments.executeUpdate();
        removeWmoDecisions.executeUpdate();
        removeAdvice.executeUpdate();
        removeBag.executeUpdate();
        removeAdresses.executeUpdate();
        removeHousingSituations.executeUpdate();
        removePersons.executeUpdate();
        removeAdjustmentDefinitions.executeUpdate();

        LOGGER.info("End clearData();");
        System.out.println("Done with clear data");
    }

    /**
     * Method for the initial data load for the training data generator. Creates a selection of conditions and
     * adjustment definition and links them together. Also fills the class variables with the initial data.
     *
     * @throws Exception
     */
    public void initialDataLoad() throws Exception {

        LOGGER.info("Start initialDataLoad();");

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
        allConditions.add(minorMovementImpairment);
        conditionProbabilityMap.put(minorMovementImpairment, 0.15);

        // Medium mobility impairment
        ConditionJPA mediumMovementImpairment = new ConditionJPA();
        mediumMovementImpairment.setId(2);
        mediumMovementImpairment.setName("Medium mobility impairment");
        mediumMovementImpairment.setChronic(false);
        entityManager.persist(mediumMovementImpairment);
        entityManager.flush();
        allConditions.add(mediumMovementImpairment);
        conditionProbabilityMap.put(mediumMovementImpairment, 0.1);

        // Severe mobility impairment
        ConditionJPA severeMovementImpairment = new ConditionJPA();
        severeMovementImpairment.setId(3);
        severeMovementImpairment.setName("Severe mobility impairment");
        severeMovementImpairment.setChronic(false);
        entityManager.persist(severeMovementImpairment);
        entityManager.flush();
        allConditions.add(severeMovementImpairment);
        conditionProbabilityMap.put(severeMovementImpairment, 0.05);

        // Wheelchair bound
        ConditionJPA wheelchairBound = new ConditionJPA();
        wheelchairBound.setId(4);
        wheelchairBound.setName("Wheelchair bound");
        wheelchairBound.setChronic(false);
        entityManager.persist(wheelchairBound);
        entityManager.flush();
        allConditions.add(wheelchairBound);
        conditionProbabilityMap.put(wheelchairBound, 0.2);

        // Minor mobility impairment - Chronic
        ConditionJPA minorMovementImpairmentChronic = new ConditionJPA();
        minorMovementImpairmentChronic.setId(5);
        minorMovementImpairmentChronic.setName("Minor mobility impairment - Chronic");
        minorMovementImpairmentChronic.setChronic(true);
        entityManager.persist(minorMovementImpairmentChronic);
        entityManager.flush();
        allConditions.add(minorMovementImpairmentChronic);
        conditionProbabilityMap.put(minorMovementImpairmentChronic, 0.1);

        // Medium mobility impairment - Chronic
        ConditionJPA mediumMovementImpairmentChronic = new ConditionJPA();
        mediumMovementImpairmentChronic.setId(6);
        mediumMovementImpairmentChronic.setName("Medium mobility impairment - Chronic");
        mediumMovementImpairmentChronic.setChronic(true);
        entityManager.persist(mediumMovementImpairmentChronic);
        entityManager.flush();
        allConditions.add(mediumMovementImpairmentChronic);
        conditionProbabilityMap.put(mediumMovementImpairmentChronic, 0.1);

        // Severe mobility impairment - Chronic
        ConditionJPA severeMovementImpairmentChronic = new ConditionJPA();
        severeMovementImpairmentChronic.setId(7);
        severeMovementImpairmentChronic.setName("Severe mobility impairment - Chronic");
        severeMovementImpairmentChronic.setChronic(true);
        entityManager.persist(severeMovementImpairmentChronic);
        entityManager.flush();
        allConditions.add(severeMovementImpairmentChronic);
        conditionProbabilityMap.put(severeMovementImpairmentChronic, 0.15);

        // Wheelchair bound - Chronic
        ConditionJPA wheelchairBoundChronic = new ConditionJPA();
        wheelchairBoundChronic.setId(8);
        wheelchairBoundChronic.setName("Wheelchair bound - Chronic");
        wheelchairBoundChronic.setChronic(true);
        entityManager.persist(wheelchairBoundChronic);
        entityManager.flush();
        allConditions.add(wheelchairBoundChronic);
        conditionProbabilityMap.put(wheelchairBoundChronic, 0.15);

        // Impaired motor function - arms
        ConditionJPA impairedMotorFunctionArms = new ConditionJPA();
        impairedMotorFunctionArms.setId(9);
        impairedMotorFunctionArms.setName("Impaired motor function - arms");
        impairedMotorFunctionArms.setChronic(true);
        entityManager.persist(impairedMotorFunctionArms);
        entityManager.flush();
        allConditions.add(impairedMotorFunctionArms);
        conditionProbabilityMap.put(impairedMotorFunctionArms, 0.05);

        // Obesitas
        ConditionJPA obesistas = new ConditionJPA();
        obesistas.setId(10);
        obesistas.setName("Obesistas");
        obesistas.setChronic(true);
        entityManager.persist(obesistas);
        entityManager.flush();
        allConditions.add(obesistas);
        conditionProbabilityMap.put(obesistas, 0.05);


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
        allAdjustments.add(showerSeat);

        // Plateau elevator
        AdjustmentDefinitionJPA plateauElevator = new AdjustmentDefinitionJPA();
        plateauElevator.setId(2);
        plateauElevator.setName("Plateau elevator");
        plateauElevator.setAverageCost(4500.0);
        plateauElevator.setCostMargin(1000.0);
        entityManager.persist(plateauElevator);
        entityManager.flush();
        allAdjustments.add(plateauElevator);

        // Between steps
        AdjustmentDefinitionJPA betweenSteps = new AdjustmentDefinitionJPA();
        betweenSteps.setId(3);
        betweenSteps.setName("Between steps");
        betweenSteps.setAverageCost(850.0);
        betweenSteps.setCostMargin(25.0);
        entityManager.persist(betweenSteps);
        entityManager.flush();
        allAdjustments.add(betweenSteps);

        // Standing stair elevator
        AdjustmentDefinitionJPA stairElevatorStanding = new AdjustmentDefinitionJPA();
        stairElevatorStanding.setId(4);
        stairElevatorStanding.setName("Standing stair elevator");
        stairElevatorStanding.setAverageCost(5000.0);
        stairElevatorStanding.setCostMargin(500.0);
        entityManager.persist(stairElevatorStanding);
        entityManager.flush();
        allAdjustments.add(stairElevatorStanding);

        // Doorstep helper
        AdjustmentDefinitionJPA doorstepHelper = new AdjustmentDefinitionJPA();
        doorstepHelper.setId(5);
        doorstepHelper.setName("Doorstep helper");
        doorstepHelper.setAverageCost(210.0);
        doorstepHelper.setCostMargin(25.0);
        entityManager.persist(doorstepHelper);
        entityManager.flush();
        allAdjustments.add(doorstepHelper);

        // Armrests
        AdjustmentDefinitionJPA armrests = new AdjustmentDefinitionJPA();
        armrests.setId(6);
        armrests.setName("Armrests");
        armrests.setAverageCost(110.0);
        armrests.setCostMargin(25.0);
        entityManager.persist(armrests);
        entityManager.flush();
        allAdjustments.add(armrests);

        // Height adjustable kitchen
        AdjustmentDefinitionJPA heightAdjustableKitchen = new AdjustmentDefinitionJPA();
        heightAdjustableKitchen.setId(7);
        heightAdjustableKitchen.setName("Height adjustable kitchen");
        heightAdjustableKitchen.setAverageCost(10000.0);
        heightAdjustableKitchen.setCostMargin(3000.0);
        entityManager.persist(heightAdjustableKitchen);
        entityManager.flush();
        allAdjustments.add(heightAdjustableKitchen);

        // Bidet toilet seat
        AdjustmentDefinitionJPA bidetToiletSeat = new AdjustmentDefinitionJPA();
        bidetToiletSeat.setId(8);
        bidetToiletSeat.setName("Bidet toilet seat");
        bidetToiletSeat.setAverageCost(1750.0);
        bidetToiletSeat.setCostMargin(250.0);
        entityManager.persist(bidetToiletSeat);
        entityManager.flush();
        allAdjustments.add(bidetToiletSeat);

        // Bidet toilet
        AdjustmentDefinitionJPA bidetToilet = new AdjustmentDefinitionJPA();
        bidetToilet.setId(9);
        bidetToilet.setName("Bidet toilet");
        bidetToilet.setAverageCost(2700.0);
        bidetToilet.setCostMargin(750.0);
        entityManager.persist(bidetToilet);
        entityManager.flush();
        allAdjustments.add(bidetToilet);

        // Toilet back support
        AdjustmentDefinitionJPA toiletBackSupport = new AdjustmentDefinitionJPA();
        toiletBackSupport.setId(10);
        toiletBackSupport.setName("Toilet back support");
        toiletBackSupport.setAverageCost(275.0);
        toiletBackSupport.setCostMargin(7.5);
        entityManager.persist(toiletBackSupport);
        entityManager.flush();
        allAdjustments.add(toiletBackSupport);

        // Toilet seat with armrests
        AdjustmentDefinitionJPA toiletSeatWithArmrests = new AdjustmentDefinitionJPA();
        toiletSeatWithArmrests.setId(11);
        toiletSeatWithArmrests.setName("Toilet seat with armrests");
        toiletSeatWithArmrests.setAverageCost(240.0);
        toiletSeatWithArmrests.setCostMargin(40.0);
        entityManager.persist(toiletSeatWithArmrests);
        entityManager.flush();
        allAdjustments.add(toiletSeatWithArmrests);

        // Heightend toilet seat
        AdjustmentDefinitionJPA heightendToiletSeat = new AdjustmentDefinitionJPA();
        heightendToiletSeat.setId(12);
        heightendToiletSeat.setName("Heightend toilet seat");
        heightendToiletSeat.setAverageCost(100.0);
        heightendToiletSeat.setCostMargin(12.5);
        entityManager.persist(heightendToiletSeat);
        entityManager.flush();
        allAdjustments.add(heightendToiletSeat);

        // Wide toilet seat
        AdjustmentDefinitionJPA wideToiletSeat = new AdjustmentDefinitionJPA();
        wideToiletSeat.setId(13);
        wideToiletSeat.setName("Wide toilet seat");
        wideToiletSeat.setAverageCost(35.0);
        wideToiletSeat.setCostMargin(3.5);
        entityManager.persist(wideToiletSeat);
        entityManager.flush();
        allAdjustments.add(wideToiletSeat);

        // Hand grips
        AdjustmentDefinitionJPA handGrips = new AdjustmentDefinitionJPA();
        handGrips.setId(14);
        handGrips.setName("Hand grips");
        handGrips.setAverageCost(225.0);
        handGrips.setCostMargin(33.3);
        entityManager.persist(handGrips);
        entityManager.flush();
        allAdjustments.add(handGrips);

        // Heightend toilet
        AdjustmentDefinitionJPA heightendToilet = new AdjustmentDefinitionJPA();
        heightendToilet.setId(15);
        heightendToilet.setName("Heightend toilet");
        heightendToilet.setAverageCost(200.0);
        heightendToilet.setCostMargin(33.3);
        entityManager.persist(heightendToilet);
        entityManager.flush();
        allAdjustments.add(heightendToilet);

        // Electrical door
        AdjustmentDefinitionJPA electricalDoor = new AdjustmentDefinitionJPA();
        electricalDoor.setId(16);
        electricalDoor.setName("Electrical door");
        electricalDoor.setAverageCost(2200.0);
        electricalDoor.setCostMargin(666.7);
        entityManager.persist(electricalDoor);
        entityManager.flush();
        allAdjustments.add(electricalDoor);

        // Anti slip mat
        AdjustmentDefinitionJPA antiSlipMat = new AdjustmentDefinitionJPA();
        antiSlipMat.setId(17);
        antiSlipMat.setName("Anti slip mat");
        antiSlipMat.setAverageCost(30.0);
        antiSlipMat.setCostMargin(10.0);
        entityManager.persist(antiSlipMat);
        entityManager.flush();
        allAdjustments.add(antiSlipMat);

        // Handrail
        AdjustmentDefinitionJPA handrail = new AdjustmentDefinitionJPA();
        handrail.setId(18);
        handrail.setName("Handrail");
        handrail.setAverageCost(340.0);
        handrail.setCostMargin(100.0);
        entityManager.persist(handrail);
        entityManager.flush();
        allAdjustments.add(handrail);

        // Ceiling mounted patient elevator
        AdjustmentDefinitionJPA ceilingMountedPatientElevator = new AdjustmentDefinitionJPA();
        ceilingMountedPatientElevator.setId(19);
        ceilingMountedPatientElevator.setName("Ceiling mounted patient elevator");
        ceilingMountedPatientElevator.setAverageCost(6500.0);
        ceilingMountedPatientElevator.setCostMargin(1166.6);
        entityManager.persist(ceilingMountedPatientElevator);
        entityManager.flush();
        allAdjustments.add(ceilingMountedPatientElevator);

        // Passive patient elevator
        AdjustmentDefinitionJPA passivePatientElevator = new AdjustmentDefinitionJPA();
        passivePatientElevator.setId(20);
        passivePatientElevator.setName("Passive patient elevator");
        passivePatientElevator.setAverageCost(5000.0);
        passivePatientElevator.setCostMargin(666.7);
        entityManager.persist(passivePatientElevator);
        entityManager.flush();
        allAdjustments.add(passivePatientElevator);

        // Wall mounted patient elevator
        AdjustmentDefinitionJPA wallMountedPatientElevator = new AdjustmentDefinitionJPA();
        wallMountedPatientElevator.setId(21);
        wallMountedPatientElevator.setName("Wall mounted patient elevator");
        wallMountedPatientElevator.setAverageCost(5000.0);
        wallMountedPatientElevator.setCostMargin(1000.0);
        entityManager.persist(wallMountedPatientElevator);
        entityManager.flush();
        allAdjustments.add(wallMountedPatientElevator);

        // Lifting sling
        AdjustmentDefinitionJPA liftingSling = new AdjustmentDefinitionJPA();
        liftingSling.setId(22);
        liftingSling.setName("Lifting sling");
        liftingSling.setAverageCost(825.0);
        liftingSling.setCostMargin(225.0);
        entityManager.persist(liftingSling);
        entityManager.flush();
        allAdjustments.add(liftingSling);

        // Support pole
        AdjustmentDefinitionJPA supportPole = new AdjustmentDefinitionJPA();
        supportPole.setId(23);
        supportPole.setName("Support pole");
        supportPole.setAverageCost(375.0);
        supportPole.setCostMargin(41.7);
        entityManager.persist(supportPole);
        entityManager.flush();
        allAdjustments.add(supportPole);

        // Stair elevator seat
        AdjustmentDefinitionJPA stairElevatorSeat = new AdjustmentDefinitionJPA();
        stairElevatorSeat.setId(24);
        stairElevatorSeat.setName("Stair elevator seat");
        stairElevatorSeat.setAverageCost(5000.0);
        stairElevatorSeat.setCostMargin(500.0);
        entityManager.persist(stairElevatorSeat);
        entityManager.flush();
        allAdjustments.add(stairElevatorSeat);


        /*
        Connect conditions and adjustments
         */

        // Adjustments for minor movement impairment
        minorMovementImpairment.setAdjustments(new ArrayList<AdjustmentDefinitionJPA>(3));
        minorMovementImpairment.getAdjustments().add(antiSlipMat);
        minorMovementImpairment.getAdjustments().add(showerSeat);
        minorMovementImpairment.getAdjustments().add(armrests);
        entityManager.merge(minorMovementImpairment);
        entityManager.flush();

        // Adjustments for medium movement impairment
        mediumMovementImpairment.setAdjustments(new ArrayList<AdjustmentDefinitionJPA>(6));
        mediumMovementImpairment.getAdjustments().add(antiSlipMat);
        mediumMovementImpairment.getAdjustments().add(showerSeat);
        mediumMovementImpairment.getAdjustments().add(armrests);
        mediumMovementImpairment.getAdjustments().add(toiletSeatWithArmrests);
        mediumMovementImpairment.getAdjustments().add(heightendToiletSeat);
        mediumMovementImpairment.getAdjustments().add(betweenSteps);
        entityManager.merge(mediumMovementImpairment);
        entityManager.flush();

        // Adjustments for severe movement impairment
        severeMovementImpairment.setAdjustments(new ArrayList<AdjustmentDefinitionJPA>());
        severeMovementImpairment.getAdjustments().add(antiSlipMat);
        severeMovementImpairment.getAdjustments().add(showerSeat);
        severeMovementImpairment.getAdjustments().add(armrests);
        severeMovementImpairment.getAdjustments().add(toiletBackSupport);
        severeMovementImpairment.getAdjustments().add(toiletSeatWithArmrests);
        severeMovementImpairment.getAdjustments().add(heightendToiletSeat);
        severeMovementImpairment.getAdjustments().add(doorstepHelper);
        severeMovementImpairment.getAdjustments().add(supportPole);
        severeMovementImpairment.getAdjustments().add(stairElevatorSeat);
        severeMovementImpairment.getAdjustments().add(stairElevatorStanding);
        entityManager.merge(severeMovementImpairment);
        entityManager.flush();

        // Adjustments for wheelchair bound
        wheelchairBound.setAdjustments(new ArrayList<AdjustmentDefinitionJPA>(7));
        wheelchairBound.getAdjustments().add(doorstepHelper);
        wheelchairBound.getAdjustments().add(handGrips);
        wheelchairBound.getAdjustments().add(supportPole);
        wheelchairBound.getAdjustments().add(stairElevatorSeat);
        wheelchairBound.getAdjustments().add(showerSeat);
        wheelchairBound.getAdjustments().add(passivePatientElevator);
        wheelchairBound.getAdjustments().add(heightendToiletSeat);
        entityManager.merge(wheelchairBound);
        entityManager.flush();

        // Adjustments for chronic minor movement impairment
        minorMovementImpairmentChronic.setAdjustments(new ArrayList<AdjustmentDefinitionJPA>(7));
        minorMovementImpairmentChronic.getAdjustments().add(antiSlipMat);
        minorMovementImpairmentChronic.getAdjustments().add(showerSeat);
        minorMovementImpairmentChronic.getAdjustments().add(armrests);
        minorMovementImpairmentChronic.getAdjustments().add(heightendToiletSeat);
        minorMovementImpairmentChronic.getAdjustments().add(doorstepHelper);
        minorMovementImpairmentChronic.getAdjustments().add(handGrips);
        minorMovementImpairmentChronic.getAdjustments().add(handrail);
        entityManager.merge(minorMovementImpairmentChronic);
        entityManager.flush();

        // Adjustments for chronic medium movement impairment
        mediumMovementImpairmentChronic.setAdjustments(new ArrayList<AdjustmentDefinitionJPA>());
        mediumMovementImpairmentChronic.getAdjustments().add(antiSlipMat);
        mediumMovementImpairmentChronic.getAdjustments().add(showerSeat);
        mediumMovementImpairmentChronic.getAdjustments().add(armrests);
        mediumMovementImpairmentChronic.getAdjustments().add(handGrips);
        mediumMovementImpairmentChronic.getAdjustments().add(handrail);
        mediumMovementImpairmentChronic.getAdjustments().add(betweenSteps);
        mediumMovementImpairmentChronic.getAdjustments().add(doorstepHelper);
        mediumMovementImpairmentChronic.getAdjustments().add(heightendToilet);
        mediumMovementImpairmentChronic.getAdjustments().add(toiletBackSupport);
        mediumMovementImpairmentChronic.getAdjustments().add(stairElevatorStanding);
        entityManager.merge(mediumMovementImpairmentChronic);
        entityManager.flush();

        // Adjustments for chronic severe movement impairment
        severeMovementImpairmentChronic.setAdjustments(new ArrayList<AdjustmentDefinitionJPA>(12));
        severeMovementImpairmentChronic.getAdjustments().add(antiSlipMat);
        severeMovementImpairmentChronic.getAdjustments().add(showerSeat);
        severeMovementImpairmentChronic.getAdjustments().add(armrests);
        severeMovementImpairmentChronic.getAdjustments().add(handGrips);
        severeMovementImpairmentChronic.getAdjustments().add(handrail);
        severeMovementImpairmentChronic.getAdjustments().add(doorstepHelper);
        severeMovementImpairmentChronic.getAdjustments().add(heightendToilet);
        severeMovementImpairmentChronic.getAdjustments().add(toiletBackSupport);
        severeMovementImpairmentChronic.getAdjustments().add(bidetToiletSeat);
        severeMovementImpairmentChronic.getAdjustments().add(stairElevatorSeat);
        severeMovementImpairmentChronic.getAdjustments().add(passivePatientElevator);
        severeMovementImpairmentChronic.getAdjustments().add(liftingSling);
        entityManager.merge(severeMovementImpairmentChronic);
        entityManager.flush();

        // Adjustments for chronic wheelchair bound
        wheelchairBoundChronic.setAdjustments(new ArrayList<AdjustmentDefinitionJPA>(16));
        wheelchairBoundChronic.getAdjustments().add(showerSeat);
        wheelchairBoundChronic.getAdjustments().add(handGrips);
        wheelchairBoundChronic.getAdjustments().add(handrail);
        wheelchairBoundChronic.getAdjustments().add(supportPole);
        wheelchairBoundChronic.getAdjustments().add(doorstepHelper);
        wheelchairBoundChronic.getAdjustments().add(heightAdjustableKitchen);
        wheelchairBoundChronic.getAdjustments().add(electricalDoor);
        wheelchairBoundChronic.getAdjustments().add(plateauElevator);
        wheelchairBoundChronic.getAdjustments().add(stairElevatorSeat);
        wheelchairBoundChronic.getAdjustments().add(bidetToilet);
        wheelchairBoundChronic.getAdjustments().add(toiletBackSupport);
        wheelchairBoundChronic.getAdjustments().add(heightendToilet);
        wheelchairBoundChronic.getAdjustments().add(ceilingMountedPatientElevator);
        wheelchairBoundChronic.getAdjustments().add(wallMountedPatientElevator);
        wheelchairBoundChronic.getAdjustments().add(passivePatientElevator);
        wheelchairBoundChronic.getAdjustments().add(liftingSling);
        entityManager.merge(wheelchairBoundChronic);
        entityManager.flush();

        // Adjustments for impaired motor functions in the arms
        impairedMotorFunctionArms.setAdjustments(new ArrayList<AdjustmentDefinitionJPA>(4));
        impairedMotorFunctionArms.getAdjustments().add(toiletSeatWithArmrests);
        impairedMotorFunctionArms.getAdjustments().add(armrests);
        impairedMotorFunctionArms.getAdjustments().add(bidetToilet);
        impairedMotorFunctionArms.getAdjustments().add(bidetToiletSeat);
        entityManager.merge(impairedMotorFunctionArms);
        entityManager.flush();

        // Adjustments for obesistas
        obesistas.setAdjustments(new ArrayList<AdjustmentDefinitionJPA>(4));
        obesistas.getAdjustments().add(wideToiletSeat);
        obesistas.getAdjustments().add(handGrips);
        obesistas.getAdjustments().add(bidetToilet);
        obesistas.getAdjustments().add(armrests);
        entityManager.merge(obesistas);
        entityManager.flush();


        LOGGER.info("End initialDataLoad();");

    }


    public void createApplicant(long numberOfApplications, long householdSize) {

        // Create a new person
        PersonJPA person = new PersonJPA();
        baseBsn++;
        person.setBsn(baseBsn);
        entityManager.persist(person);
        entityManager.flush();

        // Create new housing situation
        HousingSituationJPA housingSituation = new HousingSituationJPA();
        // Floor is taken from the range 0 ... 3 in order to create an even share of housing situations which are suitable or unsuitable
        housingSituation.setFloor((short) Math.floor(getRandom().nextDouble() * 4));
        housingSituation.setElevator(getRandom().nextBoolean());
        housingSituation.setResidents(new ArrayList<PersonJPA>());
        housingSituation.getResidents().add(person);
        entityManager.merge(housingSituation);
        entityManager.flush();

        // Generate residents, if applicable
        if (householdSize > 1) {
            for (long i = 0; i < householdSize - 1; i++) {
                PersonJPA resident = new PersonJPA();
                baseBsn++;
                resident.setBsn(baseBsn);
                housingSituation.getResidents().add(resident);
                entityManager.merge(housingSituation);
                entityManager.flush();
            }
        }


        // Generate the given number of applications
        for (long i = 0; i < numberOfApplications; i++) {
            createApplicationWithAdvice(person, housingSituation);
        }
    }


    public void createApplicationWithAdvice(PersonJPA person, HousingSituationJPA housingSituationJPA) {

        double remainingBudget = BUDGET;
        List<ConditionJPA> currentConditions = new ArrayList<ConditionJPA>();
        List<ConditionJPA> futureConditions = new ArrayList<ConditionJPA>();

        // Generate conditions
        if (person.getAdvice() == null || person.getAdvice().size() == 0) {

            // If there is no previous advice, create a new list
            person.setAdvice(new ArrayList<AdviceJPA>());

            // Generate one or more current conditions for the person
            currentConditions = getRandomConditionList(1, 3);

            // Generate 0 or more future conditions which are not part of the current conditions
            futureConditions = getRandomConditionList(0, 2);
            futureConditions.removeAll(currentConditions);

        } else {
            // If there are previous applications they are taken into account for the budget
            remainingBudget -= getTotalSpentFromPerson(person);

            // If there are predicted conditions from previous advice select one of these at random
            List<ConditionJPA> previousFutureConditions = person.getAdvice().get(-1).getFutureConditions();
            if (previousFutureConditions != null && previousFutureConditions.size() >= 1) {
                currentConditions.add(previousFutureConditions.get(getRandom().nextInt(previousFutureConditions.size())));
            }
        }

        // Create a new advice
        AdviceJPA advice = new AdviceJPA();
        advice.setApplicant(person);
        advice.setGoAhead(true); // Always true, or else the application is not taken into account
        advice.setCurrentConditions(currentConditions);
        advice.setFutureConditions(futureConditions);

        // Create a list of valid adjustment definitions
        List<AdjustmentDefinitionJPA> validAdjustmentDefinitions = new ArrayList<AdjustmentDefinitionJPA>();
        for (ConditionJPA currentCondition : currentConditions) {
            for (AdjustmentDefinitionJPA adjustmentDefinition : currentCondition.getAdjustments()) {
                if (!validAdjustmentDefinitions.contains(adjustmentDefinition)) {
                    validAdjustmentDefinitions.add(adjustmentDefinition);
                }
            }
        }

        // Based upon the valid adjustment definition list create a list of adjustments to include in the application
        List<AdjustmentJPA> proposedAdjustments = new ArrayList<AdjustmentJPA>();
        int randomLength = getRandom().nextInt(5);
        for (int i = 0; i < ((randomLength > 1) ? randomLength : 1); i++) {

            // Select random adjustment definition from the valid definitions
            AdjustmentDefinitionJPA selectedAdjustment = validAdjustmentDefinitions.get(getRandom().nextInt(validAdjustmentDefinitions.size()));

            // Create new adjustment
            AdjustmentJPA proposedAdjustment = new AdjustmentJPA();
            proposedAdjustment.setAdjustmentDefinition(selectedAdjustment);

            // Calculate cost of the definition
            proposedAdjustment.setActualCost(calculateAbsoluteGaussian(selectedAdjustment.getAverageCost(), selectedAdjustment.getCostMargin()));

            // Add the proposed adjustment to the list
            proposedAdjustments.add(proposedAdjustment);
        }

        // Judge the application
        WmoDecisionJPA decision = judgeApplication(person, housingSituationJPA, proposedAdjustments, advice, remainingBudget);

        // Add the generated decision to the proposed adjustments
        for (AdjustmentJPA proposedAdjustment : proposedAdjustments) {
            proposedAdjustment.setDecision(decision);
        }

        // Merge one of the proposed adjustments with the decision included
        entityManager.merge(proposedAdjustments.get(0));
        entityManager.flush();

        entityManager.clear();

    }


    public WmoDecisionJPA judgeApplication(PersonJPA person, HousingSituationJPA housingSituationJPA, List<AdjustmentJPA> proposedAdjustments, AdviceJPA advice, double remainingBudget) {

        // Create the decision
        WmoDecisionJPA decision = new WmoDecisionJPA();
        decision.setAdjustments(proposedAdjustments);
        decision.setAdvice(advice);

        // Check for go ahead in advice
        if (advice.isGoAhead()) {

            // Check total costs against the remaining budget
            double totalCost = 0.0;
            for (AdjustmentJPA proposedAdjustment : proposedAdjustments) {
                totalCost += proposedAdjustment.getActualCost();
            }

            if (remainingBudget > totalCost) {

                // TODO : check for adjustment type and housing situation


                // Application is valid in theory, add small chance for exception
                if (getRandom().nextDouble() <= 0.005) {
                    decision.setGranted(false);
                    decision.setException(true);
                    decision.setReason("Application rejected due to extrernal circumstances.");
                } else {
                    decision.setGranted(true);
                    decision.setException(false);
                    decision.setReason("Application granted.");
                }

            } else {

                // Application is technically invalid, add small chance for exception
                if (getRandom().nextDouble() <= 0.01) {
                    decision.setGranted(true);
                    decision.setException(true);
                    decision.setReason("Application granted due to external circumstances.");
                } else {


                    // TODO : Expand with partial payments


                    decision.setGranted(false);
                    decision.setException(false);
                    decision.setReason("Application rejected due to high costs.");
                }

            }

        } else {
            // In the case of no go ahead, the application will not be granted
            decision.setGranted(false);
            decision.setException(false);
            decision.setReason("Application rejected due to no go ahead from Idicatie Advies Bureau.");
        }


        return decision;
    }


    /**
     * Shortcut method for the calculation of an absolute Gaussian distribution.
     *
     * @param mean  - Mean of the Gaussian distribution (BEFORE absolute!)
     * @param sigma - Standard deviation of the Gaussian distribution
     * @return A value on the given absolute Gaussian distribution
     */
    private double calculateAbsoluteGaussian(double mean, double sigma) {
        return Math.abs(getRandom().nextGaussian() * sigma + mean);
    }


    /**
     * Method which returns the total spent on a person in all previously granted adjustments.
     *
     * @param person - Person for whom the total needs to be calculated
     * @return Double with the total spent on the given person
     */
    private double getTotalSpentFromPerson(PersonJPA person) {
        double totalSpent = 0.0;

        if (person.getAdvice() != null && person.getAdvice().size() >= 1) {
            for (AdviceJPA advice : person.getAdvice()) {
                if (advice.isGoAhead() && advice.getDecision().isGranted()) {
                    for (AdjustmentJPA adjustment : advice.getDecision().getAdjustments()) {
                        totalSpent += adjustment.getActualCost();
                    }
                }
            }
        }

        return totalSpent;
    }


    private ConditionJPA getConditionRandomly() {
        ConditionJPA randomCondition = null;
        double rand = getRandom().nextDouble();
        double currentRand = 0.0;

        for (ConditionJPA conditionJPA : conditionProbabilityMap.keySet()) {
            currentRand += conditionProbabilityMap.get(conditionJPA);
            if (rand < currentRand) {
                randomCondition = conditionJPA;
                break;
            }
        }

        return randomCondition;
    }


    private List<ConditionJPA> getRandomConditionList(int minLength, int maxLength) {

        List<ConditionJPA> conditionList = new ArrayList<ConditionJPA>(maxLength);
        int randomLength = getRandom().nextInt(maxLength + 1);
        for (int i = 0; i < ((randomLength > minLength) ? randomLength : minLength); i++) {
            ConditionJPA randomCondition = getConditionRandomly();
            if (!conditionList.contains(randomCondition)) {
                conditionList.add(randomCondition);
            }
        }

        return conditionList;
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
