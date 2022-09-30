// Copyright 2019, California Institute of Technology ("Caltech").
// U.S. Government sponsorship acknowledged.
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// * Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
// * Redistributions must reproduce the above copyright notice, this list of
// conditions and the following disclaimer in the documentation and/or other
// materials provided with the distribution.
// * Neither the name of Caltech nor its operating division, the Jet Propulsion
// Laboratory, nor the names of its contributors may be used to endorse or
// promote products derived from this software without specific prior written
// permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.

package gov.nasa.pds.model.plugin; 
import java.util.ArrayList;
import java.util.Iterator;

public class SchemaFileDefn {
	// identifier is the namespace id, without colon, and in caps; it must be unique within the PDS
	String identifier;
	String lddName;
	boolean isActive;			// isActive=true indicates that the namespace is found in at least one class, attribute, see GetDOMModel #040
	
	// each namespace has a version identifier - model version id
	String versionId;
	
	// each namespace has a label version identifier - xml product version id
	String labelVersionId;

	// steward_id is the primary steward identifier; the steward assigns the namespaceid 
	String stewardId;

	// nameSpaceId is the name Space Id; it must be unique within the PDS; NC stands for No Colon
	String nameSpaceIdNC;		// no colon, original
	String nameSpaceIdNCLC;		// no colon, lower cased
	String nameSpaceIdNCUC;		// no colon, upper cased
	String nameSpaceId;			// colon, lower cased
	String nameSpaceIdNCDir;		// colon, lower cased, underscores replace by slashes, urn:esa:psa:
	
	// nameSpace URL
	String nameSpaceURL;
	String nameSpaceURLs;
	String urnPrefix;
	String modelShortName;
	String sysBundleName;
	String regAuthId;
	
	// governance level
	String governanceLevel;

	// isMaster indicates the master schema
	boolean isMaster;
	
	// isLDD indicates an LDD schema
	boolean isLDD;
	
	// isDiscipline indicates a discipline schema
	boolean isDiscipline;
	
	// isMission indicates a mission schema
	boolean isMission;

	// relative File Specification Names
	String relativeFileSpecModelSpec_DOM;
	String relativeFileSpecXMLSchema;		// base dir path, xml schema dir, base file name, file extension
	String relativeFileSpecSchematron;		// base dir path, xml schema dir, base file name, file extension	
	String relativeFileNameXMLSchema;
	String relativeFileNameXMLSchema2;
	String relativeFileNameSchematron;
	String relativeFileSpecXMLLabel;		// base dir path, xml schema dir, base file name, file extension
	String relativeFileSpecDDDocXML;		
	String relativeFileSpecDDCSV;			
	String relativeFileSpecCCSDSCSV;			
	String relativeFileSpecDDProtPins;		
	String relativeFileSpecDDProtPinsSN;	// short name - used for DD diff comparisons
	String relativeFileSpecDOMModelJSON;	
	String relativeFileSpecModelRDF;
	String relativeFileSpecOWLRDF_DOM;
	String relativeFileSpecSKOSTTL_DOM;
	String relativeFileSpecReportTXT;		
	String relativeFileSpecUMLXMI;
	String relativeFileSpecUMLXMI2;
	String relativeFileSpecModelPVL;
	String relativeFileSpecModelRIM1;
	String relativeFileSpecModelRIM3;
	String relativeFileSpecModelRIM4;
	String relativeFileSpecAttrDefn;
	String relativeFileSpecClassDefn;
	String relativeFileSpecLDDPontMerge;
	
	// LDD File Names
	String sourceFileName;		// complete file name as provided on command line
	String LDDToolInputFileName;			
//	String LDDToolInputFileNameNE;					
	String LDDToolOutputFileNameNE;					
	
	// comment is from the Ingest_LDD template; one is allowed per namespace.
	String comment;
	
	// stewarArr is a list of all stewards that share authority over this namespaceid
	ArrayList <String> stewardArr; 
	
