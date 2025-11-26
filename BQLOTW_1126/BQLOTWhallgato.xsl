<?xml version="1.0" encoding="UTF-8"?>
<?xml-stylesheet type="text/xsl" href="BQLOTWhallgato.xml"?>

<xsl:stylesheet version="1.0"
      xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="html" encoding="UTF-8" indent="yes"/>

    <xsl:template match="/">
        <html>
            <head>
                <meta charset="utf-8"/>
                <title>Hallgatók</title>
                <style>
                    body{font-family:Arial,Helvetica,sans-serif;margin:20px}
                    table{border-collapse:collapse;width:100%}
                    th,td{border:1px solid #666;padding:8px;text-align:left}
                    th{background:#f0f0f0}
                    .high-osztondij{background:#fffbdd}
                </style>
            </head>
            <body>
                <h1>Hallgatók</h1>
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Név</th>
                            <th>Becenév</th>
                            <th>Kor</th>
                            <th>Ösztöndíj</th>
                        </tr>
                    </thead>
                    <tbody>
                        <xsl:for-each select="hallgatok/hallgato">
                            <xsl:variable name="osz" select="number(osztondij)"/>

                            <tr>
                                <xsl:if test="$osz &gt;= 20000">
                                    <xsl:attribute name="class">high-osztondij</xsl:attribute>
                                </xsl:if>

                                <td><xsl:value-of select="@id"/></td>
                                <td><xsl:value-of select="concat(vezeteknev, ' ', keresztnev)"/></td>
                                <td><xsl:value-of select="becenev"/></td>
                                <td><xsl:value-of select="kor"/></td>
                                <td><xsl:value-of select="osztondij"/></td>
                            </tr>
                        </xsl:for-each>
                    </tbody>
                </table>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>
