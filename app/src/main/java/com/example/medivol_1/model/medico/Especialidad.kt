package com.example.medivol_1.model.medico

enum class Especialidad(val nombreAmigable: String) {
    ORTOPEDIA("Ortopedia"),
    CARDIOLOGIA("Cardiología"),
    GINECOLOGIA("Ginecología"),
    DERMATOLOGIA("Dermatología"),
    PEDIATRIA("Pediatría"), // Ya corregida la errata, si puedes corregirla en backend
    MEDICINA_GENERAL("Medicina General");

    // Función para obtener el valor que espera el backend (si es diferente al nombre de la constante)
    fun toBackendString(): String {
        return this.name // Por defecto, es el nombre de la constante (ORTOPEDIA, CARDIOLOGIA, etc.)
    }
}
/*
*
*
*
// Enum para Especialidades (si lo usas)
enum class Especialidad(val nombreAmigable: String) {
    ORTOPEDIA("Ortopedia"),
    PEDIATRIA("Pediatría"),
    CARDIOLOGIA("Cardiología"),
    DERMATOLOGIA("Dermatología"),
    NEUROLOGIA("Neurología"),
    PSIQUIATRIA("Psiquiatría"),
    GINECOLOGIA("Ginecología"),
    OFTALMOLOGIA("Oftalmología"),
    OTORRINOLARINGOLOGIA("Otorrinolaringología"),
    UROLOGIA("Urología"),
    GASTROENTEROLOGIA("Gastroenterología"),
    ENDOCRINOLOGIA("Endocrinología"),
    ONCOLOGIA("Oncología"),
    INFECTOLOGIA("Infectología"),
    NEFROLOGIA("Nefrología"),
    REUMATOLOGIA("Reumatología"),
    ANESTESIOLOGIA("Anestesiología"),
    CIRUGIA_GENERAL("Cirugía General"),
    MEDICINA_INTERNA("Medicina Interna"),
    MEDICINA_FAMILIAR("Medicina Familiar"),
    EMERGENCIAS("Emergencias"),
    RADIOLOGIA("Radiología"),
    PATOLOGIA("Patología"),
    FISIOTERAPIA("Fisioterapia"),
    PSICOLOGIA("Psicología"),
    NUTRICION("Nutrición"),
    ODONTOLOGIA("Odontología"),
    TERAPIA_OCUPACIONAL("Terapia Ocupacional"),
    FONOAUDIOLOGIA("Fonoaudiología"),
    OSTEOPATIA("Osteopatía"),
    QUIROPRAXIA("Quiropraxia"),
    ACUPUNTURA("Acupuntura"),
    HOMEOPATIA("Homeopatía"),
    NATUROPATIA("Naturopatía"),
    GERIATRIA("Geriatría"),
    PULMONOLOGIA("Neumología"), // Renombrado a Neumología para más claridad
    HEMATOLOGIA("Hematología"),
    ALERGOLOGIA("Alergología")
}

*
*
* */