	// various version identifiers
	String ont_version_id;						// 0.1.0.0.a
	String lab_version_id;						// 0100a
	String sch_version_id;						// 1.0.0
	String ns_version_id;						// 01
	String identifier_version_id;				// 0.1
	
	public SchemaFileDefn (String id) {
		identifier = id;
		versionId = "0.0.0.0.n";
		isActive = false;
		lddName = "TBD_lddName";
		labelVersionId = "0.0";
		stewardId = "TBD_stewardId";
		nameSpaceIdNC = id;
		nameSpaceIdNCDir = nameSpaceIdNC;
		nameSpaceIdNCLC = nameSpaceIdNC.toLowerCase();
		nameSpaceIdNCUC = nameSpaceIdNC.toUpperCase();
		nameSpaceId = nameSpaceIdNCLC + ":";
		nameSpaceURL = "TBD_nameSpaceURL";
		nameSpaceURLs = "TBD_nameSpaceURLs";
		urnPrefix = "TBD_urnPrefix";
		modelShortName = "TBD_modelShortName";
		sysBundleName = "system_bundle"; // default system bundle name, can be overridden in the config.properties file.
		regAuthId =  "TBD_regAuthId";
		
		governanceLevel = "TBD_governanceLevel";
		isMaster = false;
		isLDD = false;
		isDiscipline = false;
		isMission = false;
		relativeFileSpecModelSpec_DOM = "TBD_relativeFileSpecModelSpec_DOM";
		relativeFileSpecXMLSchema = "TBD_relativeFileSpecXMLSchema";  // set after setting version_id; see below
		relativeFileSpecSchematron = "TBD_relativeFileSpecSchematron";
		relativeFileNameXMLSchema = "TBD_relativeFileNameXMLSchema";
		relativeFileNameXMLSchema2 = "TBD_relativeFileNameXMLSchema2";
		relativeFileNameSchematron = "TBD_relativeFileNameSchematron";
		relativeFileSpecXMLLabel = "TBD_relativeFileSpecXMLLabel";
		relativeFileSpecDDDocXML = "TBD_relativeFileSpecDDDocXML";	
		relativeFileSpecDDCSV = "TBD_relativeFileSpecDDCSV";
		relativeFileSpecCCSDSCSV = "TBD_relativeFileSpecCCSDSCSV";
		relativeFileSpecDDProtPins = "relativeFileSpecDDProtPins";	
		relativeFileSpecDDProtPinsSN = "relativeFileSpecDDProtPinsSN";
		relativeFileSpecDOMModelJSON = "TBD_relativeFileSpecDOMModelJSON";	
		relativeFileSpecModelRDF = "TBD_relativeFileSpecModelRDF";
		relativeFileSpecOWLRDF_DOM = "TBD_relativeFileSpecOWLRDF_DOM";
		relativeFileSpecSKOSTTL_DOM = "TBD_relativeFileSpecSKOSTTL_DOM";
		relativeFileSpecReportTXT = "TBD_relativeFileSpecReportTXT";
		relativeFileSpecUMLXMI = "TBD_relativeFileSpecUMLXMI";
		relativeFileSpecUMLXMI2 = "TBD_relativeFileSpecUMLXMI2";
		relativeFileSpecModelPVL = "TBD_relativeFileSpecModelPVL";
		relativeFileSpecModelRIM1 = "TBD_relativeFileSpecModelRIM1";
		relativeFileSpecModelRIM3 = "TBD_relativeFileSpecModelRIM3";
		relativeFileSpecModelRIM4 = "TBD_relativeFileSpecModelRIM4";
		relativeFileSpecAttrDefn = "TBD_relativeFileSpecAttrDefn";
		relativeFileSpecClassDefn = "TBD_relativeFileSpecClassDefn";
		relativeFileSpecLDDPontMerge = "TBD_relativeFileSpecLDDPontMerge";

		sourceFileName = "TBD_sourceFileName";
		LDDToolInputFileName = "TBD_LDDToolInputFileName";			
//		LDDToolInputFileNameNE = "TBD_LDDToolInputFileNameNE";					
		LDDToolOutputFileNameNE = "TBD_LDDToolOutputFileNameNE";		
		comment = "This XML schema file has been generated from the Information Model.";		
		stewardArr = new ArrayList <String>();
	} 
	
