
VLC Lua http interface modified by smart design to include minimum buttons for operations (original files marked as _old)
Adding the ability to play directly from a URL (Youtube or Vimeo or anything VLC is capable of playing)
The interface was edited to enable sending messages to the VLC httpd Lua interface - here to control external devices,
It adds a Java server to monitor a TCP port, set to 9991 in this release, and make a translation of the code, and then send it on the serial port, that is static - hard coded - at this release.
The serial communication is done through using the JSSC library, Java Simple Serial Connector, found on github at the link below (and provided with this implementation).

Links:
====
The VLC web site  . . . . . . . . . . . . . . . . http://www.videolan.org/
The VLC Lua Commands  . . . . . . . . . . . . . . https://www.videolan.org/developers/vlc/share/lua/README.txt
JSSC Library on Github  . . . . . . . . . . . . . https://github.com/scream3r/java-simple-serial-connector/releases
JSSC Library wiki . . . . . . . . . . . . . . . . https://code.google.com/p/java-simple-serial-connector/w/list

Relevant Links:
============
RXTX Library Wiki . . . . . . . . . . . . . . . . . . . . . . .  . . . . . . http://rxtx.qbang.org/wiki/index.php/Main_Page
Smart Design  . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .  http://smartdesignworldwide.com/
Reference Manual of the Programming Language Lua 2.1  . . . . . . . . . . .  http://www.lua.org/manual/2.1/

Notes:
=========
This is a specific implementation of a remote control, but the modification in the controller.js and the VLM command xml file enable a new way of controlling the VLC machine and using these commands to further connect to other services and devices.
The specific commands in our implementation relate to controlling a TV set through the serial interface. The translation between the JS and the serial port is done in two stages.
The JS script uses the new VLM Serial command function to send commands the the VLC machine. 
First step is when the VLC machine interprets the LUA script in the VLM_CMD.xml file. This file can contain a script to connect to sockets, which any application can be listening to. 
The second step is the listening application, a java server in our case that implements a translation between the code received by the VLC script (which remained unaltered in the transaction in the VLM_CMD script) to a specific set of commands - depending on end recipient. The Java server can also be altered to carry out specific functions. In this release only translation and rerouting is done by the server. the commands are hard coded, but the code could easily changed to them read from a text/xml file that can be dynamically edited. The java server here puts the new (if any) commands on the serial port. TV is ON!

Installation:
=========
First, enable the HTTP interface on the VLC preferences. You may have to download it first.
The httpd folder in the VLC application is to be replaced. There almost all files were modified to accommodate the changes. The JS files, the HTML files and some VLM commands. If you have these modified already, it is recommended to DIFF and add the lines you think relevant. 

To keep things tidy, it was preferable to install the java server files into the httpd module. You are however free to install it wherever. The JSSC library should be installed (jar file provided, but not guaranteed or supported) or kept in the same folder to compile with the server.
Edit server code to locate the appropriate serial and TCP ports (and any commands to be translated). compile with the option [-cp “.:jssc“] .run.
Change the Index.html, and ui.js for the appropriate commands (to correspond to JAVA) 
NOTE: VLM_CMD.xml is currently a pass thru only. The TCP port, however must be changed too to correspond to the Java server.

Running:
=======
Run VLC and the Java server on a machine.
Accessing the machine on port 8080 will open the VLC server. The buttons open and play will send tvOn commands. The stop button will turn the TV off.



