package nl.h2.ejb;

public final class Constants {

    // Module
    public static final String MODULE_NAAM = "TDG";
    public static final String MODULE_VERSIE = "v1.0";
    
    // Application exception codes
    public static final String ERR_NUMBER_OF_RECORDS_TOO_LOW = "TDG_001";
    public static final String ERR_MEAN_NUMBER_OF_APPLICATIONS_TOO_LOW = "TDG_002";
    public static final String ERR_SIGMA_NUMBER_OF_APPLICATIONS_TOO_LOW = "TDG_003";
    public static final String ERR_MEAN_HOUSEHOLD_SIZE_TOO_LOW = "TDG_004";
    public static final String ERR_SIGMA_HOUSEHOLD_SIZE_TOO_LOW = "TDG_005";

    // Advice remarks
    public static final String ADVICE_GO = "Adjustments are ok for the conditions of the applicant";
    public static final String ADVICE_NO_GO_NO_RELEVANT_CONDITION = "The applicant has no conditions which require the proposed adjustments";
    public static final String ADVICE_NO_GO_NO_CONDITIONS = "The applicant is in good health";
    public static final String ADVICE_GO_PARTIAL = "One or more, but not all, of the proposed adjustments are not relevant for the application";

    // WMO decision reasons
    public static final String WMO_GRANTED = "Application granted";
    public static final String WMO_GRANTED_PARTIAL = "Application partially granted";
    public static final String WMO_GRANTED_PARTIAL_ELEVATOR_ALREADY_PRESENT = "Application partially granted, stair elevator rejected, elevator is already present";
    public static final String WMO_GRANTED_PARTIAL_FLOOR_TOO_HIGH_FOR_ELEVATOR = "Application partially granted, stair elevator rejected, applicant lives on too high a floor for elevator";
    public static final String WMO_GRANTED_EXCEPTIONAL_CIRCUMSTANCE = "Application granted due to exceptional circumstances";
    public static final String WMO_REJECTED_BUDGET = "Application rejected, over budget";
    public static final String WMO_REJECTED_NO_RELEVANT_CONDITIONS = "Application rejected, no relevant conditions for the proposed adjustments";
    public static final String WMO_REJECTED_NO_CONDITIONS = "Application rejected, applicant in good health";
    public static final String WMO_REJECTED_ELEVATOR_ALREADY_PRESENT = "Application rejected, elevator already present";
    public static final String WMO_REJECTED_ELEVATOR_FLOOR_TOO_HIGH = "Application rejected, applicant lives on a too high a floor for stair elevator";
    public static final String WMO_REJECTED_EXCEPTIONAL_CIRCUMSTANCE = "Application rejected due to exceptional circumstances";
    public static final String WMO_REJECTED_NO_GO = "Application rejected, no go from advice";
    public static final String WMO_REJECTED_PREVIOUS_APPLICATIONS_OVER_BUDGET = "Application rejected, application is over budget combined with previously granted applications";
    public static final String WMO_REJECTED_NO_RELEVANT_ADJUSTMENTS = "Application rejected, application did not contain relevant adjustments";

    public static String getModule() {
        return MODULE_NAAM + " " + MODULE_VERSIE;
    }

}