	public void setStewardIds (String lSteward) {
		stewardId = lSteward;
		stewardArr.add(lSteward);
		return;
	}	
	
	public void setNameSpaceIds (String lNameSpaceIdNC) {
		nameSpaceIdNC = lNameSpaceIdNC;
		nameSpaceIdNCLC = nameSpaceIdNC.toLowerCase();
		nameSpaceIdNCUC = nameSpaceIdNC.toUpperCase();
		nameSpaceId = nameSpaceIdNCLC + ":";
		nameSpaceIdNCDir = nameSpaceIdNC;
		return;
	}
	
	public void setRegAuthority (SchemaFileDefn lSchemaFileDefn) {
		nameSpaceURL = lSchemaFileDefn.nameSpaceURL;
		nameSpaceURLs = lSchemaFileDefn.nameSpaceURLs;
		urnPrefix = lSchemaFileDefn.urnPrefix;
		modelShortName = lSchemaFileDefn.modelShortName;
		sysBundleName = lSchemaFileDefn.sysBundleName;
		regAuthId = lSchemaFileDefn.regAuthId;
		if (urnPrefix.compareTo("urn:esa:psa:") == 0) {
			nameSpaceIdNCDir = nameSpaceIdNCDir.replace('_','/');
		}
		return;
	}
	
	public void setDictionaryType (String lDictionaryType) {
		if (lDictionaryType.compareTo("Common") == 0) {
			governanceLevel = "Common";
			isMaster = true;
			isLDD = false;
			isDiscipline = false;
			isMission = false;
		} else if (lDictionaryType.compareTo("Discipline") == 0) {
			governanceLevel = "Discipline";
			isMaster = false;
			isLDD = true;
			isDiscipline = true;
			isMission = false;
		} else if (lDictionaryType.compareTo("Mission") == 0) {
			governanceLevel = "Mission";
			isMaster = false;
			isLDD = true;
			isDiscipline = false;
			isMission = true;
		}
		return;
	}
	
