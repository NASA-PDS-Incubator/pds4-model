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

import java.io.*;
import java.util.*;

// Write the Data Dictionary DocBook file with PDS mentioned removed
// Writes a DocBook file for each namespace id

class WriteDOMDocBookAnon extends Object {
	// class structures
	TreeMap <String, ClassClassificationDefnDOM> classClassificationMap;
	ArrayList <ClassClassificationDefnDOM> classClassificationArr;	
	
	// namespaces to process
	ArrayList <SchemaFileDefn> lSchemaFileDefnToWriteArr;
	
	// attribute structures
	TreeMap <String, AttrClassificationDefnDOM> attrClassificationMap;
	ArrayList <AttrClassificationDefnDOM> attrClassificationArr;
	
	// Miscellaneous
	String writtenNamespaceIds = DMDocument.masterPDSSchemaFileDefn.identifier + " v" + DMDocument.masterPDSSchemaFileDefn.versionId ;
	
// Insert zero-width space characters (&#x200B;) in text strings; form break points for the lines.
	
	public WriteDOMDocBookAnon () {
		// class structures
		classClassificationMap = new TreeMap <String, ClassClassificationDefnDOM> ();
		attrClassificationMap = new TreeMap <String, AttrClassificationDefnDOM> ();
		
		// get the current namespaces and initialize classification maps
		if (! DMDocument.LDDToolFlag) {
			// only the common dictionary (pds)
			lSchemaFileDefnToWriteArr = new ArrayList <SchemaFileDefn> ();
			lSchemaFileDefnToWriteArr.add(DMDocument.masterPDSSchemaFileDefn);
		} else {
			// intialize array to capture namespaces to be written
			ArrayList <String> lDDNamespaceArr = new ArrayList <String> ();
			lSchemaFileDefnToWriteArr = new ArrayList <SchemaFileDefn> ();
			
			// first get the common dictionary
			ArrayList <SchemaFileDefn> tempSchemaFileDefnToWriteArr = new ArrayList <SchemaFileDefn> ();
			tempSchemaFileDefnToWriteArr.add(DMDocument.masterPDSSchemaFileDefn);
    		lSchemaFileDefnToWriteArr.add(DMDocument.masterPDSSchemaFileDefn);
    		lDDNamespaceArr.add(DMDocument.masterPDSSchemaFileDefn.nameSpaceIdNC);
            
			// now add all the LDDs that are stacked for this run
            tempSchemaFileDefnToWriteArr = new ArrayList <SchemaFileDefn> (DMDocument.LDDSchemaFileSortMap.values());
			for(SchemaFileDefn lSchemaFileDefn : tempSchemaFileDefnToWriteArr) {
            	if (! lDDNamespaceArr.contains(lSchemaFileDefn.nameSpaceIdNC)) {
        			lSchemaFileDefnToWriteArr.add(lSchemaFileDefn);
        			lDDNamespaceArr.add(lSchemaFileDefn.nameSpaceIdNC);
            	}
            }
		}
		
//		System.out.println("");
		for (Iterator <SchemaFileDefn> i = lSchemaFileDefnToWriteArr.iterator(); i.hasNext();) {
			SchemaFileDefn lSchemaFileDefn = (SchemaFileDefn) i.next();
			classClassificationMap.put(lSchemaFileDefn.nameSpaceIdNC, new ClassClassificationDefnDOM (lSchemaFileDefn.nameSpaceIdNC));
			attrClassificationMap.put(lSchemaFileDefn.nameSpaceIdNC, new AttrClassificationDefnDOM (lSchemaFileDefn.nameSpaceIdNC));
		}
		
		classClassificationMap.put("pds.product", new ClassClassificationDefnDOM ("pds.product"));
		if (DMDocument.pds4ModelFlag) classClassificationMap.put("pds.ops", new ClassClassificationDefnDOM ("pds.ops"));
		classClassificationMap.put("pds.support", new ClassClassificationDefnDOM ("pds.support"));
		classClassificationMap.put("pds.other", new ClassClassificationDefnDOM ("pds.other"));

		classClassificationMap.put("other", new ClassClassificationDefnDOM ("other"));
		classClassificationArr = new ArrayList <ClassClassificationDefnDOM> (classClassificationMap.values());
		attrClassificationMap.put("other", new AttrClassificationDefnDOM ("other"));
		attrClassificationArr = new ArrayList <AttrClassificationDefnDOM> (attrClassificationMap.values());
		
		// classify attributes and classes
		// get the class classification maps
		for (Iterator <DOMClass> i = DOMInfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
			DOMClass lClass = (DOMClass) i.next();
			if (lClass.isInactive) continue;
			getClassClassification (lClass);
		}
		
		// get the classification arrays
		for (Iterator <ClassClassificationDefnDOM> i = classClassificationArr.iterator(); i.hasNext();) {
			ClassClassificationDefnDOM lClassClassificationDefn = (ClassClassificationDefnDOM) i.next();
			lClassClassificationDefn.classArr = new ArrayList <DOMClass> (lClassClassificationDefn.classMap.values());
		}
		
		// get the attribute classification maps
		for (Iterator <DOMAttr> i = DOMInfoModel.masterDOMAttrArr.iterator(); i.hasNext();) {
			DOMAttr lAttr = (DOMAttr) i.next();
			if (lAttr.isInactive) continue;
			getAttrClassification (lAttr);
		}
		
		// get the classification arrays
		for (Iterator <AttrClassificationDefnDOM> i = attrClassificationArr.iterator(); i.hasNext();) {
			AttrClassificationDefnDOM lAttrClassificationDefn = (AttrClassificationDefnDOM) i.next();
			lAttrClassificationDefn.attrArr = new ArrayList <DOMAttr> (lAttrClassificationDefn.attrMap.values());
		}
		
		// print out the class and attribute counts
		DMDocument.registerMessage ("0>info " + "DD DocBook Class Counts");
		Set <String> set9 = classClassificationMap.keySet();
		Iterator <String> iter9 = set9.iterator();
		while(iter9.hasNext()) {
			String lId = (String) iter9.next();
			ClassClassificationDefnDOM lClassClassificationDefnDOM = classClassificationMap.get(lId);
			if (lClassClassificationDefnDOM != null ) {
				DMDocument.registerMessage ("0>info " + " - namespace: " + lId + "   size: " + lClassClassificationDefnDOM.classArr.size());
				if (lClassClassificationDefnDOM.classArr.size() > 0) {
					if (! (lId.compareTo(DMDocument.masterNameSpaceIdNCLC) == 0
							|| lId.compareTo("pds.product") == 0 
							|| lId.compareTo("pds.ops") == 0 
							|| lId.compareTo("pds.support") == 0 
							|| lId.compareTo("pds.other") == 0 
							|| lId.compareTo("other") == 0 )) {
						writtenNamespaceIds += ", " + lId;
						SchemaFileDefn lSchemaFileDefn = DMDocument.LDDSchemaFileSortMap.get(lId);
						if (lSchemaFileDefn != null)
							writtenNamespaceIds += " v" + lSchemaFileDefn.versionId;
					}
				}
			}
		} 
		
		DMDocument.registerMessage ("0>info " + "DD DocBook Attribute Counts");
		Set <String> set92 = classClassificationMap.keySet();
		Iterator <String> iter92 = set92.iterator();
		while(iter92.hasNext()) {
			String lId = (String) iter92.next();
			AttrClassificationDefnDOM lAttrClassificationDefnDOM = attrClassificationMap.get(lId);
			if (lAttrClassificationDefnDOM != null )
				DMDocument.registerMessage ("0>info " + " - namespace: " + lId + "   size: " + lAttrClassificationDefnDOM.attrArr.size());
		}
		return;
	}
	
	public void getClassClassification (DOMClass lClass) {
		if (lClass.isDataType) return;
		if (lClass.isUnitOfMeasure) return;
		
		// classify the class by namespace and other criteria
		ClassClassificationDefnDOM lClassClassificationDefn = classClassificationMap.get(lClass.nameSpaceIdNC);
		if (lClassClassificationDefn != null) {			
			if (lClass.nameSpaceIdNC.compareTo(DMDocument.masterNameSpaceIdNCLC) == 0) {
				// i.e., the Common directory, pds
				if (lClass.steward.compareTo("ops") == 0) {
					lClassClassificationDefn = classClassificationMap.get("pds.ops");
					lClassClassificationDefn.classMap.put(lClass.identifier, lClass);
				} else if (lClass.isRegistryClass) {
					lClassClassificationDefn = classClassificationMap.get("pds.product");
					lClassClassificationDefn.classMap.put(lClass.identifier, lClass);	
				} else {
					lClassClassificationDefn = classClassificationMap.get("pds.support");
					lClassClassificationDefn.classMap.put(lClass.identifier, lClass);	
				}
			} else {
				// all LDD namespaces
				lClassClassificationDefn.classMap.put(lClass.identifier, lClass);
			}
		} else {
			lClassClassificationDefn = classClassificationMap.get("other");
			lClassClassificationDefn.classMap.put(lClass.identifier, lClass);
		}
		return;
	}
	
