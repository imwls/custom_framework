#!/bin/bash
###########################################################
#
# Input Args
#
FTP_DOWNLOAD_PATH=$1
ARM_64=$2
APPS_PLATFORM=$3

###########################################################
#
# App Configs
#
APPS_ORI_REP=(
# For ROM App
Provision                   SunmiWelcome
Launcher3                   SunmiLauncher
Launcher3                   HemaLauncher
Settings                    SunmiSettings
# For Sec App
PrinterService              SunmiPrinterService
SecureDialog                SunmiSecurityService
SunmiPayHardwareService     SunmiPayHardwareService
)

APPS_DATA=(
# For Sec App
SubPOS FactoryTool
# For Antfin
com.alipay.zoloz.smile
SunmiT2Demo
Orbbec_STT
)

APPS_VENDOR=()

APPS_PRIVILEGED=(SunmiWelcome SunmiScanner HemaLauncher)

###########################################################
#
# Ftp Info
#
FTP_IP=172.16.0.7
FTP_USER=woyou
FTP_PSW=woyou
FTP_DOWNLOAD_PREFIX=APK/

###########################################################
#
# Vendor - SUNMI
#
VENDOR_DIR=vendor/sunmi/app/
VENDOR_MAKEFILR=vendor/sunmi/app/Android.mk
VENDOR_SUNMI=vendor/sunmi/app/sunmi.mk

###########################################################
function check_exit()
{
  code=$?
  if [ $code != 0 ]
  then
    exit $code
 fi
}

function download_apks_from_ftp()
{
echo "-------Begin Ftp Download APK-----"
rm -rf $VENDOR_DIR
mkdir -p $VENDOR_DIR
ftp -vin <<EOF
open $FTP_IP
user $FTP_USER $FTP_PSW
cd $FTP_DOWNLOAD_PATH_APP
lcd $VENDOR_DIR
mget *
close
bye
EOF
echo "-------End Ftp Download APK-----"
}