	//	set the various version identifiers
	public void setVersionIds () {
		// get a cleaned up version id
		// the following attributes are updated.
		//    ont_version_id, lab_version_id, identifier_version_id 
		getCleanVersionId(versionId);
//		*** ont_version_id = DOMInfoModel.ont_version_id; 				// 1.0.0.0
		sch_version_id = ont_version_id;								// 1.0.0.0
//		*** identifier_version_id = DOMInfoModel.identifier_version_id;	// 1.0
//		***lab_version_id = DOMInfoModel.lab_version_id;				// 1000
		ns_version_id = lab_version_id.substring(0,1);					// 1
		
		String lLabelVersionId = getCleanLabelVersionId(DMDocument.infoModelVersionId);
		
		// set the relative file spec now that we have a version id
		relativeFileSpecModelSpec_DOM = DMDocument.outputDirPath + "index" + "_" + lab_version_id + ".html";
		if (! isLDD) {
			relativeFileSpecXMLSchema = DMDocument.outputDirPath + "SchemaXML4/" + DMDocument.mastModelId + "_" + nameSpaceIdNCUC + "_" + lab_version_id + ".xsd";
			relativeFileSpecSchematron = DMDocument.outputDirPath + "SchemaXML4/" + DMDocument.mastModelId + "_" + nameSpaceIdNCUC + "_" + lab_version_id + ".sch";
			relativeFileNameXMLSchema = DMDocument.mastModelId + "_" + nameSpaceIdNCUC + "_" + lab_version_id + ".xsd";
			relativeFileNameXMLSchema2 = DMDocument.mastModelId + "_" + nameSpaceIdNCUC + "_" + lLabelVersionId + "_" + lab_version_id + ".xsd";
			relativeFileNameSchematron = DMDocument.mastModelId + "_" + nameSpaceIdNCUC + "_" + lab_version_id + ".sch";
			relativeFileSpecXMLLabel = DMDocument.outputDirPath + "SchemaXML4/" + DMDocument.mastModelId + "_" + nameSpaceIdNCUC + "_" + lab_version_id + ".xml";
			relativeFileSpecDOMModelJSON = DMDocument.outputDirPath + "export/JSON/" + DMDocument.mastModelId + "_" + nameSpaceIdNCUC + "_" + lab_version_id + ".JSON";	
			relativeFileSpecDDCSV = DMDocument.outputDirPath + "export/csv/" + DMDocument.mastModelId + "_" + nameSpaceIdNCUC + "_" + lab_version_id;			
			relativeFileSpecCCSDSCSV = DMDocument.outputDirPath + "export/csv/" + DMDocument.mastModelId + "_" + nameSpaceIdNCUC + "_CCSDS"  + "_" + lab_version_id;			
		} else {
			relativeFileSpecXMLSchema = DMDocument.outputDirPath + DMDocument.mastModelId + "_" + nameSpaceIdNCUC + "_" + DMDocument.masterPDSSchemaFileDefn.lab_version_id + "_" + lab_version_id + ".xsd";
			relativeFileSpecSchematron = DMDocument.outputDirPath + DMDocument.mastModelId + "_" + nameSpaceIdNCUC + "_" + DMDocument.masterPDSSchemaFileDefn.lab_version_id + "_" + lab_version_id + ".sch";
			relativeFileNameXMLSchema = DMDocument.mastModelId + "_" + nameSpaceIdNCUC + "_" + DMDocument.masterPDSSchemaFileDefn.lab_version_id + "_" + lab_version_id + ".xsd";
			relativeFileNameXMLSchema2 = DMDocument.mastModelId + "_" + nameSpaceIdNCUC + "_" + DMDocument.masterPDSSchemaFileDefn.lab_version_id + "_" + lab_version_id + ".xsd";
			relativeFileNameSchematron = DMDocument.mastModelId + "_" + nameSpaceIdNCUC + "_" + DMDocument.masterPDSSchemaFileDefn.lab_version_id + "_" + lab_version_id + ".sch";
			relativeFileSpecXMLLabel = DMDocument.outputDirPath + DMDocument.mastModelId + "_" + nameSpaceIdNCUC + "_" + DMDocument.masterPDSSchemaFileDefn.lab_version_id + "_" + lab_version_id + ".xml";
			relativeFileSpecDOMModelJSON = DMDocument.outputDirPath + DMDocument.mastModelId + "_" + nameSpaceIdNCUC + "_" + DMDocument.masterPDSSchemaFileDefn.lab_version_id + "_" + lab_version_id + ".JSON";
			relativeFileSpecLDDPontMerge = DMDocument.outputDirPath + DMDocument.mastModelId + "_" + nameSpaceIdNCUC + "_" + DMDocument.masterPDSSchemaFileDefn.lab_version_id + "_" + lab_version_id + ".pont";
			relativeFileSpecReportTXT = DMDocument.outputDirPath + DMDocument.mastModelId + "_" + nameSpaceIdNCUC + "_" + DMDocument.masterPDSSchemaFileDefn.lab_version_id + "_" + lab_version_id + ".txt";	
			relativeFileSpecDDCSV = DMDocument.outputDirPath + DMDocument.mastModelId + "_" + nameSpaceIdNCUC + "_" + DMDocument.masterPDSSchemaFileDefn.lab_version_id + "_" + lab_version_id;			
			relativeFileSpecCCSDSCSV = DMDocument.outputDirPath + DMDocument.mastModelId + "_" + nameSpaceIdNCUC + "_" + DMDocument.masterPDSSchemaFileDefn.lab_version_id + "_CCSDS"  + "_" + lab_version_id;			
		}
		
		relativeFileSpecDDDocXML = DMDocument.outputDirPath + "export/DD/" + DMDocument.mastModelId + "_" + nameSpaceIdNCUC + "_" + "DD" + "_" + lab_version_id + ".xml";	
		relativeFileSpecDDProtPins = DMDocument.outputDirPath + "Model_DataDictionary/" + "dd11179_Gen_" + DMDocument.masterTodaysDateyymmdd + ".pins";	
		relativeFileSpecDDProtPinsSN = DMDocument.outputDirPath + "Model_DataDictionary/" + "dd11179_Gen" + ".pins";	
		relativeFileSpecModelRDF = DMDocument.outputDirPath + "export/rdf/" + DMDocument.mastModelId + "_" + nameSpaceIdNCUC + "_" + "MODEL" + "_" + lab_version_id + ".rdf";		
		relativeFileSpecOWLRDF_DOM = DMDocument.outputDirPath + "export/owl/" + DMDocument.mastModelId + "_" + nameSpaceIdNCUC + "_" + "OWL" + "_" + lab_version_id + ".rdf";
		relativeFileSpecSKOSTTL_DOM = DMDocument.outputDirPath + "export/skos/" + DMDocument.mastModelId + "_" + nameSpaceIdNCUC + "_" + "SKOS" + "_" + lab_version_id + ".ttl";
		relativeFileSpecUMLXMI = DMDocument.outputDirPath + "export/xmi/" + DMDocument.mastModelId + "_" + nameSpaceIdNCUC + "_" + "XMI" + "_clean" + "_" + lab_version_id + ".xmi";	
		relativeFileSpecUMLXMI2 = DMDocument.outputDirPath + "export/xmi/" + DMDocument.mastModelId + "_" + nameSpaceIdNCUC + "_" + "XMI" + "_wNames" + "_" + lab_version_id + ".xmi";	
		relativeFileSpecModelPVL = DMDocument.outputDirPath + "export/pvl/" + DMDocument.mastModelId + "_" + nameSpaceIdNCUC + "_" + "PVL" + "_" + lab_version_id + "_";	
		relativeFileSpecModelRIM1 = DMDocument.outputDirPath + "export/rim/" + DMDocument.mastModelId + "_" + nameSpaceIdNCUC + "_" + "RIM1" + "_" + lab_version_id + ".txt";	
		relativeFileSpecModelRIM3 = DMDocument.outputDirPath + "export/rim/" + DMDocument.mastModelId + "_" + nameSpaceIdNCUC + "_" + "RIM3" + "_" + lab_version_id + ".txt";	
		relativeFileSpecModelRIM4 = DMDocument.outputDirPath + "export/rim/" + DMDocument.mastModelId + "_" + nameSpaceIdNCUC + "_" + "RIM4" + "_" + lab_version_id + ".txt";	
		relativeFileSpecAttrDefn = DMDocument.outputDirPath + "export/defnAttr/";	
		relativeFileSpecClassDefn = DMDocument.outputDirPath + "export/defnClass/";	
		return;
	}
	
