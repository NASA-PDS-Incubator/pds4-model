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

public class DeprecatedDefn {
  String identifier;
  String title;
  String classNameSpaceIdNC;
  String className;
  String attrNameSpaceIdNC;
  String attrName;
  String value;
  String context;
  boolean isAttribute;
  boolean isValue;
  boolean isUnitId;

  public DeprecatedDefn(String lTitle, String lClassNameSpaceIdNC, String lClassName,
      String lAttrNameSpaceIdNC, String lAttrName, String lValue, boolean lIsUnitId) {
    title = lTitle;
    classNameSpaceIdNC = lClassNameSpaceIdNC;
    className = lClassName;
    attrNameSpaceIdNC = lAttrNameSpaceIdNC;
    attrName = lAttrName;
    value = lValue;
    isValue = false;
    isAttribute = false;
    isUnitId = lIsUnitId;
    if (value.compareTo("") != 0) {
      identifier = DOMInfoModel.getAttrIdentifier(lClassNameSpaceIdNC, lClassName,
          lAttrNameSpaceIdNC, lAttrName);
      isValue = true;
      isAttribute = true;
      context = classNameSpaceIdNC + ":" + lClassName;
    } else if (lAttrName.compareTo("") != 0) {
      identifier = DOMInfoModel.getAttrIdentifier(lClassNameSpaceIdNC, lClassName,
          lAttrNameSpaceIdNC, lAttrName);
      isAttribute = true;
      context = classNameSpaceIdNC + ":" + lClassName + "/" + lAttrNameSpaceIdNC + ":" + lAttrName;
    } else {
      identifier = DOMInfoModel.getClassIdentifier(lClassNameSpaceIdNC, lClassName);
      context = classNameSpaceIdNC + ":" + lClassName;
    }
  }
  
  String getDeprecatedDefnSerialized() {
	  String deprecatedDefnSerialized = "title:" + title + " |  classNameSpaceIdNC:"
          + classNameSpaceIdNC + " |  className:" + className
          + " |  attrNameSpaceIdNC:" + attrNameSpaceIdNC + " |  attrName:"
          + attrName + " |  value:" + value + " |  isValue:" + isValue
          + " |  isAttribute:" + isAttribute + " |  isUnitId:" + isUnitId;
	  return deprecatedDefnSerialized;
  }
}