	public void getAttrClassification (DOMAttr lAttr) {
		if (! (lAttr.isUsedInClass && lAttr.isAttribute)) return;

		// classify the class by namespace and other criteria
		String lAttrId = lAttr.title + "." + lAttr.nameSpaceIdNC + "." + lAttr.attrParentClass.title + "." + lAttr.classNameSpaceIdNC + "." +  DMDocument.registrationAuthorityIdentifierValue;
		AttrClassificationDefnDOM lAttrClassificationDefn = attrClassificationMap.get(lAttr.nameSpaceIdNC);
		if (lAttrClassificationDefn != null) {	
			lAttrClassificationDefn.attrMap.put(lAttrId, lAttr);	
		} else {
			lAttrClassificationDefn = attrClassificationMap.get("other");
			lAttrClassificationDefn.attrMap.put(lAttrId, lAttr);
		}
		return;
	}
	
//	print the DocBook Files
	public void writeDocBooks (SchemaFileDefn lSchemaFileDefn) throws java.io.IOException {
		String lFileName = lSchemaFileDefn.relativeFileSpecDDDocXML;
		String lLabelVersionId = "_" + DMDocument.masterPDSSchemaFileDefn.lab_version_id;
		String lDOMLabelVersionId = lLabelVersionId;
		lFileName = DMDocument.replaceString (lFileName, lLabelVersionId, lDOMLabelVersionId);
		
        // iterate over the namespace ids
		for (SchemaFileDefn lSchemaFileDefnToWrite : lSchemaFileDefnToWriteArr) {
			ClassClassificationDefnDOM lClassClassificationDefn = classClassificationMap.get(lSchemaFileDefnToWrite.nameSpaceIdNCLC);
			AttrClassificationDefnDOM lAttrClassificationDefn = attrClassificationMap.get(lSchemaFileDefnToWrite.nameSpaceIdNCLC);
			writeDocBook (lSchemaFileDefnToWrite, lClassClassificationDefn, lAttrClassificationDefn);
		}
		return;
	}	
	
//	write a DocBook File
	public void writeDocBook (SchemaFileDefn lSchemaFileDefn, ClassClassificationDefnDOM lClassClassificationDefn, AttrClassificationDefnDOM lAttrClassificationDefn) throws java.io.IOException {
		String lFileName = lSchemaFileDefn.relativeFileSpecDDDocXML;
		String lLabelVersionId = "_" + DMDocument.masterPDSSchemaFileDefn.lab_version_id;
		String lDOMLabelVersionId = lLabelVersionId;
		lFileName = DMDocument.replaceString (lFileName, lLabelVersionId, lDOMLabelVersionId);
		PrintWriter prDocBook = new PrintWriter(new OutputStreamWriter (new FileOutputStream(new File(lFileName)), "UTF-8"));
		writeHeader (lSchemaFileDefn, prDocBook);
        
        // write the sections
			if (lClassClassificationDefn != null) {
				if (lClassClassificationDefn.classArr.size() > 0)
					writeClassSection (lSchemaFileDefn, lClassClassificationDefn, prDocBook);
			}
			if (lAttrClassificationDefn != null) {
				if (lAttrClassificationDefn.attrArr.size() > 0)
					writeAttrSection (lSchemaFileDefn, lAttrClassificationDefn, prDocBook);
			}
        
        // write the Data Types and Units
		writeDataTypeSection (DMDocument.masterNameSpaceIdNCLC, prDocBook);
		writeUnitsSection (DMDocument.masterNameSpaceIdNCLC, prDocBook);
		writeFooter (prDocBook);
		prDocBook.close();
		return;
	}
	
	private void writeClassSection (SchemaFileDefn lSchemaFileDefn, ClassClassificationDefnDOM lClassClassificationDefn, PrintWriter prDocBook) {
        prDocBook.println("");	
        prDocBook.println("      <!-- ===================== LDD Class Begin =========================== -->");
        prDocBook.println("");
		
		prDocBook.println("        <chapter>");
//		prDocBook.println("           <title>Classes in the " + lClassClassificationDefn.namespaceId + " namespace.</title>");
		prDocBook.println("           <title>Classes in the " + lSchemaFileDefn.lddName + " dictionary.</title>");
		prDocBook.println("           <para>These classes comprise the namespace.</para>");
		for (Iterator <DOMClass> j = lClassClassificationDefn.classArr.iterator(); j.hasNext();) {
			DOMClass lClass = (DOMClass) j.next();
			writeClass (lClass, prDocBook);						
		}
		prDocBook.println("        </chapter>");
        prDocBook.println("");
		
        prDocBook.println("      <!-- ===================== LDD Class End =========================== -->");
        prDocBook.println("");
	}	
		
	private void writeAttrSection (SchemaFileDefn lSchemaFileDefn, AttrClassificationDefnDOM lAttrClassificationDefn, PrintWriter prDocBook) {
		prDocBook.println("");	
		prDocBook.println("      <!-- ===================== LDD Attribute Begin =========================== -->");
		prDocBook.println("");

		prDocBook.println("        <chapter>");
//		prDocBook.println("           <title>Attributes in the " + lAttrClassificationDefn.namespaceId + " namespace.</title>");
		prDocBook.println("           <title>Attributes in the " + lSchemaFileDefn.lddName  + " dictionary.</title>");
		prDocBook.println("           <para>These attributes are used by the classes in the " + lAttrClassificationDefn.namespaceId + " namespace. </para>");
		for (Iterator <DOMAttr> j = lAttrClassificationDefn.attrArr.iterator(); j.hasNext();) {
			DOMAttr lAttr = (DOMAttr) j.next();
			writeAttr (lAttr, prDocBook);						
		}
		prDocBook.println("        </chapter>");
		prDocBook.println("");
		
		prDocBook.println("      <!-- ===================== LDD Attribute Begin =========================== -->");
		prDocBook.println("");
	}
	
