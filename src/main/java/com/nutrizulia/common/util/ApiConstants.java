package com.nutrizulia.common.util;

/**
 * Constantes para las rutas de la API REST.
 * Siguiendo las mejores prácticas de REST y convenciones de nomenclatura.
 */
public class ApiConstants {

    // ========== AUTHENTICATION API ==========
    public static final String AUTH_BASE_URL = "/api/v1/auth";
    public static final String AUTH_SIGN_IN = "/sign-in";
    public static final String AUTH_SIGN_UP = "/sign-up";
    public static final String AUTH_CHECK = "/check";
    public static final String AUTH_FORGOT_PASSWORD = "/forgot-password";
    public static final String AUTH_CHANGE_PASSWORD = "/change-password";
    public static final String AUTH_LOGOUT = "/logout";
    public static final String AUTH_SIGN_IN_ADMIN = "/sign-in-admin";

    // ========== CATALOG API ==========
    public static final String CATALOG_BASE_URL = "/api/v1/catalog";
    public static final String CATALOG_DISEASES = "/diseases";
    public static final String CATALOG_SPECIALTIES = "/specialties";
    public static final String CATALOG_STATES = "/states";
    public static final String CATALOG_ETHNICITIES = "/ethnicities";
    public static final String CATALOG_AGE_GROUPS = "/age-groups";
    public static final String CATALOG_MUNICIPALITIES = "/municipalities";
    public static final String CATALOG_HEALTH_MUNICIPALITIES = "/health-municipalities";
    public static final String CATALOG_NATIONALITIES = "/nationalities";
    public static final String CATALOG_CHILDREN_AGE_PARAMETERS = "/children-age-parameters";
    public static final String CATALOG_PEDIATRIC_AGE_PARAMETERS = "/pediatric-age-parameters";
    public static final String CATALOG_PEDIATRIC_LENGTH_PARAMETERS = "/pediatric-length-parameters";
    public static final String CATALOG_RELATIONSHIPS = "/relationships";
    public static final String CATALOG_PARISHES = "/parishes";
    public static final String CATALOG_REGEX = "/regex";
    public static final String CATALOG_BMI_INTERPRETATION_RULES = "/bmi-interpretation-rules";
    public static final String CATALOG_PERCENTILE_INTERPRETATION_RULES = "/percentile-interpretation-rules";
    public static final String CATALOG_Z_SCORE_INTERPRETATION_RULES = "/z-score-interpretation-rules";
    public static final String CATALOG_BIOLOGICAL_RISKS = "/biological-risks";
    public static final String CATALOG_ROLES = "/roles";
    public static final String CATALOG_ACTIVITY_TYPES = "/activity-types";
    public static final String CATALOG_INDICATOR_TYPES = "/indicator-types";
    public static final String CATALOG_INSTITUTION_TYPES = "/institution-types";
    public static final String CATALOG_VERSIONS = "/versions";

    public static final String CATALOG_INSTITUTIONS = "/institutions";
    public static final String CATALOG_INSTITUTIONS_GET_ALL_WITH_USERS = "/institutions/get-all/users";

    // ========== DATA COLLECTION API ==========
    public static final String COLLECTION_BASE_URL = "/api/v1/collection";

    public static final String COLLECTION_SYNC_PATIENTS = "/sync/patients";
    public static final String COLLECTION_SYNC_REPRESENTATIVES = "/sync/representatives";
    public static final String COLLECTION_SYNC_PATIENT_REPRESENTATIVES = "/sync/patient-representatives";
    public static final String COLLECTION_SYNC_CONSULTATIONS = "/sync/consultations";
    public static final String COLLECTION_SYNC_DIAGNOSES = "/sync/diagnoses";
    public static final String COLLECTION_SYNC_ANTHROPOMETRIC_EVALUATIONS = "/sync/anthropometric-evaluations";
    public static final String COLLECTION_SYNC_ANTHROPOMETRIC_DETAILS = "/sync/anthropometric-details";
    public static final String COLLECTION_SYNC_VITAL_DETAILS = "/sync/vital-details";
    public static final String COLLECTION_SYNC_METABOLIC_DETAILS = "/sync/metabolic-details";
    public static final String COLLECTION_SYNC_PEDIATRIC_DETAILS = "/sync/pediatric-details";
    public static final String COLLECTION_SYNC_OBSTETRIC_DETAILS = "/sync/obstetric-details";
    public static final String COLLECTION_SYNC_ACTIVITIES = "/sync/activities";

    public static final String COLLECTION_SYNC_PATIENTS_FULL = "/sync/patients/full";
    public static final String COLLECTION_SYNC_REPRESENTATIVES_FULL = "/sync/representatives/full";
    public static final String COLLECTION_SYNC_PATIENT_REPRESENTATIVES_FULL = "/sync/patient-representatives/full";
    public static final String COLLECTION_SYNC_CONSULTATIONS_FULL = "/sync/consultations/full";
    public static final String COLLECTION_SYNC_DIAGNOSES_FULL = "/sync/diagnoses/full";
    public static final String COLLECTION_SYNC_ANTHROPOMETRIC_EVALUATIONS_FULL = "/sync/anthropometric-evaluations/full";
    public static final String COLLECTION_SYNC_ANTHROPOMETRIC_DETAILS_FULL = "/sync/anthropometric-details/full";
    public static final String COLLECTION_SYNC_VITAL_DETAILS_FULL = "/sync/vital-details/full";
    public static final String COLLECTION_SYNC_METABOLIC_DETAILS_FULL = "/sync/metabolic-details/full";
    public static final String COLLECTION_SYNC_PEDIATRIC_DETAILS_FULL = "/sync/pediatric-details/full";
    public static final String COLLECTION_SYNC_OBSTETRIC_DETAILS_FULL = "/sync/obstetric-details/full";
    public static final String COLLECTION_SYNC_ACTIVITIES_FULL = "/sync/activities/full";

    // ========== USERS API ==========
    public static final String USERS_BASE_URL = "/api/v1/user";
//    public static final String USERS_GET_ALL = "/get-all";
    public static final String USERS_GET_ALL_WITH_INSTITUTIONS = "/get-all/institutions";
    public static final String USERS_CREATE = "/create";
    public static final String USERS_CHECK_CEDULA = "/check-cedula";
    public static final String USERS_CHECK_EMAIL = "/check-email";
    public static final String USERS_CHECK_PHONE = "/check-phone";
    public static final String USERS_SAVE_PHONE = "/save-phone";
    public static final String USERS_SAVE_EMAIL = "/save-email";
    public static final String USERS_UPDATE = "/update";
    public static final String USER_INSTITUTIONS_GET_BY_USER = "/institutions-by-user";
    public static final String USERS_GET_DETAIL = "/detail";
    public static final String USERS_ASSIGN_INSTITUTION = "/assign-institution";
    public static final String USERS_UPDATE_INSTITUTION = "/update-institution";
}