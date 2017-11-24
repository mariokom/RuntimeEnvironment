# RuntimeEnvironment

The Integrated Runtime Environment - IRE aims at providing accessible user interfaces aligned to the individual needs and preferences of diverse users. For this purpose it includes three major functional modules:
- Support of diverse and individually tailored AT provided by the AsTeRICS runtime environment (ARE)
- Access to a variety of products and services via user interface sockets as defined by the Universal Remote Console (URC)
- Serving flexible, runtime adaptive user interfaces that match dynamic personal, technical and environmental requirements using MyUI
The IRE enables to lower development costs and simplify the development of assistive solutions, mainly through the capabilities the IRE offers to developers, in terms of reusing a variety of components (e.g., ARE bundles, URC targets/controllers, MyUI adaptive interfaces) and functionalities. The Integrated Runtime Environment also aims to offer appropriate interactions to heterogeneous groups of users, and in addition, overcome interoperability problems of heterogeneous devices and services.

Prosperity4All, D203.1 Runtime Environment Final Prototype

Repository for Task T203.3 of the Prosperity4All project

## Installation


### IRE Installation & Testing Videos

Installation & Testing IRE part 1: https://www.youtube.com/watch?v=59QCy2KHReQ&feature=youtu.be

Installation & Testing IRE part 2: https://www.youtube.com/watch?v=AadTTfL56ZI

Installation & Testing IRE part 3 (scenario): https://www.youtube.com/watch?v=jdIMmnrsH7s


### Prerequisites

1) Windows OS

2) 32-bit Java SE Development Kit (8 or above)

3) Web camera

4) Web browser (Chrome Version 45 or above for MyUI)



### Software installation

To test or use the complete solution, the following platforms should be installed and configured appropriately:

1) AsTeRICS

2) URC

3) MyUI

4) GPII


It is required to:

- download the content from this link: https://github.com/mariokom/RuntimeEnvironment

- extract the contents of the folder to a preferred location

- Important: let "REFolderPath" be this location


This is a Quickstart Guide. For more information refer to the the Runtime Environment Setup manual: 
https://github.com/mariokom/RuntimeEnvironment/blob/master/Documentation/P4All-D203.1-2-ANNEX%20III%20Runtime%20Environment%20Setup%20manual_v5.pdf

Step 1: By default, AsTeRICS is configured to communicate with UCH on the same machine (localhost). If there is a need for UCH to run on a different machine, please see Section 4 of the Runtime Environment Setup manual, otherwise no further actions are required: the AsTeRICS files are precompiled and preconfigured.

Step 2: Install GPII by using the installer to be found here: ‘REFolderPath/GPII/GPII.160119.044327.msi’

Step 3: Run (double click) the ‘REFolderPath/auto-conf.bat’ file with administrator privileges (right click on the file -> Run as administrator). The execution of this file will automatically clone MyUI in order to be hosted by the UCH server, as well as configure the GPII framework. The execution window will prompt the user to enter the GPII installation folder path (the default GPII installation folder path will be similar or identical to ‘C:\Program Files (x86)\GPII’). If any errors occur during the execution of the file, if you do not have administrator privileges or if you are running a windows version older than Windows 7 it is recommended to substitute this step (Step 3) by following the instructions in 
- Section 4.3 Installation 
- Section 4.4 Configuration
of the Runtime Environment Setup manual.
	

## Usage

To test that everything was installed and configured correctly, you can do the following:

1) Start GPII by executing the file located at <GPIIRootFolder>/start.cmd.

2) Notify GPII that a specific persona was logged in by visiting this URI in the browser: localhost:8081/user/nicholas/login


3) The GPII will automatically start the three applications (AsTeRICS, MyUI, UCH).

- AsTeRICS should take control of the mouse.

- MyUI should appear on your screen, in chrome browser.

- UCH should run on the background, ready to interact with AsTeRICS or MyUI and control the targeted devices or applications.

4) You may proceed by following Step 4 of the Head-ColouredLightBulb and WoehlkeWebsteckdose electricity outlet demo described in Sections 5.1 and 5.2 of the Runtime Environment Setup manual.

5) Close the program by logging out the persona by visiting the URI: ‘localhost:8081/user/nicholas/logout’

6) Close GPII.


## IRE Use Case Scenarios
In this document a use case scenario is described in order to show how the IRE can be used effectively in real life situations:
https://github.com/mariokom/RuntimeEnvironment/blob/master/Documentation/IRE%20Scenarios.pdf


## License

https://github.com/mariokom/RuntimeEnvironment/blob/master/License.txt
