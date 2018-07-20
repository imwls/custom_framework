################################################################################################
#                                                                                              #
#                                       商米配置信息                                             #
#                                                                                              #
################################################################################################
############################
#                          #
#       jenkins.config     #
#                          #
############################
1: 默认机型配置信息
	SUNMI_DEVICE_MODEL	- 机器默认Model值
	SUNMI_DEVICE_BOARD	- 商米机器大机型
	SUNMI_DEVICE_HARDWARE	- 仅供硬件送测使用



2: 商米开机动画
	SUNMI_SCREEN_SIZE	- 机器默认分辨率
          （1） 供开机动画使用

3: GMS
	SUNMI_GMS		- GMS 在vendor下的路径[partner_gms, google]


############################
#                          #
#          device.mk       #
#                          #
############################
1: 商米签名
	SUNMI_RELEASE_KEY	- 商米默认的一套签名（包含系统签名等）
          （1） android5， android6 不使用商米签名
          （2） android7 和之后的版本都使用商米签名

2: 平台配置信息
	SUNMI_CHIP_INFO		- 芯片信息， 包含（mtk, qcom, rockchip）
          （1） local.prop 需要区分芯片信息

3: 参考硬件配置信息
	SUNMI_PRINTER		- 机器是否包含打印机
          （1） 供虚拟蓝牙打印机使用

4: 产品标示
	PRODUCT_CHARACTERISTICS	-  [tablet, tv, default]

5: 额外预置文件
	AntFin			- 管控SDK
          （1） 支持机型包含（T2）
	Byd			- 4G 模块固件
          （1） 支持机型包含（T1， D1， T1mini）


############################
#                          #
#          one.mk          #
#                          #
############################
1: 商米二合一版本
	SUNMI_ONE_QCOM7		- qcom7 平台二合一开关
          （1） 生成 persist_qcom7.img
          （2） Copy device.prop -> /persist/sunmi/device.prop


############################
#                          #
#      prebuild_app.mk     #
#                          #
############################
1: Rom 预置的一些APP



################################################################################################
#                                                                                              #
#                                       商米仓库介绍                                             #
#                                                                                              #
################################################################################################
1: sunmi/framework 	-- vendor/sunmi 【一个主分支】
	- Sunmi OS 主仓库， 包含基础功能

2: sunmi/build 		-- vendor/sunmi/build/compile/custom 【按机型区分】
	- 各机型的编译脚本

3: sunmi/device 	-- vendor/sunmi/device/custom 【按机型区分】
	- 各机型的配置信息

4: sunmi/plugins 	-- vendor/sunmi/framework/core/plugins 【按平台版本区分】
	- 屏蔽各平台SDK差异

5: sunmi/sepolicy 	-- vendor/sunmi/sepolicy/custom 【按平台版本区分】
	- 各平台sepolicy策略

6: sunmi/prebuilts 	-- vendor/sunmi/prebuilts/custom 【一个主分支】
	- 客户预置的一些资源文件