function process_apk_lib()
{
  res=0
  apk=$1
  # include $(CLEAR_VARS)
  echo -e "include \$(CLEAR_VARS)" >> $VENDOR_MAKEFILR
  #: LOCAL_MODEL
  name=$(basename $apk)
  name=${name%%.apk}
  echo -e "LOCAL_MODULE := "$name >> $VENDOR_MAKEFILR
  # -----------SUNMI------------
  echo -en "\\" >> $VENDOR_SUNMI
  echo -en "\n      $name " >> $VENDOR_SUNMI
  # LOCAL_MODULE_TAGS := optional
  echo -e "LOCAL_MODULE_TAGS := optional" >> $VENDOR_MAKEFILR
  #LOCAL_SRC_FILES := $(LOCAL_MODULE).apk
  echo -e "LOCAL_SRC_FILES := \$(LOCAL_MODULE).apk" >> $VENDOR_MAKEFILR
  #LOCAL_MODULE_CLASS := APPS
  echo -e "LOCAL_MODULE_CLASS := APPS" >> $VENDOR_MAKEFILR
  #LOCAL_MODULE_SUFFIX := $(COMMON_ANDROID_PACKAGE_SUFFIX)
  echo -e "LOCAL_MODULE_SUFFIX := \$(COMMON_ANDROID_PACKAGE_SUFFIX)" >> $VENDOR_MAKEFILR
  #2: JNI
  #2-1: Arm-v8
  if [ "$ARM_64" = "true" ]
  then
    all_so=$(unzip -l $apk | grep 'lib/arm64-v8a.*so$' | awk '{print $4}')
  else
    all_so=()
  fi
  if [ ${#all_so} -eq 0 ]
  then
    #2-2: Arm-v7
    all_so=$(unzip -l $apk | grep 'lib/armeabi-v7a.*so$' | awk '{print $4}')
    if [ ${#all_so} -eq 0 ]
      then
      #2-3: Arm
      all_so=$(unzip -l $apk | grep 'lib/armeabi/.*so$' | awk '{print $4}')
    fi
  fi
  if [ ${#all_so} -gt 0 ]
  then
    echo -n "LOCAL_PREBUILT_JNI_LIBS := " >> $VENDOR_MAKEFILR
    for so in ${all_so[@]}
    do
      echo -en "\\" >> $VENDOR_MAKEFILR
      echo -en "\n      @$so " >> $VENDOR_MAKEFILR
    done
    echo -e >> $VENDOR_MAKEFILR
  fi

  # LOCAL_OVERRIDES_PACKAGES := Provision
  i=0
  for rep in ${APPS_REP[@]}
  do
    tmp_name=${name:0:${#rep}}
    if [ $rep = $tmp_name ]
    then
        echo -e "LOCAL_OVERRIDES_PACKAGES := "${APPS_ORI[i]} >> $VENDOR_MAKEFILR
    fi
    i=$[$i+1]
  done

  # LOCAL_MODULE_PATH := $(TARGET_OUT_DATA_APPS)
  for rep in ${APPS_DATA[@]}
  do
    tmp_name=${name:0:${#rep}}
    if [ $rep = $tmp_name ]
    then
        echo -e "LOCAL_MODULE_PATH := \$(TARGET_OUT_DATA_APPS)" >> $VENDOR_MAKEFILR
    fi
  done

  # LOCAL_PRIVILEGED_MODULE := true
  for rep in ${APPS_PRIVILEGED[@]}
  do
    tmp_name=${name:0:${#rep}}
    if [ $rep = $tmp_name ]
    then
        echo -e "LOCAL_PRIVILEGED_MODULE := true" >> $VENDOR_MAKEFILR
    fi
  done

  # LOCAL_MODULE_PATH := $(TARGET_OUT)/vendor/operator/app
  for rep in ${APPS_VENDOR[@]}
  do
    tmp_name=${name:0:${#rep}}
    if [ $rep = $tmp_name ]
    then
        echo -e "LOCAL_MODULE_PATH := \$(TARGET_OUT)/vendor/operator/app" >> $VENDOR_MAKEFILR
    fi
  done

  # LOCAL_CERTIFICATE := PRESIGNED
  if [ "${APPS_PLATFORM[0]}" = "" ]
  then
      echo -e "LOCAL_CERTIFICATE := PRESIGNED" >> $VENDOR_MAKEFILR
  fi
  for rep in ${APPS_PLATFORM[@]}
  do
    tmp_name=${name:0:${#rep}}
    if [ $rep = $tmp_name ]
    then
        echo -e "LOCAL_CERTIFICATE := platform" >> $VENDOR_MAKEFILR
    else
        echo -e "LOCAL_CERTIFICATE := PRESIGNED" >> $VENDOR_MAKEFILR
    fi
  done
  # include $(BUILD_PREBUILT)
  echo -e "include \$(BUILD_PREBUILT)" >> $VENDOR_MAKEFILR
  echo -e "\n" >> $VENDOR_MAKEFILR
}

function process_apks()
{
  #1:Clean Makefile
  echo "#" > $VENDOR_MAKEFILR
  echo "LOCAL_PATH := \$(call my-dir)" >> $VENDOR_MAKEFILR
  echo "#" > $VENDOR_SUNMI
  #2: Init APK's Array
  all_apk=$(ls $VENDOR_DIR/*apk)
  if [ ${#all_apk} -gt 0 ]
  then
    # PRODUCT_PACKAGES +=
    echo -en "PRODUCT_PACKAGES += " >> $VENDOR_SUNMI
  fi
  #echo "------------------All APK-----------"
  #echo ${all_apk[*]}
  #echo "-----------------------------------"
  for apk in ${all_apk[@]}
  do
    process_apk_lib $apk
  done 
  echo -en "\n" >> $VENDOR_SUNMI
  echo "include \$(call first-makefiles-under,\$(LOCAL_PATH))" >> $VENDOR_MAKEFILR
}

function process_apks_args()
{
  i=0
  length=${#APPS_ORI_REP[@]}
  for rep in ${APPS_ORI_REP[@]}
  do
    i=$[($i+1)%2]
    if [ $i -gt 0 ]
    then
      APPS_ORI+=("${rep[@]}")
    else
      APPS_REP+=("${rep[@]}")
    fi
  done
  #echo APPS_ORI ${APPS_ORI[*]}
  #echo APPS_REP ${APPS_REP[*]}
}

echo "------------------Begin------------------"
  ROM_Build_Type=$(echo $FTP_DOWNLOAD_PATH | grep "Release")
  if [ -n "$ROM_Build_Type" ]
  then
	  FTP_DOWNLOAD_PREFIX=APK_Release/
  fi
  FTP_DOWNLOAD_PATH_APP=$FTP_DOWNLOAD_PREFIX$FTP_DOWNLOAD_PATH
  echo --------Begin Ftp Download-------
  echo App Path:    ${FTP_DOWNLOAD_PATH_APP}
  download_apks_from_ftp
  echo --------End Ftp Download-------
  echo --------Begin App Process-------
  process_apks_args
  process_apks
  echo --------End App Process-------
echo "-------------------End-------------------"

. vendor/sunmi/build/compile/common/checkApp.sh

if [ x$status != x0 ]
then
    echo check failed, build exit.
    exit
fi