	private void writeClass (DOMClass lClass, PrintWriter prDocBook) {
 		String lValueString = "";
 		String lValueDel = "";
        String lRegistrationStatus = "Active";
        String lRegistrationStatusInsert = "";
        if (lClass.registrationStatus.compareTo("Retired") == 0) {
        	lRegistrationStatus = "Deprecated";
        	lRegistrationStatusInsert = " " + DMDocument.Literal_DEPRECATED;;
        }
 		prDocBook.println("<sect1>");
    	prDocBook.println("    <title>" + getClassAnchor(lClass) + getValue(lClass.title) + lRegistrationStatusInsert + "</title>");
        prDocBook.println("");
        prDocBook.println("<para>");
        prDocBook.println("    <informaltable frame=\"all\" colsep=\"1\">");
        prDocBook.println("        <tgroup cols=\"4\" align=\"left\" colsep=\"1\" rowsep=\"1\">");
        prDocBook.println("            <colspec colnum=\"1\" colname=\"c1\" colwidth=\"1.0*\"/>");
        prDocBook.println("            <colspec colnum=\"2\" colname=\"c2\" colwidth=\"1.0*\"/>");
        prDocBook.println("            <colspec colnum=\"3\" colname=\"c3\" colwidth=\"1.0*\"/>");
        prDocBook.println("            <colspec colnum=\"4\" colname=\"c4\" colwidth=\"1.0*\"/>");
        prDocBook.println("            <thead>");
        prDocBook.println("                <row>");
        prDocBook.println("                    <entry namest=\"c1\" nameend=\"c3\" align=\"left\">" + getPrompt("Name: ") + getValue(lClass.title) + lRegistrationStatusInsert + "</entry>");
        prDocBook.println("                    <entry>" + getPrompt("Version Id: ") + getValue(lClass.versionId) + "</entry>");
        prDocBook.println("                </row>");
        prDocBook.println("            </thead>");
        prDocBook.println("            <tbody>");
        prDocBook.println("                <row>");
        prDocBook.println("                    <entry namest=\"c1\" nameend=\"c4\" align=\"left\">" + getPrompt("Description:") + getValue(lClass.definition) + "</entry>");
        prDocBook.println("                </row>");
        prDocBook.println("                <row>");
        prDocBook.println("                    <entry>" + getPrompt("Namespace Id: ") + getValue(lClass.nameSpaceIdNC) + "</entry>");
        prDocBook.println("                    <entry>" + getPrompt("Steward: ") + getValue(lClass.steward) + "</entry>");
        prDocBook.println("                    <entry>" + getPrompt("Role: ") + getValue(lClass.role) + "</entry>");
        prDocBook.println("                    <entry>" + getPrompt("Status: ") + lRegistrationStatus + "</entry>");
        prDocBook.println("                </row>");
        prDocBook.println("");
        
        // write hierarchy
 		ArrayList <DOMClass> lClassArr = new ArrayList <DOMClass> (lClass.superClassHierArr);
 		lClassArr.add(lClass);
 		lValueString = "";
 		lValueDel = "";
		for (Iterator <DOMClass> i = lClassArr.iterator(); i.hasNext();) {
			DOMClass lHierClass = (DOMClass) i.next();
			lValueString += lValueDel + getClassLink(lHierClass);
			lValueDel = " :: "; 
		}
        prDocBook.println("                <row>");
        prDocBook.println("                    <entry namest=\"c1\" nameend=\"c4\" align=\"left\">" + getPrompt("Class Hierarchy: ") + lValueString + "</entry>");
        prDocBook.println("                </row>");

        // determine the type and number of class members (attributes and classes)
		int attrCount = 0, assocCount= 0;
        if (lClass.allAttrAssocArr != null) {
			for (Iterator <DOMProp> i = lClass.allAttrAssocArr.iterator(); i.hasNext();) {
				DOMProp lProp = (DOMProp) i.next();
				if (lProp.isAttribute) {
					attrCount ++;
				} else {
					assocCount ++;
				}
			}
        }
        
		// write the attributes
		if (attrCount == 0) {
		       prDocBook.println("                <row>");
	           prDocBook.println("                    <entry>" + getPrompt("No Attributes") + "</entry>");
	           prDocBook.println("                    <entry namest=\"c2\" nameend=\"c4\" align=\"left\"></entry>");
	           prDocBook.println("                </row>");
		} else {
            prDocBook.println("                <row>");
            prDocBook.println("                    <entry>" + getPrompt("Attribute(s)") + "</entry>");
            prDocBook.println("                    <entry>" + getPrompt("Name") + "</entry>");
            prDocBook.println("                    <entry>" + getPrompt("Cardinality") + "</entry>");
            prDocBook.println("                    <entry>" + getPrompt("Value") + "</entry>");
            prDocBook.println("                </row>");
            
			for (Iterator <DOMProp> j = lClass.allAttrAssocArr.iterator(); j.hasNext();) {
				DOMProp lProp = (DOMProp) j.next();
				if (! lProp.isAttribute) continue;
				DOMAttr lAttr = (DOMAttr)lProp.hasDOMObject;
	            lValueString = "None";
	            lValueDel = "";
	    		if ( ! (lAttr.domPermValueArr == null || lAttr.domPermValueArr.size() == 0)) {
		            lValueString = "";
	    			for (Iterator <DOMProp> k = lAttr.domPermValueArr.iterator(); k.hasNext();) {
	    				DOMProp lDOMProp = (DOMProp) k.next();
	    				if (! (lDOMProp.hasDOMObject instanceof DOMPermValDefn))  continue;	    				
	    			    DOMPermValDefn lPermValueDefn = (DOMPermValDefn) lDOMProp.hasDOMObject;
	    			    lValueString += lValueDel + getValueLink(lAttr, getValue(lPermValueDefn.value));
	    				lValueDel = ", ";
	    			}
	    		}
	            prDocBook.println("                <row>");
	            prDocBook.println("                    <entry></entry>");
	            prDocBook.println("                    <entry>" + getAttrLink(lAttr) + "</entry>");
	            prDocBook.println("                    <entry>" + getValue(getCardinality(lAttr.cardMinI, lAttr.cardMaxI)) + "</entry>");
	            prDocBook.println("                    <entry>" + lValueString + "</entry>");
	            prDocBook.println("                </row>");	
			}
		}
		
		// write the associations
		if (assocCount == 0) {
		       prDocBook.println("                <row>");
	           prDocBook.println("                    <entry>" + getPrompt("No Associations") + "</entry>");
	           prDocBook.println("                    <entry namest=\"c2\" nameend=\"c4\" align=\"left\"></entry>");
	           prDocBook.println("                </row>");
		} else {

			// write the associations
            prDocBook.println("                <row>");
            prDocBook.println("                    <entry>" + getPrompt("Association(s)") + "</entry>");
            prDocBook.println("                    <entry>" + getPrompt("Name") + "</entry>");
            prDocBook.println("                    <entry>" + getPrompt("Cardinality") + "</entry>");
            prDocBook.println("                    <entry>" + getPrompt("Class") + "</entry>");
            prDocBook.println("                </row>");
            
            // first get array of member classes for this association
            TreeMap <String, String> lPropOrderMap = new TreeMap <String, String> ();
            TreeMap <String, String> lPropCardMap = new TreeMap <String, String> ();
            TreeMap <String, TreeMap <String, DOMClass>> lPropMemberClassMap = new TreeMap <String, TreeMap <String, DOMClass>> ();

            Integer lSeqNumI = 1000;
            for (Iterator <DOMProp> j = lClass.allAttrAssocArr.iterator(); j.hasNext();) {       	
				DOMProp lDOMProp = (DOMProp) j.next();
				if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMClass) {
					DOMClass lMemberDOMClass = (DOMClass) lDOMProp.hasDOMObject;
					TreeMap <String, DOMClass> lMemberClassMap = lPropMemberClassMap.get(lDOMProp.title);
					if (lMemberClassMap != null) {
						lMemberClassMap.put(lMemberDOMClass.title, lMemberDOMClass);
					} else {
						lSeqNumI++;
						String lSeqNum = lSeqNumI.toString();
						lPropOrderMap.put(lSeqNum, lDOMProp.title);
						lMemberClassMap = new TreeMap <String, DOMClass> ();
						lMemberClassMap.put(lMemberDOMClass.title, lMemberDOMClass);
						lPropMemberClassMap.put(lDOMProp.title, lMemberClassMap);
						lPropCardMap.put(lDOMProp.title, getValue(getCardinality(lDOMProp.cardMinI, lDOMProp.cardMaxI)));
					}
	    		}
			}
            
            // write the member classes
			ArrayList <String> lPropOrderArr = new ArrayList <String> (lPropOrderMap.values());
			for (Iterator <String> j = lPropOrderArr.iterator(); j.hasNext();) {
				String lDOMPropTitle = (String) j.next();
				TreeMap <String, DOMClass> lMemberClassMap = lPropMemberClassMap.get(lDOMPropTitle);
				String lCardString = lPropCardMap.get(lDOMPropTitle);
				if (lCardString != null && lMemberClassMap != null) {
					ArrayList <DOMClass> lMemberClassArr = new ArrayList <DOMClass> (lMemberClassMap.values());
		            lValueString = "";
		            lValueDel = "";
		            for (Iterator <DOMClass> k = lMemberClassArr.iterator(); k.hasNext();) {
						DOMClass lMemberClass = (DOMClass) k.next();
		    		    lValueString += lValueDel + getClassLink(lMemberClass);
			    		lValueDel = ", ";
					}
	                prDocBook.println("                <row>");
	                prDocBook.println("                    <entry></entry>");
	                prDocBook.println("                    <entry>" + getValueBreak(lDOMPropTitle) + "</entry>");
	                prDocBook.println("                    <entry>" + lCardString + "</entry>");
	                prDocBook.println("                    <entry>" + lValueString + "</entry>");
	                prDocBook.println("                </row>");
	    		}
			}
		}
		
