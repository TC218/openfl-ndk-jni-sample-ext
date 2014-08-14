#ifndef APTIEKAEXTENSION_H
#define APTIEKAEXTENSION_H


#ifdef ANDROID
  #include <android/log.h>  
	#include <hx/CFFI.h>
	#include <jni.h>
	#define LOG_TAG "AptiekaExtensionCpp"
	#define ALOG(...) __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#endif


namespace aptiekaextension 
{
	extern JavaVM *java_vm;
  extern JNIEnv *jni_ndk_env;
  
	void JNI_OnLoad(JavaVM *vm);  
	void init();
  
	int SampleMethod(int inputValue);
	
}


#endif