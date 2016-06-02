package nl.h2.ejb;

public final class Constanten {

    // Module
    public static final String MODULE_NAAM = "TDG";
    public static final String MODULE_VERSIE = "v1.0";

    // Algemene foutcodes
    public static final String FOUT_TECHNISCH_PROBLEEM = "H2_ALG_000";
    public static final String FOUT_ONBEKENDE_FOUT = "H2_ALG_001";
    public static final String FOUT_ONGELDIGE_XML = "H2_ALG_002";
    public static final String ONVERWACHTE_FOUT = "H2_ALG_004";
    public static final String ONVOLDOENDE_PARAMETERS = "H2_ALG_005";
    
    // Applicatie foutcodes

    public static String getModule() {
        return MODULE_NAAM + " " + MODULE_VERSIE;
    }

}