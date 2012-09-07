<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xml3d="http://www.xml3d.org/2009/xml3d"
	xmlns="http://www.xml3d.org/2009/xml3d" xmlns:f="http://myfunctions.org" xmlns:cx3d="xalan://org.xml3d.converter.x3d.ConverterFunctions"
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:x3d="http://www.web3d.org/specifications/x3d-namespace"
	exclude-result-prefixes="#all" >
	<!-- standard copy template -->
	<xsl:output method="xml" indent="yes" omit-xml-declaration="no"/>


	<xsl:variable name="quot">"</xsl:variable>
	<xsl:variable name="squot">'</xsl:variable>
	<xsl:variable name="useWebGL" select="false()"/>
	<xsl:variable name="useRTFact" select="false()"/>
	<xsl:variable name="useURNs" select="true()"/>
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
	
	<xsl:template match="text()" mode="#all">
		<xsl:value-of select="normalize-space(.)" />
	</xsl:template>

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
	
	<xsl:template name="generateScripts">
	
	<xsl:if test="$useWebGL">
	  <xsl:if test="//Appearance[ImageTexture]">
	   <script id="X3DTexturedPhong-vs" type="x-shader/x-vertex">
	   <xsl:text>
	attribute vec3 position;
	attribute vec3 normal;
	attribute vec2 texcoord;
	varying vec3 fragNormal;
	varying vec3 fragLightVector;
	varying vec3 fragEyeVector;
	varying vec2 fragTexCoord;
	uniform mat4 modelViewProjectionMatrix;
	uniform mat4 modelViewMatrix;
	uniform vec3 lightDirection;
	uniform vec3 eyePosition;
	
	void main(void) {
	    gl_Position = modelViewProjectionMatrix * vec4(position, 1.0);
	    fragNormal = (modelViewMatrix * vec4(normal, 0.0)).xyz;
	    fragLightVector = -lightDirection;
	    fragEyeVector = eyePosition - (modelViewMatrix * vec4(position, 1.0)).xyz;
	    fragTexCoord = texcoord;
	}
		</xsl:text>
	   </script>
	   <script id="X3DTexturedPhong-fs" type="x-shader/x-fragment">
	uniform float ambientIntensity;
	uniform vec3 diffuseColor;
	uniform vec3 emissiveColor;
	uniform float shininess;
	uniform vec3 specularColor;
	uniform float alpha;
	uniform float lightOn;
	uniform sampler2D tex;
	
	varying vec3 fragNormal;
	varying vec3 fragLightVector;
	varying vec3 fragEyeVector;
	varying vec2 fragTexCoord;
	
	void main(void) {
	    vec3 normal = normalize(fragNormal);
	    vec3 light = normalize(fragLightVector);
	    vec3 eye = normalize(fragEyeVector);
	    vec2 texCoord = vec2(fragTexCoord.x,1.0-fragTexCoord.y);
	    float diffuse = max(0.0, dot(normal, light)) * lightOn;
	    diffuse += max(0.0, dot(normal, eye));
	    float specular = pow(max(0.0, dot(normal, normalize(light+eye))), shininess*128.0) * lightOn;
	    specular += pow(max(0.0, dot(normal, normalize(eye))), shininess*128.0);
	    vec3 rgb = emissiveColor + diffuse*texture2D(tex, texCoord).rgb + specular*specularColor;
	    rgb = clamp(rgb, 0.0, 1.0);
	    gl_FragColor = vec4(rgb, texture2D(tex, texCoord).a);
	}
	   </script>
	  </xsl:if>
	  <xsl:if test="//Appearance[Material and not(ImageTexture)]">
	   <script id="X3DPhong-vs" type="x-shader/x-vertex">
	attribute vec3 position;
	attribute vec3 normal;
	varying vec3 fragNormal;
	varying vec3 fragLightVector;
	varying vec3 fragEyeVector;
	uniform mat4 modelViewProjectionMatrix;
	uniform mat4 modelViewMatrix;
	uniform vec3 lightDirection;
	uniform vec3 eyePosition;
	
	void main(void) {
	    gl_Position = modelViewProjectionMatrix * vec4(position, 1.0);
	    fragNormal = (modelViewMatrix * vec4(normal, 0.0)).xyz;
	    fragLightVector = -lightDirection;
	    fragEyeVector = eyePosition - (modelViewMatrix * vec4(position, 1.0)).xyz;
	}
	   </script>
	   <script id="X3DPhong-fs" type="x-shader/x-fragment">
	uniform float ambientIntensity;
	uniform vec3 diffuseColor;
	uniform vec3 emissiveColor;
	uniform float shininess;
	uniform vec3 specularColor;
	uniform float transparency;
	uniform float lightOn;
	
	varying vec3 fragNormal;
	varying vec3 fragLightVector;
	varying vec3 fragEyeVector;
	
	void main(void) {
	    vec3 normal = normalize(fragNormal);
	    vec3 light = normalize(fragLightVector);
	    vec3 eye = normalize(fragEyeVector);
	    float diffuse = max(0.0, dot(normal, light)) * lightOn;
	    diffuse += max(0.0, dot(normal, eye));
	    float specular = pow(max(0.0, dot(normal, normalize(light+eye))), shininess*128.0) * lightOn;
	    specular += pow(max(0.0, dot(normal, normalize(eye))), shininess*128.0);
	    vec3 rgb = emissiveColor + diffuse*diffuseColor + specular*specularColor;
	    rgb = clamp(rgb, 0.0, 1.0);
	    gl_FragColor = vec4(rgb, max(0.0, 1.0 - transparency)); 
	}
	   </script>
	  </xsl:if>
	</xsl:if>
	<xsl:if test="$useRTFact">
	 <script id="X3DPhong">X3DPhong</script>
	 <script id="X3DTexturedPhong">X3DPhong</script>
	 <script id="X3DDefault">X3DPhong</script>
	  <xsl:if test="//PointLight">
	   <script id="X3DPointLight">X3DPointLight</script>
	  </xsl:if>
	  <xsl:if test="//PointLight">
	   <script id="X3DSpotLight">X3DSpotLight</script>
	  </xsl:if>
	  <xsl:if test="//DirectionalLight">
	   <script id="X3DDirectionalLight">X3DDirectionalLight</script>
	  </xsl:if>
	  <xsl:if test="//ORTLightShader[@name='SpotLight']">
	   <script id="ORTSpotLightScript">ORTSpotLight</script>
	  </xsl:if>
	  <xsl:if test="//ORTLightShader[@name='PointLight']">
	   <script id="ORTPointLightScript">ORTPointLight</script>
	  </xsl:if>
	  <xsl:for-each-group select="//ORTGeneralShader" group-by="@name">
	   <script>
	    <xsl:attribute name="id" select="concat('ORT', current-grouping-key())"/>
        <xsl:value-of select="concat('ORT', current-grouping-key())"/>
       </script>
      </xsl:for-each-group>
      </xsl:if>
     
	</xsl:template>
	
	<!-- Match templates  -->
	<xsl:template match="/">
		<xsl:call-template name="startXML3D"/>
	</xsl:template>
	
	<xsl:template name="startXML3D">
		<xml3d xmlns="http://www.xml3d.org/2009/xml3d"
			xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" style="width: 640px; height: 480px"
			xsi:schemaLocation="http://www.xml3d.org/2009/xml3d xml3d.xsd">
			<xsl:if test="//Viewpoint">
				<xsl:attribute name="activeView" select="f:getId(//Viewpoint[1], '#view_')"/>
			</xsl:if>
			<defs>
				<xsl:call-template name="generateScripts"></xsl:call-template>
				<xsl:apply-templates mode="defs" />
			</defs>
			<xsl:for-each select="//Viewpoint">
				<xsl:call-template name="printViewpoint"/>
			</xsl:for-each>
			<xsl:apply-templates mode="sg" />
		</xml3d>
