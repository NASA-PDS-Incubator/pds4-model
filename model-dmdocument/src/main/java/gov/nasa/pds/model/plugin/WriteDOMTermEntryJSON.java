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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


// Write the Terminological Entries to a file

class WriteDOMTermEntryJSON extends Object {
	
	// LDD name
	String lddName;
	String lddVersion;
	    
	public WriteDOMTermEntryJSON () {
		lddName = "TBD_lddName";
		lddVersion = "TBD_lddVersion";
		return;
	}
	
//	write terminological entries
	public void WriteDOMTermEntries (SchemaFileDefn lSchemaFileDefn) throws java.io.IOException {
//		String lFileName = lSchemaFileDefn.relativeFileSpecCCSDSCSV;	
		String lFileName = lSchemaFileDefn.relativeFileSpecDOMModelJSON;	
		lFileName = lFileName.replace(".JSON", "_TM.JSON");

		// get the term mappings
		TreeMap <String, TermEntryDefnGroup> TermEntryDefnGroupMap = getTermMappings ();
		
		// get the JSON object
		JSONObject jsonObjectRoot = getJSONObject (TermEntryDefnGroupMap);

		// write the JSON object
		writeJson(jsonObjectRoot, "C:\\AA7Ontologies\\A01PDS4\\Document\\LDDTool\\export\\csv\\PDS4_PDS_CCSDS_1I00_termmap.JSON");
		return;
	}
	
	// get the term mappings
	private TreeMap <String, TermEntryDefnGroup> getTermMappings () {
		// map of Term Entry Definitions index by class and attribute identifier
		TreeMap <String, TermEntryDefnGroup> termEntryDefnGroupMap = new TreeMap <String, TermEntryDefnGroup> ();

        // scan all the classes for terminological entries at the attribute level
		for (DOMClass lDOMClass : DOMInfoModel.masterDOMClassArr) {
			
			// skip inactive
			if (lDOMClass.isInactive) continue;
			
			// scan through all owned attributes of the class
			for (DOMProp lDOMProp : lDOMClass.ownedAttrArr) {
				if (lDOMProp.hasDOMObject != null && lDOMProp.hasDOMObject instanceof DOMAttr) {
					DOMAttr lDOMAttr = (DOMAttr) lDOMProp.hasDOMObject;
					
					// skip attribute with no terminological entry map
					if (lDOMAttr.termEntryMap == null || lDOMAttr.termEntryMap.isEmpty() ) continue;
					
//					System.out.println("\ndebug getTermMappings lDOMClass.identifier:" + lDOMClass.identifier);
//					System.out.println("debug getTermMappings lDOMAttr.identifier:" + lDOMAttr.identifier);
//					System.out.println("debug getTermMappings -- FOUND -- lDOMAttr.identifier:" + lDOMAttr.identifier);
					
					// found a terminological entry map; get the TermEntryDefns of the map as an array
					for (TermEntryDefn lTermEntryDefn : lDOMAttr.termEntryMap.values()) {
						lddName = lTermEntryDefn.lddName;
						lddVersion = lTermEntryDefn.lddVersion;
						String lKey = lDOMAttr.identifier + "_" + lTermEntryDefn.name + "_" + DOMInfoModel.getNextUId();
						
						TermEntryDefnGroup termEntryDefnGroup = new TermEntryDefnGroup ();
						termEntryDefnGroup.identifier = lKey;
						termEntryDefnGroup.attrId = lDOMAttr.nameSpaceId + lDOMAttr.title;
						termEntryDefnGroup.termEntryDefn = lTermEntryDefn;
						
/*						System.out.println("\ndebug getTermMappings termEntryDefnGroup.identifier:" + termEntryDefnGroup.identifier);
						System.out.println("debug getTermMappings termEntryDefnGroup.attrId:" + termEntryDefnGroup.attrId);
						System.out.println("debug getTermMappings termEntryDefnGroup.termEntryDefn.fromInstanceId:" + termEntryDefnGroup.termEntryDefn.fromInstanceId);
						System.out.println("debug getTermMappings termEntryDefnGroup.termEntryDefn.toInstanceId:" + termEntryDefnGroup.termEntryDefn.toInstanceId);
						System.out.println("debug getTermMappings termEntryDefnGroup.termEntryDefn.name:" + termEntryDefnGroup.termEntryDefn.name);
						System.out.println("debug getTermMappings termEntryDefnGroup.termEntryDefn.semanticRelation:" + termEntryDefnGroup.termEntryDefn.semanticRelation);
						System.out.println("debug getTermMappings termEntryDefnGroup.termEntryDefn.lddName:" + termEntryDefnGroup.termEntryDefn.lddName);
						System.out.println("debug getTermMappings termEntryDefnGroup.termEntryDefn.lddVersion:" + termEntryDefnGroup.termEntryDefn.lddVersion);
*/
						termEntryDefnGroupMap.put(lKey, termEntryDefnGroup);
					}
				}
			}
		}
//       return termEntryDefnMap;
       return termEntryDefnGroupMap;
	}
	
