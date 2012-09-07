<?xml version="1.0" encoding="UTF-8"?>
<!-- 
	XSL style sheet for upgrading XML3D v0.3 documents to 
	XML3D v0.4
		
	author: Benjamin Friedrich
	version: 1.0  09/10
 -->
<xsl:stylesheet version="2.0"
	xmlns="http://www.xml3d.org/2009/xml3d"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:xml3d="http://www.xml3d.org/2009/xml3d" 
	exclude-result-prefixes="#all">

	<!-- 
		Removes the whitespace between all elements. This does not affect
		text content of the elements
	 -->
	<!-- <xsl:strip-space elements="*" />  -->

	<xsl:output method="xml" encoding="utf-8" indent="yes" omit-xml-declaration="no" />


	<!-- 
		Identity template - keeps all XML3D data which are not 
		affected by the upgrade 
	-->
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*" />
			<xsl:apply-templates />
		</xsl:copy>
	</xsl:template>

	<!-- 
		Creates a new 'name' attribute in the child element of <bind>
		with the value of the 'semantic' attribute of <bind>
	  -->
	<xsl:template match="xml3d:bind/*">
		<xsl:copy>
			<xsl:attribute name="name">
				<xsl:value-of select="../@semantic" />
			</xsl:attribute>
			<xsl:apply-templates select="@*" />
			<xsl:apply-templates />
		</xsl:copy>
	</xsl:template>

	<!-- 
		Removes the <bind> element
	  -->
	<xsl:template match="xml3d:bind">
		<xsl:apply-templates select="child::*"/>
	</xsl:template>

	<!-- 
		The data of referenced meshes will be packaged into a data element.
	  -->
	<xsl:template match="xml3d:mesh[//xml3d:use[@href=concat('#',current()/@id)]]">
		<mesh type="{@type}">
			<data id="{@id}">
			<xsl:apply-templates/>
			</data>
		</mesh>
	</xsl:template>

	<!-- 
		The data of meshes defined in a defs section will be packaged into a data element.
		Keep this tempalte after the non-defs template
	  -->
	<xsl:template match="xml3d:defs/xml3d:mesh">
		<data id="{@id}">
		<xsl:apply-templates/>
		</data>
	</xsl:template>

	
	<xsl:template match="xml3d:use">
		<mesh src="{@href}" type="triangles"/>
	</xsl:template>

</xsl:stylesheet>