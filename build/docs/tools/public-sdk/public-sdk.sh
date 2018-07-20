#!/bin/bash

# Temp
PATH_TEMP=temp
SOURCE_OUT_PATH=../../../../..
SOURCES_IN=${SOURCE_OUT_PATH}/out/target/common/obj/JAVA_LIBRARIES/sunmisdk_stubs_current_intermediates/
SOURCES_OUT=$PATH_TEMP/sources.jar
DOCS_IN=${SOURCE_OUT_PATH}/out/target/common/docs/sunmi-sdk/
DOCS_OUT=$PATH_TEMP/docs.jar

# SunmiSDK
PATH_SUNMISDK=SunmiSDK
SDK_NAME=SunmiSDK
SDK_VERSION=1.0.1
AAR_OUT=${SOURCE_OUT_PATH}/out/target/common/obj/JAVA_LIBRARIES/sunmi.sdk.aar_intermediates/javalib.aar

SDK_AAR_FILE=$PATH_SUNMISDK/$SDK_NAME-$SDK_VERSION.aar
SDK_POM_FILE=$PATH_SUNMISDK/$SDK_NAME-$SDK_VERSION.pom
SDK_DOCS_FILE=$PATH_SUNMISDK/$SDK_NAME-$SDK_VERSION-docs.jar
SDK_SOURCES_FILE=$PATH_SUNMISDK/$SDK_NAME-$SDK_VERSION-sources.jar


main(){
  temp
  SunmiSDK
  if [ -f $SDK_AAR_FILE ]&&[ -f $SDK_POM_FILE ]&&[ -f $SDK_DOCS_FILE ]&&[ -f $SDK_SOURCES_FILE ]; then
  	echo Success
  else
  	echo Failed
  fi
}

SunmiSDK(){
  cp $SOURCES_OUT $SDK_SOURCES_FILE
  cp $DOCS_OUT $SDK_DOCS_FILE
  cp $AAR_OUT $SDK_AAR_FILE
  sed -i 's/<'"version"\>'.*</<'"version"'\>'"$SDK_VERSION"'</g' default.pom
  cp default.pom $SDK_POM_FILE
}

temp(){
  #1: Init OUT Path
  rm -rf $PATH_TEMP $PATH_SUNMISDK
  mkdir $PATH_TEMP $PATH_SUNMISDK
  #2: generate sources and docs
  generateJar $SOURCES_OUT $SOURCES_IN 
  generateJar $DOCS_OUT $DOCS_IN 
}


# $1 Output File
# $2 Input Path
generateJar() {
  jar cvf $1 -C $2 . > /dev/null
}

main