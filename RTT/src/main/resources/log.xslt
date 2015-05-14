<?xml version="1.0" encoding="UTF-8"?>
<!-- This program and the accompanying materials are made available under 
	the terms of the MIT license (X11 license) which accompanies this distribution. -->
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:rtt="regression.test.tool">

	<xsl:output method="html" encoding="UTF-8" indent="yes" />

	<xsl:template match="/">
		<html>
			<head>
				<title>Archive Log</title>
				<script>
					<xsl:comment>
						function changeDisplay(id) {
						var element = document.getElementById(id);
						element.style.display = element.style.display=='none'?'block':'none';
						return false;
						}
					</xsl:comment>
				</script>
			</head>

			<body>
				<xsl:apply-templates />
			</body>
		</html>
	</xsl:template>

	<!-- Template for html body. -->
	<xsl:template match="archiveLog">
		<h1>Archive Log</h1>
		<table style="background-color:rgb(200,200,200)">
			<tbody>
				<tr>
					<th>Date</th>
					<th>Message</th>
				</tr>
				<xsl:apply-templates />
			</tbody>
		</table>
	</xsl:template>	
	
	<xsl:template name="dateTemplate">
		<td><xsl:value-of select="@date" /></td>
	</xsl:template>
	
	<xsl:template name="messageTemplate">
		<xsl:value-of select="@msg" />
		<xsl:if test="string-length(@suffix)>0">
			<b>[<xsl:value-of select="@suffix" />]</b>
		</xsl:if>		
	</xsl:template>

	<xsl:template name="idTable">
		<xsl:param name="linkText" />
		<xsl:param name="details" />
		<xsl:variable name="id"><xsl:value-of select="generate-id($details)"/></xsl:variable>
		<xsl:if test="count($details) > 0">		
		<br />
		<xsl:element name="a">
			<xsl:attribute name="href">#</xsl:attribute>
			<xsl:attribute name="onclick">return changeDisplay('<xsl:value-of select="$id" />');</xsl:attribute>
			<xsl:value-of select="$linkText" />
		</xsl:element>
		<xsl:element name="table">
			<xsl:attribute name="style">display:none</xsl:attribute>
			<xsl:attribute name="id">
				<xsl:value-of select="$id" />
			</xsl:attribute>
			<xsl:for-each select="$details">
				<xsl:apply-templates select="." />
			</xsl:for-each>
		</xsl:element>
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="detail">
		<tr style="background-color:rgb(200,200,200)">
			<td><xsl:value-of select="@msg" /></td>
			<td><xsl:value-of select="@suffix" /></td>			
		</tr>
	</xsl:template>	

<!-- 	Template for log entries with informational data. Background color
 		of cells will be bright blue. -->	
	<xsl:template match="entry[@type='INFO']">
		<xsl:element name="tr">
			<xsl:attribute name="style">background-color:rgb(192,240,255)</xsl:attribute>
			<xsl:call-template name="dateTemplate" />			
				<td>
					<xsl:call-template name="messageTemplate" />
					<xsl:call-template name="idTable">
						<xsl:with-param name="linkText">Details</xsl:with-param>
						<xsl:with-param name="details" select="detail" />
					</xsl:call-template>
				</td>
			</xsl:element>
	</xsl:template>
	
