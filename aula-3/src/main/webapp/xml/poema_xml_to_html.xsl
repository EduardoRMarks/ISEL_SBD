<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" version="4.0" encoding="iso-8859-1" indent="yes"/>
	<xsl:template match="/poema">
		<html>
			<body>
				<xsl:apply-templates select="título"/><br/>
				<xsl:apply-templates select="estrofe"/>
				<xsl:apply-templates select="autor"/>
			</body>
		</html>
	</xsl:template>
	<xsl:template match="título">
<p style="color:#ff0000">
Título: <xsl:value-of select="."/>
</p>
	</xsl:template>
	<xsl:template match="autor">
 <p style="color:#00ff00">
 Autor: <xsl:value-of select="."/>
 </p>
	</xsl:template>
	<xsl:template match="estrofe">
<xsl:apply-templates select="verso"/><br/>
	</xsl:template>
	<xsl:template match="verso">
<p style="color:#003300">
<xsl:value-of select="."/>
</p>
	</xsl:template>
</xsl:stylesheet>