	// get clean version id
	void getCleanVersionId(String versionId) {
		// parse out the version id into an array of strings, one string for each digit
		char ic = 'x';
		ArrayList <String> vIdDigitArr = new ArrayList <String> ();
		String vIdDigit = "";
		StringBuffer sbVersionId = new StringBuffer(versionId);
		for (int i = 0; i < sbVersionId.length(); i++) {
			ic = sbVersionId.charAt(i);
			if (ic != '.') {
				if (Character.isDigit(ic)) vIdDigit += new Character(ic).toString();
				else vIdDigit += "x";
			} else {
				vIdDigitArr.add(vIdDigit);
				vIdDigit = "";
			}
		}
		vIdDigitArr.add(vIdDigit);
		
		// ensure that there are four digits
		for (int i = vIdDigitArr.size(); i <  4; i++) {
			vIdDigitArr.add("0");
		}
		
		// get ont_version_id - 1.0.0.0
		String lId = "";
		String lDel = "";
		for (Iterator <String> i = vIdDigitArr.iterator(); i.hasNext();) {
			String lDigit = (String) i.next();
			lId += lDel + lDigit;
			lDel = ".";
		}
		ont_version_id = lId;
		
		// get lab_version_id - 1000
		lId = "";
		for (Iterator <String> i = vIdDigitArr.iterator(); i.hasNext();) {
			String lDigit = (String) i.next();
			if (lDigit.compareTo("10") == 0) lDigit = "A";
			if (lDigit.compareTo("11") == 0) lDigit = "B";
			if (lDigit.compareTo("12") == 0) lDigit = "C";
			if (lDigit.compareTo("13") == 0) lDigit = "D";
			if (lDigit.compareTo("14") == 0) lDigit = "E";
			if (lDigit.compareTo("15") == 0) lDigit = "F";
			if (lDigit.compareTo("16") == 0) lDigit = "G";
			if (lDigit.compareTo("17") == 0) lDigit = "H";
			if (lDigit.compareTo("18") == 0) lDigit = "I";
			if (lDigit.compareTo("19") == 0) lDigit = "J";
			if (lDigit.compareTo("20") == 0) lDigit = "K";
			if (lDigit.compareTo("21") == 0) lDigit = "L";
			lId += lDigit;
		}
		lab_version_id = lId;
		
		// get identifier_version_id - 1.0
		lId = "";
		int cnt = 0;
		lDel = "";
		for (Iterator <String> i = vIdDigitArr.iterator(); i.hasNext();) {
			String lDigit = (String) i.next();
			lId += lDel + lDigit;
			lDel = ".";
			cnt++;
			if (cnt >= 2) break;
		}
		identifier_version_id = lId;
		return;
	}
	