	// get the term mappings as a JSON object
	private JSONObject getJSONObject (TreeMap <String, TermEntryDefnGroup> termEntryDefnGroupMap) {
		// create the JSON object
		JSONObject jsonObjectRoot = new JSONObject ();
		jsonObjectRoot.put("datetime", DMDocument.masterTodaysDateTimeUTCwT);
		jsonObjectRoot.put("infoModelVersionId", DMDocument.infoModelVersionId);
		jsonObjectRoot.put("title", "PDS4 Term Mappings");
		jsonObjectRoot.put("lddName", lddName);
		jsonObjectRoot.put("lddVersion", lddVersion);

		// create array of one element {from, to} arrays
		JSONArray jsonTermMapElementArrArr = new JSONArray();
		
		// scan over the term entry definitions
		for (TermEntryDefnGroup termEntryDefnGroup : termEntryDefnGroupMap.values()) {
			
			// get the term map elements
					
			// create a one {from, to} array
			JSONArray jsonFromToArr = new JSONArray();

			// get the From (new) term
			JSONObject jsonFromObject = new JSONObject ();
			String fromInstanceId = getEscapedValue(termEntryDefnGroup.termEntryDefn.fromInstanceId);
			jsonFromObject.put("from", fromInstanceId);
			jsonFromToArr.add(jsonFromObject);
			
			// get the To (PDS4) term
			JSONObject jsonToObject = new JSONObject ();
			String toInstanceId = getEscapedValue(termEntryDefnGroup.termEntryDefn.toInstanceId);
			jsonToObject.put("to", toInstanceId);
			jsonFromToArr.add(jsonToObject);
			
			// get the SKOS semanticRelation
			JSONObject jsonSKOSObject = new JSONObject ();
			String semanticRelation = getEscapedValue(termEntryDefnGroup.termEntryDefn.semanticRelation);
			jsonSKOSObject.put("skos", semanticRelation);
			jsonFromToArr.add(jsonSKOSObject);
			
			// get the description of the mapping
			JSONObject jsonDefinitionObject = new JSONObject ();
			String definition = getEscapedValue(termEntryDefnGroup.termEntryDefn.definition);
			jsonDefinitionObject.put("defn", definition);
			jsonFromToArr.add(jsonDefinitionObject);
			
			// create one {from, to} enclosing object
			JSONObject jsonFromToObj = new JSONObject ();
			jsonFromToObj.put("termmap", jsonFromToArr);
			
			// add one {from, to} enclosing object 
			jsonTermMapElementArrArr.add(jsonFromToObj);
		}
		jsonObjectRoot.put("termmaps", jsonTermMapElementArrArr);
        return jsonObjectRoot;
	}

	
    public static void writeJson(JSONObject jsonObject, String file) {
    	// write the JSON Object
        try {
//            System.out.println(jsonObject);
            FileWriter jsonFileWriter = new FileWriter(file);
            jsonFileWriter.write(jsonObject.toJSONString());
            jsonFileWriter.flush();
            jsonFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	private String getEscapedValue (String lValue) {
		return DOMInfoModel.escapeXMLChar(lValue);
	}
	
	class TermEntryDefnGroup {
		String identifier;
		String attrId;
		TermEntryDefn termEntryDefn;
	}
}
