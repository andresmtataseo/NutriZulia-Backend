package com.nutrizulia.common.util;

public class ApiConstants {
    // Auth
    public static final String AUTH_API_BASE_URL = "/auth";
    public static final String SIGN_IN_URL = "/sign-in";
    public static final String SIGN_UP_URL = "/sign-up";
    public static final String CHECK_AUTH_URL = "/check-auth";

    // User
    public static final String USER_API_BASE_URL = "/user";
    public static final String USER_ALL_URL = "/findAll";

    // Catalog
    public static final String CATALOG_API_BASE_URL = "/catalog";
    public static final String ENFERMEDADES_ALL = "/v1/enfermedades";
    public static final String ESPECIALIDADES_ALL = "/v1/especialidades";
    public static final String ESTADOS_ALL = "/v1/estados";
    public static final String ETNIAS_ALL = "/v1/etnias";
    public static final String GRUPOS_ETARIOS_ALL = "/v1/gruposEtarios";
    public static final String MUNICIPIOS_ALL = "/v1/municipios";
    public static final String MUNICIPIOS_SANITARIOS_ALL = "/v1/municipiosSanitarios";
    public static final String NACIONALIDADES_ALL = "/v1/nacionalidades";
    public static final String PARAMETROS_NINOS_EDAD_ALL = "/v1/parametrosCrecimientosNinosEdad";
    public static final String PARAMETROS_PEDIATRICOS_EDAD_ALL = "/v1/parametrosCrecimientosPediatricoEdad";
    public static final String PARAMETROS_PEDIATRICOS_LONGITUD_ALL = "/v1/parametrosCrecimientosPediatricoLongitud";
    public static final String PARENTESCOS_ALL = "/v1/parentescos";
    public static final String PARROQUIAS_ALL = "/v1/parroquias";
    public static final String REGEX_ALL = "/v1/regex";
    public static final String REGLAS_IMC_ALL = "/v1/reglasInterpretacionesImc";
    public static final String REGLAS_PERCENTIL_ALL = "/v1/reglasInterpretacionesPercentil";
    public static final String REGLAS_Z_SCORE_ALL = "v1/reglasInterpretacionesZScore";
    public static final String RIESGOS_BIOLOGICOS_ALL = "/v1/riesgosBiologicos";
    public static final String ROLES_ALL = "/v1/roles";
    public static final String TIPOS_ACTIVIDADES_ALL = "/v1/tiposActividades";
    public static final String TIPOS_INDICADORES_ALL = "/v1/tiposIndicadores";
    public static final String TIPOS_INSTITUCIONES_ALL = "/v1/tiposInstituciones";
    public static final String VERSIONES_ALL = "/v1/versiones";

    // Collection
    public static final String RECORD_API_BASE_URL = "/collection";
    public static final String RECORD_PACIENTES = "/sync/pacientes";

    // Institution
    public static final String INSTITUTION_API_BASE_URL = "/institution";
    public static final String INSTITUTION_ALL = "/v1/findAll";

    // User-Institution
    public static final String USER_INSTITUTION_API_BASE_URL = "/user-institution";
    public static final String USER_INSTITUTION_ALL_BY_ID_USER = "/v1/findAll";
}