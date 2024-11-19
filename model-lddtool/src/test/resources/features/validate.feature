Feature: pds4_information_model_integration

  Scenario Outline: <testId>
    Given the test <testName> at directory <testDir>
    Given the latest PDS4 schema/schematron is generated
    Given a new LDD is generated using the IngestLDDs <ingestLDDFileName>
    Given expected error/warning count <messageCount> with expected error/warning text of <messageText> with expected error from ValidateProblems enumeration <problemEnum>
    When execute validate command with <commandArgs> as arguments
    Then produced output from validate command should be similar to reported <testDir> JSON report or no error reported.

    # The scenario about will automatically build the latest PDS4 Information Model as part of the validation.
    
    # NOTE: The following flags are prepended to the execution by default: --disable-context-mismatch-warnings --report-style json --skip-content-validation --report-file {reportDir}/
    
    @v15.2.x
#note: 'mvn test' choks if problem enum is blank or "" even if 0 messages
    Examples: 
| testId | testName | testDir     | messageCount | messageText         | problemEnum        | commandArgs                                             | ingestLDDFileName         |
| "NASA-PDS/pds4-information-model#753"  | "new Current units nA microA"   | "github753"  |           0 | "0 errors expected" | "SCHEMATRON_ERROR" | "-t {resourceDir}/github753/pc__d139.xml" | "PDS4_MARS2020_IngestLDD.xml" |
| "NASA-PDS/pds4-information-model#784a" | "Encoded_Video for Product_Ancillary" | "github784a" |           0 | "0 errors expected" | "SCHEMATRON_ERROR" | "-t {resourceDir}/github784/FUV2017032anc.xml" | "" |
| "NASA-PDS/pds4-information-model#784b" | "Encoded_Video for Product_Browse" | "github784b" |           0 | "0 errors expected" | "SCHEMATRON_ERROR" | "-t {resourceDir}/github784/FUV2017032brw.xml" | "" |
| "NASA-PDS/pds4-information-model#794"  | "new namespace vikinglander"    | "github794"  |           0 | "0 errors expected" | "SCHEMATRON_ERROR" | "-t {resourceDir}/github794/ORB_35_STAR_SCANNER.xml" | "PDS4_VIKINGLANDER_IngestLDD.xml" |
| "NASA-PDS/pds4-information-model#795a" | "reference_type Failures for all Data Types"             | "github795"  |          10 | "10 errors expected" | "SCHEMATRON_ERROR,INTERNAL_ERROR" | "-R pds4.bundle -t {resourceDir}/github795" | "" |
| "NASA-PDS/pds4-information-model#795b" | "reference_type Failures for Product_External"             | "github795b" |           2 | "2 errors expected" | "SCHEMATRON_ERROR" | "-t {resourceDir}/github795b/occultation_prediction_som_manifest.xml" | "" |
| "NASA-PDS/pds4-information-model#797"  | "test schematron for kernel_type checks"             | "github797"  |           3 | "3 errors expected" | "SCHEMATRON_ERROR" | "-t {resourceDir}/github797/test_label1_FAIL.xml {resourceDir}/github797/u5.xml" | "PDS4_GEOM_IngestLDD.xml" |
| "NASA-PDS/pds4-information-model#829"  | "new unit mrad/pixels"          | "github829"  |           0 | "0 errors expected" | "SCHEMATRON_ERROR" | "-t {resourceDir}/github829/ORB_35_STAR_SCANNER.xml" | "PDS4_HST_IngestLDD.xml" |
| "NASA-PDS/pds4-information-model#831"  | "add Array_1D_Spectrum"         | "github831"  |           0 | "0 errors expected" | "SCHEMATRON_ERROR" | "-t {resourceDir}/github831/" | "" |
      

