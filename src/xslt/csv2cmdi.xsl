<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:cmd="http://www.clarin.eu/cmd/"
    xmlns:lat="http://lat.mpi.nl/"
    xmlns:csv="https://di.huc.knuw.nl/ns/csv"
    exclude-result-prefixes="xs csv"
    version="2.0">
    
    <xsl:param name="cvsdir" select="'.'"/>
    
    <xsl:variable name="coll" select="/cmd:CMD/cmd:Components/cmd:MeertensCollection/cmd:CoreCollectionInformation/cmd:collectionID"/>
    
    <xsl:variable name="CSV" select="concat($cvsdir,'/',$coll,'.csv')"/>
    
    <xsl:template match="node() | @*">
        <xsl:copy>
            <xsl:apply-templates select="node() | @*"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:function name="csv:getTokens" as="xs:string+">
        <xsl:param name="str" as="xs:string"/>
        <xsl:analyze-string select="concat($str, ',')" regex='(("[^"]*")+|[^,]*),'>
            <xsl:matching-substring>
                <xsl:sequence select='replace(regex-group(1), "^""|""$|("")""", "$1")'/>
            </xsl:matching-substring>
        </xsl:analyze-string>
    </xsl:function>
    
    <xsl:template match="cmd:ResourceProxyList">
        <xsl:copy>
        <xsl:apply-templates select="node() | @*"/>
            <xsl:choose>
                <xsl:when test="unparsed-text-available($CSV)">
                    <xsl:variable name="tab" select="unparsed-text($CSV)"/>
                    <xsl:variable name="lines" select="tokenize($tab, '(\r)?\n')" as="xs:string+"/>
                    <xsl:variable name="elemNames" select="('path','md5')" as="xs:string+"/>
                    <xsl:for-each select="$lines[normalize-space(.) != '']">
                        <xsl:variable name="line" select="position()"/>
                        <!-- 
    <cmd:ResourceProxy xmlns:lat="http://lat.mpi.nl/" id="H0" lat:label="FILE.EXT">
        <cmd:ResourceType mimetype="">Resource</cmd:ResourceType>
        <cmd:ResourceRef>/data2/DigitaleOnderzoekscollecties/1058_VPRO_Documentaire_Niets_mag_onopgemerkt_blijven_Alexander_van_der_Meer_1998/PATH/FILE.EXT</cmd:ResourceRef>
    </cmd:ResourceProxy>
                        -->
                        
                        <xsl:variable name="lineItems" select="csv:getTokens(.)" as="xs:string+"/>
                        <xsl:if test="count($lineItems)!=count($elemNames)">
                            <xsl:message terminate="yes">ERR: CSV[<xsl:value-of select="$CSV"/>] line[<xsl:value-of select="$line"/>] has [<xsl:value-of select="count($lineItems)"/>] cells, but the header indicates that [<xsl:value-of select="count($elemNames)"/>] cells are expected!</xsl:message>
                        </xsl:if>
                        <xsl:variable name="fileName" select="replace($lineItems[1], '.*/','')" as="xs:string"/>
                        <xsl:variable name="path" select="replace($lineItems[1],$fileName,'')" as="xs:string"/>                        
                        <cmd:ResourceProxy id="H{$line}" lat:path="{$path}" lat:label="{$fileName}" lat:md5="{$lineItems[2]}">
                             <cmd:ResourceType>Resource</cmd:ResourceType>
                             <cmd:ResourceRef>
                                 <xsl:value-of select="$lineItems[1]"/>
                             </cmd:ResourceRef>   
                        </cmd:ResourceProxy>
                    </xsl:for-each>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:message terminate="yes">ERR: couldn't load CSV[<xsl:value-of select="$CSV"/>]!</xsl:message>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:copy>
    </xsl:template>
    
</xsl:stylesheet>