package nl.h2.ejb;

import nl.h2.ejb.schema.*;
import nl.h2.utils.exception.ApplicatieException;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

@Stateless
@LocalBean
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class TrainingDataGenerator {

    public TrainingDataGenerator() {
    }

    @PersistenceContext(unitName = "tdg.database")
    private EntityManager entityManager;


    private final double BUDGET = 7200.0;
    private int baseBsn = 901454956;
    private Random random = null;
    private Map<ConditionJPA, Double> conditionProbabilityMap = new HashMap<ConditionJPA, Double>();
    private List<ConditionJPA> allConditions = new ArrayList<ConditionJPA>();
    private List<AdjustmentDefinitionJPA> allAdjustments = new ArrayList<AdjustmentDefinitionJPA>();
    private List<ContractorJPA> allContractors = new ArrayList<ContractorJPA>();
    private int totalApplications;


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
            throw new ApplicatieException(Constants.ERR_NUMBER_OF_RECORDS_TOO_LOW);
        }
        if (meanNumberOfApplications < 1) {
            throw new ApplicatieException(Constants.ERR_MEAN_NUMBER_OF_APPLICATIONS_TOO_LOW);
        }
        if (sigmaNumberOfApplications <= 0) {
            throw new ApplicatieException(Constants.ERR_SIGMA_NUMBER_OF_APPLICATIONS_TOO_LOW);
        }
        if (meanHousholdSize < 1) {
            throw new ApplicatieException(Constants.ERR_MEAN_HOUSEHOLD_SIZE_TOO_LOW);
        }
        if (sigmaHouseholdSize < 0) {
            throw new ApplicatieException(Constants.ERR_SIGMA_HOUSEHOLD_SIZE_TOO_LOW);
        }

        // Seed random function if seed is given
        if (seed != null) {
            getRandom().setSeed(seed);
        }

        System.out.println("Start generate training data");

        totalApplications = 0;
        boolean notDone = true;
        int totalNumberOfApplications = 0;

        while (notDone) {
            // For each applicant, determine the number of applications and the household size
            long numberOfApplications = Math.round(calculateAbsoluteGaussian(meanNumberOfApplications, sigmaNumberOfApplications));
            numberOfApplications = numberOfApplications == 0 ? 1 : numberOfApplications;
            long householdSize = Math.round(calculateAbsoluteGaussian(meanHousholdSize, sigmaHouseholdSize));
            householdSize = householdSize == 0 ? 1 : householdSize;

            // Determine if the number of applications is valid in order to maintain the total number of records given
            if (numberOfRecords - (totalNumberOfApplications + numberOfApplications) >= 0) {

                // Create the applicant
                createApplicant(numberOfApplications, householdSize);

                // Increase total number of applications
                totalNumberOfApplications += numberOfApplications;

                // If the number of records is equal to the number of applications, break
                if (totalNumberOfApplications == numberOfRecords) {
                    notDone = false;
                }

            } else {

                // Create applicant with remaining number of applications and break
                createApplicant(numberOfRecords - totalNumberOfApplications, householdSize);
                notDone = false;
            }
        }

        System.out.println("Total number of applications generated: " + totalApplications);
    }


    /**
     * Method to clear all data present in the data
     *
     * @throws Exception
     */
    public void clearData() throws Exception {

        System.out.println("Start clear data");

        // Clear all class data
        conditionProbabilityMap.clear();
        allConditions.clear();
        allAdjustments.clear();

        // Prepare all removal queries
        Query removeConditionsAdjustments = entityManager.createNativeQuery("DELETE FROM adjustment_definition_to_condition");
        Query removeAdviceCurrentConditions = entityManager.createNativeQuery("DELETE FROM advice_to_current_condition");
        Query removeAdviceFutureConditions = entityManager.createNativeQuery("DELETE FROM advice_to_future_condition");
        Query removeHousingAdjustments = entityManager.createNativeQuery("DELETE FROM housing_situation_to_adjustment");
        Query removeResidents = entityManager.createNativeQuery("DELETE FROM person_to_housing_situation");
        Query removeConditions = entityManager.createNativeQuery("DELETE FROM condition");
        Query removeAdjustments = entityManager.createNativeQuery("DELETE FROM adjustment");
        Query removeWmoDecisions = entityManager.createNativeQuery("DELETE FROM wmo_decision");
        Query removeAdvice = entityManager.createNativeQuery("DELETE FROM advice");
        Query removeApplication = entityManager.createNativeQuery("DELETE FROM application");
        Query removeBag = entityManager.createNativeQuery("DELETE FROM bag");
        Query removeContractor = entityManager.createNativeQuery("DELETE FROM contractor");
        Query removeAdresses = entityManager.createNativeQuery("DELETE FROM address");
        Query removeHousingSituations = entityManager.createNativeQuery("DELETE FROM housing_situation");
        Query removePersons = entityManager.createNativeQuery("DELETE FROM person");
        Query removeAdjustmentDefinitions = entityManager.createNativeQuery("DELETE FROM adjustment_definition");

        // Execute all removal queries
        removeConditionsAdjustments.executeUpdate();
        removeAdviceCurrentConditions.executeUpdate();
        removeAdviceFutureConditions.executeUpdate();
        removeHousingAdjustments.executeUpdate();
        removeResidents.executeUpdate();
        removeConditions.executeUpdate();
        removeAdjustments.executeUpdate();
        removeWmoDecisions.executeUpdate();
        removeAdvice.executeUpdate();
        removeApplication.executeUpdate();
        removeBag.executeUpdate();
        removeContractor.executeUpdate();
        removeAdresses.executeUpdate();
        removeHousingSituations.executeUpdate();
        removePersons.executeUpdate();
        removeAdjustmentDefinitions.executeUpdate();

        System.out.println("Done with clear data");
    }


    /**
     * Method for the initial data load for the training data generator. Creates a selection of conditions and
     * adjustment definition and links them together. Also fills the class variables with the initial data.
     *
     * @throws Exception
     */
    public void initialDataLoad() throws Exception {

        System.out.println("Start initial data load");

        /*
        Conditions
         */

        // Minor mobility impairment
        ConditionJPA minorMovementImpairment = new ConditionJPA();
        minorMovementImpairment.setConditionId(1L);
        minorMovementImpairment.setName("Minor mobility impairment");
        minorMovementImpairment.setChronic(false);
        entityManager.persist(minorMovementImpairment);
        entityManager.flush();
        allConditions.add(minorMovementImpairment);
        conditionProbabilityMap.put(minorMovementImpairment, 0.15);

        // Medium mobility impairment
        ConditionJPA mediumMovementImpairment = new ConditionJPA();
        mediumMovementImpairment.setConditionId(2L);
        mediumMovementImpairment.setName("Medium mobility impairment");
        mediumMovementImpairment.setChronic(false);
        entityManager.persist(mediumMovementImpairment);
        entityManager.flush();
        allConditions.add(mediumMovementImpairment);
        conditionProbabilityMap.put(mediumMovementImpairment, 0.1);

        // Severe mobility impairment
        ConditionJPA severeMovementImpairment = new ConditionJPA();
        severeMovementImpairment.setConditionId(3L);
        severeMovementImpairment.setName("Severe mobility impairment");
        severeMovementImpairment.setChronic(false);
        entityManager.persist(severeMovementImpairment);
        entityManager.flush();
        allConditions.add(severeMovementImpairment);
        conditionProbabilityMap.put(severeMovementImpairment, 0.05);

        // Wheelchair bound
        ConditionJPA wheelchairBound = new ConditionJPA();
        wheelchairBound.setConditionId(4L);
        wheelchairBound.setName("Wheelchair bound");
        wheelchairBound.setChronic(false);
        entityManager.persist(wheelchairBound);
        entityManager.flush();
        allConditions.add(wheelchairBound);
        conditionProbabilityMap.put(wheelchairBound, 0.2);

        // Minor mobility impairment - Chronic
        ConditionJPA minorMovementImpairmentChronic = new ConditionJPA();
        minorMovementImpairmentChronic.setConditionId(5L);
        minorMovementImpairmentChronic.setName("Minor mobility impairment - Chronic");
        minorMovementImpairmentChronic.setChronic(true);
        entityManager.persist(minorMovementImpairmentChronic);
        entityManager.flush();
        allConditions.add(minorMovementImpairmentChronic);
        conditionProbabilityMap.put(minorMovementImpairmentChronic, 0.1);

        // Medium mobility impairment - Chronic
        ConditionJPA mediumMovementImpairmentChronic = new ConditionJPA();
        mediumMovementImpairmentChronic.setConditionId(6L);
        mediumMovementImpairmentChronic.setName("Medium mobility impairment - Chronic");
        mediumMovementImpairmentChronic.setChronic(true);
        entityManager.persist(mediumMovementImpairmentChronic);
        entityManager.flush();
        allConditions.add(mediumMovementImpairmentChronic);
        conditionProbabilityMap.put(mediumMovementImpairmentChronic, 0.1);

        // Severe mobility impairment - Chronic
        ConditionJPA severeMovementImpairmentChronic = new ConditionJPA();
        severeMovementImpairmentChronic.setConditionId(7L);
        severeMovementImpairmentChronic.setName("Severe mobility impairment - Chronic");
        severeMovementImpairmentChronic.setChronic(true);
        entityManager.persist(severeMovementImpairmentChronic);
        entityManager.flush();
        allConditions.add(severeMovementImpairmentChronic);
        conditionProbabilityMap.put(severeMovementImpairmentChronic, 0.15);

        // Wheelchair bound - Chronic
        ConditionJPA wheelchairBoundChronic = new ConditionJPA();
        wheelchairBoundChronic.setConditionId(8L);
        wheelchairBoundChronic.setName("Wheelchair bound - Chronic");
        wheelchairBoundChronic.setChronic(true);
        entityManager.persist(wheelchairBoundChronic);
        entityManager.flush();
        allConditions.add(wheelchairBoundChronic);
        conditionProbabilityMap.put(wheelchairBoundChronic, 0.15);

        // Impaired motor function - arms
        ConditionJPA impairedMotorFunctionArms = new ConditionJPA();
        impairedMotorFunctionArms.setConditionId(9L);
        impairedMotorFunctionArms.setName("Impaired motor function - arms");
        impairedMotorFunctionArms.setChronic(true);
        entityManager.persist(impairedMotorFunctionArms);
        entityManager.flush();
        allConditions.add(impairedMotorFunctionArms);
        conditionProbabilityMap.put(impairedMotorFunctionArms, 0.05);

        // Obesitas
        ConditionJPA obesistas = new ConditionJPA();
        obesistas.setConditionId(10L);
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
        showerSeat.setAdjustmentDefinitionId(1L);
        showerSeat.setName("Shower seat");
        showerSeat.setAverageCost(400.0);
        showerSeat.setCostMargin(150.0);
        showerSeat.setConditions(new ArrayList<ConditionJPA>());
        entityManager.persist(showerSeat);
        entityManager.flush();
        allAdjustments.add(showerSeat);

        // Plateau elevator
        AdjustmentDefinitionJPA plateauElevator = new AdjustmentDefinitionJPA();
        plateauElevator.setAdjustmentDefinitionId(2L);
        plateauElevator.setName("Plateau elevator");
        plateauElevator.setAverageCost(4500.0);
        plateauElevator.setCostMargin(1000.0);
        plateauElevator.setConditions(new ArrayList<ConditionJPA>());
        entityManager.persist(plateauElevator);
        entityManager.flush();
        allAdjustments.add(plateauElevator);

        // Between steps
        AdjustmentDefinitionJPA betweenSteps = new AdjustmentDefinitionJPA();
        betweenSteps.setAdjustmentDefinitionId(3L);
        betweenSteps.setName("Between steps");
        betweenSteps.setAverageCost(850.0);
        betweenSteps.setCostMargin(25.0);
        betweenSteps.setConditions(new ArrayList<ConditionJPA>());
        entityManager.persist(betweenSteps);
        entityManager.flush();
        allAdjustments.add(betweenSteps);

        // Standing stair elevator
        AdjustmentDefinitionJPA stairElevatorStanding = new AdjustmentDefinitionJPA();
        stairElevatorStanding.setAdjustmentDefinitionId(4L);
        stairElevatorStanding.setName("Stair elevator standing");
        stairElevatorStanding.setAverageCost(5000.0);
        stairElevatorStanding.setCostMargin(500.0);
        stairElevatorStanding.setConditions(new ArrayList<ConditionJPA>());
        entityManager.persist(stairElevatorStanding);
        entityManager.flush();
        allAdjustments.add(stairElevatorStanding);

        // Doorstep helper
        AdjustmentDefinitionJPA doorstepHelper = new AdjustmentDefinitionJPA();
        doorstepHelper.setAdjustmentDefinitionId(5L);
        doorstepHelper.setName("Doorstep helper");
        doorstepHelper.setAverageCost(210.0);
        doorstepHelper.setCostMargin(25.0);
        doorstepHelper.setConditions(new ArrayList<ConditionJPA>());
        entityManager.persist(doorstepHelper);
        entityManager.flush();
        allAdjustments.add(doorstepHelper);

        // Armrests
        AdjustmentDefinitionJPA armrests = new AdjustmentDefinitionJPA();
        armrests.setAdjustmentDefinitionId(6L);
        armrests.setName("Armrests");
        armrests.setAverageCost(110.0);
        armrests.setCostMargin(25.0);
        armrests.setConditions(new ArrayList<ConditionJPA>());
        entityManager.persist(armrests);
        entityManager.flush();
        allAdjustments.add(armrests);

        // Height adjustable kitchen
        AdjustmentDefinitionJPA heightAdjustableKitchen = new AdjustmentDefinitionJPA();
        heightAdjustableKitchen.setAdjustmentDefinitionId(7L);
        heightAdjustableKitchen.setName("Height adjustable kitchen");
        heightAdjustableKitchen.setAverageCost(10000.0);
        heightAdjustableKitchen.setCostMargin(3000.0);
        heightAdjustableKitchen.setConditions(new ArrayList<ConditionJPA>());
        entityManager.persist(heightAdjustableKitchen);
        entityManager.flush();
        allAdjustments.add(heightAdjustableKitchen);

        // Bidet toilet seat
        AdjustmentDefinitionJPA bidetToiletSeat = new AdjustmentDefinitionJPA();
        bidetToiletSeat.setAdjustmentDefinitionId(8L);
        bidetToiletSeat.setName("Bidet toilet seat");
        bidetToiletSeat.setAverageCost(1750.0);
        bidetToiletSeat.setCostMargin(250.0);
        bidetToiletSeat.setConditions(new ArrayList<ConditionJPA>());
        entityManager.persist(bidetToiletSeat);
        entityManager.flush();
        allAdjustments.add(bidetToiletSeat);

        // Bidet toilet
        AdjustmentDefinitionJPA bidetToilet = new AdjustmentDefinitionJPA();
        bidetToilet.setAdjustmentDefinitionId(9L);
        bidetToilet.setName("Bidet toilet");
        bidetToilet.setAverageCost(2700.0);
        bidetToilet.setCostMargin(750.0);
        bidetToilet.setConditions(new ArrayList<ConditionJPA>());
        entityManager.persist(bidetToilet);
        entityManager.flush();
        allAdjustments.add(bidetToilet);

        // Toilet back support
        AdjustmentDefinitionJPA toiletBackSupport = new AdjustmentDefinitionJPA();
        toiletBackSupport.setAdjustmentDefinitionId(10L);
        toiletBackSupport.setName("Toilet back support");
        toiletBackSupport.setAverageCost(275.0);
        toiletBackSupport.setCostMargin(7.5);
        toiletBackSupport.setConditions(new ArrayList<ConditionJPA>());
        entityManager.persist(toiletBackSupport);
        entityManager.flush();
        allAdjustments.add(toiletBackSupport);

        // Toilet seat with armrests
        AdjustmentDefinitionJPA toiletSeatWithArmrests = new AdjustmentDefinitionJPA();
        toiletSeatWithArmrests.setAdjustmentDefinitionId(11L);
        toiletSeatWithArmrests.setName("Toilet seat with armrests");
        toiletSeatWithArmrests.setAverageCost(240.0);
        toiletSeatWithArmrests.setCostMargin(40.0);
        toiletSeatWithArmrests.setConditions(new ArrayList<ConditionJPA>());
        entityManager.persist(toiletSeatWithArmrests);
        entityManager.flush();
        allAdjustments.add(toiletSeatWithArmrests);

        // Heightend toilet seat
        AdjustmentDefinitionJPA heightendToiletSeat = new AdjustmentDefinitionJPA();
        heightendToiletSeat.setAdjustmentDefinitionId(12L);
        heightendToiletSeat.setName("Heightend toilet seat");
        heightendToiletSeat.setAverageCost(100.0);
        heightendToiletSeat.setCostMargin(12.5);
        heightendToiletSeat.setConditions(new ArrayList<ConditionJPA>());
        entityManager.persist(heightendToiletSeat);
        entityManager.flush();
        allAdjustments.add(heightendToiletSeat);

        // Wide toilet seat
        AdjustmentDefinitionJPA wideToiletSeat = new AdjustmentDefinitionJPA();
        wideToiletSeat.setAdjustmentDefinitionId(13L);
        wideToiletSeat.setName("Wide toilet seat");
        wideToiletSeat.setAverageCost(35.0);
        wideToiletSeat.setCostMargin(3.5);
        wideToiletSeat.setConditions(new ArrayList<ConditionJPA>());
        entityManager.persist(wideToiletSeat);
        entityManager.flush();
        allAdjustments.add(wideToiletSeat);

        // Hand grips
        AdjustmentDefinitionJPA handGrips = new AdjustmentDefinitionJPA();
        handGrips.setAdjustmentDefinitionId(14L);
        handGrips.setName("Hand grips");
        handGrips.setAverageCost(225.0);
        handGrips.setCostMargin(33.3);
        handGrips.setConditions(new ArrayList<ConditionJPA>());
        entityManager.persist(handGrips);
        entityManager.flush();
        allAdjustments.add(handGrips);

        // Heightend toilet
        AdjustmentDefinitionJPA heightendToilet = new AdjustmentDefinitionJPA();
        heightendToilet.setAdjustmentDefinitionId(15L);
        heightendToilet.setName("Heightend toilet");
        heightendToilet.setAverageCost(200.0);
        heightendToilet.setCostMargin(33.3);
        heightendToilet.setConditions(new ArrayList<ConditionJPA>());
        entityManager.persist(heightendToilet);
        entityManager.flush();
        allAdjustments.add(heightendToilet);

        // Electrical door
        AdjustmentDefinitionJPA electricalDoor = new AdjustmentDefinitionJPA();
        electricalDoor.setAdjustmentDefinitionId(16L);
        electricalDoor.setName("Electrical door");
        electricalDoor.setAverageCost(2200.0);
        electricalDoor.setCostMargin(666.7);
        electricalDoor.setConditions(new ArrayList<ConditionJPA>());
        entityManager.persist(electricalDoor);
        entityManager.flush();
        allAdjustments.add(electricalDoor);

        // Anti slip mat
        AdjustmentDefinitionJPA antiSlipMat = new AdjustmentDefinitionJPA();
        antiSlipMat.setAdjustmentDefinitionId(17L);
        antiSlipMat.setName("Anti slip mat");
        antiSlipMat.setAverageCost(30.0);
        antiSlipMat.setCostMargin(10.0);
        antiSlipMat.setConditions(new ArrayList<ConditionJPA>());
        entityManager.persist(antiSlipMat);
        entityManager.flush();
        allAdjustments.add(antiSlipMat);

        // Handrail
        AdjustmentDefinitionJPA handrail = new AdjustmentDefinitionJPA();
        handrail.setAdjustmentDefinitionId(18L);
        handrail.setName("Handrail");
        handrail.setAverageCost(340.0);
        handrail.setCostMargin(100.0);
        handrail.setConditions(new ArrayList<ConditionJPA>());
        entityManager.persist(handrail);
        entityManager.flush();
        allAdjustments.add(handrail);

        // Ceiling mounted patient elevator
        AdjustmentDefinitionJPA ceilingMountedPatientElevator = new AdjustmentDefinitionJPA();
        ceilingMountedPatientElevator.setAdjustmentDefinitionId(19L);
        ceilingMountedPatientElevator.setName("Ceiling mounted patient elevator");
        ceilingMountedPatientElevator.setAverageCost(6500.0);
        ceilingMountedPatientElevator.setCostMargin(1166.6);
        ceilingMountedPatientElevator.setConditions(new ArrayList<ConditionJPA>());
        entityManager.persist(ceilingMountedPatientElevator);
        entityManager.flush();
        allAdjustments.add(ceilingMountedPatientElevator);

        // Passive patient elevator
        AdjustmentDefinitionJPA passivePatientElevator = new AdjustmentDefinitionJPA();
        passivePatientElevator.setAdjustmentDefinitionId(20L);
        passivePatientElevator.setName("Passive patient elevator");
        passivePatientElevator.setAverageCost(5000.0);
        passivePatientElevator.setCostMargin(666.7);
        passivePatientElevator.setConditions(new ArrayList<ConditionJPA>());
        entityManager.persist(passivePatientElevator);
        entityManager.flush();
        allAdjustments.add(passivePatientElevator);

        // Wall mounted patient elevator
        AdjustmentDefinitionJPA wallMountedPatientElevator = new AdjustmentDefinitionJPA();
        wallMountedPatientElevator.setAdjustmentDefinitionId(21L);
        wallMountedPatientElevator.setName("Wall mounted patient elevator");
        wallMountedPatientElevator.setAverageCost(5000.0);
        wallMountedPatientElevator.setCostMargin(1000.0);
        wallMountedPatientElevator.setConditions(new ArrayList<ConditionJPA>());
        entityManager.persist(wallMountedPatientElevator);
        entityManager.flush();
        allAdjustments.add(wallMountedPatientElevator);

        // Lifting sling
        AdjustmentDefinitionJPA liftingSling = new AdjustmentDefinitionJPA();
        liftingSling.setAdjustmentDefinitionId(22L);
        liftingSling.setName("Lifting sling");
        liftingSling.setAverageCost(825.0);
        liftingSling.setCostMargin(225.0);
        liftingSling.setConditions(new ArrayList<ConditionJPA>());
        entityManager.persist(liftingSling);
        entityManager.flush();
        allAdjustments.add(liftingSling);

        // Support pole
        AdjustmentDefinitionJPA supportPole = new AdjustmentDefinitionJPA();
        supportPole.setAdjustmentDefinitionId(23L);
        supportPole.setName("Support pole");
        supportPole.setAverageCost(375.0);
        supportPole.setCostMargin(41.7);
        supportPole.setConditions(new ArrayList<ConditionJPA>());
        entityManager.persist(supportPole);
        entityManager.flush();
        allAdjustments.add(supportPole);

        // Stair elevator seat
        AdjustmentDefinitionJPA stairElevatorSeat = new AdjustmentDefinitionJPA();
        stairElevatorSeat.setAdjustmentDefinitionId(24L);
        stairElevatorSeat.setName("Stair elevator seat");
        stairElevatorSeat.setAverageCost(5000.0);
        stairElevatorSeat.setCostMargin(500.0);
        stairElevatorSeat.setConditions(new ArrayList<ConditionJPA>());
        entityManager.persist(stairElevatorSeat);
        entityManager.flush();
        allAdjustments.add(stairElevatorSeat);


        /*
        Connect conditions and adjustments
         */

        // Adjustments for minor movement impairment
        minorMovementImpairment.setAdjustmentDefinitions(new ArrayList<AdjustmentDefinitionJPA>(3));
        minorMovementImpairment.getAdjustmentDefinitions().add(antiSlipMat);
        minorMovementImpairment.getAdjustmentDefinitions().add(showerSeat);
        minorMovementImpairment.getAdjustmentDefinitions().add(armrests);
        entityManager.merge(minorMovementImpairment);
        entityManager.flush();

        // Adjustments for medium movement impairment
        mediumMovementImpairment.setAdjustmentDefinitions(new ArrayList<AdjustmentDefinitionJPA>(6));
        mediumMovementImpairment.getAdjustmentDefinitions().add(antiSlipMat);
        mediumMovementImpairment.getAdjustmentDefinitions().add(showerSeat);
        mediumMovementImpairment.getAdjustmentDefinitions().add(armrests);
        mediumMovementImpairment.getAdjustmentDefinitions().add(toiletSeatWithArmrests);
        mediumMovementImpairment.getAdjustmentDefinitions().add(heightendToiletSeat);
        mediumMovementImpairment.getAdjustmentDefinitions().add(betweenSteps);
        entityManager.merge(mediumMovementImpairment);
        entityManager.flush();

        // Adjustments for severe movement impairment
        severeMovementImpairment.setAdjustmentDefinitions(new ArrayList<AdjustmentDefinitionJPA>());
        severeMovementImpairment.getAdjustmentDefinitions().add(antiSlipMat);
        severeMovementImpairment.getAdjustmentDefinitions().add(showerSeat);
        severeMovementImpairment.getAdjustmentDefinitions().add(armrests);
        severeMovementImpairment.getAdjustmentDefinitions().add(toiletBackSupport);
        severeMovementImpairment.getAdjustmentDefinitions().add(toiletSeatWithArmrests);
        severeMovementImpairment.getAdjustmentDefinitions().add(heightendToiletSeat);
        severeMovementImpairment.getAdjustmentDefinitions().add(doorstepHelper);
        severeMovementImpairment.getAdjustmentDefinitions().add(supportPole);
        severeMovementImpairment.getAdjustmentDefinitions().add(stairElevatorSeat);
        severeMovementImpairment.getAdjustmentDefinitions().add(stairElevatorStanding);
        entityManager.merge(severeMovementImpairment);
        entityManager.flush();

        // Adjustments for wheelchair bound
        wheelchairBound.setAdjustmentDefinitions(new ArrayList<AdjustmentDefinitionJPA>(7));
        wheelchairBound.getAdjustmentDefinitions().add(doorstepHelper);
        wheelchairBound.getAdjustmentDefinitions().add(handGrips);
        wheelchairBound.getAdjustmentDefinitions().add(supportPole);
        wheelchairBound.getAdjustmentDefinitions().add(stairElevatorSeat);
        wheelchairBound.getAdjustmentDefinitions().add(showerSeat);
        wheelchairBound.getAdjustmentDefinitions().add(passivePatientElevator);
        wheelchairBound.getAdjustmentDefinitions().add(heightendToiletSeat);
        entityManager.merge(wheelchairBound);
        entityManager.flush();

        // Adjustments for chronic minor movement impairment
        minorMovementImpairmentChronic.setAdjustmentDefinitions(new ArrayList<AdjustmentDefinitionJPA>(7));
        minorMovementImpairmentChronic.getAdjustmentDefinitions().add(antiSlipMat);
        minorMovementImpairmentChronic.getAdjustmentDefinitions().add(showerSeat);
        minorMovementImpairmentChronic.getAdjustmentDefinitions().add(armrests);
        minorMovementImpairmentChronic.getAdjustmentDefinitions().add(heightendToiletSeat);
        minorMovementImpairmentChronic.getAdjustmentDefinitions().add(doorstepHelper);
        minorMovementImpairmentChronic.getAdjustmentDefinitions().add(handGrips);
        minorMovementImpairmentChronic.getAdjustmentDefinitions().add(handrail);
        entityManager.merge(minorMovementImpairmentChronic);
        entityManager.flush();

        // Adjustments for chronic medium movement impairment
        mediumMovementImpairmentChronic.setAdjustmentDefinitions(new ArrayList<AdjustmentDefinitionJPA>());
        mediumMovementImpairmentChronic.getAdjustmentDefinitions().add(antiSlipMat);
        mediumMovementImpairmentChronic.getAdjustmentDefinitions().add(showerSeat);
        mediumMovementImpairmentChronic.getAdjustmentDefinitions().add(armrests);
        mediumMovementImpairmentChronic.getAdjustmentDefinitions().add(handGrips);
        mediumMovementImpairmentChronic.getAdjustmentDefinitions().add(handrail);
        mediumMovementImpairmentChronic.getAdjustmentDefinitions().add(betweenSteps);
        mediumMovementImpairmentChronic.getAdjustmentDefinitions().add(doorstepHelper);
        mediumMovementImpairmentChronic.getAdjustmentDefinitions().add(heightendToilet);
        mediumMovementImpairmentChronic.getAdjustmentDefinitions().add(toiletBackSupport);
        mediumMovementImpairmentChronic.getAdjustmentDefinitions().add(stairElevatorStanding);
        entityManager.merge(mediumMovementImpairmentChronic);
        entityManager.flush();

        // Adjustments for chronic severe movement impairment
        severeMovementImpairmentChronic.setAdjustmentDefinitions(new ArrayList<AdjustmentDefinitionJPA>(12));
        severeMovementImpairmentChronic.getAdjustmentDefinitions().add(antiSlipMat);
        severeMovementImpairmentChronic.getAdjustmentDefinitions().add(showerSeat);
        severeMovementImpairmentChronic.getAdjustmentDefinitions().add(armrests);
        severeMovementImpairmentChronic.getAdjustmentDefinitions().add(handGrips);
        severeMovementImpairmentChronic.getAdjustmentDefinitions().add(handrail);
        severeMovementImpairmentChronic.getAdjustmentDefinitions().add(doorstepHelper);
        severeMovementImpairmentChronic.getAdjustmentDefinitions().add(heightendToilet);
        severeMovementImpairmentChronic.getAdjustmentDefinitions().add(toiletBackSupport);
        severeMovementImpairmentChronic.getAdjustmentDefinitions().add(bidetToiletSeat);
        severeMovementImpairmentChronic.getAdjustmentDefinitions().add(stairElevatorSeat);
        severeMovementImpairmentChronic.getAdjustmentDefinitions().add(passivePatientElevator);
        severeMovementImpairmentChronic.getAdjustmentDefinitions().add(liftingSling);
        entityManager.merge(severeMovementImpairmentChronic);
        entityManager.flush();

        // Adjustments for chronic wheelchair bound
        wheelchairBoundChronic.setAdjustmentDefinitions(new ArrayList<AdjustmentDefinitionJPA>(16));
        wheelchairBoundChronic.getAdjustmentDefinitions().add(showerSeat);
        wheelchairBoundChronic.getAdjustmentDefinitions().add(handGrips);
        wheelchairBoundChronic.getAdjustmentDefinitions().add(handrail);
        wheelchairBoundChronic.getAdjustmentDefinitions().add(supportPole);
        wheelchairBoundChronic.getAdjustmentDefinitions().add(doorstepHelper);
        wheelchairBoundChronic.getAdjustmentDefinitions().add(heightAdjustableKitchen);
        wheelchairBoundChronic.getAdjustmentDefinitions().add(electricalDoor);
        wheelchairBoundChronic.getAdjustmentDefinitions().add(plateauElevator);
        wheelchairBoundChronic.getAdjustmentDefinitions().add(stairElevatorSeat);
        wheelchairBoundChronic.getAdjustmentDefinitions().add(bidetToilet);
        wheelchairBoundChronic.getAdjustmentDefinitions().add(toiletBackSupport);
        wheelchairBoundChronic.getAdjustmentDefinitions().add(heightendToilet);
        wheelchairBoundChronic.getAdjustmentDefinitions().add(ceilingMountedPatientElevator);
        wheelchairBoundChronic.getAdjustmentDefinitions().add(wallMountedPatientElevator);
        wheelchairBoundChronic.getAdjustmentDefinitions().add(passivePatientElevator);
        wheelchairBoundChronic.getAdjustmentDefinitions().add(liftingSling);
        entityManager.merge(wheelchairBoundChronic);
        entityManager.flush();

        // Adjustments for impaired motor functions in the arms
        impairedMotorFunctionArms.setAdjustmentDefinitions(new ArrayList<AdjustmentDefinitionJPA>(4));
        impairedMotorFunctionArms.getAdjustmentDefinitions().add(toiletSeatWithArmrests);
        impairedMotorFunctionArms.getAdjustmentDefinitions().add(armrests);
        impairedMotorFunctionArms.getAdjustmentDefinitions().add(bidetToilet);
        impairedMotorFunctionArms.getAdjustmentDefinitions().add(bidetToiletSeat);
        entityManager.merge(impairedMotorFunctionArms);
        entityManager.flush();

        // Adjustments for obesistas
        obesistas.setAdjustmentDefinitions(new ArrayList<AdjustmentDefinitionJPA>(4));
        obesistas.getAdjustmentDefinitions().add(wideToiletSeat);
        obesistas.getAdjustmentDefinitions().add(handGrips);
        obesistas.getAdjustmentDefinitions().add(bidetToilet);
        obesistas.getAdjustmentDefinitions().add(armrests);
        entityManager.merge(obesistas);
        entityManager.flush();


        /*
        Connect Adjustment definition to conditions
         */

        // Anti slip mat Conditions
        antiSlipMat.getConditions().add(minorMovementImpairment);
        antiSlipMat.getConditions().add(mediumMovementImpairment);
        antiSlipMat.getConditions().add(severeMovementImpairment);
        antiSlipMat.getConditions().add(minorMovementImpairmentChronic);
        antiSlipMat.getConditions().add(mediumMovementImpairmentChronic);
        antiSlipMat.getConditions().add(severeMovementImpairmentChronic);
        entityManager.merge(antiSlipMat);
        entityManager.flush();

        // Shower seat Conditions
        showerSeat.getConditions().add(minorMovementImpairment);
        showerSeat.getConditions().add(mediumMovementImpairment);
        showerSeat.getConditions().add(severeMovementImpairment);
        showerSeat.getConditions().add(wheelchairBound);
        showerSeat.getConditions().add(minorMovementImpairmentChronic);
        showerSeat.getConditions().add(mediumMovementImpairmentChronic);
        showerSeat.getConditions().add(severeMovementImpairmentChronic);
        showerSeat.getConditions().add(wheelchairBoundChronic);
        entityManager.merge(showerSeat);
        entityManager.flush();

        // Armrests Conditions
        armrests.getConditions().add(minorMovementImpairment);
        armrests.getConditions().add(mediumMovementImpairment);
        armrests.getConditions().add(severeMovementImpairment);
        armrests.getConditions().add(minorMovementImpairmentChronic);
        armrests.getConditions().add(mediumMovementImpairmentChronic);
        armrests.getConditions().add(severeMovementImpairmentChronic);
        armrests.getConditions().add(impairedMotorFunctionArms);
        armrests.getConditions().add(obesistas);
        entityManager.merge(armrests);
        entityManager.flush();

        // Toilet back support Conditions
        toiletBackSupport.getConditions().add(severeMovementImpairment);
        toiletBackSupport.getConditions().add(mediumMovementImpairmentChronic);
        toiletBackSupport.getConditions().add(severeMovementImpairmentChronic);
        toiletBackSupport.getConditions().add(wheelchairBoundChronic);
        entityManager.merge(toiletBackSupport);
        entityManager.flush();

        // Toilet seat with armrests Conditions
        toiletSeatWithArmrests.getConditions().add(mediumMovementImpairment);
        toiletSeatWithArmrests.getConditions().add(severeMovementImpairment);
        toiletSeatWithArmrests.getConditions().add(impairedMotorFunctionArms);
        entityManager.merge(toiletSeatWithArmrests);
        entityManager.flush();

        // Heightend toilet seat Conditions
        heightendToiletSeat.getConditions().add(mediumMovementImpairment);
        heightendToiletSeat.getConditions().add(severeMovementImpairment);
        heightendToiletSeat.getConditions().add(wheelchairBound);
        heightendToiletSeat.getConditions().add(minorMovementImpairmentChronic);
        entityManager.merge(heightendToiletSeat);
        entityManager.flush();

        // Between steps Conditions
        betweenSteps.getConditions().add(mediumMovementImpairment);
        betweenSteps.getConditions().add(mediumMovementImpairmentChronic);
        entityManager.merge(betweenSteps);
        entityManager.flush();


        // Door step helper Conditions
        doorstepHelper.getConditions().add(severeMovementImpairment);
        doorstepHelper.getConditions().add(wheelchairBound);
        doorstepHelper.getConditions().add(minorMovementImpairmentChronic);
        doorstepHelper.getConditions().add(mediumMovementImpairmentChronic);
        doorstepHelper.getConditions().add(severeMovementImpairmentChronic);
        doorstepHelper.getConditions().add(wheelchairBoundChronic);
        entityManager.merge(doorstepHelper);
        entityManager.flush();

        // Support pole Conditions
        supportPole.getConditions().add(severeMovementImpairment);
        supportPole.getConditions().add(wheelchairBound);
        supportPole.getConditions().add(wheelchairBoundChronic);
        entityManager.merge(supportPole);
        entityManager.flush();

        // Stair elevator seat
        stairElevatorSeat.getConditions().add(severeMovementImpairment);
        stairElevatorSeat.getConditions().add(wheelchairBound);
        stairElevatorSeat.getConditions().add(severeMovementImpairmentChronic);
        stairElevatorSeat.getConditions().add(wheelchairBoundChronic);
        entityManager.merge(stairElevatorSeat);
        entityManager.flush();

        // Stair elevator standing Conditions
        stairElevatorStanding.getConditions().add(severeMovementImpairment);
        stairElevatorStanding.getConditions().add(mediumMovementImpairmentChronic);
        entityManager.merge(stairElevatorStanding);
        entityManager.flush();

        // Hand grips
        handGrips.getConditions().add(wheelchairBound);
        handGrips.getConditions().add(minorMovementImpairmentChronic);
        handGrips.getConditions().add(mediumMovementImpairmentChronic);
        handGrips.getConditions().add(severeMovementImpairmentChronic);
        handGrips.getConditions().add(wheelchairBoundChronic);
        handGrips.getConditions().add(obesistas);
        entityManager.merge(handGrips);
        entityManager.flush();

        // Passive patient elevator
        passivePatientElevator.getConditions().add(wheelchairBound);
        passivePatientElevator.getConditions().add(severeMovementImpairmentChronic);
        passivePatientElevator.getConditions().add(wheelchairBoundChronic);
        entityManager.merge(passivePatientElevator);
        entityManager.flush();

        // Handrail Conditions
        handrail.getConditions().add(minorMovementImpairmentChronic);
        handrail.getConditions().add(mediumMovementImpairmentChronic);
        handrail.getConditions().add(severeMovementImpairmentChronic);
        handrail.getConditions().add(wheelchairBoundChronic);
        entityManager.merge(handrail);
        entityManager.flush();

        // Heightend toilet Conditions
        heightendToilet.getConditions().add(mediumMovementImpairmentChronic);
        heightendToilet.getConditions().add(severeMovementImpairmentChronic);
        heightendToilet.getConditions().add(wheelchairBoundChronic);
        entityManager.merge(heightendToilet);
        entityManager.flush();

        // Bidet toilet seat Conditions
        bidetToiletSeat.getConditions().add(severeMovementImpairmentChronic);
        bidetToiletSeat.getConditions().add(impairedMotorFunctionArms);
        entityManager.merge(bidetToiletSeat);
        entityManager.flush();

        // Lifting sling Conditions
        liftingSling.getConditions().add(severeMovementImpairmentChronic);
        liftingSling.getConditions().add(wheelchairBoundChronic);
        entityManager.merge(liftingSling);
        entityManager.flush();

        // Height adjustable kitchen Conditions
        heightAdjustableKitchen.getConditions().add(wheelchairBoundChronic);
        entityManager.merge(heightAdjustableKitchen);
        entityManager.flush();

        // Electrical door Conditions
        electricalDoor.getConditions().add(wheelchairBoundChronic);
        entityManager.merge(electricalDoor);
        entityManager.flush();

        // Plateau elevator Conditions
        plateauElevator.getConditions().add(wheelchairBoundChronic);
        entityManager.merge(plateauElevator);
        entityManager.flush();

        // Bidet toilet Conditions
        bidetToilet.getConditions().add(wheelchairBoundChronic);
        bidetToilet.getConditions().add(impairedMotorFunctionArms);
        bidetToilet.getConditions().add(obesistas);
        entityManager.merge(bidetToilet);
        entityManager.flush();

        // Ceiling mounted patient elevator Conditions
        ceilingMountedPatientElevator.getConditions().add(wheelchairBoundChronic);
        entityManager.merge(ceilingMountedPatientElevator);
        entityManager.flush();

        // Wall mounted patient elevator Condtions
        wallMountedPatientElevator.getConditions().add(wheelchairBoundChronic);
        entityManager.merge(wallMountedPatientElevator);
        entityManager.flush();

        // Wide toilet seat Conditions
        wideToiletSeat.getConditions().add(obesistas);
        entityManager.merge(wideToiletSeat);
        entityManager.flush();


        /*
        Contractors
         */

        // Gekke Henkies klusbedrijf
        ContractorJPA gekkeHenkiesKlusbedrijf = new ContractorJPA();
        gekkeHenkiesKlusbedrijf.setApproved(true);
        gekkeHenkiesKlusbedrijf.setDateAdded(new Date());
        gekkeHenkiesKlusbedrijf.setName("Gekke Henkies klusbedrijf");
        gekkeHenkiesKlusbedrijf.setCostModifier(-0.06);
        entityManager.persist(gekkeHenkiesKlusbedrijf);
        entityManager.flush();
        allContractors.add(gekkeHenkiesKlusbedrijf);

        // De man met de hamer
        ContractorJPA deManMetDeHamer = new ContractorJPA();
        deManMetDeHamer.setApproved(true);
        deManMetDeHamer.setDateAdded(new Date());
        deManMetDeHamer.setName("De man met de hamer");
        deManMetDeHamer.setCostModifier(0.07);
        entityManager.persist(deManMetDeHamer);
        entityManager.flush();
        allContractors.add(deManMetDeHamer);

        // De spijker op zn kop
        ContractorJPA deSpijkerOpZnKop = new ContractorJPA();
        deSpijkerOpZnKop.setApproved(true);
        deSpijkerOpZnKop.setDateAdded(new Date());
        deSpijkerOpZnKop.setName("De spijker op zn kop");
        deSpijkerOpZnKop.setCostModifier(-0.1);
        entityManager.persist(deSpijkerOpZnKop);
        entityManager.flush();
        allContractors.add(deSpijkerOpZnKop);

        // Booris en Zoon
        ContractorJPA boorisEnZoon = new ContractorJPA();
        boorisEnZoon.setApproved(true);
        boorisEnZoon.setDateAdded(new Date());
        boorisEnZoon.setName("Booris en Zoon");
        boorisEnZoon.setCostModifier(0.05);
        entityManager.persist(boorisEnZoon);
        entityManager.flush();
        allContractors.add(boorisEnZoon);

        // Water op maat
        ContractorJPA waterOpMaat = new ContractorJPA();
        waterOpMaat.setApproved(true);
        waterOpMaat.setDateAdded(new Date());
        waterOpMaat.setName("Water op maat");
        waterOpMaat.setCostModifier(0.02);
        entityManager.persist(waterOpMaat);
        entityManager.flush();
        allContractors.add(waterOpMaat);

        // Hart voor hout
        ContractorJPA hartVoorHout = new ContractorJPA();
        hartVoorHout.setApproved(true);
        hartVoorHout.setDateAdded(new Date());
        hartVoorHout.setName("Hart vor hout");
        hartVoorHout.setCostModifier(0.01);
        entityManager.persist(hartVoorHout);
        entityManager.flush();
        allContractors.add(hartVoorHout);

        // Klusjes enzo
        ContractorJPA klusjesEnzo = new ContractorJPA();
        klusjesEnzo.setApproved(true);
        klusjesEnzo.setDateAdded(new Date());
        klusjesEnzo.setName("Klusjes enzo");
        klusjesEnzo.setCostModifier(-0.02);
        entityManager.persist(klusjesEnzo);
        entityManager.flush();
        allContractors.add(klusjesEnzo);

        // Bouwbedrijf Noorderzon
        ContractorJPA bouwbedrijfNoorderzon = new ContractorJPA();
        bouwbedrijfNoorderzon.setApproved(true);
        bouwbedrijfNoorderzon.setDateAdded(new Date());
        bouwbedrijfNoorderzon.setName("Bouwbedrijf Noorderzon");
        bouwbedrijfNoorderzon.setCostModifier(0.03);
        entityManager.persist(bouwbedrijfNoorderzon);
        entityManager.flush();
        allContractors.add(bouwbedrijfNoorderzon);

        // Schroefje vast
        ContractorJPA schroefjeVast = new ContractorJPA();
        schroefjeVast.setApproved(true);
        schroefjeVast.setDateAdded(new Date());
        schroefjeVast.setName("Schroefje vast");
        schroefjeVast.setCostModifier(-0.04);
        entityManager.persist(schroefjeVast);
        entityManager.flush();
        allContractors.add(schroefjeVast);

        // Plintenladder BV
        ContractorJPA plintenLadderBV = new ContractorJPA();
        plintenLadderBV.setApproved(true);
        plintenLadderBV.setDateAdded(new Date());
        plintenLadderBV.setName("Plintenladder BV");
        plintenLadderBV.setCostModifier(0.08);
        entityManager.persist(plintenLadderBV);
        entityManager.flush();
        allContractors.add(plintenLadderBV);

        // Ramen met smaakjes specialist
        ContractorJPA ramenMetSmaakjesSpecialist = new ContractorJPA();
        ramenMetSmaakjesSpecialist.setApproved(true);
        ramenMetSmaakjesSpecialist.setDateAdded(new Date());
        ramenMetSmaakjesSpecialist.setName("Ramen met smaakjes specialist");
        ramenMetSmaakjesSpecialist.setCostModifier(-0.01);
        entityManager.persist(ramenMetSmaakjesSpecialist);
        entityManager.flush();
        allContractors.add(ramenMetSmaakjesSpecialist);

        // De klus muts
        ContractorJPA deKlusMuts = new ContractorJPA();
        deKlusMuts.setApproved(true);
        deKlusMuts.setDateAdded(new Date());
        deKlusMuts.setName("De klus muts");
        deKlusMuts.setCostModifier(-0.09);
        entityManager.persist(deKlusMuts);
        entityManager.flush();
        allContractors.add(deKlusMuts);

        // Lucht anker specialist
        ContractorJPA luchtAnkerSpecialist = new ContractorJPA();
        luchtAnkerSpecialist.setApproved(true);
        luchtAnkerSpecialist.setDateAdded(new Date());
        luchtAnkerSpecialist.setName("Lucht anker specialist");
        luchtAnkerSpecialist.setCostModifier(0.07);
        entityManager.persist(luchtAnkerSpecialist);
        entityManager.flush();
        allContractors.add(luchtAnkerSpecialist);

        // Drie keer meten twee keer zagen BV
        ContractorJPA drieKeerMetenTweeKeerZagenBV = new ContractorJPA();
        drieKeerMetenTweeKeerZagenBV.setApproved(true);
        drieKeerMetenTweeKeerZagenBV.setDateAdded(new Date());
        drieKeerMetenTweeKeerZagenBV.setName("Drie keer meten twee keer zagen BV");
        drieKeerMetenTweeKeerZagenBV.setCostModifier(-0.04);
        entityManager.persist(drieKeerMetenTweeKeerZagenBV);
        entityManager.flush();
        allContractors.add(drieKeerMetenTweeKeerZagenBV);

        // Alles kan weer heel
        ContractorJPA allesKanWeerHeel = new ContractorJPA();
        allesKanWeerHeel.setApproved(true);
        allesKanWeerHeel.setDateAdded(new Date());
        allesKanWeerHeel.setName("Alles kan weer heel");
        allesKanWeerHeel.setCostModifier(0.04);
        entityManager.persist(allesKanWeerHeel);
        entityManager.flush();
        allContractors.add(allesKanWeerHeel);


        System.out.println("End initial data load");

    }


    /**
     * Method to create an applicant, complete with the given number of applications.
     *
     * @param numberOfApplications - The total number of applications by the applicant
     * @param householdSize        - The houshold size of the applicant
     */
    public void createApplicant(long numberOfApplications, long householdSize) {

        // Create a new person
        PersonJPA applicant = new PersonJPA();
        baseBsn++;
        applicant.setBsn(baseBsn);
        entityManager.persist(applicant);
        entityManager.flush();

        // Generate the actual conditions for this applicant, with a small chance for no actual conditions
        List<ConditionJPA> actualConditions = new ArrayList<ConditionJPA>();
        if (getRandom().nextDouble() > 0.0025) {
            actualConditions = getRandomConditionList(1, 3);
        }

        // TODO : Create address and BAG


        // Create new housing situation
        HousingSituationJPA housingSituation = new HousingSituationJPA();
        housingSituation.setFloor((short) calculateAbsoluteGaussian(2.0, 1.0));
        housingSituation.setElevator(getRandom().nextBoolean());
        housingSituation.setResidents(new ArrayList<PersonToHousingSituationJPA>());
        PersonToHousingSituationJPA resident = new PersonToHousingSituationJPA();
        resident.setResident(applicant);
        resident.setMoveInDate(new Date());
        resident.setHousingSituation(housingSituation);
        housingSituation.getResidents().add(resident);

        // Generate extra residents, if applicable
        if (householdSize > 1) {
            for (long i = 0; i < householdSize - 1; i++) {
                PersonToHousingSituationJPA extraResident = new PersonToHousingSituationJPA();
                extraResident.setMoveInDate(new Date());
                PersonJPA person = new PersonJPA();
                baseBsn++;
                person.setBsn(baseBsn);
                extraResident.setResident(person);
                extraResident.setHousingSituation(housingSituation);
                housingSituation.getResidents().add(extraResident);
            }
        }

        // Save the new housing situation
        entityManager.persist(housingSituation);

        // Generate the given number of applications
        applicant.setApplications(new ArrayList<ApplicationJPA>());
        for (long i = 0; i < numberOfApplications; i++) {
            applicant.getApplications().add(createApplication(applicant, housingSituation, actualConditions));

        }

        // Lastly save the appliant with the generated applications
        entityManager.merge(applicant);
        entityManager.flush();
    }


    /**
     * Method to create an application for the given applicant. Complete with advice and final WMO decision.
     *
     * @param applicant        - The applicant from whom the application is
     * @param housingSituation - The housing situation of the applicant
     * @param actualConditions - The actual conditions of the applicant
     * @return The application of the applicant
     */
    private ApplicationJPA createApplication(PersonJPA applicant, HousingSituationJPA housingSituation, List<ConditionJPA> actualConditions) {

        // Create application
        ApplicationJPA application = new ApplicationJPA();
        application.setApplicant(applicant);
        application.setDateRecieved(new Date());


        // Create a list of valid adjustment definitions for the actual conditions if available
        List<AdjustmentDefinitionJPA> validAdjustmentDefinitions = new ArrayList<AdjustmentDefinitionJPA>();
        for (ConditionJPA currentCondition : actualConditions) {
            for (AdjustmentDefinitionJPA adjustmentDefinition : currentCondition.getAdjustmentDefinitions()) {
                if (!validAdjustmentDefinitions.contains(adjustmentDefinition)) {
                    validAdjustmentDefinitions.add(adjustmentDefinition);
                }
            }
        }

        // Generate proposed adjustments
        List<AdjustmentJPA> proposedAdjustments = new ArrayList<AdjustmentJPA>();
        int randomLength = getRandom().nextInt(5);
        for (int i = 0; i < ((randomLength > 1) ? randomLength : 1); i++) {

            // Select an adjustment definition
            AdjustmentDefinitionJPA selectedAdjustment;
            // If there are valid adjustment definitions, select one of these, with a random chance of selecting from all adjustment definition
            if (validAdjustmentDefinitions.size() > 0 && getRandom().nextDouble() > 0.002) {
                selectedAdjustment = validAdjustmentDefinitions.get(getRandom().nextInt(validAdjustmentDefinitions.size()));
            } else {
                selectedAdjustment = allAdjustments.get(getRandom().nextInt(allAdjustments.size()));
            }

            // Create new adjustment
            AdjustmentJPA proposedAdjustment = new AdjustmentJPA();
            proposedAdjustment.setAdjustmentDefinition(selectedAdjustment);
            proposedAdjustment.setApplication(application);

            // Add a contractor to the proposed adjustment
            proposedAdjustment.setContractor(allContractors.get(getRandom().nextInt(allContractors.size())));

            // Calculate cost of the adjustment
            // TODO : take into account the age of the building
            proposedAdjustment.setActualCost(calculateAbsoluteGaussian(selectedAdjustment.getAverageCost(), selectedAdjustment.getCostMargin()) * (1 + proposedAdjustment.getContractor().getCostModifier()));

            // Add the proposed adjustment to the list
            proposedAdjustments.add(proposedAdjustment);
        }

        // Add proposed adjustments to the application
        application.setProposedAdjustments(proposedAdjustments);


        // TODO : Save the application??? ==> TEST THIS!


        // Create matching advice
        application.setAdvice(createAdviceForApplication(application, housingSituation, actualConditions));

        // Increase total application counter
        totalApplications++;

        return application;
    }


    /**
     * Method to create an advice for the given application. Also creates the final decision for the given application.
     * Assumed is that the Indicatie Advies Bureau will find all of the actual conditions.
     *
     * @param application      - The application that needs advice
     * @param housingSituation - The housing situation of the applicant
     * @param actualConditions - The actual conditions which are relevant for the applicant
     * @return The created advice
     */
    private AdviceJPA createAdviceForApplication(ApplicationJPA application, HousingSituationJPA housingSituation, List<ConditionJPA> actualConditions) {

        // Create new advice
        AdviceJPA advice = new AdviceJPA();
        advice.setApplication(application);
        advice.setDateIssued(new Date());
        List<AdjustmentJPA> irrelevantProposedAdjustments = new ArrayList<AdjustmentJPA>();
        List<AdjustmentJPA> relevantProposedAdjustments = new ArrayList<AdjustmentJPA>();

        // Check if the applicant has any conditions
        if (actualConditions != null && actualConditions.size() > 0) {

            // Check if the application contains (ir)relevant adjustments
            boolean irrelevantAdjustmentPresent = false;
            boolean relevantAdjustmentPresent = false;

            for (AdjustmentJPA proposedAdjustment : application.getProposedAdjustments()) {
                boolean relevantConditionForAdjustment = false;

                for (ConditionJPA relevantCondition : proposedAdjustment.getAdjustmentDefinition().getConditions()) {

                    if (actualConditions.contains(relevantCondition)) {
                        relevantConditionForAdjustment = true;
                        relevantProposedAdjustments.add(proposedAdjustment);
                        break;
                    }
                }

                if (relevantConditionForAdjustment) {
                    relevantAdjustmentPresent = true;
                } else {
                    irrelevantAdjustmentPresent = true;
                    irrelevantProposedAdjustments.add(proposedAdjustment);
                }
            }


            if (irrelevantAdjustmentPresent && relevantAdjustmentPresent) {

                // If there is both at least one irrelevant and at least one relevant adjustment present; partial go ahead
                advice.setGoAhead(true);
                advice.setRemarks(Constants.ADVICE_GO_PARTIAL);

            } else if (irrelevantAdjustmentPresent) {

                // If there are only irrelevant adjustments; no go
                advice.setGoAhead(false);
                advice.setRemarks(Constants.ADVICE_NO_GO_NO_RELEVANT_CONDITION);

            } else {

                // If there are only relevant adjustments; go ahead
                advice.setGoAhead(true);
                advice.setRemarks(Constants.ADVICE_GO);
            }

        } else {

            // No conditions given; no go
            advice.setGoAhead(false);
            advice.setRemarks(Constants.ADVICE_NO_GO_NO_CONDITIONS);
        }

        // Create WMO decision
        advice.setDecision(createWmoDecision(application, advice, housingSituation, relevantProposedAdjustments, irrelevantProposedAdjustments));

        return advice;
    }


    /**
     * Method to create a WMO decision based upon the given application, Indicatie Advies Bureau advice, the applicants
     * housing situation, and the (ir)relevant proposed adjustments from the application.
     *
     * @param application                   - The application
     * @param advice                        - The Indicatie Advies Bureau advice
     * @param housingSituation              - The applicants housing situation
     * @param relevantProposedAdjustments   - The list of relevant proposed adjustments
     * @param irrelevantProposedAdjustments - The list of irrelevant proposed adjustments
     * @return The final WMO decision
     */
    private WmoDecisionJPA createWmoDecision(ApplicationJPA application, AdviceJPA advice, HousingSituationJPA housingSituation, List<AdjustmentJPA> relevantProposedAdjustments, List<AdjustmentJPA> irrelevantProposedAdjustments) {

        // Init of various variables
        boolean applicationForStairElevator = false;
        double remainingBudget = BUDGET;
        List<AdjustmentJPA> stairElevatorAdjustments = new ArrayList<AdjustmentJPA>();
        List<AdjustmentJPA> otherAdjustments = new ArrayList<AdjustmentJPA>();


        // Create new decision
        WmoDecisionJPA decision = new WmoDecisionJPA();
        decision.setAdvice(advice);
        decision.setDate(new Date());

        // Check for go ahead from advice
        if (advice.isGoAhead()) {

            // Split the proposed adjustments into stair elevator adjustments, rejected and others
            for (AdjustmentJPA proposedAdjustment : relevantProposedAdjustments) {

                // Check for stair elevator adjustments
                if (proposedAdjustment.getAdjustmentDefinition().getName().contains("Stair elevator")) {
                    stairElevatorAdjustments.add(proposedAdjustment);
                } else {
                    otherAdjustments.add(proposedAdjustment);
                }
            }

            // If there is a stair elevator adjustment, check for elevator and / floor
            if (stairElevatorAdjustments.size() > 0) {

                applicationForStairElevator = true;
                if (housingSituation.isElevator() || housingSituation.getFloor() >= 2) {

                    // There already is an elevator present, or the residence floor is too high; no stair elevator granted
                    irrelevantProposedAdjustments.addAll(stairElevatorAdjustments);
                    stairElevatorAdjustments.clear();

                } else if (stairElevatorAdjustments.size() > 1) {

                    // In case of more than 1 stair elevator, choose the cheapest option
                    AdjustmentJPA chosenStairElevator = null;
                    for (AdjustmentJPA stairElevatorAdjustment : stairElevatorAdjustments) {
                        if (chosenStairElevator == null || chosenStairElevator.getActualCost() > stairElevatorAdjustment.getActualCost()) {
                            chosenStairElevator = stairElevatorAdjustment;
                        }
                    }
                    stairElevatorAdjustments.clear();
                    stairElevatorAdjustments.add(chosenStairElevator);
                }
            }


            // Check if there are application left which are not yet rejected
            if (otherAdjustments.size() > 0 || stairElevatorAdjustments.size() > 0) {

                // Budget check of current application
                for (AdjustmentJPA otherAdjustment : otherAdjustments) {
                    remainingBudget -= otherAdjustment.getActualCost();
                }

                for (AdjustmentJPA stairElevatorAdjustment : stairElevatorAdjustments) {
                    remainingBudget -= stairElevatorAdjustment.getActualCost();
                }


                if (remainingBudget >= 0) {
                    // Budget left; application granted
                    decision.setGranted(true);

                    if (irrelevantProposedAdjustments.size() > 0) {
                        // In case of rejected adjustments; partial grant
                        if (applicationForStairElevator && stairElevatorAdjustments.size() == 0) {

                            // In case of rejected stair elevator adjustment; give correct reason
                            if (housingSituation.isElevator()) {
                                decision.setReason(Constants.WMO_GRANTED_PARTIAL_ELEVATOR_ALREADY_PRESENT);
                            } else if (housingSituation.getFloor() >= 2) {
                                decision.setReason(Constants.WMO_GRANTED_PARTIAL_FLOOR_TOO_HIGH_FOR_ELEVATOR);
                            } else {
                                decision.setReason(Constants.WMO_GRANTED_PARTIAL);
                            }

                        } else {
                            decision.setReason(Constants.WMO_GRANTED_PARTIAL);
                        }
                    } else {
                        // Full grant
                        decision.setReason(Constants.WMO_GRANTED);
                    }

                    // Set cost subsidized on all granted adjustments
                    for (AdjustmentJPA otherAdjustment : otherAdjustments) {
                        otherAdjustment.setCostSubsidized(otherAdjustment.getActualCost());
                    }

                    for (AdjustmentJPA stairElevatorAdjustment : stairElevatorAdjustments) {
                        stairElevatorAdjustment.setCostSubsidized(stairElevatorAdjustment.getActualCost());
                    }


                } else {

                    // Over budget, check for partial grant possibilities
                    boolean checkTotalBudget = false;

                    // Check if there are adjustments at higher cost than the average
                    List<AdjustmentJPA> adjustmentsOverAverage = new ArrayList<AdjustmentJPA>();
                    List<AdjustmentJPA> adjustmentsUnderAverage = new ArrayList<AdjustmentJPA>();
                    for (AdjustmentJPA otherAdjustment : otherAdjustments) {
                        if (otherAdjustment.getActualCost() > otherAdjustment.getAdjustmentDefinition().getAverageCost()) {
                            adjustmentsOverAverage.add(otherAdjustment);
                        } else {
                            adjustmentsUnderAverage.add(otherAdjustment);
                        }
                    }

                    for (AdjustmentJPA stairElevatorAdjustment : stairElevatorAdjustments) {
                        if (stairElevatorAdjustment.getActualCost() > stairElevatorAdjustment.getAdjustmentDefinition().getAverageCost()) {
                            adjustmentsOverAverage.add(stairElevatorAdjustment);
                        } else {
                            adjustmentsUnderAverage.add(stairElevatorAdjustment);
                        }
                    }

                    if (adjustmentsOverAverage.size() > 0) {
                        // In case of adjustments which are over the average cost, recalculate the budget with the
                        // overcosted adjustments set to the average

                        double recalculatedBudgetRemaining = getBUDGET();

                        for (AdjustmentJPA adjustmentOverAverage : adjustmentsOverAverage) {
                            recalculatedBudgetRemaining -= adjustmentOverAverage.getAdjustmentDefinition().getAverageCost();
                        }

                        for (AdjustmentJPA adjustmentUnderAverage : adjustmentsUnderAverage) {
                            recalculatedBudgetRemaining -= adjustmentUnderAverage.getActualCost();
                        }

                        if (recalculatedBudgetRemaining > 0) {
                            // Budget remaining; application partially granted
                            decision.setGranted(true);
                            decision.setReason(Constants.WMO_GRANTED_PARTIAL);

                            for (AdjustmentJPA adjustmentOverAverage : adjustmentsOverAverage) {
                                adjustmentOverAverage.setCostSubsidized(adjustmentOverAverage.getAdjustmentDefinition().getAverageCost());
                            }

                            for (AdjustmentJPA adjustmentUnderAverage : adjustmentsUnderAverage) {
                                adjustmentUnderAverage.setCostSubsidized(adjustmentUnderAverage.getActualCost());
                            }


                        } else {
                            // If the cost reduction is not enough to reduce to under budget, check the total budget
                            checkTotalBudget = true;
                        }


                    } else {
                        // If all adjustments are under the average cost, check the total budget
                        checkTotalBudget = true;
                    }

                    // Total budget check
                    if (checkTotalBudget) {

                        // Check the total cost of the application
                        double totalCost = 0.0;
                        for (AdjustmentJPA adjustmentJPA : relevantProposedAdjustments) {
                            totalCost += adjustmentJPA.getActualCost();
                        }

                        if (totalCost > 1.5 * getBUDGET()) {

                            // Total cost is far over budget, reject full application
                            decision.setGranted(false);
                            decision.setReason(Constants.WMO_REJECTED_BUDGET);

                            for (AdjustmentJPA relevantProposedAdjustment : relevantProposedAdjustments) {
                                relevantProposedAdjustment.setCostSubsidized(0.0);
                            }
                        } else {

                            // Total cost is slightly over budget, reduce subsidized cost to full budget
                            decision.setGranted(true);
                            decision.setReason(Constants.WMO_GRANTED_PARTIAL);

                            double costReduction = getBUDGET() / totalCost;
                            for (AdjustmentJPA relevantProposedAdjustment : relevantProposedAdjustments) {
                                relevantProposedAdjustment.setCostSubsidized(costReduction * relevantProposedAdjustment.getActualCost());
                            }
                        }
                    }
                }

            } else {

                // No relevant adjustments left; reject application
                decision.setGranted(false);

                // Determine reason for rejection
                if (applicationForStairElevator) {
                    if (housingSituation.isElevator()) {
                        decision.setReason(Constants.WMO_REJECTED_ELEVATOR_ALREADY_PRESENT);
                    } else if (housingSituation.getFloor() > 2) {
                        decision.setReason(Constants.WMO_REJECTED_ELEVATOR_FLOOR_TOO_HIGH);
                    } else {
                        decision.setReason(Constants.WMO_REJECTED_NO_RELEVANT_ADJUSTMENTS);
                    }
                } else {
                    decision.setReason(Constants.WMO_REJECTED_NO_RELEVANT_ADJUSTMENTS);
                }
            }

        } else {

            // In case of no go ahead from the advice, reject the application
            decision.setGranted(false);
            if (advice.getRemarks().equalsIgnoreCase(Constants.ADVICE_NO_GO_NO_CONDITIONS)) {
                // Applicant is in good health
                decision.setReason(Constants.WMO_REJECTED_NO_CONDITIONS);
            } else if (advice.getRemarks().equalsIgnoreCase(Constants.ADVICE_NO_GO_NO_RELEVANT_CONDITION)) {
                // No relevant conditions apply to the applicant
                decision.setReason(Constants.WMO_REJECTED_NO_RELEVANT_CONDITIONS);
            } else {
                // Other cases
                decision.setReason(Constants.WMO_REJECTED_NO_GO);
            }
        }


        // Exceptional cases
        if (getRandom().nextDouble() < 0.0075 && !applicationForStairElevator) {
            if (decision.isGranted()) {
                decision.setReason(Constants.WMO_REJECTED_EXCEPTIONAL_CIRCUMSTANCE);

            } else {
                decision.setReason(Constants.WMO_GRANTED_EXCEPTIONAL_CIRCUMSTANCE);
            }
            decision.setGranted(!decision.isGranted());
            decision.setException(true);
        } else {
            decision.setException(false);
        }

        // Set cost subsidized on rejected adjustments
        for (AdjustmentJPA rejectedAdjustment : irrelevantProposedAdjustments) {
            rejectedAdjustment.setCostSubsidized(0.0);
        }


        // Set all adjustments into the application
        application.getProposedAdjustments().clear();
        application.getProposedAdjustments().addAll(otherAdjustments);
        application.getProposedAdjustments().addAll(stairElevatorAdjustments);
        application.getProposedAdjustments().addAll(irrelevantProposedAdjustments);

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


    /**
     * Generates a List of randomly selected conditions with a length from the given minLength to the given maxLength.
     * The conditions are selected from the condition probability map with the given probability.
     *
     * @param minLength - The minimum length of the List of Comditions
     * @param maxLength - The maximun length of the List of Conditions
     * @return List of Conditions
     */
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

    public double getBUDGET() {
        return BUDGET;
    }

    public int getBaseBsn() {
        return baseBsn;
    }

    public void setBaseBsn(int baseBsn) {
        this.baseBsn = baseBsn;
    }

    public Map<ConditionJPA, Double> getConditionProbabilityMap() {
        return conditionProbabilityMap;
    }

    public void setConditionProbabilityMap(Map<ConditionJPA, Double> conditionProbabilityMap) {
        this.conditionProbabilityMap = conditionProbabilityMap;
    }

    public List<ConditionJPA> getAllConditions() {
        return allConditions;
    }

    public void setAllConditions(List<ConditionJPA> allConditions) {
        this.allConditions = allConditions;
    }

    public List<AdjustmentDefinitionJPA> getAllAdjustments() {
        return allAdjustments;
    }

    public void setAllAdjustments(List<AdjustmentDefinitionJPA> allAdjustments) {
        this.allAdjustments = allAdjustments;
    }

    public List<ContractorJPA> getAllContractors() {
        return allContractors;
    }

    public void setAllContractors(List<ContractorJPA> allContractors) {
        this.allContractors = allContractors;
    }

    public int getTotalApplications() {
        return totalApplications;
    }

    public void setTotalApplications(int totalApplications) {
        this.totalApplications = totalApplications;
    }
}
