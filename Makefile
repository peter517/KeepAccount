#var

ka_path=./
putils_path=../PUtils
putils_lib_path=${putils_path}/target
putils_lib_name="PUtils-1.0.jar"

putils_install:
	echo "putils_install"
	cd ${putils_path} && mvn install

ka_package:putils_install
	echo "ka_package"
	cp ${putils_lib_path}/${putils_lib_name} ${ka_path}/libs
	cd ${ka_path} && ant debug
