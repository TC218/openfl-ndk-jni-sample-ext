@echo off
rmdir obj\android-v7 /s /q
haxelib run hxcpp Build.xml -Dandroid -DHXCPP_ARMV7
exit /b %ERRORLEVEL%