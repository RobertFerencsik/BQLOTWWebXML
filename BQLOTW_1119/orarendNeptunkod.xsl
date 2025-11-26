<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" encoding="UTF-8" indent="yes"/>

    <xsl:template match="/">
        <html>
            <head>
                <meta charset="utf-8"/>
                <title>Órarend</title>
                <style>
                    body{font-family:Arial,Helvetica,sans-serif;margin:20px}
                    table{border-collapse:collapse;width:100%}
                    th,td{border:1px solid #666;padding:8px;text-align:left}
                    th{background:#f0f0f0}
                    .type-gyakorlat{background:#f9f9ff}
                </style>
            </head>
            <body>
                <h1>Órarend</h1>
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Típus</th>
                            <th>Tárgy</th>
                            <th>Nap</th>
                            <th>Idő</th>
                            <th>Helyszín</th>
                            <th>Oktató</th>
                            <th>Szak</th>
                        </tr>
                    </thead>
                    <tbody>
                        <xsl:for-each select="BQLOTW_orarend/ora">
                            <tr>
                                <xsl:attribute name="class">
                                    <xsl:choose>
                                        <xsl:when test="translate(@típus,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='gyakorlat'">type-gyakorlat</xsl:when>
                                        <xsl:otherwise/>
                                    </xsl:choose>
                                </xsl:attribute>
                                <td><xsl:value-of select="@id"/></td>
                                <td><xsl:value-of select="@típus"/></td>
                                <td><xsl:value-of select="targy"/></td>
                                <td><xsl:value-of select="idopont/nap"/></td>
                                <td>
                                    <xsl:value-of select="idopont/tol"/>
                                    <xsl:text> - </xsl:text>
                                    <xsl:value-of select="idopont/ig"/>
                                </td>
                                <td><xsl:value-of select="helyszin"/></td>
                                <td><xsl:value-of select="oktato"/></td>
                                <td><xsl:value-of select="szak"/></td>
                            </tr>
                        </xsl:for-each>
                    </tbody>
                </table>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>
