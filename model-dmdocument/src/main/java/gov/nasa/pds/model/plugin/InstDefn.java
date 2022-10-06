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
import java.util.HashMap;

public class InstDefn extends Object {

  // note that the master11179DataDict Map key = className + "." title
  String rdfIdentifier; // e.g.
                        // dd79.DE.0001_NASA_PDS_1.rings.Stellar_Occultation.radial_sampling_interval
  String identifier; // e.g.
                     // dd79.DE.0001_NASA_PDS_1.rings.Stellar_Occultation.radial_sampling_interval
  String title; // e.g. DE.0001_NASA_PDS_1.rings.Stellar_Occultation.radial_sampling_interval
  String className; // ISO 11179 Class Name, e.g. DataElement
  String steward;
  String nameSpaceId;
  String nameSpaceIdNC;
  String description;

  HashMap<String, ArrayList<String>> genSlotMap;

  public InstDefn(String lRDFIdentifier) {
    rdfIdentifier = lRDFIdentifier;
    identifier = "TBD_identifier";
    title = "TBD_title";
    className = "TBD_className";
    steward = "TBD_steward";
    nameSpaceId = "TBD_nameSpaceId";
    nameSpaceIdNC = "TBD_nameSpaceIdNC";
    description = "TBD_description";

    genSlotMap = new HashMap<>();
  }

  // get a singleton slot value given a keyName (Protege dd11179 attribute)
  public String getSlotValueSingleton(String keyName) {
    ArrayList<String> lValArr = getSlotValueArr(keyName);
    if (lValArr == null) {
      return null;
    }
    String lValue = lValArr.get(0);
    if (lValue == null) {
      return null;
    }
    return lValue;
  }

  // get the slot value array given a keyName (Protege dd11179 attribute)
  public ArrayList<String> getSlotValueArr(String keyName) {
    ArrayList<String> lValArr = genSlotMap.get(keyName);
    return lValArr;
  }
}
