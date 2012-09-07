<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xml3d="http://www.xml3d.org/2009/xml3d"
	xmlns="http://www.xml3d.org/2009/xml3d" xmlns:f="http://myfunctions.org" xmlns:cx3d="xalan://org.xml3d.converter.x3d.ConverterFunctions"
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:x3d="http://www.web3d.org/specifications/x3d-namespace"
	exclude-result-prefixes="#all" >

	<xsl:variable name="quot">"</xsl:variable>
	<xsl:variable name="squot">'</xsl:variable>

	<xsl:function name="f:toXML3DType" as="xs:string">
	<xsl:param name="type" as="xs:string"/>
	<xsl:choose>
	<xsl:when test="$type = 'SFFloat'">float</xsl:when>
	<xsl:when test="$type = 'SFVec2f'">float2</xsl:when>
	<xsl:when test="$type = 'SFVec3f'">float3</xsl:when>
	<xsl:when test="$type = 'SFColor'">float3</xsl:when>
	<xsl:when test="$type = 'SFInt32'">int</xsl:when>
	<xsl:when test="$type = 'SFColorRGBA'">float4</xsl:when>
	<xsl:when test="$type = 'SFBool'">bool</xsl:when>
	<xsl:otherwise>unknown</xsl:otherwise>
	</xsl:choose>
	</xsl:function>
	
	<xsl:function name="f:getId" as="xs:string">
		<xsl:param name="xnode" as="element()?"/>
		<xsl:param name="prefix" as="xs:string?" />
		<xsl:choose>
		<xsl:when test="not($xnode)">
		<xsl:sequence select="'none'"/>
		</xsl:when>
		<xsl:when test="$xnode/@DEF">
			<xsl:sequence select="concat($prefix, $xnode/@DEF)"></xsl:sequence>
		</xsl:when>
		<xsl:when test="$xnode/@USE">
			<xsl:sequence select="concat($prefix, $xnode/@USE)"></xsl:sequence>
		</xsl:when>
		<xsl:otherwise>
			<xsl:sequence select="concat($prefix, generate-id($xnode))"></xsl:sequence>
		</xsl:otherwise>
		</xsl:choose>
	</xsl:function>
	
	<xsl:function name="f:getId" as="xs:string">
		<xsl:param name="xnode" as="element()"></xsl:param>
		<xsl:sequence select="f:getId($xnode, '')"></xsl:sequence>
	</xsl:function>

	<!-- Named templates  -->
	<xsl:template name="copyTransformAttributes">
	<xsl:if test="@translation">
		<xsl:copy-of select="cx3d:skipDefault(@translation)"/>
	</xsl:if>
	<xsl:if test="@rotation">
		<xsl:copy-of select="cx3d:skipDefault(@rotation)"/>
	</xsl:if>
	<xsl:if test="@center">
		<xsl:copy-of select="cx3d:skipDefault(@center)"/>
	</xsl:if>
	<xsl:if test="@scaleOrientation">
		<xsl:copy-of select="cx3d:skipDefault(@scaleOrientation)"/>
	</xsl:if>
	<xsl:if test="@scale">
		<xsl:copy-of select="cx3d:skipDefault(@scale)"/>
	</xsl:if>
	</xsl:template>


</xsl:stylesheet>