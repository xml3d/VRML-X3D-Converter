<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xml3d="http://www.xml3d.org/2009/xml3d"
	xmlns="http://www.xml3d.org/2009/xml3d" xmlns:f="http://myfunctions.org" xmlns:cx3d="xalan://org.xml3d.converter.x3d.ConverterFunctions"
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:x3d="http://www.web3d.org/specifications/x3d-namespace"
	exclude-result-prefixes="#all" >
	<!-- standard copy template -->
	<xsl:output method="xml" indent="yes" omit-xml-declaration="no"/>

	<xsl:include href="convertFunctions.xsl"/>
	
	<xsl:template match="text()" mode="#all">
		<xsl:value-of select="normalize-space(.)" />	<!--Removes unnecessary spaces--><!---->
	</xsl:template>

	<!-- Match templates  -->
	<xsl:template match="/">
		<xml3d xmlns="http://www.xml3d.org/2009/xml3d" style="width: 640px; height: 480px; background-color: grey;" >
			<xsl:if test="//Viewpoint">
				<xsl:attribute name="activeView" select="f:getId(//Viewpoint[1], '#view_')"/>
			</xsl:if>
			<xsl:if test="not(//Viewpoint)">
				<xsl:attribute name="activeView" select="'#defaultView'"/>
			</xsl:if>
			<defs>
				<xsl:apply-templates mode="defs" />
			</defs>
			<xsl:if test="not(//Viewpoint)">
				<view id="defaultView"/>
			</xsl:if>
			<xsl:for-each select="//Viewpoint">
				<xsl:call-template name="printViewpoint"/>
			</xsl:for-each>
			<xsl:apply-templates mode="sg" />
		</xml3d>
		<xsl:if test="//OrientationInterpolator | //PositionInterpolator">
			<script type="text/javascript">
			<xsl:text disable-output-escaping="yes">&lt;![CDATA[</xsl:text>
			<xsl:apply-templates mode="scripts" />
		<xsl:text disable-output-escaping="yes">]]&gt;</xsl:text>
			</script>
		</xsl:if>
	</xsl:template>


	<!-- def section -->

	<xsl:template match="Transform[not(@USE)] | HAnimJoint[not(@USE)]" mode="defs">
		<transform>
	      <xsl:attribute name="id" select="f:getId(., 't_')"/>
		  <xsl:call-template name="copyTransformAttributes"></xsl:call-template>
		</transform>
	  <xsl:apply-templates mode="defs" />
	</xsl:template>

	<xsl:template match="PointLight | DirectionalLight | SpotLight"	mode="defs">
		<xsl:if test="@location | @direction">
			<transform>
				<xsl:attribute name="id" select="f:getId(., 't_')"/>
				<xsl:if test="@location">
					<xsl:attribute name="translation" select="@location" />
				</xsl:if>
			</transform>
		</xsl:if>
		<lightshader>
		  <xsl:attribute name="id" select="f:getId(., 'ls_')"/>
		  <xsl:choose>
		  <xsl:when test="name() = 'PointLight'">
		  	<xsl:attribute name="script">urn:xml3d:lightshader:point</xsl:attribute>
		  </xsl:when>
		  <xsl:when test="name() = 'DirectionalLight'">
		  	<xsl:attribute name="script">urn:xml3d:lightshader:directional</xsl:attribute>
		  </xsl:when>
		  <xsl:when test="name() = 'SpotLight'">
		  	<xsl:attribute name="script">urn:xml3d:lightshader:spot</xsl:attribute> 
		  </xsl:when>
		  </xsl:choose>
		  <xsl:if test="@color">
					<float3 name="intensity">
						<xsl:value-of select="@color"></xsl:value-of>
					</float3>
			</xsl:if>

		</lightshader>
	</xsl:template>

	<xsl:template match="Appearance[not(@USE)]" mode="defs">
		<shader>
			<xsl:attribute name="id" select="f:getId(., 's_')"/>
			<xsl:attribute name="script">
			<xsl:choose>
			<xsl:when test="./Material">
			<xsl:text>urn:xml3d:shader:phong</xsl:text>
			</xsl:when>
			<xsl:otherwise>
			<xsl:text>urn:xml3d:shader:matte</xsl:text>
			</xsl:otherwise>
			</xsl:choose>
			</xsl:attribute>
			<xsl:apply-templates mode="defs" />
		</shader>
	</xsl:template>
	
	<xsl:template match="ImageTexture" mode="defs">
		<xsl:variable name="url" select="if(@USE) then //ImageTexture[@DEF=current()/@USE]/@url else @url"></xsl:variable>
	    <texture name="diffuseTexture">
	      <xsl:attribute name="wrapS" select="if (@repeatS = 'false') then 'clamp' else 'repeat'"/>
	      <xsl:attribute name="wrapT" select="if (@repeatT = 'false') then 'clamp' else 'repeat'"/>
		  <img>
			<xsl:attribute name="src" select="cx3d:getHTTPMFString($url)"/>
		  </img>
		</texture>
	</xsl:template>
	
	<xsl:template match="Material[@USE]" mode="defs">
		<xsl:apply-templates select="//Material[@DEF=current()/@USE]" mode="defs"/>
	</xsl:template>
	
	<xsl:template match="Material" mode="defs">
			<float3 name="diffuseColor">
				<xsl:value-of select="if (@diffuseColor) then @diffuseColor else '0.8 0.8 0.8'"></xsl:value-of>
			</float3>
			<float3 name="specularColor">
				<xsl:value-of select="if (@specularColor) then @specularColor else '0 0 0'"></xsl:value-of>
			</float3>
			<float3 name="emissiveColor">
				<xsl:value-of select="if (@emissiveColor) then @emissiveColor else '0 0 0'"></xsl:value-of>
			</float3>
			<float name="transparency">
				<xsl:value-of select="if (@transparency) then @transparency else '0'"></xsl:value-of>
			</float>
			<float name="shininess">
				<xsl:value-of select="if (@shininess) then (@shininess) else '0.2'"></xsl:value-of>
			</float>
			<float name="ambientIntensity">
				<xsl:value-of select="if (@ambientIntensity) then @ambientIntensity else '0.2'"></xsl:value-of>
			</float>
	</xsl:template>

	<xsl:template match="IndexedFaceSet[not(@USE)]" mode="defs">
		<data>
			<xsl:attribute name="id" select="f:getId(., 'data_')"/>
			<xsl:choose>
				<xsl:when test="./Coordinate/@USE">
					<xsl:copy-of select="cx3d:convertIFS(., //Coordinate[@DEF=current()/Coordinate/@USE])" />
				</xsl:when>
				<xsl:when test="./GeoCoordinate">
					<xsl:copy-of select="cx3d:convertIFS(., ./GeoCoordinate)" />
				</xsl:when>
				<xsl:when test="./Coordinate">
					<xsl:copy-of select="cx3d:convertIFS(., ./Coordinate)" />
				</xsl:when>
			</xsl:choose>
		</data>
	</xsl:template>

	<xsl:template match="IndexedLineSet[not(@USE)]" mode="defs">
		<data>
			<xsl:attribute name="id" select="f:getId(., 'data_')"/>
			<xsl:choose>
				<xsl:when test="./Coordinate/@USE">
					<xsl:copy-of select="cx3d:convertILS(., //Coordinate[@DEF=current()/Coordinate/@USE])" />
				</xsl:when>
				<xsl:when test="./GeoCoordinate">
					<xsl:copy-of select="cx3d:convertILS(., ./GeoCoordinate)" />
				</xsl:when>
				<xsl:when test="./Coordinate">
					<xsl:copy-of select="cx3d:convertILS(., ./Coordinate)" />
				</xsl:when>
			</xsl:choose>
		</data>
	</xsl:template>

	<xsl:template match="Box[not(@USE)]" mode="defs">
		<data>
			<xsl:attribute name="id" select="f:getId(., 'data_')"/>
			<xsl:copy-of select="cx3d:createBox(@size)" />
		</data>
	</xsl:template>

	<xsl:template match="Sphere[not(@USE)]" mode="defs">
		<data>
			<xsl:attribute name="id" select="f:getId(., 'data_')"/>
			<xsl:copy-of select="cx3d:createSphere(@radius)" />
		</data>
	</xsl:template>

	<xsl:template match="Cylinder[not(@USE)]" mode="defs">
		<data>
			<xsl:attribute name="id" select="f:getId(., 'data_')"/>
			<xsl:copy-of select="cx3d:createCylinder(.)" />
		</data>
	</xsl:template>

	<!-- scenegraph section -->

	<xsl:template match="Scene" mode="sg">
		<group id="root">
			<xsl:apply-templates mode="sg" />
		</group>
	</xsl:template>

	<xsl:template match="Transform | HAnimJoint[not(@USE)] | Group" mode="sg">
		<xsl:param name="depth" required="no">0</xsl:param>
		<group>
			<xsl:if test="@DEF">
				<xsl:attribute name="id" select="if($depth > 0) then concat(@DEF,'_',$depth) else @DEF" />
			</xsl:if>
			<xsl:if test="local-name(.)!='Group'">
				<xsl:attribute name="transform" select="f:getId(., '#t_')"/>
			</xsl:if>
			<xsl:apply-templates mode="sg">
				<xsl:with-param name="depth" select="$depth"/>
			</xsl:apply-templates>
		</group>
	</xsl:template>
	
	<xsl:template match="Transform[@USE] | Group[@USE]" mode="sg">
		<xsl:param name="depth">0</xsl:param>
		<xsl:apply-templates select="//node()[@DEF=current()/@USE]" mode="sg">
			<xsl:with-param name="depth" select="$depth + 1"></xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	
	<xsl:template match="Collision | Group[not(@USE)]" mode="sg">
		<group>
			<xsl:if test="@DEF">
				<xsl:attribute name="id" select="@DEF" />
			</xsl:if>
			<xsl:apply-templates mode="sg" />
		</group>
	</xsl:template>

	<xsl:template match="Shape[@USE]" mode="sg">
		<xsl:param name="depth" required="no">0</xsl:param>
		<group>
			<xsl:variable name="id" select="f:getId(., 'use_shape_')"></xsl:variable>
			<xsl:attribute name="id" select="if($depth > 0) then concat($id,'_',$depth) else $id"/>
			<xsl:apply-templates mode="sg" select="//Shape[@DEF=current()/@USE]/child::node()"/>
		</group>
	</xsl:template>

	<xsl:template match="Shape" mode="sg">
		<xsl:param name="depth" required="no">0</xsl:param>
		<group>
			<xsl:variable name="id" select="f:getId(., 'shape_')"></xsl:variable>
			<xsl:attribute name="id" select="if($depth > 0) then concat($id,'_',$depth) else $id"/>
			<xsl:for-each select="./Appearance">
				<xsl:attribute name="shader" select="f:getId(., '#s_')"/>
			</xsl:for-each>
			<xsl:apply-templates mode="sg" />
		</group>
	</xsl:template>

	<!--  <xsl:template match="Appearance" mode="sg">
			
				<xsl:attribute name="shader" select="f:getId(., '#s_')"/>
			
	</xsl:template>-->
	
	<xsl:template match="IndexedFaceSet | Box | Cylinder | Sphere" mode="sg">
		<mesh type="triangles">
			<xsl:attribute name="src" select="f:getId(., '#data_')"/>
		</mesh>
	</xsl:template>
	
	<xsl:template match="IndexedLineSet" mode="sg">
		<mesh type="lineStrips">
			<xsl:attribute name="src" select="f:getId(., '#data_')"/>
		</mesh>
	</xsl:template>


	<xsl:template match="PointLight | DirectionalLight | SpotLight
	| ORTPointLight | ORTSpotLight"
		mode="sg">
		<xsl:choose>
			<xsl:when test="@location | @direction">
				<group>
					<xsl:attribute name="transform" select="f:getId(., '#t_')"/>
					<xsl:call-template name="inLight" />
				</group>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="inLight" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="inLight">
		<light>
			<xsl:if test="@DEF">
				<xsl:attribute name="id" select="@DEF" />
			</xsl:if>
			<xsl:if test="@on">
				<xsl:attribute name="visible" select="@on" />
			</xsl:if>
			<xsl:copy-of select="@intensity" />
			<xsl:attribute name="shader" select="f:getId(., '#ls_')"/>
		</light>
	</xsl:template>

	

	<xsl:template name="printViewpoint">
		<view>
			<xsl:attribute name="id" select="f:getId(., 'view_')"/>
			<xsl:if test="@position">
			<xsl:copy-of select="@position"/>
			</xsl:if>
			<xsl:if test="@orientation">
			<xsl:copy-of select="@orientation"/>
			</xsl:if>
			<xsl:if test="@fieldOfView">
			<xsl:copy-of select="@fieldOfView"/>
			</xsl:if>
		</view>
	</xsl:template>
	
	<xsl:template match="OrientationInterpolator | PositionInterpolator" mode="sg">
	<xsl:variable name="transName" select="//ROUTE[@fromNode=current()/@DEF]/@toNode" />
	<xsl:variable name="transField" select="//ROUTE[@fromNode=current()/@DEF]/@toField" />
	<xsl:element name="{concat('x3d:', name())}">
	<xsl:attribute name="id" select="lower-case(@DEF)"/>
	<!--  <xsl:attribute name="target" select="concat('id(', $squot, 't_', $transName, $squot, ')/@', $transField)"/>  -->
	<xsl:copy-of select="@key"></xsl:copy-of>
	<xsl:copy-of select="@keyValue"></xsl:copy-of>
	</xsl:element>
	</xsl:template>

	<xsl:template match="OrientationInterpolator | PositionInterpolator" mode="scripts">
	<xsl:variable name="transName" select="//ROUTE[@fromNode=current()/@DEF]/@toNode" />
	<xsl:variable name="transField" select="//ROUTE[@fromNode=current()/@DEF]/@toField" />
	org.xml3d.startAnimation("<xsl:value-of select="lower-case(@DEF)"/>", "t_<xsl:value-of select="$transName"/>", "<xsl:value-of select="$transField"/>");</xsl:template>
    
</xsl:stylesheet>
