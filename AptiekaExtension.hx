package;

#if cpp
import cpp.Lib;
#elseif neko
import neko.Lib;
#end

#if (android && openfl)
import openfl.utils.JNI;
#end
 

class AptiekaExtension 
{
	
	public static function init()
  {
    aptiekaextension_init();
  }
  
	public static function sampleMethod (inputValue:Int):Int {
		
		#if (android && openfl)
		
		var resultJNI = aptiekaextension_sample_method_jni(inputValue);
		var resultNative = aptiekaextension_sample_method(inputValue);
		
		if (resultJNI != resultNative) {
			
			throw "Fuzzy math!";
			
		}
		
		return resultNative;
		
		#else
		
		return aptiekaextension_sample_method(inputValue);
		
		#end
		
	}
	
  public static function insert(inputValue:Int):Int 
  {
    return aptiekaextension_sample_method(inputValue);
  }
	
	private static var aptiekaextension_sample_method = Lib.load("aptiekaextension", "aptiekaextension_sample_method", 1);

	private static var aptiekaextension_init = Lib.load("aptiekaextension", "aptiekaextension_init", 0);
	
	#if (android && openfl)
	private static var aptiekaextension_sample_method_jni = JNI.createStaticMethod ("org.haxe.extension.AptiekaExtension", "sampleMethod", "(I)I");
    
	#end
	
	
}