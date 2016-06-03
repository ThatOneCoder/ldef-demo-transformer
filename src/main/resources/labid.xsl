<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/">
	<xsl:call-template name="rows">
	</xsl:call-template>
</xsl:template>

<xsl:template name="rows">
<root>
	<xsl:for-each select="//rows/ADT_A03">


	BEGIN FILE POSITION: <xsl:value-of select="position()"/><xsl:text>&#xa;</xsl:text>


<ClinicalDocument xmlns="urn:hl7-org:v3" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">  
   <realmCode code="US"/>
  
   
   <typeId root="2.16.840.1.113883.1.3" extension="POCD_HD000040"/>
  <!-- Conformant to NHSN R4 Generic Constraints -->
  
   
   <templateId root="2.16.840.1.113883.10.20.5.4.20"/>
  <!-- Conformant to the NHSN Constraints for Lab-identified Organism Numerator Report -->
  
   
   <templateId root="2.16.840.1.113883.10.20.5.17"/>
  <!-- Document ID (extension) is scoped by vendor/software -->
  
   
   <!--  <id root="2.16.840.1.113883.3.117.1.1.5.2.1.2" extension="2.16.840.1.114222.4.1.9733"/> -->
  
    <id root="2.16.840.1.113883.3.117.1.1.5.2.1.2">
          <xsl:attribute name="extension">
          	<xsl:value-of select="MSH/MSH.10"/>
          </xsl:attribute>
    </id>
  
   
   <code codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" code="51897-7" displayName="Healthcare Associated Infection Report"/>
  
   
   <title>Lab-identified Organism (LIO) Report</title>
  
   
   <effectiveTime value="20150825"/>
  
   
   <confidentialityCode codeSystem="2.16.840.1.113883.5.25" code="N"/>
  
   
   <languageCode code="en-US"/>
  
   
	<setId extension="31" >
        <xsl:attribute name="root">2.16.840.1.113883.3.117.1.1.5.2.1.1.2085<xsl:value-of select="MSH/MSH.10"/>
        </xsl:attribute>
    </setId>

  <!-- the original -->
  
   
   <versionNumber value="1"/>
  
   
	<recordTarget>
		<patientRole>
			<!-- Fake root for sample. -->
			<id  root="2.16.840.1.113883.19.5">
				<xsl:attribute name="extension">
					<xsl:value-of select="PID/PID.3/CX.1"/>
				</xsl:attribute>
			</id>

            <xsl:if test="labidevent/patient/ssn != ''">
            <id root="2.16.840.1.113883.4.1">
                <xsl:attribute name="extension"><xsl:value-of select="PID/PID.3/CX.1"/>
                </xsl:attribute>
            </id>
            </xsl:if>

			<patient>
				<name>
              				 <family><xsl:value-of select="PID/PID.5/XPN.1/FN.1"/></family>
               				 <given><xsl:value-of select="PID/PID.5/XPN.2"/></given>
              				 <given><xsl:value-of select="PID/PID.5/XPN.3"/></given>
				</name>
				<administrativeGenderCode codeSystem="2.16.840.1.113883.5.1">
					<xsl:attribute name="code">
					<xsl:value-of select="PID/PID.8"/>
					</xsl:attribute>
				</administrativeGenderCode>
				<birthTime>
					<xsl:attribute name="value">
						<xsl:value-of select="substring(PID/PID.7/TS.1,1,4)"/>
						<xsl:value-of select="substring(PID/PID.7/TS.1,5,2)"/>
						<xsl:value-of select="substring(PID/PID.7/TS.1,7,2)"/>
					</xsl:attribute>
				</birthTime>
			</patient>
		</patientRole>
	</recordTarget>
  <!-- Author/authenticator may be software or may be
       someone in the role of "infection control professional".
       This author is scoped by facility. -->
  
   
   <author>
      <time value="20080807"/>
      <assignedAuthor>
         <id root="2.16.840.1.113883.3.117.1.1.5.1.1.2" extension="anAuthorID"/>
      </assignedAuthor>
   </author>
  <!-- The custodian of the CDA document is NHSN -->
  
   
   <custodian>
      <assignedCustodian>
         <representedCustodianOrganization>
            <id root="2.16.840.1.114222.4.3.2.11"/>
         </representedCustodianOrganization>
      </assignedCustodian>
   </custodian>
  <!-- legal authenticator is scoped by facility -->
  
   
   <legalAuthenticator>
      <time value="20080807"/>
      <signatureCode code="S"/>
      <assignedEntity>
         <id root="2.16.840.1.113883.3.117.1.1.5.1.1.2" extension="aLegalAuthenticatorID"/>
      </assignedEntity>
   </legalAuthenticator>
  
   
   <componentOf>
      <encompassingEncounter>
         <code codeSystem="2.16.840.1.113883.5.4" codeSystemName="actEncounterCode" displayName="Emergency">
             <xsl:attribute name="code">
                <xsl:choose>
                    <xsl:when test="labidevent/outpatient = 'Y'">AMB</xsl:when>
                    <xsl:otherwise>EMG</xsl:otherwise>
                </xsl:choose>
            </xsl:attribute>
         </code>

       <effectiveTime>
            <!-- Date Admitted to Facility -->
                <low>
			<xsl:attribute name="value">
				<!--<xsl:choose>   
					<xsl:when test="admitDate != ''"> -->
						<xsl:value-of select="substring(PV1/PV1.44/TS.1,1,4)"/>
						<xsl:value-of select="substring(PV1/PV1.44/TS.1,5,2)"/>
						<xsl:value-of select="substring(PV1/PV1.44/TS.1,7,2)"/>
				<!--	</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="substring(PV1/PV1.44/TS.1,1,4)"/>
						<xsl:value-of select="substring(PV1/PV1.44/TS.1,5,2)"/>
						<xsl:value-of select="substring(PV1/PV1.44/TS.1,7,2)"/>
					 </xsl:otherwise>
				</xsl:choose> -->
			</xsl:attribute>
		</low>
         </effectiveTime>
            <!-- facility id -->
            <!-- (In-facility location where specimen was collected,
              and date specimen collected, are recorded with the
              specimen/micro-organism information in the body.) -->
        <location>
            <healthCareFacility>
                <!-- Facility ID -->
                <id>
                    <xsl:attribute name="root"><xsl:value-of select="MSH/MSH.4/HD.2"/>
                    </xsl:attribute>
                </id>

            </healthCareFacility>
        </location>

      </encompassingEncounter>
   </componentOf>
  <!-- ********************************************************
     Structured Body
     ******************************************************** -->
  
   
   <component>
      <structuredBody>
      <!-- ********************************************************
     Encounters Section
     ******************************************************** -->
      <!-- This section will be present if the patient was
          discharged from this facility within the previous
          3 months. -->
      
