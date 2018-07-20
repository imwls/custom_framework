#!/bin/bash
#规则：脚本传入的第一个参数为项目名称，直接输入项目名为默认为 eng 版本
#编译user or userdebug版本只需要在项目名称后加一个"-user or -userdebug"就可以，脚本中不需要另外处理
#author: heanping@sunmi.com

TOPDIR=$PWD

function help_msg() {
    echo -e "Build Script Usage:"
    echo -e "\t -j   -- for jobs e.g. -j 8"
    echo -e "\t -d   -- for delete ccache"
    echo -e "\t -r   -- for tag repo manifest.xml"
    echo -e "\t -f   -- APK or APK_Release FTP path. like: -f L2/UAT"
    echo -e "\t -p   -- project name"
    echo -e "\t -a   -- for SUPPORT_64, default: SUPPORT_64=false"
    echo -e "\t -c   -- build args"
}

function set_ccache()
{
  echo "set up ccache"
  export CCACHE_DIR=../.ccache
  export USE_CCACHE=1
  prebuilts/misc/linux-x86/ccache/ccache -M 20G
}

function disable_ccache()
{
  export USE_CCACHE=0
  prebuilts/misc/linux-x86/ccache/ccache -C
  rm -rf ../.ccache
}

function download_app()
{
    echo [====== Download prebuild App From $FTP_APP_PATH ======]
    ${TOPDIR}/vendor/sunmi/build/compile/common/Init-Vendor.sh $FTP_APP_PATH $SUPPORT_64
}

function env()
{
    echo "[======Env======]"
    # 0: Clean
    #rm -rf out/
    #repo forall -c "git checkout -f"
    #repo forall -c "git clean -df"
    # 1: arm64 or arm
    if [ x$SUPPORT_64 = xTrue ]; then
      SUPPORT_64=true
    else
      SUPPORT_64=false
    fi
    # 2: cache
    if [ x$DISABLE_CCACH = xTrue ];then
      disable_ccache
    else
      set_ccache
    fi
    # 3: manifest
    if [ x$MANIFEST = xTrue ]; then
      repo manifest -r -o ${PROJECT}_$(date +%Y%m%d%H%S).xml
    fi
}

function args()
{
  while [ x$1 != x ]; do
    case "$1" in
    "help") help_msg; exit 0 ;;
      "-j") JOBS=$2; shift 2 ;;
      "-d") DISABLE_CCACHE=True; shift ;;
      "-r") MANIFEST=True; shift ;;
      "-f") FTP_APP_PATH=$2; shift 2 ;;
      "-p") PROJ=$2; shift 2 ;;
      "-a") SUPPORT_64=True; shift ;;
      "-c") CMD=$2; shift 2 ;;
    esac
  done
}

# 0: print msg
if [ "$1" = "" ]; then
   help_msg
   exit 1
fi

# 1: init args
args $@

# 2: Init env
env

# 3: Download Ftp
download_app

# 4: Rm
rm -rf out symbol_files target_files

# 5: build img
${TOPDIR}/tools/sunmitools/shell/$PROJ.sh $CMD $SUPPORT_64
