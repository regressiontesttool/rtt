<?xml version="1.0" encoding="UTF-8"?>
<!-- This program and the accompanying materials are made available under 
	the terms of the MIT license (X11 license) which accompanies this distribution. -->
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:rtt="regression.test.tool">

	<xsl:output method="html" encoding="UTF-8" indent="yes" />
	<xsl:variable name="idx" select="0" />

	<xsl:template match="/">
		<html>
			<head>
				<title>Archive Log</title>
			</head>

			<body>
				<xsl:apply-templates />
			</body>
		</html>
	</xsl:template>

	<!-- Template for html body. -->
	<xsl:template match="rtt:archiveLog">
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


	<!-- Template for log entries with informational data. Background color 
		of cells will be bright blue. -->
	<xsl:template match="rtt:entry[@type='INFO']">
		<tr style="background-color:rgb(192,240,255)">
			<td>
				<xsl:value-of select="@date" />
			</td>
			<td>
				<xsl:value-of select="@msg" />
				<b>
					[
					<xsl:value-of select="@suffix" />
					]
				</b>
				<xsl:if test="count(rtt:detail) > 0">
					<br />
					<a href="#"
						onclick="this.nextSibling.style.display = this.nextSibling.style.display=='none'?'block':'none'; return false;">Details</a>
					<table style="display:none">
						<xsl:apply-templates select="rtt:detail" />
					</table>
				</xsl:if>
			</td>
		</tr>
	</xsl:template>

	<!-- Template for log entries with generation data. Background color of 
		cells will be yellow. -->
	<xsl:template match="rtt:entry[@type='GENERATION']">
		<tr style="background-color:rgb( 255 ,240,144)">
			<td>
				<xsl:value-of select="@date" />
			</td>
			<td>
				<xsl:value-of select="@msg" />
				<b>
					[
					<xsl:value-of select="@suffix" />
					]
				</b>
				<xsl:if test="count(rtt:detail[@priority=0]) > 0">
					<br />
					<a href="#"
						onclick="this.nextSibling.style.display = this.nextSibling.style.display=='none'?'block':'none'; return false;">Unchanged Results</a>
					<table style="display:none">
						<xsl:apply-templates select="rtt:detail[@priority=0]" />
					</table>
				</xsl:if>
				<xsl:if test="count(rtt:detail[@priority=1]) > 0">
					<br />
					<a href="#"
						onclick="this.nextSibling.style.display = this.nextSibling.style.display=='none'?'block':'none'; return false;">New Results</a>
					<table style="display:none">
						<xsl:apply-templates select="rtt:detail[@priority=1]" />
					</table>
				</xsl:if>
				<xsl:if test="count(rtt:detail[@priority=2]) > 0">
					<br />
					<a href="#"
						onclick="this.nextSibling.style.display = this.nextSibling.style.display=='none'?'block':'none'; return false;">Versioned Results</a>
					<table style="display:none">
						<xsl:apply-templates select="rtt:detail[@priority=2]" />
					</table>
				</xsl:if>
			</td>
		</tr>
	</xsl:template>

	<!-- Template for detail entries. Will display in table: | suffix | message 
		| -->
	<xsl:template match="rtt:detail">
		<tr style="background-color:rgb(200,200,200)">
			<td>
				<xsl:value-of select="@suffix" />
			</td>
			<td>
				<xsl:value-of select="@msg" />
			</td>
		</tr>
	</xsl:template>

	<!-- Template for choosing the background color of testrun entries. The 
		color will be chosen by count of failed results. -->
	<xsl:template match="rtt:entry[@xsi:type='testrun']">
		<xsl:choose>
			<xsl:when test="count(rtt:result[@type='FAILED']) > 0">
				<tr style="background-color:rgb(240,50,50)">
					<xsl:call-template name="testrun" />
				</tr>
			</xsl:when>
			<xsl:when test="count(rtt:result[@type='FAILED']) = 0">
				<tr style="background-color:rgb(50,240,50)">
					<xsl:call-template name="testrun" />
				</tr>
			</xsl:when>
			<xsl:otherwise>
				<tr style="background-color:rgb(200,200,200)">
					<xsl:call-template name="testrun" />
				</tr>
			</xsl:otherwise>

		</xsl:choose>
	</xsl:template>

	<!-- Template for testrun entries. Menus for passed, skipped and failed 
		results will be generated. -->
	<xsl:template name="testrun">
		<td>
			<xsl:value-of select="@date" />
		</td>
		<td>
			Test run (Configuration:
			<span style="font-weight:bold">
				[
				<xsl:value-of select="@configuration" />
				]
			</span>
			)
			<xsl:call-template name="menu" />
		</td>
	</xsl:template>

	<!-- Template for result entry menu. There will be one menu for each category 
		of results. -->
	<xsl:template name="menu">
		<xsl:if test="count(rtt:result[@type='PASSED']) > 0">
			<br />
			<a href="#"
				onclick="this.nextSibling.style.display = this.nextSibling.style.display=='none'?'block':'none'; return false;">Passed Tests</a>
			<table style="display:none">
				<xsl:apply-templates select="rtt:result[@type='PASSED']" />
			</table>
		</xsl:if>

		<xsl:if test="count(rtt:result[@type='SKIPPED']) > 0">
			<br />
			<a href="#"
				onclick="this.nextSibling.style.display = this.nextSibling.style.display=='none'?'block':'none'; return false;">Skipped Tests</a>
			<table style="display:none">
				<xsl:apply-templates select="rtt:result[@type='SKIPPED']" />
			</table>
		</xsl:if>

		<xsl:if test="count(rtt:result[@type='FAILED']) > 0">
			<br />
			<a href="#"
				onclick="this.nextSibling.style.display = this.nextSibling.style.display=='none'?'block':'none'; return false;">Failed Tests</a>
			<table style="display:none">
				<xsl:apply-templates select="rtt:result[@type='FAILED']" />
			</table>
		</xsl:if>
	</xsl:template>

	<!-- Template for passed results. Displayed in table like | "case name" 
		| Test [ "case name" ] in test suite [ "suite name" ] passed. | -->
	<xsl:template match="rtt:result[@type='PASSED']">
		<tr style="background-color:rgb(100,200,100)">
			<td>
				<xsl:value-of select="@testcase" />
			</td>
			<td>
				Test [
				<xsl:value-of select="@testcase" />
				] in test suite [
				<xsl:value-of select="@testsuite" />
				] passed.
			</td>
			<xsl:if test="string-length(@note) > 0">
				<td>
					Note:
					<xsl:value-of select="@note" />
				</td>
			</xsl:if>
		</tr>
	</xsl:template>

	<!-- Template for skipped results. Displayed in table like | "case name" 
		| Test [ "case name" ] in test suite [ "suite name" ] skipped. | -->
	<xsl:template match="rtt:result[@type='SKIPPED']">
		<tr style="background-color:rgb(200,200,200)">
			<td>
				<xsl:value-of select="@testcase" />
			</td>
			<td>
				Test [
				<xsl:value-of select="@testcase" />
				] in test suite [
				<xsl:value-of select="@testsuite" />
				] skipped.
			</td>
			<xsl:if test="string-length(@note) > 0">
				<td>
					Note:
					<xsl:value-of select="@note" />
				</td>
			</xsl:if>
		</tr>
	</xsl:template>

	<!-- Template for failed results. Displayed in table like | "case name" 
		| Test [ "case name" ] in test suite [ "suite name" ] failed with reason 
		... | -->
	<xsl:template match="rtt:result[@type='FAILED']">
		<tr style="background-color:rgb(240,100,100)">
			<td>
				<xsl:value-of select="@testcase" />
			</td>
			<td>
				Test [
				<xsl:value-of select="@testcase" />
				] in test suite [
				<xsl:value-of select="@testsuite" />
				] failed with reason: '
				<xsl:value-of select="@msg" />
				'

				<xsl:if test="string-length(@path)>0">
					<br />
					XPath:
					<i>
						<xsl:value-of select="@path" />
					</i>
				</xsl:if>
			</td>
			<xsl:if test="string-length(@note) > 0">
				<td>
					Note:
					<xsl:value-of select="@note" />
				</td>
			</xsl:if>
		</tr>
	</xsl:template>

</xsl:stylesheet>