	// get clean label version id
	String getCleanLabelVersionId(String versionId) {
		// parse out the version id into an array of strings, one string for each digit
		String labelVersionID = "";
		char ic = 'x';
		ArrayList <String> vIdDigitArr = new ArrayList <String> ();
		String vIdDigit = "";
		StringBuffer sbVersionId = new StringBuffer(versionId);
		for (int i = 0; i < sbVersionId.length(); i++) {
			ic = sbVersionId.charAt(i);
			if (ic != '.') {
				if (Character.isDigit(ic)) vIdDigit += new Character(ic).toString();
				else vIdDigit += "x";
			} else {
				vIdDigitArr.add(vIdDigit);
				vIdDigit = "";
			}
		}
		vIdDigitArr.add(vIdDigit);
		
		// ensure that there are four digits
		for (int i = vIdDigitArr.size(); i <  4; i++) {
			vIdDigitArr.add("0");
		}
		
		// get lab_version_id - 1000
		String lId = "";
		for (Iterator <String> i = vIdDigitArr.iterator(); i.hasNext();) {
			String lDigit = (String) i.next();
			if (lDigit.compareTo("10") == 0) lDigit = "A";
			if (lDigit.compareTo("11") == 0) lDigit = "B";
			if (lDigit.compareTo("12") == 0) lDigit = "C";
			if (lDigit.compareTo("13") == 0) lDigit = "D";
			if (lDigit.compareTo("14") == 0) lDigit = "E";
			if (lDigit.compareTo("15") == 0) lDigit = "F";
			if (lDigit.compareTo("16") == 0) lDigit = "G";
			if (lDigit.compareTo("17") == 0) lDigit = "H";
			if (lDigit.compareTo("18") == 0) lDigit = "I";
			if (lDigit.compareTo("19") == 0) lDigit = "J";
			if (lDigit.compareTo("20") == 0) lDigit = "K";
			if (lDigit.compareTo("21") == 0) lDigit = "L";
			lId += lDigit;
		}
		labelVersionID = lId;
		
		return labelVersionID;
	}
}
