<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xml3d="http://www.w3.org/2009/xml3d"
	xmlns="http://www.w3.org/2009/xml3d"
	xmlns:cx3d="xalan://org.xml3d.converter.x3d.IndexedFaceSet"
	exclude-result-prefixes="xml3d">
	<!-- standard copy template -->
	<xsl:output method="xml" indent="yes" omit-xml-declaration="no"/>
	
	<xsl:variable name="quot">"</xsl:variable>
	<xsl:template match="text()" mode="#all">
	  <xsl:value-of select="normalize-space(.)"/>
	</xsl:template>
	
	<xsl:template match="/">
		<xml3d xmlns="http://www.w3.org/2009/xml3d" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.w3.org/2009/xml3d ../../org.xml3d.spec/generated/xsd/xml3d.xsd">
		<defs>
		  <xsl:apply-templates mode="defs"/>
		</defs>
		<xsl:apply-templates mode="sg"/>
		</xml3d>
	</xsl:template>
	
	<xsl:template match="Transform" mode="defs">
	<transform>
		<xsl:attribute name="id">
		<xsl:value-of select="if (@DEF) then @DEF else concat('t_', generate-id(.))"></xsl:value-of>
		</xsl:attribute>
		<xsl:copy-of select="@center"/>
		<xsl:copy-of select="@rotation"/>
		<xsl:copy-of select="@scale"/>
		<xsl:copy-of select="@scaleOrientation"/>
		<xsl:copy-of select="@translation"/>
	</transform>
	<xsl:apply-templates mode="defs"/>
	</xsl:template>
	

	
	<xsl:template match="Scene" mode="sg">
		<group id="root">
			<xsl:apply-templates mode="sg"/>
		</group>
	</xsl:template>
	
	<xsl:template match="Transform" mode="sg">
		<group>
		<xsl:if test="@DEF">
			<xsl:attribute name="id" select="@DEF"/>
		</xsl:if>
		<xsl:attribute name="transform">
		<xsl:value-of select="if (@DEF) then @DEF else concat('t_', generate-id(.))"/>
		</xsl:attribute>
		<xsl:apply-templates mode="sg"/>
		</group>
	</xsl:template>
	
	<xsl:template match="Collision | Group" mode="sg">
		<group>
		<xsl:if test="@DEF">
			<xsl:attribute name="id" select="@DEF"/>
		</xsl:if>
		<xsl:apply-templates mode="sg"/>
		</group>
	</xsl:template>
	
	<xsl:template match="Shape" mode="sg">
		<group shader="phong">
		<xsl:apply-templates mode="sg"/>
		</group>
	</xsl:template>
	
	<xsl:template match="Appearance" mode="sg">
	<xsl:attribute name="style">
	<xsl:apply-templates mode="sg"/>
	</xsl:attribute>
	</xsl:template>
	
	<xsl:template match="ImageTexture" mode="sg">
		<xsl:text>diffuse-image: url('</xsl:text>
		<xsl:value-of select="substring-before(substring-after(@url, $quot), $quot)"></xsl:value-of>
		<xsl:text>'); </xsl:text>
	</xsl:template>

	<xsl:template match="Material" mode="sg">
		<xsl:if test="@diffuseColor">
		<xsl:text>diffuse-color: rgb(</xsl:text>
			<xsl:value-of select="translate(@diffuseColor, ' ', ',')"/>
		<xsl:text>); </xsl:text>
		</xsl:if>
		<xsl:if test="@specularColor">
		<xsl:text>specular-color: rgb(</xsl:text>
			<xsl:value-of select="@specularColor"/>
		<xsl:text>); </xsl:text>
		</xsl:if>
		<xsl:if test="@emissiveColor">
		<xsl:text>emissive-color: rgb(</xsl:text>
			<xsl:value-of select="@emissiveColor"/>
		<xsl:text>); </xsl:text>
		</xsl:if>
		<xsl:if test="@transparency">
		<xsl:text>transparency: </xsl:text>
			<xsl:value-of select="@transparency * 100.0"/>
		<xsl:text>%; </xsl:text>
		</xsl:if>
		<xsl:if test="@shininess">
		<xsl:text>shininess: </xsl:text>
			<xsl:value-of select="@shininess * 100.0"/>
		<xsl:text>%; </xsl:text>
		</xsl:if>
		<xsl:if test="@ambientIntensity">
		<xsl:text>ambientIntensity: </xsl:text>
			<xsl:value-of select="@ambientIntensity * 100.0"/>
		<xsl:text>%; </xsl:text>
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="IndexedFaceSet" mode="sg">
		<mesh type="triangles" >
		<xsl:attribute name="id" select="if (@DEF) then @DEF else concat('m_', generate-id(.))"></xsl:attribute>
		<xsl:copy-of select="cx3d:convert(.)"/> 
		</mesh>
	</xsl:template>
	
	
</xsl:stylesheet>