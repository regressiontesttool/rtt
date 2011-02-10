<?xml version="1.0" encoding="UTF-8"?>
<!--
	This program and the accompanying materials are made available under the
	terms of the MIT license (X11 license) which accompanies this distribution.
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	<xsl:output method="html" version="1.0" encoding="UTF-8" indent="yes"/>
	
<xsl:variable name="idx" select="0"/>	
	
<xsl:template match="/">
	<html>
		
		<head>
        <title>Archive Log</title>

<script type="javascript">
<!--

function toggle(obj)
{
	obj.nextSibling.style.display = 'block';
}

-->
</script>


 




		</head>
		<body>
			<xsl:apply-templates/>
		</body>
	</html>
	</xsl:template>	
	
	<xsl:template match="archiveLog">
		<h1> Archive Log</h1>
		<table style="background-color:rgb(200,200,200)" >
			<tbody>
				<tr>
					<th>Date</th>		
					<th>Message</th>
				</tr>
					<xsl:apply-templates/>
				
			</tbody>
		</table>
	</xsl:template>
	
	
	
	<xsl:template match="testrun[count(failed)=0]">
			<tr style="background-color:rgb(50,240,50)">
			<td><xsl:value-of select="@date" /></td>

		
			<td>Test run (Configuration: <span style="font-weight:bold"> [<xsl:value-of select="@configuration" />]</span>)
			<xsl:call-template name="menu" />
			</td>
		</tr>
	</xsl:template>
	
	<xsl:template match="testrun[count(failed)>0]">
		<tr style="background-color:rgb(240,50,50)">
			<td><xsl:value-of select="@date" /></td>
			<td>Test run (Configuration: <span style="font-weight:bold">[<xsl:value-of select="@configuration" />]</span>)
			<xsl:call-template name="menu" />
			
			</td>
		</tr>
	</xsl:template>

	
		<xsl:template match="failed">
		<tr style="background-color:rgb(240,100,100)">
			<td> <xsl:value-of select="@test" />  </td>
			<td>
			Test [<xsl:value-of select="@test" />] in Testsuit [<xsl:value-of select="@testsuit" />] failed with reason: '<xsl:value-of select="@msg" />'
			
			<xsl:if test="string-length(@path)>0">
			<br />
			XPath: <i><xsl:value-of select="@path" /></i>
			</xsl:if>
			</td>
		</tr>
		
	</xsl:template>




 <xsl:template name="menu">

 	<xsl:if test="count(passed) > 0">
	<br /><a href="#" onclick="this.nextSibling.style.display = this.nextSibling.style.display=='none'?'block':'none'; return false;">Passed Tests</a>
		<table  style="display:none"><xsl:apply-templates select="passed"/></table>
	</xsl:if>
	<xsl:if test="count(skipped) > 0">
	<br /><a href="#" onclick="this.nextSibling.style.display = this.nextSibling.style.display=='none'?'block':'none'; return false;">Skipped Tests</a>
		<table  style="display:none"><xsl:apply-templates select="skipped"/></table>
	</xsl:if>
	<xsl:if test="count(failed) > 0">
	<br /><a href="#" onclick="this.nextSibling.style.display = this.nextSibling.style.display=='none'?'block':'none'; return false;">Failed Tests</a>
		<table  style="display:none"><xsl:apply-templates select="failed"/></table>
	</xsl:if>

 </xsl:template>




<xsl:template match="info">
  <xsl:choose>
    <xsl:when test="@xsi:type  = 'GenerationInformation'">
      <xsl:call-template name="genInfo" />
    </xsl:when>
    <xsl:otherwise>
      <tr style="background-color:rgb(192,240,255)">
        <td>
          <xsl:value-of select="@date" />
        </td>
        <td>
          <xsl:value-of select="@msg" />
          <b>
            [<xsl:value-of select="@suffix" />]
          </b>
          <xsl:if test="count(testInformation) > 0">
            <br />
            <a href="#" onclick="this.nextSibling.style.display = this.nextSibling.style.display=='none'?'block':'none'; return false;">More Information</a>
            <table  style="display:none">
              <xsl:apply-templates select="testInformation"/>
            </table>
          </xsl:if>
        </td>
      </tr>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>
	
<xsl:template name ="genInfo" match="GenerationInformation">
		<tr style="background-color:rgb( 255 ,240,144)">
			<td><xsl:value-of select="@date" /></td>
			<td><xsl:value-of select="@msg" /> <b>[<xsl:value-of select="@suffix" />]</b>
      <xsl:if test="count(testInformation[@priority=0]) > 0">
        <br /><a href="#" onclick="this.nextSibling.style.display = this.nextSibling.style.display=='none'?'block':'none'; return false;">Unchanged Results</a>
  		  <table  style="display:none"><xsl:apply-templates select="testInformation[@priority=0]"/></table>
  		</xsl:if>
      <xsl:if test="count(testInformation[@priority=1]) > 0">
        <br /><a href="#" onclick="this.nextSibling.style.display = this.nextSibling.style.display=='none'?'block':'none'; return false;">New Results</a>
  		  <table  style="display:none"><xsl:apply-templates select="testInformation[@priority=1]"/></table>
  		</xsl:if>
      <xsl:if test="count(testInformation[@priority=2]) > 0">
        <br /><a href="#" onclick="this.nextSibling.style.display = this.nextSibling.style.display=='none'?'block':'none'; return false;">Versioned Results</a>
  		  <table  style="display:none"><xsl:apply-templates select="testInformation[@priority=2]"/></table>
  		</xsl:if>
      </td>
		</tr>
</xsl:template>



	<xsl:template match="passed">
		<tr style="background-color:rgb(100,200,100)">
			<td> <xsl:value-of select="@test" />  </td>
			<td>Test [<xsl:value-of select="@test" />] in Testsuit [<xsl:value-of select="@testSuite" />] passed.
			</td>
		</tr>
		
	</xsl:template>
	
	
	<xsl:template match="skipped">
		<tr style="background-color:rgb(200,200,200)">
				<td> <xsl:value-of select="@test" />  </td>
      <td>
			Test [<xsl:value-of select="@test" />] in Testsuit [<xsl:value-of select="@testSuite" />] skipped.
			</td>
		</tr>
		
	</xsl:template>
	
	
	
	<xsl:template match="testInformation">
		<tr style="background-color:rgb(200,200,200)">
			<td>
			  <xsl:value-of select="@test" />
			</td> 
      <td>
			  <xsl:value-of select="@msg" />
			</td>
		</tr>
		
	</xsl:template>
	
	
</xsl:stylesheet>