<!-- SS 20150823 <xsl:if test="labidevent/patDischarge = 'Y'"> -->
         <component>
            <section>
               <templateId root="2.16.840.1.113883.10.20.5.5.16"/>
               <code code="46240-8" codeSystem="2.16.840.1.113883.6.1" displayName="Encounters"/>
               <title xmlns:cda="urn:hl7-org:v3">Encounters: Previous discharge from this facility</title>
	      <text>
		 <table>
		    <tbody>
		       <tr>
			  <td valign="top">Encounter</td>
			  <td valign="top">Emergency</td>
		       </tr>
		       <tr>
			  <td valign="top">Discharge Date</td>
			  <td valign="top">December 5, 2008</td>
		       </tr>
		    </tbody>
		 </table>
               </text>
               
               <entry typeCode="DRIV">
                  <encounter classCode="ENC" moodCode="EVN">
              <!-- CCD Encounter activity template -->
              
                     
                     <templateId root="2.16.840.1.113883.10.20.1.21"/>
              <!-- HAI Prior Discharge Encounter template -->
              
                     
                     <templateId root="2.16.840.1.113883.10.20.5.6.51"/>
              
                     
                     <id nullFlavor="NI"/>

                    <code codeSystem="2.16.840.1.113883.5.4" code="IMP" displayName="Emergency encounter"/>

                    <!-- Romaine TODO: Can this ever by set to AMB?
                     <code codeSystem="2.16.840.1.113883.5.4" codeSystemName="actEncounterCode" displayName="Outpatient">
                        <xsl:attribute name="code">
                            <xsl:choose>
                                <xsl:when test="labidevent/outpatient = 'Y'">AMB</xsl:when>
                                <xsl:otherwise>IMP</xsl:otherwise>
                            </xsl:choose>
                        </xsl:attribute>
                     </code>
                     -->


                     <effectiveTime>
                        <!-- Date last discharged from Facility -->
                        <!-- ss 20150823 Changed test statement -->

           <!--   <xsl:if test="MSH/MSH.9/MSG.2 = '03'"> -->
                        <high>
                            <xsl:attribute name="value">
				<xsl:value-of select="substring(PV1/PV1.45/TS.1,1,4)"/>
				<xsl:value-of select="substring(PV1/PV1.45/TS.1,5,2)"/>
				<xsl:value-of select="substring(PV1/PV1.45/TS.1,7,2)"/>
                            </xsl:attribute>
                        </high>
                  <!--      </xsl:if> -->
                     </effectiveTime>
            
                  
                  </encounter>
               </entry>
            </section>
         </component>

           <!--   </xsl:if> -->
      <!-- ********************************************************
     Findings Section
     ******************************************************** -->  
         <component>
            <section>
               <templateId root="2.16.840.1.113883.10.20.5.5.17"/>
               <code codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" code="18769-0" displayName="Findings"/>
               <title xmlns:cda="urn:hl7-org:v3">Lab-identified organism</title>
	      <text>
		 <table>
		    <tbody>
		       <tr>

			  <td valign="top">Significant Pathogens</td>

			  <td>MRSA, VRE</td>

		       </tr>
		       <tr>
			  <td valign="top">Microorganism Identified</td>
			  <td valign="top">CDIF</td>
		       </tr>
		       <tr>
			  <td>Date specimen collected</td>
			  <td>January 21, 2009</td>
		       </tr>
		       <tr>
			  <td>Specimen type</td>
			  <td>Pericardial fluid specimen</td>
		       </tr>
		       <tr>
			  <td>In-facility location where patient was when specimen was drawn</td>
			  <td>9W Medical/Surgical Critical Care</td>
		       </tr>
		       <tr>
			  <td>Date admitted/transferred to the location where the specimen was drawn</td>
			  <td>January 17, 2009</td>
		       </tr>

		    </tbody>
		 </table>
	</text>  
               <entry typeCode="DRIV"><!-- The organism identified --><!-- The coding of the organism is done the same way
                       as in infection reports, but different additional
                       information is required in a LIO report -->
                  <observation classCode="OBS" moodCode="EVN"><!-- Pathogen Identified Observation (LIO)  -->
                     <templateId root="2.16.840.1.113883.10.20.5.6.52"/>
                     <code codeSystem="2.16.840.1.113883.6.1" codeSystemName="LOINC" code="41852-5" displayName="Microorganism Identified"/>
                     <statusCode code="completed"/>
         <!-- SS commented out test satement   <xsl:if test="mrsa = 'Y' or mssa = 'Y' or vre = 'Y'  or acine = 'Y' or cephrkleb = 'Y' or creecoli = 'Y' or crekleb = 'Y' or cdif = 'Y'">  -->
			     <value xsi:type="CD" codeSystem="2.16.840.1.113883.6.96" codeSystemName="SNOMED CT" displayName="An Organism">
				<xsl:attribute name="code">
				    <xsl:value-of select="ORU_R01.OBSERVATION/OBX/OBX.5/CWE.1"/>
				</xsl:attribute>
			     </value>
		<!--	</xsl:if>  -->
			 <!--
			     <xsl:if test="cephrkleb = 'Y' or acine = 'Y'">
				     <value xsi:type="CD" codeSystem="2.16.840.1.113883.6.277" codeSystemName="cdcNHSN" displayName="An Organism">
					<xsl:attribute name="code">
					    <xsl:value-of select="snomed_organism"/>
					</xsl:attribute>
				     </value>
			     </xsl:if>
			 -->
                     <!-- Specimen collection procedure contains specimen collection encounter -->
                     <entryRelationship typeCode="COMP">
                        <procedure classCode="PROC" moodCode="EVN"><!-- Specimen Collection Procedure (LIO)  -->
                           <templateId root="2.16.840.1.113883.10.20.5.6.53"/><!-- Date specimen collected -->

                             <effectiveTime>
                                <!-- Date last discharged from Facility -->
                  <!-- SS 20150823 commented out test statement       <xsl:if test="labidevent/specimenDate != ''"> -->
                                    <xsl:attribute name="value"><xsl:value-of select="substring(ORU_R01.ORDER_OBSERVATION/ORU_R01.SPECIMEN/SPM/SPM.17/DR.1/TS.1,1,4)"/><xsl:value-of select="substring(ORU_R01.ORDER_OBSERVATION/ORU_R01.SPECIMEN/SPM/SPM.17/DR.1/TS.1,5,2)"/><xsl:value-of select="substring(ORU_R01.ORDER_OBSERVATION/ORU_R01.SPECIMEN/SPM/SPM.17/DR.1/TS.1,7,2)"/>
                                    </xsl:attribute>
                          <!--      </xsl:if> -->
                             </effectiveTime>


                           <participant typeCode="PRD">
                    
                              
                              <participantRole classCode="SPEC">
                      
                                 
                                 <playingEntity>
                        
                                    
                                    <code codeSystem="2.16.840.1.113883.6.96" codeSystemName="SNOMED CT" displayName="A specimen source">
                                        <xsl:attribute name="code">
                                            <xsl:value-of select="ORU_R01.ORDER_OBSERVATION/ORU_R01.SPECIMEN/SPM/SPM.4/CWE.2"/>
                                        </xsl:attribute>
                                    </code>
                      
                                 
                                 </playingEntity>
                    
                              
                              </participantRole>
                  
                           
                           </participant><!-- if inpatient --><!-- The specimen collection encounter -->
                           <entryRelationship typeCode="COMP" inversionInd="true">
                              <encounter classCode="ENC" moodCode="EVN">
                      <!--Specimen Collection Encounter (LIO) -->
                      
                                 
                                 <templateId root="2.16.840.1.113883.10.20.5.6.54"/>
                      
                                 
                                 <id nullFlavor="NI"/>
                      <!-- If person was an inpatient at the in-facility location 
                                 where the specimen was taken, this will be the date
                                 admitted/transferred there -->
                      
                             <effectiveTime>
                                <!-- Date Admitted to location -->
                        <!-- SS 20150823        <xsl:if test="labidevent/locationAdmitDate != ''"> -->
					<low>
					    <xsl:attribute name="value">
					    	<xsl:value-of select="substring(PV1/PV1.44/TS.1,1,4)"/><xsl:value-of select="substring(PV1/PV1.44/TS.1,5,2)"/><xsl:value-of select="substring(PV1/PV1.44/TS.1,7,2)"/>
					    </xsl:attribute>
					</low>
                              <!--  </xsl:if> -->
                             </effectiveTime>
                      <!-- The in-facility location where the specimen was taken -->
                      
                                 
                                 <participant typeCode="LOC">
                        <!-- CCD Encounter Location Template-->
                        
                                    
                                    <templateId root="2.16.840.1.113883.10.20.1.45"/>
                        
                                    
                                    <participantRole classCode="SDLOC">
                          
                                       
                                        <id>
                                            <xsl:attribute name="root"><xsl:value-of select="//oid"/>
                                            </xsl:attribute>
                                            <xsl:attribute name="extension"><xsl:value-of select="PV1/PV1.3/PL.1"/>
                                            </xsl:attribute>
                                        </id>
                          
                                       
                                       <playingEntity classCode="PLC">
                            
                                          
                                            <code codeSystem="2.16.840.1.113883.6.259" codeSystemName="HL7 Healthcare Service Location Code" displayName="A location">
                                                 <xsl:attribute name="code">
                                                    <xsl:value-of select="PV1/PV1.3/PL.1"/>
                                                </xsl:attribute>
                                            </code>
                          
                                       
                                       </playingEntity>
                        
                                    
                                    </participantRole>
                      
                                 
                                 </participant>
                    
                              
                              </encounter>
                           </entryRelationship>
                        </procedure>
                     </entryRelationship><!-- end of information about this particular specimen -->
                  <!--   <entryRelationship typeCode="COMP">
                        <observation classCode="OBS" moodCode="EVN"> --> <!--Prior Documentation Observation  -->
                       <!--     <xsl:attribute name="negationInd">
                                <xsl:choose>
                                    <xsl:when test="labidevent/prevPos = 'Y'">false</xsl:when>
                                    <xsl:otherwise>true</xsl:otherwise>
                                </xsl:choose>
                            </xsl:attribute>
                        
                           <templateId root="2.16.840.1.113883.10.20.5.6.55"/>
                           <code codeSystem="2.16.840.1.113883.5.4" code="ASSERTION"/>
                           <statusCode code="completed"/>
                           <value xsi:type="CD" codeSystem="2.16.840.1.113883.6.277" codeSystemName="cdcNHSN" code="3001-5" displayName="Prior documentation of the patient being infected or colonized with the organism currently being reported."/>
                        </observation>
                     </entryRelationship> --><!-- end of information related to the micro-organism identified -->
                  </observation>
               </entry>
            </section>
         </component>
    
      
      </structuredBody>
   </component>


</ClinicalDocument>
</xsl:for-each>
</root>
</xsl:template>
</xsl:stylesheet>