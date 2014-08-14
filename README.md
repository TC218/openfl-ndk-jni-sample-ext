openfl-ndk-jni-sample-ext
=========================

OpenFL sample extension for making calls Haxe -> NDK C++ -> Android Java

This sample born when I searched solution for https://github.com/openfl/openfl-native/issues/145

My answers re-post:
===================

I have faced the same problem and fighted with it for about one day. I didn't use shoebox solution because its involved concepts (event sending between threads, which is which, etc) felt too disconnected for me ( I have no large experience with C++ and Java plus threads).

I have followed suggested direction in this issue to rewrite calls from (Haxe -> Java) to (Haxe -> NDK C++ -> Java). Problem is that you can't simply make JNI calls from NDK thread to JVM. Firstly, you need to attach JVM to your NDK thread using call java_vm->AttachCurrentThread(). 

To accomplish this, we create JNI_OnLoad entry point in OpenFL extensions' ExternalInterface.cpp to save JavaVM *vm. 

But here we can't call AttachCurrentThread() because current call is already from JVM thread, but we need NDK one..

So, I created init() method for extension (Haxe + CPP part), and call it from Haxe. In C++ part I can call:
int status = java_vm->AttachCurrentThread(&jni_ndk_env, NULL);
For each thread, there is a separate JNIEnv, at this point we get env for current thread.

And finally here is full method which correctly handles deallocation of Java objects so that it can be called many times. Method name is insert(). init() is called after AttachCurrentThread().
https://github.com/raivof/openfl-ndk-jni-sample-ext/blob/master/project/common/DBHelper.cpp
- there you can see whole OpenFL extension, and how things glues together. "AptiekaExtension" is like wrapper for "DBHelper" module, just to be clear, why additional files.

There is two main JNI-related concerns which should be taken care of:
* to use some object between JNI calls, it should be created as JVM global by calling NewGlobalRef(), in my sample I cache references to Java classes I'm interested in.
* for any JVM objects created, there should be according DeleteLocalRef() which actually solves main problem mentioned in this issue. I guess, this should be implemented in Lime library, so that using directly Haxe -> Java calls bridge doesn't crash application when method is called many times. 

I looked up source in OpenFL / Lime to see how much work should be done to implement DeleteLocalRef() calls. Google leads "lime_jni_call_static" to http://213.41.212.217/~archambeaua/THEUS/haxe/lib/lime/0,9,4/project/src/platform/android/JNI.cpp
however, this JNI.cpp is not available in Lime's github repo. Where it is ? 