        // write the references
 		ArrayList <DOMClass> lRefClassArr = getClassReferences (lClass);
 		lValueString = "";
 		lValueDel = "";
 		if (! (lRefClassArr == null || lRefClassArr.isEmpty())) {
 			for (Iterator <DOMClass> i = lRefClassArr.iterator(); i.hasNext();) {
 				DOMClass lRefClass = (DOMClass) i.next();
 				lValueString += lValueDel + getClassLink(lRefClass);
 				lValueDel = ", ";
 			}
 		} else {
 	 		lValueString = "None";
 		}
        prDocBook.println("                <row>");
//        prDocBook.println("                    <entry namest=\"c1\" nameend=\"c4\" align=\"left\">" + getPrompt("Referenced from: ") + getValue(lValueString) + "</entry>");
        prDocBook.println("                    <entry namest=\"c1\" nameend=\"c4\" align=\"left\">" + getPrompt("Referenced from: ") + lValueString + "</entry>");
        prDocBook.println("                </row>");
        prDocBook.println("            </tbody>");
        prDocBook.println("        </tgroup>");
        prDocBook.println("        </informaltable>");
        prDocBook.println("</para>");
    	prDocBook.println("</sect1> ");
        return;
	}	
				
	private void writeAttrSection (String lNameSpaceId, PrintWriter prDocBook) {
        prDocBook.println("");	
        prDocBook.println("      <!-- =====================Part3 Begin=========================== -->");
        prDocBook.println("");
		
		AttrClassificationDefnDOM lAttrClassificationDefn = attrClassificationMap.get(DMDocument.masterNameSpaceIdNCLC);
		if (lAttrClassificationDefn != null) {
			prDocBook.println("        <chapter>");
			prDocBook.println("           <title>Attributes in the common namespace.</title>");
			prDocBook.println("           <para>These attributes are used by the classes in the common namespace. </para>");
			for (Iterator <DOMAttr> j = lAttrClassificationDefn.attrArr.iterator(); j.hasNext();) {
				DOMAttr lAttr = (DOMAttr) j.next();
				writeAttr (lAttr, prDocBook);						
			}
			prDocBook.println("        </chapter>");
	        prDocBook.println("");
		}
		
        prDocBook.println("      <!-- =====================Part3 End=========================== -->");
        prDocBook.println("");
	}
	
	private void writeAttr (DOMAttr lAttr, PrintWriter prDocBook) {
        String lRegistrationStatus = "Active";
        String lRegistrationStatusInsert = "";
        if (lAttr.registrationStatus.compareTo("Retired") == 0) {
        	lRegistrationStatus = "Deprecated";
        	lRegistrationStatusInsert = " " + DMDocument.Literal_DEPRECATED;;
        }
	    prDocBook.println("<sect1>");

	    prDocBook.println("    <title>" + getValue(lAttr.title) + "  in  " + getClassLink(lAttr.attrParentClass) + lRegistrationStatusInsert + "</title>");
	    prDocBook.println("");
	    prDocBook.println("<para>");
	    prDocBook.println("    <informaltable frame=\"all\" colsep=\"1\">");
	    prDocBook.println("        <tgroup cols=\"4\" align=\"left\" colsep=\"1\" rowsep=\"1\">");
	    prDocBook.println("            <colspec colnum=\"1\" colname=\"c1\" colwidth=\"1.0*\"/>");
	    prDocBook.println("            <colspec colnum=\"2\" colname=\"c2\" colwidth=\"1.0*\"/>");
	    prDocBook.println("            <colspec colnum=\"3\" colname=\"c3\" colwidth=\"1.0*\"/>");
	    prDocBook.println("            <colspec colnum=\"4\" colname=\"c4\" colwidth=\"1.0*\"/>");
	    prDocBook.println("            <thead>");
	    prDocBook.println("                <row>");
	    prDocBook.println("                    <entry namest=\"c1\" nameend=\"c3\" align=\"left\">" + getPrompt("Name: ") + getAttrAnchor(lAttr) + getValue(lAttr.title) + lRegistrationStatusInsert + "</entry>");
	    prDocBook.println("                    <entry>" + getPrompt("Version Id: ") + getValue("1.0.0.0") + "</entry>");
	    prDocBook.println("                </row>");
	    prDocBook.println("            </thead>");
	    prDocBook.println("            <tbody>");
	    prDocBook.println("                <row>");
	    prDocBook.println("                    <entry namest=\"c1\" nameend=\"c4\" align=\"left\">" + getPrompt("Description: ") + getValue(lAttr.definition) + "</entry>");
	    prDocBook.println("                </row>");
	    prDocBook.println("                <row>");
	    prDocBook.println("                    <entry>" + getPrompt("Namespace Id: ") + getValue(lAttr.getNameSpaceIdNC ()) + "</entry>");
	    prDocBook.println("                    <entry>" + getPrompt("Steward: ") + getValue(lAttr.getSteward ()) + "</entry>");
	    prDocBook.println("                    <entry>" + getPrompt("Class Name: ") + getClassLink(lAttr.attrParentClass) + "</entry>");
	    prDocBook.println("                    <entry>" + getPrompt("Type: ") + getDataTypeLink(lAttr.valueType) + "</entry>");
	    prDocBook.println("                </row>");
	    prDocBook.println("                <row>");

	    prDocBook.println("                    <entry>" + getPrompt("Minimum Value: ") + getValueReplaceTBDWithNone(lAttr.getMinimumValue (true, false)) + "</entry>");
	    prDocBook.println("                    <entry>" + getPrompt("Maximum Value: ") + getValueReplaceTBDWithNone(lAttr.getMaximumValue (true, false)) + "</entry>");
	    prDocBook.println("                    <entry>" + getPrompt("Minimum Characters: ") + getValueReplaceTBDWithNone(lAttr.getMinimumCharacters (true, false)) + "</entry>");
	    prDocBook.println("                    <entry>" + getPrompt("Maximum Characters: ") + getValueReplaceTBDWithNone(lAttr.getMaximumCharacters (true, false)) + "</entry>");
	    prDocBook.println("                </row>");
	    
	    
	    prDocBook.println("                <row>");
	    prDocBook.println("                    <entry>" + getPrompt("Unit of Measure Type: ") + getUnitIdLink(lAttr.unit_of_measure_type) + "</entry>");
	    prDocBook.println("                    <entry>" + getPrompt("Default Unit Id: ") + getValueReplaceTBDWithNone(lAttr.default_unit_id) + "</entry>");
	    prDocBook.println("                    <entry>" + getPrompt("Attribute Concept: ") + getValueReplaceTBDWithNone(lAttr.classConcept) + "</entry>");
	    prDocBook.println("                    <entry>" + getPrompt("Conceptual Domain: ") + getValueReplaceTBDWithNone(lAttr.dataConcept) + "</entry>");
	    prDocBook.println("                </row>");
	    
	    prDocBook.println("                <row>");
	    prDocBook.println("                    <entry>" + getPrompt("Status: ") + lRegistrationStatus + "</entry>");
	    prDocBook.println("                    <entry>" + getPrompt("Nillable: ") + lAttr.isNilable + "</entry>");            
//	    prDocBook.println("                    <entry namest=\"c3\" nameend=\"c4\" >" + getPrompt("Pattern: ") + getValueReplaceTBDWithNone(lAttr.pattern) + "</entry>");
	    prDocBook.println("                    <entry namest=\"c3\" nameend=\"c4\" >" + getPrompt("Pattern: ") + getValueReplaceTBDWithNone(lAttr.getPattern(true)) + "</entry>");
	    prDocBook.println("                </row>");
	    prDocBook.println("");
	    
		if (lAttr.domPermValueArr == null || lAttr.domPermValueArr.size() == 0) {
	       prDocBook.println("                <row>");
	       prDocBook.println("                    <entry>" + getPrompt("Permissible Value(s)") + "</entry>");
	       prDocBook.println("                    <entry>" + getPrompt("No Values") + "</entry>");
	       prDocBook.println("                    <entry namest=\"c3\" nameend=\"c4\" align=\"left\"></entry>");
	       prDocBook.println("                </row>");	
		} else {
	       prDocBook.println("                <row>");
	       prDocBook.println("                    <entry>" + getPrompt("Permissible Value(s)") + "</entry>");
	       prDocBook.println("                    <entry>" + getPrompt("Value") + "</entry>");
	       prDocBook.println("                    <entry namest=\"c3\" nameend=\"c4\" align=\"left\">" + getPrompt("Value Meaning") + "</entry>");
	       prDocBook.println("                </row>");	

			for (Iterator <DOMProp> j = lAttr.domPermValueArr.iterator(); j.hasNext();) {
				DOMProp lProp = (DOMProp) j.next();
				if (! (lProp.hasDOMObject instanceof DOMPermValDefn))  continue;
				DOMPermValDefn lPermValueDefn = (DOMPermValDefn) lProp.hasDOMObject;
				if (lPermValueDefn.value.compareTo("...") == 0) continue;
				lRegistrationStatusInsert = "";
				if (lPermValueDefn.registrationStatus.compareTo("Retired") == 0) lRegistrationStatusInsert = " - " + DMDocument.Literal_DEPRECATED;

				String lDependValue = lAttr.valueDependencyMap.get(lPermValueDefn.value);
				String lDependClause = "";
				if (lDependValue != null) lDependClause = " (" + lDependValue + ")";								
				
				String lValueMeaning = lPermValueDefn.value_meaning;
				if (lValueMeaning == null) lValueMeaning = "TBD_value_meaning";
			    
				if (lAttr.title.compareTo("pattern") == 0) {
					if ((lValueMeaning == null) || (lValueMeaning.indexOf("TBD") == 0))
                        lValueMeaning = "";
				}
	 	        prDocBook.println("                <row>");
		        prDocBook.println("                    <entry></entry>"); 
		        prDocBook.println("                    <entry>" + getValueAnchor(lAttr, getValue(lPermValueDefn.value)) + getValueBreak (getValue(lPermValueDefn.value)) + getValue(lDependClause) + lRegistrationStatusInsert + "</entry>");
		        prDocBook.println("                    <entry namest=\"c3\" nameend=\"c4\" align=\"left\">" + getValue(lValueMeaning) + "</entry>");
		        prDocBook.println("                </row>");
			}
		}
		if (! (lAttr.permValueExtArr == null || lAttr.permValueExtArr.isEmpty())) {
		
			for (Iterator <PermValueExtDefn> i = lAttr.permValueExtArr.iterator(); i.hasNext();) {
				PermValueExtDefn lPermValueExt = (PermValueExtDefn) i.next();	
				if (lPermValueExt.permValueExtArr == null || lPermValueExt.permValueExtArr.isEmpty()) continue;
			       prDocBook.println("                <row>");
			       prDocBook.println("                    <entry>" + getPrompt("Extended Value(s) for: " + getValueBreak (lPermValueExt.xpath)) + "</entry>");
			       prDocBook.println("                    <entry>" + getPrompt("Value") + "</entry>");
			       prDocBook.println("                    <entry namest=\"c3\" nameend=\"c4\" align=\"left\">" + getPrompt("Value Meaning") + "</entry>");
			       prDocBook.println("                </row>");	
			
				for (Iterator <PermValueDefn> j = lPermValueExt.permValueExtArr.iterator(); j.hasNext();) {
					PermValueDefn lPermValueDefn = (PermValueDefn) j.next();
		 	        prDocBook.println("                <row>");
			        prDocBook.println("                    <entry></entry>"); 
			        prDocBook.println("                    <entry>" + getValueBreak (lPermValueDefn.value) + "</entry>");
//			        prDocBook.println("                    <entry>" + getValueAnchor(lAttr, lPermValueDefn.value) + getValueBreak (lPermValueDefn.value) + "</entry>");
			        prDocBook.println("                    <entry namest=\"c3\" nameend=\"c4\" align=\"left\">" + getValue(lPermValueDefn.value_meaning) + "</entry>");
			        prDocBook.println("                </row>");
				}
			}
		}
	               
	    prDocBook.println("            </tbody>");
	    prDocBook.println("        </tgroup>");
	    prDocBook.println("        </informaltable>");
	    prDocBook.println("</para>");
	  	prDocBook.println("</sect1> ");
	  	prDocBook.println("");
	}
	
	private void writeDataTypeSection (String lNameSpaceIdNC, PrintWriter prDocBook) {
		ArrayList <String> lPatternArr = new ArrayList<String> ();
        prDocBook.println("");
        prDocBook.println("      <!-- =====================Part4a Begin=========================== -->");
        prDocBook.println("");
//		prDocBook.println("    <chapter>");
//		prDocBook.println("       <title>Data Types in the common namespace.</title>");
//		prDocBook.println("       <para>These classes define the data types. </para>");
					
//		Sort the data types			
		TreeMap <String, DOMClass> sortDataTypeMap = new TreeMap <String, DOMClass> ();
		for (Iterator<DOMClass> i = DOMInfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
			DOMClass lClass = (DOMClass) i.next();
			if (lClass.isInactive) continue;
			if (lNameSpaceIdNC.compareTo(lClass.nameSpaceIdNC) != 0) continue;
			if (!lClass.isDataType) continue;
			sortDataTypeMap.put(lClass.title, lClass);
		}	
		ArrayList <DOMClass> sortDataTypeArr = new ArrayList <DOMClass> (sortDataTypeMap.values());	
		if (sortDataTypeArr.size() <= 0) {
	        prDocBook.println("");
	        prDocBook.println("      <!-- ===================== Part4a End=========================== -->");
			return;
		}
		
		prDocBook.println("    <chapter>");
		prDocBook.println("       <title>Data Types for the dictionary.</title>");
		prDocBook.println("       <para>These classes define the data types. </para>");
	
//		Write the data types
		String lSchemaBaseType = "None", lMinChar = "None", lMaxChar = "None", lMinVal = "None", lMaxVal = "None";
		for (Iterator<DOMClass> i = sortDataTypeArr.iterator(); i.hasNext();) {
			DOMClass lClass = (DOMClass) i.next();
			
			lSchemaBaseType = "None"; lMinChar = "None"; lMaxChar = "None"; lMinVal = "None"; lMaxVal = "None";
			lPatternArr = new ArrayList<String> ();

			for (Iterator<DOMProp> j = lClass.ownedAttrArr.iterator(); j.hasNext();) {
				DOMProp lProp = j.next();
				DOMAttr lAttr = (DOMAttr) lProp.hasDOMObject;
				if (lAttr.title.compareTo("xml_schema_base_type") == 0) {
					lSchemaBaseType = DOMInfoModel.getSingletonAttrValue(lAttr.valArr);
					if (lSchemaBaseType == null) lSchemaBaseType = "None";
					lSchemaBaseType = DMDocument.replaceString (lSchemaBaseType, "xsd:", "");
				}

				if (lAttr.title.compareTo("maximum_characters") == 0) {
					lMaxChar = DOMInfoModel.getSingletonAttrValue(lAttr.valArr);
					if ((lMaxChar == null || lMaxChar.compareTo("2147483647") == 0)) lMaxChar = "None";
				}
				
				if (lAttr.title.compareTo("minimum_characters") == 0) {
					lMinChar = DOMInfoModel.getSingletonAttrValue(lAttr.valArr);
					if ((lMinChar == null || lMinChar.compareTo("-2147483648") == 0)) lMinChar = "None";

				}
				if (lAttr.title.compareTo("maximum_value") == 0) {
					lMaxVal = DOMInfoModel.getSingletonAttrValue(lAttr.valArr);
					if ((lMaxVal == null || lMaxVal.compareTo("2147483647") == 0)) lMaxVal = "None";

				}
				if (lAttr.title.compareTo("minimum_value") == 0) {
					lMinVal = DOMInfoModel.getSingletonAttrValue(lAttr.valArr);
					if ((lMinVal == null || lMinVal.compareTo("-2147483648") == 0)) lMinVal = "None";

				}
				if (lProp.title.compareTo("pattern") == 0) {
					String lVal = DOMInfoModel.getSingletonAttrValue(lAttr.valArr);
					if (lVal != null) {
						// if not null there there are one or more patterns
						for (Iterator <String> k = lAttr.valArr.iterator(); k.hasNext();) {
							String lPattern = (String) k.next();
							lPatternArr.add(lPattern);
						}
					}
				}
			}
					
            prDocBook.println("<sect1>");
            prDocBook.println("    <title>" + getClassAnchor(lClass) + getValue(lClass.title) + "</title>");
            prDocBook.println("");
            prDocBook.println("<para>");
            prDocBook.println("    <informaltable frame=\"all\" colsep=\"1\">");
            prDocBook.println("        <tgroup cols=\"4\" align=\"left\" colsep=\"1\" rowsep=\"1\">");
            prDocBook.println("            <colspec colnum=\"1\" colname=\"c1\" colwidth=\"1.0*\"/>");
            prDocBook.println("            <colspec colnum=\"2\" colname=\"c2\" colwidth=\"1.0*\"/>");
            prDocBook.println("            <colspec colnum=\"3\" colname=\"c3\" colwidth=\"1.0*\"/>");
            prDocBook.println("            <colspec colnum=\"4\" colname=\"c4\" colwidth=\"1.0*\"/>");
            prDocBook.println("            <thead>");
            prDocBook.println("                <row>");
            prDocBook.println("                    <entry namest=\"c1\" nameend=\"c3\" align=\"left\">" + getPrompt("Name: ") + getValue(lClass.title) + "</entry>");
//            prDocBook.println("                    <entry>" + getPrompt("Version Id: ") + getValue("1.0.0.0") + "</entry>");
            prDocBook.println("                    <entry>" + getPrompt("Version Id: ") + getValue(lClass.versionId) + "</entry>");
            prDocBook.println("                </row>");
            prDocBook.println("            </thead>");
            prDocBook.println("            <tbody>");
            prDocBook.println("                <row>");
            prDocBook.println("                    <entry namest=\"c1\" nameend=\"c4\" align=\"left\">" + getPrompt("Description ") + getValue(lClass.definition) + "</entry>");
            prDocBook.println("                </row>");
            prDocBook.println("                <row>");
            prDocBook.println("                    <entry>" + getPrompt("Schema Base Type:  ") + getValueReplaceTBDWithNone(lSchemaBaseType) + "</entry>");
            prDocBook.println("                    <entry>" + getPrompt("") + "</entry>");
            prDocBook.println("                    <entry>" + getPrompt("") + "</entry>");
            prDocBook.println("                    <entry>" + getPrompt("") + "</entry>");
            prDocBook.println("                </row>");
            prDocBook.println("                <row>");
            prDocBook.println("                    <entry>" + getPrompt("Minimum Value: ") + getValueReplaceTBDWithNone(lMinVal) + "</entry>");
            prDocBook.println("                    <entry>" + getPrompt("Maximum Value: ") + getValueReplaceTBDWithNone(lMaxVal) + "</entry>");
            prDocBook.println("                    <entry>" + getPrompt("Minimum Characters: ") + getValueReplaceTBDWithNone(lMinChar) + "</entry>");
            prDocBook.println("                    <entry>" + getPrompt("Maximum Characters: ") + getValueReplaceTBDWithNone(lMaxChar) + "</entry>");
            prDocBook.println("                </row>");

            if (lPatternArr == null || lPatternArr.isEmpty()) {
                prDocBook.println("                <row>");
                prDocBook.println("                    <entry>" + "</entry>");
                prDocBook.println("                    <entry namest=\"c2\" nameend=\"c4\" align=\"left\">" + getPrompt("No Pattern") + "</entry>");
	            prDocBook.println("                </row>");
            } else {
                prDocBook.println("                <row>");
                prDocBook.println("                    <entry>" + "</entry>");
            	prDocBook.println("                    <entry namest=\"c2\" nameend=\"c4\" align=\"left\">" + getPrompt("Pattern") + "</entry>");           	
	            prDocBook.println("                </row>");
    			for (Iterator <String> k = lPatternArr.iterator(); k.hasNext();) {
    				String lPattern = (String) k.next();
    	            prDocBook.println("                <row>");
                    prDocBook.println("                    <entry>" + "</entry>");
    	            prDocBook.println("                    <entry namest=\"c2\" nameend=\"c4\" align=\"left\">" + getValue(lPattern) + "</entry>");
    	            prDocBook.println("                </row>");
    			}
            }
			
            prDocBook.println("");
            prDocBook.println("            </tbody>");
            prDocBook.println("        </tgroup>");
            prDocBook.println("        </informaltable>");
            prDocBook.println("</para>");
          	prDocBook.println("</sect1> ");
		}
		prDocBook.println("    </chapter>");		
        prDocBook.println("");
        prDocBook.println("      <!-- ===================== Part4a End=========================== -->");
        prDocBook.println("");
	}
	
	private void writeUnitsSection (String lNameSpaceIdNC, PrintWriter prDocBook) {
		ArrayList <String> lPatternArr = new ArrayList<String> ();
        prDocBook.println("");
        prDocBook.println("      <!-- =====================Part4b Begin=========================== -->");
        prDocBook.println("");
//		prDocBook.println("    <chapter>");
//		prDocBook.println("       <title>Units of Measure in the common namespace.</title>");
//		prDocBook.println("       <para>These classes define the units of measure. </para>");
		
		// get the units
		ArrayList <DOMProp> lDOMPermValueArr;
		TreeMap <String, DOMClass> sortUnitsMap = new TreeMap <String, DOMClass> ();
		for (Iterator<DOMClass> i = DOMInfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
			DOMClass lClass = (DOMClass) i.next();
			if (lClass.isInactive) continue;
			if (lNameSpaceIdNC.compareTo(lClass.nameSpaceIdNC) != 0) continue;
			if (! lClass.isUnitOfMeasure) continue;
			sortUnitsMap.put(lClass.title, lClass);
		}	
		ArrayList <DOMClass> sortUnitsArr = new ArrayList <DOMClass> (sortUnitsMap.values());	
		if (sortUnitsArr.size() <= 0) {
	        prDocBook.println("      <!-- ===================== Part4b End=========================== -->");
			return;
		}
		
		prDocBook.println("    <chapter>");
		prDocBook.println("       <title>Units of Measure in the common namespace.</title>");
		prDocBook.println("       <para>These classes define the units of measure. </para>");

		for (Iterator<DOMClass> i = sortUnitsArr.iterator(); i.hasNext();) {
			DOMClass lClass = (DOMClass) i.next();
			lDOMPermValueArr = new ArrayList <DOMProp> ();
			for (Iterator<DOMProp> j = lClass.allAttrAssocArr.iterator(); j.hasNext();) {
				DOMProp lProp = j.next();
				DOMAttr lDOMAttr = (DOMAttr) lProp.hasDOMObject;
				if (lDOMAttr.title.compareTo("unit_id") == 0) {
					if (lDOMAttr.domPermValueArr != null) lDOMPermValueArr = lDOMAttr.domPermValueArr;
				}
			}
					
            prDocBook.println("<sect1>");
            prDocBook.println("    <title>" + getClassAnchor(lClass) + getValue(lClass.title) + "</title>");
            prDocBook.println("");
            prDocBook.println("<para>");
            prDocBook.println("    <informaltable frame=\"all\" colsep=\"1\">");
            prDocBook.println("        <tgroup cols=\"4\" align=\"left\" colsep=\"1\" rowsep=\"1\">");
            prDocBook.println("            <colspec colnum=\"1\" colname=\"c1\" colwidth=\"1.0*\"/>");
            prDocBook.println("            <colspec colnum=\"2\" colname=\"c2\" colwidth=\"1.0*\"/>");
            prDocBook.println("            <colspec colnum=\"3\" colname=\"c3\" colwidth=\"1.0*\"/>");
            prDocBook.println("            <colspec colnum=\"4\" colname=\"c4\" colwidth=\"1.0*\"/>");
            prDocBook.println("            <thead>");
            prDocBook.println("                <row>");
            prDocBook.println("                    <entry namest=\"c1\" nameend=\"c3\" align=\"left\">" + getPrompt("Name:  ") + getValue(lClass.title) + "</entry>");
//            prDocBook.println("                    <entry>" + getPrompt("Version Id:  ") + getValue("1.0.0.0") + "</entry>");
            prDocBook.println("                    <entry>" + getPrompt("Version Id:  ") + getValue(lClass.versionId) + "</entry>");
            prDocBook.println("                </row>");
            prDocBook.println("            </thead>");
            prDocBook.println("            <tbody>");
            prDocBook.println("                <row>");
            prDocBook.println("                    <entry namest=\"c1\" nameend=\"c4\" align=\"left\">" + getPrompt("Description: ") + getValue(lClass.definition) + "</entry>");
            prDocBook.println("                </row>");
            prDocBook.println("                <row>");
            prDocBook.println("                    <entry>" + "</entry>");
            prDocBook.println("                    <entry namest=\"c2\" nameend=\"c4\" align=\"left\">" + getPrompt("Unit Id") + "</entry>");
            prDocBook.println("                </row>");
            
            if (! lDOMPermValueArr.isEmpty()) {
    			for (Iterator <DOMProp> k = lDOMPermValueArr.iterator(); k.hasNext();) {
    				DOMProp lDOMProp = (DOMProp) k.next();
    				if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMPermValDefn) {
    					DOMPermValDefn lDOMPermValDefn = (DOMPermValDefn) lDOMProp.hasDOMObject;
    		            prDocBook.println("                <row>");
    		            prDocBook.println("                    <entry>" + "</entry>");
    		            prDocBook.println("                    <entry>" + getValue(lDOMPermValDefn.value) + "</entry>");
    		            String lValueMeaning = lDOMPermValDefn.value_meaning;
    		            if (lValueMeaning == null) lValueMeaning = "TBD_value_meaning";
    		            prDocBook.println("                    <entry namest=\"c3\" nameend=\"c4\" align=\"left\">" + getValue(lValueMeaning) + "</entry>");
    		            prDocBook.println("                </row>");
    				}
    			}
            } else {
	            prDocBook.println("                <row>");
	            prDocBook.println("                    <entry>" + "</entry>");
	            prDocBook.println("                    <entry>" + "None" + "</entry>");
	            prDocBook.println("                    <entry namest=\"c3\" nameend=\"c4\" align=\"left\">" + getValue("") + "</entry>");
	            prDocBook.println("                </row>");
            }
			
            prDocBook.println("");
            prDocBook.println("            </tbody>");
            prDocBook.println("        </tgroup>");
            prDocBook.println("        </informaltable>");
            prDocBook.println("</para>");
          	prDocBook.println("</sect1> ");
		}
		
		// finalize Part 4
		prDocBook.println("         </chapter>");
        prDocBook.println("");
        prDocBook.println("      <!-- ===================== Part4b End=========================== -->");
        prDocBook.println("");
	}
	
	private void writeHeader (SchemaFileDefn lSchemaFileDefn, PrintWriter prDocBook) {
		
		prDocBook.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		prDocBook.println("<?xml-model href=\"http://www.oasis-open.org/docbook/xml/5.0/rng/docbook.rng\" schematypens=\"http://relaxng.org/ns/structure/1.0\"?>");
		prDocBook.println("<book xmlns=\"http://docbook.org/ns/docbook\"");
		prDocBook.println("    xmlns:xlink=\"http://www.w3.org/1999/xlink\" version=\"5.0\">");
		prDocBook.println("    <info>");
		prDocBook.println("        <title>" + DMDocument.ddDocTitle + " - " + lSchemaFileDefn.lddName + "</title>");
//		prDocBook.println("        <subtitle>Abridged - Version " + DMDocument.masterPDSSchemaFileDefn.ont_version_id + "</subtitle>");
		prDocBook.println("        <subtitle>Version " + DMDocument.masterPDSSchemaFileDefn.ont_version_id + "</subtitle>");
//		prDocBook.println("        <subtitle>Data Dictionary: " + lSchemaFileDefn.lddName + "</subtitle>");
//		prDocBook.println("        <author>");
//		prDocBook.println("            <orgname>" + DMDocument.ddDocTeam + "</orgname>");
//		prDocBook.println("        </author>");
//		prDocBook.println("        <releaseinfo>Information Model Version " + DMDocument.masterPDSSchemaFileDefn.ont_version_id + " on " + DMDocument.sTodaysDate + "</releaseinfo>");
		prDocBook.println("        <releaseinfo>" + DMDocument.sTodaysDate + "</releaseinfo>");
//		prDocBook.println("        <releaseinfo>Data Dictionary: " + lSchemaFileDefn.lddName + "</releaseinfo>");
		prDocBook.println("        <date>" + DMDocument.sTodaysDate + "</date>");
		prDocBook.println("    </info>");
		prDocBook.println("        ");
		prDocBook.println("        <chapter>");
		prDocBook.println("            <title>Introduction</title>");
// 333	prDocBook.println("            <para>The Data Dictionary defines the organization and components of product labels. Components of a product label include classes and their attributes.</para>");
		prDocBook.println("            <para>This is the Enterprise 2.0 Data Dictionary for " + lSchemaFileDefn.lddName + ".</para>");
		prDocBook.println("            <para>");
		prDocBook.println("            </para>");
		prDocBook.println("            <sect1>");
		prDocBook.println("                <title>Audience</title>");
		prDocBook.println("                <para>This Data Dictionary is designed to be used by anyone who needs an enterprise-level or domain-level controlled vocabulary.</para>");
		prDocBook.println("                <para>");
		prDocBook.println("                </para>");
		prDocBook.println("            </sect1>");
		prDocBook.println("            <sect1>");
		prDocBook.println("                <title>Acknowledgements</title>");
		prDocBook.println("                <para>This Data Dictionary is a joint effort involving " + lSchemaFileDefn.lddName + " experts.</para>");
		prDocBook.println("                <para>");
		prDocBook.println("                </para>");
		prDocBook.println("            </sect1>");
		prDocBook.println("            <sect1>");
		prDocBook.println("                <title>Scope</title>");
		prDocBook.println("                <para>This Data Dictionary defines classes and attributes used in the " + lSchemaFileDefn.lddName + " domain and are proposed for use where interoperability based on common terms and definitions are desired.</para>");
		prDocBook.println("                <para>");
		prDocBook.println("                </para>");
		prDocBook.println("            </sect1>");
		prDocBook.println("            <sect1>");
		prDocBook.println("                <title>Applicable Documents</title>");
		prDocBook.println("                <para>");
		prDocBook.println("                    <emphasis role=\"bold\">Controlling Documents</emphasis>");
		prDocBook.println("");
		prDocBook.println("                    <itemizedlist>");
		prDocBook.println("                        <listitem>");
		prDocBook.println("                            <para>");
		prDocBook.println("                                ISO/IEC 11179:3 Registry Metamodel and Basic Attributes Specification, 2003. - The ISO/IEC 11179 specification provides the schema for the data dictionary.");
		prDocBook.println("                            </para>");
		prDocBook.println("                        </listitem>");
		prDocBook.println("                    </itemizedlist>");
		if (! DMDocument.importJSONAttrFlag) {
			prDocBook.println("                    <emphasis role=\"bold\">Reference Documents</emphasis>");
			prDocBook.println("                    <itemizedlist>");
			prDocBook.println("                        <listitem>");
			prDocBook.println("                            <para>");
			prDocBook.println("                                Domain Source Documents - This data dictionary was derived from source documents in the domain.");
			prDocBook.println("                            </para>");
			prDocBook.println("                        </listitem>");
			prDocBook.println("                        ");
			prDocBook.println("                    </itemizedlist>");
		}
		prDocBook.println("                </para>");
		prDocBook.println("            </sect1>");
		prDocBook.println("            <sect1>");
		prDocBook.println("                <title>Terminology</title>");
		prDocBook.println("                <para>This document uses very specific engineering terminology to describe the various structures involved.  It is particularly important that readers who have absorbed the Standards Reference bear in mind that terms which are familiar in that context can have very different meanings in the present document. </para>");
		prDocBook.println("                <para>Following are some definitions of essential terms used throughout this document.</para>");
		prDocBook.println("                <itemizedlist>");

		
		prDocBook.println("                    <listitem>");
		prDocBook.println("                        <para><emphasis role=\"italic\">Artifact</emphasis> - An object made by a human being.</para>");
		prDocBook.println("                    </listitem>");

		prDocBook.println("                    <listitem>");
		prDocBook.println("                        <para><emphasis role=\"italic\">Conceptual Object</emphasis> - Something which is neither digital or physical, but conceptual in nature such as a project.</para>");
		prDocBook.println("                    </listitem>");

		prDocBook.println("                    <listitem>");
		prDocBook.println("                        <para><emphasis role=\"italic\">Conceptual Data Object</emphasis> - A digital object that represents a concept.</para>");
		prDocBook.println("                    </listitem>");

		prDocBook.println("                    <listitem>");
		prDocBook.println("                        <para><emphasis role=\"italic\">Data</emphasis> - A representation of information in a formalized manner suitable for communication, interpretation, or processing. Examples of data include a sequence of bits, a table of numbers, the characters on a page, the recording of sounds, or a descriptive attributes of a moon rock specimen.</para>");
		prDocBook.println("                    </listitem>");

		prDocBook.println("                    <listitem>");
		prDocBook.println("                        <para><emphasis role=\"italic\">Data Artifact</emphasis> - A data object produced within, or generated from, a digital ecosystem.</para>");
		prDocBook.println("                    </listitem>");

		prDocBook.println("                    <listitem>");
		prDocBook.println("                        <para><emphasis role=\"italic\">Data Object</emphasis> - A data object is either digital, physical, or conceptual. Examples of digital data objects include a time series or image. Examples of physical data objects are descriptions of such things as spacecraft, instruments, and facilities. Conceptual data objects include description of such things as projects and organizations.</para>");
		prDocBook.println("                    </listitem>");

		prDocBook.println("                    <listitem>");
		prDocBook.println("                        <para><emphasis role=\"italic\">Data Repository</emphasis> - A digital store of managed JPL data artifacts that provides reliable, long-term access, now and in the future.</para>");
		prDocBook.println("                    </listitem>");

		prDocBook.println("                    <listitem>");
		prDocBook.println("                        <para><emphasis role=\"italic\">Digital Object</emphasis> - A sequence of digital bits, 0's and 1's.</para>");
		prDocBook.println("                    </listitem>");

		prDocBook.println("                    <listitem>");
		prDocBook.println("                        <para><emphasis role=\"italic\">Digital Data Object</emphasis> - A digital object along with its representation metadata.</para>");
		prDocBook.println("                    </listitem>");

		prDocBook.println("                    <listitem>");
		prDocBook.println("                        <para><emphasis role=\"italic\">Gold Source</emphasis> - Used interchangeably with “official” in some cases, a gold source is the authoritative system or repository that contains data or information maintained by an organization responsible for its integrity. The management of gold sources follows the Controlled Content requirements of Document and Data Control (JPL Rules! DocID 57034). For example, Hardware Build Books (HBBs) may contain convenience copies of Inspection Reports (IRs), but the gold source for IRs is the Quality Assurance Reporting System (QARS). While most gold sources are now digitized, it is important to know that some hardcopy gold sources remain (e.g., legacy drawings).</para>");
		prDocBook.println("                    </listitem>");

		prDocBook.println("                    <listitem>");
		prDocBook.println("                        <para><emphasis role=\"italic\">JPL Data Artifact</emphasis> - A data artifact determined to be of value to JPL to preserve, share, and use both internally and externally to the organization. Formal definition can be found here on JPL Rules!</para>");
		prDocBook.println("                    </listitem>");

		prDocBook.println("                    <listitem>");
		prDocBook.println("                        <para><emphasis role=\"italic\">Knowledge</emphasis> - Understanding gained through experience or association. It is often characterized as propositional (know-that), competence (know-how), or acquaintance (know-about) , with relative levels of expertise determined by the depth of understanding of the subject matter, breadth of undertanding of that subject matter in relationship to other and larger domains, and skillfulness in applying it. Knowledge also is often categorized as tacit (hard to express because it is embedded in what we do) and explicit (more readily formalized and codified).</para>");
		prDocBook.println("                    </listitem>");

		prDocBook.println("                    <listitem>");
		prDocBook.println("                        <para><emphasis role=\"italic\">Metadata</emphasis> - A digital object (data) that describes a digital data object.</para>");
		prDocBook.println("                    </listitem>");

		prDocBook.println("                    <listitem>");
		prDocBook.println("                        <para><emphasis role=\"italic\">Physical Object</emphasis> - Something that physically exists that can be described such as a spacecraft or building</para>");
		prDocBook.println("                    </listitem>");

		prDocBook.println("                    <listitem>");
		prDocBook.println("                        <para><emphasis role=\"italic\">Physical Data Object</emphasis> - A digital object that represents a physical Thing.</para>");
		prDocBook.println("                    </listitem>");

		prDocBook.println("                    <listitem>");
		prDocBook.println("                        <para><emphasis role=\"italic\">Trusted Data Repository</emphasis> - A data repository that meets the CoreTrustSeal Certification requirements.</para>");
		prDocBook.println("                    </listitem>");

		prDocBook.println("                </itemizedlist>");
		prDocBook.println("");
		prDocBook.println("                <para>");
		prDocBook.println("                </para>");
		prDocBook.println("            </sect1>");
		prDocBook.println("        </chapter>");
//		prDocBook.println("    </part>");
		prDocBook.println("");
		return;
	}
	
	private void writeFooter (PrintWriter prDocBook) {
		prDocBook.println("");
		prDocBook.println("	</book>");
	}

	private String getPrompt (String lPrompt) {
		return "<emphasis>" + lPrompt + "</emphasis>";
	}
	
	private String getValue (String lValue) {
		return DOMInfoModel.escapeXMLChar(lValue);
	}
	
	private String getValueBreak (String lValue) {
		String lValueBreak = DMDocument.replaceString(lValue, "_", "_&#x200B;");
		return lValueBreak;
	}
	
	private String getValueReplaceTBDWithNone(String lValue) {
		if (lValue.indexOf("TBD") == 0) return "None";
		return DOMInfoModel.escapeXMLChar(lValue);
	}
			
	private String getValueAnchor(DOMAttr lAttr, String lValue) {
		String lAnchor = DMDocument.registrationAuthorityIdentifierValue + "." + lAttr.classNameSpaceIdNC + "." + lAttr.attrParentClass.title + "." + lAttr.nameSpaceIdNC + "." + lAttr.title + "." + lValue;
		int lAnchorI = lAnchor.hashCode();
		lAnchor = "N" + Integer.toString(lAnchorI);
		return "<anchor xml:id=\"" + lAnchor + "\"/>";
	}
	
	private String getValueLink(DOMAttr lAttr, String lValue) {
		String lAttrParentClassTitle = "TBD_lAttrParentClassTitle";
		if (lAttr.attrParentClass != null) 
			lAttrParentClassTitle = lAttr.attrParentClass.title;
		String lLink = DMDocument.registrationAuthorityIdentifierValue + "." + lAttr.classNameSpaceIdNC + "." + lAttrParentClassTitle + "." + lAttr.nameSpaceIdNC + "." + lAttr.title + "." + lValue;
		int lLinkI = lLink.hashCode();
		lLink = "N" + Integer.toString(lLinkI);
		return "<link linkend=\"" + lLink + "\">" + getValueBreak(lValue) + "</link>";
	}
	
	private String getAttrAnchor(DOMAttr lAttr) {
		String lAnchor = DMDocument.registrationAuthorityIdentifierValue + "." + lAttr.classNameSpaceIdNC + "." + lAttr.attrParentClass.title + "." + lAttr.nameSpaceIdNC + "." + lAttr.title;
		int lAnchorI = lAnchor.hashCode();
		lAnchor = "N" + Integer.toString(lAnchorI);
		return "<anchor xml:id=\"" + lAnchor + "\"/>";
	}

	private String getAttrLink(DOMAttr lAttr) {
		String lAttrParentClassTitle = "TBD_lAttrParentClassTitle";
		if (lAttr.attrParentClass != null) {
			lAttrParentClassTitle = lAttr.attrParentClass.title;
		}
		String lLink = DMDocument.registrationAuthorityIdentifierValue + "." + lAttr.classNameSpaceIdNC + "." + lAttrParentClassTitle + "." + lAttr.nameSpaceIdNC + "." + lAttr.title;
		int lLinkI = lLink.hashCode();
		lLink = "N" + Integer.toString(lLinkI);
		String lRegistrationStatusInsert = "";
		if (lAttr.registrationStatus.compareTo("Retired") == 0) lRegistrationStatusInsert = " " + DMDocument.Literal_DEPRECATED;
		return "<link linkend=\"" + lLink + "\">" + getValueBreak(lAttr.title) + lRegistrationStatusInsert + "</link>";
	}
	
	private String getClassAnchor(DOMClass lClass) {
		String lAnchor = DMDocument.registrationAuthorityIdentifierValue + "." + lClass.nameSpaceIdNC + "." + lClass.title;
		int lAnchorI = lAnchor.hashCode();
		lAnchor = "N" + Integer.toString(lAnchorI);
		return "<anchor xml:id=\"" + lAnchor + "\"/>";
	}
	
	private String getClassLink(DOMClass lClass) {
		String lLink = DMDocument.registrationAuthorityIdentifierValue + "." + lClass.nameSpaceIdNC + "." + lClass.title;
		int lLinkI = lLink.hashCode();
		lLink = "N" + Integer.toString(lLinkI);
		String lRegistrationStatusInsert = "";
		if (lClass.registrationStatus.compareTo("Retired") == 0) lRegistrationStatusInsert = " " +  DMDocument.Literal_DEPRECATED;
		return "<link linkend=\"" + lLink + "\">" + getValueBreak(lClass.title) + lRegistrationStatusInsert + "</link>";
	}
	
	private String getDataTypeLink(String lDataType) {
		String lLink = DMDocument.registrationAuthorityIdentifierValue + "." + DMDocument.masterNameSpaceIdNCLC + "." + lDataType;
		int lLinkI = lLink.hashCode();
		lLink = "N" + Integer.toString(lLinkI);
//		String lDataTypeWrap = DMDocument.replaceString(lDataType, "_", "_&#x200B;");
		return "<link linkend=\"" + lLink + "\">" + getValueBreak(lDataType) + "</link>";
	}
	
	private String getUnitIdLink(String lUnitId) {
		if (lUnitId.indexOf("TBD") == 0) return "None";
		String lLink = DMDocument.registrationAuthorityIdentifierValue + "." + DMDocument.masterNameSpaceIdNCLC + "." + lUnitId;
		int lLinkI = lLink.hashCode();
		lLink = "N" + Integer.toString(lLinkI);
		return "<link linkend=\"" + lLink + "\">" + lUnitId + "</link>";
	}
	
	private String getCardinality(int lCardMin, int lCardMax) {
		String pCardMax = "Unbounded";
		if (lCardMax != 9999999) pCardMax = (new Integer(lCardMax)).toString();
		String pCardMin = (new Integer(lCardMin)).toString();
		return pCardMin + ".." + pCardMax;
	}