<!-- 	Template for log entries with archive data. Background color
 		of cells will be blue. -->
	<xsl:template match="entry[@type='ARCHIVE']">
		<xsl:element name="tr">
			<xsl:attribute name="style">background-color:rgb(0,220,255)</xsl:attribute>
			<xsl:call-template name="dateTemplate" />			
				<td>
					<xsl:call-template name="messageTemplate" />
					<xsl:call-template name="idTable">
						<xsl:with-param name="linkText">Details</xsl:with-param>
						<xsl:with-param name="details" select="detail" />
					</xsl:call-template>
				</td>
			</xsl:element>
	</xsl:template>

	<!-- Template for log entries with generation data. Background color of 
		cells will be yellow. -->
	<xsl:template match="entry[@type='GENERATION']">
		<xsl:element name="tr">
			<xsl:attribute name="style">background-color:rgb(255,240,144)</xsl:attribute>
			<xsl:call-template name="dateTemplate" />
			<td>
				<xsl:call-template name="messageTemplate" />
				<xsl:call-template name="idTable">
					<xsl:with-param name="linkText">Unchanged Results</xsl:with-param>
					<xsl:with-param name="details" select="detail[@priority=0]" />
				</xsl:call-template>
				<xsl:call-template name="idTable">
					<xsl:with-param name="linkText">New Results</xsl:with-param>
					<xsl:with-param name="details" select="detail[@priority=1]" />
				</xsl:call-template>
				<xsl:call-template name="idTable">
					<xsl:with-param name="linkText">Erroneous Results</xsl:with-param>
					<xsl:with-param name="details" select="detail[@priority=2]" />
				</xsl:call-template>
			</td>
		</xsl:element>			
	</xsl:template>
	
	
	<!-- Template for choosing the background color of testrun entries. The 
		color will be chosen by count of failed results. -->
	<xsl:template match="entry[@type='TESTRUN']">
		<xsl:element name="tr">
			<xsl:attribute name="style">
				<xsl:choose>
					<xsl:when test="count(result[@type='FAILED']) > 0">
						background-color:rgb(240,50,50)
					</xsl:when>
					<xsl:when test="count(result[@type='FAILED']) = 0">
						background-color:rgb(50,240,50)
					</xsl:when>
					<xsl:otherwise>
						background-color:rgb(200,200,200)
					</xsl:otherwise>					
				</xsl:choose>	
			</xsl:attribute>
			<xsl:call-template name="dateTemplate" />
			<xsl:call-template name="testrun" />		
		</xsl:element>
		
	</xsl:template>

	<!-- Template for testrun entries. Menus for passed, skipped and failed 
		results will be generated. -->
	<xsl:template name="testrun">
		<td>
			<xsl:call-template name="messageTemplate" />			
			<xsl:call-template name="idTable">
				<xsl:with-param name="linkText">Passed Tests</xsl:with-param>
				<xsl:with-param name="details" select="result[@type='PASSED']" />
			</xsl:call-template>
			<xsl:call-template name="idTable">
				<xsl:with-param name="linkText">Skipped Tests</xsl:with-param>
				<xsl:with-param name="details" select="result[@type='SKIPPED']" />
			</xsl:call-template>			
			<xsl:call-template name="idTable">
				<xsl:with-param name="linkText">Failed Tests</xsl:with-param>
				<xsl:with-param name="details" select="result[@type='FAILED']" />
			</xsl:call-template>
		</td>
	</xsl:template>
	
	<xsl:template name="commentTemplate">
		<xsl:if test="string-length(comment) > 0">
			<td>Comment: <xsl:value-of select="comment" /></td>
		</xsl:if>
	</xsl:template>
	
	<xsl:template name="testrunTemplate">
		<td>[<xsl:value-of select="@testsuite" />/<xsl:value-of select="@testcase" />]</td>
		<td><xsl:value-of select="@type"/></td>
	</xsl:template>
	
	<!-- Template for passed results. -->
	<xsl:template match="result[@type='PASSED']">
		<tr style="background-color:rgb(100,200,100)">
			<xsl:call-template name="testrunTemplate" />
			<xsl:call-template name="commentTemplate"/>
		</tr>
	</xsl:template>

	<!-- Template for skipped results. -->
	<xsl:template match="result[@type='SKIPPED']">
		<tr style="background-color:rgb(200,200,200)">
			<xsl:call-template name="testrunTemplate" />
			<xsl:call-template name="commentTemplate" />
		</tr>
	</xsl:template>

	<!-- Template for failed results. Displayed in table like | "case name" 
		| Test [ "case name" ] in test suite [ "suite name" ] failed with reason 
		... | -->
	<xsl:template match="result[@type='FAILED']">
		<tr style="background-color:rgb(240,100,100)">
			<xsl:call-template name="testrunTemplate" />
			<td><ul>
				<xsl:for-each select="failure">
					<li><xsl:value-of select="@msg"/> - XPath:<i><xsl:value-of select="@path" /></i></li>
				</xsl:for-each>				
			</ul></td>
			<xsl:call-template name="commentTemplate"/>
		</tr>
	</xsl:template>

</xsl:stylesheet>