<!--		<script type="text/javascript">    -->
<!--		<xsl:text disable-output-escaping="yes">&lt;![CDATA[</xsl:text>-->
<!--     		<xsl:apply-templates mode="scripts" />-->
<!--    	<xsl:text disable-output-escaping="yes">]]&gt;</xsl:text>-->
<!--		</script>-->
	</xsl:template>

	<xsl:template match="Transform | HAnimJoint[not(@USE)]" mode="defs">
		<transform>
	      <xsl:attribute name="id" select="f:getId(., 't_')"/>
		  <xsl:call-template name="copyTransformAttributes"></xsl:call-template>
		</transform>
	  <xsl:apply-templates mode="defs" />
	</xsl:template>

	<xsl:template match="ORTLightShader" mode="defs">
	<xsl:if test="../@location | ../@direction">
			<transform>
				<xsl:attribute name="id" select="f:getId(.., 't_')"/>
				<xsl:if test="../@location">
					<xsl:attribute name="translation" select="../@location" />
				</xsl:if>
				<!-- <xsl:if test="../@direction">
					<xsl:attribute name="rotation" select="../@direction" />
				</xsl:if> -->
			</transform>
	</xsl:if>
	<lightshader>
		<xsl:attribute name="id" select="f:getId(.., 'ls_')"/>
		<xsl:attribute name="script" select="concat('#ORT', @name, 'Script')"/>
		<xsl:copy-of select="cx3d:parseORTOptions(@options)" />
	</lightshader>
	</xsl:template>
	
	<xsl:template match="PointLight | DirectionalLight | SpotLight"	mode="defs">
		<xsl:if test="@location | @direction">
			<transform>
				<xsl:attribute name="id" select="f:getId(., 't_')"/>
				<xsl:if test="@location">
					<xsl:attribute name="translation" select="@location" />
				</xsl:if>
				<!--  <xsl:if test="@direction">
					<xsl:attribute name="rotation" select="@direction" />
				</xsl:if>-->
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
				<bind semantic="color">
					<float3>
						<xsl:value-of select="@color"></xsl:value-of>
					</float3>
				</bind>
			</xsl:if>
			<xsl:if test="@ambientIntensity">
				<bind semantic="ambientIntensity">
					<float>
						<xsl:value-of select="@ambientIntensity"></xsl:value-of>
					</float>
				</bind>
			</xsl:if>
		</lightshader>
	</xsl:template>

	<xsl:template match="Appearance[not(@USE)]" mode="defs">
		<shader>
			<xsl:attribute name="id" select="f:getId(., 's_')"/>
			<xsl:attribute name="script">
			<xsl:choose>
			<xsl:when test="./Material and not(./ImageTexture)">
			<xsl:text>urn:xml3d:shader:phong</xsl:text>
			</xsl:when>
			<xsl:when test="./Material and ./ImageTexture">
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
	
	<xsl:template match="PackagedShader/field" mode="defs"> 
		<bind>
		<xsl:attribute name="semantic" select="@name"/>
		<xsl:element name="{f:toXML3DType(@type)}"><xsl:value-of select="@value"/></xsl:element>
		</bind>
	</xsl:template>

	<xsl:template match="ORTAppearance" mode="defs">
		<shader>
			<xsl:attribute name="id" select="f:getId(., 's_')"/>
			<xsl:attribute name="script"><xsl:value-of select="concat('#ORT', ./ORTGeneralShader/@name)"/></xsl:attribute>
			<xsl:copy-of select="cx3d:mergeORTShaderParameters(.)" />
			<xsl:apply-templates mode="defs" select="child::node()[not(self::Material)]" />
		</shader>
	</xsl:template>

	<xsl:template match="ImageTexture" mode="defs">
	  <bind semantic="diffuseTexture">
	    <texture>
	      <xsl:attribute name="wrapS" select="if (@repeatS = 'true') then 'repeat' else 'clamp'"/>
	      <xsl:attribute name="wrapT" select="if (@repeatT = 'true') then 'repeat' else 'clamp'"/>
		  <img>
			<xsl:attribute name="src" select="cx3d:getHTTPMFString(@url)"/>
		  </img>
		</texture>
	  </bind>
	</xsl:template>
	
	<xsl:template match="Material[@USE]" mode="defs">
		<xsl:apply-templates select="//Material[@DEF=current()/@USE]" mode="defs"/>
	</xsl:template>
	
	<xsl:template match="Material" mode="defs">
		<bind semantic="diffuseColor">
			<float3>
				<xsl:value-of select="if (@diffuseColor) then @diffuseColor else '0.8 0.8 0.8'"></xsl:value-of>
			</float3>
		</bind>
		<bind semantic="specularColor">
			<float3>
				<xsl:value-of select="if (@specularColor) then @specularColor else '0 0 0'"></xsl:value-of>
			</float3>
		</bind>
		<bind semantic="emissiveColor">
			<float3>
				<xsl:value-of select="if (@emissiveColor) then @emissiveColor else '0 0 0'"></xsl:value-of>
			</float3>
		</bind>
		<bind semantic="transparency">
			<float>
				<xsl:value-of select="if (@transparency) then @transparency else '0'"></xsl:value-of>
			</float>
		</bind>
		<bind semantic="shininess">
			<float>
				<xsl:value-of select="if (@shininess) then (@shininess*128) else '25'"></xsl:value-of>
			</float>
		</bind>
		<bind semantic="ambientIntensity">
			<float>
				<xsl:value-of select="if (@ambientIntensity) then @ambientIntensity else '0.2'"></xsl:value-of>
			</float>
		</bind>
	</xsl:template>

	<xsl:template match="Scene" mode="sg">
		<group id="root">
			<xsl:apply-templates mode="sg" />
		</group>
	</xsl:template>

	<xsl:template match="Transform | HAnimJoint[not(@USE)]" mode="sg">
		<group>
				<xsl:if test="@DEF">
					<xsl:attribute name="id" select="@DEF" />
				</xsl:if>
					<xsl:attribute name="transform" select="f:getId(., '#t_')"/>
				<xsl:apply-templates mode="sg"/>
			
		</group>
	</xsl:template>
	
	<xsl:template match="Transform[@USE]" mode="sg">
		<xsl:comment>Reuse of X3DGroupingNodes not supported yet.</xsl:comment>
	</xsl:template>
	
	<xsl:template match="Collision | Group" mode="sg">
		<group>
			<xsl:if test="@DEF">
				<xsl:attribute name="id" select="@DEF" />
			</xsl:if>
			<xsl:apply-templates mode="sg" />
		</group>
	</xsl:template>

	<xsl:template match="Shape" mode="sg">
		<group>
			<xsl:attribute name="id" select="f:getId(., 'shape_')"/>
			<xsl:choose>
			<xsl:when test="./Appearance">
				<xsl:attribute name="shader" select="f:getId(./Appearance, '#s_')"/>
			</xsl:when>
			<xsl:when test="./ORTAppearance">
				<xsl:attribute name="shader" select="f:getId(./ORTAppearance, '#s_')"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:attribute name="style" select="'color: white;'"/>
			</xsl:otherwise>
			</xsl:choose>
			<xsl:apply-templates mode="sg" />
		</group>
	</xsl:template>
	
	<xsl:template match="Shape[@USE]" mode="sg">
		<group>
			<xsl:apply-templates mode="reference" select="//Shape[@DEF=current()/@USE]/child::node()"/>
		</group>
	</xsl:template>

	<xsl:template match="Appearance" mode="reference">
			<xsl:attribute name="shader" select="f:getId(., '#s_')"/>
	</xsl:template>
	
	<xsl:template match="IndexedFaceSet | Box | Cylinder | Sphere" mode="reference">
		<use>
			<xsl:attribute name="href" select="f:getId(., '#m_')"/>
		</use> 
	</xsl:template>
	
	<xsl:template match="IndexedFaceSet" mode="sg">
	<mesh type="triangles">
		<xsl:attribute name="id" select="f:getId(., 'm_')"/>
	
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

	
	<xsl:template match="Box" mode="sg">
		<mesh type="triangles">
			<xsl:attribute name="id" select="f:getId(., 'm_')"/>
			<xsl:choose>
				<xsl:when test="@size">
					<xsl:copy-of select="cx3d:createBox(@size)" />
				</xsl:when>
			<xsl:otherwise>
					<xsl:copy-of select="cx3d:createBox()" />
			</xsl:otherwise>
			</xsl:choose>
		</mesh>
	</xsl:template>
	
	<xsl:template match="Sphere" mode="sg">
		<mesh type="triangles">
			<xsl:attribute name="id" select="f:getId(., 'm_')"/>
			<xsl:copy-of select="cx3d:createSphere(@radius)" />
		</mesh>
	</xsl:template>

	<xsl:template match="Cylinder" mode="sg">
		<mesh type="triangles">
			<xsl:attribute name="id" select="f:getId(., 'm_')"/>
			<xsl:copy-of select="cx3d:createCylinder(.)" />
		</mesh>
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