//	return all classes that reference this class 
	private ArrayList <DOMClass> getClassReferences (DOMClass lTargetClass) {
		ArrayList <DOMClass> refClassArr = new ArrayList <DOMClass> ();
		for (Iterator <DOMClass> i = DOMInfoModel.masterDOMClassArr.iterator(); i.hasNext();) {
			DOMClass lClass = (DOMClass) i.next();
			if (lClass.isInactive) continue;
			if (lClass.title.compareTo(DMDocument.TopLevelAttrClassName) != 0) {
				for (Iterator <DOMProp> j = lClass.ownedAssocArr.iterator(); j.hasNext();) {
					DOMProp lProp = j.next();
					DOMClass lCompClass = (DOMClass) lProp.hasDOMObject;
				    
					if (lTargetClass == lCompClass) {
					   if (! refClassArr.contains(lClass)) {
						  refClassArr.add(lClass);
					   }
					}
				}					
		        for (Iterator <DOMProp> j = lClass.inheritedAssocArr.iterator(); j.hasNext();) {
		        	DOMProp lProp = j.next();
					DOMClass lCompClass = (DOMClass) lProp.hasDOMObject;
				  
					if (lTargetClass == lCompClass) {
						if (! refClassArr.contains(lClass)) {
									refClassArr.add(lClass);
						}
					}
				}
			}
		}
			
		
		return refClassArr;
	}	
	
	/**
	* escape certain characters for DocBook
	*/
	 
	 // locally defined classes for classifying classes and attributes
	 
	public class ClassClassificationDefnDOM {
		String identifier;
		String namespaceId;
		ArrayList <DOMClass> classArr;
		TreeMap <String, DOMClass> classMap;
		
		public ClassClassificationDefnDOM (String id) {
			identifier = id; 
			namespaceId = id;
			classArr = new ArrayList <DOMClass> ();
			classMap = new TreeMap <String, DOMClass> ();
		} 
	}
	 
	public class AttrClassificationDefnDOM {
		String identifier;
		String namespaceId;
		ArrayList <DOMAttr> attrArr;
		TreeMap <String, DOMAttr> attrMap;
		
		public AttrClassificationDefnDOM (String id) {
			identifier = id; 
			namespaceId = id;
			attrArr = new ArrayList <DOMAttr> ();
	        attrMap = new TreeMap <String, DOMAttr> ();
		} 
	}
}
