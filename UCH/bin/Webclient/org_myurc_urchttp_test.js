/*
 Copyright: Access Technologies Group, Germany, 2007-2009.  
 This software is licensed under the CC-GNU GPL.  See http://creativecommons.org/licenses/GPL/2.0/ for 
 human-readable Commons Deed, lawyer-readable legal code, and machine-readable digital code.

 ***********************************************************************************

 Test cases for org_myurc_urchttp.

 Imports: 
   -
      
 List of exported functions:
   org_myurc_urchttp_test_getData: Get the test data for the given URC-HTTP method.  If there is multiple data sets for the same method, 

 Current limitations:
   * Test cases do not cover labels.  That means that labels are displayed as "{id}".
   
 Author: Gottfried Zimmermann, ATG
 Last modified: 2009-08-07

 **********************************************************************************/

/** TODO
*/

if (location.protocol == "file:" ||    // Read this script if file is retrieved from local file system
      (location.protocol == "http:" && location.port == "1631")) {    // ... or if Visual Studio debugger used.
   org_myurc_urchttp_test = true;  
   org_myurc_urchttp_test_tv_iteration = 1;
   org_myurc_urchttp_test_testcase1_iteration = 1;
   org_myurc_urchttp_test_testcase2_iteration = 1;
   

   /********************************************************
   org_myurc_urchttp_test_getData: Get the test data for the given URC-HTTP method.  If there is multiple data sets for the 
      same method, they will be used in a round-robin fashion.
   url: URL for retrieving the uiList
   postData: POST data as string or document for method "POST" / null for method "GET"
   return: string with test data.
   */
   org_myurc_urchttp_test_getData = function(url, postData) {

      var messageParameters = org_myurc_urchttp_test_getMessageParameters(url);
      switch (messageParameters.type) {

         case "GetCompatibleUIs":
            return(
               '  <uilist>\n' +
               '     <ui>\n' +
               '        <uiID>target 1</uiID>\n' +
               '        <name>Air conditioning device</name>\n' +
               '        <protocol shortName="URC-HTTP">\n' +
               '           <uri>http://[::1]/UCH/ac</uri>\n' +
               '           <protocolInfo>\n' +
               '              <targetName>http://res.myurc.org/actarget</targetName>\n' +
               '              <socketName friendlyName="Basic controls">http://res.myurc.org/ac</socketName>\n' +
               '              <socketDescriptionAt>http://localhost:1525/ac/ac.uis</socketDescriptionAt>\n' +
               '              <targetDescriptionAt>http://[::1]/ac/ac.td</targetDescriptionAt>\n' +
               '           </protocolInfo>\n' +
               '        </protocol>\n' +
               '     </ui>\n' +
               '     <ui>\n' +
               '        <uiID>target 1a</uiID>\n' +
               '        <name>Air conditioning device</name>\n' +
               '        <protocol shortName="URC-HTTP">\n' +
               '           <uri>http://[::1]/UCH/aca</uri>\n' +
               '           <protocolInfo>\n' +
               '              <targetName>http://res.myurc.org/actarget</targetName>\n' +
               '              <socketName>http://res.myurc.org/ac</socketName>\n' +
               '              <socketDescriptionAt>http://localhost:1525/ac/ac.uis</socketDescriptionAt>\n' +
               '              <targetDescriptionAt>http://[::1]/ac/ac.td</targetDescriptionAt>\n' +
               '           </protocolInfo>\n' +
               '        </protocol>\n' +
               '     </ui>\n' +
               '     <ui>\n' +
               '        <uiID>target 2</uiID>\n' +
               '        <name>TV</name>\n' +
               '        <protocol shortName="URC-HTTP">\n' +
               '           <uri>http://[::1]/UCH/tv</uri>\n' +
               '           <protocolInfo>\n' +
               '              <targetName>http://res.myurc.org/tvdevice</targetName>\n' +
               '              <socketName>http://res.myurc.org/tv</socketName>\n' +
               '              <socketDescriptionAt>http://[::1]/tv/tv.uis</socketDescriptionAt>\n' +
               '              <targetDescriptionAt>http://[::1]/tv/tv.td</targetDescriptionAt>\n' +
               '           </protocolInfo>\n' +
               '        </protocol>\n' +
               '     </ui>\n' +
               '     <ui>\n' +
               '        <uiID>target 3</uiID>\n' +
               '        <name>EPG</name>\n' +
               '        <protocol shortName="URC-HTTP">\n' +
               '           <uri>http://[::1]/UCH/tv_epg</uri>\n' +
               '           <protocolInfo>\n' +
               '              <targetName>http://res.myurc.org/tv_epg_target</targetName>\n' +
               '              <socketName>http://res.myurc.org/tv_epg</socketName>\n' +
               '              <socketDescriptionAt>http://[::1]/tv_epg/tv_epg.uis</socketDescriptionAt>\n' +
               '              <targetDescriptionAt>http://[::1]/tv_epg/tv_epg.td</targetDescriptionAt>\n' +
               '           </protocolInfo>\n' +
               '        </protocol>\n' +
               '     </ui>\n' +
               '     <ui>\n' +
               '        <uiID>target 3a</uiID>\n' +
               '        <name>Satellite Box Test Data</name>\n' +
               '        <protocol shortName="URC-HTTP">\n' +
               '           <uri>http://[::1]/UCH/satelliteBox</uri>\n' +
               '           <protocolInfo>\n' +
               '              <targetName>http://res.myurc.org/upnp/demo/satelliteBox</targetName>\n' +
               '              <socketName>http://res.myurc.org/upnp/demo/satelliteBox/socket</socketName>\n' +
               '              <socketDescriptionAt>http://[::1]/satelliteBox/satellite_box.uis</socketDescriptionAt>\n' +
               '              <targetDescriptionAt>http://[::1]/satelliteBox/satellite_box.td</targetDescriptionAt>\n' +
               '           </protocolInfo>\n' +
               '        </protocol>\n' +
               '     </ui>\n' +
               '     <ui>\n' +
               '        <uiID>target 4</uiID>\n' +
               '        <name>Testcase 1</name>\n' +
               '        <protocol shortName="URC-HTTP">\n' +
               '           <uri>http://[::1]/test/testcase1</uri>\n' +
               '           <protocolInfo>\n' +
               '              <targetName>http://example.com/test/testcase1</targetName>\n' +
               '              <socketName>http://example.com/test/testcase1/socket</socketName>\n' +
               '              <socketDescriptionAt>http://[::1]/test/testcase1.uis</socketDescriptionAt>\n' +
               '              <targetDescriptionAt>http://[::1]/test/testcase1.td</targetDescriptionAt>\n' +
               '           </protocolInfo>\n' +
               '        </protocol>\n' +
               '     </ui>\n' +
               '     <ui>\n' +
               '        <uiID>target 5</uiID>\n' +
               '        <name>Testcase 2</name>\n' +
               '        <protocol shortName="URC-HTTP">\n' +
               '           <uri>http://[::1]/test/testcase2</uri>\n' +
               '           <protocolInfo>\n' +
               '              <targetName>http://example.com/test/testcase2</targetName>\n' +
               '              <socketName>http://example.com/test/testcase2/socket</socketName>\n' +
               '              <socketDescriptionAt>http://[::1]/test/testcase2.uis</socketDescriptionAt>\n' +
               '              <targetDescriptionAt>http://[::1]/test/testcase2.td</targetDescriptionAt>\n' +
               '           </protocolInfo>\n' +
               '        </protocol>\n' +
               '     </ui>\n' +
               '     <ui>\n' +
               '        <uiID>UPnP-UES</uiID>\n' +
               '        <name>UES</name>\n' +
               '        <protocol shortName="URC-HTTP">\n' +
               '            <uri>http://[::1]/UCH/urchttp/UPnP-UES/level1socket</uri>\n' +
               '            <protocolInfo>\n' +
               '                <socketName>http://res.myurc.org/upnp/av/play-1.uis</socketName>\n' +
               '                <targetDescriptionAt>http://[::1]/UCH/upnpav/upnpentertainmentsystem.td</targetDescriptionAt>\n' +
               '                <conformsTo>http://myurc.org/TR/urc-http-protocol-20080627 </conformsTo>\n' +
               '                <targetId>UPnP-UES</targetId>\n' +
               '                <socketDescriptionAt>http://[::1]/UCH/upnpav/play-1.uis</socketDescriptionAt>\n' +
               '                <targetName>http://res.myurc.org/upnp/av</targetName>\n' +
               '            </protocolInfo>\n' +
               '        </protocol>\n' +
               '        <protocol shortName="URC-HTTP">\n' +
               '            <uri>http://[::1]/UCH/urchttp/UPnP-UES/level2socket</uri>\n' +
               '            <protocolInfo>\n' +
               '                <socketName>http://res.myurc.org/upnp/av/play-2.uis</socketName>\n' +
               '                <targetDescriptionAt>http://[::1]/UCH/upnpav/upnpentertainmentsystem.td</targetDescriptionAt>\n' +
               '                <conformsTo>http://myurc.org/TR/urc-http-protocol-20080627 </conformsTo>\n' +
               '                <targetId>UPnP-UES</targetId>\n' +
               '                <socketDescriptionAt>http://[::1]/UCH/upnpav/play-2.uis</socketDescriptionAt>\n' +
               '                <targetName>http://res.myurc.org/upnp/av</targetName>\n' +
               '            </protocolInfo>\n' +
               '        </protocol>\n' +
               '    </ui>\n' +
               '      <ui>\n' +
               '      <uiID>MCETA</uiID>\n' +
               '      <name>Vista Media Center Edition</name>\n' +
               '      <protocol shortName="URC-HTTP">\n' +
               '      <uri>http://192.168.50.13:869/UCH/URC-HTTP/MCETA/mce.uis</uri>\n' +
               '      <protocolInfo>\n' +
               '      <targetDescriptionAt>http://192.168.50.13:869/UCH/targets/tv/mce.td</targetDescriptionAt>\n' +
               '      <targetName>http://res.i2home.org/tv/mce</targetName>\n' +
               '      <socketName friendlyName= "mceSocket">http://res.i2home.org/tv/mce.uis</socketName>\n' +
               '      <socketDescriptionAt>http://192.168.50.13:869/UCH/targets/tv/mce.uis</socketDescriptionAt>\n' +
               '      <conformsTo>http://myurc.org/TR/urc-http-protocol-20080627/</conformsTo>\n' +
               '      </protocolInfo>\n' +
               '      </protocol>\n' +
               '      </ui>\n' +
               '  </uilist>' );
            
         case "openSessionRequest":
            
            switch (messageParameters.socket) {
               case "ac":
               case "aca":
                  return(
                     '  <sessionInfo>\n' +
                     '     <session>ac_session</session>\n' +
                     '     <updateChannel>\n' +
                     '        <ipAddress>127.0.0.1</ipAddress>\n' +
                     '        <portNo>1576</portNo>\n' +
                     '     </updateChannel>\n' +
                     '  </sessionInfo>' );

                     
               case "tv":
                  return(
                     '<sessionInfo>\n' +
                        '<session>c26905a3-7fbf-45c2-80a6-f810ec38e0f8</session>\n' +
                           '<updateChannel>\n' +
                              '<ipAddress>::1</ipAddress>\n' +
                              '<portNo>3595</portNo>\n' +
                           '</updateChannel>\n' +
                     '</sessionInfo>\n' );


               case "tv_epg":
                  return(
                     '<sessionInfo>\n' +
                        '<session>55d308d8-6e57-481a-943b-e3a5efbb2a51</session>\n' +
                           '<updateChannel>\n' +
                              '<ipAddress>::1</ipAddress>\n' +
                              '<portNo>3595</portNo>\n' +
                           '</updateChannel>\n' +
                     '</sessionInfo> ' );
               

               case "satelliteBox":
                  return(
                     '  <sessionInfo>\n' +
                     '     <session>satellitebox_session</session>\n' +
                     '     <updateChannel>\n' +
                     '        <ipAddress>127.0.0.1</ipAddress>\n' +
                     '        <portNo>1576</portNo>\n' +
                     '     </updateChannel>\n' +
                     '  </sessionInfo>' );

                     
               case "testcase1":
                  return(
                     '  <sessionInfo>\n' +
                     '     <session>testcase1_session</session>\n' +
                     '     <updateChannel>\n' +
                     '        <ipAddress>127.0.0.1</ipAddress>\n' +
                     '        <portNo>1576</portNo>\n' +
                     '     </updateChannel>\n' +
                     '  </sessionInfo>' );

                     
               case "testcase2":
                  return(
                     '  <sessionInfo>\n' +
                     '     <session>testcase2_session</session>\n' +
                     '     <updateChannel>\n' +
                     '        <ipAddress>127.0.0.1</ipAddress>\n' +
                     '        <portNo>1576</portNo>\n' +
                     '     </updateChannel>\n' +
                     '  </sessionInfo>' );

               case "level1socket":
                  return(
                     '   <sessionInfo>\n' +
                     '       <session>session-VWNLUL</session>\n' +
                     '       <updateChannel>\n' +
                     '           <ipAddress>127.0.0.1</ipAddress>\n' +
                     '           <portNo>8888</portNo>\n' +
                     '       </updateChannel>\n' +
                     '   </sessionInfo>' );

               case "mce.uis":
                  return(
                     '<sessionInfo>\n' +
                     '<session>803ccbee-74e9-48ac-8521-84a4a518896c</session>\n' +
                     '<updateChannel>\n' +
                     '<ipAddress>192.168.50.13</ipAddress>\n' +
                     '<portNo>8001</portNo>\n' +
                     '</updateChannel>\n' +
                     '</sessionInfo>' );

                     
               default:
                  throw new Error("org_myurc_urchttp_test: No openSessionRequest data for socket '" + messageParameters.socket + "' in " + url);
            }
            
         case "closeSessionRequest":
            
            return('  <sessionClosed />' );
            
            
         case "getValues":
            
            switch (messageParameters.socket) {
               case "ac":
               case "aca":
                  return(
                     '  <values>\n' +
                     '     <value ref="/remoteEnabled">True</value>\n' +
                     '     <value ref="/deviceState">ON</value>\n' +
                     '     <value ref="/deviceMode">ONLINE</value>\n' +
                     '     <value ref="/programmeChoice">COOLING</value>\n' +
                     '     <value ref="/targetFanSpeed">LEVEL_1</value>\n' +
                     '     <value ref="/targetTemp">30</value>\n' +
                     '     <value ref="/currentTemp">24</value>\n' +
                     '     <value ref="/stopTime">17:45:00.0000000+01:00</value>\n' +
                     '     <value ref="/startTime">11:30:00.0000000+01:00</value>\n' +
                     '     <value ref="/deviceType">PA2101HM01</value>\n' +
                     '     <value ref="/deviceName">Generic-Aircondition</value>\n' +
                     '     <value ref="/changeFilterAlert">inactive</value>\n' +
                     '     <value ref="/condensationContainerFullAlert">inactive</value>\n' +
                     '  </values>' );   


               case "tv":
                  var requestDoc = org_myurc_lib_toXmlDoc(postData);
                  var getNodes = org_myurc_lib_selectChildren(requestDoc.documentElement, "get");
                  var refAttr = getNodes[0].getAttribute("ref");     // Only look at first <get> node.
                  
                  switch (refAttr) {
                  
                     case "/":      // Get all values.
                        return(
                           '<values>\n' +
                           '<value ref="/powerMode">ON</value>\n' +
                           '<value ref="/activeChannel">BFM_TV</value>\n' +
                           '<value ref="/channelList">ANDALUCIA_TV ARTE BBC_World BFM_TV BLUCOM_DEMOKANAL</value>\n' +
                           '<value ref="/previousChannel">ARTE</value>\n' +
                           '<value ref="/selectPreviousChannel[state]">initial</value>\n' +
                           '<value ref="/volume">70</value>\n' +
                           '<value ref="/mute">false</value>\n' +
                           '<value ref="/notifySubscription">inactive</value>\n' +
                           '<value ref="/notifyPower">inactive</value>\n' +
                           '</values>\n' );

                     case "/channelList":      // Get values of channelList only.
                        return(
                           '<values>\n' +
                           '<value ref="/channelList">ANDALUCIA_TV ARTE BBC_World BFM_TV BLUCOM_DEMOKANAL</value>\n' +
                           '</values>\n' );
                           
                     case "/powerMode": 
                        return(
                           '<values>\n' +
                           '<value ref="/powerMode">ON</value>\n' +
                           '</values>\n' );
                           
                     case "/activeChannel": 
                        return(
                           '<values>\n' +
                           '<value ref="/activeChannel">BFM_TV</value>\n' +
                           '</values>\n' );
                           
                     case "/previousChannel": 
                        return(
                           '<values>\n' +
                           '<value ref="/previousChannel">ARTE</value>\n' +
                           '</values>\n' );
                           
                     case "/volume": 
                        return(
                           '<values>\n' +
                           '<value ref="/volume">70</value>\n' +
                           '</values>\n' );
                           
                     case "/mute": 
                        return(
                           '<values>\n' +
                           '<value ref="/mute">false</value>\n' +
                           '</values>\n' );
                           
                     case "/notifySubscription": 
                        return(
                           '<values>\n' +
                           '<value ref="/notifySubscription">active</value>\n' +
                           '</values>\n' );
                           
                     case "/notifyPower": 
                        return(
                           '<values>\n' +
                           '<value ref="/notifySubscription">active</value>\n' +
                           '</values>\n' );
                           
                     default:
                        return(
                           '<values>\n' +
                           '<value ref="' + refAttr + '">~</value>\n' +
                           '</values>\n' );
                  }


               case "tv_epg":
                  return(
                     '<values>\n' +
                     '   <value ref="/powerMode">ON</value>\n' +
                     '   <value ref="/channelList">ANDALUCIA_TV ARTE BBC_World BFM_TV BLUCOM_DEMOKANAL</value>\n' +
                     '   <value ref="/epgTable[BBC_World][2007-08-31T06:00:00]">BBC_World@2007-08-31T06:00:00@BBC_World</value>\n' +
                     '   <value ref="/epgTable[BLUCOM_DEMOKANAL][2007-08-31T08:00:00]">BLUCOM_DEMOKANAL@2007-08-31T08:00:00@BLUCOM_Demokanal</value>\n' +
                     '   <value ref="/epgTable[BLUCOM_DEMOKANAL][2007-08-31T20:00:00]">BLUCOM_DEMOKANAL@2007-08-31T20:00:00@BLUCOM_Demokanal</value>\n' +
                     '   <value ref="/epgTable[ANDALUCIA_TV][2007-08-31T19:30:00]">ANDALUCÍA_TV@2007-08-31T19:30:00@ProgramaDeTarde</value>\n' +
                     '   <value ref="/genreMap[BLUCOM_DEMOKANAL@2007-08-31T08:00:00@BLUCOM_Demokanal]">new_media</value>\n' +
                     '   <value ref="/genreMap[BLUCOM_DEMOKANAL@2007-08-31T20:00:00@BLUCOM_Demokanal]">new_media</value>\n' +
                     '   <value ref="/genreMap[ANDALUCIA_TV@2007-08-31T19:30:00@ProgramaDeTarde]">show</value>\n' +
                     '   <value ref="/genreMap[BBC_World@2007-08-31T06:00:00@BBC_World]">news</value>\n' +
                     '   <value ref="/durationMap[BLUCOM_DEMOKANAL@2007-08-31T08:00:00@BLUCOM_Demokanal]">PT12H</value>\n' +
                     '   <value ref="/durationMap[BLUCOM_DEMOKANAL@2007-08-31T20:00:00@BLUCOM_Demokanal]">PT4H</value>\n' +
                     '   <value ref="/durationMap[ANDALUCIA_TV@2007-08-31T19:30:00@ProgramaDeTarde]">PT1H30M</value>\n' +
                     '   <value ref="/durationMap[BBC_World@2007-08-31T06:00:00@BBC_World]">PT24H</value>\n' +
                     '   <value ref="/startTimeMap[BLUCOM_DEMOKANAL@2007-08-31T20:00:00@BLUCOM_Demokanal]">31.08.2007 20:00:00</value>\n' +
                     '   <value ref="/startTimeMap[ANDALUCIA_TV@2007-08-31T19:30:00@ProgramaDeTarde]">31.08.2007 19:30:00</value>\n' +
                     '   <value ref="/startTimeMap[BBC_World@2007-08-31T06:00:00@BBC_World]">31.08.2007 06:00:00</value>\n' +
                     '   <value ref="/startTimeMap[BLUCOM_DEMOKANAL@2007-08-31T08:00:00@BLUCOM_Demokanal]">31.08.2007 08:00:00</value>\n' +
                     '   <value ref="/fillEpgTable[state]">initial</value>\n' +
                     '</values>\n' );


               case "satelliteBox":
                  return(
                     '  <values>\n' +
                     '     <value ref="/activeChannel">1</value>\n' +
                     '     <value ref="/mute">false</value>\n' +
                     '     <value ref="/selectPreviousChannel[state]">initial</value>\n' +
                     '     <value ref="/powerMode">STANDY</value>\n' +
                     '     <value ref="/previousChannel">0</value>\n' +
                     '     <value ref="/volume">50</value>\n' +
                     '  </values>' );   


               case "testcase1":
               
                  return(
                     '<values>\n' +
                     '   <value ref="/var0">value for /var0</value>\n' +
                     '   <add ref="/var1[alpha]">value for /var1[alpha]</add>\n' +
                     '   <add ref="/var1[beta]">value for /var1[beta]</add>\n' +
                     '   <add ref="/var1[gamma]">value for /var1[gamma]</add>\n' +
                     '   <add ref="/var2[alpha][1]">value for /var2[alpha][1]</add>\n' +
                     '   <add ref="/var2[alpha][2]">value for /var2[alpha][2]</add>\n' +
                     '   <add ref="/var2[beta][1]">value for /var2[beta][1]</add>\n' +
                     '   <add ref="/var2[gamma][5]">value for /var2[gamma][5]</add>\n' +
                     '   <add ref="/var2[gamma][2]">value for /var2[gamma][2]</add>\n' +
                     '   <add ref="/var2[gamma][1]">value for /var2[gamma][1]</add>\n' +
                     '   <add ref="/var3[alpha][1][apple]">value for /var3[alpha][1][apple]</add>\n' +
                     '   <add ref="/var3[alpha][2][apple]">value for /var3[alpha][2][apple]</add>\n' +
                     '   <add ref="/var3[beta][1][pear]">value for /var3[beta][1][pear]</add>\n' +
                     '   <add ref="/var3[gamma][5][apple]">value for /var3[gamma][5][apple]</add>\n' +
                     '   <add ref="/var3[gamma][2][kiwi]">value for /var3[gamma][2][kiwi]</add>\n' +
                     '   <add ref="/var3[gamma][1][pear]">value for /var3[gamma][1][pear]</add>\n' +
                     '   <add ref="/var3[gamma][1][apple]">value for /var3[gamma][1][apple]</add>\n' +

                     '   <value ref="/set0/var00">value for /set0/var00</value>\n' +
                     '   <add ref="/set0/var01[alpha]">value for /set0/var01[alpha]</add>\n' +
                     '   <add ref="/set0/var01[beta]">value for /set0/var01[beta]</add>\n' +
                     '   <add ref="/set0/var01[gamma]">value for /set0/var01[gamma]</add>\n' +
                     '   <add ref="/set0/var02[alpha][1]">value for /set0/var02[alpha][1]</add>\n' +
                     '   <add ref="/set0/var02[alpha][2]">value for /set0/var02[alpha][2]</add>\n' +
                     '   <add ref="/set0/var02[beta][1]">value for /set0/var02[beta][1]</add>\n' +
                     '   <add ref="/set0/var02[gamma][5]">value for /set0/var02[gamma][5]</add>\n' +
                     '   <add ref="/set0/var02[gamma][2]">value for /set0/var02[gamma][2]</add>\n' +
                     '   <add ref="/set0/var02[gamma][1]">value for /set0/var02[gamma][1]</add>\n' +
                     '   <add ref="/set0/var03[alpha][1][apple]">value for /set0/var03[alpha][1][apple]</add>\n' +
                     '   <add ref="/set0/var03[alpha][2][apple]">value for /set0/var03[alpha][2][apple]</add>\n' +
                     '   <add ref="/set0/var03[beta][1][pear]">value for /set0/var03[beta][1][pear]</add>\n' +
                     '   <add ref="/set0/var03[gamma][5][apple]">value for /set0/var03[gamma][5][apple]</add>\n' +
                     '   <add ref="/set0/var03[gamma][2][kiwi]">value for /set0/var03[gamma][2][kiwi]</add>\n' +
                     '   <add ref="/set0/var03[gamma][1][pear]">value for /set0/var03[gamma][1][pear]</add>\n' +
                     '   <add ref="/set0/var03[gamma][1][apple]">value for /set0/var03[gamma][1][apple]</add>\n' +
                     '   <value ref="/set0/set00/var000">value for /set0/set00/var000</value>\n' +

                     '   <add ref="/set1[Madison]/var10">value for /set1[Madison]/var10</add>\n' +
                     '   <add ref="/set1[Madison]/var11[alpha]">value for /set1[Madison]/var11[alpha]</add>\n' +
                     '   <add ref="/set1[Madison]/var11[beta]">value for /set1[Madison]/var11[beta]</add>\n' +
                     '   <add ref="/set1[Madison]/var11[gamma]">value for /set1[Madison]/var11[gamma]</add>\n' +
                     '   <add ref="/set1[Madison]/var12[alpha][1]">value for /set1[Madison]/var12[alpha][1]</add>\n' +
                     '   <add ref="/set1[Madison]/var12[alpha][2]">value for /set1[Madison]/var12[alpha][2]</add>\n' +
                     '   <add ref="/set1[Madison]/var12[beta][1]">value for /set1[Madison]/var12[beta][1]</add>\n' +
                     '   <add ref="/set1[Madison]/var12[gamma][5]">value for /set1[Madison]/var12[gamma][5]</add>\n' +
                     '   <add ref="/set1[Madison]/var12[gamma][2]">value for /set1[Madison]/var12[gamma][2]</add>\n' +
                     '   <add ref="/set1[Madison]/var12[gamma][1]">value for /set1[Madison]/var12[gamma][1]</add>\n' +
                     '   <add ref="/set1[Madison]/var13[alpha][1][apple]">value for /set1[Madison]/var13[alpha][1][apple]</add>\n' +
                     '   <add ref="/set1[Madison]/var13[alpha][2][apple]">value for /set1[Madison]/var13[alpha][2][apple]</add>\n' +
                     '   <add ref="/set1[Madison]/var13[beta][1][pear]">value for /set1[Madison]/var13[beta][1][pear]</add>\n' +
                     '   <add ref="/set1[Madison]/var13[gamma][5][apple]">value for /set1[Madison]/var13[gamma][5][apple]</add>\n' +
                     '   <add ref="/set1[Madison]/var13[gamma][2][kiwi]">value for /set1[Madison]/var13[gamma][2][kiwi]</add>\n' +
                     '   <add ref="/set1[Madison]/var13[gamma][1][pear]">value for /set1[Madison]/var13[gamma][1][pear]</add>\n' +
                     '   <add ref="/set1[Madison]/var13[gamma][1][apple]">value for /set1[Madison]/var13[gamma][1][apple]</add>\n' +

                     '   <add ref="/set1[Milwaukee]/var10">value for /set1[Milwaukee]/var10</add>\n' +
                     '   <add ref="/set1[Milwaukee]/var11[alpha]">value for /set1[Milwaukee]/var11[alpha]</add>\n' +
                     '   <add ref="/set1[Milwaukee]/var11[beta]">value for /set1[Milwaukee]/var11[beta]</add>\n' +
                     '   <add ref="/set1[Milwaukee]/var11[gamma]">value for /set1[Milwaukee]/var11[gamma]</add>\n' +
                     '   <add ref="/set1[Milwaukee]/var12[alpha][1]">value for /set1[Milwaukee]/var12[alpha][1]</add>\n' +
                     '   <add ref="/set1[Milwaukee]/var12[alpha][2]">value for /set1[Milwaukee]/var12[alpha][2]</add>\n' +
                     '   <add ref="/set1[Milwaukee]/var12[beta][1]">value for /set1[Milwaukee]/var12[beta][1]</add>\n' +
                     '   <add ref="/set1[Milwaukee]/var12[gamma][5]">value for /set1[Milwaukee]/var12[gamma][5]</add>\n' +
                     '   <add ref="/set1[Milwaukee]/var12[gamma][2]">value for /set1[Milwaukee]/var12[gamma][2]</add>\n' +
                     '   <add ref="/set1[Milwaukee]/var12[gamma][1]">value for /set1[Milwaukee]/var12[gamma][1]</add>\n' +
                     '   <add ref="/set1[Milwaukee]/var13[alpha][1][apple]">value for /set1[Milwaukee]/var13[alpha][1][apple]</add>\n' +
                     '   <add ref="/set1[Milwaukee]/var13[alpha][2][apple]">value for /set1[Milwaukee]/var13[alpha][2][apple]</add>\n' +
                     '   <add ref="/set1[Milwaukee]/var13[beta][1][pear]">value for /set1[Milwaukee]/var13[beta][1][pear]</add>\n' +
                     '   <add ref="/set1[Milwaukee]/var13[gamma][5][apple]">value for /set1[Milwaukee]/var13[gamma][5][apple]</add>\n' +
                     '   <add ref="/set1[Milwaukee]/var13[gamma][2][kiwi]">value for /set1[Milwaukee]/var13[gamma][2][kiwi]</add>\n' +
                     '   <add ref="/set1[Milwaukee]/var13[gamma][1][pear]">value for /set1[Milwaukee]/var13[gamma][1][pear]</add>\n' +
                     '   <add ref="/set1[Milwaukee]/var13[gamma][1][apple]">value for /set1[Milwaukee]/var13[gamma][1][apple]</add>\n' +

                     '   <add ref="/set2[Madison][2006]/var20">value for /set2[Madison][2006]/var20</add>\n' +
                     '   <add ref="/set2[Madison][2006]/var21[alpha]">value for /set2[Madison][2006]/var21[alpha]</add>\n' +
                     '   <add ref="/set2[Madison][2006]/var21[beta]">value for /set2[Madison][2006]/var21[beta]</add>\n' +
                     '   <add ref="/set2[Madison][2006]/var21[gamma]">value for /set2[Madison][2006]/var21[gamma]</add>\n' +
                     '   <add ref="/set2[Madison][2006]/var22[alpha][1]">value for /set2[Madison][2006]/var22[alpha][1]</add>\n' +
                     '   <add ref="/set2[Madison][2006]/var22[alpha][2]">value for /set2[Madison][2006]/var22[alpha][2]</add>\n' +
                     '   <add ref="/set2[Madison][2006]/var22[beta][1]">value for /set2[Madison][2006]/var22[beta][1]</add>\n' +
                     '   <add ref="/set2[Madison][2006]/var22[gamma][5]">value for /set2[Madison][2006]/var22[gamma][5]</add>\n' +
                     '   <add ref="/set2[Madison][2006]/var22[gamma][2]">value for /set2[Madison][2006]/var22[gamma][2]</add>\n' +
                     '   <add ref="/set2[Madison][2006]/var22[gamma][1]">value for /set2[Madison][2006]/var22[gamma][1]</add>\n' +
                     '   <add ref="/set2[Madison][2006]/var23[alpha][1][apple]">value for /set2[Madison][2006]/var23[alpha][1][apple]</add>\n' +
                     '   <add ref="/set2[Madison][2006]/var23[alpha][2][apple]">value for /set2[Madison][2006]/var23[alpha][2][apple]</add>\n' +
                     '   <add ref="/set2[Madison][2006]/var23[beta][1][pear]">value for /set2[Madison][2006]/var23[beta][1][pear]</add>\n' +
                     '   <add ref="/set2[Madison][2006]/var23[gamma][5][apple]">value for /set2[Madison][2006]/var23[gamma][5][apple]</add>\n' +
                     '   <add ref="/set2[Madison][2006]/var23[gamma][2][kiwi]">value for /set2[Madison][2006]/var23[gamma][2][kiwi]</add>\n' +
                     '   <add ref="/set2[Madison][2006]/var23[gamma][1][pear]">value for /set2[Madison][2006]/var23[gamma][1][pear]</add>\n' +
                     '   <add ref="/set2[Madison][2006]/var23[gamma][1][apple]">value for /set2[Madison][2006]/var23[gamma][1][apple]</add>\n' +

                     '   <add ref="/set2[Madison][2007]/var20">value for /set2[Madison][2007]/var20</add>\n' +
                     '   <add ref="/set2[Madison][2007]/var21[alpha]">value for /set2[Madison][2007]/var21[alpha]</add>\n' +
                     '   <add ref="/set2[Madison][2007]/var21[beta]">value for /set2[Madison][2007]/var21[beta]</add>\n' +
                     '   <add ref="/set2[Madison][2007]/var21[gamma]">value for /set2[Madison][2007]/var21[gamma]</add>\n' +
                     '   <add ref="/set2[Madison][2007]/var22[alpha][1]">value for /set2[Madison][2007]/var22[alpha][1]</add>\n' +
                     '   <add ref="/set2[Madison][2007]/var22[alpha][2]">value for /set2[Madison][2007]/var22[alpha][2]</add>\n' +
                     '   <add ref="/set2[Madison][2007]/var22[beta][1]">value for /set2[Madison][2007]/var22[beta][1]</add>\n' +
                     '   <add ref="/set2[Madison][2007]/var22[gamma][5]">value for /set2[Madison][2007]/var22[gamma][5]</add>\n' +
                     '   <add ref="/set2[Madison][2007]/var22[gamma][2]">value for /set2[Madison][2007]/var22[gamma][2]</add>\n' +
                     '   <add ref="/set2[Madison][2007]/var22[gamma][1]">value for /set2[Madison][2007]/var22[gamma][1]</add>\n' +
                     '   <add ref="/set2[Madison][2007]/var23[alpha][1][apple]">value for /set2[Madison][2007]/var23[alpha][1][apple]</add>\n' +
                     '   <add ref="/set2[Madison][2007]/var23[alpha][2][apple]">value for /set2[Madison][2007]/var23[alpha][2][apple]</add>\n' +
                     '   <add ref="/set2[Madison][2007]/var23[beta][1][pear]">value for /set2[Madison][2007]/var23[beta][1][pear]</add>\n' +
                     '   <add ref="/set2[Madison][2007]/var23[gamma][5][apple]">value for /set2[Madison][2007]/var23[gamma][5][apple]</add>\n' +
                     '   <add ref="/set2[Madison][2007]/var23[gamma][2][kiwi]">value for /set2[Madison][2007]/var23[gamma][2][kiwi]</add>\n' +
                     '   <add ref="/set2[Madison][2007]/var23[gamma][1][pear]">value for /set2[Madison][2007]/var23[gamma][1][pear]</add>\n' +
                     '   <add ref="/set2[Madison][2007]/var23[gamma][1][apple]">value for /set2[Madison][2007]/var23[gamma][1][apple]</add>\n' +
                     '</values>\n' );
                           

               case "testcase2":
                  
                  return(
                     '<values>\n' +
                     '   <value ref="/cmd0b[state]">state for /cmd0b</value>\n' +
                     '   <value ref="/cmd0b/param0b-o1">value for /cmd0b/param0b-o1</value>\n' +
                     '   <value ref="/cmd0b/param0b-o2">value for /cmd0b/param0b-o2</value>\n' +
                     '   <value ref="/cmd0c[state]">value for /cmd0c[state]</value>\n' +
                     '   <value ref="/cmd0d[state]">value for /cmd0d[state]</value>\n' +
                     '   <value ref="/cmd0d/param0d-o1">value for /cmd0d/param0d-o1</value>\n' +
                     '   <value ref="/cmd0d/param0d-o2">value for /cmd0d/param0d-o2</value>\n' +
                     '   <value ref="/cmd0e[state]">value for /cmd0e[state]</value>\n' +
                     '   <value ref="/cmd0e[ttc]">value for /cmd0e[ttc]</value>\n' +
                     '   <value ref="/cmd0f[state]">value for /cmd0f[state]</value>\n' +
                     '   <value ref="/cmd0f[ttc]">value for /cmd0f[ttc]</value>\n' +
                     '   <value ref="/cmd0f/param0f-o1">value for /cmd0f/param0f-o1</value>\n' +
                     '   <value ref="/cmd0f/param0f-o2">value for /cmd0f/param0f-o2</value>\n' +
                     '   <add ref="/cmd1b[1]/param1b-o1">value for /cmd1b[1]/param1b-o1</add>\n' +
                     '   <add ref="/cmd1b[1]/param1b-o2">value for /cmd1b[1]/param1b-o2</add>\n' +
                     '   <add ref="/cmd1b[2]/param1b-o1">value for /cmd1b[2]/param1b-o1</add>\n' +
                     '   <add ref="/cmd1b[2]/param1b-o2">value for /cmd1b[2]/param1b-o2</add>\n' +
                     '   <add ref="/cmd1c[Madison][state]">value for /cmd1c[Madison][state]</add>\n' +
                     '   <add ref="/cmd1c[Milwaukee][state]">value for /cmd1c[Milwaukee][state]</add>\n' +
                     '   <add ref="/cmd1d[Madison][state]">value for /cmd1d[Madison][state]</add>\n' +
                     '   <add ref="/cmd1d[Madison]/param1d-o1">value for /cmd1d[Madison]/param1d-o1</add>\n' +
                     '   <add ref="/cmd1d[Madison]/param1d-o2">value for /cmd1d[Madison]/param1d-o2</add>\n' +
                     '   <add ref="/cmd1d[Milwaukee][state]">value for /cmd1d[Milwaukee][state]</add>\n' +
                     '   <add ref="/cmd1e[Madison][state]">value for /cmd1e[Madison][state]</add>\n' +
                     '   <add ref="/cmd1e[Madison][ttc]">value for /cmd1e[Madison][ttc]</add>\n' +
                     '   <add ref="/cmd1e[Washington][ttc]">value for /cmd1e[Madison][ttc]</add>\n' +
                     '   <add ref="/cmd1f[Madison][state]">value for /cmd1f[Madison][state]</add>\n' +
                     '   <add ref="/cmd1f[Madison][ttc]">value for /cmd1f[Madison][ttc]</add>\n' +
                     '   <add ref="/cmd1f[Madison]/param1f-o1">value for /cmd1f[Madison]/param1f-o1</add>\n' +
                     '   <add ref="/cmd1f[Washington][ttc]">value for /cmd1f[Washington][ttc]</add>\n' +
                     '   <add ref="/cmd1f[Washington]/param1f-o1">value for /cmd1f[Washington]/param1f-o1</add>\n' +
                     '   <add ref="/cmd1f[Madison]/param1f-o2">value for /cmd1f[Madison]/param1f-o2</add>\n' +
                     '   <add ref="/cmd2a[Madison][spring][state]">value for /cmd2a[Madison][spring][state]</add>\n' +
                     '   <add ref="/cmd2a[Madison][spring][ttc]">value for /cmd2a[Madison][spring][ttc]</add>\n' +
                     '   <add ref="/cmd2a[Madison][spring]/param2a-o1">value for /cmd2a[Madison][spring]/param2a-o1</add>\n' +
                     '   <add ref="/cmd2a[Washington][summer][ttc]">value for /cmd2a[Washington][summer][ttc]</add>\n' +
                     '   <add ref="/cmd2a[Washington][fall]/param2a-o1">value for /cmd2a[Washington][fall]/param2a-o1</add>\n' +
                     '   <add ref="/cmd2a[Madison][spring]/param2a-o2">value for /cmd2a[Madison][spring]/param2a-o2</add>\n' +

                     '   <value ref="/set0/cmd00d[state]">value for /set0/cmd00d[state]</value>\n' +
                     '   <value ref="/set0/cmd00d/param00d-o1">value for /set0/cmd00d/param00d-o1</value>\n' +
                     '   <value ref="/set0/cmd00d/param00d-o2">value for /set0/cmd00d/param00d-o2</value>\n' +
                     '   <add ref="/set0/cmd01d[apple][state]">value for /set0/cmd01d[apple][state]</add>\n' +
                     '   <add ref="/set0/cmd01d[apple]/param01d-o1">value for /set0/cmd01d[apple]/param01d-o1</add>\n' +
                     '   <add ref="/set0/cmd01d[apple]/param01d-o2">value for /set0/cmd01d[apple]/param01d-o2</add>\n' +
                     '   <add ref="/set0/cmd01d[orange][state]">value for /set0/cmd01d[orange][state]</add>\n' +
                     '   <add ref="/set0/cmd01d[lemon]/param01d-o1">value for /set0/cmd01d[lemon]/param01d-o1</add>\n' +
                     '   <add ref="/set0/cmd01d[orange]/param01d-o2">value for /set0/cmd01d[orange]/param01d-o2</add>\n' +
                     '   <add ref="/set0/cmd02d[apple][2006][state]">value for /set0/cmd02d[apple][2006][state]</add>\n' +
                     '   <add ref="/set0/cmd02d[apple][2006]/param02d-o1">value for /set0/cmd02d[apple][2006]/param02d-o1</add>\n' +
                     '   <add ref="/set0/cmd02d[apple][2007]/param02d-o2">value for /set0/cmd02d[apple][2007]/param02d-o2</add>\n' +
                     '   <add ref="/set0/cmd02d[orange][2006][state]">value for /set0/cmd02d[orange][2006][state]</add>\n' +
                     '   <add ref="/set0/cmd02d[lemon][2006]/param02d-o1">value for /set0/cmd02d[lemon][2006]/param02d-o1</add>\n' +
                     '   <add ref="/set0/cmd02d[orange][2006]/param02d-o2">value for /set0/cmd02d[orange][2006]/param02d-o2</add>\n' +

                     '   <add ref="/set1[Madison]/cmd10d[state]">value for /set1[Madison]/cmd10d[state]</add>\n' +
                     '   <add ref="/set1[Madison]/cmd10d/param10d-o1">value for /set1[Madison]/cmd10d/param10d-o1</add>\n' +
                     '   <add ref="/set1[Madison]/cmd10d/param10d-o2">value for /set1[Madison]/cmd10d/param10d-o2</add>\n' +
                     '   <add ref="/set1[Madison]/cmd11d[apple][state]">value for /set1[Madison]/cmd11d[state]</add>\n' +
                     '   <add ref="/set1[Madison]/cmd11d[apple]/param11d-o1">value for /set1[Madison]/cmd11d[apple]/param11d-o1</add>\n' +
                     '   <add ref="/set1[Madison]/cmd11d[apple]/param11d-o2">value for /set1[Madison]/cmd11d[apple]/param11d-o2</add>\n' +
                     '   <add ref="/set1[Madison]/cmd11d[orange][state]">value for /set1[Madison]/cmd11d[orange][state]</add>\n' +
                     '   <add ref="/set1[Madison]/cmd11d[lemon]/param11d-o1">value for /set1[Madison]/cmd11d[lemon]/param11d-o1</add>\n' +
                     '   <add ref="/set1[Madison]/cmd11d[orange]/param11d-o2">value for /set1[Madison]/cmd11d[orange]/param11d-o2</add>\n' +
                     '   <add ref="/set1[Madison]/cmd12d[apple][2006][state]">value for /set1[Madison]/cmd12d[apple][2006][state]</add>\n' +
                     '   <add ref="/set1[Madison]/cmd12d[apple][2006]/param12d-o1">value for /set1[Madison]/cmd12d[apple][2006]/param12d-o1</add>\n' +
                     '   <add ref="/set1[Madison]/cmd12d[apple][2007]/param12d-o2">value for /set1[Madison]/cmd12d[apple][2007]/param12d-o2</add>\n' +
                     '   <add ref="/set1[Madison]/cmd12d[orange][2006][state]">value for /set1[Madison]/cmd12d[orange][2006][state]</add>\n' +
                     '   <add ref="/set1[Madison]/cmd12d[lemon][2006]/param12d-o1">value for /set1[Madison]/cmd12d[lemon][2006]/param12d-o1</add>\n' +
                     '   <add ref="/set1[Madison]/cmd12d[orange][2006]/param12d-o2">value for /set1[Madison]/cmd12d[orange][2006]/param12d-o2</add>\n' +

                     '   <add ref="/set1[Appleton]/cmd10d[state]">value for /set1[Appleton]/cmd10d[state]</add>\n' +
                     '   <add ref="/set1[Appleton]/cmd10d/param10d-o1">value for /set1[Appleton]/cmd10d/param10d-o1</add>\n' +
                     '   <add ref="/set1[Appleton]/cmd10d/param10d-o2">value for /set1[Appleton]/cmd10d/param10d-o2</add>\n' +
                     '   <add ref="/set1[Appleton]/cmd11d[apple][state]">value for /set1[Appleton]/cmd11d[apple][state]</add>\n' +
                     '   <add ref="/set1[Appleton]/cmd11d[apple]/param11d-o1">value for /set1[Appleton]/cmd11d[apple]/param11d-o1</add>\n' +
                     '   <add ref="/set1[Appleton]/cmd11d[apple]/param11d-o2">value for /set1[Appleton]/cmd11d[apple]/param11d-o2</add>\n' +
                     '   <add ref="/set1[Appleton]/cmd11d[orange][state]">value for /set1[Appleton]/cmd11d[orange][state]</add>\n' +
                     '   <add ref="/set1[Appleton]/cmd11d[lemon]/param11d-o1">value for /set1[Appleton]/cmd11d[lemon]/param11d-o1</add>\n' +
                     '   <add ref="/set1[Appleton]/cmd11d[orange]/param11d-o2">value for /set1[Appleton]/cmd11d[orange]/param11d-o2</add>\n' +
                     '   <add ref="/set1[Appleton]/cmd12d[apple][2006][state]">value for /set1[Appleton]/cmd12d[apple][2006][state]</add>\n' +
                     '   <add ref="/set1[Appleton]/cmd12d[apple][2006]/param12d-o1">value for /set1[Appleton]/cmd12d[apple][2006]/param12d-o1</add>\n' +
                     '   <add ref="/set1[Appleton]/cmd12d[apple][2007]/param12d-o2">value for /set1[Appleton]/cmd12d[apple][2007]/param12d-o2</add>\n' +
                     '   <add ref="/set1[Appleton]/cmd12d[orange][2006][state]">value for /set1[Appleton]/cmd12d[orange][2006][state]</add>\n' +
                     '   <add ref="/set1[Appleton]/cmd12d[lemon][2006]/param12d-o1">value for /set1[Appleton]/cmd12d[lemon][2006]/param12d-o1</add>\n' +
                     '   <add ref="/set1[Appleton]/cmd12d[orange][2006]/param12d-o2">value for /set1[Appleton]/cmd12d[orange][2006]/param12d-o2</add>\n' +

                     '</values>\n' );

               case "play-1.uis":
                  return('<values><value ref="/mediaRenderers[uuid:526F6B75-536F-756E-6442-000D4B20069F]/renderingcontrol/chAudio[Master]/chVolumeDBMinValue">~</value><value ref="/mediaRenderers[uuid:526F6B75-536F-756E-6442-000D4B20069F]/renderingcontrol/chAudio[Master]/chVolume">79</value><value ref="/mediaServers[uuid:ab9cdfb4-e0f1-11db-84c5-0016018aacfc]/browsing/content/parentNode[state]">initial</value><value ref="/mediaRenderers[uuid:1e5e63ea-1dd2-11b2-8745-0019cb567d39]/renderingcontrol/video/contrastMaxValue">~</value><value ref="/mediaRenderers[uuid:526F6B75-536F-756E-6442-000D4B20069F]/renderingcontrol/video/brightness">~</value><value ref="/mediaServers[uuid:ab9cdfb4-e0f1-11db-84c5-0016018aacfc]/browsing/content/class">object.container</value><value ref="/mediaRenderers[uuid:34e22c00-1dd2-11b2-a1f5-0040d0b82e2d]/renderingcontrol/chAudio[Master]/chVolumeDB">~</value><value ref="/mediaRenderers[uuid:34e22c00-1dd2-11b2-a1f5-0040d0b82e2d]/renderingcontrol/video/sharpnessMaxValue">~</value><value ref="/mediaServers[uuid:0d99d258-9b59-44c1-8884-d75b61d3e04c]/searchCapabilities">~</value><value ref="/mediaRenderers[uuid:526F6B75-536F-756E-6442-000D4B20069F]/renderingcontrol/video/redVideoGain">~</value><value ref="/mediaRenderers[uuid:526F6B75-536F-756E-6442-000D4B20069F]/renderingcontrol/chAudio[Master]/chMute">~</value><value ref="/mediaRenderers[uuid:526F6B75-536F-756E-6442-000D4B20069F]/isPlayAvailable">~</value><value ref="/mediaRenderers[uuid:1e5e63ea-1dd2-11b2-8745-0019cb567d39]/renderingcontrol/video/blueVideoGain">~</value><value ref="/mediaServers[uuid:ab9cdfb4-e0f1-11db-84c5-0016018aacfc]/browsing/content/nextNode[state]">initial</value><value ref="/mediaServers[uuid:0d99d258-9b59-44c1-8884-d75b61d3e04c]/browsing/content/writeStatus">~</value><value ref="/mediaServers[uuid:0d99d258-9b59-44c1-8884-d75b61d3e04c]/browsing/content/searchable">~</value><value ref="/mediaRenderers[uuid:526F6B75-536F-756E-6442-000D4B20069F]/renderingcontrol/video/greenVideoGainMaxValue">~</value><value ref="/mediaRenderers[uuid:1e5e63ea-1dd2-11b2-8745-0019cb567d39]/renderingcontrol/video/greenVideoBlackLevel">~</value><value ref="/mediaRenderers[uuid:34e22c00-1dd2-11b2-a1f5-0040d0b82e2d]/renderingcontrol/video/blueVideoBlackLevel">~</value><value ref="/mediaServers[uuid:0d99d258-9b59-44c1-8884-d75b61d3e04c]/isNextNodeAvailable">false</value><value ref="/mediaServers[uuid:0d99d258-9b59-44c1-8884-d75b61d3e04c]/browsing/content/parentNode[state]">initial</value><value ref="/mediaRenderers[uuid:526F6B75-536F-756E-6442-000D4B20069F]/renderingcontrol/video/brightnessMaxValue">~</value><value ref="/mediaRenderers[uuid:526F6B75-536F-756E-6442-000D4B20069F]/renderingcontrol/video/colorTemperature">~</value><value ref="/mediaServers[uuid:ab9cdfb4-e0f1-11db-84c5-0016018aacfc]/browsing/content/prevNode[state]">initial</value><value ref="/mediaRenderers[uuid:1e5e63ea-1dd2-11b2-8745-0019cb567d39]/renderingcontrol/chAudio[Master]/chMute">false</value><value ref="/mediaRenderers[uuid:526F6B75-536F-756E-6442-000D4B20069F]/renderingcontrol/video/redVideoBlackLevel">~</value><value ref="/mediaRenderers[uuid:526F6B75-536F-756E-6442-000D4B20069F]/renderingcontrol/presets/presetNameList">FactoryDefaults,InstallationDefaults</value><value ref="/mediaRenderers[uuid:1e5e63ea-1dd2-11b2-8745-0019cb567d39]/mediaRendererFriendlyName">DMA-1000</value><value ref="/mediaServers[uuid:0d99d258-9b59-44c1-8884-d75b61d3e04c]/browsing/content/childCount">~</value><value ref="/mediaServers[uuid:ab9cdfb4-e0f1-11db-84c5-0016018aacfc]/browsing/content/refID">~</value><value ref="/mediaServers[uuid:ab9cdfb4-e0f1-11db-84c5-0016018aacfc]/browsing/content/writeStatus">~</value><value ref="/mediaServers[uuid:0d99d258-9b59-44c1-8884-d75b61d3e04c]/browsing/content/parentID">-1</value><value ref="/mediaRenderers[uuid:34e22c00-1dd2-11b2-a1f5-0040d0b82e2d]/renderingcontrol/video/contrastMaxValue">~</value><value ref="/mediaRenderers[uuid:34e22c00-1dd2-11b2-a1f5-0040d0b82e2d]/renderingcontrol/chAudio[Master]/chVolumeDBMinValue">~</value><value ref="/mediaRenderers[uuid:526F6B75-536F-756E-6442-000D4B20069F]/renderingcontrol/video/greenVideoGain">~</value><value ref="/mediaServers[uuid:ab9cdfb4-e0f1-11db-84c5-0016018aacfc]/browsing/content/childCount">~</value><value ref="/mediaServers[uuid:0d99d258-9b59-44c1-8884-d75b61d3e04c]/browsing/content/prevNode[state]">initial</value><value ref="/mediaRenderers[uuid:1e5e63ea-1dd2-11b2-8745-0019cb567d39]/renderingcontrol/chAudio[Master]/chVolumeDB">~</value><value ref="/mediaRenderers[uuid:34e22c00-1dd2-11b2-a1f5-0040d0b82e2d]/renderingcontrol/video/brightness">~</value><value ref="/mediaRenderers[uuid:526F6B75-536F-756E-6442-000D4B20069F]/renderingcontrol/video/horizontalKeystoneMaxValue">~</value><value ref="/mediaRenderers[uuid:526F6B75-536F-756E-6442-000D4B20069F]/mediaRendererFriendlyName">SoundBridge</value><value ref="/mediaRenderers[uuid:1e5e63ea-1dd2-11b2-8745-0019cb567d39]/isMediaRendererSelected">~</value><value ref="/mediaServers[uuid:0d99d258-9b59-44c1-8884-d75b61d3e04c]/browsing/content/selectedNodeIndex">1</value><value ref="/mediaServers[uuid:ab9cdfb4-e0f1-11db-84c5-0016018aacfc]/isFirstChildNodeAvailable">true</value><value ref="/mediaServers[uuid:0d99d258-9b59-44c1-8884-d75b61d3e04c]/isParentNodeAvailable">false</value><value ref="/mediaServers[uuid:0d99d258-9b59-44c1-8884-d75b61d3e04c]/browsing/content/restricted">1</value><value ref="/mediaServers[uuid:0d99d258-9b59-44c1-8884-d75b61d3e04c]/browsing/content/nextNode[state]">initial</value><value ref="/mediaRenderers[uuid:526F6B75-536F-756E-6442-000D4B20069F]/renderingcontrol/video/verticalKeystone">~</value><value ref="/mediaRenderers[uuid:34e22c00-1dd2-11b2-a1f5-0040d0b82e2d]/renderingcontrol/video/greenVideoGain">~</value><value ref="/mediaRenderers[uuid:1e5e63ea-1dd2-11b2-8745-0019cb567d39]/renderingcontrol/chAudio[Master]/chVolume">25</value><value ref="/mediaServers[uuid:0d99d258-9b59-44c1-8884-d75b61d3e04c]/selectedNodeId">0</value><value ref="/mediaServers[uuid:ab9cdfb4-e0f1-11db-84c5-0016018aacfc]/isParentNodeAvailable">false</value><value ref="/mediaRenderers[uuid:526F6B75-536F-756E-6442-000D4B20069F]/renderingcontrol/video/verticalKeystoneMaxValue">~</value><value ref="/mediaServers[uuid:ab9cdfb4-e0f1-11db-84c5-0016018aacfc]/sortCapabilities">dc:title,dc:date</value><value ref="/mediaServers[uuid:ab9cdfb4-e0f1-11db-84c5-0016018aacfc]/isMediaServerSelected">~</value><value ref="/mediaRenderers[uuid:34e22c00-1dd2-11b2-a1f5-0040d0b82e2d]/renderingcontrol/video/horizontalKeystoneMinValue">~</value><value ref="/mediaRenderers[uuid:1e5e63ea-1dd2-11b2-8745-0019cb567d39]/renderingcontrol/chAudio[Master]/chVolumeDBMinValue">~</value><value ref="/mediaRenderers[uuid:1e5e63ea-1dd2-11b2-8745-0019cb567d39]/renderingcontrol/video/colorTemperature">~</value><value ref="/mediaRenderers[uuid:526F6B75-536F-756E-6442-000D4B20069F]/renderingcontrol/video/blueVideoGain">~</value><value ref="/mediaRenderers[uuid:526F6B75-536F-756E-6442-000D4B20069F]/renderingcontrol/video/blueVideoBlackLevelMaxValue">~</value><value ref="/mediaRenderers[uuid:34e22c00-1dd2-11b2-a1f5-0040d0b82e2d]/renderingcontrol/video/greenVideoBlackLevel">~</value><value ref="/mediaRenderers[uuid:1e5e63ea-1dd2-11b2-8745-0019cb567d39]/isPlayAvailable">~</value><value ref="/mediaRenderers[uuid:34e22c00-1dd2-11b2-a1f5-0040d0b82e2d]/isMediaRendererSelected">~</value><value ref="/mediaServers[uuid:ab9cdfb4-e0f1-11db-84c5-0016018aacfc]/browsing/browseSortCriteria"></value><value ref="/mediaRenderers[uuid:526F6B75-536F-756E-6442-000D4B20069F]/renderingcontrol/video/horizontalKeystone">~</value><value ref="/mediaRenderers[uuid:34e22c00-1dd2-11b2-a1f5-0040d0b82e2d]/renderingcontrol/video/horizontalKeystoneMaxValue">~</value><value ref="/mediaServers[uuid:0d99d258-9b59-44c1-8884-d75b61d3e04c]/browsing/content/res">~</value><value ref="/mediaRenderers[uuid:1e5e63ea-1dd2-11b2-8745-0019cb567d39]/renderingcontrol/video/contrast">~</value><value ref="/mediaServers[uuid:ab9cdfb4-e0f1-11db-84c5-0016018aacfc]/searchCapabilities">~</value><value ref="/mediaRenderers[uuid:526F6B75-536F-756E-6442-000D4B20069F]/renderingcontrol/video/redVideoBlackLevelMaxValue">~</value><value ref="/mediaRenderers[uuid:34e22c00-1dd2-11b2-a1f5-0040d0b82e2d]/renderingcontrol/chAudio[Master]/chVolume">70</value><value ref="/mediaRenderers[uuid:526F6B75-536F-756E-6442-000D4B20069F]/renderingcontrol/volumeMaxValue">~</value><value ref="/mediaServers[uuid:ab9cdfb4-e0f1-11db-84c5-0016018aacfc]/isNextNodeAvailable">false</value><value ref="/mediaRenderers[uuid:526F6B75-536F-756E-6442-000D4B20069F]/renderingcontrol/presets/selectPreset[state]">initial</value><value ref="/mediaServers[uuid:0d99d258-9b59-44c1-8884-d75b61d3e04c]/search[state]">initial</value><value ref="/mediaRenderers[uuid:34e22c00-1dd2-11b2-a1f5-0040d0b82e2d]/renderingcontrol/video/sharpness">~</value><value ref="/mediaRenderers[uuid:526F6B75-536F-756E-6442-000D4B20069F]/renderingcontrol/video/horizontalKeystoneMinValue">~</value><value ref="/mediaRenderers[uuid:1e5e63ea-1dd2-11b2-8745-0019cb567d39]/renderingcontrol/chAudio[Master]/chLoudness">~</value><value ref="/mediaRenderers[uuid:1e5e63ea-1dd2-11b2-8745-0019cb567d39]/renderingcontrol/video/redVideoBlackLevelMaxValue">~</value><value ref="/mediaServers[uuid:ab9cdfb4-e0f1-11db-84c5-0016018aacfc]/browsing/content/createClasses">~</value><value ref="/mediaRenderers[uuid:526F6B75-536F-756E-6442-000D4B20069F]/renderingcontrol/chAudio[Master]/chVolumeDBMaxValue">~</value><value ref="/mediaRenderers[uuid:34e22c00-1dd2-11b2-a1f5-0040d0b82e2d]/renderingcontrol/volumeMaxValue">~</value><value ref="/mediaServers[uuid:0d99d258-9b59-44c1-8884-d75b61d3e04c]/isFirstChildNodeAvailable">true</value><value ref="/mediaServers[uuid:ab9cdfb4-e0f1-11db-84c5-0016018aacfc]/isPrevNodeAvailable">false</value><value ref="/mediaServers[uuid:0d99d258-9b59-44c1-8884-d75b61d3e04c]/browsing/content/createClasses">~</value><value ref="/mediaRenderers[uuid:526F6B75-536F-756E-6442-000D4B20069F]/renderingcontrol/video/blueVideoGainMaxValue">~</value><value ref="/mediaRenderers[uuid:1e5e63ea-1dd2-11b2-8745-0019cb567d39]/renderingcontrol/video/horizontalKeystone">~</value><value ref="/mediaRenderers[uuid:1e5e63ea-1dd2-11b2-8745-0019cb567d39]/renderingcontrol/video/blueVideoBlackLevel">~</value><value ref="/mediaRenderers[uuid:34e22c00-1dd2-11b2-a1f5-0040d0b82e2d]/renderingcontrol/chAudio[Master]/chMute">false</value><value ref="/mediaRenderers[uuid:34e22c00-1dd2-11b2-a1f5-0040d0b82e2d]/renderingcontrol/chAudio[Master]/chVolumeDBMaxValue">~</value><value ref="/mediaRenderers[uuid:34e22c00-1dd2-11b2-a1f5-0040d0b82e2d]/renderingcontrol/video/verticalKeystone">~</value><value ref="/mediaRenderers[uuid:34e22c00-1dd2-11b2-a1f5-0040d0b82e2d]/renderingcontrol/presets/presetNameList">~</value><value ref="/mediaRenderers[uuid:1e5e63ea-1dd2-11b2-8745-0019cb567d39]/renderingcontrol/video/redVideoBlackLevel">~</value><value ref="/mediaServers[uuid:ab9cdfb4-e0f1-11db-84c5-0016018aacfc]/browsing/content/searchable">~</value><value ref="/mediaRenderers[uuid:34e22c00-1dd2-11b2-a1f5-0040d0b82e2d]/renderingcontrol/video/redVideoGain">~</value><value ref="/mediaServers[uuid:0d99d258-9b59-44c1-8884-d75b61d3e04c]/browsing/content/firstChildNode[state]">initial</value><value ref="/mediaServers[uuid:ab9cdfb4-e0f1-11db-84c5-0016018aacfc]/selectedNodeId">0</value><value ref="/mediaRenderers[uuid:34e22c00-1dd2-11b2-a1f5-0040d0b82e2d]/renderingcontrol/video/redVideoBlackLevel">~</value><value ref="/mediaServers[uuid:ab9cdfb4-e0f1-11db-84c5-0016018aacfc]/browsing/content/selectedNodeIndex">1</value><value ref="/mediaRenderers[uuid:1e5e63ea-1dd2-11b2-8745-0019cb567d39]/renderingcontrol/video/verticalKeystoneMinValue">~</value><value ref="/mediaRenderers[uuid:34e22c00-1dd2-11b2-a1f5-0040d0b82e2d]/renderingcontrol/video/verticalKeystoneMaxValue">~</value><value ref="/mediaRenderers[uuid:1e5e63ea-1dd2-11b2-8745-0019cb567d39]/renderingcontrol/video/blueVideoGainMaxValue">~</value><value ref="/mediaServers[uuid:0d99d258-9b59-44c1-8884-d75b61d3e04c]/isPrevNodeAvailable">false</value><value ref="/mediaRenderers[uuid:526F6B75-536F-756E-6442-000D4B20069F]/renderingcontrol/video/redVideoGainMaxValue">~</value><value ref="/mediaRenderers[uuid:526F6B75-536F-756E-6442-000D4B20069F]/renderingcontrol/video/greenVideoBlackLevel">~</value><value ref="/mediaRenderers[uuid:526F6B75-536F-756E-6442-000D4B20069F]/renderingcontrol/video/contrast">~</value><value ref="/mediaRenderers[uuid:34e22c00-1dd2-11b2-a1f5-0040d0b82e2d]/renderingcontrol/video/greenVideoGainMaxValue">~</value><value ref="/mediaRenderers[uuid:1e5e63ea-1dd2-11b2-8745-0019cb567d39]/renderingcontrol/video/greenVideoBlackLevelMaxValue">~</value><value ref="/mediaRenderers[uuid:34e22c00-1dd2-11b2-a1f5-0040d0b82e2d]/renderingcontrol/video/greenVideoBlackLevelMaxValue">~</value><value ref="/mediaRenderers[uuid:1e5e63ea-1dd2-11b2-8745-0019cb567d39]/renderingcontrol/video/colorTemperatureMaxValue">~</value><value ref="/mediaRenderers[uuid:1e5e63ea-1dd2-11b2-8745-0019cb567d39]/renderingcontrol/video/horizontalKeystoneMaxValue">~</value><value ref="/mediaRenderers[uuid:34e22c00-1dd2-11b2-a1f5-0040d0b82e2d]/renderingcontrol/video/horizontalKeystone">~</value><value ref="/mediaRenderers[uuid:34e22c00-1dd2-11b2-a1f5-0040d0b82e2d]/isPauseAvailable">~</value><value ref="/mediaRenderers[uuid:34e22c00-1dd2-11b2-a1f5-0040d0b82e2d]/renderingcontrol/presets/selectPreset[state]">initial</value><value ref="/mediaServers[uuid:ab9cdfb4-e0f1-11db-84c5-0016018aacfc]/browsing/content/parentID">-1</value><value ref="/mediaRenderers[uuid:1e5e63ea-1dd2-11b2-8745-0019cb567d39]/renderingcontrol/chAudio[Master]/chVolumeDBMaxValue">~</value><value ref="/mediaRenderers[uuid:1e5e63ea-1dd2-11b2-8745-0019cb567d39]/renderingcontrol/video/brightness">~</value><value ref="/mediaServers[uuid:ab9cdfb4-e0f1-11db-84c5-0016018aacfc]/search[state]">initial</value><value ref="/mediaRenderers[uuid:526F6B75-536F-756E-6442-000D4B20069F]/renderingcontrol/video/sharpnessMaxValue">~</value><value ref="/mediaRenderers[uuid:526F6B75-536F-756E-6442-000D4B20069F]/renderingcontrol/chAudio[Master]/chLoudness">~</value><value ref="/mediaRenderers[uuid:34e22c00-1dd2-11b2-a1f5-0040d0b82e2d]/renderingcontrol/video/contrast">~</value><value ref="/mediaRenderers[uuid:34e22c00-1dd2-11b2-a1f5-0040d0b82e2d]/renderingcontrol/video/brightnessMaxValue">~</value><value ref="/mediaServers[uuid:0d99d258-9b59-44c1-8884-d75b61d3e04c]/mediaServerFriendlyName">VIDEOCON: wmplayer:</value><value ref="/mediaRenderers[uuid:526F6B75-536F-756E-6442-000D4B20069F]/isPauseAvailable">~</value><value ref="/mediaRenderers[uuid:1e5e63ea-1dd2-11b2-8745-0019cb567d39]/renderingcontrol/video/greenVideoGain">~</value><value ref="/mediaRenderers[uuid:526F6B75-536F-756E-6442-000D4B20069F]/renderingcontrol/video/greenVideoBlackLevelMaxValue">~</value><value ref="/mediaServers[uuid:ab9cdfb4-e0f1-11db-84c5-0016018aacfc]/mediaServerFriendlyName">HS-DHGLCFC: LinkStation</value><value ref="/mediaRenderers[uuid:1e5e63ea-1dd2-11b2-8745-0019cb567d39]/renderingcontrol/video/verticalKeystone">~</value><value ref="/mediaRenderers[uuid:1e5e63ea-1dd2-11b2-8745-0019cb567d39]/renderingcontrol/video/blueVideoBlackLevelMaxValue">~</value><value ref="/mediaServers[uuid:0d99d258-9b59-44c1-8884-d75b61d3e04c]/browsing/content/totalTracks">1</value><value ref="/mediaRenderers[uuid:34e22c00-1dd2-11b2-a1f5-0040d0b82e2d]/isStopAvailable">~</value><value ref="/mediaRenderers[uuid:526F6B75-536F-756E-6442-000D4B20069F]/renderingcontrol/video/contrastMaxValue">~</value><value ref="/mediaRenderers[uuid:34e22c00-1dd2-11b2-a1f5-0040d0b82e2d]/renderingcontrol/video/verticalKeystoneMinValue">~</value><value ref="/mediaServers[uuid:0d99d258-9b59-44c1-8884-d75b61d3e04c]/browsing/content/class">object.container</value><value ref="/mediaRenderers[uuid:34e22c00-1dd2-11b2-a1f5-0040d0b82e2d]/renderingcontrol/chAudio[Master]/chLoudness">~</value><value ref="/mediaServers[uuid:ab9cdfb4-e0f1-11db-84c5-0016018aacfc]/browsing/content/res">~</value><value ref="/mediaRenderers[uuid:1e5e63ea-1dd2-11b2-8745-0019cb567d39]/renderingcontrol/video/redVideoGainMaxValue">~</value><value ref="/mediaRenderers[uuid:526F6B75-536F-756E-6442-000D4B20069F]/renderingcontrol/video/verticalKeystoneMinValue">~</value><value ref="/mediaServers[uuid:0d99d258-9b59-44c1-8884-d75b61d3e04c]/sortCapabilities">dc:title,upnp:genre,upnp:album,dc:creator,res@size,res@duration,res@bitrate,dc:publisher,dc:language,upnp:originalTrackNumber,dc:date,upnp:producer,upnp:rating,upnp:actor,upnp:director,upnp:toc,dc:description,microsoft:year,microsoft:userRatingInStars,microsoft:userEffectiveRatingInStars,microsoft:userRating,microsoft:userEffectiveRating,microsoft:serviceProvider,microsoft:artistAlbumArtist,microsoft:artistPerformer,microsoft:artistConductor,microsoft:authorComposer,microsoft:authorOriginalLyricist,microsoft:authorWriter,microsoft:sourceUrl,upnp:userAnnotation</value><value ref="/mediaRenderers[uuid:526F6B75-536F-756E-6442-000D4B20069F]/renderingcontrol/video/blueVideoBlackLevel">~</value><value ref="/mediaServers[uuid:ab9cdfb4-e0f1-11db-84c5-0016018aacfc]/browsing/content/firstChildNode[state]">initial</value><value ref="/mediaServers[uuid:ab9cdfb4-e0f1-11db-84c5-0016018aacfc]/browsing/content/restricted">1</value><value ref="/mediaRenderers[uuid:1e5e63ea-1dd2-11b2-8745-0019cb567d39]/renderingcontrol/video/sharpnessMaxValue">~</value><value ref="/mediaRenderers[uuid:34e22c00-1dd2-11b2-a1f5-0040d0b82e2d]/renderingcontrol/video/colorTemperature">~</value><value ref="/mediaRenderers[uuid:34e22c00-1dd2-11b2-a1f5-0040d0b82e2d]/renderingcontrol/video/redVideoBlackLevelMaxValue">~</value><value ref="/mediaRenderers[uuid:1e5e63ea-1dd2-11b2-8745-0019cb567d39]/renderingcontrol/volumeMaxValue">~</value><value ref="/mediaServers[uuid:ab9cdfb4-e0f1-11db-84c5-0016018aacfc]/browsing/content/selectedNodeTitle">HS-DHGLCFC: LinkStation</value><value ref="/mediaRenderers[uuid:1e5e63ea-1dd2-11b2-8745-0019cb567d39]/renderingcontrol/video/brightnessMaxValue">~</value><value ref="/mediaRenderers[uuid:526F6B75-536F-756E-6442-000D4B20069F]/isStopAvailable">~</value><value ref="/mediaServers[uuid:0d99d258-9b59-44c1-8884-d75b61d3e04c]/browsing/content/selectedNodeTitle">Wurzel</value><value ref="/mediaRenderers[uuid:1e5e63ea-1dd2-11b2-8745-0019cb567d39]/renderingcontrol/presets/selectPreset[state]">initial</value><value ref="/mediaRenderers[uuid:34e22c00-1dd2-11b2-a1f5-0040d0b82e2d]/renderingcontrol/video/blueVideoGainMaxValue">~</value><value ref="/mediaRenderers[uuid:34e22c00-1dd2-11b2-a1f5-0040d0b82e2d]/isPlayAvailable">~</value><value ref="/connect[state]">initial</value><value ref="/mediaRenderers[uuid:526F6B75-536F-756E-6442-000D4B20069F]/isMediaRendererSelected">~</value><value ref="/mediaRenderers[uuid:34e22c00-1dd2-11b2-a1f5-0040d0b82e2d]/renderingcontrol/video/blueVideoGain">~</value><value ref="/mediaRenderers[uuid:34e22c00-1dd2-11b2-a1f5-0040d0b82e2d]/renderingcontrol/video/redVideoGainMaxValue">~</value><value ref="/mediaRenderers[uuid:526F6B75-536F-756E-6442-000D4B20069F]/renderingcontrol/video/sharpness">~</value><value ref="/mediaServers[uuid:0d99d258-9b59-44c1-8884-d75b61d3e04c]/isMediaServerSelected">~</value><value ref="/mediaServers[uuid:ab9cdfb4-e0f1-11db-84c5-0016018aacfc]/browsing/browseFilter">dc:title,dc:creator,res,res@duration,upnp:class,upnp:genre,upnp:artist,upnp:album,upnp:actor,duration</value><value ref="/mediaRenderers[uuid:1e5e63ea-1dd2-11b2-8745-0019cb567d39]/renderingcontrol/video/greenVideoGainMaxValue">~</value><value ref="/mediaRenderers[uuid:1e5e63ea-1dd2-11b2-8745-0019cb567d39]/renderingcontrol/video/horizontalKeystoneMinValue">~</value><value ref="/mediaRenderers[uuid:34e22c00-1dd2-11b2-a1f5-0040d0b82e2d]/renderingcontrol/video/colorTemperatureMaxValue">~</value><value ref="/mediaRenderers[uuid:1e5e63ea-1dd2-11b2-8745-0019cb567d39]/renderingcontrol/video/redVideoGain">~</value><value ref="/mediaServers[uuid:0d99d258-9b59-44c1-8884-d75b61d3e04c]/browsing/browseFilter">dc:title,dc:creator,res,res@duration,upnp:class,upnp:genre,upnp:artist,upnp:album,upnp:actor,duration</value><value ref="/mediaServers[uuid:0d99d258-9b59-44c1-8884-d75b61d3e04c]/browsing/browseSortCriteria"></value><value ref="/mediaRenderers[uuid:1e5e63ea-1dd2-11b2-8745-0019cb567d39]/isPauseAvailable">~</value><value ref="/mediaRenderers[uuid:1e5e63ea-1dd2-11b2-8745-0019cb567d39]/renderingcontrol/presets/presetNameList">FactoryDefaults</value><value ref="/mediaRenderers[uuid:34e22c00-1dd2-11b2-a1f5-0040d0b82e2d]/renderingcontrol/video/blueVideoBlackLevelMaxValue">~</value><value ref="/mediaRenderers[uuid:34e22c00-1dd2-11b2-a1f5-0040d0b82e2d]/mediaRendererFriendlyName">EZfetch Digital Media Player (EZfetch)</value><value ref="/mediaServers[uuid:0d99d258-9b59-44c1-8884-d75b61d3e04c]/browsing/content/refID">~</value><value ref="/mediaRenderers[uuid:526F6B75-536F-756E-6442-000D4B20069F]/renderingcontrol/chAudio[Master]/chVolumeDB">~</value><value ref="/mediaRenderers[uuid:1e5e63ea-1dd2-11b2-8745-0019cb567d39]/renderingcontrol/video/verticalKeystoneMaxValue">~</value><value ref="/mediaRenderers[uuid:1e5e63ea-1dd2-11b2-8745-0019cb567d39]/isStopAvailable">~</value><value ref="/mediaServers[uuid:ab9cdfb4-e0f1-11db-84c5-0016018aacfc]/browsing/content/totalTracks">1</value><value ref="/mediaRenderers[uuid:526F6B75-536F-756E-6442-000D4B20069F]/renderingcontrol/video/colorTemperatureMaxValue">~</value><value ref="/mediaRenderers[uuid:1e5e63ea-1dd2-11b2-8745-0019cb567d39]/renderingcontrol/video/sharpness">~</value></values>');
               
               case "mce.uis":
                  return('<values><value ref="/recordedTV/fastForwardRecordedTVPlayback/fastForwardRecTVSpeed">2x</value><value ref="/recordedTV/fastForwardRecordedTVPlayback[state]">initial</value></values>');

               default:
                  break;         
            }
            

         case "getUpdates":
            
            switch (messageParameters.socket) {
            
               case "tv":
                  
                  switch (org_myurc_urchttp_test_tv_iteration++ % 4) {
                     case 0:
                        return(
                           '<updates>\n' +
                           '   <value ref="/notifySubscription">active</value>\n' +
                           '</updates>\n' );
                     case 1:
                        return(
                           '<updates>\n' +
                           '   <value ref="/notifyPower">active</value>\n' +
                           '</updates>\n' );
                     case 2:
                        return(
                           '<updates>\n' +
                           '   <value ref="/notifyPower">inactive</value>\n' +
                           '</updates>\n' );
                     case 3:
                        return(
                           '<updates>\n' +
                           '   <value ref="/notifySubscription">inactive</value>\n' +
                           '</updates>\n' );
                     default:
                        break;
                  }

               case "testcase1":
                  
                  switch (org_myurc_urchttp_test_testcase1_iteration++) {

                     case 1:
                        return(
                           '<updates>\n' +
                           '   <value ref="/var0">new value for /var0</value>\n' +
                           '   <value ref="/var1[alpha]">new value for /var1[alpha]</value>\n' +
                           '   <value ref="/var1[beta]">new value for /var1[beta]</value>\n' +
                           '   <value ref="/var1[gamma]">new value for /var1[gamma]</value>\n' +
                           '   <value ref="/var2[alpha][1]">new value for /var2[alpha][1]</value>\n' +
                           '   <value ref="/var2[alpha][2]">new value for /var2[alpha][2]</value>\n' +
                           '   <value ref="/var2[beta][1]">new value for /var2[beta][1]</value>\n' +
                           '   <value ref="/var2[gamma][5]">new value for /var2[gamma][5]</value>\n' +
                           '   <value ref="/var2[gamma][2]">new value for /var2[gamma][2]</value>\n' +
                           '   <value ref="/var2[gamma][1]">new value for /var2[gamma][1]</value>\n' +
                           '   <value ref="/var3[alpha][1][apple]">new value for /var3[alpha][1][apple]</value>\n' +
                           '   <value ref="/var3[alpha][2][apple]">new value for /var3[alpha][2][apple]</value>\n' +
                           '   <value ref="/var3[beta][1][pear]">new value for /var3[beta][1][pear]</value>\n' +
                           '   <value ref="/var3[gamma][5][apple]">new value for /var3[gamma][5][apple]</value>\n' +
                           '   <value ref="/var3[gamma][2][kiwi]">new value for /var3[gamma][2][kiwi]</value>\n' +
                           '   <value ref="/var3[gamma][1][pear]">new value for /var3[gamma][1][pear]</value>\n' +
                           '   <value ref="/var3[gamma][1][apple]">new value for /var3[gamma][1][apple]</value>\n' +

                           '   <value ref="/set0/var00">new value for /set0/var00</value>\n' +
                           '   <value ref="/set0/var01[alpha]">new value for /set0/var01[alpha]</value>\n' +
                           '   <value ref="/set0/var01[beta]">new value for /set0/var01[beta]</value>\n' +
                           '   <value ref="/set0/var01[gamma]">new value for /set0/var01[gamma]</value>\n' +
                           '   <value ref="/set0/var02[alpha][1]">new value for /set0/var02[alpha][1]</value>\n' +
                           '   <value ref="/set0/var02[alpha][2]">new value for /set0/var02[alpha][2]</value>\n' +
                           '   <value ref="/set0/var02[beta][1]">new value for /set0/var02[beta][1]</value>\n' +
                           '   <value ref="/set0/var02[gamma][5]">new value for /set0/var02[gamma][5]</value>\n' +
                           '   <value ref="/set0/var02[gamma][2]">new value for /set0/var02[gamma][2]</value>\n' +
                           '   <value ref="/set0/var02[gamma][1]">new value for /set0/var02[gamma][1]</value>\n' +
                           '   <value ref="/set0/var03[alpha][1][apple]">new value for /set0/var03[alpha][1][apple]</value>\n' +
                           '   <value ref="/set0/var03[alpha][2][apple]">new value for /set0/var03[alpha][2][apple]</value>\n' +
                           '   <value ref="/set0/var03[beta][1][pear]">new value for /set0/var03[beta][1][pear]</value>\n' +
                           '   <value ref="/set0/var03[gamma][5][apple]">new value for /set0/var03[gamma][5][apple]</value>\n' +
                           '   <value ref="/set0/var03[gamma][2][kiwi]">new value for /set0/var03[gamma][2][kiwi]</value>\n' +
                           '   <value ref="/set0/var03[gamma][1][pear]">new value for /set0/var03[gamma][1][pear]</value>\n' +
                           '   <value ref="/set0/var03[gamma][1][apple]">new value for /set0/var03[gamma][1][apple]</value>\n' +
                           '   <value ref="/set0/set00/var000">new value for /set0/set00/var000</value>\n' +

                           '   <value ref="/set1[Madison]/var10">new value for /set1[Madison]/var10</value>\n' +
                           '   <value ref="/set1[Madison]/var11[alpha]">new value for /set1[Madison]/var11[alpha]</value>\n' +
                           '   <value ref="/set1[Madison]/var11[beta]">new value for /set1[Madison]/var11[beta]</value>\n' +
                           '   <value ref="/set1[Madison]/var11[gamma]">new value for /set1[Madison]/var11[gamma]</value>\n' +
                           '   <value ref="/set1[Madison]/var12[alpha][1]">new value for /set1[Madison]/var12[alpha][1]</value>\n' +
                           '   <value ref="/set1[Madison]/var12[alpha][2]">new value for /set1[Madison]/var12[alpha][2]</value>\n' +
                           '   <value ref="/set1[Madison]/var12[beta][1]">new value for /set1[Madison]/var12[beta][1]</value>\n' +
                           '   <value ref="/set1[Madison]/var12[gamma][5]">new value for /set1[Madison]/var12[gamma][5]</value>\n' +
                           '   <value ref="/set1[Madison]/var12[gamma][2]">new value for /set1[Madison]/var12[gamma][2]</value>\n' +
                           '   <value ref="/set1[Madison]/var12[gamma][1]">new value for /set1[Madison]/var12[gamma][1]</value>\n' +
                           '   <value ref="/set1[Madison]/var13[alpha][1][apple]">new value for /set1[Madison]/var13[alpha][1][apple]</value>\n' +
                           '   <value ref="/set1[Madison]/var13[alpha][2][apple]">new value for /set1[Madison]/var13[alpha][2][apple]</value>\n' +
                           '   <value ref="/set1[Madison]/var13[beta][1][pear]">new value for /set1[Madison]/var13[beta][1][pear]</value>\n' +
                           '   <value ref="/set1[Madison]/var13[gamma][5][apple]">new value for /set1[Madison]/var13[gamma][5][apple]</value>\n' +
                           '   <value ref="/set1[Madison]/var13[gamma][2][kiwi]">new value for /set1[Madison]/var13[gamma][2][kiwi]</value>\n' +
                           '   <value ref="/set1[Madison]/var13[gamma][1][pear]">new value for /set1[Madison]/var13[gamma][1][pear]</value>\n' +
                           '   <value ref="/set1[Madison]/var13[gamma][1][apple]">new value for /set1[Madison]/var13[gamma][1][apple]</value>\n' +

                           '   <value ref="/set1[Milwaukee]/var10">new value for /set1[Milwaukee]/var10</value>\n' +
                           '   <value ref="/set1[Milwaukee]/var11[alpha]">new value for /set1[Milwaukee]/var11[alpha]</value>\n' +
                           '   <value ref="/set1[Milwaukee]/var11[beta]">new value for /set1[Milwaukee]/var11[beta]</value>\n' +
                           '   <value ref="/set1[Milwaukee]/var11[gamma]">new value for /set1[Milwaukee]/var11[gamma]</value>\n' +
                           '   <value ref="/set1[Milwaukee]/var12[alpha][1]">new value for /set1[Milwaukee]/var12[alpha][1]</value>\n' +
                           '   <value ref="/set1[Milwaukee]/var12[alpha][2]">new value for /set1[Milwaukee]/var12[alpha][2]</value>\n' +
                           '   <value ref="/set1[Milwaukee]/var12[beta][1]">new value for /set1[Milwaukee]/var12[beta][1]</value>\n' +
                           '   <value ref="/set1[Milwaukee]/var12[gamma][5]">new value for /set1[Milwaukee]/var12[gamma][5]</value>\n' +
                           '   <value ref="/set1[Milwaukee]/var12[gamma][2]">new value for /set1[Milwaukee]/var12[gamma][2]</value>\n' +
                           '   <value ref="/set1[Milwaukee]/var12[gamma][1]">new value for /set1[Milwaukee]/var12[gamma][1]</value>\n' +
                           '   <value ref="/set1[Milwaukee]/var13[alpha][1][apple]">new value for /set1[Milwaukee]/var13[alpha][1][apple]</value>\n' +
                           '   <value ref="/set1[Milwaukee]/var13[alpha][2][apple]">new value for /set1[Milwaukee]/var13[alpha][2][apple]</value>\n' +
                           '   <value ref="/set1[Milwaukee]/var13[beta][1][pear]">new value for /set1[Milwaukee]/var13[beta][1][pear]</value>\n' +
                           '   <value ref="/set1[Milwaukee]/var13[gamma][5][apple]">new value for /set1[Milwaukee]/var13[gamma][5][apple]</value>\n' +
                           '   <value ref="/set1[Milwaukee]/var13[gamma][2][kiwi]">new value for /set1[Milwaukee]/var13[gamma][2][kiwi]</value>\n' +
                           '   <value ref="/set1[Milwaukee]/var13[gamma][1][pear]">new value for /set1[Milwaukee]/var13[gamma][1][pear]</value>\n' +
                           '   <value ref="/set1[Milwaukee]/var13[gamma][1][apple]">new value for /set1[Milwaukee]/var13[gamma][1][apple]</value>\n' +

                           '   <value ref="/set2[Madison][2006]/var20">new value for /set2[Madison][2006]/var20</value>\n' +
                           '   <value ref="/set2[Madison][2006]/var21[alpha]">new value for /set2[Madison][2006]/var21[alpha]</value>\n' +
                           '   <value ref="/set2[Madison][2006]/var21[beta]">new value for /set2[Madison][2006]/var21[beta]</value>\n' +
                           '   <value ref="/set2[Madison][2006]/var21[gamma]">new value for /set2[Madison][2006]/var21[gamma]</value>\n' +
                           '   <value ref="/set2[Madison][2006]/var22[alpha][1]">new value for /set2[Madison][2006]/var22[alpha][1]</value>\n' +
                           '   <value ref="/set2[Madison][2006]/var22[alpha][2]">new value for /set2[Madison][2006]/var22[alpha][2]</value>\n' +
                           '   <value ref="/set2[Madison][2006]/var22[beta][1]">new value for /set2[Madison][2006]/var22[beta][1]</value>\n' +
                           '   <value ref="/set2[Madison][2006]/var22[gamma][5]">new value for /set2[Madison][2006]/var22[gamma][5]</value>\n' +
                           '   <value ref="/set2[Madison][2006]/var22[gamma][2]">new value for /set2[Madison][2006]/var22[gamma][2]</value>\n' +
                           '   <value ref="/set2[Madison][2006]/var22[gamma][1]">new value for /set2[Madison][2006]/var22[gamma][1]</value>\n' +
                           '   <value ref="/set2[Madison][2006]/var23[alpha][1][apple]">new value for /set2[Madison][2006]/var23[alpha][1][apple]</value>\n' +
                           '   <value ref="/set2[Madison][2006]/var23[alpha][2][apple]">new value for /set2[Madison][2006]/var23[alpha][2][apple]</value>\n' +
                           '   <value ref="/set2[Madison][2006]/var23[beta][1][pear]">new value for /set2[Madison][2006]/var23[beta][1][pear]</value>\n' +
                           '   <value ref="/set2[Madison][2006]/var23[gamma][5][apple]">new value for /set2[Madison][2006]/var23[gamma][5][apple]</value>\n' +
                           '   <value ref="/set2[Madison][2006]/var23[gamma][2][kiwi]">new value for /set2[Madison][2006]/var23[gamma][2][kiwi]</value>\n' +
                           '   <value ref="/set2[Madison][2006]/var23[gamma][1][pear]">new value for /set2[Madison][2006]/var23[gamma][1][pear]</value>\n' +
                           '   <value ref="/set2[Madison][2006]/var23[gamma][1][apple]">new value for /set2[Madison][2006]/var23[gamma][1][apple]</value>\n' +

                           '   <value ref="/set2[Madison][2007]/var20">new value for /set2[Madison][2007]/var20</value>\n' +
                           '   <value ref="/set2[Madison][2007]/var21[alpha]">new value for /set2[Madison][2007]/var21[alpha]</value>\n' +
                           '   <value ref="/set2[Madison][2007]/var21[beta]">new value for /set2[Madison][2007]/var21[beta]</value>\n' +
                           '   <value ref="/set2[Madison][2007]/var21[gamma]">new value for /set2[Madison][2007]/var21[gamma]</value>\n' +
                           '   <value ref="/set2[Madison][2007]/var22[alpha][1]">new value for /set2[Madison][2007]/var22[alpha][1]</value>\n' +
                           '   <value ref="/set2[Madison][2007]/var22[alpha][2]">new value for /set2[Madison][2007]/var22[alpha][2]</value>\n' +
                           '   <value ref="/set2[Madison][2007]/var22[beta][1]">new value for /set2[Madison][2007]/var22[beta][1]</value>\n' +
                           '   <value ref="/set2[Madison][2007]/var22[gamma][5]">new value for /set2[Madison][2007]/var22[gamma][5]</value>\n' +
                           '   <value ref="/set2[Madison][2007]/var22[gamma][2]">new value for /set2[Madison][2007]/var22[gamma][2]</value>\n' +
                           '   <value ref="/set2[Madison][2007]/var22[gamma][1]">new value for /set2[Madison][2007]/var22[gamma][1]</value>\n' +
                           '   <value ref="/set2[Madison][2007]/var23[alpha][1][apple]">new value for /set2[Madison][2007]/var23[alpha][1][apple]</value>\n' +
                           '   <value ref="/set2[Madison][2007]/var23[alpha][2][apple]">new value for /set2[Madison][2007]/var23[alpha][2][apple]</value>\n' +
                           '   <value ref="/set2[Madison][2007]/var23[beta][1][pear]">new value for /set2[Madison][2007]/var23[beta][1][pear]</value>\n' +
                           '   <value ref="/set2[Madison][2007]/var23[gamma][5][apple]">new value for /set2[Madison][2007]/var23[gamma][5][apple]</value>\n' +
                           '   <value ref="/set2[Madison][2007]/var23[gamma][2][kiwi]">new value for /set2[Madison][2007]/var23[gamma][2][kiwi]</value>\n' +
                           '   <value ref="/set2[Madison][2007]/var23[gamma][1][pear]">new value for /set2[Madison][2007]/var23[gamma][1][pear]</value>\n' +
                           '   <value ref="/set2[Madison][2007]/var23[gamma][1][apple]">new value for /set2[Madison][2007]/var23[gamma][1][apple]</value>\n' +
                           '</updates>\n' );
                     
                     case 2:
                        return(
                           '<values>\n' +
                           '   <remove ref="/var1[gamma]" />\n' +
                           '</values>\n' );
                     
                     case 3:
                        return(
                           '<values>\n' +
                           '   <remove ref="/var2[alpha][1]" />\n' +
                           '</values>\n' );
                     
                     case 4:
                        return(
                           '<values>\n' +
                           '   <remove ref="/var3[beta][1][pear]" />\n' +
                           '</values>\n' );
                     
                     case 5:
                        return(
                           '<values>\n' +
                           '   <remove ref="/set0/var01[gamma]" />\n' +
                           '</values>\n' );
                     
                     case 6:
                        return(
                           '<values>\n' +
                           '   <remove ref="/set0/var02[alpha][1]" />\n' +
                           '</values>\n' );
                     
                     case 7:
                        return(
                           '<values>\n' +
                           '   <remove ref="/set0/var03[beta][1][pear]" />\n' +
                           '</values>\n' );
                     
                     case 8:
                        return(
                           '<values>\n' +
                           '   <remove ref="/set1[Madison]" />\n' +
                           '</values>\n' );
                     
                     case 9:
                        return(
                           '<values>\n' +
                           '   <remove ref="/set2[Madison][2007]" />\n' +
                           '</values>\n' );
                     
                     case 10:
                        return(
                           '<updates>\n' +
                           '   <value ref="/ntf0">active</value>\n' +
                           '   <value ref="/set0/ntf00">active</value>\n' +
                           '   <value ref="/set1[Madison]/ntf10">active</value>\n' +
                           '   <value ref="/set1[Madison]/set00/ntf000">active</value>\n' +
                           '</updates>\n' );
                           
                     default:
                        return('');
                  }

                     
               case "testcase2":
                  
                  switch (org_myurc_urchttp_test_testcase2_iteration++) {

                     case 1:
                        return(
                           '<values>\n' +
                           '   <remove ref="/cmd1b[1]" />\n' +
                           '</values>\n' );
                     
                     case 2:
                        return(
                           '<values>\n' +
                           '   <remove ref="/cmd1b[2]" />\n' +
                           '</values>\n' );
                     
                     case 3:
                        return(
                           '<values>\n' +
                              '   <add ref="/cmd1b[1]/param1b-o1">again value for /cmd1b[1]/param1b-o1</add>\n' +
                              '   <add ref="/cmd1b[1]/param1b-o2">again value for /cmd1b[1]/param1b-o2</add>\n' +
                           '</values>\n' );
                     
                     case 4:
                        return(
                           '<values>\n' +
                              '   <add ref="/cmd1b[2]/param1b-o1">again value for /cmd1b[2]/param1b-o1</add>\n' +
                              '   <add ref="/cmd1b[2]/param1b-o2">again value for /cmd1b[2]/param1b-o2</add>\n' +
                           '</values>\n' );
                     
                     case 5:
                        return(
                           '<values>\n' +
                           '   <remove ref="/cmd1c[Milwaukee]" />\n' +
                           '</values>\n' );
                     
                     case 6:
                        return(
                           '<values>\n' +
                           '   <remove ref="/cmd1d[Milwaukee]" />\n' +
                           '</values>\n' );
                     
                     case 7:
                        return(
                           '<values>\n' +
                           '   <remove ref="/cmd1e[Madison]" />\n' +
                           '</values>\n' );
                     
                     case 8:
                        return(
                           '<values>\n' +
                           '   <remove ref="/cmd1f[Madison]" />\n' +
                           '</values>\n' );
                     
                     case 9:
                        return(
                           '<values>\n' +
                           '   <remove ref="/cmd2a[Madison][spring]" />\n' +
                           '</values>\n' );
                     
                     case 10:
                        return(
                           '<values>\n' +
                           '   <remove ref="/set0/cmd01d[orange]" />\n' +
                           '</values>\n' );
                     
                     case 11:
                        return(
                           '<values>\n' +
                           '   <remove ref="/set0/cmd02d[apple][2006]" />\n' +
                           '</values>\n' );
                     
                     case 12:
                        return(
                           '<values>\n' +
                           '   <remove ref="/set1[Madison]" />\n' +
                           '</values>\n' );
                     

                     case 13:
                        return(
                           '<values>\n' +
                           '   <remove ref="/set1[Appleton]" />\n' +
                           '</values>\n' );
                     

                     case 14:
                        return(
                           '<values>\n' +
                           '   <add ref="/set1[Madison]/cmd10d[state]">value for /set1[Madison]/cmd10d[state]</add>\n' +
                           '   <add ref="/set1[Madison]/cmd10d/param10d-o1">value for /set1[Madison]/cmd10d/param10d-o1</add>\n' +
                           '   <add ref="/set1[Madison]/cmd10d/param10d-o2">value for /set1[Madison]/cmd10d/param10d-o2</add>\n' +
                           '   <add ref="/set1[Madison]/cmd11d[apple][state]">value for /set1[Madison]/cmd11d[state]</add>\n' +
                           '   <add ref="/set1[Madison]/cmd11d[apple]/param11d-o1">value for /set1[Madison]/cmd11d[apple]/param11d-o1</add>\n' +
                           '   <add ref="/set1[Madison]/cmd11d[apple]/param11d-o2">value for /set1[Madison]/cmd11d[apple]/param11d-o2</add>\n' +
                           '   <add ref="/set1[Madison]/cmd11d[orange][state]">value for /set1[Madison]/cmd11d[orange][state]</add>\n' +
                           '   <add ref="/set1[Madison]/cmd11d[lemon]/param11d-o1">value for /set1[Madison]/cmd11d[lemon]/param11d-o1</add>\n' +
                           '   <add ref="/set1[Madison]/cmd11d[orange]/param11d-o2">value for /set1[Madison]/cmd11d[orange]/param11d-o2</add>\n' +
                           '   <add ref="/set1[Madison]/cmd12d[apple][2006][state]">value for /set1[Madison]/cmd12d[apple][2006][state]</add>\n' +
                           '   <add ref="/set1[Madison]/cmd12d[apple][2006]/param12d-o1">value for /set1[Madison]/cmd12d[apple][2006]/param12d-o1</add>\n' +
                           '   <add ref="/set1[Madison]/cmd12d[apple][2007]/param12d-o2">value for /set1[Madison]/cmd12d[apple][2007]/param12d-o2</add>\n' +
                           '   <add ref="/set1[Madison]/cmd12d[orange][2006][state]">value for /set1[Madison]/cmd12d[orange][2006][state]</add>\n' +
                           '   <add ref="/set1[Madison]/cmd12d[lemon][2006]/param12d-o1">value for /set1[Madison]/cmd12d[lemon][2006]/param12d-o1</add>\n' +
                           '   <add ref="/set1[Madison]/cmd12d[orange][2006]/param12d-o2">value for /set1[Madison]/cmd12d[orange][2006]/param12d-o2</add>\n' +
                           '</values>\n' );


                     case 15:
                        return(
                           '<values>\n' +
                           '   <add ref="/set1[Appleton]/cmd10d[state]">value for /set1[Appleton]/cmd10d[state]</add>\n' +
                           '   <add ref="/set1[Appleton]/cmd10d/param10d-o1">value for /set1[Appleton]/cmd10d/param10d-o1</add>\n' +
                           '   <add ref="/set1[Appleton]/cmd10d/param10d-o2">value for /set1[Appleton]/cmd10d/param10d-o2</add>\n' +
                           '   <add ref="/set1[Appleton]/cmd11d[apple][state]">value for /set1[Appleton]/cmd11d[apple][state]</add>\n' +
                           '   <add ref="/set1[Appleton]/cmd11d[apple]/param11d-o1">value for /set1[Appleton]/cmd11d[apple]/param11d-o1</add>\n' +
                           '   <add ref="/set1[Appleton]/cmd11d[apple]/param11d-o2">value for /set1[Appleton]/cmd11d[apple]/param11d-o2</add>\n' +
                           '   <add ref="/set1[Appleton]/cmd11d[orange][state]">value for /set1[Appleton]/cmd11d[orange][state]</add>\n' +
                           '   <add ref="/set1[Appleton]/cmd11d[lemon]/param11d-o1">value for /set1[Appleton]/cmd11d[lemon]/param11d-o1</add>\n' +
                           '   <add ref="/set1[Appleton]/cmd11d[orange]/param11d-o2">value for /set1[Appleton]/cmd11d[orange]/param11d-o2</add>\n' +
                           '   <add ref="/set1[Appleton]/cmd12d[apple][2006][state]">value for /set1[Appleton]/cmd12d[apple][2006][state]</add>\n' +
                           '   <add ref="/set1[Appleton]/cmd12d[apple][2006]/param12d-o1">value for /set1[Appleton]/cmd12d[apple][2006]/param12d-o1</add>\n' +
                           '   <add ref="/set1[Appleton]/cmd12d[apple][2007]/param12d-o2">value for /set1[Appleton]/cmd12d[apple][2007]/param12d-o2</add>\n' +
                           '   <add ref="/set1[Appleton]/cmd12d[orange][2006][state]">value for /set1[Appleton]/cmd12d[orange][2006][state]</add>\n' +
                           '   <add ref="/set1[Appleton]/cmd12d[lemon][2006]/param12d-o1">value for /set1[Appleton]/cmd12d[lemon][2006]/param12d-o1</add>\n' +
                           '   <add ref="/set1[Appleton]/cmd12d[orange][2006]/param12d-o2">value for /set1[Appleton]/cmd12d[orange][2006]/param12d-o2</add>\n' +
                           '</values>\n' );


                     default:
                        return('');
                  }


               default:
                  break;         
            }


         case "setValues":
            
            switch (messageParameters.socket) {
            
               case "mce.uis":
                  return '<updates/>';
            
               default:
                  // For <set>: Confirm requested value with <value>.
                  // For <add>: Confirm requested value with <add>.
                  // For <invoke>: Do nothing.
                  // For <ack>: Set state back to "inactive" with <value>.
                  // For <remove>: Confirm with <remove>.
                  var requestDoc = org_myurc_lib_toXmlDoc(postData);
                  var result = "<updates>\n";
                  var nodes;

                  nodes = org_myurc_lib_selectChildren(requestDoc.documentElement, "set");
                  for (var i=0; i<nodes.length; i++) {
                     var ref = nodes[i].getAttribute("ref");
                     var value = org_myurc_lib_getTextContent(nodes[i]);
                     result += "<value ref=\"" + ref + "\">" + (value === undefined ? "" : value) + "</value>\n";
                  }
                  
                  nodes = org_myurc_lib_selectChildren(requestDoc.documentElement, "add");
                  for (var i=0; i<nodes.length; i++) {
                     var ref = nodes[i].getAttribute("ref");
                     var value = org_myurc_lib_getTextContent(nodes[i]);
                     result += "<add ref=\"" + ref + "\">" + (value === undefined ? "" : value) + "</value>\n";
                  }

                  nodes = org_myurc_lib_selectChildren(requestDoc.documentElement, "ack");
                  for (var i=0; i<nodes.length; i++) {
                     var ref = nodes[i].getAttribute("ref");
                     result += "<value ref=\"" + ref + "\">" + "inactive" + "</value>\n";
                  }
                  
                  nodes = org_myurc_lib_selectChildren(requestDoc.documentElement, "remove");
                  for (var i=0; i<nodes.length; i++) {
                     var ref = nodes[i].getAttribute("ref");
                     result += "<remove ref=\"" + ref + " \" />\n";
                  }
                  
                  result += "</updates>\n";
                  return result;
            }
            
         case "GetResources":
            
            switch (messageParameters.socket) {
               default:
                  // Sorry, no resources provided as test data. Return as many empty <resource> tags as requested.
                  var requestDoc = org_myurc_lib_toXmlDoc(postData);
                  var resourceNodes = org_myurc_lib_selectChildren(requestDoc.documentElement, "resource");
                  var result = "<resources>\n";
                  for (var i=0; i<resourceNodes.length; i++)
                     result += "<resource />\n";
                  result += "</resources>\n";
                  return result;
            }
            
         case "GetDocument":
            
            var urlComps = org_myurc_lib_getUrlComponents(messageParameters.args.url);
            switch (urlComps.path) {

               case "ac/ac.uis":
                  return(
                     '<?xml version="1.0" encoding="UTF-8"?>\n' +
                     '\n' +
                     '<!-- \n' +
                     '  User Interface Socket Description (based on ISO/IEC 24752-2) \n' +
                     '  for CHAIN air conditioner device. \n' +
                     '  This User Interface Socket Description is tailored \n' +
                     '  to the Siemens serve@Home mobile air conditioner PA2101HM. \n' +
                     '-->\n' +
                     '\n' +
                     '<uiSocket \n' +
                     '  about="http://res.i2home.org/chain/ac/socket"\n' +
                     '  id="socket" \n' +
                     '  xmlns="http://myurc.org/ns/uisocketdesc" \n' +
                     '  xmlns:uis="http://myurc.org/ns/uisocketdesc"\n' +
                     '  xmlns:types="http://res.i2home.org/types/" \n' +
                     '  xmlns:dc="http://purl.org/dc/elements/1.1/" \n' +
                     '  xmlns:dcterms="http://purl.org/dc/terms/" \n' +
                     '  xmlns:xsd="http://www.w3.org/2001/XMLSchema" \n' +
                     '  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" \n' +
                     '  xsi:schemaLocation="http://myurc.org/ns/uisocketdesc http://purl.org/dc/elements/1.1/ http://res.i2home.org/types/ http://localhost/UCH/webclient/types.xsd http://dublincore.org/schemas/xmls/qdc/2006/01/06/dc.xsd http://purl.org/dc/terms/ http://dublincore.org/schemas/xmls/qdc/2006/01/06/dcterms.xsd">\n' +
                     '\n' +
                     '  <dc:creator>Juergen Goerlich, Siemens AG</dc:creator>\n' +
                     '\n' +
                     '  <dc:publisher>Siemens AG, Germany</dc:publisher>\n' +
                     '  <dc:rights>Copyright 2008, Siemens AG</dc:rights>\n' +
                     '  <dc:title xml:lang="en">User Interface Socket Description for the server@home mobile air conditioner.</dc:title>\n' +
                     '  <dcterms:conformsTo>http://myurc.org/iso24752-2/2007</dcterms:conformsTo>\n' +
                     '  <dcterms:modified>2008-01-11</dcterms:modified>\n' +
                     '\n' +
                     '  <!-- ===================================================================== -->\n' +
                     '  <!-- Variables                                                             -->\n' +
                     '  <!-- ===================================================================== -->\n' +
                     '\n' +
                     '  <variable id="remoteEnabled" type="xsd:boolean">\n' +
                     '    <dc:description>\n' +
                     '      Indicates, whether the remote control functionality of the device is\n' +
                     '      activated or not (i.e. device is in serve@Home mode or not).\n' +
                     '    </dc:description>\n' +
                     '    <dependency>\n' +
                     '      <relevant>true()</relevant>\n' +
                     '      <write>false()</write>\n' +
                     '    </dependency>\n' +
                     '  </variable>\n' +
                     '\n' +
                     '  <variable id="deviceState" type="types:powerModeType">\n' +
                     '    <dc:description>\n' +
                     '      Current powers state of the device indicating whether the device is\n' +
                     '      switched on or off (see definition of type powerModeType).\n' +
                     '    </dc:description>\n' +
                     '    <dependency>\n' +
                     '      <relevant>true()</relevant>\n' +
                     '      <write>true()</write> <!-- Writing permission should be false() but is temporary set to true() for a quick-and-dirty hack -->\n' +
                     '    </dependency>\n' +
                     '  </variable>\n' +
                     '\n' +
                     '  <variable id="deviceMode" type="deviceModeType">\n' +
                     '    <dc:description>\n' +
                     '      Current mode of the device (see definition of type deviceModeType).\n' +
                     '    </dc:description>\n' +
                     '    <dependency>\n' +
                     '      <relevant>true()</relevant>\n' +
                     '      <write>false()</write>\n' +
                     '    </dependency>\n' +
                     '  </variable>\n' +
                     '\n' +
                     '  <variable id="programmeChoice" type="programmeChoiceType">\n' +
                     '    <dependency>\n' +
                     '      <relevant>not(value("deviceState") eq "OFF")</relevant>\n' +
                     '      <write>not(value("deviceState") eq "OFF")</write>\n' +
                     '    </dependency>\n' +
                     '    <dc:description>\n' +
                     '      Currently selected programme (see definition of type\n' +
                     '      "programmeChoiceType").\n' +
                     '    </dc:description>\n' +
                     '  </variable>\n' +
                     '\n' +
                     '  <variable id="targetFanSpeed" type="fanSpeedType">\n' +
                     '    <dependency>\n' +
                     '      <relevant>not(value("deviceState") eq "OFF")</relevant>\n' +
                     '      <write>not(value("deviceState") eq "OFF")</write>\n' +
                     '    </dependency>\n' +
                     '    <dc:description>\n' +
                     '      Current phase of the fan-mode, while air conditioner is running (see\n' +
                     '      definition of type fanSpeedType).\n' +
                     '    </dc:description>\n' +
                     '  </variable>\n' +
                     '\n' +
                     '  <variable id="targetTemp" type="tempType">\n' +
                     '    <dependency>\n' +
                     '      <relevant>not(value("deviceState") eq "OFF")</relevant>\n' +
                     '      <write>not(value("deviceState") eq "OFF")</write>\n' +
                     '      <minInclusive>16</minInclusive>\n' +
                     '      <maxInclusive>30</maxInclusive>\n' +
                     '    </dependency>\n' +
                     '    <dc:description>\n' +
                     '      The user chosen target temperature in degree Celsius.\n' +
                     '    </dc:description>\n' +
                     '  </variable>\n' +
                     '\n' +
                     '  <variable id="currentTemp" type="tempType">\n' +
                     '    <dependency>\n' +
                     '      <relevant>true()</relevant>\n' +
                     '      <write>false()</write>\n' +
                     '    </dependency>\n' +
                     '    <dc:description>\n' +
                     '      Current room temperature in degree Celsius.\n' +
                     '    </dc:description>\n' +
                     '  </variable>\n' +
                     '\n' +
                     '  <variable id="stopTime" type="xsd:time">\n' +
                     '    <dependency>\n' +
                     '      <relevant>not(value("deviceState") eq "OFF")</relevant>\n' +
                     '      <write>not(value("deviceState") eq "OFF")</write>\n' +
                     '    </dependency>\n' +
                     '    <dc:description>\n' +
                     '      Device stop time in minutes from 00:00h.\n' +
                     '    </dc:description>\n' +
                     '  </variable>\n' +
                     '\n' +
                     '  <variable id="startTime" type="xsd:time">\n' +
                     '    <dependency>\n' +
                     '      <relevant>not(value("deviceState") eq "OFF")</relevant>\n' +
                     '      <write>not(value("deviceState") eq "OFF")</write>\n' +
                     '    </dependency>\n' +
                     '    <dc:description>\n' +
                     '      Device start time in minutes from 00:00h.\n' +
                     '    </dc:description>\n' +
                     '  </variable>\n' +
                     '\n' +
                     '  <variable id="deviceType" type="xsd:string">\n' +
                     '    <dc:description>\n' +
                     '      Device type (order ID of the manufacturer namely, e.g. PA2101HM).\n' +
                     '    </dc:description>\n' +
                     '    <dependency>\n' +
                     '      <relevant>true()</relevant>\n' +
                     '      <write>false()</write>\n' +
                     '    </dependency>\n' +
                     '  </variable>\n' +
                     '  \n' +
                     '  <variable id="deviceName" type="xsd:string">\n' +
                     '    <dc:description>\n' +
                     '      Device name.\n' +
                     '    </dc:description>\n' +
                     '    <dependency>\n' +
                     '      <relevant>not(value("deviceState") eq "OFF")</relevant>\n' +
                     '      <write>not(value("deviceState") eq "OFF")</write>\n' +
                     '    </dependency>\n' +
                     '  </variable>\n' +
                     '\n' +
                     '  <!-- ===================================================================== -->\n' +
                     '  <!-- Notifications                                                         -->\n' +
                     '  <!-- ===================================================================== -->\n' +
                     '\n' +
                     '  <notify id="changeFilterAlert" category="alert">\n' +
                     '    <dc:description>\n' +
                     '      The device sends this notification to inform the user that the\n' +
                     '      filter needs to be changed.\n' +
                     '    </dc:description>\n' +
                     '  </notify>\n' +
                     '\n' +
                     '  <notify id="condensationContainerFullAlert" category="alert">\n' +
                     '    <dc:description>\n' +
                     '      The device sends this notification to inform the user that the\n' +
                     '      condensation container is full and needs to be emptied.\n' +
                     '    </dc:description>\n' +
                     '  </notify>\n' + 
                     '\n' +
                     '  <!-- ===================================================================== -->\n' +
                     '  <!-- Types                                                                 -->\n' +
                     '  <!-- ===================================================================== -->\n' +
                     '\n' +
                     '  <schema xmlns="http://www.w3.org/2001/XMLSchema">\n' +
                     '\n' +
                     '    <simpleType name="deviceModeType" id="idDeviceModeType">\n' +
                     '      <annotation>\n' +
                     '        <documentation>\n' +
                     '          Device mode type\n' +
                     '        </documentation>\n' +
                     '      </annotation>\n' +
                     '      <restriction base="xsd:string">\n' +
                     '        <enumeration value="ONLINE"/>\n' +
                     '        <enumeration value="OFFLINE"/>\n' +
                     '        <enumeration value="INITIALIZING"/>\n' +
                     '      </restriction>\n' +
                     '    </simpleType>\n' +
                     '\n' +
                     '    <simpleType name="programmeChoiceType" id="idProgrammeChoiceType">\n' +
                     '      <annotation>\n' +
                     '        <documentation>\n' +
                     '          Device programme type\n' +
                     '        </documentation>\n' +
                     '      </annotation>\n' +
                     '      <restriction base="xsd:string">\n' +
                     '        <enumeration value="COOLING"/>\n' +
                     '        <enumeration value="AIR_PURIFICATION"/>\n' +
                     '        <enumeration value="DEHUMIDIFICATION"/>\n' +
                     '      </restriction>\n' +
                     '    </simpleType>\n' +
                     '\n' +
                     '    <simpleType name="fanSpeedType" id="idFanSpeedType">\n' +
                     '      <annotation>\n' +
                     '        <documentation>\n' +
                     '          Device fan speed type\n' +
                     '        </documentation>\n' +
                     '      </annotation>\n' +
                     '      <restriction base="xsd:string">\n' +
                     '        <enumeration value="AUTO_MAX"/> <!-- Autom. max -->\n' +
                     '        <enumeration value="AUTO_MIN"/> <!-- Autom. min -->\n' +
                     '        <enumeration value="LEVEL_1"/>  <!-- Level 1 (20%) -->\n' +
                     '        <enumeration value="LEVEL_2"/>  <!-- Level 2 (40%) -->\n' +
                     '        <enumeration value="LEVEL_3"/>  <!-- Level 3 (60%) -->\n' +
                     '        <enumeration value="LEVEL_4"/>  <!-- Level 4 (80%) -->\n' +
                     '        <enumeration value="LEVEL_5"/>  <!-- Level 5 (100%) -->\n' +
                     '      </restriction>\n' +
                     '    </simpleType>\n' +
                     '\n' +
                     '    <simpleType name="tempType" id="idTempType">\n' +
                     '      <annotation>\n' +
                     '        <documentation>\n' +
                     '          Temperature type\n' +
                     '        </documentation>\n' +
                     '      </annotation>\n' +
                     '      <restriction base="xsd:integer"/>\n' +
                     '    </simpleType>\n' +
                     '\n' +
                     '  </schema>\n' +
                     '\n' +
                     '</uiSocket>' );   


               case "tv/tv.uis":
                  return(
                     '<uiSocket about="http://res.i2home.org/tv/tv_tuner.uisocket" id="socket" xmlns="http://myurc.org/ns/uisocketdesc" xmlns:uis="http://myurc.org/ns/uisocketdesc" xmlns:types="http://res.i2home.org/types/" xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://myurc.org/ns/uisocketdesc http://myurc.org/ns/uisocketdesc http://res.i2home.org/types/ http://localhost/UCH/webclient/types.xsd http://purl.org/dc/elements/1.1/ http://dublincore.org/schemas/xmls/qdc/2006/01/06/dc.xsd http://purl.org/dc/terms/ http://dublincore.org/schemas/xmls/qdc/2006/01/06/dcterms.xsd">\n' +
                     '	<dc:creator>Carlo Moritz G&#246;llner, Aitor Moreno, Eduardo Carrasco</dc:creator>\n' +
                     '	<dc:publisher>VICOMTech, Spain</dc:publisher>\n' +
                     '	<dc:rights>Copyright 2006, VICOMTech</dc:rights>\n' +
                     '	<dcterms:conformsTo>http://myurc.org/iso24752-2/2007</dcterms:conformsTo>\n' +
                     '	<dcterms:modified>2007-03-15</dcterms:modified>\n' +
                     '	<dcterms:modified>2007-07-17</dcterms:modified>\n' +
                     '	\n' +
                     '\n' +
                     '	<!-- Part 1 of TV tuner socket - set of variables for power mode -->\n' +
                     '	<variable id="powerMode" type="types:powerModeType">\n' +
                     '		<dependency>\n' +
                     '			<relevant>not(value("powerMode") eq "OFF")</relevant>\n' +
                     '			<write>not(value("powerMode") eq "OFF")</write> \n' +
                     '		</dependency>\n' +
                     '		<dc:description xml:lang="en">\n' +
                     '			Part 1 of TV tuner socket - power mode\n' +
                     '			\n' +
                     '			This socket element allows to adjust the power mode of the device. \n' +
                     '			\n' +
                     '			The powerModeType is a string enumeration "ON", "STANDBY" and "OFF":\n' +
                     '			 - "ON" corresponds to an activated TV tuner device.\n' +
                     '			 - "STANDBY" corresponds to a deactivated TV tuner that may still be awaken from the network.\n' +
                     '			 - "OFF" corresponds to a shut down TV tuner device that has to be switched on manually\n' +
                     '			 \n' +
                     '			If the device is "OFF" and the user tries to interact, the UI could show an appropiate\n' +
                     '			message: "Arnost, you have to switch on the set-top box manually, first." This would require\n' +
                     '			a persistent TA module though, which is active even when the TV tuner is completely "OFF".\n' +
                     '\n' +
                     '		</dc:description>\n' +
                     '	</variable>\n' +
                     '\n' +
                     '	<!-- Part 2 of TV tuner socket - set of variables for channel selection -->\n' +
                     '	<variable id="activeChannel" type="channelType">\n' +
                     '			<dependency>\n' +
                     '				<relevant>value("powerMode") eq "ON"</relevant>\n' +
                     '				<write>value("powerMode") eq "ON"</write> \n' +
                     '			</dependency>\n' +
                     '			<selection closed="true">\n' +
                     '				<selectionSetDynamic id="currentChannels" varRef="channelList"/>\n' +
                     '			</selection>\n' +
                     '			<dc:description xml:lang="en">\n' +
                     '				Part 2 of TV tuner socket\n' +
                     '			 \n' +
                     '				This variable may be used to set the active channel shown by the TV.\n' +
                     '				\n' +
                     '				A channel is identified by a unique, and possibly human readable string id (to be decided).\n' +
                     '				The variable may only be written if the power mode of the device is "ON". \n' +
                     '\n' +
                     '				\n' +
                     '				Before switching the channel, the target adaptor saves the current channel to "previousChannel"\n' +
                     '				in order to allow to return to the previously active channel.\n' +
                     '				\n' +
                     '				The string id for a channel shall be the same as in the EPG socket to\n' +
                     '				provide a direct manner of interoperability between the TV tuner and EPG socket.\n' +
                     '				It derives from uis:stringListItem and thus must not contain whitespace.\n' +
                     '				\n' +
                     '				The string id may be used to fetch long labels and icons from a resource sheet.\n' +
                     '			</dc:description>\n' +
                     '		</variable><variable id="channelList" type="uis:stringList" final="true">\n' +
                     '			<dependency>\n' +
                     '				<relevant>value("powerMode") eq "ON"</relevant>\n' +
                     '				<write>false()</write>\n' +
                     '			</dependency>\n' +
                     '			<dc:description xml:lang="en">\n' +
                     '				Part 2 of TV tuner socket\n' +
                     '			\n' +
                     '				This variable holds a list of ids of channel that the user may select as active channel.\n' +
                     '				\n' +
                     '				The UI may also show numbers to the users, using and index into this list.\n' +
                     '				The variable is marked as final, since the channel list may not be modified at runtime.\n' +
                     '				\n' +
                     '				Example: "ARD ZDF SAT-1 ARTE"\n' +
                     '			</dc:description>\n' +
                     '		</variable><variable id="previousChannel" type="channelType">\n' +
                     '			<dependency>\n' +
                     '				<relevant>value("powerMode") eq "ON"</relevant>\n' +
                     '				<write>value("powerMode") eq "ON"</write>\n' +
                     '			</dependency>	\n' +
                     '			<dc:description xml:lang="en">\n' +
                     '				Part 2 of TV tuner socket\n' +
                     '				\n' +
                     '				Contains the channel id of the previously selected channel.\n' +
                     '				\n' +
                     '				This id could be used by UIs in order to fetch a label or icon for the widget of the\n' +
                     '				command "selectPreviousChannelSlot"	from a resource sheet, instead of using a static\n' +
                     '				label such as "previous Channel".\n' +
                     '			</dc:description>\n' +
                     '		</variable><command id="selectPreviousChannel" type="uis:basicCommand">\n' +
                     '			<dependency>\n' +
                     '				<relevant>value("powerMode") eq "ON"</relevant>\n' +
                     '				<write>value("powerMode") eq "ON"</write>\n' +
                     '			</dependency>\n' +
                     '			<param idref="activeChannel" dir="inout"/>\n' +
                     '			<param idref="previousChannel" dir="inout"/>\n' +
                     '			<dc:description xml:lang="en">\n' +
                     '				Part 2 of TV tuner socket\n' +
                     '				\n' +
                     '				Command for switching back to the previously selected channel.\n' +
                     '				This command switches the values of activeChannel and previousChannel socket variables.\n' +
                     '			</dc:description>\n' +
                     '		</command><variable id="channelStream" type="uis:multiMediaStreamOut" optional="true">\n' +
                     '			<dependency>\n' +
                     '				<relevant>false()</relevant>\n' +
                     '				<write>false()</write>\n' +
                     '			</dependency>	\n' +
                     '			<dc:description xml:lang="en">\n' +
                     '				Part 2 of TV tuner socket\n' +
                     '				\n' +
                     '				A TV tuner may expose a multi media stream to URCs, i.e. the TV tuner output.\n' +
                     '				\n' +
                     '				The multi media stream according to ISO-24752 includes video and audio.\n' +
                     '				In the future we might want to add MIME types using the dc:format tag.\n' +
                     '				This is an optional variable since streaming is not part of a user scenario, yet.\n' +
                     '			</dc:description>		\n' +
                     '		</variable> <!-- end set id="channelSelection" -->\n' +
                     '\n' +
                     '	<!-- Part 3 of TV tuner socket - set of variables for volume control (volume level, mute) -->\n' +
                     '	<variable id="volume" type="volumeType">\n' +
                     '			<dependency>\n' +
                     '				<relevant>value("powerMode") eq "ON"</relevant>\n' +
                     '				<write>value("powerMode") eq "ON"</write>\n' +
                     '	<!--<write>not(value("mute")) and (value("powerMode") eq "ON")</write>-->\n' +
                     '			</dependency>\n' +
                     '			<dc:description xml:lang="en">\n' +
                     '				Part 3 of TV tuner socket\n' +
                     '				\n' +
                     '				Volume of the TV audio output may be set in range 0 to 100 (inclusive bounds), stepsize 1. \n' +
                     '				May only be set if the device is "ON".\n' +
                     '				\n' +
                     '				Volume levels in dB  (UPnP AV features dBVolume) could also be interesting for warnings\n' +
                     '				or diagnostics: "Warning, your TV volume is too high, it will disturb your neighbors."\n' +
                     '				(we need a better use case here)\n' +
                     '				\n' +
                     '				Or one could monitor the volume setting over a longer time (months or so) to diagnose\n' +
                     '				a possible hearing impairment. For these scenarios, dB values would be needed.\n' +
                     '				However, volume levels usually define line out voltages, so the actual volume is set by\n' +
                     '				an amplifier outside of the TV tuner.\n' +
                     '				\n' +
                     '				When the volume is changed, the mute is deactivated (if it was activated)\n' +
                     '			</dc:description>\n' +
                     '		</variable><variable id="mute" type="xsd:boolean">\n' +
                     '			<dependency>\n' +
                     '				<relevant>value("powerMode") eq "ON"</relevant>\n' +
                     '				<write>value("powerMode") eq "ON"</write>\n' +
                     '			</dependency>\n' +
                     '			<dc:description xml:lang="en">\n' +
                     '\n' +
                     '				Part 3 of TV tuner socket\n' +
                     '				\n' +
                     '				Activates and deactivates muted volume. \n' +
                     '				\n' +
                     '				This is a variable and not a command, since the mute state has to be available for usage\n' +
                     '				within dependency expressions of other variables and commands.\n' +
                     '			</dc:description>\n' +
                     '		</variable> <!-- end set id="volumeSettings" -->\n' +
                     '	\n' +
                     '	<!-- Part 4 of TV tuner socket - set of variables for display settings (brightness, contrast) -->\n' +
                     '	<set id="displaySettings">	\n' +
                     '		<variable id="brightness" type="brightnessType" optional="true">\n' +
                     '			<dependency>\n' +
                     '				<relevant>value("powerMode") eq "ON"</relevant>\n' +
                     '				<write>value("powerMode") eq "ON"</write>\n' +
                     '			</dependency>	\n' +
                     '			<dc:description xml:lang="en">\n' +
                     '				Part 4 of TV tuner socket\n' +
                     '				\n' +
                     '				A variable for brightness control.\n' +
                     '				\n' +
                     '				Note: Brightness control is optional for the first demonstrator, but important for\n' +
                     '				visually impared users.\n' +
                     '				\n' +
                     '				Moreover, brightness is rather a property of the monitor device, not the TV tuner,\n' +
                     '				so it might move to another socket in the future.\n' +
                     '			</dc:description>		\n' +
                     '		</variable>\n' +
                     '		\n' +
                     '		<variable id="contrast" type="contrastType" optional="true">\n' +
                     '			<dependency>\n' +
                     '				<relevant>value("powerMode") eq "ON"</relevant>\n' +
                     '				<write>value("powerMode") eq "ON"</write>\n' +
                     '			</dependency>		\n' +
                     '			<dc:description xml:lang="en">\n' +
                     '				Part 4 of TV tuner socket\n' +
                     '				\n' +
                     '				A variable for contrast control.\n' +
                     '				\n' +
                     '				Note: Contrast control is optional for the first demonstrator, but important for\n' +
                     '				visually impared users.\n' +
                     '				\n' +
                     '				Moreover, contrast is rather a property of the monitor device, not the TV tuner,\n' +
                     '				so it might move to another socket in the future.\n' +
                     '			</dc:description>\n' +
                     '		</variable>		\n' +
                     '		\n' +
                     '	</set> <!-- end set id="displaySettings" -->\n' +
                     '	\n' +
                     '	<notify id="notifySubscription" category="alert"/>' +
                     '	\n' +
                     '	<notify id="notifyPower" category="info"/>' +
                     '	\n' +
                     '	<schema xmlns="http://www.w3.org/2001/XMLSchema">\n' +
                     '	\n' +
                     '		<simpleType name="volumeType" id="volumeTypeId">\n' +
                     '			<annotation>\n' +
                     '				<documentation>4.2 Volume type</documentation> \n' +
                     '			</annotation>\n' +
                     '			<restriction base="xsd:unsignedInt">\n' +
                     '				<minInclusive value="0"/>\n' +
                     '				<maxInclusive value="100"/>\n' +
                     '			</restriction>\n' +
                     '		</simpleType>\n' +
                     '		\n' +
                     '		<simpleType name="brightnessType" id="brightnessTypeId">\n' +
                     '			<annotation>\n' +
                     '				<documentation>4.3 brightness type</documentation> \n' +
                     '			</annotation>\n' +
                     '			<restriction base="xsd:unsignedInt">\n' +
                     '				<minInclusive value="0"/>\n' +
                     '				<maxInclusive value="100"/>\n' +
                     '			</restriction>\n' +
                     '		</simpleType>\n' +
                     '\n' +
                     '		<simpleType name="contrastType" id="contrastTypeId">\n' +
                     '			<annotation>\n' +
                     '				<documentation>4.4 contrast type</documentation> \n' +
                     '			</annotation>\n' +
                     '			<restriction base="xsd:unsignedInt">\n' +
                     '				<minInclusive value="0"/>\n' +
                     '				<maxInclusive value="100"/>\n' +
                     '			</restriction>\n' +
                     '		</simpleType>	\n' +
                     '		\n' +
                     '		<simpleType name="channelType" id="channelTypeId">\n' +
                     '			<annotation>\n' +
                     '				<documentation>4.5 TV channel type</documentation> \n' +
                     '			</annotation>\n' +
                     '			<restriction base="uis:stringListItem"/>\n' +
                     '		</simpleType>	\n' +
                     '\n' +
                     '	</schema>	\n' +
                     '	\n' +
                     '</uiSocket>\n' );


               case "tv_epg/tv_epg.uis":
                  return(
                     '<uiSocket about="http://res.i2home.org/tv/tv_epg.uisocket" id="socket" \n' +
                     '	xmlns="http://myurc.org/ns/uisocketdesc"\n' +
                     '	xmlns:uis="http://myurc.org/ns/uisocketdesc" \n' +
                     '  xmlns:types="http://res.i2home.org/types/" \n' +
                     '	xmlns:dc="http://purl.org/dc/elements/1.1/"\n' +
                     '	xmlns:dcterms="http://purl.org/dc/terms/" \n' +
                     '	xmlns:xsd="http://www.w3.org/2001/XMLSchema"\n' +
                     '	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"\n' +
                     '	xsi:schemaLocation="http://myurc.org/ns/uisocketdesc http://myurc.org/ns/uisocketdesc http://res.i2home.org/types/ http://localhost/UCH/webclient/types.xsd http://purl.org/dc/elements/1.1/ http://dublincore.org/schemas/xmls/qdc/2006/01/06/dc.xsd http://purl.org/dc/terms/ http://dublincore.org/schemas/xmls/qdc/2006/01/06/dcterms.xsd">\n' +
                     '	<dc:creator>Carlo Moritz G&#246;llner</dc:creator>\n' +
                     '	<dc:publisher>VICOMTech, Spain</dc:publisher>\n' +
                     '	<dc:rights>Copyright 2006, VICOMTech</dc:rights>\n' +
                     '	<dcterms:conformsTo>http://myurc.org/iso24752-2/2007</dcterms:conformsTo>\n' +
                     '	<!--<dcterms:modified>2007-03-14</dcterms:modified>-->\n' +
                     '	<dcterms:modified>2007-08-27</dcterms:modified>\n' +
                     '\n' +
                     '	<variable id="powerMode" type="types:powerModeType">\n' +
                     '		<dependency>\n' +
                     '			<relevant>not(value("powerMode") eq "OFF")</relevant>\n' +
                     '			<write>not(value("powerMode") eq "OFF")</write> \n' +
                     '		</dependency>\n' +
                     '		<dc:description xml:lang="en">		\n' +
                     '			This socket element allows to adjust the power mode of the device. \n' +
                     '			\n' +
                     '			The powerModeType is a string enumeration "ON", "STANDBY" and "OFF":\n' +
                     '			 - "ON" corresponds to an activated EPG device.\n' +
                     '			 - "STANDBY" corresponds to a deactivated EPG device that may still be awaken from the network.\n' +
                     '			 - "OFF" corresponds to a shut down EPG device that has to be switched on manually\n' +
                     '			 \n' +
                     '			If the ePG device is "OFF" and the user tries to interact, the UI could show an appropiate\n' +
                     '			message: "Arnost, you have to switch on the set-top box manually, first." This would require a persistent TA module though, which is active even when the EPG device is completely "OFF".\n' +
                     '		</dc:description>\n' +
                     '	</variable>\n' +
                     '	\n' +
                     '	<variable id="channelList" type="uis:stringList" final="true">\n' +
                     '		<dependency>\n' +
                     '			<relevant>value("powerMode") eq "ON"</relevant>\n' +
                     '			<write>false()</write>\n' +
                     '		</dependency>\n' +
                     '		<dc:description xml:lang="en">\n' +
                     '	\n' +
                     '			This variable holds a list of ids of channel that the user can use to access the EPG info.\n' +
                     '			\n' +
                     '			The UI may also show numbers to the users, using and index into this list.\n' +
                     '			The variable is marked as final, since the channel list may not be modified at runtime.\n' +
                     '			\n' +
                     '			Example: "ARD ZDF SAT-1 ARTE"\n' +
                     '		</dc:description>\n' +
                     '	</variable>\n' +
                     '	\n' +
                     '	<variable id="epgTable" type="uis:stringListItem" dim="uis:stringListItem xsd:dateTime">\n' +
                     '		<dependency>\n' +
                     '			<relevant>value("powerMode") eq "ON"</relevant>\n' +
                     '			<write>false()</write>\n' +
                     '		</dependency>\n' +
                     '		<dc:description xml:lang="en">\n' +
                     '			A two-dimensional table to access EPG data. \n' +
                     '			\n' +
                     '			The first dimension is a string representation of a TV channel ID, type channelIdType. The format of this ID is not clear yet, it could be a DVB service identifier number, or a human readable string. Anyway, metadata for the TV channel (labels, icons, ...) will be obtained from resources using this ID as key.\n' +
                     '			\n' +
                     '			The second dimension represents event times for the content service. We use only one dimension for the event time, including date and time of day (xsd:dateTime). The xsd:dateTime includes corresponding timezone also. This is done to allow comparisons between different dateTimes. \n' +
                     '			\n' +
                     '			The value is an event ID of type eventIdType. The event ID may be used to lookup modality dependent metadata in resource sheets (title, long/short description). Modality indepedent metadata for an event can be fetched from other variables of the socket using the event ID.\n' +
                     '			\n' +
                     '			Note: in general, one distinguishes between metadata for an event, and metadata of content which is referenced by the event. We merge these to concepts into one in order to lower complexity in the first year.\n' +
                     '		</dc:description>\n' +
                     '\n' +
                     '	</variable>\n' +
                     '	\n' +
                     '	<variable id="genreMap" type="xsd:string" dim="uis:stringListItem">\n' +
                     '		<dependency>\n' +
                     '			<relevant>value("powerMode") eq "ON"</relevant>\n' +
                     '			<write>false()</write>\n' +
                     '		</dependency>\n' +
                     '		<dc:description xml:lang="en">\n' +
                     '			A one-dimensional map to access the genre classification for an event. The key is an event ID taken from the EPG table. The value is a string representing a multi-level genre classification. The value consists of a sequence of genre level identifiers separated by a slash, such as movie/drama/tragedy.\n' +
                     '			\n' +
                     '			The system of valid genre level identifiers still has to be defined. The definitions of genres by DVB-SI is a first orientation for this system [2].\n' +
                     '			\n' +
                     '			The genre level identifiers may be used as lookup key for resource sheets in order to obtain labels for a certain language.\n' +
                     ' 		</dc:description>\n' +
                     '\n' +
                     '	</variable>\n' +
                     '\n' +
                     '	<variable id="durationMap" type="xsd:duration" dim="uis:stringListItem">\n' +
                     '		<dependency>\n' +
                     '			<relevant>value("powerMode") eq "ON"</relevant>\n' +
                     '			<write>false()</write>\n' +
                     '		</dependency>\n' +
                     '		<dc:description xml:lang="en">\n' +
                     '			A one-dimensional map to access the duration of an event. The key is an event ID taken from the EPG table. The value is of type xsd:duration.\n' +
                     '		</dc:description>\n' +
                     '	</variable>\n' +
                     '\n' +
                     '	<variable id="startTimeMap" type="xsd:dateTime" dim="uis:stringListItem">\n' +
                     '		<dependency>\n' +
                     '			<relevant>value("powerMode") eq "ON"</relevant>\n' +
                     '			<write>false()</write>\n' +
                     '		</dependency>\n' +
                     '		<dc:description xml:lang="en">\n' +
                     '			A one-dimensional map to access the start time of an event. The key is an event ID taken from the EPG table. The value is of type xsd:dateTime.\n' +
                     '		</dc:description>\n' +
                     '	</variable>\n' +
                     '	\n' +
                     '	<command id="fillEpgTable" type="uis:timedCommand">\n' +
                     '		<dependency>\n' +
                     '			<relevant>value("powerMode") eq "ON"</relevant>\n' +
                     '			<write>value("powerMode") eq "ON"</write>\n' +
                     '		</dependency>\n' +
                     '		<param id="channelFilter" 	dir="in" type="uis:stringList"/>\n' +
                     '		<param id="fromTimeFilter"	dir="in" type="xsd:dateTime"/>\n' +
                     '		<param id="toTimeFilter"	dir="in" type="xsd:dateTime"/>\n' +
                     '		<param id="genreFilter"   	dir="in" type="uis:stringList"/>\n' +
                     '		<param idref="epgTable"   	dir="out"/>\n' +
                     '		<param idref="durationMap" 	dir="out"/>\n' +
                     '		<param idref="genreMap" 	dir="out"/>\n' +
                     '		<dc:description xml:lang="en"> \n' +
                     '			The command provides a mean to restrict the entries in the egpTable variable to certain dimensions of interest. \n' +
                     '			\n' +
                     '			As filtering dimensions, one can supply:\n' +
                     '			 - list of channel id strings. An "*" can be used to select all the channels.\n' +
                     '			 - time range to filter. Time range is defined using "fromTimeFilter" and "toTimeFilter" xsd:dateTime values.\n' +
                     '			 - list of genre identifiers for filtering of events by genre. An "*" can be used to select all the available genres.\n' +
                     '			 \n' +
                     '			The filters are implicitly combined using boolean AND.\n' +
                     '		</dc:description>\n' +
                     '	</command>\n' +
                     '\n' +
                     '</uiSocket>\n' );


               case "satelliteBox/satellite_box.uis":
                  return(
							'<uiSocket about="http://res.myurc.org/upnp/demo/satelliteBox/socket"                                      \n' +
							'	id="socket"                                                                                             \n' +
							'	xmlns="http://myurc.org/ns/uisocketdesc"                                                                \n' +
							'	xmlns:uis="http://myurc.org/ns/uisocketdesc"                                                            \n' +
							'	xmlns:dc="http://purl.org/dc/elements/1.1/"                                                             \n' +
							'	xmlns:dcterms="http://purl.org/dc/terms/"                                                               \n' +
							'	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"                                                   \n' +
							'	xsi:schemaLocation="http://myurc.org/ns/uisocketdesc http://myurc.org/ns/uisocketdesc                   \n' +
							'		http://purl.org/dc/elements/1.1/ http://dublincore.org/schemas/xmls/qdc/2006/01/06/dc.xsd            \n' +
							'		http://purl.org/dc/terms/ http://dublincore.org/schemas/xmls/qdc/2006/01/06/dcterms.xsd">            \n' +
							'	<dc:creator>Jon Hardin</dc:creator>                                                                     \n' +
							'	<dc:contributor>Gottfried Zimmermann</dc:contributor>                                                   \n' +
							'	<dc:publisher>Intelli-Computing Consulting</dc:publisher>                                               \n' +
							'	<dc:rights>Copyright 2008 The Board of Regents of the University of Wisconsin System</dc:rights>        \n' +
							'	<dcterms:conformsTo>http://myurc.org/iso24752-2/2007</dcterms:conformsTo>                               \n' +
							'	<dcterms:modified>2008-05-02</dcterms:modified>                                                         \n' +
							'	                                                                                                        \n' +
							'	<!-- Part 1 of Satellite Box socket - power on/off -->                                                  \n' +
							'	                                                                                                        \n' +
							'	<variable id="powerMode" type="powerModeType" includesRes="true">                                       \n' +
							'		<dc:description xml:lang="en">                                                                       \n' +
							'			This socket element allows to adjust the power mode of the device.                                \n' +
							'			The powerModeType is a string enumeration "ON", "STANDBY":                                        \n' +
							'			- "ON" corresponds to an activated Satellite Box device.                                          \n' +
							'			- "STANDBY" corresponds to a deactivated Satellite Box that may still be awaken from the network. \n' +
							'		</dc:description>                                                                                    \n' +
							'	</variable>                                                                                             \n' +
							'                                                                                                          \n' +
							'	<!-- Part 2 of Satellite Box socket - channel selection -->                                             \n' +
							'	                                                                                                        \n' +
							'	<variable id="activeChannel" type="channelType">                                                        \n' +
							'		<dc:description xml:lang="en">                                                                       \n' +
							'			This variable may be used to set the active channel shown by the Satellite.                       \n' +
							'			A channel is identified by a unique integer between 1 and 12.  Channel number 0 is used if        \n' +
							'			the device is on standby mode.                                                                    \n' +
							'			The variable may only be written if the power mode of the device is "ON".                         \n' +
							'			Before switching the channel, the target adaptor saves the current channel to "previousChannel"   \n' +
							'			in order to allow to return to the previously active channel.                                     \n' +
							'			The integer value may be used to fetch long labels and icons from a resource sheet.               \n' +
							'		</dc:description>                                                                                    \n' +
							'		<dependency>                                                                                         \n' +
							'			<relevant>value("powerMode") eq "ON"</relevant>                                                   \n' +
							'			<write>value("powerMode") eq "ON"</write>                                                         \n' +
							'		</dependency>                                                                                        \n' +
							'	</variable>                                                                                             \n' +
							'	                                                                                                        \n' +
							'	<variable id="previousChannel" type="channelType">                                                      \n' +
							'		<dc:description xml:lang="en">                                                                       \n' +
							'			Specifies the channel number of the previously selected channel.                                  \n' +
							'			This variable is read-only (i.e. it cannot be changed by the user).                               \n' +
							'		</dc:description>                                                                                    \n' +
							'		<dependency>                                                                                         \n' +
							'			<relevant>value("powerMode") eq "ON"</relevant>                                                   \n' +
							'			<write>false()</write>                                                                            \n' +
							'		</dependency>	                                                                                      \n' +
							'	</variable>                                                                                             \n' +
							'	                                                                                                        \n' +
							'	<command id="selectPreviousChannel" type="uis:basicCommand">                                            \n' +
							'		<dc:description xml:lang="en">                                                                       \n' +
							'			Command for switching back to the previously selected channel.                                    \n' +
							'			It can only be activated if the device is on.                                                     \n' +
							'			This command switches the values of activeChannel and previousChannel socket variables.           \n' +
							'		</dc:description>                                                                                    \n' +
							'		<param idref="activeChannel" dir="inout"/>                                                           \n' +
							'		<param idref="previousChannel" dir="inout"/>                                                         \n' +
							'		<dependency>                                                                                         \n' +
							'			<relevant>value("powerMode") eq "ON"</relevant>                                                   \n' +
							'			<write>value("powerMode") eq "ON"</write>                                                         \n' +
							'		</dependency>                                                                                        \n' +
							'	</command>                                                                                              \n' +
							'                                                                                                          \n' +
							'	<!-- Part 3 of Satellite Box socket - volume control (volume level, mute) -->                           \n' +
							'	                                                                                                        \n' +
							'	<variable id="volume" type="volumeType">                                                                \n' +
							'		<dc:description xml:lang="en">                                                                       \n' +
							'			Volume of the Satellite audio output may be set in range 0 to 100 (inclusive bounds), stepsize 1. \n' +
							'			May only be set if the device is "ON".                                                            \n' +
							'			When the volume is changed, the mute is deactivated (if it was on).                               \n' +
							'		</dc:description>                                                                                    \n' +
							'		<dependency>                                                                                         \n' +
							'			<relevant>value("powerMode") eq "ON"</relevant>                                                   \n' +
							'			<write>value("powerMode") eq "ON"</write>                                                         \n' +
							'		</dependency>                                                                                        \n' +
							'	</variable>                                                                                             \n' +
							'	                                                                                                        \n' +
							'	<variable id="mute" type="xsd:boolean">                                                                 \n' +
							'		<dc:description xml:lang="en">                                                                       \n' +
							'			Activates and deactivates the mute function.                                                      \n' +
							'			Can only be changed if the device is on.                                                          \n' +
							'		</dc:description>                                                                                    \n' +
							'		<dependency>                                                                                         \n' +
							'			<relevant>value("powerMode") eq "ON"</relevant>                                                   \n' +
							'			<write>value("powerMode") eq "ON"</write>                                                         \n' +
							'		</dependency>                                                                                        \n' +
							'	</variable>                                                                                             \n' +
							'	                                                                                                        \n' +
							'	<schema xmlns="http://www.w3.org/2001/XMLSchema">                                                       \n' +
							'                                                                                                          \n' +
							'		<simpleType name="channelType" id="channelTypeId">                                                   \n' +
							'			<restriction base="xsd:unsignedInt">                                                              \n' +
							'				<minInclusive value="0"/>                                                                      \n' +
							'				<maxInclusive value="12"/>                                                                     \n' +
							'			</restriction>                                                                                    \n' +
							'		</simpleType>                                                                                        \n' +
							'		                                                                                                     \n' +
							'		<simpleType name="powerModeType" id="powerModeTypeId">                                               \n' +
							'			<restriction base="xsd:string">                                                                   \n' +
							'				<enumeration value="ON"/>                                                                      \n' +
							'				<enumeration value="STANDBY"/>                                                                 \n' +
							'			</restriction>                                                                                    \n' +
							'		</simpleType>                                                                                        \n' +
							'                                                                                                          \n' +
							'		<simpleType name="volumeType" id="volumeTypeId">                                                     \n' +
							'			<restriction base="xsd:unsignedInt">                                                              \n' +
							'				<minInclusive value="0"/>                                                                      \n' +
							'				<maxInclusive value="100"/>                                                                    \n' +
							'			</restriction>                                                                                    \n' +
							'		</simpleType>                                                                                        \n' +
							'		                                                                                                     \n' +
							'	</schema>	                                                                                            \n' +
							'	                                                                                                        \n' +
                     '</uiSocket>\n' );



               case "UCH/webclient/types.xsd":
                  return(
                     '<schema \n' +
                     '	xmlns="http://www.w3.org/2001/XMLSchema" \n' +
                     '	xmlns:xsd="http://www.w3.org/2001/XMLSchema" \n' +
                     '	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" \n' +
                     '	xmlns:dc="http://purl.org/dc/elements/1.1/" \n' +
                     '	xmlns:dcterms="http://purl.org/dc/terms/" \n' +
                     '	targetNamespace="http://res.i2home.org/types/" \n' +
                     '	elementFormDefault="qualified" \n' +
                     '	attributeFormDefault="unqualified" \n' +
                     '	version="2007-08-19" \n' +
                     '	xsi:schemaLocation="http://purl.org/dc/elements/1.1/ http://dublincore.org/schemas/xmls/qdc/2006/01/06/dc.xsd \n' +
                     '		http://purl.org/dc/terms/ http://dublincore.org/schemas/xmls/qdc/2006/01/06/dcterms.xsd">\n' +
                     '	<annotation>\n' +
                     '		<documentation>\n' +
                     '			<dc:title xml:lang="en-US">XML Schema for common types</dc:title>\n' +
                     '			<dc:creator>Gottfried Zimmermann</dc:creator>\n' +
                     '			<dc:publisher xml:lang="en-US"></dc:publisher>\n' +
                     '			<dc:description xml:lang="en-US">This is the schema definition file for common types for socket descriptions.</dc:description>\n' +
                     '			<dc:language xml:lang="en-US">English</dc:language>\n' +
                     '			<dcterms:issued>2008-04-02</dcterms:issued>\n' +
                     '			<dcterms:modified>2008-04-02</dcterms:modified>\n' +
                     '			<dc:rights xml:lang="en-US">Copyright 2008, Access Technologies Group</dc:rights>\n' +
                     '		</documentation>\n' +
                     '	</annotation>\n' +
                     '	\n' +
                     '  <simpleType name="powerModeType" id="idPowerModeType">\n' +
                     '    <annotation>\n' +
                     '      <documentation>\n' +
                     '        Power mode type\n' +
                     '      </documentation>\n' +
                     '    </annotation>\n' +
                     '    <restriction base="xsd:string">\n' +
                     '      <enumeration value="OFF"/>\n' +
                     '      <enumeration value="ON"/>\n' +
                     '      <enumeration value="STANDBY"/>\n' +
                     '    </restriction>\n' +
                     '  </simpleType>\n' +
                     '\n' +
                     '</schema>\n' );


               case "test/testcase1.uis":
               case "test/testcase2.uis":
                  return(
                     ((urlComps.path == "test/testcase1.uis") ? 
                        '<uiSocket about="http://example.com/test/testcase1/socket" ' :
                        '<uiSocket about="http://example.com/test/testcase2/socket" ') +
                     '     id="socket" xmlns="http://myurc.org/ns/uisocketdesc" xmlns:uis="http://myurc.org/ns/uisocketdesc" xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://myurc.org/ns/uisocketdesc http://myurc.org/ns/uisocketdesc ' +
                     '		http://purl.org/dc/elements/1.1/ http://dublincore.org/schemas/xmls/qdc/2006/01/06/dc.xsd ' +
                     '		http://purl.org/dc/terms/ http://dublincore.org/schemas/xmls/qdc/2006/01/06/dcterms.xsd">' +
                     '	<dcterms:conformsTo>http://myurc.org/iso24752-2/2007</dcterms:conformsTo>' +
                     '	<dc:creator>Gottfried Zimmermann</dc:creator>' +
                     '	<dc:title xml:lang="en">User interface socket description for testing</dc:title>' +
                     '	<dcterms:modified>2007-10-18</dcterms:modified>' +

                     '  <constant id="const0" type="xsd:string">570</constant>' +
                     '	<variable id="var0" type="xsd:string"/>' +
                     '  <variable id="var1" type="xsd:string" dim="xsd:integer"/>' +
                     '  <variable id="var2" type="xsd:string" dim="xsd:integer xsd:integer"/>' +
                     '  <variable id="var3" type="xsd:string" dim="xsd:integer xsd:integer xsd:integer"/>' +

                     '  <command id="cmd0a" type="uis:voidCommand"/>' +
                     '  <command id="cmd0b" type="uis:voidCommand">' +
                     '     <dependency><write>false()</write></dependency>' +
                     '     <param id="param0b-i1" dir="in" type="xsd:string"/>' +
                     '     <param id="param0b-i2" dir="in" type="xsd:string"/>' +
                     '     <param id="param0b-o1" dir="out" type="xsd:string"/>' +
                     '     <param id="param0b-o2" dir="out" type="xsd:string"/>' +
                     '     <param id="param0b-io1" dir="inout" type="xsd:string"/>' +
                     '     <param id="param0b-io2" dir="inout" type="xsd:string"/>' +
                     '  </command>' +
                     '  <command id="cmd0c" type="uis:basicCommand"/>' +
                     '  <command id="cmd0d" type="uis:basicCommand">' +
                     '     <dependency><write>false()</write></dependency>' +
                     '     <param id="param0d-i1" dir="in" type="xsd:string"/>' +
                     '     <param id="param0d-i2" dir="in" type="xsd:string"/>' +
                     '     <param id="param0d-io1" dir="inout" type="xsd:string"/>' +
                     '     <param id="param0d-io2" dir="inout" type="xsd:string"/>' +
                     '     <param id="param0d-o1" dir="out" type="xsd:string"/>' +
                     '     <param id="param0d-o2" dir="out" type="xsd:string"/>' +
                     '  </command>' +
                     '  <command id="cmd0e" type="uis:timedCommand"/>' +
                     '  <command id="cmd0f" type="uis:timedCommand">' +
                     '     <dependency><write>false()</write></dependency>' +
                     '     <param id="param0f-i1" dir="in" type="xsd:string"/>' +
                     '     <param id="param0f-i2" dir="in" type="xsd:string"/>' +
                     '     <param id="param0f-io1" dir="inout" type="xsd:string"/>' +
                     '     <param id="param0f-io2" dir="inout" type="xsd:string"/>' +
                     '     <param id="param0f-o1" dir="out" type="xsd:string"/>' +
                     '     <param id="param0f-o2" dir="out" type="xsd:string"/>' +
                     '  </command>' +

                     '  <command id="cmd1a" type="uis:voidCommand" dim="xsd:string" />' +
                     '  <command id="cmd1b" type="uis:voidCommand" dim="xsd:string" >' +
                     '     <dependency><write>false()</write></dependency>' +
                     '     <param id="param1b-i1" dir="in" type="xsd:string"/>' +
                     '     <param id="param1b-i2" dir="in" type="xsd:string"/>' +
                     '     <param id="param1b-o1" dir="out" type="xsd:string"/>' +
                     '     <param id="param1b-o2" dir="out" type="xsd:string"/>' +
                     '     <param id="param1b-io1" dir="inout" type="xsd:string"/>' +
                     '     <param id="param1b-io2" dir="inout" type="xsd:string"/>' +
                     '  </command>' +
                     '  <command id="cmd1c" type="uis:basicCommand" dim="xsd:string" />' +
                     '  <command id="cmd1d" type="uis:basicCommand" dim="xsd:string" >' +
                     '     <dependency><write>false()</write></dependency>' +
                     '     <param id="param1d-i1" dir="in" type="xsd:string"/>' +
                     '     <param id="param1d-i2" dir="in" type="xsd:string"/>' +
                     '     <param id="param1d-io1" dir="inout" type="xsd:string"/>' +
                     '     <param id="param1d-io2" dir="inout" type="xsd:string"/>' +
                     '     <param id="param1d-o1" dir="out" type="xsd:string"/>' +
                     '     <param id="param1d-o2" dir="out" type="xsd:string"/>' +
                     '  </command>' +
                     '  <command id="cmd1e" type="uis:timedCommand" dim="xsd:string" />' +
                     '  <command id="cmd1f" type="uis:timedCommand" dim="xsd:string" >' +
                     '     <dependency><write>false()</write></dependency>' +
                     '     <param id="param1f-i1" dir="in" type="xsd:string"/>' +
                     '     <param id="param1f-i2" dir="in" type="xsd:string"/>' +
                     '     <param id="param1f-io1" dir="inout" type="xsd:string"/>' +
                     '     <param id="param1f-io2" dir="inout" type="xsd:string"/>' +
                     '     <param id="param1f-o1" dir="out" type="xsd:string"/>' +
                     '     <param id="param1f-o2" dir="out" type="xsd:string"/>' +
                     '  </command>' +
                     '  <command id="cmd2a" type="uis:timedCommand" dim="xsd:string xsd:string" >' +
                     '     <dependency><write>false()</write></dependency>' +
                     '     <param id="param2a-i1" dir="in" type="xsd:string"/>' +
                     '     <param id="param2a-i2" dir="in" type="xsd:string"/>' +
                     '     <param id="param2a-io1" dir="inout" type="xsd:string"/>' +
                     '     <param id="param2a-io2" dir="inout" type="xsd:string"/>' +
                     '     <param id="param2a-o1" dir="out" type="xsd:string"/>' +
                     '     <param id="param2a-o2" dir="out" type="xsd:string"/>' +
                     '  </command>' +

                     '	<notify id="ntf0" category="info"/>' +

                     '  <set id="set0">' +
                     '     <constant id="const00" type="xsd:string">570</constant>' +
                     '     <variable id="var00" type="xsd:string"/>' +
                     '     <variable id="var01" type="xsd:string" dim="xsd:integer"/>' +
                     '     <variable id="var02" type="xsd:string" dim="xsd:integer xsd:integer"/>' +
                     '     <variable id="var03" type="xsd:string" dim="xsd:integer xsd:integer xsd:integer"/>' +
                     '  <command id="cmd00a" type="uis:voidCommand"/>' +
                     '  <command id="cmd00d" type="uis:basicCommand" >' +
                     '     <dependency><write>false()</write></dependency>' +
                     '     <param id="param00d-i1" dir="in" type="xsd:string"/>' +
                     '     <param id="param00d-i2" dir="in" type="xsd:string"/>' +
                     '     <param id="param00d-io1" dir="inout" type="xsd:string"/>' +
                     '     <param id="param00d-io2" dir="inout" type="xsd:string"/>' +
                     '     <param id="param00d-o1" dir="out" type="xsd:string"/>' +
                     '     <param id="param00d-o2" dir="out" type="xsd:string"/>' +
                     '  </command>' +
                     '  <command id="cmd01a" type="uis:voidCommand" dim="xsd:string" />' +
                     '  <command id="cmd01d" type="uis:basicCommand" dim="xsd:string" >' +
                     '     <dependency><write>false()</write></dependency>' +
                     '     <param id="param01d-i1" dir="in" type="xsd:string"/>' +
                     '     <param id="param01d-i2" dir="in" type="xsd:string"/>' +
                     '     <param id="param01d-io1" dir="inout" type="xsd:string"/>' +
                     '     <param id="param01d-io2" dir="inout" type="xsd:string"/>' +
                     '     <param id="param01d-o1" dir="out" type="xsd:string"/>' +
                     '     <param id="param01d-o2" dir="out" type="xsd:string"/>' +
                     '  </command>' +
                     '  <command id="cmd02a" type="uis:voidCommand" dim="xsd:string xsd:string" />' +
                     '  <command id="cmd02d" type="uis:basicCommand" dim="xsd:string xsd:string" >' +
                     '     <dependency><write>false()</write></dependency>' +
                     '     <param id="param02d-i1" dir="in" type="xsd:string"/>' +
                     '     <param id="param02d-i2" dir="in" type="xsd:string"/>' +
                     '     <param id="param02d-io1" dir="inout" type="xsd:string"/>' +
                     '     <param id="param02d-io2" dir="inout" type="xsd:string"/>' +
                     '     <param id="param02d-o1" dir="out" type="xsd:string"/>' +
                     '     <param id="param02d-o2" dir="out" type="xsd:string"/>' +
                     '  </command>' +
                     '	<notify id="ntf00" category="alert"/>' +
                     '  <set id="set00">' +
                        '	<variable id="var000" type="xsd:string"/>' +
                        '  <command id="cmd000a" type="uis:voidCommand" />' +
                        '	<notify id="ntf000" category="error"/>' +
                     '  </set>' +
                     '  </set>' +

                     '  <set id="set1" dim="xsd:string" >' +
                     '     <constant id="const10" type="xsd:string">570</constant>' +
                     '     <variable id="var10" type="xsd:string"/>' +
                     '     <variable id="var11" type="xsd:string" dim="xsd:integer"/>' +
                     '     <variable id="var12" type="xsd:string" dim="xsd:integer xsd:integer"/>' +
                     '     <variable id="var13" type="xsd:string" dim="xsd:integer xsd:integer xsd:integer"/>' +
                     '  <command id="cmd10a" type="uis:voidCommand"/>' +
                     '  <command id="cmd10d" type="uis:basicCommand" >' +
                     '     <dependency><write>false()</write></dependency>' +
                     '     <param id="param10d-i1" dir="in" type="xsd:string"/>' +
                     '     <param id="param10d-i2" dir="in" type="xsd:string"/>' +
                     '     <param id="param10d-io1" dir="inout" type="xsd:string"/>' +
                     '     <param id="param10d-io2" dir="inout" type="xsd:string"/>' +
                     '     <param id="param10d-o1" dir="out" type="xsd:string"/>' +
                     '     <param id="param10d-o2" dir="out" type="xsd:string"/>' +
                     '  </command>' +
                     '  <command id="cmd11a" type="uis:voidCommand" dim="xsd:string" />' +
                     '  <command id="cmd11d" type="uis:basicCommand" dim="xsd:string" >' +
                     '     <dependency><write>false()</write></dependency>' +
                     '     <param id="param11d-i1" dir="in" type="xsd:string"/>' +
                     '     <param id="param11d-i2" dir="in" type="xsd:string"/>' +
                     '     <param id="param11d-io1" dir="inout" type="xsd:string"/>' +
                     '     <param id="param11d-io2" dir="inout" type="xsd:string"/>' +
                     '     <param id="param11d-o1" dir="out" type="xsd:string"/>' +
                     '     <param id="param11d-o2" dir="out" type="xsd:string"/>' +
                     '  </command>' +
                     '  <command id="cmd12a" type="uis:voidCommand" dim="xsd:string xsd:string" />' +
                     '  <command id="cmd12d" type="uis:basicCommand" dim="xsd:string xsd:string" >' +
                     '     <dependency><write>false()</write></dependency>' +
                     '     <param id="param12d-i1" dir="in" type="xsd:string"/>' +
                     '     <param id="param12d-i2" dir="in" type="xsd:string"/>' +
                     '     <param id="param12d-io1" dir="inout" type="xsd:string"/>' +
                     '     <param id="param12d-io2" dir="inout" type="xsd:string"/>' +
                     '     <param id="param12d-o1" dir="out" type="xsd:string"/>' +
                     '     <param id="param12d-o2" dir="out" type="xsd:string"/>' +
                     '  </command>' +
                     '	<notify id="ntf10" category="error"/>' +
                     '  <set id="set10">' +
                        '	<variable id="var100" type="xsd:string"/>' +
                        '  <command id="cmd100a" type="uis:voidCommand" />' +
                        '	<notify id="ntf100" category="error"/>' +
                     '  </set>' +
                     '  </set>' +

                     '  <set id="set2" dim="xsd:string xsd:string">' +
                     '     <constant id="const20" type="xsd:string">570</constant>' +
                     '     <variable id="var20" type="xsd:string"/>' +
                     '     <variable id="var21" type="xsd:string" dim="xsd:integer"/>' +
                     '     <variable id="var22" type="xsd:string" dim="xsd:integer xsd:integer"/>' +
                     '     <variable id="var23" type="xsd:string" dim="xsd:integer xsd:integer xsd:integer"/>' +
                     '  </set>' +

                     '</uiSocket>' );


               case "UCH/upnpav/play-1.uis":
                  return(
                     '<uiSocket about="http://res.myurc.org/upnp/av/play-1.uis" id="socket"\n' +
	                     'xmlns="http://myurc.org/ns/uisocketdesc" xmlns:uis="http://myurc.org/ns/uisocketdesc"\n' +
	                     'xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:dcterms="http://purl.org/dc/terms/"\n' +
	                     'xmlns:upnp="urn:schemas-upnp-org:metadata-1-0/upnp/" xmlns:upnp-av="urn:schemas-upnp-org:av:av"\n' +
	                     'xmlns:uis-upnp="http://myurc.org/ns/uis-upnp" xmlns:res-types="http://myurc.org/ns/res-types"\n' +
	                     'xmlns:xsd="http://www.w3.org/2001/XMLSchema"\n' +
	                     'xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"\n' +
	                     'xsi:schemaLocation="http://myurc.org/ns/uisocketdesc http://myurc.org/ns/uisocketdesc \n' +
		                     'http://purl.org/dc/elements/1.1/ http://www.dublincore.org/schemas/xmls/qdc/2003/04/02/dc.xsd \n' +
		                     'http://purl.org/dc/terms/ http://www.dublincore.org/schemas/xmls/qdc/2003/04/02/dcterms.xsd \n' +
		                     'urn:schemas-upnp-org:metadata-1-0/upnp/ http://www.upnp.org/schemas/av/upnp-v2-20060531.xsd \n' +
		                     'urn:schemas-upnp-org:av:av http://www.upnp.org/schemas/av/av-v1-20060531.xsd \n' +
		                     'http://myurc.org/ns/uis-upnp http://myurc.org/ns/uis-upnp\n' +
		                     'http://myurc.org/ns/res-types http://myurc.org/ns/res-types">\n' +
	                     '<!--Note to namespaces: The URC is expected to recognize types associated with these namespaces, either by look-up or by built-in capabilities.-->\n' +
	                     '<dc:creator>Gottfried Zimmermann</dc:creator>\n' +
	                     '<dc:publisher>URC Consortium</dc:publisher>\n' +
	                     '<dc:rights>Copyright 2007-2008, Access Technologies Group. This work is licensed under a\n' +
		                     'Creative Commons Attribution-Share Alike 3.0 United States License. To view a copy of this\n' +
		                     'license, visit http://creativecommons.org/licenses/by-sa/3.0/us/.</dc:rights>\n' +
	                     '<dcterms:conformsTo>http://myurc.org/iso24752-2/2007</dcterms:conformsTo>\n' +
	                     '<dcterms:modified>2008-08-25</dcterms:modified>\n' +
	                     '<mapping platform="upnp-av">\n' +
		                     '<uis-upnp:annotation>Note: The socket description contains data relevant for the user\n' +
			                     'interface only. Other data may be stored internally such as connection manager specific\n' +
			                     'instanceIDs, etc.</uis-upnp:annotation>\n' +
	                     '</mapping>\n' +
	                     '<set id="mediaServers" dim="upnp-av:UDN">\n' +
		                     '<!--Ideally, friendly names of the media servers should be made available as dynamic labels for values of uis-upnp:UDN-List.  However, this is not possible since a dimension can only refer to types, not to variables. -->\n' +
		                     '<dc:description xml:lang="en">Each instance of this set (i.e. set of subelements) represents\n' +
			                     'an available media server. From each server, the user can pick the digital media to play\n' +
			                     '(reflected by the variable "selectedNodeId"). There are two methods for the user to\n' +
			                     'select content: browsing or searching.</dc:description>\n' +
		                     '<dependency>\n' +
			                     '<insert>false()</insert>\n' +
		                     '</dependency>\n' +
		                     '<variable id="mediaServerFriendlyName" type="xsd:string"><!--Open issue: This variable will not be needed if we can find a way for TAs to deliver dynamic resources for values of external types (in this case values of upnp-av:UDN).-->\n' +
			                     '<dc:description xml:lang="en">Friendly name of media server.  This is typically a user-configured string, such as "NAS in kitchen".</dc:description>\n' +
			                     '<mapping platform="upnp-av">\n' +
				                     '<uis-upnp:annotation>Friendly name from device description.</uis-upnp:annotation>\n' +
			                     '</mapping>\n' +
			                     '<dependency>\n' +
				                     '<relevant>true()</relevant>\n' +
				                     '<write>false()</write>\n' +
			                     '</dependency>\n' +
		                     '</variable>\n' +
		                     '<variable id="isMediaServerSelected" type="xsd:boolean">\n' +
			                     '<dc:description xml:lang="en">Indicates that this is the currently selected media server\n' +
				                     'to be used for establishing a connection. Note that only one media server can be\n' +
				                     'selected at a time, i.e. another media server will be automatically deselected.</dc:description>\n' +
			                     '<dependency>\n' +
				                     '<relevant>true()</relevant>\n' +
				                     '<write>true()</write>\n' +
			                     '</dependency>\n' +
		                     '</variable>\n' +
		                     '<variable id="isNextNodeAvailable" type="xsd:boolean">\n' +
			                     '<dc:description>Is Next Node available or not?</dc:description>\n' +
			                     '<dependency>\n' +
				                     '<relevant>false()</relevant>\n' +
				                     '<write>false()</write>\n' +
			                     '</dependency>\n' +
		                     '</variable>\n' +
		                     '<variable id="isPrevNodeAvailable" type="xsd:boolean">\n' +
			                     '<dc:description>Is Previous Node available or not?</dc:description>\n' +
			                     '<dependency>\n' +
				                     '<relevant>false()</relevant>\n' +
				                     '<write>false()</write>\n' +
			                     '</dependency>\n' +
		                     '</variable>\n' +
		                     '<variable id="isParentNodeAvailable" type="xsd:boolean">\n' +
			                     '<dc:description>Is Parent Node available or not?</dc:description>\n' +
			                     '<dependency>\n' +
				                     '<relevant>false()</relevant>\n' +
				                     '<write>false()</write>\n' +
			                     '</dependency>\n' +
		                     '</variable>\n' +
		                     '<variable id="isFirstChildNodeAvailable" type="xsd:boolean">\n' +
			                     '<dc:description>Is First Child Node available or not?</dc:description>\n' +
			                     '<dependency>\n' +
				                     '<relevant>false()</relevant>\n' +
				                     '<write>false()</write>\n' +
			                     '</dependency>\n' +
		                     '</variable>\n' +
		                     '<variable id="searchCapabilities" type="uis:csvlist">\n' +
			                     '<dc:description xml:lang="en">Search capabilities of the currently selected media\n' +
				                     'server. SearchCapabilities is a CSV list of property names that can be used in\n' +
				                     'search queries. An empty string indicates that the CDS does not support any kind of\n' +
				                     'searching. A wildcard (\'*\') indicates that the device supports search queries using\n' +
				                     'all tags present in the CDS. Note 1: The "smart" URC may use this variable to tweak\n' +
				                     'its user interface for searching. Note 2: Since there is no pertinent type\n' +
				                     'definition in the upnp namespace, we are using uis:csvlist here.</dc:description>\n' +
			                     '<mapping platform="upnp-av">\n' +
				                     '<uis-upnp:annotation>Maps to return value of UPnP action GetSearchCapabilities.\n' +
					                     'Should be called at initialization and every time the user changes the selected\n' +
					                     'media server.</uis-upnp:annotation>\n' +
			                     '</mapping>\n' +
			                     '<dependency>\n' +
				                     '<relevant>false()</relevant>\n' +
				                     '<write>false()</write>\n' +
			                     '</dependency>\n' +
		                     '</variable>\n' +
		                     '<variable id="sortCapabilities" type="uis:csvlist">\n' +
			                     '<dc:description xml:lang="en">Sort capabilities of the currently selected media server.\n' +
				                     'SortCapabilities is a CSV list of tags that the CDS can use to sort Search() or\n' +
				                     'Browse() results. An empty string indicates that the device does not support any\n' +
				                     'kind of sorting. A wildcard (\'*\') indicates that the device supports sorting using\n' +
				                     'all tags present in the Content Directory. Note: Since there is no pertinent type\n' +
				                     'definition in the upnp namespace, we are using uis:csvlist here.</dc:description>\n' +
			                     '<mapping platform="upnp-av">\n' +
				                     '<uis-upnp:annotation>Maps to return value of UPnP action GetSortCapabilities. Should\n' +
					                     'be called at initialization and every time the user changes the selected media\n' +
					                     'server.</uis-upnp:annotation>\n' +
			                     '</mapping>\n' +
			                     '<dependency>\n' +
				                     '<relevant>false()</relevant>\n' +
				                     '<write>false()</write>\n' +
			                     '</dependency>\n' +
		                     '</variable>\n' +
		                     '<variable id="selectedNodeId" type="xsd:string">\n' +
			                     '<dc:description xml:lang="en">Id of the node from content that is currently selected for\n' +
				                     'playing. This node is in focus for a connect and play operation. From a user\'s\n' +
				                     'perspective, this variable gets a value implicitly either through browsing or\n' +
				                     'searching (or both). </dc:description>\n' +
			                     '<mapping platform="upnp-av">\n' +
				                     '<uis-upnp:annotation>Serves as parameter for a connect and play\n' +
				                     'operation.</uis-upnp:annotation>\n' +
			                     '</mapping>\n' +
		                     '</variable>\n' +
		                     '<set id="browsing">\n' +
			                     '<!--Open issue: Remove browseFilter and BrowseSortCriteria, and define defaults for them instead?  Or replace by a simple sort criteria variable of type metadataIndexType?-->\n' +
			                     '<variable id="browseFilter" type="uis:csvlist">\n' +
				                     '<dc:description xml:lang="en">browseFilter determines the set of properties of\n' +
					                     'digital media items presented in variable "content". Comma-separated list of\n' +
					                     'property specifiers (including namespaces); indicates which metadata properties\n' +
					                     'are to be returned in the results from browsing. Both properties represented in\n' +
					                     'CDS query results as XML elements, as well as properties represented as element\n' +
					                     'attributes, may be included in the comma-separated list. If the Filter parameter\n' +
					                     'is equal to "*", all properties are returned. As a rule, all required properties\n' +
					                     'are returned, but no optional properties will be returned unless explicitly\n' +
					                     'requested in the filter. Note: Since there is no pertinent type definition in\n' +
					                     'the upnp namespace, we are using uis:csvlist here.</dc:description>\n' +
				                     '<mapping platform="upnp-av">\n' +
					                     '<uis-upnp:annotation>Maps to the parameter Filter of action Browse. If\n' +
						                     'browseFilter is changed by the user, variable "content" needs to get\n' +
						                     'updated.</uis-upnp:annotation>\n' +
				                     '</mapping>\n' +
			                     '</variable>\n' +
			                     '<variable id="browseSortCriteria" type="uis:csvlist">\n' +
				                     '<dc:description xml:lang="en">browseSortCriteria determines how the digital media\n' +
					                     'items are sorted in variable "content". Comma-separated list of signed property\n' +
					                     'names, where signed means preceded by \'+\' or \'-\' sign. The \'+\' and \'-\' indicate\n' +
					                     'the sort is in ascending or descending order, respectively, with regard to the\n' +
					                     'value of its associated property. Properties appear in the list in order of\n' +
					                     'descending sort priority. For example, a value of\n' +
					                     '"+upnp:artist,-dc:date,+dc:title" would sort first on artist in ascending order,\n' +
					                     'then within each artist by date in descending order (most recent first) and\n' +
					                     'finally by title in ascending order. Note 1: Possible values of\n' +
					                     'browseSortCriteria are constrained by sortCapabilities. Note 2: A "knowledgeable\n' +
					                     'URC" could provide a custom-made user interface with lists and checkboxes to\n' +
					                     'fill this variable, and based on the possibilities of sorting described in\n' +
					                     'sortCapabilities. Note 3: Since there is no pertinent type definition in the\n' +
					                     'upnp namespace, we are using uis:csvlist here.</dc:description>\n' +
				                     '<mapping platform="upnp-av">\n' +
					                     '<uis-upnp:annotation>Maps to the parameter SortCriteria of action Browse. If\n' +
						                     'browseSortCriteria is changed by the user, variable "content" needs to get\n' +
						                     'updated.</uis-upnp:annotation>\n' +
				                     '</mapping>\n' +
			                     '</variable>\n' +
			                     '<set id="content">\n' +
				                     '<command id="nextNode" type="uis:voidCommand">\n' +
					                     '<!--Navigates to the next node on the same level as the current node.-->\n' +
					                     '<dependency>\n' +
						                     '<!--The execute dependency can probably be specified stricter in dependendance on the fields.-->\n' +
						                     '<relevant>true()</relevant>\n' +
						                     '<write>true()</write>\n' +
					                     '</dependency>\n' +
				                     '</command>\n' +
				                     '<command id="prevNode" type="uis:voidCommand">\n' +
					                     '<!--Navigates to the previous node on the same level as the current node.-->\n' +
					                     '<dependency>\n' +
						                     '<!--The execute dependency can probably be specified stricter in dependendance on the fields.-->\n' +
						                     '<relevant>true()</relevant>\n' +
						                     '<write>true()</write>\n' +
					                     '</dependency>\n' +
				                     '</command>\n' +
				                     '<command id="firstChildNode" type="uis:voidCommand">\n' +
					                     '<!--Navigates to the first node on the child level of the current node.-->\n' +
					                     '<dependency>\n' +
						                     '<!--The execute dependency can probably be specified stricter in dependendance on the fields.-->\n' +
						                     '<relevant>true()</relevant>\n' +
						                     '<write>true()</write>\n' +
					                     '</dependency>\n' +
				                     '</command>\n' +
				                     '<command id="parentNode" type="uis:voidCommand">\n' +
					                     '<!--Navigates to the parent node of the current node.-->\n' +
					                     '<dependency>\n' +
						                     '<!--The execute dependency can probably be specified stricter in dependendance on the fields.-->\n' +
						                     '<relevant>true()</relevant>\n' +
						                     '<write>true()</write>\n' +
					                     '</dependency>\n' +
				                     '</command>\n' +
				                     '<variable id="selectedNodeTitle" type="xsd:string">\n' +
					                     '<!--didl-lite:parentID attribute value of selected node.  Required for nodes of any class.-->\n' +
					                     '<dc:description> Name of the currently selected node. </dc:description>\n' +
					                     '<dependency>\n' +
						                     '<relevant>false()</relevant>\n' +
						                     '<write>false()</write>\n' +
					                     '</dependency>\n' +
				                     '</variable>\n' +
				                     '<variable id="selectedNodeIndex" type="xsd:unsignedInt">\n' +
					                     '<!--Open issue: Is xsd:unsigned the right type?-->\n' +
					                     '<!--Binding: Retrieved from Browse results.-->\n' +
					                     '<dc:description> Index of currently selected node in the list of its siblings.</dc:description>\n' +
					                     '<dependency>\n' +
						                     '<relevant>false()</relevant>\n' +
						                     '<write>false()</write>\n' +
					                     '</dependency>\n' +
				                     '</variable>\n' +
				                     '<variable id="totalTracks" type="xsd:unsignedInt">\n' +
					                     '<dc:description>Total number of tracks in the present container being browsed.</dc:description>\n' +
					                     '<dependency>\n' +
						                     '<relevant>false()</relevant>\n' +
						                     '<write>false()</write>\n' +
					                     '</dependency>\n' +
				                     '</variable>\n' +
				                     '<variable id="parentID" type="xsd:string">\n' +
					                     '<!--didl-lite:parentID attribute value of selected node.  Required for nodes of any class.-->\n' +
					                     '<dc:description>id property of object\'s parent. The parentID of the Content\n' +
						                     'Directory \'root\' container must be set to the reserved value of "-1". No\n' +
						                     'other parentID attribute of any other Content Directory object may take this\n' +
						                     'value.</dc:description>\n' +
					                     '<dependency>\n' +
						                     '<relevant>false()</relevant>\n' +
						                     '<write>false()</write>\n' +
					                     '</dependency>\n' +
				                     '</variable>\n' +
				                     '<variable id="childCount" type="xsd:integer">\n' +
					                     '<!--didl-lite:childCount attribute value of selected node.  Optional for nodes of class object.container.-->\n' +
					                     '<dc:description>Child count for the object. Applies to containers only.</dc:description>\n' +
					                     '<dependency>\n' +
						                     '<relevant>false()</relevant>\n' +
						                     '<write>false()</write>\n' +
					                     '</dependency>\n' +
				                     '</variable>\n' +
				                     '<variable id="class" type="upnp:classType">\n' +
					                     '<!--Binding: class property of selected node.  Required for nodes of any class.-->\n' +
					                     '<dc:description>Type of the currently selected node: folder or item. Note:\n' +
						                     'Although this information can be retrieved from xmlContent, this variable is\n' +
						                     'defined for expressing dependencies.</dc:description>\n' +
				                     '</variable>\n' +
				                     '<variable id="searchable" type="xsd:boolean">\n' +
					                     '<!--Binding: didl-lite:searchable attribute value of node with selectedNodeId.  -->\n' +
					                     '<dc:description>When true, the ability to perform a Search() action under a\n' +
						                     'container is enabled, otherwise a Search() under that container will return\n' +
						                     'no results. Note: Although this information can be retrieved from\n' +
						                     'xmlContent, this variable is defined for expressing dependencies.</dc:description>\n' +
					                     '<dependency>\n' +
						                     '<relevant>false()</relevant>\n' +
						                     '<write>false()</write>\n' +
					                     '</dependency>\n' +
				                     '</variable>\n' +
				                     '<variable id="restricted" type="xsd:boolean">\n' +
					                     '<!--didl-lite:restricted attribute value of selected node.  Required for nodes of any class.-->\n' +
					                     '<dc:description>When true, ability to modify a given object is confined to the\n' +
						                     'Content Directory Service. Control point metadata write access is disabled.\n' +
						                     'Note: Although this information can be retrieved from xmlContent, this\n' +
						                     'variable is defined for expressing dependencies.</dc:description>\n' +
					                     '<dependency>\n' +
						                     '<relevant>false()</relevant>\n' +
						                     '<write>false()</write>\n' +
					                     '</dependency>\n' +
				                     '</variable>\n' +
				                     '<variable id="writeStatus" type="upnp:writeStatusType">\n' +
					                     '<!--upnp:writeStatus element value of selected node.  Optional for nodes of any class.-->\n' +
					                     '<dc:description>When present, controls the modifiability of the resources of a\n' +
						                     'given object. Ability of a Control Point to change writeStatus of a given\n' +
						                     'resource(s) is implementation dependent. Allowed values are: WRITABLE,\n' +
						                     'PROTECTED, NOT_WRITABLE, UNKNOWN, MIXED. Note: Although this information can\n' +
						                     'be retrieved from xmlContent, this variable is defined for expressing\n' +
						                     'dependencies.</dc:description>\n' +
					                     '<dependency>\n' +
						                     '<relevant>false()</relevant>\n' +
						                     '<write>true()</write>\n' +
						                     '<!--Ability of a Control Point to change writeStatus of a given resource(s) is implementation dependent. -->\n' +
					                     '</dependency>\n' +
				                     '</variable>\n' +
				                     '<variable id="res" type="uriListType">\n' +
					                     '<!--Binding: DIDL-LITE res element of selected node.-->\n' +
					                     '<dc:description>Resource, typically a media file, associated with the object.\n' +
						                     'Values must be properly escaped URIs as described in [RFC 2396].\n' +
						                     'Space-separated list of didl-lite:res element values of selected node.\n' +
						                     'Optional for nodes of any class.</dc:description>\n' +
					                     '<dependency>\n' +
						                     '<relevant>false()</relevant>\n' +
						                     '<write>false()</write>\n' +
					                     '</dependency>\n' +
				                     '</variable>\n' +
				                     '<!--Binding note: searchClass not implemented for this socket.-->\n' +
				                     '<variable id="createClasses" type="classListType">\n' +
					                     '<!--upnp:createClass element values of selected node.  Optional for nodes of class object.container.-->\n' +
					                     '<dc:description>Create class of the associated container object. If restricted =\n' +
						                     'false, then * If no createClass elements are specified, then CreateObject\n' +
						                     'can create any class of object under the container * If createClass elements\n' +
						                     'are specified then, CreateObject can only create classes of objects\n' +
						                     'specified in the createClass tags * createClass is optional * createClass\n' +
						                     'semantics are per container, there is no parent-child relationship, they\n' +
						                     'only apply to CreateObject actions in that container</dc:description>\n' +
					                     '<dependency>\n' +
						                     '<relevant>false()</relevant>\n' +
						                     '<write>value(\'class\')==\'object.container\' and not(value(\'restricted\')) and\n' +
							                     'value(\'writeStatus\'))!=\'NOT_WRITABLE\' and\n' +
							                     'value(\'writeStatus\'))!=\'PROTECTED\')</write>\n' +
					                     '</dependency>\n' +
				                     '</variable>\n' +
				                     '<variable id="refID" type="xsd:string">\n' +
					                     '<!--didl-lite:refID attribute value of selected node.  Optional for nodes of class object.item.-->\n' +
					                     '<dc:description>id property of the item being referred to.</dc:description>\n' +
					                     '<dependency>\n' +
						                     '<relevant>false()</relevant>\n' +
						                     '<write>false()</write>\n' +
					                     '</dependency>\n' +
				                     '</variable>\n' +
				                     '<variable id="metadata" type="xsd:string" dim="metadataIndexType">\n' +
					                     '<!--Binding: Result of action Browse. 1-dimensional array containing the metadata fields pertaining to the currently selected node (container or item).  The fields must be a subset of those given in metadataIndexType.  If updated by the user, this triggers the UPnP action UpdateObject.-->\n' +
					                     '<!--Open issue: Initiate user notification before modifying metadata?-->\n' +
					                     '<dc:description>Set of human-readable metadata information pertaining to the\n' +
						                     'currently selected node (container or item). Each metadata item can be\n' +
						                     'modified by the user; which will immediately impact the Note: At least one\n' +
						                     'modification can be reversed by the undo command.</dc:description>\n' +
					                     '<dependency>\n' +
						                     '<relevant>true()</relevant>\n' +
						                     '<write>not(value(\'restricted\')) and value(\'writeStatus\'))!=\'NOT_WRITABLE\'\n' +
							                     'and value(\'writeStatus\'))!=\'PROTECTED\')</write>\n' +
					                     '</dependency>\n' +
				                     '</variable>\n' +
			                     '</set>\n' +
		                     '</set>\n' +
		                     '<command id="search" type="uis:basicCommand" optional="true">\n' +
			                     '<dc:description xml:lang="en">This command allows the invoker to search the content\n' +
				                     'directory for objects that match some search criteria. The search criteria are\n' +
				                     'specified as a query string operating on properties with comparison and logical\n' +
				                     'operators. Note 1: The search command is independent from browsing, and has its own\n' +
				                     'result "variable" as an output parameter. However, a client may show the results of\n' +
				                     'the search in the content tree if it wishes so. Note 2: It should be possible for a\n' +
				                     'client to provide an incremental search function based on the user\'s partial input,\n' +
				                     'by invoking the search command multiple times (incrementally).</dc:description>\n' +
			                     '<mapping platform="upnp-av">\n' +
				                     '<uis-upnp:annotation>Action Search in CDS. Parameters:\n' +
					                     'ContainerID=searchContainerId; SearchCriteria=searchCriteria;\n' +
					                     'Filter=searchFilter; SortCriteria=searchSortCriteria; StartingIndex and\n' +
					                     'RequestedCount implicit. Result is presented as output parameter\n' +
				                     '"searchResult".</uis-upnp:annotation>\n' +
			                     '</mapping>\n' +
			                     '<param id="searchContainerId" type="xsd:string" dir="in">\n' +
				                     '<dc:description xml:lang="en">Unique identifier of the container in which to begin\n' +
					                     'searching. A ContainerID value of zero corresponds to the root object of the\n' +
					                     'Content Directory. Note: Searchable must be true for the\n' +
				                     'container.</dc:description>\n' +
			                     '</param>\n' +
			                     '<param id="searchCriteria" type="xsd:string" dir="in">\n' +
				                     '<dc:description xml:lang="en">Search string for expert users. The SearchCriteria\n' +
					                     'parameter gives one or more search criteria to be used for querying the Content\n' +
					                     'Directory. For syntax details refer to CD 1.0 spec, 2.5.5.1. SearchCriteria\n' +
					                     'String Syntax. Note 1: Possible values are constrained by searchCapabilities.\n' +
					                     'Note 2: A "smart" URC may generate the searchCriteria string from a more\n' +
					                     'user-friendly set of widgets. Note 3: Since there is no pertinent type\n' +
					                     'definition in the upnp namespace, we are using xsd:string here.</dc:description>\n' +
			                     '</param>\n' +
			                     '<!--Open issue: Remove searchFilter and SearchSortCriteria, and define defaults for them instead?  Or replace by a simple sort criteria variable of type metadataIndexType? -->\n' +
			                     '<param id="searchFilter" type="uis:csvlist" dir="in">\n' +
				                     '<dc:description xml:lang="en">Comma-separated list of property specifiers (including\n' +
					                     'namespaces); indicates which metadata properties are to be returned in the\n' +
					                     'results from searching. Both properties represented in CDS query results as XML\n' +
					                     'elements, as well as properties represented as element attributes, may be\n' +
					                     'included in the comma-separated list. If the Filter parameter is equal to "*",\n' +
					                     'all properties are returned. As a rule, all required properties are returned,\n' +
					                     'but no optional properties will be returned unless explicitly requested in the\n' +
					                     'filter. Note 1: A "smart" URC may generate the searchFilter list from a more\n' +
					                     'user-friendly set of widgets. Note 2: Since there is no pertinent type\n' +
					                     'definition in the upnp namespace, we are using uis:csvlist\n' +
				                     'here.</dc:description>\n' +
			                     '</param>\n' +
			                     '<param id="searchSortCriteria" type="uis:csvlist" dir="in">\n' +
				                     '<dc:description xml:lang="en">CSV list of signed property names, where signed means\n' +
					                     'preceded by \'+\' or \'-\' sign. The \'+\' and \'-\' indicate the sort is in ascending\n' +
					                     'or descending order, respectively, with regard to the value of its associated\n' +
					                     'property. Properties appear in the list in order of descending sort priority.\n' +
					                     'For example, a value of "+upnp:artist,-dc:date,+dc:title" would sort first on\n' +
					                     'artist in ascending order, then within each artist by date in descending order\n' +
					                     '(most recent first) and finally by title in ascending order. Note 1: This value\n' +
					                     'is constrained by sortCapabilities. Note 2: A "smart" URC would present this in\n' +
					                     'a user-friendly way. Note 3: Since there is no pertinent type definition in the\n' +
					                     'upnp namespace, we are using uis:csvlist here.</dc:description>\n' +
			                     '</param>\n' +
			                     '<param idref="content" dir="out">\n' +
				                     '<dc:description xml:lang="en">The result of the search is reflected in the socket\n' +
					                     'elements of the set "content". No separate socket elements for the search result\n' +
					                     'are available.</dc:description>\n' +
				                     '<!--Open issue: Should we have a separate set of variables for displaying the search results?  But in a simple user interface, there will most likely be only one set of components for both the browse result and the search result...-->\n' +
			                     '</param>\n' +
		                     '</command>\n' +
	                     '</set>\n' +
	                     '<set id="mediaRenderers" dim="upnp-av:UDN">\n' +
		                     '<!--Ideally, friendly names of the media players should be made available as dynamic labels for values of uis-upnp:UDN-List.  However, this is not possible since a dimension can only refer to types, not to variables. -->\n' +
		                     '<dc:description xml:lang="en">Each instance of this set (i.e. set of subelements) represents\n' +
			                     'an available media renderer. For each renderer, the user can set rendering control\n' +
			                     'features with the renderer being connected or not connected.</dc:description>\n' +
		                     '<dependency>\n' +
			                     '<insert>false()</insert>\n' +
		                     '</dependency>\n' +
		                     '<variable id="mediaRendererFriendlyName" type="xsd:string"><!--Open issue: This variable will not be needed if we can find a way for TAs to deliver dynamic resources for values of external types (in this case values of upnp-av:UDN).-->\n' +
			                     '<dc:description xml:lang="en">Friendly name of media renderer.  This is typically a user-configured string, such as "living room TV".</dc:description>\n' +
			                     '<mapping platform="upnp-av">\n' +
				                     '<uis-upnp:annotation>Friendly name from device description.</uis-upnp:annotation>\n' +
			                     '</mapping>\n' +
			                     '<dependency>\n' +
				                     '<relevant>true()</relevant>\n' +
				                     '<write>false()</write>\n' +
			                     '</dependency>\n' +
		                     '</variable>\n' +
		                     '<variable id="isMediaRendererSelected" type="xsd:boolean">\n' +
			                     '<dc:description xml:lang="en">Indicates that this is the currently selected media\n' +
				                     'renderer to be used for establishing a connection. Note that only one media renderer\n' +
				                     'can be selected at a time, i.e. another media renderer will be automatically\n' +
				                     'deselected.</dc:description>\n' +
			                     '<dependency>\n' +
				                     '<relevant>true()</relevant>\n' +
				                     '<write>true()</write>\n' +
			                     '</dependency>\n' +
		                     '</variable>\n' +
		                     '<variable id="isPlayAvailable" type="xsd:boolean">\n' +
			                     '<dc:description>Is play available or not?</dc:description>\n' +
			                     '<dependency>\n' +
				                     '<relevant>false()</relevant>\n' +
				                     '<write>false()</write>\n' +
			                     '</dependency>\n' +
		                     '</variable>\n' +
		                     '<variable id="isStopAvailable" type="xsd:boolean">\n' +
			                     '<dc:description>Is stop available or not?</dc:description>\n' +
			                     '<dependency>\n' +
				                     '<relevant>false()</relevant>\n' +
				                     '<write>false()</write>\n' +
			                     '</dependency>\n' +
		                     '</variable>\n' +
		                     '<variable id="isPauseAvailable" type="xsd:boolean">\n' +
			                     '<dc:description>Is pause available or not?</dc:description>\n' +
			                     '<dependency>\n' +
				                     '<relevant>false()</relevant>\n' +
				                     '<write>false()</write>\n' +
			                     '</dependency>\n' +
		                     '</variable>\n' +
		                     '<set id="renderingcontrol">\n' +
			                     '<dc:description xml:lang="en">This set contains elements pertaining to the rendering\n' +
				                     'settings of the media renderer, applying to its post-mix settings\n' +
				                     '(connection-independent).</dc:description>\n' +
			                     '<mapping platform="upnp-av">\n' +
				                     '<uis-upnp:annotation>***** Elements in this set are based on RenderingControl\n' +
					                     'Service ***** Elements of this set bind to the UPnP actions with parameter\n' +
					                     'instanceID=0. </uis-upnp:annotation>\n' +
			                     '</mapping>\n' +
			                     '<variable id="volumeMaxValue" type="xsd:unsignedShort" final="true" optional="true">\n' +
				                     '<dc:description xml:lang="en">Upper value limit for variable volume. </dc:description>\n' +
				                     '<mapping platform="upnp-av">\n' +
					                     '<uis-upnp:annotation>For initialization, extract upper limit for state variable\n' +
						                     'Volume from XML Service Description for RenderingControl service of Target\n' +
						                     'device (same for all channels).</uis-upnp:annotation>\n' +
				                     '</mapping>\n' +
			                     '</variable>\n' +
			                     '<set id="presets">\n' +
				                     '<dc:description xml:lang="en">This set contains pre-defined configuration settings\n' +
					                     'for the selected media renderer.</dc:description>\n' +
				                     '<variable id="presetNameList" type="uis:csvlist">\n' +
					                     '<dc:description xml:lang="en">This variable contains a comma-separated list\n' +
						                     '(i.e. CSV(string)) of valid preset names currently supported by this device.\n' +
						                     'presetNameList must include "FactoryDefaults", and may include\n' +
						                     '"InstallationDefaults". Its value changes if/when the device changes the set\n' +
						                     'of presets that it supports. This may occur in conjunction with a\n' +
						                     'vendor-defined action or some other non-UPnP event. This state variable will\n' +
						                     'include any of the predefined presets that are supported by the device.</dc:description>\n' +
					                     '<mapping platform="upnp-av">\n' +
						                     '<uis-upnp:annotation>UPnP action ListPresets for getting, and state variable\n' +
							                     'LastChange for updates.</uis-upnp:annotation>\n' +
					                     '</mapping>\n' +
					                     '<dependency>\n' +
						                     '<relevant>true()</relevant>\n' +
						                     '<write>false()</write>\n' +
					                     '</dependency>\n' +
				                     '</variable>\n' +
				                     '<command id="selectPreset" type="uis:basicCommand">\n' +
					                     '<dc:description xml:lang="en">This command restores (a subset) of the state\n' +
						                     'variables to the values associated with the specified preset. The specified\n' +
						                     'preset name may be one of those in presetNameList. The selected preset\n' +
						                     'determines which state variables will be affected.</dc:description>\n' +
					                     '<mapping platform="upnp-av">\n' +
						                     '<uis-upnp:annotation>UPnP action SelectPreset for setting. Getter method not\n' +
							                     'available. No updates.</uis-upnp:annotation>\n' +
					                     '</mapping>\n' +
					                     '<param id="presetName" type="xsd:string" dir="in">\n' +
						                     '<selection closed="true">\n' +
							                     '<selectionSetDynamic id="presetNameSelectionId" varRef="presetNameList"\n' +
							                     '/>\n' +
						                     '</selection>\n' +
					                     '</param>\n' +
					                     '<param idref="video" dir="out"> </param>\n' +
					                     '<param idref="chAudio" dir="out"> </param>\n' +
				                     '</command>\n' +
			                     '</set>\n' +
			                     '<set id="video">\n' +
				                     '<dc:description xml:lang="en">This set contains video rendering controls for the\n' +
					                     'selected media renderer.</dc:description>\n' +
				                     '<variable id="brightnessMaxValue" type="xsd:unsignedShort" final="true"\n' +
					                     'optional="true">\n' +
					                     '<dc:description xml:lang="en">Upper value limit for variable brightness.</dc:description>\n' +
					                     '<mapping platform="upnp-av">\n' +
						                     '<uis-upnp:annotation>For initialization, extract upper limit for state\n' +
							                     'variable Brightness from XML Service Description for RenderingControl\n' +
							                     'service of Target device.</uis-upnp:annotation>\n' +
					                     '</mapping>\n' +
				                     '</variable>\n' +
				                     '<variable id="contrastMaxValue" type="xsd:unsignedShort" final="true"\n' +
					                     'optional="true">\n' +
					                     '<dc:description xml:lang="en">Upper value limit for variable contrast.</dc:description>\n' +
					                     '<mapping platform="upnp-av">\n' +
						                     '<uis-upnp:annotation>For initialization, extract upper limit for state\n' +
							                     'variable Contrast from XML Service Description for RenderingControl\n' +
							                     'service of Target device.</uis-upnp:annotation>\n' +
					                     '</mapping>\n' +
				                     '</variable>\n' +
				                     '<variable id="sharpnessMaxValue" type="xsd:unsignedShort" final="true"\n' +
					                     'optional="true">\n' +
					                     '<dc:description xml:lang="en">Upper value limit for variable sharpness.</dc:description>\n' +
					                     '<mapping platform="upnp-av">\n' +
						                     '<uis-upnp:annotation>For initialization, extract upper limit for state\n' +
							                     'variable Sharpness from XML Service Description for RenderingControl\n' +
							                     'service of Target device.</uis-upnp:annotation>\n' +
					                     '</mapping>\n' +
				                     '</variable>\n' +
				                     '<variable id="redVideoGainMaxValue" type="xsd:unsignedShort" final="true"\n' +
					                     'optional="true">\n' +
					                     '<dc:description xml:lang="en">Upper value limit for variable redVideoGain.</dc:description>\n' +
					                     '<mapping platform="upnp-av">\n' +
						                     '<uis-upnp:annotation>For initialization, extract upper limit for state\n' +
							                     'variable RedVideoGain from XML Service Description for RenderingControl\n' +
							                     'service of Target device.</uis-upnp:annotation>\n' +
					                     '</mapping>\n' +
				                     '</variable>\n' +
				                     '<variable id="greenVideoGainMaxValue" type="xsd:unsignedShort" final="true"\n' +
					                     'optional="true">\n' +
					                     '<dc:description xml:lang="en">Upper value limit for variable greenVideoGain.</dc:description>\n' +
					                     '<mapping platform="upnp-av">\n' +
						                     '<uis-upnp:annotation>For initialization, extract upper limit for state\n' +
							                     'variable GreenVideoGain from XML Service Description for\n' +
							                     'RenderingControl service of Target device.</uis-upnp:annotation>\n' +
					                     '</mapping>\n' +
				                     '</variable>\n' +
				                     '<variable id="blueVideoGainMaxValue" type="xsd:unsignedShort" final="true"\n' +
					                     'optional="true">\n' +
					                     '<dc:description xml:lang="en">Upper value limit for variable blueVideoGain.</dc:description>\n' +
					                     '<mapping platform="upnp-av">\n' +
						                     '<uis-upnp:annotation>For initialization, extract upper limit for state\n' +
							                     'variable BlueVideoGain from XML Service Description for RenderingControl\n' +
							                     'service of Target device.</uis-upnp:annotation>\n' +
					                     '</mapping>\n' +
				                     '</variable>\n' +
				                     '<variable id="redVideoBlackLevelMaxValue" type="xsd:unsignedShort" final="true"\n' +
					                     'optional="true">\n' +
					                     '<dc:description xml:lang="en">Upper value limit for variable redVideoBlackLevel.</dc:description>\n' +
					                     '<mapping platform="upnp-av">\n' +
						                     '<uis-upnp:annotation>For initialization, extract upper limit for state\n' +
							                     'variable RedVideoBlackLevel from XML Service Description for\n' +
							                     'RenderingControl service of Target device.</uis-upnp:annotation>\n' +
					                     '</mapping>\n' +
				                     '</variable>\n' +
				                     '<variable id="greenVideoBlackLevelMaxValue" type="xsd:unsignedShort" final="true"\n' +
					                     'optional="true">\n' +
					                     '<dc:description xml:lang="en">Upper value limit for variable\n' +
						                     'greenVideoBlackLevel.</dc:description>\n' +
					                     '<mapping platform="upnp-av">\n' +
						                     '<uis-upnp:annotation>For initialization, extract upper limit for state\n' +
							                     'variable GreenVideoBlackLevel from XML Service Description for\n' +
							                     'RenderingControl service of Target device.</uis-upnp:annotation>\n' +
					                     '</mapping>\n' +
				                     '</variable>\n' +
				                     '<variable id="blueVideoBlackLevelMaxValue" type="xsd:unsignedShort" final="true"\n' +
					                     'optional="true">\n' +
					                     '<dc:description xml:lang="en">Upper value limit for variable\n' +
						                     'blueVideoBlackLevel.</dc:description>\n' +
					                     '<mapping platform="upnp-av">\n' +
						                     '<uis-upnp:annotation>For initialization, extract upper limit for state\n' +
							                     'variable BlueVideoBlackLevel from XML Service Description for\n' +
							                     'RenderingControl service of Target device.</uis-upnp:annotation>\n' +
					                     '</mapping>\n' +
				                     '</variable>\n' +
				                     '<variable id="colorTemperatureMaxValue" type="xsd:unsignedShort" final="true"\n' +
					                     'optional="true">\n' +
					                     '<dc:description xml:lang="en">Upper value limit for variable colorTemperature.</dc:description>\n' +
					                     '<mapping platform="upnp-av">\n' +
						                     '<uis-upnp:annotation>For initialization, extract upper limit for state\n' +
							                     'variable ColorTemperature from XML Service Description for\n' +
							                     'RenderingControl service of Target device.</uis-upnp:annotation>\n' +
					                     '</mapping>\n' +
				                     '</variable>\n' +
				                     '<variable id="horizontalKeystoneMinValue" type="xsd:short" final="true"\n' +
					                     'optional="true">\n' +
					                     '<dc:description xml:lang="en">Upper value limit for variable horizontalKeystone.</dc:description>\n' +
					                     '<mapping platform="upnp-av">\n' +
						                     '<uis-upnp:annotation>For initialization, extract lower limit for state\n' +
							                     'variable HorizontalKeystone from XML Service Description for\n' +
							                     'RenderingControl service of Target device.</uis-upnp:annotation>\n' +
					                     '</mapping>\n' +
				                     '</variable>\n' +
				                     '<variable id="horizontalKeystoneMaxValue" type="xsd:short" final="true"\n' +
					                     'optional="true">\n' +
					                     '<dc:description xml:lang="en">Upper value limit for variable horizontalKeystone.</dc:description>\n' +
					                     '<mapping platform="upnp-av">\n' +
						                     '<uis-upnp:annotation>For initialization, extract upper limit for state\n' +
							                     'variable HorizontalKeystone from XML Service Description for\n' +
							                     'RenderingControl service of Target device.</uis-upnp:annotation>\n' +
					                     '</mapping>\n' +
				                     '</variable>\n' +
				                     '<variable id="verticalKeystoneMinValue" type="xsd:short" final="true"\n' +
					                     'optional="true">\n' +
					                     '<dc:description xml:lang="en">Upper value limit for variable verticalKeystone.</dc:description>\n' +
					                     '<mapping platform="upnp-av">\n' +
						                     '<uis-upnp:annotation>For initialization, extract lower limit for state\n' +
							                     'variable VerticalKeystone from XML Service Description for\n' +
							                     'RenderingControl service of Target device.</uis-upnp:annotation>\n' +
					                     '</mapping>\n' +
				                     '</variable>\n' +
				                     '<variable id="verticalKeystoneMaxValue" type="xsd:short" final="true"\n' +
					                     'optional="true">\n' +
					                     '<dc:description xml:lang="en">Upper value limit for variable verticalKeystone.</dc:description>\n' +
					                     '<mapping platform="upnp-av">\n' +
						                     '<uis-upnp:annotation>For initialization, extract upper limit for state\n' +
							                     'variable VerticalKeystone from XML Service Description for\n' +
							                     'RenderingControl service of Target device.</uis-upnp:annotation>\n' +
					                     '</mapping>\n' +
				                     '</variable>\n' +
				                     '<variable id="brightness" type="xsd:unsignedShort" optional="true">\n' +
					                     '<dc:description xml:lang="en">This unsigned integer variable represents the\n' +
						                     'current brightness setting of the associated display device. Its value\n' +
						                     'ranges from a minimum of 0 to some device specific maximum. A numerical\n' +
						                     'change of 1 corresponds to the smallest incremental change that is supported\n' +
						                     'by the device.</dc:description>\n' +
					                     '<mapping platform="upnp-av">\n' +
						                     '<uis-upnp:annotation>Action GetBrightness for getting, SetBrightness for\n' +
							                     'setting. State variable LastChange for updates.</uis-upnp:annotation>\n' +
					                     '</mapping>\n' +
					                     '<dependency>\n' +
						                     '<maxInclusive>value(\'brightnessMaxValue\')</maxInclusive>\n' +
					                     '</dependency>\n' +
				                     '</variable>\n' +
				                     '<variable id="contrast" type="xsd:unsignedShort" optional="true">\n' +
					                     '<dc:description xml:lang="en">This unsigned integer variable represents the\n' +
						                     'current contrast setting of the associated display device. Its value ranges\n' +
						                     'from a minimum of 0 to some device specific maximum. A numerical change of 1\n' +
						                     'corresponds to the smallest incremental change that is supported by the\n' +
						                     'device.</dc:description>\n' +
					                     '<mapping platform="upnp-av">\n' +
						                     '<uis-upnp:annotation>Action GetContrast for getting, SetContrast for\n' +
							                     'setting. State variable LastChange for updates.</uis-upnp:annotation>\n' +
					                     '</mapping>\n' +
					                     '<dependency>\n' +
						                     '<maxInclusive>value(\'contrastMaxValue\')</maxInclusive>\n' +
					                     '</dependency>\n' +
				                     '</variable>\n' +
				                     '<variable id="sharpness" type="xsd:unsignedShort" optional="true">\n' +
					                     '<dc:description xml:lang="en">This unsigned integer variable represents the\n' +
						                     'current sharpness setting of the associated display device. Its value ranges\n' +
						                     'from a minimum of 0 to some device specific maximum. A numerical change of 1\n' +
						                     'corresponds to the smallest incremental change that is supported by the\n' +
						                     'device.</dc:description>\n' +
					                     '<mapping platform="upnp-av">\n' +
						                     '<uis-upnp:annotation>Action GetSharpness for getting, SetSharpness for\n' +
							                     'setting. State variable LastChange for updates.</uis-upnp:annotation>\n' +
					                     '</mapping>\n' +
					                     '<dependency>\n' +
						                     '<maxInclusive>value(\'sharpnessMaxValue\')</maxInclusive>\n' +
					                     '</dependency>\n' +
				                     '</variable>\n' +
				                     '<variable id="redVideoGain" type="xsd:unsignedShort" optional="true">\n' +
					                     '<dc:description xml:lang="en">This unsigned integer variable represents the\n' +
						                     'current setting of the red "gain" control for the associated display device.\n' +
						                     'Its value ranges from a minimum of 0 to some device specific maximum. A\n' +
						                     'numerical change of 1 corresponds to the smallest incremental change that is\n' +
						                     'supported by the device.</dc:description>\n' +
					                     '<mapping platform="upnp-av">\n' +
						                     '<uis-upnp:annotation>Action GetRedVideoGain for getting, SetRedVideoGain for\n' +
							                     'setting. State variable LastChange for updates.</uis-upnp:annotation>\n' +
					                     '</mapping>\n' +
					                     '<dependency>\n' +
						                     '<maxInclusive>value(\'redVideoGainMaxValue\')</maxInclusive>\n' +
					                     '</dependency>\n' +
				                     '</variable>\n' +
				                     '<variable id="greenVideoGain" type="xsd:unsignedShort" optional="true">\n' +
					                     '<dc:description xml:lang="en">This unsigned integer variable represents the\n' +
						                     'current setting of the green "gain" control for the associated display\n' +
						                     'device. Its value ranges from a minimum of 0 to some device specific\n' +
						                     'maximum. A numerical change of 1 corresponds to the smallest incremental\n' +
						                     'change that is supported by the device.</dc:description>\n' +
					                     '<mapping platform="upnp-av">\n' +
						                     '<uis-upnp:annotation>Action GetGreenVideoGain for getting, SetGreenVideoGain\n' +
							                     'for setting. State variable LastChange for\n' +
						                     'updates.</uis-upnp:annotation>\n' +
					                     '</mapping>\n' +
					                     '<dependency>\n' +
						                     '<maxInclusive>value(\'greenVideoGainMaxValue\')</maxInclusive>\n' +
					                     '</dependency>\n' +
				                     '</variable>\n' +
				                     '<variable id="blueVideoGain" type="xsd:unsignedShort" optional="true">\n' +
					                     '<dc:description xml:lang="en">This unsigned integer variable represents the\n' +
						                     'current setting of the blue "gain" control for the associated display\n' +
						                     'device. Its value ranges from a minimum of 0 to some device specific\n' +
						                     'maximum. A numerical change of 1 corresponds to the smallest incremental\n' +
						                     'change that is supported by the device.</dc:description>\n' +
					                     '<mapping platform="upnp-av">\n' +
						                     '<uis-upnp:annotation>Action GetBlueVideoGain for getting, SetBlueVideoGain\n' +
							                     'for setting. State variable LastChange for\n' +
						                     'updates.</uis-upnp:annotation>\n' +
					                     '</mapping>\n' +
					                     '<dependency>\n' +
						                     '<maxInclusive>value(\'blueVideoGainMaxValue\')</maxInclusive>\n' +
					                     '</dependency>\n' +
				                     '</variable>\n' +
				                     '<variable id="redVideoBlackLevel" type="xsd:unsignedShort" optional="true">\n' +
					                     '<dc:description xml:lang="en">This unsigned integer variable represents the\n' +
						                     'current setting for the minimum output intensity of red for the associated\n' +
						                     'display device. Its value ranges from a minimum of 0 to some device specific\n' +
						                     'maximum. A numerical change of 1 corresponds to the smallest incremental\n' +
						                     'change that is supported by the device.</dc:description>\n' +
					                     '<mapping platform="upnp-av">\n' +
						                     '<uis-upnp:annotation>Action GetRedVideoBlackLevel for getting,\n' +
							                     'SetRedVideoBlackLevel for setting. State variable LastChange for\n' +
							                     'updates.</uis-upnp:annotation>\n' +
					                     '</mapping>\n' +
					                     '<dependency>\n' +
						                     '<maxInclusive>value(\'redVideoBlackLevelMaxValue\')</maxInclusive>\n' +
					                     '</dependency>\n' +
				                     '</variable>\n' +
				                     '<variable id="greenVideoBlackLevel" type="xsd:unsignedShort" optional="true">\n' +
					                     '<dc:description xml:lang="en">This unsigned integer variable represents the\n' +
						                     'current setting for the minimum output intensity of green for the associated\n' +
						                     'display device. Its value ranges from a minimum of 0 to some device specific\n' +
						                     'maximum. A numerical change of 1 corresponds to the smallest incremental\n' +
						                     'change that is supported by the device.</dc:description>\n' +
					                     '<mapping platform="upnp-av">\n' +
						                     '<uis-upnp:annotation>Action GetGreenVideoBlackLevel for getting,\n' +
							                     'SetGreenVideoBlackLevel for setting. State variable LastChange for\n' +
							                     'updates.</uis-upnp:annotation>\n' +
					                     '</mapping>\n' +
					                     '<dependency>\n' +
						                     '<maxInclusive>value(\'greenVideoBlackLevelMaxValue\')</maxInclusive>\n' +
					                     '</dependency>\n' +
				                     '</variable>\n' +
				                     '<variable id="blueVideoBlackLevel" type="xsd:unsignedShort" optional="true">\n' +
					                     '<dc:description xml:lang="en">This unsigned integer variable represents the\n' +
						                     'current setting for the minimum output intensity of blue for the associated\n' +
						                     'display device. Its value ranges from a minimum of 0 to some device specific\n' +
						                     'maximum. A numerical change of 1 corresponds to the smallest incremental\n' +
						                     'change that is supported by the device.</dc:description>\n' +
					                     '<mapping platform="upnp-av">\n' +
						                     '<uis-upnp:annotation>Action GetBlueVideoBlackLevel for getting,\n' +
							                     'SetBlueVideoBlackLevel for setting. State variable LastChange for\n' +
							                     'updates.</uis-upnp:annotation>\n' +
					                     '</mapping>\n' +
					                     '<dependency>\n' +
						                     '<maxInclusive>value(\'blueVideoBlackLevelMaxValue\')</maxInclusive>\n' +
					                     '</dependency>\n' +
				                     '</variable>\n' +
				                     '<variable id="colorTemperature" type="xsd:unsignedShort" optional="true">\n' +
					                     '<dc:description xml:lang="en">This unsigned integer variable represents the\n' +
						                     'current setting for the "color quality" of white for the associated display\n' +
						                     'device. Its value ranges from a minimum of 0 to some device specific\n' +
						                     'maximum. A numerical change of 1 corresponds to the smallest incremental\n' +
						                     'change that is supported by the device</dc:description>\n' +
					                     '<mapping platform="upnp-av">\n' +
						                     '<uis-upnp:annotation>Action GetColorTemperature for getting,\n' +
							                     'SetColorTemperature for setting. State variable LastChange for\n' +
						                     'updates.</uis-upnp:annotation>\n' +
					                     '</mapping>\n' +
					                     '<dependency>\n' +
						                     '<maxInclusive>value(\'colorTemperatureMaxValue\')</maxInclusive>\n' +
					                     '</dependency>\n' +
				                     '</variable>\n' +
				                     '<variable id="horizontalKeystone" type="xsd:short" optional="true">\n' +
					                     '<dc:description xml:lang="en">This signed integer variable represents the\n' +
						                     'current level of compensation for horizontal distortion of the associated\n' +
						                     'display device. Its value ranges from device-specific negative number to a\n' +
						                     'device specific positive number. Zero does not need to be in the middle of\n' +
						                     'this range, although it will be for most devices. A numerical change of 1\n' +
						                     'corresponds to the smallest incremental change that is supported by the\n' +
						                     'device.</dc:description>\n' +
					                     '<mapping platform="upnp-av">\n' +
						                     '<uis-upnp:annotation>Action GetHorizontalKeystone for getting,\n' +
							                     'SetHorizontalKeystone for setting. State variable LastChange for\n' +
							                     'updates.</uis-upnp:annotation>\n' +
					                     '</mapping>\n' +
					                     '<dependency>\n' +
						                     '<minInclusive>value(\'horizontalKeystoneMinValue\')</minInclusive>\n' +
						                     '<maxInclusive>value(\'horizontalKeystoneMaxValue\')</maxInclusive>\n' +
					                     '</dependency>\n' +
				                     '</variable>\n' +
				                     '<variable id="verticalKeystone" type="xsd:short" optional="true">\n' +
					                     '<dc:description xml:lang="en">This signed integer variable represents the\n' +
						                     'current level of compensation for vertical distortion of the associated\n' +
						                     'display device. Its value ranges from device-specific negative number to a\n' +
						                     'device specific positive number. Zero does not need to be in the middle of\n' +
						                     'this range, although it will be for most devices. A numerical change of 1\n' +
						                     'corresponds to the smallest incremental change that is supported by the\n' +
						                     'device.</dc:description>\n' +
					                     '<mapping platform="upnp-av">\n' +
						                     '<uis-upnp:annotation>Action GetVerticalKeystone for getting,\n' +
							                     'SetVerticalKeystone for setting. State variable LastChange for\n' +
						                     'updates.</uis-upnp:annotation>\n' +
					                     '</mapping>\n' +
					                     '<dependency>\n' +
						                     '<minInclusive>value(\'verticalKeystoneMinValue\')</minInclusive>\n' +
						                     '<maxInclusive>value(\'verticalKeystoneMaxValue\')</maxInclusive>\n' +
					                     '</dependency>\n' +
				                     '</variable>\n' +
			                     '</set>\n' +
			                     '<set id="chAudio" dim="uis-upnp:channelType">\n' +
				                     '<!--The set\'s repeating is based on uis-upnp:channelType, i.e. a subset of channelType will be used as indices.  The labels for the individual entries of channelType should be used to denote the individual group iterations.-->\n' +
				                     '<dc:description xml:lang="en">This set contains audio settings per channel, for the\n' +
					                     'selected media renderer.</dc:description>\n' +
				                     '<variable id="chVolumeDBMinValue" type="xsd:short" final="true" optional="true">\n' +
					                     '<dc:description xml:lang="en">Lower value limit for channel-specific variable\n' +
						                     'volumeDB.</dc:description>\n' +
					                     '<mapping platform="upnp-av">\n' +
						                     '<uis-upnp:annotation>For initialization, call UPnP action\n' +
						                     'GetVolumeDBRange.</uis-upnp:annotation>\n' +
					                     '</mapping>\n' +
				                     '</variable>\n' +
				                     '<variable id="chVolumeDBMaxValue" type="xsd:short" final="true" optional="true">\n' +
					                     '<dc:description xml:lang="en">Upper value limit for channel-specific variable\n' +
						                     'volumeDB.</dc:description>\n' +
					                     '<mapping platform="upnp-av">\n' +
						                     '<uis-upnp:annotation>For initialization, call UPnP action\n' +
						                     'GetVolumeDBRange.</uis-upnp:annotation>\n' +
					                     '</mapping>\n' +
				                     '</variable>\n' +
				                     '<variable id="chMute" type="xsd:boolean" optional="true">\n' +
					                     '<dc:description xml:lang="en">"Mute" setting of the corresponding channel</dc:description>\n' +
					                     '<mapping platform="upnp-av">\n' +
						                     '<uis-upnp:annotation>Action SetMute for setting, GetMute for getting. State\n' +
							                     'variable LastChange for updates.</uis-upnp:annotation>\n' +
					                     '</mapping>\n' +
				                     '</variable>\n' +
				                     '<variable id="chVolume" type="xsd:unsignedShort" optional="true">\n' +
					                     '<dc:description xml:lang="en">Volume of corresponding channel</dc:description>\n' +
					                     '<mapping platform="upnp-av">\n' +
						                     '<uis-upnp:annotation>Action SetVolume for setting, GetVolume for getting.\n' +
							                     'State variable LastChange for updates.</uis-upnp:annotation>\n' +
					                     '</mapping>\n' +
					                     '<dependency>\n' +
						                     '<write>not(value(\'./chMute\'))</write>\n' +
						                     '<maxInclusive>value(\'./chVolumeMaxValue\')</maxInclusive>\n' +
					                     '</dependency>\n' +
				                     '</variable>\n' +
				                     '<variable id="chVolumeDB" type="xsd:short" optional="true">\n' +
					                     '<dc:description xml:lang="en">This signed integer variable represents the\n' +
						                     'current volume setting of the associated audio channel. Its value represents\n' +
						                     'the current setting in units of "1/256 of a decibel (dB)". This means that a\n' +
						                     'numeric change of 1 corresponds to a volume change of 1/256 dB. The value\n' +
						                     'range for this variable is a minimum value of -32,767 (800116) (which\n' +
						                     'equals -127.9961dB) and a maximum value of +32,767 (7FFF16) (which equals\n' +
						                     '+127.9961dB). The value corresponding to 800016 is invalid. If there is any\n' +
						                     'chVolumeDB variable, chVolumeDB[Master] must be present. Note that each\n' +
						                     'chVolumeDB variable has its own value space (type).</dc:description>\n' +
					                     '<mapping platform="upnp-av">\n' +
						                     '<uis-upnp:annotation>UPnP action GetVolumeDB for getting, and SetVolumeDB\n' +
							                     'for setting. State variable LastChange for\n' +
						                     'updates.</uis-upnp:annotation>\n' +
					                     '</mapping>\n' +
					                     '<dependency>\n' +
						                     '<write>not(value(\'./chMute\'))</write>\n' +
						                     '<minInclusive>value(\'./chVolumeDBMinValue\')</minInclusive>\n' +
						                     '<maxInclusive>value(\'./chVolumeDBMaxValue\')</maxInclusive>\n' +
					                     '</dependency>\n' +
				                     '</variable>\n' +
				                     '<variable id="chLoudness" type="xsd:boolean" optional="true">\n' +
					                     '<dc:description xml:lang="en">This boolean variable represents the current\n' +
						                     '"loudness" setting of the associated audio channel. A value of TRUE\n' +
						                     'indicates that the loudness effect is active.</dc:description>\n' +
					                     '<mapping platform="upnp-av">\n' +
						                     '<uis-upnp:annotation>UPnP action GetLoudness for getting, SetLoudness for\n' +
							                     'setting. State variable LastChange for updates.</uis-upnp:annotation>\n' +
					                     '</mapping>\n' +
					                     '<dependency>\n' +
						                     '<write>not(value(\'./chMute\'))</write>\n' +
					                     '</dependency>\n' +
				                     '</variable>\n' +
			                     '</set>\n' +
		                     '</set>\n' +
	                     '</set>\n' +
	                     '<variable id="commonConnectionProtocols" type="uis:csvlist" dim="xsd:unsignedInt" final="true">\n' +
		                     '<!--type should be uis:csvlist of upnp-uis:connectionProtocol, but this is not possible to express. -->\n' +
		                     '<dc:description xml:lang="en">CSV list of available connection protocols (in ProtocolInfo\n' +
			                     'coding) that can be used for playing content between the selected Media Server (sourcet)\n' +
			                     'and the selected Media Renderer (sink). May be empty. Comes with resources, such as\n' +
			                     'labels (human-readable description of connection such as "JPEG over HTTP"). See\n' +
			                     'ConnectionManager spec, section 2.5.2 for details on the ProtocolInfo format.</dc:description>\n' +
		                     '<mapping platform="upnp-av">\n' +
			                     '<uis-upnp:annotation>Overlapping connection protocols, as exposed by the GetProtocolInfo\n' +
				                     'actions of MediaServer.ConnectionManager and MediaRenderer.ConnectionManager. Needs\n' +
				                     'to be updated if either selectedMediaServer or selectedMediaRenderer changes. Note:\n' +
				                     'Since there is no type definition for ProtocolInfo, we are using xsd:string as base\n' +
				                     'type (as in the ConnectionManager spec).</uis-upnp:annotation>\n' +
		                     '</mapping>\n' +
		                     '<dependency>\n' +
			                     '<relevant>false()</relevant>\n' +
			                     '<write>false()</write>\n' +
		                     '</dependency>\n' +
	                     '</variable>\n' +
	                     '<command id="connect" type="uis:timedCommand">\n' +
		                     '<dc:description xml:lang="en">Open a connection between the selected media server and media\n' +
			                     'renderer for playing content. Command state shall reflect success or failure. After\n' +
			                     'successful invocation, the "connections" set has a new actual index which specifies the\n' +
			                     'newly created connection. Note: A connection may cause an error even after it has been\n' +
			                     'successfully set up. In this case any of the notifications in set "conNotifications"\n' +
			                     'will be raised.</dc:description>\n' +
		                     '<mapping platform="upnp-av">\n' +
			                     '<uis-upnp:annotation>The user may choose an appropriate connection protocol as\n' +
				                     'parameter. Otherwise the system will pick one.</uis-upnp:annotation>\n' +
		                     '</mapping>\n' +
		                     '<param idref="isMediaServerSelected" dir="in"/>\n' +
		                     '<param idref="isMediaRendererSelected" dir="in"/>\n' +
		                     '<param idref="selectedNodeId" dir="in"/>\n' +
		                     '<param id="preferredConnectionProtocol" dir="in" type="uis-upnp:connectionProtocolType">\n' +
			                     '<dc:description>User-preferred connection protocol. May be left empty to indicate no\n' +
				                     'preference. Note that the system may use a different protocol if the preferred\n' +
				                     'protocol is not available.</dc:description>\n' +
			                     '<uis:selection closed="false">\n' +
				                     '<uis:selectionSetDynamic id="preferredConnectionProtocolSelection"\n' +
					                     'varRef="commonConnectionProtocols"/>\n' +
			                     '</uis:selection>\n' +
		                     '</param>\n' +
		                     '<param id="newConnectionId" type="uis-upnp:connectionIdType" dir="out">\n' +
			                     '<dc:description xml:lang="en">Identifier for new connection. This identifier can be used\n' +
				                     'as index for the connections set to get access to the newly created connection. Note\n' +
				                     'that command state must be "done" or "succeeded" after successful\n' +
			                     'creation.</dc:description>\n' +
		                     '</param>\n' +
		                     '<param id="connectionError" type="uis-upnp:connectionErrorType" dir="out">\n' +
			                     '<dc:description xml:lang="en">Must be "noError" if command state is not "failed".\n' +
			                     '</dc:description>\n' +
		                     '</param>\n' +
	                     '</command>\n' +
	                     '<set id="connections" dim="uis-upnp:connectionIdType">\n' +
		                     '<dc:description xml:lang="en">This set contains the current connections (along the index of\n' +
			                     'connection ids). Initially empty (no actual index). The user can create new connections\n' +
			                     'by invoking the command "connect". The user can remove a connection by invoking the\n' +
			                     'command "removeConnection".</dc:description>\n' +
		                     '<mapping platform="upnp-av">\n' +
			                     '<uis-upnp:annotation>When populating this set with a new instance, the system is\n' +
				                     'responsible for setting up a connection between media server and media renderer\n' +
				                     'through the connection services on either end. Only successful connections will\n' +
				                     'result in a new actual index.</uis-upnp:annotation>\n' +
		                     '</mapping>\n' +
		                     '<dependency>\n' +
			                     '<insert>false()</insert>\n' +
		                     '</dependency>\n' +
		                     '<command id="removeConnection" type="uis:timedCommand">\n' +
			                     '<dc:description xml:lang="en">Close the connection that this command belongs to.</dc:description>\n' +
			                     '<mapping platform="upnp-av">\n' +
				                     '<uis-upnp:annotation>UPnP action closeConnection on\n' +
				                     'ConnectionService.</uis-upnp:annotation>\n' +
			                     '</mapping>\n' +
		                     '</command>\n' +
		                     '<set id="conSetup">\n' +
			                     '<dc:description xml:lang="en">Basic connection properties.</dc:description>\n' +
			                     '<variable id="connectedMediaServer" type="upnp-av:UDN" final="true" includesRes="true">\n' +
				                     '<dc:description xml:lang="en">Media server of the connection.</dc:description>\n' +
				                     '<mapping platform="upnp-av">\n' +
					                     '<uis-upnp:annotation>UDN of media server.</uis-upnp:annotation>\n' +
				                     '</mapping>\n' +
			                     '</variable>\n' +
			                     '<variable id="connectedMediaServerFriendlyName" type="xsd:string"><!--Open issue: This variable will not be needed if we can find a way for TAs to deliver dynamic resources for values of external types (in this case values of upnp-av:UDN).-->\n' +
				                     '<dc:description xml:lang="en">Friendly name of connected media server.</dc:description>\n' +
				                     '<mapping platform="upnp-av">\n' +
					                     '<uis-upnp:annotation>Friendly name from device description.</uis-upnp:annotation>\n' +
				                     '</mapping>\n' +
				                     '<dependency>\n' +
					                     '<relevant>true()</relevant>\n' +
					                     '<write>false()</write>\n' +
				                     '</dependency>\n' +
			                     '</variable>\n' +
			                     '<variable id="connectedMediaRenderer" type="upnp-av:UDN" final="true" includesRes="true">\n' +
				                     '<dc:description xml:lang="en">Media renderer of the connection.</dc:description>\n' +
				                     '<mapping platform="upnp-av">\n' +
					                     '<uis-upnp:annotation>UDN of media renderer.</uis-upnp:annotation>\n' +
				                     '</mapping>\n' +
			                     '</variable>\n' +
			                     '<variable id="connectedMediaRendererFriendlyName" type="xsd:string"><!--Open issue: This variable will not be needed if we can find a way for TAs to deliver dynamic resources for values of external types (in this case values of upnp-av:UDN).-->\n' +
				                     '<dc:description xml:lang="en">Friendly name of connected media renderer.</dc:description>\n' +
				                     '<mapping platform="upnp-av">\n' +
					                     '<uis-upnp:annotation>Friendly name from device description.</uis-upnp:annotation>\n' +
				                     '</mapping>\n' +
				                     '<dependency>\n' +
					                     '<relevant>true()</relevant>\n' +
					                     '<write>false()</write>\n' +
				                     '</dependency>\n' +
			                     '</variable>			\n' +
			                     '<variable id="connectionProtocol" type="uis-upnp:connectionProtocolType" final="true">\n' +
				                     '<dc:description xml:lang="en">This value reflects the details of the protocol used\n' +
					                     'for the connection.</dc:description>\n' +
			                     '</variable>\n' +
		                     '</set>\n' +
		                     '<set id="transport">\n' +
			                     '<mapping platform="upnp-av">\n' +
				                     '<uis-upnp:annotation>***** Elements in this set are based on AVTransport Service\n' +
					                     '***** Note: The UPnP commands GetMediaInfo, GetTransportInfo, GetPositionInfo,\n' +
					                     'GetDeviceCapabilities and GetTransportSettings have no counterparts in the UI\n' +
					                     'Socket since this information is already available through socket\n' +
				                     'variables.</uis-upnp:annotation>\n' +
			                     '</mapping>\n' +
			                     '<variable id="possiblePlaybackStorageMedia" type="uis:csvlist" final="true">\n' +
				                     '<dc:description xml:lang="en">Contains a static, comma-separated list of storage\n' +
					                     'media that the device can play.</dc:description>\n' +
				                     '<mapping platform="upnp-av">\n' +
					                     '<uis-upnp:annotation>UPnP action GetDeviceCapabilities for getting. No updates\n' +
						                     'expected since static. Note: Since XSD has no means of defining the item\n' +
						                     'type of a CSV list, we are implicitly using xsd:string here as item type.\n' +
						                     'Correct item types are\n' +
					                     'upnp-av:storageMedium.wc.values.</uis-upnp:annotation>\n' +
				                     '</mapping>\n' +
				                     '<dependency>\n' +
					                     '<relevant>false()</relevant>\n' +
					                     '<write>false()</write>\n' +
				                     '</dependency>\n' +
			                     '</variable>\n' +
			                     '<variable id="possibleRecordStorageMedia" type="uis:csvlist" final="true"\n' +
				                     'optional="true">\n' +
				                     '<dc:description xml:lang="en">Contains a static, comma-separated list of storage\n' +
					                     'media onto which the device can record.</dc:description>\n' +
				                     '<mapping platform="upnp-av">\n' +
					                     '<uis-upnp:annotation>UPnP action GetDeviceCapabilities for getting. No updates\n' +
						                     'expected since static. Not present if value is "NOT_IMPLEMENTED" (i.e.\n' +
						                     'service doesn\'t support recording). Note: Since XSD has no means of defining\n' +
						                     'the item type of a CSV list, we are implicitly using xsd:string here as item\n' +
						                     'type. Correct item types are\n' +
					                     'upnp-av:storageMedium.wc.values.</uis-upnp:annotation>\n' +
				                     '</mapping>\n' +
				                     '<dependency>\n' +
					                     '<relevant>false()</relevant>\n' +
					                     '<write>false()</write>\n' +
				                     '</dependency>\n' +
			                     '</variable>\n' +
			                     '<variable id="possibleRecordQualityModes" type="uis:csvlist" final="true"\n' +
				                     'optional="true">\n' +
				                     '<dc:description xml:lang="en">Contains a static, comma-separated list of recording\n' +
					                     'quality modes that the device supports. For example, for an analog VHS recorder,\n' +
					                     'the string would be "0:EP,1:LP,2:SP", while for a PVR the string would be\n' +
					                     '"0:BASIC,1:MEDIUM,2:HIGH". The string specifics depend on the type of device\n' +
					                     'containing the AVTransport. Note that record quality modes are independent of\n' +
					                     'the content-format that may be exposed to the network through a\n' +
					                     'ConnectionManager service. </dc:description>\n' +
				                     '<mapping platform="upnp-av">\n' +
					                     '<uis-upnp:annotation>UPnP action GetDeviceCapabilities for getting. State\n' +
						                     'variable LastChange for updates. Not present if value is "NOT_IMPLEMENTED"\n' +
						                     '(i.e. service doesn\'t support recording). Note: Since XSD has no means of\n' +
						                     'defining the item type of a CSV list, we are implicitly using xsd:string\n' +
						                     'here as item type. Correct item types are\n' +
					                     'uis-upnp:recordQualityModeType.</uis-upnp:annotation>\n' +
				                     '</mapping>\n' +
				                     '<dependency>\n' +
					                     '<relevant>false()</relevant>\n' +
					                     '<write>false()</write>\n' +
				                     '</dependency>\n' +
			                     '</variable>\n' +
			                     '<variable id="isCurrentPlayModeSettable" type="xsd:boolean" final="true">\n' +
				                     '<dc:description xml:lang="en">Indicates whether the play mode can be set by the\n' +
					                     'user.</dc:description>\n' +
				                     '<mapping platform="upnp-av">\n' +
					                     '<uis-upnp:annotation>True if UPnP action SetPlayMode is available, otherwise\n' +
						                     'false.</uis-upnp:annotation>\n' +
				                     '</mapping>\n' +
				                     '<dependency>\n' +
					                     '<relevant>false()</relevant>\n' +
					                     '<write>false()</write>\n' +
				                     '</dependency>\n' +
			                     '</variable>\n' +
			                     '<variable id="recordQualityModeSettable" type="xsd:boolean" final="true" optional="true">\n' +
				                     '<dc:description xml:lang="en">Indicates that the currentRecordQualityMode can be set\n' +
					                     'by the user.</dc:description>\n' +
				                     '<mapping platform="upnp-av">\n' +
					                     '<uis-upnp:annotation>True if UPnP action SetRecordQualityMode is present,\n' +
						                     'otherwise false. Not present if value of UPnP variable\n' +
						                     'CurrentRecordQualityMode is "NOT_IMPLEMENTED".</uis-upnp:annotation>\n' +
				                     '</mapping>\n' +
				                     '<dependency>\n' +
					                     '<relevant>false()</relevant>\n' +
					                     '<write>false()</write>\n' +
				                     '</dependency>\n' +
			                     '</variable>\n' +
			                     '<variable id="availableTransportPlaySpeeds" type="uis-upnp:transportPlaySpeedListType"\n' +
				                     'final="true">\n' +
				                     '<dc:description xml:lang="en">Space-separated list of allowed values for variable\n' +
					                     'transportPlaySpeed. Note: This cannot be defined as type since it may vary\n' +
					                     'between devices.</dc:description>\n' +
				                     '<mapping platform="upnp-av">\n' +
					                     '<uis-upnp:annotation>Retrieve actually supported speeds from the AVTransport\n' +
						                     'service description, AllowedValueList for state variable\n' +
					                     'TransportPlaySpeed.</uis-upnp:annotation>\n' +
				                     '</mapping>\n' +
				                     '<dependency>\n' +
					                     '<relevant>false()</relevant>\n' +
					                     '<write>false()</write>\n' +
				                     '</dependency>\n' +
			                     '</variable>\n' +
			                     '<variable id="transportState" type="uis-upnp:transportStateType">\n' +
				                     '<!--Note: Vendor-specific values of the UPnP state variable TransportStatus may give indication for additional notify elements.-->\n' +
				                     '<dc:description xml:lang="en">Conceptually \'top-level\' state of the transport, e.g.,\n' +
					                     'whether it is playing, recording, etc.</dc:description>\n' +
				                     '<mapping platform="upnp-av">\n' +
					                     '<uis-upnp:annotation>UPnP action GetMediaInfo for getting. UPnP state variable\n' +
						                     'LastChange for updates.</uis-upnp:annotation>\n' +
				                     '</mapping>\n' +
				                     '<dependency>\n' +
					                     '<relevant>true()</relevant>\n' +
					                     '<write>false()</write>\n' +
				                     '</dependency>\n' +
			                     '</variable>\n' +
			                     '<notify id="transportStatusError" category="error">\n' +
				                     '<dc:description xml:lang="en">This notification is raised when the transport service\n' +
					                     'indicates an error.</dc:description>\n' +
				                     '<mapping platform="upnp-av">\n' +
					                     '<uis-upnp:annotation>Active if TransportStatus is ERROR_OCCURRED. UPnP action\n' +
						                     'GetTransportInfo for getting. LastChange for updates.</uis-upnp:annotation>\n' +
				                     '</mapping>\n' +
				                     '<dependency>\n' +
					                     '<explicitAck>false()</explicitAck>\n' +
				                     '</dependency>\n' +
			                     '</notify>\n' +
			                     '<variable id="playbackStorageMedium" type="upnp-av:storageMedium.wc.values">\n' +
				                     '<dc:description xml:lang="en">Indicates the storage medium of the resource specified\n' +
					                     'by AVTransportURI. If no resource is specified, then the state variable is set\n' +
					                     'to "NONE". If AVTransportURI refers to a resource received from the UPnP network\n' +
					                     'the state variable is set to "NETWORK". Device vendors may extend the specified\n' +
					                     'allowed value list of this variable. For example, various types of solid-state\n' +
					                     'media formats may be added in a vendor-specific way.</dc:description>\n' +
				                     '<mapping platform="upnp-av">\n' +
					                     '<uis-upnp:annotation>UPnP action GetMediaInfo for getting. UPnP state variable\n' +
						                     'LastChange for updates.</uis-upnp:annotation>\n' +
				                     '</mapping>\n' +
				                     '<dependency>\n' +
					                     '<write>false()</write>\n' +
				                     '</dependency>\n' +
				                     '<selection>\n' +
					                     '<selectionSetDynamic id="selectionPlaybackStorageMedium"\n' +
						                     'varRef="possiblePlaybackStorageMedia"/>\n' +
				                     '</selection>\n' +
				                     '<!--Issue: Because the type definition of upnp-av:storageMedium.wc.values does not have an id attribute, we cannot define labels for the values of this type.  However, we can define labels for the variable playbackStorageMedium.-->\n' +
			                     '</variable>\n' +
			                     '<variable id="recordStorageMedium" type="upnp-av:storageMedium.wc.values"\n' +
				                     'optional="true">\n' +
				                     '<dc:description xml:lang="en">Indicates the storage medium where the resource\n' +
					                     'specified by AVTransportURI will be recorded when a Record action is issued. If\n' +
					                     'no resource is specified, then the state variable is set to "NONE". Device\n' +
					                     'vendors may extend the allowed value list of this variable. For example, various\n' +
					                     'types of solid-state media formats may be added in a vendor-specific way.</dc:description>\n' +
				                     '<mapping platform="upnp-av">\n' +
					                     '<uis-upnp:annotation>UPnP action GetMediaInfo for getting. UPnP state variable\n' +
						                     'LastChange for updates. Not present if value is "NOT_IMPLEMENTED" (i.e.\n' +
						                     'service doesn\'t support recording).</uis-upnp:annotation>\n' +
				                     '</mapping>\n' +
				                     '<dependency>\n' +
					                     '<relevant>true()</relevant>\n' +
					                     '<write>false()</write>\n' +
				                     '</dependency>\n' +
				                     '<selection>\n' +
					                     '<selectionSetDynamic id="selectionRecordStorageMedium"\n' +
						                     'varRef="possibleRecordStorageMedia"/>\n' +
				                     '</selection>\n' +
			                     '</variable>\n' +
			                     '<variable id="currentPlayMode" type="uis-upnp:playModeType">\n' +
				                     '<dc:description xml:lang="en">Indicates the current play mode (e.g., random play,\n' +
					                     'repeated play, etc.). This notion is typical for CD-based audio media, but is\n' +
					                     'generally not supported by tape-based media. Value "DIRECT_1" indicates playing\n' +
					                     'a single track and then stop (don\'t play the next track). Value "INTRO"\n' +
					                     'indicates playing a short sample (typically 10 seconds or so) of each track on\n' +
					                     'the media. Other play mode values are self explanatory.</dc:description>\n' +
				                     '<mapping platform="upnp-av">\n' +
					                     '<uis-upnp:annotation>UPnP action GetTransportSettings for getting. State\n' +
						                     'variable LastChange for updates.</uis-upnp:annotation>\n' +
				                     '</mapping>\n' +
				                     '<dependency>\n' +
					                     '<relevant>true()</relevant>\n' +
					                     '<write>value(\'./isCurrentPlayModeSettable\')</write>\n' +
				                     '</dependency>\n' +
			                     '</variable>\n' +
			                     '<variable id="transportPlaySpeed" type="uis-upnp:transportPlaySpeedType">\n' +
				                     '<dc:description xml:lang="en">String representation of a rational fraction,\n' +
					                     'indicates the speed relative to normal speed. Example values are \'1\', \'1/2\',\n' +
					                     '\'2\', \'-1\', \'1/10\', etc. Actually supported speeds can be retrieved from the\n' +
					                     'AllowedValueList of this state variable in the AVTransport service description.\n' +
					                     'Value \'1\' is required, value \'0\' is not allowed.</dc:description>\n' +
				                     '<mapping platform="upnp-av">\n' +
					                     '<uis-upnp:annotation>UPnP action GetTransportInfo for getting. UPnP state\n' +
						                     'variable LastChange for updates.</uis-upnp:annotation>\n' +
				                     '</mapping>\n' +
				                     '<dependency>\n' +
					                     '<relevant>true()</relevant>\n' +
					                     '<write>true()</write>\n' +
				                     '</dependency>\n' +
				                     '<selection>\n' +
					                     '<selectionSetDynamic id="selectionTransportPlaySpeed"\n' +
						                     'varRef="availableTransportPlaySpeeds"/>\n' +
				                     '</selection>\n' +
			                     '</variable>\n' +
			                     '<variable id="recordMediumWriteStatus" type="upnp:status.type" optional="true">\n' +
				                     '<dc:description xml:lang="en">Write protection status of currently loaded media.\n' +
					                     'NOT_WRITABLE indicates an inherent \'read-only\' media (e.g., a DVD-ROM disc) or\n' +
					                     'the device doesn\'t support recording on the current media. PROTECTED indicates a\n' +
					                     'writable media that is currently write-protected (e.g., a protected VHS tape).\n' +
					                     'If no media is loaded, the write status will be "UNKNOWN". </dc:description>\n' +
				                     '<mapping platform="upnp-av">\n' +
					                     '<uis-upnp:annotation>UPnP action GetMediaInfo for getting. UPnP state variable\n' +
						                     'LastChange for updates. Not present if value is "NOT_IMPLEMENTED" (i.e.\n' +
						                     'service doesn\'t support recording).</uis-upnp:annotation>\n' +
				                     '</mapping>\n' +
				                     '<dependency>\n' +
					                     '<relevant>true()</relevant>\n' +
					                     '<write>false()</write>\n' +
				                     '</dependency>\n' +
			                     '</variable>\n' +
			                     '<variable id="currentRecordQualityMode" type="uis-upnp:recordQualityModeType"\n' +
				                     'optional="true">\n' +
				                     '<dc:description xml:lang="en">Indicates the currently set record quality mode. Such\n' +
					                     'a setting takes the form of "Quality Ordinal:label". The Quality Ordinal\n' +
					                     'indicates a particular relative quality level available in the device, from 0\n' +
					                     '(lowest quality) to n (highest quality). The label associated with the ordinal\n' +
					                     'provides a human-readable indication of the ordinal\'s meaning. </dc:description>\n' +
				                     '<mapping platform="upnp-av">\n' +
					                     '<uis-upnp:annotation>UPnP action GetTransportSettings for getting value. State\n' +
						                     'variable LastChange for updates. Action SetRecordQualityMode for setting.\n' +
						                     'Not present if value is "NOT_IMPLEMENTED" (i.e. service doesn\'t support\n' +
						                     'recording).</uis-upnp:annotation>\n' +
				                     '</mapping>\n' +
				                     '<dependency>\n' +
					                     '<relevant>true()</relevant>\n' +
					                     '<write>value(\'./recordQualityModeSettable\')</write>\n' +
				                     '</dependency>\n' +
				                     '<selection>\n' +
					                     '<selectionSetDynamic id="selectionRecordQualityMode"\n' +
						                     'varRef="possibleRecordQualityModes"/>\n' +
				                     '</selection>\n' +
			                     '</variable>\n' +
			                     '<variable id="numberOfTracks" type="xsd:unsignedLong">\n' +
				                     '<dc:description xml:lang="en">Number of tracks controlled by the AVTransport\n' +
					                     'instance. If no resource is associated with the AVTransport instance (via\n' +
					                     'SetAVTransportURI), and there is no \'default\' resource (for example, a loaded\n' +
					                     'disc) then NumberOfTracks shall be 0. Otherwise, it shall be 1 or higher.</dc:description>\n' +
				                     '<mapping platform="upnp-av">\n' +
					                     '<uis-upnp:annotation>UPnP action GetMediaInfo for getting. UPnP state variable\n' +
						                     'LastChange for updates.</uis-upnp:annotation>\n' +
				                     '</mapping>\n' +
				                     '<dependency>\n' +
					                     '<relevant>true()</relevant>\n' +
					                     '<write>false()</write>\n' +
				                     '</dependency>\n' +
			                     '</variable>\n' +
			                     '<variable id="currentTrack" type="xsd:unsignedLong">\n' +
				                     '<dc:description xml:lang="en">If NumberOfTracks is 0, then CurrentTrack will be 0.\n' +
					                     'Otherwise, this state variable will contain the sequence number of the currently\n' +
					                     'selected track, starting at value \'1\', up to and including NumberOfTracks. For\n' +
					                     'tape-based media that do not support the notion of tracks, this state variable\n' +
					                     'will always be \'1\'. For LD and DVD media, the notion of track equals the notion\n' +
					                     'of chapter number. For Tuners that provide an indexed list of channels, the\n' +
					                     'current track is defined as the current index number in such a list.</dc:description>\n' +
				                     '<mapping platform="upnp-av">\n' +
					                     '<uis-upnp:annotation>UPnP action GetPositionInfo for getting. UPnP state\n' +
						                     'variable LastChange for updates.</uis-upnp:annotation>\n' +
				                     '</mapping>\n' +
				                     '<dependency>\n' +
					                     '<relevant>true()</relevant>\n' +
					                     '<write>false()</write>\n' +
				                     '</dependency>\n' +
			                     '</variable>\n' +
			                     '<variable id="currentTrackDuration" type="uis-upnp:timeType" optional="true">\n' +
				                     '<dc:description xml:lang="en">Duration of the current track, specified as a string\n' +
					                     'of the following form: H+:MM:SS[.F+] or H+:MM:SS[.F0/F1]</dc:description>\n' +
				                     '<mapping platform="upnp-av">\n' +
					                     '<uis-upnp:annotation>UPnP action GetPositionInfo for getting. UPnP state\n' +
						                     'variable LastChange for updates. Not present if value is "NOT_IMPLEMENTED"\n' +
						                     '(i.e. service doesn\'t support media duration\n' +
					                     'information).</uis-upnp:annotation>\n' +
				                     '</mapping>\n' +
				                     '<dependency>\n' +
					                     '<relevant>true()</relevant>\n' +
					                     '<write>false()</write>\n' +
				                     '</dependency>\n' +
			                     '</variable>\n' +
			                     '<variable id="currentMediaDuration" type="uis-upnp:timeType" optional="true">\n' +
				                     '<dc:description xml:lang="en">Duration of the current track, specified as a string\n' +
					                     'of the following form: H+:MM:SS[.F+] or H+:MM:SS[.F0/F1]</dc:description>\n' +
				                     '<mapping platform="upnp-av">\n' +
					                     '<uis-upnp:annotation>UPnP action GetMediaInfo for getting. UPnP state variable\n' +
						                     'LastChange for updates. Not present if value is "NOT_IMPLEMENTED" (i.e.\n' +
						                     'service doesn\'t support media duration information).</uis-upnp:annotation>\n' +
				                     '</mapping>\n' +
				                     '<dependency>\n' +
					                     '<write>false()</write>\n' +
				                     '</dependency>\n' +
			                     '</variable>\n' +
			                     '<variable id="nodeNowPlaying" type="xsd:string">\n' +
				                     '<dc:description xml:lang="en">Id of the node from content that is currently in focus\n' +
					                     'for playing.</dc:description>\n' +
				                     '<mapping platform="upnp-av">\n' +
					                     '<uis-upnp:annotation>Serves as parameter for a connect and play\n' +
					                     'operation.</uis-upnp:annotation>\n' +
				                     '</mapping>\n' +
				                     '<dependency>\n' +
					                     '<write>false()</write>\n' +
				                     '</dependency>\n' +
			                     '</variable>\n' +
			                     '<variable id="currentTrackMetaData" type="xsd:string" dim="metadataIndexType"\n' +
				                     'optional="true">\n' +
				                     '<dc:description xml:lang="en">Metadata, in the form of a 1-dimensional array\n' +
					                     'containing the metadata fields pertaining to the resource pointed to by state\n' +
					                     'variable CurrentTrackURI. The meta data may have been extracted from state\n' +
					                     'variable AVTransportURIMetaData, or extracted from the resource binary itself\n' +
					                     '(e.g., embedded ID3 tags for MP3 audio). This is implementation dependent. Note:\n' +
					                     'This variable reflects the content of the item currently being played, whereas\n' +
					                     'the variable "metadata" in set "content" contains the metadata of the currently\n' +
					                     'selected node. Also, currentTrackMetaData cannot be derived from the selected\n' +
					                     'node and metadata, since the current selection may be the containing folder of\n' +
					                     'the item currently being played.</dc:description>\n' +
				                     '<mapping platform="upnp-av">\n' +
					                     '<uis-upnp:annotation>UPnP action GetPositionInfo for getting. UPnP state\n' +
						                     'variable LastChange for updates. Not present if value is\n' +
					                     '"NOT_IMPLEMENTED".</uis-upnp:annotation>\n' +
				                     '</mapping>\n' +
				                     '<dependency>\n' +
					                     '<relevant>true()</relevant>\n' +
					                     '<write>false()</write>\n' +
				                     '</dependency>\n' +
			                     '</variable>\n' +
			                     '<variable id="currentTrackUri" type="xsd:anyURI">\n' +
				                     '<dc:description xml:lang="en">Reference, in the form of a URI, to the current track.\n' +
					                     'The URI enables a control point to retrieve any meta data associated with\n' +
					                     'current track, such as, for example, title and author information, via the\n' +
					                     'ContentDirectory service.</dc:description>\n' +
				                     '<mapping platform="upnp-av">\n' +
					                     '<uis-upnp:annotation>UPnP action GetPositionInfo for getting. UPnP state\n' +
						                     'variable LastChange for udates.</uis-upnp:annotation>\n' +
				                     '</mapping>\n' +
				                     '<dependency>\n' +
					                     '<relevant>false()</relevant>\n' +
					                     '<write>false()</write>\n' +
				                     '</dependency>\n' +
			                     '</variable>\n' +
			                     '<variable id="avTransportUri" type="xsd:anyURI">\n' +
				                     '<dc:description xml:lang="en">Reference, in the form of a URI of the resource\n' +
					                     'controlled by the AVTransport instance. This URI may refer to a \'single\' item\n' +
					                     '(e.g., a song) or to a collection of items (e.g., a playlist). In the single\n' +
					                     'item case, the AVTransport will have 1 \'track\' and AVTransportURI is equal to\n' +
					                     'CurrentTrackURI. In the \'collection of items\' case, the AVTransport will have\n' +
					                     'multiple tracks, and AVTransportURI will remain constant during track changes.\n' +
					                     'The URI enables a control point to retrieve any meta data associated with the\n' +
					                     'AVTransport instance, such as, for example, title and author information, via\n' +
					                     'the ContentDirectory service.</dc:description>\n' +
				                     '<mapping platform="upnp-av">\n' +
					                     '<uis-upnp:annotation>UPnP action GetMediaInfo for getting. UPnP state variable\n' +
						                     'LastChange for updates. This is read-only since the command play will set\n' +
						                     'avTransportUri to the desired URI.</uis-upnp:annotation>\n' +
				                     '</mapping>\n' +
				                     '<dependency>\n' +
					                     '<relevant>false()</relevant>\n' +
					                     '<write>false()</write>\n' +
				                     '</dependency>\n' +
			                     '</variable>\n' +
			                     '<variable id="avTransportUriMetaData" type="xsd:string" dim="metadataIndexType"\n' +
				                     'optional="true">\n' +
				                     '<dc:description xml:lang="en">Metadata, in the form of a 1-dimensional array\n' +
					                     'containing the metadata fields pertaining to the resource pointed to by state\n' +
					                     'variable AVTransportURI. See the ContentDirectory service specification for\n' +
					                     'details. If the service implementation doesn\'t support this feature then this\n' +
					                     'state variable must be set to value "NOT_IMPLEMENTED".</dc:description>\n' +
				                     '<mapping platform="upnp-av">\n' +
					                     '<uis-upnp:annotation>UPnP action GetMediaInfo for getting. UPnP state variable\n' +
						                     'LastChange for updates. Not present if value is\n' +
					                     '"NOT_IMPLEMENTED".</uis-upnp:annotation>\n' +
				                     '</mapping>\n' +
				                     '<dependency>\n' +
					                     '<relevant>true()</relevant>\n' +
					                     '<write>false()</write>\n' +
				                     '</dependency>\n' +
			                     '</variable>\n' +
			                     '<variable id="nextAvTransportUri" type="xsd:anyURI" optional="true">\n' +
				                     '<dc:description xml:lang="en">AVTransportURI value to be played when the playback of\n' +
					                     'the current AVTransportURI finishes. Setting this variable ahead of time (via\n' +
					                     'action SetNextAVTransportURI) enables a device to provide seamless transitions\n' +
					                     'between resources for certain streaming protocols that need buffering (e.g. HTTP\n' +
					                     'GET). </dc:description>\n' +
				                     '<mapping platform="upnp-av">\n' +
					                     '<uis-upnp:annotation>UPnP action GetMediaInfo for getting. UPnP state variable\n' +
						                     'LastChange for updates. For setting call UPnP action SetNextAVTransportURI.\n' +
						                     'Not present if value is "NOT_IMPLEMENTED" (i.e. the service doesn\'t support\n' +
						                     'this feature).</uis-upnp:annotation>\n' +
				                     '</mapping>\n' +
				                     '<dependency>\n' +
					                     '<relevant>false()</relevant>\n' +
					                     '<write>true()</write>\n' +
				                     '</dependency>\n' +
			                     '</variable>\n' +
			                     '<variable id="nextAvTransportUriMetaData" type="xsd:string" dim="metadataIndexType"\n' +
				                     'optional="true">\n' +
				                     '<dc:description xml:lang="en">Metadata, in the form of a 1-dimensional array\n' +
					                     'containing the metadata fields pertaining to the resource pointed to by state\n' +
					                     'variable NextAVTransportURI. See the ContentDirectory service specification for\n' +
					                     'details. If the service implementation doesn\'t support this feature then this\n' +
					                     'state variable must be set to value "NOT_IMPLEMENTED".</dc:description>\n' +
				                     '<mapping platform="upnp-av">\n' +
					                     '<uis-upnp:annotation>UPnP action GetMediaInfo for getting. UPnP state variable\n' +
						                     'LastChange for updates. Not present if value is\n' +
					                     '"NOT_IMPLEMENTED".</uis-upnp:annotation>\n' +
				                     '</mapping>\n' +
				                     '<dependency>\n' +
					                     '<relevant>true()</relevant>\n' +
					                     '<write>false()</write>\n' +
				                     '</dependency>\n' +
			                     '</variable>\n' +
			                     '<variable id="relativeTimePosition" type="uis-upnp:timeType" optional="true">\n' +
				                     '<dc:description xml:lang="en">Duration in the following form: H+:MM:SS[.F+] or\n' +
					                     'H+:MM:SS[.F0/F1]. This state variable contains the current position, in terms of\n' +
					                     'time, from the beginning of the current track. For tape-based media that do not\n' +
					                     'support multiple tracks on a single tape, this state variable contains the\n' +
					                     'position, in terms of time, from a zero reference point on the tape. The time\n' +
					                     'format is the same as for state variable CurrentTrackDuration. </dc:description>\n' +
				                     '<mapping platform="upnp-av">\n' +
					                     '<uis-upnp:annotation>UPnP action GetPositionInfo for getting and polling (no\n' +
						                     'updates through LastChange). Not present if value is "NOT_IMPLEMENTED" (i.e.\n' +
						                     'the service doesn\'t support relative time based position\n' +
					                     'information).</uis-upnp:annotation>\n' +
				                     '</mapping>\n' +
				                     '<dependency>\n' +
					                     '<relevant>true()</relevant>\n' +
					                     '<write>false()</write>\n' +
				                     '</dependency>\n' +
			                     '</variable>\n' +
			                     '<variable id="absoluteTimePosition" type="uis-upnp:timeType" optional="true">\n' +
				                     '<dc:description xml:lang="en">Duration in the following form: H+:MM:SS[.F+] or\n' +
					                     'H+:MM:SS[.F0/F1] This state variable contains the current position, in terms of\n' +
					                     'a time, from the beginning of the media. The time format is the same as for\n' +
					                     'state variable CurrentTrackDuration. </dc:description>\n' +
				                     '<mapping platform="upnp-av">\n' +
					                     '<uis-upnp:annotation>UPnP action GetPositionInfo for getting and polling (no\n' +
						                     'updates through LastChange). Not present if value is "NOT_IMPLEMENTED" or\n' +
						                     '"END_OF_MEDIA" (i.e. the service doesn\'t support time position information).\n' +
						                     'Note: The UPnP spec allows for devices that don\'t support time position\n' +
						                     'information to use the value "END_OF_MEDIA" to indicate the end of the\n' +
						                     'current media. This information is lost in the Socket in favor of a more\n' +
						                     'rigid approach.</uis-upnp:annotation>\n' +
				                     '</mapping>\n' +
				                     '<dependency>\n' +
					                     '<relevant>true()</relevant>\n' +
					                     '<write>false()</write>\n' +
				                     '</dependency>\n' +
			                     '</variable>\n' +
			                     '<variable id="relativeCounterPosition" type="xsd:long" optional="true">\n' +
				                     '<dc:description xml:lang="en">This state variable contains the current position, in\n' +
					                     'terms of a dimensionless counter, from the beginning of the current track. For\n' +
					                     'tape-based media that do not support multiple tracks on a single tape, this\n' +
					                     'state variable contains the position, in terms of a dimensionless counter, from\n' +
					                     'a zero reference point on the tape. </dc:description>\n' +
				                     '<mapping platform="upnp-av">\n' +
					                     '<uis-upnp:annotation>UPnP action GetPositionInfo for getting and poling (no\n' +
						                     'updates through LastChange). Not present if value is maximum value of the i4\n' +
						                     'data type (i.e. the service doesn\'t support relative count-based position\n' +
						                     'information).</uis-upnp:annotation>\n' +
				                     '</mapping>\n' +
				                     '<dependency>\n' +
					                     '<relevant>true()</relevant>\n' +
					                     '<write>false()</write>\n' +
				                     '</dependency>\n' +
			                     '</variable>\n' +
			                     '<variable id="absoluteCounterPosition" type="xsd:long" optional="true">\n' +
				                     '<dc:description xml:lang="en">This state variable contains the current position, in\n' +
					                     'terms of a dimensionless counter, from the beginning of the loaded media. </dc:description>\n' +
				                     '<mapping platform="upnp-av">\n' +
					                     '<uis-upnp:annotation>UPnP action GetPositionInfo for getting and polling (no\n' +
						                     'updates through LastChange). Not present if value is maximum value of the i4\n' +
						                     'data type (i.e. the service doesn\'t support absolute count-based position\n' +
						                     'information).</uis-upnp:annotation>\n' +
				                     '</mapping>\n' +
				                     '<dependency>\n' +
					                     '<relevant>true()</relevant>\n' +
					                     '<write>false()</write>\n' +
				                     '</dependency>\n' +
			                     '</variable>\n' +
			                     '<variable id="currentTransportActions" type="uis:csvlist" optional="true">\n' +
				                     '<dc:description xml:lang="en">This state variable contains a comma-separated list of\n' +
					                     'transport-controlling actions that can be successfully invoked for the current\n' +
					                     'resource at this specific point in time. The list will contain a subset of the\n' +
					                     'following actions: Play, Stop, Pause, Seek, Next, Previous and Record. For\n' +
					                     'example, when a live stream from the Internet is being controlled, the variable\n' +
					                     'may be only "Play, Stop". When a local audio CD is being controlled, the\n' +
					                     'variable may be "Play, Stop, Pause, Seek, Next, Previous". This information can\n' +
					                     'be used, for example, to dynamically enable or disable play, stop, pause\n' +
					                     'buttons, etc., on a user interface.</dc:description>\n' +
				                     '<mapping platform="upnp-av">\n' +
					                     '<uis-upnp:annotation>UPnP action GetCurrentTransportActions for getting. State\n' +
						                     'variable LastChange for updates. Note: We could define a type for valid\n' +
						                     'transportActions but that would not help here since XML Schema doesn\'t allow\n' +
						                     'us to define the item type of a CSV list. Note: We could use this variable\n' +
						                     'for the execute dependencies of play, stop, etc., but might have the\n' +
						                     'following problems: (1) This variable is optional, so may not be present.\n' +
						                     'One could check this at runtime, but if not present, all commands would\n' +
						                     'always be active. (2) Since this variable isn\'t directly evented, variable\n' +
						                     'changes will arrive with delay through LastChange or with delay through\n' +
						                     'polling.</uis-upnp:annotation>\n' +
				                     '</mapping>\n' +
				                     '<dependency>\n' +
					                     '<relevant>false()</relevant>\n' +
					                     '<write>false()</write>\n' +
				                     '</dependency>\n' +
			                     '</variable>\n' +
			                     '<command id="stop" type="uis:basicCommand">\n' +
				                     '<dc:description xml:lang="en">This action stops the progression of the current\n' +
					                     'resource that is associated with the specified instance. Additionally, it is\n' +
					                     'recommended that the "output of the device" (defined below) should change to\n' +
					                     'something other than the current snippet of resource. Although the exact nature\n' +
					                     'of this change varies from device to device, a common behavior is to immediately\n' +
					                     'cease all "output" from the device. Nevertheless, the exact behavior is defined\n' +
					                     'by the manufacturer of the device. This action is allowed in all transport\n' +
					                     'states except in state "NO_MEDIA_PRESENT".</dc:description>\n' +
				                     '<mapping platform="upnp-av">\n' +
					                     '<uis-upnp:annotation>Action Stop.</uis-upnp:annotation>\n' +
				                     '</mapping>\n' +
				                     '<dependency>\n' +
					                     '<relevant>true()</relevant>\n' +
					                     '<write>value(\'./transportState\')!=\'NO_MEDIA_PRESENT\' </write>\n' +
					                     '<assert>value(\'./transportState\') eq \'STOPPED\'</assert>\n' +
				                     '</dependency>\n' +
				                     '<param dir="out" idref="transportState"/>\n' +
			                     '</command>\n' +
			                     '<command id="play" type="uis:basicCommand">\n' +
				                     '<dc:description xml:lang="en">Start playing the resource of the specified instance,\n' +
					                     'at the specified speed, starting at the current position, according to the\n' +
					                     'current play mode. Keep playing until the resource ends or the transport state\n' +
					                     'is changed via actions Stop or Pause. The device should do a \'best effort\' to\n' +
					                     'match the specified play speed. Actually supported speeds can be retrieved from\n' +
					                     'the AllowedValueList of the TransportPlaySpeed state variable in the AVTransport\n' +
					                     'service description. This action is allowed in the "STOPPED", "PLAYING", and\n' +
					                     '"PAUSED_PLAYBACK" transport states.</dc:description>\n' +
				                     '<mapping platform="upnp-av">\n' +
					                     '<uis-upnp:annotation>If no connection established yet, call UPnP action\n' +
						                     'PrepareForConnection. Note that ConnectionComplete should be called when the\n' +
						                     'session ends. UPnP action setAVTransportUri with the URI pertaining to the\n' +
						                     'selectedNodeId of xmlContent. Then invoke action Play; the command\'s state\n' +
						                     'should be \'inProgress\' as long as transportState has value\n' +
					                     '\'TRANSITIONING\'.</uis-upnp:annotation>\n' +
				                     '</mapping>\n' +
				                     '<dependency>\n' +
					                     '<write>value(\'./transportState\') eq \'STOPPED\' or value(\'./transportState\') eq\n' +
						                     '\'PAUSED_PLAYBACK\' or value(\'./transportState\') eq \'PLAYING\'</write>\n' +
					                     '<assert>value(\'./transportState\') eq \'PLAYING\'</assert>\n' +
				                     '</dependency>\n' +
				                     '<param id="playListMode" type="playSourceType" dir="in">\n' +
					                  '   <dc:description xml:lang="en">Either one of the following:\n' +
						               '      "SingleBrowse" = Play selectedBrowseId item from browse list.\n' +
						               '      "SingleSearch" = Play selectedSearchId item from search list.\n' +
						               '      "ContinuousBrowse" = Play items from browse list, starting with selectedBrowseId.\n' +
						               '      "ContinuousSearch" = Play items from search list, starting with selectedSearchId.\n' +
					                  '   </dc:description>\n' +
				                     '</param>\n' +
				                     '<param id="playCurrentPlayMode" type="uis-upnp:playModeType" dir="in">\n' +
					                     '<dc:description xml:lang="en">Indicates the desired play mode (e.g., random\n' +
						                     'play, repeated play, etc.). Ignored if play mode cannot be set for the media\n' +
						                     'renderer.</dc:description>\n' +
					                     '<mapping platform="upnp-av">\n' +
						                     '<uis-upnp:annotation>This parameter should be ignored if UPnP action\n' +
							                     'SetPlayMode is not present (optional).</uis-upnp:annotation>\n' +
					                     '</mapping>\n' +
				                     '</param>\n' +
				                     '<param id="playTransportPlaySpeed" type="uis-upnp:transportPlaySpeedListType"\n' +
					                     'dir="in"/>\n' +
				                     '<param id="nodeToPlay" type="xsd:string" dir="in">\n' +
					                     '<dc:description xml:lang="en">Indicates the desired node to play. An empty\n' +
						                     'string indicates that the system should automatically determine the item to\n' +
						                     'play (depending on the internal navigation model).</dc:description>\n' +
				                     '</param>\n' +
				                     '<param idref="avTransportUri" dir="out"/>\n' +
				                     '<param idref="currentTrackUri" dir="out"/>\n' +
				                     '<param idref="currentPlayMode" dir="out"/>\n' +
				                     '<param idref="transportPlaySpeed" dir="out"/>\n' +
				                     '<param idref="transportState" dir="out"/>\n' +
			                     '</command>\n' +
			                     '<command id="pause" type="uis:basicCommand" optional="true">\n' +
				                     '<dc:description xml:lang="en">While the device is in a playing state, e.g.\n' +
					                     'TransportState is "PLAYING", this action halts the progression of the resource\n' +
					                     'that is associated with the specified instance Id. Any visual representation of\n' +
					                     'the resource should remain displayed in a static manner (e.g. the last frame of\n' +
					                     'video should remain displayed). Any audio representation of the resource should\n' +
					                     'be muted. The difference between Pause() and Stop() is that Pause() MUST remain\n' +
					                     'at the current position within the resource and the current resource must\n' +
					                     'persist as describe above (e.g. the current video resource continues to be\n' +
					                     'transmitted/displayed). This action is always allowed while playing or\n' +
					                     'recording.</dc:description>\n' +
				                     '<mapping platform="upnp-av">\n' +
					                     '<uis-upnp:annotation>UPnP action Pause.</uis-upnp:annotation>\n' +
				                     '</mapping>\n' +
				                     '<dependency>\n' +
					                     '<relevant>true()</relevant>\n' +
					                     '<write>value(\'./transportState\') eq \'PLAYING\' or value(\'./transportState\') eq\n' +
						                     '\'RECORDING\'</write>\n' +
					                     '<assert>value(\'./transportState\') eq \'PAUSED_RECORDING\' or\n' +
						                     'value(\'./transportState\') eq \'PAUSED_PLAYBACK\'</assert>\n' +
				                     '</dependency>\n' +
				                     '<param dir="out" idref="transportState"/>\n' +
			                     '</command>\n' +
			                     '<command id="record" type="uis:basicCommand" optional="true">\n' +
				                     '<dc:description xml:lang="en">Start recording on the specified transport instance,\n' +
					                     'at the current position on the media, according to the currently specified\n' +
					                     'recording quality, and return immediately. If AVTransportURI is set (differs\n' +
					                     'from the empty string) then that resource will be recorded. If no AVTransportURI\n' +
					                     'is set (equals the empty string), then the source of the content being recording\n' +
					                     'is device dependent. In both cases, whether the device outputs the resource to a\n' +
					                     'screen or speakers while recording is device dependent. If the device\n' +
					                     'implementing the Record action also has a ContentDirectory service, then\n' +
					                     'recorded content will be added to this ContentDirectory in a device-dependent\n' +
					                     'way. Specifically, there is no UPnP mechanism to specify the location of the\n' +
					                     'recorded content in the ContentDirectory hierarchy. This action is allowed in\n' +
					                     'the "STOPPED" or "PAUSED_RECORDING" transport states.</dc:description>\n' +
				                     '<mapping platform="upnp-av">\n' +
					                     '<uis-upnp:annotation>UPnP action Record.</uis-upnp:annotation>\n' +
				                     '</mapping>\n' +
				                     '<dependency>\n' +
					                     '<relevant>true()</relevant>\n' +
					                     '<write>value(\'./transportState\') eq \'STOPPED\' or value(\'./transportState\') eq\n' +
						                     '\'PAUSED_RECORDING\'</write>\n' +
					                     '<assert>value(\'./transportState\') eq \'RECORDING\'</assert>\n' +
				                     '</dependency>\n' +
				                     '<param idref="currentRecordQualityMode" dir="in"/>\n' +
				                     '<param idref="avTransportUri" dir="out"/>\n' +
				                     '<param idref="currentTrackUri" dir="out"/>\n' +
				                     '<param idref="transportState" dir="out"/>\n' +
			                     '</command>\n' +
			                     '<command id="seek" type="uis:timedCommand">\n' +
				                     '<!--If known, the device can inform about how long it will take.-->\n' +
				                     '<dc:description xml:lang="en">Start seeking through the resource controlled by the\n' +
					                     'specified instance - as fast as possible - to the specified target position.\n' +
					                     'Unit value "TRACK_NR" indicates seeking to a particular track number. For\n' +
					                     'tape-based media that do not support the notion of track (such as VCRs),\n' +
					                     'Seek("TRACK_NR","1") is equivalent to the common "FastReverse" VCR\n' +
					                     'functionality. Special track number \'0\' is used to indicate the end of the\n' +
					                     'media, hence, Seek("TRACK_NR","0") is equivalent to the common "FastForward" VCR\n' +
					                     'functionality. This action is allowed in the STOPPED and PLAYING transport\n' +
					                     'states.</dc:description>\n' +
				                     '<mapping platform="upnp-av">\n' +
					                     '<uis-upnp:annotation>UPnP action Seek. If invoked, its state should be\n' +
						                     '\'inProgress\' as long as transportState has value\n' +
					                     '\'TRANSITIONING\'.</uis-upnp:annotation>\n' +
				                     '</mapping>\n' +
				                     '<dependency>\n' +
					                     '<relevant>true()</relevant>\n' +
					                     '<write>value(\'./transportState\') eq \'STOPPED\' or value(\'./transportState\') eq\n' +
						                     '\'PLAYING\'</write>\n' +
				                     '</dependency>\n' +
				                     '<param id="unit" type="uis-upnp:seekModeType" dir="in">\n' +
					                     '<dc:description xml:lang="en">This parameter indicates the allowed units in\n' +
						                     'which the amount of seeking to be performed is specified. It can be\n' +
						                     'specified as a time (relative or absolute), a count (relative or absolute),\n' +
						                     'a track number, a tape-index (e.g., for tapes with a indexing facility) or\n' +
						                     'even a video frame. A device vendor is allowed to implement a subset of the\n' +
						                     'allowed value list of this state variable. Only value \'TRACK_NR\' is\n' +
						                     'required.</dc:description>\n' +
				                     '</param>\n' +
				                     '<param id="target" type="xsd:string" dir="in">\n' +
					                     '<dc:description xml:lang="en">This parameter indicates the target position of\n' +
						                     'the seek action, in terms of units defined by parameter unit. The data type\n' +
						                     'of this variable is \'string\'. However, depending on the actual seek mode\n' +
						                     'used, it must contains string representations of values of UPnP types \'ui4\'\n' +
						                     '(ABS_COUNT, REL_COUNT, TRACK_NR, TAPE-INDEX, FRAME), \'time\' (ABS_TIME,\n' +
						                     'REL_TIME) or \'float\' (CHANNEL_FREQ, in Hz). Supported ranges of these\n' +
						                     'integer, time or float values are device-dependent.</dc:description>\n' +
				                     '</param>\n' +
				                     '<param idref="currentTrackUri" dir="out"/>\n' +
				                     '<!--Open issue: Describe error return codes as notifications or output parameter?-->\n' +
			                     '</command>\n' +
			                     '<command id="next" type="uis:basicCommand">\n' +
				                     '<dc:description xml:lang="en">Convenient action to advance to the next track. This\n' +
					                     'action is functionally equivalent to Seek(TRACK_NR,CurrentTrackNr+1). This\n' +
					                     'action does not \'cycle\' back to the first track. This action is allowed in the\n' +
					                     'STOPPED and PLAYING transport states.</dc:description>\n' +
				                     '<mapping platform="upnp-av">\n' +
					                     '<uis-upnp:annotation>UPnP action Next. If invoked, its state should be\n' +
						                     '\'inProgress\' as long as transportState has value\n' +
					                     '\'TRANSITIONING\'.</uis-upnp:annotation>\n' +
				                     '</mapping>\n' +
				                     '<dependency>\n' +
					                     '<relevant>true()</relevant>\n' +
					                     '<write>value(\'./transportState\') eq \'STOPPED\' or value(\'./transportState\') eq\n' +
						                     '\'PLAYING\'</write>\n' +
				                     '</dependency>\n' +
				                     '<param idref="currentTrackUri" dir="inout"/>\n' +
			                     '</command>\n' +
			                     '<command id="previous" type="uis:basicCommand">\n' +
				                     '<dc:description xml:lang="en">Convenient action to advance to the previous track.\n' +
					                     'This action is functionally equivalent to Seek(TRACK_NR,CurrentTrackNr-1) . This\n' +
					                     'action does not \'cycle\' back to the last track. This action is allowed in the\n' +
					                     'STOPPED and PLAYING transport states.</dc:description>\n' +
				                     '<mapping platform="upnp-av">\n' +
					                     '<uis-upnp:annotation>UPnP action Previous. If invoked, its state should be\n' +
						                     '\'inProgress\' as long as transportState has value\n' +
					                     '\'TRANSITIONING\'.</uis-upnp:annotation>\n' +
				                     '</mapping>\n' +
				                     '<dependency>\n' +
					                     '<relevant>true()</relevant>\n' +
					                     '<write>value(\'./transportState\') eq \'STOPPED\' or value(\'./transportState\') eq\n' +
						                     '\'PLAYING\'</write>\n' +
				                     '</dependency>\n' +
				                     '<param idref="currentTrackUri" dir="inout"/>\n' +
			                     '</command>\n' +
		                     '</set>\n' +
		                     '<set id="conNotifications">\n' +
			                     '<dc:description xml:lang="en">This set contains notifications that can occur for a\n' +
				                     'running connection.</dc:description>\n' +
			                     '<mapping platform="upnp-av">\n' +
				                     '<uis-upnp:annotation>***** Elements in this set are based on ConnectionManager\n' +
					                     'Service ***** This set defines notifications to be raised after a connection has\n' +
					                     'been created. Errors upon creating a connection should be indicated through the\n' +
					                     '"connectionError" parameter of the "connect" command.</uis-upnp:annotation>\n' +
			                     '</mapping>\n' +
			                     '<notify id="conNotifyContentFormatMismatch" category="error" optional="true">\n' +
				                     '<dc:description xml:lang="en">This notification is raised when the connection cannot\n' +
					                     'be continued because of a protocol/format mismatch.</dc:description>\n' +
				                     '<mapping platform="upnp-av">\n' +
					                     '<uis-upnp:annotation>Raised when\n' +
						                     'GetCurrentConnectionInfo(Status)=="ContentFormatMismatch".</uis-upnp:annotation>\n' +
				                     '</mapping>\n' +
				                     '<dependency>\n' +
					                     '<explicitAck>false()</explicitAck>\n' +
				                     '</dependency>\n' +
			                     '</notify>\n' +
			                     '<notify id="conNotifyInsufficientNetworkResources" category="error" optional="true">\n' +
				                     '<dc:description xml:lang="en">This notification is raised when connection cannot be\n' +
					                     'continued because of unsufficient network resources (bandwidth, channels, etc.).</dc:description>\n' +
				                     '<mapping platform="upnp-av">\n' +
					                     '<uis-upnp:annotation>Raised when\n' +
						                     'GetCurrentConnectionInfo(Status)=="InsufficientBandwidth".</uis-upnp:annotation>\n' +
				                     '</mapping>\n' +
				                     '<dependency>\n' +
					                     '<explicitAck>false()</explicitAck>\n' +
				                     '</dependency>\n' +
			                     '</notify>\n' +
			                     '<notify id="conNotifyUnreliableChannel" category="error" optional="true">\n' +
				                     '<dc:description xml:lang="en">This notification is raised when connection cannot be\n' +
					                     'continued because an unreliable channel has been chosen.</dc:description>\n' +
				                     '<mapping platform="upnp-av">\n' +
					                     '<uis-upnp:annotation>Raised when\n' +
						                     'GetCurrentConnectionInfo(Status)=="UnreliableChannel".</uis-upnp:annotation>\n' +
				                     '</mapping>\n' +
				                     '<dependency>\n' +
					                     '<explicitAck>false()</explicitAck>\n' +
				                     '</dependency>\n' +
			                     '</notify>\n' +
			                     '<notify id="conNotifyUnknownConnectionError" category="error" optional="true">\n' +
				                     '<dc:description xml:lang="en">This notification is raised when connection cannot be\n' +
					                     'continued because of an unknown error.</dc:description>\n' +
				                     '<mapping platform="upnp-av">\n' +
					                     '<uis-upnp:annotation>Raised when\n' +
					                     'GetCurrentConnectionInfo(Status)=="Unknown".</uis-upnp:annotation>\n' +
				                     '</mapping>\n' +
				                     '<dependency>\n' +
					                     '<explicitAck>false()</explicitAck>\n' +
				                     '</dependency>\n' +
			                     '</notify>\n' +
		                     '</set>\n' +
	                     '</set>\n' +
	                     '<schema xmlns="http://www.w3.org/2001/XMLSchema">\n' +
		                     '<simpleType name="playSourceType" id="idPlaySourceType">\n' +
			                  '   <restriction base="xsd:string">\n' +
				               '      <enumeration value="SingleBrowse"/>\n' +
				               '      <enumeration value="SingleSearch"/>\n' +
				               '      <enumeration value="MultipleBrowse"/>\n' +
				               '      <enumeration value="MultipleSearch"/>\n' +
				               '      <enumeration value="Continue"/>\n' +
			                  '   </restriction>\n' +
		                     '</simpleType>\n' +
		                     '<simpleType name="metadataIndexType" id="idMetadataIndexType">\n' +
			                     '<annotation>\n' +
				                     '<documentation>This type defines the possible indices for the metadata variable. The\n' +
					                     'indices are strings based on the DIDL-LITE schema file and the metadata\n' +
					                     'information it defines. Note that this type does not define the XML elements,\n' +
					                     'but rather enumerates their identifiers as strings (in the form of\n' +
					                     '"prefix:name"). In the future, other namespaces may be added for additional\n' +
					                     'metadata types, e.g. for an EPG.</documentation>\n' +
			                     '</annotation>\n' +
			                     '<restriction base="xsd:string">\n' +
				                     '<!--TODO: Add metadata fields from namespace "urn:schemas-upnp-org:metadata-1-0/upnp/".-->\n' +
				                     '<!--Open issue: Do we want to strip this list down to save bandwidth?-->\n' +
				                     '<enumeration value="none"><!--Can be used as sort criteria.--></enumeration>\n' +
				                     '<enumeration value="title"/>\n' +
				                     '<enumeration value="creator"/>\n' +
				                     '<enumeration value="subject"/>\n' +
				                     '<enumeration value="description"/>\n' +
				                     '<enumeration value="publisher"/>\n' +
				                     '<enumeration value="contributor"/>\n' +
				                     '<enumeration value="date"/>\n' +
				                     '<enumeration value="type"/>\n' +
				                     '<enumeration value="format"/>\n' +
				                     '<enumeration value="identifier"/>\n' +
				                     '<enumeration value="source"/>\n' +
				                     '<enumeration value="language"/>\n' +
				                     '<enumeration value="relation"/>\n' +
				                     '<enumeration value="coverage"/>\n' +
				                     '<enumeration value="rights"/>\n' +
				                     '<enumeration value="upnp:artist"/>\n' +
				                     '<enumeration value="upnp:actor"/>\n' +
				                     '<enumeration value="upnp:author"/>\n' +
				                     '<enumeration value="upnp:producer"/>\n' +
				                     '<enumeration value="upnp:director"/>\n' +
				                     '<enumeration value="upnp:genre"/>\n' +
				                     '<enumeration value="upnp:album"/>\n' +
				                     '<enumeration value="upnp:playlist"/>\n' +
				                     '<!--Note: Strictly, any of these metadata items could occur multiple times in the DIDL-LITE element structure.  But that should not occur in practice.-->\n' +
			                     '</restriction>\n' +
		                     '</simpleType>\n' +
	                     '</schema>\n' +
                     '</uiSocket>' );


               case "UCH/targets/tv/mce.uis":
                  return(
                     '<?xml version="1.0" encoding="UTF-8" ?>\n' +
                     '<?oxygen SCHSchema="http://myurc.org/ns/uisocketdesc/uisocketdesc.sch"?>\n' +
                     '<uiSocket about="http://res.i2home.org/tv/mce.uis" id="socket"\n' +
                     '	xmlns="http://myurc.org/ns/uisocketdesc" xmlns:uis="http://myurc.org/ns/uisocketdesc"\n' +
                     '	xmlns:photo="http://res.i2home.org/tv/mce-photo-schema"\n' +
                     '	xmlns:music="http://res.i2home.org/tv/mce-music-schema"\n' +
                     '	xmlns:video="http://res.i2home.org/tv/mce-video-schema"\n' +
                     '	xmlns:recordedTV="http://res.i2home.org/tv/mce-recordedTV-schema"\n' +
                     '	xmlns:mceBase="http://res.i2home.org/tv/mce-schema"\n' +
                     '	xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:dcterms="http://purl.org/dc/terms/"\n' +
                     '	xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"\n' +
                     '	xsi:schemaLocation="http://myurc.org/ns/uisocketdesc http://myurc.org/ns/uisocketdesc\n' +
                     '		http://purl.org/dc/elements/1.1/ http://dublincore.org/schemas/xmls/qdc/2006/01/06/dc.xsd\n' +
                     '		http://purl.org/dc/terms/ http://dublincore.org/schemas/xmls/qdc/2006/01/06/dcterms.xsd\n' +
                     '		http://res.i2home.org/tv/mce-photo-schema mce-photo.xsd\n' +
                     '		http://res.i2home.org/tv/mce-music-schema mce-music.xsd\n' +
                     '		http://res.i2home.org/tv/mce-video-schema mce-video.xsd\n' +
                     '		http://res.i2home.org/tv/mce-recordedTV-schema mce-recordedTV.xsd\n' +
                     '		http://res.i2home.org/tv/mce-schema mce-schema.xsd">\n' +
                     '	<dc:description xml:lang="en">UI socket for the multimedia playback and EPG functionalities provided by Windows Vista Media Center.</dc:description>\n' +
                     '	<dc:creator>Gorka Epelde, VICOMTech</dc:creator>\n' +
                     '	<dc:contributor>Eduardo Carrasco, VICOMTech</dc:contributor>\n' +
                     '	<dc:contributor>Gottfried Zimmermann, Access Technologies Group</dc:contributor>\n' +
                     '	<dc:publisher>VICOMTech, Spain</dc:publisher>\n' +
                     '	<dcterms:conformsTo>http://myurc.org/iso24752-2/2007</dcterms:conformsTo>\n' +
                     '	<dcterms:modified>2008-08-06</dcterms:modified>\n' +
                     '	<set id="volumeControls">\n' +
                     '		<dc:description xml:lang="en">This set contains volume related elements.  These are common across different groups of functionalities.</dc:description>\n' +
                     '		<variable id="volume" type="mceBase:volumeType">\n' +
                     '			<dc:description xml:lang="en">\n' +
                     '				Volume of the TV audio output may be set in range 0 to 50 (inclusive bounds), stepsize 1.  This variable is intended to be used to specify directly the value of volume.\n' +
                     '				When the volume is changed, the mute is deactivated (if it was activated).\n' +
                     '			</dc:description>\n' +
                     '		</variable>\n' +
                     '		<variable id="mute" type="xsd:boolean">\n' +
                     '			<dc:description xml:lang="en">\n' +
                     '				Activates and deactivates muted volume.\n' +
                     '\n' +
                     '				This is a variable and not a command, since the mute state has to be available for usage\n' +
                     '				within dependency expressions of other variables and commands.\n' +
                     '			</dc:description>\n' +
                     '		</variable>\n' +
                     '	</set>\n' +
                     '	<set id="liveTVControls">\n' +
                     '		<variable id="activeChannel" type="mceBase:channelType">\n' +
                     '			<dc:description xml:lang="en">\n' +
                     '				This variable may be used to set the active channel shown by the TV.  If tv is "stopped"\n' +
                     '				or "paused" and the user sets a new channel, it will be played automatically.\n' +
                     '\n' +
                     '				The string id for a channel shall be the same as in the EPG socket to\n' +
                     '				provide a direct manner of interoperability between the TV tuner and EPG socket.\n' +
                     '				It derives from uis:stringListItem and thus must not contain whitespace.\n' +
                     '				The string id may be used to fetch long labels and icons from a resource sheet.\n' +
                     '			</dc:description>\n' +
                     '			<selection closed="true">\n' +
                     '				<selectionSetDynamic id="currentChannels" varRef="channelList"/>\n' +
                     '			</selection>\n' +
                     '		</variable>\n' +
                     '		<variable id="channelList" type="mceBase:channelListType" final="true">\n' +
                     '			<dc:description xml:lang="en">\n' +
                     '\n' +
                     '				This variable holds a space-separated list of ids of channel that the user may select as active channel.\n' +
                     '\n' +
                     '				The UI may also show numbers to the users, using an index into this list.\n' +
                     '				The variable is marked as final, since the channel list may not be modified at runtime.\n' +
                     '				!! The channelList has to be polled to the MCE, so if the user tunes new channels or loses previous ones, the channel list is uploaded.\n' +
                     '				!! If we\'re going to update\n' +
                     '				Example: "ARD ZDF SAT-1 ARTE"\n' +
                     '\n' +
                     '			</dc:description>\n' +
                     '			<dependency>\n' +
                     '			 	<relevant>true()</relevant>\n' +
                     '				<write>false()</write>\n' +
                     '			</dependency>\n' +
                     '		</variable>\n' +
                     '		<variable id="tvStatus" type="mceBase:statusType">\n' +
                     '			<dc:description xml:lang="en">\n' +
                     '				Indicates the status of the TV ("stopped", "playing", "paused").\n' +
                     '			</dc:description>\n' +
                     '			<dependency>\n' +
                     '				<relevant>true()</relevant>\n' +
                     '				<write> false() </write>\n' +
                     '			</dependency>\n' +
                     '		</variable>\n' +
                     '		<command id="playTV" type="uis:voidCommand">\n' +
                     '			<dc:description xml:lang="en">\n' +
                     '				This command plays the selected channel ("activeChannel").\n' +
                     '			</dc:description>\n' +
                     '			<dependency>\n' +
                     '				<write> value(\'tvStatus\') neq \'playing\' </write>\n' +
                     '			</dependency>\n' +
                     '		</command>\n' +
                     '		<command id="stopTV" type="uis:voidCommand">\n' +
                     '			<dc:description xml:lang="en">\n' +
                     '				This command stops playing the selected channel.\n' +
                     '			</dc:description>\n' +
                     '			<dependency>\n' +
                     '				<write> value(\'tvStatus\') neq \'stopped\' </write>\n' +
                     '			</dependency>\n' +
                     '		</command>\n' +
                     '		<command id="pauseTV" type="uis:voidCommand">\n' +
                     '			<dc:description xml:lang="en">\n' +
                     '				This command is part of the timeshifting functionality of the TV. This would pause the TV broadcast to give the option of continuing from this point to the user.\n' +
                     '				If successful, the variable "tvPaused" will be set to true.\n' +
                     '				!!Maybe this should be a variable instead of a command, since, commands like ResumeTV - ResumeLiveTV make sense only if PauseTV is set.\n' +
                     '			</dc:description>\n' +
                     '			<dependency>\n' +
                     '				<write> value(\'tvStatus\') eq \'playing\' </write>\n' +
                     '			</dependency>\n' +
                     '		</command>\n' +
                     '		<command id="resumeTV" type="uis:voidCommand">\n' +
                     '			<dc:description xml:lang="en">This command is part of the timeshifting functionality of the TV. This resume command would continue the TV broadcast from the point it was paused.</dc:description>\n' +
                     '			<dependency>\n' +
                     '				<write> value(\'tvStatus\') eq \'paused\' </write>\n' +
                     '			</dependency>\n' +
                     '		</command>\n' +
                     '		<command id="resumeLiveTV" type="uis:voidCommand">\n' +
                     '			<dc:description xml:lang="en">This command is part of the timeshifting functionality of the TV. This resume command would continue the TV broadcast, going to liveTV broadcast instead of using the point it was paused.</dc:description>\n' +
                     '			<dependency>\n' +
                     '				<write> value(\'tvStatus\') eq \'paused\' </write>\n' +
                     '			</dependency>\n' +
                     '		</command>\n' +
                     '		<variable id="tvIsRecording" type="xsd:boolean">\n' +
                     '			<dc:description xml:lang="en">This variable shows us if we are manually recording a show on live TV mode..</dc:description>\n' +
                     '		</variable>\n' +
                     '		<command id="recordTV" type="uis:voidCommand">\n' +
                     '			<dc:description xml:lang="en">This command would allow the user to record the liveTV\n' +
                     '				content she/he\'s watching. Record is available while TV is paused.</dc:description>\n' +
                     '			<dependency>\n' +
                     '				<write> (value(\'tvStatus\') neq \'\'stopped\') and  (value(\'tvIsRecording\') eq \'false\')</write>\n' +
                     '			</dependency>\n' +
                     '		</command>\n' +
                     '		<command id="stopRecordingTV" type="uis:voidCommand">\n' +
                     '			<dc:description xml:lang="en">This command would allow the user to record the liveTV\n' +
                     '				content she/he\'s watching. Record is available while TV is paused.</dc:description>\n' +
                     '			<dependency>\n' +
                     '				<write> value (\'tvIsRecording\') eq \'true\'</write>\n' +
                     '			</dependency>\n' +
                     '		</command>\n' +
                     '\n' +
                     '	</set>\n' +
                     '\n' +
                     '	<set id="liveRadioControls">\n' +
                     '		<variable id="tunedFrequency" type="mceBase:radioFrequencyType">\n' +
                     '			<dc:description xml:lang="en">\n' +
                     '				This variable would allow to see the tuned radio station\'s frequency or to set a frequency to tune by hand. If the user changes this variable, tunedRadioStation will be set to "ManuallyTuned" automatically.\n' +
                     '			</dc:description>\n' +
                     '		</variable>\n' +
                     '		<variable id="tunedRadioStation" type="mceBase:radioStationType">\n' +
                     '			<dc:description xml:lang="en">\n' +
                     '				This variable would allow us to select a Radio Station from a list. The list should have an option "ManuallyTuned", just in case "tunedFrequency" variable is used to select radio station.\n' +
                     '			</dc:description>\n' +
                     '			<selection closed="true">\n' +
                     '				<selectionSetDynamic id="currentRadioStations" varRef="radioStationsList"/>\n' +
                     '			</selection>\n' +
                     '		</variable>\n' +
                     '		<variable id="radioStationsList" type="mceBase:radioStationsListType" final="true">\n' +
                     '			<dc:description xml:lang="en">\n' +
                     '				This variable holds a list of presets for radio.  Should include "ManuallyTuned" if manual tuning is allowed.\n' +
                     '			</dc:description>\n' +
                     '		</variable>\n' +
                     '		<variable id="radioStatus" type="mceBase:statusType">\n' +
                     '			<dc:description xml:lang="en">\n' +
                     '				Indicates the status of the TV ("stopped", "playing", "paused").\n' +
                     '			</dc:description>\n' +
                     '			<dependency>\n' +
                     '				<relevant>true()</relevant>\n' +
                     '				<write>false()</write>\n' +
                     '			</dependency>\n' +
                     '		</variable>\n' +
                     '		<command id="playRadio" type="uis:voidCommand">\n' +
                     '			<dc:description xml:lang="en">\n' +
                     '				This command plays the selected channel ("activeChannel").\n' +
                     '			</dc:description>\n' +
                     '			<dependency>\n' +
                     '				<write> value(\'radioStatus\') neq \'playing\' </write>\n' +
                     '			</dependency>\n' +
                     '		</command>\n' +
                     '		<command id="stopRadio" type="uis:voidCommand">\n' +
                     '			<dc:description xml:lang="en">\n' +
                     '				This command stops playing the selected channel.\n' +
                     '			</dc:description>\n' +
                     '			<dependency>\n' +
                     '				<write> value(\'radioStatus\') neq \'stopped\' </write>\n' +
                     '			</dependency>\n' +
                     '		</command>\n' +
                     '		<command id="pauseRadio" type="uis:voidCommand">\n' +
                     '			<dc:description xml:lang="en">\n' +
                     '				This command is part of the timeshifting functionality of the radio. This would pause the radio broadcast to give the option of continuing from this point to the user.\n' +
                     '				If successful, "radioPaused" is set to true.\n' +
                     '			</dc:description>\n' +
                     '			<dependency>\n' +
                     '				<write> value(\'radioStatus\') eq \'playing\' </write>\n' +
                     '			</dependency>\n' +
                     '		</command>\n' +
                     '		<command id="resumeRadio" type="uis:voidCommand">\n' +
                     '			<dc:description xml:lang="en">This command is part of the timeshifting functionality of the Radio. This resume command would continue the Radio broadcast from the point it was paused.</dc:description>\n' +
                     '			<dependency>\n' +
                     '				<write> value(\'radioStatus\') eq \'paused\' </write>\n' +
                     '			</dependency>\n' +
                     '		</command>\n' +
                     '		<command id="resumeLiveRadio" type="uis:voidCommand">\n' +
                     '			<dc:description xml:lang="en">This command is part of the timeshifting functionality of the radio. This resume command would continue the radio broadcast, going to liveRadio broadcast instead of using the point it was paused.</dc:description>\n' +
                     '			<dependency>\n' +
                     '				<write> value(\'radioStatus\') eq \'paused\' </write>\n' +
                     '			</dependency>\n' +
                     '		</command>\n' +
                     '		<!-- Radio cannot be recorded in MCE. -->\n' +
                     '	</set>\n' +
                     '\n' +
                     '	<set id="photos">\n' +
                     '		<variable id="photoLib" type="photo:photoLibType"/>\n' +
                     '		<!-- TODO: add comments to the variables & commands. -->\n' +
                     '		<variable id="photoSelection" type="photo:ID">\n' +
                     '			<dc:description xml:lang="en">Sets the folder or item for display.  Empty string if no photo is selected.</dc:description>\n' +
                     '		</variable>\n' +
                     '		<variable id="slideshowStatus" type="mceBase:statusType">\n' +
                     '			<dc:description xml:lang="en">Reflects current status of photo: "Stopped" and something selected = rest\n' +
                     '			on photo, "playing" = slide show playing, "paused" = pause in slide show. </dc:description>\n' +
                     '			<dependency>\n' +
                     '				<relevant>true()</relevant>\n' +
                     '				<write>false()</write>\n' +
                     '			</dependency>\n' +
                     '		</variable>\n' +
                     '\n' +
                     '		<variable id="currentPhoto" type="photo:ID">\n' +
                     '			<dc:description xml:lang="en">This variable reflects the currently displayed photo. Will be updated for every photo change in a side show.</dc:description>\n' +
                     '		</variable>\n' +
                     '		<command id="startSlideshow" type="uis:voidCommand">\n' +
                     '			<dc:description xml:lang="en">Starts slide show, beginning with "selectedContent". </dc:description>\n' +
                     '			<!-- GE: Can\'t we ask to start a new slideshow while there is a previous one running?-->\n' +
                     '			<dependency>\n' +
                     '				<write> value(\'slideshowStatus\') neq \'playing\' </write>\n' +
                     '			</dependency>\n' +
                     '		</command>\n' +
                     '		<command id="stopSlideshow" type="uis:voidCommand">\n' +
                     '			<dc:description xml:lang="en">Stops slide show. Shows "selectedContent". </dc:description>\n' +
                     '			<dependency>\n' +
                     '				<write> value(\'slideshowStatus\') neq \'stopped\' </write>\n' +
                     '			</dependency>\n' +
                     '		</command>\n' +
                     '		<command id="pauseSlideshow" type="uis:voidCommand">\n' +
                     '			<dc:description xml:lang="en">Rests on the currently displayed picture of the slide show.  The user can use "nextPhoto"\n' +
                     '			and "prevPhoto" to manually switch photos. </dc:description>\n' +
                     '			<dependency>\n' +
                     '				<write> value(\'slideshowStatus\') eq \'playing\' </write>\n' +
                     '			</dependency>\n' +
                     '		</command>\n' +
                     '		<command id="resumeSlideshow" type="uis:voidCommand">\n' +
                     '			<dc:description xml:lang="en"/>\n' +
                     '			<dependency>\n' +
                     '				<write> value(\'slideshowStatus\') eq \'paused\' </write>\n' +
                     '			</dependency>\n' +
                     '		</command>\n' +
                     '\n' +
                     '		<command id="showPhoto">\n' +
                     '			<dc:description xml:lang="en">Shows the "selectedContent" as a single photo.  If "selectedContent" is a folder, shows the\n' +
                     '				first photo in the folder or the first descendent folder. The user can move forward and backward in the folder by\n' +
                     '				"nextPhoto" and "prevPhoto". </dc:description>\n' +
                     '			<dependency>\n' +
                     '				<write> value(\'slideshowStatus\') neq \'playing\'</write>\n' +
                     '			</dependency>\n' +
                     '		</command>\n' +
                     '		<command id="nextPhoto" type="uis:voidCommand">\n' +
                     '			<dc:description xml:lang="en"/>\n' +
                     '			<dependency>\n' +
                     '				<write> value(\'slideshowStatus\') neq \'stopped\' </write>\n' +
                     '			</dependency>\n' +
                     '		</command>\n' +
                     '		<command id="prevPhoto" type="uis:voidCommand">\n' +
                     '			<dc:description xml:lang="en"/>\n' +
                     '			<dependency>\n' +
                     '				<write> value(\'slideshowStatus\') neq \'stopped\' </write>\n' +
                     '			</dependency>\n' +
                     '		</command>\n' +
                     '\n' +
                     '		<!-- Filtering of photos are not part of the scenarios.  We can implement this in the future.\n' +
                     '		<command id="filterPhotoLibraryData" type="uis:basicCommand">\n' +
                     '			<dc:description xml:lang="en">Command to load the Picture data in set "PictureLibrary" based on the filter fields.</dc:description>\n' +
                     '			<param dir="in" id="filterPictureTitle" type="xsd:string"/>\n' +
                     '			<param dir="in" id="filterPictureFolder" type="xsd:string"/>\n' +
                     '			<param dir="in" id="filterPictureStartDate" type="xsd:date">\n' +
                     '			<dc:description xml:lang="en">Filter for lower (inclusive) end of date range.  If changed by the user, the target should change "filterEndDate" accordingly to keep the duration constant.  Clients are encouraged to use date-type-specific widgets or commonly used words for days such as "today", "tomorrow", or even for ranges such as "next week", etc.</dc:description>\n' +
                     '			</param>\n' +
                     '			<param dir="in" id="filterPictureEndDate" type="xsd:date">\n' +
                     '			<dc:description xml:lang="en">Filter for higher (inclusive) end of date range.  Clients are encouraged to use date-type-specific widgets or commonly used words for days such as "today", "tomorrow", or even for ranges such as "next week", etc.</dc:description>\n' +
                     '			</param>\n' +
                     '			<param dir="in" id="filterPictureKeywords" type="uis:stringList"/>\n' +
                     '			<param dir="in" id="filterPictureRating" type="ratingType"/>\n' +
                     '			<param dir="out" idref="pictureLibrary"/>\n' +
                     '		<command>\n' +
                     '		-->\n' +
                     '	</set>\n' +
                     '\n' +
                     '	<set id="recordedTV">\n' +
                     '		<variable id="recordedTVLib" type="recordedTV:recordedTVLibType"></variable>\n' +
                     '\n' +
                     '		<variable id="recordedTvSelection" type="recordedTV:ID">\n' +
                     '			<dc:description xml:lang="en">Sets the item for play.  Empty string if no item is selected.</dc:description>\n' +
                     '		</variable>\n' +
                     '\n' +
                     '		<!-- Don\'t need filtering for recorded TVs now.  May implement in the future.\n' +
                     '		<command id="getRecordedTVLibraryData" type="uis:basicCommand">\n' +
                     '			<dc:description xml:lang="en">Command to load the Picture data in set "PictureLibrary" based on the filter fields.	</dc:description>\n' +
                     '			<param dir="in" id="filterRecordedTVTitle" type="xsd:string"/>\n' +
                     '			<param dir="in" id="filterRecordedTVStartDate" type="xsd:date">\n' +
                     '				<dc:description xml:lang="en">Filter for lower (inclusive) end of date range.  If changed by the user, the target should change "filterEndDate" accordingly to keep the duration constant.  Clients are encouraged to use date-type-specific widgets or commonly used words for days such as "today", "tomorrow", or even for ranges such as "next week", etc.</dc:description>\n' +
                     '			</param>\n' +
                     '			<param dir="in" id="filterRecordedTVEndDate" type="xsd:date">\n' +
                     '				<dc:description xml:lang="en">Filter for higher (inclusive) end of date range.  Clients are encouraged to use date-type-specific widgets or commonly used words for days such as "today", "tomorrow", or even for ranges such as "next week", etc.</dc:description>\n' +
                     '			</param>\n' +
                     '			<param dir="in" id="filterRecordedTVGenre" type="recordedTVGenreType"/>\n' +
                     '			<param dir="in" id="filterRecordedTVActors" type="uis:csvlist"/>\n' +
                     '			<param dir="in" id="filterRecordedTVRating" type="ratingType"/>\n' +
                     '			OpenIssue: I don\'t know if it is possible to access to RecordTV\'s series property, since it is treated as a video in the WMP Library. Research needed.\n' +
                     '				<param dir="in"" id="filterRecordedTVSeries" type="xsd:string"/>\n' +
                     '			<param dir="out" idref="recordedTVLibrary"/>\n' +
                     '		</command>\n' +
                     '		-->\n' +
                     '		<variable id="recordedTvStatus" type="mceBase:statusType">\n' +
                     '			<dc:description> Indicates status of recorded TV. </dc:description>\n' +
                     '			<dependency>\n' +
                     '				<relevant>true()</relevant>\n' +
                     '				<write>false()</write>\n' +
                     '			</dependency>\n' +
                     '		</variable>\n' +
                     '		<command id="playRecordedTV" type="uis:voidCommand">\n' +
                     '			<dc:description xml:lang="en"/>\n' +
                     '			<dependency>\n' +
                     '				<write> value(\'recordedTvStatus\') neq \'playing\' </write>\n' +
                     '			</dependency>\n' +
                     '		</command>\n' +
                     '		<command id="stopRecordedTV" type="uis:voidCommand">\n' +
                     '			<dc:description xml:lang="en"/>\n' +
                     '			<dependency>\n' +
                     '				<write> value(\'recordedTvStatus\') neq \'stopped\' </write>\n' +
                     '			</dependency>\n' +
                     '		</command>\n' +
                     '		<command id="pauseRecordedTVPlayback" type="uis:voidCommand">\n' +
                     '			<dc:description xml:lang="en"/>\n' +
                     '			<dependency>\n' +
                     '				<write> value(\'recordedTvStatus\') eq \'playing\' </write>\n' +
                     '			</dependency>\n' +
                     '		</command>\n' +
                     '		<command id="resumeRecordedTVPlayback" type="uis:voidCommand">\n' +
                     '			<dc:description xml:lang="en"/>\n' +
                     '			<dependency>\n' +
                     '				<write> value(\'recordedTvStatus\') eq \'paused\' </write>\n' +
                     '			</dependency>\n' +
                     '		</command>\n' +
                     '		<command id="restartRecordedTVPlayback" type="uis:voidCommand">\n' +
                     '			<dc:description xml:lang="en">We can restart RecordedTV playback, both when is playing and paused</dc:description>\n' +
                     '			<dependency>\n' +
                     '				<write> value(\'recordedTvStatus\') neq \'stopped\' </write>\n' +
                     '			</dependency>\n' +
                     '		</command>\n' +
                     '		<command id="fastForwardRecordedTVPlayback" type="uis:basicCommand">\n' +
                     '			<dc:description xml:lang="en">Fast forward only works if content is being playind</dc:description>\n' +
                     '			<dependency>\n' +
                     '				<write> value(\'recordedTvStatus\') eq \'\'playing\' </write>\n' +
                     '			</dependency>\n' +
                     '			<param dir="in" id="fastForwardRecTVSpeed" type="xsd:string"/>\n' +
                     '\n' +
                     '		</command>\n' +
                     '		<command id="rewindRecordedTVPlayback" type="uis:basicCommand">\n' +
                     '			<dc:description xml:lang="en">Rewind only works if content is being playind</dc:description>\n' +
                     '			<dependency>\n' +
                     '				<write> value(\'recordedTvStatus\') eq \'\'playing\' </write>\n' +
                     '			</dependency>\n' +
                     '			<param dir="in" id="rewindRecTVSpeed" type="xsd:string"/>\n' +
                     '\n' +
                     '		</command>\n' +
                     '	</set>\n' +
                     '\n' +
                     '	<set id="video">\n' +
                     '		<variable id="videoLib" type="video:videoLibType"></variable>\n' +
                     '		<variable id="videoSelection" type="video:ID">\n' +
                     '			<dc:description xml:lang="en">Video item to be played (no folder allowed). Empty string if nothing selected.</dc:description>\n' +
                     '		</variable>\n' +
                     '		<!-- The filter command is optional for our scenarios.  We may implement it in the future.\n' +
                     '		<command id="getVideoLibraryData" type="uis:basicCommand">\n' +
                     '			<dc:description xml:lang="en">Command to load the Picture data in set "PictureLibrary" based on the filter fields.\n' +
                     '			</dc:description>\n' +
                     '			<param id="filterVideoTitle" type="xsd:string" dir="in">\n' +
                     '				<dc:description xml:lang="en"/>\n' +
                     '			</param>\n' +
                     '			<param id="filterVideoStartDate" type="xsd:date" dir="in">\n' +
                     '				<dc:description xml:lang="en">Filter for lower (inclusive) end of date range.  If changed by the user, the target should change "filterEndDate" accordingly to keep the duration constant.  Clients are encouraged to use date-type-specific widgets or commonly used words for days such as "today", "tomorrow", or even for ranges such as "next week", etc.</dc:description>\n' +
                     '			</param>\n' +
                     '			<param id="filterVideoEndDate" type="xsd:date" dir="in">\n' +
                     '				<dc:description xml:lang="en">Filter for higher (inclusive) end of date range.  Clients are encouraged to use date-type-specific widgets or commonly used words for days such as "today", "tomorrow", or even for ranges such as "next week", etc.</dc:description>\n' +
                     '			</param>\n' +
                     '			<param id="filterVideoGenre" type="mceBase:videoGenreType" dir="in">\n' +
                     '				<dc:description xml:lang="en"/>\n' +
                     '			</param>\n' +
                     '			<param id="filterVideoActors" type="uis:csvlist" dir="in">\n' +
                     '				<dc:description xml:lang="en"/>\n' +
                     '			</param>\n' +
                     '			<param id="filterVideoRating" type="mceBase:ratingType" dir="in">\n' +
                     '				<dc:description xml:lang="en"/>\n' +
                     '			</param>\n' +
                     '			<param dir="out" idref="videoLib"/>\n' +
                     '		</command>\n' +
                     '		-->\n' +
                     '		<variable id="videoStatus" type="mceBase:statusType">\n' +
                     '			<dc:description xml:lang="en">Reflects current status of video playing.</dc:description>\n' +
                     '			<dependency>\n' +
                     '				<relevant>true()</relevant>\n' +
                     '				<write>false()</write>\n' +
                     '			</dependency>\n' +
                     '		</variable>\n' +
                     '		<command id="playVideo" type="uis:voidCommand">\n' +
                     '			<dc:description xml:lang="en"/>\n' +
                     '			<dependency>\n' +
                     '				<write> value(\'videoStatus\') neq \'playing\' </write>\n' +
                     '			</dependency>\n' +
                     '		</command>\n' +
                     '		<command id="stopVideo" type="uis:voidCommand">\n' +
                     '			<dc:description xml:lang="en"/>\n' +
                     '			<dependency>\n' +
                     '				<write> value(\'videoStatus\') neq \'stopped\' </write>\n' +
                     '			</dependency>\n' +
                     '		</command>\n' +
                     '\n' +
                     '		<command id="pauseVideoPlayback" type="uis:voidCommand">\n' +
                     '			<dc:description xml:lang="en"/>\n' +
                     '			<dependency>\n' +
                     '				<write> value(\'videoStatus\') eq \'playing\' </write>\n' +
                     '			</dependency>\n' +
                     '		</command>\n' +
                     '		<command id="resumeVideoPlayback" type="uis:voidCommand">\n' +
                     '			<dc:description xml:lang="en"/>\n' +
                     '			<dependency>\n' +
                     '				<write> value(\'videoStatus\') eq \'paused\' </write>\n' +
                     '			</dependency>\n' +
                     '		</command>\n' +
                     '		<command id="restartVideoPlayback" type="uis:voidCommand">\n' +
                     '			<dc:description xml:lang="en"/>\n' +
                     '			<dependency>\n' +
                     '				<write> value(\'videoStatus\') neq \'stopped\' </write>\n' +
                     '			</dependency>\n' +
                     '		</command>\n' +
                     '		<command id="fastForwardVideoPlayback" type="uis:basicCommand">\n' +
                     '			<dc:description xml:lang="en"/>\n' +
                     '			<dependency>\n' +
                     '				<write> value(\'videoStatus\') eq \'playing\' </write>\n' +
                     '			</dependency>\n' +
                     '			<param dir="in" id="fastForwardVideoSpeed" type="xsd:string"/>\n' +
                     '		</command>\n' +
                     '		<command id="rewindVideoPlayback" type="uis:basicCommand">\n' +
                     '			<dc:description xml:lang="en"/>\n' +
                     '			<dependency>\n' +
                     '				<write> value(\'videoStatus\') eq \'\'playing\' </write>\n' +
                     '			</dependency>\n' +
                     '			<param dir="in" id="rewindVideoSpeed" type="xsd:string"/>\n' +
                     '		</command>\n' +
                     '	</set>\n' +
                     '\n' +
                     '	<set id="music">\n' +
                     '		<variable id="musicLib" type="music:musicLibType"></variable>\n' +
                     '		<variable id="musicSelection" type="music:ID">\n' +
                     '			<dc:description xml:lang="en">Selected album or music item.</dc:description>\n' +
                     '		</variable>\n' +
                     '		<variable id="currentMusic" type="music:ID">\n' +
                     '			<dc:description xml:lang="en">Reflects the music item currently played.</dc:description>\n' +
                     '			<dependency>\n' +
                     '				<write>false()</write>\n' +
                     '			</dependency>\n' +
                     '		</variable>\n' +
                     '		<variable id="musicStatus" type="mceBase:statusType">\n' +
                     '			<dc:description xml:lang="en">Reflects current status of music playing.</dc:description>\n' +
                     '			<dependency>\n' +
                     '				<relevant>true()</relevant>\n' +
                     '				<write>false()</write>\n' +
                     '			</dependency>\n' +
                     '		</variable>\n' +
                     '\n' +
                     '		<!-- GZ: The filter command is optional for our scenarios.  We may implement it in the future.\n' +
                     '		<command id="filterMusicLibrary" type="uis:basicCommand">\n' +
                     '			<dc:description xml:lang="en">Command to load the Picture data in set "PictureLibrary" based on the filter fields.</dc:description>\n' +
                     '			<param id="filterMusicTitle" type="xsd:string" dir="in" >\n' +
                     '				<dc:description xml:lang="en"/>\n' +
                     '			</param>\n' +
                     '			<param id="filterMusicArtist" type="xsd:string" dir="in" >\n' +
                     '				<dc:description xml:lang="en"/>\n' +
                     '			</param>\n' +
                     '			<param id="filterMusicAlbum" type="xsd:string" dir="in" >\n' +
                     '				<dc:description xml:lang="en"/>\n' +
                     '			</param>\n' +
                     '			<param id="filterMusicStartDate" type="xsd:date" dir="in" >\n' +
                     '				<dc:description xml:lang="en">These are (filterMusicStartDate and filterMusicEndDate) to filter for ReleaseYear. Filter for lower (inclusive) end of date range.  If changed by the user, the target should change "filterEndDate" accordingly to keep the duration constant.  Clients are encouraged to use date-type-specific widgets or commonly used words for days such as "today", "tomorrow", or even for ranges such as "next week", etc.</dc:description>\n' +
                     '			</param>\n' +
                     '			<param id="filterMusicEndDate" type="xsd:date" dir="in" >\n' +
                     '				<dc:description xml:lang="en">These are (filterMusicStartDate and filterMusicEndDate) to filter for ReleaseYear. Filter for higher (inclusive) end of date range.  Clients are encouraged to use date-type-specific widgets or commonly used words for days such as "today", "tomorrow", or even for ranges such as "next week", etc.</dc:description>\n' +
                     '			</param>\n' +
                     '			<param id="filterMusicGenre" type="musicGenreType" dir="in" >\n' +
                     '				<dc:description xml:lang="en"/>\n' +
                     '			</param>\n' +
                     '			<param id="filterMusicRating" type="ratingType" dir="in" >\n' +
                     '				<dc:description xml:lang="en"/>\n' +
                     '			</param>\n' +
                     '			<param dir="out" idref="musicLib"/>\n' +
                     '		</command>\n' +
                     '		-->\n' +
                     '\n' +
                     '		<command id="playMusic" type="uis:voidCommand">\n' +
                     '			<dc:description xml:lang="en">Note that songs, albums and playlists can be provided as parameter.</dc:description>\n' +
                     '			<dependency>\n' +
                     '				<write> value(\'musicStatus\') neq \'playing\' </write>\n' +
                     '			</dependency>\n' +
                     '		</command>\n' +
                     '		<command id="stopMusic" type="uis:voidCommand">\n' +
                     '			<dc:description xml:lang="en">Note that songs, albums and playlists can be provided as parameter.</dc:description>\n' +
                     '			<dependency>\n' +
                     '				<write> value(\'musicStatus\') neq \'stopped\' </write>\n' +
                     '			</dependency>\n' +
                     '		</command>\n' +
                     '\n' +
                     '		<command id="pauseMusic" type="uis:voidCommand">\n' +
                     '			<dc:description xml:lang="en"/>\n' +
                     '			<dependency>\n' +
                     '				<write> value(\'musicStatus\') eq \'playing\' </write>\n' +
                     '			</dependency>\n' +
                     '		</command>\n' +
                     '		<command id="resumeMusic" type="uis:voidCommand">\n' +
                     '			<dc:description xml:lang="en"/>\n' +
                     '			<dependency>\n' +
                     '				<write> value(\'musicStatus\') eq \'playing\' </write>\n' +
                     '			</dependency>\n' +
                     '		</command>\n' +
                     '		<command id="restartMusic" type="uis:voidCommand">\n' +
                     '			<dc:description xml:lang="en"/>\n' +
                     '			<dependency>\n' +
                     '				<write> value(\'musicStatus\') neq \'\'stopped\' </write>\n' +
                     '			</dependency>\n' +
                     '		</command>\n' +
                     '		<command id="fastForwardMusic" type="uis:basicCommand">\n' +
                     '			<dc:description xml:lang="en"/>\n' +
                     '			<dependency>\n' +
                     '				<write> value(\'musicStatus\') eq \'playing\' </write>\n' +
                     '			</dependency>\n' +
                     '			<param dir="in" id="fastForwardMusicSpeed" type=" "/>\n' +
                     '		</command>\n' +
                     '		<!-- In the MCE interface rewind for audio is not available. -->\n' +
                     '		<command id="nextSong" type="uis:voidCommand">\n' +
                     '			<dc:description xml:lang="en">Go to next song.  Works for any status. </dc:description>\n' +
                     '			<dependency>\n' +
                     '				<write> value(\'musicStatus\') neq \'stopped\' </write>\n' +
                     '			</dependency>\n' +
                     '		</command>\n' +
                     '		<command id="prevSong" type="uis:voidCommand">\n' +
                     '			<dc:description xml:lang="en">Go to previous song.  Works for any status. </dc:description>\n' +
                     '			<dependency>\n' +
                     '				<write> value(\'musicStatus\') neq \'stopped\' </write>\n' +
                     '			</dependency>\n' +
                     '		</command>\n' +
                     '\n' +
                     '		<!-- Playlists are not part of our scenarios.  We drop this for now, and implement later.\n' +
                     '		<command id="addSongToPlaylist" type="uis:basicCommand">\n' +
                     '			<param id="songNodeId" dir="in" type="music:ID"></param>\n' +
                     '			<param id="playListName" type="xsd:string" dir="in"></param>\n' +
                     '			</command>\n' +
                     '			<command id="removeSongFromPlaylist" type="uis:basicCommand">\n' +
                     '			<dc:description xml:lang="en"/>\n' +
                     '			<dependency>\n' +
                     '			<relevant>true()</relevant>\n' +
                     '			</dependency>\n' +
                     '			<param id="sonfToRemove" dir="in" type="xsd:string"></param>\n' +
                     '		</command>\n' +
                     '		-->\n' +
                     '	</set>\n' +
                     '\n' +
                     '	<set id="epgFilter">\n' +
                     '		<dc:description xml:lang="en">This set contains input fields for filtering the content of the epg set.  Multiple filter fields are implicitly ANDed for the result.  The user should invoke "getEpgData" to fill the set "epg" based on the filter criteria.\n' +
                     '		</dc:description>\n' +
                     '		<variable id="availableChannels" type="channelListType" includesRes="true">\n' +
                     '			<dc:description xml:lang="en">List of available channels.  Must include one entry "---all---" for selecting all channels.  Channel codes must not contain spaces.  Labels for channel codes (including "---all---") must be provided as dynamic resources.\n' +
                     '			</dc:description>\n' +
                     '			<dependency>\n' +
                     '				<relevant>false()</relevant>\n' +
                     '				<write>false()</write>\n' +
                     '			</dependency>\n' +
                     '			<!--Note: Since dynamic resources cannot be specified on type definitions, the channel labels have to be provided for both variables (availableChannels and filterChannel) separately.-->\n' +
                     '		</variable>\n' +
                     '		<command id="getEpgData" type="uis:basicCommand">\n' +
                     '			<dc:description xml:lang="en">Command to load the EPG data in set "epg" based on the filter fields.\n' +
                     '			</dc:description>\n' +
                     '			<param dir="in" id="filterChannel" type="channelListType">\n' +
                     '				<dc:description xml:lang="en">Filter for channels. Empty content means all channels.</dc:description>\n' +
                     '				<selection closed="true">\n' +
                     '					<selectionSetDynamic id="filterChannelSelection" varRef="availableChannels"/>\n' +
                     '				</selection>\n' +
                     '			</param>\n' +
                     '			<param dir="in" id="filterStartDate" type="xsd:date">\n' +
                     '				<dc:description xml:lang="en">Filter for lower (inclusive) end of date range.  If changed by the user, the target should change "filterEndDate" accordingly to keep the duration constant.  Clients are encouraged to use date-type-specific widgets or commonly used words for days such as "today", "tomorrow", or even for ranges such as "next week", etc.</dc:description>\n' +
                     '			</param>\n' +
                     '			<param dir="in" id="filterEndDate" type="xsd:date">\n' +
                     '				<dc:description xml:lang="en">Filter for higher (inclusive) end of date range.  Clients are encouraged to use date-type-specific widgets or commonly used words for days such as "today", "tomorrow", or even for ranges such as "next week", etc.</dc:description>\n' +
                     '			</param>\n' +
                     '			<param dir="in" id="filterGenre" type="epgGenreType">\n' +
                     '				<dc:description xml:lang="en">Filter for genre.  Only one genre can be specified for filtering.  The value "---all---" is reserved to indicate that all genres should be included. </dc:description>\n' +
                     '			</param>\n' +
                     '			<param dir="in" id="filterTitle" type="xsd:string">\n' +
                     '				<dc:description xml:lang="en">Looks for word matches in title field of program data.  Use quotes for exact matches.  Empty content means no filtering based on filterTitle.</dc:description>\n' +
                     '			</param>\n' +
                     '			<param dir="in" id="filterText" type="xsd:string">\n' +
                     '				<dc:description xml:lang="en">Looks for word matches in all text fields of program data (title, description, episodetitle, keywords, credits, etc.).  Use quotes for exact matches. Empty content means no filtering based on filterText.</dc:description>\n' +
                     '			</param>\n' +
                     '			<param dir="in" id="filterCredits" type="xsd:string">\n' +
                     '				<dc:description xml:lang="en">Looks for word matches in the credits field of program data.  Use quotes for exact matches.  Empty content means no filtering based on filterCredits.</dc:description>\n' +
                     '			</param>\n' +
                     '		</command>\n' +
                     '	</set>\n' +
                     '	<set id="epg" dim="channelType xsd:dateTime">\n' +
                     '		<dc:description>EPG data provided as tabular data along the dimensions "channel" (see channelType) and "start-time" (see timeType).  Labels for the dimension types have to be provided via resource sheets.  Note that this is a sparse 2-dim array, i.e. not all possible combinations of channels and start-times are populated (since there is usually not a program item starting every minute).  Nevertheless, this 2-dim structure is deemed suitable for users to be able to navigate along the "channel" and "start-time" dimension in a meaningful way, even for controllers that have no knowledge about EPGs in general.</dc:description>\n' +
                     '		<dependency>\n' +
                     '			<write>false()</write>\n' +
                     '		</dependency>\n' +
                     '		<variable id="epgEndTime" type="xsd:dateTime">\n' +
                     '			<dc:description xml:lang="en">End time of broadcast.  Note that this may differ from the duration of the program item due to commercials or other real-time impacts.</dc:description>\n' +
                     '		</variable>\n' +
                     '		<variable id="epgTitle" type="xsd:string">\n' +
                     '			<dc:description xml:lang="en">Title of the program item.  Language depends on the source of the EPG information.</dc:description>\n' +
                     '		</variable>\n' +
                     '		<variable id="epgGenre" type="epgGenreType">\n' +
                     '			<dc:description xml:lang="en">Genre of program item.</dc:description>\n' +
                     '		</variable>\n' +
                     '		<variable id="Description" type="xsd:string">\n' +
                     '			<dc:description xml:lang="en">Text description of the program item.  Language depends on the source of the EPG information.</dc:description>\n' +
                     '		</variable>\n' +
                     '		<variable id="isepisodic" type="xsd:boolean">\n' +
                     '			<dc:description xml:lang="en">true if the program item is part of an episode, otherwise false.  If true, the variable "episodetitle" reflects the title of the episode.</dc:description>\n' +
                     '			<!--Open issue: Do we really need this?  Can we just conclude that if "episodetitle" is empty, the item is not episodic?-->\n' +
                     '		</variable>\n' +
                     '		<variable id="episodetitle" type="xsd:string">\n' +
                     '			<dc:description xml:lang="en">Episode title if program item belongs to an episode - otherwise empty.  Language depends on the source of the EPG information.</dc:description>\n' +
                     '		</variable>\n' +
                     '		<variable id="properties" type="xsd:string" dim="mceBase:epgPropertyName">\n' +
                     '			<dc:description xml:lang="en">Property map for additional program data, in the form of properties[propName]=value (string). Property names are defined in the type definition of epgPropertyName, and labels for them should be defined in resource sheets.  Properties may include: language, year, originalairdate, rating, keywords, credits, copyright.  </dc:description>\n' +
                     '		</variable>\n' +
                     '		<command id="recordProgramInEpg" type="uis:basicCommand">\n' +
                     '			<dc:description xml:lang="en"> Record EPG item.  These recording preferences are configured as the attributes of the programRecord element. Look MCE SDK for more information This variables have to be changed for each programme request, otherwise, programme request would be done with the previous values of these variables. </dc:description>\n' +
                     '			<param id="isRecurringInEpg" dir="in" type="xsd:boolean">\n' +
                     '				<dc:description xml:lang="en">Optional. Indicates whether the request is for a series of recordings, or just a one-time recording. A value of "true" indicates a recurring (series) recording, and "false" indicates a one-time recording. The default value is "false".</dc:description>\n' +
                     '			</param>\n' +
                     '			<param dir="in" idref="prepadding"/>\n' +
                     '			<param dir="in" idref="postpadding"/>\n' +
                     '			<param dir="in" idref="allowAlternateAirings"/>\n' +
                     '			<param dir="in" idref="allowAlternateServices"/>\n' +
                     '			<param dir="in" idref="firstRunOnly"/>\n' +
                     '			<param dir="in" idref="daysOfWeek"/>\n' +
                     '			<param dir="in" idref="keepUntil"/>\n' +
                     '			<param dir="in" idref="quality"/>\n' +
                     '		</command>\n' +
                     '	</set>\n' +
                     '	<set id="epgControls">\n' +
                     '		<command id="recordProgram" type="uis:basicCommand">\n' +
                     '			<dc:description xml:lang="en">These recording preferences are configured as the attributes of the programRecord element. Look MCE SDK for more information This variables have to be changed for each programme request, otherwise, programme request would be done with the previous values of these variables. </dc:description>\n' +
                     '			<param id="mappedChannelNumber" dir="in" type="channelType"></param>\n' +
                     '			<param id="starttime" dir="in" type="xsd:datetime"/>\n' +
                     '			<!-- mappedChannelNumber and starttime need to be taken from EPG target exploration-->\n' +
                     '			<param id="programDuration" dir="in" type="xsd:duration">\n' +
                     '				<dc:description xml:lang="en">Optional. Specifies the duration of the program, in minutes. This attribute is used only if the document does not specify any program elements, and results in a manual recording. This attribute must specify a positive, non-zero number to create a manual recording. </dc:description>\n' +
                     '			</param>\n' +
                     '			<param id="isRecurring" dir="in" type="xsd:boolean">\n' +
                     '				<dc:description xml:lang="en">Optional. Indicates whether the request is for a series of recordings, or just a one-time recording. A value of "true" indicates a recurring (series) recording, and "false" indicates a one-time recording. The default value is "false".</dc:description>\n' +
                     '			</param>\n' +
                     '			<param dir="in" idref="prepadding"/>\n' +
                     '			<param dir="in" idref="postpadding"/>\n' +
                     '			<param dir="in" idref="allowAlternateAirings"/>\n' +
                     '			<param dir="in" idref="allowAlternateServices"/>\n' +
                     '			<param dir="in" idref="firstRunOnly"/>\n' +
                     '			<param dir="in" idref="daysOfWeek"/>\n' +
                     '			<param dir="in" idref="keepUntil"/>\n' +
                     '			<param dir="in" idref="quality"/>\n' +
                     '		</command>\n' +
                     '		<variable id="prepadding" type="mceBase:epgPrepadding">\n' +
                     '		    	<dc:description xml:lang="en">Optional. Specifies the pre-padding time, in seconds, with respect to the start time. To begin recording before the start time, specify a negative value. To begin recording after the start time, specify a positive value. The default value is 0</dc:description>\n' +
                     '		</variable>\n' +
                     '		<variable id="postpadding" type="mceBase:epgPostpadding">\n' +
                     '			<dc:description xml:lang="en">Optional. Specifies the post-padding time, in seconds, with respect to the end time. To stop recording before the end time, specify a negative value. To stop recording after the end time, specify a positive value. The default value is 0.</dc:description>\n' +
                     '		</variable>\n' +
                     '		<variable id="allowAlternateAirings" type="xsd:boolean">\n' +
                     '			<dc:description xml:lang="en">Optional. Indicates how Windows Media Center should respond when it cannot find the specified airing of a program.\n' +
                     'A value of "true" indicates that Windows Media Center can schedule the recording at an alternate time. A series will target a specific time for the first item that is found chronologically in the Electronic Program Guide (EPG).\n' +
                     'A value of "false" indicates that if Windows Media Center cannot find the program within the specified time period, it should fail the request and inform the user. The default value is "false".</dc:description>\n' +
                     '		</variable>\n' +
                     '		<variable id="allowAlternateServices" type="xsd:boolean">\n' +
                     '			<dc:description xml:lang="en">Optional. Indicates how Windows Media Center should respond when it cannot find the specified airing of a program on a particular broadcast service. A value of "true" indicates that Windows Media Center can look for the same program on a different broadcast service. A value of "false" indicates that if Windows Media Center cannot find the program on the specified broadcast service, it should fail the request. The default value is "false".</dc:description>\n' +
                     '		</variable>\n' +
                     '		<variable id="firstRunOnly" type="xsd:boolean">\n' +
                     '			<dc:description xml:lang="en">Optional. Indicates whether to allow recording of TV programs that are marked as reruns in the program guide. A value of "true" prevents recording of reruns, and "false" allows it. The default value is "false".</dc:description>\n' +
                     '		</variable>\n' +
                     '		<variable id="daysOfWeek" type="xsd:boolean" dim="dayOfWeekType">\n' +
                     '			<dc:description xml:lang="en">Optional. Indicates the days of the week that a program can be recorded for manual and generic "keyword" requests. The default is true for every day.</dc:description>\n' +
                     '		</variable>\n' +
                     '		<variable id="keepUntil" type="keepUntilType">\n' +
                     '			<dc:description xml:lang="en">Optional. Specifies how long to keep the recorded files. If no value is specified, the computer\'s default value is used. </dc:description>\n' +
                     '		</variable>\n' +
                     '		<variable id="quality" type="qualityType">\n' +
                     '			<dc:description xml:lang="en">Optional. Specifies the quality of recording. If no value is specified, the computer\'s default value is used. </dc:description>\n' +
                     '		</variable>\n' +
                     '	</set>\n' +
                     '</uiSocket>' );
                     

               default:
                  throw new Error("org_myurc_urchttp_test: No GetDocument data for socket '" + messageParameters.args.url + "'");    
                  break;        
            }
            
         default: 
            throw new Error("org_myurc.urchttp.test: No data for message type '" + messageParameters.type + "' in message to " + url);
      }
   };


   /********************************************************
   org_myurc_urchttp_test_getMessageParameters: Determine the parameters of a URC-HTTP request message.
   url: URL of the request message
   return: New object with the following properties:
      type: One of {"GetCompatibleUIs", "GetResources", "GetDocument", 
         "openSessionRequest", "closeSessionRequest", "getValues", "getUpdates", "setValues"}.
      socket: Name of the socket as used in the RUIPath / null for out-of-session messages.
      sessionId: session identifier / null for out-of-session messages.
   */
   org_myurc_urchttp_test_getMessageParameters = function(url) {

      // First handle the out-of-session message types
      var outOfSessionMessage = url.match(/GetCompatibleUIs|GetResources|GetDocument/);
      if (outOfSessionMessage)
         return { "type": outOfSessionMessage[0], "socket": null, "args": org_myurc_lib_getArgs(url) };

      var inSessionMessage = url.match(/\w+:\/\/[\S.]+\/(\S*)\?([^\&]*)/);
      if (inSessionMessage)
         return { "type": inSessionMessage[2], "socket": inSessionMessage[1], "args": org_myurc_lib_getArgs(url) };
      else
         return { "type": "GetDocument", "socket": null, "args": {"url": url} };    // A plain URL is treated as "GetDocument".
   };

}     // end big if.
