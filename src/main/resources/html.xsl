<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
   <xsl:template match="/">
      <html>
         <body>
               <xsl:for-each select="outputData/list">
			   <div style="margin-bottom:10pt">
			   <table margin-top="3cm">  
					 <tr>
					 <td>
                      <p>  <b> <xsl:value-of select="xmlGCDate" /></b> </p>
                     </td>
					 </tr>
					 <tr>
					 <td>
                      <p> <b> <xsl:value-of select="thread" /></b> </p>
                     </td>
					 </tr >
					 <tr>
					 <td>
                       <p> <font color="#002266">  <xsl:value-of select="textBlock"/> </font> </p>
				 	 </td>
					 </tr >
				 </table>
				 </div>
               </xsl:for-each>
         </body>
		 <style>
   body { 
    margin-right: 3%; 
    margin-left: 3%; 
	margin-top: 3%; 
   }
  </style>
      </html>
   </xsl:template>
</xsl:stylesheet>