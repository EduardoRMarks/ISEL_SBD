<?xml version="1.0" encoding="UTF-8"?>
<!-- mostar uma fotografia  -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema">
	<xsl:output method="html" encoding="ISO-8859-1" indent="yes"/>
	<xsl:template match="/fotografia">
		<html>
			<head>
			<title>Fotografia</title>
			</head>
			<body>
				<h3>Título: <xsl:value-of select="título/text()"/></h3>
				<p><img title='Imagem do tipo {tipo/text()}' src='data:{tipo/text()};base64,{conteudo/text()}' /></p>
				<h3>Autor: <xsl:value-of select="autor/text()"/></h3>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>



