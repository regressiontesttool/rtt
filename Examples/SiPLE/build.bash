#!/bin/bash

# This program and the accompanying materials are made available under the
# terms of the MIT license (X11 license) which accompanies this distribution.

# author: C. Bürger

clear_all(){
	rm -rf src-gen
	rm -rf java-bin
	mkdir src-gen
	mkdir java-bin
}

make_syntax(){
	java -jar tools/jflex.jar --quiet -d src-gen/siple specifications/Lexer.jflex
	java -jar tools/beaver.jar -c -d src-gen specifications/Parser.beaver
}

make_binary(){
	javac -deprecation -encoding utf-8 -classpath tools/beaver-rt.jar \
		-d java-bin -sourcepath src-gen src/**/*.java src-gen/**/*.java
	binaries=`find java-bin/*`
	jar cfm $1 manifest.txt -C java-bin ${binaries#*/}
}

# Delete jars:
rm *.jar

# Build:
clear_all
make_syntax
# BEWARE: Because of buggy JastAdd visit checks (v 2.1.10) false-positive circularity exceptions are thrown!
java -jar tools/jastadd2.jar --rewrite="" --visitCheck="false" --package="siple.ast" --o=src-gen \
	specifications/*.ast specifications/*.jrag
make_binary siple.jar
