# Program name:	Makefile
# Description:	Build the NVrmlToX3dNist translator distribution
# Author:	Don Brutzman
# Created:	17 March 2002
# Revised:	17 March 2002
#
# Invocation:	make all
#
# ==============================================================================

help:
	@echo 'Usage:  make [all | doc]'
all:
	date
	mkdir class
	java  -version
	javac -classpath javasrc -d class -deprecation \
		javasrc/iicm/ge3d/*.java \
		javasrc/iicm/utils3d/*.java \
		javasrc/iicm/vrml/pw/*.java \
		javasrc/iicm/vrml/pwutils/*.java \
		javasrc/iicm/vrml/vrml2x3d/*.java
#	make -i mkdir
#	make    doc
	cd class; jar -cvf Vrml97ToX3dNist.jar */*/*.class */*/*/*.class
	mv class/Vrml97ToX3dNist.jar .
#	@echo copy .jar for use with Vrml97ToX3dNist.bat
#	cp Vrml97ToX3dNist.jar c:/www.web3D.org/TaskGroups/x3d/translation/
#	cp Vrml97ToX3dNist.jar c:/v2x3d/.
	@echo done.

#mkdir:
#	mkdir javadoc

#doc:
#	javadoc -classpath javasrc -d javadoc -author -version -package			\
#		-windowtitle "NIST Translator: VRML 97 to X3D Javadoc"			\
#		-doctitle "<center>NIST Translator: VRML 97 to X3D Javadoc</center>"	\
#		iicm.ge3d		\
#		iicm.utils3d		\
#		iicm.vrml.pw		\
#		iicm.vrml.pwutils	\
#		iicm.vrml.vrml2x3d


