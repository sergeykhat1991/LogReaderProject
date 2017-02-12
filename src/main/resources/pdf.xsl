<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.1" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:fo="http://www.w3.org/1999/XSL/Format" exclude-result-prefixes="fo">
<xsl:template match="outputData">
    <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
      <fo:layout-master-set>
        <fo:simple-page-master master-name="simpleA4" page-height="29.7cm" page-width="21cm" margin-top="2cm" margin-bottom="2cm" margin-left="2cm" margin-right="2cm">
          <fo:region-body/>
        </fo:simple-page-master>
      </fo:layout-master-set>
      <fo:page-sequence master-reference="simpleA4">
        <fo:flow flow-name="xsl-region-body">
          <fo:block>
          <fo:table>    
            <fo:table-body>
			
              <xsl:apply-templates select="list"/>
			  
            </fo:table-body>
          </fo:table>
          </fo:block>
        </fo:flow>
      </fo:page-sequence>
     </fo:root>
</xsl:template>
<xsl:template match="list">

    <fo:table-row >   
      <fo:table-cell>
        <fo:block font-weight = "bold" font-size="9pt">
          <xsl:value-of select="xmlGCDate"/>
        </fo:block>
      </fo:table-cell>
	</fo:table-row>
	<fo:table-row>
      <fo:table-cell>
        <fo:block font-weight = "bold" margin-bottom="2pt" font-size="9pt">
          <xsl:value-of select="thread"/>
        </fo:block>
      </fo:table-cell>  
	</fo:table-row>
	<fo:table-row>
      <fo:table-cell>
        <fo:block margin-bottom="12pt" color="#003366" font-size="10pt">
      <xsl:value-of select="textBlock"/>
        </fo:block>
      </fo:table-cell>
    </fo:table-row>
	
  </xsl:template>
</xsl:stylesheet>