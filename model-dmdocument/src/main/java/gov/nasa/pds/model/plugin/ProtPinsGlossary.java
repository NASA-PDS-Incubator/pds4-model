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
import java.util.*;

class ProtPinsGlossary extends Object{
	TreeMap <String, AttrDefn> glossMap;
	TreeMap <String, String> glossTitleIdMap;
	
	ProtPins protPinsInst;   
	
	public ProtPinsGlossary () {
		glossMap = new TreeMap <String, AttrDefn> ();
		glossTitleIdMap = new TreeMap <String, String> ();
		return;
	}
	
	public void getProtPinsGlossary (String subModelId, String fname) throws Throwable {

		protPinsInst = new ProtPins();
		protPinsInst.getProtInst("PDS3", "pds3", fname);
		
		HashMap <String, InstDefn> tDict = protPinsInst.instDict;
		Set <String> set1 = tDict.keySet();
		Iterator <String> iter1 = set1.iterator();
		while(iter1.hasNext()) {
			String instRDFId = (String) iter1.next();		
			InstDefn localInst = (InstDefn) tDict.get(instRDFId);
			AttrDefn attrClass = new AttrDefn(localInst.rdfIdentifier);
			attrClass.regAuthId = DMDocument.registrationAuthorityIdentifierValue;
			attrClass.subModelId = subModelId;
			attrClass.title = InfoModel.unEscapeProtegeString(localInst.title);
			attrClass.genAttrMap = localInst.genSlotMap;
			ArrayList attrdescarr = (ArrayList) attrClass.genAttrMap.get("column_desc");
			String lDescription = (String) attrdescarr.get(0);
			lDescription =  InfoModel.unEscapeProtegeString(lDescription);
			attrClass.description = lDescription;
			
			glossMap.put(attrClass.rdfIdentifier, attrClass);
			glossTitleIdMap.put(attrClass.title, attrClass.rdfIdentifier);
		}
		return;
	}
}
